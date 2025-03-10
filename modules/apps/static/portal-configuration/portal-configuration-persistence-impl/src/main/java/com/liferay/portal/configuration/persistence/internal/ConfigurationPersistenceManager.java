/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.configuration.persistence.internal;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.persistence.ConfigurationOverridePropertiesUtil;
import com.liferay.portal.configuration.persistence.ReloadablePersistenceManager;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListenerException;
import com.liferay.portal.file.install.constants.FileInstallConstants;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsValues;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import java.net.URI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.sql.DataSource;

import org.apache.felix.cm.NotCachablePersistenceManager;
import org.apache.felix.cm.PersistenceManager;
import org.apache.felix.cm.file.ConfigurationHandler;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * @author Raymond Augé
 * @author Sampsa Sohlman
 */
public class ConfigurationPersistenceManager
	implements NotCachablePersistenceManager, PersistenceManager,
			   ReloadablePersistenceManager {

	public ConfigurationPersistenceManager(
		BundleContext bundleContext, DataSource dataSource) {

		_bundleContext = bundleContext;
		_dataSource = dataSource;
	}

	@Override
	public void delete(String pid) throws IOException {
		String pidKey = null;

		if (!pid.endsWith("factory")) {
			Dictionary<?, ?> dictionary = _getDictionary(pid);

			if (dictionary != null) {
				pidKey = (String)dictionary.get(
					ConfigurationAdmin.SERVICE_FACTORYPID);

				if (pidKey == null) {
					pidKey = (String)dictionary.get(Constants.SERVICE_PID);
				}

				if (pidKey == null) {
					pidKey = pid;
				}
			}
		}

		_visitConfigurationModelListeners(
			"*",
			configurationModelListener ->
				configurationModelListener.onBeforeDelete(pid));
		_visitConfigurationModelListeners(
			pidKey,
			configurationModelListener ->
				configurationModelListener.onBeforeDelete(pid));

		Lock lock = _readWriteLock.writeLock();

		lock.lock();

		try {
			Dictionary<?, ?> dictionary = _dictionaries.remove(pid);

			if (dictionary != null) {
				_deleteFromDatabase(pid);
			}
		}
		finally {
			lock.unlock();
		}

		_visitConfigurationModelListeners(
			"*",
			configurationModelListener ->
				configurationModelListener.onAfterDelete(pid));
		_visitConfigurationModelListeners(
			pidKey,
			configurationModelListener ->
				configurationModelListener.onAfterDelete(pid));
	}

	@Override
	public boolean exists(String pid) {
		Lock lock = _readWriteLock.readLock();

		lock.lock();

		try {
			return _dictionaries.containsKey(pid);
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public Enumeration<?> getDictionaries() {
		Lock lock = _readWriteLock.readLock();

		lock.lock();

		try {
			return Collections.enumeration(_dictionaries.values());
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public Dictionary<?, ?> load(String pid) {
		Lock lock = _readWriteLock.readLock();

		lock.lock();

		try {
			return _dictionaries.get(pid);
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public void reload(String pid) throws IOException {
		Lock lock = _readWriteLock.writeLock();

		lock.lock();

		try {
			Dictionary<Object, Object> dictionary = _overrideDictionary(
				pid, _getDictionary(pid));

			if (dictionary == null) {
				_dictionaries.remove(pid);
			}
			else {
				_dictionaries.put(pid, dictionary);
			}
		}
		finally {
			lock.unlock();
		}
	}

	public void start() {
		try {
			_populateDictionaries();
		}
		catch (IOException | SQLException exception) {
			_createConfigurationTable();

			for (Bundle bundle : _bundleContext.getBundles()) {
				if (Objects.equals(
						bundle.getSymbolicName(),
						"org.apache.felix.configurator")) {

					File stateFile = bundle.getDataFile("state.ser");

					if (stateFile.exists()) {
						stateFile.delete();
					}

					break;
				}
			}
		}
	}

	public void stop() {
		_dictionaries.clear();

		if (_serviceTrackerMap != null) {
			_serviceTrackerMap.close();

			_serviceTrackerMap = null;
		}
	}

	@Override
	public void store(
			String pid, @SuppressWarnings("rawtypes") Dictionary dictionary)
		throws IOException {

		String pidKey = null;

		if (!pid.endsWith("factory") &&
			(dictionary.get("_felix_.cm.newConfiguration") == null)) {

			pidKey = (String)dictionary.get(
				ConfigurationAdmin.SERVICE_FACTORYPID);

			if (pidKey == null) {
				pidKey = pid;
			}

			if (pidKey.endsWith(".scoped")) {
				pidKey = StringUtil.replaceLast(
					pidKey, ".scoped", StringPool.BLANK);
			}
		}

		_visitConfigurationModelListeners(
			"*",
			configurationModelListener ->
				configurationModelListener.onBeforeSave(pid, dictionary));
		_visitConfigurationModelListeners(
			pidKey,
			configurationModelListener ->
				configurationModelListener.onBeforeSave(pid, dictionary));

		Dictionary<Object, Object> newDictionary = _copyDictionary(dictionary);

		String fileName = (String)newDictionary.get(
			FileInstallConstants.FELIX_FILE_INSTALL_FILENAME);

		if (fileName != null) {
			File file = new File(URI.create(fileName));

			newDictionary.put(
				FileInstallConstants.FELIX_FILE_INSTALL_FILENAME,
				file.getName());
		}

		Lock lock = _readWriteLock.writeLock();

		lock.lock();

		try {
			_storeInDatabase(pid, newDictionary);

			if (fileName != null) {
				newDictionary.put(
					FileInstallConstants.FELIX_FILE_INSTALL_FILENAME, fileName);
			}

			_dictionaries.put(pid, _overrideDictionary(pid, newDictionary));
		}
		finally {
			lock.unlock();
		}

		_visitConfigurationModelListeners(
			"*",
			configurationModelListener ->
				configurationModelListener.onAfterSave(pid, dictionary));
		_visitConfigurationModelListeners(
			pidKey,
			configurationModelListener ->
				configurationModelListener.onAfterSave(pid, dictionary));
	}

	protected void store(ResultSet resultSet, Dictionary<?, ?> dictionary)
		throws IOException, SQLException {

		OutputStream outputStream = new UnsyncByteArrayOutputStream();

		ConfigurationHandler.write(outputStream, dictionary);

		resultSet.updateString(2, outputStream.toString());
	}

	@SuppressWarnings("unchecked")
	protected Dictionary<Object, Object> toDictionary(String dictionaryString)
		throws IOException {

		if (dictionaryString == null) {
			return new HashMapDictionary<>();
		}

		Dictionary<Object, Object> dictionary = ConfigurationHandler.read(
			new UnsyncByteArrayInputStream(
				dictionaryString.getBytes(StringPool.UTF8)));

		String fileName = (String)dictionary.get(
			FileInstallConstants.FELIX_FILE_INSTALL_FILENAME);

		if (fileName != null) {
			File file = _getCanonicalConfigFile(fileName);

			URI uri = file.toURI();

			dictionary.put(
				FileInstallConstants.FELIX_FILE_INSTALL_FILENAME,
				uri.toString());
		}

		return dictionary;
	}

	private Dictionary<Object, Object> _copyDictionary(
		Dictionary<?, ?> dictionary) {

		Dictionary<Object, Object> newDictionary = new HashMapDictionary<>();

		Enumeration<?> enumeration = dictionary.keys();

		while (enumeration.hasMoreElements()) {
			Object key = enumeration.nextElement();

			newDictionary.put(key, dictionary.get(key));
		}

		return newDictionary;
	}

	private void _createConfigurationTable() {
		try (Connection connection = _dataSource.getConnection();
			Statement statement = connection.createStatement()) {

			statement.executeUpdate(
				_db.buildSQL(
					"create table Configuration_ (configurationId " +
						"VARCHAR(255) not null primary key, dictionary TEXT)"));
		}
		catch (IOException | SQLException exception) {
			ReflectionUtil.throwException(exception);
		}

		Map<String, Map<String, Object>> overridePropertiesMap =
			ConfigurationOverridePropertiesUtil.getOverridePropertiesMap();

		overridePropertiesMap.forEach(
			(key, value) -> _dictionaries.put(
				key, new HashMapDictionary<>((Map)value)));
	}

	private void _deleteFromDatabase(String pid) throws IOException {
		try (Connection connection = _dataSource.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(
				_db.buildSQL(
					"delete from Configuration_ where configurationId = ?"))) {

			preparedStatement.setString(1, pid);

			preparedStatement.executeUpdate();
		}
		catch (SQLException sqlException) {
			throw new IOException(sqlException);
		}
	}

	private File _getCanonicalConfigFile(String fileName) throws IOException {
		File configFile = new File(
			PropsValues.MODULE_FRAMEWORK_CONFIGS_DIR, fileName);

		return configFile.getCanonicalFile();
	}

	private Dictionary<Object, Object> _getDictionary(String pid)
		throws IOException {

		try (Connection connection = _dataSource.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(
				_db.buildSQL(
					"select dictionary from Configuration_ where " +
						"configurationId = ?"))) {

			preparedStatement.setString(1, pid);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return toDictionary(resultSet.getString(1));
				}
			}

			return null;
		}
		catch (SQLException sqlException) {
			return ReflectionUtil.throwException(sqlException);
		}
	}

	private Dictionary<Object, Object> _overrideDictionary(
		String pid, Dictionary<Object, Object> dictionary) {

		Map<String, Object> overrideProperties =
			ConfigurationOverridePropertiesUtil.getOverrideProperties(pid);

		if (overrideProperties != null) {
			if (dictionary == null) {
				dictionary = new HashMapDictionary<>();
			}

			for (Map.Entry<String, Object> entry :
					overrideProperties.entrySet()) {

				dictionary.put(entry.getKey(), entry.getValue());
			}
		}

		return dictionary;
	}

	private void _populateDictionaries() throws IOException, SQLException {
		Map<String, Map<String, Object>> overridePropertiesMap = new HashMap<>(
			ConfigurationOverridePropertiesUtil.getOverridePropertiesMap());

		try (Connection connection = _dataSource.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(
				_db.buildSQL(
					"select configurationId, dictionary from Configuration_"),
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				String pid = resultSet.getString(1);

				Dictionary<Object, Object> dictionary = _verifyDictionary(
					pid, resultSet.getString(2));

				if (dictionary != null) {
					overridePropertiesMap.remove(pid);

					_dictionaries.put(
						pid, _overrideDictionary(pid, dictionary));
				}
			}
		}

		overridePropertiesMap.forEach(
			(key, value) -> _dictionaries.put(
				key, new HashMapDictionary<>((Map)value)));
	}

	private void _storeInDatabase(String pid, Dictionary<?, ?> dictionary)
		throws IOException {

		UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
			new UnsyncByteArrayOutputStream();

		ConfigurationHandler.write(unsyncByteArrayOutputStream, dictionary);

		try (Connection connection = _dataSource.getConnection()) {
			connection.setAutoCommit(false);

			try (PreparedStatement preparedStatement1 =
					connection.prepareStatement(
						_db.buildSQL(
							"update Configuration_ set dictionary = ? where " +
								"configurationId = ?"))) {

				preparedStatement1.setString(
					1, unsyncByteArrayOutputStream.toString());
				preparedStatement1.setString(2, pid);

				if (preparedStatement1.executeUpdate() == 0) {
					try (PreparedStatement preparedStatement2 =
							connection.prepareStatement(
								_db.buildSQL(
									"insert into Configuration_ (" +
										"configurationId, dictionary) values " +
											"(?, ?)"))) {

						preparedStatement2.setString(1, pid);
						preparedStatement2.setString(
							2, unsyncByteArrayOutputStream.toString());

						preparedStatement2.executeUpdate();
					}
				}
			}

			connection.commit();
		}
		catch (SQLException sqlException) {
			ReflectionUtil.throwException(sqlException);
		}
	}

	private Dictionary<Object, Object> _verifyDictionary(
			String pid, String dictionaryString)
		throws IOException {

		if (dictionaryString == null) {
			return new HashMapDictionary<>();
		}

		Dictionary<Object, Object> dictionary = ConfigurationHandler.read(
			new UnsyncByteArrayInputStream(
				dictionaryString.getBytes(StringPool.UTF8)));

		String felixFileInstallFileName = (String)dictionary.get(
			FileInstallConstants.FELIX_FILE_INSTALL_FILENAME);

		if (felixFileInstallFileName == null) {
			return dictionary;
		}

		boolean needSave = false;

		if (dictionary.get(_SERVIE_BUNDLE_LOCATION) == null) {
			dictionary.put(_SERVIE_BUNDLE_LOCATION, "?");

			needSave = true;
		}

		File configFile = null;

		if (felixFileInstallFileName.startsWith("file:")) {
			try {
				configFile = new File(URI.create(felixFileInstallFileName));
			}
			catch (Exception exception) {
				configFile = new File(felixFileInstallFileName);
			}

			dictionary.put(
				FileInstallConstants.FELIX_FILE_INSTALL_FILENAME,
				configFile.getName());

			_storeInDatabase(pid, dictionary);

			dictionary.put(
				FileInstallConstants.FELIX_FILE_INSTALL_FILENAME,
				felixFileInstallFileName);

			needSave = false;
		}
		else {
			configFile = _getCanonicalConfigFile(felixFileInstallFileName);

			URI uri = configFile.toURI();

			dictionary.put(
				FileInstallConstants.FELIX_FILE_INSTALL_FILENAME,
				uri.toString());
		}

		if (needSave) {
			_storeInDatabase(pid, dictionary);
		}

		String ignore = (String)dictionary.get("configuration.cleaner.ignore");

		if (!Boolean.valueOf(ignore) && !configFile.exists()) {
			_deleteFromDatabase(pid);

			return null;
		}

		return dictionary;
	}

	private void _visitConfigurationModelListeners(
			String key,
			UnsafeConsumer
				<ConfigurationModelListener,
				 ConfigurationModelListenerException>
					configurationModelListenerUnsafeConsumer)
		throws ConfigurationModelListenerException {

		if (Validator.isNull(key)) {
			return;
		}

		if (_serviceTrackerMap == null) {
			_serviceTrackerMap = ServiceTrackerMapFactory.openMultiValueMap(
				_bundleContext, ConfigurationModelListener.class,
				"model.class.name");
		}

		if (_serviceTrackerMap.containsKey(key)) {
			UnsafeConsumer.accept(
				_serviceTrackerMap.getService(key),
				configurationModelListenerUnsafeConsumer,
				ConfigurationModelListenerException.class);
		}
	}

	private static final String _SERVIE_BUNDLE_LOCATION =
		"service.bundleLocation";

	private final BundleContext _bundleContext;
	private final DataSource _dataSource;
	private final DB _db = DBManagerUtil.getDB();
	private final ConcurrentMap<String, Dictionary<?, ?>> _dictionaries =
		new ConcurrentHashMap<>();
	private final ReadWriteLock _readWriteLock = new ReentrantReadWriteLock(
		true);
	private ServiceTrackerMap<String, List<ConfigurationModelListener>>
		_serviceTrackerMap;

}