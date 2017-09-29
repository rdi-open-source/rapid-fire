/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.rse.ui.view.AbstractSystemRemoteAdapterFactory;
import org.eclipse.rse.ui.view.ISystemViewElementAdapter;
import org.eclipse.ui.views.properties.IPropertySource;

public class DeveloperAdapterFactory extends AbstractSystemRemoteAdapterFactory implements IAdapterFactory {

    // private TeamResourceAdapter teamAdapter = new TeamResourceAdapter();
    // private DeveloperResourceAdapter developerAdapter = new
    // DeveloperResourceAdapter();

    /**
     * Constructor for DeveloperAdapterFactory.
     */
    public DeveloperAdapterFactory() {
        super();
    }

    /**
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(Object, Class)
     */
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        ISystemViewElementAdapter adapter = null;

        // if (adaptableObject instanceof TeamResource)
        // adapter = teamAdapter;
        // else if (adaptableObject instanceof DeveloperResource)
        // adapter = developerAdapter;

        // these lines are very important!
        if ((adapter != null) && (adapterType == IPropertySource.class)) {
            adapter.setPropertySourceInput(adaptableObject);
        }

        return adapter;
    }

}
