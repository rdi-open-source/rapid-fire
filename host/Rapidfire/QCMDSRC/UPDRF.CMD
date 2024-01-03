     /*-------------------------------------------------------------------------------------------*/
     /*                                                                                           */
     /* Copyright (c) 2016 Task Force IT-Consulting GmbH, Waltrop (Germany)                       */
     /* This software and the accompanying materials are made available                           */
     /* under the terms of the GNU General Public License version 2.0 (GPLv2)                     */
     /* which accompanies this distribution and is available at                                   */
     /* http://www.gnu.org/licenses/gpl-2.0.html                                                  */
     /*                                                                                           */
     /*-------------------------------------------------------------------------------------------*/
     /*                                                                                           */
     /* Update Rapid Fire                                                                         */
     /*                                                                                           */
     /*------------+--------+---------------------------------------------------------------------*/
     /*   Date     I Progr. I Description                                                         */
     /*------------+--------+---------------------------------------------------------------------*/
     /* 22.03.2016 I   TR   I Created                                                             */
     /*------------+--------+---------------------------------------------------------------------*/
     /* >>PRE-COMPILER<<                                                                          */
     /*   >>CRTCMD<< CRTCMD       CMD(&LI/&OB) SRCFILE(&SL/&SF) SRCMBR(&SM);                      */
     /*   >>IMPORTANT<<                                                                           */
     /*     >>PARM<< PGM(*LIBL/UPDRF001PG);                                                       */
     /*     >>PARM<< ALLOW(*ALL);                                                                 */
     /*     >>PARM<< ALWLMTUSR(*NO);                                                              */
     /*     >>PARM<< HLPPNLGRP(*LIBL/UPDRF001GP);                                                 */
     /*     >>PARM<< HLPID(UPDRF);                                                                */
     /*     >>PARM<< CURLIB(*NOCHG);                                                              */
     /*     >>PARM<< PRDLIB(&LI);                                                                 */
     /*   >>END-IMPORTANT<<                                                                       */
     /*   >>EXECUTE<<                                                                             */
     /* >>END-PRE-COMPILER<<                                                                      */
     /*------------+--------+---------------------------------------------------------------------*/

             CMD        PROMPT('Update Rapid Fire')

             PARM       KWD(SAVF) TYPE(QUAL1)          +
                          MIN(1)                       +
                          PROMPT('Save file')

             PARM       KWD(LIB) TYPE(*NAME)           +
                          LEN(10)                      +
                          DFT(RAPIDFIRE)               +
                          PROMPT('Library')

             PARM       KWD(RNMOLDLIB) TYPE(*NAME)     +
                          LEN(10)                      +
                          DFT(*VERSION)                +
                          SPCVAL(*VERSION)             +
                          PROMPT('Rename old library to')

             PARM       KWD(CLROLDLIB) TYPE(*CHAR)     +
                          LEN(10)                      +
                          DFT(*NO)                     +
                          RSTD(*YES)                   +
                          VALUES(*NO *YES)             +
                          PROMPT('Clear old library')

             PARM       KWD(JRNDLTOPT) TYPE(*CHAR)     +
                          LEN(11)                      +
                          MIN(0)                       +
                          MAX(3)                       +
                          DFT(*IGNINQMSG)              +
                          RSTD(*YES)                   +
                          SNGVAL(*NONE)                +
                          VALUES(*IGNINQMSG            +
                                 *IGNEXITPGM           +
                                 *IGNTGTRCV  )         +
                          PROMPT('Journal receiver delete opt.')

 QUAL1:      QUAL       TYPE(*NAME) LEN(10) MIN(1)
             QUAL       TYPE(*NAME) LEN(10) DFT(*LIBL) +
                          SPCVAL((*LIBL) (*CURLIB))    +
                          PROMPT('Library')

