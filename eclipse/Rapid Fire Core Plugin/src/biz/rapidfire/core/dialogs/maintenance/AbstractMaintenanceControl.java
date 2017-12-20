/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import biz.rapidfire.core.maintenance.MaintenanceMode;

public abstract class AbstractMaintenanceControl extends Composite implements ModifyListener, SelectionListener {

    private boolean isParentKeyFieldsVisible;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

    public AbstractMaintenanceControl(Composite parent, int style, boolean parentKeyFieldsVisible) {
        super(parent, style);

        this.isParentKeyFieldsVisible = parentKeyFieldsVisible;

        GridLayout layout = new GridLayout(2, false);
        setLayout(layout);

        createContent(this);
        setMode(MaintenanceMode.DISPLAY);
    }

    protected boolean isParentKeyFieldsVisible() {
        return isParentKeyFieldsVisible;
    }

    public void setMode(MaintenanceMode mode) {

        if (MaintenanceMode.CREATE.equals(mode) || MaintenanceMode.COPY.equals(mode)) {
            enableParentKeyFields = false;
            enableKeyFields = true;
            enableFields = true;
        } else if (MaintenanceMode.CHANGE.equals(mode)) {
            enableParentKeyFields = false;
            enableKeyFields = false;
            enableFields = true;
        } else {
            enableParentKeyFields = false;
            enableKeyFields = false;
            enableFields = false;
        }
    }

    protected boolean isParentKeyFieldsEnabled() {
        return enableParentKeyFields;
    }

    protected boolean isKeyFieldsEnabled() {
        return enableKeyFields;
    }

    protected boolean isFieldsEnabled() {
        return enableFields;
    }

    protected abstract void createContent(Composite parent);

    public void widgetDefaultSelected(SelectionEvent arg0) {
    }

    public void widgetSelected(SelectionEvent arg0) {
    }

    public void modifyText(ModifyEvent arg0) {
    }

}
