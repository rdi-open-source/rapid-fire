/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.subsystems.AbstractConnectorService;

/**
 * Our system class that manages connecting to, and disconnecting from, our
 * remote server-side code.
 */
public class DeveloperConnectorService extends AbstractConnectorService {

    private boolean connected = false;

    /**
     * Constructor for DeveloperConnectorService.
     * 
     * @param host
     */
    public DeveloperConnectorService(IHost host) {
        super("connectorservice.devr.name", "connectorservice.devr.desc", host, 0);
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.rse.core.subsystems.IConnectorService#isConnected()
     */
    public boolean isConnected() {
        return connected;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.core.subsystems.AbstractConnectorService#internalConnect
     * (org.eclipse.core.runtime.IProgressMonitor)
     */
    protected void internalConnect(IProgressMonitor monitor) throws Exception {
        // pretend. Normally, we'd connect to our remote server-side code here
        connected = true;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.core.subsystems.AbstractConnectorService#internalDisconnect
     * (org.eclipse.core.runtime.IProgressMonitor)
     */
    public void internalDisconnect(IProgressMonitor monitor) throws Exception {
        // pretend. Normally, we'd disconnect from our remote server-side code
        // here
        connected = false;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.rse.core.subsystems.IConnectorService#
     * supportsRemoteServerLaunching()
     */
    public boolean supportsRemoteServerLaunching() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.rse.core.subsystems.IConnectorService#
     * supportsServerLaunchProperties()
     */
    public boolean supportsServerLaunchProperties() {
        return false;
    }

    public boolean supportsUserId() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean supportsPassword() {
        // TODO Auto-generated method stub
        return false;
    }

    public String getUserId() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setUserId(String paramString) {
        // TODO Auto-generated method stub

    }

    public void saveUserId() {
        // TODO Auto-generated method stub

    }

    public void removeUserId() {
        // TODO Auto-generated method stub

    }

    public void setPassword(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2) {
        // TODO Auto-generated method stub

    }

    public void savePassword() {
        // TODO Auto-generated method stub

    }

    public void removePassword() {
        // TODO Auto-generated method stub

    }

    public void clearPassword(boolean paramBoolean1, boolean paramBoolean2) {
        // TODO Auto-generated method stub

    }

    public boolean hasPassword(boolean paramBoolean) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean inheritsCredentials() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean sharesCredentials() {
        // TODO Auto-generated method stub
        return false;
    }

    public void clearCredentials() {
        // TODO Auto-generated method stub

    }

    public void acquireCredentials(boolean paramBoolean) throws OperationCanceledException {
        // TODO Auto-generated method stub

    }

    public boolean isSuppressed() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setSuppressed(boolean paramBoolean) {
        // TODO Auto-generated method stub

    }

    public boolean requiresPassword() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean requiresUserId() {
        // TODO Auto-generated method stub
        return false;
    }
}