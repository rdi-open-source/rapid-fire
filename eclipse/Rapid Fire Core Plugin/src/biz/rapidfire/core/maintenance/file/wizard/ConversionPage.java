/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file.wizard;

import org.eclipse.swt.widgets.Composite;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.job.JobValues;
import biz.rapidfire.core.maintenance.job.wizard.NewJobWizard;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;

public class ConversionPage extends AbstractWizardPage {

    public static final String NAME = "CONVERSION_PAGE"; //$NON-NLS-1$

    public ConversionPage() {
        super(NAME);

        setTitle(Messages.Wizard_Page_Conversion);
        setDescription(Messages.Wizard_Page_Conversion_description);
    }

    @Override
    public void setFocus() {

    }

    public JobValues getValues() {
        return null;
    }

    public void createContent(Composite parent) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void setInputData() {

    }

    @Override
    protected void addControlListeners() {

    }

    @Override
    protected void updatePageComplete(Object source) {

    }

    private void updateValues() {

    }

    private void updatePageEnablement() {

        NewJobWizard wizard = (NewJobWizard)getWizard();
        // if (nameValues.isCreateEnvironment()) {
        // wizard.setPageEnablement(LibraryListPage.NAME, true);
        // } else {
        // wizard.setPageEnablement(LibraryListPage.NAME, false);
        // }
    }

    @Override
    protected void storePreferences() {
    }
}
