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

    private static final String LIBRARY = DOMAIN + "LIBRARY.";
    private static final String LIBRARY_HOST_NAME = LIBRARY + "HOST_NAME"; //$NON-NLS-1$
    private static final String LIBRARY_FTP_PORT_NUMBER = LIBRARY + "FTP_PORT_NUMBER"; //$NON-NLS-1$
    private static final String LIBRARY_RAPID_FIRE_LIBRARY = LIBRARY + "LIBRARY"; //$NON-NLS-1$
    private static final String LIBRARY_ASP_GROUP = LIBRARY + "ASP_GROUP"; //$NON-NLS-1$

    private static final String APPEARANCE = DOMAIN + "APPEARANCE.";
    public static final String APPEARANCE_PROGRESS_BAR_SIZE = APPEARANCE + "PROGRESS_BAR_SIZE"; //$NON-NLS-1$
    private static final String APPEARANCE_DATE_FORMAT = APPEARANCE + "DATE_FORMAT"; //$NON-NLS-1$
    private static final String APPEARANCE_TIME_FORMAT = APPEARANCE + "TIME_FORMAT"; //$NON-NLS-1$
    private static final String APPEARANCE_IS_ACTION_CACHE_ENABLED = APPEARANCE + "IS_ACTION_CACHE_ENABLED"; //$NON-NLS-1$

    private static final String WIZARD = DOMAIN + "WIZARD.";
    private static final String WIZARD_CONNECTION = WIZARD + "CONNECTION"; //$NON-NLS-1$
    private static final String WIZARD_RAPID_FIRE_LIBRARY = WIZARD + "RAPID_FIRE_LIBRARY"; //$NON-NLS-1$
    private static final String WIZARD_SKIP_DISABLED_PAGES = WIZARD + "SKIP_DISABLED_PAGES"; //$NON-NLS-1$

    private static final String GENERATOR = DOMAIN + "GENERATOR.";
    private static final String GENERATOR_OPEN_MEMBER = GENERATOR + "OPEN_MEMBER"; //$NON-NLS-1$
    private static final String GENERATOR_LIBRARY = GENERATOR + "LIBRARY"; //$NON-NLS-1$
    private static final String GENERATOR_SHADOW_LIBRARY = GENERATOR + "SHADOW_LIBRARY"; //$NON-NLS-1$
    private static final String GENERATOR_CONVERSION_PROGRAM = GENERATOR + "CONVERSION_PROGRAM"; //$NON-NLS-1$
    private static final String GENERATOR_CONVERSION_PROGRAM_LIBRARY = GENERATOR + "CONVERSION_PROGRAM_LIBRARY"; //$NON-NLS-1$

    private static final String CONNECTION = DOMAIN + "CONNECTION.";
    private static final String CONNECTION_IS_SLOW = CONNECTION + "IS_SLOW"; //$NON-NLS-1$

    private static final String INSTALL = DOMAIN + "INSTALL.";
    private static final String INSTALL_IS_START_JOURNALING = INSTALL + "IS_START_JOURNALING"; //$NON-NLS-1$

    private static final String DATE_FORMAT_LOCALE = "*LOCALE"; //$NON-NLS-1$
    private static final String TIME_FORMAT_LOCALE = "*LOCALE"; //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$

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
        return preferenceStore.getString(LIBRARY_HOST_NAME);
    }

    public int getFtpPortNumber() {
        return preferenceStore.getInt(LIBRARY_FTP_PORT_NUMBER);
    }

    public String getRapidFireLibrary() {
        return preferenceStore.getString(LIBRARY_RAPID_FIRE_LIBRARY);
    }

    public String getASPGroup() {
        return preferenceStore.getString(LIBRARY_ASP_GROUP);
    }

    public boolean isLargeProgressBar() {
        return preferenceStore.getBoolean(APPEARANCE_PROGRESS_BAR_SIZE);
    }

    public String getDateFormatLabel() {
        return preferenceStore.getString(APPEARANCE_DATE_FORMAT);
    }

    public String getTimeFormatLabel() {
        return preferenceStore.getString(APPEARANCE_TIME_FORMAT);
    }

    public boolean isActionCacheEnabled() {
        return preferenceStore.getBoolean(APPEARANCE_IS_ACTION_CACHE_ENABLED);
    }

    public String getWizardConnection() {
        return preferenceStore.getString(WIZARD_CONNECTION);
    }

    public String getWizardRapidFireLibrary() {
        return preferenceStore.getString(WIZARD_RAPID_FIRE_LIBRARY);
    }

    public boolean skipDisabledWizardPages() {
        return preferenceStore.getBoolean(WIZARD_SKIP_DISABLED_PAGES);
    }

    public boolean isOpenGeneratedCopyProgram() {
        return preferenceStore.getBoolean(GENERATOR_OPEN_MEMBER);
    }

    public String getGeneratorLibrary() {
        return preferenceStore.getString(GENERATOR_LIBRARY);
    }

    public String getGeneratorShadowLibrary() {
        return preferenceStore.getString(GENERATOR_SHADOW_LIBRARY);
    }

    public String getGeneratorConversionProgram() {
        return preferenceStore.getString(GENERATOR_CONVERSION_PROGRAM);
    }

    public String getGeneratorConversionProgramLibrary() {
        return preferenceStore.getString(GENERATOR_CONVERSION_PROGRAM_LIBRARY);
    }

    public boolean isSlowConnection() {
        return preferenceStore.getBoolean(CONNECTION_IS_SLOW);
    }

    public boolean isStartJournaling() {
        return preferenceStore.getBoolean(INSTALL_IS_START_JOURNALING);
    }

    /*
     * Preferences: SETTER
     */

    public void setConnectionName(String aHostName) {
        preferenceStore.setValue(LIBRARY_HOST_NAME, aHostName);
    }

    public void setFtpPortNumber(int aPortNumber) {
        preferenceStore.setValue(LIBRARY_FTP_PORT_NUMBER, aPortNumber);
    }

    public void setRapidFireLibrary(String aLibrary) {
        preferenceStore.setValue(LIBRARY_RAPID_FIRE_LIBRARY, aLibrary.trim());
    }

    public void setASPGroup(String aASPGroup) {
        preferenceStore.setValue(LIBRARY_ASP_GROUP, aASPGroup.trim());
    }

    public void setLargeProgressBar(boolean isLarge) {
        preferenceStore.setValue(APPEARANCE_PROGRESS_BAR_SIZE, isLarge);
    }

    public void setDateFormatLabel(String dateFormatLabel) {
        preferenceStore.setValue(APPEARANCE_DATE_FORMAT, dateFormatLabel);
    }

    public void setTimeFormatLabel(String dateFormatLabel) {
        preferenceStore.setValue(APPEARANCE_TIME_FORMAT, dateFormatLabel);
    }

    public void setActionCacheEnabled(boolean enabled) {
        preferenceStore.setValue(APPEARANCE_IS_ACTION_CACHE_ENABLED, enabled);
    }

    public void setWizardConnection(String connectionName) {
        preferenceStore.setValue(WIZARD_CONNECTION, connectionName);
    }

    public void setWizardRapidFireLibrary(String libraryName) {
        preferenceStore.setValue(WIZARD_RAPID_FIRE_LIBRARY, libraryName);
    }

    public void setSkipDisabledWizardPages(boolean skip) {
        preferenceStore.setValue(WIZARD_SKIP_DISABLED_PAGES, skip);
    }

    public void setOpenGeneratedCopyProgram(boolean openMember) {
        preferenceStore.setValue(GENERATOR_OPEN_MEMBER, openMember);
    }

    public void setGeneratorLibrary(String library) {
        preferenceStore.setValue(GENERATOR_LIBRARY, library);
    }

    public void setGeneratorShadowLibrary(String shadowLibrary) {
        preferenceStore.setValue(GENERATOR_SHADOW_LIBRARY, shadowLibrary);
    }

    public void setGeneratorConversionProgram(String conversionProgram) {
        preferenceStore.setValue(GENERATOR_CONVERSION_PROGRAM, conversionProgram);
    }

    public void setGeneratorConversionProgramLibrary(String conversionProgramLibrary) {
        preferenceStore.setValue(GENERATOR_CONVERSION_PROGRAM_LIBRARY, conversionProgramLibrary);
    }

    public void setSlowConnection(boolean isSlow) {
        preferenceStore.setValue(CONNECTION_IS_SLOW, isSlow);
    }

    public void setStartJournaling(boolean isStartJournaling) {
        preferenceStore.setValue(INSTALL_IS_START_JOURNALING, isStartJournaling);
    }

    /*
     * Preferences: Default Initializer
     */

    public void initializeDefaultPreferences() {

        preferenceStore.setDefault(LIBRARY_HOST_NAME, getDefaultHostName());
        preferenceStore.setDefault(LIBRARY_FTP_PORT_NUMBER, getDefaultFtpPortNumber());
        preferenceStore.setDefault(LIBRARY_RAPID_FIRE_LIBRARY, getDefaultRapidFireLibrary());
        preferenceStore.setDefault(LIBRARY_ASP_GROUP, getDefaultASPGroup());
        preferenceStore.setDefault(APPEARANCE_PROGRESS_BAR_SIZE, getDefaultIsLargeProgressBar());
        preferenceStore.setDefault(APPEARANCE_DATE_FORMAT, getDefaultDateFormatLabel());
        preferenceStore.setDefault(APPEARANCE_TIME_FORMAT, getDefaultTimeFormatLabel());
        preferenceStore.setDefault(APPEARANCE_IS_ACTION_CACHE_ENABLED, getDefaultIsActionCacheEnabled());

        preferenceStore.setDefault(WIZARD_CONNECTION, getDefaultWizardConnection());
        preferenceStore.setDefault(WIZARD_RAPID_FIRE_LIBRARY, getDefaultWizardRapidFireLibrary());
        preferenceStore.setDefault(WIZARD_SKIP_DISABLED_PAGES, getDefaultSkipDisabledWizardPages());

        preferenceStore.setDefault(GENERATOR_OPEN_MEMBER, getDefaultOpenGeneratedCopyProgram());
        preferenceStore.setDefault(GENERATOR_LIBRARY, getDefaultGeneratorLibrary());
        preferenceStore.setDefault(GENERATOR_SHADOW_LIBRARY, getDefaultGeneratorShadowLibrary());
        preferenceStore.setDefault(GENERATOR_CONVERSION_PROGRAM, getDefaultGeneratorConversionProgram());
        preferenceStore.setDefault(GENERATOR_CONVERSION_PROGRAM_LIBRARY, getDefaultGeneratorConversionProgramLibrary());

        preferenceStore.setDefault(CONNECTION_IS_SLOW, getDefaultIsSlowConnection());

        preferenceStore.setDefault(INSTALL_IS_START_JOURNALING, getDefaultIsStartJournaling());
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

    /**
     * Returns the default asp group.
     * 
     * @return default asp group
     */
    public String getDefaultASPGroup() {
        return "*NONE";
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

    public boolean getDefaultIsActionCacheEnabled() {
        return true;
    }

    public String getDefaultWizardConnection() {
        return "";
    }

    public String getDefaultWizardRapidFireLibrary() {
        return getDefaultRapidFireLibrary();
    }

    public String getDefaultWizardASPGroup() {
        return getDefaultASPGroup();
    }

    public boolean getDefaultSkipDisabledWizardPages() {
        return false;
    }

    private boolean getDefaultOpenGeneratedCopyProgram() {
        return true;
    }

    private String getDefaultGeneratorLibrary() {
        return EMPTY_STRING;
    }

    private String getDefaultGeneratorShadowLibrary() {
        return EMPTY_STRING;
    }

    private String getDefaultGeneratorConversionProgram() {
        return EMPTY_STRING;
    }

    private String getDefaultGeneratorConversionProgramLibrary() {
        return EMPTY_STRING;
    }

    public boolean getDefaultIsSlowConnection() {
        return false;
    }

    public boolean getDefaultIsStartJournaling() {
        return true;
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
        dateFormats.put("iso (yyyy.mm.dd)", "yyyy.MM.dd");

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
        timeFormats.put("iso (hh.mm.ss)", "HH.mm.ss");

        return timeFormats;
    }
}