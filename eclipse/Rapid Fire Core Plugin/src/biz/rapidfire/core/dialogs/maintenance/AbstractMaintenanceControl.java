/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;

public abstract class AbstractMaintenanceControl extends Composite implements ModifyListener, SelectionListener {

    private static final String PREV_VALUE = "prev.value";
    private static final String DFT_VALUE = "dft.value";
    protected static final String EMPTY_STRING = "";

    private boolean isParentKeyFieldsVisible;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

    public AbstractMaintenanceControl(Composite parent, int style, boolean parentKeyFieldsVisible) {
        super(parent, style);

        this.isParentKeyFieldsVisible = parentKeyFieldsVisible;

        GridLayout layout = new GridLayout(2, false);
        setLayout(layout);

        createContent(this);
        setMode(MaintenanceMode.DISPLAY);
    }

    protected boolean isParentKeyFieldsVisible() {
        return isParentKeyFieldsVisible;
    }

    public void setMode(MaintenanceMode mode) {

        if (MaintenanceMode.CREATE.equals(mode) || MaintenanceMode.COPY.equals(mode)) {
            enableParentKeyFields = false;
            enableKeyFields = true;
            enableFields = true;
        } else if (MaintenanceMode.CHANGE.equals(mode)) {
            enableParentKeyFields = false;
            enableKeyFields = false;
            enableFields = true;
        } else {
            enableParentKeyFields = false;
            enableKeyFields = false;
            enableFields = false;
        }
    }

    protected boolean isParentKeyFieldsEnabled() {
        return enableParentKeyFields;
    }

    protected boolean isKeyFieldsEnabled() {
        return enableKeyFields;
    }

    protected boolean isFieldsEnabled() {
        return enableFields;
    }

    protected abstract void createContent(Composite parent);

    public void widgetDefaultSelected(SelectionEvent arg0) {
    }

    public void widgetSelected(SelectionEvent arg0) {
    }

    public void modifyText(ModifyEvent arg0) {
    }

    protected void setDefaultValue(Control control, String defaultValue) {
        control.setData(DFT_VALUE, defaultValue);
    }

    private String getDefaultValue(Control control) {
        String value = (String)control.getData(DFT_VALUE);
        if (value != null) {
            return value;
        }
        return EMPTY_STRING;
    }

    protected void setText(Text textControl, String text) {

        if (StringHelper.isNullOrEmpty(text)) {
            String defaultValue = getDefaultValue(textControl);
            if (StringHelper.isNullOrEmpty(defaultValue)) {
                textControl.setText(EMPTY_STRING);
            } else {
                textControl.setText(defaultValue);
            }
        } else {
            textControl.setText(text);
        }
    }

    protected void setText(Combo comboControl, String text) {

        if (StringHelper.isNullOrEmpty(text)) {
            String defaultValue = getDefaultValue(comboControl);
            if (StringHelper.isNullOrEmpty(defaultValue)) {
                comboControl.setText(EMPTY_STRING);
            } else {
                comboControl.setText(defaultValue);
            }
        } else {
            comboControl.setText(text);
        }
    }

    protected void saveCurrentValue(Text textControl) {
        storeCurrentValue(textControl, textControl.getText());
        textControl.setText(EMPTY_STRING);
    }

    protected void saveCurrentValue(Combo comboControl) {
        storeCurrentValue(comboControl, comboControl.getText());
        comboControl.setText(EMPTY_STRING);
    }

    protected void restorePreviousValue(Text control) {
        setText(control, getPreviousValue(control));
    }

    protected void restorePreviousValue(Combo control) {
        setText(control, getPreviousValue(control));
    }

    protected void setSelectedItem(Combo control, String item) {

        int i = findItem(control.getItems(), item);
        if (i >= 0) {
            control.select(i);
        } else {
            control.select(-1);
        }
    }

    protected int findItem(String[] items, String item) {

        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(item)) {
                    return i;
                }
            }
        }

        return -1;
    }

    private void storeCurrentValue(Control control, String text) {
        control.setData(PREV_VALUE, text);
    }

    private String getPreviousValue(Control control) {
        String text = (String)control.getData(PREV_VALUE);
        if (text == null) {
            return EMPTY_STRING;
        }
        return text;
    }
}
