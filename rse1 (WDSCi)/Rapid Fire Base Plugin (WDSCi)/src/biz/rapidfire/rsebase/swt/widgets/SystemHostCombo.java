/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.swt.widgets;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.iseries.core.ui.widgets.ISeriesConnectionCombo;
import com.ibm.etools.systems.model.SystemConnection;

public class SystemHostCombo {

    private ISeriesConnectionCombo connectionCombo;

    public SystemHostCombo(Composite parent) {
        this(parent, SWT.NONE);
    }

    public SystemHostCombo(Composite parent, int style) {
        ISeriesConnection connection=null;
        this.connectionCombo = new ISeriesConnectionCombo(parent, connection, true);
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
        return connectionCombo.getSystemConnection().getAliasName();
    }

    public String getHostName() {
        return connectionCombo.getSystemConnection().getHostName();
    }

    public boolean selectConnection(String connectionName) {

        if (connectionName == null) {
            return false;
        }

        SystemConnection[] hosts = connectionCombo.getConnections();
        for (int i = 0; i < hosts.length; i++) {
            SystemConnection connection = hosts[i];
            if (connectionName.equalsIgnoreCase(connection.getAliasName())) {
                connectionCombo.setSelectionIndex(i);
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
