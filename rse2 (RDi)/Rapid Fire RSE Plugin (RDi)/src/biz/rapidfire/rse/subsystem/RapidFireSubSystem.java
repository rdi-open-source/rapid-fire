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
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.IProperty;
import org.eclipse.rse.core.model.IPropertySet;
import org.eclipse.rse.core.model.PropertySet;
import org.eclipse.rse.core.model.SystemMessageObject;
import org.eclipse.rse.core.subsystems.IConnectorService;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.core.subsystems.SubSystem;
import org.eclipse.rse.services.clientserver.NamePatternMatcher;
import org.eclipse.rse.services.clientserver.messages.SystemMessage;
import org.eclipse.rse.ui.RSEUIPlugin;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.model.IRapidFireInstanceResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.RapidFireFilter;
import biz.rapidfire.rse.model.RapidFireInstanceResource;

import com.ibm.etools.iseries.subsystems.qsys.IISeriesSubSystem;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;
import com.ibm.etools.iseries.subsystems.qsys.commands.QSYSCommandSubSystem;
import com.ibm.etools.iseries.subsystems.qsys.objects.QSYSObjectSubSystem;

public class RapidFireSubSystem extends SubSystem implements IISeriesSubSystem, IRapidFireSubSystem {

    private CommunicationsListener communicationsListener;
    private RapidFireSubSystemAttributes subSystemAttributes;
    private boolean connected;

    public RapidFireSubSystem(IHost host, IConnectorService connectorService) {
        super(host, connectorService);

        this.connected = false;
        this.subSystemAttributes = new RapidFireSubSystemAttributes(this);

        this.communicationsListener = new CommunicationsListener(this);
        getConnectorService().addCommunicationsListener(communicationsListener);
    }

    public RapidFireSubSystemAttributes getSubSystemAttributes() {
        return subSystemAttributes;
    }

    public void setParentConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    protected Object[] internalResolveFilterString(String filterString, IProgressMonitor monitor) throws InvocationTargetException,
        InterruptedException {

        IRapidFireInstanceResource[] allResources = getSubSystemAttributes().getRapidFireInstances();

        RapidFireFilter filter = new RapidFireFilter(filterString);
        NamePatternMatcher libraryMatcher = new NamePatternMatcher(filter.getLibrary());

        Vector<IRapidFireInstanceResource> filteredResources = new Vector<IRapidFireInstanceResource>();
        for (int i = 0; i < allResources.length; i++) {
            if (libraryMatcher.matches(allResources[i].getLibrary())) {
                filteredResources.addElement(allResources[i]);
            }
        }

        return filteredResources.toArray(new RapidFireInstanceResource[filteredResources.size()]);
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
    protected Object[] internalResolveFilterString(Object parent, String filterString, IProgressMonitor monitor) throws InvocationTargetException,
        InterruptedException {

        return internalResolveFilterString(filterString, monitor);
    }

    public QSYSObjectSubSystem getCommandExecutionProperties() {
        return IBMiConnection.getConnection(getHost()).getQSYSObjectSubSystem();
    }

    public QSYSCommandSubSystem getCmdSubSystem() {

        IHost iHost = getHost();
        ISubSystem[] iSubSystems = iHost.getSubSystems();
        for (int ssIndx = 0; ssIndx < iSubSystems.length; ssIndx++) {
            SubSystem subsystem = (SubSystem)iSubSystems[ssIndx];
            if ((subsystem instanceof QSYSCommandSubSystem)) {
                return (QSYSCommandSubSystem)subsystem;
            }
        }
        return null;
    }

    public ISubSystem getObjectSubSystem() {

        IHost iHost = getHost();
        ISubSystem[] iSubSystems = iHost.getSubSystems();
        for (int ssIndx = 0; ssIndx < iSubSystems.length; ssIndx++) {
            ISubSystem iSubSystem = iSubSystems[ssIndx];
            if ((iSubSystem instanceof QSYSObjectSubSystem)) {
                return iSubSystem;
            }
        }

        return null;
    }

    private SystemMessageObject createErrorMessage(Throwable e) {

        SystemMessage msg = RSEUIPlugin.getPluginMessage("RSEO1012"); //$NON-NLS-1$
        msg.makeSubstitution(e.getMessage());
        SystemMessageObject msgObj = new SystemMessageObject(msg, 0, null);

        return msgObj;
    }

    // private AS400 getToolboxAS400Object() {
    //
    // try {
    // return IBMiConnection.getConnection(getHost()).getAS400ToolboxObject();
    // } catch (SystemMessageException e) {
    // RapidFireCorePlugin.logError(e.getLocalizedMessage(), e);
    // return null;
    // }
    // }

    public String getVendorAttribute(String key) {

        IProperty property = getVendorAttributes().getProperty(key);
        if (property == null) {
            return null;
        }

        return property.getValue();
    }

    public void setVendorAttribute(String key, String value) {
        getVendorAttributes().addProperty(key, value);
    }

    public void removeVendorAttribute(String key) {
        getVendorAttributes().removeProperty(key);
    }

    private IPropertySet getVendorAttributes() {

        IPropertySet propertySet = getPropertySet(RapidFireSubSystemAttributes.VENDOR_ID);
        if (propertySet == null) {
            propertySet = new PropertySet(RapidFireSubSystemAttributes.VENDOR_ID);
            addPropertySet(propertySet);
        }

        return propertySet;
    }
}