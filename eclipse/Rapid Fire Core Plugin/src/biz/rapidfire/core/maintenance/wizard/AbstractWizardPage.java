/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractWizardPage extends WizardPage implements ModifyListener, SelectionListener {

    protected AbstractWizardPage(String name) {
        super(name);
    }

    public void createControl(Composite parent) {

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, false));
        container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        createContent(container);

        setControl(container);
        updatePageComplete();
    }

    public void modifyText(ModifyEvent arg0) {
        updatePageComplete();
    }

    public void widgetDefaultSelected(SelectionEvent event) {
        updatePageComplete();
    }

    public void widgetSelected(SelectionEvent event) {
        updatePageComplete();
    }

    protected abstract void updatePageComplete();

    protected abstract void createContent(Composite container);
}
