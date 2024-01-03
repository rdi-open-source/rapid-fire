/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import biz.rapidfire.rsebase.host.SystemFileType;

import com.ibm.etools.iseries.rse.ui.widgets.QSYSMemberPrompt;

public class SystemMemberPrompt {

    private QSYSMemberPrompt memberPrompt;

    public SystemMemberPrompt(Composite parent, SystemFileType fileType) {
        this(parent, SWT.NONE, fileType);
    }

    public SystemMemberPrompt(Composite parent, int style, SystemFileType fileType) {
        this(parent, style, true, true, fileType);
    }

    public SystemMemberPrompt(Composite parent, int style, boolean allowGeneric, boolean allowLibl, SystemFileType fileType) {
        this.memberPrompt = new QSYSMemberPrompt(parent, style, allowGeneric, allowLibl, fileType.intValue());
    }

    public String getLibraryName() {
        return memberPrompt.getLibraryName();
    }

    public void setLibraryName(String libraryName) {
        this.memberPrompt.setLibraryName(libraryName);
    }

    public String getFileName() {
        return memberPrompt.getFileName();
    }

    public void setFileName(String fileName) {
        this.memberPrompt.setFileName(fileName);
    }

    public String getMemberName() {
        return memberPrompt.getMemberName();
    }

    public void setMemberName(String memberName) {
        memberPrompt.setMemberName(memberName);
    }

    public void setLayoutData(Object layoutData) {
        memberPrompt.setLayoutData(layoutData);
    }

    public void updateHistory() {
        memberPrompt.updateHistory();
    }
}
