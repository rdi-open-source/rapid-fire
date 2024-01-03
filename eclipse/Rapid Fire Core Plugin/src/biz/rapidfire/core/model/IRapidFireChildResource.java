/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

public interface IRapidFireChildResource<P> extends IRapidFireResource {

    public IRapidFireJobResource getParentJob();

    public P getParentResource();

    public IRapidFireNodeResource getParentNode();

    public void setParentNode(IRapidFireNodeResource parentNode);
}
