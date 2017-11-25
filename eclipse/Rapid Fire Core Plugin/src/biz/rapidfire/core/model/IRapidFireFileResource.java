/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import biz.rapidfire.core.model.maintenance.file.shared.FileType;

public interface IRapidFireFileResource extends IRapidFireResource {

    /*
     * Key attributes
     */

    public String getJob();

    public int getPosition();

    /*
     * Data attributes
     */

    public String getName();

    public void setName(String name);

    public FileType getFileType();

    public void setFileType(FileType fileType);

    public String getCopyProgramName();

    public void setCopyProgramName(String copyProgramName);

    public String getCopyProgramLibrary();

    public void setCopyProgramLibrary(String copyProgramLibrary);

    public String getConversionProgramName();

    public void setConversionProgramName(String conversionProgramName);

    public String getConversionProgramLibrary();

    public void setConversionProgramLibrary(String conversionProgramLibrary);
}
