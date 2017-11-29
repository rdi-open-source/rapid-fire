/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.actions.file;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.file.NewFileHandler;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.FilesNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireFileResource;

import com.ibm.etools.systems.core.ui.actions.SystemBaseAction;

public class NewFilesNodePopupMenuAction extends SystemBaseAction {

    public NewFilesNodePopupMenuAction(Shell shell) {
        super(Messages.ActionLabel_New_File, Messages.ActionTooltip_New_File, shell);

        setImageDescriptor(RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_NEW_FILE));
        setContextMenuGroup("group.new");
    }

    @Override
    public void run() {

        try {

            Object element = getFirstSelection();

            if (element instanceof FilesNode) {
                FilesNode filesNode = (FilesNode)element;
                IRapidFireJobResource job = filesNode.getJob();

                RapidFireFileResource file = RapidFireFileResource.createEmptyInstance(job);

                NewFileHandler handler = new NewFileHandler();
                IStructuredSelection selection = new StructuredSelection(file);
                handler.executeWithSelection(selection);
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not execute 'New File' handler ***", e); //$NON-NLS-1$
        }

    }
}
