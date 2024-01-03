/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.library;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.library.ILibraryCheck;
import biz.rapidfire.core.maintenance.library.LibraryManager;
import biz.rapidfire.core.maintenance.library.LibraryValues;

public class LibraryMaintenanceDialog extends AbstractMaintenanceDialog {

    private LibraryManager manager;

    private LibraryValues values;
    private LibraryMaintenanceControl libraryMaintenanceControl;

    public static LibraryMaintenanceDialog getCreateDialog(Shell shell, LibraryManager manager) {
        return new LibraryMaintenanceDialog(shell, MaintenanceMode.CREATE, manager);
    }

    public static LibraryMaintenanceDialog getCopyDialog(Shell shell, LibraryManager manager) {
        return new LibraryMaintenanceDialog(shell, MaintenanceMode.COPY, manager);
    }

    public static LibraryMaintenanceDialog getChangeDialog(Shell shell, LibraryManager manager) {
        return new LibraryMaintenanceDialog(shell, MaintenanceMode.CHANGE, manager);
    }

    public static LibraryMaintenanceDialog getDeleteDialog(Shell shell, LibraryManager manager) {
        return new LibraryMaintenanceDialog(shell, MaintenanceMode.DELETE, manager);
    }

    public static LibraryMaintenanceDialog getDisplayDialog(Shell shell, LibraryManager manager) {
        return new LibraryMaintenanceDialog(shell, MaintenanceMode.DISPLAY, manager);
    }

    public void setValue(LibraryValues values) {
        this.values = values;
    }

    public LibraryValues getValue() {
        return values;
    }

    private LibraryMaintenanceDialog(Shell shell, MaintenanceMode mode, LibraryManager manager) {
        super(shell, mode);

        this.manager = manager;
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        libraryMaintenanceControl = new LibraryMaintenanceControl(parent, SWT.NONE);
        libraryMaintenanceControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        libraryMaintenanceControl.setMode(getMode());
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Library;
    }

    @Override
    protected void setScreenValues() {

        libraryMaintenanceControl.setJobName(values.getKey().getJobName());
        libraryMaintenanceControl.setLibraryName(values.getKey().getLibrary());
        libraryMaintenanceControl.setShadowLibraryName(values.getShadowLibrary());
    }

    @Override
    protected void okPressed() {

        LibraryValues newValues = values.clone();
        newValues.getKey().setLibrary(libraryMaintenanceControl.getLibraryName());
        newValues.setShadowLibrary(libraryMaintenanceControl.getShadowLibraryName());

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

        if (ILibraryCheck.FIELD_JOB.equals(fieldName)) {
            libraryMaintenanceControl.setFocusJobName();
            message = Messages.bind(Messages.Job_name_A_is_not_valid, libraryMaintenanceControl.getJobName());
        } else if (ILibraryCheck.FIELD_LIBRARY.equals(fieldName)) {
            libraryMaintenanceControl.setFocusLibraryName();
            message = Messages.bind(Messages.Name_of_library_A_is_not_valid, libraryMaintenanceControl.getLibraryName());
        } else if (ILibraryCheck.FIELD_SHADOW_LIBRARY.equals(fieldName)) {
            libraryMaintenanceControl.setFocusShadowLibraryName();
            message = Messages.bind(Messages.Name_of_shadow_library_A_is_not_valid, libraryMaintenanceControl.getShadowLibraryName());
        }

        setErrorMessage(message, result);
    }
}
