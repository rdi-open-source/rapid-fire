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
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.file.NewFileHandler;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.FilesNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireFileResource;

public class NewFileAction extends AbstractNewNodePopupMenuAction<FilesNode, IRapidFireFileResource> {

    public NewFileAction(Shell shell) {
        super(Messages.ActionLabel_New_File, Messages.ActionTooltip_New_File, shell, new NewFileHandler());

        setImageDescriptor(RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_NEW_FILE));
        setContextMenuGroup("group.new");
    }

    @Override
    public void run() {

        try {

            Object element = getFirstSelection();
            FilesNode filesNode = (FilesNode)element;
            IRapidFireJobResource job = filesNode.getParentResource();

            RapidFireFileResource file = RapidFireFileResource.createEmptyInstance(job);
            file.setParentNode(filesNode);

            IStructuredSelection selection = new StructuredSelection(file);
            getHandler().executeWithSelection(selection);

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not execute 'New File' handler ***", e); //$NON-NLS-1$
        }

    }

    @Override
    protected IRapidFireFileResource createNewResource(FilesNode node) {
        return RapidFireFileResource.createEmptyInstance(node.getParentResource());
    }

    @Override
    protected FilesNode getResource(Object object) {

        if (object instanceof FilesNode) {
            return (FilesNode)object;
        }

        return null;
    }
}
