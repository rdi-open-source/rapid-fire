/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.helpers;

import org.eclipse.swt.widgets.Display;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.rse.ui.resources.QSYSEditableRemoteSourceFileMember;
import com.ibm.etools.iseries.services.qsys.api.IQSYSMember;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;

public class AbstractRapidFireHelper {

    /**
     * Returns a system for a given connection name.
     * 
     * @parm connectionName - Name of the connection a system is returned for
     * @return AS400
     */
    public static AS400 getSystem(String connectionName) {
        return getSystem(null, connectionName);
    }

    /**
     * Returns a system for a given profile and connection name.
     * 
     * @parm profile - Profile that is searched for the connection
     * @parm connectionName - Name of the connection a system is returned for
     * @return AS400
     */
    public static AS400 getSystem(String profile, String connectionName) {

        IBMiConnection connection = getConnection(profile, connectionName);
        if (connection == null) {
            return null;
        }

        try {
            return connection.getAS400ToolboxObject();
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Internal method that returns a connection for a given profile and
     * connection name. The profile might be null.
     * 
     * @parm profile - Profile that is searched for the connection
     * @parm connectionName - Name of the connection a system is returned for
     * @return IBMiConnection
     */
    private static IBMiConnection getConnection(String profile, String connectionName) {

        if (profile == null) {
            return IBMiConnection.getConnection(connectionName);
        }

        return IBMiConnection.getConnection(profile, connectionName);
    }

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
}
