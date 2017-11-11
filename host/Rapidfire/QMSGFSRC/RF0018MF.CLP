     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /* >>PRE-COMPILER<<                                                      */
     /*   >>CRTCMD<<  CALL       PGM(QTEMP/&OB) PARM(&LI &OB);                */
     /*   >>CMD<<     CRTCLPGM   PGM(QTEMP/&OB) +                             */
     /*                          SRCFILE(&SL/&SF) SRCMBR(&SM) +               */
     /*               TEXT('Generate Copy Program');                          */
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
                            MSGDTA('Usage: CALL PGM(RF0018MF  ) +
                                    PARM(libName [msgFName)') +
                            TOPGMQ(*PRV (*PGMBDY)) MSGTYPE(*ESCAPE)
             ENDDO

             CHGVAR     VAR(&OBJ       ) VALUE(&I_OBJ)
             MONMSG     MSGID(MCH3601) EXEC(DO)
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
               CHGVAR     VAR(&OBJ) VALUE('RF0018MF  ')
             ENDDO

             DLTMSGF MSGF(&LIB/&OBJ)
             MONMSG     MSGID(CPF2105)  EXEC(DO) /* Object not found */
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
             ENDDO

             CRTMSGF MSGF(&LIB/&OBJ) +
               TEXT('Generate Copy Program')

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0001                    */
     /*                        Generate Copy Program                */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0001) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'                       Generate Copy Program'                        +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0006                    */
     /* F3=Exit  F12=Cancel                                         */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0006) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'F3=Exit  F12=Cancel'                                                 +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0007                    */
     /* Job  . . . . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0007) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Job  . . . . . . . :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0008                    */
     /* Press F10 to generate the copy program or F12 to cancel.    */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0008) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Press F10 to generate the copy program or F12 to cancel.'            +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0009                    */
     /* File . . . . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0009) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'File . . . . . . . :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0010                    */
     /* Position . . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0010) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Position . . . . . :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0013                    */
     /* F3=Exit  F4=Prompt  F12=Cancel                              */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0013) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'F3=Exit  F4=Prompt  F12=Cancel'                                      +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0014                    */
     /* Name                                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0014) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Name'                                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0015                    */
     /* Name                                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0015) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Name'                                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0016                    */
     /* Name                                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0016) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Name'                                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0017                    */
     /* Number                                                      */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0017) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Number'                                                              +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0018                    */
     /* Source file  . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0018) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Source file  . . . :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0019                    */
     /*   Library  . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0019) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'  Library  . . . . :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0022                    */
     /* Source member  . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0022) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Source member  . . :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0023                    */
     /* Area . . . . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0023) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Area . . . . . . . :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0024                    */
     /* Name                                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0024) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Name'                                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0025                    */
     /* *NONE, Name                                                 */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0025) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'*NONE, Name'                                                         +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0026                    */
     /* Library  . . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0026) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Library  . . . . . :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0027                    */
     /* Name                                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0027) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Name'                                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0028                    */
     /* Shadow library . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0028) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Shadow library . . :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0029                    */
     /* Name                                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0029) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Name'                                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0030                    */
     /* Conversion program :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0030) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Conversion program :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0031                    */
     /* Name, *NONE                                                 */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0031) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Name, *NONE'                                                         +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0032                    */
     /* Library  . . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0032) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Library  . . . . . :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0033                    */
     /* Name                                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0033) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Name'                                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0034                    */
     /* Replace  . . . . . :                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0034) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Replace  . . . . . :'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0035                    */
     /* *YES, *NO                                                   */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0035) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'*YES, *NO'                                                           +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0001                    */
     /* The value in field 'Source File' is not valid.              */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0001) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''Source File'' is not valid.'                    +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0002                    */
     /* The value in field 'Library' is not valid.                  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0002) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''Library'' is not valid.'                        +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0003                    */
     /* The value in field 'Source member' is not valid.            */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0003) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''Source member'' is not valid.'                  +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0004                    */
     /* Library does not exist.                                     */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0004) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Library does not exist.'                                             +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0005                    */
     /* File does not exist.                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0005) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'File does not exist.'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0006                    */
     /* File is not a source file.                                  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0006) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'File is not a source file.'                                          +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0007                    */
     /* Member already exists.                                      */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0007) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Member already exists.'                                              +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0008                    */
     /* The value in field 'Area' is not valid.                     */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0008) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''Area'' is not valid.'                           +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0009                    */
     /* Area does not exist.                                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0009) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Area does not exist.'                                                +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0010                    */
     /* Library record of area not found.                           */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0010) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Library record of area not found.'                                   +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0011                    */
     /* The value in field 'Conversion program' is not valid.       */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0011) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''Conversion program'' is not valid.'             +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0012                    */
     /* The value in field 'Library' is not valid.                  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0012) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''Library'' is not valid.'                        +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0013                    */
     /* The library must not be specified.                          */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0013) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The library must not be specified.'                                  +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0014                    */
     /* The value in field 'Replace' is not valid.                  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0014) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'The value in field ''Replace'' is not valid.'                        +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0015                    */
     /* No prompt for the requested field available.                */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0015) +
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
     /*   Cmd-ADDMSGD für Message-ID :   ERR0016                    */
     /* Could not generate copy program. Check job log for details. */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0016) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Could not generate copy program. Check job log for details.'         +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0017                    */
     /* Area and libraries are missing. Specify the area or the lib */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0017) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Area and libraries are missing. Specify the area or the libraries.'  +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0018                    */
     /* Data library with old file does not exist.                  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0018) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Data library with old file does not exist.'                          +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0019                    */
     /* Shadow library with new file does not exist.                */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0019) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Shadow library with new file does not exist.'                        +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0020                    */
     /* Old file does not exist in data library.                    */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0020) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'Old file does not exist in data library.'                            +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   ERR0021                    */
     /* New file does not exist in shadow library.                  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(ERR0021) +
                MSGF(&LIB/&OBJ) +
                MSG(+
'New file does not exist in shadow library.'                          +
                ) +
                SEV(00) +
     /*   Keine Ersatzvariablen definiert!                          */ +
                ALROPT(*NO) +
                CCSID( 65535) +
                /*  -EOF-  */

             RCLRSC LVL(*)

             ENDPGM
