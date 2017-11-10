/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import biz.rapidfire.core.RapidFireCorePlugin;

/**
 * Class to manage access to the preferences of the plugin.
 * <p>
 * Eclipse stores the preferences as <i>diffs</i> to their default values in
 * directory
 * <code>[workspace]\.metadata\.plugins\org.eclipse.core.runtime\.settings\</code>.
 * 
 * @author Thomas Raddatz
 */
public final class Preferences {

    /**
     * The instance of this Singleton class.
     */
    private static Preferences instance;

    /**
     * Global preferences of the plugin.
     */
    private IPreferenceStore preferenceStore;

    /*
     * Preferences keys:
     */

    private static final String DOMAIN = RapidFireCorePlugin.PLUGIN_ID + "."; //$NON-NLS-1$

    public static final String PROGRESS_BAR_SIZE = DOMAIN + "PROGRESS_BAR_SIZE"; //$NON-NLS-1$

    /**
     * Private constructor to ensure the Singleton pattern.
     */
    private Preferences() {
    }

    /**
     * Thread-safe method that returns the instance of this Singleton class.
     */
    public synchronized static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
            instance.preferenceStore = RapidFireCorePlugin.getDefault().getPreferenceStore();
        }
        return instance;
    }

    /**
     * Thread-safe method that disposes the instance of this Singleton class.
     * <p>
     * This method is intended to be call from
     * {@link org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)}
     * to free the reference to itself.
     */
    public static void dispose() {
        if (instance != null) {
            instance = null;
        }
    }

    /*
     * Preferences: GETTER
     */

    public boolean isLargeProgressBar() {
        return preferenceStore.getBoolean(PROGRESS_BAR_SIZE);
    }

    /*
     * Preferences: SETTER
     */

    public void setLargeProgressBar(boolean isLarge) {
        preferenceStore.setValue(PROGRESS_BAR_SIZE, isLarge);
    }

    /*
     * Preferences: Default Initializer
     */

    public void initializeDefaultPreferences() {

        preferenceStore.setDefault(PROGRESS_BAR_SIZE, getDefaultIsLargeProgressBar());
    }

    /*
     * Preferences: Default Values
     */

    public boolean getDefaultIsLargeProgressBar() {

        return false;
    }

    public void registerPreferencesListener(IPropertyChangeListener listener) {
        preferenceStore.addPropertyChangeListener(listener);
    }

    public void removePreferencesListener(IPropertyChangeListener listener) {
        preferenceStore.removePropertyChangeListener(listener);
    }
}