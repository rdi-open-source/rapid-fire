package biz.rapidfire.core.handlers.job;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.activity.ActivityMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.activity.ActivityManager;
import biz.rapidfire.core.maintenance.activity.ActivityValues;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

public class MaintainActivitiesHandler extends AbstractJobMaintenanceHandler implements IHandler {

    private JobAction jobAction;

    public MaintainActivitiesHandler() {
        super(MaintenanceMode.CHANGE, JobAction.MNTAS);

        this.jobAction = JobAction.MNTAS;
    }

    @Override
    protected Result initialize(IRapidFireJobResource job) throws Exception {
        return Result.createSuccessResult();
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {

        ActivityManager activityManager = null;

        ActivityMaintenanceDialog dialog;
        if (canChangeJob(job)) {

            IJDBCConnection jdbcConnection = JDBCConnectionManager.getInstance().getConnectionForUpdateNoAutoCommit(
                job.getParentSubSystem().getConnectionName(), job.getDataLibrary());

            activityManager = new ActivityManager(jdbcConnection);

            ActivityValues[] values = activityManager.getValues(job, getShell());

            dialog = ActivityMaintenanceDialog.getChangeDialog(getShell(), activityManager);
            dialog.setValue(values);
            if (dialog.open() == Dialog.OK) {
                activityManager.book(); // Book changes.
                JDBCConnectionManager.getInstance().commit(jdbcConnection);
            } else {
                JDBCConnectionManager.getInstance().rollback(jdbcConnection);
            }

            job.reload(getShell());
            SystemConnectionHelper.refreshUIChanged(job.getParentSubSystem(), job, job.getParentFilters());

        } else {

            activityManager = new ActivityManager(JDBCConnectionManager.getInstance().getConnectionForRead(
                job.getParentSubSystem().getConnectionName(), job.getDataLibrary()));
            ActivityValues[] values = activityManager.getValues(job, getShell());

            dialog = ActivityMaintenanceDialog.getDisplayDialog(getShell(), activityManager);
            dialog.setValue(values);
            dialog.open();
            // Nothing to update here.

        }
    }

    private boolean canChangeJob(IRapidFireJobResource job) {

        try {

            Result result = getManager().checkAction(job.getKey(), JobAction.CHANGE);

            return result.isSuccessfull();

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not check job action ***", e); //$NON-NLS-1$
        }

        return false;
    }
}
