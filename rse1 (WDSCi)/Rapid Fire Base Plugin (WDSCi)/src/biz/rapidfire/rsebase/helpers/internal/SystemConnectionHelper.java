/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.helpers.internal;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.systems.model.SystemConnection;

public class SystemConnectionHelper {

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
