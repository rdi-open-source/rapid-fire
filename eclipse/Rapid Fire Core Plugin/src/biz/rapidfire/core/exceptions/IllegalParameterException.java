/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.exceptions;

import biz.rapidfire.core.Messages;

public class IllegalParameterException extends RuntimeException {

    private static final long serialVersionUID = -6347306016504970343L;

    public IllegalParameterException(String parameterName, String value) {
        super(Messages.bind("Illegal parameter value: {0}={1}", parameterName, value)); //$NON-NLS-1$
    }

}
