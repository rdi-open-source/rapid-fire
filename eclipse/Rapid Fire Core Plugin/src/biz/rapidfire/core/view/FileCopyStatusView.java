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

import org.eclipse.core.commands.ExecutionException;
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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.action.RefreshViewIntervalAction;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.exceptions.AutoReconnectErrorException;
import biz.rapidfire.core.handlers.reapplychanges.ReapplyChangesHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.job.IJobFinishedListener;
import biz.rapidfire.core.model.IFileCopyStatus;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.queries.FileCopyStatus;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.swt.widgets.ProgressBarPainter;
import biz.rapidfire.core.view.listener.AutoRefreshViewCloseListener;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

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
    private static final int COLUMN_ID_RECORDS_WITH_DUPLICATE_KEY = 8;
    private static final int COLUMN_ID_CHANGES_TO_APPLY = 9;
    private static final int COLUMN_ID_CHANGES_APPLIED = 10;

    private IRapidFireJobResource inputData;
    private boolean inputDataAvailable;

    private TableViewer tvJobStatuses;
    private NumberFormat formatter = NumberFormat.getIntegerInstance();
    private ProgressBarPainter progressBarPainter;

    private Action actionRefreshView;
    private RefreshViewIntervalAction disableRefreshViewAction;
    private List<RefreshViewIntervalAction> refreshIntervalActions;
    private AutoRefreshJob autoRefreshJob;

    private Action reapplyChangesAction;

    private ReapplyChangesHandler handler;

    private int columnIds[];
    
    private Text textConnection;
    private Text textJob;
    private Text textStatus;
    private Text textPhase;
    
    public FileCopyStatusView() {

        this.inputData = null;
        this.inputDataAvailable = false;
        this.handler = new ReapplyChangesHandler();
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
        initializeTableItemMenu();
    }

    private void createJobStatusTable(Composite parent) {

        Composite jobStatusHeader = new Composite(parent, SWT.NONE);
        jobStatusHeader.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
  
        GridLayout gridLayoutJobStatusHeader = new GridLayout();
        gridLayoutJobStatusHeader.numColumns = 8;
        jobStatusHeader.setLayout(gridLayoutJobStatusHeader);
        
        Label labelConnection = new Label(jobStatusHeader, SWT.NONE);
        labelConnection.setText(Messages.Label_Connection_colon);
        
        textConnection = new Text(jobStatusHeader, SWT.BORDER);
        textConnection.setLayoutData(new GridData(100, SWT.DEFAULT));
        textConnection.setText("./.");
        textConnection.setEditable(false);
        
        Label labelJob = new Label(jobStatusHeader, SWT.NONE);
        labelJob.setText(Messages.Label_Job_colon);
        
        textJob = new Text(jobStatusHeader, SWT.BORDER);
        textJob.setLayoutData(new GridData(100, SWT.DEFAULT));
        textJob.setText("./.");
        textJob.setEditable(false);
        
        Label labelStatus = new Label(jobStatusHeader, SWT.NONE);
        labelStatus.setText(Messages.Label_Status_colon);
        
        textStatus = new Text(jobStatusHeader, SWT.BORDER);
        textStatus.setLayoutData(new GridData(100, SWT.DEFAULT));
        textStatus.setText("./.");
        textStatus.setEditable(false);
        
        Label labelPhase = new Label(jobStatusHeader, SWT.NONE);
        labelPhase.setText(Messages.Label_Phase_colon);
        
        textPhase = new Text(jobStatusHeader, SWT.BORDER);
        textPhase.setLayoutData(new GridData(100, SWT.DEFAULT));
        textPhase.setText("./.");
        textPhase.setEditable(false);
        
        Composite jobStatusArea = new Composite(parent, SWT.NONE);
        jobStatusArea.setLayout(new FillLayout());
        jobStatusArea.setLayoutData(new GridData(GridData.FILL_BOTH));

        tvJobStatuses = new TableViewer(jobStatusArea, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        Table tblJobStatuses = tvJobStatuses.getTable();

        List<Integer> availableColumns = new LinkedList<Integer>();

        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_FILE, 110, Messages.ColumnLabel_File));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_LIBRARY, 110, Messages.ColumnLabel_Library));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_RECORDS_IN_PRODUCTION_LIBRARY, 110, Messages.ColumnLabel_Records_in_production_library));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_RECORDS_IN_SHADOW_LIBRARY, 110, Messages.ColumnLabel_Records_in_shadow_library));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_RECORDS_TO_COPY, 110, Messages.ColumnLabel_Records_to_copy));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_RECORDS_COPIED, 110, Messages.ColumnLabel_Records_copied));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_ESTIMATED_TIME, 110, Messages.ColumnLabel_Estimated_time));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_RECORDS_WITH_DUPLICATE_KEY, 110, Messages.ColumnLabel_Records_with_duplicate_key));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_CHANGES_TO_APPLY, 110, Messages.ColumnLabel_Changes_to_apply));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_CHANGES_APPLIED, 110, Messages.ColumnLabel_Changes_applied));
        availableColumns.add(createColumn(tblJobStatuses, COLUMN_ID_PROGRESS, 440, Messages.ColumnLabel_Progress));

        columnIds = new int[availableColumns.size()];
        for (int i = 0; i < availableColumns.size(); i++) {
            columnIds[i] = availableColumns.get(i);
        }

        tblJobStatuses.setLinesVisible(true);
        tblJobStatuses.setHeaderVisible(true);

        progressBarPainter = new ProgressBarPainter(tblJobStatuses, getColumnIndex(COLUMN_ID_PROGRESS));
        tblJobStatuses.addListener(SWT.EraseItem, progressBarPainter);
        tblJobStatuses.addListener(SWT.PaintItem, progressBarPainter);

        tblJobStatuses.addSelectionListener(new TableSelectionChangedListener());

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

        actionRefreshView = new Action() {
            @Override
            public void run() {
                IFileCopyStatus[] fileCopyStatuses = loadInputData(inputData);
                setInputInternally(fileCopyStatuses);

                try {
                    inputData.reload(getShell());
                    SystemConnectionHelper.refreshUIChanged(Preferences.getInstance().isSlowConnection(), inputData.getParentSubSystem(), inputData,
                        inputData.getParentFilters());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        actionRefreshView.setText(Messages.ActionLabel_Refresh);
        actionRefreshView.setToolTipText(Messages.ActionTooltip_Refresh);
        actionRefreshView.setImageDescriptor(RapidFireCorePlugin.getDefault().getImageDescriptor(RapidFireCorePlugin.IMAGE_REFRESH));
        actionRefreshView.setEnabled(false);

        disableRefreshViewAction = new RefreshViewIntervalAction(this, -1);
        refreshIntervalActions = new ArrayList<RefreshViewIntervalAction>();
        refreshIntervalActions.add(new RefreshViewIntervalAction(this, 1));
        refreshIntervalActions.add(new RefreshViewIntervalAction(this, 3));
        refreshIntervalActions.add(new RefreshViewIntervalAction(this, 10));
        refreshIntervalActions.add(new RefreshViewIntervalAction(this, 30));

        reapplyChangesAction = new Action() {
            @Override
            public void run() {
                try {
                    handler.executeWithSelection(tvJobStatuses.getSelection());
                } catch (ExecutionException e1) {
                    RapidFireCorePlugin.logError("*** Could not execute 'ReapplyChangesHandler'  ***", e1);
                }
            }
        };
        reapplyChangesAction.setText(Messages.ActionLabel_Reapply_changes);
        reapplyChangesAction.setToolTipText(Messages.ActionTooltip_Reapply_changes);
        reapplyChangesAction.setImageDescriptor(RapidFireCorePlugin.getDefault().getImageDescriptor(RapidFireCorePlugin.IMAGE_REAPPLY_CHANGES));
        reapplyChangesAction.setEnabled(false);

        refreshActionsEnablement();
    }

    private void initializeViewMenu() {

        IActionBars actionBars = getViewSite().getActionBars();
        IMenuManager viewMenu = actionBars.getMenuManager();

        viewMenu.add(createAuoRefreshSubMenu());
        // TODO: decide whether or not to keep "Reapply Changes" action
        // viewMenu.add(reapplyChangesAction);
    }

    private void initializeTableItemMenu() {

        // TODO: decide whether or not to keep "Reapply Changes" action
        // Table tblJobStatuses = tvJobStatuses.getTable();
        // Menu jobStatusesPopupMenu = new Menu(tblJobStatuses);
        // jobStatusesPopupMenu.addMenuListener(new
        // JobStatusItemPopupMenu(handler));
        // tblJobStatuses.setMenu(jobStatusesPopupMenu);

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
        // TODO: decide whether or not to keep "Reapply Changes" action
        // toolbarManager.add(reapplyChangesAction);
        toolbarManager.add(actionRefreshView);
    }

    @Override
    public void setFocus() {

    }

    public void setInput(IRapidFireJobResource job) {

    	textConnection.setFocus();
    	
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

        } catch (AutoReconnectErrorException e) {
            MessageDialogAsync.displayError(e.getLocalizedMessage());
            return null;
        } catch (Exception e) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
            RapidFireCorePlugin.logError("*** Could not load file copy statuses ***", e);
            return null;
        }
    }

    private void refreshActionsEnablement() {

        handler.setEnabled(getSelectedTableItems(tvJobStatuses.getTable()));

        if (!isDataAvailable() || isAutoRefreshOn()) {
            actionRefreshView.setEnabled(false);
            textConnection.setText("./.");
            textJob.setText("./.");
            textStatus.setText("./.");
            textPhase.setText("./.");
        } else {
            actionRefreshView.setEnabled(true);
            textConnection.setText(inputData.getParentSubSystem().getConnectionName());
            textJob.setText(inputData.getName());
            textStatus.setText(inputData.getStatus().label());
            textPhase.setText(inputData.getPhase().label());
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

        reapplyChangesAction.setEnabled(handler.isEnabled());
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

    private ISelection getSelectedTableItems(Table table) {

        List<FileCopyStatus> selectedItems = new LinkedList<FileCopyStatus>();

        TableItem[] tableItems = table.getSelection();
        for (TableItem tableItem : tableItems) {
            if (tableItem.getData() instanceof FileCopyStatus) {
                FileCopyStatus item = (FileCopyStatus)tableItem.getData();
                selectedItems.add(item);
            }
        }

        return new StructuredSelection(selectedItems);
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
            case COLUMN_ID_RECORDS_WITH_DUPLICATE_KEY:
                return formatter.format(fileCopyStatus.getRecordsWithDuplicateKey());
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

        if (Preferences.APPEARANCE_PROGRESS_BAR_SIZE.equals(event.getProperty())) {
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

    public void jobFinished(final Job job) {

        if (job != autoRefreshJob) {
            return;
        }

        new UIJob("") {

            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {

                autoRefreshJob = null;
                refreshActionsEnablement();

                return Status.OK_STATUS;
            }
        }.schedule();
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

            try {
                inputData.reload(getShell());
                SystemConnectionHelper.refreshUIChanged(Preferences.getInstance().isSlowConnection(), inputData.getParentSubSystem(), inputData,
                    inputData.getParentFilters());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (finishedListener != null) {
                finishedListener.jobFinished(this);
            }

            return Status.OK_STATUS;
        }

    }

    private class TableSelectionChangedListener implements SelectionListener {

        public void widgetSelected(SelectionEvent event) {
            refreshActionsEnablement();
        }

        public void widgetDefaultSelected(SelectionEvent event) {
            widgetSelected(event);
        }
    }

    private class JobStatusItemPopupMenu extends MenuAdapter {

        private ReapplyChangesHandler handler;
        private Menu menu;

        private MenuItem menuItemReapplyChanges;

        public JobStatusItemPopupMenu(ReapplyChangesHandler handler) {
            this.handler = handler;
        }

        @Override
        public void menuShown(MenuEvent event) {

            this.menu = (Menu)event.getSource();

            destroyMenuItems();
            createMenuItems();
        }

        public void destroyMenuItems() {

            if (!((menuItemReapplyChanges == null) || (menuItemReapplyChanges.isDisposed()))) {
                menuItemReapplyChanges.dispose();
            }
        }

        public void createMenuItems() {
            createMenuItemReapplyChanges();
        }

        public void createMenuItemReapplyChanges() {

            menuItemReapplyChanges = new MenuItem(this.menu, SWT.NONE);
            menuItemReapplyChanges.setText(Messages.ActionLabel_Reapply_changes);
            menuItemReapplyChanges.setImage(RapidFireCorePlugin.getDefault().getImageRegistry().get(RapidFireCorePlugin.IMAGE_REAPPLY_CHANGES));
            menuItemReapplyChanges.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    try {
                        JobStatusItemPopupMenu.this.handler.executeWithSelection(tvJobStatuses.getSelection());
                    } catch (ExecutionException e1) {
                        RapidFireCorePlugin.logError("*** Could not execute 'ReapplyChangesHandler'  ***", e1);
                    }
                }
            });

            menuItemReapplyChanges.setEnabled(handler.isEnabled());
        }
    }
}
