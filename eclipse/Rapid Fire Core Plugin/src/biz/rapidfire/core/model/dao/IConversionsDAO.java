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

import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.IRapidFireFileResource;

public interface IConversionsDAO {

    public List<IRapidFireConversionResource> load(IRapidFireFileResource file, Shell shell) throws Exception;

    public IRapidFireConversionResource load(IRapidFireFileResource file, String fieldToConvert, Shell shell) throws Exception;
}
