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
import org.eclipse.rse.core.filters.ISystemFilter;
import org.eclipse.rse.core.filters.ISystemFilterReference;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.core.subsystems.SubSystemHelpers;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.job.NewJobHandler;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.RapidFireFilter;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.RapidFireJobResource;

public class NewJobAction extends AbstractNewNodePopupMenuAction<ISystemFilterReference, IRapidFireJobResource> {

    public NewJobAction(Shell shell) {
        super(Messages.ActionLabel_New_Job, Messages.ActionTooltip_New_Job, shell, new NewJobHandler());

        setImageDescriptor(RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_NEW_JOB));
        setContextMenuGroup("group.new");
    }

    @Override
    public void run() {

        try {

            Object object = getFirstSelection();

            if (object instanceof ISystemFilterReference) {

                ISystemFilterReference filterReference = (ISystemFilterReference)object;
                ISystemFilter systemFilter = filterReference.getReferencedFilter();

                if (systemFilter.getFilterStringCount() >= 1) {
                    RapidFireFilter filter = new RapidFireFilter(systemFilter.getFilterStrings()[0]);

                    ISubSystem subSystem = SubSystemHelpers.getParentSubSystem(filterReference.getParentSystemFilterReferencePool());
                    RapidFireJobResource job = RapidFireJobResource.createEmptyInstance((IRapidFireSubSystem)subSystem, filter.getDataLibrary());
                    job.setFilter(filter);

                    NewJobHandler handler = new NewJobHandler();
                    IStructuredSelection selection = new StructuredSelection(job);
                    handler.executeWithSelection(selection);
                }
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not execute 'New Job' handler ***", e); //$NON-NLS-1$
        }

    }

    @Override
    protected IRapidFireJobResource createNewResource(ISystemFilterReference filterReference) {
        // return RapidFireJobResource.createEmptyInstance(node.getJob());

        ISystemFilter systemFilter = filterReference.getReferencedFilter();
        RapidFireFilter filter = new RapidFireFilter(systemFilter.getFilterStrings()[0]);
        IRapidFireSubSystem subSystem = (IRapidFireSubSystem)SubSystemHelpers
            .getParentSubSystem(filterReference.getParentSystemFilterReferencePool());

        return RapidFireJobResource.createEmptyInstance(subSystem, filter.getDataLibrary());
    }

    @Override
    protected ISystemFilterReference getResource(Object object) {

        if (object instanceof ISystemFilterReference) {

            ISystemFilterReference filterReference = (ISystemFilterReference)object;
            ISystemFilter systemFilter = filterReference.getReferencedFilter();

            if (systemFilter.getFilterStringCount() >= 1) {
                ISubSystem subSystem = SubSystemHelpers.getParentSubSystem(filterReference.getParentSystemFilterReferencePool());

                if (subSystem instanceof IRapidFireSubSystem) {
                    return filterReference;
                }
            }
        }

        return null;
    }
}
