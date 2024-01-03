/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance;

import biz.rapidfire.core.maintenance.activity.ActivityValues;
import biz.rapidfire.core.maintenance.area.AreaValues;
import biz.rapidfire.core.maintenance.command.CommandValues;
import biz.rapidfire.core.maintenance.conversion.ConversionValues;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.job.JobValues;
import biz.rapidfire.core.maintenance.library.LibraryValues;
import biz.rapidfire.core.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.maintenance.notification.NotificationValues;

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
