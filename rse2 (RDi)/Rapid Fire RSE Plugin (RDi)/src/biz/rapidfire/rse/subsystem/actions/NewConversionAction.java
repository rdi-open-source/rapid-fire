/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.ui.actions.SystemBaseAction;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.conversion.NewConversionHandler;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.ConversionsNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireConversionResource;

public class NewConversionAction extends SystemBaseAction {

    public NewConversionAction(Shell shell) {
        super(Messages.ActionLabel_New_Conversion, Messages.ActionTooltip_New_Conversion, shell);

        setImageDescriptor(RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_NEW_CONVERSION));
        setContextMenuGroup("group.new");
    }

    @Override
    public void run() {

        try {

            Object element = getFirstSelection();
            ConversionsNode conversionsNode = (ConversionsNode)element;
            IRapidFireFileResource file = conversionsNode.getFile();

            RapidFireConversionResource conversion = RapidFireConversionResource.createEmptyInstance(file);
            conversion.setSubSystem((ISubSystem)file.getParentSubSystem());

            NewConversionHandler handler = new NewConversionHandler();
            IStructuredSelection selection = new StructuredSelection(conversion);
            handler.executeWithSelection(selection);

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not execute 'New Conversion' handler ***", e); //$NON-NLS-1$
        }

    }
}
