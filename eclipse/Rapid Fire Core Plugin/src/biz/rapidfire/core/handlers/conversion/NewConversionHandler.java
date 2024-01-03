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

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.dialogs.maintenance.conversion.ConversionMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.conversion.ConversionValues;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionAction;
import biz.rapidfire.core.model.IRapidFireConversionResource;

public class NewConversionHandler extends AbstractConversionMaintenanceHandler implements IHandler {

    public NewConversionHandler() {
        super(MaintenanceMode.CREATE, ConversionAction.CREATE);
    }

    @Override
    protected void performAction(IRapidFireConversionResource conversion) throws Exception {

        ConversionValues values = getManager().getValues();

        ConversionMaintenanceDialog dialog = ConversionMaintenanceDialog.getCreateDialog(getShell(), getManager());
        dialog.setFields(getFieldNames(conversion));
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();

            values = dialog.getValue();
            IRapidFireConversionResource newConversion = conversion.getParentSubSystem().getConversion(conversion.getParentResource(),
                values.getKey().getFieldToConvert(), getShell());
            if (newConversion != null) {
                newConversion.setParentNode(conversion.getParentNode());
                refreshUICreated(newConversion.getParentSubSystem(), newConversion, newConversion.getParentNode());
            } else {
                MessageDialogAsync.displayError(Messages.Could_not_create_resource_Resource_not_found);
            }
        }
    }
}
