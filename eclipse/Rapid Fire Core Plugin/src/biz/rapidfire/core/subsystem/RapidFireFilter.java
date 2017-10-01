/*******************************************************************************
 * Copyright (c) 2005 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *     iSphere Project Owners - Maintenance and enhancements
 *******************************************************************************/
package biz.rapidfire.core.subsystem;

public class RapidFireFilter {

    private static final String SLASH = "/"; //$NON-NLS-1$
    private static final String ASTERISK = "*"; //$NON-NLS-1$

    private String library;

    public RapidFireFilter() {
        super();

        setLibrary(ASTERISK);
    }

    public RapidFireFilter(String filterString) {
        this();
        setFilterString(filterString);
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getFilterString() {

        StringBuffer filterString = new StringBuffer();

        if (library == null) {
            filterString.append(ASTERISK);
        } else {
            filterString.append(library);
        }
        filterString.append(SLASH);

        return filterString.toString();
    }

    public static RapidFireFilter getDefaultFilter() {
        return new RapidFireFilter();
    }

    public static String getDefaultFilterString() {
        return getDefaultFilter().getFilterString();
    }

    public void setFilterString(String filterString) {

        int index;

        index = filterString.indexOf(SLASH);
        String temp = filterString.substring(0, index);
        if (!temp.equals(ASTERISK)) {
            setLibrary(temp);
        }
    }

}
