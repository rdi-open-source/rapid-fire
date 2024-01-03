/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.adapters;

import java.util.Arrays;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.exceptions.AutoReconnectErrorException;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.actions.NewCommandAction;
import biz.rapidfire.rse.subsystem.resources.CommandsNode;

public class CommandsNodeAdapter extends AbstractNodeAdapter<CommandsNode> {

    @Override
    public final boolean hasChildren(IAdaptable element) {
        return true;
    }

    @Override
    public String getText(Object element) {
        return Messages.NodeText_Commands;
    }

    @Override
    protected String getAbsoluteNamePrefix() {
        return "node.commands.";
    }

    @Override
    public void addActions(SystemMenuManager menuManager, IStructuredSelection selection, Shell shell, String menuGroup) {
        menuManager.add(null, new NewCommandAction(shell));
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object element) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_COMMAND);
    }

    @Override
    public Object[] getChildren(IAdaptable element, IProgressMonitor progressMonitor) {

        try {

            CommandsNode commandsNode = (CommandsNode)element;
            IRapidFireFileResource fileResource = commandsNode.getParentResource();

            IRapidFireCommandResource[] commands = fileResource.getParentSubSystem().getCommands(fileResource, getShell());
            for (IRapidFireCommandResource command : commands) {
                command.setParentNode(commandsNode);
            }

            Arrays.sort(commands);

            return commands;

        } catch (AutoReconnectErrorException e) {
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could resolve filter string and load commands ***", e); //$NON-NLS-1$
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        }

        return new Object[0];
    }

    @Override
    public String getType(Object element) {
        return Messages.NodeType_Commands;
    }

    @Override
    public String getRemoteType(Object element) {
        return "node.commands"; //$NON-NLS-1$
    }

}