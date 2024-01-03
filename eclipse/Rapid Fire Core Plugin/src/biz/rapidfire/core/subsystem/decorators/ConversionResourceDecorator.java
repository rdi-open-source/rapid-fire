/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.decorators;

import org.eclipse.jface.viewers.IDecoration;

import biz.rapidfire.core.model.IRapidFireConversionResource;

public class ConversionResourceDecorator extends AbstractResourceDecorator<IRapidFireConversionResource> {

    @Override
    public void decorate(IRapidFireConversionResource resource, IDecoration decorationBuilder) {

        if (resource.getConversions() != null && resource.getConversions().length > 0) {
            decorationBuilder.addSuffix(DELIMITER + resource.getConversions()[0]);
        }
    }
}
