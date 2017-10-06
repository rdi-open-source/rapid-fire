/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.subsystem.RapidFireSubSystemFactory;

import com.ibm.etools.systems.core.ui.SystemMenuManager;
import com.ibm.etools.systems.core.ui.view.AbstractSystemViewAdapter;
import com.ibm.etools.systems.core.ui.view.ISystemRemoteElementAdapter;

public abstract class AbstractResourceAdapter extends AbstractSystemViewAdapter implements ISystemRemoteElementAdapter {

    public void addActions(SystemMenuManager menu, IStructuredSelection selection, Shell parent, String menuGroup) {
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object arg0) {
        return null;
    }

    @Override
    public boolean handleDoubleClick(Object element) {
        return false;
    }

    public String getText(Object element) {
        return Messages.EMPTY;
    }

    public String getAbsoluteName(Object element) {
        return null;
    }

    @Override
    public String getType(Object paramObject) {
        return null;
    }

    public Object getParent(Object element) {
        return null; // not really used, which is good because it is ambiguous
    }

    public boolean hasChildren(IAdaptable element) {
        return false;
    }

    @Override
    public Object[] getChildren(Object o) {
        return null;
    }

    protected IPropertyDescriptor[] internalGetPropertyDescriptors() {
        return null;
    }

    protected Object internalGetPropertyValue(Object key) {
        return null;
    }

    public String getSubSystemFactoryId(Object element) {
        return RapidFireSubSystemFactory.ID;
    }

    // --------------------------------------
    // ISystemRemoteElementAdapter methods...
    // --------------------------------------

    public String getAbsoluteParentName(Object element) {
        // not really applicable as we have no unique hierarchy
        return "root"; //$NON-NLS-1$
    }

    public String getRemoteTypeCategory(Object element) {
        // Course grained. Same for all our remote resources.
        return "rapid fire"; //$NON-NLS-1$; 
    }

    public String getRemoteType(Object element) {
        // Fine grained. Unique to this resource type.
        return getClass().getSimpleName();
    }

    public String getRemoteSubType(Object element) {
        // Very fine grained. We don't use it.
        return null;
    }

    public boolean refreshRemoteObject(Object oldElement, Object newElement) {
        return false;
    }

    public Object getRemoteParent(Object element, IProgressMonitor monitor) throws Exception {
        return null; // maybe this would be a Project or Roster object, or leave
                     // as null if this is the root
    }

    public String[] getRemoteParentNamesInUse(Object element, IProgressMonitor monitor) throws Exception {
        // Return list of all parent names
        return null;
    }

    /*
     * Start of RDi/WDSCi specific methods.
     */

    public String getSubSystemConfigurationId(Object element) {
        // as declared in extension in plugin.xml
        return RapidFireSubSystemFactory.ID;
    }
}
