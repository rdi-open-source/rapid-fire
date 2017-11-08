/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.helpers;


public final class ExceptionHelper {

    public static String getLocalizedMessage(Throwable throwable) {

        if (StringHelper.isNullOrEmpty(throwable.getLocalizedMessage())) {
            return throwable.getClass().getName();
        } else {
            return throwable.getLocalizedMessage();
        }

    }

}
