/*******************************************************************************
 * Copyright (c) 2005 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *     iSphere Project Owners - Maintenance and enhancements
 *******************************************************************************/
package biz.rapidfire.rse.subsystem;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.RapidFireFilter;
import biz.rapidfire.rse.model.RapidFireJobResource;
import biz.rapidfire.rse.model.dao.JobsDAO;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.core.IISeriesSubSystem;
import com.ibm.etools.iseries.core.IISeriesSubSystemCommandExecutionProperties;
import com.ibm.etools.iseries.core.ISeriesSystemDataStore;
import com.ibm.etools.iseries.core.ISeriesSystemManager;
import com.ibm.etools.iseries.core.ISeriesSystemToolbox;
import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.systems.as400cmdsubsys.CmdSubSystem;
import com.ibm.etools.systems.as400cmdsubsys.impl.CmdSubSystemImpl;
import com.ibm.etools.systems.as400filesubsys.FileSubSystem;
import com.ibm.etools.systems.core.SystemPlugin;
import com.ibm.etools.systems.core.messages.SystemMessage;
import com.ibm.etools.systems.dftsubsystem.impl.DefaultSubSystemImpl;
import com.ibm.etools.systems.model.SystemConnection;
import com.ibm.etools.systems.model.SystemRegistry;
import com.ibm.etools.systems.model.impl.SystemMessageObject;
import com.ibm.etools.systems.subsystems.ISystem;
import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.impl.AbstractSystemManager;

public class RapidFireSubSystem extends DefaultSubSystemImpl implements IISeriesSubSystem, IRapidFireSubSystem {

    private CommunicationsListener communicationsListener;
    private RapidFireSubSystemAttributes subSystemAttributes;
    private boolean connected;
    private SystemConnection connection;

    public RapidFireSubSystem(SystemConnection connection) {
        super();

        this.connected = false;
        this.connection = connection;
        this.subSystemAttributes = new RapidFireSubSystemAttributes(this);
    }

    public RapidFireSubSystemAttributes getSubSystemAttributes() {
        return subSystemAttributes;
    }

    public void setParentConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    protected Object[] internalResolveFilterString(IProgressMonitor monitor, String filterString ) throws InvocationTargetException,
        InterruptedException {

        try {
            RapidFireFilter filter = new RapidFireFilter(filterString);
            JobsDAO dao = new JobsDAO(getSystemConnectionName());
            List<IRapidFireJobResource> allResources = dao.load(filter.getLibrary());
            Vector<IRapidFireJobResource> filteredResources = new Vector<IRapidFireJobResource>();
            for (IRapidFireJobResource resource : allResources) {
                if (filter.matches(resource)) {
                    resource.setParentSubSystem(this);
                    filteredResources.addElement(resource);
                }
            }

            return filteredResources.toArray(new RapidFireJobResource[filteredResources.size()]);
        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could resolve filter string and load jobs ***", e); //$NON-NLS-1$
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        }

        return null;
    }

    @Override
    protected Object[] sortResolvedFilterStringObjects(Object[] input) {

        Arrays.sort(input);

        return input;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    @Override
    public Shell getShell() {
        // Damn, this caused me a lot of grief! Phil
        if (shell != null) {
            return shell;
        } else {
            return super.getShell();
        }
    }

    private void debugPrint(String message) {
        // System.out.println(message);
    }

    /*
     * Start of RDi/WDSCi specific methods.
     */

    @Override
    protected Object[] internalResolveFilterString(IProgressMonitor monitor, Object parent, String filterString) throws InvocationTargetException,
        InterruptedException {

        return internalResolveFilterString(monitor, filterString);
    }


    public IISeriesSubSystemCommandExecutionProperties getCommandExecutionProperties() {
        return getObjectSubSystem();
    }

    public CmdSubSystem getCmdSubSystem() {

        SystemConnection sc = getSystemConnection();
        SystemRegistry registry = SystemPlugin.getTheSystemRegistry();
        SubSystem[] subsystems = registry.getSubSystems(sc);
        SubSystem subsystem;

        for (int ssIndx = 0; ssIndx < subsystems.length; ssIndx++) {
            subsystem = subsystems[ssIndx];
            if (subsystem instanceof CmdSubSystemImpl) {
                return (CmdSubSystemImpl)subsystem;
            }
        }

        return null;
    }

    public FileSubSystem getObjectSubSystem() {
        return ISeriesConnection.getConnection(getSystemConnection()).getISeriesFileSubSystem();
    }

    private SystemMessageObject createErrorMessage(Throwable e) {

        SystemMessage msg = SystemPlugin.getPluginMessage("RSEO1012"); //$NON-NLS-1$
        msg.makeSubstitution(e.getMessage());
        SystemMessageObject msgObj = new SystemMessageObject(msg, 0, null);

        return msgObj;
    }

    public AS400 getToolboxAS400Object() {
        ISeriesSystemToolbox system = (ISeriesSystemToolbox)getSystem();
        return system.getAS400Object();
    }

    @Override
    public ISystem getSystem() {

        ISystem system = super.getSystem();

        if (communicationsListener == null) {
            communicationsListener = new CommunicationsListener(this);
            system.addCommunicationsListener(communicationsListener);
        }

        return system;
    }

    @Override
    public AbstractSystemManager getSystemManager() {
        return ISeriesSystemManager.getTheISeriesSystemManager();
    }

    public ISeriesSystemDataStore getISeriesSystem() {
        ISeriesSystemDataStore iSeriesSystemDataStore = (ISeriesSystemDataStore)getSystem();
        return iSeriesSystemDataStore;
    }

    public String getVendorAttribute(String key) {
        return super.getVendorAttribute(RapidFireSubSystemAttributes.VENDOR_ID, key);
    }

    public void setVendorAttribute(String key, String value) {
        super.setVendorAttribute(RapidFireSubSystemAttributes.VENDOR_ID, key, value);
    }

    public void removeVendorAttribute(String key) {
        removeVendorAttribute(key);
    }
}