/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.conversion;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.exceptions.FieldsNotAvailableException;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.host.files.Field;
import biz.rapidfire.core.host.files.FieldList;
import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionAction;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionKey;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

import com.ibm.as400.access.AS400;

public class ConversionManager extends AbstractManager<IRapidFireConversionResource, ConversionKey, ConversionValues, ConversionAction> {

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
    public Result initialize(MaintenanceMode mode, ConversionKey key) throws Exception {

        JobKey jobKey = new JobKey(key.getJobName());
        fileKey = new FileKey(jobKey, key.getPosition());

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_initialize\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IConversionInitialize.MODE, mode.label());
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
    public Result book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_book\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();

        return null;
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(ConversionKey key, ConversionAction conversionAction) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_checkAction\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IConversionCheckAction.ACTION, conversionAction.label());
        statement.setString(IConversionCheckAction.JOB, key.getJobName());
        statement.setInt(IConversionCheckAction.POSITION, key.getPosition());
        statement.setString(IConversionCheckAction.FIELD_TO_CONVERT, key.getFieldToConvert());
        statement.setString(IConversionCheckAction.SUCCESS, Success.NO.label());
        statement.setString(IConversionCheckAction.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IConversionCheckAction.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IConversionCheckAction.MESSAGE, Types.CHAR);

        statement.execute();

        String success = statement.getString(IConversionCheckAction.SUCCESS);
        String message = statement.getString(IConversionCheckAction.MESSAGE);

        return new Result(null, message, success);
    }

    protected ConversionAction[] getValidActions(ConversionKey conversionKey) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTCNV_getValidActions\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IConversionGetValidActions.JOB, conversionKey.getJobName());
        statement.setInt(IConversionGetValidActions.POSITION, conversionKey.getPosition());
        statement.setString(IConversionGetValidActions.FIELD_TO_CONVERT, conversionKey.getFieldToConvert());
        statement.setInt(IConversionGetValidActions.NUMBER_ACTIONS, 0);
        statement.setString(IConversionGetValidActions.ACTIONS, EMPTY_STRING);

        statement.registerOutParameter(IConversionGetValidActions.NUMBER_ACTIONS, Types.DECIMAL);
        statement.registerOutParameter(IConversionGetValidActions.ACTIONS, Types.CHAR);

        statement.execute();

        int numberActions = statement.getBigDecimal(IConversionGetValidActions.NUMBER_ACTIONS).intValue();
        String[] actions = splitActions(statement.getString(IConversionGetValidActions.ACTIONS), numberActions);

        Set<ConversionAction> conversionActions = new HashSet<ConversionAction>();
        for (String action : actions) {
            conversionActions.add(ConversionAction.find(action.trim()));
        }

        Result result = checkAction(ConversionKey.createNew(new FileKey(new JobKey(conversionKey.getJobName()), conversionKey.getPosition())),
            ConversionAction.CREATE);
        if (result.isSuccessfull()) {
            conversionActions.add(ConversionAction.CREATE);
        }

        return conversionActions.toArray(new ConversionAction[conversionActions.size()]);
    }

    @Override
    public boolean isValidAction(IRapidFireConversionResource conversion, ConversionAction action) throws Exception {

        if (isActionCacheEnabled()) {
            return isValidCachedAction(conversion, action);
        } else {
            return isValidUncachedAction(conversion, action);
        }
    }

    private boolean isValidUncachedAction(IRapidFireConversionResource conversion, ConversionAction action) throws Exception {

        Result result = checkAction(conversion.getKey(), action);

        return result.isSuccessfull();
    }

    private boolean isValidCachedAction(IRapidFireConversionResource conversion, ConversionAction action) throws Exception {

        KeyConversionActionCache conversionActionsKey = new KeyConversionActionCache(conversion);

        Set<ConversionAction> actionsSet = ConversionActionCache.getInstance().getActions(conversionActionsKey);
        if (actionsSet == null) {
            ConversionAction[] conversionActions = getValidActions(conversion.getKey());
            ConversionActionCache.getInstance().putActions(conversionActionsKey, conversionActions);
            actionsSet = ConversionActionCache.getInstance().getActions(conversionActionsKey);
        }

        return actionsSet.contains(action);
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }

    public Field[] getFieldsOfFirstArea(Shell shell, IRapidFireFileResource file) throws Exception {

        String connectionName = file.getParentSubSystem().getConnectionName();
        String libraryName = null;
        String fileName = null;

        IRapidFireAreaResource[] areas = file.getParentSubSystem().getAreas(file, shell);
        if (areas == null || areas.length == 0) {
            throw new FieldsNotAvailableException(Messages.Field_list_not_available_Areas_have_not_yet_been_defined);
        }

        final int LIBRARY_NOT_FOUND = 1;
        final int FILE_NOT_FOUND = 2;

        int errorType = 0;
        for (IRapidFireAreaResource iRapidFireAreaResource : areas) {
            libraryName = iRapidFireAreaResource.getLibrary();
            AS400 system = file.getParentSubSystem().getHostSystem();
            if (RapidFireHelper.checkLibrary(system, libraryName)) {
                fileName = file.getName();
                if (RapidFireHelper.checkFile(system, libraryName, file.getName())) {
                    break;
                } else {
                    errorType = FILE_NOT_FOUND;
                }
            } else {
                errorType = LIBRARY_NOT_FOUND;
            }
        }

        if (errorType != 0) {

            switch (errorType) {
            case FILE_NOT_FOUND:
                throw new FieldsNotAvailableException(Messages.bindParameters(Messages.File_A_not_found_in_areas, fileName));

            case LIBRARY_NOT_FOUND:
                throw new FieldsNotAvailableException(Messages.bindParameters(Messages.Library_A_not_found_on_system_B, libraryName, connectionName));

            default:
                break;
            }

            return null;
        }

        FieldList fieldList = new FieldList(connectionName, fileName, libraryName);

        return fieldList.getFields();
    }

    public String getSourceFilePrefix(boolean isConversionProgram, String srcLibraryName, String srcFileName, String tgtLibraryName,
        String tgtFileName) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"PROMOTER_get_Source_Field_Prefix\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        if (isConversionProgram) {
            statement.setString(IConversionGetSourceFieldPrefix.CONVERSION_PROGRAM, "*ANY");
        } else {
            statement.setString(IConversionGetSourceFieldPrefix.CONVERSION_PROGRAM, "*NONE");
        }
        statement.setString(IConversionGetSourceFieldPrefix.SOURCE_LIBRARY, srcLibraryName);
        statement.setString(IConversionGetSourceFieldPrefix.SOURCE_FILE, srcFileName);
        statement.setString(IConversionGetSourceFieldPrefix.TARGET_LIBRARY, tgtLibraryName);
        statement.setString(IConversionGetSourceFieldPrefix.TARGET_FILE, tgtFileName);
        statement.setString(IConversionGetSourceFieldPrefix.PREFIX, EMPTY_STRING);

        statement.registerOutParameter(IConversionGetSourceFieldPrefix.PREFIX, Types.CHAR);

        statement.execute();

        String prefix = getStringTrim(statement, IConversionGetSourceFieldPrefix.PREFIX);

        return prefix;
    }

    public String getTargetFilePrefix(boolean isConversionProgram, String srcLibraryName, String srcFileName, String tgtLibraryName,
        String tgtFileName) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"PROMOTER_get_Target_Field_Prefix\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        if (isConversionProgram) {
            statement.setString(IConversionGetTargetFieldPrefix.CONVERSION_PROGRAM, "*ANY");
        } else {
            statement.setString(IConversionGetTargetFieldPrefix.CONVERSION_PROGRAM, "*NONE");
        }
        statement.setString(IConversionGetTargetFieldPrefix.SOURCE_LIBRARY, srcLibraryName);
        statement.setString(IConversionGetTargetFieldPrefix.SOURCE_FILE, srcFileName);
        statement.setString(IConversionGetTargetFieldPrefix.TARGET_LIBRARY, tgtLibraryName);
        statement.setString(IConversionGetTargetFieldPrefix.TARGET_FILE, tgtFileName);
        statement.setString(IConversionGetTargetFieldPrefix.PREFIX, EMPTY_STRING);

        statement.registerOutParameter(IConversionGetTargetFieldPrefix.PREFIX, Types.CHAR);

        statement.execute();

        String prefix = getStringTrim(statement, IConversionGetTargetFieldPrefix.PREFIX);

        return prefix;
    }
}
