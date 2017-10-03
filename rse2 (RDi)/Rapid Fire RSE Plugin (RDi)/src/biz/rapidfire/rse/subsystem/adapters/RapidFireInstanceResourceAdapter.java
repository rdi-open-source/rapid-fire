/*******************************************************************************
 * Copyright (c) 2005 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *     iSphere Project Owners - Maintenance and enhancements
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.adapters;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.rse.ui.view.ISystemRemoteElementAdapter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import biz.rapidfire.core.model.IRapidFireInstanceResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.IJobsDAO;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.model.RapidFireInstanceResource;
import biz.rapidfire.rse.model.dao.JobsDAO;
import biz.rapidfire.rse.subsystem.RapidFireSubSystem;

public class RapidFireInstanceResourceAdapter extends AbstractResourceAdapter implements ISystemRemoteElementAdapter {

    private static final String LIBRARY = "LIBRARY"; //$NON-NLS-1$
    private static final String CONNECTION = "CONNECTION"; //$NON-NLS-1$

    public RapidFireInstanceResourceAdapter() {
        super();
    }

    @Override
    public void addActions(SystemMenuManager menu, IStructuredSelection selection, Shell parent, String menuGroup) {
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object object) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_LIBRARY);
    }

    @Override
    public boolean handleDoubleClick(Object object) {
        return false;
    }

    /**
     * Returns the name of the resource, e.g. for showing the name in the status
     * line.
     */
    public String getText(Object element) {
        return ((RapidFireInstanceResource)element).getName();
    }

    /**
     * Returns the absolute name of the resource for building hash values.
     */
    public String getAbsoluteName(Object element) {

        RapidFireInstanceResource resource = (RapidFireInstanceResource)element;
        RapidFireSubSystem parent = (RapidFireSubSystem)resource.getParent();

        String name = "RapidFireInstance." + parent.getHostAliasName() + "." + resource.getLibrary(); //$NON-NLS-1$ //$NON-NLS-1$

        return name;
    }

    /**
     * Returns the type of the resource. Used for qualifying the name of the
     * resource, when displayed on the status line.
     */
    @Override
    public String getType(Object element) {
        return "Rapid Fire Library";
    }

    @Override
    public Object getParent(Object element) {
        return ((RapidFireInstanceResource)element).getParent();
    }

    @Override
    public boolean hasChildren(IAdaptable element) {
        return true;
    }

    @Override
    public Object[] getChildren(IAdaptable element, IProgressMonitor progressMonitor) {

        RapidFireInstanceResource resource = (RapidFireInstanceResource)element;

        try {

            IJobsDAO dao = new JobsDAO(resource.getConnectionName());
            List<IRapidFireJobResource> jobs = dao.load(resource);

            return jobs.toArray(new IRapidFireJobResource[jobs.size()]);

        } catch (Throwable e) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, e.getClass().getSimpleName() + ":\n\n" + e.getLocalizedMessage());
        }

        return new IRapidFireJobResource[0];
    }

    @Override
    protected IPropertyDescriptor[] internalGetPropertyDescriptors() {

        PropertyDescriptor[] ourPDs = new PropertyDescriptor[2];
        ourPDs[0] = new PropertyDescriptor(CONNECTION, "Connection");
        ourPDs[0].setDescription("Specifies the connection name.");
        ourPDs[1] = new PropertyDescriptor(LIBRARY, "Library");
        ourPDs[1].setDescription("Specifies the name of the Rapid Fire library.");

        return ourPDs;
    }

    @Override
    public Object internalGetPropertyValue(Object propKey) {

        IRapidFireInstanceResource resource = (IRapidFireInstanceResource)propertySourceInput;

        if (propKey.equals(CONNECTION)) {
            return resource.getConnectionName();
        } else if (propKey.equals(LIBRARY)) {
            return resource.getLibrary();
        }

        return null;
    }

    @Override
    public boolean showDelete(Object element) {
        return true;
    }

    @Override
    public boolean canDelete(Object element) {
        return true;
    }

    @Override
    public boolean doDelete(Shell shell, Object element, IProgressMonitor monitor) {

        RapidFireInstanceResource resource = (RapidFireInstanceResource)element;
        RapidFireSubSystem subSystem = (RapidFireSubSystem)resource.getSubSystem();
        String removedLibrary = subSystem.getSubSystemAttributes().removeRapidFireInstance(resource);
        if (removedLibrary == null) {
            return false;
        }

        return true;
    }
}