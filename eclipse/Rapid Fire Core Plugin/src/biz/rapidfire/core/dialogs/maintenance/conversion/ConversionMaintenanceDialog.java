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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
import biz.rapidfire.core.maintenance.conversion.shared.NewFieldName;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class ConversionMaintenanceDialog extends AbstractMaintenanceDialog {

    private ConversionManager manager;

    private ConversionValues values;

    private Text textJobName;
    private Text textPosition;
    private Combo comboFieldToConvert;
    private Combo comboNewFieldName;
    private Text textStatement1;
    private Text textStatement2;
    private Text textStatement3;
    private Text textStatement4;
    private Text textStatement5;
    private Text textStatement6;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

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

    public void setFields(String[] fieldNames) {

        this.fieldNames = fieldNames;

        Arrays.sort(fieldNames);
    }

    private ConversionMaintenanceDialog(Shell shell, MaintenanceMode mode, ConversionManager manager) {
        super(shell, mode);

        this.manager = manager;

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

    @Override
    protected void createEditorAreaContent(Composite parent) {

        WidgetFactory.createLabel(parent, Messages.Label_Job_colon, Messages.Tooltip_Job);

        textJobName = WidgetFactory.createNameText(parent);
        textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobName.setToolTipText(Messages.Tooltip_Job);
        textJobName.setEnabled(enableParentKeyFields);

        WidgetFactory.createLabel(parent, Messages.Label_Position_colon, Messages.Tooltip_Position);

        textPosition = WidgetFactory.createIntegerText(parent);
        textPosition.setTextLimit(6);
        textPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textPosition.setToolTipText(Messages.Tooltip_Position);
        textPosition.setEnabled(enableParentKeyFields);

        WidgetFactory.createLabel(parent, Messages.Label_Field_to_convert_colon, Messages.Tooltip_Field_to_convert);

        comboFieldToConvert = WidgetFactory.createNameCombo(parent);
        comboFieldToConvert.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboFieldToConvert.setToolTipText(Messages.Tooltip_Field_to_convert);
        comboFieldToConvert.setEnabled(enableKeyFields);
        comboFieldToConvert.setItems(fieldNames);

        WidgetFactory.createLabel(parent, Messages.Label_Rename_field_in_old_file_to_colon, Messages.Tooltip_Rename_field_in_old_file_to);

        comboNewFieldName = WidgetFactory.createNameCombo(parent);
        setDefaultValue(comboNewFieldName, NewFieldName.NONE.label());
        comboNewFieldName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboNewFieldName.setToolTipText(Messages.Tooltip_Rename_field_in_old_file_to);
        comboNewFieldName.setEnabled(enableFields);
        comboNewFieldName.setItems(ConversionValues.getNewFieldNameSpecialValues());

        WidgetFactory.createLabel(parent, Messages.Label_Conversions_colon, Messages.Tooltip_Conversions);

        textStatement1 = createConversionStatement(parent);
        new Composite(parent, SWT.NONE).setLayoutData(new GridData(SWT.RIGHT, SWT.BEGINNING, false, false, 1, 5));
        textStatement2 = createConversionStatement(parent);
        textStatement3 = createConversionStatement(parent);
        textStatement4 = createConversionStatement(parent);
        textStatement5 = createConversionStatement(parent);
        textStatement6 = createConversionStatement(parent);
    }

    private Text createConversionStatement(Composite parent) {

        Text textStatement = WidgetFactory.createText(parent);
        textStatement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        textStatement.setToolTipText(Messages.Tooltip_Conversions);
        textStatement.setEnabled(enableFields);

        return textStatement;
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Area;
    }

    @Override
    protected void setScreenValues() {

        setText(textJobName, values.getKey().getJobName());
        setText(textPosition, Integer.toString(values.getKey().getPosition()));
        setText(comboFieldToConvert, values.getKey().getFieldToConvert());

        setText(comboNewFieldName, values.getNewFieldName());

        String[] conversions = values.getConversions();
        setText(textStatement1, conversions[0]);
        setText(textStatement2, conversions[1]);
        setText(textStatement3, conversions[2]);
        setText(textStatement4, conversions[3]);
        setText(textStatement5, conversions[4]);
        setText(textStatement6, conversions[5]);
    }

    @Override
    protected void okPressed() {

        ConversionValues newValues = values.clone();
        newValues.getKey().setFieldToConvert(comboFieldToConvert.getText());
        newValues.setNewFieldName(comboNewFieldName.getText());

        String[] conversions = new String[6];
        conversions[0] = textStatement1.getText();
        conversions[1] = textStatement2.getText();
        conversions[2] = textStatement3.getText();
        conversions[3] = textStatement4.getText();
        conversions[4] = textStatement5.getText();
        conversions[5] = textStatement6.getText();

        newValues.setConversions(conversions);

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
            comboFieldToConvert.setFocus();
            message = Messages.bindParameters(Messages.Field_name_A_is_not_valid, comboFieldToConvert.getText());
        } else if (IConversionCheck.FIELD_NEW_FIELD_NAME.equals(fieldName)) {
            comboNewFieldName.setFocus();
            message = Messages.bindParameters(Messages.Field_name_A_is_not_valid, comboNewFieldName.getText());
        } else if (IConversionCheck.FIELD_SAME_FIELD_NAMES.equals(fieldName)) {
            comboNewFieldName.setFocus();
            message = Messages.Field_names_must_not_match;
        } else if (IConversionCheck.FIELD_STATEMENT.equals(fieldName)) {
            textStatement1.setFocus();
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
