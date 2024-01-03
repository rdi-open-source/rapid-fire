     /*-------------------------------------------------------------------------------------------*/
     /*                                                                                           */
     /* Copyright (c) 2014 Task Force IT-Consulting GmbH, Waltrop (Germany)                       */
     /* This software and the accompanying materials are made available                           */
     /* under the terms of the GNU General Public License version 2.0 (GPLv2)                     */
     /* which accompanies this distribution and is available at                                   */
     /* http://www.gnu.org/licenses/gpl-2.0.html                                                  */
     /*                                                                                           */
     /*-------------------------------------------------------------------------------------------*/
     /*                                                                                           */
     /* Uninstall Rapid Fire                                                                      */
     /*                                                                                           */
     /*-------------------------------------------------------------------------------------------*/
     /*  >>PRE-COMPILER<<                                                                         */
     /*    >>CRTCMD<< CRTCLPGM     PGM(&LI/&OB) +                                                 */
     /*                              SRCFILE(&SL/&SF) SRCMBR(&SM);                                */
     /*    >>IMPORTANT<<                                                                          */
     /*      >>PARM<<  OPTION(*SRCDBG);                                                           */
     /*      >>PARM<<  TGTRLS(&TR);                                                               */
     /*    >>END-IMPORTANT<<                                                                      */
     /*    >>EXECUTE<<                                                                            */
     /*  >>END-PRE-COMPILER<<                                                                     */
     /*------------+--------+---------------------------------------------------------------------*/
     /*   Date     I Progr. I Description                                                         */
     /*------------+--------+---------------------------------------------------------------------*/
     /* 14.03.2018 I   FH   I Program created                                                     */
     /*------------+--------+---------------------------------------------------------------------*/

     /* Start */
             PGM        PARM(&LIB &CURRFLIB)

     /* Declare variables */
             DCL        VAR(&LIB) TYPE(*CHAR) LEN(10)
             DCL        VAR(&CURRFLIB) TYPE(*CHAR) LEN(10)

     /* Determine the Rapid Fire library where the currently called program resides */
             RTVOBJD    OBJ(UNINSTALL) OBJTYPE(*CMD) RTNLIB(&CURRFLIB)

     /* Change library list */
             CHGLIBL    LIBL(QTEMP) CURLIB(*CRTDFT)

     /* Remove all objects from library QTEMP */
             CLRLIB     LIB(QTEMP)

     /* Copy needed objects for uninstall procedure to library QTEMP */
             CRTDUPOBJ  OBJ(UNINSTALL*) FROMLIB(&CURRFLIB) +
                          OBJTYPE(*ALL) TOLIB(QTEMP)

     /* Call uninstall program */
             TFRCTL     PGM(UNINSTALL1) PARM(&LIB &CURRFLIB)

     /* End */
 STEP999:    ENDPGM
