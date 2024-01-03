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
//* Job                                                                                     *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTSQLRPGI   OBJ(&LI/&OB) +                                               *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM) +                             *
//*                              OBJTYPE(*MODULE);                                          *
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
//* 20.12.2016 I   FH   I STRPREPRC Header hinzugefügt.                                     *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,JOB
/Include QCPYSRC,RAPIDFIRE
/Include QCPYSRC,EXCCMD
/Include QCPYSRC,QMHRCVPM
/Include QCPYSRC,RCVM0100
/Include QCPYSRC,ERRC0100
/Include QCPYSRC,QUSRJOBI
/Include QCPYSRC,JOBI0700
/Include QCPYSRC,SQLSTS

//*-----------------------------------------------------------------------------------------*

// File declarations

DCL-DS Jobs
       Extname('JOBS') Alias Qualified;
END-DS;
DCL-DS Libraries
       Extname('LIBRARIES') Alias Qualified;
END-DS;

// Global field declarations

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

//*=========================================================================================*
//* Start                                                                                   *
//*=========================================================================================*

DCL-PROC JOB_start EXPORT;

  DCL-PI JOB_start;
    p_Job
    CHAR(10) Const;
    p_Test
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Job
        CHAR(10);
  DCL-S l_User
        CHAR(10);
  DCL-S l_Number
        CHAR(10);
  DCL-S l_Error
        IND;
  DCL-S l_Count
        INT(10);

  // Read job
  Exec SQL
    Select * Into :Jobs From Jobs Where Job = :p_Job;
  setIgnoreWarnings();
  If SqlCheckState(sqlState) And
        Jobs.Status = '*RDY';

    // Initialize error
    l_Error = *On;

    // Check, if there are more than 300 files specified
    Exec SQL
      Select Count(*) Into :l_Count
      From Files_With_Shadow_Library
      Where Job = :p_Job And
            Type = '*PHYSICAL';
    setIgnoreWarnings();
    If (SqlCheckState(sqlState) And
        l_Count > 300);

      // There are more than 300 files specified. 300 files is the limit for a Rapid Fire job.
      RAPIDFIRE_log_Error(
        p_Job :
        'MSG0061' :
        p_Job :
        *Blanks :
        *Blanks
      );

    Else;

      // Check, if the combination of library and file has been specified multiple times
      Exec SQL
        Select Count(*) Into :l_Count
        From
        (
         Select File, Library, Count(*)
         From Files_With_Shadow_Library
         Where Job = :p_Job
         Group By File, Library
         Having Count(*) > 1
        ) As X;
      setIgnoreWarnings();
      If (SqlCheckState(sqlState) And
          l_Count > *Zero);

        // At least one combination of library and file has been specified multiple times
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0056' :
          p_Job :
          *Blanks :
          *Blanks
        );

      Else;

        // Submit control job
        EXCCMD_execute_Command(
          'SBMJOB ' +
          'CMD(' +
            'CALL ' +
            'PGM(CONTROL) ' +
            'PARM(' +
              '''' + p_Job + '''' + ' ' +
              '''' + p_Test + '''' +
            ')' +
          ') ' +
          'JOB(' + %Trim(p_Job) + ') ' +
          'JOBQ(' + %Trim(Jobs.Job_Queue_Library) + '/' +
                    %Trim(Jobs.Job_Queue) + ') ' +
          'LOG(4 0 *MSG) ' +
          'HOLD(*YES) ' +
          'SYSLIBL(*CURRENT) ' +
          'CURLIB(*CURRENT) ' +
          'INLLIBL(*CURRENT) ' +
          'SPLFACN(*KEEP)'
        );

        // Control job for job &1 could not be submitted
        If EXCCMD_Message_Id <> *Blanks;
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0033' :
            p_Job :
            EXCCMD_Message_Id :
            EXCCMD_Message_Data
          );

        Else;

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

          // Control job for job &1 could not be retrieved
          If EXCCMD_Message_Id <> *Blanks Or
                RCVM0100.Message_Id <> 'CPC1221';
            RAPIDFIRE_log_Error(
              p_Job :
              'MSG0034' :
              p_Job :
              EXCCMD_Message_Id :
              EXCCMD_Message_Data
            );

          Else;

            // Retrieve job
            l_Job = %SubSt(RCVM0100.Message_Text : 1 : 10);
            l_User = %SubSt(RCVM0100.Message_Text : 11 : 10);
            l_Number = %SubSt(RCVM0100.Message_Text : 21 : 6);

            // Update Jobs
            Exec SQL
              Update Jobs
              Set Status = '*RUN-PND',
                  Batch_Job = :l_Job,
                  Batch_User = :l_User,
                  Batch_Number = :l_Number
              Where Job = :p_Job;
            setIgnoreWarnings();
            SqlCheckState(sqlState);

            // Release journal job
            EXCCMD_execute_Command(
              'RLSJOB ' +
              'JOB(' + %Trim(l_Number) + '/' +
                       %Trim(l_User) + '/' +
                       %Trim(l_Job) + ')'
            );

            // Control job for job &1 could not be released
            If EXCCMD_Message_Id <> *Blanks;
              RAPIDFIRE_log_Error(
                p_Job :
                'MSG0035' :
                p_Job :
                EXCCMD_Message_Id :
                EXCCMD_Message_Data
              );

            Else;

              // Initialize error
              l_Error = *Off;

            EndIf;

          EndIf;

        EndIf;

      EndIf;

    EndIf;

    // Set status to *END in case of an error
    If l_Error;

      Exec SQL
        Update Jobs
        Set Status = '*END'
        Where Job = :p_Job;
      setIgnoreWarnings();
      SqlCheckState(sqlState);

    EndIf;

  EndIf;

