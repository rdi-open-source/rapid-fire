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
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.actions.AbstractNewNodePopupMenuAction;
import biz.rapidfire.rse.subsystem.resources.LibraryListsNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireLibraryListResource;

public class NewLibraryListsNodePopupMenuAction extends AbstractNewNodePopupMenuAction<LibraryListsNode, IRapidFireLibraryListResource> {

    public NewLibraryListsNodePopupMenuAction(Shell shell) {
        super(Messages.ActionLabel_New_Library, Messages.ActionTooltip_New_Library, shell, new NewLibraryListHandler());

        setImageDescriptor(RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_NEW_LIBRARY));
        setContextMenuGroup("group.new");
    }

    @Override
    public void run() {

        try {

            Object element = getFirstSelection();

            if (element instanceof LibraryListsNode) {
                LibraryListsNode librariesNode = (LibraryListsNode)element;
                IRapidFireJobResource job = librariesNode.getParentResource();

                RapidFireLibraryListResource library = RapidFireLibraryListResource.createEmptyInstance(job);

                IStructuredSelection selection = new StructuredSelection(library);
                getHandler().executeWithSelection(selection);
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not execute 'New Library List' handler ***", e); //$NON-NLS-1$
        }

    }

    @Override
    protected IRapidFireLibraryListResource createNewResource(LibraryListsNode node) {
        return RapidFireLibraryListResource.createEmptyInstance(node.getParentResource());
    }

    @Override
    protected LibraryListsNode getResource(Object object) {

        if (object instanceof LibraryListsNode) {
            return (LibraryListsNode)object;
        }

        return null;
    }
}
