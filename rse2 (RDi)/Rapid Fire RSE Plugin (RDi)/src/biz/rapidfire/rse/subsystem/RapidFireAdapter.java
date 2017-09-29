/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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

public class RapidFireAdapter extends AbstractSystemViewAdapter implements ISystemRemoteElementAdapter {

    public RapidFireAdapter() {
        return;
    }

    public String getRemoteSubType(Object arg0) {
        return null; // Very fine grained. We don't use it.
    }

    public String getRemoteType(Object arg0) {
        return "team"; // Fine grained. Unique to this resource type.
    }

    public String getRemoteTypeCategory(Object arg0) {
        // TODO Auto-generated method stub
        return "developers"; // Course grained. Same for all our remote
                             // resources.
    }

    public String getSubSystemConfigurationId(Object arg0) {
        return "biz.rapidfire.rse.subsystem.factory"; // as declared in
                                                      // extension in plugin.xml
    }

    public String getText(Object arg0) {
        // TODO Auto-generated method stub
        // ((TeamResource)element).getName();
        return null;
    }

    public String getAbsoluteName(Object arg0) {
        // TODO Auto-generated method stub
        // TeamResource team = (TeamResource)object;
        // return "Team_"+team.getName();
        return null;
    }

    public String getAbsoluteParentName(Object arg0) {
        return "root"; // not really applicable as we have no unique hierarchy
    }

    public Object getRemoteParent(Object arg0, IProgressMonitor arg1) throws Exception {
        // TODO Auto-generated method stub
        return null; // maybe this would be a Project or Roster object, or leave
                     // as null if this is the root
    }

    public String[] getRemoteParentNamesInUse(Object arg0, IProgressMonitor arg1) throws Exception {
        // TODO Auto-generated method stub
        // DeveloperSubSystem ourSS = (DeveloperSubSystem)getSubSystem(element);
        // TeamResource[] allTeams = ourSS.getAllTeams();
        // String[] allNames = new String[allTeams.length];
        // for (int idx = 0; idx < allTeams.length; idx++)
        // allNames[idx] = allTeams[idx].getName();
        // return allNames; // Return list of all team names }
        return null;
    }

    public boolean refreshRemoteObject(Object arg0, Object arg1) {
        // TODO Auto-generated method stub
        return false; // If developer objects held references to their team
                      // names, we'd have to return true
    }

    @Override
    public void addActions(SystemMenuManager arg0, IStructuredSelection arg1, Shell arg2, String arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object[] getChildren(IAdaptable arg0, IProgressMonitor arg1) {
        // TODO Auto-generated method stub
        // return ((TeamResource)element).getDevelopers();
        return null;
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object arg0) {
        // TODO Auto-generated method stub
        // RSESamplesPlugin.getDefault().getImageDescriptor("ICON_ID_TEAM");
        return null;
    }

    @Override
    public Object getParent(Object arg0) {
        return null; // not really used, which is good because it is ambiguous
    }

    @Override
    public String getType(Object arg0) {
        // TODO Auto-generated method stub
        // return
        // RSESamplesPlugin.getResourceString("property.team_resource.type");
        return null;
    }

    @Override
    public boolean hasChildren(IAdaptable arg0) {
        return true;
    }

    @Override
    protected IPropertyDescriptor[] internalGetPropertyDescriptors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Object internalGetPropertyValue(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canRename(Object element) {
        return false;
    }

    @Override
    public boolean doRename(Shell shell, Object element, String name, IProgressMonitor monitor) throws Exception {
        return false;
    }

}
