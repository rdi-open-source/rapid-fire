/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.wizard.shared;

import biz.rapidfire.core.model.IRapidFireResource;

/**
 * Interface for tagging Rapid Fire resources that support the "new" wizards.
 * Required for the Eclispe "org.eclipse.ui.newWizards" extension point.
 */
public interface IWizardSupporter extends IRapidFireResource {

}
