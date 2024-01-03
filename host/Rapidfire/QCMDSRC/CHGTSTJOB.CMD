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
     /* Change Test Job                                                       */
     /*                                                                       */
     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /*   >>PRE-COMPILER<<                                                    */
     /*     >>CRTCMD<<    CRTCMD   CMD(&LI/&OB) +                             */
     /*                              SRCFILE(&SL/&SF) SRCMBR(&SM);            */
     /*       >>COMPILE<<                                                     */
     /*         >>PARM<<   PGM(*LIBL/CHGTSTJOB);                              */
     /*       >>END-COMPILE<<                                                 */
     /*       >>EXECUTE<<                                                     */
     /*   >>END-PRE-COMPILER<<                                                */
     /*-----------------------------------------------------------------------*/

             CMD        PROMPT('Change Test Job')

             PARM       KWD(JOB) TYPE(*NAME) LEN(10) MIN(1) +
                          PROMPT('Job')

             PARM       KWD(FROMROW) TYPE(*DEC) LEN(6 0) MIN(1) +
                          PROMPT('From row')

             PARM       KWD(TOROW) TYPE(*DEC) LEN(6 0) MIN(1) +
                          PROMPT('To row')

             PARM       KWD(CHGVAL) TYPE(*CHAR) LEN(3) MIN(1) +
                          PROMPT('Change value')
