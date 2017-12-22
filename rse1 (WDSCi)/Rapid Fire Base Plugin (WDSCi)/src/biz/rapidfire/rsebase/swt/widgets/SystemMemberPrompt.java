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

import com.ibm.etools.iseries.core.ui.widgets.ISeriesMemberPrompt;

import biz.rapidfire.rsebase.host.SystemFileType;

public class SystemMemberPrompt {

    private ISeriesMemberPrompt memberPrompt;

    public SystemMemberPrompt(Composite parent, SystemFileType fileType) {
        this(parent, SWT.NONE, fileType);
    }

    public SystemMemberPrompt(Composite parent, int style, SystemFileType fileType) {
        this(parent, style, true, true, fileType);
    }

    public SystemMemberPrompt(Composite parent, int style, boolean allowGeneric, boolean allowLibl, SystemFileType fileType) {
        this.memberPrompt = new ISeriesMemberPrompt(parent, style, allowGeneric, allowLibl, fileType.intValue());
    }

    public String getLibraryName() {
        return memberPrompt.getLibraryName();
    }

    public String getFileName() {
        return memberPrompt.getFileName();
    }

    public String getMemberName() {
        return memberPrompt.getMemberName();
    }

    public void setLayoutData(Object layoutData) {
        memberPrompt.setLayoutData(layoutData);
    }

    public void updateHistory() {
        memberPrompt.updateHistory();
    }
}
