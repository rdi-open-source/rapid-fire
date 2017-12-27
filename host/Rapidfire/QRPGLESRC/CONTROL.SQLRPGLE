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
//* Control                                                                                 *
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

// Control

CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT DftActGrp(*NO) ActGrp('RAPIDFIRE') BNDDIR('RAPIDFIRE');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,CONTROL
/Include QCPYSRC,RAPIDFIRE
/Include QCPYSRC,PROMOTER
/Include QCPYSRC,QMHRCVPM
/Include QCPYSRC,RCVM0100
/Include QCPYSRC,ERRC0100
/Include QCPYSRC,EXCCMD
/Include QCPYSRC,JOB
/Include QCPYSRC,NOTIFIER
/Include QCPYSRC,SQLSTS

//*-----------------------------------------------------------------------------------------*

// File declarations

DCL-DS RFJobs
       Extname('RFJOBS') Alias Qualified;
END-DS;

// Global constant declarations

DCL-S JOB_TYPE_JOURNAL
      CHAR(10) Inz('*JOURNAL');

DCL-S JOB_TYPE_FILE
      CHAR(10) Inz('*FILE');

// Global field declarations

DCL-S g_Loop
      IND Inz(*On);
DCL-S g_wait_Seconds
      ZONED(3) Inz(15);

//*-----------------------------------------------------------------------------------------*

// Parameter for this program

DCL-PI CONTROL;
  p_Job
  CHAR(10) Const;
  p_Test
  CHAR(10) Const;
END-PI;

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*-----------------------------------------------------------------------------------------*

Main(p_Job : p_Test);

*INLR = *On;

//*=========================================================================================*
//* Main                                                                                    *
//*=========================================================================================*

DCL-PROC Main;

  DCL-PI Main;
    p_Job
    CHAR(10) Const;
    p_Test
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Journal_Jobs
        INT(10);
  DCL-S l_File_Jobs
        INT(10);
  DCL-S l_Jobs_With_Apply_Changes
        INT(10);
  DCL-S l_Status
        CHAR(10);
  DCL-S l_Abort
        IND;
  DCL-S l_Apply_Changes
        IND;
  DCL-S l_Stop_Apply_Changes
        CHAR(1);

  Exec SQL
    Select Status Into :l_Status
    From Jobs
    Where Job = :p_Job;
  setIgnoreWarnings();
  If SqlCheckState(sqlState) And l_Status = '*RUN-PND';

    Exec SQL
      Update Jobs
      Set Status = '*RUN'
      Where Job = :p_Job;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

    Exec SQL
      Update Areas X
      Set X.File =
      (Select Y.File
      From Files Y
      Where X.Job = Y.Job And
            X.Position = Y.Position)
      Where X.Job = :p_Job;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

    If p_Test = '*YES';

      PROMOTER_initialize(p_Job : p_Test);

    Else;

      l_Abort = *Off;

      PROMOTER_initialize(p_Job : p_Test);
      If Not RAPIDFIRE_is_Error(p_Job);

        l_Journal_Jobs = submit_Journal_Jobs(p_Job);
        If Not RAPIDFIRE_is_Error(p_Job);

          wait_For_Jobs_To_Be_Active(
            p_Job : JOB_TYPE_JOURNAL : l_Journal_Jobs);
          If Not RAPIDFIRE_is_Error(p_Job);

            l_File_Jobs = submit_File_Jobs(p_Job);
            If Not RAPIDFIRE_is_Error(p_Job);

              wait_For_Jobs_To_Be_Active(
                p_Job : JOB_TYPE_FILE : l_File_Jobs);
              If Not RAPIDFIRE_is_Error(p_Job);

                Exec SQL
                  Update Jobs
                  Set Phase = '*CPY-RCD'
                  Where Job = :p_Job;
                setIgnoreWarnings();
                SqlCheckState(sqlState);

                l_Apply_Changes = *Off;

                DoW g_Loop;

                  If Not l_Apply_Changes;
                    Exec SQL
                      Select Count(*) Into :l_Jobs_With_Apply_Changes
                      From RFJobs
                      Where Job = :p_Job And
                            Status = '*RUN' And
                            Phase = '*APY-CHG';
                    setIgnoreWarnings();
                    If (SqlCheckState(sqlState) And
                        l_Jobs_With_Apply_Changes = l_File_Jobs);
                      l_Apply_Changes = *On;
                      Exec SQL
                        Update Jobs
                        Set Phase = '*APY-CHG'
                        Where Job = :p_Job;
                      setIgnoreWarnings();
                      SqlCheckState(sqlState);
                      NOTIFIER_notify_Subjects(
                        p_Job : '*LIBL' : 'RAPIDFIRE' :
                        'MSG0047' : p_Job);
                    EndIf;
                  EndIf;

                  // At least one job is no longer active
                  If get_Number_Of_Active_Jobs(p_Job) <>
                        l_Journal_Jobs + l_File_Jobs;
                    Exec SQL
                      Select Status Into :l_Status
                      From Jobs
                      where Job = :p_Job;
                    setIgnoreWarnings();
                    If SqlCheckState(sqlState) And l_Status = '*RUN';
                      RAPIDFIRE_log_Error(
                        p_Job :
                        'MSG0042' :
                        *Blanks :
                        *Blanks :
                        *Blanks
                      );
                    EndIf;
                    Leave;
                  EndIf;

                  Exec SQL
                    Select Status Into :l_Status
                    From Jobs
                    where Job = :p_Job;
                  setIgnoreWarnings();
                  If Not (SqlCheckState(sqlState) And l_Status = '*RUN');
                    Leave;
                  EndIf;

                  EXCCMD_execute_Command(
                    'DLYJOB DLY(' + %Char(g_wait_Seconds) + ')'
                  );

                EndDo;

                If Not l_Apply_Changes;
                  l_Abort = *On;
                EndIf;

                DoW g_Loop;

                  If get_Number_Of_Active_Jobs(p_Job) = *Zero;
                    Leave;
                  EndIf;

                  EXCCMD_execute_Command(
                    'DLYJOB DLY(' + %Char(g_wait_Seconds) + ')'
                  );

                EndDo;

              EndIf;

            EndIf;

          EndIf;

        EndIf;

      EndIf;

      If Not l_Abort;

        If Not RAPIDFIRE_is_Error(p_Job);

          Exec SQL
            Update Jobs
            Set Phase = '*RDY-PRD'
            Where Job = :p_Job;
          setIgnoreWarnings();
          SqlCheckState(sqlState);

        EndIf;

      Else;

        Exec SQL
          Update Jobs
          Set Phase = '*ABORT'
          Where Job = :p_Job;
        setIgnoreWarnings();
        SqlCheckState(sqlState);

      EndIf;

    EndIf;

    Exec SQL
      Select Stop_Apply_Changes Into :l_Stop_Apply_Changes
      From Jobs
      where Job = :p_Job;
    setIgnoreWarnings();
    If SqlCheckState(sqlState) And
          l_Stop_Apply_Changes = 'N';
      PROMOTER_clean(p_Job);
    EndIf;

    Exec SQL
      Update Jobs
      Set Status = '*END'
      Where Job = :p_Job;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC Main;

