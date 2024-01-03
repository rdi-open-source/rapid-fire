/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.helpers;

public class RapidFireDataArea {

    /**
     * <pre>
     *  Offset      *...+....1....+....2....+....3
     *      0      '040500040100C410000032       
     *     50      '                             
     *    100      '                             
     *    150      '                             
     *    200      '                             
     *    250      '      '
     * </pre>
     */

    /**
     * <pre>
     *  DCL        VAR(&RAPIDFIRE) TYPE(*CHAR) LEN(256)                                  
     *    DCL        VAR(&RF_VER ) TYPE(*CHAR) STG(*DEFINED) LEN( 2) DEFVAR(&RAPIDFIRE  1)
     *    DCL        VAR(&RF_RLS ) TYPE(*CHAR) STG(*DEFINED) LEN( 2) DEFVAR(&RAPIDFIRE  3)
     *    DCL        VAR(&RF_MDN ) TYPE(*CHAR) STG(*DEFINED) LEN( 2) DEFVAR(&RAPIDFIRE  5)
     *    DCL        VAR(&CM_VER ) TYPE(*CHAR) STG(*DEFINED) LEN( 2) DEFVAR(&RAPIDFIRE  7)
     *    DCL        VAR(&CM_RLS ) TYPE(*CHAR) STG(*DEFINED) LEN( 2) DEFVAR(&RAPIDFIRE  9)
     *    DCL        VAR(&CM_MDN ) TYPE(*CHAR) STG(*DEFINED) LEN( 2) DEFVAR(&RAPIDFIRE 11)
     *    DCL        VAR(&CM_PTF ) TYPE(*CHAR) STG(*DEFINED) LEN(10) DEFVAR(&RAPIDFIRE 13)
     *    DCL        VAR(&BD_DATE) TYPE(*CHAR) STG(*DEFINED) LEN(10) DEFVAR(&RAPIDFIRE 23)
     *    DCL        VAR(&CL_VER ) TYPE(*CHAR) STG(*DEFINED) LEN( 2) DEFVAR(&RAPIDFIRE 33)
     *    DCL        VAR(&CL_RLS ) TYPE(*CHAR) STG(*DEFINED) LEN( 2) DEFVAR(&RAPIDFIRE 35)
     *    DCL        VAR(&CL_MDN ) TYPE(*CHAR) STG(*DEFINED) LEN( 2) DEFVAR(&RAPIDFIRE 37)
     * </pre>
     */

    private String content;

    public RapidFireDataArea(String content) {
        this.content = content;
    }

    public String getServerVersion() {
        return content.substring(0, 6);
    }

    public String getCMOneVersion() {
        return content.substring(6, 12);
    }

    public String getCMOnePTF() {
        return content.substring(12, 22);
    }

    public String getBuildDate() {
        return content.substring(22, 32);
    }

    public String getClientVersion() {
        return content.substring(32, 38);
    }
}
