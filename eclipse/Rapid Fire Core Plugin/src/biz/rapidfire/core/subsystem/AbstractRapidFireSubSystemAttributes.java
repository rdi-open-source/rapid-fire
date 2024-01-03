/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem;

public abstract class AbstractRapidFireSubSystemAttributes {

    public final static String VENDOR_ID = "biz.rapidfire"; //$NON-NLS-1$

    private static final String DOMAIN = VENDOR_ID + ".subsystem"; //$NON-NLS-1$

    public AbstractRapidFireSubSystemAttributes() {
    }

    protected abstract void saveSubSystem() throws Exception;

    protected abstract String getVendorAttribute(String key);

    protected abstract void setVendorAttribute(String key, String value);

    protected abstract void removeVendorAttribute(String key);
}
