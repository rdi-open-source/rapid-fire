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
             PGM        PARM(&I_LIB &I_OBJ)

             DCL        VAR(&I_LIB     ) TYPE(*CHAR) LEN(10)
             DCL        VAR(&I_OBJ     ) TYPE(*CHAR) LEN(10)
             DCL        VAR(&LIB       ) TYPE(*CHAR) LEN(10)
             DCL        VAR(&OBJ       ) TYPE(*CHAR) LEN(10)

             CHGVAR     VAR(&LIB       ) VALUE(&I_LIB)
             MONMSG     MSGID(MCH3601) EXEC(DO)
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
               SNDPGMMSG  MSGID(CPF9898) MSGF(*LIBL/QCPFMSG) +
                            MSGDTA('Usage: CALL PGM(RAPIDFIRE) +
                                    PARM(libName [msgFName)') +
                            TOPGMQ(*PRV (*PGMBDY)) MSGTYPE(*ESCAPE)
             ENDDO

             CHGVAR     VAR(&OBJ       ) VALUE(&I_OBJ)
             MONMSG     MSGID(MCH3601) EXEC(DO)
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
               CHGVAR     VAR(&OBJ) VALUE('RAPIDFIRE ')
             ENDDO

             DLTMSGF MSGF(&LIB/&OBJ)
             MONMSG     MSGID(CPF2105)  EXEC(DO) /* Object not found */
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
             ENDDO

             CRTMSGF MSGF(&LIB/&OBJ) +
               TEXT('Rapid Fire')

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0001                    */
     /* Journal job for journal &1 in library &2 could not be submi */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0001) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Journal job for journal &1 in library &2 could not be submitted.'    +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0002                    */
     /* Journal job for journal &1 in library &2 could not be retri */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0002) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Journal job for journal &1 in library &2 could not be retrieved.'    +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0003                    */
     /* No journal jobs submitted.                                  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0003) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'No journal jobs submitted.'                                          +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0004                    */
     /* At least one submitted job has not started within 120 secon */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0004) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'At least one submitted job has not started within 120 seconds.'      +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0005                    */
     /* Journal job for journal &1 in library &2 could not read "Ch */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0005) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Journal job for journal &1 in library &2 could not read "Changes Work-
file" for file &3 in library &4.'                                     +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0006                    */
     /* Journal job for journal &1 in library &2 could not write to */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0006) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Journal job for journal &1 in library &2 could not write to "Changes -
Workfile" for file &3 in library &4. Failed Statement : &5'           +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 128) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0007                    */
     /* User space PROMOTOR in library QTEMP could not be created.  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0007) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'User space PROMOTOR in library QTEMP could not be created.'          +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0008                    */
     /* Pointer to user space PROMOTOR in library QTEMP could not b */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0008) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Pointer to user space PROMOTOR in library QTEMP could not be retrieve-
d.'                                                                   +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0009                    */
     /* User space PROMOTOR in library QTEMP could not be deleted.  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0009) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'User space PROMOTOR in library QTEMP could not be deleted.'          +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0010                    */
     /* Current library list could not be retrieved.                */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0010) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Current library list could not be retrieved.'                        +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0011                    */
     /* The journal of file &1 in library &2 could not be retrieved */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0011) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The journal of file &1 in library &2 could not be retrieved.'        +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0012                    */
     /* The ASP of library &1 could not be retrieved.               */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0012) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The ASP of library &1 could not be retrieved.'                       +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0013                    */
     /* The ASP device of library &1 could not be retrieved.        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0013) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The ASP device of library &1 could not be retrieved.'                +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0014                    */
     /* Library &1 could not be created.                            */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0014) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Library &1 could not be created.'                                    +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0015                    */
     /* Command for position &1, area &2, type &3 and sequence &4 c */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0015) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Command for position &1, area &2, type &3 and sequence &4 could not b-
