/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.maintenance.area.shared.AreaKey;
import biz.rapidfire.core.swt.widgets.viewers.stringlist.IStringListItem;

public interface IRapidFireAreaResource extends IRapidFireChildResource<IRapidFireFileResource>, IStringListItem {

    /*
     * Key attributes
     */

    public AreaKey getKey();

    public String getJob();

    public int getPosition();

    public String getName();

    /*
     * Data attributes
     */

    public String getLibrary();

    public void setLibrary(String library);

    public String getLibraryList();

    public void setLibraryList(String libraryList);

    public String getLibraryCcsid();

    public void setLibraryCcsid(String ccsid);

    public String getCommandExtension();

    public void setCommandExtension(String commandExtension);

    public void reload(Shell shell) throws Exception;
}
