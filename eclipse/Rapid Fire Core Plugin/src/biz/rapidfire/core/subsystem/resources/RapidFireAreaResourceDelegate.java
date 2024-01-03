/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.resources;

import biz.rapidfire.core.model.IRapidFireAreaResource;

public class RapidFireAreaResourceDelegate implements Comparable<IRapidFireAreaResource> {

    private String dataLibrary;
    private String job;
    private int position;
    private String area;
    private String library;
    private String libraryList;
    private String libraryCcsid;
    private String commandExtension;

    public RapidFireAreaResourceDelegate(String dataLibrary, String job, int position, String area) {

        this.dataLibrary = dataLibrary;
        this.job = job;
        this.position = position;
        this.area = area;
    }

    /*
     * IRapidFireResource methods
     */

    public String getDataLibrary() {
        return dataLibrary;
    }

    /*
     * IRapidFireFileResource methods
     */

    public String getJob() {
        return job;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return area;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getLibraryList() {
        return libraryList;
    }

    public void setLibraryList(String libraryList) {
        this.libraryList = libraryList;
    }

    public String getLibraryCcsid() {
        return libraryCcsid;
    }

    public void setLibraryCcsid(String libraryCcsid) {
        this.libraryCcsid = libraryCcsid;
    }

    public String getCommandExtension() {
        return commandExtension;
    }

    public void setCommandExtension(String commandExtension) {
        this.commandExtension = commandExtension;
    }

    public int compareTo(IRapidFireAreaResource resource) {

        if (resource == null) {
            return 1;
        }

        int result = resource.getDataLibrary().compareTo(getDataLibrary());
        if (result != 0) {
            return result;
        }

        result = resource.getJob().compareTo(getJob());
        if (result != 0) {
            return result;
        }

        if (position > resource.getPosition()) {
            return 1;
        } else if (position < resource.getPosition()) {
            return -1;
        }

        return getName().compareTo(resource.getName());
    }

    @Override
    public String toString() {
        return getName();
    }

}
