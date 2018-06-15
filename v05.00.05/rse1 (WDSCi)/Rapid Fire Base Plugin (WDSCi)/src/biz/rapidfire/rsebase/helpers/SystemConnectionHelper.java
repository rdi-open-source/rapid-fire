/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.helpers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;

import biz.rapidfire.rsebase.ui.handlers.HandlerUtil;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.comm.interfaces.IISeriesMember;
import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.iseries.core.api.ISeriesMember;
import com.ibm.etools.iseries.core.resources.ISeriesEditableSrcPhysicalFileMember;
import com.ibm.etools.systems.core.SystemPlugin;
import com.ibm.etools.systems.filters.SystemFilterReference;
import com.ibm.etools.systems.model.ISystemResourceChangeEvents;
import com.ibm.etools.systems.model.SystemConnection;
import com.ibm.etools.systems.model.SystemRegistry;
import com.ibm.etools.systems.model.impl.SystemResourceChangeEvent;
import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.SubSystemHelpers;

public class SystemConnectionHelper {

    /**
     * Returns the currently selected items from an ExecutionEvent.
     * 
     * @param event - Execution event that is queried for the selected items
     * @return selected items
     * @throws ExecutionException
     */
    public static ISelection getCurrentSelection(ExecutionEvent event) throws ExecutionException {

        return HandlerUtil.getCurrentSelection(event);
    }

    /**
     * Returns the subsystem that is identified by the provided connection name.
     * 
     * @param connectionName - name of the system connection whose subsystem is
     *        returned
     * @return subsystem that is identified by the provided connection name
     */
    public static Object getSubSystem(String connectionName, Class<?> clazz) {

        ISeriesConnection connection = ISeriesConnection.getConnection(connectionName);
        if (connection == null) {
            return null;
        }

        SystemConnection sytemConnection = connection.getSystemConnection();
        if (sytemConnection == null) {
            return null;
        }

        SubSystem[] subSystems = sytemConnection.getSubSystems();
        if (subSystems == null || subSystems.length == 0) {
            return null;
        }

        for (SubSystem subSystem : subSystems) {
            Class[] interfaces = subSystem.getClass().getInterfaces();
            for (Class interfaze : interfaces) {
                if (interfaze.equals(clazz)) {
                    return subSystem;
                }
            }
        }

        return null;
    }

    /**
     * Checks whether 'element' is a filter reference or not.
     * 
     * @param element - element that is tested for a filter reference
     * @return <code>true</code> if 'element' is a filter reference, else
     *         <code>false</code>
     */
    public static boolean isFilterReference(Object element) {

        if (element instanceof SystemFilterReference) {
            return true;
        }

        return false;
    }

    /**
     * Return the subsystem of a given filter reference
     * 
     * @param element - filter reference whose associated subsystem is returned
     * @return subsystem of the supplied filter reference
     */
    public static Object getSubSystemOfFilterReference(Object element) {

        if (element instanceof SystemFilterReference) {
            SystemFilterReference filterReference = (SystemFilterReference)element;
            SubSystem subSystem = SubSystemHelpers.getParentSubSystem(filterReference.getParentSystemFilterReferencePool());
            return subSystem;
        }

        return null;
    }

    public static void refreshUICreated(boolean isSlowConnection, Object subSystem, Object resource, Object... parents) {

        if (resource != null) {
            SystemRegistry sr = SystemPlugin.getTheSystemRegistry();
            for (Object parent : parents) {
                if (parent instanceof SystemFilterReference) {
                    SystemFilterReference systemFilterReference = (SystemFilterReference)parent;
                    sr.fireEvent(new SystemResourceChangeEvent(systemFilterReference, ISystemResourceChangeEvents.EVENT_EXPAND_SELECTED, null));
                    systemFilterReference.markStale(true);
                    sr.fireEvent(new SystemResourceChangeEvent(systemFilterReference, ISystemResourceChangeEvents.EVENT_REFRESH, null));
                } else {
                    sr.fireEvent(new SystemResourceChangeEvent(parent, ISystemResourceChangeEvents.EVENT_EXPAND_SELECTED, null));
                    sr.fireEvent(new SystemResourceChangeEvent(resource, ISystemResourceChangeEvents.EVENT_ADD, parent));
                    if (!isSlowConnection) {
                        sr.fireEvent(new SystemResourceChangeEvent(resource, ISystemResourceChangeEvents.EVENT_CHANGE_CHILDREN, parent));
                    }
                }
            }
        }
    }

