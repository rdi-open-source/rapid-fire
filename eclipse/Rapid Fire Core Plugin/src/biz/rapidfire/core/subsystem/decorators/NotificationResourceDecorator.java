/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.decorators;

import org.eclipse.jface.viewers.IDecoration;

import biz.rapidfire.core.model.IRapidFireNotificationResource;

public class NotificationResourceDecorator extends AbstractResourceDecorator<IRapidFireNotificationResource> {

    @Override
    public void decorate(IRapidFireNotificationResource resource, IDecoration decorationBuilder) {
        decorationBuilder.addPrefix(Integer.toString(resource.getPosition()) + ": ");
        decorationBuilder.addSuffix(DELIMITER + resource.getNotificationType().toString());
    }
}
