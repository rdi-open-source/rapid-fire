/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import biz.rapidfire.core.preferences.Preferences;

public abstract class AbstractWizardPage extends WizardPage implements ModifyListener, SelectionListener {

    private boolean isEnabled;

    protected AbstractWizardPage(String name) {
        super(name);

    }

    public void setFocus() {
    }

    public void createControl(Composite parent) {

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, false));
        container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        createContent(container);

        setControl(container);
        setInputData();
        addControlListeners();
        updatePageComplete(null);

        setErrorMessage(null);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean visible) {
        this.isEnabled = visible;
    }

    public void updateMode() {
    }

    public void modifyText(ModifyEvent event) {
        updatePageComplete(event.getSource());
    }

    public void widgetDefaultSelected(SelectionEvent event) {
        updatePageComplete(event.getSource());
    }

    public void widgetSelected(SelectionEvent event) {
        updatePageComplete(event.getSource());
    }

    @Override
    public AbstractNewWizard<?> getWizard() {
        return (AbstractNewWizard<?>)super.getWizard();
    }

    public void prepareForDisplay() {
    }

    protected void scheduleUpdatePageComplete(IUpdatePageCompleteHandler handler, Object source) {
        getWizard().scheduleUpdatePageComplete(handler, source);
    }

    protected void setInputData() {
    }

    protected abstract void addControlListeners();

    protected abstract void updatePageComplete(Object source);

    protected abstract void createContent(Composite container);

    protected Preferences getPreferences() {
        return Preferences.getInstance();
    }

    protected void storePreferences() {
    }
}
