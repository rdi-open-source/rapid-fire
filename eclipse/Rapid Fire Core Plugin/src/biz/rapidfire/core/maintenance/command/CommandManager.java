/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.command;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.command.shared.CommandAction;
import biz.rapidfire.core.maintenance.command.shared.CommandKey;
import biz.rapidfire.core.maintenance.command.shared.CommandType;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class CommandManager extends AbstractManager<IRapidFireCommandResource, CommandKey, CommandValues, CommandAction> {

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
    public Result book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCMD_book\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();

        return null;
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCMD_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(CommandKey key, CommandAction commandAction) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCMD_checkAction\"(?, ?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(ICommandCheckAction.ACTION, commandAction.label());
        statement.setString(ICommandCheckAction.JOB, key.getJobName());
        statement.setInt(ICommandCheckAction.POSITION, key.getPosition());
        statement.setString(ICommandCheckAction.COMMAND_TYPE, key.getCommandType());
        statement.setInt(ICommandCheckAction.SEQUENCE, key.getSequence());
        statement.setString(ICommandCheckAction.SUCCESS, Success.NO.label());
        statement.setString(ICommandCheckAction.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(ICommandCheckAction.SUCCESS, Types.CHAR);
        statement.registerOutParameter(ICommandCheckAction.MESSAGE, Types.CHAR);

        statement.execute();

        String success = statement.getString(ICommandCheckAction.SUCCESS);
        String message = statement.getString(ICommandCheckAction.MESSAGE);

        return new Result(null, message, success);
    }

    protected CommandAction[] getValidActions(CommandKey commandKey) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCMD_getValidActions\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(ICommandGetValidActions.JOB, commandKey.getJobName());
        statement.setInt(ICommandGetValidActions.POSITION, commandKey.getPosition());
        statement.setString(ICommandGetValidActions.COMMAND_TYPE, commandKey.getCommandType());
        statement.setInt(ICommandGetValidActions.SEQUENCE, commandKey.getSequence());
        statement.setInt(ICommandGetValidActions.NUMBER_ACTIONS, 0);
        statement.setString(ICommandGetValidActions.ACTIONS, EMPTY_STRING);

        statement.registerOutParameter(ICommandGetValidActions.NUMBER_ACTIONS, Types.DECIMAL);
        statement.registerOutParameter(ICommandGetValidActions.ACTIONS, Types.CHAR);

        statement.execute();

        int numberActions = statement.getBigDecimal(ICommandGetValidActions.NUMBER_ACTIONS).intValue();
        String[] actions = splitActions(statement.getString(ICommandGetValidActions.ACTIONS), numberActions);

        Set<CommandAction> commandActions = new HashSet<CommandAction>();
        for (String action : actions) {
            commandActions.add(CommandAction.find(action.trim()));
        }

        Result result = checkAction(CommandKey.createNew(new FileKey(new JobKey(commandKey.getJobName()), commandKey.getPosition())),
            CommandAction.CREATE);
        if (result.isSuccessfull()) {
            commandActions.add(CommandAction.CREATE);
        }

        return commandActions.toArray(new CommandAction[commandActions.size()]);
    }

    @Override
    public boolean isValidAction(IRapidFireCommandResource command, CommandAction action) throws Exception {

        if (isActionCacheEnabled()) {
            return isValidCachedAction(command, action);
        } else {
            return isValidUncachedAction(command, action);
        }
    }

    private boolean isValidUncachedAction(IRapidFireCommandResource command, CommandAction action) throws Exception {

        Result result = checkAction(command.getKey(), action);

        return result.isSuccessfull();
    }

    private boolean isValidCachedAction(IRapidFireCommandResource command, CommandAction action) throws Exception {

        KeyCommandActionCache commandActionsKey = new KeyCommandActionCache(command);

        Set<CommandAction> actionsSet = CommandActionCache.getInstance().getActions(commandActionsKey);
        if (actionsSet == null) {
            CommandAction[] commandActions = getValidActions(command.getKey());
            CommandActionCache.getInstance().putActions(commandActionsKey, commandActions);
            actionsSet = CommandActionCache.getInstance().getActions(commandActionsKey);
        }

        return actionsSet.contains(action);
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }
}
