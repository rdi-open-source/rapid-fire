/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.resources;

import biz.rapidfire.core.model.IRapidFireCommandResource;

public class RapidFireCommandResourceDelegate implements Comparable<IRapidFireCommandResource> {

    private String dataLibrary;
    private String job;
    private int position;
    private String commandType;
    private int sequence;
    private String command;

    public RapidFireCommandResourceDelegate(String dataLibrary, String job, int position, String commandType, int sequence) {

        this.dataLibrary = dataLibrary;
        this.job = job;
        this.position = position;
        this.commandType = commandType;
        this.sequence = sequence;
    }

    /*
     * IRapidFireResource methods
     */

    public String getDataLibrary() {
        return dataLibrary;
    }

    /*
     * IRapidFireFileResource methods
     */

    public String getJob() {
        return job;
    }

    public int getPosition() {
        return position;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int compareTo(IRapidFireCommandResource resource) {

        if (resource == null) {
            return 1;
        }

        int result = resource.getDataLibrary().compareTo(getDataLibrary());
        if (result != 0) {
            return result;
        }

        result = resource.getJob().compareTo(getJob());
        if (result != 0) {
            return result;
        }

        if (position > resource.getPosition()) {
            return 1;
        } else if (position < resource.getPosition()) {
            return -1;
        }

        result = resource.getCommandType().compareTo(getCommandType());
        if (result != 0) {
            return result;
        }

        if (sequence > resource.getSequence()) {
            return 1;
        } else if (sequence < resource.getSequence()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();

        buffer.append(getCommand());
        buffer.append("/"); //$NON-NLS-1$
        buffer.append(getSequence());
        buffer.append(" ("); //$NON-NLS-1$
        buffer.append(getCommand());
        buffer.append(")"); //$NON-NLS-1$

        return buffer.toString();
    }

}
