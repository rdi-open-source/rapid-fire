/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.decorators.shared;

import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.core.subsystem.decorators.AreaResourceDecorator;
import biz.rapidfire.core.subsystem.decorators.CommandResourceDecorator;
import biz.rapidfire.core.subsystem.decorators.ConversionResourceDecorator;
import biz.rapidfire.core.subsystem.decorators.FileResourceDecorator;
import biz.rapidfire.core.subsystem.decorators.JobResourceDecorator;
import biz.rapidfire.core.subsystem.decorators.LibraryListResourceDecorator;
import biz.rapidfire.core.subsystem.decorators.LibraryResourceDecorator;
import biz.rapidfire.core.subsystem.decorators.NotificationResourceDecorator;

public class ResourceDecorator implements ILightweightLabelDecorator {

    public static final String ID = "biz.rapidfire.core.subsystem.decorators.shared.ResourceDecorator";

    private FileResourceDecorator fileResourceDecorator;
    private JobResourceDecorator jobResourceDecorator;
    private LibraryResourceDecorator libraryResourceDecorator;
    private LibraryListResourceDecorator libraryListResourceDecorator;
    private NotificationResourceDecorator notificationResourceDecorator;
    private ConversionResourceDecorator conversionResourceDecorator;
    private CommandResourceDecorator commandResourceDecorator;
    private AreaResourceDecorator areaResourceDecorator;

    public ResourceDecorator() {
        this.fileResourceDecorator = new FileResourceDecorator();
        this.jobResourceDecorator = new JobResourceDecorator();
        this.libraryListResourceDecorator = new LibraryListResourceDecorator();
        this.libraryResourceDecorator = new LibraryResourceDecorator();
        this.notificationResourceDecorator = new NotificationResourceDecorator();
        this.conversionResourceDecorator = new ConversionResourceDecorator();
        this.commandResourceDecorator = new CommandResourceDecorator();
        this.areaResourceDecorator = new AreaResourceDecorator();
    }

    public void addListener(ILabelProviderListener arg0) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object arg0, String arg1) {
        return false;
    }

    public void removeListener(ILabelProviderListener arg0) {
        return;
    }

    public void decorate(Object resource, IDecoration decorationBuilder) {

        if (resource instanceof IRapidFireFileResource) {
            fileResourceDecorator.decorate((IRapidFireFileResource)resource, decorationBuilder);
        } else if (resource instanceof IRapidFireJobResource) {
            jobResourceDecorator.decorate((IRapidFireJobResource)resource, decorationBuilder);
        } else if (resource instanceof IRapidFireLibraryListResource) {
            libraryListResourceDecorator.decorate((IRapidFireLibraryListResource)resource, decorationBuilder);
        } else if (resource instanceof IRapidFireLibraryResource) {
            libraryResourceDecorator.decorate((IRapidFireLibraryResource)resource, decorationBuilder);
        } else if (resource instanceof IRapidFireNotificationResource) {
            notificationResourceDecorator.decorate((IRapidFireNotificationResource)resource, decorationBuilder);
        } else if (resource instanceof IRapidFireConversionResource) {
            conversionResourceDecorator.decorate((IRapidFireConversionResource)resource, decorationBuilder);
        } else if (resource instanceof IRapidFireCommandResource) {
            commandResourceDecorator.decorate((IRapidFireCommandResource)resource, decorationBuilder);
        } else if (resource instanceof IRapidFireAreaResource) {
            areaResourceDecorator.decorate((IRapidFireAreaResource)resource, decorationBuilder);
        }
    }
}
