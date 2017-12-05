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
import biz.rapidfire.core.model.IRapidFireActivityResource;
import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.IActivitiesDAO;
import biz.rapidfire.core.model.dao.IAreasDAO;
import biz.rapidfire.core.model.dao.ICommandsDAO;
import biz.rapidfire.core.model.dao.IConversionsDAO;
import biz.rapidfire.core.model.dao.INotificationsDAO;
import biz.rapidfire.core.model.queries.FileCopyStatus;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.RapidFireFilter;
import biz.rapidfire.rse.model.dao.ActivitiesDAO;
import biz.rapidfire.rse.model.dao.AreasDAO;
import biz.rapidfire.rse.model.dao.CommandsDAO;
import biz.rapidfire.rse.model.dao.ConversionsDAO;
import biz.rapidfire.rse.model.dao.FileCopyStatusDAO;
import biz.rapidfire.rse.model.dao.FilesDAO;
import biz.rapidfire.rse.model.dao.JobsDAO;
import biz.rapidfire.rse.model.dao.LibrariesDAO;
import biz.rapidfire.rse.model.dao.LibraryListsDAO;
import biz.rapidfire.rse.model.dao.NotificationsDAO;

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
                    job.setFilter(filter);
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
        List<IRapidFireJobResource> jobs = dao.load(this, shell);
        if (jobs == null) {
            return null;
        }

        return jobs.toArray(new IRapidFireJobResource[jobs.size()]);
    }

    public IRapidFireActivityResource[] getActivities(IRapidFireJobResource job, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireActivityResource[0];
        }

        String libraryName = job.getDataLibrary();

        IActivitiesDAO dao = new ActivitiesDAO(getConnectionName(), libraryName);
        List<IRapidFireActivityResource> activities = dao.load(job, shell);
        if (activities == null) {
            return null;
        }

        return activities.toArray(new IRapidFireActivityResource[activities.size()]);
    }

    public IRapidFireFileResource[] getFiles(IRapidFireJobResource job, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireFileResource[0];
        }

        String libraryName = job.getDataLibrary();

        FilesDAO dao = new FilesDAO(getConnectionName(), libraryName);
        List<IRapidFireFileResource> files = dao.load(job, shell);
        if (files == null) {
            return null;
        }

        return files.toArray(new IRapidFireFileResource[files.size()]);
    }

    public IRapidFireLibraryListResource[] getLibraryLists(IRapidFireJobResource job, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireLibraryListResource[0];
        }

        String libraryName = job.getDataLibrary();

        LibraryListsDAO dao = new LibraryListsDAO(getConnectionName(), libraryName);
        List<IRapidFireLibraryListResource> libraryLists = dao.load(job, shell);
        if (libraryLists == null) {
            return null;
        }

        return libraryLists.toArray(new IRapidFireLibraryListResource[libraryLists.size()]);
    }

    public IRapidFireLibraryResource[] getLibraries(IRapidFireJobResource job, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireLibraryResource[0];
        }

        String libraryName = job.getDataLibrary();

        LibrariesDAO dao = new LibrariesDAO(getConnectionName(), libraryName);
        List<IRapidFireLibraryResource> libraries = dao.load(job, shell);
        if (libraries == null) {
            return null;
        }

        return libraries.toArray(new IRapidFireLibraryResource[libraries.size()]);
    }

    public IRapidFireNotificationResource[] getNotifications(IRapidFireJobResource job, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireNotificationResource[0];
        }

        String libraryName = job.getDataLibrary();

        INotificationsDAO dao = new NotificationsDAO(getConnectionName(), libraryName);
        List<IRapidFireNotificationResource> notification = dao.load(job, shell);
        if (notification == null) {
            return null;
        }

        return notification.toArray(new IRapidFireNotificationResource[notification.size()]);
    }

    public IRapidFireAreaResource[] getAreas(IRapidFireFileResource file, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireAreaResource[0];
        }

        IRapidFireJobResource job = file.getParentJob();
        String libraryName = job.getDataLibrary();

        IAreasDAO dao = new AreasDAO(getConnectionName(), libraryName);
        List<IRapidFireAreaResource> areas = dao.load(file, shell);
        if (areas == null) {
            return null;
        }

        return areas.toArray(new IRapidFireAreaResource[areas.size()]);
    }

    public IRapidFireConversionResource[] getConversions(IRapidFireFileResource file, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireConversionResource[0];
        }

        IRapidFireJobResource job = file.getParentJob();
        String libraryName = job.getDataLibrary();

        IConversionsDAO dao = new ConversionsDAO(getConnectionName(), libraryName);
        List<IRapidFireConversionResource> conversions = dao.load(file, shell);
        if (conversions == null) {
            return null;
        }

        return conversions.toArray(new IRapidFireConversionResource[conversions.size()]);
    }

    public IRapidFireCommandResource[] getCommands(IRapidFireFileResource file, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireCommandResource[0];
        }

        IRapidFireJobResource job = file.getParentJob();
        String libraryName = job.getDataLibrary();

        ICommandsDAO dao = new CommandsDAO(getConnectionName(), libraryName);
        List<IRapidFireCommandResource> commands = dao.load(file, shell);
        if (commands == null) {
            return null;
        }

        return commands.toArray(new IRapidFireCommandResource[commands.size()]);
    }

    public IFileCopyStatus[] getFileCopyStatus(IRapidFireJobResource job, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IFileCopyStatus[0];
        }

        String libraryName = job.getDataLibrary();

        FileCopyStatusDAO dao = new FileCopyStatusDAO(getHostName(), libraryName);
        List<IFileCopyStatus> fileCopyStatuses = dao.load(job, shell);
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