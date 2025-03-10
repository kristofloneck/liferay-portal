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

package com.liferay.dynamic.data.mapping.form.field.type.internal.select.multi.language.option;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.form.field.type.internal.select.SelectDDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Paulino
 */
@Component(
	immediate = true,
	property = "ddm.form.field.type.name=" + DDMFormFieldTypeConstants.MULTI_LANGUAGE_OPTION_SELECT,
	service = DDMFormFieldTemplateContextContributor.class
)
public class MultiLanguageOptionSelectDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Set<Locale> availableLocales = _language.getAvailableLocales();

		Map<String, Object> parameters =
			_selectDDMFormFieldTemplateContextContributor.getParameters(
				ddmFormField, ddmFormFieldRenderingContext);

		for (Map<String, Object> option :
				(List<Map<String, Object>>)parameters.get("options")) {

			HashMap<String, String> labelsMap = new HashMap<>();

			option.put("label", labelsMap);

			for (Locale availableLocale : availableLocales) {
				labelsMap.put(
					_language.getLanguageId(availableLocale),
					_language.get(
						ResourceBundleUtil.getModuleAndPortalResourceBundle(
							availableLocale, getClass()),
						(String)option.get("value")));
			}
		}

		return parameters;
	}

	@Reference
	private Language _language;

	@Reference
	private SelectDDMFormFieldTemplateContextContributor
		_selectDDMFormFieldTemplateContextContributor;

}