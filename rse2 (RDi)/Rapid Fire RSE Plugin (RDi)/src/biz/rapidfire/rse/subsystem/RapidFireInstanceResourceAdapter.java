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

package biz.rapidfire.rse.subsystem;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.rse.ui.view.AbstractSystemViewAdapter;
import org.eclipse.rse.ui.view.ISystemRemoteElementAdapter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import biz.rapidfire.core.subsystem.IRapidFireInstanceResource;
import biz.rapidfire.rse.RapidFireRSEPlugin;

public class RapidFireInstanceResourceAdapter extends AbstractSystemViewAdapter implements ISystemRemoteElementAdapter {

    private static final String LIBRARY = "LIBRARY";
    private static final String CONNECTION = "CONNECTION";

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
        RapidFireSubSystem subSystem = (RapidFireSubSystem)resource.getSubSystem();
        String name = "RapidFireInstance." + subSystem.getHostAliasName() + "." + resource.getLibrary();

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

    @Override
    public Object[] getChildren(IAdaptable paramIAdaptable, IProgressMonitor paramIProgressMonitor) {
        return new Object[0];
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

        IRapidFireInstanceResource rapidFireResource = (IRapidFireInstanceResource)propertySourceInput;

        if (propKey.equals(CONNECTION)) {
            return rapidFireResource.getConnectionName();
        } else if (propKey.equals(LIBRARY)) {
            return rapidFireResource.getLibrary();
        }

        return null;
    }

    public String getAbsoluteParentName(Object element) {
        return "root"; //$NON-NLS-1$
    }

    public String getSubSystemFactoryId(Object element) {
        return RapidFireSubSystemFactory.ID;
    }

    public String getRemoteTypeCategory(Object element) {
        return "rapid fire library";
    }

    public String getRemoteType(Object element) {
        return "rapid fire library";
    }

    public String getRemoteSubType(Object arg0) {
        return null;
    }

    public boolean refreshRemoteObject(Object oldElement, Object newElement) {
        return false;
    }

    public Object getRemoteParent(Object paramObject, IProgressMonitor paramIProgressMonitor) throws Exception {
        return null;
    }

    public String[] getRemoteParentNamesInUse(Object paramObject, IProgressMonitor paramIProgressMonitor) throws Exception {
        return null;
    }

    /*
     * Start of RDi/WDSCi specific methods.
     */

    public String getSubSystemConfigurationId(Object arg0) {
        return null;
    }
}