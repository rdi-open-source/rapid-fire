/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.model.Status;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.core.validators.Validator;

public class RapidFireFilterStringEditPaneDelegate {

    private Text dataLibraryText;
    private Text jobText;
    private Combo statusCombo;

    public RapidFireFilterStringEditPaneDelegate() {
    }

    public Control createContents(Composite parent) {

        ((GridLayout)parent.getLayout()).marginWidth = 0;

        dataLibraryText = createUpperCaseText(parent, Messages.Label_Library_colon, 10, null);
        jobText = createUpperCaseText(parent, Messages.Label_Job_colon, 10, Messages.Label_Full_generic_string);
        statusCombo = createReadOnlyCombo(parent, Messages.Label_Status_colon, Status.values());

        return parent;
    }

    private Combo createReadOnlyCombo(Composite parent, String label, Status[] values) {

        Label libraryLabel = new Label(parent, SWT.NONE);
        libraryLabel.setText(label);

        List<String> items = new LinkedList<String>();
        items.add(RapidFireFilter.ASTERISK);
        for (Status status : values) {
            items.add(status.label);
        }

        Combo combo = WidgetFactory.createReadOnlyCombo(parent);
        combo.setLayoutData(createGridData());
        combo.setItems(items.toArray(new String[items.size()]));

        return combo;
    }

    private Text createUpperCaseText(Composite parent, String label, int textLimit, String choice) {

        Label libraryLabel = new Label(parent, SWT.NONE);
        libraryLabel.setText(label);

        Text text = WidgetFactory.createUpperCaseText(parent);
        text.setLayoutData(createGridData());
        text.setTextLimit(textLimit);

        if (choice == null) {
            new Label(parent, SWT.NONE).setLayoutData(createGridData());
        } else {
            Label choiceLabel = new Label(parent, SWT.NONE);
            choiceLabel.setLayoutData(createGridData());
            choiceLabel.setText(Messages.Label_Full_generic_string);
        }

        return text;
    }

    private GridData createGridData() {
        return new GridData(SWT.FILL, SWT.BEGINNING, true, false);
    }

    public void addModifyListener(ModifyListener keyListener) {

        dataLibraryText.addModifyListener(keyListener);
        jobText.addModifyListener(keyListener);
        statusCombo.addModifyListener(keyListener);
    }

    public Control getInitialFocusControl() {
        return dataLibraryText;
    }

    public void doInitializeFields(String inputFilterString) {

        if (inputFilterString != null) {
            RapidFireFilter filter = new RapidFireFilter(inputFilterString);
            dataLibraryText.setText(filter.getDataLibrary());
            jobText.setText(filter.getJob());
            statusCombo.setText(filter.getStatus());
        } else {
            resetFields();
        }
    }

    public void resetFields() {

        dataLibraryText.setText(RapidFireFilter.RAPIDFIRE_LIBRARY); //$NON-NLS-1$
        jobText.setText(RapidFireFilter.ASTERISK); //$NON-NLS-1$
        statusCombo.select(0);
    }

    public String validateInput() {

        String library = dataLibraryText.getText();
        if (StringHelper.isNullOrEmpty(library)) {
            return Messages.The_library_name_must_be_specified;
        }

        if (!Validator.getLibraryNameInstance().validate(library)) {
            return Messages.bind(Messages.Library_name_A_is_not_valid, library);
        }

        String job = jobText.getText();
        if (StringHelper.isNullOrEmpty(job)) {
            return Messages.The_job_name_must_be_specified;
        }

        String status = statusCombo.getText();
        if (StringHelper.isNullOrEmpty(status)) {
            return Messages.The_job_status_must_be_specified;
        }

        return null;
    }

    public boolean areFieldsComplete() {

        if (validateInput() != null) {
            return false;
        }

        return true;
    }

    public String getFilterString() {

        RapidFireFilter filter = new RapidFireFilter();

        filter.setDataLibrary(dataLibraryText.getText().toUpperCase());
        filter.setJob(jobText.getText().toUpperCase());
        filter.setStatus(statusCombo.getText());

        return filter.getFilterString();
    }
}
