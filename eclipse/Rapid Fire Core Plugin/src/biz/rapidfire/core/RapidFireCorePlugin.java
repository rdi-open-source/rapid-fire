/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core;

import java.net.URL;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import biz.rapidfire.core.plugin.AbstractExtendedUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class RapidFireCorePlugin extends AbstractExtendedUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "biz.rapidfire.core"; //$NON-NLS-1$

    // Minimal required version of Rapid Fire library
    private static final String MIN_SERVER_VERSION = "5.0.20"; //$NON-NLS-1$

    // The shared instance
    private static RapidFireCorePlugin plugin;

    // URL, where the plug-in is installed
    private static URL installURL;

    // Contributor logos
    public static final String IMAGE_RAPIDFIRE = "rapidfire.gif"; //$NON-NLS-1$
    public static final String IMAGE_TASKFORCE = "TaskForce.png";
    public static final String IMAGE_TOOLS400 = "Tools400.bmp";

    public static final String IMAGE_ERROR = "error.gif"; //$NON-NLS-1$
    public static final String IMAGE_REFRESH = "refresh.gif"; //$NON-NLS-1$
    public static final String IMAGE_AUTO_REFRESH_OFF = "auto_refresh_off.gif"; //$NON-NLS-1$
    public static final String IMAGE_LIBRARY = "library.gif"; //$NON-NLS-1$
    public static final String IMAGE_PROGRAM = "program.gif"; //$NON-NLS-1$

    public static final String IMAGE_DISABLED = "disabled.gif"; //$NON-NLS-1$
    public static final String IMAGE_ENABLED = "enabled.gif"; //$NON-NLS-1$

    public static final String IMAGE_TRANSFER_LIBRARY = "transfer_library.gif";
    public static final String IMAGE_REAPPLY_CHANGES = "reapply_changes.gif";

    public static final String COLOR_PROGRESS_BAR_FOREGROUND = "COLOR_PROGRESS_BAR_FOREGROUND"; //$NON-NLS-1$
    public static final String COLOR_PROGRESS_BAR_BACKGROUND = "COLOR_PROGRESS_BAR_BACKGROUND"; //$NON-NLS-1$

    public static final String COLOR_DIALOG_MODE_FOREGROUND = "COLOR_DIALOG_MODE_FOREGROUND"; //$NON-NLS-1$
    public static final String COLOR_DIALOG_MODE_CREATE = "COLOR_DIALOG_MODE_CREATE"; //$NON-NLS-1$
    public static final String COLOR_DIALOG_MODE_COPY = "COLOR_DIALOG_MODE_COPY"; //$NON-NLS-1$
    public static final String COLOR_DIALOG_MODE_CHANGE = "COLOR_DIALOG_MODE_CHANGE"; //$NON-NLS-1$
    public static final String COLOR_DIALOG_MODE_DELETE = "COLOR_DIALOG_MODE_DELETE"; //$NON-NLS-1$
    public static final String COLOR_DIALOG_MODE_DISPLAY = "COLOR_DIALOG_MODE_DISPLAY"; //$NON-NLS-1$

    public static final String OVERLAY_ERROR = "error_ovr.gif"; //$NON-NLS-1$
    public static final String OVERLAY_YELLOW_CIRCLE = "yellow_circle_ovr.gif"; //$NON-NLS-1$
    public static final String OVERLAY_GREEN_CIRCLE = "green_circle_ovr.gif"; //$NON-NLS-1$
    public static final String OVERLAY_ORANGE_CIRCLE = "orange_circle_ovr.gif"; //$NON-NLS-1$

    /**
     * The constructor
     */
    public RapidFireCorePlugin() {
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
        installURL = context.getBundle().getEntry("/");
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static RapidFireCorePlugin getDefault() {
        return plugin;
    }

    /**
     * Returns the version of the plugin, as assigned to "Bundle-Version" in
     * "MANIFEST.MF".
     * 
     * @return Version of the plugin.
     */
    public String getVersion() {
        String version = (String)getBundle().getHeaders().get(Constants.BUNDLE_VERSION);
        if (version == null) {
            version = "0.0.0";
        }
        return version;
    }

    /**
     * Returns the version of the plugin, as assigned to "Bundle-Version" in
     * "MANIFEST.MF" formatted as "vvrrmm".
     * 
     * @return Version of the plugin.
     */
    public String getMinServerVersion() {
        return MIN_SERVER_VERSION;
    }

    public static URL getInstallURL() {
        return installURL;
    }

    @Override
    protected void initializeImageRegistry(ImageRegistry reg) {
        super.initializeImageRegistry(reg);

        reg.put(IMAGE_RAPIDFIRE, getImageDescriptor(IMAGE_RAPIDFIRE));
        reg.put(IMAGE_TASKFORCE, getImageDescriptor(IMAGE_TASKFORCE));
        reg.put(IMAGE_TOOLS400, getImageDescriptor(IMAGE_TOOLS400));
        reg.put(IMAGE_ERROR, getImageDescriptor(IMAGE_ERROR));
        reg.put(IMAGE_REFRESH, getImageDescriptor(IMAGE_REFRESH));
        reg.put(IMAGE_AUTO_REFRESH_OFF, getImageDescriptor(IMAGE_AUTO_REFRESH_OFF));
        reg.put(IMAGE_LIBRARY, getImageDescriptor(IMAGE_LIBRARY));
        reg.put(IMAGE_PROGRAM, getImageDescriptor(IMAGE_PROGRAM));
        reg.put(IMAGE_ENABLED, getImageDescriptor(IMAGE_ENABLED));
        reg.put(IMAGE_DISABLED, getImageDescriptor(IMAGE_DISABLED));
        reg.put(IMAGE_TRANSFER_LIBRARY, getImageDescriptor(IMAGE_TRANSFER_LIBRARY));
        reg.put(IMAGE_REAPPLY_CHANGES, getImageDescriptor(IMAGE_REAPPLY_CHANGES));

        reg.put(OVERLAY_ERROR, getImageDescriptor(OVERLAY_ERROR));
        reg.put(OVERLAY_YELLOW_CIRCLE, getImageDescriptor(OVERLAY_YELLOW_CIRCLE));
        reg.put(OVERLAY_GREEN_CIRCLE, getImageDescriptor(OVERLAY_GREEN_CIRCLE));
        reg.put(OVERLAY_ORANGE_CIRCLE, getImageDescriptor(OVERLAY_ORANGE_CIRCLE));
    }

    @Override
    protected void initializeColorRegistry(ColorRegistry reg) {
        super.initializeColorRegistry(reg);

        reg.put(COLOR_PROGRESS_BAR_FOREGROUND, new RGB(100, 230, 80));
        reg.put(COLOR_PROGRESS_BAR_BACKGROUND, new RGB(220, 220, 220));

        reg.put(COLOR_DIALOG_MODE_FOREGROUND, Display.getCurrent().getSystemColor(SWT.COLOR_WHITE).getRGB());
        reg.put(COLOR_DIALOG_MODE_CREATE, Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY).getRGB());
        reg.put(COLOR_DIALOG_MODE_COPY, Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY).getRGB());
        reg.put(COLOR_DIALOG_MODE_CHANGE, Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY).getRGB());
        reg.put(COLOR_DIALOG_MODE_DELETE, Display.getCurrent().getSystemColor(SWT.COLOR_RED).getRGB());
        reg.put(COLOR_DIALOG_MODE_DISPLAY, Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY).getRGB());
    }

    /**
     * Convenience method to log error messages to the application log.
     * 
     * @param message Message
     * @param e The exception that has produced the error
     */
    public static void logError(String message, Throwable e) {
        if (plugin == null) {
            System.err.println(message);
            if (e != null) {
                e.printStackTrace();
            }
            return;
        }
        plugin.getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.ERROR, message, e));
    }

}
