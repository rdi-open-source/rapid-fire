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
     /* Create Test Job                                                       */
     /*                                                                       */
     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /*   >>PRE-COMPILER<<                                                    */
     /*     >>CRTCMD<<    CRTCMD   CMD(&LI/&OB) +                             */
     /*                              SRCFILE(&SL/&SF) SRCMBR(&SM);            */
     /*       >>COMPILE<<                                                     */
     /*         >>PARM<<   PGM(*LIBL/CRTTSTJOB);                              */
     /*       >>END-COMPILE<<                                                 */
     /*       >>EXECUTE<<                                                     */
     /*   >>END-PRE-COMPILER<<                                                */
     /*-----------------------------------------------------------------------*/

             CMD        PROMPT('Create Test Job')

             PARM       KWD(JOB) TYPE(*NAME) LEN(10) MIN(1) +
                          PROMPT('Job')

             PARM       KWD(PRDLIB) TYPE(*NAME) LEN(10) MIN(1) +
                          PROMPT('Production library')

             PARM       KWD(SDWLIB) TYPE(*NAME) LEN(10) MIN(1) +
                          PROMPT('Shadow library')

             PARM       KWD(NBRTBL) TYPE(*DEC) LEN(3 0) MIN(1) +
                          PROMPT('Number of tables')

             PARM       KWD(NBRCOL) TYPE(*DEC) LEN(3 0) MIN(1) +
                          PROMPT('Number of columns')

             PARM       KWD(NBRROW) TYPE(*DEC) LEN(6 0) MIN(1) +
                          PROMPT('Number of rows')

             PARM       KWD(NBRIDX) TYPE(*DEC) LEN(3 0) MIN(1) +
                          PROMPT('Number of indices')

             PARM       KWD(ADDCOL) TYPE(*CHAR) LEN(10) RSTD(*YES) +
                          VALUES(*YES *NO) MIN(1) +
                          PROMPT('Additional column (Shadow)')

             PARM       KWD(JOBQ) TYPE(JOBQ) MIN(1) PROMPT('Job queue')
 JOBQ:       QUAL       TYPE(*NAME) LEN(10) MIN(1)
             QUAL       TYPE(*NAME) LEN(10) DFT(*LIBL) +
                          SPCVAL((*LIBL) (*CURLIB)) PROMPT('Library')
