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

public abstract class AbstractResourceDecorator<M> {

    protected static final String DELIMITER = " - ";
    protected static final String SPACE = " ";
    protected static final String QUOTES = "\"";

    private ImageDescriptor errorOverlay;

    protected ImageDescriptor getErrorOverlay() {

        if (errorOverlay == null) {
            errorOverlay = RapidFireCorePlugin.getDefault().getImageRegistry().getDescriptor(RapidFireCorePlugin.OVERLAY_ERROR);
        }

        return errorOverlay;
    }

    public abstract void decorate(M resource, IDecoration decorationBuilder);
}
