/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.conversion;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.conversion.ConversionManager;
import biz.rapidfire.core.model.maintenance.conversion.ConversionValues;
import biz.rapidfire.core.model.maintenance.conversion.IConversionCheck;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class ConversionMaintenanceDialog extends AbstractMaintenanceDialog {

    private ConversionManager manager;

    private ConversionValues values;

    private Text textJobName;
    private Text textPosition;
    private Text textFieldToConvert;
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

        Label labelJobName = new Label(parent, SWT.NONE);
        labelJobName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelJobName.setText(Messages.Label_Job_colon);
        labelJobName.setToolTipText(Messages.Tooltip_Job);

        textJobName = WidgetFactory.createNameText(parent);
        textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobName.setToolTipText(Messages.Tooltip_Job);
        textJobName.setEnabled(enableParentKeyFields);

        Label labelPosition = new Label(parent, SWT.NONE);
        labelPosition.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelPosition.setText(Messages.Label_Position_colon);
        labelPosition.setToolTipText(Messages.Tooltip_Position);

        textPosition = WidgetFactory.createIntegerText(parent);
        textPosition.setTextLimit(6);
        textPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textPosition.setToolTipText(Messages.Tooltip_Position);
        textPosition.setEnabled(enableParentKeyFields);

        Label labelFieldToConvert = new Label(parent, SWT.NONE);
        labelFieldToConvert.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelFieldToConvert.setText(Messages.Label_Field_to_convert_colon);
        labelFieldToConvert.setToolTipText(Messages.Tooltip_Field_to_convert);

        textFieldToConvert = WidgetFactory.createNameText(parent);
        textFieldToConvert.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textFieldToConvert.setToolTipText(Messages.Tooltip_Field_to_convert);
        textFieldToConvert.setEnabled(enableKeyFields);

        Label labelNewFieldName = new Label(parent, SWT.NONE);
        labelNewFieldName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelNewFieldName.setText(Messages.Label_Rename_field_in_old_file_to_colon);
        labelNewFieldName.setToolTipText(Messages.Tooltip_Rename_field_in_old_file_to);

        comboNewFieldName = WidgetFactory.createNameCombo(parent);
        comboNewFieldName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboNewFieldName.setToolTipText(Messages.Tooltip_Rename_field_in_old_file_to);
        comboNewFieldName.setEnabled(enableFields);
        comboNewFieldName.setItems(ConversionValues.getNewFieldNameSpecialValues());

        Label labelConversions = new Label(parent, SWT.NONE);
        labelConversions.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        labelConversions.setText(Messages.Label_Conversions_colon);
        labelConversions.setToolTipText(Messages.Tooltip_Conversions);

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

        textJobName.setText(values.getKey().getJobName());
        textPosition.setText(Integer.toString(values.getKey().getPosition()));
        textFieldToConvert.setText(values.getKey().getFieldToConvert());

        comboNewFieldName.setText(values.getNewFieldName());

        String[] conversions = values.getConversions();
        textStatement1.setText(conversions[0]);
        textStatement2.setText(conversions[1]);
        textStatement3.setText(conversions[2]);
        textStatement4.setText(conversions[3]);
        textStatement5.setText(conversions[4]);
        textStatement6.setText(conversions[5]);
    }

    @Override
    protected void okPressed() {

        ConversionValues newValues = values.clone();
        newValues.getKey().setFieldToConvert(textFieldToConvert.getText());
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
            textFieldToConvert.setFocus();
            message = Messages.bindParameters(Messages.Field_name_A_is_not_valid, textFieldToConvert.getText());
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
