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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.librarylist.ILibraryListCheck;
import biz.rapidfire.core.maintenance.librarylist.LibraryListEntry;
import biz.rapidfire.core.maintenance.librarylist.LibraryListManager;
import biz.rapidfire.core.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.swt.widgets.listeditors.librarylist.LibraryListItem;

public class LibraryListMaintenanceDialog extends AbstractMaintenanceDialog {

    private LibraryListManager manager;
    private LibraryListValues values;

    private LibraryListMaintenanceControl libraryListMaintenanceControl;

    public static LibraryListMaintenanceDialog getCreateDialog(Shell shell, LibraryListManager manager) {
        return new LibraryListMaintenanceDialog(shell, MaintenanceMode.CREATE, manager);
    }

    public static LibraryListMaintenanceDialog getCopyDialog(Shell shell, LibraryListManager manager) {
        return new LibraryListMaintenanceDialog(shell, MaintenanceMode.COPY, manager);
    }

    public static LibraryListMaintenanceDialog getChangeDialog(Shell shell, LibraryListManager manager) {
        return new LibraryListMaintenanceDialog(shell, MaintenanceMode.CHANGE, manager);
    }

    public static LibraryListMaintenanceDialog getDeleteDialog(Shell shell, LibraryListManager manager) {
        return new LibraryListMaintenanceDialog(shell, MaintenanceMode.DELETE, manager);
    }

    public static LibraryListMaintenanceDialog getDisplayDialog(Shell shell, LibraryListManager manager) {
        return new LibraryListMaintenanceDialog(shell, MaintenanceMode.DISPLAY, manager);
    }

    public void setValue(LibraryListValues values) {
        this.values = values;
    }

    public LibraryListValues getValue() {
        return values;
    }

    private LibraryListMaintenanceDialog(Shell shell, MaintenanceMode mode, LibraryListManager manager) {
        super(shell, mode);

        this.manager = manager;
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        libraryListMaintenanceControl = new LibraryListMaintenanceControl(parent, SWT.NONE);
        libraryListMaintenanceControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        libraryListMaintenanceControl.setMode(getMode());
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Library_List;
    }

    @Override
    protected void setScreenValues() {

        libraryListMaintenanceControl.setJobName(values.getKey().getJobName());

        libraryListMaintenanceControl.setLibraryListName(values.getKey().getLibraryList());
        libraryListMaintenanceControl.setDescription(values.getDescription());
        LibraryListEntry[] libraries = values.getLibraryList();

        List<LibraryListItem> libraryListItems = new LinkedList<LibraryListItem>();
        for (LibraryListEntry libraryListEntry : libraries) {
            LibraryListItem libraryItem = new LibraryListItem(libraryListEntry.getSequenceNumber(), libraryListEntry.getLibrary());
            libraryListItems.add(libraryItem);
        }

        libraryListMaintenanceControl.setLibraries(libraryListItems.toArray(new LibraryListItem[libraryListItems.size()]));
    }

    @Override
    protected void okPressed() {

        LibraryListValues newValues = values.clone();
        newValues.getKey().setLibraryList(libraryListMaintenanceControl.getLibraryListName());
        newValues.setDescription(libraryListMaintenanceControl.getDescription());
        newValues.setLibraryList(getLibraryList(libraryListMaintenanceControl.getLibraries()));

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
            libraryListMaintenanceControl.setFocusJobName();
            message = Messages.bind(Messages.Job_name_A_is_not_valid, libraryListMaintenanceControl.getJobName());
        } else if (ILibraryListCheck.FIELD_LIBRARY_LIST.equals(fieldName)) {
            libraryListMaintenanceControl.setFocusLibraryListName();
            message = Messages.bind(Messages.Library_list_name_A_is_not_valid, libraryListMaintenanceControl.getLibraryListName());
        } else if (ILibraryListCheck.FIELD_DESCRIPTION.equals(fieldName)) {
            libraryListMaintenanceControl.setFocusDescription();
            message = Messages.bind(Messages.Library_list_description_A_is_not_valid, libraryListMaintenanceControl.getDescription());
        } else if (ILibraryListCheck.FIELD_SEQUENCE.equals(fieldName)) {
            LibraryListItem item = libraryListMaintenanceControl.getLibrary(recordNumber - 1);
            int sequenceNumber;
            if (item == null) {
                sequenceNumber = -1;
                libraryListMaintenanceControl.setFocusLibraryListEditor();
            } else {
                sequenceNumber = item.getSequenceNumber();
                libraryListMaintenanceControl.setFocusLibraryListEditor(recordNumber - 1);
            }
            message = Messages.bind(Messages.Invalid_sequence_number_A, sequenceNumber);
        } else if (ILibraryListCheck.FIELD_DUPLICATE.equals(fieldName)) {
            libraryListMaintenanceControl.setFocusDescription();
            setErrorMessage(Messages.bind(Messages.Description_A_is_not_valid, libraryListMaintenanceControl.getDescription()));
        }

        setErrorMessage(message, result);
    }
}
