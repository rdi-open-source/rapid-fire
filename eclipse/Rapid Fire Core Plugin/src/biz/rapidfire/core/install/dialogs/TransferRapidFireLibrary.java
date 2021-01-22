/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.install.dialogs;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.install.ProductLibraryUploader;
import biz.rapidfire.core.handlers.install.StatusMessageReceiver;
import biz.rapidfire.core.helpers.ClipboardHelper;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;
import biz.rapidfire.rsebase.swt.widgets.SystemHostCombo;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.SecureAS400;

public class TransferRapidFireLibrary extends Shell implements StatusMessageReceiver {

    private static final String COMBO_CONNECTIONS = "comboConnections"; //$NON-NLS-1$
    private static final String BUTTON_START = "buttonStart"; //$NON-NLS-1$
    private static final String BUTTON_START_JOURNALING = "buttonStartJournaling"; //$NON-NLS-1$
    private static final String BUTTON_CLOSE = "buttonClose"; //$NON-NLS-1$
    private static final String BUTTON_JOB_LOG = "buttonJobLog"; //$NON-NLS-1$

    private AS400 as400;
    private CommandCall commandCall;
    private Table tableStatus;
    private SystemHostCombo comboConnections;
    private Composite buttonPanel;
    private Button buttonStart;
    private Button buttonStartJournaling;
    private Button buttonClose;
    private Button buttonJobLog;
    private String rapidFireLibrary;
    private String aspGroup;
    private int ftpPort;
    private String connectionName;

    private Map<String, Boolean> controlStatus = new HashMap<String, Boolean>();

    public TransferRapidFireLibrary(Display display, int style, String rapidFireLibrary, String aASPGroup, String aConnectionName, int aFtpPort) {
        super(display, style);

        setImage(RapidFireCorePlugin.getDefault().getImageRegistry().get(RapidFireCorePlugin.IMAGE_TRANSFER_LIBRARY));

        this.rapidFireLibrary = rapidFireLibrary;
        this.aspGroup = aASPGroup;
        this.connectionName = aConnectionName;
        setFtpPort(aFtpPort);

        createContents();

        addListener(SWT.Close, new Listener() {
            public void handleEvent(Event event) {

                try {
                    disconnectSystem();
                } catch (Throwable e) {

                }

                if (!buttonClose.isDisposed()) {
                    event.doit = buttonClose.isEnabled();
                } else {
                    event.doit = true;
                }
            }
        });

        addShellListener(new ShellAdapter() {
            public void shellClosed(ShellEvent arg0) {
                if (as400 != null) {
                    as400.disconnectAllServices();
                    as400 = null;
                    commandCall = null;
                }
            }
        });
    }

    private void setFtpPort(int aFtpPort) {
        if (aFtpPort <= 0) {
            ftpPort = Preferences.getInstance().getDefaultFtpPortNumber();
        } else {
            ftpPort = aFtpPort;
        }
    }

