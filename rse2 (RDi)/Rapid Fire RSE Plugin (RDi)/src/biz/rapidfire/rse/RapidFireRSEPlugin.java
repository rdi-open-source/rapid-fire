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

import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.rse.subsystem.RapidFireAdapterFactory;
import biz.rapidfire.rse.subsystem.RapidFireSubSystemConfigurationAdapterFactory;
import biz.rapidfire.rse.subsystem.resources.AreasNode;
import biz.rapidfire.rse.subsystem.resources.CommandsNode;
import biz.rapidfire.rse.subsystem.resources.ConversionsNode;
import biz.rapidfire.rse.subsystem.resources.FilesNode;
import biz.rapidfire.rse.subsystem.resources.LibrariesNode;
import biz.rapidfire.rse.subsystem.resources.LibraryListsNode;
import biz.rapidfire.rse.subsystem.resources.NotificationsNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireAreaResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireCommandResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireConversionResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireFileResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireJobResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireLibraryListResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireLibraryResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireNotificationResource;

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
    public static final String IMAGE_LOGICAL_FILE = "logical_file.gif"; //$NON-NLS-1$
    public static final String IMAGE_LIBRARY = "library.gif"; //$NON-NLS-1$
    public static final String IMAGE_LIBRARY_LIST = "library_list.gif"; //$NON-NLS-1$
    public static final String IMAGE_NOTIFICATION = "notification.gif"; //$NON-NLS-1$
    public static final String IMAGE_AREA = "area.gif"; //$NON-NLS-1$
    public static final String IMAGE_CONVERSION = "conversion.gif"; //$NON-NLS-1$
    public static final String IMAGE_COMMAND = "command.gif"; //$NON-NLS-1$
    public static final String IMAGE_MESSAGE_QUEUE = "message_queue.gif"; //$NON-NLS-1$
    public static final String IMAGE_USER = "user.gif"; //$NON-NLS-1$

    public static final String IMAGE_NEW_JOB = "new_job.gif"; //$NON-NLS-1$
    public static final String IMAGE_NEW_FILE = "new_file.gif"; //$NON-NLS-1$
    public static final String IMAGE_NEW_LIBRARY = "new_library.gif"; //$NON-NLS-1$
    public static final String IMAGE_NEW_LIBRARY_LIST = "new_library_list.gif"; //$NON-NLS-1$
    public static final String IMAGE_NEW_NOTIFICATION = "new_notification.gif"; //$NON-NLS-1$
    public static final String IMAGE_NEW_AREA = "new_area.gif"; //$NON-NLS-1$
    public static final String IMAGE_NEW_CONVERSION = "new_conversion.gif"; //$NON-NLS-1$
    public static final String IMAGE_NEW_COMMAND = "new_command.gif"; //$NON-NLS-1$

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
    @Override
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
    @Override
    public void stop(BundleContext context) throws Exception {
        JDBCConnectionManager.getInstance().destroy();
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
        manager.registerAdapters(adapterFactory, RapidFireLibraryListResource.class);
        manager.registerAdapters(adapterFactory, RapidFireLibraryResource.class);
        manager.registerAdapters(adapterFactory, RapidFireNotificationResource.class);
        manager.registerAdapters(adapterFactory, RapidFireAreaResource.class);
        manager.registerAdapters(adapterFactory, RapidFireConversionResource.class);
        manager.registerAdapters(adapterFactory, RapidFireCommandResource.class);

        manager.registerAdapters(adapterFactory, FilesNode.class);
        manager.registerAdapters(adapterFactory, LibraryListsNode.class);
        manager.registerAdapters(adapterFactory, LibrariesNode.class);
        manager.registerAdapters(adapterFactory, NotificationsNode.class);
        manager.registerAdapters(adapterFactory, AreasNode.class);
        manager.registerAdapters(adapterFactory, ConversionsNode.class);
        manager.registerAdapters(adapterFactory, CommandsNode.class);

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
        reg.put(IMAGE_LOGICAL_FILE, getImageDescriptor(IMAGE_LOGICAL_FILE));
        reg.put(IMAGE_LIBRARY_LIST, getImageDescriptor(IMAGE_LIBRARY_LIST));
        reg.put(IMAGE_LIBRARY, getImageDescriptor(IMAGE_LIBRARY));
        reg.put(IMAGE_NOTIFICATION, getImageDescriptor(IMAGE_NOTIFICATION));
        reg.put(IMAGE_AREA, getImageDescriptor(IMAGE_AREA));
        reg.put(IMAGE_CONVERSION, getImageDescriptor(IMAGE_CONVERSION));
        reg.put(IMAGE_COMMAND, getImageDescriptor(IMAGE_COMMAND));
        reg.put(IMAGE_MESSAGE_QUEUE, getImageDescriptor(IMAGE_MESSAGE_QUEUE));
        reg.put(IMAGE_USER, getImageDescriptor(IMAGE_USER));

        reg.put(IMAGE_NEW_JOB, getImageDescriptor(IMAGE_NEW_JOB));
        reg.put(IMAGE_NEW_FILE, getImageDescriptor(IMAGE_NEW_FILE));
        reg.put(IMAGE_NEW_LIBRARY_LIST, getImageDescriptor(IMAGE_NEW_LIBRARY_LIST));
        reg.put(IMAGE_NEW_LIBRARY, getImageDescriptor(IMAGE_NEW_LIBRARY));
        reg.put(IMAGE_NEW_NOTIFICATION, getImageDescriptor(IMAGE_NEW_NOTIFICATION));
        reg.put(IMAGE_NEW_AREA, getImageDescriptor(IMAGE_NEW_AREA));
        reg.put(IMAGE_NEW_CONVERSION, getImageDescriptor(IMAGE_NEW_CONVERSION));
        reg.put(IMAGE_NEW_COMMAND, getImageDescriptor(IMAGE_NEW_COMMAND));
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
