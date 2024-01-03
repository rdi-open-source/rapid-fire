/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.command.shared;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceKey;
import biz.rapidfire.core.maintenance.file.shared.FileKey;

public class CommandKey implements IResourceKey {

    private FileKey fileKey;
    private CommandType commandType;
    private int sequence;

    public static CommandKey createNew(FileKey fileKey) {

        CommandKey key = new CommandKey(fileKey, null, 0);

        return key;
    }

    public CommandKey(FileKey fileKey, CommandType commandType, int sequence) {

        this.fileKey = fileKey;
        this.commandType = commandType;
        this.sequence = sequence;
    }

    public String getJobName() {
        return fileKey.getJobName();
    }

    public int getPosition() {
        return fileKey.getPosition();
    }

    public String getCommandType() {

        if (commandType == null) {
            return ""; //$NON-NLS-1$
        } else {
            return commandType.label();
        }
    }

    public void setCommandType(String type) {

        if (type == null || type.trim().length() == 0) {
            this.commandType = null;
        } else {
            this.commandType = CommandType.find(type.trim());
        }
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();
        buffer.append(fileKey.toString());
        buffer.append(DELIMITER);
        buffer.append(getCommandType());
        buffer.append(DELIMITER);
        buffer.append(getSequence());

        return buffer.toString();
    }

    @Override
    public Object clone() {
        try {

            CommandKey conversionKey = (CommandKey)super.clone();
            conversionKey.fileKey = (FileKey)fileKey.clone();

            return conversionKey;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
