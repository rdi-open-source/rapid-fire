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
import biz.rapidfire.core.exceptions.AutoReconnectErrorException;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.command.shared.CommandType;
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
import biz.rapidfire.core.model.dao.IFileCopyStatusDAO;
import biz.rapidfire.core.model.dao.IFilesDAO;
import biz.rapidfire.core.model.dao.IJobsDAO;
import biz.rapidfire.core.model.dao.ILibrariesDAO;
import biz.rapidfire.core.model.dao.ILibraryListsDAO;
import biz.rapidfire.core.model.dao.INotificationsDAO;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
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

    public AS400 getHostSystem() {

        String connectionName = getConnectionName();

        try {

            IBMiConnection connection = IBMiConnection.getConnection(connectionName);
            if (connection == null) {
                return null;
            }

            return connection.getAS400ToolboxObject();

        } catch (Throwable e) {
            RapidFireCorePlugin.logError("*** Could not get 'system' of connection " + connectionName + " ***", e); //$NON-NLS-1$ //$NON-NLS-2$
        }

        return null;
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
                    job.setFilter(filter);
                    filteredJobs.addElement(job);
                }
            }

            return filteredJobs.toArray();

        } catch (AutoReconnectErrorException e) {
            MessageDialogAsync.displayError(e.getLocalizedMessage());
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
        List<IRapidFireJobResource> jobs = dao.load(this, shell);
        if (jobs == null) {
            return null;
        }

        return jobs.toArray(new IRapidFireJobResource[jobs.size()]);
    }

    public IRapidFireJobResource getJob(String libraryName, String jobName, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return null;
        }

        IJobsDAO dao = new JobsDAO(getConnectionName(), libraryName);
        IRapidFireJobResource job = dao.load(this, jobName, shell);
        if (job == null) {
            return null;
        }

        return job;
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

        IFilesDAO dao = new FilesDAO(getConnectionName(), libraryName);
        List<IRapidFireFileResource> files = dao.load(job, shell);
        if (files == null) {
            return null;
        }

        return files.toArray(new IRapidFireFileResource[files.size()]);
    }

    public IRapidFireFileResource getFile(IRapidFireJobResource job, int position, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return null;
        }

        String libraryName = job.getDataLibrary();

        IFilesDAO dao = new FilesDAO(getConnectionName(), libraryName);
        IRapidFireFileResource file = dao.load(job, position, shell);

        return file;
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

    public IRapidFireLibraryListResource getLibraryList(IRapidFireJobResource job, String libraryListName, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return null;
        }

        String dataLibraryName = job.getDataLibrary();

        ILibraryListsDAO dao = new LibraryListsDAO(getConnectionName(), dataLibraryName);
        IRapidFireLibraryListResource libraryList = dao.load(job, libraryListName, shell);

        return libraryList;
    }

    public IRapidFireLibraryResource[] getLibraries(IRapidFireJobResource job, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IRapidFireLibraryResource[0];
        }

        String libraryName = job.getDataLibrary();

        ILibrariesDAO dao = new LibrariesDAO(getConnectionName(), libraryName);
        List<IRapidFireLibraryResource> libraries = dao.load(job, shell);
        if (libraries == null) {
            return null;
        }

        return libraries.toArray(new IRapidFireLibraryResource[libraries.size()]);
    }

    public IRapidFireLibraryResource getLibrary(IRapidFireJobResource job, String libraryName, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return null;
        }

        String dataLibraryName = job.getDataLibrary();

        ILibrariesDAO dao = new LibrariesDAO(getConnectionName(), dataLibraryName);
        IRapidFireLibraryResource library = dao.load(job, libraryName, shell);

        return library;
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

    public IRapidFireNotificationResource getNotification(IRapidFireJobResource job, int position, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return null;
        }

        String libraryName = job.getDataLibrary();

        INotificationsDAO dao = new NotificationsDAO(getConnectionName(), libraryName);
        IRapidFireNotificationResource notification = dao.load(job, position, shell);

        return notification;
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

    public IRapidFireAreaResource getArea(IRapidFireFileResource file, String areaName, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return null;
        }

        IRapidFireJobResource job = file.getParentJob();
        String libraryName = job.getDataLibrary();

        IAreasDAO dao = new AreasDAO(getConnectionName(), libraryName);
        IRapidFireAreaResource area = dao.load(file, areaName, shell);

        return area;
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

    public IRapidFireConversionResource getConversion(IRapidFireFileResource file, String fieldToConvert, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return null;
        }

        IRapidFireJobResource job = file.getParentJob();
        String libraryName = job.getDataLibrary();

        IConversionsDAO dao = new ConversionsDAO(getConnectionName(), libraryName);
        IRapidFireConversionResource conversion = dao.load(file, fieldToConvert, shell);

        return conversion;
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

    public IRapidFireCommandResource getCommand(IRapidFireFileResource file, CommandType commandType, int sequence, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return null;
        }

        IRapidFireJobResource job = file.getParentJob();
        String libraryName = job.getDataLibrary();

        ICommandsDAO dao = new CommandsDAO(getConnectionName(), libraryName);
        IRapidFireCommandResource command = dao.load(file, commandType, sequence, shell);
        if (command == null) {
            return null;
        }

        return command;
    }

    public IFileCopyStatus[] getFileCopyStatus(IRapidFireJobResource job, Shell shell) throws Exception {

        if (!successFullyLoaded()) {
            return new IFileCopyStatus[0];
        }

        String libraryName = job.getDataLibrary();

        IFileCopyStatusDAO dao = new FileCopyStatusDAO(getConnectionName(), libraryName);
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