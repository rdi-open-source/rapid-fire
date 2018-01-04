/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.library.LibraryMaintenanceControl;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.library.LibraryValues;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;

public class LibraryPage extends AbstractWizardPage {

    public static final String NAME = "LIBRARY_PAGE"; //$NON-NLS-1$

    private LibraryValues libraryValues;

    private LibraryMaintenanceControl libraryMaintenanceControl;

    protected LibraryPage(LibraryValues libraryValues) {
        super(NAME);

        this.libraryValues = libraryValues;

        setTitle(Messages.Wizard_Page_Libraries);
        setDescription(Messages.Wizard_Page_Libraries_description);
    }

    @Override
    public void setFocus() {

        if (StringHelper.isNullOrEmpty(libraryMaintenanceControl.getLibraryName())) {
            libraryMaintenanceControl.setFocusLibraryName();
        } else if (StringHelper.isNullOrEmpty(libraryMaintenanceControl.getShadowLibraryName())) {
            libraryMaintenanceControl.setFocusShadowLibraryName();
        } else {
            libraryMaintenanceControl.setFocusLibraryName();
        }
    }

    public LibraryValues getValues() {
        return libraryValues;
    }

    @Override
    public void createContent(Composite parent) {

        libraryMaintenanceControl = new LibraryMaintenanceControl(parent, false, SWT.NONE);
        libraryMaintenanceControl.setMode(MaintenanceMode.CREATE);
        libraryMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
    }

    @Override
    protected void setInputData() {

        libraryMaintenanceControl.setJobName(libraryValues.getKey().getJobName());
        libraryMaintenanceControl.setLibraryName(libraryValues.getKey().getLibrary());
        libraryMaintenanceControl.setShadowLibraryName(libraryValues.getShadowLibrary());
    }

    @Override
    protected void addControlListeners() {

        libraryMaintenanceControl.addModifyListener(this);
    }

    @Override
    protected void updatePageComplete(Object source) {

        String message = null;

        if (StringHelper.isNullOrEmpty(libraryMaintenanceControl.getLibraryName())) {
            // libraryMaintenanceControl.setFocusLibraryName();
            message = Messages.bind(Messages.Name_of_library_A_is_not_valid, libraryMaintenanceControl.getLibraryName());
        } else if (StringHelper.isNullOrEmpty(libraryMaintenanceControl.getShadowLibraryName())) {
            // libraryMaintenanceControl.setFocusShadowLibraryName();
            message = Messages.bind(Messages.Name_of_shadow_library_A_is_not_valid, libraryMaintenanceControl.getShadowLibraryName());
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

        libraryValues.getKey().setLibrary(libraryMaintenanceControl.getLibraryName());
        libraryValues.setShadowLibrary(libraryMaintenanceControl.getShadowLibraryName());
    }

    @Override
    protected void storePreferences() {
    }
}
