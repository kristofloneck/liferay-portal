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

package com.liferay.layout.admin.web.internal.servlet.taglib.clay;

import com.liferay.frontend.taglib.clay.servlet.taglib.VerticalCard;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class SelectLayoutPageTemplateEntryVerticalCard implements VerticalCard {

	public SelectLayoutPageTemplateEntryVerticalCard(
		LayoutPageTemplateEntry layoutPageTemplateEntry,
		RenderRequest renderRequest, RenderResponse renderResponse) {

		_layoutPageTemplateEntry = layoutPageTemplateEntry;
		_renderResponse = renderResponse;

		_httpServletRequest = PortalUtil.getHttpServletRequest(renderRequest);
		_themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public String getCssClass() {
		return "add-layout-action-option card-interactive " +
			"card-interactive-primary";
	}

	@Override
	public Map<String, String> getDynamicAttributes() {
		Map<String, String> data = new HashMap<>();

		try {
			data.put(
				"data-add-layout-url",
				PortletURLBuilder.createRenderURL(
					_renderResponse
				).setMVCRenderCommandName(
					"/layout_admin/add_layout"
				).setBackURL(
					ParamUtil.getString(_httpServletRequest, "redirect")
				).setParameter(
					"layoutPageTemplateEntryId",
					_layoutPageTemplateEntry.getLayoutPageTemplateEntryId()
				).setParameter(
					"privateLayout",
					ParamUtil.getBoolean(_httpServletRequest, "privateLayout")
				).setParameter(
					"selPlid", ParamUtil.getLong(_httpServletRequest, "selPlid")
				).setWindowState(
					LiferayWindowState.POP_UP
				).buildString());
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		data.put("role", "button");
		data.put("tabIndex", "0");

		return data;
	}

	@Override
	public String getIcon() {
		if (Objects.equals(
				_layoutPageTemplateEntry.getType(),
				LayoutPageTemplateEntryTypeConstants.TYPE_WIDGET_PAGE)) {

			return "page-template";
		}

		return "page";
	}

	@Override
	public String getImageSrc() {
		return _layoutPageTemplateEntry.getImagePreviewURL(_themeDisplay);
	}

	@Override
	public String getSubtitle() {
		if (Objects.equals(
				_layoutPageTemplateEntry.getType(),
				LayoutPageTemplateEntryTypeConstants.TYPE_WIDGET_PAGE)) {

			return LanguageUtil.get(
				_httpServletRequest, "widget-page-template");
		}

		return LanguageUtil.get(_httpServletRequest, "content-page-template");
	}

	@Override
	public String getTitle() {
		return HtmlUtil.escape(_layoutPageTemplateEntry.getName());
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SelectLayoutPageTemplateEntryVerticalCard.class);

	private final HttpServletRequest _httpServletRequest;
	private final LayoutPageTemplateEntry _layoutPageTemplateEntry;
	private final RenderResponse _renderResponse;
	private final ThemeDisplay _themeDisplay;

}