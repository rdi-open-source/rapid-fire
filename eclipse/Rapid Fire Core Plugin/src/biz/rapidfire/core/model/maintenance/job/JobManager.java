/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.job;

import java.sql.CallableStatement;
import java.sql.Types;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.model.dao.IBaseDAO;
import biz.rapidfire.core.model.maintenance.AbstractManager;
import biz.rapidfire.core.model.maintenance.CheckStatus;

public class JobManager extends AbstractManager<JobKey, JobValues> {

    public static final String MODE_CREATE = "*CREATE";
    public static final String MODE_COPY = "*COPY";
    public static final String MODE_CHANGE = "*CHANGE";
    public static final String MODE_DELETE = "*DELETE";
    public static final String MODE_DISPLAY = "*DISPLAY";

    private IBaseDAO dao;

    public JobManager(IBaseDAO dao) {
        this.dao = dao;
    }

    @Override
    public void openFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IBaseDAO.LIBRARY + "\"MNTJOB_openFiles\"()}"));
        statement.execute();
    }

    @Override
    public CheckStatus initialize(String mode, JobKey key) throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IBaseDAO.LIBRARY + "\"MNTJOB_initialize\"(?, ?, ?)}"));

        statement.setString(1, mode); // Mode
        statement.setString(2, key.getJobName()); // Job name
        statement.setString(3, ""); // Success

        statement.registerOutParameter(3, Types.CHAR);

        statement.execute();

        String success = statement.getString(3);

        CheckStatus status = new CheckStatus(null, null, success);
        if (status.isError()) {
            status.setMessage(Messages.bind("Could not initialize 'Job Manager' for mode ''{0}''.", mode));
        }

        return status;
    }

    @Override
    public JobValues getValues() throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IBaseDAO.LIBRARY + "\"MNTJOB_getValues\"(?, ?, ?, ?, ?)}"));

        statement.setString(1, "");
        statement.setString(2, "");
        statement.setString(3, "");
        statement.setString(4, "");
        statement.setString(5, "");

        statement.registerOutParameter(1, Types.CHAR);
        statement.registerOutParameter(2, Types.CHAR);
        statement.registerOutParameter(3, Types.CHAR);
        statement.registerOutParameter(4, Types.CHAR);
        statement.registerOutParameter(5, Types.CHAR);

        statement.execute();

        JobValues values = new JobValues();
        values.getKey().setJobName(statement.getString(1));
        values.setDescription(statement.getString(2));
        values.setCreateEnvironment(statement.getString(3));
        values.setJobQueueName(statement.getString(4));
        values.setJobQueueLibraryName(statement.getString(5));

        return values;
    }

    @Override
    public void setValues(JobValues values) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IBaseDAO.LIBRARY + "\"MNTJOB_setValues\"(?, ?, ?, ?, ?)}"));

        statement.setString(1, values.getKey().getJobName());
        statement.setString(2, values.getDescription());
        statement.setString(3, values.getCreateEnvironment());
        statement.setString(4, values.getJobQueueName());
        statement.setString(5, values.getJobQueueLibraryName());

        statement.execute();
    }

    @Override
    public CheckStatus check() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IBaseDAO.LIBRARY + "\"MNTJOB_check\"(?, ?, ?)}"));

        statement.setString(1, ""); // Field name
        statement.setString(2, ""); // Message
        statement.setString(3, ""); // success

        statement.registerOutParameter(1, Types.CHAR);
        statement.registerOutParameter(2, Types.CHAR);
        statement.registerOutParameter(3, Types.CHAR);

        statement.execute();

        String fieldName = statement.getString(1); // Field name
        String message = statement.getString(2); // Message
        String success = statement.getString(3); // Success

        return new CheckStatus(fieldName, message, success);
    }

    @Override
    public void book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IBaseDAO.LIBRARY + "\"MNTJOB_book\"()}"));
        statement.execute();
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IBaseDAO.LIBRARY + "\"MNTJOB_closeFiles\"()" + "}"));
        statement.execute();
    }

}
