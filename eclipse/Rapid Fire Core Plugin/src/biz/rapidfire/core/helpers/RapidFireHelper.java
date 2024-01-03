/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.helpers;

import java.io.IOException;
import java.text.DecimalFormat;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.IllegalObjectTypeException;
import com.ibm.as400.access.ObjectDoesNotExistException;

public class RapidFireHelper {

    private static final String QSYS = "QSYS";
    private static final String ASP_GROUP_NONE_VALUE = "*NONE";

    public static String getRapidFireLibraryVersion(AS400 as400, String library) {

        RapidFireDataArea dataAreaRapidFireContent = readRapidFireDataArea(null, as400, library);
        if (dataAreaRapidFireContent == null) {
            return null;
        }

        String libraryVersion = dataAreaRapidFireContent.getServerVersion();
        if (libraryVersion == null) {
            return null;
        }

        return getVersionFormatted(libraryVersion);
    }
    
    public static String getRapidFireLibraryVersionUnformatted(AS400 as400, String library) {

        RapidFireDataArea dataAreaRapidFireContent = readRapidFireDataArea(null, as400, library);
        if (dataAreaRapidFireContent == null) {
            return null;
        }

        String libraryVersion = dataAreaRapidFireContent.getServerVersion();
        if (libraryVersion == null) {
            return null;
        }

        return libraryVersion;
    }

    public static String getRapidFireLibraryBuildDate(AS400 as400, String library) {

        RapidFireDataArea dataAreaRapidFireContent = readRapidFireDataArea(null, as400, library);
        if (dataAreaRapidFireContent == null) {
            return null;
        }

        String buildDate = dataAreaRapidFireContent.getBuildDate();
        if (buildDate == null || buildDate.trim().length() == 0) {
            return null;
        }

        return buildDate;
    }

    public static boolean checkRapidFireLibrary(Shell shell, AS400 as400, String library) {
        return checkRapidFireLibrary(shell, as400, library, null);
    }

    public static boolean checkRapidFireLibrary(Shell shell, AS400 as400, String library, StringBuilder errorMessage) {

        if (as400 == null) {
            return false;
        }

        if (!checkLibrary(as400, library)) {
            setErrorMessage(errorMessage, Messages.bindParameters(Messages.Library_A_not_found_on_system_B, library, as400.getSystemName()));
            return false;
        }

        RapidFireDataArea dataAreaRapidFireContent = readRapidFireDataArea(shell, as400, library);
        if (dataAreaRapidFireContent == null) {
            return false;
        }

        String serverProvided = dataAreaRapidFireContent.getServerVersion();
        String serverNeedsClient = dataAreaRapidFireContent.getClientVersion();

        String clientProvided = comparableVersion(RapidFireCorePlugin.getDefault().getVersion());
        String clientNeedsServer = comparableVersion(RapidFireCorePlugin.getDefault().getMinServerVersion());

        String message = null;
        if (serverProvided.compareTo(clientNeedsServer) < 0) {

            message = Messages.bind(
                Messages.Rapid_Fire_library_A_on_system_B_is_of_version_C_but_at_least_version_D_is_required_Please_update_the_Rapid_Fire_library,
                new String[] { library, as400.getSystemName(), getVersionFormatted(serverProvided), getVersionFormatted(clientNeedsServer) });

        } else if (clientProvided.compareTo(serverNeedsClient) < 0) {

            message = Messages
                .bind(
                    Messages.The_installed_Rapid_Fire_plug_in_version_A_is_outdated_because_the_installed_Rapid_Fire_library_requires_at_least_version_B_of_the_Rapid_Fire_plug_in_Please_update_your_Rapid_Fire_plug_in,
                    new String[] { getVersionFormatted(clientProvided), getVersionFormatted(serverNeedsClient) });
        }

        if (message != null) {

            if (errorMessage != null) {
                setErrorMessage(errorMessage, message);
            } else {
                MessageDialogAsync.displayError(shell, message);
            }

            return false;
        }

        return true;
    }

    private static String setErrorMessage(StringBuilder errorMessage, String message) {

        if (errorMessage == null) {
            return message;
        }

        errorMessage.replace(0, errorMessage.length(), message);

        return errorMessage.toString();
    }

    public static boolean checkLibrary(AS400 system, String library) {
        return checkObject(system, QSYS, library, SystemObjectType.LIB);
    }

    public static boolean checkFile(AS400 system, String library, String file) {
        return checkObject(system, library, file, SystemObjectType.FILE);
    }

    public static boolean checkMember(AS400 system, String library, String file, String member) {
        return checkObject(system, library, file, member, SystemObjectType.MBR);
    }

    public static boolean checkObject(AS400 system, String library, String file, SystemObjectType objectType) {
        return checkObject(system, library, file, null, objectType);
    }

    public static boolean checkObject(AS400 system, String libraryName, String objectName, String memberName, SystemObjectType objectType) {

        StringBuilder command = new StringBuilder();
        command.append("CHKOBJ OBJ("); //$NON-NLS-1$
        command.append(libraryName);
        command.append("/"); //$NON-NLS-1$
        command.append(objectName);
        command.append(") OBJTYPE("); //$NON-NLS-1$
        command.append(objectType.value());
        command.append(")"); //$NON-NLS-1$

        if (!StringHelper.isNullOrEmpty(memberName)) {
            command.append(" MBR("); //$NON-NLS-1$
            command.append(memberName);
            command.append(")"); //$NON-NLS-1$
        }

        try {
            String message = executeCommand(system, command.toString());
            if (StringHelper.isNullOrEmpty(message)) {
                return true;
            }
        } catch (Exception e) {
            RapidFireCorePlugin.logError(ExceptionHelper.getLocalizedMessage(e), e);
        }

        return false;
    }

