/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.librarylist;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.librarylist.ILibraryListCheck;
import biz.rapidfire.core.model.maintenance.librarylist.LibraryListEntry;
import biz.rapidfire.core.model.maintenance.librarylist.LibraryListManager;
import biz.rapidfire.core.model.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.core.swt.widgets.listeditors.librarylist.LibraryListEditor;
import biz.rapidfire.core.swt.widgets.listeditors.librarylist.LibraryListItem;

public class LibraryListMaintenanceDialog extends AbstractMaintenanceDialog {

    private LibraryListManager manager;

    private LibraryListValues values;

    private Text textJobName;
    private Text textLibraryList;
    private Text textDescription;
    private LibraryListEditor editorLibraryList;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

    public static LibraryListMaintenanceDialog getCreateDialog(Shell shell, LibraryListManager manager) {
        return new LibraryListMaintenanceDialog(shell, MaintenanceMode.MODE_CREATE, manager);
    }

    public static LibraryListMaintenanceDialog getCopyDialog(Shell shell, LibraryListManager manager) {
        return new LibraryListMaintenanceDialog(shell, MaintenanceMode.MODE_COPY, manager);
    }

    public static LibraryListMaintenanceDialog getChangeDialog(Shell shell, LibraryListManager manager) {
        return new LibraryListMaintenanceDialog(shell, MaintenanceMode.MODE_CHANGE, manager);
    }

    public static LibraryListMaintenanceDialog getDeleteDialog(Shell shell, LibraryListManager manager) {
        return new LibraryListMaintenanceDialog(shell, MaintenanceMode.MODE_DELETE, manager);
    }

    public static LibraryListMaintenanceDialog getDisplayDialog(Shell shell, LibraryListManager manager) {
        return new LibraryListMaintenanceDialog(shell, MaintenanceMode.MODE_DISPLAY, manager);
    }

    public void setValue(LibraryListValues values) {
        this.values = values;
    }

    private LibraryListMaintenanceDialog(Shell shell, MaintenanceMode mode, LibraryListManager manager) {
        super(shell, mode);

        this.manager = manager;

        if (MaintenanceMode.MODE_CREATE.equals(mode) || MaintenanceMode.MODE_COPY.equals(mode)) {
            enableParentKeyFields = false;
            enableKeyFields = true;
            enableFields = true;
        } else if (MaintenanceMode.MODE_CHANGE.equals(mode)) {
            enableParentKeyFields = false;
            enableKeyFields = false;
            enableFields = true;
        } else {
            enableParentKeyFields = false;
            enableKeyFields = false;
            enableFields = false;
        }
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        Label labelJobName = new Label(parent, SWT.NONE);
        labelJobName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelJobName.setText(Messages.Label_Job_colon);
        labelJobName.setToolTipText(Messages.Tooltip_Job);

        textJobName = WidgetFactory.createNameText(parent);
        textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobName.setToolTipText(Messages.Tooltip_Job);
        textJobName.setEnabled(enableParentKeyFields);

        Label labelLibrary = new Label(parent, SWT.NONE);
        labelLibrary.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelLibrary.setText(Messages.Label_Library_list_colon);
        labelLibrary.setToolTipText(Messages.Tooltip_Library_list);

        textLibraryList = WidgetFactory.createNameText(parent);
        textLibraryList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textLibraryList.setToolTipText(Messages.Tooltip_Library_list);
        textLibraryList.setEnabled(enableKeyFields);

        Label labelShadowLibrary = new Label(parent, SWT.NONE);
        labelShadowLibrary.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelShadowLibrary.setText(Messages.Label_Description_colon);
        labelShadowLibrary.setToolTipText(Messages.Tooltip_Description);

        textDescription = WidgetFactory.createText(parent);
        textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textDescription.setToolTipText(Messages.Tooltip_Description);
        textDescription.setEnabled(enableFields);

        Group groupLibraryList = new Group(parent, SWT.NONE);
        groupLibraryList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        groupLibraryList.setLayout(new GridLayout(1, false));
        groupLibraryList.setText(Messages.Label_Library_list_colon);
        groupLibraryList.setToolTipText(Messages.Tooltip_Library_list);

        editorLibraryList = WidgetFactory.createLibraryListEditor(groupLibraryList);
        editorLibraryList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        editorLibraryList.setToolTipText(Messages.Tooltip_Library_list);
        editorLibraryList.setEnableLowerCase(true);
        editorLibraryList.setEnabled(enableFields);
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Library_List;
    }