//*=========================================================================================*
//* Submit Journal Jobs                                                                     *
//*=========================================================================================*

DCL-PROC submit_Journal_Jobs;

  DCL-PI submit_Journal_Jobs
         INT(10);
    p_Job
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Job_Queue_Library
        CHAR(10);
  DCL-S l_Job_Queue
        CHAR(10);
  DCL-S l_Jobs
        INT(10);
  DCL-S l_Journal_Library
        CHAR(10);
  DCL-S l_Journal
        CHAR(10);
  DCL-S l_Job
        CHAR(10);
  DCL-S l_User
        CHAR(10);
  DCL-S l_Number
        CHAR(10);
  DCL-S l_Files
        CHAR(2048);
  DCL-S l_Cmd
        CHAR(2048);

  // Initialize jobs
  l_Jobs = *Zero;

  Exec SQL
    Select Job_Queue_Library, Job_Queue
    Into :l_Job_Queue_Library, :l_Job_Queue From Jobs
    Where Job = :p_Job;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    // Declare Cursor
    Exec SQL
      Declare Journals Cursor For
        Select Distinct Journal_Library, Journal
        From Files_With_Shadow_Library As Files
        Where Job = :p_Job And
              Type = '*PHYSICAL' And
              Exists (Select * From SysTables Where
              SysTables.System_Table_Schema = Files.Library And
              SysTables.System_Table_Name = Files.File) And
              Exists (Select * From Members Where
              Members.Job = Files.Job And
              Members.Position = Files.Position And
              Members.Area = Files.Area And
              Members.From_Area = '*YES')
        For Read Only;

    // Open cursor
    Exec SQL
      Open Journals;
    setIgnoreWarnings();
    If SqlCheckState(sqlState);

      DoW g_Loop;

        // Read 'Journals'
        Exec SQL
          Fetch Next From Journals
          Into :l_Journal_Library, :l_Journal;

        // End of file
        setIgnoreWarnings();
        If Not SqlCheckState(sqlState);
          Leave;
        EndIf;

        // Get journaled files
        l_Files = get_Journaled_Files(
          p_Job :
          l_Journal_Library :
          l_Journal
        );
        If RAPIDFIRE_is_Error(p_Job);
          Leave;
        EndIf;

        // Create command RCVJRNE
        l_Cmd =
          'RCVJRNE ' +
          'JRN(' + %Trim(l_Journal_Library) + '/' +
                   %Trim(l_Journal) + ') ' +
          'EXITPGM(JOURNAL) ' +
          'FILE(' + %Trim(l_Files) + ') ' +
          'RCVRNG(*CURCHAIN) ' +
          'JRNCDE((R) (F)) ' +
          'ENTFMT(*TYPE1) ' +
          'DELAY(' + %Char(g_wait_Seconds) + ') ' +
          'BLKLEN(*CALC)';

        // Submit journal job
        EXCCMD_execute_Command(
          'SBMJOB ' +
          'CMD(' +
            'CALL ' +
            'PGM(RCVJRNE) ' +
            'PARM(' +
              '''' + p_Job + '''' + ' ' +
              '''' + l_Cmd + '''' +
            ')' +
          ') ' +
          'JOB(' + %Trim(p_Job) + ') ' +
          'JOBQ(' + %Trim(l_Job_Queue_Library) + '/' +
                    %Trim(l_Job_Queue) + ') ' +
          'LOG(4 0 *MSG) ' +
          'HOLD(*YES)'
        );

        // Journal job for journal &1 in library &2 could not be submitted
        If EXCCMD_Message_Id <> *Blanks;
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0001' :
            l_Journal +
            l_Journal_Library :
            EXCCMD_Message_Id :
            EXCCMD_Message_Data
          );
          Leave;
        EndIf;

        // Get submitted job
        Reset ERRC0100;
        QMHRCVPM(
          RCVM0100 :        // <-- Message information
          %Size(RCVM0100) : // --> Length of message information
          'RCVM0100' :      // --> Format name
          '*' :             // --> Call stack entry
          0 :               // --> Call stack counter
          '*LAST' :         // --> Message type
          *Blanks :         // --> Message key
          0 :               // --> Wait time
          '*SAME' :         // --> Message action
          ERRC0100 :        // <-> Error code
          %Size('*') :      // --> Length of call stack entry
          '*NONE     *NONE' // --> Call stack entry qualification
        );

        // Journal job for journal &1 in library &2 could not be retrieved
        If EXCCMD_Message_Id <> *Blanks Or
              RCVM0100.Message_Id <> 'CPC1221';
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0002' :
            l_Journal +
            l_Journal_Library :
            EXCCMD_Message_Id :
            EXCCMD_Message_Data
          );
          Leave;
        EndIf;

        // Increase jobs
        l_Jobs+=1;

        // Retrieve job
        l_Job = %SubSt(RCVM0100.Message_Text : 1 : 10);
        l_User = %SubSt(RCVM0100.Message_Text : 11 : 10);
        l_Number = %SubSt(RCVM0100.Message_Text : 21 : 6);

        // Write status record for journal
        Exec SQL
          Insert Into RFJobs
          (Job, Batch_Job, Batch_User, Batch_Number, Type,
           Status, Phase, Stop_Apply_Changes, Receive_Journal_Entries)
          Values(:p_Job, :l_Job, :l_User, :l_Number, :JOB_TYPE_JOURNAL,
           '*START', '*JOURNAL', 'N', 'N');
        setIgnoreWarnings();
        SqlCheckState(sqlState);

        // Release journal job
        EXCCMD_execute_Command(
          'RLSJOB ' +
          'JOB(' + %Trim(l_Number) + '/' +
                   %Trim(l_User) + '/' +
                   %Trim(l_Job) + ')'
        );

        // Journal job for journal &1 in library &2 could not be released
        If EXCCMD_Message_Id <> *Blanks;
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0032' :
            l_Journal +
            l_Journal_Library :
            EXCCMD_Message_Id :
            EXCCMD_Message_Data
          );
          Leave;
        EndIf;

      EndDo;

      // Close cursor
      Exec SQL
        Close Journals;
      setIgnoreWarnings();
      SqlCheckState(sqlState);

    EndIf;

  EndIf;

  // No journal jobs submitted
  If l_Jobs = *Zero;
    //RAPIDFIRE_log_Error(
    //  p_Job :
    //  'MSG0003' :
    //  *Blanks :
    //  *Blanks :
    //  *Blanks
    //);
  EndIf;

  // Return jobs
  Return l_Jobs;

