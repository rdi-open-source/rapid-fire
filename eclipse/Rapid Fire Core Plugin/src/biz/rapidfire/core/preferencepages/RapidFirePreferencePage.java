/*******************************************************************************
 * Copyright (c) 2012-2015 iSphere Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.preferencepages;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class RapidFirePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private Button btnLargeProgressBar;

    public RapidFirePreferencePage() {
        super();

        setPreferenceStore(RapidFireCorePlugin.getDefault().getPreferenceStore());
        getPreferenceStore();
    }

    public void init(IWorkbench workbench) {
    }

    @Override
    public Control createContents(Composite parent) {

        Composite container = new Composite(parent, SWT.NONE);
        final GridLayout gridLayout = new GridLayout(2, false);
        container.setLayout(gridLayout);

        createSectionGlobal(container);

        setScreenToValues();

        return container;
    }

    private void createSectionGlobal(Composite parent) {

        btnLargeProgressBar = WidgetFactory.createCheckbox(parent);
        btnLargeProgressBar.setText(Messages.Label_Enable_large_progress_bar);
        btnLargeProgressBar.setToolTipText(Messages.Tooltip_Enable_large_progress_bar);
        btnLargeProgressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        btnLargeProgressBar.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
                if (validateLargeProgressBar()) {
                    checkAllValues();
                }
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });
    }

    @Override
    protected void performApply() {
        super.performApply();
    }

    @Override
    protected void performDefaults() {
        setScreenToDefaultValues();
        super.performDefaults();
    }

    @Override
    public boolean performOk() {

        setStoreToValues();

        return super.performOk();
    }

    protected void setStoreToValues() {

        Preferences preferences = getPreferences();

        preferences.setLargeProgressBar(btnLargeProgressBar.getSelection());
    }

    protected void setScreenToValues() {

        Preferences preferences = getPreferences();

        btnLargeProgressBar.setSelection(preferences.isLargeProgressBar());

        checkAllValues();
        setControlsEnablement();
    }

    protected void setScreenToDefaultValues() {

        Preferences preferences = getPreferences();

        btnLargeProgressBar.setSelection(preferences.getDefaultIsLargeProgressBar());

        checkAllValues();
        setControlsEnablement();
    }

    private boolean validateLargeProgressBar() {

        return true;
    }

    private boolean checkAllValues() {

        if (!validateLargeProgressBar()) {
            return false;
        }

        return clearError();
    }

    private void setControlsEnablement() {

    }

    private boolean setError(String message) {
        setErrorMessage(message);
        setValid(false);
        return false;
    }

    private boolean clearError() {
        setErrorMessage(null);
        setValid(true);
        return true;
    }

    private GridData createLabelLayoutData() {
        return new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    }

    private Preferences getPreferences() {
        return Preferences.getInstance();
    }
}