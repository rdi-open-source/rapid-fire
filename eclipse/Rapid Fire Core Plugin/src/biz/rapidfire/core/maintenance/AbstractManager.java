/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance;

import java.sql.CallableStatement;
import java.sql.SQLException;

import biz.rapidfire.core.maintenance.shared.IResourceAction;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.preferences.Preferences;

public abstract class AbstractManager<R extends IRapidFireResource, K extends IResourceKey, V extends IResourceValues, A extends IResourceAction> {

    private static int SQL_ACTION_LENGTH = 10;

    public abstract void openFiles() throws Exception;

    // Return succes: Y or N
    public abstract Result initialize(MaintenanceMode mode, K key) throws Exception;

    public abstract V getValues() throws Exception;

    public abstract void setValues(V values) throws Exception;

    public abstract Result check() throws Exception;

    public abstract Result book() throws Exception;

    public abstract void closeFiles() throws Exception;

    public abstract Result checkAction(K key, A resourceAction) throws Exception;

    public abstract void recoverError();

    public boolean isValidAction(R resource, A resourceAction) throws Exception {
        return true;
    }

    protected boolean isActionCacheEnabled() {
        return Preferences.getInstance().isActionCacheEnabled();
    }

    protected String getStringTrim(CallableStatement statement, int parameterIndex) throws SQLException {
        return statement.getString(parameterIndex).trim();
    }

    protected int getInt(CallableStatement statement, int parameterIndex) throws SQLException {
        return statement.getInt(parameterIndex);
    }

    protected Float getFloat(CallableStatement statement, int parameterIndex) throws SQLException {
        return statement.getFloat(parameterIndex);
    }

    protected String[] splitActions(String actionsString, int numberActions) {

        String[] actions = new String[numberActions];

        int i = 0;
        int offset = 0;
        while (i < numberActions && offset < actionsString.length()) {
            actions[i] = actionsString.substring(offset, offset + SQL_ACTION_LENGTH);
            offset += SQL_ACTION_LENGTH;
            i++;
        }

        return actions;
    }
}
