/*******************************************************************************
 * Copyright (c) 2017-2021 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import biz.rapidfire.rsebase.swt.widgets.AbstractSystemHostCombo;

public class SystemHostCombo extends AbstractSystemHostCombo implements ISystemHostCombo {

    public SystemHostCombo(Composite parent) {
        this(parent, SWT.NONE);
    }

    public SystemHostCombo(Composite parent, int style) {
        this(parent, style, true);
    }

    public SystemHostCombo(Composite parent, int style, boolean showNewButton) {
        this(parent, style, showNewButton, true);
    }

    public SystemHostCombo(Composite parent, int style, boolean showNewButton, boolean showLabel) {
        super(parent, style, showNewButton, showLabel);
    }

}
