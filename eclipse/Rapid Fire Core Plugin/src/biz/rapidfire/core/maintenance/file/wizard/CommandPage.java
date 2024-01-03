/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.command.CommandMaintenanceControl;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.file.wizard.model.FileWizardDataModel;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class CommandPage extends AbstractWizardPage {

    public static final String NAME = "COMMAND_PAGE"; //$NON-NLS-1$

    private FileWizardDataModel model;

    private CommandMaintenanceControl commandMaintenanceControl;
    private Text infoBox;

    private boolean editable;

    public CommandPage(FileWizardDataModel model) {
        super(NAME);

        this.model = model;

        setTitle(Messages.Wizard_Page_Command);

        updateMode();
    }

    @Override
    public void updateMode() {

        IRapidFireJobResource job = model.getJob();

        if (job == null || job.isDoCreateEnvironment()) {
            setDescription(Messages.Wizard_Page_Command_description);
            editable = true;
        } else {
            setDescription(Messages.Wizard_Not_applicable_for_jobs_that_do_not_create_a_shadow_environment);
            editable = false;
        }

        if (commandMaintenanceControl != null) {
            if (editable) {
                commandMaintenanceControl.setMode(MaintenanceMode.CREATE);
            } else {
                commandMaintenanceControl.setMode(MaintenanceMode.DISPLAY);
            }
        }
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
        } else {
            commandMaintenanceControl.setFocusCommandType();
        }
    }

    @Override
    public void createContent(Composite parent) {

        commandMaintenanceControl = new CommandMaintenanceControl(parent, false, SWT.NONE);
        commandMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

        infoBox = WidgetFactory.createMultilineLabel(parent);
        infoBox.setLayoutData(new GridData(GridData.FILL_BOTH));

        updateMode();
    }

    /**
     * Sets the bulk text of the infoBox control. If the text is set in
     * createContent(), the wizard page is rendered ugly.
     */
    @Override
    public void prepareForDisplay() {

        infoBox.setText(Messages.Wizard_Command_page_info_box);
    }

    @Override
    protected void setInputData() {

        commandMaintenanceControl.setCommandType(model.getCommandTypeForUI());
        commandMaintenanceControl.setSequence(Integer.toString(model.getSequence()));
        commandMaintenanceControl.setCommand(model.getCommand());
    }

    @Override
    protected void addControlListeners() {

        commandMaintenanceControl.addModifyListener(this);
        commandMaintenanceControl.addSelectionListener(this);
    }

    @Override
    protected void updatePageComplete(Object source) {

        String message = null;

        if (editable) {
            if (StringHelper.isNullOrEmpty(commandMaintenanceControl.getSequence())
                || IntHelper.tryParseInt(commandMaintenanceControl.getSequence(), -1) <= 0) {

                message = Messages.bindParameters(Messages.Invalid_sequence_number_A, commandMaintenanceControl.getSequence());

            } else if (StringHelper.isNullOrEmpty(commandMaintenanceControl.getCommand())) {

                message = Messages.Object_compile_command_is_missing;
            }

            updateValues();
        }

        if (message == null) {
            setPageComplete(true);
        } else {
            setPageComplete(false);
        }

        setErrorMessage(message);
    }

    private void updateValues() {

        model.setCommandTypeFromUI(commandMaintenanceControl.getCommandType());
        model.setSequence(IntHelper.tryParseInt(commandMaintenanceControl.getSequence(), 0));
        model.setCommand(commandMaintenanceControl.getCommand());
    }

    @Override
    protected void storePreferences() {
    }
}
