/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.progress.WorkbenchJob;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.IFileCopyStatus;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.queries.FileCopyStatus;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.RapidFireFilter;
import biz.rapidfire.rse.model.dao.FileCopyStatusDAO;
import biz.rapidfire.rse.model.dao.FilesDAO;
import biz.rapidfire.rse.model.dao.JobsDAO;
import biz.rapidfire.rse.model.dao.LibrariesDAO;
import biz.rapidfire.rse.model.dao.LibraryListsDAO;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.core.IISeriesSubSystem;
import com.ibm.etools.iseries.core.IISeriesSubSystemCommandExecutionProperties;
import com.ibm.etools.iseries.core.ISeriesSystemDataStore;
import com.ibm.etools.iseries.core.ISeriesSystemManager;
import com.ibm.etools.iseries.core.ISeriesSystemToolbox;
import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.systems.as400cmdsubsys.CmdSubSystem;
import com.ibm.etools.systems.as400cmdsubsys.impl.CmdSubSystemImpl;
import com.ibm.etools.systems.as400filesubsys.FileSubSystem;
import com.ibm.etools.systems.core.SystemBasePlugin;
import com.ibm.etools.systems.core.SystemPlugin;
import com.ibm.etools.systems.dftsubsystem.impl.DefaultSubSystemImpl;
import com.ibm.etools.systems.model.SystemConnection;
import com.ibm.etools.systems.model.SystemRegistry;
import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.impl.AbstractSystemManager;

public class RapidFireSubSystem extends DefaultSubSystemImpl implements IISeriesSubSystem, IRapidFireSubSystem {

    private RapidFireSubSystemAttributes subSystemAttributes;
    private boolean isLoading;

    public RapidFireSubSystem(SystemConnection connection) {
        super();

        this.subSystemAttributes = new RapidFireSubSystemAttributes(this);
        this.isLoading = true;

        new WorkbenchJob("") {

            @Override
            public IStatus runInUIThread(IProgressMonitor arg0) {

                IWorkbenchWindow win = SystemBasePlugin.getActiveWorkbenchWindow();
                if (win != null) {
                    setShell(SystemBasePlugin.getActiveWorkbenchShell());
                }

                isLoading = false;

                return Status.OK_STATUS;
            }

        }.schedule();
    }

    public RapidFireSubSystemAttributes getSubSystemAttributes() {
        return subSystemAttributes;
    }

    public String getConnectionName() {
        return getSystemConnectionName();
    }

    @Override
    protected Object[] internalResolveFilterString(IProgressMonitor monitor, String filterString) throws InvocationTargetException,
        InterruptedException {

        try {

            RapidFireFilter filter = new RapidFireFilter(filterString);
            IRapidFireJobResource[] allJobs = getJobs(filter.getDataLibrary(), getShell());
            if (allJobs == null) {
                return null;
            }

            Vector<IRapidFireResource> filteredJobs = new Vector<IRapidFireResource>();

            for (IRapidFireJobResource job : allJobs) {
                if (filter.matches(job)) {
                    filteredJobs.addElement(job);
                }
            }

            return filteredJobs.toArray();

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could resolve filter string and load jobs ***", e); //$NON-NLS-1$
            MessageDialogAsync.displayError(ExceptionHelper.getLocalizedMessage(e));
        }

        return null;
    }

    public IRapidFireJobResource[] getJobs(String libraryName, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireJobResource[0];
        }

        JobsDAO dao = new JobsDAO(getConnectionName(), libraryName);
        List<IRapidFireJobResource> jobs = dao.load(shell);
        if (jobs == null) {
            return null;
        }

        for (IRapidFireJobResource job : jobs) {
            job.setParentSubSystem(this);
        }

