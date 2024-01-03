/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.filecopyprogramgenerator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.as400.access.AS400;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.file.shared.ConversionProgram;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.FileCopyProgramGeneratorManager;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.FileCopyProgramGeneratorValues;
import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.QualifiedProgramName;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.swt.widgets.ISystemHostCombo;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.core.swt.widgets.viewers.stringlist.ItemSelectionDialog;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;
import biz.rapidfire.rsebase.host.SystemFileType;
import biz.rapidfire.rsebase.swt.widgets.SystemMemberPrompt;

public class FileCopyProgramGeneratorMaintenanceDialog extends AbstractMaintenanceDialog implements SelectionListener {

    private static final int NUM_COLUMNS = 3;

    private FileCopyProgramGeneratorManager manager;

    private ISystemHostCombo systemHostCombo;
    private SystemMemberPrompt memberPrompt;
    private Button buttonSelectArea;
    private Text textLibrary;
    private Text textShadowLibrary;
    private Button buttonSelectConversionProgram;
    private Text textConversionProgram;
    private Text textConversionProgramLibrary;
    private Button checkboxOpenMember;

    private String connectionName;
    private IRapidFireAreaResource[] areaItems;
    private QualifiedProgramName[] conversionProgramItems;

    private String sourceFile;
    private String sourceFileLibrary;
    private String sourceMember;
    private String library;
    private String shadowLibrary;
    private String conversionProgram;
    private String conversionProgramLibrary;

    private Preferences preferences;
    private boolean isOpenMember;

    public static FileCopyProgramGeneratorMaintenanceDialog getCreateDialog(Shell shell, FileCopyProgramGeneratorManager manager) {
        return new FileCopyProgramGeneratorMaintenanceDialog(shell, MaintenanceMode.CREATE, manager);
    }

    public boolean isOpenMember() {
        return isOpenMember;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public String getSourceFileLibrary() {
        return sourceFileLibrary;
    }

    public String getSourceMember() {
        return sourceMember;
    }

    private FileCopyProgramGeneratorMaintenanceDialog(Shell shell, MaintenanceMode mode, FileCopyProgramGeneratorManager manager) {
        super(shell, mode);

        this.manager = manager;

        this.preferences = Preferences.getInstance();

        this.isOpenMember = preferences.isOpenGeneratedCopyProgram();
        this.sourceFile = null;
        this.sourceFileLibrary = null;
        this.sourceMember = null;
    }

    public void setConnectionName(String connectionName) {

        this.connectionName = connectionName;

        if (systemHostCombo != null) {
            systemHostCombo.selectConnection(connectionName);
        }
    }

    public void setAreas(IRapidFireAreaResource[] areas) {
        this.areaItems = areas;
    }

    public void setConversionProgramName(String library, String name) {
        this.conversionProgramItems = new QualifiedProgramName[] { new QualifiedProgramName(library, name) };
    }

    @Override
    protected int getNumColumns() {
        return NUM_COLUMNS;
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        systemHostCombo = WidgetFactory.createSystemHostCombo(parent, SWT.NONE, false);
        systemHostCombo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, NUM_COLUMNS, 1));

