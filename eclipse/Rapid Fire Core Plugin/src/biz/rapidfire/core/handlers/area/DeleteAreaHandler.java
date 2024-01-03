/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.area;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.dialogs.maintenance.area.AreaMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.area.AreaValues;
import biz.rapidfire.core.maintenance.area.shared.AreaAction;
import biz.rapidfire.core.model.IRapidFireAreaResource;

public class DeleteAreaHandler extends AbstractAreaMaintenanceHandler implements IHandler {

    public DeleteAreaHandler() {
        super(MaintenanceMode.DELETE, AreaAction.DELETE);
    }

    @Override
    protected void performAction(IRapidFireAreaResource area) throws Exception {

        AreaValues values = getManager().getValues();

        AreaMaintenanceDialog dialog = AreaMaintenanceDialog.getDeleteDialog(getShell(), getManager());
        dialog.setLibraries(getManager().getLibraries(getShell(), area));
        dialog.setLibraryLists(getManager().getLibraryLists(getShell(), area));
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();

            refreshUIDeleted(area.getParentSubSystem(), area, area.getParentNode());
        }
    }
}
