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

import {ClayButtonWithIcon} from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import PropTypes from 'prop-types';
import React, {useContext, useEffect, useState} from 'react';

import FrontendDataSetContext from '../../FrontendDataSetContext';
import persistVisibleFieldNames from '../../thunks/persistVisibleFieldNames';
import ViewsContext from '../ViewsContext';

const FieldsSelectorDropdown = ({fields}) => {
	const {appURL, id, portletId} = useContext(FrontendDataSetContext);
	const [{visibleFieldNames}, dispatch] = useContext(ViewsContext);

	const [active, setActive] = useState(false);
	const [filteredFields, setFilteredFields] = useState(fields);
	const [query, setQuery] = useState('');

	const selectedFieldNames = Object.keys(visibleFieldNames).length
		? visibleFieldNames
		: fields.reduce(
				(selectedFieldNames, field) => ({
					...selectedFieldNames,
					[field.fieldName]: true,
				}),
				{}
		  );

	useEffect(() => {
		setFilteredFields(
			fields.filter((field) =>
				field.label.toLowerCase().includes(query.toLowerCase())
			)
		);
	}, [fields, query]);

	return (
		<ClayDropDown
			active={active}
			className="ml-auto"
			hasLeftSymbols
			onActiveChange={setActive}
			trigger={
				<ClayButtonWithIcon
					aria-label={
						active
							? Liferay.Language.get('close-fields-menu')
							: Liferay.Language.get('open-fields-menu')
					}
					borderless
					displayType="secondary"
					symbol={active ? 'caret-top' : 'caret-bottom'}
				/>
			}
		>
			<ClayDropDown.Search
				formProps={{onSubmit: (event) => event.preventDefault()}}
				onChange={(event) => setQuery(event.target.value)}
				value={query}
			/>

			{filteredFields.length ? (
				<ClayDropDown.ItemList>
					{filteredFields.map(({fieldName, label}) => (
						<ClayDropDown.Item
							key={fieldName}
							onClick={() => {
								const newVisibleFieldNames = {
									...selectedFieldNames,
									[fieldName]: !selectedFieldNames[fieldName],
								};

								const isVisible = Object.keys(
									newVisibleFieldNames
								).some(
									(visibleFieldName) =>
										newVisibleFieldNames[visibleFieldName]
								);

								if (isVisible) {
									dispatch(
										persistVisibleFieldNames({
											appURL,
											id,
											portletId,
											visibleFieldNames: {
												...selectedFieldNames,
												[fieldName]: !selectedFieldNames[
													fieldName
												],
											},
										})
									);
								}
							}}
							symbolLeft={
								selectedFieldNames[fieldName] ? 'check' : null
							}
						>
							{label}
						</ClayDropDown.Item>
					))}
				</ClayDropDown.ItemList>
			) : (
				<div className="dropdown-section text-muted">
					{Liferay.Language.get('no-fields-were-found')}
				</div>
			)}
		</ClayDropDown>
	);
};

FieldsSelectorDropdown.propTypes = {
	fields: PropTypes.arrayOf(
		PropTypes.shape({
			fieldName: PropTypes.oneOfType([
				PropTypes.string,
				PropTypes.arrayOf(PropTypes.string),
			]),
			label: PropTypes.string,
		})
	),
};

export default FieldsSelectorDropdown;
