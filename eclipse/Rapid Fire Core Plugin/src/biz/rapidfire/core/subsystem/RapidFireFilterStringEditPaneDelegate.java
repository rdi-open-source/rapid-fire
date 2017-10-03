/*******************************************************************************
 * Copyright (c) 2005 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *     iSphere Project Owners - Maintenance and enhancements
 *******************************************************************************/

package biz.rapidfire.core.subsystem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class RapidFireFilterStringEditPaneDelegate {

    private static final String ASTERISK = "*"; //$NON-NLS-1$
    private Text libraryText;

    public RapidFireFilterStringEditPaneDelegate() {
    }

    public Control createContents(Composite composite_prompts) {

        ((GridLayout)composite_prompts.getLayout()).marginWidth = 0;

        Label libraryLabel = new Label(composite_prompts, SWT.NONE);
        libraryLabel.setText(Messages.Library_colon);

        libraryText = WidgetFactory.createUpperCaseText(composite_prompts);
        GridData gd = new GridData();
        gd.widthHint = 75;
        gd.horizontalSpan = 2;
        libraryText.setLayoutData(gd);
        libraryText.setTextLimit(10);

        return composite_prompts;
    }

    public void addModifyListener(ModifyListener keyListener) {

        libraryText.addModifyListener(keyListener);
    }

    public Control getInitialFocusControl() {
        return libraryText;
    }

    public void doInitializeFields(String inputFilterString) {

        if (inputFilterString != null) {

            RapidFireFilter filter = new RapidFireFilter(inputFilterString);
            libraryText.setText(filter.getLibrary());
        }
    }

    public void resetFields() {

        libraryText.setText(ASTERISK);
    }

    public boolean areFieldsComplete() {
        return (libraryText.getText().trim().length() >= 1);
    }

    public String getFilterString() {

        RapidFireFilter filter = new RapidFireFilter();

        filter.setLibrary(libraryText.getText().toUpperCase());

        return filter.getFilterString();
    }
}
