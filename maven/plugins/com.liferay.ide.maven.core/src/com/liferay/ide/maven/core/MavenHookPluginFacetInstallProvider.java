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
package com.liferay.ide.maven.core;

import com.liferay.ide.project.core.facet.HookPluginFacetInstallDataModelProvider;


/**
 * @author Gregory Amerson
 */
public class MavenHookPluginFacetInstallProvider extends HookPluginFacetInstallDataModelProvider
{

    @Override
    public Object getDefaultProperty( String propertyName )
    {
        if( propertyName.equals( INSTALL_LIFERAY_PLUGIN_LIBRARY_DELEGATE ) )
        {
            return false;
        }
        else if( propertyName.equals( SETUP_DEFAULT_OUTPUT_LOCATION ) )
        {
            return false;
        }
 
        return super.getDefaultProperty( propertyName );
    }

}
