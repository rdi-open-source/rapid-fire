/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import org.eclipse.rse.services.clientserver.messages.SystemMessage;
import org.eclipse.rse.ui.SystemWidgetHelpers;
import org.eclipse.rse.ui.filters.SystemFilterStringEditPane;
import org.eclipse.rse.ui.messages.SystemMessageDialog;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.subsystem.RapidFireFilterStringEditPaneDelegate;
import biz.rapidfire.rse.Messages;

public class RapidFireInstanceFilterStringEditPane extends SystemFilterStringEditPane {

    private RapidFireFilterStringEditPaneDelegate delegate;

    public RapidFireInstanceFilterStringEditPane(Shell shell) {
        super(shell);

        delegate = new RapidFireFilterStringEditPaneDelegate();
    }

    @Override
    public Control createContents(Composite parent) {

        int nbrColumns = 3;
        Composite composite = SystemWidgetHelpers.createComposite(parent, nbrColumns);

        delegate.createContents(composite);

        resetFields();
        doInitializeFields();

        ModifyListener keyListener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateInput();
            }
        };

        delegate.addModifyListener(keyListener);

        return composite;
    }

    private void validateInput() {

        String message = delegate.validateInput();
        if (message != null) {
            errorMessage = new SystemMessage("", "", "", SystemMessage.ERROR, message, ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        } else {
            errorMessage = null;
        }

        fireChangeEvent(errorMessage);
    }

    @Override
    public Control getInitialFocusControl() {
        return delegate.getInitialFocusControl();
    }

    @Override
    protected void doInitializeFields() {
        delegate.doInitializeFields(getInputFilterString());
    }

    @Override
    protected void resetFields() {
        delegate.resetFields();
    }

    @Override
    protected boolean areFieldsComplete() {
        return delegate.areFieldsComplete();
    }

    @Override
    public String getFilterString() {
        return delegate.getFilterString();
    }

    /**
     * Called, when the dialog is first displayed.
     */
    @Override
    public boolean isComplete() {
        return areFieldsComplete();
    }

    /**
     * Called, when the [Finish] button is pressed.
     */
    @Override
    public SystemMessage verify() {

        if (!areFieldsComplete()) {
            return SystemMessageDialog.getExceptionMessage(Display.getCurrent().getActiveShell(), new Exception(
                Messages.Rapid_Fire_filter_contains_invalid_values));
        }

        return null;
    }
}