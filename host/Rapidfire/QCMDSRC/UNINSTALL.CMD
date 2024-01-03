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
     /* Uninstall Rapid Fire                                                  */
     /*                                                                       */
     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /*   >>PRE-COMPILER<<                                                    */
     /*     >>CRTCMD<<    CRTCMD   CMD(&LI/&OB) +                             */
     /*                              SRCFILE(&SL/&SF) SRCMBR(&SM);            */
     /*       >>COMPILE<<                                                     */
     /*         >>PARM<<   PGM(*LIBL/UNINSTALL);                              */
     /*       >>END-COMPILE<<                                                 */
     /*       >>EXECUTE<<                                                     */
     /*   >>END-PRE-COMPILER<<                                                */
     /*-----------------------------------------------------------------------*/

             CMD        PROMPT('Uninstall Rapid Fire')

             PARM       KWD(LIB) TYPE(*NAME) LEN(10) MIN(1) +
                          PROMPT('Rapid Fire library')

             PARM       KWD(CURRFLIB) TYPE(*NAME) LEN(10) +
                          CONSTANT(*NONE) SPCVAL((*NONE))
