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

package com.liferay.portal.background.task.internal.upgrade.registry;

import com.liferay.portal.background.task.internal.upgrade.v1_0_0.SchemaUpgradeProcess;
import com.liferay.portal.background.task.internal.upgrade.v1_0_0.UpgradeKernelPackage;
import com.liferay.portal.background.task.internal.upgrade.v2_0_0.util.BackgroundTaskTable;
import com.liferay.portal.kernel.upgrade.BaseSQLServerDatetimeUpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeProcessFactory;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Cristina Rodríguez
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class BackgroundTaskServiceUpgradeStepRegistrator
	implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"0.0.1", "0.0.2", new SchemaUpgradeProcess(),
			new UpgradeKernelPackage());

		registry.register(
			"0.0.2", "1.0.0",
			UpgradeProcessFactory.alterColumnType(
				"BackgroundTask", "name", "VARCHAR(255) null"),
			UpgradeProcessFactory.alterColumnName(
				"BackgroundTask", "taskContext", "taskContextMap TEXT null"));

		registry.register(
			"1.0.0", "2.0.0",
			new BaseSQLServerDatetimeUpgradeProcess(
				new Class<?>[] {BackgroundTaskTable.class}));
	}

}