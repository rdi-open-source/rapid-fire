/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.model.IFileCopyStatus;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.queries.FileCopyStatus;

public abstract class AbstractFileCopyStatusDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String POSITION = "POS"; //$NON-NLS-1$
    public static final String AREA = "ARA"; //$NON-NLS-1$
    public static final String FILE = "FILE"; //$NON-NLS-1$
    public static final String LIBRARY = "LIB"; //$NON-NLS-1$
    public static final String RCDS_IN_PRODUCTION_LIBRARY = "RPL"; //$NON-NLS-1$
    public static final String RCDS_IN_SHADOW_LIBRARY = "RSL"; //$NON-NLS-1$
    public static final String RCDS_TO_COPY = "RTO"; //$NON-NLS-1$
    public static final String RCDS_COPIED = "RCO"; //$NON-NLS-1$
    public static final String ESTIMATED_TIME = "ETC"; //$NON-NLS-1$
    public static final String RCDS_WITH_DUPLICATE_KEY = "RDK"; //$NON-NLS-1$
    public static final String CHANGES_TO_APPLY = "CTA"; //$NON-NLS-1$
    public static final String CHANGES_APPLIED = "CAP"; //$NON-NLS-1$
    public static final String PERCENT_DONE = "PRC"; //$NON-NLS-1$

    private IJDBCConnection dao;

    public AbstractFileCopyStatusDAO(IJDBCConnection dao) {

        this.dao = dao;
    }

    public List<IFileCopyStatus> load(IRapidFireJobResource job, Shell shell) throws Exception {

        final List<IFileCopyStatus> fileCopyStatuses = new ArrayList<IFileCopyStatus>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return fileCopyStatuses;
        }

        try {

            String sqlStatement = getSqlStatement(job.getName());
            preparedStatement = dao.prepareStatement(sqlStatement);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    fileCopyStatuses.add(produceFileCopyStatus(job, resultSet));
                }
            }

        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return fileCopyStatuses;
    }

    private FileCopyStatus produceFileCopyStatus(IRapidFireJobResource job, ResultSet resultSet) throws SQLException {

        int position = resultSet.getInt(POSITION);
        String area = resultSet.getString(AREA).trim();
        String file = resultSet.getString(FILE).trim();
        String library = resultSet.getString(LIBRARY).trim();
        long recordsInProductionLibrary = resultSet.getLong(RCDS_IN_PRODUCTION_LIBRARY);
        long recordsInShadowLibrary = resultSet.getLong(RCDS_IN_SHADOW_LIBRARY);
        long recordsToCopy = resultSet.getLong(RCDS_TO_COPY);
        long recordsCopied = resultSet.getLong(RCDS_COPIED);
        String estimatedTime = resultSet.getString(ESTIMATED_TIME).trim();
        long recordsWithDuplicateKey = resultSet.getLong(RCDS_WITH_DUPLICATE_KEY);
        long changesToApply = resultSet.getLong(CHANGES_TO_APPLY);
        long changesApplied = resultSet.getLong(CHANGES_APPLIED);
        int percentDone = resultSet.getInt(PERCENT_DONE);

        FileCopyStatus fileCopyStatus = new FileCopyStatus();
        fileCopyStatus.setJob(job);
        fileCopyStatus.setPosition(position);
        fileCopyStatus.setArea(area);
        fileCopyStatus.setFile(file);
        fileCopyStatus.setLibrary(library);
        fileCopyStatus.setRecordsInProductionLibrary(recordsInProductionLibrary);
        fileCopyStatus.setRecordsInShadowLibrary(recordsInShadowLibrary);
        fileCopyStatus.setRecordsToCopy(recordsToCopy);
        fileCopyStatus.setRecordsCopied(recordsCopied);
        fileCopyStatus.setEstimatedTime(estimatedTime);
        fileCopyStatus.setRecordsWithDuplicateKey(recordsWithDuplicateKey);
        fileCopyStatus.setChangesToApply(changesToApply);
        fileCopyStatus.setChangesApplied(changesApplied);

        // TODO: enable/disable debug code
        // percentDone = new Random().nextInt((100) + 1);

        fileCopyStatus.setPercentDone(percentDone);

        return fileCopyStatus;
    }

    private String getSqlStatement(String job) throws Exception {

        String where;
        if (job != null) {
            where = "AREAS.JOB = ''" + job + "''";
        } else {
            where = "";
        }

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            JOB + ", " +
            POSITION + ", " +
            AREA + ", " +
            FILE + ", " +
            LIBRARY + ", " +
            RCDS_IN_PRODUCTION_LIBRARY + ", " +
            RCDS_IN_SHADOW_LIBRARY + ", " +
            RCDS_TO_COPY + ", " +
            RCDS_COPIED + ", " +
            ESTIMATED_TIME + ", " +
            RCDS_WITH_DUPLICATE_KEY + ", " +
            CHANGES_TO_APPLY + ", " +
            CHANGES_APPLIED + ", " +
            PERCENT_DONE + " " +
        "FROM TABLE(" +
            IJDBCConnection.LIBRARY +
            "\"LODSTSE_loadStatusEntries\"('" + where + "')" +
            ") AS X";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
}
