/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import biz.rapidfire.core.maintenance.wizard.shared.IWizardSupporter;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.rse.Messages;

public class ConversionsNode extends AbstractNodeResource implements IWizardSupporter {

    private IRapidFireFileResource file;

    public ConversionsNode(IRapidFireFileResource file) {
        super(file.getParentJob(), Messages.NodeText_Conversions);

        this.file = file;
    }

    public IRapidFireFileResource getFile() {
        return file;
    }
}
