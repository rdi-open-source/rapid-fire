/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.adapters;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.CreateJobHandler;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.CreateJobNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireJobResource;

public class CreateJobNodeAdapter extends AbstractNodeAdapter {

    @Override
    public String getText(Object element) {
        return Messages.NodeText_Create_job;
    }

    @Override
    public String getAbsoluteName(Object element) {
        return "node.createJob"; //$NON-NLS-1$
    }

    @Override
    public void addActions(SystemMenuManager menuManager, IStructuredSelection selection, Shell shell, String paramString) {

    }

    @Override
    public ImageDescriptor getImageDescriptor(Object element) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_CREATE_JOB);
    }

    @Override
    public Object[] getChildren(IAdaptable element, IProgressMonitor progressMonitor) {
        return new Object[0];
    }

    @Override
    public String getType(Object element) {
        return Messages.NodeType_Create_job;
    }

    @Override
    public String getRemoteType(Object element) {
        return "node.createJob"; //$NON-NLS-1$
    }

    @Override
    public boolean handleDoubleClick(Object element) {

        if (element instanceof CreateJobNode) {

            try {

                CreateJobNode createJobNode = (CreateJobNode)element;

                RapidFireJobResource job = RapidFireJobResource.createEmptyInstance(createJobNode.getDataLibrary());
                job.setSubSystem((ISubSystem)createJobNode.getParentSubSystem());

                CreateJobHandler handler = new CreateJobHandler();
                IStructuredSelection selection = new StructuredSelection(job);
                handler.executeWithSelection(selection);

            } catch (Exception e) {
                RapidFireCorePlugin.logError("*** Could not execute create job handler ***", e); //$NON-NLS-1$
            }

        }

        return false;
    }

}