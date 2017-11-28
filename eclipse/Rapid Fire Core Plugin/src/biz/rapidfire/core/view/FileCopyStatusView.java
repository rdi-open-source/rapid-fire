/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.view;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.action.RefreshViewIntervalAction;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.job.IJobFinishedListener;
import biz.rapidfire.core.model.IFileCopyStatus;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.queries.FileCopyStatus;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.swt.widgets.ProgressBarPainter;
import biz.rapidfire.core.view.listener.AutoRefreshViewCloseListener;

public class FileCopyStatusView extends ViewPart implements IPropertyChangeListener, IAutoRefreshView, IJobFinishedListener {

    public static final String ID = "biz.rapidfire.core.view.FileCopyStatusView"; //$NON-NLS-1$

    private static final int COLUMN_ID_FILE = 0;
    private static final int COLUMN_ID_LIBRARY = 1;
    private static final int COLUMN_ID_PROGRESS = 2;
    private static final int COLUMN_ID_RECORDS_IN_PRODUCTION_LIBRARY = 3;
    private static final int COLUMN_ID_RECORDS_IN_SHADOW_LIBRARY = 4;
    private static final int COLUMN_ID_RECORDS_TO_COPY = 5;
    private static final int COLUMN_ID_RECORDS_COPIED = 6;
    private static final int COLUMN_ID_ESTIMATED_TIME = 7;
    private static final int COLUMN_ID_CHANGES_TO_APPLY = 8;
    private static final int COLUMN_ID_CHANGES_APPLIED = 9;

    private IRapidFireJobResource inputData;
    private boolean inputDataAvailable;

    private TableViewer tvJobStatuses;
    private NumberFormat formatter = NumberFormat.getIntegerInstance();
    private ProgressBarPainter progressBarPainter;

    private Action actionRefreshView;
    private RefreshViewIntervalAction disableRefreshViewAction;
    private List<RefreshViewIntervalAction> refreshIntervalActions;
    private AutoRefreshJob autoRefreshJob;

    private int columnIds[];

    public FileCopyStatusView() {

        this.inputData = null;
        this.inputDataAvailable = false;
    }

    @Override
    public void init(IViewSite site) throws PartInitException {
        super.init(site);

        registerPreferencesListener();
    }

    @Override
    public void createPartControl(Composite parent) {

        parent.addDisposeListener(new AutoRefreshViewCloseListener(this));

        Composite mainArea = new Composite(parent, SWT.NONE);
        mainArea.setLayout(new GridLayout());
        mainArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        createJobStatusTable(mainArea);

        createActions();
        initializeToolBar();
        initializeViewMenu();
    }

