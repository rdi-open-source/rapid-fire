**FREE

//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* Copyright (c) 2016 Task Force IT-Consulting GmbH, Waltrop (Germany)                     *
//* This software and the accompanying materials are made available                         *
//* under the terms of the GNU General Public License version 2.0 (GPLv2)                   *
//* which accompanies this distribution and is available at                                 *
//* http://www.gnu.org/licenses/gpl-2.0.html                                                *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* Exit Program for Command RCVJRNE                                                        *
//*                                                                                         *
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
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 03.02.2016 I   TR   I STRPREPRC Header hinzugefügt.                                     *
//*------------+--------+-------------------------------------------------------------------*
//* 11.03.2016 I   TR   I Problem korrigiert, so dass jetzt auch Dateien mit einem Punkt    *
//*            I        I im Dateinamen verarbeitet werden.                                 *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE' :
         'JOURNALRTN');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,RAPIDFIRE
/Include QCPYSRC,JOURNALRTN
/Include QCPYSRC,SQLSTS
/Include QCPYSRC,EXCCMD

//*-----------------------------------------------------------------------------------------*

// Parameter for this program

DCL-PR JOURNAL
       ExtPgm('JOURNAL');
  p_Block_Length
  ZONED(5);
  p_Journal_Status
       LikeDs(g_Journal_Status);
END-PR;

DCL-PI JOURNAL;
  p_Block_Length
  ZONED(5);
  p_Journal_Status
       LikeDs(g_Journal_Status);
END-PI;

//*-----------------------------------------------------------------------------------------*

// Program status data structure

DCL-DS PSDS
       LEN(429) Qualified PSDS;
  Job
  CHAR(10) POS(244);
  User
  CHAR(10) POS(254);
  Number
  CHAR(6) POS(264);
END-DS;

// Data structure for *TYPE1 journal entry

DCL-S g_Journal_Entry_Pointer
      POINTER;

DCL-DS g_Journal_Entry
       Qualified
       Based(g_Journal_Entry_Pointer);
  Entry_Length
  ZONED(5);
  Sequence_Number
  ZONED(10);
  Journal_Code
  CHAR(1);
  Journal_Entry_Type
  CHAR(2);
  Date
  CHAR(6);
  Time
  CHAR(6);
  Job
  CHAR(10);
  User
  CHAR(10);
  Number
  ZONED(6);
  Program
  CHAR(10);
  Object
  CHAR(10);
  Object_Library
  CHAR(10);
  Member
  CHAR(10);
  Relative_Record_Number
  ZONED(10);
  Indicator
  CHAR(1);
  Commit_Cycle_Id
  ZONED(10);
  Incomplete_Data
  CHAR(1);
  Minimized_Entry
  CHAR(1);
  Reserved
  CHAR(6);
END-DS;

// Data structure for status informations

DCL-DS g_Journal_Status
       Qualified
       Based(g_Default_Pointer);
  Status
  CHAR(1);
  Available
  CHAR(1);
  What_Passed
  CHAR(1);
END-DS;

// Status for received journal entries

DCL-C STATUS_NO_ENTRY
      '0';
DCL-C STATUS_SINGLE_ENTRY
      '1';
DCL-C STATUS_BLOCK_ENTRY
      '2';
DCL-C STATUS_RECEIVER_CHANGED
      '3';
DCL-C STATUS_BEGIN_BLOCK_MODE
      '8';
DCL-C STATUS_END_RECEIVE_JOURNAL_ENTRY
      '9';

// Global field declarations

DCL-S g_Default_Pointer
      POINTER;
DCL-S g_Job
      CHAR(10) Inz('*NONE');
DCL-S g_Status
      CHAR(10);
DCL-S g_Workfile_Library
      CHAR(10);
DCL-S g_Workfile
      CHAR(10);
DCL-S g_Journal_Library
      CHAR(10);
DCL-S g_Journal
      CHAR(10);
DCL-S g_Insert
      CHAR(256);
DCL-S g_Error
      CHAR(1);
DCL-S g_Date
      CHAR(6);
DCL-S g_Time
      CHAR(6);
DCL-S g_Relative_Record_Number
      ZONED(10);
DCL-S g_SQL_Statement
      CHAR(1024);
DCL-S g_Number_Of_Changes
      PACKED(10);
DCL-S g_Loop
      IND Inz(*On);

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*-----------------------------------------------------------------------------------------*

If g_Job = '*NONE';

  // Initialize job, date and time
  g_Job = JOURNALRTN_get_Job();
  g_Date = JOURNALRTN_get_Date();
  g_Time = JOURNALRTN_get_Time();

  // Set status
  Exec SQL
    Update RFJobs
    Set Status = '*RUN'
    Where Job = :g_Job And
          Batch_Job = :PSDS.Job And
          Batch_User = :PSDS.User And
          Batch_Number = :PSDS.Number;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

EndIf;

Select;

  // Received journal entries in block mode
