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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.file.FileManager;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.file.IFileCheck;
import biz.rapidfire.core.maintenance.file.shared.ConversionProgram;
import biz.rapidfire.core.maintenance.file.shared.CopyProgram;
import biz.rapidfire.core.maintenance.file.shared.FileType;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class FileMaintenanceDialog extends AbstractMaintenanceDialog {

    private FileManager manager;

    private FileValues values;

    private Text textJobName;
    private Text textPosition;
    private Text textFileName;
    private Combo comboFileType;
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

        WidgetFactory.createLabel(parent, Messages.Label_Job_colon, Messages.Tooltip_Job);

        textJobName = WidgetFactory.createNameText(parent);
        textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobName.setToolTipText(Messages.Tooltip_Job);
        textJobName.setEnabled(enableParentKeyFields);

        WidgetFactory.createLabel(parent, Messages.Label_Position_colon, Messages.Tooltip_Position);

        textPosition = WidgetFactory.createIntegerText(parent);
        textPosition.setTextLimit(6);
        textPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textPosition.setToolTipText(Messages.Tooltip_Position);
        textPosition.setEnabled(enableKeyFields);

        WidgetFactory.createLabel(parent, Messages.Label_File_colon, Messages.Tooltip_File);

        textFileName = WidgetFactory.createNameText(parent);
        textFileName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textFileName.setToolTipText(Messages.Tooltip_File);
        textFileName.setEnabled(enableFields);

        WidgetFactory.createLabel(parent, Messages.Label_FileType_colon, Messages.Tooltip_FileType);

        comboFileType = WidgetFactory.createReadOnlyCombo(parent);
        setDefaultValue(comboFileType, FileType.PHYSICAL.label());
        comboFileType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboFileType.setToolTipText(Messages.Tooltip_Create_environment);
        comboFileType.setEnabled(enableFields);
        comboFileType.setItems(FileValues.getTypeLabels());
        comboFileType.addSelectionListener(new FileTypeChangedListener());

        WidgetFactory.createLabel(parent, Messages.Label_Copy_program_name_colon, Messages.Tooltip_Copy_program_name);

        comboCopyProgramName = WidgetFactory.createNameCombo(parent);
        setDefaultValue(comboCopyProgramName, CopyProgram.GEN.label());
        comboCopyProgramName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboCopyProgramName.setToolTipText(Messages.Tooltip_Copy_program_name);
        comboCopyProgramName.setEnabled(enableFields);
        comboCopyProgramName.setItems(FileValues.getCopyProgramSpecialValues());
        comboCopyProgramName.addSelectionListener(new ProgramChangedListener());

        WidgetFactory.createLabel(parent, Messages.Label_Copy_program_library_name_colon, Messages.Tooltip_Copy_program_library_name);

        textCopyProgramLibraryName = WidgetFactory.createNameText(parent);
        textCopyProgramLibraryName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textCopyProgramLibraryName.setToolTipText(Messages.Tooltip_Copy_program_library_name);
        textCopyProgramLibraryName.setEnabled(enableFields);

        WidgetFactory.createLabel(parent, Messages.Label_Conversion_program_name_colon, Messages.Tooltip_Conversion_program_name);

        comboConversionProgramName = WidgetFactory.createNameCombo(parent);
        setDefaultValue(comboConversionProgramName, ConversionProgram.NONE.label());
        comboConversionProgramName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboConversionProgramName.setToolTipText(Messages.Tooltip_Conversion_program_name);
        comboConversionProgramName.setEnabled(enableFields);
        comboConversionProgramName.setItems(FileValues.getConversionProgramSpecialValues());
        comboConversionProgramName.addSelectionListener(new ProgramChangedListener());

        WidgetFactory.createLabel(parent, Messages.Label_Conversion_program_library_name_colon, Messages.Tooltip_Conversion_program_library_name);

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

        setText(textJobName, values.getKey().getJobName());

        setText(textPosition, Integer.toString(values.getKey().getPosition()));
        setText(textFileName, values.getFileName());
        setText(comboFileType, values.getFileType());
        setText(comboCopyProgramName, values.getCopyProgramName());
        setText(textCopyProgramLibraryName, values.getCopyProgramLibraryName());
        setText(comboConversionProgramName, values.getConversionProgramName());
        setText(textConversionProgramLibraryName, values.getConversionProgramLibraryName());
    }

    @Override
    protected void okPressed() {

        FileValues newValues = values.clone();
        newValues.getKey().setPosition(IntHelper.tryParseInt(textPosition.getText(), -1));
        newValues.setFileName(textFileName.getText());
        newValues.setFileType(comboFileType.getText());
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
            comboFileType.setFocus();
            message = Messages.bindParameters(Messages.File_type_A_is_not_valid, comboFileType.getText());
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

    private void updateControlEnablement() {

        if (comboFileType.getText().equals(FileType.LOGICAL.label())) {
            comboCopyProgramName.setEnabled(false);
            textCopyProgramLibraryName.setEnabled(false);
            comboConversionProgramName.setEnabled(false);
            textConversionProgramLibraryName.setEnabled(false);
        } else {
            comboCopyProgramName.setEnabled(true);
            textCopyProgramLibraryName.setEnabled(true);
            comboConversionProgramName.setEnabled(true);
            textConversionProgramLibraryName.setEnabled(true);
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

    private class FileTypeChangedListener implements SelectionListener {

        public void widgetSelected(SelectionEvent event) {

            if (comboFileType.getText().equals(FileType.LOGICAL.label())) {
                if (!comboCopyProgramName.getText().equals(CopyProgram.NONE.label())) {
                    saveCurrentValue(comboCopyProgramName);
                    saveCurrentValue(textCopyProgramLibraryName);
                    setText(comboCopyProgramName, CopyProgram.NONE.label());
                    setText(textCopyProgramLibraryName, EMPTY_STRING);
                }
                if (!comboConversionProgramName.getText().equals(CopyProgram.NONE.label())) {
                    saveCurrentValue(comboConversionProgramName);
                    saveCurrentValue(textConversionProgramLibraryName);
                    setText(comboConversionProgramName, ConversionProgram.NONE.label());
                    setText(textConversionProgramLibraryName, EMPTY_STRING);
                }
            } else if (comboFileType.getText().equals(FileType.PHYSICAL.label())) {
                restorePreviousValue(comboCopyProgramName);
                restorePreviousValue(textCopyProgramLibraryName);
                restorePreviousValue(comboConversionProgramName);
                restorePreviousValue(textConversionProgramLibraryName);
            }

            updateControlEnablement();
        }

        public void widgetDefaultSelected(SelectionEvent event) {
            widgetSelected(event);
        }
    }

    private class ProgramChangedListener implements SelectionListener {

        public void widgetSelected(SelectionEvent event) {

            if (event.getSource().equals(comboCopyProgramName)) {
                performCopyProgramChanged();
            } else if (event.getSource().equals(comboConversionProgramName)) {
                performConversionProgramChanged();
            }
        }

        private void performCopyProgramChanged() {

            if (CopyProgram.GEN.label().equals(comboCopyProgramName.getText()) || CopyProgram.NONE.label().equals(comboCopyProgramName.getText())) {
                saveCurrentValue(textCopyProgramLibraryName);// textCopyProgramLibraryName.getText()
                setText(textCopyProgramLibraryName, EMPTY_STRING);
            } else {
                if (StringHelper.isNullOrEmpty(textCopyProgramLibraryName.getText())) {
                    internallyRestorePreviousValue(textCopyProgramLibraryName);
                }
            }
        }

        private void performConversionProgramChanged() {

            if (ConversionProgram.NONE.label().equals(comboConversionProgramName.getText())) {
                saveCurrentValue(textConversionProgramLibraryName);
                setText(textConversionProgramLibraryName, EMPTY_STRING);
            } else {
                if (StringHelper.isNullOrEmpty(textConversionProgramLibraryName.getText())) {
                    internallyRestorePreviousValue(textConversionProgramLibraryName);
                }
            }
        }

        private void internallyRestorePreviousValue(Text textControl) {

            restorePreviousValue(textControl);
            if (StringHelper.isNullOrEmpty(textControl.getText())) {
                setText(textControl, "*LIBL");
            }

        }

        public void widgetDefaultSelected(SelectionEvent event) {
            widgetSelected(event);
        }
    }
}
