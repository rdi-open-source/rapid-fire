/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.install.dialogs;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
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
import biz.rapidfire.core.helpers.ClipboardHelper;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400FTP;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.FTP;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobLog;
import com.ibm.as400.access.QueuedMessage;

public class TransferRapidFireLibrary extends Shell {

    private AS400 as400;
    private CommandCall commandCall;
    private Table tableStatus;
    private Composite buttonPanel;
    private Button buttonStart;
    private Button buttonStartJournaling;
    private Button buttonClose;
    private Button buttonJobLog;
    private String rapidFireLibrary;
    private String aspGroup;
    private int ftpPort;
    private String hostName;

    public TransferRapidFireLibrary(Display display, int style, String rapidFireLibrary, String aASPGroup, String aHostName, int aFtpPort) {
        super(display, style);

        setImage(RapidFireCorePlugin.getDefault().getImageRegistry().get(RapidFireCorePlugin.IMAGE_TRANSFER_LIBRARY));

        this.rapidFireLibrary = rapidFireLibrary;
        this.aspGroup = aASPGroup;
        this.hostName = aHostName;
        setFtpPort(aFtpPort);

        createContents();

        addListener(SWT.Close, new Listener() {
            public void handleEvent(Event event) {
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

    private void setStatus(String status) {
        TableItem itemStatus = new TableItem(tableStatus, SWT.BORDER);
        itemStatus.setText(status);
        tableStatus.update();
        redraw();
    }

    private void setErrorStatus(String status) {
        setStatus("!!!   " + status + "   !!!");
    }

    private boolean checkLibraryPrecondition(String libraryName, String aspGroup) {

        while (libraryExists(libraryName)) {
            if (!MessageDialog.openQuestion(
                getShell(),
                Messages.DialogTitle_Delete_Object,
                Messages.bind(Messages.Library_A_does_already_exist, libraryName) + "\n\n"
                    + Messages.bind(Messages.Question_Do_you_want_to_delete_library_A, libraryName))) {
                return false;
            }
            setStatus(Messages.bind(Messages.Deleting_library_A, libraryName));
            deleteLibrary(libraryName, aspGroup);
        }

        return true;
    }

    private boolean libraryExists(String libraryName) {

        if (!RapidFireHelper.checkLibrary(as400, libraryName)) {
            return false;
        }

        return true;
    }

    private boolean deleteLibrary(String libraryName, String aspGroup) {

        String cpfMsg;

        try {

            executeCommand("ADDLIBLE LIB(" + libraryName + ") POSITION(*FIRST)", false);

            cpfMsg = dropSqlProcedures(libraryName, true);
            if (!cpfMsg.equals("")) {
                return false;
            }

            cpfMsg = endJournaling(libraryName, true);
            if (!cpfMsg.equals("")) {
                return false;
            }

        } finally {
            executeCommand("RMVLIBLE LIB(" + libraryName + ")", false);
        }

        cpfMsg = executeCommand(produceDeleteLibraryCommand(libraryName, aspGroup), true);
        if (!cpfMsg.equals("")) {
            return false;
        }

        return true;
    }

    private boolean checkSaveFilePrecondition(String workLibrary, String saveFileName) {

        while (saveFileExists(workLibrary, saveFileName)) {
            if (!MessageDialog.openQuestion(
                getShell(),
                Messages.DialogTitle_Delete_Object,
                Messages.bind(Messages.File_B_in_library_A_does_already_exist, new String[] { workLibrary, saveFileName }) + "\n\n"
                    + Messages.bind(Messages.Question_Do_you_want_to_delete_object_A_B_type_C, new String[] { workLibrary, saveFileName, "*FILE" }))) {
                return false;
            }
            setStatus(Messages.bind(Messages.Deleting_object_A_B_of_type_C, new String[] { workLibrary, saveFileName, "*FILE" }));
            deleteSaveFile(workLibrary, saveFileName, true);
        }

        return true;
    }

    private boolean saveFileExists(String workLibrary, String saveFileName) {

        if (!RapidFireHelper.checkFile(as400, workLibrary, saveFileName)) {
            return false;
        }

        return true;
    }

    private boolean deleteSaveFile(String workLibrary, String saveFileName, boolean logErrors) {

        if (!executeCommand("DLTF FILE(" + workLibrary + "/" + saveFileName + ")", logErrors).equals("")) {
            return false;
        }

        return true;
    }

    private boolean createSaveFile(String workLibrary, String saveFileName, boolean logErrors) {

        if (!executeCommand("CRTSAVF FILE(" + workLibrary + "/" + saveFileName + ") TEXT('RAPIDFIRE')", logErrors).equals("")) {
            return false;
        }

        return true;
    }

    private boolean restoreLibrary(String workLibrary, String saveFileName, String libraryName, String aspGroup) throws Exception {

        AS400 system = commandCall.getSystem();
        Job serverJob = commandCall.getServerJob();
        JobLog jobLog = new JobLog(system, serverJob.getName(), serverJob.getUser(), serverJob.getNumber());
        jobLog.setListDirection(false);

        Date startingMessageDate = null;
        QueuedMessage[] startingMessages = jobLog.getMessages(0, 1);
        if (startingMessages != null) {
            startingMessageDate = startingMessages[0].getDate().getTime();
        }

        String cpfMsg = executeCommand(produceRestoreLibraryCommand(workLibrary, saveFileName, libraryName, aspGroup), true);
        if (!cpfMsg.equals("")) {
            if (cpfMsg.equals("CPF3773")) {

                List<QueuedMessage> countNotRestored = new LinkedList<QueuedMessage>();
                List<QueuedMessage> countIgnored = new LinkedList<QueuedMessage>();

                QueuedMessage[] messages;
                final int chunkSize = 20;
                int offset = 0;

                jobLog = new JobLog(system, serverJob.getName(), serverJob.getUser(), serverJob.getNumber());
                jobLog.setListDirection(false);
                while ((messages = jobLog.getMessages(offset, chunkSize)) != null && messages.length > 0 && startingMessageDate != null) {
                    for (QueuedMessage message : messages) {

                        // CPF3756 - &2 &1 not restored to &3.
                        if ("CPF3756".equals(message.getID())) {
                            countNotRestored.add(message);
                        }

                        // CPF7086 - Cannot restore journal &1 to library &4.
                        // CPF707F - Cannot restore receiver &1 into library &2.
                        if ("CPF7086".equals(message.getID()) || "CPF707F".equals(message.getID())) {
                            countIgnored.add(message);
                        }

                        if (message.getDate().getTime().compareTo(startingMessageDate) < 0) {
                            startingMessageDate = null;
                            break;
                        }
                    }
                    offset = offset + chunkSize;
                }

                for (int i = countNotRestored.size() - 1; i >= 0; i--) {
                    QueuedMessage notRestoredMessage = countNotRestored.get(i);
                    setStatus(countNotRestored.get(i) + ": " + notRestoredMessage.getText());
                }

                if (countNotRestored.size() == countIgnored.size()) {
                    setStatus(Messages.Journaling_will_be_started_by_the_installer);
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    private boolean initializeLibrary(String libraryName, boolean startJournaling) {

        String cpfMsg;
        boolean isLibraryListChanged = false;

        try {

            cpfMsg = executeCommand("ADDLIBLE LIB(" + libraryName + ") POSITION(*FIRST)", true);
            if (!cpfMsg.equals("")) {
                isLibraryListChanged = false;
            } else {
                isLibraryListChanged = true;
            }

            // Form now on (18.02.2019) the library is shipped without a
            // journal.
            // cpfMsg = endJournaling(libraryName, true);
            // if (!cpfMsg.equals("")) {
            // return false;
            // }

            if (startJournaling) {
                cpfMsg = startJournaling(libraryName, true);
                if (!cpfMsg.equals("")) {
                    return false;
                }
            }

            // Form now on (18.02.2019) the library is shipped without SQL
            // procedures and functions.
            // cpfMsg = dropSqlProcedures(libraryName, true);
            // if (!cpfMsg.equals("")) {
            // return false;
            // }

            cpfMsg = createSqlProcedures(libraryName, true);
            if (!cpfMsg.equals("")) {
                return false;
            }

        } finally {
            if (isLibraryListChanged) {
                executeCommand("RMVLIBLE LIB(" + libraryName + ")", true);
            }
        }

        return true;
    }

    private String endJournaling(String libraryName, boolean logError) {
        return executeCommand("CALL PGM(" + libraryName + "/STRENDJRN) PARM(*END " + libraryName + ")", logError);
    }

    private String startJournaling(String libraryName, boolean logError) {
        return executeCommand("CALL PGM(" + libraryName + "/STRENDJRN) PARM(*START " + libraryName + ")", logError);
    }

    private String dropSqlProcedures(String libraryName, boolean logError) {
        return executeCommand("CALL PGM(" + libraryName + "/CRTDRPSQL) PARM(*DROP " + libraryName + ")", logError);
    }

    private String createSqlProcedures(String libraryName, boolean logError) {
        return executeCommand("CALL PGM(" + libraryName + "/CRTDRPSQL) PARM(*CREATE " + libraryName + ")", logError);
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

    public boolean connect() {
        buttonStart.setEnabled(false);
        buttonStartJournaling.setEnabled(false);
        buttonClose.setEnabled(false);
        SignOnDialog signOnDialog = new SignOnDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), hostName);
        if (signOnDialog.open() == Dialog.OK) {
            as400 = signOnDialog.getAS400();
            if (as400 != null) {
                try {
                    as400.connectService(AS400.COMMAND);
                    commandCall = new CommandCall(as400);
                    if (commandCall != null) {
                        setStatus(Messages.Server_job_colon + " " + commandCall.getServerJob().toString());
                        hostName = as400.getSystemName();
                        setStatus(Messages.bind(Messages.About_to_transfer_library_A_to_host_B_using_port_C, new String[] { rapidFireLibrary.trim() + " (" + Messages.Label_ASP_group_colon + aspGroup + ")",
                            hostName, Integer.toString(ftpPort) }));
                        buttonStart.setEnabled(true);
                        buttonStartJournaling.setEnabled(true);
                        buttonClose.setEnabled(true);
                        return true;
                    }
                } catch (Throwable e) {
                    RapidFireCorePlugin.logError("Failed to connect to host: " + hostName, e);
                }
            }
        }
        return false;
    }

    private String produceRestoreLibraryCommand(String workLibrary, String saveFileName, String libraryName, String aspGroup) {

        String command = "RSTLIB SAVLIB(RAPIDFIRE) DEV(*SAVF) SAVF(" + workLibrary + "/" + saveFileName + ") RSTLIB(" + libraryName + ")";
        if (RapidFireHelper.isASPGroupSpecified(aspGroup)) {
            command += " RSTASPDEV(" + aspGroup + ")";
        }

        return command;
    }

    private String produceDeleteLibraryCommand(String libraryName, String aspGroup) {

        String command = "DLTLIB LIB(" + libraryName + ")";
        if (RapidFireHelper.isASPGroupSpecified(aspGroup)) {
            command += " ASPDEV(*)";
        }

        return command;
    }

    private class TransferLibrarySelectionAdapter extends SelectionAdapter {
        @Override
        public void widgetSelected(final SelectionEvent event) {

            buttonStart.setEnabled(false);
            buttonStartJournaling.setEnabled(false);
            buttonClose.setEnabled(false);

            boolean successfullyTransfered = false;

            try {

                boolean startJournaling = buttonStartJournaling.getSelection();

                String workLibrary = "QGPL";
                String saveFileName = rapidFireLibrary;

                boolean ok = true;
                if (RapidFireHelper.isASPGroupSpecified(aspGroup)) {
                    String cpfMsg = executeCommand("SETASPGRP ASPGRP(" + aspGroup + ")", true);
                    if (!cpfMsg.equals("")) {
                        setStatus(Messages.bind(Messages.Error_occurred_while_setting_the_asp_group_to_A, aspGroup));
                        ok = false;
                    }
            	}
                if (ok) {
	                setStatus(Messages.bind(Messages.Checking_library_A_for_existence, rapidFireLibrary));
	                if (!checkLibraryPrecondition(rapidFireLibrary, aspGroup)) {
	                    setErrorStatus(Messages.bind(Messages.Library_A_does_already_exist, rapidFireLibrary));
	                } else {
	                    setStatus(Messages.bind(Messages.Checking_file_B_in_library_A_for_existence, new String[] { workLibrary, saveFileName }));
	                    if (!checkSaveFilePrecondition(workLibrary, saveFileName)) {
	                        setErrorStatus(Messages.bind(Messages.File_B_in_library_A_does_already_exist, new String[] { workLibrary, saveFileName }));
	                    } else {
	
	                        setStatus(Messages.bind(Messages.Creating_save_file_B_in_library_A, new String[] { workLibrary, saveFileName }));
	                        if (!createSaveFile(workLibrary, saveFileName, true)) {
	                            setErrorStatus(Messages.bind(Messages.Could_not_create_save_file_B_in_library_A,
	                                new String[] { workLibrary, saveFileName }));
	                        } else {
	
	                            try {
	
	                                setStatus(Messages.Sending_save_file_to_host);
	                                setStatus(Messages.bind(Messages.Using_Ftp_port_number, new Integer(ftpPort)));
	                                AS400FTP client = new AS400FTP(as400);
	
	                                URL fileUrl = FileLocator.toFileURL(RapidFireCorePlugin.getInstallURL());
	                                File file = new File(fileUrl.getPath() + "Server" + File.separator + "RAPIDFIRE.SAVF");
	                                client.setPort(ftpPort);
	                                client.setDataTransferType(FTP.BINARY);
	                                if (client.connect()) {
	                                    client.put(file, "/QSYS.LIB/" + workLibrary + ".LIB/" + saveFileName + ".FILE");
	                                    client.disconnect();
	                                }
	
	                                setStatus(Messages.bind(Messages.Restoring_library_A, rapidFireLibrary));
	                                if (!restoreLibrary(workLibrary, saveFileName, rapidFireLibrary, aspGroup)) {
	                                    setErrorStatus(Messages.bind(Messages.Could_not_restore_library_A, rapidFireLibrary));
	                                } else {
	
	                                    if (initializeLibrary(rapidFireLibrary, startJournaling)) {
	                                        successfullyTransfered = true;
	                                    }
	
	                                }
	
	                            } catch (Throwable e) {
	                                RapidFireCorePlugin.logError(Messages.Could_not_send_save_file_to_host, e);
	
	                                setErrorStatus(Messages.Could_not_send_save_file_to_host);
	                                setStatus(e.getLocalizedMessage());
	                            } finally {
	
	                                setStatus(Messages.bind(Messages.Deleting_object_A_B_of_type_C, new String[] { workLibrary, saveFileName, "*FILE" }));
	                                deleteSaveFile(workLibrary, saveFileName, true);
	                            }
	
	                        }
	                    }
	                }
                }

                buttonPanel.dispose();
                buttonPanel = createButtons(true);
                layout(true);

            } finally {

                if (successfullyTransfered) {
                    setErrorStatus(Messages.bind(Messages.Library_A_successfull_transfered, rapidFireLibrary));
                    buttonStart.setEnabled(false);
                    buttonStartJournaling.setEnabled(false);
                    buttonClose.setEnabled(true);
                    buttonClose.setFocus();
                } else {
                    setErrorStatus(Messages.bind(Messages.Error_occurred_while_transfering_library_A, rapidFireLibrary));
                    buttonStart.setEnabled(true);
                    buttonStartJournaling.setEnabled(true);
                    buttonClose.setEnabled(true);
                    buttonClose.setFocus();
                }
            }
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
