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
package biz.rapidfire.rse.model;

import org.eclipse.rse.core.subsystems.AbstractResource;
import org.eclipse.rse.core.subsystems.ISubSystem;

import biz.rapidfire.core.model.IRapidFireInstanceResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;

public class RapidFireInstanceResource extends AbstractResource implements IRapidFireInstanceResource, Comparable<RapidFireInstanceResource> {

    private String name;
    private String library;

    public RapidFireInstanceResource(IRapidFireSubSystem subSystem, String name, String library) {
        super((ISubSystem)subSystem);

        this.name = name;
        this.library = library;
    }

    public RapidFireInstanceResource() {
        super();
    }

    /*
     * IRapidFireInstanceResource methods
     */

    public String getName() {
        return name;
    }

    public String getLibrary() {
        return library;
    }

    public String getConnectionName() {
        return getSubSystem().getHostAliasName();
    }

    public IRapidFireSubSystem getParent() {
        return (IRapidFireSubSystem)getSubSystem();
    }

    public int compareTo(RapidFireInstanceResource resource) {

        if (resource == null || resource.getLibrary() == null) {
            return 1;
        } else if (getLibrary() == null) {
            return -1;
        }

        return getLibrary().compareTo(resource.getLibrary());
    }

    @Override
    public String toString() {
        return getSubSystem().getName() + "." + library; //$NON-NLS-1$
    }
}
