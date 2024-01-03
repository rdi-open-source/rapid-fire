/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.plugin;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class AbstractExtendedUIPlugin extends AbstractUIPlugin {

    private static URL installURL;

    /**
     * The registry for all colors; <code>null</code> if not yet initialized.
     */
    private ColorRegistry colorRegistry = null;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);

        installURL = context.getBundle().getEntry("/"); //$NON-NLS-1$
    }

    /**
     * Returns the color registry for this UI plug-in.
     * <p>
     * The color registry contains the images used by this plug-in that are very
     * frequently used and so need to be globally shared within the plug-in.
     * Since many OSs have a severe limit on the number of colors that can be in
     * memory at any given time, a plug-in should only keep a small number of
     * colors in their registry.
     * <p>
     * Subclasses should reimplement <code>initializeColorRegistry</code> if
     * they have custom colors to load.
     * </p>
     * <p>
     * Subclasses may override this method but are not expected to.
     * </p>
     * 
     * @return the image registry
     */
    public ColorRegistry getColorRegistry() {
        if (colorRegistry == null) {
            colorRegistry = createColorRegistry();
            initializeColorRegistry(colorRegistry);
        }
        return colorRegistry;
    }

    public Color getColor(String symbolicName) {
        return getColorRegistry().get(symbolicName);
    }

    /**
     * Returns a new color registry for this plugin-in. The registry will be
     * used to manage colors which are frequently used by the plugin-in.
     * <p>
     * The default implementation of this method creates an empty registry.
     * Subclasses may override this method if needed.
     * </p>
     * 
     * @return ColorRegistry the resulting registry.
     * @see #getColorRegistry
     */
    protected ColorRegistry createColorRegistry() {

        // If we are in the UI Thread use that
        if (Display.getCurrent() != null) {
            return new ColorRegistry(Display.getCurrent());
        }

        if (PlatformUI.isWorkbenchRunning()) {
            return new ColorRegistry(PlatformUI.getWorkbench().getDisplay());
        }

        // Invalid thread access if it is not the UI Thread
        // and the workbench is not created.
        throw new SWTError(SWT.ERROR_THREAD_INVALID_ACCESS);
    }

    /**
     * Initializes an color registry with colors which are frequently used by
     * the plugin.
     * <p>
     * The color registry contains the colors used by this plug-in that are very
     * frequently used and so need to be globally shared within the plug-in.
     * Since many OSs have a severe limit on the number of color that can be in
     * memory at any given time, each plug-in should only keep a small number of
     * color in its registry.
     * </p>
     * <p>
     * Subclasses may override this method to fill the color registry.
     * </p>
     * 
     * @param reg the registry to initialize
     * @see #getColorRegistry
     */
    protected void initializeColorRegistry(ColorRegistry reg) {
        // spec'ed to do nothing
    }

    public Image getImage(String symbolicName) {
        return getImageRegistry().get(symbolicName);
    }

    public ImageDescriptor getImageDescriptor(String name) {

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
