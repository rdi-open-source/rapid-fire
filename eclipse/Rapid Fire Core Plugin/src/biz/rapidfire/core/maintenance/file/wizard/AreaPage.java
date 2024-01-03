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
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.area.AreaMaintenanceControl;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.area.shared.Area;
import biz.rapidfire.core.maintenance.area.shared.Ccsid;
import biz.rapidfire.core.maintenance.file.wizard.model.FileWizardDataModel;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.core.validators.Validator;

public class AreaPage extends AbstractWizardPage {

    public static final String NAME = "AREA_PAGE"; //$NON-NLS-1$

    private FileWizardDataModel model;

    private Validator areaNameValidator;
    private Validator nameValidator;

    private AreaMaintenanceControl areaMaintenanceControl;
    private Text infoBox;

    public AreaPage(FileWizardDataModel model) {
        super(NAME);

        this.model = model;

        this.areaNameValidator = Validator.getNameInstance(Area.labels());
        this.nameValidator = Validator.getNameInstance();

        setTitle(Messages.Wizard_Page_Area);

        updateMode();
    }

    @Override
    public void updateMode() {

        setDescription(Messages.Wizard_Page_Area_description);

        if (areaMaintenanceControl != null) {
            areaMaintenanceControl.setMode(MaintenanceMode.CREATE);
        }
    }

    @Override
    public void setFocus() {

        if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getAreaName())) {
            areaMaintenanceControl.setFocusArea();
        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getLibraryName())) {
            areaMaintenanceControl.setFocusLibrary();
        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getLibraryListName())) {
            areaMaintenanceControl.setFocusLibraryList();
        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getLibraryCcsid())) {
            areaMaintenanceControl.setFocusLibraryCcsid();
        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getCommandExtension())) {
            areaMaintenanceControl.setFocusCommandExtension();
        } else {
            areaMaintenanceControl.setFocusArea();
        }
    }

    @Override
    public void createContent(Composite parent) {

        areaMaintenanceControl = new AreaMaintenanceControl(parent, false, SWT.NONE);
        areaMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

        infoBox = WidgetFactory.createMultilineLabel(parent);
        infoBox.setLayoutData(new GridData(GridData.FILL_BOTH));

        updateMode();
    }

    /**
     * Sets the bulk text of the infoBox control. If the text is set in
     * createContent(), the wizard page is rendered ugly.
     */
    @Override
    public void prepareForDisplay() {

        infoBox.setText(Messages.Wizard_Area_page_info_box);
    }

    @Override
    protected void setInputData() {

        areaMaintenanceControl.setAreaName(model.getAreaName());
        areaMaintenanceControl.setLibraryName(model.getLibraryName());
        areaMaintenanceControl.setLibraryListName(model.getLibraryListName());
        areaMaintenanceControl.setLibraryCcsid(model.getLibraryCcsid());
        areaMaintenanceControl.setCommandExtension(model.getCommandExtension());
    }

    public void setLibraryNames(String[] libraryNames) {
        areaMaintenanceControl.setLibraryNames(libraryNames);
    }

    public void setLibraryListNames(String[] libraryListNames) {
        areaMaintenanceControl.setLibraryListNames(libraryListNames);
    }

    @Override
    protected void addControlListeners() {

        areaMaintenanceControl.addModifyListener(this);
        areaMaintenanceControl.addSelectionListener(this);
    }

    @Override
    protected void updatePageComplete(Object source) {

        String message = null;

        if (!areaNameValidator.validate(areaMaintenanceControl.getAreaName())) {

            message = Messages.bindParameters(Messages.Area_name_A_is_not_valid, areaMaintenanceControl.getAreaName());

        } else if (!nameValidator.validate(areaMaintenanceControl.getLibraryName())) {

            message = Messages.bindParameters(Messages.Library_name_A_is_not_valid, areaMaintenanceControl.getLibraryName());

        } else if (StringHelper.isNullOrEmpty(areaMaintenanceControl.getLibraryListName())) {

            message = Messages.bindParameters(Messages.Library_list_name_A_is_not_valid, areaMaintenanceControl.getLibraryListName());

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

        model.setAreaName(areaMaintenanceControl.getAreaName());
        model.setLibraryName(areaMaintenanceControl.getLibraryName());
        model.setLibraryListName(areaMaintenanceControl.getLibraryListName());
        model.setLibraryCcsid(areaMaintenanceControl.getLibraryCcsid());
        model.setCommandExtension(areaMaintenanceControl.getCommandExtension());
    }

    @Override
    protected void storePreferences() {
    }
}
