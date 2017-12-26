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
import biz.rapidfire.core.dialogs.maintenance.file.FileMaintenanceControl;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.validators.Validator;

public class FilePage extends AbstractWizardPage {

    public static final String NAME = "FILE_PAGE"; //$NON-NLS-1$

    private FileValues fileValues;

    private Validator nameValidator;
    private Validator libraryValidator;

    private FileMaintenanceControl fileMaintenanceControl;

    public FilePage(FileValues fileValues) {
        super(NAME);

        this.fileValues = fileValues;

        this.nameValidator = Validator.getNameInstance();
        this.libraryValidator = Validator.getLibraryNameInstance(Validator.LIBRARY_LIBL, Validator.LIBRARY_CURLIB);

        setTitle(Messages.Wizard_Page_File);
        setDescription(Messages.Wizard_Page_File_description);
    }

    @Override
    public void setFocus() {

        if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getJobName())) {
            fileMaintenanceControl.setFocusJobName();
        } else if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getPosition())) {
            fileMaintenanceControl.setFocusPosition();
        } else if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getFileName())) {
            fileMaintenanceControl.setFocusFileName();
        } else if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getFileType())) {
            fileMaintenanceControl.setFocusFileType();
        } else if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getCopyProgramName())) {
            fileMaintenanceControl.setFocusCopyProgramName();
        } else if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getCopyProgramLibraryName())) {
            fileMaintenanceControl.setFocusCopyProgramLibraryName();
        } else if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getConversionProgramName())) {
            fileMaintenanceControl.setFocusConversionProgramName();
        } else if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getConversionProgramLibraryName())) {
            fileMaintenanceControl.setFocusConversionProgramLibraryName();
        } else {
            fileMaintenanceControl.setFocusJobName();
        }
    }

    public FileValues getValues() {
        return fileValues;
    }

    public void createContent(Composite parent) {

        fileMaintenanceControl = new FileMaintenanceControl(parent, SWT.NONE);
        fileMaintenanceControl.setMode(MaintenanceMode.CREATE);
        fileMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
    }

    @Override
    protected void setInputData() {

        fileMaintenanceControl.setJobName(fileValues.getKey().getJobName());
        fileMaintenanceControl.setPosition(fileValues.getKey().getPosition());
        fileMaintenanceControl.setFileName(fileValues.getFileName());
        fileMaintenanceControl.setFileType(fileValues.getFileType());
        fileMaintenanceControl.setCopyProgramName(fileValues.getCopyProgramName());
        fileMaintenanceControl.setCopyProgramLibraryName(fileValues.getCopyProgramLibraryName());
        fileMaintenanceControl.setConversionProgramName(fileValues.getConversionProgramName());
        fileMaintenanceControl.setConversionProgramLibraryName(fileValues.getConversionProgramLibraryName());

        updatePageEnablement();
    }

    @Override
    protected void addControlListeners() {

        fileMaintenanceControl.addModifyListener(this);
        fileMaintenanceControl.addSelectionListener(this);
    }

    @Override
    protected void updatePageComplete(Object source) {

        String message = null;

        if (!nameValidator.validate(fileMaintenanceControl.getJobName())) {
            // fileMaintenanceControl.setFocusJobName();
            message = Messages.bindParameters(Messages.Job_name_A_is_not_valid, fileMaintenanceControl.getJobName());
        } else if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getPosition())) {
            // fileMaintenanceControl.setFocusDescription();
            message = Messages.bind(Messages.File_position_A_is_not_valid, fileMaintenanceControl.getPosition());
        } else if (!nameValidator.validate(fileMaintenanceControl.getFileName())) {
            // fileMaintenanceControl.setFocusJobQueueName();
            message = Messages.bindParameters(Messages.File_name_A_is_not_valid, fileMaintenanceControl.getFileName());
        } else if (!nameValidator.validate(fileMaintenanceControl.getCopyProgramName())) {
            // fileMaintenanceControl.setFocusJobQueueName();
            message = Messages.bindParameters(Messages.Copy_program_name_A_is_not_valid, fileMaintenanceControl.getCopyProgramName());
        } else if (!libraryValidator.validate(fileMaintenanceControl.getCopyProgramLibraryName())) {
            // fileMaintenanceControl.setFocusJobQueueLibraryName();
            message = Messages.bind(Messages.Library_name_A_is_not_valid, fileMaintenanceControl.getCopyProgramLibraryName());
        } else if (!nameValidator.validate(fileMaintenanceControl.getConversionProgramName())) {
            // fileMaintenanceControl.setFocusJobQueueName();
            message = Messages.bindParameters(Messages.Conversion_program_name_A_is_not_valid, fileMaintenanceControl.getConversionProgramName());
        } else if (!libraryValidator.validate(fileMaintenanceControl.getConversionProgramLibraryName())) {
            // fileMaintenanceControl.setFocusJobQueueLibraryName();
            message = Messages.bind(Messages.Library_name_A_is_not_valid, fileMaintenanceControl.getConversionProgramLibraryName());
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

        fileValues.getKey().setPosition(IntHelper.tryParseInt(fileMaintenanceControl.getPosition(), -1));
        fileValues.setFileName(fileMaintenanceControl.getFileName());
        fileValues.setFileType(fileMaintenanceControl.getFileType());
        fileValues.setCopyProgramName(fileMaintenanceControl.getCopyProgramName());
        fileValues.setCopyProgramLibraryName(fileMaintenanceControl.getCopyProgramLibraryName());
        fileValues.setConversionProgramName(fileMaintenanceControl.getConversionProgramName());
        fileValues.setConversionProgramLibraryName(fileMaintenanceControl.getConversionProgramLibraryName());

        updatePageEnablement();
    }

    private void updatePageEnablement() {
    }

    @Override
    protected void storePreferences() {
    }
}
