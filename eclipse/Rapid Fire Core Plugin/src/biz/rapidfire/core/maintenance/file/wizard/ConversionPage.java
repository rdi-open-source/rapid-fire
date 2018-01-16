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
import biz.rapidfire.core.dialogs.maintenance.conversion.ConversionMaintenanceControl;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.file.wizard.model.FileWizardDataModel;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;

public class ConversionPage extends AbstractWizardPage {

    public static final String NAME = "CONVERSION_PAGE"; //$NON-NLS-1$

    private FileWizardDataModel model;

    private ConversionMaintenanceControl conversionMaintenanceControl;

    public ConversionPage(FileWizardDataModel model) {
        super(NAME);

        this.model = model;

        setTitle(Messages.Wizard_Page_Conversion);

        updateMode();
    }

    @Override
    public void updateMode() {

        setDescription(Messages.Wizard_Page_Conversion_description);

        if (conversionMaintenanceControl != null) {
            conversionMaintenanceControl.setMode(MaintenanceMode.CREATE);
        }
    }

    @Override
    public void setFocus() {

        if (StringHelper.isNullOrEmpty(conversionMaintenanceControl.getFieldToConvert())) {
            conversionMaintenanceControl.setFocusFieldToConvert();
        } else if (StringHelper.isNullOrEmpty(conversionMaintenanceControl.getNewFieldName())) {
            conversionMaintenanceControl.setFocusNewFieldName();
        } else if (isNullOrEmptyArray(conversionMaintenanceControl.getConversions()[0])) {
            conversionMaintenanceControl.setFocusStatements();
        } else {
            conversionMaintenanceControl.setFocusFieldToConvert();
        }
    }

    private boolean isNullOrEmptyArray(String... values) {

        for (String value : values) {
            if (!StringHelper.isNullOrEmpty(value)) {
                return false;
            }
        }

        return true;
    }

    public void createContent(Composite parent) {

        conversionMaintenanceControl = new ConversionMaintenanceControl(parent, false, SWT.NONE);
        conversionMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

        updateMode();
    }

    @Override
    protected void setInputData() {

        conversionMaintenanceControl.setFieldToConvert(model.getFieldToConvertName());
        conversionMaintenanceControl.setNewFieldName(model.getNewFieldName());
        conversionMaintenanceControl.setConversions(model.getConversionsForUI());
    }

    public void setFieldsToConvert(String[] fieldNames) {
        conversionMaintenanceControl.setFieldsToConvert(fieldNames);
    }

    @Override
    protected void addControlListeners() {

        conversionMaintenanceControl.addModifyListener(this);
        conversionMaintenanceControl.addSelectionListener(this);
    }

    @Override
    protected void updatePageComplete(Object source) {

        String message = null;

        if (!StringHelper.isNullOrEmpty(conversionMaintenanceControl.getFieldToConvert())
            && isNullOrEmptyArray(conversionMaintenanceControl.getConversions())) {

            message = Messages.Conversion_statement_is_missing;
        } else if (StringHelper.isNullOrEmpty(conversionMaintenanceControl.getFieldToConvert())
            && !isNullOrEmptyArray(conversionMaintenanceControl.getConversions())) {

            message = Messages.Field_to_convert_is_missing;
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

        model.setFieldToConvertName(conversionMaintenanceControl.getFieldToConvert());
        model.setNewFieldName(conversionMaintenanceControl.getNewFieldName());
        model.setConversionsFromUI(conversionMaintenanceControl.getConversions());
    }

    @Override
    protected void storePreferences() {
    }
}
