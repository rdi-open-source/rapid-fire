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
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.IProperty;
import org.eclipse.rse.core.model.IPropertySet;
import org.eclipse.rse.core.model.PropertySet;
import org.eclipse.rse.core.subsystems.CommunicationsEvent;
import org.eclipse.rse.core.subsystems.ICommunicationsListener;
import org.eclipse.rse.core.subsystems.IConnectorService;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.core.subsystems.SubSystem;
import org.eclipse.rse.services.clientserver.messages.SystemMessageException;
import org.eclipse.rse.ui.SystemBasePlugin;
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
import biz.rapidfire.core.model.dao.IFileCopyStatusDAO;
import biz.rapidfire.core.model.dao.IFilesDAO;
import biz.rapidfire.core.model.dao.IJobsDAO;
import biz.rapidfire.core.model.dao.ILibrariesDAO;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.queries.FileCopyStatus;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.RapidFireFilter;
import biz.rapidfire.rse.model.dao.FileCopyStatusDAO;
import biz.rapidfire.rse.model.dao.FilesDAO;
import biz.rapidfire.rse.model.dao.JobsDAO;
import biz.rapidfire.rse.model.dao.LibrariesDAO;
import biz.rapidfire.rse.model.dao.LibraryListsDAO;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.subsystems.qsys.IISeriesSubSystem;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;
import com.ibm.etools.iseries.subsystems.qsys.commands.QSYSCommandSubSystem;
import com.ibm.etools.iseries.subsystems.qsys.objects.QSYSObjectSubSystem;

public class RapidFireSubSystem extends SubSystem implements IISeriesSubSystem, IRapidFireSubSystem, ICommunicationsListener {

    private RapidFireSubSystemAttributes subSystemAttributes;
    private boolean isLoading;

    public RapidFireSubSystem(IHost host, IConnectorService connectorService) {
        super(host, connectorService);

        this.subSystemAttributes = new RapidFireSubSystemAttributes(this);
        this.isLoading = true;
        getConnectorService().addCommunicationsListener(this);

        new WorkbenchJob("Setting shell ...") { //$NON-NLS-1$

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
        return getHostAliasName();
    }

    @Override
    protected Object[] internalResolveFilterString(String filterString, IProgressMonitor monitor) throws InvocationTargetException,
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

        IJobsDAO dao = new JobsDAO(getConnectionName(), libraryName);
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

        IFilesDAO dao = new FilesDAO(getConnectionName(), libraryName);
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

        ILibrariesDAO dao = new LibrariesDAO(getConnectionName(), libraryName);
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

        IFileCopyStatusDAO dao = new FileCopyStatusDAO(getConnectionName(), libraryName);
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
            MessageDialogAsync.displayError("*** Could not successfully load the Rapid Fire subsystem ***"); //$NON-NLS-1$
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
    protected Object[] internalResolveFilterString(Object parent, String filterString, IProgressMonitor monitor) throws InvocationTargetException,
        InterruptedException {

        return internalResolveFilterString(filterString, monitor);
    }

    public QSYSObjectSubSystem getCommandExecutionProperties() {
        return IBMiConnection.getConnection(getHost()).getQSYSObjectSubSystem();
    }

    public QSYSCommandSubSystem getCmdSubSystem() {

        IHost iHost = getHost();
        ISubSystem[] iSubSystems = iHost.getSubSystems();
        for (int ssIndx = 0; ssIndx < iSubSystems.length; ssIndx++) {
            SubSystem subsystem = (SubSystem)iSubSystems[ssIndx];
            if ((subsystem instanceof QSYSCommandSubSystem)) {
                return (QSYSCommandSubSystem)subsystem;
            }
        }
        return null;
    }

    public ISubSystem getObjectSubSystem() {

        IHost iHost = getHost();
        ISubSystem[] iSubSystems = iHost.getSubSystems();
        for (int ssIndx = 0; ssIndx < iSubSystems.length; ssIndx++) {
            ISubSystem iSubSystem = iSubSystems[ssIndx];
            if ((iSubSystem instanceof QSYSObjectSubSystem)) {
                return iSubSystem;
            }
        }

        return null;
    }

    public AS400 getToolboxAS400Object() {

        try {
            return IBMiConnection.getConnection(getHost()).getAS400ToolboxObject();
        } catch (SystemMessageException e) {
            RapidFireCorePlugin.logError(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public String getVendorAttribute(String key) {

        IProperty property = getVendorAttributes().getProperty(key);
        if (property == null) {
            return null;
        }

        return property.getValue();
    }

    public void setVendorAttribute(String key, String value) {
        getVendorAttributes().addProperty(key, value);
    }

    public void removeVendorAttribute(String key) {
        getVendorAttributes().removeProperty(key);
    }

    private IPropertySet getVendorAttributes() {

        IPropertySet propertySet = getPropertySet(RapidFireSubSystemAttributes.VENDOR_ID);
        if (propertySet == null) {
            propertySet = new PropertySet(RapidFireSubSystemAttributes.VENDOR_ID);
            addPropertySet(propertySet);
        }

        return propertySet;
    }

    public void communicationsStateChange(CommunicationsEvent event) {

        String connectionName = event.getSystem().getHost().getAliasName();

        if (event.getState() == CommunicationsEvent.AFTER_CONNECT) {
            JDBCConnectionManager.getInstance().connected(connectionName);
        } else if (event.getState() == CommunicationsEvent.BEFORE_DISCONNECT) {
            JDBCConnectionManager.getInstance().disconnected(connectionName);
        }
    }

    public boolean isPassiveCommunicationsListener() {
        return true;
    }
}