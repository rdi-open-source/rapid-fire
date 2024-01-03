       //=======================================================================
       //
       // Kurzdokumentation :
       // RPGUnit test for testing the 'List API Manager' service program.
       //
       //=======================================================================
       // History:
       //
       // Datum       Name          Änderung
       // ----------  ------------  --------------------------------------------
       //
       //
       //=======================================================================
       //  >>PRE-COMPILER<<
       //    >>CRTCMD<<  RUCRTTST    TSTPGM(&LI/&OB) +
       //                            SRCFILE(&SL/&SF) SRCMBR(&SM);
       //    >>COMPILE<<
       //      >>PARM<< DBGVIEW(*LIST);
       //      >>PARM<< BNDSRVPGM(FLDATTRS);
       //    >>END-COMPILE<<
       //    >>EXECUTE<<
       //  >>END-PRE-COMPILER<<
       //=======================================================================
       ctl-opt NoMain Option(*SrcStmt);

       dcl-f QSYSPRT printer(80) oflind(*in70) usropn;

      /include RPGUNIT1,TESTCASE   // RPGUnit

      /include QCPYSRC,FLDATTRS    // Field Attributes
      /include QCPYSRC,ERRC0100    // Format ERRC0100

       dcl-pr setUpSuite      extproc('setUpSuite') end-pr;
       dcl-pr tearDownSuite   extproc('tearDownSuite') end-pr;
       dcl-pr setUp           extproc('setUp') end-pr;
       dcl-pr tearDown        extproc('tearDown') end-pr;

       dcl-ds sds psds qualified;
          pgm char(10) pos(1);
          lib char(10) pos(81);
       end-ds;

       dcl-c FILE_NAME 'RUNIT.TEST';

       dcl-s g_testFile varchar(21);
       dcl-s g_rcdFmt varchar(10);
       dcl-s g_command varchar(1000);

       //  system -- Execute a Command
       //     Return value       Meaning
       //     ----------------   --------------------------------------
       //     0                  the command is successful
       //     -1                 for a NULL pointer
       //     1                  the command fails
       dcl-pr system int(5) extproc('system') ;
          i_clCommand pointer value options(*string);
       end-pr;

       dcl-s EXCP_MSGID char(7) import('_EXCP_MSGID');

       //=======================================================================
       // Opens the printer.
       //=======================================================================
       dcl-proc openPrinter;

       dcl-pi *N end-pi;

         runCmd('OVRPRTF FILE(QSYSPRT) TOFILE(*FILE) +
                 SPLFNAME(LSTAPIMGRR) OVRSCOPE(*JOB)');
         open QSYSPRT;

       end-proc;

       //=======================================================================
       // Prints a message.
       //=======================================================================
       dcl-proc print;

       dcl-pi *N;
          text                varchar(128) value options(*nopass);
       end-pi;

       dcl-ds lineOutput len(80) inz end-ds;

         if (not %open(QSYSPRT));
            openPrinter();
         endif;

         if (%parms() >= %parmnum(text));
            lineOutput = text;
         else;
            lineOutput = '';
         endif;
         write QSYSPRT lineOutput;

       end-proc;

       //=======================================================================
       // Closes the printer.
       //=======================================================================
       dcl-proc closePrinter;

       dcl-pi *N end-pi;

         if (%open(QSYSPRT));
            close QSYSPRT;
         endif;

       end-proc;

       //=======================================================================
       // Set up test suite.
       // Executed once before the first test case is executed.
       //=======================================================================
       dcl-proc setUpSuite export;

         dcl-pi *N end-pi;

         g_testFile = %trim(sds.lib) + '."' + FILE_NAME + '"';
         g_rcdFmt = 'RUNIT.TEST';

         g_command =
         '''CREATE TABLE ' + g_testFile + ' (+
             DATE_ DATE, +
             TIME_ TIME NOT NULL WITH DEFAULT CURRENT_TIME, +
             TIMESTAMP_ TIMESTAMP WITH DEFAULT NULL, +
             HEX_ CHAR(10) NOT NULL WITH DEFAULT x''''C49695819384'''', +
             BOOL_ CHAR(1) NOT NULL WITH DEFAULT ''''0'''' +
              CHECK (BOOL_ IN (''''0'''', ''''1'''')), +
             CHAR273_ CHAR(10) CCSID 273  NOT NULL WITH DEFAULT ''''Hello'''', +
             CHAR37_ VARCHAR(10) CCSID 37  NOT NULL WITH DEFAULT ''''Joe'''' +
             ) +
             RCDFMT "' + g_rcdFmt + '"''';

         g_command =
            'QSYS/RUNSQL SQL(' + g_command + ') COMMIT(*NONE) NAMING(*SYS)';

         system(g_command);


       end-proc;

       //=======================================================================
       // Tear down test suite.
       // Executed once after the last test case has been executed.
       //=======================================================================
       dcl-proc tearDownSuite export;

       dcl-pi *N end-pi;

         // system('RCLACTGRP ACTGRP(RAPIDFIRE)');

         g_command = '''DROP TABLE ' + g_testFile + '''';

         g_command =
            'QSYS/RUNSQL SQL(' + g_command + ') COMMIT(*NONE) NAMING(*SYS)';

         system(g_command);

         // Nur löschen, wenn Druckfunktion komplett ausgebaut wird.
         closePrinter();

       end-proc;

       //=======================================================================
       // Set up test case.
       // Called before each test case.
       //=======================================================================
       dcl-proc setUp export;

         dcl-pi *N end-pi;

         monitor;
            //
         on-error;
         endmon;

       end-proc;

       //=======================================================================
       // Tear down test case.
       // Called after each test case.
       //=======================================================================
       dcl-proc tearDown export;

         dcl-pi *N end-pi;

         monitor;
            //
         on-error;
         endmon;

       end-proc;

       //=======================================================================
       // Basic create/delete list test.
       //=======================================================================
       dcl-proc test_create_delete export;

         dcl-pi *N end-pi;

         dcl-s pList like(fldAttrs_handle_t);

         // Setup.

         pList = FieldAttributes_new(FILE_NAME: sds.lib
                                     : FLDATTRS_RCDFMT_FIRST: ERRC0100);

         assert(pList <> *null: 'List pointer must not be null');
         iEqual(7: FieldAttributes_length(pList): 'FieldAttributes_length()');

         // Run.

         FieldAttributes_delete(pList);

         // Check.

         assert(pList = *null: 'List pointer must be null');

       end-proc;

       //=======================================================================
       // FLDA0100 test. Simple sequential read.
       //=======================================================================
       dcl-proc test_flda0100 export;

         dcl-pi *N end-pi;

         dcl-s pList like(fldAttrs_handle_t);
         dcl-ds flda0100 likeds(flda0100_t);
         dcl-s rc int(5);

         pList = FieldAttributes_new(FILE_NAME: sds.lib
                                     : FLDATTRS_RCDFMT_FIRST: ERRC0100);
         assert(pList <> *null: 'List pointer must not be null');

         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(1: rc: 'rc');
         aEqual('DATE_': flda0100.name: 'flda0100.name');

         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(2: rc: 'rc');
         aEqual('TIME_': flda0100.name: 'flda0100.name');

         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(3: rc: 'rc');
         aEqual('TIMESTAMP_': flda0100.name: 'flda0100.name');

         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(4: rc: 'rc');
         aEqual('HEX_': flda0100.name: 'flda0100.name');

         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(5: rc: 'rc');
         aEqual('BOOL_': flda0100.name: 'flda0100.name');

         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(6: rc: 'rc');
         aEqual('CHAR273_': flda0100.name: 'flda0100.name');

         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(7: rc: 'rc');
         aEqual('CHAR37_': flda0100.name: 'flda0100.name');

         // End of field list
         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(-1: rc: 'rc');

       end-proc;

       //=======================================================================
       // FLDA0200 test. Simple sequential read.
       //=======================================================================
       dcl-proc test_flda0200 export;

         dcl-pi *N end-pi;

         dcl-s pList like(fldAttrs_handle_t);
         dcl-ds flda0200 likeds(flda0200_t);
         dcl-s rc int(5);

         pList = FieldAttributes_new(FILE_NAME: sds.lib
                                     : FLDATTRS_RCDFMT_FIRST: ERRC0100);
         assert(pList <> *null: 'List pointer must not be null');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0200: 'FLDA0200');
         iEqual(1: rc: 'rc');
         aEqual('DATE_': flda0200.name: 'flda0200.name');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0200: 'FLDA0200');
         iEqual(2: rc: 'rc');
         aEqual('TIME_': flda0200.name: 'flda0200.name');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0200: 'FLDA0200');
         iEqual(3: rc: 'rc');
         aEqual('TIMESTAMP_': flda0200.name: 'flda0200.name');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0200: 'FLDA0200');
         iEqual(4: rc: 'rc');
         aEqual('HEX_': flda0200.name: 'flda0200.name');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0200: 'FLDA0200');
         iEqual(5: rc: 'rc');
         aEqual('BOOL_': flda0200.name: 'flda0200.name');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0200: 'FLDA0200');
         iEqual(6: rc: 'rc');
         aEqual('CHAR273_': flda0200.name: 'flda0200.name');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0200: 'FLDA0200');
         iEqual(7: rc: 'rc');
         aEqual('CHAR37_': flda0200.name: 'flda0200.name');

         // End of field list
         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0200: 'FLDA0200');
         iEqual(-1: rc: 'rc');

       end-proc;

       //=======================================================================
       // FLDA0300 test. Simple sequential read.
       //=======================================================================
       dcl-proc test_flda0300 export;

         dcl-pi *N end-pi;

         dcl-s pList like(fldAttrs_handle_t);
         dcl-ds flda0300 likeds(flda0300_t);
         dcl-s rc int(5);

         pList = FieldAttributes_new(FILE_NAME: sds.lib
                                     : FLDATTRS_RCDFMT_FIRST: ERRC0100);
         assert(pList <> *null: 'List pointer must not be null');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0300: 'FLDA0300');
         iEqual(1: rc: 'rc');
         aEqual('DATE_': flda0300.name: 'flda0300.name');
         iEqual(1: flda0300.fieldIndex: 'flda0300.fieldNum');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0300: 'FLDA0300');
         iEqual(2: rc: 'rc');
         aEqual('TIME_': flda0300.name: 'flda0300.name');
         iEqual(2: flda0300.fieldIndex: 'flda0300.fieldNum');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0300: 'FLDA0300');
         iEqual(3: rc: 'rc');
         aEqual('TIMESTAMP_': flda0300.name: 'flda0300.name');
         iEqual(3: flda0300.fieldIndex: 'flda0300.fieldNum');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0300: 'FLDA0300');
         iEqual(4: rc: 'rc');
         aEqual('HEX_': flda0300.name: 'flda0300.name');
         iEqual(4: flda0300.fieldIndex: 'flda0300.fieldNum');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0300: 'FLDA0300');
         iEqual(5: rc: 'rc');
         aEqual('BOOL_': flda0300.name: 'flda0300.name');
         iEqual(5: flda0300.fieldIndex: 'flda0300.fieldNum');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0300: 'FLDA0300');
         iEqual(6: rc: 'rc');
         aEqual('CHAR273_': flda0300.name: 'flda0300.name');
         iEqual(6: flda0300.fieldIndex: 'flda0300.fieldNum');

         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0300: 'FLDA0300');
         iEqual(7: rc: 'rc');
         aEqual('CHAR37_': flda0300.name: 'flda0300.name');
         iEqual(7: flda0300.fieldIndex: 'flda0300.fieldNum');

         // End of field list
         rc = FieldAttributes_retrieve(
                 pList: FLDATTRS_FLD_NEXT: flda0300: 'FLDA0300');
         iEqual(-1: rc: 'rc');

       end-proc;

       //=======================================================================
       // Random field access.
       //=======================================================================
       dcl-proc test_random export;

         dcl-pi *N end-pi;

         dcl-s pList like(fldAttrs_handle_t);
         dcl-ds flda0300 likeds(flda0300_t);
         dcl-s rc int(5);

         pList = FieldAttributes_new(FILE_NAME: sds.lib
                                     : FLDATTRS_RCDFMT_FIRST: ERRC0100);
         assert(pList <> *null: 'List pointer must not be null');

         rc = FieldAttributes_retrieve(pList: 'TIME_': flda0300: 'FLDA0300');
         iEqual(2: rc: 'rc');
         aEqual('TIME_': flda0300.name: 'flda0300.name');

         rc = FieldAttributes_retrieve(pList: 'DATE_': flda0300: 'FLDA0300');
         iEqual(1: rc: 'rc');
         aEqual('DATE_': flda0300.name: 'flda0300.name');

         rc = FieldAttributes_retrieve(pList: 'BOOL_': flda0300: 'FLDA0300');
         iEqual(5: rc: 'rc');
         aEqual('BOOL_': flda0300.name: 'flda0300.name');

         rc = FieldAttributes_retrieve(pList: 'HEX_': flda0300: 'FLDA0300');
         iEqual(4: rc: 'rc');
         aEqual('HEX_': flda0300.name: 'flda0300.name');

       end-proc;

       //=======================================================================
       // Rewind field pointer.
       //=======================================================================
       dcl-proc test_rewind export;

         dcl-pi *N end-pi;

         dcl-s pList like(fldAttrs_handle_t);
         dcl-ds flda0100 likeds(flda0100_t);
         dcl-s rc int(5);

         pList = FieldAttributes_new(FILE_NAME: sds.lib
                                     : FLDATTRS_RCDFMT_FIRST: ERRC0100);
         assert(pList <> *null: 'List pointer must not be null');

         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(1: rc: 'rc');
         aEqual('DATE_': flda0100.name: 'flda0100.name');

         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(2: rc: 'rc');
         aEqual('TIME_': flda0100.name: 'flda0100.name');

         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(3: rc: 'rc');
         aEqual('TIMESTAMP_': flda0100.name: 'flda0100.name');

         FieldAttributes_rewind(pList);

         rc = FieldAttributes_retrieve(pList: FLDATTRS_FLD_NEXT: flda0100);
         iEqual(1: rc: 'rc');
         aEqual('DATE_': flda0100.name: 'flda0100.name');

       end-proc;

       //=======================================================================
       // Check default values, NULL allowed and CCSID.
       //=======================================================================
       dcl-proc test_defaultNullCcsid export;

         dcl-pi *N end-pi;

         dcl-s pList like(fldAttrs_handle_t);
         dcl-ds flda0300 likeds(flda0300_t);
         dcl-s rc int(5);
         dcl-s dftValue char(200) based(flda0300.pDftValue);

         pList = FieldAttributes_new(FILE_NAME: sds.lib
                                     : FLDATTRS_RCDFMT_FIRST: ERRC0100);
         assert(pList <> *null: 'List pointer must not be null');

         rc = FieldAttributes_retrieve(pList: 'DATE_': flda0300: 'FLDA0300');
         aEqual('DATE_': flda0300.name: 'flda0300.name');
         nEqual(*ON: flda0300.alwNull: 'flda0300.alwNull');
         iEqual(0: flda0300.lenDftValue: 'flda0300.lenDftValue');
         nEqual(*OFF: flda0300.isCurrTime: 'flda0300.isCurrentTime');
         nEqual(*OFF: flda0300.isHexLiteral: 'flda0300.isHexLiteral');
         nEqual(*OFF: flda0300.isNullDft: 'flda0300.isNullDft');
         nEqual(*OFF: flda0300.hasDtfKeyword: 'flda0300.hasDtfKeyword'); // Always *OFF. IBM bug?
         iEqual(273: flda0300.ccsid: 'flda0300.ccsid');

         rc = FieldAttributes_retrieve(pList: 'TIME_': flda0300: 'FLDA0300');
         aEqual('TIME_': flda0300.name: 'flda0300.name');
         nEqual(*OFF: flda0300.alwNull: 'flda0300.alwNull');
         iEqual(0: flda0300.lenDftValue: 'flda0300.lenDftValue');
         nEqual(*ON: flda0300.isCurrTime: 'flda0300.isCurrentTime');
         nEqual(*OFF: flda0300.isHexLiteral: 'flda0300.isHexLiteral');
         nEqual(*OFF: flda0300.isNullDft: 'flda0300.isNullDft');
         nEqual(*OFF: flda0300.hasDtfKeyword: 'flda0300.hasDtfKeyword'); // Always *OFF. IBM bug?
         iEqual(273: flda0300.ccsid: 'flda0300.ccsid');

         rc = FieldAttributes_retrieve(pList: 'TIMESTAMP_':flda0300:'FLDA0300');
         aEqual('TIMESTAMP_': flda0300.name: 'flda0300.name');
         nEqual(*ON: flda0300.alwNull: 'flda0300.alwNull');
         iEqual(5: flda0300.lenDftValue: 'flda0300.lenDftValue');
         aEqual('*NULL': %subst(dftValue: 1: flda0300.lenDftValue)
                : 'flda0300.pDftValue');
         nEqual(*OFF: flda0300.isCurrTime: 'flda0300.isCurrentTime');
         nEqual(*OFF: flda0300.isHexLiteral: 'flda0300.isHexLiteral');
         nEqual(*ON: flda0300.isNullDft: 'flda0300.isNullDft');
         nEqual(*OFF: flda0300.hasDtfKeyword: 'flda0300.hasDtfKeyword'); // Always *OFF. IBM bug?
         iEqual(273: flda0300.ccsid: 'flda0300.ccsid');

         rc = FieldAttributes_retrieve(pList: 'HEX_': flda0300: 'FLDA0300');
         aEqual('HEX_': flda0300.name: 'flda0300.name');
         nEqual(*OFF: flda0300.alwNull: 'flda0300.alwNull');
         iEqual(15: flda0300.lenDftValue: 'flda0300.lenDftValue');
         aEqual('X''C49695819384''': %subst(dftValue: 1: flda0300.lenDftValue)
                : 'flda0300.pDftValue');
         nEqual(*OFF: flda0300.isCurrTime: 'flda0300.isCurrentTime');
         nEqual(*ON: flda0300.isHexLiteral: 'flda0300.isHexLiteral');
         nEqual(*OFF: flda0300.isNullDft: 'flda0300.isNullDft');
         nEqual(*OFF: flda0300.hasDtfKeyword: 'flda0300.hasDtfKeyword'); // Always *OFF. IBM bug?
         iEqual(273: flda0300.ccsid: 'flda0300.ccsid');

         rc = FieldAttributes_retrieve(pList: 'BOOL_': flda0300: 'FLDA0300');
         aEqual('BOOL_': flda0300.name: 'flda0300.name');
         nEqual(*OFF: flda0300.alwNull: 'flda0300.alwNull');
         iEqual(3: flda0300.lenDftValue: 'flda0300.lenDftValue');
         aEqual('''0''': %subst(dftValue: 1: flda0300.lenDftValue)
                : 'flda0300.pDftValue');
         nEqual(*OFF: flda0300.isCurrTime: 'flda0300.isCurrentTime');
         nEqual(*OFF: flda0300.isHexLiteral: 'flda0300.isHexLiteral');
         nEqual(*OFF: flda0300.isNullDft: 'flda0300.isNullDft');
         nEqual(*OFF: flda0300.hasDtfKeyword: 'flda0300.hasDtfKeyword'); // Always *OFF. IBM bug?
         iEqual(273: flda0300.ccsid: 'flda0300.ccsid');

         rc = FieldAttributes_retrieve(pList: 'CHAR273_': flda0300: 'FLDA0300');
         aEqual('CHAR273_': flda0300.name: 'flda0300.name');
         nEqual(*OFF: flda0300.alwNull: 'flda0300.alwNull');
         iEqual(7: flda0300.lenDftValue: 'flda0300.lenDftValue');
         aEqual('''Hello''': %subst(dftValue: 1: flda0300.lenDftValue)
                : 'flda0300.pDftValue');
         nEqual(*OFF: flda0300.isCurrTime: 'flda0300.isCurrentTime');
         nEqual(*OFF: flda0300.isHexLiteral: 'flda0300.isHexLiteral');
         nEqual(*OFF: flda0300.isNullDft: 'flda0300.isNullDft');
         nEqual(*OFF: flda0300.hasDtfKeyword: 'flda0300.hasDtfKeyword'); // Always *OFF. IBM bug?
         iEqual(273: flda0300.ccsid: 'flda0300.ccsid');

         rc = FieldAttributes_retrieve(pList: 'CHAR37_': flda0300: 'FLDA0300');
         aEqual('CHAR37_': flda0300.name: 'flda0300.name');
         nEqual(*OFF: flda0300.alwNull: 'flda0300.alwNull');
         iEqual(5: flda0300.lenDftValue: 'flda0300.lenDftValue');
         aEqual('''Joe''': %subst(dftValue: 1: flda0300.lenDftValue)
                : 'flda0300.pDftValue');
         nEqual(*OFF: flda0300.isCurrTime: 'flda0300.isCurrentTime');
         nEqual(*OFF: flda0300.isHexLiteral: 'flda0300.isHexLiteral');
         nEqual(*OFF: flda0300.isNullDft: 'flda0300.isNullDft');
         nEqual(*OFF: flda0300.hasDtfKeyword: 'flda0300.hasDtfKeyword'); // Always *OFF. IBM bug?
         iEqual(37: flda0300.ccsid: 'flda0300.ccsid');

       end-proc;

