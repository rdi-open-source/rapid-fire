package biz.rapidfire.core.helpers;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;

public class ISphereHelper {

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
}
