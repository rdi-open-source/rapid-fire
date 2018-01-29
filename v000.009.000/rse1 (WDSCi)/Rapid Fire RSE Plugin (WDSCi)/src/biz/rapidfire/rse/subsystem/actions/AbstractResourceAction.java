/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISources;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.job.DisplayJobHandler;
import biz.rapidfire.core.handlers.shared.IMaintenanceHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;

import com.ibm.etools.iseries.core.ui.actions.isv.ISeriesAbstractQSYSPopupMenuExtensionAction;

public abstract class AbstractResourceAction extends ISeriesAbstractQSYSPopupMenuExtensionAction {

    private IMaintenanceHandler handler = new DisplayJobHandler();

    public AbstractResourceAction(IMaintenanceHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {

        try {

            Object[] selection = getSelectedRemoteObjects();

            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(ISources.ACTIVE_CURRENT_SELECTION_NAME, new StructuredSelection(selection));
            ExecutionEvent event = new ExecutionEvent(null, properties, null, null);

            handler.executeWDSCi(event);

        } catch (ExecutionException e) {
            RapidFireCorePlugin.logError("*** Could not execute the requested action ***", e); //$NON-NLS-1$
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        handler.setEnabledWDSCi(selection);
        /*
         * Must be called after handler.setEnabledWDSCi(), because getEnabled()
         * is called by super.selectionChanged().
         */
        super.selectionChanged(action, selection);
    }

    @Override
    public boolean getEnabled(Object[] arg0) {
        return handler.isEnabled();
    }
}