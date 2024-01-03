/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.library;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceControl;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class LibraryMaintenanceControl extends AbstractMaintenanceControl {

    private Text textJobName;
    private Text textLibrary;
    private Text textShadowLibrary;

    public LibraryMaintenanceControl(Composite parent, int style) {
        super(parent, SWT.NONE, true);
    }

    public LibraryMaintenanceControl(Composite parent, boolean parentKeyFieldsVisible, int style) {
        super(parent, style, parentKeyFieldsVisible);
    }

    public void setFocusJobName() {
        textJobName.setFocus();
    }

    public void setFocusLibraryName() {
        textLibrary.setFocus();
    }

    public void setFocusShadowLibraryName() {
        textShadowLibrary.setFocus();
    }

    @Override
    public void setMode(MaintenanceMode mode) {

        super.setMode(mode);

        if (isParentKeyFieldsVisible()) {
            textJobName.setEnabled(isParentKeyFieldsEnabled());
        }

        textLibrary.setEnabled(isKeyFieldsEnabled());
        textShadowLibrary.setEnabled(isFieldsEnabled());
    }

    @Override
    protected void createContent(Composite parent) {

        if (isParentKeyFieldsVisible()) {

            WidgetFactory.createLabel(parent, Messages.Label_Job_colon, Messages.Tooltip_Job);

            textJobName = WidgetFactory.createNameText(parent);
            textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            textJobName.setToolTipText(Messages.Tooltip_Job);
        }

        WidgetFactory.createLabel(parent, Messages.Label_Library_colon, Messages.Tooltip_Library);

        textLibrary = WidgetFactory.createNameText(parent);
        textLibrary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textLibrary.setToolTipText(Messages.Tooltip_Library);

        WidgetFactory.createLabel(parent, Messages.Label_Shadow_library_colon, Messages.Tooltip_Shadow_library);

        textShadowLibrary = WidgetFactory.createNameText(parent);
        textShadowLibrary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textShadowLibrary.setToolTipText(Messages.Tooltip_Shadow_library);
    }

    public String getJobName() {
        return textJobName.getText();
    }

    public void setJobName(String jobName) {

        if (isParentKeyFieldsVisible()) {
            textJobName.setText(jobName);
        }
    }

    public String getLibraryName() {
        return textLibrary.getText();
    }

    public void setLibraryName(String libraryName) {
        textLibrary.setText(libraryName);
    }

    public String getShadowLibraryName() {
        return textShadowLibrary.getText();
    }

    public void setShadowLibraryName(String shadowLibraryName) {
        textShadowLibrary.setText(shadowLibraryName);
    }

    public void addModifyListener(ModifyListener listener) {

        if (isParentKeyFieldsVisible()) {
            textJobName.addModifyListener(listener);
        }

        textLibrary.addModifyListener(listener);
        textShadowLibrary.addModifyListener(listener);
    }

    public void removeModifyListener(ModifyListener listener) {

        if (isParentKeyFieldsVisible()) {
            textJobName.removeModifyListener(listener);
        }

        textLibrary.removeModifyListener(listener);
        textShadowLibrary.removeModifyListener(listener);
    }
}
