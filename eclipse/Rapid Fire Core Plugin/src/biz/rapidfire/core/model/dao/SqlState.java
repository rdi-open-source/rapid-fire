/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.dao;

import java.sql.SQLException;

/**
 * Enumeration with IBM i SQL states. See <a href=
 * "https://www.ibm.com/support/knowledgecenter/ssw_ibm_i_73/rzala/rzalaccl.htm#cc__classcode08"
 * >Listing of SQLSTATE values</a>.
 */
public enum SqlState {
    _08003 ("The connection does not exist.");

    private String value;
    private String description;

    private SqlState(String description) {

        this.value = super.toString().substring(1);
        this.description = description;
    }

    public String value() {
        return value;
    }

    public boolean matches(SQLException e) {
        return value.equals(e.getSQLState());
    }

    public String getDescription() {
        return description;
    }
}
