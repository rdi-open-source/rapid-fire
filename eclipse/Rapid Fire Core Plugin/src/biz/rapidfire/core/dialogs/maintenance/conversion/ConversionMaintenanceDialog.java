/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.conversion;

import java.util.Arrays;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.conversion.ConversionManager;
import biz.rapidfire.core.maintenance.conversion.ConversionValues;
import biz.rapidfire.core.maintenance.conversion.IConversionCheck;

public class ConversionMaintenanceDialog extends AbstractMaintenanceDialog {

    private ConversionManager manager;

    private ConversionValues values;
    private ConversionMaintenanceControl conversionMaintenanceControl;

    private String[] fieldNames;

    public static ConversionMaintenanceDialog getCreateDialog(Shell shell, ConversionManager manager) {
        return new ConversionMaintenanceDialog(shell, MaintenanceMode.CREATE, manager);
    }

    public static ConversionMaintenanceDialog getCopyDialog(Shell shell, ConversionManager manager) {
        return new ConversionMaintenanceDialog(shell, MaintenanceMode.COPY, manager);
    }

    public static ConversionMaintenanceDialog getChangeDialog(Shell shell, ConversionManager manager) {
        return new ConversionMaintenanceDialog(shell, MaintenanceMode.CHANGE, manager);
    }

    public static ConversionMaintenanceDialog getDeleteDialog(Shell shell, ConversionManager manager) {
        return new ConversionMaintenanceDialog(shell, MaintenanceMode.DELETE, manager);
    }

    public static ConversionMaintenanceDialog getDisplayDialog(Shell shell, ConversionManager manager) {
        return new ConversionMaintenanceDialog(shell, MaintenanceMode.DISPLAY, manager);
    }

    public void setValue(ConversionValues values) {
        this.values = values;
    }

    public ConversionValues getValue() {
        return values;
    }

    public void setFields(String[] fieldNames) {

        this.fieldNames = fieldNames;

        Arrays.sort(fieldNames);

        if (conversionMaintenanceControl != null) {
            conversionMaintenanceControl.setFieldNames(this.fieldNames);
        }
    }

    private ConversionMaintenanceDialog(Shell shell, MaintenanceMode mode, ConversionManager manager) {
        super(shell, mode);

        this.manager = manager;
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        conversionMaintenanceControl = new ConversionMaintenanceControl(parent, SWT.NONE);
        conversionMaintenanceControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        conversionMaintenanceControl.setMode(getMode());

        setFields(fieldNames);
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Area;
    }

    @Override
    protected void setScreenValues() {

        conversionMaintenanceControl.setJobName(values.getKey().getJobName());
        conversionMaintenanceControl.setPosition(values.getKey().getPosition());
        conversionMaintenanceControl.setFieldToConvert(values.getKey().getFieldToConvert());

        conversionMaintenanceControl.setNewFieldName(values.getNewFieldName());

        conversionMaintenanceControl.setConversions(values.getConversions());
    }

    @Override
    protected void okPressed() {

        ConversionValues newValues = values.clone();
        newValues.getKey().setFieldToConvert(conversionMaintenanceControl.getFieldToConvert());
        newValues.setNewFieldName(conversionMaintenanceControl.getNewFieldName());
        newValues.setConversions(conversionMaintenanceControl.getConversions());

        if (!isDisplayMode()) {
            try {
                manager.setValues(newValues);
                Result result = manager.check();
                if (result.isError()) {
                    setErrorFocus(result);
                    return;
                }
            } catch (Exception e) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
                return;
            }
        }

        values = newValues;

        super.okPressed();
    }

    private void setErrorFocus(Result result) {

        String fieldName = result.getFieldName();
        String message = null;

        if (IConversionCheck.FIELD_FIELD_TO_CONVERT.equals(fieldName)) {
            conversionMaintenanceControl.setFocusFieldToConvert();
            message = Messages.bindParameters(Messages.Field_name_A_is_not_valid, conversionMaintenanceControl.getFieldToConvert());
        } else if (IConversionCheck.FIELD_NEW_FIELD_NAME.equals(fieldName)) {
            conversionMaintenanceControl.setFocusNewFieldName();
            message = Messages.bindParameters(Messages.Field_name_A_is_not_valid, conversionMaintenanceControl.getNewFieldName());
        } else if (IConversionCheck.FIELD_SAME_FIELD_NAMES.equals(fieldName)) {
            conversionMaintenanceControl.setFocusNewFieldName();
            message = Messages.Field_names_must_not_match;
        } else if (IConversionCheck.FIELD_STATEMENT.equals(fieldName)) {
            conversionMaintenanceControl.setFocusStatements();
            message = Messages.Conversion_statement_is_missing;
        }

        setErrorMessage(message, result);
    }

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
