/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.action;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.job.shared.JobAction;

public class ConfirmActionDialog {

    private int returnCode;
    private boolean isDeleteShadowLibraries;

    private ConfirmActionDialog(boolean isConfirmed) {

        if (isConfirmed) {
            this.returnCode = IDialogConstants.OK_ID;
        } else {
            this.returnCode = IDialogConstants.CANCEL_ID;
        }
        this.isDeleteShadowLibraries = false;
    }

    private ConfirmActionDialog(int returnCode, boolean deleteShadowLibraries) {
        this.returnCode = returnCode;
        this.isDeleteShadowLibraries = deleteShadowLibraries;
    }

    public static ConfirmActionDialog open(Shell shell, JobAction jobAction, String jobName) {

        switch (jobAction) {
        case RESETJOB:
        case RESETJOBA:

            MessageDialogWithToggle messageDialog = MessageDialogWithToggle.openOkCancelConfirm(shell, getDialogTitle(jobAction, jobName),
                getQuestion(jobAction, jobName), Messages.Question_Delete_shadow_library, false, null, null);

            return new ConfirmActionDialog(messageDialog.getReturnCode(), messageDialog.getToggleState());

        default:
            boolean isConfirmed = MessageDialog.openConfirm(shell, getDialogTitle(jobAction, jobName), getQuestion(jobAction, jobName));
            return new ConfirmActionDialog(isConfirmed);
        }
    }

    public boolean isConfirmed() {

        if (returnCode == IDialogConstants.OK_ID) {
            return true;
        }

        return false;
    }

    public boolean isDeleteShadowLibrary() {
        return isDeleteShadowLibraries;
    }

    private static String getDialogTitle(JobAction jobAction, String jobName) {

        switch (jobAction) {
        case TSTJOB:
            return Messages.bindParameters(Messages.DialogTitle_Test_Job_A, jobName);
        case STRJOB:
            return Messages.bindParameters(Messages.DialogTitle_Start_Job_A, jobName);
        case ENDJOB:
            return Messages.bindParameters(Messages.DialogTitle_End_Job_A, jobName);
        case RESETJOB:
            return Messages.bindParameters(Messages.DialogTitle_Reset_Job_A, jobName);
        case RESETJOBA:
            return Messages.bindParameters(Messages.DialogTitle_Reset_Job_A_after_abortion, jobName);

        default:
            return "???"; // //$NON-NLS-1$
        }
    }

    private static String getQuestion(JobAction jobAction, String jobName) {

        switch (jobAction) {
        case TSTJOB:
            return Messages.bindParameters(Messages.Question_Do_you_want_to_test_job_A, jobName);
        case STRJOB:
            return Messages.bindParameters(Messages.Question_Do_you_want_to_start_job_A, jobName);
        case ENDJOB:
            return Messages.bindParameters(Messages.Question_Do_you_want_to_end_job_A, jobAction);
        case RESETJOB:
            return Messages.bindParameters(Messages.Question_Do_you_want_to_reset_job_A, jobName);
        case RESETJOBA:
            return Messages.bindParameters(Messages.Question_Do_you_want_to_reset_job_A_after_abortion, jobName);

        default:
            return "???"; // //$NON-NLS-1$
        }
    }
}