When p_Journal_Status.Status = STATUS_BLOCK_ENTRY;

  // Signal that journal entries are being received
  Exec SQL
    Update RFJobs
    Set Receive_Journal_Entries = 'Y'
    Where Job = :g_Job And
          Batch_Job = :PSDS.Job And
          Batch_User = :PSDS.User And
          Batch_Number = :PSDS.Number;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

  If p_Block_Length <> *Zero;

    // Set the pointer to the first entry
    g_Journal_Entry_Pointer =
      %Addr(p_Block_Length) +
      %Size(p_Block_Length);

    DoW g_Journal_Entry.Entry_Length <> *Zero;

      If (g_Journal_Entry.Journal_Code = 'R' And
            g_Journal_Entry.Journal_Entry_Type <> 'IL' And
            g_Journal_Entry.Relative_Record_Number <> *Zero) Or
           (g_Journal_Entry.Journal_Code = 'F' And
            g_Journal_Entry.Journal_Entry_Type = 'CR') Or
           (g_Journal_Entry.Journal_Code = 'F' And
            g_Journal_Entry.Journal_Entry_Type = 'RG');

        // Initialize error
        g_Error = 'N';

        // Get workfile
        Exec SQL
          Select Shadow_Library, Workfile_Changes,
                 Journal_Library, Journal
          Into :g_Workfile_Library, :g_Workfile,
               :g_Journal_Library, :g_Journal
          From Files_With_Shadow_Library
          Where Job = :g_Job And
                Library = :g_Journal_Entry.Object_Library And
                File = :g_Journal_Entry.Object;
        setIgnoreWarnings();
        If Not SqlCheckState(sqlState);

          // Journal job for journal &1 in library &2 could not read
          // "Changes Workfile" for file &3 in library &4
          RAPIDFIRE_log_Error(
            g_Job :
            'MSG0005' :
            g_Journal +
            g_Journal_Library +
            g_Journal_Entry.Object +
            g_Journal_Entry.Object_Library :
            *Blanks :
            *Blanks
          );

          g_Error = 'Y';

        Else;

          if g_Journal_Entry.Journal_Code = 'F' And
             g_Journal_Entry.Journal_Entry_Type = 'RG';

            // Journal job for journal &1 in library &2 received performed
            // command RGZPFM for file &3 in library &4
            RAPIDFIRE_log_Error(
              g_Job :
              'MSG0059' :
              g_Journal +
              g_Journal_Library +
              g_Journal_Entry.Object +
              g_Journal_Entry.Object_Library :
              *Blanks :
              *Blanks
            );

            g_Error = 'Y';

          Else;

            If g_Journal_Entry.Journal_Code = 'F' And
                g_Journal_Entry.Journal_Entry_Type = 'CR';
              g_Relative_Record_Number = -1;
            Else;
              g_Relative_Record_Number =
                g_Journal_Entry.Relative_Record_Number;
            EndIf;

            g_Insert =
              'Insert Into ' +
                quote(%Trim(g_Workfile_Library)) + '/' +
                quote(%Trim(g_Workfile)) +
              ' (Member, Record_Number) ' +
              'Values(' +
                sqlCharValue(%Trim(g_Journal_Entry.Member)) + ', ' +
                %Char(g_Relative_Record_Number) + ')';
            Exec SQL
              Prepare Changes From :g_Insert;
            Exec SQL
              Execute Changes;
            setIgnoreWarnings();
            If Not SqlCheckState(sqlState);

              // Journal job for journal &1 in library &2 could not write to
              // "Changes Workfile" for file &3 in library &4. Failed Statement : &5
              RAPIDFIRE_log_Error(
                g_Job :
                'MSG0006' :
                g_Journal +
                g_Journal_Library +
                g_Journal_Entry.Object +
                g_Journal_Entry.Object_Library +
                %SubSt(g_Insert : 1 : 128) :
                *Blanks :
                *Blanks
              );

              g_Error = 'Y';

            Else;

              // Update statistics for changes to apply for file
              If g_Journal_Entry.Journal_Entry_Type <> 'BR' And
                    g_Journal_Entry.Journal_Entry_Type <> 'UB';

                Exec SQL
                  Update Areas
                  Set Changes_To_Apply = Changes_To_Apply + 1
                  Where Job = :g_Job And
                        Library = :g_Journal_Entry.Object_Library And
                        File = :g_Journal_Entry.Object;
                setIgnoreWarnings();
                SqlCheckState(sqlState);

              EndIf;

              // Set date and time
              g_Date = g_Journal_Entry.Date;
              g_Time = g_Journal_Entry.Time;

              // Wait, until file handler job has cleared member of file in shadow library
              If g_Relative_Record_Number = -1;
                g_SQL_Statement =
                  'Select Count(*) From ' +
                  quote(%Trim(g_Workfile_Library)) + '/' +
                  quote(%Trim(g_Workfile)) +
                  ' Where Member = ' +
                  sqlCharValue(%Trim(g_Journal_Entry.Member));
                Exec SQL
                  Prepare Select_Changes From :g_SQL_Statement;
                setIgnoreWarnings();
                If Not SqlCheckState(sqlState);
                  g_Error = 'Y';
                Else;
                  Exec SQL
                    Declare Cursor_Changes Cursor For Select_Changes;
                  setIgnoreWarnings();
                  If Not SqlCheckState(sqlState);
                    g_Error = 'Y';
                  Else;
                    DoW g_Loop;
                      Exec SQL
                        Open Cursor_Changes;
                      setIgnoreWarnings();
                      If Not SqlCheckState(sqlState);
                        g_Error = 'Y';
                        Leave;
                      Else;
                        Exec SQL
                          Fetch Next From Cursor_Changes
                          Into :g_Number_Of_Changes;
                        setIgnoreWarnings();
                        If Not SqlCheckState(sqlState);
                          g_Error = 'Y';
                          Leave;
                        Else;
                          Exec SQL
                            Close Cursor_Changes;
                          setIgnoreWarnings();
                          If Not SqlCheckState(sqlState);
                            g_Error = 'Y';
                            Leave;
                          Else;
                            If g_Number_Of_Changes = *Zero;
                              Leave;
                            Else;
                              EXCCMD_execute_Command(
                                'DLYJOB DLY(5)'
                              );
                            EndIf;
                          EndIf;
                        EndIf;
                      EndIf;
                    EndDo;
                  EndIf;
                EndIf;
                If g_Error = 'Y';
                  // Journal job for journal &1 in library &2 could not retrieve changes in
                  // "Changes Workfile" for file &3 in library &4. Failed Statement : &5
                  RAPIDFIRE_log_Error(
                    g_Job :
                    'MSG0052' :
                    g_Journal +
                    g_Journal_Library +
                    g_Journal_Entry.Object +
                    g_Journal_Entry.Object_Library +
                    %SubSt(g_SQL_Statement : 1 : 128) :
                    *Blanks :
                    *Blanks
                  );
                EndIf;
              EndIf;

            EndIf;

          EndIf;

        EndIf;

        If g_Error = 'Y';

          // Set returncode
          JOURNALRTN_set_Return_Code('*ERROR');

          // Exit this program
          p_Journal_Status.Status = STATUS_END_RECEIVE_JOURNAL_ENTRY;

          // Leave the loop
          Leave;

        EndIf;

      EndIf;

      // Set the pointer to the next entry
      g_Journal_Entry_Pointer += g_Journal_Entry.Entry_Length;

    EndDo;

  EndIf;

  // The journal receiver has changed
