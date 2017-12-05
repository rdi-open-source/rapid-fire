/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.preferencepages;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferencesUtil;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class AppearancePage extends PreferencePage implements IWorkbenchPreferencePage {

    private Button btnLargeProgressBar;

    public AppearancePage() {
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

        createSectionLabelDecorations(container);
        createSectionGlobal(container);

        setScreenToValues();

        return container;
    }

    private void createSectionLabelDecorations(Composite parent) {

        // Group group = new Group(parent, SWT.NONE);
        // group.setLayout(new GridLayout(3, false));
        // group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        // group.setText(Messages.Label_Decorations_RSE_host_objects);

        String headline = Messages.bind(Messages.Label_Label_Decorations_RSE_host_objects_Description, new String[] {
            "<a href=\"org.eclipse.ui.preferencePages.Decorators\">", "</a>" });

        Link lnkJavaTaskTags = new Link(parent, SWT.MULTI | SWT.WRAP);
        lnkJavaTaskTags.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        lnkJavaTaskTags.setText(headline);
        lnkJavaTaskTags.pack();
        lnkJavaTaskTags.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                PreferencesUtil.createPreferenceDialogOn(getShell(), e.text, null, null);
            }
        });
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