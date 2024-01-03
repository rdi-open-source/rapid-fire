     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /* >>PRE-COMPILER<<                                                      */
     /*   >>CRTCMD<<  CALL       PGM(QTEMP/&OB) PARM(&LI &OB);                */
     /*   >>CMD<<     CRTCLPGM   PGM(QTEMP/&OB) +                             */
     /*                          SRCFILE(&SL/&SF) SRCMBR(&SM) +               */
     /*               TEXT('Maintain Activity Schedule');                     */
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
                            MSGDTA('Usage: CALL PGM(RF0020MF) +
                                    PARM(libName [msgFName)') +
                            TOPGMQ(*PRV (*PGMBDY)) MSGTYPE(*ESCAPE)
             ENDDO

             CHGVAR     VAR(&OBJ       ) VALUE(&I_OBJ)
             MONMSG     MSGID(MCH3601) EXEC(DO)
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
               CHGVAR     VAR(&OBJ) VALUE('RF0020MF  ')
             ENDDO

             DLTMSGF MSGF(&LIB/&OBJ)
             MONMSG     MSGID(CPF2105)  EXEC(DO) /* Object not found */
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
             ENDDO

             CRTMSGF MSGF(&LIB/&OBJ) +
               TEXT('Maintain Activity Schedule')

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0001                    */
     /*                  Maintain Activity Schedule                 */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0001) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                 Maintain Activity Schedule'                         +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0002                    */
     /*                   Display Activity Schedule                 */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0002) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                  Display Activity Schedule'                         +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0003                    */
     /* Type option and press enter.                                */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0003) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Type option and press enter.'                                        +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0004                    */
     /*   1=Set Activity ON/OFF                                     */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0004) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'  1=Set Activity ON/OFF'                                             +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0006                    */
     /* F3=Exit  F4=Prompt  F5=Refresh  F9=Retrieve  F12=Cancel  F2 */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0006) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'F3=Exit  F4=Prompt  F5=Refresh  F9=Retrieve  F12=Cancel  F24=More key-
s'                                                                    +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0008                    */
     /* ( No positions available )                                  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0008) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'( No positions available )'                                          +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0009                    */
     /* Opt.  ----- T I M E -----  Activity                         */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0009) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Opt.  ----- T I M E -----  Activity'                                 +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0010                    */
     /* Command                                                     */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0010) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Command'                                                             +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0011                    */
     /* Job  . . . . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0011) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Job  . . . . . . . :'                                                +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0013                    */
     /*        More ...                                             */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0013) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'       More ...'                                                     +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0014                    */
     /*             End                                             */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0014) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'            End'                                                     +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0015                    */
     /* F17=Top  F18=Bottom  F24=More keys                          */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0015) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'F17=Top  F18=Bottom  F24=More keys'                                  +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0001                    */
     /* The option is not valid.                                    */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0001) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The option is not valid.'                                            +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0002                    */
     /* The top of the list has already been reached.               */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0002) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The top of the list has already been reached.'                       +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0003                    */
     /* The bottom of the list has already been reached.            */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0003) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The bottom of the list has already been reached.'                    +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0004                    */
     /* No prompt for the requested field available.                */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0004) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'No prompt for the requested field available.'                        +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

             RCLRSC LVL(*)

             ENDPGM
