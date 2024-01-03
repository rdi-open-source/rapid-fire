     /*-----------------------------------------------------------------------*/
     /*                                                                       */
     /* Copyright (c) 2018 Task Force IT-Consulting GmbH, Waltrop (Germany)   */
     /* This software and the accompanying materials are made available       */
     /* under the terms of the GNU General Public License version 2.0 (GPLv2) */
     /* which accompanies this distribution and is available at               */
     /* http://www.gnu.org/licenses/gpl-2.0.html                              */
     /*                                                                       */
     /*-----------------------------------------------------------------------*/
     /*                                                                       */
     /* Rapid Fire Build                                                      */
     /*                                                                       */
     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /*   >>PRE-COMPILER<<                                                    */
     /*     >>CRTCMD<<    CRTCMD   CMD(&LI/&OB) +                             */
     /*                              SRCFILE(&SL/&SF) SRCMBR(&SM);            */
     /*       >>COMPILE<<                                                     */
     /*         >>PARM<<   PGM(*LIBL/BUILD);                                  */
     /*         >>PARM<<   HLPPNLGRP(*LIBL/BUILD);                            */
     /*         >>PARM<<   HLPID(BUILD);                                      */
     /*       >>END-COMPILE<<                                                 */
     /*       >>EXECUTE<<                                                     */
     /*   >>END-PRE-COMPILER<<                                                */
     /*-----------------------------------------------------------------------*/

             CMD        PROMPT('Rapid Fire Build')

             PARM       KWD(LIB) TYPE(*NAME) LEN(10) DFT(RAPIDFIRE) +
                          PROMPT('Rapid Fire library')

             PARM       KWD(CPYSAVF) TYPE(*CHAR) LEN(10) DFT(*NO) +
                          RSTD(*YES) SPCVAL((*YES) (*NO)) +
                          PROMPT('Copy save file')