    protected void setScreenValues() {

        textJobName.setText(values.getKey().getJobName());

        textLibraryList.setText(values.getKey().getLibraryList());
        textDescription.setText(values.getDescription());
        LibraryListEntry[] libraries = values.getLibraryList();

        List<LibraryListItem> libraryListItems = new LinkedList<LibraryListItem>();
        for (LibraryListEntry libraryListEntry : libraries) {
            LibraryListItem libraryItem = new LibraryListItem(libraryListEntry.getSequenceNumber(), libraryListEntry.getLibrary());
            libraryListItems.add(libraryItem);
        }

        editorLibraryList.setItems(libraryListItems.toArray(new LibraryListItem[libraryListItems.size()]));
    }

    @Override
    protected void okPressed() {

        LibraryListValues newValues = values.clone();
        newValues.getKey().setLibraryList(textLibraryList.getText());
        newValues.setDescription(textDescription.getText());
        newValues.setLibraryList(getLibraryList(editorLibraryList.getItems()));

        if (!isDisplayMode()) {
            try {
                manager.setValues(newValues);
                Result result = manager.check();
                if (result.isError()) {
                    setErrorFocus(result);
                    return;
                }
            } catch (Exception e) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
                return;
            }
        }

        values = newValues;

        super.okPressed();
    }

    private LibraryListEntry[] getLibraryList(LibraryListItem[] items) {

        List<LibraryListEntry> libraries = new LinkedList<LibraryListEntry>();
        for (LibraryListItem item : items) {
            libraries.add(new LibraryListEntry(item.getSequenceNumber(), item.getLibrary()));
        }

        return libraries.toArray(new LibraryListEntry[libraries.size()]);
    }

    private void setErrorFocus(Result result) {

        String fieldName = result.getFieldName();
        int recordNumber = result.getRecordNbr();
        String message = null;

        if (ILibraryListCheck.FIELD_JOB.equals(fieldName)) {
            textJobName.setFocus();
            message = Messages.bind(Messages.Job_name_A_is_not_valid, textJobName.getText());
        } else if (ILibraryListCheck.FIELD_LIBRARY_LIST.equals(fieldName)) {
            textLibraryList.setFocus();
            message = Messages.bind(Messages.Library_list_name_A_is_not_valid, textLibraryList.getText());
        } else if (ILibraryListCheck.FIELD_DESCRIPTION.equals(fieldName)) {
            textDescription.setFocus();
            message = Messages.bind(Messages.Library_list_description_A_is_not_valid, textDescription.getText());
        } else if (ILibraryListCheck.FIELD_SEQUENCE.equals(fieldName)) {
            LibraryListItem item = editorLibraryList.getItem(recordNumber - 1);
            int sequenceNumber;
            if (item == null) {
                sequenceNumber = -1;
                editorLibraryList.setFocus();
            } else {
                sequenceNumber = item.getSequenceNumber();
                editorLibraryList.setFocus(recordNumber - 1);
            }
            message = Messages.bind(Messages.Invalid_sequence_number_A, sequenceNumber);
        } else if (ILibraryListCheck.FIELD_DUPLICATE.equals(fieldName)) {
            textDescription.setFocus();
            setErrorMessage(Messages.bind(Messages.Description_A_is_not_valid, textDescription.getText()));
        }

        setErrorMessage(message, result);
    }
}
