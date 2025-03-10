<%--
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
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/clay" prefix="clay" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.commerce.item.selector.web.internal.display.context.CommerceCountryItemSelectorViewDisplayContext" %><%@
page import="com.liferay.commerce.item.selector.web.internal.display.context.CommerceCountryItemSelectorViewManagementToolbarDisplayContext" %><%@
page import="com.liferay.commerce.item.selector.web.internal.display.context.CommerceInventoryWarehouseItemSelectorViewDisplayContext" %><%@
page import="com.liferay.commerce.item.selector.web.internal.display.context.CommerceInventoryWarehouseManagementToolbarDisplayContext" %><%@
page import="com.liferay.commerce.item.selector.web.internal.display.context.CommercePriceListItemSelectorViewDisplayContext" %><%@
page import="com.liferay.commerce.item.selector.web.internal.display.context.CommercePriceListItemSelectorViewManagementToolbarDisplayContext" %><%@
page import="com.liferay.commerce.item.selector.web.internal.display.context.CommercePricingClassItemSelectorViewDisplayContext" %><%@
page import="com.liferay.commerce.item.selector.web.internal.display.context.CommercePricingClassItemSelectorViewManagementToolbarDisplayContext" %><%@
page import="com.liferay.commerce.item.selector.web.internal.display.context.CommerceProductInstanceItemSelectorViewDisplayContext" %><%@
page import="com.liferay.commerce.item.selector.web.internal.display.context.CommerceProductInstanceItemSelectorViewManagementToolbarDisplayContext" %><%@
page import="com.liferay.commerce.item.selector.web.internal.display.context.SimpleSiteItemSelectorViewDisplayContext" %><%@
page import="com.liferay.commerce.item.selector.web.internal.display.context.SimpleSiteItemSelectorViewManagementToolbarDisplayContext" %><%@
page import="com.liferay.commerce.product.model.CPDefinition" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.util.HashMapBuilder" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />