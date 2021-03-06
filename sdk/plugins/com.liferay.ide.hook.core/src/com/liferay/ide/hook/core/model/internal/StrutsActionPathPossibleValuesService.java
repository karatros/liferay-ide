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
 * Contributors:
 * 		Gregory Amerson - initial implementation and ongoing maintenance
 *******************************************************************************/

package com.liferay.ide.hook.core.model.internal;

import static com.liferay.ide.core.util.CoreUtil.empty;

import com.liferay.ide.server.core.ILiferayRuntime;
import com.liferay.ide.server.util.ServerUtil;
import com.liferay.ide.hook.core.model.Hook;
import com.liferay.ide.hook.core.model.StrutsAction;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.sapphire.services.PossibleValuesService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Gregory Amerson
 */
public class StrutsActionPathPossibleValuesService extends PossibleValuesService
{

    private IPath portalDir;
    private TreeSet<String> possibleValues;

    @Override
    protected void fillPossibleValues( SortedSet<String> values )
    {
        if( this.portalDir != null && this.portalDir.toFile().exists() )
        {
            if( this.possibleValues == null )
            {
                this.possibleValues = new TreeSet<String>();
                File strutsConfigFile = this.portalDir.append( "WEB-INF/struts-config.xml" ).toFile();

                if( strutsConfigFile.exists() )
                {
                    try
                    {
                        Document doc =
                            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( strutsConfigFile );
                        NodeList actions = doc.getElementsByTagName( "action" );

                        if( actions != null )
                        {
                            for( int i = 0; i < actions.getLength(); i++ )
                            {
                                Node action = actions.item( i );

                                Node path = action.getAttributes().getNamedItem( "path" );

                                if( path != null )
                                {
                                    possibleValues.add( path.getNodeValue() );
                                }
                            }
                        }
                    }
                    catch( Exception e )
                    {
                    }
                }
            }

            values.addAll( this.possibleValues );

            // add the value that is current set by the user
            String actionPath = context( StrutsAction.class ).getStrutsActionPath().getContent( false );

            if( !empty( actionPath ) )
            {
                values.add( actionPath );
            }
        }
    }

    @Override
    protected void init()
    {
        super.init();

        final IProject project = project();

        if( project != null )
        {
            try
            {
                ILiferayRuntime lr = ServerUtil.getLiferayRuntime( project );

                if( lr != null )
                {
                    this.portalDir = lr.getPortalDir();
                }
            }
            catch( CoreException e )
            {
            }
        }
    }

    protected Hook hook()
    {
        return this.context().find( Hook.class );
    }

    protected IProject project()
    {
        return this.hook().adapt( IProject.class );
    }
}
