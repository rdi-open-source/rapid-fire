/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.job;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceControl;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class JobMaintenanceControl extends AbstractMaintenanceControl {

    private Text textJobName;
    private Text textDescription;
    private Button buttonCreateEnvironment;
    private Text textJobQueueName;
    private Text textJobQueueLibraryName;
    private Button buttonCancelASPThresholdExceeds;

    public JobMaintenanceControl(Composite parent, int style) {
        super(parent, style, true);
    }

    public JobMaintenanceControl(Composite parent, boolean parentKeyFieldsVisible, int style) {
        super(parent, style, parentKeyFieldsVisible);
    }

    public void setFocusJobName() {
        textJobName.setFocus();
    }

    public void setFocusDescription() {
        textDescription.setFocus();
    }

    public void setFocusCreateEnvironment() {
        buttonCreateEnvironment.setFocus();
    }

    public void setFocusJobQueueName() {
        textJobQueueName.setFocus();
    }

    public void setFocusJobQueueLibraryName() {
        textJobQueueLibraryName.setFocus();
    }

    public void setFocusCancelASPThresholdExceeds() {
        buttonCancelASPThresholdExceeds.setFocus();
    }

    @Override
    public void setMode(MaintenanceMode mode) {

        super.setMode(mode);

        textJobName.setEnabled(isKeyFieldsEnabled());
        textDescription.setEnabled(isFieldsEnabled());
        buttonCreateEnvironment.setEnabled(isFieldsEnabled());
        textJobQueueName.setEnabled(isFieldsEnabled());
        textJobQueueLibraryName.setEnabled(isFieldsEnabled());
        buttonCancelASPThresholdExceeds.setEnabled(isFieldsEnabled());
    }

    @Override
    protected void createContent(Composite parent) {

        WidgetFactory.createLabel(parent, Messages.Label_Job_colon, Messages.Tooltip_Job);

        textJobName = WidgetFactory.createNameText(parent);
        textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobName.setToolTipText(Messages.Tooltip_Job);

        WidgetFactory.createLabel(parent, Messages.Label_Description_colon, Messages.Tooltip_Description);

        textDescription = WidgetFactory.createDescriptionText(parent);
        textDescription.setTextLimit(IRapidFireJobResource.DESCRIPTION_MAX_LENGTH);
        textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textDescription.setToolTipText(Messages.Tooltip_Description);

        WidgetFactory.createLabel(parent, Messages.Label_Create_environment_colon, Messages.Tooltip_Create_environment);

        buttonCreateEnvironment = WidgetFactory.createCheckbox(parent);
        buttonCreateEnvironment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        buttonCreateEnvironment.setToolTipText(Messages.Tooltip_Create_environment);

        WidgetFactory.createLabel(parent, Messages.Label_Job_queue_name_colon, Messages.Tooltip_Job_queue_name);

        textJobQueueName = WidgetFactory.createNameText(parent);
        textJobQueueName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobQueueName.setToolTipText(Messages.Tooltip_Job_queue_name);

        WidgetFactory.createLabel(parent, Messages.Label_Job_queue_library_name_colon, Messages.Tooltip_Job_queue_library_name);

        textJobQueueLibraryName = WidgetFactory.createNameText(parent);
        textJobQueueLibraryName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobQueueLibraryName.setToolTipText(Messages.Tooltip_Job_queue_library_name);

        WidgetFactory.createLabel(parent, Messages.Label_Cancel_ASP_threshold_exceeds_colon, Messages.Tooltip_Cancel_ASP_threshold_exceeds);

        buttonCancelASPThresholdExceeds = WidgetFactory.createCheckbox(parent);
        buttonCancelASPThresholdExceeds.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        buttonCancelASPThresholdExceeds.setToolTipText(Messages.Tooltip_Cancel_ASP_threshold_exceeds);

    }

    public String getJobName() {
        return textJobName.getText();
    }

    public void setJobName(String jobName) {
        textJobName.setText(jobName);
    }

    public String getDescription() {
        return textDescription.getText();
    }

    public void setDescription(String description) {
        textDescription.setText(description);
    }

    public boolean isCreateEnvironment() {
        return buttonCreateEnvironment.getSelection();
    }

    public void setCreateEnvironment(boolean createEnvironment) {
        buttonCreateEnvironment.setSelection(createEnvironment);
    }

    public String getJobQueueName() {
        return textJobQueueName.getText();
    }

    public void setJobQueueName(String jobQueueName) {
        textJobQueueName.setText(jobQueueName);
    }

    public String getJobQueueLibraryName() {
        return textJobQueueLibraryName.getText();
    }

    public void setJobQueueLibraryName(String jobQueueLibraryName) {
        textJobQueueLibraryName.setText(jobQueueLibraryName);
    }

    public boolean isCancelASPThresholdExceeds() {
        return buttonCancelASPThresholdExceeds.getSelection();
    }

    public void setCancelASPThresholdExceeds(boolean cancelASPThresholdExceeds) {
        buttonCancelASPThresholdExceeds.setSelection(cancelASPThresholdExceeds);
    }

    public void addSelectionListener(SelectionListener listener) {
        buttonCreateEnvironment.addSelectionListener(listener);
        buttonCancelASPThresholdExceeds.addSelectionListener(listener);
    }

    public void removeSelectionListener(SelectionListener listener) {
        buttonCreateEnvironment.removeSelectionListener(listener);
        buttonCancelASPThresholdExceeds.removeSelectionListener(listener);
    }

    public void addModifyListener(ModifyListener listener) {

        textJobName.addModifyListener(listener);
        textDescription.addModifyListener(listener);
        textJobQueueName.addModifyListener(listener);
        textJobQueueLibraryName.addModifyListener(listener);
    }

    public void removeModifyListener(ModifyListener listener) {

        textJobName.removeModifyListener(listener);
        textDescription.removeModifyListener(listener);
        textJobQueueName.removeModifyListener(listener);
        textJobQueueLibraryName.removeModifyListener(listener);
    }
}
