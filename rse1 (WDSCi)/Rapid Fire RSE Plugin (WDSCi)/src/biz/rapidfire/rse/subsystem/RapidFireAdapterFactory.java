/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import biz.rapidfire.rse.subsystem.adapters.AreasNodeAdapter;
import biz.rapidfire.rse.subsystem.adapters.FilesNodeAdapter;
import biz.rapidfire.rse.subsystem.adapters.LibrariesNodeAdapter;
import biz.rapidfire.rse.subsystem.adapters.LibraryListsNodeAdapter;
import biz.rapidfire.rse.subsystem.adapters.NotificationsNodeAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireAreaResourceAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireFileResourceAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireJobResourceAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireLibraryListResourceAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireLibraryResourceAdapter;
import biz.rapidfire.rse.subsystem.adapters.RapidFireNotificationResourceAdapter;
import biz.rapidfire.rse.subsystem.resources.AreasNode;
import biz.rapidfire.rse.subsystem.resources.FilesNode;
import biz.rapidfire.rse.subsystem.resources.LibrariesNode;
import biz.rapidfire.rse.subsystem.resources.LibraryListsNode;
import biz.rapidfire.rse.subsystem.resources.NotificationsNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireAreaResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireFileResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireJobResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireLibraryListResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireLibraryResource;
import biz.rapidfire.rse.subsystem.resources.RapidFireNotificationResource;

import com.ibm.etools.systems.core.ui.view.AbstractSystemRemoteAdapterFactory;
import com.ibm.etools.systems.core.ui.view.ISystemViewElementAdapter;

public class RapidFireAdapterFactory extends AbstractSystemRemoteAdapterFactory implements IAdapterFactory {

    private RapidFireJobResourceAdapter jobResourceAdapter = new RapidFireJobResourceAdapter();
    private RapidFireFileResourceAdapter fileResourceAdapter = new RapidFireFileResourceAdapter();
    private RapidFireLibraryListResourceAdapter libraryListResourceAdapter = new RapidFireLibraryListResourceAdapter();
    private RapidFireLibraryResourceAdapter libraryResourceAdapter = new RapidFireLibraryResourceAdapter();
    private RapidFireNotificationResourceAdapter notificationResourceAdapter = new RapidFireNotificationResourceAdapter();
    private RapidFireAreaResourceAdapter areaResourceAdapter = new RapidFireAreaResourceAdapter();

    private FilesNodeAdapter filesNodeAdapter = new FilesNodeAdapter();
    private LibraryListsNodeAdapter libraryListsNodeAdapter = new LibraryListsNodeAdapter();
    private LibrariesNodeAdapter librariesNodeAdapter = new LibrariesNodeAdapter();
    private NotificationsNodeAdapter notificationsNodeAdapter = new NotificationsNodeAdapter();
    private AreasNodeAdapter areasNodeAdapter = new AreasNodeAdapter();

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
        } else if (adaptableObject instanceof RapidFireLibraryListResource) {
            adapter = libraryListResourceAdapter;
        } else if (adaptableObject instanceof RapidFireLibraryResource) {
            adapter = libraryResourceAdapter;
        } else if (adaptableObject instanceof RapidFireNotificationResource) {
            adapter = notificationResourceAdapter;
        } else if (adaptableObject instanceof RapidFireAreaResource) {
            adapter = areaResourceAdapter;
        } else if (adaptableObject instanceof FilesNode) {
            adapter = filesNodeAdapter;
        } else if (adaptableObject instanceof LibraryListsNode) {
            adapter = libraryListsNodeAdapter;
        } else if (adaptableObject instanceof LibrariesNode) {
            adapter = librariesNodeAdapter;
        } else if (adaptableObject instanceof NotificationsNode) {
            adapter = notificationsNodeAdapter;
        } else if (adaptableObject instanceof AreasNode) {
            adapter = areasNodeAdapter;
        }

        if ((adapter != null) && (adapterType == IPropertySource.class)) {
            adapter.setPropertySourceInput(adaptableObject);
        }

        return adapter;
    }
}