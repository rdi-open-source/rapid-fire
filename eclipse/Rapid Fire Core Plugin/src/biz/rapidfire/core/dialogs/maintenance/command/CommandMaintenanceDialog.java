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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

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

public class CommandMaintenanceDialog extends AbstractMaintenanceDialog {

    private CommandManager manager;

    private CommandValues values;
    private CommandMaintenanceControl commandMaintenanceControl;

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
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        commandMaintenanceControl = new CommandMaintenanceControl(parent, SWT.NONE);
        commandMaintenanceControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        commandMaintenanceControl.setMode(getMode());
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Command;
    }

    @Override
    protected void setScreenValues() {

        commandMaintenanceControl.setJobName(values.getKey().getJobName());
        commandMaintenanceControl.setPosition(values.getKey().getPosition());

        commandMaintenanceControl.setCommandType(values.getKey().getCommandType());
        commandMaintenanceControl.setSequence(Integer.toString(values.getKey().getSequence()));
        commandMaintenanceControl.setCommand(values.getCommand());
    }

    @Override
    protected void okPressed() {

        CommandValues newValues = values.clone();
        newValues.getKey().setCommandType(commandMaintenanceControl.getCommandType());
        newValues.getKey().setSequence(IntHelper.tryParseInt(commandMaintenanceControl.getSequence(), -1));
        newValues.setCommand(commandMaintenanceControl.getCommand());

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
            commandMaintenanceControl.setFocusCommandType();
            message = Messages.bindParameters(Messages.Field_name_A_is_not_valid, commandMaintenanceControl.getCommandType());
        } else if (ICommandCheck.FIELD_SEQUENCE.equals(fieldName)) {
            commandMaintenanceControl.setFocusSequence();
            message = Messages.bindParameters(Messages.Field_name_A_is_not_valid, commandMaintenanceControl.getSequence());
        } else if (ICommandCheck.FIELD_EXIST.equals(fieldName)) {
            commandMaintenanceControl.setFocusSequence();
            message = Messages.Field_names_must_not_match;
        } else if (ICommandCheck.FIELD_COMMAND.equals(fieldName)) {
            commandMaintenanceControl.setFocusCommand();
            message = Messages.Object_compile_command_is_missing;
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
