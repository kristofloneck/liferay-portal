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

package com.liferay.adaptive.media.web.internal.upgrade.registry;

import com.liferay.adaptive.media.web.internal.configuration.AMConfiguration;
import com.liferay.adaptive.media.web.internal.upgrade.v1_0_0.BlogsEntryDataFileEntryIdUpgradeProcess;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.portal.configuration.persistence.upgrade.ConfigurationUpgradeStepFactory;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class AMWebUpgradeStepRegistrator implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.registerInitialization();

		registry.register(
			"1.0.0", "1.0.1",
			_configurationUpgradeStepFactory.createUpgradeStep(
				"com.liferay.adaptive.media.internal.configuration." +
					"AMConfiguration",
				AMConfiguration.class.getName()));

		registry.register(
			"1.0.1", "1.0.2",
			new BlogsEntryDataFileEntryIdUpgradeProcess(
				_blogsEntryLocalService));
	}

	@Reference
	private BlogsEntryLocalService _blogsEntryLocalService;

	@Reference
	private ConfigurationUpgradeStepFactory _configurationUpgradeStepFactory;

}