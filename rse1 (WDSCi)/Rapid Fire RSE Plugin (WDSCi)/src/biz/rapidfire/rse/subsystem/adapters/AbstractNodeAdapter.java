/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.adapters;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public abstract class AbstractNodeAdapter extends AbstractResourceAdapter {

    @Override
    public final Object getParent(Object element) {
        return null;
    }

    @Override
    public final boolean hasChildren(Object element) {
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

    @Override
    public Object getRemoteParent(Shell shell, Object element) throws Exception {
        return null;
    }

    @Override
    public String[] getRemoteParentNamesInUse(Shell shell, Object element) throws Exception {
        return null;
    }

    @Override
    public boolean supportsUserDefinedActions(Object shell) {
        return false;
    }
}