e executed.'                                                          +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 6) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 3) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0016                    */
     /* Library list &1 could not be seted/reseted : Type = &2.     */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0016) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Library list &1 could not be seted/reseted : Type = &2.'             +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0017                    */
     /* Source physical file of copy program for file &1 in library */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0017) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Source physical file of copy program for file &1 in library &2 could -
not be created in library QTEMP.'                                     +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0018                    */
     /* Source file of copy program for file &1 in library &2 could */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0018) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Source file of copy program for file &1 in library &2 could not be op-
ened.'                                                                +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0019                    */
     /* Copy program for file &1 in library &2 could not be compile */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0019) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Copy program for file &1 in library &2 could not be compiled.'       +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0020                    */
     /* Source physical file of copy program for file &1 in library */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0020) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Source physical file of copy program for file &1 in library &2 could -
not be deleted in library QTEMP.'                                     +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0021                    */
     /* The record format of file &1 in library &2 could not be ret */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0021) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The record format of file &1 in library &2 could not be retrieved.'  +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0022                    */
     /* File &1 in library &2 has more than one record format.      */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0022) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'File &1 in library &2 has more than one record format.'              +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0023                    */
     /* The members of file &1 in library &2 could not be retrieved */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0023) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The members of file &1 in library &2 could not be retrieved.'        +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0024                    */
     /* Member &1 could not be added to file &2 in library &3.      */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0024) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Member &1 could not be added to file &2 in library &3.'              +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0025                    */
     /* The description of member &1 in file &2 in library &3 could */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0025) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The description of member &1 in file &2 in library &3 could not be re-
trieved.'                                                             +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0026                    */
     /* Workfile Records for file &1 in library &2 could not be cre */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0026) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Workfile Records for file &1 in library &2 could not be created.'    +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0027                    */
     /* Workfile Changes for file &1 in library &2 could not be cre */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0027) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Workfile Changes for file &1 in library &2 could not be created.'    +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0028                    */
     /* The description file &1 in library &2 could not be retrieve */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0028) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The description file &1 in library &2 could not be retrieved.'       +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0029                    */
     /* The parameter FRCRATIO in file &1 in library &2 could not b */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0029) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The parameter FRCRATIO in file &1 in library &2 could not be set to t-
he value &3.'                                                         +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0030                    */
     /* Copy program for file &1 in library &2 crashed. Mode &3.    */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0030) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Copy program for file &1 in library &2 crashed. Mode &3.'            +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0031                    */
     /* File job for file &1 in library &2 could not be submitted.  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0031) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'File job for file &1 in library &2 could not be submitted.'          +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0032                    */
     /* Journal job for journal &1 in library &2 could not be relea */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0032) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Journal job for journal &1 in library &2 could not be released.'     +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0033                    */
     /* Control job for job &1 could not be submitted.              */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0033) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Control job for job &1 could not be submitted.'                      +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0034                    */
     /* Control job for job &1 could not be retrieved.              */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0034) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Control job for job &1 could not be retrieved.'                      +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0035                    */
     /* Control job for job &1 could not be released.               */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0035) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Control job for job &1 could not be released.'                       +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0036                    */
     /* Library &1 does not exit.                                   */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0036) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Library &1 does not exit.'                                           +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0037                    */
     /* File &1 in library &2 does not exit.                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0037) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'File &1 in library &2 does not exit.'                                +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0038                    */
     /* Target file of copy program for file &1 in library &2 could */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0038) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Target file of copy program for file &1 in library &2 could not be op-
