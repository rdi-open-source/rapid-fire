/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.actions.job;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.job.NewJobHandler;
import biz.rapidfire.core.subsystem.RapidFireFilter;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.RapidFireJobResource;

import com.ibm.etools.systems.core.ui.actions.SystemBaseAction;
import com.ibm.etools.systems.filters.SystemFilter;
import com.ibm.etools.systems.filters.SystemFilterReference;
import com.ibm.etools.systems.subsystems.SubSystemHelpers;

public class NewJobFilterPopupMenuAction extends SystemBaseAction {

    public NewJobFilterPopupMenuAction(Shell shell) {
        super(Messages.ActionLabel_New_Job, Messages.ActionTooltip_New_Job, shell);

        setImageDescriptor(RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_NEW_JOB));
        setContextMenuGroup("group.new");
    }

    @Override
    public void run() {

        try {

            Object element = getFirstSelection();
            SystemFilterReference filterReference = (SystemFilterReference)element;
            SystemFilter systemFilter = filterReference.getReferencedFilter();

            if (systemFilter.getFilterStringCount() == 1) {
                RapidFireFilter filter = new RapidFireFilter(systemFilter.getFilterStrings()[0]);

                RapidFireJobResource job = RapidFireJobResource.createEmptyInstance(filter.getDataLibrary());
                job.setSubSystem(SubSystemHelpers.getParentSubSystem(filterReference.getParentSystemFilterReferencePool()));

                NewJobHandler handler = new NewJobHandler();
                IStructuredSelection selection = new StructuredSelection(job);
                handler.executeWithSelection(selection);
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not execute 'New Job' handler ***", e); //$NON-NLS-1$
        }

    }
}
