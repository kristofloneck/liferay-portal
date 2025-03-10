/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import {array, date, number, object, string} from 'yup';

import {TypeActivityExternalReferenceCode} from '../../../../../common/enums/typeActivityExternalReferenceCode';
import TypeActivity from '../../../../../common/interfaces/typeActivity';
import isObjectEmpty from '../../../utils/isObjectEmpty';

const activitiesSchema = object({
	activities: array()
		.of(
			object({
				activityPromotion: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.EVENT,
					then: (schema) =>
						schema
							.max(350, 'You have exceeded the character limit')
							.required('Required'),
				}),
				assetsLiferayRequired: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.DIGITAL_MARKETING,
					then: (schema) => schema.required('Required'),
				}),
				budgets: array()
					.of(
						object({
							cost: number()
								.moreThan(0, 'Required')
								.required('Required'),
							expense: object({
								key: string(),
								name: string(),
							}).test(
								'is-empty',
								'Required',
								(value) => !isObjectEmpty(value)
							),
						})
					)
					.min(1, 'Required'),
				description: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.EVENT,
					then: (schema) =>
						schema
							.max(350, 'You have exceeded the character limit')
							.required('Required'),
				}),
				detailsLeadFollowUp: string()
					.max(350, 'You have exceeded the character limit')
					.required('Required'),
				endDate: date()
					.test(
						'end-date-six-month',
						'End date must be less than six month after start date',
						(endDate, testContext) => {
							if (endDate) {
								const startDate = testContext.parent.startDate;

								return (
									endDate.getMonth() -
										startDate.getMonth() +
										12 *
											(endDate.getFullYear() -
												startDate.getFullYear()) <=
									6
								);
							}

							return false;
						}
					)
					.test(
						'end-date-less-end-date',
						'The end date cannot be before start date',
						(endDate, testContext) => {
							if (endDate && testContext.parent.startDate) {
								return testContext.parent.startDate < endDate;
							}

							return false;
						}
					)
					.required('Required'),
				gatedLandingPage: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.CONTENT_MARKETING,
					then: (schema) => schema.required('Required'),
				}),
				goalOfContent: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.CONTENT_MARKETING,
					then: (schema) =>
						schema
							.max(350, 'You have exceeded the character limit')
							.required('Required'),
				}),
				hiringOutsideWriterOrAgency: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.CONTENT_MARKETING,
					then: (schema) => schema.required('Required'),
				}),
				howLiferayBrandUsed: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.DIGITAL_MARKETING,
					then: (schema) => schema.required('Required'),
				}),
				leadFollowUpStrategies: array().min(1, 'Required'),
				leadGenerated: string().required('Required'),
				liferayBranding: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.EVENT,
					then: (schema) =>
						schema
							.max(350, 'You have exceeded the character limit')
							.required('Required'),
				}),
				liferayParticipationRequirements: string().when(
					'typeActivity',
					{
						is: (typeActivity: TypeActivity) =>
							typeActivity.externalReferenceCode ===
							TypeActivityExternalReferenceCode.EVENT,
						then: (schema) =>
							schema
								.max(
									350,
									'You have exceeded the character limit'
								)
								.required('Required'),
					}
				),
				location: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.EVENT,
					then: (schema) =>
						schema
							.max(350, 'You have exceeded the character limit')
							.required('Required'),
				}),
				mdfRequestAmount: number()
					.moreThan(0, 'Required')
					.required('Required')
					.test(
						'is-greater-than-the-percentage',
						'It is not possible to give a bigger discount than the Claim Percent',
						(mdfRequestAmount, testContext) => {
							if (mdfRequestAmount) {
								return !(
									+mdfRequestAmount >
									+testContext.parent.totalCostOfExpense * 0.5
								);
							}

							return false;
						}
					),
				name: string()
					.max(350, 'You have exceeded the character limit')
					.required('Required'),
				overallMessageContentCTA: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.DIGITAL_MARKETING,
					then: (schema) =>
						schema
							.max(350, 'You have exceeded the character limit')
							.required('Required'),
				}),
				primaryThemeOrMessage: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.CONTENT_MARKETING,
					then: (schema) =>
						schema
							.max(350, 'You have exceeded the character limit')
							.required('Required'),
				}),
				sourceAndSizeOfInviteeList: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.EVENT,
					then: (schema) =>
						schema
							.max(350, 'You have exceeded the character limit')
							.required('Required'),
				}),
				specificSites: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.DIGITAL_MARKETING,
					then: (schema) =>
						schema
							.max(350, 'You have exceeded the character limit')
							.required('Required'),
				}),
				startDate: date()
					.test(
						'is-today',
						'Start date need to be after today',
						(startDate) => {
							if (startDate) {
								const currentDate = new Date();
								currentDate.setHours(0, 0, 0, 0);

								return currentDate < startDate;
							}

							return false;
						}
					)
					.required('Required'),
				tactic: object({
					id: number(),
					name: string(),
				}).test(
					'is-empty',
					'Required',
					(value) => !isObjectEmpty(value)
				),
				targetOfLeads: string()
					.max(350, 'You have exceeded the character limit')
					.required('Required'),
				typeActivity: object({
					externalReferenceCode: string(),
					id: number(),
					name: string(),
				}).test(
					'is-empty',
					'Required',
					(value) => !isObjectEmpty(value)
				),
				venueName: string().when('typeActivity', {
					is: (typeActivity: TypeActivity) =>
						typeActivity.externalReferenceCode ===
						TypeActivityExternalReferenceCode.EVENT,
					then: (schema) =>
						schema
							.max(350, 'You have exceeded the character limit')
							.required('Required'),
				}),
			})
		)
		.min(1),
});

export default activitiesSchema;
