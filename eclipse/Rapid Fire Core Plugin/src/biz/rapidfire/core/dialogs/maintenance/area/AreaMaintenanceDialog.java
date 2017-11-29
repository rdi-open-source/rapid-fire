/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.area;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.area.AreaManager;
import biz.rapidfire.core.model.maintenance.area.AreaValues;
import biz.rapidfire.core.model.maintenance.area.IAreaCheck;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class AreaMaintenanceDialog extends AbstractMaintenanceDialog {

    private AreaManager manager;

    private AreaValues values;

    private Text textJobName;
    private Text textPosition;
    private Combo comboArea;
    private Text textLibrary;
    private Combo comboLibraryList;
    private Combo comboLibraryCcsid;
    private Text textCommandExtension;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

    public static AreaMaintenanceDialog getCreateDialog(Shell shell, AreaManager manager) {
        return new AreaMaintenanceDialog(shell, MaintenanceMode.MODE_CREATE, manager);
    }

    public static AreaMaintenanceDialog getCopyDialog(Shell shell, AreaManager manager) {
        return new AreaMaintenanceDialog(shell, MaintenanceMode.MODE_COPY, manager);
    }

    public static AreaMaintenanceDialog getChangeDialog(Shell shell, AreaManager manager) {
        return new AreaMaintenanceDialog(shell, MaintenanceMode.MODE_CHANGE, manager);
    }

    public static AreaMaintenanceDialog getDeleteDialog(Shell shell, AreaManager manager) {
        return new AreaMaintenanceDialog(shell, MaintenanceMode.MODE_DELETE, manager);
    }

    public static AreaMaintenanceDialog getDisplayDialog(Shell shell, AreaManager manager) {
        return new AreaMaintenanceDialog(shell, MaintenanceMode.MODE_DISPLAY, manager);
    }

    public void setValue(AreaValues values) {
        this.values = values;
    }

    private AreaMaintenanceDialog(Shell shell, MaintenanceMode mode, AreaManager manager) {
        super(shell, mode);

        this.manager = manager;

        if (MaintenanceMode.MODE_CREATE.equals(mode) || MaintenanceMode.MODE_COPY.equals(mode)) {
            enableParentKeyFields = false;
            enableKeyFields = true;
            enableFields = true;
        } else if (MaintenanceMode.MODE_CHANGE.equals(mode)) {
            enableParentKeyFields = false;
            enableKeyFields = false;
            enableFields = true;
        } else {
            enableParentKeyFields = false;
            enableKeyFields = false;
            enableFields = false;
        }
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        Label labelJobName = new Label(parent, SWT.NONE);
        labelJobName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelJobName.setText(Messages.Label_Job_colon);
        labelJobName.setToolTipText(Messages.Tooltip_Job);

        textJobName = WidgetFactory.createNameText(parent);
        textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobName.setToolTipText(Messages.Tooltip_Job);
        textJobName.setEnabled(enableParentKeyFields);

        Label labelPosition = new Label(parent, SWT.NONE);
        labelPosition.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelPosition.setText(Messages.Label_Position_colon);
        labelPosition.setToolTipText(Messages.Tooltip_Position);

        textPosition = WidgetFactory.createIntegerText(parent);
        textPosition.setTextLimit(6);
        textPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textPosition.setToolTipText(Messages.Tooltip_Position);
        textPosition.setEnabled(enableParentKeyFields);

        Label labelArea = new Label(parent, SWT.NONE);
        labelArea.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelArea.setText(Messages.Label_Area_colon);
        labelArea.setToolTipText(Messages.Tooltip_Area);

        comboArea = WidgetFactory.createNameCombo(parent);
        comboArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboArea.setToolTipText(Messages.Tooltip_Area);
        comboArea.setEnabled(enableKeyFields);
        comboArea.setItems(AreaValues.getAreaLabels());

        Label labelLibrary = new Label(parent, SWT.NONE);
        labelLibrary.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelLibrary.setText(Messages.Label_Area_library_colon);
        labelLibrary.setToolTipText(Messages.Tooltip_Area_library);

        textLibrary = WidgetFactory.createNameText(parent);
        textLibrary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textLibrary.setToolTipText(Messages.Tooltip_Area_library);
        textLibrary.setEnabled(enableFields);

        Label labelLibraryList = new Label(parent, SWT.NONE);
        labelLibraryList.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelLibraryList.setText(Messages.Label_Area_library_list_colon);
        labelLibraryList.setToolTipText(Messages.Tooltip_Area_library_list);

        comboLibraryList = WidgetFactory.createNameCombo(parent);
        comboLibraryList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboLibraryList.setToolTipText(Messages.Tooltip_Area_library_list);
        comboLibraryList.setEnabled(enableFields);
        comboLibraryList.setItems(AreaValues.getLibraryListSpecialValues());

        Label labelLibraryCcsid = new Label(parent, SWT.NONE);
        labelLibraryCcsid.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelLibraryCcsid.setText(Messages.Label_Area_library_ccsid);
        labelLibraryCcsid.setToolTipText(Messages.Tooltip_Area_library_ccsid);

        comboLibraryCcsid = WidgetFactory.createNameCombo(parent);
        comboLibraryCcsid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboLibraryCcsid.setToolTipText(Messages.Tooltip_Area_library_ccsid);
        comboLibraryCcsid.setEnabled(enableFields);
        comboLibraryCcsid.setItems(AreaValues.getCcsidSpecialValues());

        Label labelCommandExtension = new Label(parent, SWT.NONE);
        labelCommandExtension.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelCommandExtension.setText(Messages.Label_Command_extension_colon);
        labelCommandExtension.setToolTipText(Messages.Tooltip_Command_extension);

        textCommandExtension = WidgetFactory.createNameText(parent);
        textCommandExtension.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textCommandExtension.setToolTipText(Messages.Tooltip_Command_extension);
        textCommandExtension.setEnabled(enableFields);
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Area;
    }

    protected void setScreenValues() {

        textJobName.setText(values.getKey().getJobName());
        textPosition.setText(Integer.toString(values.getKey().getPosition()));

        comboArea.setText(values.getKey().getArea());
        textLibrary.setText(values.getLibrary());
        comboLibraryList.setText(values.getLibraryList());
        comboLibraryCcsid.setText(values.getLibraryCcsid());
        textCommandExtension.setText(values.getCommandExtension());
    }

    @Override
    protected void okPressed() {

        AreaValues newValues = values.clone();
        newValues.getKey().setArea(comboArea.getText());
        newValues.setLibrary(textLibrary.getText());
        newValues.setLibraryList(comboLibraryList.getText());
        newValues.setLibraryCcsid(comboLibraryCcsid.getText());

        if (!isDisplayMode()) {
            try {
                manager.setValues(newValues);
                Result result = manager.check();
                if (result.isError()) {
                    setErrorFocus(result);
                    return;
                }
            } catch (Exception e) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
                return;
            }
        }

        values = newValues;

        super.okPressed();
    }

    private void setErrorFocus(Result result) {

        String fieldName = result.getFieldName();
        String message = null;

        if (IAreaCheck.FIELD_AREA.equals(fieldName)) {
            comboArea.setFocus();
            message = Messages.bind(Messages.Area_name_A_is_not_valid, comboArea.getText());
        } else if (IAreaCheck.FIELD_LIBRARY.equals(fieldName)) {
            textLibrary.setFocus();
            message = Messages.bind(Messages.Library_name_A_is_not_valid, textLibrary.getText());
        } else if (IAreaCheck.FIELD_LIBRARY_LIST.equals(fieldName)) {
            comboLibraryList.setFocus();
            message = Messages.bind(Messages.Library_list_name_A_is_not_valid, comboLibraryList.getText());
        } else if (IAreaCheck.FIELD_LIBRARY_CCSID.equals(fieldName)) {
            comboLibraryCcsid.setFocus();
            message = Messages.bind(Messages.Ccsid_A_is_not_valid, comboLibraryCcsid.getText());
        } else if (IAreaCheck.FIELD_COMMAND_EXTENSION.equals(fieldName)) {
            textCommandExtension.setFocus();
            message = Messages.bind(Messages.Command_extension_A_is_not_valid, textCommandExtension.getText());
        }

        setErrorMessage(message, result);
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
