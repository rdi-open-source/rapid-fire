/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

public interface IRapidFireLibraryResource extends IRapidFireResource {

    public String getDataLibrary();

    public String getJob();

    public String getLibrary();

    public String getShadowLibrary();

    public void setShadowLibrary(String shadowLibrary);
}
