/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.eclipse.swt.widgets.Shell;

import com.ibm.as400.access.AS400;

public interface IJDBCConnection {

    public static final String LIBRARY = "<LIBRARY>";
    public static final String CATALOG_SEPARATOR = "<CATALOG_SEPARATOR>";

    public AS400 getSystem();

    public String getLibraryName();

    public String getConnectionName();

    public PreparedStatement prepareStatement(String sql) throws Exception;

    public CallableStatement prepareCall(String sql) throws Exception;

    public void closeResultSet(ResultSet resultSet);

    public void closeStatement(PreparedStatement preparedStatement);

    public boolean checkRapidFireLibrary(Shell shell);

    public String insertLibraryQualifier(String sqlStatement);

    public boolean convertYesNo(String yesNoValue);

    public boolean isAutoCommit();

    public String getKey();
}
