package biz.rapidfire.rse.model.dao;

import biz.rapidfire.core.model.dao.AbstractBaseDAO;
import biz.rapidfire.core.model.dao.IBaseDAO;
import biz.rapidfire.rse.Messages;

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.connectorservice.ToolboxConnectorService;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;

public class BaseDAO extends AbstractBaseDAO implements IBaseDAO {

    private IBMiConnection ibmiConnection;
    private AS400 system;

    public BaseDAO(String connectionName) throws Exception {

        if (connectionName == null) {
            throw new Exception(Messages.bind(Messages.RseBaseDAO_Invalid_or_missing_connection_name_A, connectionName));
        }

        this.ibmiConnection = IBMiConnection.getConnection(connectionName);
        if (ibmiConnection == null) {
            throw new Exception(Messages.bind(Messages.RseBaseDAO_Connection_A_not_found, connectionName));
        }

        this.system = this.ibmiConnection.getAS400ToolboxObject();
    }

    public AS400 getSystem() throws Exception {
        return system;
    }

    public String getHostName() {
        return ibmiConnection.getHostName();
    }

    public String getConnectionName() {
        return ibmiConnection.getConnectionName();
    }

    /*
     * Does not work at the moment due to a bug in
     * IBMiConnection.getJdbcConnection().
     */
    // public Connection getJdbcConnection(String defaultLibrary) throws
    // Exception {
    //
    // String properties;
    // if (defaultLibrary == null) {
    // properties = "";
    // } else {
    // properties = ";libraries=" + defaultLibrary + ",*LIBL;";
    // }
    //
    // return ibmiConnection.getJDBCConnection(properties, false);
    // }

    protected String getUser() {

        ToolboxConnectorService service = (ToolboxConnectorService)ibmiConnection.getConnectorService();
        return service.getUserId();
    }

    protected String getPassword() {

        ToolboxConnectorService service = (ToolboxConnectorService)ibmiConnection.getConnectorService();
        return service.getPassword();
    }
}
