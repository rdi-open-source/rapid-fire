/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.dao;

import java.util.List;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;

public interface IFilesDAO {

    public List<IRapidFireFileResource> load(IRapidFireJobResource job, Shell shell) throws Exception;

    public IRapidFireFileResource load(IRapidFireJobResource job, int position, Shell shell) throws Exception;
}
