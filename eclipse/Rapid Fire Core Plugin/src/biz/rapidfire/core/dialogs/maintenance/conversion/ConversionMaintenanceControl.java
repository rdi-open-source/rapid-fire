/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.conversion;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceControl;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.conversion.ConversionValues;
import biz.rapidfire.core.maintenance.conversion.shared.NewFieldName;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class ConversionMaintenanceControl extends AbstractMaintenanceControl {

    private Text textJobName;
    private Text textPosition;

    private Combo comboFieldToConvert;
    private Combo comboNewFieldName;
    private Text[] textStatements;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

    public ConversionMaintenanceControl(Composite parent, int style) {
        super(parent, style, true);
    }

    public ConversionMaintenanceControl(Composite parent, boolean parentKeyFieldsVisible, int style) {
        super(parent, style, parentKeyFieldsVisible);
    }

    public void setFocusJobName() {

        if (isParentKeyFieldsVisible()) {
            textJobName.setFocus();
        }
    }

    public void setFocusPosition() {

        if (isParentKeyFieldsVisible()) {
            textPosition.setFocus();
        }
    }

    public void setFocusFieldToConvert() {
        comboFieldToConvert.setFocus();
    }

    public void setFocusNewFieldName() {
        comboNewFieldName.setFocus();
    }

    public void setFocusStatements() {
        textStatements[0].setFocus();
    }

    @Override
    public void setMode(MaintenanceMode mode) {

        super.setMode(mode);

        if (isParentKeyFieldsVisible()) {
            textJobName.setEnabled(isParentKeyFieldsEnabled());
            textPosition.setEnabled(isParentKeyFieldsEnabled());
        }

        comboFieldToConvert.setEnabled(isKeyFieldsEnabled());

        comboNewFieldName.setEnabled(isFieldsEnabled());

        for (Text textStatement : textStatements) {
            textStatement.setEnabled(isFieldsEnabled());
        }
    }

    @Override
    protected void createContent(Composite parent) {

        if (isParentKeyFieldsVisible()) {

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
        }

        WidgetFactory.createLabel(parent, Messages.Label_Field_to_convert_colon, Messages.Tooltip_Field_to_convert);

        comboFieldToConvert = WidgetFactory.createNameCombo(parent);
        comboFieldToConvert.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboFieldToConvert.setToolTipText(Messages.Tooltip_Field_to_convert);
        comboFieldToConvert.setEnabled(enableKeyFields);
        // comboFieldToConvert.setItems(fieldNames);

        WidgetFactory.createLabel(parent, Messages.Label_Rename_field_in_old_file_to_colon, Messages.Tooltip_Rename_field_in_old_file_to);

        comboNewFieldName = WidgetFactory.createNameCombo(parent);
        setDefaultValue(comboNewFieldName, NewFieldName.NONE.label());
        comboNewFieldName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboNewFieldName.setToolTipText(Messages.Tooltip_Rename_field_in_old_file_to);
        comboNewFieldName.setEnabled(enableFields);
        comboNewFieldName.setItems(ConversionValues.getNewFieldNameSpecialValues());

        WidgetFactory.createLabel(parent, Messages.Label_Conversions_colon, Messages.Tooltip_Conversions);

        textStatements = new Text[6];
        textStatements[0] = createConversionStatement(parent);
        new Composite(parent, SWT.NONE).setLayoutData(new GridData(SWT.RIGHT, SWT.BEGINNING, false, false, 1, 5));

        for (int i = 1; i < textStatements.length; i++) {
            textStatements[i] = createConversionStatement(parent);
        }
    }

    private Text createConversionStatement(Composite parent) {

        Text textStatement = WidgetFactory.createText(parent);
        textStatement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        textStatement.setToolTipText(Messages.Tooltip_Conversions);
        textStatement.setEnabled(enableFields);

        return textStatement;
    }

    public String getJobName() {

        if (isParentKeyFieldsVisible()) {
            return textJobName.getText();
        } else {
            return null;
        }
    }

    public void setJobName(String jobName) {

        if (isParentKeyFieldsVisible()) {
            setText(textJobName, jobName);
        }
    }

    public String getPosition() {

        if (isParentKeyFieldsVisible()) {
            return textPosition.getText();
        } else {
            return null;
        }
    }

    public void setPosition(int position) {

        if (isParentKeyFieldsVisible()) {
            setText(textPosition, Integer.toString(position));
        }
    }

    public String getFieldToConvert() {
        return comboFieldToConvert.getText();
    }

    public void setFieldToConvert(String commandType) {
        setText(comboFieldToConvert, commandType);
    }

    public void setFieldsToConvert(String[] fieldNames) {
        String fieldToCOnvert = comboFieldToConvert.getText();
        comboFieldToConvert.setItems(fieldNames);
        comboFieldToConvert.setText(fieldToCOnvert);
    }

    public void setFieldNames(String[] fieldNames) {
        comboFieldToConvert.setItems(fieldNames);
    }

    public String getNewFieldName() {
        return comboNewFieldName.getText();
    }

    public void setNewFieldName(String newFieldName) {
        setText(comboNewFieldName, newFieldName);
    }

    public String[] getConversions() {

        String[] statements = new String[textStatements.length];
        for (int i = 0; i < statements.length; i++) {
            statements[i] = textStatements[i].getText();
        }

        return statements;
    }

    public void setConversions(String[] statements) {

        for (int i = 0; i < statements.length; i++) {
            setText(textStatements[i], statements[i]);
        }
    }

    public void addSelectionListener(SelectionListener listener) {

        comboFieldToConvert.addSelectionListener(listener);
        comboNewFieldName.addSelectionListener(listener);

        for (Text textStatement : textStatements) {
            textStatement.addSelectionListener(listener);
        }
    }

    public void removeSelectionListener(SelectionListener listener) {

        comboFieldToConvert.removeSelectionListener(listener);
        comboNewFieldName.removeSelectionListener(listener);

        for (Text textStatement : textStatements) {
            textStatement.removeSelectionListener(listener);
        }
    }

    public void addModifyListener(ModifyListener listener) {

        if (isParentKeyFieldsVisible()) {
            textJobName.addModifyListener(listener);
            textPosition.addModifyListener(listener);
        }

        comboFieldToConvert.addModifyListener(listener);
        comboNewFieldName.addModifyListener(listener);

        for (Text textStatement : textStatements) {
            textStatement.addModifyListener(listener);
        }
    }

    public void removeModifyListener(ModifyListener listener) {

        if (isParentKeyFieldsVisible()) {
            textJobName.removeModifyListener(listener);
            textPosition.removeModifyListener(listener);
        }

        comboFieldToConvert.removeModifyListener(listener);
        comboNewFieldName.removeModifyListener(listener);

        for (Text textStatement : textStatements) {
            textStatement.removeModifyListener(listener);
        }
    }
}
