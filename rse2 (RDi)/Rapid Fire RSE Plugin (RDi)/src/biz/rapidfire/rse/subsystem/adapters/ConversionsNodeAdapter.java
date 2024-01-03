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
import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.actions.NewConversionAction;
import biz.rapidfire.rse.subsystem.resources.ConversionsNode;

public class ConversionsNodeAdapter extends AbstractNodeAdapter<ConversionsNode> {

    @Override
    public final boolean hasChildren(IAdaptable element) {
        return true;
    }

    @Override
    public String getText(Object element) {
        return Messages.NodeText_Conversions;
    }

    @Override
    protected String getAbsoluteNamePrefix() {
        return "node.conversions.";
    }

    @Override
    public void addActions(SystemMenuManager menuManager, IStructuredSelection selection, Shell shell, String menuGroup) {
        menuManager.add(null, new NewConversionAction(shell));
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object element) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_CONVERSION);
    }

    @Override
    public Object[] getChildren(IAdaptable element, IProgressMonitor progressMonitor) {

        try {

            ConversionsNode conversionsNode = (ConversionsNode)element;
            IRapidFireFileResource fileResource = conversionsNode.getParentResource();

            IRapidFireConversionResource[] conversions = fileResource.getParentSubSystem().getConversions(fileResource, getShell());
            for (IRapidFireConversionResource conversion : conversions) {
                conversion.setParentNode(conversionsNode);
            }

            Arrays.sort(conversions);

            return conversions;

        } catch (AutoReconnectErrorException e) {
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could resolve filter string and load conversions ***", e); //$NON-NLS-1$
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        }

        return new Object[0];
    }

    @Override
    public String getType(Object element) {
        return Messages.NodeType_Conversions;
    }

    @Override
    public String getRemoteType(Object element) {
        return "node.conversions"; //$NON-NLS-1$
    }

}