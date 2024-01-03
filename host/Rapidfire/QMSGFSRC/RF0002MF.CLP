     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /* >>PRE-COMPILER<<                                                      */
     /*   >>CRTCMD<<  CALL       PGM(QTEMP/&OB) PARM(&LI &OB);                */
     /*   >>CMD<<     CRTCLPGM   PGM(QTEMP/&OB) +                             */
     /*                          SRCFILE(&SL/&SF) SRCMBR(&SM) +               */
     /*               TEXT('Maintain Job');                                   */
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
                            MSGDTA('Usage: CALL PGM(RF0002MF) +
                                    PARM(libName [msgFName)') +
                            TOPGMQ(*PRV (*PGMBDY)) MSGTYPE(*ESCAPE)
             ENDDO

             CHGVAR     VAR(&OBJ       ) VALUE(&I_OBJ)
             MONMSG     MSGID(MCH3601) EXEC(DO)
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
               CHGVAR     VAR(&OBJ) VALUE('RF0002MF  ')
             ENDDO

             DLTMSGF MSGF(&LIB/&OBJ)
             MONMSG     MSGID(CPF2105)  EXEC(DO) /* Object not found */
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
             ENDDO

             CRTMSGF MSGF(&LIB/&OBJ) +
               TEXT('Maintain Job')

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0001                    */
     /*                        Create Job                           */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0001) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                       Create Job'                                   +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0002                    */
     /*                        Change Job                           */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0002) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                       Change Job'                                   +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0003                    */
     /*                         Copy Job                            */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0003) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                        Copy Job'                                    +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0004                    */
     /*                        Delete Job                           */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0004) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                       Delete Job'                                   +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0005                    */
     /*                        Display Job                          */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0005) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                       Display Job'                                  +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0006                    */
     /* F3=Exit  F12=Cancel                                         */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0006) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'F3=Exit  F12=Cancel'                                                 +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0007                    */
     /* Name                                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0007) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Name'                                                                +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0008                    */
     /* Please confirm deletion by pressing enter. Otherwise press  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0008) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Please confirm deletion by pressing enter. Otherwise press F12.'     +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0009                    */
     /* Create environment :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0009) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Create environment :'                                                +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0010                    */
     /* Job  . . . . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0010) +
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
     /*   Cmd-ADDMSGD für Message-ID :   CON0011                    */
     /* Description  . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0011) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Description  . . . :'                                                +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0012                    */
     /* *YES, *NO                                                   */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0012) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'*YES, *NO'                                                           +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0013                    */
     /* F3=Exit  F4=Prompt  F12=Cancel                              */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0013) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'F3=Exit  F4=Prompt  F12=Cancel'                                      +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0014                    */
     /* Cancel, if ASP threshold exceeds  :                         */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0014) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Cancel, if ASP threshold exceeds  :'                                 +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0015                    */
     /* *YES, *NO                                                   */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0015) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'*YES, *NO'                                                           +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0056                    */
     /* Job queue  . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0056) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Job queue  . . . . :'                                                +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0057                    */
     /*   Library  . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0057) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'  Library  . . . . :'                                                +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0068                    */
     /* Name                                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0068) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Name'                                                                +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0069                    */
     /* Name, *LIBL, *CURLIB                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0069) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Name, *LIBL, *CURLIB'                                                +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0007                    */
     /* No prompt for the requested field available.                */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0007) +
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
