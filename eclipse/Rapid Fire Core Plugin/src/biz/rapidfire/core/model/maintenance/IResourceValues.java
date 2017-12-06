/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance;

import biz.rapidfire.core.model.maintenance.activity.ActivityValues;
import biz.rapidfire.core.model.maintenance.area.AreaValues;
import biz.rapidfire.core.model.maintenance.command.CommandValues;
import biz.rapidfire.core.model.maintenance.conversion.ConversionValues;
import biz.rapidfire.core.model.maintenance.file.FileValues;
import biz.rapidfire.core.model.maintenance.job.JobValues;
import biz.rapidfire.core.model.maintenance.library.LibraryValues;
import biz.rapidfire.core.model.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.model.maintenance.notification.NotificationValues;

/**
 * Tagging interface for resource values.
 * 
 * @see ActivityValues
 * @see AreaValues
 * @see CommandValues
 * @see ConversionValues
 * @see FileValues
 * @see JobValues
 * @see LibraryValues
 * @see LibraryListValues
 * @see NotificationValues
 */
public interface IResourceValues extends Cloneable {

}
