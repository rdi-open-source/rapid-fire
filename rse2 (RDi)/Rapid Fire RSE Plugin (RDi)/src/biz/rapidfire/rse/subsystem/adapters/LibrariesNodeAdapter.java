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
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.actions.NewLibraryAction;
import biz.rapidfire.rse.subsystem.resources.LibrariesNode;

public class LibrariesNodeAdapter extends AbstractNodeAdapter<LibrariesNode> {

    @Override
    public final boolean hasChildren(IAdaptable element) {
        return true;
    }

    @Override
    public String getText(Object element) {
        return Messages.NodeText_Libraries;
    }

    @Override
    protected String getAbsoluteNamePrefix() {
        return "node.libraries.";
    }

    @Override
    public void addActions(SystemMenuManager menuManager, IStructuredSelection selection, Shell shell, String paramString) {
        menuManager.add(null, new NewLibraryAction(shell));
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object element) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_LIBRARY);
    }

    @Override
    public Object[] getChildren(IAdaptable element, IProgressMonitor progressMonitor) {

        try {

            LibrariesNode librariesNode = (LibrariesNode)element;
            IRapidFireJobResource jobResource = librariesNode.getParentResource();

            IRapidFireLibraryResource[] libraries = jobResource.getParentSubSystem().getLibraries(jobResource, getShell());
            for (IRapidFireLibraryResource library : libraries) {
                library.setParentNode(librariesNode);
            }

            Arrays.sort(libraries);

            return libraries;

        } catch (AutoReconnectErrorException e) {
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could resolve filter string and load libraries ***", e); //$NON-NLS-1$
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        }

        return new Object[0];
    }

    @Override
    public String getType(Object element) {
        return Messages.NodeType_Libraries;
    }

    @Override
    public String getRemoteType(Object element) {
        return "node.libraries"; //$NON-NLS-1$
    }

}