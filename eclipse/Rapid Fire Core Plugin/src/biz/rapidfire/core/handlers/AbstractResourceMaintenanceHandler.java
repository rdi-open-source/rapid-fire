/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.maintenance.IMaintenance;

public abstract class AbstractResourceMaintenanceHandler extends AbstractResourceHandler {

    public AbstractResourceMaintenanceHandler(String mode) {
        super(mode);
    }

    protected boolean isCommitControl() {

        String mode = getMode();
        if (IMaintenance.MODE_CHANGE.equals(mode) || IMaintenance.MODE_DELETE.equals(mode)) {
            return true;
        }

        return false;
    }

    protected void logError(String message, Throwable e) {

        RapidFireCorePlugin.logError(message, e); //$NON-NLS-1$
        MessageDialogAsync.displayError(getShell(), ExceptionHelper.getLocalizedMessage(e));
    }
}
