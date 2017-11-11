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
     /* Rapid Fire                                                                                */
     /*                                                                                           */
     /*------------+--------+---------------------------------------------------------------------*/
     /*   Date     I Progr. I Description                                                         */
     /*------------+--------+---------------------------------------------------------------------*/
     /* 01.07.2014 I   FH   I Created                                                             */
     /*------------+--------+---------------------------------------------------------------------*/
     /* >>PRE-COMPILER<<                                                                          */
     /*   >>CRTCMD<< CRTCMD       CMD(&LI/&OB) SRCFILE(&SL/&SF) SRCMBR(&SM);                      */
     /*   >>IMPORTANT<<                                                                           */
     /*     >>PARM<< PGM(*LIBL/RF);                                                               */
     /*     >>PARM<< ALLOW(*IMOD *IREXX *IPGM *EXEC *INTERACT);                                   */
     /*     >>PARM<< ALWLMTUSR(*NO);                                                              */
     /*     >>PARM<< PMTFILE(*LIBL/RF *STATIC);                                                   */
     /*     >>PARM<< HLPPNLGRP(*LIBL/RF);                                                         */
     /*     >>PARM<< HLPID(RF);                                                                   */
     /*     >>PARM<< CURLIB(*NOCHG);                                                              */
     /*     >>PARM<< PRDLIB(&LI);                                                                 */
     /*   >>END-IMPORTANT<<                                                                       */
     /*   >>EXECUTE<<                                                                             */
     /* >>END-PRE-COMPILER<<                                                                      */
     /*------------+--------+---------------------------------------------------------------------*/

             CMD        PROMPT(CON0001)
