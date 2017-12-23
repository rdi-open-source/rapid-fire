/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.filecopyprogramgenerator;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

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
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.rsebase.host.SystemFileType;
import biz.rapidfire.rsebase.swt.widgets.SystemHostCombo;
import biz.rapidfire.rsebase.swt.widgets.SystemMemberPrompt;

import com.ibm.as400.access.AS400;

public class FileCopyProgramGeneratorMaintenanceDialog extends AbstractMaintenanceDialog {

    private FileCopyProgramGeneratorManager manager;

    private SystemHostCombo systemHostCombo;
    private SystemMemberPrompt memberPrompt;

    private Button checkboxOpenMember;

    private boolean isOpenMember;
    private String connectionName;
    private String sourceFile;
    private String sourceFileLibrary;
    private String sourceMember;

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

        this.isOpenMember = Preferences.getInstance().isOpenGeneratedCopyProgram();
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

    @Override
    protected void createEditorAreaContent(Composite parent) {

        // Group memberGroup = new Group(parent, SWT.NONE);
        // memberGroup.setLayout(new GridLayout(1, false));
        // memberGroup.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
        // false, 2, 1));

        systemHostCombo = WidgetFactory.createSystemHostCombo(parent, SWT.NONE, false);
        systemHostCombo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

        memberPrompt = WidgetFactory.createSystemMemberPrompt(parent, SWT.NONE, false, false, SystemFileType.SRC);
        memberPrompt.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

        WidgetFactory.createSeparator(parent, 2);

        checkboxOpenMember = WidgetFactory.createCheckbox(parent, Messages.Label_Open_member_colon, Messages.Tooltip_Open_member, SWT.LEFT);
        checkboxOpenMember.setSelection(isOpenMember());
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_File;
    }

    @Override
    protected void setScreenValues() {

        if (connectionName != null) {
            systemHostCombo.selectConnection(connectionName);
        }
    }

    @Override
    protected void okPressed() {

        memberPrompt.updateHistory();

        Preferences.getInstance().setOpenGeneratedCopyProgram(checkboxOpenMember.getSelection());

        connectionName = systemHostCombo.getConnectionName();
        sourceFileLibrary = memberPrompt.getLibraryName();
        sourceFile = memberPrompt.getFileName();
        sourceMember = memberPrompt.getMemberName();

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
        values.setArea("RFPRI");
        values.setLibrary("RFPRI");
        values.setShadowLibrary("RADDATZ");
        values.setConversionProgram(ConversionProgram.NONE.label());
        values.setConversionProgramLibrary("");

        isOpenMember = checkboxOpenMember.getSelection();

        try {
            manager.setValues(values);
            Result result = manager.check();
            if (result.isError()) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, result.getMessage());
                return;
            }
        } catch (Exception e) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
            return;
        }

        super.okPressed();
    }

    private String validateLibrary(String connectionName, String library) {

        AS400 system = RapidFireHelper.getSystem(connectionName);

        if (StringHelper.isNullOrEmpty(library)) {
            return Messages.Library_name_is_missing;
        }

        if (!RapidFireHelper.checkLibrary(system, library)) {
            return Messages.bindParameters(Messages.Library_A_not_found_on_system_B, library, connectionName);
        }

        return null;
    }

    private String validateFile(String connectionName, String library, String file) {

        AS400 system = RapidFireHelper.getSystem(connectionName);

        if (StringHelper.isNullOrEmpty(file)) {
            return Messages.File_name_is_missing;
        }

        if (!RapidFireHelper.checkFile(system, library, file)) {
            return Messages.bindParameters(Messages.File_C_not_found_in_library_B_on_system_A, connectionName, library, file);
        }

        return null;
    }

    private String validateMember(String connectionName, String library, String file, String member) {

        AS400 system = RapidFireHelper.getSystem(connectionName);

        if (StringHelper.isNullOrEmpty(member)) {
            return Messages.Member_name_is_missing;
        }

        if (RapidFireHelper.checkMember(system, library, file, member)) {
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
        return getShell().computeSize(Size.getSize(300), SWT.DEFAULT, true);
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
