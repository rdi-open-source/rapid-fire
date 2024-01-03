/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.swt.widgets.listeditors.librarylist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.swt.widgets.UpperCaseOnlyVerifier;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

/**
 * Implements a string list editor with options for adding, removing and moving
 * items.
 * 
 * @see com.ibm.etools.iseries.rse.ui.widgets.IBMiLibraryListComposite
 */
public class LibraryListEditor extends Composite implements SelectionListener {

    private static final int INITIAL_INCREMENT = 10;

    private static final int COLUMN_SEQUENCE = 0;
    private static final int COLUMN_LIBRARY = 1;

    private static final String COLUMN_PROPERTY_SEQUENCE = "SEQUENCE"; //$NON-NLS-1$
    private static final String COLUMN_PROPERTY_LIBRARY = "LIBRARY"; //$NON-NLS-1$
    private static final String[] COLUMN_PROPERTIES = { COLUMN_PROPERTY_SEQUENCE, COLUMN_PROPERTY_LIBRARY };

    private Shell shell;
    private Button parentDefaultButton;
    private List<LibraryListItem> itemsList;

    private boolean enableLowerCase;

    private UpperCaseOnlyVerifier upperCaseOnlyVerifier;

    private Table itemsTable;
    private TableViewer itemsViewer;
    private Text textItem;
    private Button addButton;
    private Button removeButton;
    private Button removeAllButton;
    private Button moveUpButton;
    private Button moveDownButton;
    private ILibraryListEntryValidator validator;

    private CellEditor[] cellEditors;

    public LibraryListEditor(Composite parent, int style) {
        super(parent, style);

        this.shell = parent.getShell();
        this.itemsList = new ArrayList<LibraryListItem>();

        this.upperCaseOnlyVerifier = new UpperCaseOnlyVerifier();

        int numColumns = 3;
        prepareComposite(numColumns);
        createContentArea(numColumns);
    }

    public void setValidator(ILibraryListEntryValidator validator) {
        this.validator = validator;
    }

    public void setTextLimit(int limit) {
        textItem.setTextLimit(limit);
        Text editorControl = (Text)cellEditors[COLUMN_LIBRARY].getControl();
        editorControl.setTextLimit(limit);
    }

    public void setEnableLowerCase(boolean enable) {
        this.enableLowerCase = enable;

        Text editorControl = (Text)cellEditors[COLUMN_LIBRARY].getControl();
        if (this.enableLowerCase) {
            textItem.addVerifyListener(upperCaseOnlyVerifier);
            editorControl.addVerifyListener(upperCaseOnlyVerifier);
        } else {
            textItem.removeVerifyListener(upperCaseOnlyVerifier);
            editorControl.removeVerifyListener(upperCaseOnlyVerifier);
        }
    }

    public boolean contains(LibraryListItem item) {

        if (item == null) {
            return false;
        }

        for (int i = 0; i < itemsList.size(); i++) {
            if (item.getLibrary().trim().equals(itemsList.get(i).getLibrary().trim())) {
                return true;
            }
        }

        return false;
    }

    public void clearAll() {
        textItem.setText(""); //$NON-NLS-1$
        itemsList.clear();
        itemsViewer.refresh();
    }

    public void setItems(LibraryListItem[] items) {
        this.itemsList.clear();
        for (LibraryListItem item : items) {
            this.itemsList.add(item);
        }

        itemsViewer.setInput(itemsList);

        setButtonEnablement();
    }

    public LibraryListItem[] getItems() {

        LibraryListItem[] items = new LibraryListItem[itemsList.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = itemsList.get(i);
        }

        return items;
    }

    public LibraryListItem getItem(int index) {

        if (index >= 0 && index < itemsList.size()) {
            return itemsList.get(index);
        }

        return null;
    }

    public int getItemCount() {
        return itemsTable.getItemCount();
    }

    @Override
    public boolean setFocus() {
        if (textItem.isEnabled()) {
            return textItem.setFocus();
        } else {
            return super.setFocus();
        }
    }

