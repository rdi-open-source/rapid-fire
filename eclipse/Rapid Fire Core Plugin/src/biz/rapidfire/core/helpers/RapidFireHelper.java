package biz.rapidfire.core.helpers;

import java.io.IOException;
import java.text.DecimalFormat;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.rsebase.helpers.AbstractRapidFireHelper;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.IllegalObjectTypeException;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.QSYSObjectPathName;

public class RapidFireHelper extends AbstractRapidFireHelper {

    public static String getRapidFireLibraryVersion(AS400 as400, String library) {

        String dataAreaRapidFireContent = readRapidFireDataArea(null, as400, library);
        if (dataAreaRapidFireContent == null) {
            return null;
        }

        String libraryVersion = retrieveServerVersion(dataAreaRapidFireContent);
        if (libraryVersion == null) {
            return null;
        }

        return getVersionFormatted(libraryVersion);
    }

    public static String getRapidFireLibraryBuildDate(AS400 as400, String library) {

        String dataAreaRapidFireContent = readRapidFireDataArea(null, as400, library);
        if (dataAreaRapidFireContent == null) {
            return null;
        }

        String buildDate = retrieveBuildDate(dataAreaRapidFireContent);
        if (buildDate == null || buildDate.trim().length() == 0) {
            return null;
        }

        return buildDate;
    }

    public static boolean checkRapidFireLibrary(Shell shell, String connectionName, String libraryName) {
        AS400 as400 = getSystem(connectionName);
        return checkRapidFireLibrary(shell, as400, libraryName);
    }

    public static boolean checkRapidFireLibrary(Shell shell, AS400 as400, String library) {

        if (as400 == null) {
            return false;
        }

        String dataAreaRapidFireContent = readRapidFireDataArea(shell, as400, library);
        if (dataAreaRapidFireContent == null) {
            return false;
        }

        String serverProvided = retrieveServerVersion(dataAreaRapidFireContent);
        String serverNeedsClient = retrieveRequiredClientVersion(dataAreaRapidFireContent);

        String clientProvided = comparableVersion(RapidFireCorePlugin.getDefault().getVersion());
        String clientNeedsServer = comparableVersion(RapidFireCorePlugin.getDefault().getMinServerVersion());

        if (serverProvided.compareTo(clientNeedsServer) < 0) {

            String message = Messages.bind(
                Messages.Rapid_Fire_library_A_on_system_B_is_of_version_C_but_at_least_version_D_is_required_Please_update_the_Rapid_Fire_library,
                new String[] { library, as400.getSystemName(), getVersionFormatted(serverProvided), getVersionFormatted(clientNeedsServer) });
            MessageDialogAsync.displayError(shell, message);

            return false;
        }

        if (clientProvided.compareTo(serverNeedsClient) < 0) {

            String message = Messages
                .bind(
                    Messages.The_installed_Rapid_Fire_plug_in_version_A_is_outdated_because_the_installed_Rapid_Fire_library_requires_at_least_version_B_of_the_Rapid_Fire_plug_in_Please_update_your_Rapid_Fire_plug_in,
                    new String[] { getVersionFormatted(clientProvided), getVersionFormatted(serverNeedsClient) });
            MessageDialogAsync.displayError(shell, message);

            return false;
        }

        return true;
    }

    public static String executeCommand(AS400 as400, String command) throws Exception {

        CommandCall commandCall = new CommandCall(as400);
        commandCall.run(command);
        AS400Message[] messageList = commandCall.getMessageList();
        if (messageList.length > 0) {
            for (int idx = 0; idx < messageList.length; idx++) {
                if (messageList[idx].getType() == AS400Message.ESCAPE) {
                    return messageList[idx].getID();
                }
            }
        }
        return ""; //$NON-NLS-1$
    }

    public static String getCurrentLibrary(AS400 _as400) throws Exception {

        String currentLibrary = null;

        Job[] jobs = _as400.getJobs(AS400.COMMAND);

        if (jobs.length == 1) {

            if (!jobs[0].getCurrentLibraryExistence()) {
                currentLibrary = "*CRTDFT"; //$NON-NLS-1$
            } else {
                currentLibrary = jobs[0].getCurrentLibrary();
            }

        }

        return currentLibrary;

    }

    public static boolean setCurrentLibrary(AS400 _as400, String currentLibrary) throws Exception {

        String command = "CHGCURLIB CURLIB(" + currentLibrary + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        CommandCall commandCall = new CommandCall(_as400);

        if (commandCall.run(command)) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean checkLibrary(AS400 system, String library) {
        return checkObject(system, new QSYSObjectPathName("QSYS", library, "LIB")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static boolean checkObject(AS400 system, QSYSObjectPathName pathName) {

        StringBuilder command = new StringBuilder();
        command.append("CHKOBJ OBJ("); //$NON-NLS-1$
        command.append(pathName.getLibraryName());
        command.append("/"); //$NON-NLS-1$
        command.append(pathName.getObjectName());
        command.append(") OBJTYPE("); //$NON-NLS-1$
        command.append("*"); //$NON-NLS-1$
        command.append(pathName.getObjectType());
        command.append(")"); //$NON-NLS-1$

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
        parts = comparableVersion.split("\\.");
        DecimalFormat formatter = new DecimalFormat("00");
        for (int i = 0; i < parts.length; i++) {
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
        return comparableVersion;
    }

    private static String getVersionFormatted(String aVersionNumber) {
        return Integer.parseInt(aVersionNumber.substring(0, 2)) + "." + Integer.parseInt(aVersionNumber.substring(2, 4)) + "."
            + Integer.parseInt(aVersionNumber.substring(4, 6));
    }

    private static String retrieveServerVersion(String dataAreaRapidFireContent) {
        return dataAreaRapidFireContent.substring(7, 13);
    }

    private static String retrieveRequiredClientVersion(String dataAreaRapidFireContent) {
        return dataAreaRapidFireContent.substring(21, 27);
    }

    private static String retrieveBuildDate(String dataAreaRapidFireContent) {
        return dataAreaRapidFireContent.substring(39, 49);
    }

    private static String readRapidFireDataArea(Shell shell, AS400 as400, String libraryName) {

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
        CharacterDataArea dataAreaRapidFire = new CharacterDataArea(as400, "/QSYS.LIB/" + libraryName + ".LIB/RAPIDFIRE.DTAARA");
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

        return dataAreaRapidFireContent;
    }
}
