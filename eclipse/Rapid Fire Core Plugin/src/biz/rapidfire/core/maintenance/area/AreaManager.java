/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.area;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.area.shared.AreaAction;
import biz.rapidfire.core.maintenance.area.shared.AreaKey;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class AreaManager extends AbstractManager<IRapidFireAreaResource, AreaKey, AreaValues, AreaAction> {

    private static final String ERROR_001 = "001"; //$NON-NLS-1$
    private static final String ERROR_002 = "002"; //$NON-NLS-1$
    private static final String ERROR_003 = "003"; //$NON-NLS-1$
    private static final String ERROR_004 = "004"; //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$
    private IJDBCConnection dao;
    private FileKey fileKey;

    public AreaManager(IJDBCConnection dao) {
        this.dao = dao;
    }

    @Override
    public void openFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAREA_openFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result initialize(MaintenanceMode mode, AreaKey key) throws Exception {

        JobKey jobKey = new JobKey(key.getJobName());
        fileKey = new FileKey(jobKey, key.getPosition());

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAREA_initialize\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IAreaInitialize.MODE, mode.label());
        statement.setString(IAreaInitialize.JOB, key.getJobName());
        statement.setInt(IAreaInitialize.POSITION, key.getPosition());
        statement.setString(IAreaInitialize.AREA, key.getArea());
        statement.setString(IAreaInitialize.SUCCESS, Success.NO.label());
        statement.setString(IAreaInitialize.ERROR_CODE, EMPTY_STRING);

        statement.registerOutParameter(IAreaInitialize.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IAreaInitialize.ERROR_CODE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, IAreaInitialize.SUCCESS);
        String errorCode = getStringTrim(statement, IAreaInitialize.ERROR_CODE);

        String message;
        if (Success.YES.label().equals(success)) {
            message = null;
        } else {
            message = Messages.bindParameters(Messages.Could_not_initialize_area_manager_for_area_D_of_file_C_of_job_A_in_library_B,
                key.getJobName(), dao.getLibraryName(), key.getPosition(), key.getArea(), getErrorMessage(errorCode));
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
            return Messages.AreaManager_001;
        } else if (ERROR_002.equals(errorCode)) {
            return Messages.AreaManager_002;
        } else if (ERROR_003.equals(errorCode)) {
            return Messages.AreaManager_003;
        } else if (ERROR_004.equals(errorCode)) {
            return Messages.AreaManager_004;
        }

        return Messages.bindParameters(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    @Override
    public AreaValues getValues() throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAREA_getValues\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IAreaGetValues.AREA, EMPTY_STRING);
        statement.setString(IAreaGetValues.LIBRARY, EMPTY_STRING);
        statement.setString(IAreaGetValues.LIBRARY_LIST, EMPTY_STRING);
        statement.setString(IAreaGetValues.LIBRARY_CCSID, EMPTY_STRING);
        statement.setString(IAreaGetValues.COMMAND_EXTENSION, EMPTY_STRING);

        statement.registerOutParameter(IAreaGetValues.AREA, Types.CHAR);
        statement.registerOutParameter(IAreaGetValues.LIBRARY, Types.CHAR);
        statement.registerOutParameter(IAreaGetValues.LIBRARY_LIST, Types.CHAR);
        statement.registerOutParameter(IAreaGetValues.LIBRARY_CCSID, Types.CHAR);
        statement.registerOutParameter(IAreaGetValues.COMMAND_EXTENSION, Types.CHAR);

        statement.execute();

        AreaValues values = new AreaValues();
        values.setKey(new AreaKey(fileKey, getStringTrim(statement, IAreaGetValues.AREA)));
        values.setLibrary(getStringTrim(statement, IAreaGetValues.LIBRARY));
        values.setLibraryList(getStringTrim(statement, IAreaGetValues.LIBRARY_LIST));
        values.setLibraryCcsid(getStringTrim(statement, IAreaGetValues.LIBRARY_CCSID));
        values.setCommandExtension(getStringTrim(statement, IAreaGetValues.COMMAND_EXTENSION));

        return values;
    }

    @Override
    public void setValues(AreaValues values) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAREA_setValues\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IAreaSetValues.AREA, values.getKey().getArea());
        statement.setString(IAreaSetValues.LIBRARY, values.getLibrary());
        statement.setString(IAreaSetValues.LIBRARY_LIST, values.getLibraryList());
        statement.setString(IAreaSetValues.LIBRARY_CCSID, values.getLibraryCcsid());
        statement.setString(IAreaSetValues.COMMAND_EXTENSION, values.getCommandExtension());

        statement.execute();
    }

    @Override
    public Result check() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAREA_check\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IAreaCheck.SUCCESS, Success.NO.label());
        statement.setString(IAreaCheck.FIELD_NAME, EMPTY_STRING);
        statement.setString(IAreaCheck.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IAreaCheck.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IAreaCheck.FIELD_NAME, Types.CHAR);
        statement.registerOutParameter(IAreaCheck.MESSAGE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, IAreaCheck.SUCCESS);
        String fieldName = getStringTrim(statement, IAreaCheck.FIELD_NAME);
        String message = getStringTrim(statement, IAreaCheck.MESSAGE);

        return new Result(fieldName, message, success);
    }

    @Override
    public Result book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAREA_book\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();

        return null;
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAREA_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(AreaKey key, AreaAction areaAction) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAREA_checkAction\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IAreaCheckAction.ACTION, areaAction.label());
        statement.setString(IAreaCheckAction.JOB, key.getJobName());
        statement.setInt(IAreaCheckAction.POSITION, key.getPosition());
        statement.setString(IAreaCheckAction.AREA, key.getArea());
        statement.setString(IAreaCheckAction.SUCCESS, Success.NO.label());
        statement.setString(IAreaCheckAction.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IAreaCheckAction.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IAreaCheckAction.MESSAGE, Types.CHAR);

        statement.execute();

        String success = statement.getString(IAreaCheckAction.SUCCESS);
        String message = statement.getString(IAreaCheckAction.MESSAGE);

        return new Result(null, message, success);
    }

    protected AreaAction[] getValidActions(AreaKey areaKey) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAREA_getValidActions\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IAreaGetValidActions.JOB, areaKey.getJobName());
        statement.setInt(IAreaGetValidActions.POSITION, areaKey.getPosition());
        statement.setString(IAreaGetValidActions.AREA, areaKey.getArea());
        statement.setInt(IAreaGetValidActions.NUMBER_ACTIONS, 0);
        statement.setString(IAreaGetValidActions.ACTIONS, EMPTY_STRING);

        statement.registerOutParameter(IAreaGetValidActions.NUMBER_ACTIONS, Types.DECIMAL);
        statement.registerOutParameter(IAreaGetValidActions.ACTIONS, Types.CHAR);

        statement.execute();

        int numberActions = statement.getBigDecimal(IAreaGetValidActions.NUMBER_ACTIONS).intValue();
        String[] actions = splitActions(statement.getString(IAreaGetValidActions.ACTIONS), numberActions);

        Set<AreaAction> areaActions = new HashSet<AreaAction>();
        for (String action : actions) {
            areaActions.add(AreaAction.find(action.trim()));
        }

        Result result = checkAction(AreaKey.createNew(new FileKey(new JobKey(areaKey.getJobName()), areaKey.getPosition())), AreaAction.CREATE);
        if (result.isSuccessfull()) {
            areaActions.add(AreaAction.CREATE);
        }

        return areaActions.toArray(new AreaAction[areaActions.size()]);
    }

    @Override
    public boolean isValidAction(IRapidFireAreaResource area, AreaAction action) throws Exception {

        if (isActionCacheEnabled()) {
            return isValidCachedAction(area, action);
        } else {
            return isValidUncachedAction(area, action);
        }
    }

    private boolean isValidUncachedAction(IRapidFireAreaResource area, AreaAction action) throws Exception {

        Result result = checkAction(area.getKey(), action);

        return result.isSuccessfull();
    }

    private boolean isValidCachedAction(IRapidFireAreaResource area, AreaAction action) throws Exception {

        KeyAreaActionCache areaActionsKey = new KeyAreaActionCache(area);

        Set<AreaAction> actionsSet = AreaActionCache.getInstance().getActions(areaActionsKey);
        if (actionsSet == null) {
            AreaAction[] areaActions = getValidActions(area.getKey());
            AreaActionCache.getInstance().putActions(areaActionsKey, areaActions);
            actionsSet = AreaActionCache.getInstance().getActions(areaActionsKey);
        }

        return actionsSet.contains(action);
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }

    public IRapidFireLibraryResource[] getLibraries(Shell shell, IRapidFireAreaResource area) throws Exception {

        IRapidFireLibraryResource[] libraries = area.getParentSubSystem().getLibraries(area.getParentJob(), shell);

        return libraries;

    }

    public IRapidFireLibraryListResource[] getLibraryLists(Shell shell, IRapidFireAreaResource area) throws Exception {

        IRapidFireLibraryListResource[] libraryLists = area.getParentSubSystem().getLibraryLists(area.getParentJob(), shell);

        return libraryLists;

    }
}
