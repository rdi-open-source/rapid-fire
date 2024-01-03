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
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.file.FileMaintenanceControl;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.file.shared.ConversionProgram;
import biz.rapidfire.core.maintenance.file.shared.CopyProgram;
import biz.rapidfire.core.maintenance.file.wizard.model.FileWizardDataModel;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.core.validators.Validator;

public class FilePage extends AbstractWizardPage {

    public static final String NAME = "FILE_PAGE"; //$NON-NLS-1$

    private FileWizardDataModel model;
    private boolean updateDataModel;

    private Validator nameValidator;
    private Validator copyProgramNameValidator;
    private Validator conversionProgramNameValidator;
    private Validator libraryValidator;

    private FileMaintenanceControl fileMaintenanceControl;
    private Text infoBox;

    public FilePage(FileWizardDataModel model) {
        super(NAME);

        this.model = model;
        this.updateDataModel = true;

        this.nameValidator = Validator.getNameInstance();
        this.copyProgramNameValidator = Validator.getNameInstance(CopyProgram.labels());
        this.conversionProgramNameValidator = Validator.getNameInstance(ConversionProgram.labels());
        this.libraryValidator = Validator.getLibraryNameInstance(Validator.LIBRARY_LIBL, Validator.LIBRARY_CURLIB);

        setTitle(Messages.Wizard_Page_File);
    }

    @Override
    public void updateMode() {

        setDescription(Messages.Wizard_Page_File_description);

        if (fileMaintenanceControl != null) {
            fileMaintenanceControl.setMode(MaintenanceMode.CREATE);
        }
    }

    @Override
    public void setFocus() {

        if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getJobName())) {
            fileMaintenanceControl.setFocusJobName();
        } else if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getPosition())
            || IntHelper.tryParseInt(fileMaintenanceControl.getPosition(), -1) <= 0) {
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

    public void setJobNames(String[] jobNames) {

        try {
            updateDataModel = false;

            if (jobNames != null) {
                String jobName = model.getJobName();
                fileMaintenanceControl.setJobNames(jobNames);
                fileMaintenanceControl.selectJob(jobName);
            }

        } finally {
            updateDataModel = true;
        }
    }

    @Override
    public void createContent(Composite parent) {

        fileMaintenanceControl = new FileMaintenanceControl(parent, SWT.NONE);
        fileMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

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

        infoBox.setText(Messages.Wizard_File_page_info_box);
    }

    @Override
    protected void setInputData() {

        fileMaintenanceControl.setJobNames(new String[] { model.getJobName() });
        fileMaintenanceControl.selectJob(model.getJobName());
        fileMaintenanceControl.setPosition(model.getPosition());
        fileMaintenanceControl.setFileName(model.getFileName());
        fileMaintenanceControl.setFileType(model.getFileTypeForUI());
        fileMaintenanceControl.setCopyProgramName(model.getQualifiedCopyProgramName().getName());
        fileMaintenanceControl.setCopyProgramLibraryName(model.getCopyProgramLibraryName());
        fileMaintenanceControl.setConversionProgramName(model.getConversionProgramName());
        fileMaintenanceControl.setConversionProgramLibraryName(model.getConversionProgramLibraryName());
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

            message = Messages.bindParameters(Messages.Job_name_A_is_not_valid, fileMaintenanceControl.getJobName());

        } else if (StringHelper.isNullOrEmpty(fileMaintenanceControl.getPosition())
            || IntHelper.tryParseInt(fileMaintenanceControl.getPosition(), -1) <= 0) {

            message = Messages.bind(Messages.File_position_A_is_not_valid, fileMaintenanceControl.getPosition());

        } else if (!nameValidator.validate(fileMaintenanceControl.getFileName())) {

            message = Messages.bindParameters(Messages.File_name_A_is_not_valid, fileMaintenanceControl.getFileName());

        } else if (!copyProgramNameValidator.validate(fileMaintenanceControl.getCopyProgramName())) {

            message = Messages.bindParameters(Messages.Copy_program_name_A_is_not_valid, fileMaintenanceControl.getCopyProgramName());

        } else if (!isSpecialValue(fileMaintenanceControl.getCopyProgramName(), CopyProgram.labels())
            && !libraryValidator.validate(fileMaintenanceControl.getCopyProgramLibraryName())) {

            message = Messages.bind(Messages.Copy_program_library_name_A_is_not_valid, fileMaintenanceControl.getCopyProgramLibraryName());

        } else if (!conversionProgramNameValidator.validate(fileMaintenanceControl.getConversionProgramName())) {

            message = Messages.bindParameters(Messages.Conversion_program_name_A_is_not_valid, fileMaintenanceControl.getConversionProgramName());

        } else if (!isSpecialValue(fileMaintenanceControl.getConversionProgramName(), ConversionProgram.labels())
            && !libraryValidator.validate(fileMaintenanceControl.getConversionProgramLibraryName())) {

            message = Messages
                .bind(Messages.Conversion_program_library_name_A_is_not_valid, fileMaintenanceControl.getConversionProgramLibraryName());
        }

        updateValues();

        if (message == null) {
            setPageComplete(true);
        } else {
            setPageComplete(false);
        }

        setErrorMessage(message);
    }

    private boolean isSpecialValue(String value, String[] specialValues) {

        if (value == null) {
            return false;
        }

        for (String specialValue : specialValues) {
            if (value.equals(specialValue)) {
                return true;
            }
        }

        return false;
    }

    private void updateValues() {

        if (!updateDataModel) {
            return;
        }

        model.setJobName(fileMaintenanceControl.getJobName());
        model.setPosition(IntHelper.tryParseInt(fileMaintenanceControl.getPosition(), -1));
        model.setFileName(fileMaintenanceControl.getFileName());
        model.setFileTypeFromUI(fileMaintenanceControl.getFileType());
        model.setCopyProgramName(fileMaintenanceControl.getCopyProgramName());
        model.setCopyProgramLibraryName(fileMaintenanceControl.getCopyProgramLibraryName());
        model.setConversionProgramName(fileMaintenanceControl.getConversionProgramName());
        model.setConversionProgramLibraryName(fileMaintenanceControl.getConversionProgramLibraryName());
    }

    @Override
    protected void storePreferences() {
    }
}
