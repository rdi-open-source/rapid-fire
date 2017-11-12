/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance;

public class CheckStatus {

    private String fieldName;
    private String message;
    private Success success;

    public CheckStatus(String fieldName, String message, String success) {

        if (fieldName != null) {
            this.fieldName = fieldName.trim();
        }

        if (message != null) {
            this.message = message.trim();
        }

        this.success = Success.value(success);
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message.trim();
    }

    public boolean isSuccessfull() {
        return success.equals(Success.YES);
    }

    public boolean isError() {
        return !isSuccessfull();
    }
}
