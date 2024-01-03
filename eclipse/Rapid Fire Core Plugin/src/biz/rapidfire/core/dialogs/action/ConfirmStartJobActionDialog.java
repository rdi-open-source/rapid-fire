/*******************************************************************************
 * Copyright (c) 2012-2018 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class ConfirmStartJobActionDialog extends XDialog {

    private boolean isConfirmed;
    private String jobName;
    private String version;

    public ConfirmStartJobActionDialog(Shell shell, String jobName, String version) {
        super(shell);

        this.isConfirmed = false;
        this.jobName = jobName;
        this.version = version;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.bindParameters(Messages.DialogTitle_Start_Job_A, jobName));
    }

    @Override
    protected Control createDialogArea(Composite parent) {

        Composite container = new Composite(parent, SWT.BORDER);
        container.setLayout(new GridLayout());
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        container.setBackground(RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_FOREGROUND));

        Label header = new Label(container, SWT.CENTER);
        header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        header.setText("! ! !   I M P O R T A N T   I N F O R M A T I O N   ! ! !");
        header.setBackground(RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_FOREGROUND));

        WidgetFactory.createLineFiller(container);

        Text textInfoText = WidgetFactory.createMultilineLabel(container, SWT.V_SCROLL);
        textInfoText.setLayoutData(new GridData(GridData.FILL_BOTH));
        textInfoText.setText(loadWarningMessage());
        textInfoText.setBackground(RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_FOREGROUND));

        return container;
    }

    private String loadWarningMessage() {

        InputStream in = null;
        StringBuilder buffer = new StringBuilder();

        try {

            in = getClass().getClassLoader().getResourceAsStream("/biz/rapidfire/core/dialogs/action/resources/start_job_warning.text");
        	
            BufferedReader r = new BufferedReader(new InputStreamReader(in));

            // reads each line
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().length() == 0) {
                    buffer.append("\n\n");
                } else {
                    buffer.append(line.trim());
                    buffer.append(" ");
                }
            }
            in.close();

        } catch (IOException e) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        return buffer.toString();
    }

    @Override
    protected void okPressed() {

        isConfirmed = true;

        super.okPressed();
    }

    public boolean isConfirmed() {
        return isConfirmed;
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
        return getShell().computeSize(Size.getSize(250), SWT.DEFAULT, true);
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
