/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance;

import biz.rapidfire.core.maintenance.activity.shared.ActivityKey;
import biz.rapidfire.core.maintenance.area.shared.AreaKey;
import biz.rapidfire.core.maintenance.command.shared.CommandKey;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionKey;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.maintenance.library.shared.LibraryKey;
import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListKey;
import biz.rapidfire.core.maintenance.notification.shared.NotificationKey;

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

    public static final String DELIMITER = ","; //$NON-NLS-1$
}
