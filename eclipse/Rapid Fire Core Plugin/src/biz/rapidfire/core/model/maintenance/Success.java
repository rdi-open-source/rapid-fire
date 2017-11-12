/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance;

public enum Success {
    YES ("Y"),
    NO ("N");

    private String value;

    private Success(String value) {
        this.value = value;
    }

    public static String value(boolean success) {
        if (success) {
            return Success.YES.value;
        } else {
            return Success.NO.value;
        }
    }

    public static Success value(String value) {
        if (Success.YES.value.equalsIgnoreCase(value)) {
            return Success.YES;
        } else {
            return Success.NO;
        }
    }
}