When p_Journal_Status.Status = STATUS_RECEIVER_CHANGED;

  // Set returncode, date and time
  JOURNALRTN_set_Return_Code('*DETACHED');
  JOURNALRTN_set_Date(g_Date);
  JOURNALRTN_set_Time(g_Time);

  // No further journal entries available
When p_Journal_Status.Status = STATUS_NO_ENTRY;

  // Signal that journal entries will no longer be received
  Exec SQL
    Update RFJobs
    Set Receive_Journal_Entries = 'N'
    Where Job = :g_Job And
          Batch_Job = :PSDS.Job And
          Batch_User = :PSDS.User And
          Batch_Number = :PSDS.Number;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

  // Get status of job
  Exec SQL
    Select Status Into :g_Status
    From Jobs
    Where Job = :g_Job;
  setIgnoreWarnings();
  If Not (SqlCheckState(sqlState) And g_Status = '*RUN');

    // Set returncode
    JOURNALRTN_set_Return_Code('*NORMAL');

    // Exit this program
    p_Journal_Status.Status = STATUS_END_RECEIVE_JOURNAL_ENTRY;

  EndIf;

  // Unexpected status received
Other;

  // Journal job for journal &1 in library &2 received
  // unexpected status from command RCVJRNE
  RAPIDFIRE_log_Error(
    g_Job :
    'MSG0048' :
    g_Journal +
    g_Journal_Library :
    *Blanks :
    *Blanks
  );

  // Set returncode
  JOURNALRTN_set_Return_Code('*ERROR');

  // Exit this program
  p_Journal_Status.Status = STATUS_END_RECEIVE_JOURNAL_ENTRY;

EndSl;

// Leave this program
Return;

//*=========================================================================================*
//* Adds double single quotes around a SQL statement token, such as the table name.         *
//*=========================================================================================*

DCL-PROC quote;

  DCL-PI *N VARCHAR(100);
    p_token
    VARCHAR(100) Const;
  END-PI;

  DCL-C DOUBLE_QUOTES
        '"';

  Return DOUBLE_QUOTES + p_token + DOUBLE_QUOTES;

END-PROC;

//*=========================================================================================*
//* Puts a given SQL character value into quotes.                                           *
//*=========================================================================================*

DCL-PROC sqlCharValue;

  DCL-PI *N VARCHAR(100);
    p_charValue
    VARCHAR(100) Const;
  END-PI;

  DCL-C SINGLE_QUOTES
        '''';

  Return SINGLE_QUOTES + p_charValue + SINGLE_QUOTES;

END-PROC;
