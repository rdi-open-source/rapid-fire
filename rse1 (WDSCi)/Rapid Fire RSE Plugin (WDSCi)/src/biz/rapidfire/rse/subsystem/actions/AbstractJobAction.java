package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.model.RapidFireJobResource;

import com.ibm.etools.iseries.core.ui.actions.isv.ISeriesAbstractQSYSPopupMenuExtensionAction;

public abstract class AbstractJobAction extends ISeriesAbstractQSYSPopupMenuExtensionAction {

    @Override
    public void run() {

        init();

        String message = null;

        Object[] selection = getSelectedRemoteObjects();
        for (int i = 0; i < selection.length; i++) {
            if (selection[i] instanceof RapidFireJobResource) {
                RapidFireJobResource jobResource = (RapidFireJobResource)selection[i];
                message = execute(jobResource);
                if (message != null) {
                    MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.Error, message);
                    break;
                }
            }
        }

        if (message == null) {
            message = finish();
            if (message != null) {
                MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.Error, message);
            }
        }

    }

    public void init() {
    }

    public abstract String execute(RapidFireJobResource jobResource);

    public String finish() {
        return null;
    }

}