END-PROC submit_Journal_Jobs;

//*=========================================================================================*
//* Get Journaled Files                                                                     *
//*=========================================================================================*

DCL-PROC get_Journaled_Files;

  DCL-PI get_Journaled_Files
         CHAR(2048);
    p_Job
    CHAR(10) Const;
    p_Journal_Library
    CHAR(10) Const;
    p_Journal
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Files
        CHAR(2048);
  DCL-S l_Library
        CHAR(10);
  DCL-S l_File
        CHAR(10);

  // Initialize files
  l_Files = *Blanks;

  // Declare Cursor
  Exec SQL
    Declare Files_1 Cursor For
      Select Library, File From Files_With_Shadow_Library
      Where Job = :p_Job And
            Journal_Library = : p_Journal_Library And
            Journal = :p_Journal
      For Read Only;

  // Open cursor
  Exec SQL
    Open Files_1;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Files'
      Exec SQL
        Fetch Next From Files_1
        Into :l_Library, :l_File;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      // Build parameter files
      l_Files = %Trim(l_Files) +
        ' (' + %Trim(l_Library) + '/' + %Trim(l_File) + ' *ALL)';

    EndDo;

    // Close cursor
    Exec SQL
      Close Files_1;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

  // Return files
  Return %Trim(l_Files);

