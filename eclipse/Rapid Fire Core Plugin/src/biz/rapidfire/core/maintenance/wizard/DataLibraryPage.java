/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.ibm.as400.access.AS400;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.wizard.model.WizardDataModel;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.swt.widgets.ISystemHostCombo;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.core.validators.Validator;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

public class DataLibraryPage extends AbstractWizardPage implements IUpdatePageCompleteHandler {

    public static final String NAME = "DATA_LIBRARY_PAGE"; //$NON-NLS-1$

    private ISystemHostCombo comboConnection;
    private Text textDataLibrary;

    private WizardDataModel model;

    private Validator libraryValidator;

    public DataLibraryPage(WizardDataModel model) {
        super(NAME);

        this.model = model;
        this.libraryValidator = Validator.getLibraryNameInstance();

        setTitle(Messages.Wizard_Page_Data_Library);
        setDescription(Messages.Wizard_Page_Data_Library_description);
    }

    @Override
    public void setFocus() {
        textDataLibrary.setFocus();
    }

    @Override
    public void createContent(Composite parent) {

        comboConnection = WidgetFactory.createSystemHostCombo(parent, SWT.NONE);
        comboConnection.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        comboConnection.setToolTipText(Messages.Tooltip_Connection_name);
        comboConnection.getCombo().setToolTipText(Messages.Tooltip_Connection_name);

        WidgetFactory.createLabel(parent, Messages.Label_Rapid_Fire_library_colon, Messages.Tooltip_Rapid_Fire_library);

        textDataLibrary = WidgetFactory.createNameText(parent);
        textDataLibrary.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        textDataLibrary.setToolTipText(Messages.Tooltip_Job);
    }

    @Override
    protected void setInputData() {

        if (!StringHelper.isNullOrEmpty(model.getConnectionName())) {
            comboConnection.selectConnection(model.getConnectionName());
        } else {
            comboConnection.selectConnection(getPreferences().getWizardConnection());
        }

        if (!StringHelper.isNullOrEmpty(model.getDataLibraryName())) {
            textDataLibrary.setText(model.getDataLibraryName());
        } else {
            textDataLibrary.setText(getPreferences().getWizardRapidFireLibrary());
        }
    }

    @Override
    protected void addControlListeners() {

        comboConnection.addSelectionListener(this);
        textDataLibrary.addModifyListener(this);
    }

    @Override
    protected void updatePageComplete(Object source) {

        updateValues();

        // Schedule the actual work, to reduce calls to the host.
        scheduleUpdatePageComplete(this, source);
    }

    /**
     * Implementation of the IUpdatePageCompleteHandler interface. Indirectly
     * called by scheduleUpdatePageComplete().
     */
    public String performUpdatePageComplete(Object source) {

        String message = null;

        if (StringHelper.isNullOrEmpty(model.getConnectionName())) {

            message = Messages.Connection_is_missing;
        } else if (StringHelper.isNullOrEmpty(model.getConnectionName())) {

            message = Messages.The_Rapid_Fire_product_library_name_is_missing;
        } else if (!libraryValidator.validate(model.getDataLibraryName())) {

            message = Messages.bindParameters(Messages.Library_name_A_is_not_valid, model.getDataLibraryName());
        } else {

            if (!Preferences.getInstance().isSlowConnection()) {
                message = validateRapidFireLibrary(model.getDataLibraryName());
            }
        }

        return message;
    }

    /**
     * Implementation of the IUpdatePageCompleteHandler interface. Indirectly
     * called by scheduleUpdatePageComplete().
     */
    public void performUpdatePageCompleteFinished(Object source, String message) {

        if (message == null) {
            setPageComplete(true);
        } else {
            setPageComplete(false);
        }

        setErrorMessage(message);
    }

    @Override
    public void setErrorMessage(String newMessage) {
        super.setErrorMessage(newMessage);
    }

    @Override
    public boolean canFlipToNextPage() {

        if (!super.canFlipToNextPage()) {
            return false;
        }

        String message = validateRapidFireLibrary(textDataLibrary.getText());
        if (message != null) {
            setErrorMessage(message);
            return false;
        }

        return true;
    }

    private String validateRapidFireLibrary(String dataLibraryName) {

        StringBuilder errorMessage = new StringBuilder();

        if (!RapidFireHelper.checkRapidFireLibrary(getShell(), getSystem(), dataLibraryName, errorMessage)) {
            return errorMessage.toString();
        }

        return null;
    }

    private AS400 getSystem() {
        return SystemConnectionHelper.getSystemChecked(model.getConnectionName());
    }

    private void updateValues() {

        model.setConnectionName(comboConnection.getConnectionName());
        model.setDataLibraryName(textDataLibrary.getText());
    }

    @Override
    protected void storePreferences() {

        getPreferences().setWizardConnection(model.getConnectionName());
        getPreferences().setWizardRapidFireLibrary(model.getDataLibraryName());
    }
}
