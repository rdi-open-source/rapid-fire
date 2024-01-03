/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.action;

import org.eclipse.jface.action.Action;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.view.IAutoRefreshView;

public class RefreshViewIntervalAction extends Action {

    public static final int REFRESH_OFF = -1;

    private IAutoRefreshView view;
    private int seconds;

    public RefreshViewIntervalAction(IAutoRefreshView view, int seconds) {
        super("");

        this.view = view;
        this.seconds = seconds;

        if (seconds == REFRESH_OFF) {
            setText(Messages.ActionLabel_Auto_refresh_menu_item_stop);
            setToolTipText(Messages.ActionTooltip_Auto_refresh_menu_item_stop);
            setImageDescriptor(RapidFireCorePlugin.getDefault().getImageDescriptor(RapidFireCorePlugin.IMAGE_AUTO_REFRESH_OFF));
        } else {
            setText(Messages.bind(Messages.ActionLabel_Auto_refresh_menu_item_every_A_seconds, seconds));
            setToolTipText(Messages.bind(Messages.ActionLabel_Auto_refresh_menu_item_every_A_seconds, seconds));
        }

        setEnabled(false);
    }

    public int getInterval() {
        return seconds;
    }

    @Override
    public void run() {
        view.setRefreshInterval(seconds);
    }
}
