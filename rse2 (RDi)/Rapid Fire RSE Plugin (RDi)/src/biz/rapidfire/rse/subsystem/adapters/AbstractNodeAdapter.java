/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.adapters;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public abstract class AbstractNodeAdapter extends AbstractResourceAdapter {

    @Override
    public final Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(IAdaptable element) {
        return false;
    }

    @Override
    protected final IPropertyDescriptor[] internalGetPropertyDescriptors() {
        return null;
    }

    @Override
    protected final Object internalGetPropertyValue(Object element) {
        return null;
    }

    /**
     * Returns the absolute name of the node. The name must be unique for the
     * "Remote Systems" view.
     */
    public final String getAbsoluteName(Object element) {
        return getAbsoluteNamePrefix() + element.hashCode(); //$NON-NLS-1$
    }

    protected abstract String getAbsoluteNamePrefix();
}
