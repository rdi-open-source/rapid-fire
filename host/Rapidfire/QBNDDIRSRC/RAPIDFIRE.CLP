     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /* >>PRE-COMPILER<<                                                      */
     /*   >>CRTCMD<<  CALL       PGM(QTEMP/&OB) PARM(&LI &OB);                */
     /*   >>CMD<<     CRTCLPGM   PGM(QTEMP/&OB) +                             */
     /*                          SRCFILE(&SL/&SF) SRCMBR(&SM) +               */
     /*               TEXT('Rapid Fire');                                     */
     /*   >>EXECUTE<<                                                         */
     /*   >>CMD<<     DLTPGM PGM(QTEMP/&OB);                                  */
     /* >>END-PRE-COMPILER<<                                                  */
     /*-----------------------------------------------------------------------*/
             PGM        PARM(&LIB &OBJ)

             DCL        VAR(&LIB ) TYPE(*CHAR) LEN(10)
             DCL        VAR(&OBJ ) TYPE(*CHAR) LEN(10)


             /* ------------------------- Error handler ------------------------- */
             DCL        VAR(&ERRMSGKEY ) TYPE(*CHAR) LEN( 4)
             DCL        VAR(&ERRMSGTYPE) TYPE(*CHAR) LEN(10) VALUE('*DIAG')
             DCL        VAR(&ERRNUMTYPE) TYPE(*CHAR) LEN( 4) VALUE(x'00000001')
             DCL        VAR(&ERRSTKE   ) TYPE(*CHAR) LEN(10) VALUE('*PGMBDY')
             DCL        VAR(&ERRSTKCNT ) TYPE(*CHAR) LEN( 4) VALUE(x'00000001')
             DCL        VAR(&ERRCODE   ) TYPE(*CHAR) LEN( 8) +
                          VALUE(x'0000000000000000')

             MONMSG     MSGID(CPF0000) EXEC(GOTO ERRHDLR)
             /* ----------------------------------------------------------------- */

             DLTBNDDIR  BNDDIR(&LIB/&OBJ)
             MONMSG     MSGID(CPF2105) EXEC(DO)
                RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
             ENDDO

             CRTBNDDIR  BNDDIR(&LIB/&OBJ) TEXT('Rapid Fire')

             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/EXCCMD     *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/PROMOTER   *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/RAPIDFIRE  *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/QJOURNAL   *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/CMDLIN     *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/MISC       *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/JOB        *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/NOTIFIER   *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/FLDATTRS   *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/SQLSTS     *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/WHOAMI     *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/FILE       *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/CPLSTSE    *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/MNTJOB     *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/MNTLIB     *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/MNTFILE    *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/MNTAREA    *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/MNTCNV     *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/MNTCMD     *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/MNTSTBN    *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/MNTLIBL    *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/MNTAS      *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/CHKSTSE    *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/GNRCPYPGM  *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/CHKSTSE    *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/EXCSQL     *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/PRCSDT     *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/LANGUAGE   *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/EXCCMDVAR  *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/CRTTSTJOB  *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/CHGTSTJOB  *SRVPGM *IMMED))
             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/FLDGENCLS  *SRVPGM *IMMED))

             RETURN

             /* --------------------------------------------------- */
             /*   Error Handler                                     */
             /* --------------------------------------------------- */
 ERRHDLR:    CALL       PGM(QMHMOVPM) PARM(&ERRMSGKEY &ERRMSGTYPE &ERRNUMTYPE +
                          &ERRSTKE &ERRSTKCNT &ERRCODE    )
             MONMSG     MSGID(CPF0000)

             CALL       PGM(QMHRSNEM) PARM(&ERRMSGKEY &ERRCODE    )

             ENDPGM
