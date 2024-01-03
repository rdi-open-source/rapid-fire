/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import biz.rapidfire.core.helpers.StringHelper;

public class JobName {

    private String name;
    private String user;
    private String number;

    public JobName(String name, String user, String number) {
        this.name = name;
        this.user = user;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();

        if (!StringHelper.isNullOrEmpty(name) && !StringHelper.isNullOrEmpty(user) && !StringHelper.isNullOrEmpty(number)) {
            buffer.append(number);
            buffer.append("/"); //$NON-NLS-1$
            buffer.append(user);
            buffer.append("/"); //$NON-NLS-1$
            buffer.append(name);
        }

        return buffer.toString();
    }
}
