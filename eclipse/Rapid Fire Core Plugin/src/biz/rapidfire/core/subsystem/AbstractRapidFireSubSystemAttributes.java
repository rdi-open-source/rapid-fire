/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.model.IRapidFireInstanceResource;

public abstract class AbstractRapidFireSubSystemAttributes {

    public final static String VENDOR_ID = "biz.rapidfire"; //$NON-NLS-1$

    private static final String DOMAIN = "biz.rapidfire.subsystem.instances"; //$NON-NLS-1$
    private static final String COUNT = DOMAIN + ".count"; //$NON-NLS-1$
    private static final String NAME = DOMAIN + ".name_"; //$NON-NLS-1$
    private static final String LIBRARY = DOMAIN + ".library_"; //$NON-NLS-1$

    private Map<String, IRapidFireInstanceResource> rapidFireInstances;

    public AbstractRapidFireSubSystemAttributes() {

        rapidFireInstances = new HashMap<String, IRapidFireInstanceResource>();
    }

    public boolean hasRapidFireInstance(String library) {
        return rapidFireInstances.containsKey(getResourceKey(library));
    }

    public IRapidFireInstanceResource addRapidFireInstance(String name, String library) {

        IRapidFireInstanceResource resource = createRapidFireInstanceResource(name, library);
        rapidFireInstances.put(getResourceKey(resource), resource);

        updateInstances();

        return resource;
    }

    protected abstract IRapidFireInstanceResource createRapidFireInstanceResource(String name, String library);

    public String removeRapidFireInstance(IRapidFireInstanceResource resource) {

        if (rapidFireInstances.containsKey(getResourceKey(resource))) {
            IRapidFireInstanceResource removedResource = rapidFireInstances.remove(getResourceKey(resource));
            updateInstances();
            return removedResource.getLibrary();
        }

        return null;
    }

    public IRapidFireInstanceResource[] getRapidFireInstances() {

        loadInstances();

        return rapidFireInstances.values().toArray(new IRapidFireInstanceResource[rapidFireInstances.size()]);
    }

    protected void loadInstances() {

        rapidFireInstances.clear();

        int i;
        int c = IntHelper.tryParseInt(getVendorAttribute(COUNT), 0);
        for (i = 0; i < c; i++) {
            String name = getVendorAttribute(NAME + i);
            String library = getVendorAttribute(LIBRARY + i);

            IRapidFireInstanceResource resource = createRapidFireInstanceResource(name, library);

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
        for (Iterator<IRapidFireInstanceResource> iterator = rapidFireInstances.values().iterator(); iterator.hasNext();) {
            IRapidFireInstanceResource resource = iterator.next();
            setVendorAttribute(NAME + Integer.toString(i), resource.getName());
            setVendorAttribute(LIBRARY + Integer.toString(i), resource.getLibrary());
            i++;
        }

        try {
            setVendorAttribute(COUNT, Integer.toString(c));

            saveSubSystem();
        } catch (Throwable e) {
            RapidFireCorePlugin.logError("*** Could not save subsystem configuration. ***", e); //$NON-NLS-1$
        }
    }

    protected abstract void saveSubSystem() throws Exception;

    protected abstract String getResourceKey(IRapidFireInstanceResource resource);

    protected abstract String getResourceKey(String library);

    protected abstract String getVendorAttribute(String key);

    protected abstract void setVendorAttribute(String key, String value);

    protected abstract void removeVendorAttribute(String key);
}
