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

public class ChangeConversionHandler extends AbstractConversionMaintenanceHandler implements IHandler {

    public ChangeConversionHandler() {
        super(MaintenanceMode.CHANGE, ConversionAction.CHANGE);
    }

    @Override
    protected void performAction(IRapidFireConversionResource conversion) throws Exception {

        ConversionValues values = getManager().getValues();

        // String prefix1= getManager().getSourceFilePrefix(true, "RFPRI",
        // "JOBS", "RFPRI", "JOBS");
        // String prefix2= getManager().getSourceFilePrefix(true, "RFPRI",
        // "JOBS", "RFPRI", "FILES");

        // String prefix3= getManager().getSourceFilePrefix(false, "RFPRI",
        // "JOBS", "RFPRI", "JOBS");
        // String prefix4= getManager().getSourceFilePrefix(false, "RFPRI",
        // "JOBS", "RFPRI", "FILES");

        // String prefix5= getManager().getTargetFilePrefix(true, "RFPRI",
        // "JOBS", "RFPRI", "JOBS");
        // String prefix6= getManager().getTargetFilePrefix(true, "RFPRI",
        // "JOBS", "RFPRI", "FILES");

        // String prefix7= getManager().getTargetFilePrefix(false, "RFPRI",
        // "JOBS", "RFPRI", "JOBS");
        // String prefix8= getManager().getTargetFilePrefix(false, "RFPRI",
        // "JOBS", "RFPRI", "FILES");

        ConversionMaintenanceDialog dialog = ConversionMaintenanceDialog.getChangeDialog(getShell(), getManager());
        dialog.setFields(new String[0]);
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();

            conversion.reload(getShell());
            refreshUIChanged(conversion.getParentSubSystem(), conversion, conversion.getParentNode());
        }
    }
}
