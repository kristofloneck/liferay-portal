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

package com.liferay.commerce.account.service.base;

import com.liferay.commerce.account.model.CommerceAccountGroupCommerceAccountRel;
import com.liferay.commerce.account.service.CommerceAccountGroupCommerceAccountRelService;
import com.liferay.commerce.account.service.CommerceAccountGroupCommerceAccountRelServiceUtil;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;

import java.lang.reflect.Field;

import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the commerce account group commerce account rel remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.commerce.account.service.impl.CommerceAccountGroupCommerceAccountRelServiceImpl}.
 * </p>
 *
 * @author Marco Leo
 * @see com.liferay.commerce.account.service.impl.CommerceAccountGroupCommerceAccountRelServiceImpl
 * @generated
 */
public abstract class CommerceAccountGroupCommerceAccountRelServiceBaseImpl
	extends BaseServiceImpl
	implements AopService, CommerceAccountGroupCommerceAccountRelService,
			   IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>CommerceAccountGroupCommerceAccountRelService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>CommerceAccountGroupCommerceAccountRelServiceUtil</code>.
	 */
	@Deactivate
	protected void deactivate() {
		_setServiceUtilService(null);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			CommerceAccountGroupCommerceAccountRelService.class,
			IdentifiableOSGiService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		commerceAccountGroupCommerceAccountRelService =
			(CommerceAccountGroupCommerceAccountRelService)aopProxy;

		_setServiceUtilService(commerceAccountGroupCommerceAccountRelService);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CommerceAccountGroupCommerceAccountRelService.class.getName();
	}

	protected Class<?> getModelClass() {
		return CommerceAccountGroupCommerceAccountRel.class;
	}

	protected String getModelClassName() {
		return CommerceAccountGroupCommerceAccountRel.class.getName();
	}

	private void _setServiceUtilService(
		CommerceAccountGroupCommerceAccountRelService
			commerceAccountGroupCommerceAccountRelService) {

		try {
			Field field =
				CommerceAccountGroupCommerceAccountRelServiceUtil.class.
					getDeclaredField("_service");

			field.setAccessible(true);

			field.set(null, commerceAccountGroupCommerceAccountRelService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Reference
	protected com.liferay.commerce.account.service.
		CommerceAccountGroupCommerceAccountRelLocalService
			commerceAccountGroupCommerceAccountRelLocalService;

	protected CommerceAccountGroupCommerceAccountRelService
		commerceAccountGroupCommerceAccountRelService;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

}