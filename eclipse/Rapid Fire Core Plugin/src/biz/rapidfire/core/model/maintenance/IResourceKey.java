/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance;

import biz.rapidfire.core.model.maintenance.activity.ActivityKey;
import biz.rapidfire.core.model.maintenance.area.shared.AreaKey;
import biz.rapidfire.core.model.maintenance.command.shared.CommandKey;
import biz.rapidfire.core.model.maintenance.conversion.shared.ConversionKey;
import biz.rapidfire.core.model.maintenance.file.shared.FileKey;
import biz.rapidfire.core.model.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.maintenance.library.shared.LibraryKey;
import biz.rapidfire.core.model.maintenance.librarylist.shared.LibraryListKey;
import biz.rapidfire.core.model.maintenance.notification.shared.NotificationKey;

/**
 * Tagging interface for resource keys.
 * 
 * @see ActivityKey
 * @see AreaKey
 * @see CommandKey
 * @see ConversionKey
 * @see FileKey
 * @see JobKey
 * @see LibraryKey
 * @see LibraryListKey
 * @see NotificationKey
 */
public interface IResourceKey extends Cloneable {

}
