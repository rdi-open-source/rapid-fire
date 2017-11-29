/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.actions.librarylist;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.librarylist.NewLibraryListHandler;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.LibrariesNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireLibraryListResource;

import com.ibm.etools.systems.core.ui.actions.SystemBaseAction;

public class NewLibraryListsNodePopupMenuAction extends SystemBaseAction {

    public NewLibraryListsNodePopupMenuAction(Shell shell) {
        super(Messages.ActionLabel_New_Library, Messages.ActionTooltip_New_Library, shell);

        setImageDescriptor(RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_NEW_LIBRARY));
        setContextMenuGroup("group.new");
    }

    @Override
    public void run() {

        try {

            Object element = getFirstSelection();

            if (element instanceof LibrariesNode) {
                LibrariesNode librariesNode = (LibrariesNode)element;
                IRapidFireJobResource job = librariesNode.getJob();

                RapidFireLibraryListResource library = RapidFireLibraryListResource.createEmptyInstance(job);

                NewLibraryListHandler handler = new NewLibraryListHandler();
                IStructuredSelection selection = new StructuredSelection(library);
                handler.executeWithSelection(selection);
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not execute 'New Library List' handler ***", e); //$NON-NLS-1$
        }

    }
}
