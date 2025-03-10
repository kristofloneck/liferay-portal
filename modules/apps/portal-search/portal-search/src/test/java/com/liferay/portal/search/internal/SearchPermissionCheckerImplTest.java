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

package com.liferay.portal.search.internal;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchPermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.UserBag;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.configuration.SearchPermissionCheckerConfiguration;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author André de Oliveira
 */
public class SearchPermissionCheckerImplTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		Mockito.doReturn(
			_indexer
		).when(
			_indexerRegistry
		).getIndexer(
			Mockito.nullable(String.class)
		);

		_searchPermissionChecker = _createSearchPermissionChecker();
	}

	@Test
	public void testNullInput() {
		Assert.assertNull(
			_searchPermissionChecker.getPermissionBooleanFilter(
				0, null, 0, null, null, null));
	}

	@Test
	public void testPermissionFilterTakesOverNullInputFilter()
		throws Exception {

		long userId = RandomTestUtil.randomLong();

		_whenIndexerIsPermissionAware(true);
		_whenPermissionCheckerGetUser(_user);
		_whenPermissionCheckerGetUserBag(_userBag);
		_whenUserGetUserId(userId);

		Assert.assertNotNull(
			_searchPermissionChecker.getPermissionBooleanFilter(
				0, null, userId, null, null, new SearchContext()));
	}

	private SearchPermissionCheckerImpl _createSearchPermissionChecker() {
		return new SearchPermissionCheckerImpl() {
			{
				indexerRegistry = _indexerRegistry;
				permissionChecker = _permissionChecker;
				resourcePermissionLocalService =
					_resourcePermissionLocalService;
				roleLocalService = _roleLocalService;
				searchPermissionCheckerConfiguration =
					_searchPermissionCheckerConfiguration;
				userLocalService = _userLocalService;
			}
		};
	}

	private boolean _whenIndexerIsPermissionAware(boolean permissionAware) {
		return Mockito.doReturn(
			permissionAware
		).when(
			_indexer
		).isPermissionAware();
	}

	private User _whenPermissionCheckerGetUser(User user) {
		return Mockito.doReturn(
			user
		).when(
			_permissionChecker
		).getUser();
	}

	private void _whenPermissionCheckerGetUserBag(UserBag userBag)
		throws Exception {

		Mockito.doReturn(
			userBag
		).when(
			_permissionChecker
		).getUserBag();
	}

	private long _whenUserGetUserId(long userId) {
		return Mockito.doReturn(
			userId
		).when(
			_user
		).getUserId();
	}

	private final Indexer<?> _indexer = Mockito.mock(Indexer.class);
	private final IndexerRegistry _indexerRegistry = Mockito.mock(
		IndexerRegistry.class);
	private final PermissionChecker _permissionChecker = Mockito.mock(
		PermissionChecker.class);
	private final ResourcePermissionLocalService
		_resourcePermissionLocalService = Mockito.mock(
			ResourcePermissionLocalService.class);
	private final RoleLocalService _roleLocalService = Mockito.mock(
		RoleLocalService.class);
	private SearchPermissionChecker _searchPermissionChecker;
	private final SearchPermissionCheckerConfiguration
		_searchPermissionCheckerConfiguration = Mockito.mock(
			SearchPermissionCheckerConfiguration.class);
	private final User _user = Mockito.mock(User.class);
	private final UserBag _userBag = Mockito.mock(UserBag.class);
	private final UserLocalService _userLocalService = Mockito.mock(
		UserLocalService.class);

}