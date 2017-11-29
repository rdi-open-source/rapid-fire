/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.preferences;

import java.text.SimpleDateFormat;

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

    private static final String HOST_NAME = DOMAIN + "HOST_NAME"; //$NON-NLS-1$
    private static final String FTP_PORT_NUMBER = DOMAIN + "FTP_PORT_NUMBER"; //$NON-NLS-1$
    private static final String RAPID_FIRE_LIBRARY = DOMAIN + "LIBRARY"; //$NON-NLS-1$

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

    public String getHostName() {
        return preferenceStore.getString(HOST_NAME);
    }

    public int getFtpPortNumber() {
        return preferenceStore.getInt(FTP_PORT_NUMBER);
    }

    public String getRapidFireLibrary() {
        return preferenceStore.getString(RAPID_FIRE_LIBRARY);
    }

    public boolean isLargeProgressBar() {
        return preferenceStore.getBoolean(PROGRESS_BAR_SIZE);
    }

    /*
     * Preferences: SETTER
     */

    public void setConnectionName(String aHostName) {
        preferenceStore.setValue(HOST_NAME, aHostName);
    }

    public void setFtpPortNumber(int aPortNumber) {
        preferenceStore.setValue(FTP_PORT_NUMBER, aPortNumber);
    }

    public void setRapidFireLibrary(String aLibrary) {
        preferenceStore.setValue(RAPID_FIRE_LIBRARY, aLibrary.trim());
    }

    public void setLargeProgressBar(boolean isLarge) {
        preferenceStore.setValue(PROGRESS_BAR_SIZE, isLarge);
    }

    /*
     * Preferences: Default Initializer
     */

    public void initializeDefaultPreferences() {

        preferenceStore.setDefault(HOST_NAME, getDefaultHostName());
        preferenceStore.setDefault(FTP_PORT_NUMBER, getDefaultFtpPortNumber());
        preferenceStore.setDefault(RAPID_FIRE_LIBRARY, getDefaultRapidFireLibrary());
        preferenceStore.setDefault(PROGRESS_BAR_SIZE, getDefaultIsLargeProgressBar());
    }

    /*
     * Preferences: Default Values
     */

    /**
     * Returns the default host name where to upload the Rapid Fire library.
     * 
     * @return default host name
     */
    public String getDefaultHostName() {
        return "";
    }

    /**
     * Returns the default FTP port number.
     * 
     * @return default FTPport number
     */
    public int getDefaultFtpPortNumber() {
        return 21;
    }

    /**
     * Returns the default Rapid Fire library name.
     * 
     * @return default Rapid Fire library name
     */
    public String getDefaultRapidFireLibrary() {
        return "RAPIDFIRE";
    }

    public boolean getDefaultIsLargeProgressBar() {

        return false;
    }

    public void registerPreferencesListener(IPropertyChangeListener listener) {
        preferenceStore.addPropertyChangeListener(listener);
    }

    public void removePreferencesListener(IPropertyChangeListener listener) {
        preferenceStore.removePropertyChangeListener(listener);
    }

    public SimpleDateFormat getDateFormatter() {
        String pattern = "dd.MM.yyyy";
        // String pattern = getDateFormatsMap().get(getDateFormatLabel());
        // if (pattern == null) {
        // pattern = getDateFormatsMap().get(getDefaultDateFormatLabel());
        // }
        //
        // if (pattern == null) {
        // return new
        // SimpleDateFormat(FastDateFormat.getDateInstance(FastDateFormat.SHORT).getPattern());
        // }

        return new SimpleDateFormat(pattern);
    }
}