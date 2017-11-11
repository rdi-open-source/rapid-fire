     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /* >>PRE-COMPILER<<                                                      */
     /*   >>CRTCMD<<  CALL       PGM(QTEMP/&OB) PARM(&LI &OB);                */
     /*   >>CMD<<     CRTCLPGM   PGM(QTEMP/&OB) +                             */
     /*                          SRCFILE(&SL/&SF) SRCMBR(&SM) +               */
     /*               TEXT('Maintain Subjects To Be Notified');               */
     /*   >>EXECUTE<<                                                         */
     /*   >>CMD<<     DLTPGM PGM(QTEMP/&OB);                                  */
     /* >>END-PRE-COMPILER<<                                                  */
     /*-----------------------------------------------------------------------*/
             PGM        PARM(&I_LIB &I_OBJ)

             DCL        VAR(&I_LIB     ) TYPE(*CHAR) LEN(10)
             DCL        VAR(&I_OBJ     ) TYPE(*CHAR) LEN(10)
             DCL        VAR(&LIB       ) TYPE(*CHAR) LEN(10)
             DCL        VAR(&OBJ       ) TYPE(*CHAR) LEN(10)

             CHGVAR     VAR(&LIB       ) VALUE(&I_LIB)
             MONMSG     MSGID(MCH3601) EXEC(DO)
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
               SNDPGMMSG  MSGID(CPF9898) MSGF(*LIBL/QCPFMSG) +
                            MSGDTA('Usage: CALL PGM(MNTSTBN   ) +
                                    PARM(libName [msgFName)') +
                            TOPGMQ(*PRV (*PGMBDY)) MSGTYPE(*ESCAPE)
             ENDDO

             CHGVAR     VAR(&OBJ       ) VALUE(&I_OBJ)
             MONMSG     MSGID(MCH3601) EXEC(DO)
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
               CHGVAR     VAR(&OBJ) VALUE('MNTSTBN   ')
             ENDDO

             DLTMSGF MSGF(&LIB/&OBJ)
             MONMSG     MSGID(CPF2105)  EXEC(DO) /* Object not found */
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
             ENDDO

             CRTMSGF MSGF(&LIB/&OBJ) +
               TEXT('Maintain Subjects To Be Notified')

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0001                    */
     /* The position does already exist.                            */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0001) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The position does already exist.'                                    +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0002                    */
     /* The value in field 'Position' is not valid.                 */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0002) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''Position'' is not valid.'                       +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0005                    */
     /* The value in field 'User' is not valid.                     */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0005) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''User'' is not valid.'                           +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0006                    */
     /* The value in field 'Type' is not valid.                     */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0006) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''Type'' is not valid.'                           +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0009                    */
     /* The value in field 'Message queue' is not valid.            */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0009) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''Message queue'' is not valid.'                  +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0012                    */
     /* The value in field 'Message queue - Library' is not valid.  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0012) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''Message queue - Library'' is not valid.'        +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

             RCLRSC LVL(*)

             ENDPGM
