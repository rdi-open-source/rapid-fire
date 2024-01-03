/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.command;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceControl;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.command.shared.CommandType;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class CommandMaintenanceControl extends AbstractMaintenanceControl {

    private static int COMPILE_SEQUENCE = 5;

    private Text textJobName;
    private Text textPosition;

    private Combo comboCommandType;
    private Text textSequence;
    private Text textCommand;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

    public CommandMaintenanceControl(Composite parent, int style) {
        super(parent, style, true);
    }

    public CommandMaintenanceControl(Composite parent, boolean parentKeyFieldsVisible, int style) {
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

    public void setFocusCommandType() {
        comboCommandType.setFocus();
    }

    public void setFocusSequence() {
        textSequence.setFocus();
    }

    public void setFocusCommand() {
        textCommand.setFocus();
    }

    @Override
    public void setMode(MaintenanceMode mode) {

        super.setMode(mode);

        if (isParentKeyFieldsVisible()) {
            textJobName.setEnabled(isParentKeyFieldsEnabled());
            textPosition.setEnabled(isParentKeyFieldsEnabled());
        }

        comboCommandType.setEnabled(isKeyFieldsEnabled());
        textSequence.setEnabled(isKeyFieldsEnabled());

        textCommand.setEnabled(isFieldsEnabled());
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

        WidgetFactory.createLabel(parent, Messages.Label_Command_type_colon, Messages.Tooltip_Command_type);

        comboCommandType = WidgetFactory.createReadOnlyCombo(parent);
        setDefaultValue(comboCommandType, CommandType.COMPILE.label());
        comboCommandType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboCommandType.setToolTipText(Messages.Tooltip_Command_type);
        comboCommandType.setEnabled(enableKeyFields);
        comboCommandType.setItems(CommandType.labels());
        comboCommandType.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                updateSequenceValueAndEnablement();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });

        WidgetFactory.createLabel(parent, Messages.Label_Command_sequence_colon, Messages.Tooltip_Command_sequence);

        textSequence = WidgetFactory.createIntegerText(parent);
        textSequence.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textSequence.setToolTipText(Messages.Tooltip_Command_sequence);
        textSequence.setEnabled(enableKeyFields);

        WidgetFactory.createLabel(parent, Messages.Label_Command_command_colon, Messages.Tooltip_Command_command);

        textCommand = WidgetFactory.createText(parent);
        textCommand.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textCommand.setToolTipText(Messages.Tooltip_Command_command);
        textCommand.setEnabled(enableFields);
    }

    private void updateSequenceValueAndEnablement() {

        if (isCompileCommandType()) {
            textSequence.setText(Integer.toString(COMPILE_SEQUENCE));
            textSequence.setEnabled(false);
        } else {
            textSequence.setEnabled(isKeyFieldsEnabled());
        }
    }

    private boolean isCompileCommandType() {
        return CommandType.COMPILE.label().equals(comboCommandType.getText());
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

    public String getCommandType() {
        return comboCommandType.getText();
    }

    public void setCommandType(String commandType) {
        setText(comboCommandType, commandType);
        updateSequenceValueAndEnablement();
    }

    public String getSequence() {
        return textSequence.getText();
    }

    public void setSequence(String sequence) {
        setText(textSequence, sequence);
        updateSequenceValueAndEnablement();
    }

    public String getCommand() {
        return textCommand.getText();
    }

    public void setCommand(String command) {
        setText(textCommand, command);
    }

    public void addSelectionListener(SelectionListener listener) {

        comboCommandType.addSelectionListener(listener);
        textSequence.addSelectionListener(listener);
    }

    public void removeSelectionListener(SelectionListener listener) {

        comboCommandType.removeSelectionListener(listener);
        textSequence.removeSelectionListener(listener);
    }

    public void addModifyListener(ModifyListener listener) {

        if (isParentKeyFieldsVisible()) {
            textJobName.addModifyListener(listener);
            textPosition.addModifyListener(listener);
        }

        comboCommandType.addModifyListener(listener);
        textSequence.addModifyListener(listener);
        textCommand.addModifyListener(listener);
    }

    public void removeModifyListener(ModifyListener listener) {

        if (isParentKeyFieldsVisible()) {
            textJobName.removeModifyListener(listener);
            textPosition.removeModifyListener(listener);
        }

        comboCommandType.removeModifyListener(listener);
        textSequence.removeModifyListener(listener);
        textCommand.removeModifyListener(listener);
    }
}
