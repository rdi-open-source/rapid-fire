/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.command;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceValues;
import biz.rapidfire.core.maintenance.command.shared.CommandKey;
import biz.rapidfire.core.maintenance.command.shared.CommandType;
import biz.rapidfire.core.maintenance.conversion.shared.NewFieldName;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;

public class CommandValues implements IResourceValues {

    private static final String EMPTY = ""; //$NON-NLS-1$

    private CommandKey key;
    private String command;

    public static String[] getNewFieldNameSpecialValues() {
        return NewFieldName.labels();
    }

    public static CommandValues createInitialized() {

        CommandValues commandValues = new CommandValues();
        commandValues.setKey(new CommandKey(new FileKey(new JobKey(EMPTY), 0), CommandType.COMPILE, 0));
        commandValues.setCommand(EMPTY);

        return commandValues;
    }

    public CommandKey getKey() {
        ensureKey();
        return key;
    }

    public void setKey(CommandKey key) {
        ensureKey();
        this.key = key;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void clear() {
        setCommand(null);
    }

    private void ensureKey() {

        if (key == null) {
            key = new CommandKey(null, null, 0);
        }
    }

    @Override
    public CommandValues clone() {

        try {

            CommandValues commandValues = (CommandValues)super.clone();
            commandValues.setKey((CommandKey)getKey().clone());

            return commandValues;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
