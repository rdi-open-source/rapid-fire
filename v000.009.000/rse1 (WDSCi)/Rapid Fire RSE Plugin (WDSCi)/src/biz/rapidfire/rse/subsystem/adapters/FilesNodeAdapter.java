/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.adapters;

import java.util.Arrays;
import java.util.Vector;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.exceptions.AutoReconnectErrorException;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.subsystem.RapidFireFilter;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.actions.file.NewFilesNodePopupMenuAction;
import biz.rapidfire.rse.subsystem.resources.FilesNode;

import com.ibm.etools.systems.core.ui.SystemMenuManager;

public class FilesNodeAdapter extends AbstractNodeAdapter<FilesNode> {

    @Override
    public final boolean hasChildren(Object element) {
        return true;
    }

    @Override
    public String getText(Object element) {
        return Messages.NodeText_Files;
    }

    @Override
    protected String getAbsoluteNamePrefix() {
        return "node.files."; //$NON-NLS-1$
    }

    @Override
    public void addActions(SystemMenuManager menuManager, IStructuredSelection selection, Shell shell, String paramString) {
        menuManager.add(null, new NewFilesNodePopupMenuAction(shell));
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object element) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_FILE);
    }

    @Override
    public Object[] getChildren(Object element) {

        try {

            FilesNode filesNode = (FilesNode)element;
            IRapidFireJobResource jobResource = filesNode.getParentResource();

            IRapidFireFileResource[] files = jobResource.getParentSubSystem().getFiles(jobResource, getShell());

            RapidFireFilter filter = jobResource.getFilter();
            Vector<IRapidFireResource> filteredFiles = new Vector<IRapidFireResource>();
            for (IRapidFireFileResource file : files) {
                file.setParentNode(filesNode);
                if (filter.isShowLogicalFiles() || file.isPhysicalFile()) {
                    filteredFiles.addElement(file);
                }
            }

            files = filteredFiles.toArray(new IRapidFireFileResource[filteredFiles.size()]);

            Arrays.sort(files);

            return files;

        } catch (AutoReconnectErrorException e) {
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could resolve filter string and load files ***", e); //$NON-NLS-1$
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        }

        return new Object[0];
    }

    @Override
    public String getType(Object element) {
        return Messages.NodeType_Files;
    }

    @Override
    public String getRemoteType(Object element) {
        return "node.files"; //$NON-NLS-1$
    }

}