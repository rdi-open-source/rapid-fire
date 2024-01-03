/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.wizard.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.exceptions.AutoReconnectErrorException;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.Status;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

public abstract class WizardDataModel {

    protected static final String EMPTY = ""; //$NON-NLS-1$

    // Data library page
    private String connectionName;
    private String dataLibraryName;

    // Common
    private String jobName;

    // Connection and data library dependent resources
    private IRapidFireJobResource[] jobResources;

    // Job dependent resources
    private IRapidFireJobResource jobResource;
    private IRapidFireLibraryResource[] libraryResources;
    private IRapidFireLibraryListResource[] libraryListResources;

    protected WizardDataModel() {
    }

    public void initialize() {

        setConnectionName(EMPTY);
        setDataLibraryName(EMPTY);
        setJobName(EMPTY);
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {

        if (!hasConnectionChanged(connectionName)) {
            return;
        }

        this.connectionName = connectionName;

        clearConnectionDependantResources();
    }

    public String getDataLibraryName() {
        return dataLibraryName;
    }

    public void setDataLibraryName(String dataLibraryName) {

        if (!hasDataLibraryChanged(dataLibraryName)) {
            return;
        }

        this.dataLibraryName = dataLibraryName;

        clearDataLibraryDependantResources();
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {

        if (!hasJobChanged(jobName)) {
            return;
        }

        this.jobName = jobName;

        clearJobDependantResources();
    }

    public IRapidFireJobResource getJob() {

        if (jobResource == null) {
            jobResource = loadJobResource();
        }

        return jobResource;
    }

    public IRapidFireJobResource[] getJobs() {

        if (jobResources == null) {
            jobResources = loadJobResources();
        }

        return jobResources;
    }

    public IRapidFireLibraryResource[] getLibraries() {

        if (libraryResources == null) {
            libraryResources = loadLibraryResources();
        }

        return libraryResources;
    }

    public IRapidFireLibraryResource getLibrary(String libraryName) {

        if (!StringHelper.isNullOrEmpty(libraryName)) {
            IRapidFireLibraryResource[] libraryResources = getLibraries();
            for (IRapidFireLibraryResource libraryResource : libraryResources) {
                if (libraryResource.getName().equals(libraryName)) {
                    return libraryResource;
                }
            }
        }

        return null;
    }

    public IRapidFireLibraryListResource[] getLibraryLists() {

        if (libraryListResources == null) {
            libraryListResources = loadLibraryListResources();
        }

        return libraryListResources;
    }

    private boolean hasConnectionChanged(String newConnectionName) {

        if (this.connectionName == null || !this.connectionName.equals(newConnectionName)) {
            return true;
        }

        return false;
    }

    private boolean hasDataLibraryChanged(String newDataLibraryName) {

        if (this.dataLibraryName == null || !this.dataLibraryName.equals(newDataLibraryName)) {
            return true;
        }

        return false;
    }

    private boolean hasJobChanged(String newJobName) {

        if (this.jobName == null || !this.jobName.equals(newJobName)) {
            return true;
        }

        return false;
    }

    private void clearConnectionDependantResources() {

        clearDataLibraryDependantResources();
    }

    private void clearDataLibraryDependantResources() {

        this.jobResources = null;

        clearJobDependantResources();
    }

    private void clearJobDependantResources() {

        this.jobResource = null;
        this.libraryResources = null;
        this.libraryListResources = null;
    }

    private IRapidFireJobResource loadJobResource() {

        if (StringHelper.isNullOrEmpty(connectionName) || StringHelper.isNullOrEmpty(dataLibraryName) || StringHelper.isNullOrEmpty(jobName)) {
            return null;
        }

        IRapidFireSubSystem subSystem = (IRapidFireSubSystem)SystemConnectionHelper.getSubSystem(connectionName, IRapidFireSubSystem.class);
        if (subSystem == null) {
            return null;
        }

        Shell shell = Display.getCurrent().getActiveShell();

        try {
            jobResource = subSystem.getJob(dataLibraryName, jobName, shell);
        } catch (Exception e) {
            MessageDialog.openError(shell, Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        }

        return jobResource;
    }

    private IRapidFireJobResource[] loadJobResources() {

        IRapidFireSubSystem subSystem = (IRapidFireSubSystem)SystemConnectionHelper.getSubSystem(connectionName, IRapidFireSubSystem.class);
        if (subSystem == null) {
            return null;
        }

        Shell shell = Display.getCurrent().getActiveShell();

        try {

            IRapidFireJobResource[] allJobs = subSystem.getJobs(dataLibraryName, shell);

            List<IRapidFireJobResource> filteredJobs = new ArrayList<IRapidFireJobResource>();

            if (allJobs != null) {
                for (IRapidFireJobResource job : allJobs) {
                    if (Status.RDY.equals(job.getStatus())) {
                        filteredJobs.add(job);
                    }
                }
            }

            jobResources = filteredJobs.toArray(new IRapidFireJobResource[filteredJobs.size()]);

        } catch (AutoReconnectErrorException e) {
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        } catch (Exception e) {
            MessageDialog.openError(shell, Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        }

        return jobResources;
    }

    private IRapidFireLibraryResource[] loadLibraryResources() {

        IRapidFireSubSystem subSystem = (IRapidFireSubSystem)SystemConnectionHelper.getSubSystem(connectionName, IRapidFireSubSystem.class);
        if (subSystem == null) {
            return null;
        }

        Shell shell = Display.getCurrent().getActiveShell();

        try {
            libraryResources = subSystem.getLibraries(getJob(), shell);
        } catch (Exception e) {
            MessageDialog.openError(shell, Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        }

        return libraryResources;
    }

    private IRapidFireLibraryListResource[] loadLibraryListResources() {

        IRapidFireSubSystem subSystem = (IRapidFireSubSystem)SystemConnectionHelper.getSubSystem(connectionName, IRapidFireSubSystem.class);
        if (subSystem == null) {
            return null;
        }

        Shell shell = Display.getCurrent().getActiveShell();

        try {
            libraryListResources = subSystem.getLibraryLists(getJob(), shell);
        } catch (Exception e) {
            MessageDialog.openError(shell, Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        }

        return libraryListResources;
    }
}
