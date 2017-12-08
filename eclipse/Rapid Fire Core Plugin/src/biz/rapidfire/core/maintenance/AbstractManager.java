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

import biz.rapidfire.core.model.IRapidFireResource;

public abstract class AbstractManager<R extends IRapidFireResource, K extends IResourceKey, V extends IResourceValues, A extends IResourceAction> {

    public abstract void openFiles() throws Exception;

    // Return succes: Y or N
    public abstract Result initialize(MaintenanceMode mode, K key) throws Exception;

    public abstract V getValues() throws Exception;

    public abstract void setValues(V values) throws Exception;

    public abstract Result check() throws Exception;

    public abstract void book() throws Exception;

    public abstract void closeFiles() throws Exception;

    public abstract Result checkAction(K key, A resourceAction) throws Exception;

    public boolean isValidAction(R resource, A resourceAction) throws Exception {
        return true;
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
}
