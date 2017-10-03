/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.actions;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.events.ISystemResourceChangeEvents;
import org.eclipse.rse.core.events.SystemResourceChangeEvent;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.ui.actions.SystemBaseAction;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.dialogs.AddRapidFireInstanceDialog;
import biz.rapidfire.core.model.IRapidFireInstanceResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.RapidFireSubSystem;
import biz.rapidfire.rse.subsystem.RapidFireSubSystemAttributes;

public class AddRapidFireInstanceAction extends SystemBaseAction {

    private RapidFireSubSystem subSystem;

    public AddRapidFireInstanceAction(Shell shell, RapidFireSubSystem subSystem) {
        super(null, shell);

        this.subSystem = subSystem;

        setText(Messages.Add_Rapid_Fire_Instance);
        setToolTipText(Messages.Tooltip_Add_Rapid_Fire_Instance);
        setImageDescriptor(RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_RAPID_FIRE));
    }

    @Override
    public void run() {

        AddRapidFireInstanceDialog dialog = new AddRapidFireInstanceDialog(getShell());
        if (dialog.open() == Dialog.OK) {

            RapidFireSubSystemAttributes subSystemAttributes = subSystem.getSubSystemAttributes();
            if (subSystemAttributes.hasRapidFireInstance(dialog.getLibrary())) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, Messages.bind(Messages.Instance_A_exists, dialog.getLibrary()));
                return;
            }

            IRapidFireInstanceResource resource = subSystemAttributes.addRapidFireInstance(dialog.getName(), dialog.getLibrary());

            // Refresh filters
            ISystemRegistry sr = RSECorePlugin.getTheSystemRegistry();
            Object[] filters = subSystem.getFilterPoolReferenceManager().getSystemFilterReferences(subSystem);
            for (Object object : filters) {
                sr.fireEvent(new SystemResourceChangeEvent(resource, ISystemResourceChangeEvents.EVENT_ADD, object));
            }
        }
    }
}
