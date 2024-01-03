/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.exceptions;

import biz.rapidfire.core.Messages;

public class AutoReconnectErrorException extends Exception {

    private static final long serialVersionUID = -4868597010305897522L;

    String connectionName;

    public AutoReconnectErrorException(String connectionName) {
        this.connectionName = connectionName;
    }

    @Override
    public String getMessage() {
        return Messages.bindParameters(Messages.AutoReconnectErrorException_Connection_broken_Could_not_reconnect_to_system_A, connectionName);
    }
}
