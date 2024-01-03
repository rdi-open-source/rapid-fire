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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.conversion.ConversionMaintenanceControl;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.file.wizard.model.FileWizardDataModel;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class ConversionPage extends AbstractWizardPage {

    public static final String NAME = "CONVERSION_PAGE"; //$NON-NLS-1$

    private FileWizardDataModel model;

    private ConversionMaintenanceControl conversionMaintenanceControl;
    private Text infoBox;

    private Label textSourceFieldPrefix;
    private Label textTargetFieldPrefix;

    private String sourceFieldsPrefix;
    private String targetFieldsPrefix;

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

    @Override
    public void createContent(Composite parent) {

        conversionMaintenanceControl = new ConversionMaintenanceControl(parent, false, SWT.NONE);
        conversionMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

        WidgetFactory.createLabel(parent, "Source field prefix:", null);
        textSourceFieldPrefix = WidgetFactory.createLabel(parent, "", null);

        WidgetFactory.createLabel(parent, "Target field prefix:", null);
        textTargetFieldPrefix = WidgetFactory.createLabel(parent, "", null);

        infoBox = WidgetFactory.createMultilineLabel(parent);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gridData.minimumHeight = 80;
        infoBox.setLayoutData(gridData);

        updateMode();
    }

    /**
     * Sets the bulk text of the infoBox control. If the text is set in
     * createContent(), the wizard page is rendered ugly.
     */
    @Override
    public void prepareForDisplay() {

        textSourceFieldPrefix.setText(sourceFieldsPrefix);
        textTargetFieldPrefix.setText(targetFieldsPrefix);

        StringBuilder buffer = new StringBuilder();
        buffer.append(Messages.Wizard_Conversion_page_info_box_1);
        buffer.append("\n"); //$NON-NLS-1$
        buffer.append(Messages.Wizard_Conversion_page_info_box_2);
        infoBox.setText(buffer.toString());

        textTargetFieldPrefix.getParent().getParent().layout(true, true);

        getWizard().getContainer().getShell().layout(true, true);
    }

    @Override
    protected void setInputData() {

        conversionMaintenanceControl.setFieldToConvert(model.getFieldToConvertName());
        conversionMaintenanceControl.setNewFieldName(model.getNewFieldName());
        conversionMaintenanceControl.setConversions(model.getConversionsForUI());
    }

    public void setLibraryListNames(String[] fieldNames) {
        conversionMaintenanceControl.setFieldNames(fieldNames);
    }

    public void setFieldsToConvert(String[] fieldNames) {

        if (fieldNames == null) {
            return;
        }

        conversionMaintenanceControl.setFieldsToConvert(fieldNames);
    }

    public void setSourceFieldsPrefix(String prefix) {
        this.sourceFieldsPrefix = prefix;
    }

    public void setTargetFieldsPrefix(String prefix) {
        this.targetFieldsPrefix = prefix;
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
