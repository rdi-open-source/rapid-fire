/*******************************************************************************
 * Copyright (c) 2017-2021 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.swt.widgets;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

public interface ISystemHostCombo {

    public boolean isDisposed();

    public void setEnabled(boolean enabled);

    public boolean getEnabled();

    public void setFocus();

    public void setData(String key, Object data);

    public Object getData(String key);

    public void addModifyListener(ModifyListener listener);

    public void removeModifyListener(ModifyListener listener);

    public void addSelectionListener(SelectionListener listener);

    public void addSelectionChangedListener(ISelectionChangedListener listener);

    public void removeSelectionChangedListener(ISelectionChangedListener listener);

    public ISelection getSelection();

    public void setSelection(ISelection selection);

    public int getSelectionIndex();

    public void setSelectionIndex(int index);

    public Combo getCombo();

    public String getText();

    public String getConnectionName();

    public String getHostName();

    public boolean selectConnection(String connectionName);

    public void setToolTipText(String tip);

    public void setLayoutData(Object layoutData);

}
