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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
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
    private Combo textDateFormat;
    private Combo textTimeFormat;
    private Button btnIsSlowConnection;
    private Button btnIsActionCacheEnabled;
    private Button btnSkipDisabledPages;

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
        WidgetFactory.createLineFiller(container, SWT.DEFAULT);

        createSectionGlobal(container);
        WidgetFactory.createLineFiller(container, SWT.DEFAULT);

        createSectionDateAndTime(container);
        WidgetFactory.createLineFiller(container, SWT.DEFAULT);

        createSectionConnection(container);
        WidgetFactory.createLineFiller(container, SWT.DEFAULT);

        createSectionWizard(container);

        setScreenToValues();

        return container;
    }

    private void createSectionLabelDecorations(Composite parent) {

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

        Group groupDateAndTimeFormats = new Group(parent, SWT.NONE);
        groupDateAndTimeFormats.setLayout(new GridLayout(2, false));
        groupDateAndTimeFormats.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
        groupDateAndTimeFormats.setText(Messages.Label_Job_status_view);

        btnLargeProgressBar = WidgetFactory.createCheckbox(groupDateAndTimeFormats);
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

    private void createSectionDateAndTime(Composite parent) {

        Group groupDateAndTimeFormats = new Group(parent, SWT.NONE);
        groupDateAndTimeFormats.setLayout(new GridLayout(3, false));
        groupDateAndTimeFormats.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
        groupDateAndTimeFormats.setText(Messages.Label_DateAndTimeFormats);

        WidgetFactory.createLabel(groupDateAndTimeFormats, Messages.Label_Date_colon,
            Messages.Tooltip_Specifies_the_format_for_displaying_date_values);

        textDateFormat = WidgetFactory.createReadOnlyCombo(groupDateAndTimeFormats);
        textDateFormat.setToolTipText(Messages.Tooltip_Specifies_the_format_for_displaying_date_values);
        textDateFormat.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        textDateFormat.setItems(Preferences.getInstance().getDateFormatLabels());

        WidgetFactory.createLabel(groupDateAndTimeFormats, Messages.Label_Time_colon,
            Messages.Tooltip_Specifies_the_format_for_displaying_time_values);

        textTimeFormat = WidgetFactory.createReadOnlyCombo(groupDateAndTimeFormats);
        textTimeFormat.setToolTipText(Messages.Tooltip_Specifies_the_format_for_displaying_time_values);
        textTimeFormat.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        textTimeFormat.setItems(Preferences.getInstance().getTimeFormatLabels());
    }

    private void createSectionConnection(Composite parent) {

        Group groupRemoteConnection = new Group(parent, SWT.NONE);
        groupRemoteConnection.setLayout(new GridLayout(1, false));
        groupRemoteConnection.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
        groupRemoteConnection.setText(Messages.Label_Remote_Connection_settings);

        btnIsSlowConnection = WidgetFactory.createCheckbox(groupRemoteConnection);
        btnIsSlowConnection.setText(Messages.Label_Is_slow_connection);
        btnIsSlowConnection.setToolTipText(Messages.Tooltip_Is_slow_connection);
        btnIsSlowConnection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        btnIsSlowConnection.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
                if (validateSlowConnection()) {
                    checkAllValues();
                }
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });

        btnIsActionCacheEnabled = WidgetFactory.createCheckbox(groupRemoteConnection);
        btnIsActionCacheEnabled.setText(Messages.Label_Enable_action_cache);
        btnIsActionCacheEnabled.setToolTipText(Messages.Tooltip_Enable_action_cache);
        btnIsActionCacheEnabled.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        btnIsActionCacheEnabled.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
                if (validateActionCache()) {
                    checkAllValues();
                }
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });
    }

    private void createSectionWizard(Composite parent) {

        // Wizard settings
        Group groupWizardSettings = new Group(parent, SWT.NONE);
        groupWizardSettings.setLayout(new GridLayout(3, false));
        groupWizardSettings.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
        groupWizardSettings.setText(Messages.Label_Wizard_settings);

        btnSkipDisabledPages = WidgetFactory.createCheckbox(groupWizardSettings);
        btnSkipDisabledPages.setText(Messages.Label_Skip_disabled_pages);
        btnSkipDisabledPages.setToolTipText(Messages.Tooltip_Skip_disabled_pages);
        btnSkipDisabledPages.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        btnSkipDisabledPages.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
                if (validateSkipDisabledPages()) {
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
        preferences.setDateFormatLabel(textDateFormat.getText());
        preferences.setTimeFormatLabel(textTimeFormat.getText());
        preferences.setSlowConnection(btnIsSlowConnection.getSelection());
        preferences.setActionCacheEnabled(btnIsActionCacheEnabled.getSelection());
        preferences.setSkipDisabledWizardPages(btnSkipDisabledPages.getSelection());
    }

    protected void setScreenToValues() {

        Preferences preferences = getPreferences();

        btnLargeProgressBar.setSelection(preferences.isLargeProgressBar());
        textDateFormat.setText(preferences.getDateFormatLabel());
        textTimeFormat.setText(preferences.getTimeFormatLabel());
        btnIsSlowConnection.setSelection(preferences.isSlowConnection());
        btnIsActionCacheEnabled.setSelection(preferences.isActionCacheEnabled());
        btnSkipDisabledPages.setSelection(preferences.skipDisabledWizardPages());

        checkAllValues();
        setControlsEnablement();
    }

    protected void setScreenToDefaultValues() {

        Preferences preferences = getPreferences();

        btnLargeProgressBar.setSelection(preferences.getDefaultIsLargeProgressBar());
        textDateFormat.setText(preferences.getDefaultDateFormatLabel());
        textTimeFormat.setText(preferences.getDefaultTimeFormatLabel());
        btnIsSlowConnection.setSelection(preferences.getDefaultIsSlowConnection());
        btnIsActionCacheEnabled.setSelection(preferences.getDefaultIsActionCacheEnabled());
        btnSkipDisabledPages.setSelection(preferences.getDefaultSkipDisabledWizardPages());

        checkAllValues();
        setControlsEnablement();
    }

    private boolean validateLargeProgressBar() {

        return true;
    }

    private boolean validateSlowConnection() {

        return true;
    }

    private boolean validateActionCache() {

        return true;
    }

    private boolean validateSkipDisabledPages() {

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