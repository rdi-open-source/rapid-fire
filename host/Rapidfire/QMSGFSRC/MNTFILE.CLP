     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /* >>PRE-COMPILER<<                                                      */
     /*   >>CRTCMD<<  CALL       PGM(QTEMP/&OB) PARM(&LI &OB);                */
     /*   >>CMD<<     CRTCLPGM   PGM(QTEMP/&OB) +                             */
     /*                          SRCFILE(&SL/&SF) SRCMBR(&SM) +               */
     /*               TEXT('Maintain File');                                  */
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
                            MSGDTA('Usage: CALL PGM(MNTFILE) +
                                    PARM(libName [msgFName)') +
                            TOPGMQ(*PRV (*PGMBDY)) MSGTYPE(*ESCAPE)
             ENDDO

             CHGVAR     VAR(&OBJ       ) VALUE(&I_OBJ)
             MONMSG     MSGID(MCH3601) EXEC(DO)
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
               CHGVAR     VAR(&OBJ) VALUE('MNTFILE   ')
             ENDDO

             DLTMSGF MSGF(&LIB/&OBJ)
             MONMSG     MSGID(CPF2105)  EXEC(DO) /* Object not found */
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
             ENDDO

             CRTMSGF MSGF(&LIB/&OBJ) +
               TEXT('Maintain File')

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0001                    */
     /* The position does already exist.                            */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0001) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The position does already exist.'                                    +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0002                    */
     /* The value in field 'Position' is not valid.                 */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0002) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The value in field ''Position'' is not valid.'                       +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0003                    */
     /* Invalid parameter values. Error = &1.                       */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0003) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Invalid parameter values. Error = &1.'                               +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 256) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0004                    */
     /* The action is only valid for jobs with status *RDY.         */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0004) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The action is only valid for jobs with status *RDY.'                 +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0005                    */
     /* The value in field 'File' is not valid.                     */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0005) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The value in field ''File'' is not valid.'                           +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0006                    */
     /* The value in field 'Type' is not valid.                     */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0006) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The value in field ''Type'' is not valid.'                           +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0007                    */
     /* The action is only valid for files with "Type" *PHYSICAL.   */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0007) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The action is only valid for files with "Type" *PHYSICAL.'           +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0009                    */
     /* The value in field 'Copy program' is not valid.             */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0009) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The value in field ''Copy program'' is not valid.'                   +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0010                    */
     /* For physical files the value *NONE is not allowed.          */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0010) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'For physical files the value *NONE is not allowed.'                  +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0011                    */
     /* For logical files only the value *NONE is allowed.          */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0011) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'For logical files only the value *NONE is allowed.'                  +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0012                    */
     /* The value in field 'Copy program - Library' is not valid.   */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0012) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The value in field ''Copy program - Library'' is not valid.'         +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0013                    */
     /* The library has to be specified.                            */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0013) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The library has to be specified.'                                    +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0014                    */
     /* The library must not be specified.                          */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0014) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The library must not be specified.'                                  +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0015                    */
     /* The value in field 'Conversion program' is not valid.       */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0015) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The value in field ''Conversion program'' is not valid.'             +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0016                    */
     /* For logical files only the value *NONE is allowed.          */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0016) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'For logical files only the value *NONE is allowed.'                  +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0017                    */
     /* The value in field 'Conversion program - Library' is not va */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0017) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The value in field ''Conversion program - Library'' is not valid.'   +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0018                    */
     /* The library has to be specified.                            */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0018) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The library has to be specified.'                                    +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0019                    */
     /* The library must not be specified.                          */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0019) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The library must not be specified.'                                  +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0020                    */
     /* The action is only valid for jobs with "Create environment" */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0020) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The action is only valid for jobs with "Create environment" *YES.'   +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0021                    */
     /* Invalid parameter values. Error = Position has to be zero.  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0021) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Invalid parameter values. Error = Position has to be zero.'          +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0022                    */
     /* &1                                                          */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0022) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'&1'                                                                  +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 256) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

             RCLRSC LVL(*)

             ENDPGM
