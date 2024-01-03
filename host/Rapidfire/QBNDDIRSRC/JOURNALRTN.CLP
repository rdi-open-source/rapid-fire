     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /* >>PRE-COMPILER<<                                                      */
     /*   >>CRTCMD<<  CALL       PGM(QTEMP/&OB) PARM(&LI &OB);                */
     /*   >>CMD<<     CRTCLPGM   PGM(QTEMP/&OB) +                             */
     /*                          SRCFILE(&SL/&SF) SRCMBR(&SM) +               */
     /*               TEXT('Returncode of Exit Program for Command RCVJRNE'); */
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

             CRTBNDDIR  BNDDIR(&LIB/&OBJ) TEXT('Returncode of Exit Program for Command RCVJRNE')

             ADDBNDDIRE BNDDIR(&LIB/&OBJ) OBJ((*LIBL/JOURNALRTN *SRVPGM *IMMED))

             RETURN

             /* --------------------------------------------------- */
             /*   Error Handler                                     */
             /* --------------------------------------------------- */
 ERRHDLR:    CALL       PGM(QMHMOVPM) PARM(&ERRMSGKEY &ERRMSGTYPE &ERRNUMTYPE +
                          &ERRSTKE &ERRSTKCNT &ERRCODE    )
             MONMSG     MSGID(CPF0000)

             CALL       PGM(QMHRSNEM) PARM(&ERRMSGKEY &ERRCODE    )

             ENDPGM
