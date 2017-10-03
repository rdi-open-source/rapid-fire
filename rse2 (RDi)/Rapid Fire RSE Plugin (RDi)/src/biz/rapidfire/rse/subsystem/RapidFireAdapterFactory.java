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

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.rse.ui.view.AbstractSystemRemoteAdapterFactory;
import org.eclipse.rse.ui.view.ISystemViewElementAdapter;
import org.eclipse.ui.views.properties.IPropertySource;

import biz.rapidfire.rse.model.RapidFireInstanceResource;
import biz.rapidfire.rse.model.RapidFireJobResource;
import biz.rapidfire.rse.subsystem.adapters.RapidFireInstanceResourceAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireJobResourceAdapter;

public class RapidFireAdapterFactory extends AbstractSystemRemoteAdapterFactory implements IAdapterFactory {

    private RapidFireInstanceResourceAdapter instanceResourceAdapter = new RapidFireInstanceResourceAdapter();
    private RapidFireJobResourceAdapter jobResourceAdapter = new RapidFireJobResourceAdapter();

    public RapidFireAdapterFactory() {
        super();
    }

    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {

        ISystemViewElementAdapter adapter = null;
        if (adaptableObject instanceof RapidFireInstanceResource) {
            adapter = instanceResourceAdapter;
        } else if (adaptableObject instanceof RapidFireJobResource) {
            adapter = jobResourceAdapter;
        }

        if ((adapter != null) && (adapterType == IPropertySource.class)) {
            adapter.setPropertySourceInput(adaptableObject);
        }

        return adapter;
    }
}