/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.decorators;

import org.eclipse.jface.viewers.IDecoration;

import biz.rapidfire.core.model.IRapidFireLibraryResource;

public class LibraryResourceDecorator extends AbstractResourceDecorator<IRapidFireLibraryResource> {

    @Override
    public void decorate(IRapidFireLibraryResource resource, IDecoration decorationBuilder) {
        decorationBuilder.addSuffix(DELIMITER + resource.getShadowLibrary());
    }
}
