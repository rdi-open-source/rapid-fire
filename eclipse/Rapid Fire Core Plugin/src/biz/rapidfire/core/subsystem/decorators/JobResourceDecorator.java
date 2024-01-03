/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.decorators;

import org.eclipse.jface.viewers.IDecoration;

import biz.rapidfire.core.model.IRapidFireJobResource;

public class JobResourceDecorator extends AbstractResourceDecorator<IRapidFireJobResource> {

    @Override
    public void decorate(IRapidFireJobResource resource, IDecoration decorationBuilder) {

        if (resource.isError()) {
            decorationBuilder.addSuffix(DELIMITER + resource.getErrorText());
            decorationBuilder.addOverlay(getErrorOverlay(), IDecoration.TOP_RIGHT);
        } else {
            decorationBuilder.addSuffix(DELIMITER + resource.getStatus().label() + " - " + resource.getPhase().label() + " - " + QUOTES
                + resource.getDescription() + QUOTES);
        }
    }
}
