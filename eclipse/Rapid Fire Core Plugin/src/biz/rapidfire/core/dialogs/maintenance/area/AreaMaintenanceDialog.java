/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.area;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.area.AreaManager;
import biz.rapidfire.core.maintenance.area.AreaValues;
import biz.rapidfire.core.maintenance.area.IAreaCheck;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;

public class AreaMaintenanceDialog extends AbstractMaintenanceDialog {

    private AreaManager manager;

    private AreaValues values;
    private AreaMaintenanceControl areaMaintenanceControl;

    private IRapidFireLibraryResource[] libraries;
    private IRapidFireLibraryListResource[] libraryLists;

    public static AreaMaintenanceDialog getCreateDialog(Shell shell, AreaManager manager) {
        return new AreaMaintenanceDialog(shell, MaintenanceMode.CREATE, manager);
    }

    public static AreaMaintenanceDialog getCopyDialog(Shell shell, AreaManager manager) {
        return new AreaMaintenanceDialog(shell, MaintenanceMode.COPY, manager);
    }

    public static AreaMaintenanceDialog getChangeDialog(Shell shell, AreaManager manager) {
        return new AreaMaintenanceDialog(shell, MaintenanceMode.CHANGE, manager);
    }

    public static AreaMaintenanceDialog getDeleteDialog(Shell shell, AreaManager manager) {
        return new AreaMaintenanceDialog(shell, MaintenanceMode.DELETE, manager);
    }

    public static AreaMaintenanceDialog getDisplayDialog(Shell shell, AreaManager manager) {
        return new AreaMaintenanceDialog(shell, MaintenanceMode.DISPLAY, manager);
    }

    public void setValue(AreaValues values) {
        this.values = values;
    }

    public AreaValues getValue() {
        return values;
    }

    public void setLibraries(IRapidFireLibraryResource[] libraries) {

        this.libraries = libraries;

        Arrays.sort(this.libraries);
    }

    public void setLibraryLists(IRapidFireLibraryListResource[] libraryLists) {

        this.libraryLists = libraryLists;

        Arrays.sort(this.libraryLists);
    }

    private AreaMaintenanceDialog(Shell shell, MaintenanceMode mode, AreaManager manager) {
        super(shell, mode);

        this.manager = manager;
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        areaMaintenanceControl = new AreaMaintenanceControl(parent, SWT.NONE);
        areaMaintenanceControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        areaMaintenanceControl.setMode(getMode());

        areaMaintenanceControl.setAreaNames(AreaValues.getAreaLabels());
        areaMaintenanceControl.setLibraryNames(getLibraries());
        areaMaintenanceControl.setLibraryListNames(getLibraryLists(AreaValues.getLibraryListSpecialValues()));
    }

    private String[] getLibraries(String... librarySpecialValues) {

        Set<String> duplicates = new HashSet<String>();

        List<String> items = new LinkedList<String>();
        for (String librarySpecialValue : librarySpecialValues) {
            checkDuplicatesAndAddItem(duplicates, librarySpecialValue, items);
        }

        if (libraries != null) {
            for (IRapidFireLibraryResource library : libraries) {
                checkDuplicatesAndAddItem(duplicates, library.getName(), items);
            }
        }

        return items.toArray(new String[items.size()]);
    }

    private String[] getLibraryLists(String... libraryListSpecialValues) {

        Set<String> duplicates = new HashSet<String>();

        List<String> items = new LinkedList<String>();
        for (String libraryListSpecialValue : libraryListSpecialValues) {
            checkDuplicatesAndAddItem(duplicates, libraryListSpecialValue, items);
        }

        if (libraryLists != null) {
            for (IRapidFireLibraryListResource libraryList : libraryLists) {
                checkDuplicatesAndAddItem(duplicates, libraryList.getName(), items);
            }
        }

        return items.toArray(new String[items.size()]);
    }

    private void checkDuplicatesAndAddItem(Set<String> duplicates, String item, List<String> items) {

        if (!duplicates.contains(item)) {
            items.add(item);
            duplicates.add(item);
        }
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Area;
    }

    @Override
    protected void setScreenValues() {

        areaMaintenanceControl.setJobName(values.getKey().getJobName());
        areaMaintenanceControl.setPosition(values.getKey().getPosition());

        areaMaintenanceControl.setAreaName(values.getKey().getArea());
        areaMaintenanceControl.setLibraryName(values.getLibrary());
        areaMaintenanceControl.setLibraryListName(values.getLibraryList());
        areaMaintenanceControl.setLibraryCcsid(values.getLibraryCcsid());
        areaMaintenanceControl.setCommandExtension(values.getCommandExtension());
    }

    @Override
    protected void okPressed() {

        AreaValues newValues = values.clone();
        newValues.getKey().setArea(areaMaintenanceControl.getAreaName());
        newValues.setLibrary(areaMaintenanceControl.getLibraryName());
        newValues.setLibraryList(areaMaintenanceControl.getLibraryListName());
        newValues.setLibraryCcsid(areaMaintenanceControl.getLibraryCcsid());
        newValues.setCommandExtension(areaMaintenanceControl.getCommandExtension());

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
            areaMaintenanceControl.setFocusArea();
            message = Messages.bind(Messages.Area_name_A_is_not_valid, areaMaintenanceControl.getAreaName());
        } else if (IAreaCheck.FIELD_LIBRARY.equals(fieldName)) {
            areaMaintenanceControl.setFocusLibrary();
            message = Messages.bind(Messages.Library_name_A_is_not_valid, areaMaintenanceControl.getLibraryName());
        } else if (IAreaCheck.FIELD_LIBRARY_LIST.equals(fieldName)) {
            areaMaintenanceControl.setFocusLibraryList();
            message = Messages.bind(Messages.Library_list_name_A_is_not_valid, areaMaintenanceControl.getLibraryListName());
        } else if (IAreaCheck.FIELD_LIBRARY_CCSID.equals(fieldName)) {
            areaMaintenanceControl.setFocusLibraryCcsid();
            message = Messages.bind(Messages.Ccsid_A_is_not_valid, areaMaintenanceControl.getLibraryCcsid());
        } else if (IAreaCheck.FIELD_COMMAND_EXTENSION.equals(fieldName)) {
            areaMaintenanceControl.setFocusCommandExtension();
            message = Messages.bind(Messages.Command_extension_A_is_not_valid, areaMaintenanceControl.getCommandExtension());
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
