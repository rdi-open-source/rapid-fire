/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.action.ConfirmStartJobActionDialog;
import biz.rapidfire.core.dialogs.action.FieldsWithGeneratedClauseDialog;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.model.IRapidFireJobResource;

public class StartJobHandler extends AbstractJobActionHandler {

    public StartJobHandler() {
        super(JobAction.STRJOB);
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {
    	
    	String _version = RapidFireHelper.getRapidFireServerVersion(getShell(), getManager().getDao().getSystem(), getManager().getDao().getLibraryName());
    	
    	if (_version == null) {

    		MessageBox errorBox = new MessageBox(getShell(), SWT.ICON_ERROR);
			errorBox.setText(Messages.Label_Job_colon + job);
			errorBox.setMessage(Messages.Could_not_determine_the_version_of_the_Rapid_Fire_library);
			errorBox.open();
    		
			return;
			
    	}
    	
        ConfirmStartJobActionDialog dialog = new ConfirmStartJobActionDialog(getShell(), job.getName(), _version);
        dialog.open();
        if (dialog.isConfirmed()) {

        	if (_version.compareTo("060000") >= 0) {
            	
                int result = getManager().buildFieldsWithGeneratedClause(job.getKey());
                
                if (result < 0) {
                	
                	String _error = "FLDGENCLS_build(" + Integer.toString(result) + ")";
                	
        			MessageBox errorBox = new MessageBox(getShell(), SWT.ICON_ERROR);
        			errorBox.setText(Messages.Label_Job_colon + job);
        			errorBox.setMessage(Messages.Internal_error_colon + _error);
        			errorBox.open();

        			return;
                	
                }
                else if (result == 0) {
                	// Nothing to do
                }
                else if (result > 0) {
                    
                	FieldsWithGeneratedClauseDialog dialogGenerated = new FieldsWithGeneratedClauseDialog(getShell(), getManager().getDao(), job);
                	dialogGenerated.open();
                    if (!dialogGenerated.isContinue()) {
                    	return;
                    }
                	
                }
        		
        	}
            	
            getManager().startJob(job.getKey());

            job.reload(getShell());
            refreshUIChanged(job.getParentSubSystem(), job, job.getParentFilters());
            
        }
    }
}
