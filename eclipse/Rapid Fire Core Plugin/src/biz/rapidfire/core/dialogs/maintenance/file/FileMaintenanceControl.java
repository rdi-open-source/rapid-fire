/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.file;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceControl;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.file.shared.ConversionProgram;
import biz.rapidfire.core.maintenance.file.shared.CopyProgram;
import biz.rapidfire.core.maintenance.file.shared.FileType;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class FileMaintenanceControl extends AbstractMaintenanceControl {

    private Combo comboJobName;
    private Text textPosition;
    private Text textFileName;
    private Combo comboFileType;
    private Combo comboCopyProgramName;
    private Text textCopyProgramLibraryName;
    private Combo comboConversionProgramName;
    private Text textConversionProgramLibraryName;

    public FileMaintenanceControl(Composite parent, int style) {
        super(parent, style, true);
    }

    public FileMaintenanceControl(Composite parent, boolean parentKeyFieldsVisible, int style) {
        super(parent, style, parentKeyFieldsVisible);
    }

    public void setFocusJobName() {
        comboJobName.setFocus();
    }

    public void setFocusPosition() {
        textPosition.setFocus();
    }

    public void setFocusFileName() {
        textFileName.setFocus();
    }

    public void setFocusFileType() {
        comboFileType.setFocus();
    }

    public void setFocusCopyProgramName() {
        comboCopyProgramName.setFocus();
    }

    public void setFocusCopyProgramLibraryName() {
        textCopyProgramLibraryName.setFocus();
    }

    public void setFocusConversionProgramName() {
        comboConversionProgramName.setFocus();
    }

    public void setFocusConversionProgramLibraryName() {
        textConversionProgramLibraryName.setFocus();
    }

    @Override
    public void setMode(MaintenanceMode mode) {

        super.setMode(mode);

        updateControlEnablement();
    }

    @Override
    protected void createContent(Composite parent) {

        WidgetFactory.createLabel(parent, Messages.Label_Job_colon, Messages.Tooltip_Job);

        comboJobName = WidgetFactory.createReadOnlyCombo(parent);
        comboJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboJobName.setToolTipText(Messages.Tooltip_Job);

        WidgetFactory.createLabel(parent, Messages.Label_Position_colon, Messages.Tooltip_Position);

        textPosition = WidgetFactory.createIntegerText(parent);
        textPosition.setTextLimit(6);
        textPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textPosition.setToolTipText(Messages.Tooltip_Position);

        WidgetFactory.createLabel(parent, Messages.Label_File_colon, Messages.Tooltip_File);

        textFileName = WidgetFactory.createNameText(parent);
        textFileName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textFileName.setToolTipText(Messages.Tooltip_File);

        WidgetFactory.createLabel(parent, Messages.Label_FileType_colon, Messages.Tooltip_FileType);

        comboFileType = WidgetFactory.createReadOnlyCombo(parent);
        setDefaultValue(comboFileType, FileType.PHYSICAL.label());
        comboFileType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboFileType.setToolTipText(Messages.Tooltip_Create_environment);
        comboFileType.setItems(FileValues.getTypeLabels());
        comboFileType.addSelectionListener(new FileTypeChangedListener());

        WidgetFactory.createLabel(parent, Messages.Label_Copy_program_name_colon, Messages.Tooltip_Copy_program_name);

        comboCopyProgramName = WidgetFactory.createNameCombo(parent);
        setDefaultValue(comboCopyProgramName, CopyProgram.GEN.label());
        comboCopyProgramName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboCopyProgramName.setToolTipText(Messages.Tooltip_Copy_program_name);
        comboCopyProgramName.setItems(FileValues.getCopyProgramSpecialValues());
        comboCopyProgramName.addSelectionListener(new ProgramChangedListener());

        WidgetFactory.createLabel(parent, Messages.Label_Copy_program_library_name_colon, Messages.Tooltip_Copy_program_library_name);

        textCopyProgramLibraryName = WidgetFactory.createNameText(parent);
        textCopyProgramLibraryName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textCopyProgramLibraryName.setToolTipText(Messages.Tooltip_Copy_program_library_name);

        WidgetFactory.createLabel(parent, Messages.Label_Conversion_program_name_colon, Messages.Tooltip_Conversion_program_name);

        comboConversionProgramName = WidgetFactory.createNameCombo(parent);
        setDefaultValue(comboConversionProgramName, ConversionProgram.NONE.label());
        comboConversionProgramName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboConversionProgramName.setToolTipText(Messages.Tooltip_Conversion_program_name);
        comboConversionProgramName.setItems(FileValues.getConversionProgramSpecialValues());
        comboConversionProgramName.addSelectionListener(new ProgramChangedListener());

        WidgetFactory.createLabel(parent, Messages.Label_Conversion_program_library_name_colon, Messages.Tooltip_Conversion_program_library_name);

        textConversionProgramLibraryName = WidgetFactory.createNameText(parent);
        textConversionProgramLibraryName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textConversionProgramLibraryName.setToolTipText(Messages.Tooltip_Conversion_program_library_name);
    }

    public String getJobName() {
        return comboJobName.getText();
    }

    public void setJobNames(String[] jobNames) {

        comboJobName.setItems(jobNames);

        updateControlEnablement();
    }

    public void selectJob(String jobName) {
        setSelectedItem(comboJobName, jobName);
    }

    public String getPosition() {
        return textPosition.getText();
    }

    public void setPosition(int position) {
        textPosition.setText(Integer.toString(position));
    }

    public String getFileName() {
        return textFileName.getText();
    }

    public void setFileName(String fileName) {
        textFileName.setText(fileName);
    }

    public String getFileType() {
        return comboFileType.getText();
    }

    public void setFileType(String fileType) {
        setText(comboFileType, fileType);
    }

    public String getCopyProgramName() {
        return comboCopyProgramName.getText();
    }

    public void setCopyProgramName(String copyProgramName) {
        setText(comboCopyProgramName, copyProgramName);
    }

    public String getCopyProgramLibraryName() {
        return textCopyProgramLibraryName.getText();
    }

    public void setCopyProgramLibraryName(String copyProgramLibraryName) {
        textCopyProgramLibraryName.setText(copyProgramLibraryName);
    }

    public String getConversionProgramName() {
        return comboConversionProgramName.getText();
    }

    public void setConversionProgramName(String conversionProgramName) {
        setText(comboConversionProgramName, conversionProgramName);
    }

    public String getConversionProgramLibraryName() {
        return textConversionProgramLibraryName.getText();
    }

    public void setConversionProgramLibraryName(String conversionProgramLibraryName) {
        textConversionProgramLibraryName.setText(conversionProgramLibraryName);
    }

    public void addSelectionListener(SelectionListener listener) {

        comboConversionProgramName.addSelectionListener(listener);
        comboCopyProgramName.addSelectionListener(listener);
        comboFileType.addSelectionListener(listener);
        comboJobName.addSelectionListener(listener);
    }

    public void removeSelectionListener(SelectionListener listener) {

        comboConversionProgramName.removeSelectionListener(listener);
        comboCopyProgramName.removeSelectionListener(listener);
        comboFileType.removeSelectionListener(listener);
        comboJobName.addSelectionListener(listener);
    }

    public void addModifyListener(ModifyListener listener) {

        comboConversionProgramName.addModifyListener(listener);
        comboCopyProgramName.addModifyListener(listener);
        comboFileType.addModifyListener(listener);
        comboJobName.addModifyListener(listener);

        textConversionProgramLibraryName.addModifyListener(listener);
        textCopyProgramLibraryName.addModifyListener(listener);
        textFileName.addModifyListener(listener);
        textPosition.addModifyListener(listener);
    }

    public void removeModifyListener(ModifyListener listener) {

        comboConversionProgramName.removeModifyListener(listener);
        comboCopyProgramName.removeModifyListener(listener);
        comboFileType.removeModifyListener(listener);
        comboJobName.removeModifyListener(listener);

        textConversionProgramLibraryName.removeModifyListener(listener);
        textCopyProgramLibraryName.removeModifyListener(listener);
        textFileName.removeModifyListener(listener);
        textPosition.removeModifyListener(listener);
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

    private void updateControlEnablement() {

        if (comboJobName.getItemCount() > 0) {
            comboJobName.setEnabled(true);
        } else {
            comboJobName.setEnabled(isParentKeyFieldsEnabled());
        }

        textPosition.setEnabled(isKeyFieldsEnabled());
        textFileName.setEnabled(isFieldsEnabled());
        comboFileType.setEnabled(isFieldsEnabled());

        if (comboFileType.getText().equals(FileType.LOGICAL.label())) {
            comboCopyProgramName.setEnabled(false);
            textCopyProgramLibraryName.setEnabled(false);
            comboConversionProgramName.setEnabled(false);
            textConversionProgramLibraryName.setEnabled(false);
        } else {
            comboCopyProgramName.setEnabled(isFieldsEnabled());
            textCopyProgramLibraryName.setEnabled(isFieldsEnabled());
            comboConversionProgramName.setEnabled(isFieldsEnabled());
            textConversionProgramLibraryName.setEnabled(isFieldsEnabled());
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
