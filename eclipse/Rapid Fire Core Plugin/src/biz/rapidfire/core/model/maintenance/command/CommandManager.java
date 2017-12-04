/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.command;

import java.sql.CallableStatement;
import java.sql.Types;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.maintenance.AbstractManager;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.Success;
import biz.rapidfire.core.model.maintenance.command.shared.CommandAction;
import biz.rapidfire.core.model.maintenance.command.shared.CommandKey;
import biz.rapidfire.core.model.maintenance.command.shared.CommandType;
import biz.rapidfire.core.model.maintenance.file.shared.FileKey;
import biz.rapidfire.core.model.maintenance.job.shared.JobKey;

public class CommandManager extends AbstractManager<CommandKey, CommandValues, CommandAction> {

    private static final String ERROR_001 = "001"; //$NON-NLS-1$
    private static final String ERROR_002 = "002"; //$NON-NLS-1$
    private static final String ERROR_003 = "003"; //$NON-NLS-1$
    private static final String ERROR_004 = "004"; //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$
    private IJDBCConnection dao;
    private FileKey fileKey;

    public CommandManager(IJDBCConnection dao) {
        this.dao = dao;
    }

    @Override
    public void openFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCMD_openFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result initialize(MaintenanceMode mode, CommandKey key) throws Exception {

        JobKey jobKey = new JobKey(key.getJobName());
        fileKey = new FileKey(jobKey, key.getPosition());

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCMD_initialize\"(?, ?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(ICommandInitialize.MODE, mode.label());
        statement.setString(ICommandInitialize.JOB, key.getJobName());
        statement.setInt(ICommandInitialize.POSITION, key.getPosition());
        statement.setString(ICommandInitialize.TYPE, key.getCommandType());
        statement.setInt(ICommandInitialize.SEQUENCE, key.getSequence());
        statement.setString(ICommandInitialize.SUCCESS, Success.NO.label());
        statement.setString(ICommandInitialize.ERROR_CODE, EMPTY_STRING);

        statement.registerOutParameter(ICommandInitialize.SUCCESS, Types.CHAR);
        statement.registerOutParameter(ICommandInitialize.ERROR_CODE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, ICommandInitialize.SUCCESS);
        String errorCode = getStringTrim(statement, ICommandInitialize.ERROR_CODE);

        String message;
        if (Success.YES.label().equals(success)) {
            message = null;
        } else {
            message = Messages.bindParameters(
                Messages.Could_not_initialize_command_manager_for_command_type_D_and_sequence_E_of_file_C_of_job_A_in_library_B, key.getJobName(),
                dao.getLibraryName(), key.getPosition(), key.getCommandType(), key.getSequence(), getErrorMessage(errorCode));
        }

        Result result = new Result(success, message);

        return result;
    }

    /**
     * Translates the API error code to message text.
     * 
     * @param errorCode - Error code that was returned by the API.
     * @return message text
     */
    private String getErrorMessage(String errorCode) {

        // TODO: use reflection
        if (ERROR_001.equals(errorCode)) {
            return Messages.CommandManager_001;
        } else if (ERROR_002.equals(errorCode)) {
            return Messages.CommandManager_002;
        } else if (ERROR_003.equals(errorCode)) {
            return Messages.CommandManager_003;
        } else if (ERROR_004.equals(errorCode)) {
            return Messages.CommandManager_004;
        }

        return Messages.bindParameters(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    @Override
    public CommandValues getValues() throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCMD_getValues\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(ICommandGetValues.TYPE, EMPTY_STRING);
        statement.setInt(ICommandGetValues.SEQUENCE, 0);
        statement.setString(ICommandGetValues.COMMAND, EMPTY_STRING);

        statement.registerOutParameter(ICommandGetValues.TYPE, Types.CHAR);
        statement.registerOutParameter(ICommandGetValues.SEQUENCE, Types.INTEGER);
        statement.registerOutParameter(ICommandGetValues.COMMAND, Types.CHAR);

        statement.execute();

        CommandValues values = new CommandValues();
        values.setKey(new CommandKey(fileKey, CommandType.find(getStringTrim(statement, ICommandGetValues.TYPE)), getInt(statement,
            ICommandGetValues.SEQUENCE)));
        values.setCommand(getStringTrim(statement, ICommandGetValues.COMMAND));

        return values;
    }

    @Override
    public void setValues(CommandValues values) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCMD_setValues\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(ICommandSetValues.TYPE, values.getKey().getCommandType());
        statement.setInt(ICommandSetValues.SEQUENCE, values.getKey().getSequence());
        statement.setString(ICommandSetValues.COMMAND, values.getCommand());

        statement.execute();
    }

    @Override
    public Result check() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCMD_check\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(ICommandCheck.SUCCESS, Success.NO.label());
        statement.setString(ICommandCheck.FIELD_NAME, EMPTY_STRING);
        statement.setString(ICommandCheck.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(ICommandCheck.SUCCESS, Types.CHAR);
        statement.registerOutParameter(ICommandCheck.FIELD_NAME, Types.CHAR);
        statement.registerOutParameter(ICommandCheck.MESSAGE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, ICommandCheck.SUCCESS);
        String fieldName = getStringTrim(statement, ICommandCheck.FIELD_NAME);
        String message = getStringTrim(statement, ICommandCheck.MESSAGE);

        return new Result(fieldName, message, success);
    }

    @Override
    public void book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCMD_book\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCMD_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(CommandKey key, CommandAction commandAction) throws Exception {
        // TODO: check action!
        Result result = new Result(Success.YES.label(), null);
        return result;
    }
}
