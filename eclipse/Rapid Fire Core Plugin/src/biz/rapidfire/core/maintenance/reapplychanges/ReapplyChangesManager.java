/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.reapplychanges;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.area.shared.AreaKey;
import biz.rapidfire.core.model.IFileCopyStatus;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class ReapplyChangesManager extends AbstractManager<IFileCopyStatus, AreaKey, ReapplyChangesValues, ReapplyChangesAction> {

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$
    private IJDBCConnection dao;

    private ReapplyChangesValues values;

    public ReapplyChangesManager(IJDBCConnection dao) {
        this.dao = dao;
    }

    @Override
    public void openFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_openFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result initialize(MaintenanceMode mode, AreaKey areaKey) throws Exception {

        return Result.createSuccessResult();
    }

    @Override
    public ReapplyChangesValues getValues() throws Exception {
        throw new IllegalAccessError("Calling getValues() is not allowed. Method has not been implemented.");
    }

    @Override
    public void setValues(ReapplyChangesValues values) throws Exception {

        this.values = values;
    }

    @Override
    public Result check() throws Exception {
        throw new IllegalAccessError("Calling check() is not allowed. Method has not been implemented.");
    }

    @Override
    public Result book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"PROMOTER_reapplyAllChanges\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IReapplyChangesSetValues.JOB, values.getJob());
        statement.setInt(IReapplyChangesSetValues.POSITION, values.getPosition());
        statement.setString(IReapplyChangesSetValues.AREA, values.getArea());

        statement.execute();

        return null;
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(AreaKey areaKey, ReapplyChangesAction action) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_checkAction\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IReapplyChangesCheckAction.ACTION, action.label());
        statement.setString(IReapplyChangesCheckAction.JOB, areaKey.getJobName());
        statement.setInt(IReapplyChangesCheckAction.POSITION, areaKey.getPosition());
        statement.setString(IReapplyChangesCheckAction.AREA, Success.NO.label());
        statement.setString(IReapplyChangesCheckAction.SUCCESS, Success.NO.label());
        statement.setString(IReapplyChangesCheckAction.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IReapplyChangesCheckAction.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IReapplyChangesCheckAction.MESSAGE, Types.CHAR);

        statement.execute();

        String success = statement.getString(IReapplyChangesCheckAction.SUCCESS);
        String message = statement.getString(IReapplyChangesCheckAction.MESSAGE);

        return new Result(null, message, success);
    }

    protected ReapplyChangesAction[] getValidActions(AreaKey areaKey) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_getValidActions\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IReapplyChangesGetValidActions.JOB, areaKey.getJobName());
        statement.setInt(IReapplyChangesGetValidActions.POSITION, areaKey.getPosition());
        statement.setString(IReapplyChangesGetValidActions.AREA, areaKey.getJobName());
        statement.setInt(IReapplyChangesGetValidActions.NUMBER_ACTIONS, 0);
        statement.setString(IReapplyChangesGetValidActions.ACTIONS, EMPTY_STRING);

        statement.registerOutParameter(IReapplyChangesGetValidActions.NUMBER_ACTIONS, Types.DECIMAL);
        statement.registerOutParameter(IReapplyChangesGetValidActions.ACTIONS, Types.CHAR);

        statement.execute();

        int numberActions = statement.getBigDecimal(IReapplyChangesGetValidActions.NUMBER_ACTIONS).intValue();
        String[] actions = splitActions(statement.getString(IReapplyChangesGetValidActions.ACTIONS), numberActions);

        Set<ReapplyChangesAction> areaActions = new HashSet<ReapplyChangesAction>();
        for (String action : actions) {
            areaActions.add(ReapplyChangesAction.find(action.trim()));
        }

        return areaActions.toArray(new ReapplyChangesAction[areaActions.size()]);
    }

    @Override
    public boolean isValidAction(IFileCopyStatus area, ReapplyChangesAction action) throws Exception {

        if (isActionCacheEnabled()) {
            return isValidCachedAction(area, action);
        } else {
            return isValidUncachedAction(area, action);
        }
    }

    private boolean isValidUncachedAction(IFileCopyStatus area, ReapplyChangesAction action) throws Exception {

        Result result = checkAction(area.getKey(), action);

        return result.isSuccessfull();
    }

    private boolean isValidCachedAction(IFileCopyStatus area, ReapplyChangesAction action) throws Exception {

        KeyReapplyChangesActionCache reapplyChangesActionsKey = new KeyReapplyChangesActionCache(area);

        Set<ReapplyChangesAction> actionsSet = ReapplyChangesActionCache.getInstance().getActions(reapplyChangesActionsKey);
        if (actionsSet == null) {
            ReapplyChangesAction[] fileCopyProgramGeneratorActions = getValidActions(area.getKey());
            ReapplyChangesActionCache.getInstance().putActions(reapplyChangesActionsKey, fileCopyProgramGeneratorActions);
            actionsSet = ReapplyChangesActionCache.getInstance().getActions(reapplyChangesActionsKey);
        }

        return actionsSet.contains(action);
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }
}
