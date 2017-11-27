/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.conversion;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.maintenance.AbstractManager;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.Success;
import biz.rapidfire.core.model.maintenance.file.FileKey;
import biz.rapidfire.core.model.maintenance.job.JobKey;

public class ConversionManager extends AbstractManager<ConversionKey, ConversionValues> {

    private static final String ERROR_001 = "001"; //$NON-NLS-1$
    private static final String ERROR_002 = "002"; //$NON-NLS-1$
    private static final String ERROR_003 = "003"; //$NON-NLS-1$
    private static final String ERROR_004 = "004"; //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$
    private IJDBCConnection dao;
    private FileKey fileKey;

    public ConversionManager(IJDBCConnection dao) {
        this.dao = dao;
    }

    @Override
    public void openFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_openFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result initialize(String mode, ConversionKey key) throws Exception {

        JobKey jobKey = new JobKey(key.getJobName());
        fileKey = new FileKey(jobKey, key.getPosition());

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_initialize\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IConversionInitialize.MODE, mode);
        statement.setString(IConversionInitialize.JOB, key.getJobName());
        statement.setInt(IConversionInitialize.POSITION, key.getPosition());
        statement.setString(IConversionInitialize.FIELD_TO_CONVERT, key.getFieldToConvert());
        statement.setString(IConversionInitialize.SUCCESS, Success.NO.label());
        statement.setString(IConversionInitialize.ERROR_CODE, EMPTY_STRING);

        statement.registerOutParameter(IConversionInitialize.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IConversionInitialize.ERROR_CODE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, IConversionInitialize.SUCCESS);
        String errorCode = getStringTrim(statement, IConversionInitialize.ERROR_CODE);

        String message;
        if (Success.YES.label().equals(success)) {
            message = null;
        } else {
            message = Messages.bindParameters(Messages.Could_not_initialize_conversion_manager_for_field_D_of_file_C_of_job_A_in_library_B,
                key.getJobName(), dao.getLibraryName(), key.getPosition(), key.getFieldToConvert(), getErrorMessage(errorCode));
        }

        Result status = new Result(success, message);

        return status;
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
            return Messages.ConversionManager_001;
        } else if (ERROR_002.equals(errorCode)) {
            return Messages.ConversionManager_002;
        } else if (ERROR_003.equals(errorCode)) {
            return Messages.ConversionManager_003;
        } else if (ERROR_004.equals(errorCode)) {
            return Messages.ConversionManager_004;
        }

        return Messages.bindParameters(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    @Override
    public ConversionValues getValues() throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_getValues\"(?, ?, ?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IConversionGetValues.FIELD_TO_CONVERT, EMPTY_STRING);
        statement.setString(IConversionGetValues.NEW_FIELD_NAME, EMPTY_STRING);
        statement.setString(IConversionGetValues.STATEMENT_1, EMPTY_STRING);
        statement.setString(IConversionGetValues.STATEMENT_2, EMPTY_STRING);
        statement.setString(IConversionGetValues.STATEMENT_3, EMPTY_STRING);
        statement.setString(IConversionGetValues.STATEMENT_4, EMPTY_STRING);
        statement.setString(IConversionGetValues.STATEMENT_5, EMPTY_STRING);
        statement.setString(IConversionGetValues.STATEMENT_6, EMPTY_STRING);

        statement.registerOutParameter(IConversionGetValues.FIELD_TO_CONVERT, Types.CHAR);
        statement.registerOutParameter(IConversionGetValues.NEW_FIELD_NAME, Types.CHAR);
        statement.registerOutParameter(IConversionGetValues.STATEMENT_1, Types.CHAR);
        statement.registerOutParameter(IConversionGetValues.STATEMENT_2, Types.CHAR);
        statement.registerOutParameter(IConversionGetValues.STATEMENT_3, Types.CHAR);
        statement.registerOutParameter(IConversionGetValues.STATEMENT_4, Types.CHAR);
        statement.registerOutParameter(IConversionGetValues.STATEMENT_5, Types.CHAR);
        statement.registerOutParameter(IConversionGetValues.STATEMENT_6, Types.CHAR);

        statement.execute();

        ConversionValues values = new ConversionValues();
        values.setKey(new ConversionKey(fileKey, getStringTrim(statement, IConversionGetValues.FIELD_TO_CONVERT)));
        values.setNewFieldName(getStringTrim(statement, IConversionGetValues.NEW_FIELD_NAME));

        List<String> conversons = new LinkedList<String>();
        conversons.add(getStringTrim(statement, IConversionGetValues.STATEMENT_1));
        conversons.add(getStringTrim(statement, IConversionGetValues.STATEMENT_2));
        conversons.add(getStringTrim(statement, IConversionGetValues.STATEMENT_3));
        conversons.add(getStringTrim(statement, IConversionGetValues.STATEMENT_4));
        conversons.add(getStringTrim(statement, IConversionGetValues.STATEMENT_5));
        conversons.add(getStringTrim(statement, IConversionGetValues.STATEMENT_6));

        values.setConversions(conversons.toArray(new String[conversons.size()]));

        return values;
    }

    @Override
    public void setValues(ConversionValues values) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_setValues\"(?, ?, ?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IConversionSetValues.FIELD_TO_CONVERT, values.getKey().getFieldToConvert());
        statement.setString(IConversionSetValues.NEW_FIELD_NAME, values.getNewFieldName());

        String[] conversons = values.getConversions();
        int i = IConversionSetValues.STATEMENT_1;
        for (String conversion : conversons) {
            statement.setString(i, conversion);
            i++;
        }

        statement.execute();
    }

    @Override
    public Result check() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_check\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IConversionCheck.SUCCESS, Success.NO.label());
        statement.setString(IConversionCheck.FIELD_NAME, EMPTY_STRING);
        statement.setString(IConversionCheck.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IConversionCheck.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IConversionCheck.FIELD_NAME, Types.CHAR);
        statement.registerOutParameter(IConversionCheck.MESSAGE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, IConversionCheck.SUCCESS);
        String fieldName = getStringTrim(statement, IConversionCheck.FIELD_NAME);
        String message = getStringTrim(statement, IConversionCheck.MESSAGE);

        return new Result(fieldName, message, success);
    }

    @Override
    public void book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_book\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

}