END-PROC get_Journaled_Files;

//*=========================================================================================*
//* Submit File Jobs                                                                        *
//*=========================================================================================*

DCL-PROC submit_File_Jobs;

  DCL-PI submit_File_Jobs
         INT(10);
    p_Job
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Job_Queue_Library
        CHAR(10);
  DCL-S l_Job_Queue
        CHAR(10);
  DCL-S l_Jobs
        INT(10);
  DCL-S l_File_Library
        CHAR(10);
  DCL-S l_File
        CHAR(10);
  DCL-S l_Job
        CHAR(10);
  DCL-S l_User
        CHAR(10);
  DCL-S l_Number
        CHAR(10);

  // Initialize jobs
  l_Jobs = *Zero;

  Exec SQL
    Select Job_Queue_Library, Job_Queue
    Into :l_Job_Queue_Library, :l_Job_Queue From Jobs
    Where Job = :p_Job;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    // Declare Cursor
    Exec SQL
      Declare Files_2 Cursor For
        Select Library, File
        From Files_With_Shadow_Library As Files
        Where Job = :p_Job And
              Type = '*PHYSICAL' And
              Exists (Select * From SysTables Where
              SysTables.System_Table_Schema = Files.Library And
              SysTables.System_Table_Name = Files.File) And
              Exists (Select * From Members Where
              Members.Job = Files.Job And
              Members.Position = Files.Position And
              Members.Area = Files.Area And
              Members.From_Area = '*YES')
        For Read Only;

    // Open cursor
    Exec SQL
      Open Files_2;
    setIgnoreWarnings();
    If SqlCheckState(sqlState);

      DoW g_Loop;

        // Read 'Files'
        Exec SQL
          Fetch Next From Files_2
          Into :l_File_Library, :l_File;

        // End of file
        setIgnoreWarnings();
        If Not SqlCheckState(sqlState);
          Leave;
        EndIf;

        // Submit file job
        EXCCMD_execute_Command(
          'SBMJOB ' +
          'CMD(' +
            'CALL ' +
            'PGM(FILEHDLR) ' +
            'PARM(' +
              '''' + p_Job + '''' + ' ' +
              '''' + l_File_Library + '''' + ' ' +
              '''' + l_File + '''' +
            ')' +
          ') ' +
          'JOB(' + %Trim(p_Job) + ') ' +
          'JOBQ(' + %Trim(l_Job_Queue_Library) + '/' +
                    %Trim(l_Job_Queue) + ') ' +
          'LOG(4 0 *MSG) ' +
          'HOLD(*YES)'
        );

        // File job for file &1 in library &2 could not be submitted
        If EXCCMD_Message_Id <> *Blanks;
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0031' :
            l_File +
            l_File_Library :
            EXCCMD_Message_Id :
            EXCCMD_Message_Data
          );
          Leave;
        EndIf;

        // Get submitted job
        Reset ERRC0100;
        QMHRCVPM(
          RCVM0100 :        // <-- Message information
          %Size(RCVM0100) : // --> Length of message information
          'RCVM0100' :      // --> Format name
          '*' :             // --> Call stack entry
          0 :               // --> Call stack counter
          '*LAST' :         // --> Message type
          *Blanks :         // --> Message key
          0 :               // --> Wait time
          '*SAME' :         // --> Message action
          ERRC0100 :        // <-> Error code
          %Size('*') :      // --> Length of call stack entry
          '*NONE     *NONE' // --> Call stack entry qualification
        );

        // File job for file &1 in library &2 could not be retrieved
        If EXCCMD_Message_Id <> *Blanks Or
              RCVM0100.Message_Id <> 'CPC1221';
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0043' :
            l_File +
            l_File_Library :
            EXCCMD_Message_Id :
            EXCCMD_Message_Data
          );
          Leave;
        EndIf;

        // Increase jobs
        l_Jobs+=1;

        // Retrieve job
        l_Job = %SubSt(RCVM0100.Message_Text : 1 : 10);
        l_User = %SubSt(RCVM0100.Message_Text : 11 : 10);
        l_Number = %SubSt(RCVM0100.Message_Text : 21 : 6);

        // Write status record for file
        Exec SQL
          Insert Into RFJobs
          (Job, Batch_Job, Batch_User, Batch_Number, Type,
           Status, Phase, Stop_Apply_Changes, Receive_Journal_Entries)
          Values(:p_Job, :l_Job, :l_User, :l_Number, :JOB_TYPE_FILE,
           '*START', '*NONE', 'N', 'N');
        setIgnoreWarnings();
        SqlCheckState(sqlState);

        // Release file job
        EXCCMD_execute_Command(
          'RLSJOB ' +
          'JOB(' + %Trim(l_Number) + '/' +
                   %Trim(l_User) + '/' +
                   %Trim(l_Job) + ')'
        );

        // File job for file &1 in library &2 could not be released
        If EXCCMD_Message_Id <> *Blanks;
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0044' :
            l_File +
            l_File_Library :
            EXCCMD_Message_Id :
            EXCCMD_Message_Data
          );
          Leave;
        EndIf;

      EndDo;

      // Close cursor
      Exec SQL
        Close Files_2;
      setIgnoreWarnings();
      SqlCheckState(sqlState);

    EndIf;

  EndIf;

  // No file jobs submitted
  If l_Jobs = *Zero;
    //RAPIDFIRE_log_Error(
    //  p_Job :
    //  'MSG0045' :
    //  *Blanks :
    //  *Blanks :
    //  *Blanks
    //);
  EndIf;

  // Return jobs
  Return l_Jobs;

