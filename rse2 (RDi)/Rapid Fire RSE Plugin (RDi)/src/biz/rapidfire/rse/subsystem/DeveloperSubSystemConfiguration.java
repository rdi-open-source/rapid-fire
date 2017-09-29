/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import java.util.Vector;

import org.eclipse.rse.core.filters.ISystemFilter;
import org.eclipse.rse.core.filters.ISystemFilterPool;
import org.eclipse.rse.core.filters.ISystemFilterPoolManager;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.subsystems.IConnectorService;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.core.subsystems.SubSystemConfiguration;

/**
 * This is our subsystem factory, which creates instances of our subsystems, and
 * supplies the subsystem and filter actions to their popup menus.
 */
public class DeveloperSubSystemConfiguration extends SubSystemConfiguration {

    /**
     * Constructor for DeveloperSubSystemConfiguration.
     */
    public DeveloperSubSystemConfiguration() {
        super();
    }

    /**
     * Create an instance of our subsystem.
     */
    public ISubSystem createSubSystemInternal(IHost conn) {
        return new DeveloperSubSystem(conn, getConnectorService(conn));
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.core.subsystems.ISubSystemConfiguration#getConnectorService
     * (org.eclipse.rse.core.model.IHost)
     */
    public IConnectorService getConnectorService(IHost host) {
        return DeveloperConnectorServiceManager.getInstance().getConnectorService(host, IRapidFireSubSystem.class);
    }

    /**
     * Intercept of parent method that creates an initial default filter pool.
     * We intercept so that we can create an initial filter in that pool, which
     * will list all teams.
     */
    protected ISystemFilterPool createDefaultFilterPool(ISystemFilterPoolManager mgr) {
        ISystemFilterPool defaultPool = null;
        try {
            // true=>is, deletable by user
            defaultPool = mgr.createSystemFilterPool(getDefaultFilterPoolName(mgr.getName(), getId()), true);
            Vector strings = new Vector();
            strings.add("*");
            mgr.createSystemFilter(defaultPool, "All instances", strings);
        } catch (Exception exc) {
        }
        return defaultPool;
    }

    /**
     * Intercept of parent method so we can supply our own value shown in the
     * property sheet for the "type" property when a filter is selected within
     * our subsystem. Requires this line in rseSamplesResources.properties:
     * property.type.teamfilter=Team filter
     */
    public String getTranslatedFilterTypeProperty(ISystemFilter selectedFilter) {
        return "property.type.teamfilter";
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.rse.core.subsystems.SubSystemConfiguration#supportsUserId()
     */
    public boolean supportsUserId() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.rse.core.subsystems.SubSystemConfiguration#
     * supportsServerLaunchProperties(org.eclipse.rse.core.model.IHost)
     */
    public boolean supportsServerLaunchProperties(IHost host) {
        return false;
    }

}