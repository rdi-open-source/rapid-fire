/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.core.model.maintenance.notification.shared.NotificationType;

public abstract class AbstractNotificationsDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String POSITION = "POSITION"; //$NON-NLS-1$
    public static final String TYPE = "TYPE"; //$NON-NLS-1$
    public static final String USER = "\"USER\""; //$NON-NLS-1$
    public static final String MESSAGE_QUEUE_LIBRARY = "MESSAGE_QUEUE_LIBRARY"; //$NON-NLS-1$
    public static final String MESSAGE_QUEUE = "MESSAGE_QUEUE"; //$NON-NLS-1$

    private IJDBCConnection dao;

    public AbstractNotificationsDAO(IJDBCConnection dao) {

        this.dao = dao;
    }

    public List<IRapidFireNotificationResource> load(String job, Shell shell) throws Exception {

        final List<IRapidFireNotificationResource> notifications = new ArrayList<IRapidFireNotificationResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return notifications;
        }

        try {

            String sqlStatement = getSqlStatement();
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    notifications.add(produceNotification(resultSet));
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return notifications;
    }

    private IRapidFireNotificationResource produceNotification(ResultSet resultSet) throws SQLException {

        String job = resultSet.getString(JOB).trim();
        int position = resultSet.getInt(POSITION);

        IRapidFireNotificationResource notificationResource = createNotificationInstance(dao.getLibraryName(), job, position);

        String type = resultSet.getString(TYPE).trim();
        String user = resultSet.getString(USER).trim();
        String messageQueueName = resultSet.getString(MESSAGE_QUEUE).trim();
        String messageQueueLibrary = resultSet.getString(MESSAGE_QUEUE_LIBRARY).trim();

        NotificationType notificationType = NotificationType.find(type);

        notificationResource.setNotificationType(notificationType);
        notificationResource.setUser(user);
        notificationResource.setMessageQueueName(messageQueueName);
        notificationResource.setMessageQueueLibrary(messageQueueLibrary);

        return notificationResource;
    }

    protected abstract IRapidFireNotificationResource createNotificationInstance(String libraryName, String job, int position);

    private String getSqlStatement() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "POSITION, " +
            "TYPE, " +
            "\"USER\", " +
            "MESSAGE_QUEUE_LIBRARY, " +
            "MESSAGE_QUEUE " +
        "FROM " +
            IJDBCConnection.LIBRARY +
            "SUBJECTS_TO_BE_NOTIFIED " +
        "WHERE " +
            "JOB = ?";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
}