    private void createJobStatusTable(Composite parent) {

        Composite jobStatusArea = new Composite(parent, SWT.NONE);
        jobStatusArea.setLayout(new FillLayout());
        jobStatusArea.setLayoutData(new GridData(GridData.FILL_BOTH));

        tvJobStatuses = new TableViewer(jobStatusArea, SWT.BORDER | SWT.FULL_SELECTION);
        final Table tblJobStatuses = tvJobStatuses.getTable();

        List<Integer> availableColumns = new LinkedList<Integer>();

        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_FILE, 80, Messages.ColumnLabel_File));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_LIBRARY, 80, Messages.ColumnLabel_Library));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_PROGRESS, 110, Messages.ColumnLabel_Progress));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_RECORDS_IN_PRODUCTION_LIBRARY, 110,
            Messages.ColumnLabel_Records_in_production_library));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_RECORDS_IN_SHADOW_LIBRARY, 110, Messages.ColumnLabel_Records_in_shadow_library));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_RECORDS_TO_COPY, 110, Messages.ColumnLabel_Records_to_copy));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_RECORDS_COPIED, 110, Messages.ColumnLabel_Records_copied));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_ESTIMATED_TIME, 110, Messages.ColumnLabel_Estimated_time));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_CHANGES_TO_APPLY, 110, Messages.ColumnLabel_Changes_to_apply));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_CHANGES_APPLIED, 110, Messages.ColumnLabel_Changes_applied));

        columnIds = new int[availableColumns.size()];
        for (int i = 0; i < availableColumns.size(); i++) {
            columnIds[i] = availableColumns.get(i);
        }

        // final TableColumn columnFile = new TableColumn(tblJobStatuses,
        // SWT.NONE);
        // columnFile.setWidth(80);
        // columnFile.setText(Messages.ColumnLabel_File);
        //
        // final TableColumn columnLibrary = new TableColumn(tblJobStatuses,
        // SWT.NONE);
        // columnLibrary.setWidth(80);
        // columnLibrary.setText(Messages.ColumnLabel_Library);
        //
        // final TableColumn columnProgress = new TableColumn(tblJobStatuses,
        // SWT.CENTER);
        // columnProgress.setWidth(110);
        // columnProgress.setText(Messages.ColumnLabel_Progress);
        // int columnIndexProgressBar = tblJobStatuses.getColumnCount() - 1;
        //
        // final TableColumn columnRecordsInProductionLibrary = new
        // TableColumn(tblJobStatuses, SWT.NONE);
        // columnRecordsInProductionLibrary.setWidth(110);
        // columnRecordsInProductionLibrary.setText(Messages.ColumnLabel_Records_in_production_library);
        //
        // final TableColumn columnRecordsInShadowLibrary = new
        // TableColumn(tblJobStatuses, SWT.NONE);
        // columnRecordsInShadowLibrary.setWidth(110);
        // columnRecordsInShadowLibrary.setText(Messages.ColumnLabel_Records_in_shadow_library);
        //
        // final TableColumn columnRecordsToCopy = new
        // TableColumn(tblJobStatuses, SWT.NONE);
        // columnRecordsToCopy.setWidth(110);
        // columnRecordsToCopy.setText(Messages.ColumnLabel_Records_to_copy);
        //
        // final TableColumn columnRecordsCopied = new
        // TableColumn(tblJobStatuses, SWT.NONE);
        // columnRecordsCopied.setWidth(110);
        // columnRecordsCopied.setText(Messages.ColumnLabel_Records_copied);
        //
        // final TableColumn columnEstimatedTime = new
        // TableColumn(tblJobStatuses, SWT.NONE);
        // columnEstimatedTime.setWidth(110);
        // columnEstimatedTime.setText(Messages.ColumnLabel_Estimated_time);
        //
        // final TableColumn columnChangesToApply = new
        // TableColumn(tblJobStatuses, SWT.NONE);
        // columnChangesToApply.setWidth(110);
        // columnChangesToApply.setText(Messages.ColumnLabel_Changes_to_apply);
        //
        // final TableColumn columnApplied = new TableColumn(tblJobStatuses,
        // SWT.NONE);
        // columnApplied.setWidth(110);
        // columnApplied.setText(Messages.ColumnLabel_Changes_applied);

        tblJobStatuses.setLinesVisible(true);
        tblJobStatuses.setHeaderVisible(true);

        progressBarPainter = new ProgressBarPainter(tblJobStatuses, getColumnIndex(COLUMN_ID_PROGRESS));
        tblJobStatuses.addListener(SWT.EraseItem, progressBarPainter);
        tblJobStatuses.addListener(SWT.PaintItem, progressBarPainter);

        tvJobStatuses.setLabelProvider(new JobStatusesLabelProvider());
        tvJobStatuses.setContentProvider(new JobStatusesContentProvider());
    }

    private int getColumnIndex(int columnId) {

        for (int i = 0; i < columnIds.length; i++) {
            if (columnIds[i] == columnId) {
                return i;
            }
        }

        return -1;
    }

    private Integer createColumn(Table tblJobStatuses, int columnId, int width, String label) {

        TableColumn columnFile = new TableColumn(tblJobStatuses, SWT.NONE);
        columnFile.setWidth(width);
        columnFile.setText(label);

        return new Integer(columnId);
    }

    private void createActions() {

        actionRefreshView = new Action("") { //$NON-NLS-1$
            @Override
            public void run() {
                IFileCopyStatus[] fileCopyStatuses = loadInputData(inputData);
                setInputInternally(fileCopyStatuses);
            }
        };
        actionRefreshView.setToolTipText(Messages.ActionTooltip_Refresh);
        actionRefreshView.setImageDescriptor(RapidFireCorePlugin.getDefault().getImageDescriptor(RapidFireCorePlugin.IMAGE_REFRESH));
        actionRefreshView.setEnabled(false);

        disableRefreshViewAction = new RefreshViewIntervalAction(this, -1);
        refreshIntervalActions = new ArrayList<RefreshViewIntervalAction>();
        refreshIntervalActions.add(new RefreshViewIntervalAction(this, 1));
        refreshIntervalActions.add(new RefreshViewIntervalAction(this, 3));
        refreshIntervalActions.add(new RefreshViewIntervalAction(this, 10));
        refreshIntervalActions.add(new RefreshViewIntervalAction(this, 30));

        refreshActionsEnablement();
    }

    private void initializeViewMenu() {

        IActionBars actionBars = getViewSite().getActionBars();
        IMenuManager viewMenu = actionBars.getMenuManager();

        viewMenu.add(createAuoRefreshSubMenu());
    }

    private MenuManager createAuoRefreshSubMenu() {

        MenuManager autoRefreshSubMenu = new MenuManager(Messages.ActionLabel_Auto_refresh_menu_item);
        autoRefreshSubMenu.add(disableRefreshViewAction);

        for (RefreshViewIntervalAction refreshAction : refreshIntervalActions) {
            autoRefreshSubMenu.add(refreshAction);
        }

        return autoRefreshSubMenu;
    }

    private void initializeToolBar() {

        IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
        toolbarManager.add(actionRefreshView);
    }

    @Override
    public void setFocus() {

    }

    public void setInput(IRapidFireJobResource job) {

        this.inputData = job;

        if (this.inputData != null) {
            BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                public void run() {
                    setInputInternally(loadInputData(FileCopyStatusView.this.inputData));
                }
            });
        } else {
            setInputInternally(null);
        }
    }

    private void setInputInternally(IFileCopyStatus[] fileCopyStatuses) {

        tvJobStatuses.setInput(fileCopyStatuses);

        if (fileCopyStatuses != null && fileCopyStatuses.length > 0) {
            inputDataAvailable = true;
        } else {
            inputDataAvailable = false;
        }

        refreshActionsEnablement();
    }

    private IFileCopyStatus[] loadInputData(IRapidFireJobResource job) {

        try {

            IRapidFireSubSystem subSystem = job.getParentSubSystem();

            IFileCopyStatus[] fileCopyStatuses = subSystem.getFileCopyStatus(job, getShell());

            return fileCopyStatuses;

        } catch (Exception e) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
            return null;
        }
    }

    private void refreshActionsEnablement() {

        if (!isDataAvailable() || isAutoRefreshOn()) {
            actionRefreshView.setEnabled(false);
        } else {
            actionRefreshView.setEnabled(true);
        }

        if (isAutoRefreshOn()) {
            disableRefreshViewAction.setEnabled(true);
        } else {
            disableRefreshViewAction.setEnabled(false);
        }

        for (RefreshViewIntervalAction refreshAction : refreshIntervalActions) {
            if (isAutoRefreshOn()) {
                if (autoRefreshJob.getInterval() == refreshAction.getInterval()) {
                    refreshAction.setEnabled(false);
                } else {
                    refreshAction.setEnabled(true);
                }
            } else {
                if (!isDataAvailable()) {
                    refreshAction.setEnabled(false);
                } else {
                    refreshAction.setEnabled(true);
                }
            }
        }
    }

    private boolean isAutoRefreshOn() {

        if (autoRefreshJob != null) {
            return true;
        }
        return false;
    }

    private boolean isDataAvailable() {
        return inputDataAvailable;
    }

    private Shell getShell() {
        return getViewSite().getShell();
    }

    private class JobStatusesLabelProvider extends LabelProvider implements ITableLabelProvider {

        private static final String PERCENT_SIGN = "%"; //$NON-NLS-1$

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {

            IFileCopyStatus fileCopyStatus = (IFileCopyStatus)element;

            int columnId = columnIds[columnIndex];

            switch (columnId) {
            case COLUMN_ID_FILE:
                return fileCopyStatus.getFile();
            case COLUMN_ID_LIBRARY:
                return fileCopyStatus.getLibrary();
            case COLUMN_ID_RECORDS_IN_PRODUCTION_LIBRARY:
                return formatter.format(fileCopyStatus.getRecordsInProductionLibrary());
            case COLUMN_ID_RECORDS_IN_SHADOW_LIBRARY:
                return formatter.format(fileCopyStatus.getRecordsInShadowLibrary());
            case COLUMN_ID_RECORDS_TO_COPY:
                return formatter.format(fileCopyStatus.getRecordsToCopy());
            case COLUMN_ID_RECORDS_COPIED:
                return formatter.format(fileCopyStatus.getRecordsCopied());
            case COLUMN_ID_ESTIMATED_TIME:
                return fileCopyStatus.getEstimatedTime();
            case COLUMN_ID_CHANGES_TO_APPLY:
                return formatter.format(fileCopyStatus.getChangesToApply());
            case COLUMN_ID_CHANGES_APPLIED:
                return formatter.format(fileCopyStatus.getChangesApplied());
            case COLUMN_ID_PROGRESS:
                return formatter.format(fileCopyStatus.getPercentDone()) + PERCENT_SIGN;
            default:
                return null;
            }
        }
    }

    private class JobStatusesContentProvider implements IStructuredContentProvider {

        private IFileCopyStatus[] fileCopyStatuses;

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

            if (newInput instanceof IFileCopyStatus[]) {
                fileCopyStatuses = (IFileCopyStatus[])newInput;
            } else {
                fileCopyStatuses = new FileCopyStatus[0];
            }
        }

        public Object[] getElements(Object input) {
            return fileCopyStatuses;
        }

    }

    private void registerPreferencesListener() {
        Preferences.getInstance().registerPreferencesListener(this);
    }

    private void removePreferencesListener() {
        Preferences.getInstance().removePreferencesListener(this);
    }

    public void propertyChange(PropertyChangeEvent event) {

        if (Preferences.PROGRESS_BAR_SIZE.equals(event.getProperty())) {
            progressBarPainter.enableLargeProgressBar((Boolean)event.getNewValue());
            tvJobStatuses.getTable().redraw();
        }
    }

    public void startClosingView() {

        if (autoRefreshJob != null) {
            autoRefreshJob.setJobFinishedListener(null);
            autoRefreshJob.cancel();
        }
    }

    public void setRefreshInterval(int seconds) {

        if (!isDataAvailable()) {
            seconds = RefreshViewIntervalAction.REFRESH_OFF;
        }

        if (autoRefreshJob != null) {

            if (seconds == RefreshViewIntervalAction.REFRESH_OFF) {
                autoRefreshJob.cancel();
            } else {
                autoRefreshJob.setInterval(seconds);
            }

            return;

        } else {

            autoRefreshJob = new AutoRefreshJob(this, FileCopyStatusView.this.inputData, seconds);
            autoRefreshJob.schedule();
            refreshActionsEnablement();
        }
    }

    public void jobFinished(Job job) {

        if (job == autoRefreshJob) {
            autoRefreshJob = null;
            refreshActionsEnablement();
        }
    }

    @Override
    public void dispose() {
        removePreferencesListener();
        super.dispose();
    }

    /**
     * Job, that periodically refreshes the content of the view.
     */
    private class AutoRefreshJob extends Job implements IJobFinishedListener {

        final int MILLI_SECONDS = 1000;

        private IJobFinishedListener jobFinishedListener;
        private IRapidFireJobResource job;
        private int interval;
        private UpdateDataUIJob updateDataUIJob;
        private int waitTime;

        public AutoRefreshJob(IJobFinishedListener listener, IRapidFireJobResource job, int seconds) {
            super(Messages.JobLabel_Refreshing_file_copy_statuses);
            this.jobFinishedListener = listener;
            this.job = job;
            setInterval(seconds);
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {

            final int SLEEP_INTERVAL = 50;

            while (!monitor.isCanceled()) {

                try {

                    /*
                     * Load copy statuses.
                     */
                    IFileCopyStatus[] fileCopyStatuses = loadInputData(job);

                    /*
                     * Create a UI job to update the view with the new data.
                     */
                    updateDataUIJob = new UpdateDataUIJob(fileCopyStatuses);
                    updateDataUIJob.setJobFinishedListener(this);
                    updateDataUIJob.schedule();

                    waitTime = interval;
                    while ((!monitor.isCanceled() && waitTime > 0) || updateDataUIJob != null) {
                        Thread.sleep(SLEEP_INTERVAL);
                        if (waitTime > interval) {
                            waitTime = interval;
                        }
                        if (waitTime > 0) {
                            waitTime = waitTime - SLEEP_INTERVAL;
                        }
                    }

                } catch (InterruptedException e) {
                    // exit the thread
                    break;
                } catch (Throwable e) {
                    RapidFireCorePlugin.logError(e.getMessage(), e);
                    MessageDialogAsync.displayError(getViewSite().getShell(), e.getLocalizedMessage());
                    break;
                }
            }

            if (jobFinishedListener != null) {
                jobFinishedListener.jobFinished(this);
            }

            return Status.OK_STATUS;
        }

        public int getInterval() {

            return interval / MILLI_SECONDS;
        }

        public void setInterval(int seconds) {

            interval = seconds * MILLI_SECONDS;
        }

        public void jobFinished(Job job) {

            if (job == updateDataUIJob) {
                updateDataUIJob = null;
            }
        }

        public void setJobFinishedListener(IJobFinishedListener listener) {
            this.jobFinishedListener = listener;
        }
    }

    /**
     * Job, that runs on the UI thread and which updates the view with the data
     * of the remote object.
     * <p>
     * It is the third and last job in a series of three.
     */
    private class UpdateDataUIJob extends UIJob {

        private IFileCopyStatus[] fileCopyStatuses;
        private IJobFinishedListener finishedListener;

        public UpdateDataUIJob(IFileCopyStatus[] fileCopyStatuses) {
            super(getViewSite().getShell().getDisplay(), ""); //$NON-NLS-1$
            this.fileCopyStatuses = fileCopyStatuses;
        }

        public void setJobFinishedListener(IJobFinishedListener listener) {
            this.finishedListener = listener;
        }

        @Override
        public IStatus runInUIThread(IProgressMonitor monitor) {

            setInputInternally(fileCopyStatuses);

            if (finishedListener != null) {
                finishedListener.jobFinished(this);
            }

            return Status.OK_STATUS;
        }

    }
}
