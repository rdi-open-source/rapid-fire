/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

public enum Status {
    RDY ("*RDY"),
    RUN_PENDING ("*RUN-PND"),
    RUN ("*RUN"),
    END_PND ("*END-PND"),
    END ("*END");

    public String label;

    private Status(String label) {
        this.label = label;
    }
}
