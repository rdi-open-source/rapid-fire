/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.library;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.library.ILibraryCheck;
import biz.rapidfire.core.model.maintenance.library.LibraryManager;
import biz.rapidfire.core.model.maintenance.library.LibraryValues;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class LibraryMaintenanceDialog extends XDialog {

    private String mode;
    private LibraryManager manager;

    private LibraryValues values;

    private Text textJobName;
    private Text textLibrary;
    private Text textShadowLibrary;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

    public static LibraryMaintenanceDialog getCreateDialog(Shell shell, LibraryManager manager) {
        return new LibraryMaintenanceDialog(shell, IMaintenance.MODE_CREATE, manager);
    }

    public static LibraryMaintenanceDialog getCopyDialog(Shell shell, LibraryManager manager) {
        return new LibraryMaintenanceDialog(shell, IMaintenance.MODE_COPY, manager);
    }

    public static LibraryMaintenanceDialog getChangeDialog(Shell shell, LibraryManager manager) {
        return new LibraryMaintenanceDialog(shell, IMaintenance.MODE_CHANGE, manager);
    }

    public static LibraryMaintenanceDialog getDeleteDialog(Shell shell, LibraryManager manager) {
        return new LibraryMaintenanceDialog(shell, IMaintenance.MODE_DELETE, manager);
    }

    public static LibraryMaintenanceDialog getDisplayDialog(Shell shell, LibraryManager manager) {
        return new LibraryMaintenanceDialog(shell, IMaintenance.MODE_DISPLAY, manager);
    }

    public void setValue(LibraryValues values) {
        this.values = values;
    }

    private LibraryMaintenanceDialog(Shell shell, String mode, LibraryManager manager) {
        super(shell);

        this.mode = mode;
        this.manager = manager;

        if (IMaintenance.MODE_CREATE.equals(mode) || IMaintenance.MODE_COPY.equals(mode)) {
            enableParentKeyFields = false;
            enableKeyFields = true;
            enableFields = true;
        } else if (IMaintenance.MODE_CHANGE.equals(mode)) {
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
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);

        newShell.setText(Messages.DialogTitle_Library);
    }

    @Override
    protected Control createDialogArea(Composite parent) {

        Composite container = (Composite)super.createDialogArea(parent);
        container.setLayout(new GridLayout(2, false));

        WidgetFactory.createDialogSubTitle(container, mode);

        Label labelJobName = new Label(container, SWT.NONE);
        labelJobName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelJobName.setText(Messages.Label_Job_colon);
        labelJobName.setToolTipText(Messages.Tooltip_Job);

        textJobName = WidgetFactory.createNameText(container);
        textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobName.setToolTipText(Messages.Tooltip_Job);
        textJobName.setEnabled(enableParentKeyFields);

        Label labelLibrary = new Label(container, SWT.NONE);
        labelLibrary.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelLibrary.setText(Messages.Label_Library_colon);
        labelLibrary.setToolTipText(Messages.Tooltip_Library);

        textLibrary = WidgetFactory.createNameText(container);
        textLibrary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textLibrary.setToolTipText(Messages.Tooltip_Library);
        textLibrary.setEnabled(enableKeyFields);

        Label labelShadwLibrary = new Label(container, SWT.NONE);
        labelShadwLibrary.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelShadwLibrary.setText(Messages.Label_Shadow_library_colon);
        labelShadwLibrary.setToolTipText(Messages.Tooltip_Shadow_library);

        textShadowLibrary = WidgetFactory.createNameText(container);
        textShadowLibrary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textShadowLibrary.setToolTipText(Messages.Tooltip_Shadow_library);
        textShadowLibrary.setEnabled(enableFields);

        createStatusLine(container);

        setScreenValues();

        return container;
    }

    private void setScreenValues() {

        textJobName.setText(values.getKey().getJobName());
        textLibrary.setText(values.getKey().getLibrary());
        textShadowLibrary.setText(values.getShadowLibrary());
    }

    @Override
    protected void okPressed() {

        LibraryValues newValues = values.clone();
        newValues.getKey().setLibrary(textLibrary.getText());
        newValues.setShadowLibrary(textShadowLibrary.getText());

        if (!IMaintenance.MODE_DISPLAY.equals(mode)) {
            try {
                manager.setValues(newValues);
                Result result = manager.check();
                if (result.isError()) {
                    setErrorFocus(result.getFieldName());
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

    private void setErrorFocus(String fieldName) {

        if (ILibraryCheck.FIELD_JOB.equals(fieldName)) {
            textJobName.setFocus();
            setErrorMessage(Messages.bind(Messages.Job_name_A_is_not_valid, textJobName.getText()));
        } else if (ILibraryCheck.FIELD_LIBRARY.equals(fieldName)) {
            textLibrary.setFocus();
            setErrorMessage(Messages.bind(Messages.File_position_A_is_not_valid, textLibrary.getText()));
        } else if (ILibraryCheck.FIELD_SHADOW_LIBRARY.equals(fieldName)) {
            textShadowLibrary.setFocus();
            setErrorMessage(Messages.bind(Messages.File_name_A_is_not_valid, textShadowLibrary.getText()));
        }
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
