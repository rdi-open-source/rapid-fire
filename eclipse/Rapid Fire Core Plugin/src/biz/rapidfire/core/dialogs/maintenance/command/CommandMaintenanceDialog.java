/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.command;

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
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.command.CommandManager;
import biz.rapidfire.core.maintenance.command.CommandValues;
import biz.rapidfire.core.maintenance.command.ICommandCheck;
import biz.rapidfire.core.maintenance.command.shared.CommandType;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class CommandMaintenanceDialog extends AbstractMaintenanceDialog {

    private CommandManager manager;

    private CommandValues values;

    private Text textJobName;
    private Text textPosition;
    private Combo comboCommandType;
    private Text textSequence;
    private Text textCommand;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

    public static CommandMaintenanceDialog getCreateDialog(Shell shell, CommandManager manager) {
        return new CommandMaintenanceDialog(shell, MaintenanceMode.CREATE, manager);
    }

    public static CommandMaintenanceDialog getCopyDialog(Shell shell, CommandManager manager) {
        return new CommandMaintenanceDialog(shell, MaintenanceMode.COPY, manager);
    }

    public static CommandMaintenanceDialog getChangeDialog(Shell shell, CommandManager manager) {
        return new CommandMaintenanceDialog(shell, MaintenanceMode.CHANGE, manager);
    }

    public static CommandMaintenanceDialog getDeleteDialog(Shell shell, CommandManager manager) {
        return new CommandMaintenanceDialog(shell, MaintenanceMode.DELETE, manager);
    }

    public static CommandMaintenanceDialog getDisplayDialog(Shell shell, CommandManager manager) {
        return new CommandMaintenanceDialog(shell, MaintenanceMode.DISPLAY, manager);
    }

    public void setValue(CommandValues values) {
        this.values = values;
    }

    public CommandValues getValue() {
        return values;
    }

    private CommandMaintenanceDialog(Shell shell, MaintenanceMode mode, CommandManager manager) {
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

        WidgetFactory.createLabel(parent, Messages.Label_Command_type_colon, Messages.Tooltip_Command_type);

        comboCommandType = WidgetFactory.createReadOnlyCombo(parent);
        setDefaultValue(comboCommandType, CommandType.COMPILE.label());
        comboCommandType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboCommandType.setToolTipText(Messages.Tooltip_Command_type);
        comboCommandType.setEnabled(enableKeyFields);
        comboCommandType.setItems(CommandType.labels());

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

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Area;
    }

    @Override
    protected void setScreenValues() {

        setText(textJobName, values.getKey().getJobName());
        setText(textPosition, Integer.toString(values.getKey().getPosition()));
        setText(comboCommandType, values.getKey().getCommandType());
        setText(textSequence, Integer.toString(values.getKey().getSequence()));

        setText(textCommand, values.getCommand());
    }

    @Override
    protected void okPressed() {

        CommandValues newValues = values.clone();
        newValues.getKey().setCommandType(comboCommandType.getText());
        newValues.getKey().setSequence(IntHelper.tryParseInt(textSequence.getText(), -1));
        newValues.setCommand(textCommand.getText());

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

        if (ICommandCheck.FIELD_TYPE.equals(fieldName)) {
            comboCommandType.setFocus();
            message = Messages.bindParameters(Messages.Field_name_A_is_not_valid, comboCommandType.getText());
        } else if (ICommandCheck.FIELD_SEQUENCE.equals(fieldName)) {
            textSequence.setFocus();
            message = Messages.bindParameters(Messages.Field_name_A_is_not_valid, textSequence.getText());
        } else if (ICommandCheck.FIELD_EXIST.equals(fieldName)) {
            textSequence.setFocus();
            message = Messages.Field_names_must_not_match;
        } else if (ICommandCheck.FIELD_COMMAND.equals(fieldName)) {
            textCommand.setFocus();
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
