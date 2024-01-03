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
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.events.ISystemResourceChangeEvents;
import org.eclipse.rse.core.events.SystemResourceChangeEvent;
import org.eclipse.rse.core.filters.ISystemFilterReference;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.core.subsystems.SubSystemHelpers;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.rse.ui.resources.QSYSEditableRemoteSourceFileMember;
import com.ibm.etools.iseries.services.qsys.api.IQSYSMember;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;

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

        IBMiConnection connection = IBMiConnection.getConnection(connectionName);
        if (connection == null) {
            return null;
        }

        ISubSystem[] subSystems = connection.getSubSystems();
        if (subSystems == null || subSystems.length == 0) {
            return null;
        }

        for (ISubSystem subSystem : subSystems) {
            Class<?>[] interfaces = subSystem.getClass().getInterfaces();
            for (Class<?> interfaze : interfaces) {
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

        if (element instanceof ISystemFilterReference) {
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

        if (element instanceof ISystemFilterReference) {
            ISystemFilterReference filterReference = (ISystemFilterReference)element;
            ISubSystem subSystem = SubSystemHelpers.getParentSubSystem(filterReference.getParentSystemFilterReferencePool());
            return subSystem;
        }

        return null;
    }

    public static void refreshUICreated(boolean isSlowConnection, Object subSystem, Object resource, Object... parents) {

        if (resource != null) {
            ISystemRegistry sr = RSECorePlugin.getTheSystemRegistry();
            for (Object parent : parents) {
                if (parent instanceof ISystemFilterReference) {
                    ISystemFilterReference systemFilterReference = (ISystemFilterReference)parent;
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
            ISystemRegistry sr = RSECorePlugin.getTheSystemRegistry();
            for (Object parent : parents) {
                sr.fireEvent(new SystemResourceChangeEvent(resource, ISystemResourceChangeEvents.EVENT_REFRESH, parent));
            }
        }
    }

    public static void refreshUIDeleted(boolean isSlowConnection, Object subSystem, Object resource, Object... parents) {

        if (resource != null) {
            ISystemRegistry sr = RSECorePlugin.getTheSystemRegistry();
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

        IBMiConnection connection = IBMiConnection.getConnection(connectionName);
        if (connection == null) {
            return false;
        }

        IQSYSMember qsysMember = connection.getMember(libraryName, fileName, memberName, null);

        QSYSEditableRemoteSourceFileMember editableMember = new QSYSEditableRemoteSourceFileMember(qsysMember);

        editableMember.open(Display.getCurrent().getActiveShell(), false);

        return true;
    }

    /**
     * Returns a system for a given connection name.
     * 
     * @param connectionName - Name of the connection a system is returned for
     * @return AS400
     * @throws Exception
     */
    public static AS400 getSystem(String connectionName) throws Exception {

        IBMiConnection connection = IBMiConnection.getConnection(connectionName);
        if (connection == null) {
            return null;
        }

        return connection.getAS400ToolboxObject();
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

        IBMiConnection[] connections = IBMiConnection.getConnections();
        for (IBMiConnection connection : connections) {
            if (connection.getHostName().equalsIgnoreCase(hostName)) {
                return connection.getAS400ToolboxObject();
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

        IBMiConnection[] connections = getConnections();
        for (IBMiConnection connection : connections) {
            connectionNames.add(connection.getConnectionName());
        }

        return connectionNames.toArray(new String[connectionNames.size()]);
    }

    /**
     * Returns an array of hosts sorted by connection name.
     * 
     * @return sorted list of hosts
     */
    public static IHost[] getHosts() {

        IBMiConnection[] connections = getConnections();

        List<IHost> hosts = new LinkedList<IHost>();
        for (IBMiConnection connection : connections) {
            hosts.add(connection.getHost());
        }

        return hosts.toArray(new IHost[hosts.size()]);
    }

    /**
     * Returns an array of IBMiConnections sorted by connection name.
     * 
     * @return sorted list of IBMiConnections
     */
    private static IBMiConnection[] getConnections() {

        IBMiConnection[] connections = IBMiConnection.getConnections();

        Arrays.sort(connections, new Comparator<IBMiConnection>() {

            public int compare(IBMiConnection o1, IBMiConnection o2) {

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
