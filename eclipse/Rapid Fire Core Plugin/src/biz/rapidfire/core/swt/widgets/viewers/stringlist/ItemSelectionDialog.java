/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.swt.widgets.viewers.stringlist;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.progress.UIJob;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;

public class ItemSelectionDialog<M extends IStringListItem> extends XDialog implements SelectionListener {

    private static final int DEFAULT_COLUMN_WIDTH = 100;

    private Table itemsTable;
    private TableViewer itemsViewer;

    private String title;
    private String columnHeading;
    private int columnWidth;
    private M[] items;
    private M selectedItem;

    public ItemSelectionDialog(Shell parentShell, String title, String columnHeading) {
        this(parentShell, title, columnHeading, DEFAULT_COLUMN_WIDTH);
    }

    public ItemSelectionDialog(Shell parentShell, String title, String columnHeading, int columnWidth) {
        super(parentShell);

        this.title = title;
        this.columnHeading = columnHeading;
        this.columnWidth = columnWidth;

        this.items = null;
    }

    public void setInputData(M[] items) {

        this.items = items;
        if (itemsViewer != null) {
            itemsViewer.setInput(this.items);
        }
    }

    public M getSelectedItem() {
        return selectedItem;
    }

    @Override
    protected Control createDialogArea(Composite parent) {

        Composite container = (Composite)super.createDialogArea(parent);
        container.setLayout(new GridLayout());

        itemsTable = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        itemsTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        itemsTable.setLinesVisible(true);
        itemsTable.setHeaderVisible(true);
        itemsTable.addSelectionListener(this);
        itemsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent event) {
                if (event.getSource() instanceof Table) {
                    Table table = (Table)event.getSource();
                    Object[] selectedItems = table.getSelection();
                    if (selectedItems != null && selectedItems.length == 1) {
                        if (selectedItems[0] instanceof TableItem) {
                            TableItem tableItem = (TableItem)selectedItems[0];
                            selectedItem = (M)tableItem.getData();
                            new UIJob("") {

                                @Override
                                public IStatus runInUIThread(IProgressMonitor monitor) {
                                    okPressed();
                                    return Status.OK_STATUS;
                                }
                            }.schedule();
                        }
                    }
                }
            }
        });

        TableColumn itemColumn = new TableColumn(itemsTable, 0);
        itemColumn.setText(columnHeading);
        itemColumn.setWidth(columnWidth);

        itemsViewer = new TableViewer(itemsTable);
        StringListContentProvider cp = new StringListContentProvider();
        itemsViewer.setContentProvider(cp);
        itemsViewer.setLabelProvider(new StringListLabelProvider());
        itemsViewer.setInput(items);

        return container;
    }

    @Override
    protected void okPressed() {
        super.okPressed();
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(title);
    }

    public void widgetDefaultSelected(SelectionEvent event) {
        widgetSelected(event);
    }

    public void widgetSelected(SelectionEvent event) {
        selectedItem = (M)event.item.getData();
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    /**
     * Overridden to provide a default size to {@link XDialog}.
     */
    @Override
    protected Point getDefaultSize() {
        return getShell().computeSize(Size.getSize(200), SWT.DEFAULT, true);
    }

    /**
     * Overridden to let {@link XDialog} store the state of this dialog in a
     * separate section of the dialog settings file.
     */
    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        return super.getDialogBoundsSettings(RapidFireCorePlugin.getDefault().getDialogSettings());
    }

    private class StringListLabelProvider extends LabelProvider {

        @Override
        public String getText(Object element) {
            return ((M)element).getLabel();
        }
    }

    private class StringListContentProvider implements IStructuredContentProvider {

        private M[] items;

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            this.items = (M[])newInput;
        }

        public Object[] getElements(Object arg0) {
            return items;
        }
    }
}
