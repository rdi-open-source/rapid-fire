/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.preferences;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.FastDateFormat;
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

    /**
     * List of date formats.
     */
    private Map<String, String> dateFormats;

    /**
     * List of time formats.
     */
    private Map<String, String> timeFormats;

    /*
     * Preferences keys:
     */

    private static final String DOMAIN = RapidFireCorePlugin.PLUGIN_ID + "."; //$NON-NLS-1$

    public static final String PROGRESS_BAR_SIZE = DOMAIN + "PROGRESS_BAR_SIZE"; //$NON-NLS-1$

    private static final String HOST_NAME = DOMAIN + "HOST_NAME"; //$NON-NLS-1$
    private static final String FTP_PORT_NUMBER = DOMAIN + "FTP_PORT_NUMBER"; //$NON-NLS-1$
    private static final String RAPID_FIRE_LIBRARY = DOMAIN + "LIBRARY"; //$NON-NLS-1$
    private static final String APPEARANCE_DATE_FORMAT = DOMAIN + "DATE_FORMAT"; //$NON-NLS-1$
    private static final String APPEARANCE_TIME_FORMAT = DOMAIN + "TIME_FORMAT"; //$NON-NLS-1$

    private static final String WIZARD = DOMAIN + "WIZARD.";
    private static final String WIZARD_CONNECTION = WIZARD + "CONNECTION"; //$NON-NLS-1$
    private static final String WIZARD_RAPID_FIRE_LIBRARY = WIZARD + "RAPID_FIRE_LIBRARY"; //$NON-NLS-1$

    private static final String DATE_FORMAT_LOCALE = "*LOCALE"; //$NON-NLS-1$
    private static final String TIME_FORMAT_LOCALE = "*LOCALE"; //$NON-NLS-1$

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

    public String getDateFormatLabel() {
        return preferenceStore.getString(APPEARANCE_DATE_FORMAT);
    }

    public String getTimeFormatLabel() {
        return preferenceStore.getString(APPEARANCE_TIME_FORMAT);
    }

    public String getWizardConnection() {
        return preferenceStore.getString(WIZARD_CONNECTION);
    }

    public String getWizardRapidFireLibrary() {
        return preferenceStore.getString(WIZARD_RAPID_FIRE_LIBRARY);
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

    public void setDateFormatLabel(String dateFormatLabel) {
        preferenceStore.setValue(APPEARANCE_DATE_FORMAT, dateFormatLabel);
    }

    public void setTimeFormatLabel(String dateFormatLabel) {
        preferenceStore.setValue(APPEARANCE_TIME_FORMAT, dateFormatLabel);
    }

    public void setWizardConnection(String connectionName) {
        preferenceStore.setValue(WIZARD_CONNECTION, connectionName);
    }

    public void setWizardRapidFireLibrary(String libraryName) {
        preferenceStore.setValue(WIZARD_RAPID_FIRE_LIBRARY, libraryName);
    }

    /*
     * Preferences: Default Initializer
     */

    public void initializeDefaultPreferences() {

        preferenceStore.setDefault(HOST_NAME, getDefaultHostName());
        preferenceStore.setDefault(FTP_PORT_NUMBER, getDefaultFtpPortNumber());
        preferenceStore.setDefault(RAPID_FIRE_LIBRARY, getDefaultRapidFireLibrary());
        preferenceStore.setDefault(PROGRESS_BAR_SIZE, getDefaultIsLargeProgressBar());
        preferenceStore.setDefault(APPEARANCE_DATE_FORMAT, getDefaultDateFormatLabel());
        preferenceStore.setDefault(APPEARANCE_TIME_FORMAT, getDefaultTimeFormatLabel());

        preferenceStore.setDefault(WIZARD_CONNECTION, getDefaultWizardConnection());
        preferenceStore.setDefault(WIZARD_RAPID_FIRE_LIBRARY, getDefaultWizardRapidFireLibrary());
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

    public String getDefaultDateFormatLabel() {
        return DATE_FORMAT_LOCALE;
    }

    public String getDefaultTimeFormatLabel() {
        return TIME_FORMAT_LOCALE;
    }

    public String getDefaultWizardConnection() {
        return "";
    }

    public String getDefaultWizardRapidFireLibrary() {
        return "RAPIDFIRE"; //$NON-NLS-1$
    }

    public void registerPreferencesListener(IPropertyChangeListener listener) {
        preferenceStore.addPropertyChangeListener(listener);
    }

    public void removePreferencesListener(IPropertyChangeListener listener) {
        preferenceStore.removePropertyChangeListener(listener);
    }

    public String[] getDateFormatLabels() {

        Set<String> formats = getDateFormatsMap().keySet();

        String[] dateFormats = formats.toArray(new String[formats.size()]);
        Arrays.sort(dateFormats);

        return dateFormats;
    }

    public SimpleDateFormat getDateFormatter() {

        String pattern = getDateFormatsMap().get(getDateFormatLabel());
        if (pattern == null) {
            pattern = getDateFormatsMap().get(getDefaultDateFormatLabel());
        }

        if (pattern == null) {
            return new SimpleDateFormat(FastDateFormat.getDateInstance(FastDateFormat.SHORT).getPattern());
        }

        return new SimpleDateFormat(pattern);
    }

    public SimpleDateFormat getTimeFormatter() {

        String pattern = getTimeFormatsMap().get(getTimeFormatLabel());
        if (pattern == null) {
            pattern = getTimeFormatsMap().get(getDefaultTimeFormatLabel());
        }

        if (pattern == null) {
            return new SimpleDateFormat(FastDateFormat.getTimeInstance(FastDateFormat.SHORT).getPattern());
        }

        return new SimpleDateFormat(pattern);
    }

    public String[] getTimeFormatLabels() {

        Set<String> formats = getTimeFormatsMap().keySet();

        String[] timeFormats = formats.toArray(new String[formats.size()]);
        Arrays.sort(timeFormats);

        return timeFormats;
    }

    private Map<String, String> getDateFormatsMap() {

        if (dateFormats != null) {
            return dateFormats;
        }

        dateFormats = new HashMap<String, String>();

        dateFormats.put(getDefaultDateFormatLabel(), null);
        dateFormats.put("de (dd.mm.yyyy)", "dd.MM.yyyy");
        dateFormats.put("us (mm/dd/yyyy)", "MM/dd/yyyy");

        return dateFormats;
    }

    private Map<String, String> getTimeFormatsMap() {

        if (timeFormats != null) {
            return timeFormats;
        }

        timeFormats = new HashMap<String, String>();

        timeFormats.put(getDefaultDateFormatLabel(), null);
        timeFormats.put("de (hh:mm:ss)", "HH:mm:ss"); //$NON-NLS-1$
        timeFormats.put("us (hh:mm:ss AM/PM)", "KK:mm:ss a"); //$NON-NLS-1$

        return timeFormats;
    }
}