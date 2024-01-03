/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.file;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.file.FileManager;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.file.IFileCheck;

public class FileMaintenanceDialog extends AbstractMaintenanceDialog {

    private FileManager manager;

    private FileValues values;
    private FileMaintenanceControl fileMaintenanceControl;

    public static FileMaintenanceDialog getCreateDialog(Shell shell, FileManager manager) {
        return new FileMaintenanceDialog(shell, MaintenanceMode.CREATE, manager);
    }

    public static FileMaintenanceDialog getCopyDialog(Shell shell, FileManager manager) {
        return new FileMaintenanceDialog(shell, MaintenanceMode.COPY, manager);
    }

    public static FileMaintenanceDialog getChangeDialog(Shell shell, FileManager manager) {
        return new FileMaintenanceDialog(shell, MaintenanceMode.CHANGE, manager);
    }

    public static FileMaintenanceDialog getDeleteDialog(Shell shell, FileManager manager) {
        return new FileMaintenanceDialog(shell, MaintenanceMode.DELETE, manager);
    }

    public static FileMaintenanceDialog getDisplayDialog(Shell shell, FileManager manager) {
        return new FileMaintenanceDialog(shell, MaintenanceMode.DISPLAY, manager);
    }

    public void setValue(FileValues values) {
        this.values = values;
    }

    public FileValues getValue() {
        return values;
    }

    private FileMaintenanceDialog(Shell shell, MaintenanceMode mode, FileManager manager) {
        super(shell, mode);

        this.manager = manager;
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        fileMaintenanceControl = new FileMaintenanceControl(parent, SWT.NONE);
        fileMaintenanceControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fileMaintenanceControl.setMode(getMode());
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_File;
    }

    @Override
    protected void setScreenValues() {

        fileMaintenanceControl.setJobNames(new String[] { values.getKey().getJobName() });
        fileMaintenanceControl.selectJob(values.getKey().getJobName());

        fileMaintenanceControl.setPosition(values.getKey().getPosition());
        fileMaintenanceControl.setFileName(values.getFileName());
        fileMaintenanceControl.setFileType(values.getFileType());
        fileMaintenanceControl.setCopyProgramName(values.getCopyProgramName());
        fileMaintenanceControl.setCopyProgramLibraryName(values.getCopyProgramLibraryName());
        fileMaintenanceControl.setConversionProgramName(values.getConversionProgramName());
        fileMaintenanceControl.setConversionProgramLibraryName(values.getConversionProgramLibraryName());
    }

    @Override
    protected void okPressed() {

        FileValues newValues = values.clone();
        newValues.getKey().setPosition(IntHelper.tryParseInt(fileMaintenanceControl.getPosition(), -1));
        newValues.setFileName(fileMaintenanceControl.getFileName());
        newValues.setFileType(fileMaintenanceControl.getFileType());
        newValues.setCopyProgramName(fileMaintenanceControl.getCopyProgramName());
        newValues.setCopyProgramLibraryName(fileMaintenanceControl.getCopyProgramLibraryName());
        newValues.setConversionProgramName(fileMaintenanceControl.getConversionProgramName());
        newValues.setConversionProgramLibraryName(fileMaintenanceControl.getConversionProgramLibraryName());

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

        if (IFileCheck.FIELD_JOB.equals(fieldName)) {
            fileMaintenanceControl.setFocusJobName();
            message = Messages.bind(Messages.Job_name_A_is_not_valid, fileMaintenanceControl.getJobName());
        } else if (IFileCheck.FIELD_POSITION.equals(fieldName)) {
            fileMaintenanceControl.setFocusPosition();
            message = Messages.bind(Messages.File_position_A_is_not_valid, fileMaintenanceControl.getPosition());
        } else if (IFileCheck.FIELD_FILE.equals(fieldName)) {
            fileMaintenanceControl.setFocusFileName();
            message = Messages.bind(Messages.File_name_A_is_not_valid, fileMaintenanceControl.getFileName());
        } else if (IFileCheck.FIELD_TYPE.equals(fieldName)) {
            fileMaintenanceControl.setFocusFileType();
            message = Messages.bindParameters(Messages.File_type_A_is_not_valid, fileMaintenanceControl.getFileType());
        } else if (IFileCheck.FIELD_COPY_PROGRAM_NAME.equals(fieldName)) {
            fileMaintenanceControl.setFocusCopyProgramName();
            message = Messages.bind(Messages.Copy_program_name_A_is_not_valid, fileMaintenanceControl.getCopyProgramName());
        } else if (IFileCheck.FIELD_COPY_PROGRAM_LIBRARY_NAME.equals(fieldName)) {
            fileMaintenanceControl.setFocusCopyProgramLibraryName();
            message = Messages.bind(Messages.Library_name_A_is_not_valid, fileMaintenanceControl.getCopyProgramLibraryName());
        } else if (IFileCheck.FIELD_CONVERSION_PROGRAM_NAME.equals(fieldName)) {
            fileMaintenanceControl.setFocusConversionProgramName();
            message = Messages.bind(Messages.Conversion_program_name_A_is_not_valid, fileMaintenanceControl.getConversionProgramName());
        } else if (IFileCheck.FIELD_CONVERSION_PROGRAM_LIBRARY_NAME.equals(fieldName)) {
            fileMaintenanceControl.setFocusConversionProgramLibraryName();
            message = Messages.bind(Messages.Library_name_A_is_not_valid, fileMaintenanceControl.getConversionProgramLibraryName());
        }

        setErrorMessage(message, result);
    }
}