    public static void refreshUIChanged(boolean isSlowConnection, Object subSystem, Object resource, Object... parents) {

        if (resource != null) {
            SystemRegistry sr = SystemPlugin.getTheSystemRegistry();
            for (Object parent : parents) {
                sr.fireEvent(new SystemResourceChangeEvent(resource, ISystemResourceChangeEvents.EVENT_REFRESH, parent));
            }
        }
    }

    public static void refreshUIDeleted(boolean isSlowConnection, Object subSystem, Object resource, Object... parents) {

        if (resource != null) {
            SystemRegistry sr = SystemPlugin.getTheSystemRegistry();
            for (Object parent : parents) {
                sr.fireEvent(new SystemResourceChangeEvent(resource, ISystemResourceChangeEvents.EVENT_DELETE, parent));
            }
        }
    }

    /**
     * Open an editable source member.
     * 
     * @param connectionName - Name of the connection a system is returned for
     * @param libraryName - library where the source file is stored
     * @param fileName - source file that contains the member
     * @param memberName - source member that is opened
     * @return true, if the member could be opened.
     * @throws Exception
     */
    public static boolean openMember(String connectionName, String libraryName, String fileName, String memberName) throws Exception {

        ISeriesConnection connection = ISeriesConnection.getConnection(connectionName);
        if (connection == null) {
            return false;
        }

        IISeriesMember qsysMember = connection.getISeriesMember(libraryName, fileName, memberName);
        if (qsysMember instanceof ISeriesMember) {
            ISeriesEditableSrcPhysicalFileMember editableMember = new ISeriesEditableSrcPhysicalFileMember((ISeriesMember)qsysMember);
            editableMember.open(Display.getCurrent().getActiveShell(), false);
        }

        return true;
    }

    /**
     * Returns a system for a given connection name.
     * 
     * @parm connectionName - Name of the connection a system is returned for
     * @return AS400
     */
    public static AS400 getSystem(String connectionName) throws Exception {

        ISeriesConnection connection = ISeriesConnection.getConnection(connectionName);
        if (connection == null) {
            return null;
        }

        return connection.getAS400ToolboxObject(null);
    }

    /**
     * Returns a system for a given connection name. Does not throw an
     * exception.
     * 
     * @param connectionName - Name of the connection a system is returned for
     * @return AS400
     */
    public static AS400 getSystemChecked(String connectionName) {

        try {
            return getSystem(connectionName);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Returns a system for a given profile and connection name.
     * 
     * @param profile - Profile that is searched for the connection
     * @param connectionName - Name of the connection a system is returned for
     * @return AS400
     * @throws Exception
     */
    public static AS400 findSystem(String hostName) throws Exception {

        ISeriesConnection[] connections = ISeriesConnection.getConnections();
        for (ISeriesConnection connection : connections) {
            if (connection.getHostName().equalsIgnoreCase(hostName)) {
                return connection.getAS400ToolboxObject(null);
            }
        }

        return null;
    }

    /**
     * Returns an array of connection names sorted by connection name.
     * 
     * @return sorted list of connection names
     */
    public static String[] getConnectionNames() {

        List<String> connectionNames = new LinkedList<String>();

        ISeriesConnection[] connections = getConnections();
        for (ISeriesConnection connection : connections) {
            connectionNames.add(connection.getConnectionName());
        }

        return connectionNames.toArray(new String[connectionNames.size()]);
    }

    /**
     * Returns an array of hosts sorted by connection name.
     * 
     * @return sorted list of hosts
     */
    public static SystemConnection[] getHosts() {

        ISeriesConnection[] connections = getConnections();

        List<SystemConnection> hosts = new LinkedList<SystemConnection>();
        for (ISeriesConnection connection : connections) {
            hosts.add(connection.getSystemConnection());
        }

        return hosts.toArray(new SystemConnection[hosts.size()]);
    }

    /**
     * Returns an array of IBMiConnections sorted by connection name.
     * 
     * @return sorted list of IBMiConnections
     */
    private static ISeriesConnection[] getConnections() {

        ISeriesConnection[] connections = ISeriesConnection.getConnections();

        Arrays.sort(connections, new Comparator<ISeriesConnection>() {

            public int compare(ISeriesConnection o1, ISeriesConnection o2) {

                if (o2 == null || o2.getConnectionName() == null) {
                    return 1;
                } else if (o1 == null || o1.getConnectionName() == null) {
                    return -1;
                } else {
                    return o1.getConnectionName().compareToIgnoreCase(o2.getConnectionName());
                }

            }
        });

        return connections;
    }
}
