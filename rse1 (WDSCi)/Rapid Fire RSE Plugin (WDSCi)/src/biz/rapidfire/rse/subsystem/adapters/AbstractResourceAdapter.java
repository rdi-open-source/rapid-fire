/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.adapters;

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

    @Override
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

    @Override
    public String getText(Object element) {
        return Messages.EMPTY;
    }

    /**
     * Returns the absolute name of the node. The name must be unique for the
     * "Remote Systems" view.
     */
    @Override
    public abstract String getAbsoluteName(Object element);

    protected abstract String getAbsoluteNamePrefix();

    @Override
    public String getType(Object paramObject) {
        return null;
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        return false;
    }

    @Override
    public Object[] getChildren(Object o) {
        return null;
    }

    @Override
    protected IPropertyDescriptor[] internalGetPropertyDescriptors() {
        return null;
    }

    @Override
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

    /**
     * Value of the "typecategoryfilter" attribute of the
     * "com.ibm.etools.systems.core.popupMenus" extension point.
     */
    public String getRemoteTypeCategory(Object element) {
        return "rapid fire"; //$NON-NLS-1$; 
    }

    /**
     * Value of the "typefilter" attribute of the
     * "com.ibm.etools.systems.core.popupMenus" extension point.
     */
    public abstract String getRemoteType(Object element);

    /**
     * Value of the "subtypefilter" attribute of the
     * "com.ibm.etools.systems.core.popupMenus" extension point.
     */
    public final String getRemoteSubType(Object element) {
        return null;
    }

    public boolean refreshRemoteObject(Object oldElement, Object newElement) {
        return false;
    }

    public Object getRemoteParent(Object element, IProgressMonitor monitor) throws Exception {
        // maybe this would be a Project or Roster object, or leave as null if
        // this is the root
        return null;
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

    public Object getRemoteParent(Shell shell, Object element) throws Exception {
        return null;
    }

    public String[] getRemoteParentNamesInUse(Shell shell, Object element) throws Exception {
        return null;
    }

    public boolean supportsUserDefinedActions(Object shell) {
        return false;
    }
}
