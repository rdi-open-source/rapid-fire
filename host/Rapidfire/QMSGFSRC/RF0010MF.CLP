     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /* >>PRE-COMPILER<<                                                      */
     /*   >>CRTCMD<<  CALL       PGM(QTEMP/&OB) PARM(&LI &OB);                */
     /*   >>CMD<<     CRTCLPGM   PGM(QTEMP/&OB) +                             */
     /*                          SRCFILE(&SL/&SF) SRCMBR(&SM) +               */
     /*               TEXT('Maintain Conversion');                            */
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
                            MSGDTA('Usage: CALL PGM(RF0010MF) +
                                    PARM(libName [msgFName)') +
                            TOPGMQ(*PRV (*PGMBDY)) MSGTYPE(*ESCAPE)
             ENDDO

             CHGVAR     VAR(&OBJ       ) VALUE(&I_OBJ)
             MONMSG     MSGID(MCH3601) EXEC(DO)
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
               CHGVAR     VAR(&OBJ) VALUE('RF0010MF  ')
             ENDDO

             DLTMSGF MSGF(&LIB/&OBJ)
             MONMSG     MSGID(CPF2105)  EXEC(DO) /* Object not found */
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
             ENDDO

             CRTMSGF MSGF(&LIB/&OBJ) +
               TEXT('Maintain Conversion')

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0001                    */
     /*                     Create Conversion                       */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0001) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                    Create Conversion'                               +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0002                    */
     /*                     Change Conversion                       */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0002) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                    Change Conversion'                               +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0003                    */
     /*                      Copy Conversion                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0003) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                     Copy Conversion'                                +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0004                    */
     /*                     Delete Conversion                       */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0004) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                    Delete Conversion'                               +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0005                    */
     /*                     Display Conversion                      */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0005) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                    Display Conversion'                              +
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
     /* Job . . . . . . . . . . . . . . . :                         */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0007) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Job . . . . . . . . . . . . . . . :'                                 +
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
     /* Name                                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0009) +
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
     /*   Cmd-ADDMSGD für Message-ID :   CON0010                    */
     /* Field to convert  . . . . . . . . :                         */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0010) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Field to convert  . . . . . . . . :'                                 +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0011                    */
     /* Rename field in old file to . . . :                         */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0011) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Rename field in old file to . . . :'                                 +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0012                    */
     /* Name, *NONE                                                 */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0012) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Name, *NONE'                                                         +
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
     /* Position  . . . . . . . . . . . . :                         */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0014) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Position  . . . . . . . . . . . . :'                                 +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0015                    */
     /* If an attribute of a field has changed you have to enter a  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0015) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'If an attribute of a field has changed you have to enter a value here-
.'                                                                    +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0016                    */
     /* Example : Field_To_Convert = Rename_Field_In_Old_File_To;   */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0016) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Example : Field_To_Convert = Rename_Field_In_Old_File_To;'           +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0017                    */
     /* ------------------ Conversion statements ------------------ */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0017) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'------------------ Conversion statements -------------------'        +
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
