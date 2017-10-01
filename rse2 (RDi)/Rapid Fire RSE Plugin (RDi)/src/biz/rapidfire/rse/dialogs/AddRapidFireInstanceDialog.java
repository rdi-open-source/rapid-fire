/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.dialogs;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.swt.widgets.UpperCaseOnlyVerifier;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.core.validators.Validator;

public class AddRapidFireInstanceDialog extends XDialog {

    private Text textName;
    private Text textLibrary;

    private String name;
    private String library;

    public AddRapidFireInstanceDialog(Shell shell) {
        super(shell);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Messages.Specify_a_filter");
    }

    @Override
    protected Control createDialogArea(Composite parent) {

        Composite container = (Composite)super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));

        Composite compositeFilter = new Composite(container, SWT.NONE);
        compositeFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        compositeFilter.setLayout(new GridLayout(2, false));

        Label labelName = new Label(compositeFilter, SWT.NONE);
        labelName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelName.setText("Messages.Name_colon");

        textName = WidgetFactory.createText(compositeFilter);
        textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textName.setText(Messages.EMPTY);

        Label labelFilter = new Label(compositeFilter, SWT.NONE);
        labelFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelFilter.setText("Messages.Library_colon");

        textLibrary = WidgetFactory.createText(compositeFilter);
        textLibrary.addVerifyListener(new UpperCaseOnlyVerifier());
        textLibrary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textLibrary.setText(Messages.EMPTY);
        textLibrary.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                String name = textName.getText();
                String library = textLibrary.getText();
                if (library.startsWith(name) && library.length() == name.length() + 1) {
                    textName.setText(library);
                    return;
                }
            }
        });

        createStatusLine(container);

        return container;
    }

    @Override
    public void setFocus() {
        textLibrary.setFocus();
    }

    @Override
    protected void okPressed() {

        textLibrary.setText(textLibrary.getText().trim());

        if (!Validator.getLibraryNameInstance().validate(textLibrary.getText())) {
            setErrorMessage("Messages.The_value_in_field_Filter_is_not_valid");
            textLibrary.setFocus();
            return;
        }

        name = textName.getText();
        library = textLibrary.getText();

        super.okPressed();

    }

    public String getName() {
        return name;
    }

    public String getLibrary() {
        return library;
    }

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
        return getShell().computeSize(Size.getSize(250), SWT.DEFAULT, true);
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
