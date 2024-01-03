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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.notification.INotificationCheck;
import biz.rapidfire.core.maintenance.notification.NotificationManager;
import biz.rapidfire.core.maintenance.notification.NotificationValues;
import biz.rapidfire.core.maintenance.notification.shared.MessageQueueLibrary;
import biz.rapidfire.core.maintenance.notification.shared.NotificationType;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class NotificationMaintenanceDialog extends AbstractMaintenanceDialog {

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
        return new NotificationMaintenanceDialog(shell, MaintenanceMode.CREATE, manager);
    }

    public static NotificationMaintenanceDialog getCopyDialog(Shell shell, NotificationManager manager) {
        return new NotificationMaintenanceDialog(shell, MaintenanceMode.COPY, manager);
    }

    public static NotificationMaintenanceDialog getChangeDialog(Shell shell, NotificationManager manager) {
        return new NotificationMaintenanceDialog(shell, MaintenanceMode.CHANGE, manager);
    }

    public static NotificationMaintenanceDialog getDeleteDialog(Shell shell, NotificationManager manager) {
        return new NotificationMaintenanceDialog(shell, MaintenanceMode.DELETE, manager);
    }

    public static NotificationMaintenanceDialog getDisplayDialog(Shell shell, NotificationManager manager) {
        return new NotificationMaintenanceDialog(shell, MaintenanceMode.DISPLAY, manager);
    }

    public void setValue(NotificationValues values) {
        this.values = values;
    }

    public NotificationValues getValue() {
        return values;
    }

    private NotificationMaintenanceDialog(Shell shell, MaintenanceMode mode, NotificationManager manager) {
        super(shell, mode);

        this.manager = manager;

        if (MaintenanceMode.CREATE.equals(mode) || MaintenanceMode.COPY.equals(mode)) {
            enableParentKeyFields = false;
            enableKeyFields = true;
            enableFields = true;
        } else if (MaintenanceMode.CHANGE.equals(mode)) {
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
        textPosition.setEnabled(enableKeyFields);

        WidgetFactory.createLabel(parent, Messages.Label_NotificationType_colon, Messages.Tooltip_NotificationType);

        comboType = WidgetFactory.createReadOnlyCombo(parent);
        setDefaultValue(comboType, NotificationType.USR.label());
        comboType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboType.setToolTipText(Messages.Tooltip_NotificationType);
        comboType.setEnabled(enableFields);
        comboType.setItems(NotificationValues.getTypeLabels());
        comboType.addSelectionListener(new NotificationChangedListener());

        WidgetFactory.createLabel(parent, Messages.Label_User_colon, Messages.Tooltip_User);

        textUser = WidgetFactory.createNameText(parent);
        textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textUser.setToolTipText(Messages.Tooltip_User);
        textUser.setEnabled(enableFields);

        WidgetFactory.createLabel(parent, Messages.Label_Message_queue_name_colon, Messages.Tooltip_Message_queue_name);

        textMessageQueueName = WidgetFactory.createNameText(parent);
        textMessageQueueName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textMessageQueueName.setToolTipText(Messages.Tooltip_Message_queue_name);
        textMessageQueueName.setEnabled(enableFields);

        WidgetFactory.createLabel(parent, Messages.Label_Message_queue_library_name_colon, Messages.Tooltip_Message_queue_library_name);

        comboMessageQueueLibraryName = WidgetFactory.createNameCombo(parent);
        setDefaultValue(comboMessageQueueLibraryName, MessageQueueLibrary.LIBL.label());
        comboMessageQueueLibraryName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboMessageQueueLibraryName.setToolTipText(Messages.Tooltip_Message_queue_library_name);
        comboMessageQueueLibraryName.setEnabled(enableFields);
        comboMessageQueueLibraryName.setItems(MessageQueueLibrary.labels());

        comboType.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
                updateControlEnablement();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });
    }

    private void updateControlEnablement() {

        if (NotificationType.MSGQ.label().equals(comboType.getText())) {
            textUser.setEnabled(false);
            textMessageQueueName.setEnabled(enableFields);
            comboMessageQueueLibraryName.setEnabled(enableFields);
        } else if (NotificationType.USR.label().equals(comboType.getText())) {
            textUser.setEnabled(enableFields);
            textMessageQueueName.setEnabled(false);
            comboMessageQueueLibraryName.setEnabled(false);
        }
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
            // intentionally calling setText() directly!
            comboMessageQueueLibraryName.setText(values.getMessageQueueLibraryName());
        }

        updateControlEnablement();
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
    }
}