    protected void createContents() {

        GridLayout gl_shell = new GridLayout(2, false);
        gl_shell.marginTop = 10;
        gl_shell.verticalSpacing = 10;
        setLayout(gl_shell);

        setText(Messages.DialogTitle_Transfer_Rapid_Fire_library);
        setSize(500, 400);

        comboConnections = WidgetFactory.createSystemHostCombo(this, SWT.NONE);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;

        comboConnections.setLayoutData(gridData);
        comboConnections.selectConnection(connectionName);
        comboConnections.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent arg0) {
                disconnectSystem();
                connectionName = comboConnections.getText();
                clearStatus();
                showConnectionProperties();
            }
        });

        buttonStart = WidgetFactory.createPushButton(this);
        buttonStart.addSelectionListener(new TransferLibrarySelectionAdapter());
        buttonStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        buttonStart.setText(Messages.ActionLabel_Start_Transfer);

        buttonStartJournaling = WidgetFactory.createCheckbox(this, Messages.Label_Start_journaling_Rapid_Fire_files,
            Messages.Tooltip_Start_journaling_Rapid_Fire_files, SWT.RIGHT);
        buttonStartJournaling.setSelection(Preferences.getInstance().isStartJournaling());
        buttonStartJournaling.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                Preferences.getInstance().setStartJournaling(buttonStartJournaling.getSelection());
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });

        Link lnkHelp = new Link(this, SWT.NONE);
        lnkHelp.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
        lnkHelp.setText(Messages.bindParameters(Messages.Label_Start_journaling_Rapid_Fire_files_help, "<a>", "</a>")); //$NON-NLS-1$ //$NON-NLS-2$
        lnkHelp.pack();
        lnkHelp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                PlatformUI.getWorkbench().getHelpSystem()
                    .displayHelpResource("/biz.rapidfire.help/html/install/install_library.html#before_you_begin"); //$NON-NLS-1$
            }
        });

        tableStatus = new Table(this, SWT.BORDER | SWT.MULTI);
        final GridData gd_tableStatus = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        tableStatus.setLayoutData(gd_tableStatus);

        final TableColumn columnStatus = new TableColumn(tableStatus, SWT.NONE);
        columnStatus.setWidth(getSize().x);

        tableStatus.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent event) {
                Table table = (Table)event.getSource();
                if (table.getClientArea().width > 0) {
                    // Resize the column to the width of the table
                    columnStatus.setWidth(table.getClientArea().width);
                }
            }
        });

        tableStatus.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent event) {
            }

            public void keyPressed(KeyEvent event) {
                if ((event.stateMask & SWT.CTRL) != SWT.CTRL) {
                    return;
                }
                if (event.keyCode == 'a') {
                    tableStatus.selectAll();
                }
                if (event.keyCode == 'c') {
                    copyStatusLinesToClipboard(tableStatus.getSelection());
                }
            }
        });

        tableStatus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                if (e.button == 1) {
                    copyStatusLinesToClipboard(tableStatus.getSelection());
                }
            }
        });

        Menu menuTableStatusContextMenu = new Menu(tableStatus);
        menuTableStatusContextMenu.addMenuListener(new TableContextMenu(tableStatus));
        tableStatus.setMenu(menuTableStatusContextMenu);

        buttonPanel = createButtons(false);

        clearStatus();
        showConnectionProperties();
    }

    private void showConnectionProperties() {

        if (StringHelper.isNullOrEmpty(connectionName)) {
            setStatus(Messages.Please_select_a_connection);
            return;
        }

        if (as400 == null) {
            // setStatus(Messages.bind(Messages.Not_yet_connected_to_A,
            // connectionName));
            setStatus(Messages.bind(Messages.About_to_transfer_library_A_ASP_group_D_to_host_B_using_port_C, new Object[] { rapidFireLibrary,
                connectionName, ftpPort, aspGroup }));
        } else {

            try {
                setStatus(Messages.Server_job_colon + " " + commandCall.getServerJob().toString()); //$NON-NLS-1$
            } catch (Throwable e) {
            }

            setStatus(Messages.bind(Messages.About_to_transfer_library_A_ASP_group_D_to_host_B_using_port_C, new Object[] { rapidFireLibrary,
                connectionName, ftpPort, aspGroup }));
        }

        if (StringHelper.isNullOrEmpty(connectionName)) {
            comboConnections.setFocus();
        } else {
            buttonStart.setFocus();
        }
    }

    protected void copyStatusLinesToClipboard(TableItem[] tableItems) {

        if (tableItems.length == 1) {
            copyStatusLineToClipboard();
        } else {
            ClipboardHelper.setTableItemsText(tableItems);
        }
    }

    protected void copyStatusLineToClipboard() {

        TableItem[] tableItems = tableStatus.getSelection();
        if (tableItems != null && tableItems.length >= 1) {
            String text = tableItems[0].getText();
            if (text.startsWith(Messages.Server_job_colon)) {
                text = text.substring(Messages.Server_job_colon.length());
            }
            ClipboardHelper.setText(text.trim());
        }
    }

    private Composite createButtons(boolean printJobLogButton) {

        Composite buttonPanel = new Composite(this, SWT.NONE);
        GridLayout buttonPanelLayout = new GridLayout(1, true);
        buttonPanel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
        buttonPanelLayout.marginHeight = 0;
        buttonPanelLayout.marginWidth = 0;
        buttonPanel.setLayout(buttonPanelLayout);

        if (printJobLogButton) {
            createButtonPrintJobLog(buttonPanel);
        }

        createButtonClose(buttonPanel);

        return buttonPanel;
    }

    private void createButtonPrintJobLog(Composite buttonPanel) {

        GridLayout buttonPanelLayout = (GridLayout)buttonPanel.getLayout();
        buttonPanelLayout.numColumns++;

        buttonJobLog = WidgetFactory.createPushButton(buttonPanel);
        buttonJobLog.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        buttonJobLog.setText(Messages.ActionLabel_Print_job_log);
        buttonJobLog.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                printJobLog();
            }
        });
    }

    private void createButtonClose(Composite buttonPanel) {
        buttonClose = WidgetFactory.createPushButton(buttonPanel);
        buttonClose.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        buttonClose.setText(Messages.ActionLabel_Close);
        buttonClose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                close();
            }
        });
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    private void clearStatus() {
        tableStatus.removeAll();
        tableStatus.update();
    }

    public void setStatus(String status) {
        TableItem itemStatus = new TableItem(tableStatus, SWT.BORDER);
        itemStatus.setText(status);
        tableStatus.update();
        redraw();
    }

    public void setErrorStatus(String status) {
        setStatus("!!!   " + status + "   !!!");
    }

    private void printJobLog() {

        String cpfMsg = executeCommand("DSPJOBLOG JOB(*) OUTPUT(*PRINT)", true);
        if (cpfMsg.equals("")) {
            setStatus(Messages.Job_log_has_been_printed);
        }
    }

    private String executeCommand(String command, boolean logError) {
        try {
            commandCall.run(command);
            AS400Message[] messageList = commandCall.getMessageList();
            if (messageList.length > 0) {
                AS400Message escapeMessage = null;
                for (int idx = 0; idx < messageList.length; idx++) {
                    if (messageList[idx].getType() == AS400Message.ESCAPE) {
                        escapeMessage = messageList[idx];
                    }
                }
                if (escapeMessage != null) {
                    if (logError) {
                        for (int idx = 0; idx < messageList.length; idx++) {
                            setStatus(messageList[idx].getID() + ": " + messageList[idx].getText());
                        }
                    }
                    return escapeMessage.getID();
                }
            }
            return "";
        } catch (Exception e) {
            return "CPF0000";
        }
    }

    private class TransferLibrarySelectionAdapter extends SelectionAdapter {

        @Override
        public void widgetSelected(final SelectionEvent event) {

            disableControls();

            boolean successfullyTransfered = false;

            try {

                clearStatus();
                if (as400 == null) {
                    setStatus(Messages.bind(Messages.Connecting_to_A, connectionName));
                    if (!connectSystem()) {
                        setStatus(Messages.Operation_has_been_canceled_by_the_user);
                        showConnectionProperties();
                        enableControls();
                        return;
                    }
                }

                showConnectionProperties();

                boolean startJournaling = buttonStartJournaling.getSelection();

                ProductLibraryUploader uploader = new ProductLibraryUploader(getShell(), as400, ftpPort, rapidFireLibrary, aspGroup, startJournaling);
                uploader.setStatusMessageReceiver(TransferRapidFireLibrary.this);
                successfullyTransfered = uploader.run();

                buttonPanel.dispose();
                buttonPanel = createButtons(true);
                layout(true);

            } finally {

                if (successfullyTransfered) {
                    setErrorStatus(Messages.bind(Messages.Library_A_successfull_transfered, rapidFireLibrary));
                    disableControls();
                    enableControl(BUTTON_JOB_LOG, buttonJobLog);
                    enableControl(BUTTON_CLOSE, buttonClose);
                    buttonClose.setFocus();
                } else {
                    setErrorStatus(Messages.bind(Messages.Error_occurred_while_transfering_library_A, rapidFireLibrary));
                    enableControls();
                    buttonClose.setFocus();
                }
            }
        }
    }

    private boolean connectSystem() {

        disableControls();

        try {

            if (as400 != null) {
                disconnectSystem();
            }

            AS400 tempSystem = SystemConnectionHelper.getSystem(connectionName);
            if (tempSystem == null) {
                commandCall = null;
            } else {

                if (tempSystem instanceof SecureAS400) {
                    as400 = new SecureAS400(tempSystem);
                } else {
                    as400 = new AS400(tempSystem);
                }

                commandCall = new CommandCall(as400);
            }

        } catch (Throwable e) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        } finally {
            restoreControlsEnablement();
        }

        if (as400 == null) {
            return false;
        } else {
            return true;
        }
    }

    private void disconnectSystem() {

        if (as400 != null) {
            as400.disconnectAllServices();
            as400 = null;
        }

        commandCall = null;
    }

    private void enableControls() {

        enableControl(COMBO_CONNECTIONS, comboConnections);
        enableControl(BUTTON_START, buttonStart);
        enableControl(BUTTON_START_JOURNALING, buttonStartJournaling);
        enableControl(BUTTON_CLOSE, buttonClose);
        enableControl(BUTTON_JOB_LOG, buttonJobLog);
    }

    private void enableControl(String key, Object object) {

        if (object == null) {
            controlStatus.remove(key);
            return;
        }

        if (object instanceof Control) {
            Control control = (Control)object;
            if (!control.isDisposed()) {
                controlStatus.put(key, Boolean.TRUE);
                control.setEnabled(true);
            }
        } else if (object instanceof SystemHostCombo) {
            SystemHostCombo control = (SystemHostCombo)object;
            if (!control.isDisposed()) {
                controlStatus.put(key, Boolean.TRUE);
                control.setEnabled(true);
            }
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }

    private void disableControls() {

        disableControl(COMBO_CONNECTIONS, comboConnections);
        disableControl(BUTTON_START, buttonStart);
        disableControl(BUTTON_START_JOURNALING, buttonStartJournaling);
        disableControl(BUTTON_CLOSE, buttonClose);
        disableControl(BUTTON_JOB_LOG, buttonJobLog);
    }

    private void disableControl(String key, Object object) {

        if (object == null) {
            controlStatus.remove(key);
            return;
        }

        if (object instanceof Control) {
            Control control = (Control)object;
            if (!control.isDisposed()) {
                controlStatus.put(key, control.getEnabled());
                control.setEnabled(false);
            }
        } else if (object instanceof SystemHostCombo) {
            SystemHostCombo control = (SystemHostCombo)object;
            if (!control.isDisposed()) {
                controlStatus.put(key, control.getEnabled());
                control.setEnabled(false);
            }
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }

    private void restoreControlsEnablement() {

        restoreControlEnablement(COMBO_CONNECTIONS, comboConnections);
        restoreControlEnablement(BUTTON_START, buttonStart);
        restoreControlEnablement(BUTTON_START_JOURNALING, buttonStartJournaling);
        restoreControlEnablement(BUTTON_CLOSE, buttonClose);
        restoreControlEnablement(BUTTON_JOB_LOG, buttonJobLog);
    }

    private void restoreControlEnablement(String key, Object object) {

        if (object == null) {
            return;
        }

        if (object instanceof Control) {
            Control control = (Control)object;
            if (!control.isDisposed()) {
                control.setEnabled(getPreviousEnablement(key));
            }
        } else if (object instanceof SystemHostCombo) {
            SystemHostCombo control = (SystemHostCombo)object;
            if (!control.isDisposed()) {
                control.setEnabled(getPreviousEnablement(key));
            }
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }

    private boolean getPreviousEnablement(String key) {
        Boolean enabled = controlStatus.get(key);
        if (enabled instanceof Boolean) {
            return enabled;
        } else {
            return true;
        }
    }

    /**
     * Class that implements the context menu for the table rows.
     */
    private class TableContextMenu extends MenuAdapter {

        private Table table;
        private MenuItem menuItemCopySelected;
        private MenuItem menuItemCopyAll;

        public TableContextMenu(Table table) {
            this.table = table;
        }

        @Override
        public void menuShown(MenuEvent event) {
            destroyMenuItems();
            createMenuItems();
        }

        private Menu getMenu() {
            return table.getMenu();
        }

        private void destroyMenuItems() {
            if (!((menuItemCopySelected == null) || (menuItemCopySelected.isDisposed()))) {
                menuItemCopySelected.dispose();
            }
            if (!((menuItemCopyAll == null) || (menuItemCopyAll.isDisposed()))) {
                menuItemCopyAll.dispose();
            }
        }

        private void createMenuItems() {

            createMenuItemCopySelected();
        }

        private void createMenuItemCopySelected() {

            menuItemCopySelected = new MenuItem(getMenu(), SWT.NONE);
            menuItemCopySelected.setText(Messages.ActionLabel_Copy);
            menuItemCopySelected.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    copyStatusLinesToClipboard(table.getSelection());
                }
            });

            menuItemCopyAll = new MenuItem(getMenu(), SWT.NONE);
            menuItemCopyAll.setText(Messages.ActionLabel_Copy_all);
            menuItemCopyAll.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    copyStatusLinesToClipboard(table.getItems());
                }
            });
        }
    }
}
