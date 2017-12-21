/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.conversion;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.conversion.ConversionMaintenanceDialog;
import biz.rapidfire.core.exceptions.FieldsNotAvailableException;
import biz.rapidfire.core.host.files.Field;
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
            refreshUI(conversion);
        }
    }

    private String[] getFieldNames(IRapidFireConversionResource conversion) throws Exception {

        List<String> fieldNames = new LinkedList<String>();

        try {

            Field[] fields = getManager().getFields(getShell(), conversion.getParent());
            if (fields == null || fields.length == 0) {

                String connectionName = conversion.getParentSubSystem().getConnectionName();
                return new String[] { Messages.bindParameters(Messages.Field_list_not_available_Areas_have_not_yet_been_defined, connectionName) };
            }

            for (Field field : fields) {
                fieldNames.add(field.getName());
            }

        } catch (FieldsNotAvailableException e) {
            fieldNames.add(e.getLocalizedMessage());
        }

        return fieldNames.toArray(new String[fieldNames.size()]);
    }
}
