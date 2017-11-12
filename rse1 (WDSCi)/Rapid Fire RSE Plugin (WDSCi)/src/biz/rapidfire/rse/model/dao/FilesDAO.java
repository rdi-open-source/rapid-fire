/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model.dao;

import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.dao.AbstractFilesDAO;
import biz.rapidfire.core.model.dao.IFilesDAO;
import biz.rapidfire.rse.model.RapidFireFileResource;

public class FilesDAO extends AbstractFilesDAO implements IFilesDAO {

    public FilesDAO(String connectionName, String libraryName) throws Exception {
        super(DAOManager.getInstance().getBaseDAO(connectionName, libraryName, false));
    }

    @Override
    protected IRapidFireFileResource createFileInstance(String dataLibrary, String job, int position) {
        return new RapidFireFileResource(dataLibrary, job, position);
    }

}
