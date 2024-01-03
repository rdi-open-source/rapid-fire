/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.adapters;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.rse.ui.view.AbstractSystemViewAdapter;
import org.eclipse.rse.ui.view.ISystemRemoteElementAdapter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.subsystem.RapidFireSubSystemFactory;

public abstract class AbstractResourceAdapter<R extends IRapidFireResource> extends AbstractSystemViewAdapter implements ISystemRemoteElementAdapter {

    private Set<String> forbiddenActions;

    public AbstractResourceAdapter() {

        forbiddenActions = new HashSet<String>();
        // "Refresh" action should be enabled, because it refresh the children.
        // forbiddenActions.add("org.eclipse.rse.ui.actions.SystemRefreshAction");
        forbiddenActions.add("org.eclipse.rse.internal.ui.actions.SystemCommonRenameAction");
        forbiddenActions.add("org.eclipse.rse.internal.ui.actions.SystemCommonDeleteAction");
    }

    protected Set<String> getForbiddenActions() {
        return forbiddenActions;
    }

    @Override
    public ISubSystem getSubSystem(Object element) {

        R node = (R)element;

        return (ISubSystem)node.getParentSubSystem();
    }

    @Override
    public void addActions(SystemMenuManager menu, IStructuredSelection selection, Shell parent, String menuGroup) {
    }

    /**
     * Removes unwanted actions, because the can* methods do not work for items,
     * that are selected, when the workbench starts. It starts working, when the
     * user clicks another item. But that might be too late. So we go the hard
     * way here.
     */
    @Override
    public void addCommonRemoteActions(SystemMenuManager menu, IStructuredSelection selection, Shell shell, String menuGroup) {

        IContributionItem[] items = menu.getMenuManager().getItems();
        for (int i = 0; i < items.length; i++) {
            IContributionItem item = items[i];
            if (item instanceof ActionContributionItem) {
                ActionContributionItem actionItem = (ActionContributionItem)item;
                if (getForbiddenActions().contains(actionItem.getAction().getClass().getName())) {
                    menu.getMenuManager().remove(items[i]);
                }
            }
        }

        super.addCommonRemoteActions(menu, selection, shell, menuGroup);
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

    /*
     * Disabled default action. Does not work for items, that are selected when
     * the workbench starts. It starts working as soon, as the user switched to
     * another item. Therefore we go the hard why in addCommonRemoteActions()
     * and remove the unwanted actions.
     */
    @Override
    public boolean showRefresh(Object element) {
        return true;
    }

    @Override
    public boolean showRename(Object element) {
        return false;
    }

    @Override
    public boolean showDelete(Object element) {
        return false;
    }

    /**
     * Returns the absolute name of the node. The name must be unique for the
     * "Remote Systems" view.
     */
    public String getAbsoluteName(Object element) {
        return Integer.toString(element.hashCode());
    }

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
    public boolean hasChildren(IAdaptable element) {
        return false;
    }

    @Override
    public Object[] getChildren(IAdaptable element, IProgressMonitor progressMonitor) {
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
}
