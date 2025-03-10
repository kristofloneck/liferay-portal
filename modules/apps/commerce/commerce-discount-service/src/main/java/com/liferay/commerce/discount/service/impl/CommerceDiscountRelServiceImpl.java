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

package com.liferay.commerce.discount.service.impl;

import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.discount.model.CommerceDiscountRel;
import com.liferay.commerce.discount.service.base.CommerceDiscountRelServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false,
	property = {
		"json.web.service.context.name=commerce",
		"json.web.service.context.path=CommerceDiscountRel"
	},
	service = AopService.class
)
public class CommerceDiscountRelServiceImpl
	extends CommerceDiscountRelServiceBaseImpl {

	@Override
	public CommerceDiscountRel addCommerceDiscountRel(
			long commerceDiscountId, String className, long classPK,
			ServiceContext serviceContext)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.addCommerceDiscountRel(
			commerceDiscountId, className, classPK, serviceContext);
	}

	@Override
	public void deleteCommerceDiscountRel(long commerceDiscountRelId)
		throws PortalException {

		CommerceDiscountRel commerceDiscountRel =
			commerceDiscountRelLocalService.getCommerceDiscountRel(
				commerceDiscountRelId);

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountRel.getCommerceDiscountId(),
			ActionKeys.UPDATE);

		commerceDiscountRelLocalService.deleteCommerceDiscountRel(
			commerceDiscountRel);
	}

	@Override
	public CommerceDiscountRel fetchCommerceDiscountRel(
			String className, long classPK)
		throws PortalException {

		CommerceDiscountRel commerceDiscountRel =
			commerceDiscountRelLocalService.fetchCommerceDiscountRel(
				className, classPK);

		if (commerceDiscountRel != null) {
			_commerceDiscountResourcePermission.check(
				getPermissionChecker(),
				commerceDiscountRel.getCommerceDiscountId(), ActionKeys.UPDATE);
		}

		return commerceDiscountRel;
	}

	@Override
	public List<CommerceDiscountRel> getCategoriesByCommerceDiscountId(
			long commerceDiscountId, String name, int start, int end)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.
			getCategoriesByCommerceDiscountId(
				commerceDiscountId, name, start, end);
	}

	@Override
	public int getCategoriesByCommerceDiscountIdCount(
			long commerceDiscountId, String name)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.
			getCategoriesByCommerceDiscountIdCount(commerceDiscountId, name);
	}

	@Override
	public long[] getClassPKs(long commerceDiscountId, String className)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.getClassPKs(
			commerceDiscountId, className);
	}

	@Override
	public CommerceDiscountRel getCommerceDiscountRel(
			long commerceDiscountRelId)
		throws PortalException {

		CommerceDiscountRel commerceDiscountRel =
			commerceDiscountRelLocalService.getCommerceDiscountRel(
				commerceDiscountRelId);

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountRel.getCommerceDiscountId(),
			ActionKeys.UPDATE);

		return commerceDiscountRel;
	}

	@Override
	public List<CommerceDiscountRel> getCommerceDiscountRels(
			long commerceDiscountId, String className)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.getCommerceDiscountRels(
			commerceDiscountId, className);
	}

	@Override
	public List<CommerceDiscountRel> getCommerceDiscountRels(
			long commerceDiscountId, String className, int start, int end,
			OrderByComparator<CommerceDiscountRel> orderByComparator)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.getCommerceDiscountRels(
			commerceDiscountId, className, start, end, orderByComparator);
	}

	@Override
	public int getCommerceDiscountRelsCount(
			long commerceDiscountId, String className)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.getCommerceDiscountRelsCount(
			commerceDiscountId, className);
	}

	@Override
	public List<CommerceDiscountRel>
			getCommercePricingClassesByCommerceDiscountId(
				long commerceDiscountId, String title, int start, int end)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.
			getCommercePricingClassesByCommerceDiscountId(
				commerceDiscountId, title, start, end);
	}

	@Override
	public int getCommercePricingClassesByCommerceDiscountIdCount(
			long commerceDiscountId, String title)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.
			getCommercePricingClassesByCommerceDiscountIdCount(
				commerceDiscountId, title);
	}

	@Override
	public List<CommerceDiscountRel> getCPDefinitionsByCommerceDiscountId(
			long commerceDiscountId, String name, String languageId, int start,
			int end)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.
			getCPDefinitionsByCommerceDiscountId(
				commerceDiscountId, name, languageId, start, end);
	}

	@Override
	public int getCPDefinitionsByCommerceDiscountIdCount(
			long commerceDiscountId, String name, String languageId)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.
			getCPDefinitionsByCommerceDiscountIdCount(
				commerceDiscountId, name, languageId);
	}

	@Override
	public List<CommerceDiscountRel> getCPInstancesByCommerceDiscountId(
			long commerceDiscountId, String sku, int start, int end)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.
			getCPInstancesByCommerceDiscountId(
				commerceDiscountId, sku, start, end);
	}

	@Override
	public int getCPInstancesByCommerceDiscountIdCount(
			long commerceDiscountId, String sku)
		throws PortalException {

		_commerceDiscountResourcePermission.check(
			getPermissionChecker(), commerceDiscountId, ActionKeys.UPDATE);

		return commerceDiscountRelLocalService.
			getCPInstancesByCommerceDiscountIdCount(commerceDiscountId, sku);
	}

	@Reference(
		target = "(model.class.name=com.liferay.commerce.discount.model.CommerceDiscount)"
	)
	private ModelResourcePermission<CommerceDiscount>
		_commerceDiscountResourcePermission;

}