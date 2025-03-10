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

package com.liferay.portal.workflow.kaleo.service.impl;

import com.liferay.exportimport.kernel.staging.Staging;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.TimeUnit;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.scheduler.TriggerFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.workflow.kaleo.definition.DelayDuration;
import com.liferay.portal.workflow.kaleo.definition.DurationScale;
import com.liferay.portal.workflow.kaleo.model.KaleoInstanceToken;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskInstanceToken;
import com.liferay.portal.workflow.kaleo.model.KaleoTimer;
import com.liferay.portal.workflow.kaleo.model.KaleoTimerInstanceToken;
import com.liferay.portal.workflow.kaleo.runtime.calendar.DueDateCalculator;
import com.liferay.portal.workflow.kaleo.runtime.constants.KaleoRuntimeDestinationNames;
import com.liferay.portal.workflow.kaleo.runtime.util.SchedulerUtil;
import com.liferay.portal.workflow.kaleo.runtime.util.WorkflowContextUtil;
import com.liferay.portal.workflow.kaleo.service.base.KaleoTimerInstanceTokenLocalServiceBaseImpl;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoInstanceTokenPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTimerPersistence;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	property = "model.class.name=com.liferay.portal.workflow.kaleo.model.KaleoTimerInstanceToken",
	service = AopService.class
)
public class KaleoTimerInstanceTokenLocalServiceImpl
	extends KaleoTimerInstanceTokenLocalServiceBaseImpl {

	@Override
	public KaleoTimerInstanceToken addKaleoTimerInstanceToken(
			long kaleoInstanceTokenId, long kaleoTaskInstanceTokenId,
			long kaleoTimerId, String kaleoTimerName,
			Map<String, Serializable> workflowContext,
			ServiceContext serviceContext)
		throws PortalException {

		User user = _userLocalService.getUser(
			serviceContext.getGuestOrUserId());
		KaleoInstanceToken kaleoInstanceToken =
			_kaleoInstanceTokenPersistence.findByPrimaryKey(
				kaleoInstanceTokenId);
		KaleoTimer kaleoTimer = _kaleoTimerPersistence.findByPrimaryKey(
			kaleoTimerId);
		Date date = new Date();

		long kaleoTimerInstanceTokenId = counterLocalService.increment();

		KaleoTimerInstanceToken kaleoTimerInstanceToken =
			kaleoTimerInstanceTokenPersistence.create(
				kaleoTimerInstanceTokenId);

		kaleoTimerInstanceToken.setGroupId(
			_staging.getLiveGroupId(serviceContext.getScopeGroupId()));
		kaleoTimerInstanceToken.setCompanyId(user.getCompanyId());
		kaleoTimerInstanceToken.setUserId(user.getUserId());
		kaleoTimerInstanceToken.setUserName(user.getFullName());
		kaleoTimerInstanceToken.setCreateDate(date);
		kaleoTimerInstanceToken.setModifiedDate(date);
		kaleoTimerInstanceToken.setKaleoClassName(
			kaleoTimer.getKaleoClassName());
		kaleoTimerInstanceToken.setKaleoClassPK(kaleoTimer.getKaleoClassPK());
		kaleoTimerInstanceToken.setKaleoDefinitionId(
			kaleoInstanceToken.getKaleoDefinitionId());
		kaleoTimerInstanceToken.setKaleoDefinitionVersionId(
			kaleoInstanceToken.getKaleoDefinitionVersionId());
		kaleoTimerInstanceToken.setKaleoInstanceId(
			kaleoInstanceToken.getKaleoInstanceId());
		kaleoTimerInstanceToken.setKaleoInstanceTokenId(kaleoInstanceTokenId);
		kaleoTimerInstanceToken.setKaleoTaskInstanceTokenId(
			kaleoTaskInstanceTokenId);
		kaleoTimerInstanceToken.setKaleoTimerId(kaleoTimerId);
		kaleoTimerInstanceToken.setKaleoTimerName(kaleoTimerName);
		kaleoTimerInstanceToken.setBlocking(kaleoTimer.isBlocking());
		kaleoTimerInstanceToken.setCompleted(false);
		kaleoTimerInstanceToken.setWorkflowContext(
			WorkflowContextUtil.convert(workflowContext));

		kaleoTimerInstanceToken = kaleoTimerInstanceTokenPersistence.update(
			kaleoTimerInstanceToken);

		scheduleTimer(kaleoTimerInstanceToken, kaleoTimer);

		return kaleoTimerInstanceToken;
	}

	@Override
	public List<KaleoTimerInstanceToken> addKaleoTimerInstanceTokens(
			KaleoInstanceToken kaleoInstanceToken,
			KaleoTaskInstanceToken kaleoTaskInstanceToken,
			Collection<KaleoTimer> kaleoTimers,
			Map<String, Serializable> workflowContext,
			ServiceContext serviceContext)
		throws PortalException {

		if (kaleoTimers.isEmpty()) {
			return Collections.emptyList();
		}

		List<KaleoTimerInstanceToken> kaleoTimerInstanceTokens =
			new ArrayList<>(kaleoTimers.size());

		long kaleoTaskInstanceTokenId = 0;

		if (kaleoTaskInstanceToken != null) {
			kaleoTaskInstanceTokenId =
				kaleoTaskInstanceToken.getKaleoTaskInstanceTokenId();
		}

		for (KaleoTimer kaleoTimer : kaleoTimers) {
			KaleoTimerInstanceToken kaleoTimerInstanceToken =
				addKaleoTimerInstanceToken(
					kaleoInstanceToken.getKaleoInstanceTokenId(),
					kaleoTaskInstanceTokenId, kaleoTimer.getKaleoTimerId(),
					kaleoTimer.getName(), workflowContext, serviceContext);

			kaleoTimerInstanceTokens.add(kaleoTimerInstanceToken);
		}

		return kaleoTimerInstanceTokens;
	}

	@Override
	public KaleoTimerInstanceToken completeKaleoTimerInstanceToken(
			long kaleoTimerInstanceTokenId, ServiceContext serviceContext)
		throws PortalException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken =
			kaleoTimerInstanceTokenPersistence.findByPrimaryKey(
				kaleoTimerInstanceTokenId);

		kaleoTimerInstanceToken.setCompletionUserId(serviceContext.getUserId());
		kaleoTimerInstanceToken.setCompleted(true);
		kaleoTimerInstanceToken.setCompletionDate(new Date());

		kaleoTimerInstanceToken = kaleoTimerInstanceTokenPersistence.update(
			kaleoTimerInstanceToken);

		deleteScheduledTimer(kaleoTimerInstanceToken);

		return kaleoTimerInstanceToken;
	}

	@Override
	public void completeKaleoTimerInstanceTokens(
			List<KaleoTimerInstanceToken> kaleoTimerInstanceTokens,
			ServiceContext serviceContext)
		throws PortalException {

		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				kaleoTimerInstanceTokens) {

			completeKaleoTimerInstanceToken(
				kaleoTimerInstanceToken.getKaleoTimerInstanceTokenId(),
				serviceContext);
		}
	}

	@Override
	public void completeKaleoTimerInstanceTokens(
			long kaleoInstanceTokenId, ServiceContext serviceContext)
		throws PortalException {

		List<KaleoTimerInstanceToken> kaleoTimerInstanceTokens =
			kaleoTimerInstanceTokenPersistence.findByKITI_C(
				kaleoInstanceTokenId, false);

		completeKaleoTimerInstanceTokens(
			kaleoTimerInstanceTokens, serviceContext);
	}

	@Override
	public void deleteKaleoTimerInstanceToken(
			long kaleoInstanceTokenId, long kaleoTimerId)
		throws PortalException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken =
			getKaleoTimerInstanceToken(kaleoInstanceTokenId, kaleoTimerId);

		deleteScheduledTimer(kaleoTimerInstanceToken);

		kaleoTimerInstanceTokenPersistence.remove(kaleoTimerInstanceToken);
	}

	@Override
	public void deleteKaleoTimerInstanceTokens(long kaleoInstanceId) {
		List<KaleoTimerInstanceToken> kaleoTimerInstanceTokens =
			kaleoTimerInstanceTokenPersistence.findByKaleoInstanceId(
				kaleoInstanceId);

		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				kaleoTimerInstanceTokens) {

			if (kaleoTimerInstanceToken.isCompleted()) {
				continue;
			}

			try {
				deleteScheduledTimer(kaleoTimerInstanceToken);
			}
			catch (PortalException portalException) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to unschedule " + kaleoTimerInstanceToken,
						portalException);
				}
			}

			kaleoTimerInstanceTokenPersistence.remove(kaleoTimerInstanceToken);
		}
	}

	@Override
	public KaleoTimerInstanceToken getKaleoTimerInstanceToken(
			long kaleoInstanceTokenId, long kaleoTimerId)
		throws PortalException {

		return kaleoTimerInstanceTokenPersistence.findByKITI_KTI(
			kaleoInstanceTokenId, kaleoTimerId);
	}

	@Override
	public List<KaleoTimerInstanceToken> getKaleoTimerInstanceTokens(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		ServiceContext serviceContext) {

		return kaleoTimerInstanceTokenPersistence.findByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed);
	}

	@Override
	public int getKaleoTimerInstanceTokensCount(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		ServiceContext serviceContext) {

		return kaleoTimerInstanceTokenPersistence.countByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed);
	}

	protected void deleteScheduledTimer(
			KaleoTimerInstanceToken kaleoTimerInstanceToken)
		throws PortalException {

		String groupName = getSchedulerGroupName(kaleoTimerInstanceToken);

		_schedulerEngineHelper.delete(groupName, StorageType.PERSISTED);
	}

	protected String getSchedulerGroupName(
		KaleoTimerInstanceToken kaleoTimerInstanceToken) {

		return SchedulerUtil.getGroupName(
			kaleoTimerInstanceToken.getKaleoTimerInstanceTokenId());
	}

	protected void scheduleTimer(
			KaleoTimerInstanceToken kaleoTimerInstanceToken,
			KaleoTimer kaleoTimer)
		throws PortalException {

		deleteScheduledTimer(kaleoTimerInstanceToken);

		String groupName = getSchedulerGroupName(kaleoTimerInstanceToken);

		DelayDuration delayDuration = new DelayDuration(
			kaleoTimer.getDuration(),
			DurationScale.parse(kaleoTimer.getScale()));

		Date dueDate = _dueDateCalculator.getDueDate(new Date(), delayDuration);

		int interval = 0;
		TimeUnit timeUnit = TimeUnit.SECOND;

		if (kaleoTimer.isRecurring()) {
			DelayDuration recurrenceDelayDuration = new DelayDuration(
				kaleoTimer.getRecurrenceDuration(),
				DurationScale.parse(kaleoTimer.getRecurrenceScale()));

			interval = (int)recurrenceDelayDuration.getDuration();

			DurationScale durationScale =
				recurrenceDelayDuration.getDurationScale();

			timeUnit = TimeUnit.valueOf(
				StringUtil.toUpperCase(durationScale.getValue()));
		}

		Trigger trigger = TriggerFactoryUtil.createTrigger(
			groupName, groupName, dueDate, interval, timeUnit);

		Message message = new Message();

		message.put("companyId", kaleoTimerInstanceToken.getCompanyId());
		message.put(
			"kaleoTimerInstanceTokenId",
			kaleoTimerInstanceToken.getKaleoTimerInstanceTokenId());

		_schedulerEngineHelper.schedule(
			trigger, StorageType.PERSISTED, null,
			KaleoRuntimeDestinationNames.WORKFLOW_TIMER, message, 0);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		KaleoTimerInstanceTokenLocalServiceImpl.class);

	@Reference
	private DueDateCalculator _dueDateCalculator;

	@Reference
	private KaleoInstanceTokenPersistence _kaleoInstanceTokenPersistence;

	@Reference
	private KaleoTimerPersistence _kaleoTimerPersistence;

	@Reference
	private SchedulerEngineHelper _schedulerEngineHelper;

	@Reference
	private Staging _staging;

	@Reference
	private TriggerFactory _triggerFactory;

	@Reference
	private UserLocalService _userLocalService;

}