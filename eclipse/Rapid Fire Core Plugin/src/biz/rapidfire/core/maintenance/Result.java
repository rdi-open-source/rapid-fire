/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance;

public class Result {

    private String fieldName;
    private int recordNbr;
    private String message;
    private Success success;

    public static Result createSuccessResult() {
        return new Result(Success.YES.label(), null);
    }

    public Result(String success, String message) {
        this(null, message, success);
    }

    public Result(String fieldName, int recordNbr, String message, String success) {
        this(fieldName, message, success);

        this.recordNbr = recordNbr;
    }

    public Result(String fieldName, String message, Success success) {

        if (fieldName != null) {
            this.fieldName = fieldName.trim();
        }

        if (message != null) {
            this.message = message.trim();
        }

        this.success = success;
    }

    public Result(String fieldName, String message, String success) {
        this(fieldName, message, Success.find(success));
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getRecordNbr() {
        return recordNbr;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccessfull() {
        return Success.YES.equals(success);
    }

    public boolean isError() {
        return !isSuccessfull();
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();

        buffer.append("Success="); //$NON-NLS-1$
        buffer.append(success.label());
        buffer.append(" (message="); //$NON-NLS-1$
        buffer.append(message);
        buffer.append(", fieldName="); //$NON-NLS-1$
        buffer.append(fieldName);
        buffer.append(", rcdNbr="); //$NON-NLS-1$
        buffer.append(recordNbr);
        buffer.append(")"); //$NON-NLS-1$

        return buffer.toString();
    }
}