    public boolean setFocus(int index) {
        if (index >= 0 && index < getItemCount()) {
            itemsTable.setTopIndex(index);
            return true;
        }

        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (!enabled) {
            textItem.setEnabled(false);
            itemsTable.setEnabled(false);
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
            removeAllButton.setEnabled(false);
            moveUpButton.setEnabled(false);
            moveDownButton.setEnabled(false);
        } else {
            textItem.setEnabled(true);
            itemsTable.setEnabled(true);
            setButtonEnablement();
        }
    }

    private void setButtonEnablement() {

        // Add-button
        if (textItem.getText().trim().length() > 0) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }

        // Remove all-button
        if (itemsTable.getItemCount() <= 0 || !isEnabled()) {
            removeAllButton.setEnabled(false);
        } else {
            removeAllButton.setEnabled(true);
        }

        // Other buttons
        if (itemsTable.getSelectionCount() == 0) {
            removeButton.setEnabled(false);
            moveUpButton.setEnabled(false);
            moveDownButton.setEnabled(false);
        } else {
            int[] selectionIndices = itemsTable.getSelectionIndices();
            boolean upEnabled = true;
            boolean downEnabled = true;
            for (int loop = 0; loop < selectionIndices.length; loop++) {
                if (selectionIndices[loop] == 0) {
                    upEnabled = false;
                }
                if (selectionIndices[loop] == itemsTable.getItemCount() - 1) {
                    downEnabled = false;
                }
            }
            removeButton.setEnabled(true);
            moveUpButton.setEnabled(upEnabled);
            moveDownButton.setEnabled(downEnabled);
        }

    }

    protected Composite prepareComposite(int numColumns) {
        Composite composite = this;

        GridLayout layout = new GridLayout();
        layout.numColumns = numColumns;
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        composite.setLayout(layout);

        GridData data = new GridData();

        data.horizontalAlignment = 4;
        data.grabExcessHorizontalSpace = true;

        data.verticalAlignment = 4;
        data.grabExcessVerticalSpace = true;

        composite.setLayoutData(data);
        return composite;
    }

    private void createContentArea(int numColumns) {

        Composite mainPanel = this;
        mainPanel.setLayout(new GridLayout(numColumns + 1, false));
        mainPanel.setLayoutData(new GridData(GridData.FILL_BOTH));

        Label labelLibrary = new Label(mainPanel, SWT.NONE);
        labelLibrary.setText(Messages.Library_colon);
        labelLibrary.setToolTipText(Messages.Library_Tooltip);

        textItem = WidgetFactory.createText(mainPanel);
        textItem.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        textItem.setToolTipText(Messages.Library_Tooltip);
        textItem.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (textItem.getText().trim().length() > 0) {
                    addButton.getParent().getShell().setDefaultButton(addButton);
                }
                setButtonEnablement();
            }
        });

        textItem.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                parentDefaultButton = addButton.getParent().getShell().getDefaultButton();
                if (addButton.isEnabled()) {
                    addButton.getParent().getShell().setDefaultButton(addButton);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                shell.setDefaultButton(parentDefaultButton);
            }

        });

        addButton = WidgetFactory.createPushButton(mainPanel);
        addButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        addButton.setText(Messages.Add);
        addButton.addSelectionListener(this);

        itemsTable = new Table(mainPanel, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI); // 68354?
        itemsTable.setLinesVisible(true);
        itemsTable.setHeaderVisible(true);
        itemsTable.addSelectionListener(this);

        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = (numColumns - 1);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        itemsTable.setLayoutData(gd);

        TableColumn itemSequenceNumber = new TableColumn(itemsTable, 0);
        itemSequenceNumber.setText(Messages.Sequence);
        itemSequenceNumber.setWidth(80);

        TableColumn itemColumn = new TableColumn(itemsTable, 0);
        itemColumn.setText(Messages.Item);
        itemColumn.setWidth(100);

        itemsViewer = new TableViewer(itemsTable);
        itemsViewer.setContentProvider(new LibraryListContentProvider());
        itemsViewer.setLabelProvider(new LibraryListLabelProvider());
        itemsViewer.setSorter(new LibraryListSorter());
        itemsViewer.setCellModifier(new LibraryListCellModifier());
        itemsViewer.setColumnProperties(COLUMN_PROPERTIES);
        itemsViewer.setInput(null);

        cellEditors = new CellEditor[2];
        cellEditors[COLUMN_SEQUENCE] = new TextCellEditor(itemsTable);
        cellEditors[COLUMN_SEQUENCE].setValidator(new ICellEditorValidator() {
            public String isValid(Object value) {
                if (validator == null) {
                    return null;
                } else {
                    ValidationEvent event;
                    if (!cellEditors[COLUMN_SEQUENCE].isActivated()) {
                        event = new ValidationEvent(ValidationEvent.ACTIVATE, (LibraryListItem)value);
                    } else {
                        event = new ValidationEvent(ValidationEvent.CHANGE, (LibraryListItem)value);
                    }
                    return validator.isValid(event);
                }
            }
        });

        cellEditors[COLUMN_LIBRARY] = new TextCellEditor(itemsTable);
        cellEditors[COLUMN_LIBRARY].setValidator(new ICellEditorValidator() {
            public String isValid(Object value) {
                if (validator == null) {
                    return null;
                } else {
                    ValidationEvent event;
                    if (!cellEditors[COLUMN_LIBRARY].isActivated()) {
                        event = new ValidationEvent(ValidationEvent.ACTIVATE, (LibraryListItem)value);
                    } else {
                        event = new ValidationEvent(ValidationEvent.CHANGE, (LibraryListItem)value);
                    }
                    return validator.isValid(event);
                }
            }
        });

        itemsViewer.setCellEditors(cellEditors);

        Composite buttonsPanel = new Composite(mainPanel, 0);
        GridLayout layout = new GridLayout();
        numColumns = 1;
        buttonsPanel.setLayout(layout);

        removeButton = WidgetFactory.createPushButton(buttonsPanel);
        removeButton.setText(Messages.Remove);
        removeButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        removeButton.addSelectionListener(this);

        removeAllButton = WidgetFactory.createPushButton(buttonsPanel);
        removeAllButton.setText(Messages.Remove_all);
        removeAllButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        removeAllButton.addSelectionListener(this);

        moveUpButton = WidgetFactory.createPushButton(buttonsPanel);
        moveUpButton.setText(Messages.Move_up);
        moveUpButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        moveUpButton.addSelectionListener(this);

        moveDownButton = WidgetFactory.createPushButton(buttonsPanel);
        moveDownButton.setText(Messages.Move_down);
        moveDownButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        moveDownButton.addSelectionListener(this);

        setButtonEnablement();
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }

    public void widgetSelected(SelectionEvent e) {

        if (e.widget == addButton) {
            LibraryListItem itemToAdd = new LibraryListItem(getNextSequenceNumber(), textItem.getText().trim());
            if (validator == null || validator.isValid(new ValidationEvent(ValidationEvent.ADD, itemToAdd)) == null) {
                if (itemToAdd.getSequenceNumber() > 0 || itemToAdd.getLibrary().trim().length() > 0) {
                    itemsList.add(itemToAdd);
                    textItem.setText(""); //$NON-NLS-1$
                    textItem.setFocus();
                    itemsViewer.setInput(itemsList);
                    if (itemsList.size() > 0) {
                        itemsTable.setTopIndex(itemsList.size());
                    }
                }
            }

        } else if (e.widget == removeButton) {
            int[] selections = itemsTable.getSelectionIndices();
            if (selections != null) {
                for (int loop = selections.length - 1; loop >= 0; loop--) {
                    itemsList.remove(selections[loop]);
                }
                itemsViewer.refresh();
            }
        } else if (e.widget == removeAllButton) {
            itemsList.clear();
            itemsViewer.refresh();
        } else if (e.widget == moveUpButton) {
            int[] selections = itemsTable.getSelectionIndices();
            itemsTable.deselectAll();
            if (selections != null) {

                for (int loop = 0; loop < selections.length; loop++) {
                    if (selections[loop] > 0) {
                        LibraryListItem temp = itemsList.remove(selections[loop]);
                        itemsList.add(selections[loop] - 1, temp);
                    }
                }
                itemsViewer.refresh();

                for (int loop = 0; loop < selections.length; loop++) {
                    if (selections[loop] > 0) {
                        itemsTable.select(selections[loop] - 1);
                    } else {
                        itemsTable.select(selections[loop]);
                    }
                }
            }
        } else if (e.widget == moveDownButton) {
            int[] selections = itemsTable.getSelectionIndices();
            itemsTable.deselectAll();

            if (selections != null) {

                for (int loop = selections.length - 1; loop >= 0; loop--) {
                    if (selections[loop] < itemsList.size() - 1) {
                        LibraryListItem temp = itemsList.remove(selections[loop]);
                        itemsList.add(selections[loop] + 1, temp);
                    }
                }
                itemsViewer.refresh();

                for (int loop = 0; loop < selections.length; loop++) {
                    if (selections[loop] < itemsList.size() - 1) {
                        itemsTable.select(selections[loop] + 1);
                    } else {
                        itemsTable.select(selections[loop]);
                    }
                }
            }
        }

        setButtonEnablement();
    }

    public void addSelectionListener(SelectionListener listener) {

        addButton.addSelectionListener(listener);
        removeButton.addSelectionListener(listener);
        removeAllButton.addSelectionListener(listener);
    }

    public void removeSelectionListener(SelectionListener listener) {

        addButton.removeSelectionListener(listener);
        removeButton.removeSelectionListener(listener);
        removeAllButton.removeSelectionListener(listener);
    }

    private int getNextSequenceNumber() {

        int itemCount = itemsTable.getItemCount();
        if (itemCount > 0) {
            TableItem lastItem = itemsTable.getItem(itemCount - 1);
            LibraryListItem lastLibraryListItem = (LibraryListItem)lastItem.getData();
            if (itemCount > 1) {
                TableItem prevItem = itemsTable.getItem(itemCount - 2);
                LibraryListItem prevLibraryListItem = (LibraryListItem)prevItem.getData();
                return lastLibraryListItem.getSequenceNumber() + (lastLibraryListItem.getSequenceNumber() - prevLibraryListItem.getSequenceNumber());
            } else {
                return lastLibraryListItem.getSequenceNumber() + lastLibraryListItem.getSequenceNumber();
            }
        } else {
            return INITIAL_INCREMENT;
        }
    }

    private class LibraryListContentProvider implements IStructuredContentProvider {

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        public Object[] getElements(Object arg0) {
            return itemsList.toArray();
        }

    }

    private class LibraryListLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {

            LibraryListItem item = (LibraryListItem)element;
            switch (columnIndex) {
            case 0:
                return Integer.toString(item.getSequenceNumber());
            case 1:
                return item.getLibrary();
            }

            return null;
        }
    }

    private class LibraryListSorter extends ViewerSorter {
        @Override
        public int compare(Viewer viewer, Object e1, Object e2) {
            if (e1 == null) {
                return -1;
            } else if (e2 == null) {
                return 1;
            } else {
                return ((LibraryListItem)e1).compareTo((LibraryListItem)e2);
            }
        }
    }

    private class LibraryListCellModifier implements ICellModifier {

        public boolean canModify(Object element, String property) {
            return true;
        }

        public Object getValue(Object element, String property) {

            LibraryListItem item = (LibraryListItem)element;

            if (COLUMN_PROPERTY_SEQUENCE.equals(property)) {
                return Integer.toString(item.getSequenceNumber());
            } else if (COLUMN_PROPERTY_LIBRARY.equals(property)) {
                return item.getLibrary();
            }

            return null;
        }

        public void modify(Object element, String property, Object value) {

            TableItem tableItem = (TableItem)element;

            LibraryListItem item = (LibraryListItem)tableItem.getData();

            if (COLUMN_PROPERTY_SEQUENCE.equals(property)) {
                item.setSequenceNumber(IntHelper.tryParseInt((String)value, 0));
            } else if (COLUMN_PROPERTY_LIBRARY.equals(property)) {
                item.setLibrary((String)value);
            }

            itemsViewer.refresh();
        }

    }
}