ened.'                                                                +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0039                    */
     /* Journaling for file &1 in library &2 could not be started.  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0039) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Journaling for file &1 in library &2 could not be started.'          +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0040                    */
     /* Journaling for file &1 in library &2 could not be ended.    */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0040) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Journaling for file &1 in library &2 could not be ended.'            +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0041                    */
     /* Journal RAPIDFIRE could not be created in library &1.       */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0041) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Journal RAPIDFIRE could not be created in library &1.'               +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0042                    */
     /* At least one job is no longer active.                       */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0042) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'At least one job is no longer active.'                               +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0043                    */
     /* File job for file &1 in library &2 could not be retrieved.  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0043) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'File job for file &1 in library &2 could not be retrieved.'          +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0044                    */
     /* File job for file &1 in library &2 could not be released.   */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0044) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'File job for file &1 in library &2 could not be released.'           +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0045                    */
     /* No file jobs submitted.                                     */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0045) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'No file jobs submitted.'                                             +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0046                    */
     /* Data for file &1 in library &2 could not be retrieved.      */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0046) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Data for file &1 in library &2 could not be retrieved.'              +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0047                    */
     /* All records in all files of Rapid Fire job &1 have copied.  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0047) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'All records in all files of Rapid Fire job &1 have copied. The Rapid -
Fire job &1 is ready for installation.'                               +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0048                    */
     /* Journal job for journal &1 in library &2 received unexpecte */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0048) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Journal job for journal &1 in library &2 received unexpected status f-
rom command RCVJRNE.'                                                 +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0049                    */
     /* Command RCVJRNE has finished with error &1.                 */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0049) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Command RCVJRNE has finished with error &1.'                         +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 7) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0050                    */
     /* Exit program for command RCVJRNE has finished with error.   */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0050) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Exit program for command RCVJRNE has finished with error.'           +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0051                    */
     /* Could not retrieve field list of file &1 in library &2.     */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0051) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Could not retrieve field list of file &1 in library &2.'             +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0052                    */
     /* Journal job for journal &1 in library &2 could not retrieve */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0052) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Journal job for journal &1 in library &2 could not retrieve changes i-
n "Changes Workfile" for file &3 in library &4. Failed Stm.:&5'       +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 128) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0053                    */
     /* Workfile Changes Log for file &1 in library &2 could not be */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0053) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Workfile Changes Log for file &1 in library &2 could not be created.'+
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0054                    */
     /* Copy program could not open file &1.                        */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0054) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Copy program could not open file &1.'                                +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 21) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0055                    */
     /* Workfile "Records Target" for file &1 in library &2 could n */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0055) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Workfile "Records Target" for file &1 in library &2 could not be crea-
ted.'                                                                 +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0056                    */
     /* At least one combination of library and file has been speci */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0056) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'At least one combination of library and file has been specified multi-
ple times.'                                                           +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0057                    */
     /* &1                                                          */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0057) +
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

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0058                    */
     /* File &1 in library &2 contains fields with "Special Data Ty */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0058) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'File &1 in library &2 contains fields with "Special Data Types" like -
LOB and DATALINK. This is not supported.'                             +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0059                    */
     /* Journal job for journal &1 in library &2 received performed */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0059) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Journal job for journal &1 in library &2 received performed command R-
GZPFM for file &3 in library &4.'                                     +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0060                    */
     /* Command PWRDWNSYS, ENDSBS or ENDJOB with parameter OPTION(* */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0060) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Command PWRDWNSYS, ENDSBS or ENDJOB with parameter OPTION(*CNTRLD) ha-
s performed.'                                                         +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0061                    */
     /* There are more than 300 files specified. 300 files is the l */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0061) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'There are more than 300 files specified. 300 files is the limit for a-
 Rapid Fire job.'                                                     +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0062                    */
     /* File &1 in shadow library &2 has journaled. Perhaps a HA so */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0062) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'File &1 in shadow library &2 has journaled. Perhaps a HA solution lik-
e BUS/400, iTERA, MIMIX ... has caused the problem.'                  +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 10) +
                    (*CHAR 10) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   MSG0063                    */
     /* The threshold for ASP &1 has exceeded.                      */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(MSG0063) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The threshold for ASP &1 has exceeded.'                              +
                ) +
                SEV(0) +
                FMT( +
                    (*CHAR 25) +
                ) +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

             RCLRSC LVL(*)

             ENDPGM
