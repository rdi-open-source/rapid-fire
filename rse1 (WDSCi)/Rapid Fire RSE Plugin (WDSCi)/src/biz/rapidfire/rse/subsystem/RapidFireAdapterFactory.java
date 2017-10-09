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
import org.eclipse.ui.views.properties.IPropertySource;

import biz.rapidfire.rse.model.RapidFireFileResource;
import biz.rapidfire.rse.model.RapidFireJobResource;
import biz.rapidfire.rse.model.RapidFireLibraryResource;
import biz.rapidfire.rse.subsystem.adapters.FilesNodeAdapter;
import biz.rapidfire.rse.subsystem.adapters.LibrariesNodeAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireFileResourceAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireJobResourceAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireLibraryResourceAdapter;

import com.ibm.etools.systems.core.ui.view.AbstractSystemRemoteAdapterFactory;
import com.ibm.etools.systems.core.ui.view.ISystemViewElementAdapter;

public class RapidFireAdapterFactory extends AbstractSystemRemoteAdapterFactory implements IAdapterFactory {

    private RapidFireJobResourceAdapter jobResourceAdapter = new RapidFireJobResourceAdapter();
    private RapidFireFileResourceAdapter fileResourceAdapter = new RapidFireFileResourceAdapter();
    private RapidFireLibraryResourceAdapter libraryResourceAdapter = new RapidFireLibraryResourceAdapter();

    private FilesNodeAdapter filesResourceAdapter = new FilesNodeAdapter();
    private LibrariesNodeAdapter librariesResourceAdapter = new LibrariesNodeAdapter();

    public RapidFireAdapterFactory() {
        super();
    }

    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {

        ISystemViewElementAdapter adapter = null;
        if (adaptableObject instanceof RapidFireJobResource) {
            adapter = jobResourceAdapter;
        } else if (adaptableObject instanceof RapidFireFileResource) {
            adapter = fileResourceAdapter;
        } else if (adaptableObject instanceof RapidFireLibraryResource) {
            adapter = libraryResourceAdapter;
        } else if (adaptableObject instanceof FilesNode) {
            adapter = filesResourceAdapter;
        } else if (adaptableObject instanceof LibrariesNode) {
            adapter = librariesResourceAdapter;
        }

        if ((adapter != null) && (adapterType == IPropertySource.class)) {
            adapter.setPropertySourceInput(adaptableObject);
        }

        return adapter;
    }
}