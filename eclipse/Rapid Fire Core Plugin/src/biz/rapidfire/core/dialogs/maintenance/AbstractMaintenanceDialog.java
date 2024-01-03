/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public abstract class AbstractMaintenanceDialog extends XDialog {

    private static final String PREV_VALUE = "prev.value";
    private static final String DFT_VALUE = "dft.value";
    protected static final String EMPTY_STRING = "";

    private MaintenanceMode mode;
    private boolean isScrollable;
    private int defaultNumColumns;

    public AbstractMaintenanceDialog(Shell shell, MaintenanceMode mode) {
        super(shell);

        this.mode = mode;
        this.isScrollable = true;
        this.defaultNumColumns = 2;
    }

    protected boolean isDisplayMode() {
        return MaintenanceMode.DISPLAY.equals(getMode());
    }

    protected MaintenanceMode getMode() {
        return mode;
    }

    protected void setScrollable(boolean enabled) {
        this.isScrollable = enabled;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);

        newShell.setText(getDialogTitle());
    }

    @Override
    protected Control createDialogArea(Composite parent) {

        Composite container = (Composite)super.createDialogArea(parent);
        container.setLayout(new FillLayout());

        ScrolledComposite scrollable = null;
        Composite editorArea = null;
        if (isScrollable) {
            scrollable = new ScrolledComposite(container, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
            scrollable.setExpandHorizontal(true);
            scrollable.setExpandVertical(true);
            editorArea = new Composite(scrollable, SWT.NONE);
            editorArea.setLayoutData(new GridData(GridData.FILL_BOTH));
        } else {
            editorArea = new Composite(container, SWT.NONE);
        }

        editorArea.setLayout(new GridLayout(getNumColumns(), false));

        WidgetFactory.createDialogSubTitle(editorArea, getMode());

        createEditorAreaContent(editorArea);
        createStatusLine(editorArea);

        setScreenValues();

        editorArea.layout();

        if (scrollable != null) {
            scrollable.setContent(editorArea);
            scrollable.setMinSize(editorArea.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        }

        return container;
    }

    protected int getNumColumns() {
        return defaultNumColumns;
    }

    protected void setErrorMessage(String errorMessage, Result apiCallResult) {

        if (!StringHelper.isNullOrEmpty(apiCallResult.getMessage())) {
            setErrorMessage(errorMessage + " " + apiCallResult.getMessage()); //$NON-NLS-1$
        } else {
            setErrorMessage(errorMessage);
        }
    };

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

    protected abstract String getDialogTitle();

    protected abstract void createEditorAreaContent(Composite editorArea);

    protected abstract void setScreenValues();

    /**
     * Overridden to make this dialog resizable.
     */
    @Override
    protected boolean isResizable() {
        return true;
    }

    /**
     * Overridden to provide a default size to {@link XDialog}.
     */
    @Override
    protected Point getDefaultSize() {
        return getShell().computeSize(Size.getSize(510), SWT.DEFAULT, true);
    }

    /**
     * Overridden to let {@link XDialog} store the state of this dialog in a
     * separate section of the dialog settings file.
     */
    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        return super.getDialogBoundsSettings(RapidFireCorePlugin.getDefault().getDialogSettings());
    }
}