END-PROC JOB_start;

//*=========================================================================================*
//* End                                                                                     *
//*=========================================================================================*

DCL-PROC JOB_end EXPORT;

  DCL-PI JOB_end;
    p_Job
    CHAR(10) Const;
  END-PI;

  // Read job
  Exec SQL
    Select * Into :Jobs
    From Jobs
    Where Job = :p_Job;
  setIgnoreWarnings();
  If SqlCheckState(sqlState) And
        Jobs.Status = '*RUN';

    // Update Jobs
    Exec SQL
      Update Jobs
      Set Status = '*END-PND'
      Where Job = :p_Job;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC JOB_end;

//*=========================================================================================*
//* Reset                                                                                   *
//*=========================================================================================*

DCL-PROC JOB_reset EXPORT;

  DCL-PI JOB_reset;
    p_Job
    CHAR(10) Const;
    p_Delete_Shadow_Libraries
    CHAR(1) Const;
  END-PI;

  // Read job
  Exec SQL
    Select * Into :Jobs
    From Jobs
    Where Job = :p_Job;
  setIgnoreWarnings();
  If SqlCheckState(sqlState) And
        Jobs.Status <> '*RDY' And
        Not is_Active(p_Job);

    If p_Delete_Shadow_Libraries = 'X';

      // Declare Cursor
      Exec SQL
        Declare Libraries Cursor For
          Select * From Libraries
          Where Job = :p_Job
          Order By Job, Library
          For Read Only;

      // Open cursor
      Exec SQL
        Open Libraries;
      setIgnoreWarnings();
      If SqlCheckState(sqlState);

        DoW g_Loop;

          // Read 'Libraries'
          Exec SQL
            Fetch Next From Libraries
            Into :Libraries;

          // End of file
          setIgnoreWarnings();
          If Not SqlCheckState(sqlState);
            Leave;
          EndIf;

          // End journaling of access paths
          EXCCMD_execute_Command(
            'ENDJRNAP ' +
            'FILE(*ALL) ' +
            'JRN(' + %Trim(Libraries.Shadow_Library) + '/' +
                     'RAPIDFIRE)'
          );

          // End journaling of physical files
          EXCCMD_execute_Command(
            'ENDJRNPF ' +
            'FILE(*ALL) ' +
            'JRN(' + %Trim(Libraries.Shadow_Library) + '/' +
                     'RAPIDFIRE)'
          );

          // Delete journal
          EXCCMD_execute_Command(
            'DLTJRN ' +
            'JRN(' + %Trim(Libraries.Shadow_Library) + '/' +
                     'RAPIDFIRE)'
          );

          // Delete journal receiver
          EXCCMD_execute_Command(
            'DLTJRNRCV ' +
            'JRNRCV(' + %Trim(Libraries.Shadow_Library) + '/' +
                        'RAPID*) ' +
            'DLTOPT(*IGNINQMSG)'
          );

          // Delete shadow library
          EXCCMD_execute_Command(
            'DLTLIB ' +
            'LIB(' + %Trim(Libraries.Shadow_Library) + ')'
          );

        EndDo;

        // Close cursor
        Exec SQL
          Close Libraries;
        setIgnoreWarnings();
        SqlCheckState(sqlState);

      EndIf;

    EndIf;

    // Update Jobs
    Exec SQL
      Update Jobs
      Set Status = '*RDY',
          Phase = '*NONE',
          Error = 'N',
          Error_Text = '',
          Batch_Job = '',
          Batch_User = '',
          Batch_Number = '',
          Stop_Apply_Changes = 'N'
      Where Job = :p_Job;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

    // Update Libraries
    Exec SQL
      Update Libraries
      Set ASP_Number = 0,
          ASP_Device = ' '
      Where Job = :p_Job;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

    // Update Areas
    Exec SQL
      Update Areas
      Set File = '',
          Workfile_Records = '',
          Workfile_Records_Target = '',
          Workfile_Changes = '',
          Workfile_ChangesLog = '',
          SDT_View_Source = '',
          SDT_View_Target = '',
          Journaling = '',
          Journal_Library = '',
          Journal = '',
          Records_To_Copy = 0,
          Records_Copied = 0,
          Time_Consumed = 0,
          Changes_To_Apply = 0,
          Changes_Applied = 0
      Where Job = :p_Job;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

    // Delete Rapid Fire Jobs
    Exec SQL
      Delete
      From RFJobs
      Where Job = :p_Job;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

    // Delete Members
    Exec SQL
      Delete
      From Members
      Where Job = :p_Job;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

    // Delete Fields
    Exec SQL
      Delete
      From Fields
      Where Job = :p_Job;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC JOB_reset;

