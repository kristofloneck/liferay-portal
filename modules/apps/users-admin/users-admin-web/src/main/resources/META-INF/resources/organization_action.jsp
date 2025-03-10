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

<%@ include file="/init.jsp" %>

<%
SearchContainer<?> searchContainer = (SearchContainer<?>)request.getAttribute("liferay-ui:search:searchContainer");

String redirect = currentURL;

if ((searchContainer != null) && (searchContainer instanceof OrganizationSearch)) {
	PortletURL iteratorURL = searchContainer.getIteratorURL();

	redirect = iteratorURL.toString();
}

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Organization organization = null;

if (row != null) {
	organization = (Organization)row.getObject();
}
else {
	organization = (Organization)request.getAttribute("view_organizations_tree.jsp-organization");
}

long organizationId = organization.getOrganizationId();

Group organizationGroup = organization.getGroup();

long organizationGroupId = organization.getGroupId();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>

	<%
	boolean hasUpdatePermission = OrganizationPermissionUtil.contains(permissionChecker, organization, ActionKeys.UPDATE);
	%>

	<c:if test="<%= hasUpdatePermission %>">
		<portlet:renderURL var="editOrganizationURL">
			<portlet:param name="mvcRenderCommandName" value="/users_admin/edit_organization" />
			<portlet:param name="backURL" value="<%= redirect %>" />
			<portlet:param name="organizationId" value="<%= String.valueOf(organizationId) %>" />
		</portlet:renderURL>

		<liferay-ui:icon
			message="edit"
			url="<%= editOrganizationURL %>"
		/>
	</c:if>

	<c:if test="<%= organizationGroup.isSite() && (GroupPermissionUtil.contains(permissionChecker, organizationGroup, ActionKeys.MANAGE_STAGING) || hasUpdatePermission) %>">
		<liferay-ui:icon
			message="manage-site"
			url='<%=
				PortletURLBuilder.create(
					PortletProviderUtil.getPortletURL(request, organizationGroup, Group.class.getName(), PortletProvider.Action.EDIT)
				).setParameter(
					"viewOrganizationsRedirect", currentURL
				).buildString()
			%>'
		/>
	</c:if>

	<c:if test="<%= permissionChecker.isGroupOwner(organizationGroupId) || OrganizationPermissionUtil.contains(permissionChecker, organization, ActionKeys.ASSIGN_USER_ROLES) %>">
		<liferay-ui:icon
			message="assign-organization-roles"
			url='<%=
				PortletURLBuilder.create(
					PortletProviderUtil.getPortletURL(request, UserGroupRole.class.getName(), PortletProvider.Action.EDIT)
				).setParameter(
					"className", User.class.getName()
				).setParameter(
					"groupId", organizationGroupId
				).setWindowState(
					LiferayWindowState.POP_UP
				).buildString()
			%>'
			useDialog="<%= true %>"
		/>
	</c:if>

	<c:if test="<%= OrganizationPermissionUtil.contains(permissionChecker, organization, ActionKeys.ASSIGN_MEMBERS) %>">

		<%
		String taglibOnClick = liferayPortletResponse.getNamespace() + "openSelectUsersDialog('" + organizationId + "');";
		%>

		<liferay-ui:icon
			message="assign-users"
			onClick="<%= taglibOnClick %>"
			url="javascript:void(0);"
		/>
	</c:if>

	<c:if test="<%= OrganizationPermissionUtil.contains(permissionChecker, organization, ActionKeys.MANAGE_USERS) %>">
		<portlet:renderURL var="addUserURL">
			<portlet:param name="mvcRenderCommandName" value="/users_admin/edit_user" />
			<portlet:param name="backURL" value="<%= redirect %>" />
			<portlet:param name="organizationsSearchContainerPrimaryKeys" value="<%= String.valueOf(organizationId) %>" />
		</portlet:renderURL>

		<liferay-ui:icon
			message="add-user"
			url="<%= addUserURL %>"
		/>
	</c:if>

	<c:if test="<%= organization.isParentable() %>">

		<%
		for (String childrenType : organization.getChildrenTypes()) {
		%>

			<c:if test="<%= OrganizationPermissionUtil.contains(permissionChecker, organization, ActionKeys.ADD_ORGANIZATION) %>">
				<portlet:renderURL var="addSuborganizationURL">
					<portlet:param name="mvcRenderCommandName" value="/users_admin/edit_organization" />
					<portlet:param name="backURL" value="<%= redirect %>" />
					<portlet:param name="parentOrganizationSearchContainerPrimaryKeys" value="<%= String.valueOf(organizationId) %>" />
					<portlet:param name="type" value="<%= childrenType %>" />
				</portlet:renderURL>

				<liferay-ui:icon
					message='<%= LanguageUtil.format(request, "add-x", childrenType) %>'
					url="<%= addSuborganizationURL %>"
				/>
			</c:if>

		<%
		}
		%>

	</c:if>

	<c:if test="<%= OrganizationPermissionUtil.contains(permissionChecker, organization, ActionKeys.DELETE) %>">

		<%
		String taglibDeleteURL = "javascript:" + liferayPortletResponse.getNamespace() + "deleteOrganization('" + organizationId + "');";
		%>

		<liferay-ui:icon
			cssClass="item-remove"
			message="delete"
			url="<%= taglibDeleteURL %>"
		/>
	</c:if>

	<%
	long parentOrganizationId = GetterUtil.getLong(request.getAttribute("view.jsp-organizationId"));
	%>

	<c:if test="<%= (parentOrganizationId > 0) && hasUpdatePermission %>">
		<portlet:actionURL name="/users_admin/edit_organization_assignments" var="removeOrganizationURL">
			<portlet:param name="assignmentsRedirect" value="<%= redirect %>" />
			<portlet:param name="removeOrganizationIds" value="<%= String.valueOf(organizationId) %>" />
		</portlet:actionURL>

		<liferay-ui:icon
			message="remove"
			url="<%= removeOrganizationURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>