/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.rse.ui.view.AbstractSystemViewAdapter;
import org.eclipse.rse.ui.view.ISystemRemoteElementAdapter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public abstract class AbstractModelObject extends AbstractSystemViewAdapter implements ISystemRemoteElementAdapter {

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.ui.view.AbstractSystemViewAdapter#addActions(org.eclipse
     * .rse.ui.SystemMenuManager,
     * org.eclipse.jface.viewers.IStructuredSelection,
     * org.eclipse.swt.widgets.Shell, java.lang.String)
     */
    public void addActions(SystemMenuManager menu, IStructuredSelection selection, Shell parent, String menuGroup) {
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.ui.view.AbstractSystemViewAdapter#getParent(java.lang
     * .Object)
     */
    public Object getParent(Object element) {
        return null; // not really used, which is good because it is ambiguous
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.ui.view.AbstractSystemViewAdapter#hasChildren(java.lang
     * .Object)
     */
    public boolean hasChildren(IAdaptable element) {
        return false;
    }

    @Override
    public Object[] getChildren(IAdaptable arg0, IProgressMonitor arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.rse.ui.view.AbstractSystemViewAdapter#internalGetPropertyDescriptors()
     */
    protected IPropertyDescriptor[] internalGetPropertyDescriptors() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.ui.view.AbstractSystemViewAdapter#internalGetPropertyValue
     * (java.lang.Object)
     */
    protected Object internalGetPropertyValue(Object key) {
        return null;
    }

    /**
     * Intercept of parent method to indicate these objects can be renamed using
     * the RSE-supplied rename action.
     */
    public boolean canRename(Object element) {
        return false;
    }

    // --------------------------------------
    // ISystemRemoteElementAdapter methods...
    // --------------------------------------

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.ui.view.ISystemRemoteElementAdapter#getAbsoluteParentName
     * (java.lang.Object)
     */
    public String getAbsoluteParentName(Object element) {
        return "root"; // not really applicable as we have no unique hierarchy
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.rse.ui.view.ISystemRemoteElementAdapter#
     * getSubSystemConfigurationId(java.lang.Object)
     */
    public String getSubSystemConfigurationId(Object element) {
        return "biz.rapidfire.rse.subsystem.factory"; // as declared in
                                                      // extension in plugin.xml
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.ui.view.ISystemRemoteElementAdapter#getRemoteSubType(
     * java.lang.Object)
     */
    public String getRemoteSubType(Object element) {
        return null; // Very fine grained. We don't use it.
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.ui.view.ISystemRemoteElementAdapter#getRemoteParent(org
     * .eclipse.swt.widgets.Shell, java.lang.Object)
     */
    public Object getRemoteParent(Object element, IProgressMonitor monitor) throws Exception {
        return null; // maybe this would be a Project or Roster object, or leave
                     // as null if this is the root
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.ui.view.ISystemRemoteElementAdapter#getRemoteParentNamesInUse
     * (org.eclipse.swt.widgets.Shell, java.lang.Object)
     */
    public String[] getRemoteParentNamesInUse(Object element, IProgressMonitor monitor) throws Exception {
        return new String[0]; // Return list of all team names
    }
}