//*=========================================================================================*
//* Is Active                                                                               *
//*=========================================================================================*

DCL-PROC is_Active;

  DCL-PI is_Active
         IND;
    p_Job
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Job
        CHAR(10);
  DCL-S l_User
        CHAR(10);
  DCL-S l_Number
        CHAR(10);

  // Check control job
  Exec SQL
    Select Batch_Job, Batch_User, Batch_Number
    Into :l_Job, :l_User, :l_Number
    From Jobs
    Where Job = :p_Job;
  setIgnoreWarnings();
  If Not (SqlCheckState(sqlState) And
    JOB_is_Job_Active(l_Job : l_User : l_Number));

    Return *Off;

    // Check journal job
  Else;

    // Declare Cursor
    Exec SQL
      Declare RFJobs Cursor For
      Select Batch_Job, Batch_User, Batch_Number
      From RFJobs
      Where Job = :p_Job;

    // Open cursor
    Exec SQL
      Open RFJobs;
    setIgnoreWarnings();
    If SqlCheckState(sqlState);

      DoW g_Loop;

        // Read 'RFJobs'
        Exec SQL
          Fetch Next
          From RFJobs
          Into :l_Job, :l_User, :l_Number;

        // End of file
        setIgnoreWarnings();
        If Not SqlCheckState(sqlState);
          Leave;
        EndIf;

        // Job is not active
        If Not JOB_is_Job_Active(l_Job : l_User : l_Number);
          Return *Off;
        EndIf;

      EndDo;

      // Close cursor
      Exec SQL
        Close RFJobs;
      setIgnoreWarnings();
      SqlCheckState(sqlState);

    EndIf;

  EndIf;

  // Job is active
  Return *On;

END-PROC is_Active;

//*=========================================================================================*
//* Is Job Active                                                                           *
//*=========================================================================================*

DCL-PROC JOB_is_Job_Active EXPORT;

  DCL-PI JOB_is_Job_Active
         IND;
    p_Job
    CHAR(10) Const;
    p_User
    CHAR(10) Const;
    p_Number
    CHAR(6) Const;
  END-PI;

  Reset ERRC0100;
  QUSRJOBI(
    JOBI0700 :        // <-- Receiver variable
    %Size(JOBI0700) : // --> Length of receiver variable
    'JOBI0700' :      // --> Format name
    p_Job +
    p_User +
    p_Number :        // --> Qualified job name
    *Blanks :         // --> Internal job identifier
    ERRC0100          // <-> Error code
  );

  If ERRC0100.Exeption_Id = *Blanks And
        (JOBI0700.Job_Status = '*ACTIVE' Or
        JOBI0700.Job_Status = '*JOBQ');
    Return *On;
  Else;
    Return *Off;
  EndIf;

END-PROC JOB_is_Job_Active;

//*=========================================================================================*
//* Is Job Active SP                                                                        *
//*=========================================================================================*

DCL-PROC JOB_is_Job_Active_SP EXPORT;

  DCL-PI JOB_is_Job_Active_SP;
    p_Job
    CHAR(10) Const;
    p_User
    CHAR(10) Const;
    p_Number
    CHAR(6) Const;
    p_Active
    CHAR(1);
  END-PI;

  if (JOB_is_Job_Active(p_Job : p_User : p_Number));
    p_Active = 'Y';
  else;
    p_Active = 'N';
  endif;

END-PROC JOB_is_Job_Active_SP;
