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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.FileCopyProgramGeneratorManager;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.rsebase.host.SystemFileType;
import biz.rapidfire.rsebase.swt.widgets.SystemHostCombo;
import biz.rapidfire.rsebase.swt.widgets.SystemMemberPrompt;

import com.ibm.as400.access.AS400;

public class FileCopyProgramGeneratorMaintenanceDialog extends AbstractMaintenanceDialog {

    private FileCopyProgramGeneratorManager manager;
    private String connectionName;

    private SystemHostCombo systemHostCombo;
    private SystemMemberPrompt memberPrompt;

    public static FileCopyProgramGeneratorMaintenanceDialog getCreateDialog(Shell shell, FileCopyProgramGeneratorManager manager) {
        return new FileCopyProgramGeneratorMaintenanceDialog(shell, MaintenanceMode.CREATE, manager);
    }

    private FileCopyProgramGeneratorMaintenanceDialog(Shell shell, MaintenanceMode mode, FileCopyProgramGeneratorManager manager) {
        super(shell, mode);

        this.manager = manager;
    }

    public void setConnectionName(String connectionName) {

        this.connectionName = connectionName;

        if (systemHostCombo != null) {
            systemHostCombo.selectConnection(connectionName);
        }
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        systemHostCombo = WidgetFactory.createSystemHostCombo(parent, SWT.NONE, false);
        systemHostCombo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

        memberPrompt = WidgetFactory.createSystemMemberPrompt(parent, SWT.NONE, false, false, SystemFileType.SRC);
        memberPrompt.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
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

        String connectionName = systemHostCombo.getConnectionName();
        String library = memberPrompt.getLibraryName();
        String file = memberPrompt.getFileName();
        String member = memberPrompt.getMemberName();

        String errorMessage;

        errorMessage = validateLibrary(connectionName, library);
        if (errorMessage != null) {
            setErrorMessage(errorMessage);
            return;
        }

        errorMessage = validateFile(connectionName, library, file);
        if (errorMessage != null) {
            setErrorMessage(errorMessage);
            return;
        }

        errorMessage = validateMember(connectionName, library, file, member);
        if (errorMessage != null) {
            setErrorMessage(errorMessage);
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
                return null;
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
        return getShell().computeSize(Size.getSize(270), SWT.DEFAULT, true);
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
