/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.command.CommandMaintenanceControl;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.command.CommandValues;
import biz.rapidfire.core.maintenance.job.JobValues;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;

public class CommandPage extends AbstractWizardPage {

    public static final String NAME = "COMMAND_PAGE"; //$NON-NLS-1$

    private CommandValues commandValues;

    private CommandMaintenanceControl commandMaintenanceControl;

    public CommandPage(CommandValues commandValues) {
        super(NAME);

        this.commandValues = commandValues;

        setTitle(Messages.Wizard_Page_Command);
        setDescription(Messages.Wizard_Page_Command_description);
    }

    @Override
    public void setFocus() {

        if (StringHelper.isNullOrEmpty(commandMaintenanceControl.getCommandType())) {
            commandMaintenanceControl.setFocusCommandType();
        } else if (StringHelper.isNullOrEmpty(commandMaintenanceControl.getSequence())
            || IntHelper.tryParseInt(commandMaintenanceControl.getSequence(), -1) <= 0) {
            commandMaintenanceControl.setFocusSequence();
        } else if (StringHelper.isNullOrEmpty(commandMaintenanceControl.getCommand())) {
            commandMaintenanceControl.setFocusCommand();
            commandMaintenanceControl.setFocusCommandType();
        }
    }

    public JobValues getValues() {
        return null;
    }

    public void createContent(Composite parent) {

        commandMaintenanceControl = new CommandMaintenanceControl(parent, false, SWT.NONE);
        commandMaintenanceControl.setMode(MaintenanceMode.CREATE);
        commandMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
    }

    @Override
    protected void setInputData() {

        commandMaintenanceControl.setCommandType(commandValues.getKey().getCommandType());
        commandMaintenanceControl.setSequence(Integer.toString(commandValues.getKey().getSequence()));
        commandMaintenanceControl.setCommand(commandValues.getCommand());
    }

    @Override
    protected void addControlListeners() {

        commandMaintenanceControl.addModifyListener(this);
        commandMaintenanceControl.addSelectionListener(this);
    }

    @Override
    protected void updatePageComplete(Object source) {

        String message = null;

        if (StringHelper.isNullOrEmpty(commandMaintenanceControl.getSequence())
            || IntHelper.tryParseInt(commandMaintenanceControl.getSequence(), -1) <= 0) {

            message = Messages.bindParameters(Messages.Invalid_sequence_number_A, commandMaintenanceControl.getSequence());

        } else if (StringHelper.isNullOrEmpty(commandMaintenanceControl.getCommand())) {

            message = Messages.Object_compile_command_is_missing;
        }

        updateValues();

        if (message == null) {
            setPageComplete(true);
        } else {
            setPageComplete(false);
        }

        setErrorMessage(message);
    }

    private void updateValues() {

        commandValues.getKey().setCommandType(commandMaintenanceControl.getCommandType());
        commandValues.getKey().setSequence(IntHelper.tryParseInt(commandMaintenanceControl.getSequence(), 0));
        commandValues.setCommand(commandMaintenanceControl.getCommand());
    }

    @Override
    protected void storePreferences() {
    }
}
