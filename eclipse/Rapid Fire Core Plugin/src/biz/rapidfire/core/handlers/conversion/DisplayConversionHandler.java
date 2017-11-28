/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.conversion;

import org.eclipse.core.commands.IHandler;

import biz.rapidfire.core.dialogs.maintenance.conversion.ConversionMaintenanceDialog;
import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.conversion.ConversionValues;
import biz.rapidfire.core.model.maintenance.conversion.shared.ConversionAction;

public class DisplayConversionHandler extends AbstractConversionMaintenanceHandler implements IHandler {

    public DisplayConversionHandler() {
        super(IMaintenance.MODE_DISPLAY, ConversionAction.DISPLAY);
    }

    @Override
    protected void performAction(IRapidFireConversionResource conversion) throws Exception {

        ConversionValues values = getManager().getValues();

        ConversionMaintenanceDialog dialog = ConversionMaintenanceDialog.getDisplayDialog(getShell(), getManager());
        dialog.setValue(values);

        dialog.open();
    }
}
