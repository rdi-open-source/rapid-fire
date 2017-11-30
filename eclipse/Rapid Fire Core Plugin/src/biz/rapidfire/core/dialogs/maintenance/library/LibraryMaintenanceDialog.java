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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.library.ILibraryCheck;
import biz.rapidfire.core.model.maintenance.library.LibraryManager;
import biz.rapidfire.core.model.maintenance.library.LibraryValues;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class LibraryMaintenanceDialog extends AbstractMaintenanceDialog {

    private LibraryManager manager;

    private LibraryValues values;

    private Text textJobName;
    private Text textLibrary;
    private Text textShadowLibrary;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

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

    private LibraryMaintenanceDialog(Shell shell, MaintenanceMode mode, LibraryManager manager) {
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

        Label labelJobName = new Label(parent, SWT.NONE);
        labelJobName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelJobName.setText(Messages.Label_Job_colon);
        labelJobName.setToolTipText(Messages.Tooltip_Job);

        textJobName = WidgetFactory.createNameText(parent);
        textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobName.setToolTipText(Messages.Tooltip_Job);
        textJobName.setEnabled(enableParentKeyFields);

        Label labelLibrary = new Label(parent, SWT.NONE);
        labelLibrary.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelLibrary.setText(Messages.Label_Library_colon);
        labelLibrary.setToolTipText(Messages.Tooltip_Library);

        textLibrary = WidgetFactory.createNameText(parent);
        textLibrary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textLibrary.setToolTipText(Messages.Tooltip_Library);
        textLibrary.setEnabled(enableKeyFields);

        Label labelShadwLibrary = new Label(parent, SWT.NONE);
        labelShadwLibrary.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelShadwLibrary.setText(Messages.Label_Shadow_library_colon);
        labelShadwLibrary.setToolTipText(Messages.Tooltip_Shadow_library);

        textShadowLibrary = WidgetFactory.createNameText(parent);
        textShadowLibrary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textShadowLibrary.setToolTipText(Messages.Tooltip_Shadow_library);
        textShadowLibrary.setEnabled(enableFields);
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Library;
    }

    @Override
    protected void setScreenValues() {

        textJobName.setText(values.getKey().getJobName());

        textLibrary.setText(values.getKey().getLibrary());
        textShadowLibrary.setText(values.getShadowLibrary());
    }

    @Override
    protected void okPressed() {

        LibraryValues newValues = values.clone();
        newValues.getKey().setLibrary(textLibrary.getText());
        newValues.setShadowLibrary(textShadowLibrary.getText());

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
            textJobName.setFocus();
            message = Messages.bind(Messages.Job_name_A_is_not_valid, textJobName.getText());
        } else if (ILibraryCheck.FIELD_LIBRARY.equals(fieldName)) {
            textLibrary.setFocus();
            message = Messages.bind(Messages.Name_of_library_A_is_not_valid, textLibrary.getText());
        } else if (ILibraryCheck.FIELD_SHADOW_LIBRARY.equals(fieldName)) {
            textShadowLibrary.setFocus();
            message = Messages.bind(Messages.Name_of_shadow_library_A_is_not_valid, textShadowLibrary.getText());
        }

        setErrorMessage(message, result);
    }
}