    public static String removeMember(String connectionName, String libraryName, String fileName, String memberName) throws Exception {
        return removeMember(SystemConnectionHelper.getSystem(connectionName), libraryName, fileName, memberName);
    }

    public static String removeMember(AS400 system, String libraryName, String fileName, String memberName) throws Exception {

        StringBuilder command = new StringBuilder();
        command.append("RMVM FILE("); //$NON-NLS-1$
        command.append(libraryName);
        command.append("/"); //$NON-NLS-1$
        command.append(fileName);
        command.append(") MBR("); //$NON-NLS-1$
        command.append(memberName);
        command.append(")"); //$NON-NLS-1$

        String message = null;

        try {

            message = executeCommand(system, command.toString());

        } catch (Exception e) {
            RapidFireCorePlugin.logError(ExceptionHelper.getLocalizedMessage(e), e);
            message = ExceptionHelper.getLocalizedMessage(e);
        }

        return message;
    }

    private static String executeCommand(AS400 as400, String command) throws Exception {

        CommandCall commandCall = new CommandCall(as400);
        commandCall.run(command);
        AS400Message[] messageList = commandCall.getMessageList();
        if (messageList.length > 0) {
            for (int idx = 0; idx < messageList.length; idx++) {
                if (messageList[idx].getType() == AS400Message.ESCAPE) {
                    return messageList[idx].getID() + ": " + messageList[idx].getText();
                }
            }
        }
        return null;
    }

    /**
     * Changes a given version string of type "v.r.m" to a comparable version
     * string of type "VVRRMM".
     * 
     * @param version Version String of type "v.r.m".
     * @return Comparable version String.
     */
    private static String comparableVersion(String version) {
        String comparableVersion = version;
        String[] parts = new String[3];
        parts = comparableVersion.split("\\."); //$NON-NLS-1$
        DecimalFormat formatter = new DecimalFormat("00"); //$NON-NLS-1$

        // Build comparable version string ignoring the "b" (beta) and "r"
        // (release) part.
        for (int i = 0; i < parts.length; i++) {
            if (!parts[i].startsWith("b") && !parts[i].startsWith("r")) {
                if (parts[i] == null) {
                    parts[i] = formatter.format(0L);
                } else {
                    parts[i] = formatter.format(IntHelper.tryParseInt(parts[i], 0));
                }
                if (i == 0) {
                    comparableVersion = parts[i];
                } else {
                    comparableVersion = comparableVersion + parts[i];
                }
            }
        }
        return comparableVersion;
    }

    private static String getVersionFormatted(String aVersionNumber) {
        return Integer.parseInt(aVersionNumber.substring(0, 2)) + "." + Integer.parseInt(aVersionNumber.substring(2, 4)) + "."
            + Integer.parseInt(aVersionNumber.substring(4, 6));
    }

    private static RapidFireDataArea readRapidFireDataArea(Shell shell, AS400 as400, String libraryName) {

        if (!checkLibrary(as400, libraryName)) {

            if (shell != null) {
                String message = Messages.bind(
                    Messages.Rapid_Fire_library_A_does_not_exist_on_system_B_Please_install_Rapid_Fire_library_A_on_system_B, new String[] {
                        libraryName, as400.getSystemName() });
                MessageDialogAsync.displayError(shell, message);
            }

            return null;
        }

        String dataAreaRapidFireContent = null;
        CharacterDataArea dataAreaRapidFire = new CharacterDataArea(as400, "/QSYS.LIB/" + libraryName + ".LIB/RAPIDFIRE.DTAARA"); //$NON-NLS-1$ //$NON-NLS-2$
        try {
            dataAreaRapidFireContent = dataAreaRapidFire.read();
        } catch (AS400SecurityException e) {
            e.printStackTrace();
        } catch (ErrorCompletingRequestException e) {
            e.printStackTrace();
        } catch (IllegalObjectTypeException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ObjectDoesNotExistException e) {
            e.printStackTrace();
        }
        if (dataAreaRapidFireContent == null) {

            if (shell != null) {
                String message = Messages.bind(Messages.The_specified_library_A_on_system_B_is_not_a_Rapid_Fire_library, new String[] { libraryName,
                    as400.getSystemName() });
                MessageDialogAsync.displayError(shell, message);
            }

            return null;
        }

        return new RapidFireDataArea(dataAreaRapidFireContent);
    }

    public static boolean isASPGroupSpecified(String aspGroup) {

        if (StringHelper.isNullOrEmpty(aspGroup)) {
            return false;
        }

        if (ASP_GROUP_NONE_VALUE.equals(aspGroup)) {
            return false;
        }

        return true;
    }

    public static String getRapidFireServerVersion(Shell shell, AS400 as400, String library) {

        if (as400 == null) {
            return null;
        }

        if (!checkLibrary(as400, library)) {
            return null;
        }

        RapidFireDataArea dataAreaRapidFireContent = readRapidFireDataArea(shell, as400, library);
        if (dataAreaRapidFireContent == null) {
            return null;
        }

        return dataAreaRapidFireContent.getServerVersion();
    }
    
}
