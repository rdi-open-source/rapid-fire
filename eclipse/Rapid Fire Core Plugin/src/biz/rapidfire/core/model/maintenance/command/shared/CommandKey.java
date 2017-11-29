/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.command.shared;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.maintenance.file.shared.FileKey;

public class CommandKey implements Cloneable {

    private FileKey fileKey;
    private CommandType commandType;
    private int sequence;

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
            this.commandType = null; //$NON-NLS-1$
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
