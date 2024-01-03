/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.shared;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;

public interface IMaintenanceHandler extends IHandler {

    public boolean isEnabled();

    public void setEnabled(Object paramObject);

    public void setEnabledWDSCi(ISelection selection);

    public void executeWDSCi(ExecutionEvent event) throws ExecutionException;

    public Object executeWithSelection(ISelection selection) throws ExecutionException;
}
