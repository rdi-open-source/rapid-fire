package biz.rapidfire.rse.model.dao;

import biz.rapidfire.core.model.dao.AbstractBaseDAO;
import biz.rapidfire.core.model.dao.IBaseDAO;
import biz.rapidfire.rse.Messages;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;

public class BaseDAO extends AbstractBaseDAO implements IBaseDAO {

    private IBMiConnection ibmiConnection;

    public BaseDAO(String connectionName) throws Exception {

        if (connectionName == null) {
            throw new Exception(Messages.bind(Messages.RseBaseDAO_Invalid_or_missing_connection_name_A, connectionName));
        }

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
