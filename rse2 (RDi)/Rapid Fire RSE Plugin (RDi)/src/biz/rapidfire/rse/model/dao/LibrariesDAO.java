/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.dao.AbstractLibrariesDAO;
import biz.rapidfire.core.model.dao.ILibrariesDAO;
import biz.rapidfire.rse.model.RapidFireLibraryResource;

public class LibrariesDAO extends AbstractLibrariesDAO implements ILibrariesDAO {

    public LibrariesDAO(String connectionName) throws Exception {
        super(new BaseDAO(connectionName));
    }

    @Override
    protected IRapidFireLibraryResource createLibraryInstance(String dataLibrary, String job, String library) {
        return new RapidFireLibraryResource(dataLibrary, job, library);
    }
}
