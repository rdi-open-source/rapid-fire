/*******************************************************************************
 * Copyright (c) 2017-2021 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.install;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.RapidFireHelper;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400FTP;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.FTP;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobLog;
import com.ibm.as400.access.QueuedMessage;

public class ProductLibraryUploader {
    private Shell shell;
    private AS400 system;
    private int ftpPort;
    private String rapidFireLibrary;
    private String aspGroup;
    private boolean startJournaling;

    private CommandCall commandCall;

    private StatusMessageReceiver statusMessageReceiver;

    public ProductLibraryUploader(Shell shell, AS400 system, int ftpPort, String rapidFireLibrary, String aspGroup, boolean startJournaling) {

        this.shell = shell;
        this.system = system;
        this.ftpPort = ftpPort;
        this.rapidFireLibrary = rapidFireLibrary;
        this.aspGroup = aspGroup;
        this.startJournaling = startJournaling;
    }

    public void setStatusMessageReceiver(StatusMessageReceiver statusMessageReceiver) {
        this.statusMessageReceiver = statusMessageReceiver;
    }

    public boolean run() {

        boolean successfullyTransfered = false;

        try {

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
                                AS400FTP client = new AS400FTP(system);

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

        } finally {

            if (successfullyTransfered) {
                setStatus("!!!   " + Messages.bind(Messages.Library_A_successfull_transfered, rapidFireLibrary) + "   !!!");
            } else {
                setStatus("!!!   " + Messages.bind(Messages.Error_occurred_while_transfering_library_A, rapidFireLibrary) + "   !!!");
            }
        }

        return successfullyTransfered;
    }

    private boolean checkLibraryPrecondition(String libraryName, String aspGroup) {

        while (libraryExists(libraryName)) {
            if (!MessageDialog.openQuestion(
                shell,
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

        if (!RapidFireHelper.checkLibrary(system, libraryName)) {
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

    private boolean checkSaveFilePrecondition(String workLibrary, String saveFileName) {

        while (saveFileExists(workLibrary, saveFileName)) {
            if (!MessageDialog.openQuestion(
                shell,
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

        if (!RapidFireHelper.checkFile(system, workLibrary, saveFileName)) {
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

    private String executeCommand(String command, boolean logError) {

        if (commandCall == null) {
            commandCall = new CommandCall(system);
        }

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
                        setStatus(Messages.bind(Messages.Error_A, command));
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

    private void setStatus(String message) {
        if (statusMessageReceiver != null) {
            statusMessageReceiver.setStatus(message);
        }
    }

    private void setErrorStatus(String message) {
        if (statusMessageReceiver != null) {
            statusMessageReceiver.setErrorStatus(message);
        }
    }
}
