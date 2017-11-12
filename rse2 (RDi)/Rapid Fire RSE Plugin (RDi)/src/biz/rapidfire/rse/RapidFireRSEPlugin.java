/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import biz.rapidfire.rse.model.RapidFireFileResource;
import biz.rapidfire.rse.model.RapidFireJobResource;
import biz.rapidfire.rse.model.RapidFireLibraryResource;
import biz.rapidfire.rse.model.dao.DAOManager;
import biz.rapidfire.rse.subsystem.FilesNode;
import biz.rapidfire.rse.subsystem.LibrariesNode;
import biz.rapidfire.rse.subsystem.RapidFireAdapterFactory;
import biz.rapidfire.rse.subsystem.RapidFireSubSystemConfigurationAdapterFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class RapidFireRSEPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "biz.rapidfire.rse"; //$NON-NLS-1$

    // The shared instance
    private static RapidFireRSEPlugin plugin;
    private static URL installURL;

    public static final String IMAGE_RAPID_FIRE = "rapidfire.gif"; //$NON-NLS-1$
    public static final String IMAGE_RAPID_FIRE_FILTER = "rapidfire_filter.gif"; //$NON-NLS-1$
    public static final String IMAGE_RAPID_FIRE_JOB = "rapidfire_job.gif"; //$NON-NLS-1$
    public static final String IMAGE_FILE = "file.gif"; //$NON-NLS-1$
    public static final String IMAGE_LIBRARY = "library.gif"; //$NON-NLS-1$

    /**
     * The constructor
     */
    public RapidFireRSEPlugin() {
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);

        plugin = this;
        installURL = context.getBundle().getEntry("/"); //$NON-NLS-1$

        setupAdapters();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    public void stop(BundleContext context) throws Exception {
        DAOManager.getInstance().destroy();
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static RapidFireRSEPlugin getDefault() {
        return plugin;
    }

    private void setupAdapters() {

        IAdapterManager manager = Platform.getAdapterManager();

        RapidFireAdapterFactory adapterFactory = new RapidFireAdapterFactory();
        manager.registerAdapters(adapterFactory, RapidFireJobResource.class);
        manager.registerAdapters(adapterFactory, RapidFireFileResource.class);
        manager.registerAdapters(adapterFactory, RapidFireLibraryResource.class);

        manager.registerAdapters(adapterFactory, FilesNode.class);
        manager.registerAdapters(adapterFactory, LibrariesNode.class);

        RapidFireSubSystemConfigurationAdapterFactory subSystemConfigurationAdapterFactory = new RapidFireSubSystemConfigurationAdapterFactory();
        subSystemConfigurationAdapterFactory.registerWithManager(manager);
    }

    @Override
    protected void initializeImageRegistry(ImageRegistry reg) {
        super.initializeImageRegistry(reg);

        reg.put(IMAGE_RAPID_FIRE, getImageDescriptor(IMAGE_RAPID_FIRE));
        reg.put(IMAGE_RAPID_FIRE_FILTER, getImageDescriptor(IMAGE_RAPID_FIRE_FILTER));
        reg.put(IMAGE_RAPID_FIRE_JOB, getImageDescriptor(IMAGE_RAPID_FIRE_JOB));
        reg.put(IMAGE_FILE, getImageDescriptor(IMAGE_FILE));
        reg.put(IMAGE_LIBRARY, getImageDescriptor(IMAGE_LIBRARY));
    }

    private ImageDescriptor getImageDescriptor(String name) {
        String iconPath = "icons/"; //$NON-NLS-1$
        try {
            URL url = new URL(installURL, iconPath + name);
            return ImageDescriptor.createFromURL(url);
        } catch (MalformedURLException e) {
            // should not happen
            return ImageDescriptor.getMissingImageDescriptor();
        }
    }

}