        memberPrompt = WidgetFactory.createSystemMemberPrompt(parent, SWT.NONE, false, false, SystemFileType.SRC);
        memberPrompt.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, NUM_COLUMNS, 1));

        WidgetFactory.createLineFiller(parent);

        WidgetFactory.createLabel(parent, Messages.Label_Library_colon, Messages.Tooltip_Library);

        textLibrary = WidgetFactory.createNameText(parent);
        textLibrary.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        buttonSelectArea = WidgetFactory.createPushButton(parent, RapidFireCorePlugin.getDefault().getImage(RapidFireCorePlugin.IMAGE_LIBRARY));
        buttonSelectArea.setToolTipText(Messages.Tooltip_Select_libraries_by_area);
        buttonSelectArea.addSelectionListener(this);

        WidgetFactory.createLabel(parent, Messages.Label_Shadow_library_colon, Messages.Tooltip_Shadow_library);

        textShadowLibrary = WidgetFactory.createNameText(parent);
        textShadowLibrary.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        WidgetFactory.createLabel(parent, null, null).setVisible(false); // filler

        WidgetFactory.createLineFiller(parent);

        WidgetFactory.createLabel(parent, Messages.Label_Conversion_program_name_colon, Messages.Tooltip_Conversion_program_name);

        textConversionProgram = WidgetFactory.createNameText(parent);
        textConversionProgram.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        setDefaultValue(textConversionProgram, ConversionProgram.NONE.label());

        buttonSelectConversionProgram = WidgetFactory.createPushButton(parent,
            RapidFireCorePlugin.getDefault().getImage(RapidFireCorePlugin.IMAGE_PROGRAM));
        buttonSelectConversionProgram.setToolTipText(Messages.Tooltip_Select_conversion_program_of_file);
        buttonSelectConversionProgram.addSelectionListener(this);

        WidgetFactory.createLabel(parent, Messages.Label_Conversion_program_library_name_colon, Messages.Tooltip_Conversion_program_library_name);

        textConversionProgramLibrary = WidgetFactory.createNameText(parent);
        textConversionProgramLibrary.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        WidgetFactory.createLabel(parent, null, null).setVisible(false); // filler

        WidgetFactory.createLineFiller(parent);

        WidgetFactory.createSeparator(parent, NUM_COLUMNS);

        checkboxOpenMember = WidgetFactory.createCheckbox(parent, Messages.Label_Open_member_colon, Messages.Tooltip_Open_member, SWT.LEFT);
        checkboxOpenMember.setSelection(isOpenMember());
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogFile_Copy_Program_Generator;
    }

    @Override
    protected void setScreenValues() {

        if (connectionName != null) {
            systemHostCombo.selectConnection(connectionName);
        }

        setText(textLibrary, preferences.getGeneratorLibrary());
        setText(textShadowLibrary, preferences.getGeneratorShadowLibrary());
        setText(textConversionProgram, preferences.getGeneratorConversionProgram());
        setText(textConversionProgramLibrary, preferences.getGeneratorConversionProgramLibrary());
    }

    @Override
    protected void okPressed() {

        memberPrompt.updateHistory();

        preferences.setOpenGeneratedCopyProgram(checkboxOpenMember.getSelection());
        preferences.setGeneratorLibrary(textLibrary.getText());
        preferences.setGeneratorShadowLibrary(textShadowLibrary.getText());
        preferences.setGeneratorConversionProgram(textConversionProgram.getText());
        preferences.setGeneratorConversionProgramLibrary(textConversionProgramLibrary.getText());

        connectionName = systemHostCombo.getConnectionName();
        sourceFileLibrary = memberPrompt.getLibraryName();
        sourceFile = memberPrompt.getFileName();
        sourceMember = memberPrompt.getMemberName();
        library = textLibrary.getText();
        shadowLibrary = textShadowLibrary.getText();
        conversionProgram = textConversionProgram.getText();
        conversionProgramLibrary = textConversionProgramLibrary.getText();

        String errorMessage;

        errorMessage = validateLibrary(connectionName, sourceFileLibrary);
        if (errorMessage != null) {
            setErrorMessage(errorMessage);
            return;
        }

        errorMessage = validateFile(connectionName, sourceFileLibrary, sourceFile);
        if (errorMessage != null) {
            setErrorMessage(errorMessage);
            return;
        }

        errorMessage = validateMember(connectionName, sourceFileLibrary, sourceFile, sourceMember);
        if (errorMessage != null) {
            setErrorMessage(errorMessage);
            return;
        }

        FileCopyProgramGeneratorValues values = new FileCopyProgramGeneratorValues();
        values.setSourceFile(sourceFile);
        values.setSourceFileLibrary(sourceFileLibrary);
        values.setSourceMember(sourceMember);
        values.setReplace(false);
        values.setArea(""); //$NON-NLS-1$
        values.setLibrary(library);
        values.setShadowLibrary(shadowLibrary);
        values.setConversionProgram(conversionProgram);
        values.setConversionProgramLibrary(conversionProgramLibrary);

        isOpenMember = checkboxOpenMember.getSelection();

        try {

            manager.setValues(values);
            Result result = manager.check();

            FileCopyProgramGeneratorValues newValues = manager.getValues();

            if (result.isError()) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, result.getMessage());
                return;
            }

            if (!values.equals(newValues)) {
                memberPrompt.setFileName(newValues.getSourceFile());
                memberPrompt.setLibraryName(newValues.getSourceFileLibrary());
                memberPrompt.setMemberName(newValues.getSourceMember());
                // textArea.setText(newValues.getArea());
                textLibrary.setText(newValues.getLibrary());
                textShadowLibrary.setText(newValues.getShadowLibrary());
                textConversionProgram.setText(newValues.getConversionProgram());
                textConversionProgramLibrary.setText(newValues.getConversionProgramLibrary());
                return;
            }

        } catch (Exception e) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
            return;
        }

        super.okPressed();
    }

    private String validateLibrary(String connectionName, String library) {

        if (StringHelper.isNullOrEmpty(library)) {
            return Messages.Library_name_is_missing;
        }

        AS400 system = SystemConnectionHelper.getSystemChecked(connectionName);
        if (system != null && !RapidFireHelper.checkLibrary(system, library)) {
            return Messages.bindParameters(Messages.Library_A_not_found_on_system_B, library, connectionName);
        }

        return null;
    }

    private String validateFile(String connectionName, String library, String file) {

        if (StringHelper.isNullOrEmpty(file)) {
            return Messages.File_name_is_missing;
        }

        AS400 system = SystemConnectionHelper.getSystemChecked(connectionName);
        if (system != null && !RapidFireHelper.checkFile(system, library, file)) {
            return Messages.bindParameters(Messages.File_C_not_found_in_library_B_on_system_A, connectionName, library, file);
        }

        return null;
    }

    private String validateMember(String connectionName, String library, String file, String member) {

        if (StringHelper.isNullOrEmpty(member)) {
            return Messages.Member_name_is_missing;
        }

        AS400 system = SystemConnectionHelper.getSystemChecked(connectionName);
        if (system != null && RapidFireHelper.checkMember(system, library, file, member)) {
            if (MessageDialog.openQuestion(getShell(), Messages.E_R_R_O_R,
                Messages.bindParameters(Messages.Member_A_exists_Do_you_want_to_replace_the_member_A, member))) {

                String errorMessage = null;

                try {

                    errorMessage = RapidFireHelper.removeMember(connectionName, sourceFileLibrary, sourceFile, sourceMember);

                } catch (Throwable e) {
                    errorMessage = ExceptionHelper.getLocalizedMessage(e);
                }

                return errorMessage;
            }

            return Messages.bindParameters(Messages.Member_A_already_exists, member);
        }

        return null;
    }

    public void widgetDefaultSelected(SelectionEvent event) {
        widgetSelected(event);
    }

    public void widgetSelected(SelectionEvent event) {

        if (event.getSource() == buttonSelectArea) {
            ItemSelectionDialog<IRapidFireAreaResource> dialog = new ItemSelectionDialog<IRapidFireAreaResource>(getShell(),
                Messages.DialogTitle_Select_area, Messages.ColumnLabel_Area);
            dialog.setInputData(areaItems);
            if (dialog.open() == Dialog.OK) {
                IRapidFireAreaResource selectedArea = dialog.getSelectedItem();
                setLibraries(selectedArea);
                return;
            }
        } else if (event.getSource() == buttonSelectConversionProgram) {
            ItemSelectionDialog<QualifiedProgramName> dialog = new ItemSelectionDialog<QualifiedProgramName>(getShell(),
                Messages.DialogTitle_Select_conversion_program, Messages.ColumnLabel_Conversion_program, 200);
            dialog.setInputData(conversionProgramItems);
            if (dialog.open() == Dialog.OK) {
                QualifiedProgramName selectedArea = dialog.getSelectedItem();
                setConversionProgram(selectedArea);
                return;
            }
        }
    }

    private void setLibraries(IRapidFireAreaResource area) {

        try {

            IRapidFireLibraryResource library = area.getParentSubSystem().getLibrary(area.getParentJob(), area.getLibrary(), getShell());
            if (library != null) {
                if (library.getName() != null) {
                    textLibrary.setText(library.getName());
                }
                if (library.getShadowLibrary() != null) {
                    textShadowLibrary.setText(library.getShadowLibrary());
                }
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not read libraries of area: " + area.getName() + " ***", e); //$NON-NLS-1$ //$NON-NLS-2$
        }

    }

    private void setConversionProgram(QualifiedProgramName qualifiedProgram) {

        textConversionProgram.setText(qualifiedProgram.getName());
        textConversionProgramLibrary.setText(qualifiedProgram.getLibrary());
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
        return getShell().computeSize(Size.getSize(400), SWT.DEFAULT, true);
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
