     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /* >>PRE-COMPILER<<                                                      */
     /*   >>CRTCMD<<  CALL       PGM(QTEMP/&OB) PARM(&LI &OB);                */
     /*   >>CMD<<     CRTCLPGM   PGM(QTEMP/&OB) +                             */
     /*                          SRCFILE(&SL/&SF) SRCMBR(&SM) +               */
     /*               TEXT('Display Status');                                 */
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
                            MSGDTA('Usage: CALL PGM(RF0013MF  ) +
                                    PARM(libName [msgFName)') +
                            TOPGMQ(*PRV (*PGMBDY)) MSGTYPE(*ESCAPE)
             ENDDO

             CHGVAR     VAR(&OBJ       ) VALUE(&I_OBJ)
             MONMSG     MSGID(MCH3601) EXEC(DO)
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
               CHGVAR     VAR(&OBJ) VALUE('RF0013MF  ')
             ENDDO

             DLTMSGF MSGF(&LIB/&OBJ)
             MONMSG     MSGID(CPF2105)  EXEC(DO) /* Object not found */
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
             ENDDO

             CRTMSGF MSGF(&LIB/&OBJ) +
               TEXT('Display Status')

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0001                    */
     /*                                                  Display St */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0001) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'                                                 Display Status'     +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0002                    */
     /* Type option and press enter.                                */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0002) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Type option and press enter.'                                        +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0003                    */
     /*   99=Reapply all changes                                    */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0003) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'  99=Reapply all changes'                                            +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0006                    */
     /* F3=Exit  F5=Refresh  F12=Cancel  F17=Top  F18=Bottom        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0006) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'F3=Exit  F5=Refresh  F12=Cancel  F17=Top  F18=Bottom'                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0008                    */
     /* ( No positions available )                                  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0008) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'( No positions available )'                                          +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0009                    */
     /* OP  Name        Library     prod. lib.     shadow lib.    c */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0009) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'OP  Name        Library     prod. lib.     shadow lib.    copy       -
    copied         time         apply          applied'               +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0011                    */
     /* Job  . . . . . . . . . . . . :                              */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0011) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Job  . . . . . . . . . . . . :'                                      +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0013                    */
     /*        More ...                                             */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0013) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'       More ...'                                                     +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0014                    */
     /*             End                                             */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0014) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'            End'                                                     +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0016                    */
     /*     -------- File --------  Records in     Records in     R */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0016) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'    -------- File --------  Records in     Records in     Records to -
    Records        Estimated    Changes to     Changes'               +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0001                    */
     /* The option is not valid.                                    */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0001) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The option is not valid.'                                            +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0002                    */
     /* The top of the list has already been reached.               */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0002) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The top of the list has already been reached.'                       +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0003                    */
     /* The bottom of the list has already been reached.            */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0003) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The bottom of the list has already been reached.'                    +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0004                    */
     /* No prompt for the requested field available.                */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0004) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'No prompt for the requested field available.'                        +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0005                    */
     /* The option "Reapply all changes" is only valid in status *R */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0005) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The option "Reapply all changes" is only valid in status *RUN and pha-
se *APY-CHG.'                                                         +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

             RCLRSC LVL(*)

             ENDPGM
