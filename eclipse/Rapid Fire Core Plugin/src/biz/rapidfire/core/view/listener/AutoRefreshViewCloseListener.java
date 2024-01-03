/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.view.listener;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import biz.rapidfire.core.view.IAutoRefreshView;

public class AutoRefreshViewCloseListener implements DisposeListener {

    private IAutoRefreshView view;

    public AutoRefreshViewCloseListener(IAutoRefreshView view) {
        this.view = view;
    }

    public void widgetDisposed(DisposeEvent event) {
        view.startClosingView();
    }
}
