/*******************************************************************************
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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
 *
 * Contributors:
 *    Kamesh Sampath - initial implementation
 ******************************************************************************/

package com.liferay.ide.hook.core.model;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.NoDuplicates;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author <a href="mailto:kamesh.sampath@hotmail.com">Kamesh Sampath</a>
 */
@GenerateImpl
public interface NameValue extends IModelElement
{

    ModelElementType TYPE = new ModelElementType( NameValue.class );

    /*
     * Name Element
     */

    @Label( standard = "Name" )
    @Required
    @NoDuplicates
    @XmlBinding( path = "param-name" )
    ValueProperty PROP_NAME = new ValueProperty( TYPE, "Name" );

    Value<String> getName();

    void setName( String name );

    /*
     * Value Element
     */

    @Label( standard = "Value" )
    @XmlBinding( path = "param-value" )
    ValueProperty PROP_VALUE = new ValueProperty( TYPE, "Value" );

    Value<String> getValue();

    void setValue( String value );
}
