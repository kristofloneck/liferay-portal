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

import ClayLabel from '@clayui/label';
import React from 'react';

import Panel from '../../Panel/Panel';
import {useLayoutContext} from '../objectLayoutContext';

interface IObjectLayoutRelationshipProps
	extends React.HTMLAttributes<HTMLElement> {
	objectRelationshipId: number;
}
const defaultLanguageId = Liferay.ThemeDisplay.getDefaultLanguageId();

const ObjectLayoutRelationship: React.FC<IObjectLayoutRelationshipProps> = ({
	objectRelationshipId,
}) => {
	const [{objectRelationships}] = useLayoutContext();

	const objectRelationship = objectRelationships.find(
		({id}) => id === objectRelationshipId
	)!;

	return (
		<>
			<Panel key={`field_${objectRelationshipId}`}>
				<Panel.SimpleBody
					title={objectRelationship?.label[defaultLanguageId]!}
				>
					<small className="text-secondary">
						{Liferay.Language.get('relationship')} |{' '}
					</small>

					<ClayLabel
						displayType={
							Liferay.FeatureFlags['LPS-158478']
								? objectRelationship.reverse
									? 'info'
									: 'success'
								: 'secondary'
						}
					>
						{Liferay.FeatureFlags['LPS-158478']
							? objectRelationship.reverse
								? Liferay.Language.get('child')
								: Liferay.Language.get('parent')
							: objectRelationship?.type}
					</ClayLabel>
				</Panel.SimpleBody>
			</Panel>
		</>
	);
};

export default ObjectLayoutRelationship;
