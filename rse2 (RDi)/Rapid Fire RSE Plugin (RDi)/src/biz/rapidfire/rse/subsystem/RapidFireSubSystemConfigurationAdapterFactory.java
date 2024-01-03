/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Rapid Fire Project Team - Maintenance and enhancements
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.rse.ui.subsystems.ISubSystemConfigurationAdapter;

public class RapidFireSubSystemConfigurationAdapterFactory implements IAdapterFactory {

    private ISubSystemConfigurationAdapter subSystemConfigurationAdapter = new RapidFireSubSystemConfigurationAdapter();

    public RapidFireSubSystemConfigurationAdapterFactory() {
        super();
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {

        if ((adaptableObject instanceof RapidFireSubSystemFactory)) {
            return subSystemConfigurationAdapter;
        }

        return null;
    }

    public Class<?>[] getAdapterList() {
        return new Class<?>[] { ISubSystemConfigurationAdapter.class };
    }

    public void registerWithManager(IAdapterManager manager) {

        manager.registerAdapters(this, RapidFireSubSystemFactory.class);
    }
}
