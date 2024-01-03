/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.area.shared;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceKey;
import biz.rapidfire.core.maintenance.file.shared.FileKey;

public class AreaKey implements IResourceKey {

    private FileKey fileKey;
    private String area;

    public static AreaKey createNew(FileKey fileKey) {

        AreaKey key = new AreaKey(fileKey, ""); //$NON-NLS-1$

        return key;
    }

    public AreaKey(FileKey fileKey, String area) {

        this.fileKey = fileKey;
        this.area = area;
    }

    public String getJobName() {
        return fileKey.getJobName();
    }

    public int getPosition() {
        return fileKey.getPosition();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();
        buffer.append(fileKey.toString());
        buffer.append(DELIMITER);
        buffer.append(getArea());

        return buffer.toString();
    }

    @Override
    public Object clone() {
        try {

            AreaKey areaKey = (AreaKey)super.clone();
            areaKey.fileKey = (FileKey)fileKey.clone();

            return areaKey;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
