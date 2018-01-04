/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job.wizard;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.librarylist.LibraryListMaintenanceControl;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.librarylist.LibraryListEntry;
import biz.rapidfire.core.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.swt.widgets.listeditors.librarylist.LibraryListItem;

public class LibraryListPage extends AbstractWizardPage {

    public static final String NAME = "LIBRARY_LIST_PAGE"; //$NON-NLS-1$

    private LibraryListValues libraryListValues;

    private LibraryListMaintenanceControl libraryListMaintenanceControl;

    protected LibraryListPage(LibraryListValues libraryListValues) {
        super(NAME);

        this.libraryListValues = libraryListValues;

        setTitle(Messages.Wizard_Page_Library_List);
        setDescription(Messages.Wizard_Page_Library_List_description);
    }

    @Override
    public void setFocus() {

        if (StringHelper.isNullOrEmpty(libraryListMaintenanceControl.getLibraryListName())) {
            libraryListMaintenanceControl.setFocusLibraryListName();
        } else if (StringHelper.isNullOrEmpty(libraryListMaintenanceControl.getDescription())) {
            libraryListMaintenanceControl.setFocusDescription();
        } else if (libraryListMaintenanceControl.getLibrariesCount() == 0) {
            libraryListMaintenanceControl.setFocusLibraryListEditor();
        } else {
            libraryListMaintenanceControl.setFocusLibraryListName();
        }
    }

    public LibraryListValues getValues() {
        return libraryListValues;
    }

    @Override
    public void createContent(Composite parent) {

        libraryListMaintenanceControl = new LibraryListMaintenanceControl(parent, false, SWT.NONE);
        libraryListMaintenanceControl.setParentKeyFieldsVisible(false);
        libraryListMaintenanceControl.setMode(MaintenanceMode.CREATE);
        libraryListMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
    }

    @Override
    protected void setInputData() {

        libraryListMaintenanceControl.setJobName(libraryListValues.getKey().getJobName());
        libraryListMaintenanceControl.setLibraryListName(libraryListValues.getKey().getLibraryList());
        libraryListMaintenanceControl.setDescription(libraryListValues.getDescription());
        setLibraryList(libraryListValues.getLibraryList());
    }

    @Override
    protected void addControlListeners() {

        libraryListMaintenanceControl.addModifyListener(this);
        libraryListMaintenanceControl.addSelectionListener(this);
    }

    @Override
    protected void updatePageComplete(Object source) {

        String message = null;

        if (StringHelper.isNullOrEmpty(libraryListMaintenanceControl.getLibraryListName())) {
            // libraryListMaintenanceControl.setFocusLibraryListName();
            message = Messages.bind(Messages.Library_list_name_A_is_not_valid, libraryListMaintenanceControl.getLibraryListName());
        } else if (StringHelper.isNullOrEmpty(libraryListMaintenanceControl.getDescription())) {
            // libraryListMaintenanceControl.setFocusDescription();
            message = Messages.bind(Messages.Library_list_description_A_is_not_valid, libraryListMaintenanceControl.getDescription());
        } else if (libraryListMaintenanceControl.getLibrariesCount() <= 0) {
            // libraryListMaintenanceControl.setFocusLibraryListEditor();
            message = Messages.Library_list_entries_are_missing;
        }

        updateValues();

        if (message == null) {
            setPageComplete(true);
        } else {
            setPageComplete(false);
        }

        setErrorMessage(message);
    }

    private void updateValues() {

        libraryListValues.getKey().setLibraryList(libraryListMaintenanceControl.getLibraryListName());
        libraryListValues.setDescription(libraryListMaintenanceControl.getDescription());
        libraryListValues.setLibraryList(getLibraryList(libraryListMaintenanceControl.getLibraries()));
    }

    private LibraryListEntry[] getLibraryList(LibraryListItem[] libraries) {

        List<LibraryListEntry> list = new LinkedList<LibraryListEntry>();
        for (LibraryListItem item : libraries) {
            list.add(new LibraryListEntry(item.getSequenceNumber(), item.getLibrary()));
        }

        return list.toArray(new LibraryListEntry[list.size()]);
    }

    private void setLibraryList(LibraryListEntry[] libraries) {

        List<LibraryListItem> list = new LinkedList<LibraryListItem>();
        for (LibraryListEntry item : libraries) {
            list.add(new LibraryListItem(item.getSequenceNumber(), item.getLibrary()));
        }

        libraryListMaintenanceControl.setLibraries(list.toArray(new LibraryListItem[list.size()]));
    }

    @Override
    protected void storePreferences() {
    }
}