END-PROC submit_File_Jobs;

//*=========================================================================================*
//* Wait For Jobs To Be Active                                                              *
//*=========================================================================================*

DCL-PROC wait_For_Jobs_To_Be_Active;

  DCL-PI wait_For_Jobs_To_Be_Active;
    p_Job
    CHAR(10) Const;
    p_Type
    CHAR(10) Const;
    p_Job_Counter
    INT(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Wait
        INT(10);
  DCL-S l_Active_Job_Counter
        INT(10);

  l_Wait = *Zero;

  DoU l_Active_Job_Counter = p_Job_Counter;

    Exec SQL
      Select Count(*) Into :l_Active_Job_Counter
      From RFJobs
      Where Job = :p_Job And
            Type = :p_Type And
            Status = '*RUN';
    setIgnoreWarnings();
    If SqlCheckState(sqlState) And
          l_Active_Job_Counter <> p_Job_Counter;

      l_Wait+=5;
      If l_Wait > 120;
        // At least one submitted job has not started within 120 seconds
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0004' :
          *Blanks :
          *Blanks :
          *Blanks
        );
        Leave;
      EndIf;

      EXCCMD_execute_Command(
        'DLYJOB DLY(5)'
      );

    EndIf;

  EndDo;

END-PROC wait_For_Jobs_To_Be_Active;

//*=========================================================================================*
//* Get Number Of Active Jobs                                                               *
//*=========================================================================================*

DCL-PROC get_Number_Of_Active_Jobs;

  DCL-PI get_Number_Of_Active_Jobs
         INT(10);
    p_Job
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Jobs
        INT(10);

  // Initialize jobs
  l_Jobs = *Zero;

  // Declare Cursor
  Exec SQL
    Declare RFJobs Cursor For
      Select *
      From RFJobs
      Where Job = :p_Job
      For Read Only;

  // Open cursor
  Exec SQL
    Open RFJobs;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'RFJobs'
      Exec SQL
        Fetch Next From RFJobs
        Into :RFJobs;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      // Increase job counter
      If JOB_is_Job_Active(
        RFJobs.Batch_Job :  // --> Job
        RFJobs.Batch_User : // --> User
        RFJobs.Batch_Number // --> Number
        );
        l_Jobs+=1;
      EndIf;

    EndDo;

    // Close cursor
    Exec SQL
      Close RFJobs;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

  // Return jobs
  Return l_Jobs;

END-PROC get_Number_Of_Active_Jobs;
