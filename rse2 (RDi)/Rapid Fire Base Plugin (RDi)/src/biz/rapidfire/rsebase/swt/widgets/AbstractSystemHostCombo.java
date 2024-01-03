/*******************************************************************************
 * Copyright (c) 2017-2021 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.swt.widgets;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.ibm.etools.iseries.rse.ui.widgets.IBMiConnectionCombo;

import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

public abstract class AbstractSystemHostCombo {

    private IBMiConnectionCombo connectionCombo;

    public AbstractSystemHostCombo(Composite parent, int style, boolean showNewButton, boolean showLabel) {

        this.connectionCombo = new IBMiConnectionCombo(parent, style, null, showNewButton, showLabel);

        this.connectionCombo.setConnections(SystemConnectionHelper.getHosts());
        this.connectionCombo.setItems(SystemConnectionHelper.getConnectionNames());
        this.connectionCombo.setSelectionIndex(0);
    }

    public boolean isDisposed() {
        return connectionCombo.isDisposed();
    }

    public void setEnabled(boolean enabled) {
        connectionCombo.setEnabled(enabled);
    }

    public boolean getEnabled() {
        return connectionCombo.getEnabled();
    }

    public void setFocus() {
        connectionCombo.setFocus();
    }

    public void setData(String key, Object data) {
        connectionCombo.setData(key, data);
    }

    public Object getData(String key) {
        return connectionCombo.getData(key);
    }

    public void addModifyListener(ModifyListener listener) {
        connectionCombo.addModifyListener(listener);
    }

    public void removeModifyListener(ModifyListener listener) {
        connectionCombo.removeModifyListener(listener);
    }

    public void addSelectionListener(SelectionListener listener) {
        connectionCombo.addSelectionListener(listener);
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        connectionCombo.addSelectionChangedListener(listener);
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        connectionCombo.removeSelectionChangedListener(listener);
    }

    public ISelection getSelection() {
        return connectionCombo.getSelection();
    }

    public void setSelection(ISelection selection) {
        connectionCombo.setSelection(selection);
    }

    public int getSelectionIndex() {
        return connectionCombo.getSelectionIndex();
    }

    public void setSelectionIndex(int index) {
        connectionCombo.setSelectionIndex(index);
    }

    public Combo getCombo() {
        return connectionCombo.getCombo();
    }

    public String getText() {
        return connectionCombo.getText();
    }

    public String getConnectionName() {

        IHost host = connectionCombo.getHost();
        if (host == null) {
            return null;
        }

        return host.getAliasName();
    }

    public String getHostName() {

        IHost host = connectionCombo.getHost();
        if (host == null) {
            return null;
        }

        return host.getHostName();
    }

    public boolean selectConnection(String connectionName) {

        if (connectionName == null) {
            return false;
        }

        IHost[] hosts = connectionCombo.getConnections();
        for (int i = 0; i < hosts.length; i++) {
            IHost host = hosts[i];
            if (connectionName.equalsIgnoreCase(host.getAliasName())) {
                connectionCombo.select(host);
                return true;
            }
        }

        return false;
    }

    public void setToolTipText(String tip) {
        connectionCombo.setToolTipText(tip);
    }

    public void setLayoutData(Object layoutData) {
        connectionCombo.setLayoutData(layoutData);
    }
}
