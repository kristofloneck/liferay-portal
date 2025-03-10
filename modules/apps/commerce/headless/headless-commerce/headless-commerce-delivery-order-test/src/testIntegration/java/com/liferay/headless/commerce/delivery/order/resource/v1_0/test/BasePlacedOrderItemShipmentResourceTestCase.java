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

package com.liferay.headless.commerce.delivery.order.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.delivery.order.client.dto.v1_0.PlacedOrderItemShipment;
import com.liferay.headless.commerce.delivery.order.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.order.client.pagination.Page;
import com.liferay.headless.commerce.delivery.order.client.resource.v1_0.PlacedOrderItemShipmentResource;
import com.liferay.headless.commerce.delivery.order.client.serdes.v1_0.PlacedOrderItemShipmentSerDes;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.Method;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.lang.time.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public abstract class BasePlacedOrderItemShipmentResourceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	@Before
	public void setUp() throws Exception {
		irrelevantGroup = GroupTestUtil.addGroup();
		testGroup = GroupTestUtil.addGroup();

		testCompany = CompanyLocalServiceUtil.getCompany(
			testGroup.getCompanyId());

		_placedOrderItemShipmentResource.setContextCompany(testCompany);

		PlacedOrderItemShipmentResource.Builder builder =
			PlacedOrderItemShipmentResource.builder();

		placedOrderItemShipmentResource = builder.authentication(
			"test@liferay.com", "test"
		).locale(
			LocaleUtil.getDefault()
		).build();
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testClientSerDesToDTO() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				enable(SerializationFeature.INDENT_OUTPUT);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		PlacedOrderItemShipment placedOrderItemShipment1 =
			randomPlacedOrderItemShipment();

		String json = objectMapper.writeValueAsString(placedOrderItemShipment1);

		PlacedOrderItemShipment placedOrderItemShipment2 =
			PlacedOrderItemShipmentSerDes.toDTO(json);

		Assert.assertTrue(
			equals(placedOrderItemShipment1, placedOrderItemShipment2));
	}

	@Test
	public void testClientSerDesToJSON() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		PlacedOrderItemShipment placedOrderItemShipment =
			randomPlacedOrderItemShipment();

		String json1 = objectMapper.writeValueAsString(placedOrderItemShipment);
		String json2 = PlacedOrderItemShipmentSerDes.toJSON(
			placedOrderItemShipment);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		PlacedOrderItemShipment placedOrderItemShipment =
			randomPlacedOrderItemShipment();

		placedOrderItemShipment.setAuthor(regex);
		placedOrderItemShipment.setCarrier(regex);
		placedOrderItemShipment.setShippingOptionName(regex);
		placedOrderItemShipment.setTrackingNumber(regex);

		String json = PlacedOrderItemShipmentSerDes.toJSON(
			placedOrderItemShipment);

		Assert.assertFalse(json.contains(regex));

		placedOrderItemShipment = PlacedOrderItemShipmentSerDes.toDTO(json);

		Assert.assertEquals(regex, placedOrderItemShipment.getAuthor());
		Assert.assertEquals(regex, placedOrderItemShipment.getCarrier());
		Assert.assertEquals(
			regex, placedOrderItemShipment.getShippingOptionName());
		Assert.assertEquals(regex, placedOrderItemShipment.getTrackingNumber());
	}

	@Test
	public void testGetPlacedOrderItemPlacedOrderItemShipmentsPage()
		throws Exception {

		Long placedOrderItemId =
			testGetPlacedOrderItemPlacedOrderItemShipmentsPage_getPlacedOrderItemId();
		Long irrelevantPlacedOrderItemId =
			testGetPlacedOrderItemPlacedOrderItemShipmentsPage_getIrrelevantPlacedOrderItemId();

		Page<PlacedOrderItemShipment> page =
			placedOrderItemShipmentResource.
				getPlacedOrderItemPlacedOrderItemShipmentsPage(
					placedOrderItemId);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantPlacedOrderItemId != null) {
			PlacedOrderItemShipment irrelevantPlacedOrderItemShipment =
				testGetPlacedOrderItemPlacedOrderItemShipmentsPage_addPlacedOrderItemShipment(
					irrelevantPlacedOrderItemId,
					randomIrrelevantPlacedOrderItemShipment());

			page =
				placedOrderItemShipmentResource.
					getPlacedOrderItemPlacedOrderItemShipmentsPage(
						irrelevantPlacedOrderItemId);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantPlacedOrderItemShipment),
				(List<PlacedOrderItemShipment>)page.getItems());
			assertValid(page);
		}

		PlacedOrderItemShipment placedOrderItemShipment1 =
			testGetPlacedOrderItemPlacedOrderItemShipmentsPage_addPlacedOrderItemShipment(
				placedOrderItemId, randomPlacedOrderItemShipment());

		PlacedOrderItemShipment placedOrderItemShipment2 =
			testGetPlacedOrderItemPlacedOrderItemShipmentsPage_addPlacedOrderItemShipment(
				placedOrderItemId, randomPlacedOrderItemShipment());

		page =
			placedOrderItemShipmentResource.
				getPlacedOrderItemPlacedOrderItemShipmentsPage(
					placedOrderItemId);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(placedOrderItemShipment1, placedOrderItemShipment2),
			(List<PlacedOrderItemShipment>)page.getItems());
		assertValid(page);
	}

	protected PlacedOrderItemShipment
			testGetPlacedOrderItemPlacedOrderItemShipmentsPage_addPlacedOrderItemShipment(
				Long placedOrderItemId,
				PlacedOrderItemShipment placedOrderItemShipment)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetPlacedOrderItemPlacedOrderItemShipmentsPage_getPlacedOrderItemId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetPlacedOrderItemPlacedOrderItemShipmentsPage_getIrrelevantPlacedOrderItemId()
		throws Exception {

		return null;
	}

	protected PlacedOrderItemShipment
			testGraphQLPlacedOrderItemShipment_addPlacedOrderItemShipment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		PlacedOrderItemShipment placedOrderItemShipment,
		List<PlacedOrderItemShipment> placedOrderItemShipments) {

		boolean contains = false;

		for (PlacedOrderItemShipment item : placedOrderItemShipments) {
			if (equals(placedOrderItemShipment, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			placedOrderItemShipments + " does not contain " +
				placedOrderItemShipment,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		PlacedOrderItemShipment placedOrderItemShipment1,
		PlacedOrderItemShipment placedOrderItemShipment2) {

		Assert.assertTrue(
			placedOrderItemShipment1 + " does not equal " +
				placedOrderItemShipment2,
			equals(placedOrderItemShipment1, placedOrderItemShipment2));
	}

	protected void assertEquals(
		List<PlacedOrderItemShipment> placedOrderItemShipments1,
		List<PlacedOrderItemShipment> placedOrderItemShipments2) {

		Assert.assertEquals(
			placedOrderItemShipments1.size(), placedOrderItemShipments2.size());

		for (int i = 0; i < placedOrderItemShipments1.size(); i++) {
			PlacedOrderItemShipment placedOrderItemShipment1 =
				placedOrderItemShipments1.get(i);
			PlacedOrderItemShipment placedOrderItemShipment2 =
				placedOrderItemShipments2.get(i);

			assertEquals(placedOrderItemShipment1, placedOrderItemShipment2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<PlacedOrderItemShipment> placedOrderItemShipments1,
		List<PlacedOrderItemShipment> placedOrderItemShipments2) {

		Assert.assertEquals(
			placedOrderItemShipments1.size(), placedOrderItemShipments2.size());

		for (PlacedOrderItemShipment placedOrderItemShipment1 :
				placedOrderItemShipments1) {

			boolean contains = false;

			for (PlacedOrderItemShipment placedOrderItemShipment2 :
					placedOrderItemShipments2) {

				if (equals(
						placedOrderItemShipment1, placedOrderItemShipment2)) {

					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				placedOrderItemShipments2 + " does not contain " +
					placedOrderItemShipment1,
				contains);
		}
	}

	protected void assertValid(PlacedOrderItemShipment placedOrderItemShipment)
		throws Exception {

		boolean valid = true;

		if (placedOrderItemShipment.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("accountId", additionalAssertFieldName)) {
				if (placedOrderItemShipment.getAccountId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("author", additionalAssertFieldName)) {
				if (placedOrderItemShipment.getAuthor() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("carrier", additionalAssertFieldName)) {
				if (placedOrderItemShipment.getCarrier() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("createDate", additionalAssertFieldName)) {
				if (placedOrderItemShipment.getCreateDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"estimatedDeliveryDate", additionalAssertFieldName)) {

				if (placedOrderItemShipment.getEstimatedDeliveryDate() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"estimatedShippingDate", additionalAssertFieldName)) {

				if (placedOrderItemShipment.getEstimatedShippingDate() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("modifiedDate", additionalAssertFieldName)) {
				if (placedOrderItemShipment.getModifiedDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("orderId", additionalAssertFieldName)) {
				if (placedOrderItemShipment.getOrderId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("quantity", additionalAssertFieldName)) {
				if (placedOrderItemShipment.getQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingAddressId", additionalAssertFieldName)) {

				if (placedOrderItemShipment.getShippingAddressId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("shippingMethodId", additionalAssertFieldName)) {
				if (placedOrderItemShipment.getShippingMethodId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingOptionName", additionalAssertFieldName)) {

				if (placedOrderItemShipment.getShippingOptionName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("status", additionalAssertFieldName)) {
				if (placedOrderItemShipment.getStatus() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("trackingNumber", additionalAssertFieldName)) {
				if (placedOrderItemShipment.getTrackingNumber() == null) {
					valid = false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(Page<PlacedOrderItemShipment> page) {
		boolean valid = false;

		java.util.Collection<PlacedOrderItemShipment> placedOrderItemShipments =
			page.getItems();

		int size = placedOrderItemShipments.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected List<GraphQLField> getGraphQLFields() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field :
				getDeclaredFields(
					com.liferay.headless.commerce.delivery.order.dto.v1_0.
						PlacedOrderItemShipment.class)) {

			if (!ArrayUtil.contains(
					getAdditionalAssertFieldNames(), field.getName())) {

				continue;
			}

			graphQLFields.addAll(getGraphQLFields(field));
		}

		return graphQLFields;
	}

	protected List<GraphQLField> getGraphQLFields(
			java.lang.reflect.Field... fields)
		throws Exception {

		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field : fields) {
			com.liferay.portal.vulcan.graphql.annotation.GraphQLField
				vulcanGraphQLField = field.getAnnotation(
					com.liferay.portal.vulcan.graphql.annotation.GraphQLField.
						class);

			if (vulcanGraphQLField != null) {
				Class<?> clazz = field.getType();

				if (clazz.isArray()) {
					clazz = clazz.getComponentType();
				}

				List<GraphQLField> childrenGraphQLFields = getGraphQLFields(
					getDeclaredFields(clazz));

				graphQLFields.add(
					new GraphQLField(field.getName(), childrenGraphQLFields));
			}
		}

		return graphQLFields;
	}

	protected String[] getIgnoredEntityFieldNames() {
		return new String[0];
	}

	protected boolean equals(
		PlacedOrderItemShipment placedOrderItemShipment1,
		PlacedOrderItemShipment placedOrderItemShipment2) {

		if (placedOrderItemShipment1 == placedOrderItemShipment2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("accountId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						placedOrderItemShipment1.getAccountId(),
						placedOrderItemShipment2.getAccountId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("author", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						placedOrderItemShipment1.getAuthor(),
						placedOrderItemShipment2.getAuthor())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("carrier", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						placedOrderItemShipment1.getCarrier(),
						placedOrderItemShipment2.getCarrier())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("createDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						placedOrderItemShipment1.getCreateDate(),
						placedOrderItemShipment2.getCreateDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"estimatedDeliveryDate", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						placedOrderItemShipment1.getEstimatedDeliveryDate(),
						placedOrderItemShipment2.getEstimatedDeliveryDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"estimatedShippingDate", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						placedOrderItemShipment1.getEstimatedShippingDate(),
						placedOrderItemShipment2.getEstimatedShippingDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						placedOrderItemShipment1.getId(),
						placedOrderItemShipment2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("modifiedDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						placedOrderItemShipment1.getModifiedDate(),
						placedOrderItemShipment2.getModifiedDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("orderId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						placedOrderItemShipment1.getOrderId(),
						placedOrderItemShipment2.getOrderId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("quantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						placedOrderItemShipment1.getQuantity(),
						placedOrderItemShipment2.getQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingAddressId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						placedOrderItemShipment1.getShippingAddressId(),
						placedOrderItemShipment2.getShippingAddressId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("shippingMethodId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						placedOrderItemShipment1.getShippingMethodId(),
						placedOrderItemShipment2.getShippingMethodId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingOptionName", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						placedOrderItemShipment1.getShippingOptionName(),
						placedOrderItemShipment2.getShippingOptionName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("status", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						placedOrderItemShipment1.getStatus(),
						placedOrderItemShipment2.getStatus())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("trackingNumber", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						placedOrderItemShipment1.getTrackingNumber(),
						placedOrderItemShipment2.getTrackingNumber())) {

					return false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		return true;
	}

	protected boolean equals(
		Map<String, Object> map1, Map<String, Object> map2) {

		if (Objects.equals(map1.keySet(), map2.keySet())) {
			for (Map.Entry<String, Object> entry : map1.entrySet()) {
				if (entry.getValue() instanceof Map) {
					if (!equals(
							(Map)entry.getValue(),
							(Map)map2.get(entry.getKey()))) {

						return false;
					}
				}
				else if (!Objects.deepEquals(
							entry.getValue(), map2.get(entry.getKey()))) {

					return false;
				}
			}

			return true;
		}

		return false;
	}

	protected java.lang.reflect.Field[] getDeclaredFields(Class clazz)
		throws Exception {

		Stream<java.lang.reflect.Field> stream = Stream.of(
			ReflectionUtil.getDeclaredFields(clazz));

		return stream.filter(
			field -> !field.isSynthetic()
		).toArray(
			java.lang.reflect.Field[]::new
		);
	}

	protected java.util.Collection<EntityField> getEntityFields()
		throws Exception {

		if (!(_placedOrderItemShipmentResource instanceof
				EntityModelResource)) {

			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_placedOrderItemShipmentResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		java.util.Collection<EntityField> entityFields = getEntityFields();

		Stream<EntityField> stream = entityFields.stream();

		return stream.filter(
			entityField ->
				Objects.equals(entityField.getType(), type) &&
				!ArrayUtil.contains(
					getIgnoredEntityFieldNames(), entityField.getName())
		).collect(
			Collectors.toList()
		);
	}

	protected String getFilterString(
		EntityField entityField, String operator,
		PlacedOrderItemShipment placedOrderItemShipment) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("accountId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("author")) {
			sb.append("'");
			sb.append(String.valueOf(placedOrderItemShipment.getAuthor()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("carrier")) {
			sb.append("'");
			sb.append(String.valueOf(placedOrderItemShipment.getCarrier()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("createDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							placedOrderItemShipment.getCreateDate(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							placedOrderItemShipment.getCreateDate(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(
					_dateFormat.format(
						placedOrderItemShipment.getCreateDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("estimatedDeliveryDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							placedOrderItemShipment.getEstimatedDeliveryDate(),
							-2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							placedOrderItemShipment.getEstimatedDeliveryDate(),
							2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(
					_dateFormat.format(
						placedOrderItemShipment.getEstimatedDeliveryDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("estimatedShippingDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							placedOrderItemShipment.getEstimatedShippingDate(),
							-2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							placedOrderItemShipment.getEstimatedShippingDate(),
							2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(
					_dateFormat.format(
						placedOrderItemShipment.getEstimatedShippingDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("modifiedDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							placedOrderItemShipment.getModifiedDate(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							placedOrderItemShipment.getModifiedDate(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(
					_dateFormat.format(
						placedOrderItemShipment.getModifiedDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("orderId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("quantity")) {
			sb.append(String.valueOf(placedOrderItemShipment.getQuantity()));

			return sb.toString();
		}

		if (entityFieldName.equals("shippingAddressId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("shippingMethodId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("shippingOptionName")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					placedOrderItemShipment.getShippingOptionName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("status")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("trackingNumber")) {
			sb.append("'");
			sb.append(
				String.valueOf(placedOrderItemShipment.getTrackingNumber()));
			sb.append("'");

			return sb.toString();
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected String invoke(String query) throws Exception {
		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(
			JSONUtil.put(
				"query", query
			).toString(),
			"application/json");
		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);
		httpInvoker.path("http://localhost:8080/o/graphql");
		httpInvoker.userNameAndPassword("test@liferay.com:test");

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse.getContent();
	}

	protected JSONObject invokeGraphQLMutation(GraphQLField graphQLField)
		throws Exception {

		GraphQLField mutationGraphQLField = new GraphQLField(
			"mutation", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(mutationGraphQLField.toString()));
	}

	protected JSONObject invokeGraphQLQuery(GraphQLField graphQLField)
		throws Exception {

		GraphQLField queryGraphQLField = new GraphQLField(
			"query", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(queryGraphQLField.toString()));
	}

	protected PlacedOrderItemShipment randomPlacedOrderItemShipment()
		throws Exception {

		return new PlacedOrderItemShipment() {
			{
				accountId = RandomTestUtil.randomLong();
				author = StringUtil.toLowerCase(RandomTestUtil.randomString());
				carrier = StringUtil.toLowerCase(RandomTestUtil.randomString());
				createDate = RandomTestUtil.nextDate();
				estimatedDeliveryDate = RandomTestUtil.nextDate();
				estimatedShippingDate = RandomTestUtil.nextDate();
				id = RandomTestUtil.randomLong();
				modifiedDate = RandomTestUtil.nextDate();
				orderId = RandomTestUtil.randomLong();
				quantity = RandomTestUtil.randomInt();
				shippingAddressId = RandomTestUtil.randomLong();
				shippingMethodId = RandomTestUtil.randomLong();
				shippingOptionName = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				trackingNumber = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
			}
		};
	}

	protected PlacedOrderItemShipment randomIrrelevantPlacedOrderItemShipment()
		throws Exception {

		PlacedOrderItemShipment randomIrrelevantPlacedOrderItemShipment =
			randomPlacedOrderItemShipment();

		return randomIrrelevantPlacedOrderItemShipment;
	}

	protected PlacedOrderItemShipment randomPatchPlacedOrderItemShipment()
		throws Exception {

		return randomPlacedOrderItemShipment();
	}

	protected PlacedOrderItemShipmentResource placedOrderItemShipmentResource;
	protected Group irrelevantGroup;
	protected Company testCompany;
	protected Group testGroup;

	protected static class BeanTestUtil {

		public static void copyProperties(Object source, Object target)
			throws Exception {

			Class<?> sourceClass = _getSuperClass(source.getClass());

			Class<?> targetClass = target.getClass();

			for (java.lang.reflect.Field field :
					sourceClass.getDeclaredFields()) {

				if (field.isSynthetic()) {
					continue;
				}

				Method getMethod = _getMethod(
					sourceClass, field.getName(), "get");

				Method setMethod = _getMethod(
					targetClass, field.getName(), "set",
					getMethod.getReturnType());

				setMethod.invoke(target, getMethod.invoke(source));
			}
		}

		public static boolean hasProperty(Object bean, String name) {
			Method setMethod = _getMethod(
				bean.getClass(), "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod != null) {
				return true;
			}

			return false;
		}

		public static void setProperty(Object bean, String name, Object value)
			throws Exception {

			Class<?> clazz = bean.getClass();

			Method setMethod = _getMethod(
				clazz, "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod == null) {
				throw new NoSuchMethodException();
			}

			Class<?>[] parameterTypes = setMethod.getParameterTypes();

			setMethod.invoke(bean, _translateValue(parameterTypes[0], value));
		}

		private static Method _getMethod(Class<?> clazz, String name) {
			for (Method method : clazz.getMethods()) {
				if (name.equals(method.getName()) &&
					(method.getParameterCount() == 1) &&
					_parameterTypes.contains(method.getParameterTypes()[0])) {

					return method;
				}
			}

			return null;
		}

		private static Method _getMethod(
				Class<?> clazz, String fieldName, String prefix,
				Class<?>... parameterTypes)
			throws Exception {

			return clazz.getMethod(
				prefix + StringUtil.upperCaseFirstLetter(fieldName),
				parameterTypes);
		}

		private static Class<?> _getSuperClass(Class<?> clazz) {
			Class<?> superClass = clazz.getSuperclass();

			if ((superClass == null) || (superClass == Object.class)) {
				return clazz;
			}

			return superClass;
		}

		private static Object _translateValue(
			Class<?> parameterType, Object value) {

			if ((value instanceof Integer) &&
				parameterType.equals(Long.class)) {

				Integer intValue = (Integer)value;

				return intValue.longValue();
			}

			return value;
		}

		private static final Set<Class<?>> _parameterTypes = new HashSet<>(
			Arrays.asList(
				Boolean.class, Date.class, Double.class, Integer.class,
				Long.class, Map.class, String.class));

	}

	protected class GraphQLField {

		public GraphQLField(String key, GraphQLField... graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(String key, List<GraphQLField> graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			GraphQLField... graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = Arrays.asList(graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			List<GraphQLField> graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = graphQLFields;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(_key);

			if (!_parameterMap.isEmpty()) {
				sb.append("(");

				for (Map.Entry<String, Object> entry :
						_parameterMap.entrySet()) {

					sb.append(entry.getKey());
					sb.append(": ");
					sb.append(entry.getValue());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append(")");
			}

			if (!_graphQLFields.isEmpty()) {
				sb.append("{");

				for (GraphQLField graphQLField : _graphQLFields) {
					sb.append(graphQLField.toString());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append("}");
			}

			return sb.toString();
		}

		private final List<GraphQLField> _graphQLFields;
		private final String _key;
		private final Map<String, Object> _parameterMap;

	}

	private static final com.liferay.portal.kernel.log.Log _log =
		LogFactoryUtil.getLog(
			BasePlacedOrderItemShipmentResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.delivery.order.resource.v1_0.
		PlacedOrderItemShipmentResource _placedOrderItemShipmentResource;

}