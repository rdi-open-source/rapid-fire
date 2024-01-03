/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
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

import biz.rapidfire.core.subsystem.RapidFireFilter;
import biz.rapidfire.rse.Messages;

import com.ibm.etools.iseries.subsystems.qsys.objects.QSYSObjectSubSystem;

public class RapidFireSubSystemFactory extends SubSystemConfiguration {

    public static final String ID = "biz.rapidfire.rse.subsystem.RapidFireSubSystemFactory"; //$NON-NLS-1$

    public RapidFireSubSystemFactory() {
        super();
    }

    @Override
    public ISubSystem createSubSystemInternal(IHost host) {
        RapidFireSubSystem subSystem = new RapidFireSubSystem(host, getConnectorService(host));
        return subSystem;
    }

    @Override
    protected void removeSubSystem(ISubSystem subSystem) {
        getSubSystems(false);
        super.removeSubSystem(subSystem);
    }

    @Override
    public String getTranslatedFilterTypeProperty(ISystemFilter selectedFilter) {
        return Messages.Rapid_Fire_filter_type;
    }

    @Override
    protected ISystemFilterPool createDefaultFilterPool(ISystemFilterPoolManager mgr) {

        ISystemFilterPool defaultPool = super.createDefaultFilterPool(mgr);
        Vector<String> strings = new Vector<String>();

        RapidFireFilter instanceFilter = RapidFireFilter.getDefaultFilter();
        strings.add(instanceFilter.getFilterString());
        try {
            ISystemFilter filter = mgr.createSystemFilter(defaultPool, Messages.My_Rapid_Fire, strings);
            filter.setType(Messages.Rapid_Fire_filter_type);
        } catch (Exception exc) {
        }
        return defaultPool;
    }

    @Override
    public boolean supportsFilters() {
        return true;
    }

    @Override
    public boolean supportsNestedFilters() {
        return false;
    }

    /*
     * Start of RDi/WDSCi specific methods.
     */

    @Override
    public IConnectorService getConnectorService(IHost host) {

        ISubSystem[] subSystems = host.getSubSystems();
        for (int i = 0; i < subSystems.length; i++) {
            ISubSystem subSystem = subSystems[i];
            if ((subSystem instanceof QSYSObjectSubSystem)) {
                return subSystem.getConnectorService();
            }
        }

        return null;
    }
}