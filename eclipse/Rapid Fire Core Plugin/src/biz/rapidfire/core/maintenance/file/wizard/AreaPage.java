/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.area.AreaMaintenanceControl;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.area.AreaValues;
import biz.rapidfire.core.maintenance.area.shared.Ccsid;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.validators.Validator;

public class AreaPage extends AbstractWizardPage {

    public static final String NAME = "AREA_PAGE"; //$NON-NLS-1$

    private AreaValues areaValues;

    private Validator nameValidator;

    private AreaMaintenanceControl areaMaintenanceControl;

    public AreaPage(AreaValues areaValues) {
        super(NAME);

        this.areaValues = areaValues;

        this.nameValidator = Validator.getNameInstance();

        setTitle(Messages.Wizard_Page_Area);
        setDescription(Messages.Wizard_Page_Area_description);
    }

    @Override
    public void setFocus() {

        if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getJobName())) {
            areaMaintenanceControl.setFocusJobName();
        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getPosition())
            || IntHelper.tryParseInt(areaMaintenanceControl.getPosition(), -1) <= 0) {
            areaMaintenanceControl.setFocusPosition();
        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getArea())) {
            areaMaintenanceControl.setFocusArea();
        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getLibrary())) {
            areaMaintenanceControl.setFocusLibrary();
        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getLibraryList())) {
            areaMaintenanceControl.setFocusLibraryList();
        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getLibraryCcsid())) {
            areaMaintenanceControl.setFocusLibraryCcsid();
        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getCommandExtension())) {
            areaMaintenanceControl.setFocusCommandExtension();
        } else {
            areaMaintenanceControl.setFocusJobName();
        }
    }

    public void createContent(Composite parent) {

        areaMaintenanceControl = new AreaMaintenanceControl(parent, false, SWT.NONE);
        areaMaintenanceControl.setMode(MaintenanceMode.CREATE);
        areaMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
    }

    @Override
    protected void setInputData() {

        areaMaintenanceControl.setJobName(areaValues.getKey().getJobName());
        areaMaintenanceControl.setPosition(areaValues.getKey().getPosition());
        areaMaintenanceControl.setArea(areaValues.getKey().getArea());
        areaMaintenanceControl.setLibrary(areaValues.getLibrary());
        areaMaintenanceControl.setLibraryList(areaValues.getLibraryList());
        areaMaintenanceControl.setLibraryCcsid(areaValues.getLibraryCcsid());
        areaMaintenanceControl.setCommandExtension(areaValues.getCommandExtension());

        updatePageEnablement();
    }

    @Override
    protected void addControlListeners() {

        areaMaintenanceControl.addModifyListener(this);
        areaMaintenanceControl.addSelectionListener(this);
    }

    @Override
    protected void updatePageComplete(Object source) {

        String message = null;

        if (!nameValidator.validate(areaMaintenanceControl.getJobName())) {

            message = Messages.bindParameters(Messages.Job_name_A_is_not_valid, areaMaintenanceControl.getJobName());

        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getPosition())
            || IntHelper.tryParseInt(areaMaintenanceControl.getPosition(), -1) <= 0) {

            message = Messages.bind(Messages.File_position_A_is_not_valid, areaMaintenanceControl.getPosition());

        } else if (!nameValidator.validate(areaMaintenanceControl.getArea())) {

            message = Messages.bindParameters(Messages.Area_name_A_is_not_valid, areaMaintenanceControl.getArea());

        } else if (!nameValidator.validate(areaMaintenanceControl.getLibrary())) {

            message = Messages.bindParameters(Messages.Library_name_A_is_not_valid, areaMaintenanceControl.getLibrary());

        } else if (!StringHelper.isNullOrEmpty(areaMaintenanceControl.getLibraryList())) {

            message = Messages.bindParameters(Messages.Library_list_name_A_is_not_valid, areaMaintenanceControl.getLibraryList());

        } else if (!isSpecialValue(areaMaintenanceControl.getLibraryCcsid(), Ccsid.labels())
            && IntHelper.tryParseInt(areaMaintenanceControl.getLibraryCcsid(), -1) <= 0) {

            message = Messages.bind(Messages.Ccsid_A_is_not_valid, areaMaintenanceControl.getLibraryCcsid());
        }

        updateValues();

        if (message == null) {
            setPageComplete(true);
        } else {
            setPageComplete(false);
        }

        setErrorMessage(message);
    }

    private boolean isSpecialValue(String value, String[] specialValues) {

        if (value == null) {
            return false;
        }

        for (String specialValue : specialValues) {
            if (value.equals(specialValue)) {
                return true;
            }
        }

        return false;
    }

    private void updateValues() {

        areaValues.getKey().setArea(areaMaintenanceControl.getArea());
        areaValues.setLibrary(areaMaintenanceControl.getLibrary());
        areaValues.setLibraryList(areaMaintenanceControl.getLibraryList());
        areaValues.setLibraryCcsid(areaMaintenanceControl.getLibraryCcsid());
        areaValues.setCommandExtension(areaMaintenanceControl.getCommandExtension());

        updatePageEnablement();
    }

    private void updatePageEnablement() {
    }

    @Override
    protected void storePreferences() {
    }
}
