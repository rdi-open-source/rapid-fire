/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import org.eclipse.core.runtime.IAdaptable;

import biz.rapidfire.core.subsystem.IRapidFireSubSystem;

public interface IRapidFireResource extends IAdaptable {

    public String getDataLibrary();

    public IRapidFireSubSystem getParentSubSystem();
}
