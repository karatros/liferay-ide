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

package com.liferay.ide.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatterOptions;
import org.eclipse.jdt.internal.ui.preferences.PreferencesAccess;
import org.eclipse.jdt.internal.ui.preferences.formatter.FormatterProfileStore;
import org.eclipse.jdt.internal.ui.preferences.formatter.ProfileManager.CustomProfile;
import org.eclipse.jdt.internal.ui.preferences.formatter.ProfileManager.Profile;
import org.eclipse.jdt.internal.ui.preferences.formatter.ProfileVersioner;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;

/**
 * The activator class controls the plugin life cycle
 * 
 * @author Greg Amerson
 */
@SuppressWarnings( { "restriction", "deprecation" } )
public class LiferayUIPlugin extends AbstractUIPlugin implements IStartup
{

    public static final String FIRST_STARTUP_COMPLETE = "FIRST_STARTUP_COMPLETE";

    public static final String IMG_LIFERAY_ICON_SMALL = "IMG_LIFERAY_ICON_SMALL";

    // The plugin ID
    public static final String PLUGIN_ID = "com.liferay.ide.ui";

    // The shared instance
    private static LiferayUIPlugin plugin;

    public static void clearAllPersistentSettings() throws BackingStoreException
    {

        IEclipsePreferences tomcatCorePrefs = new InstanceScope().getNode( "com.liferay.ide.server.tomcat.core" );

        tomcatCorePrefs.remove( "ADDED_EXT_PLUGIN_TOGGLE_KEY" );
        tomcatCorePrefs.remove( "ADDED_EXT_PLUGIN_WITHOUT_ZIP_TOGGLE_KEY" );
        tomcatCorePrefs.remove( "REMOVE_EXT_PLUGIN_TOGGLE_KEY" );

        tomcatCorePrefs.flush();
    }

    public static IStatus createErrorStatus( String string )
    {
        return new Status( IStatus.ERROR, PLUGIN_ID, string );
    }
    
    public static void logError( String msg, Exception e )
    {
        getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, msg, e));
    }

    public static IWorkbenchPage getActivePage()
    {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static LiferayUIPlugin getDefault()
    {
        return plugin;
    }

    @SuppressWarnings( "rawtypes" )
    public static Map getLiferaySettings()
    {
        final Map options = new DefaultCodeFormatterOptions( LiferayDefaultCodeFormatterSettings.settings ).getMap();
        ProfileVersioner.setLatestCompliance( options );
        return options;
    }

    public static IWorkspace getWorkspace()
    {
        return ResourcesPlugin.getWorkspace();
    }

    public static void logError(Exception e)
    {
        logError( e.getMessage(), e );
    }

    protected TextFileDocumentProvider fTextFileDocumentProvider;

    protected Map<String, ImageDescriptor> imageDescriptors = new HashMap<String, ImageDescriptor>();

    /**
     * The constructor
     */
    public LiferayUIPlugin()
    {
    }

    public void earlyStartup()
    {
        if( isFirstStartup() )
        {
            installLiferayFormatterProfile();

            firstStartupComplete();
        }
    }

    public Image getImage( String key )
    {
        return getImageRegistry().get( key );
    }

    public ImageDescriptor getImageDescriptor( String key )
    {
        getImageRegistry();

        return imageDescriptors.get( key );
    }

    public synchronized IDocumentProvider getTextFileDocumentProvider()
    {
        if( fTextFileDocumentProvider == null )
        {
            fTextFileDocumentProvider = new TextFileDocumentProvider();
        }

        return fTextFileDocumentProvider;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
     */
    public void start( BundleContext context ) throws Exception
    {

        super.start( context );

        plugin = this;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
     */
    public void stop( BundleContext context ) throws Exception
    {

        plugin = null;

        super.stop( context );
    }

    private void firstStartupComplete()
    {
        getPreferences().putBoolean( FIRST_STARTUP_COMPLETE, true );
    }

    private IEclipsePreferences getPreferences()
    {
        return new InstanceScope().getNode( PLUGIN_ID );
    }

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    private void installLiferayFormatterProfile()
    {
        PreferencesAccess access = PreferencesAccess.getOriginalPreferences();
        ProfileVersioner profileVersioner = new ProfileVersioner();
        IScopeContext instanceScope = access.getInstanceScope();
        try
        {
            FormatterProfileStore store = new FormatterProfileStore( profileVersioner );
            List profiles = store.readProfiles( instanceScope );
            if( profiles == null )
            {
                profiles = new ArrayList();
            }

            // add liferay profile
            final Profile eclipseProfile =
                new CustomProfile(
                    "Liferay [plug-in]", getLiferaySettings(), profileVersioner.getCurrentVersion(),
                    profileVersioner.getProfileKind() );
            profiles.add( eclipseProfile );

            store.writeProfiles( profiles, instanceScope );
            // ProfileManager manager = new FormatterProfileManager(profiles, instanceScope, access, profileVersioner);
            // manager.setSelected(eclipseProfile);
            // manager.commitChanges(instanceScope);
            instanceScope.getNode( JavaUI.ID_PLUGIN ).flush();
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                instanceScope.getNode( JavaCore.PLUGIN_ID ).flush();
            }
            catch( BackingStoreException e )
            {
            }
        }
    }

    private boolean isFirstStartup()
    {
        IScopeContext[] scopes = new IScopeContext[] { new InstanceScope() };

        return !( Platform.getPreferencesService().getBoolean( PLUGIN_ID, FIRST_STARTUP_COMPLETE, false, scopes ) );
    }

    @Override
    protected void initializeImageRegistry( ImageRegistry reg )
    {
        registerImage( reg, IMG_LIFERAY_ICON_SMALL, "/icons/e16/liferay.png" );
    }

    protected void registerImage( ImageRegistry registry, String key, String path )
    {
        try
        {
            ImageDescriptor id = ImageDescriptor.createFromURL( getBundle().getEntry( path ) );

            imageDescriptors.put( key, id );

            registry.put( key, id );
        }
        catch( Exception e )
        {
        }
    }

    // public synchronized IDocumentProvider
    // getPluginPropertiesFileDocumentProvider() {
    // if (fPluginPropertiesFileDocumentProvider == null) {
    // fPluginPropertiesFileDocumentProvider = new
    // PluginPropertiesFileDocumentProvider();
    // }
    // return fPluginPropertiesFileDocumentProvider;
    // }
}
