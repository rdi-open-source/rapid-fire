/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.area;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceControl;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.area.AreaValues;
import biz.rapidfire.core.maintenance.area.shared.Ccsid;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class AreaMaintenanceControl extends AbstractMaintenanceControl {

    private Text textJobName;
    private Text textPosition;

    private Combo comboArea;
    private Combo comboLibrary;
    private Combo comboLibraryList;
    private Combo comboLibraryCcsid;
    private Text textCommandExtension;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

    public AreaMaintenanceControl(Composite parent, int style) {
        super(parent, style, true);
    }

    public AreaMaintenanceControl(Composite parent, boolean parentKeyFieldsVisible, int style) {
        super(parent, style, parentKeyFieldsVisible);
    }

    public void setFocusJobName() {

        if (isParentKeyFieldsVisible()) {
            textJobName.setFocus();
        }
    }

    public void setFocusPosition() {

        if (isParentKeyFieldsVisible()) {
            textPosition.setFocus();
        }
    }

    public void setFocusArea() {
        comboArea.setFocus();
    }

    public void setFocusLibrary() {
        comboLibrary.setFocus();
    }

    public void setFocusLibraryList() {
        comboLibraryList.setFocus();
    }

    public void setFocusLibraryCcsid() {
        comboLibraryCcsid.setFocus();
    }

    public void setFocusCommandExtension() {
        textCommandExtension.setFocus();
    }

    @Override
    public void setMode(MaintenanceMode mode) {

        super.setMode(mode);

        if (isParentKeyFieldsVisible()) {
            textJobName.setEnabled(isParentKeyFieldsEnabled());
            textPosition.setEnabled(isParentKeyFieldsEnabled());
        }

        comboArea.setEnabled(isKeyFieldsEnabled());

        comboLibrary.setEnabled(isFieldsEnabled());
        comboLibraryList.setEnabled(isFieldsEnabled());
        comboLibraryCcsid.setEnabled(isFieldsEnabled());
        textCommandExtension.setEnabled(isFieldsEnabled());
    }

    @Override
    protected void createContent(Composite parent) {

        if (isParentKeyFieldsVisible()) {

            WidgetFactory.createLabel(parent, Messages.Label_Job_colon, Messages.Tooltip_Job);

            textJobName = WidgetFactory.createNameText(parent);
            textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            textJobName.setToolTipText(Messages.Tooltip_Job);
            textJobName.setEnabled(enableParentKeyFields);

            WidgetFactory.createLabel(parent, Messages.Label_Position_colon, Messages.Tooltip_Position);

            textPosition = WidgetFactory.createIntegerText(parent);
            textPosition.setTextLimit(6);
            textPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            textPosition.setToolTipText(Messages.Tooltip_Position);
            textPosition.setEnabled(enableParentKeyFields);
        }

        WidgetFactory.createLabel(parent, Messages.Label_Area_colon, Messages.Tooltip_Area);

        comboArea = WidgetFactory.createNameCombo(parent);
        comboArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboArea.setToolTipText(Messages.Tooltip_Area);
        comboArea.setEnabled(enableKeyFields);
        comboArea.setItems(AreaValues.getAreaLabels());

        WidgetFactory.createLabel(parent, Messages.Label_Area_library_colon, Messages.Tooltip_Area_library);

        comboLibrary = WidgetFactory.createReadOnlyCombo(parent);
        comboLibrary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboLibrary.setToolTipText(Messages.Tooltip_Area_library);
        comboLibrary.setEnabled(enableFields);

        WidgetFactory.createLabel(parent, Messages.Label_Area_library_list_colon, Messages.Tooltip_Area_library_list);

        comboLibraryList = WidgetFactory.createReadOnlyCombo(parent);
        comboLibraryList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboLibraryList.setToolTipText(Messages.Tooltip_Area_library_list);
        comboLibraryList.setEnabled(enableFields);

        WidgetFactory.createLabel(parent, Messages.Label_Area_library_ccsid, Messages.Tooltip_Area_library_ccsid);

        comboLibraryCcsid = WidgetFactory.createCombo(parent);
        setDefaultValue(comboLibraryCcsid, Ccsid.JOB.label());
        comboLibraryCcsid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboLibraryCcsid.setToolTipText(Messages.Tooltip_Area_library_ccsid);
        comboLibraryCcsid.setEnabled(enableFields);
        comboLibraryCcsid.setItems(AreaValues.getCcsidSpecialValues());

        WidgetFactory.createLabel(parent, Messages.Label_Command_extension_colon, Messages.Tooltip_Command_extension);

        textCommandExtension = WidgetFactory.createText(parent);
        textCommandExtension.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textCommandExtension.setToolTipText(Messages.Tooltip_Command_extension);
        textCommandExtension.setEnabled(enableFields);
    }

    public String getJobName() {

        if (isParentKeyFieldsVisible()) {
            return textJobName.getText();
        } else {
            return null;
        }
    }

    public void setJobName(String jobName) {

        if (isParentKeyFieldsVisible()) {
            setText(textJobName, jobName);
        }
    }

    public String getPosition() {

        if (isParentKeyFieldsVisible()) {
            return textPosition.getText();
        } else {
            return null;
        }
    }

    public void setPosition(int position) {

        if (isParentKeyFieldsVisible()) {
            setText(textPosition, Integer.toString(position));
        }
    }

    public String getAreaName() {
        return comboArea.getText();
    }

    public void setAreaName(String areaName) {
        setText(comboArea, areaName);
    }

    public void setAreaNames(String[] areaNames) {
        comboArea.setItems(areaNames);
    }

    public String getLibraryName() {
        return comboLibrary.getText();
    }

    public void setLibraryName(String libraryName) {
        setText(comboLibrary, libraryName);
    }

    public void setLibraryNames(String[] libraryNames) {
        String libraryName = comboLibrary.getText();
        comboLibrary.setItems(libraryNames);
        comboLibrary.setText(libraryName);
    }

    public String getLibraryListName() {
        return comboLibraryList.getText();
    }

    public void setLibraryListName(String libraryListName) {
        setText(comboLibraryList, libraryListName);
    }

    public void setLibraryListNames(String[] libraryListNames) {
        String libraryListName = comboLibraryList.getText();
        comboLibraryList.setItems(libraryListNames);
        comboLibraryList.setText(libraryListName);
    }

    public String getLibraryCcsid() {
        return comboLibraryCcsid.getText();
    }

    public void setLibraryCcsid(String libraryCcsid) {
        setText(comboLibraryCcsid, libraryCcsid);
    }

    public String getCommandExtension() {
        return textCommandExtension.getText();
    }

    public void setCommandExtension(String commandExtension) {
        setText(textCommandExtension, commandExtension);
    }

    public void addSelectionListener(SelectionListener listener) {

        comboArea.addSelectionListener(listener);
        comboLibrary.addSelectionListener(listener);
        comboLibraryCcsid.addSelectionListener(listener);
        comboLibraryList.addSelectionListener(listener);
    }

    public void removeSelectionListener(SelectionListener listener) {

        comboArea.removeSelectionListener(listener);
        comboLibrary.removeSelectionListener(listener);
        comboLibraryCcsid.removeSelectionListener(listener);
        comboLibraryList.removeSelectionListener(listener);
    }

    public void addModifyListener(ModifyListener listener) {

        if (isParentKeyFieldsVisible()) {
            textJobName.addModifyListener(listener);
            textPosition.addModifyListener(listener);
        }

        comboArea.addModifyListener(listener);
        comboLibrary.addModifyListener(listener);
        comboLibraryCcsid.addModifyListener(listener);
        comboLibraryList.addModifyListener(listener);

        textCommandExtension.addModifyListener(listener);
    }

    public void removeModifyListener(ModifyListener listener) {

        if (isParentKeyFieldsVisible()) {
            textJobName.removeModifyListener(listener);
            textPosition.removeModifyListener(listener);
        }

        comboArea.removeModifyListener(listener);
        comboLibrary.removeModifyListener(listener);
        comboLibraryCcsid.removeModifyListener(listener);
        comboLibraryList.removeModifyListener(listener);

        textCommandExtension.removeModifyListener(listener);
    }
}
