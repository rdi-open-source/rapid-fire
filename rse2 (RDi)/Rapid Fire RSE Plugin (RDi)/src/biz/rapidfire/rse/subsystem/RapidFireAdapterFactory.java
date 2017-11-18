/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
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

import biz.rapidfire.rse.subsystem.adapters.FilesNodeAdapter;
import biz.rapidfire.rse.subsystem.adapters.LibrariesNodeAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireFileResourceAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireJobResourceAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireLibraryResourceAdapter;
import biz.rapidfire.rse.subsystem.resources.FilesNode;
import biz.rapidfire.rse.subsystem.resources.LibrariesNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireFileResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireJobResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireLibraryResource;

public class RapidFireAdapterFactory extends AbstractSystemRemoteAdapterFactory implements IAdapterFactory {

    private RapidFireJobResourceAdapter jobResourceAdapter = new RapidFireJobResourceAdapter();
    private RapidFireFileResourceAdapter fileResourceAdapter = new RapidFireFileResourceAdapter();
    private RapidFireLibraryResourceAdapter libraryResourceAdapter = new RapidFireLibraryResourceAdapter();

    private FilesNodeAdapter filesNodeAdapter = new FilesNodeAdapter();
    private LibrariesNodeAdapter librariesNodeAdapter = new LibrariesNodeAdapter();

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
            adapter = filesNodeAdapter;
        } else if (adaptableObject instanceof LibrariesNode) {
            adapter = librariesNodeAdapter;
        }

        if ((adapter != null) && (adapterType == IPropertySource.class)) {
            adapter.setPropertySourceInput(adaptableObject);
        }

        return adapter;
    }
}