/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.decorators;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.model.IRapidFireCommandResource;

public class CommandResourceDecorator extends AbstractResourceDecorator<IRapidFireCommandResource> {

    @Override
    public void decorate(IRapidFireCommandResource resource, IDecoration decorationBuilder) {

        decorationBuilder.addPrefix(Integer.toString(resource.getSequence()) + ": ");
        decorationBuilder.addSuffix(getParameters(resource.getCommand()));

        ImageDescriptor image;
        switch (resource.getCommandType()) {
        case PRERUN:
            image = RapidFireCorePlugin.getDefault().getImageRegistry().getDescriptor(RapidFireCorePlugin.OVERLAY_YELLOW_CIRCLE);
            break;

        case COMPILE:
            image = RapidFireCorePlugin.getDefault().getImageRegistry().getDescriptor(RapidFireCorePlugin.OVERLAY_GREEN_CIRCLE);
            break;

        case POSTRUN:
            image = RapidFireCorePlugin.getDefault().getImageRegistry().getDescriptor(RapidFireCorePlugin.OVERLAY_ORANGE_CIRCLE);
            break;

        default:
            image = null;
        }

        decorationBuilder.addOverlay(image, IDecoration.TOP_RIGHT);
    }

    private String getParameters(String command) {

        int x = command.indexOf(" "); //$NON-NLS-1$
        if (x >= 0) {
            return command.substring(x);
        }

        return "";
    }
}
