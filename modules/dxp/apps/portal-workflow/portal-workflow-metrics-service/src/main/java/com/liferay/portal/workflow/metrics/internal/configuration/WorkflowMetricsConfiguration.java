/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.workflow.metrics.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Rafael Praxedes
 */
@ExtendedObjectClassDefinition(category = "workflow")
@Meta.OCD(
	id = "com.liferay.portal.workflow.metrics.internal.configuration.WorkflowMetricsConfiguration",
	localization = "content/Language",
	name = "workflow-metrics-configuration-name"
)
public interface WorkflowMetricsConfiguration {

	@Meta.AD(
		deflt = "1", description = "check-sla-job-interval-description",
		name = "check-sla-job-interval", required = false
	)
	public int checkSLAJobInterval();

	@Meta.AD(
		deflt = "10",
		description = "check-sla-definitions-job-interval-description",
		name = "check-sla-definitions-job-interval", required = false
	)
	public int checkSLADefinitionsJobInterval();

}