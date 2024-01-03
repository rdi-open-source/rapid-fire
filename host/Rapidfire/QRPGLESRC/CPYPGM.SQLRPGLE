      //*------------------------------------------------------------------------------------------*
      //*                                                                                          *
      //* Copyright (c) 2016 Task Force IT-Consulting GmbH, Waltrop (Germany)                      *
      //* This software and the accompanying materials are made available                          *
      //* under the terms of the GNU General Public License version 2.0 (GPLv2)                    *
      //* which accompanies this distribution and is available at                                  *
      //* http://www.gnu.org/licenses/gpl-2.0.html                                                 *
      //*                                                                                          *
      //*------------------------------------------------------------------------------------------*
      //*                                                                                          *
      //* Copy Program                                                                             *
      //*                                                                                          *
      //*-----------------------------------------------------------------------------------------*
      //*  >>PRE-COMPILER<<                                                                       *
      //*    >>CRTCMD<< CRTSQLRPGI   OBJ(&LI/&OB) +                                               *
      //*                              SRCFILE(&SL/&SF) SRCMBR(&SM) +                             *
      //*                              OBJTYPE(*PGM);                                             *
      //*    >>IMPORTANT<<                                                                        *
      //*      >>PARM<<  OUTPUT(*PRINT);                                                          *
      //*      >>PARM<<  GENLVL(0);                                                               *
      //*      >>PARM<<  DBGVIEW(*SOURCE);                                                        *
      //*      >>PARM<<  OPTION(*EVENTF);                                                         *
      //*      >>PARM<<  TGTRLS(&TR);                                                             *
      //*    >>END-IMPORTANT<<                                                                    *
      //*    >>EXECUTE<<                                                                          *
      //*  >>END-PRE-COMPILER<<                                                                   *
      //*------------+--------+--------------------------------------------------------------------*
      //*   Datum    I Progr. I Beschreibung                                                       *
      //*------------+--------+--------------------------------------------------------------------*
      //* 03.02.2016 I   TR   I STRPREPRC Header hinzugefügt.                                      *
      //*------------+--------+--------------------------------------------------------------------*
      //* 11.03.2016 I   TR   I Programm erweitert, so dass bei einer Änderung im Satzformat die   *
      //*            I        I Felder einzelnd, Feld für Feld, kopiert werden. Damit ist dafür    *
      //*            I        I nicht mehr zwingend ein Kopierprogramm erforderlich.               *
      //*------------+--------+--------------------------------------------------------------------*
      //* 11.03.2016 I   TR   I Problem korrigiert, so dass jetzt auch Dateien mit einem Punkt     *
      //*            I        I im Dateinamen verarbeitet werden.                                  *
      //*------------------------------------------------------------------------------------------*

       // Control

     h DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,') AlwNull(*USRCTL)
     h DftActGrp(*NO) ActGrp('RAPIDFIRE') BndDir('RAPIDFIRE')
     h Copyright('(C) Copyright Task Force IT-Consulting GmbH')
     h ExtBinInt(*YES)

       //*-----------------------------------------------------------------------------------------*

       // Files

     fSOURCE    IF   E             Disk    ExtDesc('JOBS')                      %%SOURCE_EXTDESC%%
     f                                     Rename(JOBS : SOURCERF)              %%SOURCE_RENAME%%
     f                                     Prefix(S_)                           %%SOURCE_PREFIX%%
     f                                     ExtFile(p_Qual_Source)
     f                                     ExtMbr(p_Member)
     f                                     InfDs(g_InfDs_Source)
     f                                     UsrOpn

     fTARGET    UF A E             Disk    ExtDesc('JOBS')                      %%TARGET_EXTDESC%%
     f                                     Rename(JOBS : TARGETRF)              %%TARGET_RENAME%%
     f                                     Prefix(T_)                           %%TARGET_PREFIX%%
     f                                     ExtFile(p_Qual_Target)
     f                                     ExtMbr(p_Member)
     f                                     InfDs(g_InfDs_Target)
     f                                     UsrOpn

     fRECORDS   UF A E           K Disk    ExtFile(p_Qual_Records)
     f                                     Rename(RECORDS : RECORDSRF)
     f                                     UsrOpn

     fRCDTGT    UF   E           K Disk    ExtDesc('RECORDSI01')
     f                                     ExtFile(p_Qual_Rcd_Tgt)
     f                                     Rename(RECORDS : RCDTGTRF)
     f                                     Prefix(RCDTGT_)
     f                                     UsrOpn

     fCHANGES   IF A E           K Disk    ExtFile(p_Qual_Changes)
     f                                     Rename(CHANGES : CHANGESRF)
     f                                     UsrOpn

     fCHANGESLOGIF A E           K Disk    ExtFile(p_Qual_Chg_Log)
     f                                     Rename(CHANGESLOG : CHGLOGRF)
     f                                     UsrOpn

     fDUPRCDKEYSUF A E           K Disk    ExtFile(p_Qual_Dup_Rcd)
     f                                     Rename(DUPRCDKEYS : DRKRF)
     f                                     UsrOpn

      //*------------------------------------------------------------------------------------------*

      // Includes

      /Include QCPYSRC,RAPIDFIRE
      /Include QCPYSRC,SQLSTS
      /Include QCPYSRC,EXCCMD
      /Include QCPYSRC,DPLKEYHDLR

       //*-----------------------------------------------------------------------------------------*

       // Prototype for calling the optional conversion program
     d Cnv_Pgm...
     d                 PR
     d                                     extpgm(g_Cnv_Pgm)
     d i_Mode                        10A   Const
     d o_pAfter_Img                    *   Const
     d o_after_NInd                    N         Dim(8000)
     d i_after_Count                 10I 0 Const
     d i_pBefore_Img                   *   Const
     d i_before_NInd                   N   Const Dim(8000)
     d i_before_Count                10I 0 Const

       //*-----------------------------------------------------------------------------------------*

       // Parameter for this program

     d  p_Mode...
     d                 S             10A
     d  p_Job...
     d                 S             10A
     d  p_Position...
     d                 S              6S 0
     d  p_Area...
     d                 S             10A
     d  p_Qual_Source...
     d                 S             21A
     d  p_Qual_Target...
     d                 S             21A
     d  p_Member...
     d                 S             10A
     d  p_Qual_Records...
     d                 S             21A
     d  p_Qual_Rcd_Tgt...
     d                 S             21A
     d  p_Qual_Changes...
     d                 S             21A
     d  p_Qual_Chg_Log...
     d                 S             21A
     d  p_Qual_Dup_Rcd...
     d                 S             21A

       // Source record format (before image data)

     d g_Source_fmtLvlId...
     d                 C                   ''                                   %%SOURCE_FMTLVLID%%

     d g_Source_rcdFmt...
     d                 C                   ''                                   %%SOURCE_RCDFMT%%

      /define COPY_MODE                                                         %%COPY_MODE%%
      /if defined(FIELD_COPY)
     d g_Source...
     d               E DS                  ExtName('JOBS')                      %%SOURCE_EXTNAME%%
     d                                     Prefix(S_)                           %%SOURCE_PREFIX%%

     d g_Source_nullMap...
     d                 S               N   Dim(8000)

      /endif
       // Target record format (after image data)

     d g_Target_fmtLvlId...
     d                 C                   ''                                   %%TARGET_FMTLVLID%%

     d g_Target_rcdFmt...
     d                 C                   ''                                   %%TARGET_RCDFMT%%

     d g_Target...
     d               E DS                  ExtName('JOBS')                      %%TARGET_EXTNAME%%
     d                                     Prefix(T_)                           %%TARGET_PREFIX%%

     d g_Target_nullMap...
     d                 S               N   Dim(8000)

       // File information data structure for file Source

     d g_InfDs_Source...
     d                 DS                  Qualified
     d  qFile...
     d                                     LikeDs(qObj_t)
     d                                     Overlay(g_InfDs_Source: 83)
     d  Record_Number...
     d                       397    400U 0

       // File information data structure for file Target

     d g_InfDs_Target...
     d                 DS                  LikeDs(g_InfDs_Source)

       // Types

     d TYPE_WRITE...
     d                 C                   1
     d TYPE_UPDATE...
     d                 C                   2
     d TYPE_DELETE...
     d                 C                   3

       // Type Templates

     d qObj_t          DS                  Qualified Template
     d  name                         10a
     d  lib                          10a

     d errCode_t       DS                  Qualified Template
     d  bytPrv                       10i 0 Inz(%Size(errCode_t))
     d  bytAvl                       10i 0
     d  excId                         7a
     d  reserved                      1a
     d  excData                     256a

       // Program status data structure

     d PSDS...
     d                SDS           429    Qualified
     d  MsgId...
     d                        40     46A
     d  MsgDta...
     d                        91    170A
     d  currUser...
     d                       358    367A

       // Global field declarations

     d g_Loop...
     d                 S               N   Inz(*On)
     d g_Done...
     d                 S               N
     d g_SQL_Statement...
     d                 S           1024A
     d g_Type...
     d                 S              1S 0
     d g_Record_Number...
     d                 S             10P 0
     d g_Sequence_Number...
     d                 S             10P 0
     d g_Records_Copied_Internal...
     d                 S             10P 0
     d g_Records_Copied...
     d                 S             10P 0
     d g_Cnv_Pgm...
     d                 S             21A                                        %%CNV_PGM%%
     d g_Mode...
     d                 S             10A
     d g_Status...
     d                 S             10A
     d g_Start_Time...
     d                 S               Z
     d g_Current_Time...
     d                 S               Z
     d g_Time_Consumed...
     d                 S             10I 0
     d g_CLRPFM...
     d                 S              1A
     d g_Error...
     d                 S              1A
     d g_Activity...
     d                 S              1A
     d g_Time...
     d                 S               T
     d g_Job_End...
     d                 S              1A
     d g_CURRENT_TIMESTAMP...
     d                 S               Z
     d g_Record_With_Duplicate_Key...
     d                 S               N
     d g_Changes_Not_Yet_Applied...
     d                 S             10S 0
     d g_Records_With_Duplicate_Key...
     d                 S             10S 0
     d g_RCD1...
     d                 S             10U 0
     d g_RCD2...
     d                 S             10U 0
     d g_OK...
     d                 S              1A
     d g_Applied...
     d                 S              1A

       //*-----------------------------------------------------------------------------------------*

     iSOURCERF
       // %%RENAME_AREA_SOURCE%%

     iTARGETRF
       // %%RENAME_AREA_TARGET%%

       //*-----------------------------------------------------------------------------------------*

       // Parameter for this program

     c     *Entry        PList
     c                   Parm                    p_Mode
     c                   Parm                    p_Job
     c                   Parm                    p_Position
     c                   Parm                    p_Area
     c                   Parm                    p_Qual_Source
     c                   Parm                    p_Qual_Target
     c                   Parm                    p_Member
     c                   Parm                    p_Qual_Records
     c                   Parm                    p_Qual_Rcd_Tgt
     c                   Parm                    p_Qual_Changes
     c                   Parm                    p_Qual_Chg_Log
     c                   Parm                    p_Qual_Dup_Rcd

       //*-----------------------------------------------------------------------------------------*

        // Set SQL options

        Exec SQL
          Set Option
          DatFmt=*ISO,
          TimFmt=*ISO,
          Commit=*NONE,
          CloSQLCsr=*ENDMOD;

       //*-----------------------------------------------------------------------------------------*

      /Free

        // Check activity schedule
        ExSr check_Activity_Schedule;
        If g_Job_End <> 'Y';

          // Initialize
          g_CLRPFM = 'N';

          // Open source file
          g_Error = 'N';
          DoW g_Loop;
            Monitor;
              Open Source;
            On-Error 01217;
              If %Scan('CPF4128' : PSDS.MsgDta) <> 0;
                EXCCMD_execute_Command(
                  'DLYJOB DLY(10)'
                );
                Iter;
              Else;
                // Copy program could not open file &1.
                g_Error = 'Y';
                RAPIDFIRE_log_Error(
                  p_Job :
                  'MSG0054' :
                  p_Qual_Source :
                  *Blanks :
                  *Blanks
                );
              EndIf;
            EndMon;
            Leave;
          EndDo;

          If g_Error = 'N';

            // Open files
            Open Target;
            Open Records;
            Open RcdTgt;
            Open Changes;
            Open ChangesLog;
            Open DupRcdKeys;
            // %%CONVERSION_OPEN%%

            Select;

            // Copy records
            When p_Mode = '*COPY';

              // Copy records
              checkFormatLevelId(
                g_InfDs_Source.qFile: g_Source_rcdFmt: g_Source_fmtLvlId);
              checkFormatLevelId(
                g_InfDs_Target.qFile: g_Target_rcdFmt: g_Target_fmtLvlId);
              ExSr copy_Records;

            // Apply changes
            When p_Mode = '*APPLY';

              DoU Not g_Record_With_Duplicate_Key;

                // Initialize record with duplicate key
                g_Record_With_Duplicate_Key = *Off;

                // Apply Changes
                ExSr apply_Changes;

                // record with duplicate key detected
                If g_Record_With_Duplicate_Key;

                  // Delay job
                  EXCCMD_execute_Command(
                    'DLYJOB DLY(1)'
                  );

                  // Leave, if job status is not run
                  Exec SQL
                    Select Status Into :g_Status
                    From Jobs
                    Where Job = :p_Job;
                  setIgnoreWarnings();
                  If Not (SqlCheckState(sqlState) And g_Status = '*RUN');
                    Leave;
                  EndIf;

                EndIf;

              EndDo;

            EndSl;

            // Close files
            Close Source;
            Close Target;
            Close Records;
            Close RcdTgt;
            Close Changes;
            Close ChangesLog;
            Close DupRcdKeys;
            // %%CONVERSION_CLOSE%%

          EndIf;

          // Member has cleared by command CLRPFM
          If g_CLRPFM = 'Y';

            // Clear member in target file
            EXCCMD_execute_Command(
              'CLRPFM ' +
              'FILE(' + %Trim(p_Qual_Target) + ') ' +
              'MBR(' + %Trim(p_Member) + ')'
            );

            // Delete Workfile Records
            g_SQL_Statement =
              'Delete ' +
              'From ' + prepareSQLTableName(p_Qual_Records) + ' ' +
              'Where Member = ''' + %Trim(p_Member) + '''';
            Exec SQL
              Prepare Delete_Records From :g_SQL_Statement;
            Exec SQL
              Execute Delete_Records;
            setIgnoreWarnings();
            SqlCheckState(sqlState);

            // Delete Workfile Changes
            g_SQL_Statement =
              'Delete ' +
              'From ' + prepareSQLTableName(p_Qual_Changes) + ' ' +
              'Where Member = ''' + %Trim(p_Member) + '''';
            Exec SQL
              Prepare Delete_Changes From :g_SQL_Statement;
            Exec SQL
              Execute Delete_Changes;
            setIgnoreWarnings();
            SqlCheckState(sqlState);

            // Delete Workfile Changes Log
            g_SQL_Statement =
              'Delete ' +
              'From ' + prepareSQLTableName(p_Qual_Chg_Log) + ' ' +
              'Where Member = ''' + %Trim(p_Member) + '''';
            Exec SQL
              Prepare Delete_ChangesLog From :g_SQL_Statement;
            Exec SQL
              Execute Delete_ChangesLog;
            setIgnoreWarnings();
            SqlCheckState(sqlState);

            // Delete Duplicate Record Keys
            g_SQL_Statement =
              'Delete ' +
              'From ' + prepareSQLTableName(p_Qual_Dup_Rcd) + ' ' +
              'Where Member = ''' + %Trim(p_Member) + '''';
            Exec SQL
              Prepare Delete_DuplicateRecordKeys From :g_SQL_Statement;
            Exec SQL
              Execute Delete_DuplicateRecordKeys;
            setIgnoreWarnings();
            SqlCheckState(sqlState);

          EndIf;

        EndIf;

        // End of program
        *INLR = *On;

       //*-----------------------------------------------------------------------------------------*
       //* Copy Records                                                                            *
       //*-----------------------------------------------------------------------------------------*

        BegSr copy_Records;

          // Initialize record with duplicate key
          g_Record_With_Duplicate_Key = *Off;

          // Set start time
          g_Start_Time = %TimeStamp();

          // Initialize records copied
          g_Records_Copied_Internal = *Zero;
          g_Records_Copied = *Zero;

          // Read source
          Clear TARGETRF;
          Read SOURCERF;

          DoW Not %EOF;

            // Build target
            g_Type = TYPE_WRITE;
            ExSr build_Target;

            // Increase records
            g_Records_Copied_Internal+=1;
            g_Records_Copied+=1;

            If g_Records_Copied_Internal = 10000;

              // Calculate time consumed
              g_Current_Time = %TimeStamp();
              g_Time_Consumed = %Diff(g_Current_Time : g_Start_Time : *Seconds);

              // Update records copied
              Exec SQL
                Update Areas
                Set Records_Copied =
                      Records_Copied + :g_Records_Copied_Internal,
                    Time_Consumed = :g_Time_Consumed
                Where Job = :p_Job And
                      Position = :p_Position And
                      Area = :p_Area;
              setIgnoreWarnings();
              SqlCheckState(sqlState);

              // Reset records
              g_Records_Copied_Internal = *Zero;

              // Check activity schedule
              ExSr check_Activity_Schedule;
              If g_Job_End = 'Y';
                Leave;
              EndIf;

            EndIf;

            // Read source
            Clear TARGETRF;
            Read SOURCERF;

          EndDo;

          // Update records copied
          Exec SQL
            Update Areas
            Set Records_Copied =
                  Records_Copied + :g_Records_Copied_Internal
            Where Job = :p_Job And
                  Position = :p_Position And
                  Area = :p_Area;
          setIgnoreWarnings();
          SqlCheckState(sqlState);

        EndSr;

       //*-----------------------------------------------------------------------------------------*
       //* Apply Changes                                                                           *
       //*-----------------------------------------------------------------------------------------*

        BegSr apply_Changes;

          // Get highest sequence number
          SetGt (p_Member) CHANGESRF;
          ReadPE (p_Member) CHANGESRF;

          If Not %EOF;

            // Save sequence number
            g_Sequence_Number = X8SEQ;

            // Declare cursor for file Changes
            g_SQL_Statement =
              'Select Distinct Record_Number ' +
              'From ' + prepareSQLTableName(p_Qual_Changes) + ' ' +
              'Where Member = ''' + %Trim(p_Member) + ''' And ' +
              'Sequence_Number <= ' + %Char(g_Sequence_Number) + ' ' +
              'Order By Record_Number';
            Exec SQL
              Prepare Select_Changes From :g_SQL_Statement;
            Exec SQL
              Declare Cursor_Changes Cursor For Select_Changes;

            // Open cursor
            Exec SQL
              Open Cursor_Changes;
            setIgnoreWarnings();
            If SqlCheckState(sqlState);

              DoW g_Loop;

                // Read 'Changes'
                Exec SQL
                  Fetch Next From Cursor_Changes
                  Into :g_Record_Number;

                // End of file
                setIgnoreWarnings();
                If Not SqlCheckState(sqlState);
                  Leave;
                EndIf;

                // Member has cleared by command CLRPFM
                If g_Record_Number = -1;
                  g_CLRPFM = 'Y';
                  Leave;
                EndIf;

                // Set "Change applied"
                g_Applied = 'Y';

                // Does record in source file exist?
                SetLL g_Record_Number SOURCERF;
                If Not %Equal;

                  // Apply delete
                  Chain (p_Member : g_Record_Number) RECORDSRF;
                  If %Found;
                    Delete RECORDSRF;
                    Chain X9TRN TARGETRF;
                    If %Found;
                      Delete TARGETRF;
                    EndIf;
                  EndIf;

                Else;

                  // Does record in target file exist?
                  SetLL (p_Member : g_Record_Number) RECORDSRF;
                  If Not %Equal;

                    // Apply write
                    Clear TARGETRF;
                    Chain g_Record_Number SOURCERF;
                    If %Found;
                      g_Type = TYPE_WRITE;
                      ExSr build_Target;
                    EndIf;

                  Else;

                    // Apply update
                    Chain (p_Member : g_Record_Number) RECORDSRF;
                    If %Found;
                      Chain X9TRN TARGETRF;
                      If %Found;
                        Clear TARGETRF;
                        Chain X9SRN SOURCERF;
                        If %Found;
                          g_Type = TYPE_UPDATE;
                          ExSr build_Target;
                        EndIf;

                      Else;

                        // Should not occur, but if yes then apply write
                        Delete RECORDSRF;
                        Clear TARGETRF;
                        Chain g_Record_Number SOURCERF;
                        If %Found;
                          g_Type = TYPE_WRITE;
                          ExSr build_Target;
                        EndIf;

                      EndIf;

                    EndIf;

                  EndIf;

                EndIf;

                // Log changes
                SetLL (p_Member : g_Record_Number) CHGLOGRF;
                If Not %Equal;
                  XEMBR = p_Member;
                  XERCD = g_Record_Number;
                  Write CHGLOGRF;
                EndIf;

                // Change has applied
                If (g_Applied = 'Y');
                  Chain (p_Member : g_Record_Number) DRKRF;
                  If %Found;
                    Delete DRKRF;
                  EndIf;
                EndIf;

              EndDo;

              // Close cursor
              Exec SQL
                Close Cursor_Changes;
              setIgnoreWarnings();
              SqlCheckState(sqlState);

              // Delete all processed records in file Changes
              g_SQL_Statement =
                'Delete ' +
                'From ' + prepareSQLTableName(p_Qual_Changes) + ' ' +
                'Where Member = ''' + %Trim(p_Member) + ''' And ' +
                'Sequence_Number <= ' + %Char(g_Sequence_Number);
              Exec SQL
                Prepare Delete_Changes From :g_SQL_Statement;
              Exec SQL
                Execute Delete_Changes;
              setIgnoreWarnings();
              SqlCheckState(sqlState);

              // Get not yet applied changes
              g_SQL_Statement =
                'Values (' +
                  'Select Count(*) From (' +
                    'Select Distinct Record_Number ' +
                    'From ' + prepareSQLTableName(p_Qual_Changes) + ' ' +
                    'Where Member = ''' + %Trim(p_Member) + '''' +
                  ') As X' +
                ') Into ?';
              Exec SQL
                Prepare Changes_Not_Yet_Applied
                  From :g_SQL_Statement;
              setIgnoreWarnings();
              If Not SqlCheckState(sqlState);
                g_Changes_Not_Yet_Applied = *Zero;
              Else;
                exec sql
                  Execute Changes_Not_Yet_Applied
                    Using :g_Changes_Not_Yet_Applied;
                setIgnoreWarnings();
                If Not SqlCheckState(sqlState);
                  g_Changes_Not_Yet_Applied = *Zero;
                EndIf;
              EndIf;

              // Get records with duplicate key
              g_SQL_Statement =
                'Values (' +
                  'Select Count(*) ' +
                  'From ' + prepareSQLTableName(p_Qual_Dup_Rcd) + ' ' +
                  'Where Member = ''' + %Trim(p_Member) + '''' +
                ') Into ?';
              Exec SQL
                Prepare Records_With_Duplicate_Key
                  From :g_SQL_Statement;
              setIgnoreWarnings();
              If Not SqlCheckState(sqlState);
                g_Records_With_Duplicate_Key = *Zero;
              Else;
                exec sql
                  Execute Records_With_Duplicate_Key
                    Using :g_Records_With_Duplicate_Key;
                setIgnoreWarnings();
                If Not SqlCheckState(sqlState);
                  g_Records_With_Duplicate_Key = *Zero;
                EndIf;
              EndIf;

              // Update changes applied
              Exec SQL
                Update Areas
                Set Changes_Applied =
                      Changes_To_Apply -
                      (:g_Changes_Not_Yet_Applied -
                       :g_Records_With_Duplicate_Key)
                Where Job = :p_Job And
                      Position = :p_Position And
                      Area = :p_Area;
              setIgnoreWarnings();
              SqlCheckState(sqlState);

              // Update records with duplicate key
              Exec SQL
                Update Members
                Set Records_With_Duplicate_Key =
                      :g_Records_With_Duplicate_Key
                Where Job = :p_Job And
                      Position = :p_Position And
                      Area = :p_Area And
                      Member = :p_Member;
              setIgnoreWarnings();
              SqlCheckState(sqlState);

            EndIf;

          EndIf;

        EndSr;

       //*-----------------------------------------------------------------------------------------*
       //* Build Target                                                                            *
       //*-----------------------------------------------------------------------------------------*

        BegSr build_Target;

          g_CURRENT_TIMESTAMP = %timestamp();

          // %%CONVERSION_AREA%%

          Monitor;

            // Write target
            Select;
            When g_Type = TYPE_WRITE;
              Write TARGETRF;
            When g_Type = TYPE_UPDATE;
              Update TARGETRF;
            EndSl;

            // Done
            g_Done = *On;

          On-Error 01021;

            // Set record with duplicate key
            g_Record_With_Duplicate_Key = *On;

            If p_Mode = '*APPLY';

              // Reset "Change applied"
              g_Applied = 'N';

            EndIf;

            // Unlock files. Even if a duplicate key occors, then the records still remain locked
            Unlock TARGET;
            Unlock RECORDS;

            // Get the records affected by the duplicate key
            DPLKEYHDLR(g_RCD1 : g_RCD2);

            // Set OK
            g_OK = 'N';

            // Delete first record from shadow library
            If g_RCD1 <> *Zero;
              Chain (p_Member : g_RCD1) RCDTGTRF;
              If %Found;
                Delete RCDTGTRF;
                Chain g_RCD1 TARGETRF;
                If %Found;
                  Delete TARGETRF;

                  // Records with duplicate key have to be reapplied
                  X8MBR = p_Member;
                  X8RCD = RCDTGT_X9SRN;
                  Write CHANGESRF;

                  If p_Mode = '*APPLY';

                    // Log duplicate record key
                    Chain (p_Member : RCDTGT_X9SRN) DRKRF;
                    If Not %Found;
                      Clear *All DRKRF;
                      XHMBR = p_Member;
                      XHRN = RCDTGT_X9SRN;
                      XHNOAA = 1;
                      Write DRKRF;
                    Else;
                      XHNOAA += 1;
                      Update DRKRF;
                    EndIf;

                  EndIf;

                  // Everything is fine
                  If RCDTGT_X9SRN = g_InfDs_Source.Record_Number;
                    g_OK = 'Y';
                  EndIf;

                EndIf;
              EndIf;
            EndIf;

            // Delete second record from shadow library
            If g_RCD2 <> *Zero;
              Chain (p_Member : g_RCD2) RCDTGTRF;
              If %Found;
                Delete RCDTGTRF;
                Chain g_RCD2 TARGETRF;
                If %Found;
                  Delete TARGETRF;

                  // Records with duplicate key have to be reapplied
                  X8MBR = p_Member;
                  X8RCD = RCDTGT_X9SRN;
                  Write CHANGESRF;

                  If p_Mode = '*APPLY';

                    // Log duplicate record key
                    Chain (p_Member : RCDTGT_X9SRN) DRKRF;
                    If Not %Found;
                      Clear *All DRKRF;
                      XHMBR = p_Member;
                      XHRN = RCDTGT_X9SRN;
                      XHNOAA = 1;
                      Write DRKRF;
                    Else;
                      XHNOAA += 1;
                      Update DRKRF;
                    EndIf;

                  EndIf;

                  // Everything is fine
                  If RCDTGT_X9SRN = g_InfDs_Source.Record_Number;
                    g_OK = 'Y';
                  EndIf;

                EndIf;
              EndIf;
            EndIf;

            If g_OK = 'N';

              // Records with duplicate key have to be reapplied
              X8MBR = p_Member;
              X8RCD = g_InfDs_Source.Record_Number;
              Write CHANGESRF;

              If p_Mode = '*APPLY';

                // Log duplicate record key
                Chain (p_Member : g_InfDs_Source.Record_Number) DRKRF;
                If Not %Found;
                  Clear *All DRKRF;
                  XHMBR = p_Member;
                  XHRN = g_InfDs_Source.Record_Number;
                  XHNOAA = 1;
                  Write DRKRF;
                Else;
                  XHNOAA += 1;
                  Update DRKRF;
                EndIf;

              EndIf;

            EndIf;

            // Not done
            g_Done = *Off;

          EndMon;

          // Write records
          If g_Type = TYPE_WRITE And
                g_Done;
            X9MBR = p_Member;
            X9SRN = g_InfDs_Source.Record_Number;
            X9TRN = g_InfDs_Target.Record_Number;
            Write RECORDSRF;
          EndIf;

        EndSr;

       //*-----------------------------------------------------------------------------------------*
       //* Check Activity Schedule                                                                 *
       //*-----------------------------------------------------------------------------------------*

        BegSr check_Activity_Schedule;

          DoW g_Loop;

            g_Job_End = 'N';
            Exec SQL
              Select Status Into :g_Status
              From Jobs
              Where Job = :p_Job;
            setIgnoreWarnings();
            If Not (SqlCheckState(sqlState) And g_Status = '*RUN');
              g_Job_End = 'Y';
              LeaveSr;
            EndIf;

            g_Time = %Time();
            Exec SQL
              Select Activity Into :g_Activity
              From Activity_Schedule
              Where Job = :p_Job And
                    :g_Time Between Start_Time and End_Time;
            setIgnoreWarnings();
            If SqlCheckState(sqlState) And
                  g_Activity = 'N';
              EXCCMD_execute_Command(
                'DLYJOB DLY(10)'
              );
              Iter;
            Else;
              Leave;
            EndIf;

          EndDo;

        EndSr;

      /End-Free

      //*==========================================================================================*
      //* Check format level identifier in case one or both files have level check disabled.       *
      //*==========================================================================================*

     p checkFormatLevelId...
     p                 B
     d                 PI
     d  i_qFile                            Const LikeDs(qObj_t)
     d  i_rcdFmt                     10a   Const
     d  i_expectedId                 13a   Const

      //  Retrieve Database File Description (QDBRTVFD) API
     d QDBRTVFD        PR                  ExtPgm('QDBRTVFD')
     d  o_rcvVar                  32767a   Options(*VarSize)                    <-- Receiver variabl
     d  i_lenRcvVar                  10i 0 Const                                --> Length of receiv
     d  o_retFileName                20a                                        <-- Qualified return
     d  i_format                      8a   Const                                --> Format name
     d  i_qFile                      20a   Const                                --> Qualified file n
     d  i_rcdFormat                  10a   Const                                --> Record format na
     d  i_override                    1a   Const                                --> Override process
     d  i_system                     10a   Const                                --> System
     d  i_formatType                 10a   Const                                --> Format type
     d  io_errCode                32767a   Options(*VarSize : *NoPass)          <-> Error code

      //  Format Definition Header (Qdb_Qddfmt)
     d Qdb_Qddfmt      DS           256    Qualified
     d  bytRet                 1      4i 0
     d  bytAvl                 5      8i 0
     d  Qddfseq               81     93a

     d NO_OVERRIDE     C                   '0'

     d rtnQFile        DS                  Qualified
     d  name                         10a
     d  lib                          10a

     d errCode         DS                  LikeDs(errCode_t) inz(*LikeDs)

      /free

       QDBRTVFD(Qdb_Qddfmt: %size(Qdb_Qddfmt)
                : rtnQFile: 'FILD0200': i_qFile: i_rcdFmt: NO_OVERRIDE
                : '*LCL':'*EXT': errCode);
       if (errCode.excId <> '');
         kill('Could not retrieve file description of file ' +
              %trim(i_qFile.lib) + '/' + %trim(i_qFile.name) +
              ', record format ' + i_rcdFmt);
       endif;

       if (i_expectedId <> Qdb_Qddfmt.Qddfseq);
         kill('Level check on file ' +
              %trim(i_qFile.lib) + '/' + %trim(i_qFile.name) +
              ', record format ' + i_rcdFmt);
       endif;

      /end-free
     p                 E

      //*==========================================================================================*
      //* Prepare SQL Table Name                                                                   *
      //*==========================================================================================*

     p prepareSQLTableName...
     p                 B
     d                 PI            25a   Varying
     d  i_Qual_Changes...
     d                               21a   Const

     d i               S             10I 0
     d file            S             10A   Varying
     d library         S             10A   Varying

     d SLASH           C                   '/'
     d DOUBLE_QUOTES   C                   '"'

      /free

       i = %scan(SLASH: i_Qual_Changes);
       if (i = 0);
          return DOUBLE_QUOTES + %trim(i_Qual_Changes) + DOUBLE_QUOTES;
       endif;

       library = %trim(%subst(i_Qual_Changes: 1: i - 1));
       file = %trim(%subst(i_Qual_Changes: i + 1));

       return DOUBLE_QUOTES + library + DOUBLE_QUOTES + SLASH +
              DOUBLE_QUOTES + file + DOUBLE_QUOTES;

      /end-free
     p                 E

      //*==========================================================================================*
      //* Sends an *ESCAPE message to kill the program due to an error condition.                  *
      //*==========================================================================================*

     p kill...
     p                 B
     d                 PI
     d  i_text                      256a   Const Varying

      //  Send Program Message (QMHSNDPM) API
     d QMHSNDPM...
     d                 pr                  extpgm('QMHSNDPM')
     d  i_msgID                       7a   const
     d  i_qMsgF                      20A   const
     d  i_msgData                 32767A   const  options(*varsize )
     d  i_length                     10i 0 const
     d  i_msgType                    10a   const
     d  i_callStkE                  256a   const
     d  i_callStkC                   10i 0 const
     d  o_msgKey                      4a
     d  io_ErrCode                32767A          options(*varsize )

     d qMsgF           DS                  LikeDs(qObj_t)
     d msgKey          S              4A

     d errCode         DS                  LikeDs(errCode_t) inz(*LikeDs)

      /free

       qMsgF.name = 'QCPFMSG';
       qMsgF.lib = 'QSYS';

       qmhsndpm('CPF9898': qMsgF: i_text: %len(i_text)
                : '*ESCAPE': '*PGMBDY': 1: msgKey: errCode);

      /end-free
     p                 E
