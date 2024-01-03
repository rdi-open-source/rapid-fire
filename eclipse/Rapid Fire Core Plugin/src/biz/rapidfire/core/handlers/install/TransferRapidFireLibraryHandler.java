/*******************************************************************************
 * Copyright (c) 2017-2021 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.install;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.install.dialogs.TransferRapidFireLibrary;

/**
 * This class is the action handler of the "TransferLibraryAction". It is used
 * for uploading the rapid Fire library to the host.
 */
public class TransferRapidFireLibraryHandler extends AbstractHandler implements IHandler {

    private String connectionName;
    private int ftpPort;
    private String rapidFireLibrary;
    private String aspGroup;

    /**
     * Default constructor, used by the Eclipse framework.
     */
    public TransferRapidFireLibraryHandler() {
        super();
    }

    public TransferRapidFireLibraryHandler(String connectionName, int ftpPort, String rapidFireLibrary, String aspGroup) {
        this.connectionName = connectionName;
        this.ftpPort = ftpPort;
        this.rapidFireLibrary = rapidFireLibrary;
        this.aspGroup = aspGroup;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
     * ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {

        try {

            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

            if (StringHelper.isNullOrEmpty(rapidFireLibrary)) {
                MessageDialog.openError(shell, Messages.E_R_R_O_R, Messages.Rapid_Fire_library_not_set_in_preferences);
                return null;
            }

            TransferRapidFireLibrary statusDialog = new TransferRapidFireLibrary(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                SWT.APPLICATION_MODAL | SWT.SHELL_TRIM, rapidFireLibrary, aspGroup, connectionName, ftpPort);
            statusDialog.open();

        } catch (Throwable e) {
            RapidFireCorePlugin.logError("Failed to invoke the 'Transfer Library' handler.", e);
        }
        return null;
    }
}
