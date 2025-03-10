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

package com.liferay.dispatch.service.base;

import com.liferay.dispatch.model.DispatchTrigger;
import com.liferay.dispatch.service.DispatchTriggerService;
import com.liferay.dispatch.service.DispatchTriggerServiceUtil;
import com.liferay.dispatch.service.persistence.DispatchTriggerPersistence;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.util.PortalUtil;

import java.lang.reflect.Field;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the dispatch trigger remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.dispatch.service.impl.DispatchTriggerServiceImpl}.
 * </p>
 *
 * @author Matija Petanjek
 * @see com.liferay.dispatch.service.impl.DispatchTriggerServiceImpl
 * @generated
 */
public abstract class DispatchTriggerServiceBaseImpl
	extends BaseServiceImpl
	implements AopService, DispatchTriggerService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>DispatchTriggerService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>DispatchTriggerServiceUtil</code>.
	 */
	@Deactivate
	protected void deactivate() {
		_setServiceUtilService(null);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			DispatchTriggerService.class, IdentifiableOSGiService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		dispatchTriggerService = (DispatchTriggerService)aopProxy;

		_setServiceUtilService(dispatchTriggerService);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return DispatchTriggerService.class.getName();
	}

	protected Class<?> getModelClass() {
		return DispatchTrigger.class;
	}

	protected String getModelClassName() {
		return DispatchTrigger.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = dispatchTriggerPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	private void _setServiceUtilService(
		DispatchTriggerService dispatchTriggerService) {

		try {
			Field field = DispatchTriggerServiceUtil.class.getDeclaredField(
				"_service");

			field.setAccessible(true);

			field.set(null, dispatchTriggerService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Reference
	protected com.liferay.dispatch.service.DispatchTriggerLocalService
		dispatchTriggerLocalService;

	protected DispatchTriggerService dispatchTriggerService;

	@Reference
	protected DispatchTriggerPersistence dispatchTriggerPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

}