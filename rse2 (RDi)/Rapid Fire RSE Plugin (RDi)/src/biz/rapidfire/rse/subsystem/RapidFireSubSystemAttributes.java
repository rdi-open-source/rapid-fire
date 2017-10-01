/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.IntHelper;

public class RapidFireSubSystemAttributes {

    public final static String VENDOR_ID = "biz.rapidfire"; //$NON-NLS-1$

    private static final String DOMAIN = "biz.rapidfire.subsystem.instances";
    private static final String COUNT = DOMAIN + ".count";
    private static final String NAME = DOMAIN + ".name_";
    private static final String LIBRARY = DOMAIN + ".library_";

    private RapidFireSubSystem subSystem;
    private Map<String, RapidFireInstanceResource> rapidFireInstances;

    public RapidFireSubSystemAttributes(RapidFireSubSystem subSystem) {
        this.subSystem = subSystem;

        rapidFireInstances = new HashMap<String, RapidFireInstanceResource>();

        loadInstances();
    }

    public boolean hasRapidFireInstance(String library) {
        return rapidFireInstances.containsKey(getResourceKey(library));
    }

    public RapidFireInstanceResource addRapidFireInstance(String name, String library) {

        RapidFireInstanceResource resource = new RapidFireInstanceResource(subSystem, name, library);
        rapidFireInstances.put(getResourceKey(resource), resource);

        updateInstances();

        return resource;
    }

    public String removeRapidFireInstance(RapidFireInstanceResource resource) {

        if (rapidFireInstances.containsKey(getResourceKey(resource))) {
            RapidFireInstanceResource removedResource = rapidFireInstances.remove(getResourceKey(resource));
            updateInstances();
            return removedResource.getLibrary();
        }

        return null;
    }

    public RapidFireInstanceResource[] getRapidFireInstances() {

        loadInstances();

        return rapidFireInstances.values().toArray(new RapidFireInstanceResource[rapidFireInstances.size()]);
    }

    private void loadInstances() {

        rapidFireInstances.clear();

        int i;
        int c = IntHelper.tryParseInt(getVendorAttribute(COUNT), 0);
        for (i = 0; i < c; i++) {
            String name = getVendorAttribute(NAME + i);
            String library = getVendorAttribute(LIBRARY + i);

            RapidFireInstanceResource resource = new RapidFireInstanceResource(subSystem, name, library);

            rapidFireInstances.put(getResourceKey(resource), resource);
        }
    }

    private void updateInstances() {

        int i;
        int c = IntHelper.tryParseInt(getVendorAttribute(COUNT), 0);
        for (i = 0; i < c; i++) {
            removeVendorAttribute(NAME + i);
            removeVendorAttribute(LIBRARY + i);
        }

        setVendorAttribute(COUNT, Integer.toString(0));

        c = rapidFireInstances.size();
        i = 0;
        for (Iterator<RapidFireInstanceResource> iterator = rapidFireInstances.values().iterator(); iterator.hasNext();) {
            RapidFireInstanceResource resource = iterator.next();
            setVendorAttribute(NAME + Integer.toString(i), resource.getName());
            setVendorAttribute(LIBRARY + Integer.toString(i), resource.getLibrary());
            i++;
        }

        setVendorAttribute(COUNT, Integer.toString(c));

        try {
            subSystem.getSubSystemConfiguration().saveSubSystem(subSystem);
        } catch (Throwable e) {
            RapidFireCorePlugin.logError("*** Could not save subsystem configuration. ***", e);
        }
    }

    private String getResourceKey(RapidFireInstanceResource resource) {

        return getResourceKey(resource.getLibrary());
    }

    private String getResourceKey(String library) {

        return subSystem.getHostAliasName() + "." + library;
    }

    private String getVendorAttribute(String key) {
        return subSystem.getVendorAttribute(key);
    }

    private void setVendorAttribute(String key, String value) {
        subSystem.setVendorAttribute(key, value);
    }

    private void removeVendorAttribute(String key) {
        subSystem.removeVendorAttribute(key);
    }
}
