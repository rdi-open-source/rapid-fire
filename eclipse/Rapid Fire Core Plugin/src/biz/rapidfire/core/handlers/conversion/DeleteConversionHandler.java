/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.conversion;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.dialogs.maintenance.conversion.ConversionMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.conversion.ConversionValues;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionAction;
import biz.rapidfire.core.model.IRapidFireConversionResource;

public class DeleteConversionHandler extends AbstractConversionMaintenanceHandler implements IHandler {

    public DeleteConversionHandler() {
        super(MaintenanceMode.DELETE, ConversionAction.DELETE);
    }

    @Override
    protected void performAction(IRapidFireConversionResource conversion) throws Exception {

        ConversionValues values = getManager().getValues();

        ConversionMaintenanceDialog dialog = ConversionMaintenanceDialog.getDeleteDialog(getShell(), getManager());
        dialog.setFields(new String[0]);
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();

            refreshUIDeleted(conversion.getParentSubSystem(), conversion, conversion.getParentNode());
        }
    }
}