        return jobs.toArray(new IRapidFireJobResource[jobs.size()]);
    }

    public IRapidFireFileResource[] getFiles(String libraryName, String jobName, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireFileResource[0];
        }

        FilesDAO dao = new FilesDAO(getConnectionName(), libraryName);
        List<IRapidFireFileResource> files = dao.load(jobName, shell);
        if (files == null) {
            return null;
        }

        for (IRapidFireFileResource file : files) {
            file.setParentSubSystem(this);
        }

        return files.toArray(new IRapidFireFileResource[files.size()]);
    }

    public IRapidFireLibraryListResource[] getLibraryLists(String libraryName, String jobName, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireLibraryListResource[0];
        }

        LibraryListsDAO dao = new LibraryListsDAO(getConnectionName(), libraryName);
        List<IRapidFireLibraryListResource> libraryLists = dao.load(jobName, shell);
        if (libraryLists == null) {
            return null;
        }

        for (IRapidFireLibraryListResource libraryList : libraryLists) {
            libraryList.setParentSubSystem(this);
        }

        return libraryLists.toArray(new IRapidFireLibraryListResource[libraryLists.size()]);
    }

    public IRapidFireLibraryResource[] getLibraries(String libraryName, String jobName, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireLibraryResource[0];
        }

        LibrariesDAO dao = new LibrariesDAO(getConnectionName(), libraryName);
        List<IRapidFireLibraryResource> libraries = dao.load(jobName, shell);
        if (libraries == null) {
            return null;
        }

        for (IRapidFireLibraryResource library : libraries) {
            library.setParentSubSystem(this);
        }

        return libraries.toArray(new IRapidFireLibraryResource[libraries.size()]);
    }

    public IFileCopyStatus[] getFileCopyStatus(String libraryName, String jobName, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IFileCopyStatus[0];
        }

        FileCopyStatusDAO dao = new FileCopyStatusDAO(getHostName(), libraryName);
        List<IFileCopyStatus> fileCopyStatuses = dao.load(jobName, shell);
        if (fileCopyStatuses == null) {
            return null;
        }

        return fileCopyStatuses.toArray(new FileCopyStatus[fileCopyStatuses.size()]);
    }

    private boolean successFullyLoaded() {

        if (isLoading) {

            final int SLEEP_TIME = 250;
            int maxTime = 30 * 1000 / SLEEP_TIME;

            while (isLoading && maxTime > 0) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                }
            }
        }

        if (getShell() == null) {
            MessageDialogAsync.displayError("*** Could not successfully load the Rapid Fire subsystem ***");
            return false;
        }

        return true;
    }

    @Override
    protected Object[] sortResolvedFilterStringObjects(Object[] input) {

        Arrays.sort(input);

        return input;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    @Override
    public Shell getShell() {
        // Damn, this caused me a lot of grief! Phil
        if (shell != null) {
            return shell;
        } else {
            return super.getShell();
        }
    }

    /*
     * Start of RDi/WDSCi specific methods.
     */

    @Override
    protected Object[] internalResolveFilterString(IProgressMonitor monitor, Object parent, String filterString) throws InvocationTargetException,
        InterruptedException {

        return internalResolveFilterString(monitor, filterString);
    }

    public IISeriesSubSystemCommandExecutionProperties getCommandExecutionProperties() {
        return getObjectSubSystem();
    }

    public CmdSubSystem getCmdSubSystem() {

        SystemConnection sc = getSystemConnection();
        SystemRegistry registry = SystemPlugin.getTheSystemRegistry();
        SubSystem[] subsystems = registry.getSubSystems(sc);
        SubSystem subsystem;

        for (int ssIndx = 0; ssIndx < subsystems.length; ssIndx++) {
            subsystem = subsystems[ssIndx];
            if (subsystem instanceof CmdSubSystemImpl) {
                return (CmdSubSystemImpl)subsystem;
            }
        }

        return null;
    }

    public FileSubSystem getObjectSubSystem() {
        return ISeriesConnection.getConnection(getSystemConnection()).getISeriesFileSubSystem();
    }

    public AS400 getToolboxAS400Object() {
        ISeriesSystemToolbox system = (ISeriesSystemToolbox)getSystem();
        return system.getAS400Object();
    }

    @Override
    public AbstractSystemManager getSystemManager() {
        return ISeriesSystemManager.getTheISeriesSystemManager();
    }

    public ISeriesSystemDataStore getISeriesSystem() {
        ISeriesSystemDataStore iSeriesSystemDataStore = (ISeriesSystemDataStore)getSystem();
        return iSeriesSystemDataStore;
    }

    public String getVendorAttribute(String key) {
        return super.getVendorAttribute(RapidFireSubSystemAttributes.VENDOR_ID, key);
    }

    public void setVendorAttribute(String key, String value) {
        super.setVendorAttribute(RapidFireSubSystemAttributes.VENDOR_ID, key, value);
    }

    public void removeVendorAttribute(String key) {
        removeVendorAttribute(key);
    }
}