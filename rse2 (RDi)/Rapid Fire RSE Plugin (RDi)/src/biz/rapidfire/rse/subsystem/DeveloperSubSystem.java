/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.subsystems.IConnectorService;
import org.eclipse.rse.core.subsystems.SubSystem;
import org.eclipse.rse.services.clientserver.messages.SystemMessageException;

/**
 * This is our subsystem, which manages the remote connection and resources for
 * a particular system connection object.
 */
public class DeveloperSubSystem extends SubSystem implements IRapidFireSubSystem {
    // private TeamResource[] jobs; // faked-out master list of teams
    private Vector devVector = new Vector(); // faked-out master list of
                                             // developers
    private static int employeeId = 123456; // employee Id factory

    /**
     * @param host
     * @param connectorService
     */
    public DeveloperSubSystem(IHost host, IConnectorService connectorService) {
        super(host, connectorService);
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.core.subsystems.SubSystem#initializeSubSystem(org.eclipse
     * .core.runtime.IProgressMonitor)
     */
    public void initializeSubSystem(IProgressMonitor monitor) throws SystemMessageException {
        super.initializeSubSystem(monitor);

        if (Platform.getAdapterManager().hasAdapter(this, "biz.rapidfire.rse.subsystem.RapidFireAdapter")) {
            Platform.getAdapterManager().loadAdapter(this, "biz.rapidfire.rse.subsystem.RapidFireAdapter");
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.core.subsystems.ISubSystem#uninitializeSubSystem(org.
     * eclipse.core.runtime.IProgressMonitor)
     */
    public void uninitializeSubSystem(IProgressMonitor monitor) {
    }

    /**
     * For drag and drop, and clipboard support of remote objects. Return the
     * remote object within the subsystem that corresponds to the specified
     * unique ID. Because each subsystem maintains it's own objects, it's the
     * responsability of the subsystem to determine how an ID (or key) for a
     * given object maps to the real object. By default this returns null.
     */
    public Object getObjectWithAbsoluteName(String key) {
        // Functional opposite of getAbsoluteName(Object) in our resource
        // adapters

        //        if (key.startsWith("Team_")) //$NON-NLS-1$
        // {
        // String teamName = key.substring(5);
        // TeamResource[] allTeams = getAllTeams();
        // for (int idx = 0; idx < allTeams.length; idx++)
        // if (allTeams[idx].getName().equals(teamName)) return allTeams[idx];
        //        } else if (key.startsWith("Devr_")) //$NON-NLS-1$
        // {
        // String devrId = key.substring(5);
        // DeveloperResource[] devrs = getAllDevelopers();
        // for (int idx = 0; idx < devrs.length; idx++)
        // if (devrs[idx].getId().equals(devrId)) return devrs[idx];
        // }
        return null;
    }

    /**
     * When a filter is expanded, this is called for each filter string in the
     * filter. Using the criteria of the filter string, it must return objects
     * representing remote resources. For us, this will be an array of
     * TeamResource objects.
     * 
     * @param monitor - the progress monitor in effect while this operation
     *        performs
     * @param filterString - one of the filter strings from the expanded filter.
     */
    protected Object[] internalResolveFilterString(String filterString, IProgressMonitor monitor) throws java.lang.reflect.InvocationTargetException,
        java.lang.InterruptedException {

        // Fake it out for now and return dummy list.
        // In reality, this would communicate with remote server-side code/data.
        // Job[] allTeams = getAllTeams();

        // Now, subset master list, based on filter string...
        // NamePatternMatcher subsetter = new NamePatternMatcher(filterString);
        // Vector v = new Vector();
        // for (int idx = 0; idx < allTeams.length; idx++) {
        // if (subsetter.matches(allTeams[idx].getName()))
        // v.addElement(allTeams[idx]);
        // }
        // TeamResource[] teams = new TeamResource[v.size()];
        // for (int idx = 0; idx < v.size(); idx++)
        // teams[idx] = (TeamResource)v.elementAt(idx);
        // return teams;

        return null;
    }

    /**
     * When a remote resource is expanded, this is called to return the children
     * of the resource, if the resource's adapter states the resource object is
     * expandable. For us, it is a Team resource that was expanded, and an array
     * of Developer resources will be returned.
     * 
     * @param monitor - the progress monitor in effect while this operation
     *        performs
     * @param parent - the parent resource object being expanded
     * @param filterString - typically defaults to "*". In future additional
     *        user-specific quick-filters may be supported.
     */
    protected Object[] internalResolveFilterString(Object parent, String filterString, IProgressMonitor monitor)
        throws java.lang.reflect.InvocationTargetException, java.lang.InterruptedException {
        // typically we ignore the filter string as it is always "*"
        // until support is added for "quick filters" the user can
        // specify/select
        // at the time they expand a remote resource.

        // TeamResource team = (TeamResource)parent;
        // return team.getDevelopers();
        return null;
    }

    // ------------------
    // Our own methods...
    // ------------------

    /**
     * Get the list of all teams. Normally this would involve a trip the server,
     * but we fake it out and return a hard-coded local list.
     */
    // public Job[] getAllTeams() {
    // if (teams == null) teams = createTeams("Team ", 4);
    // return teams;
    // }

    /**
     * Get the list of all developers. Normally this would involve a trip the
     * server, but we fake it out and return a hard-coded local list.
     */
    // public DeveloperResource[] getAllDevelopers() {
    // DeveloperResource[] allDevrs = new DeveloperResource[devVector.size()];
    // for (int idx = 0; idx < allDevrs.length; idx++)
    // allDevrs[idx] = (DeveloperResource)devVector.elementAt(idx);
    // return allDevrs;
    // }

    /*
     * Create and return a dummy set of teams
     */
    // private TeamResource[] createTeams(String prefix, int howMany) {
    // TeamResource[] teams = new TeamResource[howMany];
    // for (int idx = 0; idx < teams.length; idx++) {
    // teams[idx] = new TeamResource(this);
    // teams[idx].setName(prefix + (idx + 1));
    // teams[idx].setDevelopers(createDevelopers(teams[idx].getName() +
    // " developer", idx + 1));
    // }
    // return teams;
    // }

    /*
     * Create and return a dummy set of developers
     */
    // private DeveloperResource[] createDevelopers(String prefix, int nbr) {
    // DeveloperResource[] devrs = new DeveloperResource[nbr];
    // for (int idx = 0; idx < devrs.length; idx++) {
    // devrs[idx] = new DeveloperResource(this);
    // devrs[idx].setName(prefix + (idx + 1));
    // devrs[idx].setId(Integer.toString(employeeId++));
    // devrs[idx].setDeptNbr(Integer.toString((idx + 1) * 100));
    // devVector.add(devrs[idx]); // update master list
    // }
    // return devrs;
    // }

}