/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.swt.widgets.listeditors.librarylist;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "biz.rapidfire.core.swt.widgets.listeditors.librarylist.messages"; //$NON-NLS-1$

    public static String Add;
    public static String Remove;
    public static String Remove_all;
    public static String Sequence;
    public static String Item;
    public static String Move_up;
    public static String Move_down;
    public static String Library_colon;
    public static String Library_Tooltip;

    private Messages() {
    }

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
}
