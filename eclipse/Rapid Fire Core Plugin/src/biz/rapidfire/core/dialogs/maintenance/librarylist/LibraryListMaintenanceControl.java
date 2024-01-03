/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.librarylist;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceControl;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.core.swt.widgets.listeditors.librarylist.LibraryListEditor;
import biz.rapidfire.core.swt.widgets.listeditors.librarylist.LibraryListItem;

public class LibraryListMaintenanceControl extends AbstractMaintenanceControl {

    private Text textJobName;
    private Text textLibraryList;
    private Text textDescription;
    private LibraryListEditor editorLibraryList;

    private boolean isParentKeyFieldsVisible;

    public LibraryListMaintenanceControl(Composite parent, int style) {
        super(parent, SWT.NONE, true);
    }

    public LibraryListMaintenanceControl(Composite parent, boolean parentKeyFieldsVisible, int style) {
        super(parent, style, parentKeyFieldsVisible);
    }

    public void setFocusJobName() {
        textJobName.setFocus();
    }

    public void setFocusLibraryListName() {
        textLibraryList.setFocus();
    }

    public void setFocusDescription() {
        textDescription.setFocus();
    }

    public void setFocusLibraryListEditor() {
        editorLibraryList.setFocus();
    }

    public void setFocusLibraryListEditor(int index) {
        editorLibraryList.setFocus(index);
    }

    public void setParentKeyFieldsVisible(boolean enabled) {
        this.isParentKeyFieldsVisible = enabled;
    }

    @Override
    public void setMode(MaintenanceMode mode) {

        super.setMode(mode);

        if (isParentKeyFieldsVisible) {
            textJobName.setEnabled(isParentKeyFieldsEnabled());
        }

        textLibraryList.setEnabled(isKeyFieldsEnabled());
        textDescription.setEnabled(isFieldsEnabled());
        editorLibraryList.setEnabled(isFieldsEnabled());
    }

    @Override
    protected void createContent(Composite parent) {

        if (isParentKeyFieldsVisible) {

            WidgetFactory.createLabel(parent, Messages.Label_Job_colon, Messages.Tooltip_Job);

            textJobName = WidgetFactory.createNameText(parent);
            textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            textJobName.setToolTipText(Messages.Tooltip_Job);
        }

        WidgetFactory.createLabel(parent, Messages.Label_Library_list_colon, Messages.Tooltip_Library_list);

        textLibraryList = WidgetFactory.createNameText(parent);
        textLibraryList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textLibraryList.setToolTipText(Messages.Tooltip_Library_list);

        WidgetFactory.createLabel(parent, Messages.Label_Description_colon, Messages.Tooltip_Description);

        textDescription = WidgetFactory.createText(parent);
        textDescription.setTextLimit(IRapidFireLibraryListResource.DESCRIPTION_MAX_LENGTH);
        textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textDescription.setToolTipText(Messages.Tooltip_Description);

        Group groupLibraryList = new Group(parent, SWT.NONE);
        groupLibraryList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        groupLibraryList.setLayout(new GridLayout(1, false));
        groupLibraryList.setText(Messages.Label_Library_list_colon);
        groupLibraryList.setToolTipText(Messages.Tooltip_Library_list);

        editorLibraryList = WidgetFactory.createLibraryListEditor(groupLibraryList);
        editorLibraryList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        editorLibraryList.setToolTipText(Messages.Tooltip_Library_list);
        editorLibraryList.setEnableLowerCase(true);
    }

    public String getJobName() {
        return textJobName.getText();
    }

    public void setJobName(String jobName) {

        if (isParentKeyFieldsVisible) {
            textJobName.setText(jobName);
        }
    }

    public String getLibraryListName() {
        return textLibraryList.getText();
    }

    public void setLibraryListName(String libraryListName) {
        textLibraryList.setText(libraryListName);
    }

    public String getDescription() {
        return textDescription.getText();
    }

    public void setDescription(String description) {
        textDescription.setText(description);
    }

    public int getLibrariesCount() {
        return editorLibraryList.getItemCount();
    }

    public LibraryListItem[] getLibraries() {
        return editorLibraryList.getItems();
    }

    public LibraryListItem getLibrary(int index) {
        return editorLibraryList.getItem(index);
    }

    public void setLibraries(LibraryListItem[] libraries) {
        editorLibraryList.setItems(libraries);
    }

    public void addSelectionListener(SelectionListener listener) {
        editorLibraryList.addSelectionListener(listener);
    }

    public void removeSelectionListener(SelectionListener listener) {
        editorLibraryList.removeSelectionListener(listener);
    }

    public void addModifyListener(ModifyListener listener) {

        if (isParentKeyFieldsVisible) {
            textJobName.addModifyListener(listener);
        }

        textLibraryList.addModifyListener(listener);
        textDescription.addModifyListener(listener);
    }

    public void removeModifyListener(ModifyListener listener) {

        if (isParentKeyFieldsVisible) {
            textJobName.removeModifyListener(listener);
        }

        textLibraryList.removeModifyListener(listener);
        textDescription.removeModifyListener(listener);
    }
}
