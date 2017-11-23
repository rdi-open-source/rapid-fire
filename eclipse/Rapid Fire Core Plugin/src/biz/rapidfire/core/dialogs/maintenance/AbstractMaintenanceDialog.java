/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public abstract class AbstractMaintenanceDialog extends XDialog {

    public AbstractMaintenanceDialog(Shell shell) {
        super(shell);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);

        newShell.setText(getDialogTitle());
    }

    @Override
    protected Control createDialogArea(Composite parent) {

        Composite container = (Composite)super.createDialogArea(parent);
        container.setLayout(new FillLayout());

        ScrolledComposite scrollable = new ScrolledComposite(container, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
        scrollable.setExpandHorizontal(true);
        scrollable.setExpandVertical(true);

        Composite editorArea = new Composite(scrollable, SWT.NONE);
        editorArea.setLayout(new GridLayout(2, false));
        editorArea.setLayoutData(new GridData(GridData.FILL_BOTH));

        WidgetFactory.createDialogSubTitle(editorArea, getMode());

        createEditorAreaContent(editorArea);
        createStatusLine(editorArea);

        setScreenValues();

        editorArea.layout();

        scrollable.setContent(editorArea);
        scrollable.setMinSize(editorArea.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        return container;
    }

    protected abstract String getDialogTitle();

    protected abstract String getMode();

    protected abstract void createEditorAreaContent(Composite editorArea);

    protected abstract void setScreenValues();

    /**
     * Overridden to make this dialog resizable.
     */
    @Override
    protected boolean isResizable() {
        return true;
    }

    /**
     * Overridden to provide a default size to {@link XDialog}.
     */
    @Override
    protected Point getDefaultSize() {
        return getShell().computeSize(Size.getSize(510), SWT.DEFAULT, true);
    }

    /**
     * Overridden to let {@link XDialog} store the state of this dialog in a
     * separate section of the dialog settings file.
     */
    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        return super.getDialogBoundsSettings(RapidFireCorePlugin.getDefault().getDialogSettings());
    }
}
