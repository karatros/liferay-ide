/*******************************************************************************
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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
 *******************************************************************************/

package com.liferay.ide.taglib.ui.model;

import com.liferay.ide.taglib.ui.model.internal.DescriptionBinding;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.ReadOnly;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.CustomXmlValueBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author Gregory Amerson
 */
@GenerateImpl
public interface Attribute extends IModelElement
{

    ModelElementType TYPE = new ModelElementType( Attribute.class );

    // *** Name ***

    @XmlBinding( path = "name" )
    @ReadOnly
    ValueProperty PROP_NAME = new ValueProperty( TYPE, "Name" );

    Value<String> getName();

    // *** Value ***

    @XmlBinding( path = "value" )
    ValueProperty PROP_VALUE = new ValueProperty( TYPE, "Value" );

    Value<String> getValue();

    void setValue( String value );

    @XmlBinding( path = "default-value" )
    @ReadOnly
    ValueProperty PROP_DEFAULT_VALUE = new ValueProperty( TYPE, "DefaultValue" );

    Value<String> getDefaultValue();

    // *** Description ***

    @XmlBinding( path = "description" )
    @ReadOnly
    @CustomXmlValueBinding( impl = DescriptionBinding.class )
    ValueProperty PROP_DESCRIPTION = new ValueProperty( TYPE, "Description" );

    Value<String> getDescription();

    // *** Required ***

    @Type( base = Boolean.class )
    @XmlBinding( path = "required" )
    @ReadOnly
    ValueProperty PROP_REQUIRED = new ValueProperty( TYPE, "Required" );

    Value<Boolean> isRequired();

    // *** Type ***

    @XmlBinding( path = "type" )
    @ReadOnly
    ValueProperty PROP_TYPE = new ValueProperty( TYPE, "Type" );

    Value<String> getType();

}
