/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.model.IRapidFireResource;

// TODO: http://www.vogella.com/tutorials/EclipseWizards/article.html
public abstract class AbstractNewWizard extends Wizard implements INewWizard {

    private String connectionName;
    private String dataLibrary;

    @Override
    public void addPages() {
        if (StringHelper.isNullOrEmpty(connectionName) || StringHelper.isNullOrEmpty(dataLibrary)) {
            addPage(new DataLibraryPage());
        }
    }

    @Override
    public boolean performFinish() {
        return false;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            if (!structuredSelection.isEmpty()) {
                Object element = structuredSelection.getFirstElement();
                if (element instanceof IRapidFireResource) {
                    IRapidFireResource resource = (IRapidFireResource)element;
                    connectionName = resource.getParentSubSystem().getConnectionName();
                    dataLibrary = resource.getDataLibrary();
                }
            }
        }

        return;
    }
}
