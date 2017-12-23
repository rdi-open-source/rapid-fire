package biz.rapidfire.core.maintenance.reapplychanges;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.FileCopyProgramGeneratorActionCache;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.KeyFileCopyProgramGeneratorActionCache;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.shared.FileCopyProgramGeneratorAction;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class ReapplyChangesManager extends AbstractManager<IRapidFireFileResource, FileKey, FileValues, FileCopyProgramGeneratorAction> {

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$
    private IJDBCConnection dao;

    public ReapplyChangesManager(IJDBCConnection dao) {
        this.dao = dao;
    }

    @Override
    public void openFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_openFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result initialize(MaintenanceMode mode, FileKey key) throws Exception {
        throw new IllegalAccessError("Calling initialize() is not allowed. Method has not been implemented.");
    }

    @Override
    public FileValues getValues() throws Exception {
        throw new IllegalAccessError("Calling getValues() is not allowed. Method has not been implemented.");
    }

    @Override
    public void setValues(FileValues values) throws Exception {
        throw new IllegalAccessError("Calling setValues() is not allowed. Method has not been implemented.");
    }

    @Override
    public Result check() throws Exception {
        throw new IllegalAccessError("Calling check() is not allowed. Method has not been implemented.");
    }

    @Override
    public Result book() throws Exception {

        return null;
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(FileKey fileKey, FileCopyProgramGeneratorAction action) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_checkAction\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IReapplyChangesCheckAction.ACTION, action.label());
        statement.setString(IReapplyChangesCheckAction.JOB, fileKey.getJobName());
        statement.setInt(IReapplyChangesCheckAction.POSITION, fileKey.getPosition());
        statement.setString(IReapplyChangesCheckAction.AREA, Success.NO.label());
        statement.setString(IReapplyChangesCheckAction.SUCCESS, Success.NO.label());
        statement.setString(IReapplyChangesCheckAction.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IReapplyChangesCheckAction.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IReapplyChangesCheckAction.MESSAGE, Types.CHAR);

        statement.execute();

        String success = statement.getString(IReapplyChangesCheckAction.SUCCESS);
        String message = statement.getString(IReapplyChangesCheckAction.MESSAGE);

        return new Result(null, message, success);
    }

    protected FileCopyProgramGeneratorAction[] getValidActions(FileKey fileKey) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_getValidActions\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IReapplyChangesGetValidActions.JOB, fileKey.getJobName());
        statement.setInt(IReapplyChangesGetValidActions.POSITION, fileKey.getPosition());
        statement.setString(IReapplyChangesGetValidActions.AREA, fileKey.getJobName());
        statement.setInt(IReapplyChangesGetValidActions.NUMBER_ACTIONS, 0);
        statement.setString(IReapplyChangesGetValidActions.ACTIONS, EMPTY_STRING);

        statement.registerOutParameter(IReapplyChangesGetValidActions.NUMBER_ACTIONS, Types.DECIMAL);
        statement.registerOutParameter(IReapplyChangesGetValidActions.ACTIONS, Types.CHAR);

        statement.execute();

        int numberActions = statement.getBigDecimal(IReapplyChangesGetValidActions.NUMBER_ACTIONS).intValue();
        String[] actions = splitActions(statement.getString(IReapplyChangesGetValidActions.ACTIONS), numberActions);

        Set<FileCopyProgramGeneratorAction> fileCopyProgramGeneratorActions = new HashSet<FileCopyProgramGeneratorAction>();
        for (String action : actions) {
            fileCopyProgramGeneratorActions.add(FileCopyProgramGeneratorAction.find(action.trim()));
        }

        Result result = checkAction(FileKey.createNew(new JobKey(fileKey.getJobName())), FileCopyProgramGeneratorAction.CREATE);
        if (result.isSuccessfull()) {
            fileCopyProgramGeneratorActions.add(FileCopyProgramGeneratorAction.CREATE);
        }

        return fileCopyProgramGeneratorActions.toArray(new FileCopyProgramGeneratorAction[fileCopyProgramGeneratorActions.size()]);
    }

    @Override
    public boolean isValidAction(IRapidFireFileResource file, FileCopyProgramGeneratorAction action) throws Exception {

        KeyFileCopyProgramGeneratorActionCache fileCopyProgramGeneratorActionsKey = new KeyFileCopyProgramGeneratorActionCache(file);

        Set<FileCopyProgramGeneratorAction> actionsSet = FileCopyProgramGeneratorActionCache.getInstance().getActions(
            fileCopyProgramGeneratorActionsKey);
        if (actionsSet == null) {
            FileCopyProgramGeneratorAction[] fileCopyProgramGeneratorActions = getValidActions(file.getKey());
            FileCopyProgramGeneratorActionCache.getInstance().putActions(fileCopyProgramGeneratorActionsKey, fileCopyProgramGeneratorActions);
            actionsSet = FileCopyProgramGeneratorActionCache.getInstance().getActions(fileCopyProgramGeneratorActionsKey);
        }

        return actionsSet.contains(action);
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }
}
