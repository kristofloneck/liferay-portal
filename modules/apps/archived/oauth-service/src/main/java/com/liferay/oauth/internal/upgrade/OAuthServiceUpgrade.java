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

package com.liferay.oauth.internal.upgrade;

import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.upgrade.release.BaseUpgradeServiceModuleRelease;

import org.osgi.service.component.annotations.Component;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class OAuthServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"0.0.1", "1.0.0",
			new BaseUpgradeServiceModuleRelease() {

				@Override
				protected String getNamespace() {
					return "OAuth";
				}

				@Override
				protected String getNewBundleSymbolicName() {
					return "com.liferay.oauth.service";
				}

				@Override
				protected String getOldBundleSymbolicName() {
					return "oauth-portlet";
				}

			});
	}

}