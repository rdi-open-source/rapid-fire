/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.notification;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.maintenance.notification.shared.NotificationAction;
import biz.rapidfire.core.maintenance.notification.shared.NotificationKey;
import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class NotificationManager extends AbstractManager<IRapidFireNotificationResource, NotificationKey, NotificationValues, NotificationAction> {

    private static final String ERROR_001 = "001"; //$NON-NLS-1$
    private static final String ERROR_002 = "002"; //$NON-NLS-1$
    private static final String ERROR_003 = "003"; //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$
    private IJDBCConnection dao;
    private JobKey jobKey;

    public NotificationManager(IJDBCConnection dao) {
        this.dao = dao;
    }

    @Override
    public void openFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTSTBN_openFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result initialize(MaintenanceMode mode, NotificationKey key) throws Exception {

        jobKey = new JobKey(key.getJobName());

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTSTBN_initialize\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(INotificationInitialize.MODE, mode.label());
        statement.setString(INotificationInitialize.JOB, key.getJobName());
        statement.setInt(INotificationInitialize.POSITION, key.getPosition());
        statement.setString(INotificationInitialize.SUCCESS, Success.NO.label());
        statement.setString(INotificationInitialize.ERROR_CODE, EMPTY_STRING);

        statement.registerOutParameter(INotificationInitialize.SUCCESS, Types.CHAR);
        statement.registerOutParameter(INotificationInitialize.ERROR_CODE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, INotificationInitialize.SUCCESS);
        String errorCode = getStringTrim(statement, INotificationInitialize.ERROR_CODE);

        String message;
        if (Success.YES.label().equals(success)) {
            message = null;
        } else {
            message = Messages.bindParameters(
                Messages.Could_not_initialize_notification_manager_for_notification_at_position_C_of_job_A_in_library_B, key.getJobName(),
                dao.getLibraryName(), key.getPosition(), getErrorMessage(errorCode));
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
            return Messages.NotificationManager_001;
        } else if (ERROR_002.equals(errorCode)) {
            return Messages.NotificationManager_002;
        } else if (ERROR_003.equals(errorCode)) {
            return Messages.NotificationManager_003;
        }

        return Messages.bindParameters(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    @Override
    public NotificationValues getValues() throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTSTBN_getValues\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setInt(INotificationGetValues.POSITION, 0);
        statement.setString(INotificationGetValues.TYPE, EMPTY_STRING);
        statement.setString(INotificationGetValues.USER, EMPTY_STRING);
        statement.setString(INotificationGetValues.MESSAGE_QUEUE_LIBRARY_NAME, EMPTY_STRING);
        statement.setString(INotificationGetValues.MESSAGE_QUEUE_NAME, EMPTY_STRING);

        statement.registerOutParameter(INotificationGetValues.POSITION, Types.INTEGER);
        statement.registerOutParameter(INotificationGetValues.TYPE, Types.CHAR);
        statement.registerOutParameter(INotificationGetValues.USER, Types.CHAR);
        statement.registerOutParameter(INotificationGetValues.MESSAGE_QUEUE_LIBRARY_NAME, Types.CHAR);
        statement.registerOutParameter(INotificationGetValues.MESSAGE_QUEUE_NAME, Types.CHAR);

        statement.execute();

        NotificationValues values = new NotificationValues();
        values.setKey(new NotificationKey(jobKey, getInt(statement, INotificationGetValues.POSITION)));
        values.setNotificationType(getStringTrim(statement, INotificationGetValues.TYPE));
        values.setUser(getStringTrim(statement, INotificationGetValues.USER));
        values.setMessageQueueLibraryName(getStringTrim(statement, INotificationGetValues.MESSAGE_QUEUE_LIBRARY_NAME));
        values.setMessageQueueName(getStringTrim(statement, INotificationGetValues.MESSAGE_QUEUE_NAME));

        return values;
    }

    @Override
    public void setValues(NotificationValues values) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTSTBN_setValues\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setInt(INotificationSetValues.POSITION, values.getKey().getPosition());
        statement.setString(INotificationSetValues.TYPE, values.getNotificationType());
        statement.setString(INotificationSetValues.USER, values.getUser());
        statement.setString(INotificationSetValues.MESSAGE_QUEUE_LIBRARY_NAME, values.getMessageQueueLibraryName());
        statement.setString(INotificationSetValues.MESSAGE_QUEUE_NAME, values.getMessageQueueName());

        statement.execute();
    }

    @Override
    public Result check() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTSTBN_check\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(INotificationCheck.SUCCESS, Success.NO.label());
        statement.setString(INotificationCheck.FIELD_NAME, EMPTY_STRING);
        statement.setString(INotificationCheck.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(INotificationCheck.SUCCESS, Types.CHAR);
        statement.registerOutParameter(INotificationCheck.FIELD_NAME, Types.CHAR);
        statement.registerOutParameter(INotificationCheck.MESSAGE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, INotificationCheck.SUCCESS);
        String fieldName = getStringTrim(statement, INotificationCheck.FIELD_NAME);
        String message = getStringTrim(statement, INotificationCheck.MESSAGE);

        return new Result(fieldName, message, success);
    }

    @Override
    public Result book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTSTBN_book\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();

        return null;
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTSTBN_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(NotificationKey key, NotificationAction areaAction) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTSTBN_checkAction\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(INotificationCheckAction.ACTION, areaAction.label());
        statement.setString(INotificationCheckAction.JOB, key.getJobName());
        statement.setInt(INotificationCheckAction.POSITION, key.getPosition());
        statement.setString(INotificationCheckAction.SUCCESS, Success.NO.label());
        statement.setString(INotificationCheckAction.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(INotificationCheckAction.SUCCESS, Types.CHAR);
        statement.registerOutParameter(INotificationCheckAction.MESSAGE, Types.CHAR);

        statement.execute();

        String success = statement.getString(INotificationCheckAction.SUCCESS);
        String message = statement.getString(INotificationCheckAction.MESSAGE);

        return new Result(null, message, success);
    }

    protected NotificationAction[] getValidActions(NotificationKey notificationKey) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTSTBN_getValidActions\"(?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(INotificationGetValidActions.JOB, notificationKey.getJobName());
        statement.setInt(INotificationGetValidActions.POSITION, notificationKey.getPosition());
        statement.setInt(INotificationGetValidActions.NUMBER_ACTIONS, 0);
        statement.setString(INotificationGetValidActions.ACTIONS, EMPTY_STRING);

        statement.registerOutParameter(INotificationGetValidActions.NUMBER_ACTIONS, Types.DECIMAL);
        statement.registerOutParameter(INotificationGetValidActions.ACTIONS, Types.CHAR);

        statement.execute();

        int numberActions = statement.getBigDecimal(INotificationGetValidActions.NUMBER_ACTIONS).intValue();
        String[] actions = splitActions(statement.getString(INotificationGetValidActions.ACTIONS), numberActions);

        Set<NotificationAction> notificationActions = new HashSet<NotificationAction>();
        for (String action : actions) {
            notificationActions.add(NotificationAction.find(action.trim()));
        }

        Result result = checkAction(NotificationKey.createNew(new JobKey(notificationKey.getJobName())), NotificationAction.CREATE);
        if (result.isSuccessfull()) {
            notificationActions.add(NotificationAction.CREATE);
        }

        return notificationActions.toArray(new NotificationAction[notificationActions.size()]);
    }

    @Override
    public boolean isValidAction(IRapidFireNotificationResource notification, NotificationAction action) throws Exception {

        if (isActionCacheEnabled()) {
            return isValidCachedAction(notification, action);
        } else {
            return isValidUncachedAction(notification, action);
        }
    }

    private boolean isValidUncachedAction(IRapidFireNotificationResource notification, NotificationAction action) throws Exception {

        Result result = checkAction(notification.getKey(), action);

        return result.isSuccessfull();
    }

    private boolean isValidCachedAction(IRapidFireNotificationResource notification, NotificationAction action) throws Exception {

        KeyNotificationActionCache notificationActionsKey = new KeyNotificationActionCache(notification);

        Set<NotificationAction> actionsSet = NotificationActionCache.getInstance().getActions(notificationActionsKey);
        if (actionsSet == null) {
            NotificationAction[] fileActions = getValidActions(notification.getKey());
            NotificationActionCache.getInstance().putActions(notificationActionsKey, fileActions);
            actionsSet = NotificationActionCache.getInstance().getActions(notificationActionsKey);
        }

        return actionsSet.contains(action);
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }
}
