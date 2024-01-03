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
import biz.rapidfire.core.maintenance.job.wizard.model.JobWizardDataModel;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;

public class LibraryPage extends AbstractWizardPage {

    public static final String NAME = "LIBRARY_PAGE"; //$NON-NLS-1$

    private JobWizardDataModel model;

    private LibraryMaintenanceControl libraryMaintenanceControl;

    protected LibraryPage(JobWizardDataModel model) {
        super(NAME);

        this.model = model;

        setTitle(Messages.Wizard_Page_Libraries);

        updateMode();
    }

    @Override
    public void updateMode() {

        setDescription(Messages.Wizard_Page_Libraries_description);

        if (libraryMaintenanceControl != null) {
            libraryMaintenanceControl.setMode(MaintenanceMode.CREATE);
        }
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

    @Override
    public void createContent(Composite parent) {

        libraryMaintenanceControl = new LibraryMaintenanceControl(parent, false, SWT.NONE);
        libraryMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

        updateMode();
    }

    @Override
    protected void setInputData() {

        libraryMaintenanceControl.setLibraryName(model.getLibraryName());
        libraryMaintenanceControl.setShadowLibraryName(model.getShadowLibraryName());
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

        model.setLibraryName(libraryMaintenanceControl.getLibraryName());
        model.setShadowLibraryName(libraryMaintenanceControl.getShadowLibraryName());
    }

    @Override
    protected void storePreferences() {
    }
}
