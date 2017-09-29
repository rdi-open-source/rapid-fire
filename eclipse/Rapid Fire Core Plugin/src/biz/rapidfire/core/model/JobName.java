/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

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
}
