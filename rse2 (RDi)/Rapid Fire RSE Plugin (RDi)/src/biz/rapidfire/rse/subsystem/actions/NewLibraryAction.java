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
import org.eclipse.rse.core.subsystems.SubSystemHelpers;
import org.eclipse.rse.ui.actions.SystemBaseAction;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.job.NewJobHandler;
import biz.rapidfire.core.subsystem.RapidFireFilter;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.RapidFireJobResource;

public class NewLibraryAction extends SystemBaseAction {

    public NewLibraryAction(Shell shell) {
        super(Messages.ActionLabel_New_Library, Messages.ActionTooltip_New_Library, shell);

        setImageDescriptor(RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_NEW_LIBRARY));
        setContextMenuGroup("group.new");
    }

    @Override
    public void run() {

        try {

            Object element = getFirstSelection();
            ISystemFilterReference filterReference = (ISystemFilterReference)element;
            ISystemFilter systemFilter = filterReference.getReferencedFilter();

            if (systemFilter.getFilterStringCount() == 1) {
                RapidFireFilter filter = new RapidFireFilter(systemFilter.getFilterStrings()[0]);

                RapidFireJobResource job = RapidFireJobResource.createEmptyInstance(filter.getDataLibrary());
                job.setSubSystem(SubSystemHelpers.getParentSubSystem(filterReference.getParentSystemFilterReferencePool()));

                NewJobHandler handler = new NewJobHandler();
                IStructuredSelection selection = new StructuredSelection(job);
                handler.executeWithSelection(selection);
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not execute 'New Library' handler ***", e); //$NON-NLS-1$
        }

    }
}
