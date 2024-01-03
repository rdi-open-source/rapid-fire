/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.jface.dialogs;

import org.eclipse.jface.dialogs.IDialogSettings;

import biz.rapidfire.core.helpers.BooleanHelper;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.helpers.StringHelper;

public class DialogSettingsManager {

    private IDialogSettings dialogSettings = null;
    private Class<?> section;

    public DialogSettingsManager(IDialogSettings aDialogSettings) {
        this(aDialogSettings, null);
    }

    public DialogSettingsManager(IDialogSettings aDialogSettings, Class<?> section) {
        this.dialogSettings = aDialogSettings;
        this.section = section;
    }

    /**
     * Retrieves the screen value that was last displayed on the dialog.
     * 
     * @param aKey - key, that is used to retrieve the value from the store
     * @param aDefault - default value, that is returned if then key does not
     *        yet exist
     * @return the screen value that was last shown
     */
    public String loadValue(String aKey, String aDefault) {
        String tValue = getDialogSettings().get(aKey);
        if (StringHelper.isNullOrEmpty(tValue)) {
            tValue = aDefault;
        }
        return tValue;
    }

    /**
     * Stores a given string value to preserve it for the next time the dialog
     * is shown.
     * 
     * @param aKey - key, the value is assigned to
     * @param aValue - the screen value that is stored
     */
    public void storeValue(String aKey, String aValue) {
        getDialogSettings().put(aKey, aValue);
    }

    /**
     * Retrieves the screen value that was last displayed on the dialog.
     * 
     * @param aKey - key, that is used to retrieve the value from the store
     * @param aDefault - default value, that is returned if then key does not
     *        yet exist
     * @return the screen value that was last shown
     */
    public boolean loadBooleanValue(String aKey, boolean aDefault) {
        String tValue = getDialogSettings().get(aKey);
        return BooleanHelper.tryParseBoolean(tValue, aDefault);
    }

    /**
     * Stores a given boolean value to preserve it for the next time the dialog
     * is shown.
     * 
     * @param aKey - key, the value is assigned to
     * @param aValue - the screen value that is stored
     */
    public void storeValue(String aKey, boolean aValue) {
        getDialogSettings().put(aKey, aValue);
    }

    /**
     * Retrieves the the value that is assigned to a given key.
     * 
     * @param aKey - key, that is used to retrieve the value from the store
     * @param aDefault - default value, that is returned if then key does not
     *        yet exist
     * @return the value that is assigned to the key
     */
    public int loadIntValue(String aKey, int aDefault) {
        return IntHelper.tryParseInt(getDialogSettings().get(aKey), aDefault);
    }

    /**
     * Stores a given integer value to preserve it for the next time the dialog
     * is shown.
     * 
     * @param aKey - key, the value is assigned to
     * @param aValue - the screen value that is stored
     */
    public void storeValue(String aKey, int aValue) {
        getDialogSettings().put(aKey, aValue);
    }

    /**
     * Returns the dialog settings store.
     * 
     * @return dialog settings
     */
    private IDialogSettings getDialogSettings() {

        if (section == null) {
            return dialogSettings;
        }

        String sectionName = section.getName();
        IDialogSettings dialogSectionSettings = dialogSettings.getSection(sectionName);
        if (dialogSectionSettings == null) {
            dialogSectionSettings = dialogSettings.addNewSection(sectionName);
        }

        return dialogSectionSettings;
    }

}
