/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.file;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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
import biz.rapidfire.core.maintenance.file.FileManager;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.file.IFileCheck;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class FileMaintenanceDialog extends AbstractMaintenanceDialog {

    private FileManager manager;

    private FileValues values;

    private Text textJobName;
    private Text textPosition;
    private Text textFileName;
    private Combo comboType;
    private Combo comboCopyProgramName;
    private Text textCopyProgramLibraryName;
    private Combo comboConversionProgramName;
    private Text textConversionProgramLibraryName;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

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

    private FileMaintenanceDialog(Shell shell, MaintenanceMode mode, FileManager manager) {
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

        Label labelPosition = new Label(parent, SWT.NONE);
        labelPosition.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelPosition.setText(Messages.Label_Position_colon);
        labelPosition.setToolTipText(Messages.Tooltip_Position);

        textPosition = WidgetFactory.createIntegerText(parent);
        textPosition.setTextLimit(6);
        textPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textPosition.setToolTipText(Messages.Tooltip_Position);
        textPosition.setEnabled(enableKeyFields);

        Label labelFileName = new Label(parent, SWT.NONE);
        labelFileName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelFileName.setText(Messages.Label_File_colon);
        labelFileName.setToolTipText(Messages.Tooltip_File);

        textFileName = WidgetFactory.createNameText(parent);
        textFileName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textFileName.setToolTipText(Messages.Tooltip_File);
        textFileName.setEnabled(enableFields);

        Label labelType = new Label(parent, SWT.NONE);
        labelType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelType.setText(Messages.Label_Create_environment_colon);
        labelType.setToolTipText(Messages.Tooltip_Create_environment);

        comboType = WidgetFactory.createReadOnlyCombo(parent);
        comboType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboType.setToolTipText(Messages.Tooltip_Create_environment);
        comboType.setEnabled(enableFields);
        comboType.setItems(FileValues.getTypeLabels());

        Label labelCopyProgramName = new Label(parent, SWT.NONE);
        labelCopyProgramName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelCopyProgramName.setText(Messages.Label_Copy_program_name_colon);
        labelCopyProgramName.setToolTipText(Messages.Tooltip_Copy_program_name);

        comboCopyProgramName = WidgetFactory.createNameCombo(parent);
        comboCopyProgramName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboCopyProgramName.setToolTipText(Messages.Tooltip_Copy_program_name);
        comboCopyProgramName.setEnabled(enableFields);
        comboCopyProgramName.setItems(FileValues.getCopyProgramSpecialValues());

        Label labelCopyProgramLibraryName = new Label(parent, SWT.NONE);
        labelCopyProgramLibraryName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelCopyProgramLibraryName.setText(Messages.Label_Copy_program_library_name_colon);
        labelCopyProgramLibraryName.setToolTipText(Messages.Tooltip_Copy_program_library_name);

        textCopyProgramLibraryName = WidgetFactory.createNameText(parent);
        textCopyProgramLibraryName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textCopyProgramLibraryName.setToolTipText(Messages.Tooltip_Copy_program_library_name);
        textCopyProgramLibraryName.setEnabled(enableFields);

        Label labelConversionProgramName = new Label(parent, SWT.NONE);
        labelConversionProgramName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelConversionProgramName.setText(Messages.Label_Conversion_program_name_colon);
        labelConversionProgramName.setToolTipText(Messages.Tooltip_Conversion_program_name);

        comboConversionProgramName = WidgetFactory.createNameCombo(parent);
        comboConversionProgramName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboConversionProgramName.setToolTipText(Messages.Tooltip_Conversion_program_name);
        comboConversionProgramName.setEnabled(enableFields);
        comboConversionProgramName.setItems(FileValues.getConversionProgramSpecialValues());

        Label labelConversionProgramLibraryName = new Label(parent, SWT.NONE);
        labelConversionProgramLibraryName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelConversionProgramLibraryName.setText(Messages.Label_Conversion_program_library_name_colon);
        labelConversionProgramLibraryName.setToolTipText(Messages.Tooltip_Conversion_program_library_name);

        textConversionProgramLibraryName = WidgetFactory.createNameText(parent);
        textConversionProgramLibraryName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textConversionProgramLibraryName.setToolTipText(Messages.Tooltip_Conversion_program_library_name);
        textConversionProgramLibraryName.setEnabled(enableFields);
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_File;
    }

    @Override
    protected void setScreenValues() {

        textJobName.setText(values.getKey().getJobName());

        textPosition.setText(Integer.toString(values.getKey().getPosition()));
        textFileName.setText(values.getFileName());
        comboType.setText(values.getFileType());
        comboCopyProgramName.setText(values.getCopyProgramName());
        textCopyProgramLibraryName.setText(values.getCopyProgramLibraryName());
        comboConversionProgramName.setText(values.getConversionProgramName());
        textConversionProgramLibraryName.setText(values.getConversionProgramLibraryName());
    }

    @Override
    protected void okPressed() {

        FileValues newValues = values.clone();
        newValues.getKey().setPosition(IntHelper.tryParseInt(textPosition.getText(), -1));
        newValues.setFileName(textFileName.getText());
        newValues.setFileType(comboType.getText());
        newValues.setCopyProgramName(comboCopyProgramName.getText());
        newValues.setCopyProgramLibraryName(textCopyProgramLibraryName.getText());
        newValues.setConversionProgramName(comboConversionProgramName.getText());
        newValues.setConversionProgramLibraryName(textConversionProgramLibraryName.getText());

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
            textJobName.setFocus();
            message = Messages.bind(Messages.Job_name_A_is_not_valid, textJobName.getText());
        } else if (IFileCheck.FIELD_POSITION.equals(fieldName)) {
            textPosition.setFocus();
            message = Messages.bind(Messages.File_position_A_is_not_valid, textPosition.getText());
        } else if (IFileCheck.FIELD_FILE.equals(fieldName)) {
            textFileName.setFocus();
            message = Messages.bind(Messages.File_name_A_is_not_valid, textFileName.getText());
        } else if (IFileCheck.FIELD_TYPE.equals(fieldName)) {
            comboType.setFocus();
            message = Messages.bindParameters(Messages.File_type_A_is_not_valid, comboType.getText());
        } else if (IFileCheck.FIELD_COPY_PROGRAM_NAME.equals(fieldName)) {
            comboCopyProgramName.setFocus();
            message = Messages.bind(Messages.Copy_program_name_A_is_not_valid, comboCopyProgramName.getText());
        } else if (IFileCheck.FIELD_COPY_PROGRAM_LIBRARY_NAME.equals(fieldName)) {
            textCopyProgramLibraryName.setFocus();
            message = Messages.bind(Messages.Library_name_A_is_not_valid, textCopyProgramLibraryName.getText());
        } else if (IFileCheck.FIELD_CONVERSION_PROGRAM_NAME.equals(fieldName)) {
            comboConversionProgramName.setFocus();
            message = Messages.bind(Messages.Conversion_program_name_A_is_not_valid, comboConversionProgramName.getText());
        } else if (IFileCheck.FIELD_CONVERSION_PROGRAM_LIBRARY_NAME.equals(fieldName)) {
            textConversionProgramLibraryName.setFocus();
            message = Messages.bind(Messages.Library_name_A_is_not_valid, textConversionProgramLibraryName.getText());
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
