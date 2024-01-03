     /*-----------------------------------------------------------------------*/
     /* STRPREPRC Compile Options:                                            */
     /* >>PRE-COMPILER<<                                                      */
     /*   >>CRTCMD<<  CALL       PGM(QTEMP/&OB) PARM(&LI &OB);                */
     /*   >>CMD<<     CRTCLPGM   PGM(QTEMP/&OB) +                             */
     /*                          SRCFILE(&SL/&SF) SRCMBR(&SM) +               */
     /*               TEXT('Fields with GENERATED clause');                   */
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
                            MSGDTA('Usage: CALL PGM(RF0023MF) +
                                    PARM(libName [msgFName)') +
                            TOPGMQ(*PRV (*PGMBDY)) MSGTYPE(*ESCAPE)
             ENDDO

             CHGVAR     VAR(&OBJ       ) VALUE(&I_OBJ)
             MONMSG     MSGID(MCH3601) EXEC(DO)
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
               CHGVAR     VAR(&OBJ) VALUE('RF0023MF  ')
             ENDDO

             DLTMSGF MSGF(&LIB/&OBJ)
             MONMSG     MSGID(CPF2105)  EXEC(DO) /* Object not found */
               RMVMSG     PGMQ(*SAME (*)) CLEAR(*ALL)
             ENDDO

             CRTMSGF MSGF(&LIB/&OBJ) +
               TEXT('Fields with GENERATED clause')

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0001                    */
     /*                                           Fields with GENER */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0001) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                                          Fields with GENERATED claus-
e'                                                                    +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0002                    */
     /* Library    File       Field      Text                       */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0002) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Library    File       Field      Text'                               +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0006                    */
     /* F5=Refresh  F10=Continue  F12=Cancel  F17=Top  F18=Bottom   */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0006) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'F5=Refresh  F10=Continue  F12=Cancel  F17=Top  F18=Bottom'           +
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
     /*   Cmd-ADDMSGD für Message-ID :   CON0011                    */
     /* Job  . . . . . . . . . . . . :                              */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0011) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'Job  . . . . . . . . . . . . :'                                      +
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
     /*   Cmd-ADDMSGD für Message-ID :   CON0019                    */
     /*                                                 ! ! !   A T */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0019) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'                                                ! ! !   A T T E N T I-
 O N   ! ! !'                                                         +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0020                    */
     /* The Rapid Fire job contains files, which have fields with t */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0020) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The Rapid Fire job contains files, which have fields with the SQL GEN-
ERATED clause. If you use Rapid Fire for such files, then the'        +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0021                    */
     /* values of the fields with the SQL GENERATED clause will be  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0021) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'values of the fields with the SQL GENERATED clause will be regenerate-
d for each record in the files. The current IBM i release'            +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0022                    */
     /* provides a feature to avoid this. This feature is controlle */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0022) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'provides a feature to avoid this. This feature is controlled by the g-
lobal variable REPLICATION_OVERRIDE. You can find a brief'            +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0023                    */
     /* description of this global variable here: https://www.ibm.c */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0023) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'description of this global variable here: https://www.ibm.com/support-
/pages/node/6828143 . The usage of this global variable is'           +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0024                    */
     /* protected by the function QIBM_DB_GENCOL_OVERRIDE. Currentl */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0024) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'protected by the function QIBM_DB_GENCOL_OVERRIDE. Currently you are -
NOT allowed to use the global variable REPLICATION_OVERRIDE.'         +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0025                    */
     /* To be allowed to use the global variable REPLICATION_OVERRI */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0025) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'To be allowed to use the global variable REPLICATION_OVERRIDE a user -
with special authority *SECADM has to execute the following'          +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0026                    */
     /* command: CHGFCNUSG FCNID(QIBM_DB_GENCOL_OVERRIDE) USER(<use */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0026) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'command: CHGFCNUSG FCNID(QIBM_DB_GENCOL_OVERRIDE) USER(<user-profile>-
) USAGE(*ALLOWED). IF YOU CONTINUE, THEN THE VALUES WILL BE'          +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0027                    */
     /* REGENERATED. We advise you to cancel. You can continue if y */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0027) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'REGENERATED. We advise you to cancel. You can continue if you really -
understand what this means and if this causes NO problems'            +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0028                    */
     /* in your application. It is your decision. Be warned. The wr */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0028) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'in your application. It is your decision. Be warned. The wrong decisi-
on can destroy your entire application.'                              +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0030                    */
     /* The Rapid Fire job contains files, which have fields with t */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0030) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'The Rapid Fire job contains files, which have fields with the SQL GEN-
ERATED clause. If you use Rapid Fire for such files, then the'        +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0031                    */
     /* values of the fields with the SQL GENERATED clause will be  */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0031) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'values of the fields with the SQL GENERATED clause will be regenerate-
d for each record in the files. IF YOU CONTINUE, THEN THE '           +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0032                    */
     /* VALUES WILL BE REGENERATED. We advise you to cancel. You ca */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0032) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'VALUES WILL BE REGENERATED. We advise you to cancel. You can continue-
 if you really understand what this means and if this causes '        +
                ) +
                SEV(0) +
     /*   No replacement variables defined!                         */ +
                ALROPT(*NO) +
                CCSID(65535) +
                /*  -EOF-  */

     /*-------------------------------------------------------------*/
     /*   Cmd-ADDMSGD für Message-ID :   CON0033                    */
     /* NO problems in your application. It is your decision. Be wa */
     /*-------------------------------------------------------------*/

             ADDMSGD +
                MSGID(CON0033) +
                MSGF(&LIB/&OBJ) +
                MSG( +
'NO problems in your application. It is your decision. Be warned. The -
wrong decision can destroy your entire application.'                  +
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

             RCLRSC LVL(*)

             ENDPGM
