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

import ClayEmptyState from '@clayui/empty-state';
import {ClayCheckbox, ClayRadio} from '@clayui/form';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import React, {useContext, useRef} from 'react';

import FrontendDataSetContext from '../../FrontendDataSetContext';
import Actions from '../../actions/Actions';
import {getValueDetailsFromItem} from '../../utils/index';
import ViewsContext from '../ViewsContext';
import TableCell from './TableCell';
import TableHead from './TableHead';
import TableInlineAddingRow from './TableInlineAddingRow';
import DndTable from './dnd_table/index';

function getItemFields(
	item,
	fields,
	itemId,
	itemsActions,
	itemInlineChanges = null
) {
	return fields.map((field) => {
		const {actionDropdownItems} = item;
		const {rootPropertyName, value, valuePath} = field.fieldName
			? getValueDetailsFromItem(item, field.fieldName)
			: {};

		return (
			<TableCell
				actions={itemsActions || actionDropdownItems}
				inlineEditSettings={field.inlineEditSettings}
				itemData={item}
				itemId={itemId}
				itemInlineChanges={itemInlineChanges}
				key={valuePath ? valuePath.join('_') : field.label}
				options={field}
				rootPropertyName={rootPropertyName}
				value={value}
				valuePath={valuePath}
				view={{
					contentRenderer: field.contentRenderer,
					contentRendererModuleURL: field.contentRendererModuleURL,
				}}
			/>
		);
	});
}

export function getVisibleFields(fields, visibleFieldNames) {
	const visibleFields = fields.filter(
		({fieldName}) => visibleFieldNames[fieldName]
	);

	return visibleFields.length ? visibleFields : fields;
}

function Table({items, itemsActions, schema, style}) {
	const {
		highlightedItemsValue,
		inlineAddingSettings,
		itemsChanges,
		nestedItemsKey,
		nestedItemsReferenceKey,
		selectItems,
		selectable,
		selectedItemsKey,
		selectedItemsValue,
		selectionType,
		sorting,
		updateSorting,
	} = useContext(FrontendDataSetContext);
	const [
		{
			activeView: {quickActionsEnabled},
			visibleFieldNames,
		},
	] = useContext(ViewsContext);

	const visibleFields = getVisibleFields(schema.fields, visibleFieldNames);

	const actionExistsRef = useRef(
		Boolean(
			itemsActions?.length ||
				items?.find((item) => item.actionDropdownItems?.length)
		)
	);

	const SelectionComponent =
		selectionType === 'multiple' ? ClayCheckbox : ClayRadio;

	const columnNames = [];

	if (selectable) {
		columnNames.push('item-selector');
	}

	columnNames.push(
		...visibleFields.map((field) => String(field.fieldName)),
		'item-actions'
	);

	return (
		<DndTable.ContextProvider columnNames={columnNames}>
			{(inlineAddingSettings ||
				(!inlineAddingSettings && !!items.length)) && (
				<DndTable.Table
					borderless
					className={classNames(`table-style-${style}`, {
						'with-quick-actions': quickActionsEnabled,
					})}
					hover={false}
					responsive
					striped
				>
					<TableHead
						fields={visibleFields}
						items={items}
						schema={schema}
						selectItems={selectItems}
						selectable={selectable}
						selectedItemsKey={selectedItemsKey}
						selectedItemsValue={selectedItemsValue}
						selectionType={selectionType}
						sorting={sorting}
						updateSorting={updateSorting}
					/>

					<DndTable.Body>
						{inlineAddingSettings && (
							<TableInlineAddingRow
								fields={visibleFields}
								selectable={selectable}
							/>
						)}

						{!!items.length &&
							items.map((item) => {
								const itemId = item[selectedItemsKey ?? 'id'];

								const nestedItems =
									nestedItemsReferenceKey &&
									item[nestedItemsReferenceKey];

								return (
									<React.Fragment key={itemId}>
										<DndTable.Row
											className={classNames({
												active: highlightedItemsValue.includes(
													itemId
												),
												selected: selectedItemsValue.includes(
													itemId
												),
											})}
										>
											{selectable && (
												<DndTable.Cell
													className="item-selector"
													columnName="item-selector"
												>
													<SelectionComponent
														checked={
															!!selectedItemsValue.find(
																(element) =>
																	String(
																		element
																	) ===
																	String(
																		itemId
																	)
															)
														}
														onChange={() =>
															selectItems(itemId)
														}
														value={itemId}
													/>
												</DndTable.Cell>
											)}

											{getItemFields(
												item,
												visibleFields,
												itemId,
												itemsActions,
												itemsChanges[itemId]
											)}

											<ActionsCell
												item={item}
												itemId={itemId}
												itemsActions={itemsActions}
											/>
										</DndTable.Row>

										{nestedItems &&
											nestedItems.map((nestedItem, i) => (
												<DndTable.Row
													className={classNames(
														'nested',
														highlightedItemsValue.includes(
															nestedItem[
																nestedItemsKey
															]
														) && 'active',
														i ===
															nestedItems.length -
																1 && 'last'
													)}
													key={
														nestedItem[
															nestedItemsKey
														]
													}
													paddingLeftCells={
														selectable && 1
													}
													paddingRightCells={
														actionExistsRef.current &&
														1
													}
												>
													{getItemFields(
														nestedItem,
														visibleFields,
														nestedItem[
															nestedItemsKey
														],
														itemsActions
													)}
												</DndTable.Row>
											))}
									</React.Fragment>
								);
							})}
					</DndTable.Body>
				</DndTable.Table>
			)}

			{!items.length && (
				<ClayEmptyState
					description={Liferay.Language.get(
						'sorry,-no-results-were-found'
					)}
					imgSrc={`${themeDisplay.getPathThemeImages()}/states/search_state.gif`}
					title={Liferay.Language.get('no-results-found')}
				/>
			)}
		</DndTable.ContextProvider>
	);
}

const ActionsCell = ({item, itemId, itemsActions}) => {
	return (
		<DndTable.Cell className="item-actions" columnName="item-actions">
			{(itemsActions?.length > 0 ||
				item.actionDropdownItems?.length > 0) && (
				<Actions
					actions={itemsActions || item.actionDropdownItems}
					itemData={item}
					itemId={itemId}
				/>
			)}
		</DndTable.Cell>
	);
};

Table.propTypes = {
	items: PropTypes.arrayOf(PropTypes.object),
	itemsActions: PropTypes.array,
	schema: PropTypes.shape({
		fields: PropTypes.arrayOf(
			PropTypes.shape({
				fieldName: PropTypes.oneOfType([
					PropTypes.string,
					PropTypes.array,
				]),
				mapData: PropTypes.func,
			})
		).isRequired,
	}).isRequired,
	style: PropTypes.string.isRequired,
};

Table.defaultProps = {
	items: [],
};

export default Table;
