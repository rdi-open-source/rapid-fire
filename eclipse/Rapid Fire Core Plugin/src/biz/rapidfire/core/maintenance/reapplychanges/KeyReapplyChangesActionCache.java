/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.reapplychanges;

import biz.rapidfire.core.maintenance.AbstractKeyResourceActionCache;
import biz.rapidfire.core.model.IFileCopyStatus;

/**
 * This class produces the key value for the FileActionCache.
 * 
 * <pre>
 * Form of the key:    [jobStatus], [createEnvironment], [phase]
 * Example key value:  RDY, true, phase
 * </pre>
 * 
 * The key is composed from the attributes of the job ('status' and 'create
 * environment') plus the phase of the job.
 */
public class KeyReapplyChangesActionCache extends AbstractKeyResourceActionCache {

    public KeyReapplyChangesActionCache(IFileCopyStatus area) {
        super(area.getJob(), area.getJob().getPhase().label());
    }
}
