package biz.rapidfire.rse.model.dao;

import java.sql.Connection;

import biz.rapidfire.core.model.dao.AbstractBaseDAO;
import biz.rapidfire.core.model.dao.IBaseDAO;
import biz.rapidfire.rse.Messages;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;

public class BaseDAO extends AbstractBaseDAO implements IBaseDAO {

    private static final String BOOLEAN_Y = "Y"; //$NON-NLS-1$
    private static final String BOOLEAN_N = "N"; //$NON-NLS-1$
    private static final String BOOLEAN_YES = "*YES"; //$NON-NLS-1$
    private static final String BOOLEAN_NO = "*NO"; //$NON-NLS-1$

    // protected static final String properties = "thread used=false; extendeddynamic=true; package criteria=select; package cache=true;"; //$NON-NLS-1$
    protected static final String properties = "translate hex=binary; prompt=false; extended dynamic=true; package cache=true"; //$NON-NLS-1$

    private IBMiConnection ibmiConnection;
    private Connection connection;

    public BaseDAO(String connectionName) throws Exception {

        if (connectionName == null) {
            throw new Exception(Messages.bind(Messages.RseBaseDAO_Invalid_or_missing_connection_name_A, connectionName));
        }

        // if (library == null) {
        // throw new
        // Exception(Messages.bind(Messages.RseBaseDAO_Invalid_or_missing_library_name_A,
        // connectionName));
        // }

        ibmiConnection = IBMiConnection.getConnection(connectionName);
        if (ibmiConnection == null) {
            throw new Exception(Messages.bind(Messages.RseBaseDAO_Connection_A_not_found, connectionName));
        }
        if (!ibmiConnection.isConnected()) {
            if (!ibmiConnection.connect()) {
                throw new Exception(Messages.bind(Messages.RseBaseDAO_Failed_to_connect_to_A, connectionName));
            }
        }

        connection = ibmiConnection.getJDBCConnection(properties, true);
        connection.setAutoCommit(false);
    }

    public String getConnectionName() {
        return ibmiConnection.getConnectionName();
    }

    public AS400 getSystem() throws Exception {
        return ibmiConnection.getAS400ToolboxObject();
    }
}
