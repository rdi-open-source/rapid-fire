package biz.rapidfire.rse.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.model.dao.IBaseDAO;
import biz.rapidfire.rse.Messages;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Date;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;

public class BaseDAO implements IBaseDAO {

    private static final String BOOLEAN_Y = "Y"; //$NON-NLS-1$
    private static final String BOOLEAN_N = "N"; //$NON-NLS-1$
    private static final String BOOLEAN_YES = "*YES"; //$NON-NLS-1$
    private static final String BOOLEAN_NO = "*NO"; //$NON-NLS-1$

    // protected static final String properties = "thread used=false; extendeddynamic=true; package criteria=select; package cache=true;"; //$NON-NLS-1$
    protected static final String properties = "translate hex=binary; prompt=false; extended dynamic=true; package cache=true"; //$NON-NLS-1$

    private IBMiConnection ibmiConnection;
    private Connection connection;

    private String dateFormat;
    private String dateSeparator;
    private String timeSeparator;

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

        dateFormat = ibmiConnection.getQSYSJobSubSystem().getServerJob(null).getInternationalProperties().getDateFormat();
        if (dateFormat.startsWith("*")) { //$NON-NLS-1$
            dateFormat = dateFormat.substring(1);
        }

        dateSeparator = ibmiConnection.getQSYSJobSubSystem().getServerJob(null).getInternationalProperties().getDateSeparator();
        timeSeparator = ibmiConnection.getQSYSJobSubSystem().getServerJob(null).getInternationalProperties().getTimeSeparator();

        connection = ibmiConnection.getJDBCConnection(properties, true);
        connection.setAutoCommit(false);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    public void destroy() {

        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    connection = null;
                }
            } catch (Throwable e) {
                RapidFireCorePlugin.logError("*** Could not destroy connection ***", e); //$NON-NLS-1$
            }
        }
    }

    public void destroy(Connection connection) throws Exception {
        if (connection != null && !connection.isClosed()) connection.close();
    }

    public void destroy(ResultSet resultSet) throws Exception {
        if (resultSet != null) resultSet.close();
    }

    public void destroy(PreparedStatement preparedStatement) throws Exception {
        if (preparedStatement != null) preparedStatement.close();
    }

    public void rollback(Connection connection) throws Exception {
        if (connection != null && !connection.isClosed()) {
            if (connection.getAutoCommit() == false) connection.rollback();
        }
    }

    public void commit(Connection connection) throws Exception {
        if (connection != null && !connection.isClosed()) {
            if (connection.getAutoCommit() == false) connection.commit();
        }
    }

    public Character getTimeSeparator() {
        return timeSeparator.charAt(0);
    }

    public Character getDateSeparator() {
        return dateSeparator.charAt(0);
    }

    public int getDateFormat() {
        return AS400Date.toFormat(dateFormat);
    }

    public Connection getConnection() {
        return connection;
    }

    public String getConnectionName() {
        return ibmiConnection.getConnectionName();
    }

    public AS400 getSystem() throws Exception {
        return ibmiConnection.getAS400ToolboxObject();
    }

    public boolean convertYesNo(String yesNoValue) {

        boolean booleanValue;
        if (BOOLEAN_YES.equals(yesNoValue) || BOOLEAN_Y.equals(yesNoValue)) {
            booleanValue = true;
        } else {
            booleanValue = false;
        }

        return booleanValue;
    }
}
