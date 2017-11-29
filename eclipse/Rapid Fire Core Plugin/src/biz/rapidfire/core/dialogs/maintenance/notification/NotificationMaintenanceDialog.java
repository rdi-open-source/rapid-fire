/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.notification;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.notification.INotificationCheck;
import biz.rapidfire.core.model.maintenance.notification.NotificationManager;
import biz.rapidfire.core.model.maintenance.notification.NotificationValues;
import biz.rapidfire.core.model.maintenance.notification.shared.MessageQueueLibrary;
import biz.rapidfire.core.model.maintenance.notification.shared.NotificationType;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class NotificationMaintenanceDialog extends AbstractMaintenanceDialog {

    private static final String DFT_VALUE = "dft.value";
    private static final String PREV_VALUE = "prev.value";
    private static final String EMPTY_STRING = "";

    private NotificationManager manager;

    private NotificationValues values;

    private Text textJobName;
    private Text textPosition;
    private Combo comboType;
    private Text textUser;
    private Text textMessageQueueName;
    private Combo comboMessageQueueLibraryName;

    private boolean enableParentKeyFields;
    private boolean enableKeyFields;
    private boolean enableFields;

    public static NotificationMaintenanceDialog getCreateDialog(Shell shell, NotificationManager manager) {
        return new NotificationMaintenanceDialog(shell, MaintenanceMode.MODE_CREATE, manager);
    }

    public static NotificationMaintenanceDialog getCopyDialog(Shell shell, NotificationManager manager) {
        return new NotificationMaintenanceDialog(shell, MaintenanceMode.MODE_COPY, manager);
    }

    public static NotificationMaintenanceDialog getChangeDialog(Shell shell, NotificationManager manager) {
        return new NotificationMaintenanceDialog(shell, MaintenanceMode.MODE_CHANGE, manager);
    }

    public static NotificationMaintenanceDialog getDeleteDialog(Shell shell, NotificationManager manager) {
        return new NotificationMaintenanceDialog(shell, MaintenanceMode.MODE_DELETE, manager);
    }

    public static NotificationMaintenanceDialog getDisplayDialog(Shell shell, NotificationManager manager) {
        return new NotificationMaintenanceDialog(shell, MaintenanceMode.MODE_DISPLAY, manager);
    }

    public void setValue(NotificationValues values) {
        this.values = values;
    }

    private NotificationMaintenanceDialog(Shell shell, MaintenanceMode mode, NotificationManager manager) {
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
        textPosition.setEnabled(enableKeyFields);

        Label labelType = new Label(parent, SWT.NONE);
        labelType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelType.setText(Messages.Label_NotificationType_colon);
        labelType.setToolTipText(Messages.Tooltip_NotificationType);

        comboType = WidgetFactory.createReadOnlyCombo(parent);
        setDefaultValue(comboType, NotificationType.USR.label());
        comboType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboType.setToolTipText(Messages.Tooltip_NotificationType);
        comboType.setEnabled(enableFields);
        comboType.setItems(NotificationValues.getTypeLabels());
        comboType.addSelectionListener(new NotificationChangedListener());

        Label labelUser = new Label(parent, SWT.NONE);
        labelUser.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelUser.setText(Messages.Label_User_colon);
        labelUser.setToolTipText(Messages.Tooltip_User);

        textUser = WidgetFactory.createNameText(parent);
        textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textUser.setToolTipText(Messages.Tooltip_User);
        textUser.setEnabled(enableFields);

        Label labelCopyProgramName = new Label(parent, SWT.NONE);
        labelCopyProgramName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelCopyProgramName.setText(Messages.Label_Message_queue_name_colon);
        labelCopyProgramName.setToolTipText(Messages.Tooltip_Message_queue_name);

        textMessageQueueName = WidgetFactory.createNameText(parent);
        textMessageQueueName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textMessageQueueName.setToolTipText(Messages.Tooltip_Message_queue_name);
        textMessageQueueName.setEnabled(enableFields);

        Label labelCopyProgramLibraryName = new Label(parent, SWT.NONE);
        labelCopyProgramLibraryName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelCopyProgramLibraryName.setText(Messages.Label_Message_queue_library_name_colon);
        labelCopyProgramLibraryName.setToolTipText(Messages.Tooltip_Message_queue_library_name);

        comboMessageQueueLibraryName = WidgetFactory.createNameCombo(parent);
        setDefaultValue(comboMessageQueueLibraryName, MessageQueueLibrary.LIBL.label());
        comboMessageQueueLibraryName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboMessageQueueLibraryName.setToolTipText(Messages.Tooltip_Message_queue_library_name);
        comboMessageQueueLibraryName.setEnabled(enableFields);
        comboMessageQueueLibraryName.setItems(MessageQueueLibrary.labels());
    }

    private void setDefaultValue(Control control, String defaultValue) {
        control.setData(DFT_VALUE, defaultValue);
    }

    private String getDefaultValue(Control control) {
        String value = (String)control.getData(DFT_VALUE);
        if (value != null) {
            return value;
        }
        return EMPTY_STRING;
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Notification;
    }

    @Override
    protected void setScreenValues() {

        setText(textJobName, values.getKey().getJobName());

        setText(textPosition, Integer.toString(values.getKey().getPosition()));
        setText(comboType, values.getNotificationType());
        setText(textUser, values.getUser());
        setText(textMessageQueueName, values.getMessageQueueName());

        if (NotificationType.MSGQ.equals(values.getNotificationType())) {
            setText(comboMessageQueueLibraryName, values.getMessageQueueLibraryName());
        } else {
            comboMessageQueueLibraryName.setText(values.getMessageQueueLibraryName());
        }
    }

    private void setText(Text textControl, String text) {

        if (StringHelper.isNullOrEmpty(text)) {
            String defaultValue = getDefaultValue(textControl);
            if (StringHelper.isNullOrEmpty(defaultValue)) {
                textControl.setText(EMPTY_STRING);
            } else {
                textControl.setText(defaultValue);
            }
        } else {
            textControl.setText(text);
        }
    }

    private void setText(Combo comboControl, String text) {

        if (StringHelper.isNullOrEmpty(text)) {
            String defaultValue = getDefaultValue(comboControl);
            if (StringHelper.isNullOrEmpty(defaultValue)) {
                comboControl.setText(EMPTY_STRING);
            } else {
                comboControl.setText(defaultValue);
            }
        } else {
            comboControl.setText(text);
        }
    }

    @Override
    protected void okPressed() {

        NotificationValues newValues = values.clone();
        newValues.getKey().setPosition(IntHelper.tryParseInt(textPosition.getText(), -1));
        newValues.setNotificationType(comboType.getText());
        newValues.setUser(textUser.getText());
        newValues.setMessageQueueName(textMessageQueueName.getText());
        newValues.setMessageQueueLibraryName(comboMessageQueueLibraryName.getText());

        if (!isDisplayMode()) {
            try {
                manager.setValues(newValues);
                Result result = manager.check();
                if (result.isError()) {
                    setErrorFocus(result.getFieldName());
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

    private void setErrorFocus(String fieldName) {

        if (INotificationCheck.FIELD_JOB.equals(fieldName)) {
            textJobName.setFocus();
            setErrorMessage(Messages.bind(Messages.Job_name_A_is_not_valid, textJobName.getText()));
        } else if (INotificationCheck.FIELD_POSITION.equals(fieldName)) {
            textPosition.setFocus();
            setErrorMessage(Messages.bind(Messages.Notification_position_A_is_not_valid, textPosition.getText()));
        } else if (INotificationCheck.FIELD_TYPE.equals(fieldName)) {
            comboType.setFocus();
            setErrorMessage(Messages.Notification_type_A_is_not_valid);
        } else if (INotificationCheck.FIELD_USER.equals(fieldName)) {
            textUser.setFocus();
            setErrorMessage(Messages.bind(Messages.User_name_A_is_not_valid, textUser.getText()));
        } else if (INotificationCheck.FIELD_MESSAGE_QUEUE_NAME.equals(fieldName)) {
            textMessageQueueName.setFocus();
            setErrorMessage(Messages.bind(Messages.Message_queue_name_A_is_not_valid, textMessageQueueName.getText()));
        } else if (INotificationCheck.FIELD_MESSAGE_QUEUE_LIBRARY_NAME.equals(fieldName)) {
            comboMessageQueueLibraryName.setFocus();
            setErrorMessage(Messages.bind(Messages.Library_name_A_is_not_valid, comboMessageQueueLibraryName.getText()));
        }
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

    private class NotificationChangedListener implements SelectionListener {

        public void widgetSelected(SelectionEvent event) {
            performNotificationTypeChanged();
        }

        private void performNotificationTypeChanged() {
            if (NotificationType.MSGQ.label().equals(comboType.getText())) {
                saveCurrentValue(textUser);
                restorePreviousValue(textMessageQueueName);
                restorePreviousValue(comboMessageQueueLibraryName);
            } else if (NotificationType.USR.label().equals(comboType.getText())) {
                saveCurrentValue(textMessageQueueName);
                saveCurrentValue(comboMessageQueueLibraryName);
                restorePreviousValue(textUser);
            }
        }

        public void widgetDefaultSelected(SelectionEvent event) {
            widgetSelected(event);
        }

        private void saveCurrentValue(Text textControl) {
            storeCurrentValue(textControl, textControl.getText());
            textControl.setText(EMPTY_STRING);
        }

        private void saveCurrentValue(Combo comboControl) {
            storeCurrentValue(comboControl, comboControl.getText());
            comboControl.setText(EMPTY_STRING);
        }

        private void restorePreviousValue(Text control) {
            setText(control, getPreviousValue(control));
        }

        private void restorePreviousValue(Combo control) {
            setText(control, getPreviousValue(control));
        }

        private void storeCurrentValue(Control control, String text) {
            control.setData(PREV_VALUE, text);
        }

        private String getPreviousValue(Control control) {
            String text = (String)control.getData(PREV_VALUE);
            if (text == null) {
                return EMPTY_STRING;
            }
            return text;
        }
    }
}
