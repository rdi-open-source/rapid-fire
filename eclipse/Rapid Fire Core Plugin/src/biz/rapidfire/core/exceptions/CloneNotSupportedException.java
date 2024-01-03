/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.exceptions;

public class CloneNotSupportedException extends RuntimeException {

    private static final long serialVersionUID = 2467886739040458070L;

    public CloneNotSupportedException(String message, Throwable e) {
        super(message, e);
    }
}
