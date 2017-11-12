/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance;

public abstract class AbstractManager<K, V> {

    public final String SUCCESS_NO = "N";
    public final String SUCCESS_YES = "Y";

    public abstract void openFiles() throws Exception;

    // Return succes: Y or N
    public abstract CheckStatus initialize(String mode, K key) throws Exception;

    public abstract V getValues() throws Exception;

    public abstract void setValues(V values) throws Exception;

    public abstract CheckStatus check() throws Exception;

    public abstract void book() throws Exception;

    public abstract void closeFiles() throws Exception;
}
