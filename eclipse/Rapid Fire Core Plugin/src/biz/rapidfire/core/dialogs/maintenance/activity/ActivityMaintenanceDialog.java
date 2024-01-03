/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.activity;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.activity.ActivityManager;
import biz.rapidfire.core.maintenance.activity.ActivityValues;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class ActivityMaintenanceDialog extends AbstractMaintenanceDialog {

    private ActivityManager manager;

    private Table itemsTable;
    private TableViewer itemsViewer;

    private ActivityValues[] values;
    private boolean enableFields;

    public static ActivityMaintenanceDialog getChangeDialog(Shell shell, ActivityManager manager) {
        return new ActivityMaintenanceDialog(shell, MaintenanceMode.CHANGE, manager);
    }

    public static ActivityMaintenanceDialog getDisplayDialog(Shell shell, ActivityManager manager) {
        return new ActivityMaintenanceDialog(shell, MaintenanceMode.DISPLAY, manager);
    }

    public void setValue(ActivityValues[] values) {
        this.values = values;
    }

    private ActivityMaintenanceDialog(Shell shell, MaintenanceMode mode, ActivityManager manager) {
        super(shell, mode);

        setScrollable(false);

        this.manager = manager;

        if (MaintenanceMode.CREATE.equals(mode) || MaintenanceMode.COPY.equals(mode)) {
            enableFields = true;
        } else if (MaintenanceMode.CHANGE.equals(mode)) {
            enableFields = true;
        } else {
            enableFields = false;
        }
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        itemsTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI); // 68354?
        itemsTable.setLinesVisible(true);
        itemsTable.setHeaderVisible(true);

        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalSpan = 2;
        itemsTable.setLayoutData(gd);

        TableColumn columnActivity = new TableColumn(itemsTable, 0);
        columnActivity.setText(Messages.ColumnLabel_Active);
        columnActivity.setWidth(50);

        TableColumn columnStartTime = new TableColumn(itemsTable, 0);
        columnStartTime.setText(Messages.ColumnLabel_Start_time);
        columnStartTime.setWidth(100);

        TableColumn columnEndTime = new TableColumn(itemsTable, 0);
        columnEndTime.setText(Messages.ColumnLabel_End_time);
        columnEndTime.setWidth(100);

        itemsViewer = new TableViewer(itemsTable);
        itemsViewer.setContentProvider(new ActivitiesContentProvider());
        itemsViewer.setLabelProvider(new ActivitiesLabelProvider());
        itemsViewer.setInput(this.values);

        itemsTable.setMenu(new Menu(itemsTable));

        if (getMode() == MaintenanceMode.CHANGE) {

            Text textUsageInfo = WidgetFactory.createMultilineLabel(parent);
            textUsageInfo.setText(Messages.Label_Maintain_ativity_status_usage_info);
            textUsageInfo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

            itemsViewer.addDoubleClickListener(new ActivitiesDoubleClickActionHandler());
            itemsTable.getMenu().addMenuListener(new ActivitiesPopupMenu(itemsViewer));
        }
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Activity_Schedule;
    }

    @Override
    protected void setScreenValues() {

    }

    @Override
    protected void okPressed() {

        ActivityValues[] newValues = values.clone();

        if (!isDisplayMode()) {
            try {
                manager.setValues(newValues);
            } catch (Exception e) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
                return;
            }
        }

        values = newValues;

        super.okPressed();
    }

    /**
     * Overridden to provide a default size to {@link XDialog}.
     */
    @Override
    protected Point getDefaultSize() {
        return getShell().computeSize(Size.getSize(350), 500, true);
    }

    private class ActivitiesContentProvider implements IStructuredContentProvider {

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        public Object[] getElements(Object elements) {
            return (ActivityValues[])elements;
        }
    }

    private class ActivitiesLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {

            ActivityValues item = (ActivityValues)element;
            switch (columnIndex) {
            case 0:
                if (item.isActive()) {
                    return RapidFireCorePlugin.getDefault().getImage(RapidFireCorePlugin.IMAGE_ENABLED);
                } else {
                    return RapidFireCorePlugin.getDefault().getImage(RapidFireCorePlugin.IMAGE_DISABLED);
                }
            }

            return null;
        }

        public String getColumnText(Object element, int columnIndex) {

            ActivityValues item = (ActivityValues)element;
            switch (columnIndex) {
            case 1:
                return item.getStartTime().toString();
            case 2:
                return item.getEndTime().toString();
            }

            return null;
        }
    }

    private class ActivitiesPopupMenu extends MenuAdapter {

        private TableViewer tableViewer;

        private MenuItem menuItemNew;
        private MenuItem menuItemChange;

        public ActivitiesPopupMenu(TableViewer tableViewer) {
            this.tableViewer = tableViewer;
        }

        @Override
        public void menuShown(MenuEvent event) {
            destroyMenuItems();
            createMenuItems();
        }

        public void destroyMenuItems() {
            if (!((menuItemNew == null) || (menuItemNew.isDisposed()))) {
                menuItemNew.dispose();
            }
            if (!((menuItemChange == null) || (menuItemChange.isDisposed()))) {
                menuItemChange.dispose();
            }
        }

        public void createMenuItems() {
            createMenuItemEnable();
            createMenuItemsDisable();
        }

        private void createMenuItemEnable() {
            menuItemChange = new MenuItem(tableViewer.getTable().getMenu(), SWT.NONE);
            menuItemChange.setText(Messages.ActionLabel_Enable_activity_time_frame);
            menuItemChange.setImage(RapidFireCorePlugin.getDefault().getImageRegistry().get(RapidFireCorePlugin.IMAGE_ENABLED));
            menuItemChange.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    ActivityValues[] selectedItems = getSelectedItems();
                    for (ActivityValues activityValues : selectedItems) {
                        activityValues.setActivity(true);
                    }
                    tableViewer.refresh();
                }
            });
        }

        private void createMenuItemsDisable() {
            menuItemNew = new MenuItem(tableViewer.getTable().getMenu(), SWT.NONE);
            menuItemNew.setText(Messages.ActionLabel_Disable_activity_time_frame);
            menuItemNew.setImage(RapidFireCorePlugin.getDefault().getImageRegistry().get(RapidFireCorePlugin.IMAGE_DISABLED));
            menuItemNew.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    ActivityValues[] selectedItems = getSelectedItems();
                    for (ActivityValues activityValues : selectedItems) {
                        activityValues.setActivity(false);
                    }
                    tableViewer.refresh();
                }
            });
        }

        private ActivityValues[] getSelectedItems() {

            TableItem[] items = tableViewer.getTable().getSelection();
            List<ActivityValues> values = new LinkedList<ActivityValues>();
            for (TableItem item : items) {
                if (item.getData() instanceof ActivityValues) {
                    values.add((ActivityValues)item.getData());
                }
            }

            return values.toArray(new ActivityValues[values.size()]);
        }
    }

    private class ActivitiesDoubleClickActionHandler implements IDoubleClickListener {

        private TableViewer tableViewer;

        public void doubleClick(DoubleClickEvent event) {

            tableViewer = (TableViewer)event.getSource();
            ActivityValues[] activityValues = getSelectedItems(tableViewer);
            if (values == null) {
                return;
            }

            for (ActivityValues activity : activityValues) {
                activity.setActivity(!activity.isActive());
            }

            tableViewer.refresh();
        }

        private ActivityValues[] getSelectedItems(TableViewer tableViewer) {
            ISelection selection = tableViewer.getSelection();
            if (selection instanceof StructuredSelection) {
                StructuredSelection structuredSelection = (StructuredSelection)selection;
                if (structuredSelection.isEmpty()) {
                    return null;
                }

                List<ActivityValues> activityValues = new LinkedList<ActivityValues>();
                Object[] items = structuredSelection.toArray();
                for (Object item : items) {
                    if (item instanceof ActivityValues) {
                        activityValues.add((ActivityValues)item);
                    }
                }
                return activityValues.toArray(new ActivityValues[activityValues.size()]);
            }

            return null;
        }
    }
}
