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
//* Promoter                                                                                *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTSQLRPGI   OBJ(&LI/&OB) +                                               *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM) +                             *
//*                              OBJTYPE(*MODULE);                                          *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  OUTPUT(*PRINT);                                                          *
//*      >>PARM<<  GENLVL(0);                                                               *
//*      >>PARM<<  DBGVIEW(*NONE);                                                          *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*      >>PARM<<  COMPILEOPT('DBGVIEW(*ALL)');                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* IMPORTANT:                                                                              *
//* ==========                                                                              *
//* NULL values are not correctly copied, when the field in question has been renamed.      *
//* That is due to the bad implementation of IBM's null support.                            *
//*                                                                                         *
//* Usage notes:                                                                            *
//* ============                                                                            *
//* Rapid Fire uses "record mode", when the record format IDs of the source and target      *
//* files are the same and when there is no conversion program specified. In record mode,   *
//* there is just one program buffer for the input and output data.                         *
//*                                                                                         *
//* If the record format IDs are different or when there is a conversion program to use,    *
//* Rapid Fire copies the field values field by field. In field mode the conversion program *
//* receives the input and output buffers.                                                  *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//* >>PRE-COMPILER<<                                                                        *
//*   >>CRTCMD<<  CRTSQLRPGI SRCFILE(&SL/&SF) SRCMBR(&SM) OBJ(&LI/&OB) OBJTYPE(*MODULE);    *
//*   >>IMPORTANT<<                                                                         *
//*        >>PARM<<  CLOSQLCSR(*ENDMOD);                                                    *
//*        >>PARM<<  OUTPUT(*PRINT);                                                        *
//*        >>PARM<<  DBGVIEW(*SOURCE);                                                      *
//*        >>PARM<<  GENLVL(0);                                                             *
//*   >>END-IMPORTANT<<                                                                     *
//*   >>EXECUTE<<                                                                           *
//* >>END-PRE-COMPILER<<                                                                    *
//*------------+--------+-------------------------------------------------------------------*
//*   Datum    I Progr. I Beschreibung                                                      *
//*------------+--------+-------------------------------------------------------------------*
//* 03.02.2016 I   TR   I STRPREPRC Header hinzugefügt.                                     *
//*------------+--------+-------------------------------------------------------------------*
//* 11.03.2016 I   TR   I Programm erweitert, so dass bei einer Änderung im Satzformat die  *
//*            I        I Felder einzelnd, Feld für Feld, kopiert werden. Damit ist dafür   *
//*            I        I nicht mehr zwingend ein Kopierprogramm erforderlich.              *
//*------------+--------+-------------------------------------------------------------------*
//* 14.07.2016 I   TR   I Refactored 'Build_Target' of CPYPGM to initialize new field       *
//*            I        I prior the conversion statements and field assignments.            *
//*------------+--------+-------------------------------------------------------------------*
//* 18.07.2016 I   TR   I Fixed problem of duplicate field assignments in subroutine        *
//*            I        I generate_initial_Field_Assignments.                               *
//*------------+--------+-------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,') AlwNull(*USRCTL);
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT BndDir('RAPIDFIRE');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Enable/disable debug mode
// In debug mode, the generate source member is
// copied to the developer's library:
//   user_profile/QTARGET.QTARGET

// Includes

/Include QCPYSRC,PROMOTER

/Include QCPYSRC,RAPIDFIRE
/Include QCPYSRC,EXCCMD
/Include QCPYSRC,QLIRLIBD
/Include QCPYSRC,ERRC0100
/Include QCPYSRC,QUSRJOBI
/Include QCPYSRC,JOBI0700
/Include QCPYSRC,QUSCRTUS
/Include QCPYSRC,QUSPTRUS
/Include QCPYSRC,QUSDLTUS
/Include QCPYSRC,QUSLMBR
/Include QCPYSRC,MBRL0330
/Include QCPYSRC,MBRD0300
/Include QCPYSRC,GHDR0300
/Include QCPYSRC,QLICHGLL
/Include QCPYSRC,QUSRMBRD
/Include QCPYSRC,QUSLRCD
/Include QCPYSRC,RCDL0100
/Include QCPYSRC,QDBRTVFD
/Include QCPYSRC,FILD0100
/Include QCPYSRC,SQLSTS
/Include QCPYSRC,SETCCSID
/Include QCPYSRC,PRCSDT
/Include QCPYSRC,TYPES_H                    Type Templates
/Include QCPYSRC,FLDATTRS                   Field Attributes
/Include QCPYSRC,CVTDATE

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
  currUser
  CHAR(10) POS(358);
END-DS;

// Rapid Fire Home Library

DCL-F QSOURCE_t Disk(112) USAGE(*INPUT) Template Block(*no);
DCL-F QTARGET_t Disk(112) USAGE(*OUTPUT) Template Block(*no);

DCL-DS l_Rapid_Fire_Home_Library
       LEN(10) DtaAra('HOME');
END-DS;

// File templates

DCL-DS Statement_t
       LEN(100) Qualified Template;
  Filler_1
  CHAR(8) Inz(*Blanks);
  Value
  CHAR(72) Inz(*Blanks);
  Comment
  CHAR(20) Inz(*Blanks);
END-DS;

// File declarations

DCL-DS Libraries
       Extname('LIBRARIES') Alias Qualified;
END-DS;
DCL-DS Files
       Extname('FILESV01') Alias Qualified;
END-DS;
DCL-DS Library_List_Entries
       Extname('LIBLETYV01') Alias Qualified;
  Shadow_Library_Indicator
  INT(5);
END-DS;
DCL-DS Commands
       Extname('COMMANDS') Alias Qualified;
END-DS;
DCL-DS Members
       Extname('MEMBERS') Alias Qualified;
END-DS;
DCL-DS Conversions
       Extname('CNVS') Alias Qualified;
END-DS;

// Global constant declarations

DCL-C COMMAND_TYPE_PRERUN
      '*PRERUN';
DCL-C COMMAND_TYPE_COMPILE
      '*COMPILE';
DCL-C COMMAND_TYPE_POSTRUN
      '*POSTRUN';

DCL-C USRSPC_PROMOTER
      'PROMOTER  QTEMP     ';

// Field Prefixes

DCL-C SOURCE_PREFIX
      'S_';
DCL-C TARGET_PREFIX
      'T_';

// Generation Levels

//  Copy record format, used when the
//  record format level IDs are the same
DCL-C GEN_LVL_RECORD_COPY
      1;

//  Copy field by field, used when the
//  record formats are different
DCL-C GEN_LVL_FIELD_COPY
      2;

// Global field declarations

DCL-S g_Loop
      IND Inz(*On);
DCL-S g_Current_Library_List_Number
      ZONED(3);
DCL-S g_Current_Library_List
      CHAR(10) Dim(250);
DCL-S g_User_Library_List_Number
      ZONED(3);
DCL-S g_User_Library_List
      CHAR(10) Dim(250);
DCL-S g_User_Space_Pointer
      POINTER;
DCL-S g_File_Counter
      ZONED(3);
DCL-S g_src_Pfx
      CHAR(2) Inz('*N');
DCL-S g_tgt_Pfx
      CHAR(2) Inz('*N');
DCL-S g_Status
      CHAR(10);

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*=========================================================================================*
//* Initialize                                                                              *
//*=========================================================================================*

DCL-PROC PROMOTER_initialize EXPORT;

  DCL-PI PROMOTER_initialize;
    p_Job
    CHAR(10) Const;
    p_Test
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Create_Environment
        CHAR(10);

  // Initialize file counter
  g_File_Counter = *Zero;

  // Read Rapid Fire Home Library
  In l_Rapid_Fire_Home_Library;

  // Check if environment has to be created
  Exec SQL
    Select Create_Environment Into :l_Create_Environment
    From Jobs
    Where Job = :p_Job;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    // Create user space PROMOTOR in library QTEMP
    ERRC0100 = UserSpace_create(USRSPC_PROMOTER);

    If ERRC0100.Exeption_Id <> *Blanks;

      // User space PROMOTOR in library QTEMP could not be created
      RAPIDFIRE_log_Error(
        p_Job :
        'MSG0007' :
        *Blanks :
        ERRC0100.Exeption_Id :
        ERRC0100.Exeption_Data
      );

    Else;

      // Retrieve pointer to user space PROMOTOR in library QTEMP
      ERRC0100 = UserSpace_retrievePointer(
                   USRSPC_PROMOTER: g_User_Space_Pointer);

      If ERRC0100.Exeption_Id <> *Blanks;

        // Pointer to user space PROMOTOR in library QTEMP could not be retrieved
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0008' :
          *Blanks :
          ERRC0100.Exeption_Id :
          ERRC0100.Exeption_Data
        );

      Else;

        // Promote
        get_Current_Library_List(p_Job);
        If Not RAPIDFIRE_is_Error(p_Job);

          build_Shadow_Libraries(p_Job :
            l_Create_Environment);
          If Not RAPIDFIRE_is_Error(p_Job);

            build_Environment(p_Job : '*PHYSICAL' :
              l_Create_Environment);
            If Not RAPIDFIRE_is_Error(p_Job);

              build_Environment(p_Job : '*LOGICAL' :
                l_Create_Environment);
              If Not RAPIDFIRE_is_Error(p_Job);

                If p_Test = '*NO';
                  set_Journal(p_Job);
                EndIf;
                If Not RAPIDFIRE_is_Error(p_Job);
                EndIf;

              EndIf;

            EndIf;

          EndIf;

        EndIf;

      EndIf;

      // Delete user space PROMOTOR in library QTEMP
      ERRC0100 = UserSpace_delete(USRSPC_PROMOTER);

      If ERRC0100.Exeption_Id <> *Blanks;

        // User space PROMOTOR in library QTEMP could not be deleted
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0009' :
          *Blanks :
          ERRC0100.Exeption_Id :
          ERRC0100.Exeption_Data
        );

      EndIf;

    EndIf;

  EndIf;

END-PROC PROMOTER_initialize;

//*=========================================================================================*
//* Get Current Library List                                                                *
//*=========================================================================================*

DCL-PROC get_Current_Library_List;

  DCL-PI get_Current_Library_List;
    p_Job
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Counter
        INT(10);

  // Retrieve job informations
  Reset ERRC0100;
  QUSRJOBI(
    JOBI0700 :        // <-- Receiver variable
    %Size(JOBI0700) : // --> Length of receiver variable
    'JOBI0700' :      // --> Format name
    PSDS.Job +
    PSDS.User +
    PSDS.Number :     // --> Qualified job name
    *Blanks :         // --> Internal job identifier
    ERRC0100          // <-> Error code
  );

  // Current library list could not be retrieved
  If ERRC0100.Exeption_Id <> *Blanks;
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0010' :
      *Blanks :
      ERRC0100.Exeption_Id :
      ERRC0100.Exeption_Data
    );
    Return;
  EndIf;

  // Make library list informations global
  g_Current_Library_List_Number = JOBI0700.Current_Library_List_Number;
  g_User_Library_List_Number = JOBI0700.User_Library_List_Number;
  g_Current_Library_List(*) = *Blanks;
  g_User_Library_List(*) = *Blanks;

  For l_Counter = 1 To JOBI0700.Current_Library_List_Number;
    g_Current_Library_List(l_Counter) =
      %Subst(
        JOBI0700.Library_Area :
        ((JOBI0700.System_Library_List_Number +
          JOBI0700.Product_Library_List_Number) * 11) + 1 +
            ((l_Counter - 1) * 11) :
        10
      );
  EndFor;

  For l_Counter = 1 To JOBI0700.User_Library_List_Number;
    g_User_Library_List(l_Counter) =
      %Subst(
        JOBI0700.Library_Area :
        ((JOBI0700.System_Library_List_Number +
          JOBI0700.Product_Library_List_Number +
          JOBI0700.Current_Library_List_Number) * 11) + 1 +
            ((l_Counter - 1) * 11) :
        10
      );
  EndFor;

END-PROC get_Current_Library_List;

//*=========================================================================================*
//* Set Journal                                                                             *
//*=========================================================================================*

DCL-PROC set_Journal;

  DCL-PI set_Journal;
    p_Job
    CHAR(10) Const;
  END-PI;

  // Journal

  DCL-DS l_Journal
         Qualified;
    Journaling
    CHAR(1);
    Library
    CHAR(10);
    Journal
    CHAR(10);
  END-DS;

  // Locale field declarations

  DCL-S l_Qualified_File
        CHAR(20);

  // Declare Cursor
  Exec SQL
    Declare Files_3 Cursor For
      Select *
      From Files_With_Shadow_Library
      Where Job = :p_Job And
            Type = '*PHYSICAL' And
            Exists (Select * From SysTables Where
            SysTables.System_Table_Schema =
              Files_With_Shadow_Library.Library And
            SysTables.System_Table_Name =
              Files_With_Shadow_Library.File) And
            Exists (Select * From Members Where
            Members.Job =
              Files_With_Shadow_Library.Job And
            Members.Position =
              Files_With_Shadow_Library.Position And
            Members.Area =
              Files_With_Shadow_Library.Area And
            Members.From_Area =
              '*YES')
      Order By Job, Position, Area
      For Read Only;

  // Open cursor
  Exec SQL
    Open Files_3;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Files'
      Exec SQL
        Fetch Next
        From Files_3
        Into :Files;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      // Initialize journal
      l_Journal.Journaling = 'N';
      l_Journal.Library = *Blanks;
      l_Journal.Journal = *Blanks;

      // Get number of bytes needed
      Reset ERRC0100;
      QDBRTVFD(
        FILD0100 :         // <-- Receiver variable
        %Size(FILD0100) :  // --> Length of receiver variable
        l_Qualified_File : // <-- Qualified returned file name
        'FILD0100' :       // --> Format name
        Files.File +
        Files.Library :    // --> Qualified file name
        '*FIRST' :         // --> Record format name
        '0' :              // --> Override processing
        '*LCL' :           // --> System
        '*INT' :           // --> Format type
        ERRC0100           // <-> Error code
      );

      If ERRC0100.Exeption_Id = *Blanks;

        // Allocate heap storage
        QDBRTVFD_Heap_Pointer = %Alloc(Qdbfyavl);

        // Retrieve informations
        Reset ERRC0100;
        QDBRTVFD(
          QDBRTVFD_Heap :    // <-- Receiver variable
          Qdbfyavl :         // --> Length of receiver variable
          l_Qualified_File : // <-- Qualified returned file name
          'FILD0100' :       // --> Format name
          Files.File +
          Files.Library :    // --> Qualified file name
          '*FIRST' :         // --> Record format name
          '0' :              // --> Override processing
          '*LCL' :           // --> System
          '*INT' :           // --> Format type
          ERRC0100           // <-> Error code
        );

        If ERRC0100.Exeption_Id = *Blanks;

          // Set pointer to 'File Definition Header'
          Qdb_QdbfhP = QDBRTVFD_Heap_Pointer;

          // Set pointer to 'Journal Informations'
          Qdb_QdbfjoalP = Qdb_QdbfhP + Qdbfjorn;

          // The file is journaled
          If Qdbfjact = '1';
            l_Journal.Journaling = 'Y';
            l_Journal.Library = Qdbfolib;
            l_Journal.Journal = Qdbfojrn;
          EndIf;

        EndIf;

        // Deallocate heap storate
        DeAlloc QDBRTVFD_Heap_Pointer;

      EndIf;

      // The journal of file &1 in library &2 could not be retrieved
      If ERRC0100.Exeption_Id <> *Blanks;
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0011' :
          Files.File +
          Files.Library :
          ERRC0100.Exeption_Id :
          ERRC0100.Exeption_Data
        );
        Leave;
      EndIf;

      If l_Journal.Journaling = 'N';

        // Start journaling of physical file
        EXCCMD_execute_Command(
          'STRJRNPF ' +
          'FILE(' + %Trim(Files.Library) + '/' +
                    %Trim(Files.File) + ') ' +
          'JRN(' + %Trim(Files.Shadow_Library) + '/RAPIDFIRE) ' +
          'IMAGES(*BOTH) ' +
          'OMTJRNE(*OPNCLO)'
        );

        // Journaling for file &1 in library &2 could not be started
        If EXCCMD_Message_Id <> *Blanks;
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0039' :
            Files.File +
            Files.Library :
            EXCCMD_Message_Id :
            EXCCMD_Message_Data
          );
          Leave;
        EndIf;

        // Set journal
        l_Journal.Library = Files.Shadow_Library;
        l_Journal.Journal = 'RAPIDFIRE';

      EndIf;

      // Set journal for job
      Exec SQL
        Update Areas
        Set Journaling = :l_Journal.Journaling,
            Journal_Library = :l_Journal.Library,
            Journal = :l_Journal.Journal
        Where Job = :p_Job And
              Position = :Files.Position And
              Area = :Files.Area;
      setIgnoreWarnings();
      SqlCheckState(sqlState);

    EndDo;

    // Close cursor
    Exec SQL
      Close Files_3;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC set_Journal;

//*=========================================================================================*
//* Build Shadow Libraries                                                                  *
//*=========================================================================================*

DCL-PROC build_Shadow_Libraries;

  DCL-PI build_Shadow_Libraries;
    p_Job
    CHAR(10) Const;
    p_Create_Libraries
    CHAR(10) Const;
  END-PI;

  // ASP

  DCL-DS l_ASP_DS
         LEN(32) Qualified;
    l_ASP
    INT(10) Pos(29);
  END-DS;

  // ASP-Device

  DCL-DS l_ASP_Device_DS
         LEN(38) Qualified;
    l_ASP_Device
    CHAR(10) Pos(29);
  END-DS;

  // Locale field declarations

  DCL-S l_ASP
        CHAR(10);
  DCL-S l_ASP_Device
        CHAR(10);
  DCL-S l_ASP_Number
        INT(10);

  // Declare Cursor
  Exec SQL
    Declare Libraries Cursor For
      Select *
      From Libraries
      Where Job = :p_Job
      Order By Job, Library
      For Update Of ASP_Number, ASP_Device;

  // Open cursor
  Exec SQL
    Open Libraries;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Libraries'
      Exec SQL
        Fetch Next
        From Libraries
        Into :Libraries;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      // Retrieve ASP
      Reset ERRC0100;
      QLIRLIBD(
        l_ASP_DS :            // <-- Receiver variable
        %Size(l_ASP_DS) :     // --> Length of receiver variable
        Libraries.Library :   // --> Library name
        X'0000000100000002' : // --> Attributes to retrieve
        ERRC0100              // <-> Error code
      );

      // The ASP of library &1 could not be retrieved
      If ERRC0100.Exeption_Id <> *Blanks;
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0012' :
          Libraries.Library :
          ERRC0100.Exeption_Id :
          ERRC0100.Exeption_Data
        );
        Leave;
      EndIf;

      // Retrieve ASP-Unit
      Reset ERRC0100;
      QLIRLIBD(
        l_ASP_Device_DS :        // <-- Receiver variable
        %Size(l_ASP_Device_DS) : // --> Length of receiver variable
        Libraries.Library :      // --> Library name
        X'0000000100000008' :    // --> Attributes to retrieve
        ERRC0100                 // <-> Error code
      );

      // The ASP device of library &1 could not be retrieved
      If ERRC0100.Exeption_Id <> *Blanks;
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0013' :
          Libraries.Library :
          ERRC0100.Exeption_Id :
          ERRC0100.Exeption_Data
        );
        Leave;
      EndIf;

      If p_Create_Libraries = '*YES';

        // Initialize ASP and ASP-Device
        If l_ASP_DS.l_ASP <= 32;
          l_ASP = %Char(l_ASP_DS.l_ASP);
          l_ASP_Device = '*ASP';
        Else;
          l_ASP = '*ASPDEV';
          l_ASP_Device = l_ASP_Device_DS.l_ASP_Device;
        EndIf;

        // Create library
        EXCCMD_execute_Command(
          'CRTLIB ' +
          'LIB(' + %Trim(Libraries.Shadow_Library) + ') ' +
          'TEXT(''Rapid Fire - Shadow Library'') ' +
          'ASP(' + %Trim(l_ASP) + ') ' +
          'ASPDEV(' + %Trim(l_ASP_Device) + ')'
        );

        // Library &1 could not be created
        If EXCCMD_Message_Id <> *Blanks;
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0014' :
            Libraries.Shadow_Library :
            EXCCMD_Message_Id :
            EXCCMD_Message_Data
          );
          Leave;
        EndIf;

      EndIf;

      // Check existence of library
      EXCCMD_execute_Command(
        'CHKOBJ ' +
        'OBJ(QSYS/' + %Trim(Libraries.Shadow_Library) + ') ' +
        'OBJTYPE(*LIB)'
      );

      // Library &1 does not exit
      If EXCCMD_Message_Id <> *Blanks;
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0036' :
          Libraries.Shadow_Library :
          EXCCMD_Message_Id :
          EXCCMD_Message_Data
        );
        Leave;
      EndIf;

      // Create journal receiver
      EXCCMD_execute_Command(
        'CRTJRNRCV ' +
        'JRNRCV(' + %Trim(Libraries.Shadow_Library) + '/RAPIDFIRE) ' +
        'THRESHOLD(*NONE) ' +
        'TEXT(''* * *   R A P I D   F I R E   * * *'')'
      );

      If EXCCMD_Message_Id = *Blanks;

        // Create journal
        EXCCMD_execute_Command(
          'CRTJRN ' +
          'JRN(' + %Trim(Libraries.Shadow_Library) + '/RAPIDFIRE) ' +
          'JRNRCV(' + %Trim(Libraries.Shadow_Library) + '/RAPIDFIRE) ' +
          'MNGRCV(*USER) ' +
          'RCVSIZOPT(*MAXOPT3) ' +
          'TEXT(''* * *   R A P I D   F I R E   * * *'')'
        );

      EndIf;

      // Journal RAPIDFIRE could not be created in library &1
      If EXCCMD_Message_Id <> *Blanks;
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0041' :
          Libraries.Shadow_Library :
          EXCCMD_Message_Id :
          EXCCMD_Message_Data
        );
        Leave;
      EndIf;

      // Initialize ASP-Number and ASP-Device
      l_ASP_Number = l_ASP_DS.l_ASP;
      If l_ASP_DS.l_ASP <= 32;
        l_ASP_Device = *blanks;
      Else;
        l_ASP_Device = l_ASP_Device_DS.l_ASP_Device;
      EndIf;

      // Update 'Libraries'
      Exec SQL
        Update Libraries
        Set ASP_Number = :l_ASP_Number,
            ASP_Device = :l_ASP_Device
        Where Current Of Libraries;
      setIgnoreWarnings();
      SqlCheckState(sqlState);

    EndDo;

    // Close cursor
    Exec SQL
      Close Libraries;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC build_Shadow_Libraries;

//*=========================================================================================*
//* Build Environment                                                                       *
//*=========================================================================================*

DCL-PROC build_Environment;

  DCL-PI build_Environment;
    p_Job
    CHAR(10) Const;
    p_Type
    CHAR(10) Const;
    p_Create_Files
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_SDTViewTarget CHAR(10);
  DCL-S l_SDTViewRecordFormatTarget CHAR(10);
  DCL-S l_SDTViewFormatLevelIdTarget CHAR(13);
  DCL-S l_SDTViewSource CHAR(10);
  DCL-S l_SDTViewRecordFormatSource CHAR(10);
  DCL-S l_SDTViewFormatLevelIdSource CHAR(13);
  DCL-S l_Error CHAR(256);

  // Spoolfiles created by Rapid Fire have to be created with special parameters
  EXCCMD_execute_Command(
    'OVRPRTF ' +
    'FILE(*PRTF) ' +
    'HOLD(*YES) ' +
    'OUTQ(*JOB) ' +
    'OVRSCOPE(*JOB)'
  );

  // Declare Cursor
  Exec SQL
    Declare Files_1 Cursor For
      Select *
      From Files_With_Shadow_Library
      Where Job = :p_Job And
            Type = :p_Type
      Order By Job, Position, Area
      For Read Only;

  // Open cursor
  Exec SQL
    Open Files_1;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Files'
      Exec SQL
        Fetch Next
        From Files_1
        Into :Files;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      If p_Create_Files = '*YES';

        // CCSID-Setzen
        If Files.CCSID <> '*JOB';
          SetCCSID(%Dec(Files.CCSID : 5 : 0));
        EndIf;

        // Execute prerun commands
        execute_Commands(
          p_Job :
          Files.Position :
          Files.Area :
          Files.Shadow_Library :
          Files.File :
          Files.Library_List :
          *Blanks :
          COMMAND_TYPE_PRERUN
        );
        If RAPIDFIRE_is_Error(p_Job);
          Leave;
        EndIf;

        // Execute compile command
        execute_Commands(
          p_Job :
          Files.Position :
          Files.Area :
          Files.Shadow_Library :
          Files.File :
          Files.Library_List :
          Files.Command_Extension :
          COMMAND_TYPE_COMPILE
        );
        If RAPIDFIRE_is_Error(p_Job);
          Leave;
        EndIf;

        // Execute postrun commands
        execute_Commands(
          p_Job :
          Files.Position :
          Files.Area :
          Files.Shadow_Library :
          Files.File :
          Files.Library_List :
          *Blanks :
          COMMAND_TYPE_POSTRUN
        );
        If RAPIDFIRE_is_Error(p_Job);
          Leave;
        EndIf;

        // CCSID-Zurücksetzen
        If Files.CCSID <> '*JOB';
          SetCCSID(-1);
        EndIf;

      EndIf;

      // Check existence of file
      EXCCMD_execute_Command(
        'CHKOBJ ' +
        'OBJ(' + %Trim(Files.Shadow_Library) + '/' +
                 %Trim(Files.File) + ') ' +
        'OBJTYPE(*FILE)'
      );

      // File &1 in library &2 does not exit
      If EXCCMD_Message_Id <> *Blanks;
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0037' :
          Files.File +
          Files.Shadow_Library :
          EXCCMD_Message_Id :
          EXCCMD_Message_Data
        );
        Leave;
      EndIf;

      // Don`t continue, if file in production library does not exist or
      // file in production library does not contain at least one member
      EXCCMD_execute_Command(
        'CHKOBJ ' +
        'OBJ(' + %Trim(Files.Library) + '/' +
                 %Trim(Files.File) + ') ' +
        'OBJTYPE(*FILE) ' +
        'MBR(*FIRST)'
      );
      If EXCCMD_Message_Id <> *Blanks;
        Iter;
      EndIf;

      // Retrieve members of old file
      retrieve_Members(
        p_Job :
        Files.Position :
        Files.Area :
        Files.Library :
        Files.File :
        Files.Type :
        '*OLD'
      );
      If RAPIDFIRE_is_Error(p_Job);
        Leave;
      EndIf;

      // Retrieve members of new file
      retrieve_Members(
        p_Job :
        Files.Position :
        Files.Area :
        Files.Shadow_Library :
        Files.File :
        Files.Type :
        '*NEW'
      );
      If RAPIDFIRE_is_Error(p_Job);
        Leave;
      EndIf;

      // Create members
      create_Members(
        p_Job :
        Files.Position :
        Files.Area :
        Files.Library :
        Files.Shadow_Library :
        Files.File :
        Files.Type
      );
      If RAPIDFIRE_is_Error(p_Job);
        Leave;
      EndIf;

      If Files.Type = '*PHYSICAL';

        // Create work files
        create_Work_Files(
          p_Job :
          Files.Position :
          Files.Area :
          Files.Library :
          Files.Shadow_Library :
          Files.File
        );
        If RAPIDFIRE_is_Error(p_Job);
          Leave;
        EndIf;

        // Create environment for "Special Data Types" like LOB and DATALINK
        If Not PRCSDT_createEnvironment(
          p_Job :                        // --> Job
          Files.Position :               // --> Position
          Files.Area :                   // --> Area
          Files.Library :                // --> Library
          Files.Shadow_Library :         // --> Shadow library
          Files.File :                   // --> File
          'N' :                          // --> SDT View Processing
          l_SDTViewTarget :              // <-- SDT View Target
          l_SDTViewRecordFormatTarget :  // <-- SDT View Record Format Target
          l_SDTViewFormatLevelIdTarget : // <-- SDT View Format Level Id. Target
          l_SDTViewSource :              // <-- SDT View Source
          l_SDTViewRecordFormatSource :  // <-- SDT View Record Format Target
          l_SDTViewFormatLevelIdSource : // <-- SDT View Format Level Id. Target
          l_Error                        // <-- Error
        );
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0057' :
            l_Error :
            *Blanks :
            *Blanks
          );
          Leave;
        EndIf;

        // Store SDT views
        Exec SQL
          Update Areas
          Set SDT_View_Target = :l_SDTViewTarget,
              SDT_View_Source = :l_SDTViewSource
          Where Job = :p_Job And
                Position = :Files.Position And
                Area = :Files.Area;
        setIgnoreWarnings();
        SqlCheckState(sqlState);

        // Check, if file in source library contains fields with "Special Data Types" like LOB and DATALINK
        If PRCSDT_containsSDTFields(
          p_Job :
          Files.Position :
          Files.Area :
          'S'
        );
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0058' :
            Files.File +
            Files.Library :
            *Blanks :
            *Blanks
          );
          Leave;
        EndIf;

        // Check, if file in target library contains fields with "Special Data Types" like LOB and DATALINK
        If PRCSDT_containsSDTFields(
          p_Job :
          Files.Position :
          Files.Area :
          'T'
        );
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0058' :
            Files.File +
            Files.Shadow_Library :
            *Blanks :
            *Blanks
          );
          Leave;
        EndIf;

        // Generate copy program
        If Files.Copy_Program = '*GEN';
          PROMOTER_generate_Copy_Program(
            p_Job :
            Files.Position :
            Files.Area :
            Files.Library :
            Files.Shadow_Library :
            Files.File :
            Files.Conversion_Program_Library :
            Files.Conversion_Program
          );
          If RAPIDFIRE_is_Error(p_Job);
            Leave;
          EndIf;
        EndIf;

      EndIf;

    EndDo;

    // Close cursor
    Exec SQL
      Close Files_1;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

  // Delete overrides
  EXCCMD_execute_Command(
    'DLTOVR ' +
    'FILE(*PRTF) ' +
    'LVL(*JOB)'
  );

END-PROC build_Environment;

//*=========================================================================================*
//* Execute Commands                                                                        *
//*=========================================================================================*

DCL-PROC execute_Commands;

  DCL-PI execute_Commands;
    p_Job
    CHAR(10) Const;
    p_Position
    ZONED(6) Const;
    p_Area
    CHAR(10) Const;
    p_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
    p_Library_List
    CHAR(10) Const;
    p_Command_Extension
    CHAR(1024) Const;
    p_Type
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Message_Id
        CHAR(7);
  DCL-S l_Message_Data
        CHAR(256);

  // Declare Cursor
  Exec SQL
    Declare Commands Cursor For
      Select *
      From Commands
      Where Job = :p_Job And
            Position = :p_Position And
            Type = :p_Type
      Order By Job, Position, Type
      For Read Only;

  // Open cursor
  Exec SQL
    Open Commands;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Commands '
      Exec SQL
        Fetch Next
        From Commands
        Into :Commands ;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      // Replace substitution variables
      replace_Substitution_Variable('&FILE' : p_File : Commands.Command);
      replace_Substitution_Variable('&LIB' : p_Library : Commands.Command);

      // Set library list
      If Not set_Library_List('*SET' : p_Job : p_Library_List);
        Leave;
      EndIf;

      // Add command extension
      If p_Type = COMMAND_TYPE_COMPILE;
        Commands.Command = %Trim(Commands.Command) + ' ' +
                           %Trim(p_Command_Extension);
      EndIf;

      // Execute command
      EXCCMD_execute_Command(
        Commands.Command
      );

      // Save error informations
      l_Message_Id = EXCCMD_Message_Id;
      l_Message_Data = EXCCMD_Message_Data;

      // Reset library list
      If Not set_Library_List('*RESET');
        Leave;
      EndIf;

      // Command for position &1, area &2, type &3 and sequence &4 could not be executed
      If l_Message_Id <> *Blanks;
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0015' :
          %EditW(p_Position : '    0 ') +
          p_Area +
          p_type +
          %EditW(Commands.Sequence : ' 0 ') :
          l_Message_Id :
          l_Message_Data
        );
        Leave;
      EndIf;

    EndDo;

    // Close cursor
    Exec SQL
      Close Commands;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC execute_Commands;

//*=========================================================================================*
//* Replace Substitution Variable                                                           *
//*=========================================================================================*

DCL-PROC replace_Substitution_Variable;

  DCL-PI replace_Substitution_Variable;
    p_Variable
    CHAR(10) Const;
    p_Value
    CHAR(256) Const;
    p_Command
    VARCHAR(1024);
  END-PI;

  // Locale field declarations

  DCL-S l_Start
        ZONED(5);

  DoW g_Loop;
    l_Start = %Scan(%Trim(p_Variable) : p_Command);
    If l_Start = *Zero;
      Leave;
    Else;
      p_Command = %Replace(%Trim(p_Value) : p_Command :
        l_Start : %Len(%Trim(p_Variable)));
    EndIf;
  EndDo;

END-PROC replace_Substitution_Variable;

//*=========================================================================================*
//* Set Library List                                                                        *
//*=========================================================================================*

DCL-PROC set_Library_List;

  DCL-PI set_Library_List
         IND;
    p_Type
    CHAR(10) Const;
    p_Job
    CHAR(10) Const Options(*NoPass);
    p_Library_List
    CHAR(10) Const Options(*NoPass);
  END-PI;

  // Locale field declarations

  DCL-S l_Current_Library
        CHAR(11);
  DCL-S l_Library_List_Number
        INT(10);
  DCL-S l_Library_List
        CHAR(11) Dim(250);
  DCL-S l_Counter
        INT(10);

  Select;

    // Set library list
  When p_Type = '*SET';

    // Set current library
    l_Current_Library = '*CRTDFT';

    // Set library list
    l_Library_List_Number = *Zero;
    l_Library_List(*) = *Blanks;

    // Declare Cursor
    Exec SQL
      Declare Library_List_Entries Cursor For
        Select Job, Library_List, Sequence, Library, Shadow_Library
        From Library_List_Entries_With_Shadow_Library
        Where Job = :p_Job And
              Library_List = :p_Library_List
        Order By Job, Library_List, Sequence
        For Read Only;

    // Open cursor
    Exec SQL
      Open Library_List_Entries;
    setIgnoreWarnings();
    If SqlCheckState(sqlState);

      DoW g_Loop;

        // Read 'Library_List_Entries'
        Exec SQL
          Fetch Next
          From Library_List_Entries
          Into :Library_List_Entries.Job,
               :Library_List_Entries.Library_List,
               :Library_List_Entries.Sequence,
               :Library_List_Entries.Library,
               :Library_List_Entries.Shadow_Library
               :Library_List_Entries.Shadow_Library_Indicator;

        // End of file
        setIgnoreWarnings();
        If Not SqlCheckState(sqlState);
          Leave;
        EndIf;

        // Fill table with shadow library
        If Library_List_Entries.Shadow_Library_Indicator <> -1;
          l_Library_List_Number += 1;
          l_Library_List(l_Library_List_Number) =
            Library_List_Entries.Shadow_Library;
        EndIf;

        // Fill table with library
        l_Library_List_Number += 1;
        l_Library_List(l_Library_List_Number) =
          Library_List_Entries.Library;

      EndDo;

      // Close cursor
      Exec SQL
        Close Library_List_Entries;
      setIgnoreWarnings();
      SqlCheckState(sqlState);

    EndIf;

    // Reset library list
  When p_Type = '*RESET';

    // Set current library
    If g_Current_Library_List_Number = *Zero;
      l_Current_Library = '*CRTDFT';
    Else;
      l_Current_Library = g_Current_Library_List(1);
    EndIf;

    // Set library list
    l_Library_List_Number = g_User_Library_List_Number;
    l_Library_List(*) = *Blanks;
    For l_Counter = 1 To g_User_Library_List_Number By 1;
      l_Library_List(l_Counter) = g_User_Library_List(l_Counter);
    EndFor;

  EndSl;

  // Change library list
  Reset ERRC0100;
  QLICHGLL(
    l_Current_Library :     // --> Current library name
    '*SAME' :               // --> First product library name
    '*SAME' :               // --> Second product library name
    l_Library_List :        // --> User library list names
    l_Library_List_Number : // --> Number of user library names
    ERRC0100                // <-> Error code
  );

  // Library list &1 could not be seted/reseted : Type = &2
  If ERRC0100.Exeption_Id <> *Blanks;
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0016' :
      p_Library_List +
      p_Type :
      ERRC0100.Exeption_Id :
      ERRC0100.Exeption_Data
    );
    Return *Off;
  EndIf;

  // Everything is allright
  Return *On;

END-PROC set_Library_List;

//*=========================================================================================*
//* Generate Copy Program                                                                   *
//*=========================================================================================*

DCL-PROC PROMOTER_generate_Copy_Program EXPORT;

  DCL-F QSOURCE LikeFile(QSOURCE_t) ExtFile(l_File) ExtMbr('CPYPGM')
        UsrOpn;
  DCL-F QTARGET LikeFile(QTARGET_t) ExtFile('QTEMP/QTARGET')
        ExtMbr('QTARGET') UsrOpn;

  DCL-PI PROMOTER_generate_Copy_Program;
    p_Job
    CHAR(10) Const;
    p_Position
    ZONED(6) Const;
    p_Area
    CHAR(10) Const;
    p_Library
    CHAR(10) Const;
    p_Shadow_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
    p_Conversion_Program_Library
    CHAR(10) Const;
    p_Conversion_Program
    CHAR(10) Const;
    p_is_option_81
    IND Const Options(*NoPass);
    p_Source_Member
         Const LikeDs(qMbr_t) Options(*NoPass);
  END-PI;

  // Record Format of QSOURCE

  DCL-DS QSOURCERF
         Qualified;
    Sequence
    ZONED(6:2);
    Date
    ZONED(6);
    Data
    CHAR(100);
  END-DS;

  // Record Format of QTARGET

  DCL-DS QTARGETRF
         Qualified;
    Sequence
    ZONED(6:2);
    Date
    ZONED(6);
    Data
    CHAR(100);
  END-DS;

  // Rename

  DCL-DS Rename
         LEN(100) Qualified;
    Filler_1
    CHAR(5) Inz(*Blanks);
    Specification
    CHAR(1) Inz('i');
    Filler_2
    CHAR(14) Inz(*Blanks);
    Old_Field
    CHAR(10);
    Filler_3
    CHAR(18) Inz(*Blanks);
    New_Field
    CHAR(10);
    Filler_4
    CHAR(42) Inz(*Blanks);
  END-DS;

  // Statement

  DCL-S Statement
        Like(Statement_t.Value);

  // Locale field declarations

  DCL-S l_Source_Record_Format
        CHAR(10);
  DCL-S l_Target_Record_Format
        CHAR(10);
  DCL-S l_File
        CHAR(21);
  DCL-S l_Compile
        IND;
  DCL-S l_Area
        CHAR(10);
  DCL-S l_Scope
        CHAR(10);
  DCL-S l_Mode
        CHAR(4);
  DCL-DS l_qFile likeds(qObj_t);
  DCL-DS l_cnvEntries qualified;
    length INT(10);
    field LikeDs(l_field_t) Dim(2000);
  END-DS;
  DCL-DS l_field_t Qualified Template;
    name CHAR(10);
    newName CHAR(10);
  END-DS;
  DCL-S l_index INT(10);
  DCL-S l_hSrcFields Like(fldAttrs_handle_t) inz;
  DCL-S l_hTgtFields Like(fldAttrs_handle_t) inz;
  DCL-S l_numFields INT(10);
  DCL-DS l_srcField LikeDs(flda0300_t) inz;
  DCL-DS l_tgtField LikeDs(flda0300_t) inz;
  DCL-DS l_nullMapField LikeDs(flda0300_t) inz;
  DCL-S l_types CHAR(2);
  DCL-S l_gen_Level
        INT(10);
  DCL-S l_conversionError
        VARCHAR(40) Inz;
  DCL-S l_targetFieldValue
        VARCHAR(60) Inz;
  DCL-S l_Delete_User_Space
        IND Inz(*OFF);
  DCL-S l_tmpDftValue VARCHAR(2048) Inz;
  DCL-S l_logErrJob Like(p_job);

  DCL-S l_srcFormatLevelId Char(13);
  DCL-S l_tgtFormatLevelId Char(13);

  l_hSrcFields = *null;
  l_hTgtFields = *null;

  // If p_job = RAPIDFIRE_JOB_NONE;
  If %parms() >= %parmnum(p_is_option_81) and
     p_is_option_81 = *ON;
    UserSpace_create(USRSPC_PROMOTER);
    UserSpace_retrievePointer(
      USRSPC_PROMOTER: g_User_Space_Pointer);
    l_Delete_User_Space = *ON;
    l_logErrJob = RAPIDFIRE_JOB_NONE;
    // Read Rapid Fire Home Library
    In l_Rapid_Fire_Home_Library;
  Else;
    l_Delete_User_Space = *OFF;
    l_logErrJob = p_job;
  EndIf;

  DoU '1';

    // Get record format of source file
    l_Source_Record_Format = get_Record_Format(
      l_logErrJob :
      p_Library :
      p_File :
      USRSPC_PROMOTER
    );

    If RAPIDFIRE_is_Error(l_logErrJob);
      Leave; // Cleanup & Exit
    Endif;

    // Get record format of target file
    l_Target_Record_Format = get_Record_Format(
      l_logErrJob :
      p_Shadow_Library :
      p_File :
      USRSPC_PROMOTER
    );

    If RAPIDFIRE_is_Error(l_logErrJob);
      Leave; // Cleanup & Exit
    EndIf;

    // Produce field list of SOURCE file
    l_hSrcFields = FieldAttributes_new(p_File: p_Library:
                                       l_Source_Record_Format: ERRC0100);
    If FieldAttributes_isNull(l_hSrcFields);
      RAPIDFIRE_log_Error(
        l_logErrJob :
        'MSG0051' :             // Could not retrieve field list of file &1 in library &2.
        p_File + p_Library :
        ERRC0100.Exeption_Id :
        ERRC0100.Exeption_Data
      );
      // Do not leave here, because we need to delete
      // the handle at the end of the procedure.
    EndIf;

    If Not RAPIDFIRE_is_Error(l_logErrJob);

      // Produce field list of TARGET file
      l_hTgtFields = FieldAttributes_new(p_File: p_Shadow_Library :
                                         FLDATTRS_RCDFMT_FIRST: ERRC0100);
      If FieldAttributes_isNull(l_hTgtFields);
        RAPIDFIRE_log_Error(
          l_logErrJob :
          'MSG0051' :           // Could not retrieve field list of file &1 in library &2.
          p_File + p_Shadow_Library :
          ERRC0100.Exeption_Id :
          ERRC0100.Exeption_Data
        );
        // Do not leave here, because we need to delete
        // the handle at the end of the procedure.
      EndIf;

      If Not RAPIDFIRE_is_Error(l_logErrJob);

        // If p_Conversion_Program <> '*NONE' or
        //    FieldAttributes_getFormatLevelId(l_hSrcFields) <>
        //    FieldAttributes_getFormatLevelId(l_hTgtFields);
        //
        //   // Use 'field copy mode' when the record formats are different
        //   // or when a copy program is used.
        //   l_gen_Level = GEN_LVL_FIELD_COPY;
        //   g_src_Pfx = SOURCE_PREFIX;
        //   g_tgt_Pfx = TARGET_PREFIX;
        // Else;
        //
        //   // Else use the faster 'record copy mode'
        //   l_gen_Level = GEN_LVL_RECORD_COPY;
        //   g_src_Pfx = '''''';
        //   g_tgt_Pfx = g_src_Pfx;
        // Endif;

        l_srcFormatLevelId = FieldAttributes_getFormatLevelId(l_hSrcFields);
        l_tgtFormatLevelId = FieldAttributes_getFormatLevelId(l_hTgtFields);

        l_gen_Level = getFieldCopyMode(p_Conversion_Program: l_srcFormatLevelId: l_tgtFormatLevelId);
        g_src_Pfx = getSourceFieldPrefix(l_gen_Level);
        g_tgt_Pfx = getTargetFieldPrefix(l_gen_Level);

        // Create source physical file in library QTEMP
        EXCCMD_execute_Command(
          'CRTSRCPF ' +
          'FILE(QTEMP/QTARGET) ' +
          'MBR(QTARGET) ' +
          'RCDLEN(112)'
        );

        // Source physical file of copy program for file &1 in library &2 could not be created
        If EXCCMD_Message_Id <> *Blanks;
          RAPIDFIRE_log_Error(
            l_logErrJob :
            'MSG0017' :
            p_File +
            p_Library :
            EXCCMD_Message_Id :
            EXCCMD_Message_Data
          );

        Else;

          // Initialize compile
          l_Compile = *Off;

          // Open source file
          l_File = %Trim(l_Rapid_Fire_Home_Library) + '/QRPGLESRC';
          Open(E) QSOURCE;

          // Source file of copy program for file &1 in library &2 could not be opened
          If %Error;
            RAPIDFIRE_log_Error(
              l_logErrJob :
              'MSG0018' :
              p_File +
              p_Library :
              *Blanks :
              *Blanks
            );

          Else;

            // Open target file
            Open(E) QTARGET;

            // Target file of copy program for file &1 in library &2 could not be opened
            If %Error;
              RAPIDFIRE_log_Error(
                l_logErrJob :
                'MSG0038' :
                p_File +
                p_Library :
                *Blanks :
                *Blanks
              );

            Else;

              // Set compile
              l_Compile = *On;

              DoW g_Loop;

                // Read source
                Read QSOURCE QSOURCERF;

                // End of file
                If %EOF;
                  Leave;
                EndIf;

                // Rename area - Source
                If %Scan('%%RENAME_AREA_SOURCE%%' : QSOURCERF.Data) <> 0;
                  l_Area = '*RNM';
                  l_Scope = '*SRC';
                  ExSr generate_Area;
                  If RAPIDFIRE_is_Error(l_logErrJob);
                    Leave;
                  EndIf;
                  Iter;
                EndIf;

                // Rename area - Target
                If %Scan('%%RENAME_AREA_TARGET%%' : QSOURCERF.Data) <> 0;
                  l_Area = '*RNM';
                  l_Scope = '*TGT';
                  ExSr generate_Area;
                  If RAPIDFIRE_is_Error(l_logErrJob);
                    Leave;
                  EndIf;
                  Iter;
                EndIf;

                // Conversion area
                If %Scan('%%CONVERSION_AREA%%' : QSOURCERF.Data) <> 0;
                  l_Area = '*CNV';
                  ExSr generate_new_Field_Initialisations;
                  ExSr generate_Area;
                  ExSr generate_initial_Field_Assignments;
                  ExSr generate_initial_Null_Ind_Assignments;
                  If RAPIDFIRE_is_Error(l_logErrJob);
                    Leave;
                  EndIf;
                  If p_Conversion_Program <> '*NONE';
                    l_Mode = '*CNV';
                    ExSr generate_Call_Conversion_Program;
                  EndIf;
                  Iter;
                EndIf;

                // Conversion open
                If %Scan('%%CONVERSION_OPEN%%' : QSOURCERF.Data) <> 0;
                  If p_Conversion_Program <> '*NONE';
                    l_Mode = '*OPN';
                    ExSr generate_Call_Conversion_Program;
                  EndIf;
                  Iter;
                EndIf;

                // Conversion close
                If %Scan('%%CONVERSION_CLOSE%%' : QSOURCERF.Data) <> 0;
                  If p_Conversion_Program <> '*NONE';
                    l_Mode = '*CLS';
                    ExSr generate_Call_Conversion_Program;
                  EndIf;
                  Iter;
                EndIf;

                // Fill target
                QTARGETRF.Sequence = 0;
                QTARGETRF.Date = 0;
                QTARGETRF.Data = QSOURCERF.Data;

                // Change target
                Select;
                When %Scan('%%SOURCE_EXTDESC%%' : QTARGETRF.Data) <> 0;
                  %SubSt(QTARGETRF.Data : 44) =
                    'ExtDesc(''' +
                     %Trim(p_Library) + '/' + %Trim(p_File) +
                    ''')';
                When %Scan('%%SOURCE_RENAME%%' : QTARGETRF.Data) <> 0;
                  %SubSt(QTARGETRF.Data : 44) =
                    'Rename(' +
                     %Trim(l_Source_Record_Format) + ' : SourceRF' +
                    ')';
                When %Scan('%%SOURCE_EXTNAME%%' : QTARGETRF.Data) <> 0;
                  %SubSt(QTARGETRF.Data : 44) =
                    'ExtName(''' +
                     %Trim(p_Library) + '/' + %Trim(p_File) +
                    ''')';
                When %Scan('%%SOURCE_FMTLVLID%%' : QTARGETRF.Data) <> 0;
                  %SubSt(QTARGETRF.Data : 44) =
                    '''' +
                    FieldAttributes_getFormatLevelId(l_hSrcFields) +
                    '''';
                When %Scan('%%SOURCE_RCDFMT%%' : QTARGETRF.Data) <> 0;
                  %SubSt(QTARGETRF.Data : 44) =
                    '''' +
                    %Trim(l_Source_Record_Format) +
                    '''';
                When %Scan('%%SOURCE_PREFIX%%' : QTARGETRF.Data) <> 0;
                  If g_src_Pfx = *Blanks;
                    QTARGETRF.Data = *Blanks;
                  Else;
                    %SubSt(QTARGETRF.Data : 44) =
                      'Prefix(' + g_src_Pfx + ')';
                  EndIf;
                When %Scan('%%TARGET_EXTDESC%%' : QTARGETRF.Data) <> 0;
                  %SubSt(QTARGETRF.Data : 44) =
                    'ExtDesc(''' +
                     %Trim(p_Shadow_Library) + '/' + %Trim(p_File) +
                    ''')';
                When %Scan('%%TARGET_RENAME%%' : QTARGETRF.Data) <> 0;
                  %SubSt(QTARGETRF.Data : 44) =
                    'Rename(' +
                     %Trim(l_Target_Record_Format) + ' : TargetRF' +
                    ')';
                When %Scan('%%TARGET_EXTNAME%%' : QTARGETRF.Data) <> 0;
                  %SubSt(QTARGETRF.Data : 44) =
                    'ExtName(''' +
                     %Trim(p_Shadow_Library) + '/' + %Trim(p_File) +
                    ''')';
                When %Scan('%%TARGET_FMTLVLID%%' : QTARGETRF.Data) <> 0;
                  %SubSt(QTARGETRF.Data : 44) =
                    '''' +
                    FieldAttributes_getFormatLevelId(l_hTgtFields) +
                    '''';
                When %Scan('%%TARGET_RCDFMT%%' : QTARGETRF.Data) <> 0;
                  %SubSt(QTARGETRF.Data : 44) =
                    '''' +
                    %Trim(l_Target_Record_Format) +
                    '''';
                When %Scan('%%TARGET_PREFIX%%' : QTARGETRF.Data) <> 0;
                  If g_tgt_Pfx = *Blanks;
                    QTARGETRF.Data = *Blanks;
                  Else;
                    %SubSt(QTARGETRF.Data : 44) =
                      'Prefix(' + g_tgt_Pfx + ')';
                  EndIf;
                When %Scan('%%CNV_PGM%%' : QTARGETRF.Data) <> 0;
                  If p_Conversion_Program <> '*NONE';
                    %SubSt(QTARGETRF.Data : 44) =
                      'Inz(''' +
                       %Trim(p_Conversion_Program_Library) + '/' +
                       %Trim(p_Conversion_Program) +
                      ''')';
                  EndIf;
                When %Scan('%%RECORD_COPY%%' : QTARGETRF.Data) <> 0;
                  If l_gen_Level = GEN_LVL_FIELD_COPY;
                    Iter;
                  Else;
                    %SubSt(QTARGETRF.Data : 81) = '';
                  EndIf;
                When %Scan('%%COPY_MODE%%' : QTARGETRF.Data) <> 0;
                  If l_gen_Level = GEN_LVL_FIELD_COPY;
                    %SubSt(QTARGETRF.Data : 15) = 'FIELD_COPY';
                  Else;
                    %SubSt(QTARGETRF.Data : 15) = 'RECORD_COPY';
                  EndIf;
                EndSl;

                // Write target
                Write QTARGET QTARGETRF;

              EndDo;

              // Close target file
              Close QTARGET;

            EndIf;

            // Close source file
            Close QSOURCE;

          EndIf;

          If %parms() >= %parmnum(p_Source_Member);
            EXCCMD_execute_Command(
              'CPYSRCF FROMFILE(QTEMP/QTARGET) ' +
                      'TOFILE(' + %trim(p_Source_Member.lib) + '/' +
                                  %trim(p_Source_Member.file) + ') ' +
                      'FROMMBR(QTARGET) ' +
                      'TOMBR(' + %trim(p_Source_Member.name) + ') ' +
                      'MBROPT(*REPLACE)');
            EXCCMD_execute_Command(
              'CHGPFM FILE(' + %trim(p_Source_Member.lib) + '/' +
                               %trim(p_Source_Member.file) + ') ' +
                     'MBR(' + %trim(p_Source_Member.name) + ') ' +
                     'SRCTYPE(SQLRPGLE) ' +
              'TEXT(''* * *   R A P I D   F I R E   * * *'')');
          EndIf;

          If l_Compile;

            // Compile copy program
            EXCCMD_execute_Command(
              'CRTSQLRPGI ' +
              'OBJ(' + %Trim(p_Shadow_Library) + '/' +
                       %Trim(p_File) + ') ' +
              'SRCFILE(QTEMP/QTARGET) ' +
              'SRCMBR(QTARGET) ' +
              'OBJTYPE(*PGM) ' +
              'OUTPUT(*PRINT) ' +
              'GENLVL(0) ' +
              'DBGVIEW(*SOURCE) ' +
              'TGTRLS(*CURRENT) ' +
              'CLOSQLCSR(*ENDMOD) ' +
              'TEXT(''* * *   R A P I D   F I R E   * * *'')'
            );

            // Copy program for file &1 in library &2 could not be compiled
            If EXCCMD_Message_Id <> *Blanks;
              RAPIDFIRE_log_Error(
                l_logErrJob :
                'MSG0019' :
                p_File +
                p_Library :
                EXCCMD_Message_Id :
                EXCCMD_Message_Data
              );
            EndIf;

          EndIf;

          // Delete source physical file in library QTEMP
          EXCCMD_execute_Command(
            'DLTF ' +
            'FILE(QTEMP/QTARGET)'
          );

          // Source physical file of copy program for file &1 in library &2 could not be delete
          If EXCCMD_Message_Id <> *Blanks;
            RAPIDFIRE_log_Error(
              l_logErrJob :
              'MSG0020' :
              p_File +
              p_Library :
              EXCCMD_Message_Id :
              EXCCMD_Message_Data
            );
          EndIf;

        EndIf;

      EndIf;

    EndIf;

  EndDo;

  If l_hSrcFields <> *null;
    FieldAttributes_delete(l_hSrcFields);
    l_hSrcFields = *null;
  EndIf;

  If l_hTgtFields <> *null;
    FieldAttributes_delete(l_hTgtFields);
    l_hTgtFields = *null;
  EndIf;

  If (l_Delete_User_Space);
    UserSpace_delete(USRSPC_PROMOTER);
    l_Delete_User_Space = *OFF;
  EndIf;

  //*---------------------------------------------------------------------------------------*

  BegSr generate_Area;

    Select;
    When l_Area = '*CNV';
      generate_Statement(QTARGET: '');
      generate_Statement(QTARGET: '// Conversion statements': 1);
    EndSl;

    Clear l_cnvEntries;

    // Declare Cursor
    Exec SQL
      Declare Conversions Cursor For
        Select *
        From Conversions
        Where Job = :p_Job And
              Position = :p_Position
        Order By Job, Position, Field_To_Convert
        For Read Only;

    // Open cursor
    Exec SQL
      Open Conversions;
    setIgnoreWarnings();
    If SqlCheckState(sqlState);

      DoW g_Loop;

        // Read 'Conversions'
        Exec SQL
          Fetch Next
          From Conversions
          Into :Conversions;

        // End of file
        setIgnoreWarnings();
        If Not SqlCheckState(sqlState);
          Leave;
        EndIf;

        l_cnvEntries.length += 1;
        l_cnvEntries.field(l_cnvEntries.length).name =
           Conversions.Field_To_Convert;
        l_cnvEntries.field(l_cnvEntries.length).newName =
           Conversions.Rename_Field_In_Old_File_To;

        Select;

          // *** Rename area ***
        When l_Area = '*RNM';

          Select;

          When l_Scope = '*SRC' And
                Conversions.Rename_Field_In_Old_File_To <> '*NONE';

            // Build I-Specification
            Reset Rename;
            Rename.Old_Field = Conversions.Field_To_Convert;
            Rename.New_Field = Conversions.Rename_Field_In_Old_File_To;

            // Write target
            QTARGETRF.Sequence = 0;
            QTARGETRF.Date = 0;
            QTARGETRF.Data = Rename;
            Write QTARGET QTARGETRF;

          When l_Scope = '*TGT';

            // Rename only existing fields
            If FieldAttributes_retrieve(
                 l_hTgtFields: Conversions.Field_To_Convert
                 : l_tgtField: 'FLDA0300') >= 1;

              // Build I-Specification
              Reset Rename;
              Rename.Old_Field = Conversions.Field_To_Convert;
              Rename.New_Field = Conversions.Field_To_Convert;

              // Write target
              QTARGETRF.Sequence = 0;
              QTARGETRF.Date = 0;
              QTARGETRF.Data = Rename;
              Write QTARGET QTARGETRF;
            EndIf;

          EndSl;

          // *** Conversion area ***
        When l_Area = '*CNV';

          // Generate statements
          If Conversions.Statement_1 <> *Blanks;
            generate_Statement(QTARGET: Conversions.Statement_1: 1);
          EndIf;
          If Conversions.Statement_2 <> *Blanks;
            generate_Statement(QTARGET: Conversions.Statement_2: 1);
          EndIf;
          If Conversions.Statement_3 <> *Blanks;
            generate_Statement(QTARGET: Conversions.Statement_3: 1);
          EndIf;
          If Conversions.Statement_4 <> *Blanks;
            generate_Statement(QTARGET: Conversions.Statement_4: 1);
          EndIf;
          If Conversions.Statement_5 <> *Blanks;
            generate_Statement(QTARGET: Conversions.Statement_5: 1);
          EndIf;
          If Conversions.Statement_6 <> *Blanks;
            generate_Statement(QTARGET: Conversions.Statement_6: 1);
          EndIf;

        EndSl;

      EndDo;

      // Close cursor
      Exec SQL
        Close Conversions;
      setIgnoreWarnings();
      SqlCheckState(sqlState);

    EndIf;

  EndSr;

  //*---------------------------------------------------------------------------------------*

  Begsr generate_new_Field_Initialisations;

    If l_gen_Level <> GEN_LVL_FIELD_COPY;
      LeaveSr;
    EndIf;

    // Separate simple assignments from prior
    // conversion statements
    generate_Statement(QTARGET: '// Initialize new fields': 1);

    // Spin through the target fields and generate assignment statements
    FieldAttributes_rewind(l_hTgtFields);
    Dow FieldAttributes_retrieve(
            l_hTgtFields: FLDATTRS_FLD_NEXT: l_tgtField: 'FLDA0300') > 0;

      // Check source field
      If FieldAttributes_retrieve(
            l_hSrcFields: l_tgtField.name: l_srcField: 'FLDA0300') <= 0;

        // Field has been added to target file.
        If (not (l_tgtField.alwNull and l_tgtField.isNullDft));
          Select;
          When (l_tgtField.lenDftValue > 0 or l_tgtField.isCurrTime);
            If (l_tgtField.isHexLiteral);
              // Hex literals do not yet work.
              // TODO: how can we assign a hex literal to a graphic field?
              //    Statement =
              //      g_tgt_Pfx + %trim(l_tgtField.name) + ' -> ' +
              //      '*ERROR: *HEX default value';
              Statement =
                g_tgt_Pfx + %trim(l_tgtField.name) + ' = ' +
                getDefaultValue(l_tgtField) + ';';
            Else;
              // Default value is specified.
              Statement =
                g_tgt_Pfx + %trim(l_tgtField.name) + ' = ' +
                getDefaultValue(l_tgtField) + ';';
            EndIf;
          Other;
            // Default value is missing.
            // Set date, time and timestamp fields to
            // current date, time or timestamp value.
            // Clear all other field types.
            If l_tgtField.type = FLDATTRS_TYPE_DATE or
               l_tgtField.type = FLDATTRS_TYPE_TIME or
               l_tgtField.type = FLDATTRS_TYPE_TMSTMP;
              Statement =
                g_tgt_Pfx + %trim(l_tgtField.name) + ' = ' +
                getDefaultValueCurrentTime(l_tgtField)+ ';';
            else;
              Statement =
                'Clear ' + g_tgt_Pfx + %trim(l_tgtField.name) + ';';
            EndIf;
          EndSl;
          generate_Statement(QTARGET: Statement: 1);
        EndIf;

        If (l_tgtField.alwNull);
          If (l_tgtField.isNullDft);
            Statement =
            '%NullInd(' + g_tgt_Pfx + %trim(l_tgtField.name) + ') = *ON;';
          Else;
            Statement =
            '%NullInd(' + g_tgt_Pfx + %trim(l_tgtField.name) + ') = *OFF;';
          EndIf;
          generate_Statement(QTARGET: Statement: 1);
        EndIf;

      EndIf;
    EndDo;

  EndSr;

  //*---------------------------------------------------------------------------------------*

  BegSr generate_initial_Field_Assignments;

    If l_gen_Level <> GEN_LVL_FIELD_COPY;
      LeaveSr;
    EndIf;

    // Separate simple assignments from prior
    // conversion statements
    generate_Statement(QTARGET: '');
    generate_Statement(QTARGET: '// Assign field values': 1);

    // Spin through the target fields and generate assignment statements
    FieldAttributes_rewind(l_hTgtFields);
    Dow FieldAttributes_retrieve(
            l_hTgtFields: FLDATTRS_FLD_NEXT: l_tgtField: 'FLDA0300') > 0;

      // Exclude fields with conversion rules.
      If %lookup(l_tgtField.name : l_cnvEntries.field(*).name) > 0;
        Iter;
      EndIf;

      // Find source field, which might be at a different
      // position in the record format
      If FieldAttributes_retrieve(
            l_hSrcFields: l_tgtField.name: l_srcField: 'FLDA0300') > 0;

        Clear l_conversionError;

        Select;
        When l_tgtField.type <> l_srcField.type;
          l_types = l_tgtField.type + l_srcField.type;
          // Allow copying between numeric fields:
          //   Packed, Zoned and Binary
          If (%check(FLDATTRS_TYPE_PACKED +
                     FLDATTRS_TYPE_ZONED +
                     FLDATTRS_TYPE_BINARY
                     : l_types) <> 0);
            l_conversionError = '*ERROR: different types';
          EndIf;
     // When l_tgtField.ccsid <> l_srcField.ccsid;
     //   l_conversionError = '*ERROR: different CCSIDs';
     // When l_tgtField.datTimFmt <> l_srcField.datTimFmt;
     //   l_conversionError = '*ERROR: different date/time formats';
     // When l_tgtField.datTimSep <> l_srcField.datTimSep;
     //   l_conversionError = '*ERROR: different date/time separators';
        EndSl;

        If l_conversionError <> '';
          Statement =
            %trim(l_tgtField.name) + ' -> ' + l_conversionError;
        Else;
          Statement =
            g_tgt_Pfx + %trim(l_tgtField.name) + ' = ' +
            g_src_Pfx + %trim(l_srcField.name) + ';';
        EndIf;

        generate_Statement(QTARGET: Statement: 1);
      EndIf;
    EndDo;

  EndSr;

  //*---------------------------------------------------------------------------------------*

  BegSr generate_initial_Null_Ind_Assignments;

    If l_gen_Level <> GEN_LVL_FIELD_COPY;
      LeaveSr;
    EndIf;

    // Separate simple assignments from prior
    // assignment statements
    generate_Statement(QTARGET: '');
    generate_Statement(QTARGET: '// Assign null map indicators': 1);

    // Spin through the target fields and generate assignment statements
    FieldAttributes_rewind(l_hTgtFields);
    Dow FieldAttributes_retrieve(
            l_hTgtFields: FLDATTRS_FLD_NEXT: l_tgtField: 'FLDA0300') > 0;

      // Skip fields with conversion rules.
      If (%lookup(l_tgtField.name: l_cnvEntries.field(*).name) > 0);
        Iter;
      EndIf;

      // Skip target fields that are not nillable.
      If (not l_tgtField.alwNull);
        Iter;
      EndIf;

      // Find source field, which might be at a different
      // position in the record format
      If (FieldAttributes_retrieve(
            l_hSrcFields: l_tgtField.name: l_srcField: 'FLDA0300') > 0);
        // Target must be updated from source field
        If (l_srcField.alwNull);
          Statement =
            '%NullInd(' + g_tgt_Pfx + %trim(l_tgtField.name) + ') = ' +
            '%NullInd(' + g_src_Pfx + %trim(l_srcField.name) + ');';
        Else;
          Statement =
            '%NullInd(' + g_tgt_Pfx + %trim(l_tgtField.name) + ') = *OFF;';
        EndIf;
        generate_Statement(QTARGET: Statement: 1);
      EndIf;

    EndDo;

  EndSr;

  //*---------------------------------------------------------------------------------------*

  BegSr generate_Call_Conversion_Program;

    // ------------------------------
    // Initialize null maps
    // ------------------------------
    If l_Mode = '*CNV';
      generate_Statement(QTARGET: '');
      ExSr generate_beforeImage_nullMap;
      If l_gen_Level = GEN_LVL_FIELD_COPY;
        generate_Statement(QTARGET: '');
        ExSr generate_afterImage_nullMap;
      EndIf;
    EndIf;

    // ------------------------------
    // Call conversion program
    // ------------------------------
    generate_Statement(QTARGET: '');

    // Parameter: program
    Statement = 'Cnv_Pgm(';
    If l_Mode = '*CNV';
      generate_Statement(QTARGET
                         : '// Call user defined conversion program': 1);
      generate_Statement(QTARGET: Statement: 1);
    Else;
      generate_Statement(QTARGET: Statement: 0);
    EndIf;

    // Parameter: mode
    Statement = '''' + %trim(l_mode) +  '''';
    If l_Mode = '*CNV';
      generate_Statement(QTARGET: Statement: 2);
    Else;
      generate_Statement(QTARGET: Statement: 1);
    EndIf;

    // Parameter: after image data (TARGET)
    If l_Mode = '*CNV';
      generate_Statement(QTARGET: ': %Addr(g_Target)': 2);
    Else;
      generate_Statement(QTARGET: ': *Null': 1);
    EndIf;

    // Parameter: after image null map
    Statement = ': g_Target_nullMap';
    If l_Mode = '*CNV';
      generate_Statement(QTARGET: Statement: 2);
    Else;
      generate_Statement(QTARGET: Statement: 1);
    EndIf;

    // Parameter: after image field count
    If l_Mode = '*CNV';
      Statement = ': ' + %char(FieldAttributes_length(l_hTgtFields));
      generate_Statement(QTARGET: Statement: 2);
    Else;
      Statement = ': 0';
      generate_Statement(QTARGET: Statement: 1);
    EndIf;

    // Parameter: before image data (SOURCE)
    If l_Mode = '*CNV';
      If l_gen_Level = GEN_LVL_RECORD_COPY;
        generate_Statement(QTARGET: ': *Null': 2);
      Else;
        generate_Statement(QTARGET: ': %Addr(g_Source)': 2);
      EndIf;
    Else;
      generate_Statement(QTARGET: ': *Null': 1);
    EndIf;

    // Parameter: before image null map
    If l_gen_Level = GEN_LVL_RECORD_COPY;
      Statement = ': ''''';
    Else;
      Statement = ': g_Source_nullMap';
    EndIf;
    If l_Mode = '*CNV';
      generate_Statement(QTARGET: Statement: 2);
    Else;
      generate_Statement(QTARGET: Statement: 1);
    EndIf;

    // Parameter: before image field count
    If l_Mode = '*CNV';
      If l_gen_Level = GEN_LVL_RECORD_COPY;
        Statement = ': 0';
      Else;
        Statement = ': ' + %char(FieldAttributes_length(l_hSrcFields));
      EndIf;
      generate_Statement(QTARGET: Statement: 2);
    Else;
      Statement = ': 0';
      generate_Statement(QTARGET: Statement: 1);
    EndIf;

    // Close call statement
    Statement = ');';
    If l_Mode = '*CNV';
      generate_Statement(QTARGET: Statement: 2);
    Else;
      generate_Statement(QTARGET: Statement: 1);
    EndIf;


    // ------------------------------
    // Assign null map changes
    // ------------------------------
    If l_Mode = '*CNV';
      generate_Statement(QTARGET: '');
      ExSr generate_assign_nullMap_changes;
    EndIf;

  EndSr;

  //*---------------------------------------------------------------------------------------*

  BegSr generate_beforeImage_nullMap;

    // Spin through the fields and generate NULL map.
    FieldAttributes_rewind(l_hSrcFields);
    Dow FieldAttributes_retrieve(l_hSrcFields: FLDATTRS_FLD_NEXT
                                 : l_nullMapField: 'FLDA0300') > 0;
      If l_nullMapField.alwNull;
        // Use new field name, if the field has been
        // renamed by a conversion rule.
        l_index = %lookup(l_nullMapField.name: l_cnvEntries.field(*).name);
        If l_index > 0 and
          l_cnvEntries.field(l_index).newName <> '*NONE';
          l_nullMapField.name = l_cnvEntries.field(l_index).newName;
        Else;
          l_nullMapField.name = g_src_Pfx + %trim(l_nullMapField.name);
        EndIf;
        Statement = 'g_Source_nullMap(' +
                    %char(l_nullMapField.fieldIndex)  + ') = ' +
                    '%NullInd(' + %trim(l_nullMapField.name) + ');';
      Else;
        Statement = 'g_Source_nullMap(' +
                    %char(l_nullMapField.fieldIndex)  + ') = ''0'';';
      Endif;

      generate_Statement(QTARGET: Statement: 1);
    EndDo;

  EndSr;

  //*---------------------------------------------------------------------------------------*

  BegSr generate_afterImage_nullMap;

    // Spin through the fields and generate NULL map.
    FieldAttributes_rewind(l_hTgtFields);
    Dow FieldAttributes_retrieve(l_hTgtFields: FLDATTRS_FLD_NEXT
                                 : l_nullMapField: 'FLDA0300') > 0;
      If l_nullMapField.alwNull;
        Statement = 'g_Target_nullMap(' +
                    %char(l_nullMapField.fieldIndex)  + ') = ' +
                    '%NullInd(T_' + %trim(l_nullMapField.name) + ');';
      Else;
        Statement = 'g_Target_nullMap(' +
                    %char(l_nullMapField.fieldIndex)  + ') = ''0'';';
      Endif;

      generate_Statement(QTARGET: Statement: 1);
    EndDo;

  EndSr;


  //*---------------------------------------------------------------------------------------*

  BegSr generate_assign_nullMap_changes;

    If l_gen_Level <> GEN_LVL_FIELD_COPY;
      LeaveSr;
    EndIf;

    // Spin through the fields and generate NULL map.
    FieldAttributes_rewind(l_hTgtFields);
    Dow FieldAttributes_retrieve(l_hTgtFields: FLDATTRS_FLD_NEXT
                                 : l_nullMapField: 'FLDA0300') > 0;
      If l_nullMapField.alwNull;
        generate_Statement(QTARGET
                        : '%NullInd(T_' +
                          %trim(l_nullMapField.name) + ') = ' +
                          'g_Target_nullMap(' +
                          %char(l_nullMapField.fieldIndex) + ');'
                        : 1);
      Endif;
    EndDo;

  EndSr;

END-PROC PROMOTER_generate_Copy_Program;

//*=========================================================================================*
//* Get default value of a given field.                                                     *
//*=========================================================================================*

DCL-PROC getDefaultValue;

  DCL-PI *N VARCHAR(256);
    p_fldAttr
         Const LikeDs(flda0300_t);
  END-PI;

  DCL-S l_dftValue CHAR(2048) Based(l_pDftValue);
  DCL-S l_tmpDftValue VARCHAR(2048);
  DCL-S l_toDate CHAR(10);
  DCL-S l_hour ZONED(2 : 0);
  DCL-S l_save CHAR(2);

  DCL-C QUOTE '''';

  If p_fldAttr.lenDftValue = 0 and not p_fldAttr.isCurrTime;
    Return '';
  endif;

  l_pDftValue = p_fldAttr.pDftValue;

  Select;
  When (p_fldAttr.isCurrTime);
    l_tmpDftValue = getDefaultValueCurrentTime(p_fldAttr);
  Other;
    l_tmpDftValue = %subst(l_dftValue: 1: p_fldAttr.lenDftValue);
    If (l_tmpDftValue = 'USER');
      l_tmpDftValue = 'PSDS.currUser';
    Else;
      Select;
      When p_fldAttr.type = FLDATTRS_TYPE_DATE;
        Select;
        When p_fldAttr.datTimFmt = '*ISO' or
             p_fldAttr.datTimFmt = '*JIS';
          l_tmpDftValue =
            'd' +
            l_tmpDftValue;
        When p_fldAttr.datTimFmt = '*USA' and
             %subst(l_tmpDftValue : 1 : 1) = '''' and
             %subst(l_tmpDftValue : 12 : 1) = '''';
          l_tmpDftValue =
            'd' +
            '''' +
            %subst(l_tmpDftValue : 8 : 4) + '-' +
            %subst(l_tmpDftValue : 2 : 2) + '-' +
            %subst(l_tmpDftValue : 5 : 2) +
            '''';
        When p_fldAttr.datTimFmt = '*EUR' and
             %subst(l_tmpDftValue : 1 : 1) = '''' and
             %subst(l_tmpDftValue : 12 : 1) = '''';
          l_tmpDftValue =
            'd' +
            '''' +
            %subst(l_tmpDftValue : 8 : 4) + '-' +
            %subst(l_tmpDftValue : 5 : 2) + '-' +
            %subst(l_tmpDftValue : 2 : 2) +
            '''';
        When (p_fldAttr.datTimFmt = '*MDY' or
              p_fldAttr.datTimFmt = '*DMY' or
              p_fldAttr.datTimFmt = '*YMD') and
             %subst(l_tmpDftValue : 1 : 1) = '''' and
             %subst(l_tmpDftValue : 10 : 1) = '''';
          CvtDate(
            %subst(l_tmpDftValue : 2 : 8) : // --> From date
            p_fldAttr.datTimFmt :           // --> From format
            '*ISO' :                        // --> To format
            '-' :                           // --> To separator
            l_toDate                        // --> To date
          );
          l_tmpDftValue =
            'd' +
            '''' +
            %trim(l_toDate) +
            '''';
        When p_fldAttr.datTimFmt = '*JUL' and
             %subst(l_tmpDftValue : 1 : 1) = '''' and
             %subst(l_tmpDftValue : 8 : 1) = '''';
          CvtDate(
            %subst(l_tmpDftValue : 2 : 6) : // --> From date
            p_fldAttr.datTimFmt :           // --> From format
            '*ISO' :                        // --> To format
            '-' :                           // --> To separator
            l_toDate                        // --> To date
          );
          l_tmpDftValue =
            'd' +
            '''' +
            %trim(l_toDate) +
            '''';
        Other;
          l_tmpDftValue =
            'd' +
            l_tmpDftValue +
            %trim(p_fldAttr.datTimFmt) +
            '-*CVTERR';
        EndSl;
      When p_fldAttr.type = FLDATTRS_TYPE_TIME;
        Select;
        When p_fldAttr.datTimFmt = '*ISO' or
             p_fldAttr.datTimFmt = '*EUR';
          l_tmpDftValue =
            't' +
            l_tmpDftValue;
        When (p_fldAttr.datTimFmt = '*HMS' or
              p_fldAttr.datTimFmt = '*JIS') and
             %subst(l_tmpDftValue : 1 : 1) = '''' and
             %subst(l_tmpDftValue : 10 : 1) = '''';
          l_tmpDftValue =
            't' +
            '''' +
            %subst(l_tmpDftValue : 2 : 2) + '.' +
            %subst(l_tmpDftValue : 5 : 2) + '.' +
            %subst(l_tmpDftValue : 8 : 2) +
            '''';
        When p_fldAttr.datTimFmt = '*USA' and
             %subst(l_tmpDftValue : 1 : 1) = '''' and
             %subst(l_tmpDftValue : 10 : 1) = '''' and
             (%subst(l_tmpDftValue : 8 : 2) = 'AM' or
              %subst(l_tmpDftValue : 8 : 2) = 'PM');
          l_save = %subst(l_tmpDftValue : 8 : 2);
          l_tmpDftValue =
            't' +
            '''' +
            %subst(l_tmpDftValue : 2 : 2) + '.' +
            %subst(l_tmpDftValue : 5 : 2) + '.' +
            '00' +
            '''';
          If l_save = 'PM';
            Monitor;
              l_hour = %dec(%subst(l_tmpDftValue : 3 : 2) : 2 : 0);
              l_hour += 12;
              %subst(l_tmpDftValue : 3 : 2) = %EditC(l_hour : 'X');
            On-Error;
              l_tmpDftValue =
                l_tmpDftValue +
                %trim(p_fldAttr.datTimFmt) +
                '-*CVTERR';
            EndMon;
          EndIf;
        Other;
          l_tmpDftValue =
            't' +
            l_tmpDftValue +
            %trim(p_fldAttr.datTimFmt) +
            '-*CVTERR';
        EndSl;
      When p_fldAttr.type = FLDATTRS_TYPE_TMSTMP;
        l_tmpDftValue = 'z' + l_tmpDftValue;
      Other;
        l_tmpDftValue = l_tmpDftValue;
      EndSl;
    EndIf;
  Endsl;

  Return l_tmpDftValue;

END-PROC;

//*=========================================================================================*
//* Get defaulkt value: current date, current time or current timestamp.                    *
//*=========================================================================================*

DCL-PROC getDefaultValueCurrentTime;

  DCL-PI *N VARCHAR(256);
    p_fldAttr
         Const LikeDs(flda0300_t);
  END-PI;

  DCL-S l_tmpDftValue VARCHAR(2048);

  Select;
  When p_fldAttr.type = FLDATTRS_TYPE_DATE;
    l_tmpDftValue = '%Date(g_CURRENT_TIMESTAMP)';
  When p_fldAttr.type = FLDATTRS_TYPE_TIME;
    l_tmpDftValue = '%Time(g_CURRENT_TIMESTAMP)';
  When p_fldAttr.type = FLDATTRS_TYPE_TMSTMP;
    l_tmpDftValue = 'g_CURRENT_TIMESTAMP';
  Other;
    l_tmpDftValue = '*ERROR';
  EndSl;

  Return l_tmpDftValue;

END-PROC;

//*=========================================================================================*
//* Write statement to QTARGET.                                                             *
//*=========================================================================================*

DCL-PROC generate_Statement;

  DCL-PI *N;
    p_file
         LikeFile(QTARGET_t);
    p_statement
         Const Like(Statement_t.Value);
    p_tabs
    INT(10) Const Options(*nopass: *omit);
  END-PI;

  DCL-S tabs Like(p_tabs);

  DCL-DS Statement LikeDs(Statement_t) inz(*LikeDs);

  DCL-DS srcLine
         Qualified;
    Sequence
    ZONED(6:2);
    Date
    ZONED(6);
    Data
    CHAR(100);
  END-DS;

  If (%parms() >= %parmnum(p_tabs) and %addr(p_tabs) <> *null);
    tabs = p_tabs;
  Else;
    tabs = 0;
  EndIf;

  Clear Statement;
  Statement.Value = tab(tabs) + p_statement;

  Clear SrcLine;
  SrcLine.Sequence = 0;
  SrcLine.Date = 0;
  SrcLine.Data = Statement;

  Write p_file srcLine;

END-PROC;

//*=========================================================================================*
//* Create tabulatur.                                                                       *
//*=========================================================================================*

DCL-PROC tab;

  DCL-PI tab
         VARCHAR(80);
    p_tabs
    INT(10) Value;
  END-PI;

  DCL-S l_indent VARCHAR(80);

  DoW p_tabs > 0;
    l_indent = l_indent + '  ';
    p_tabs = p_tabs - 1;
  EndDo;

  Return l_indent;

END-PROC tab;

//*=========================================================================================*
//* Get Record Format                                                                       *
//*=========================================================================================*

DCL-PROC get_Record_Format;

  DCL-PI get_Record_Format
         CHAR(10);
    p_Job
    CHAR(10) Const;
    p_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
    p_usrspc
    CHAR(20) Const;
  END-PI;

  // List record formats
  Reset ERRC0100;
  QUSLRCD(
    p_usrspc :        // --> Qualified user space name
    'RCDL0100' :      // --> Format name
    p_File +
    p_Library :       // --> Qualified file name
    '0' :             // --> Override processing
    ERRC0100          // <-> Error code
  );

  // The record format of file &1 in library &2 could not be retrieved
  If ERRC0100.Exeption_Id <> *Blanks;
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0021' :
      p_File +
      p_Library :
      ERRC0100.Exeption_Id :
      ERRC0100.Exeption_Data
    );
    Return *Blanks;
  EndIf;

  // Set pointer of generic header
  GHDR0300_Pointer = g_User_Space_Pointer;

  // File &1 in library &2 has more than one record format
  If GHDR0300.Number_Of_List_Entries > 1;
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0022' :
      p_File +
      p_Library :
      *Blanks :
      *Blanks
    );
    Return *Blanks;
  EndIf;

  // Set pointer of list data section
  RCDL0100_Pointer =
    g_User_Space_Pointer +
    GHDR0300.Offset_To_List_Data_Section;

  // Return record format
  Return RCDL0100.Record_Format;

END-PROC get_Record_Format;

//*=========================================================================================*
//* Retrieve Members                                                                        *
//*=========================================================================================*

DCL-PROC retrieve_Members;

  DCL-PI retrieve_Members;
    p_Job
    CHAR(10) Const;
    p_Position
    ZONED(6) Const;
    p_Area
    CHAR(10) Const;
    p_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
    p_Type
    CHAR(10) Const;
    p_Reference_Point
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Counter
        INT(10);
  DCL-S l_Updated_Rows
        PACKED(10);
  DCL-S l_Member
        CHAR(10);
  DCL-S l_Records_To_Copy
        PACKED(10);

  // Initialize records to copy
  If p_Reference_Point = '*OLD' And
        p_Type = '*PHYSICAL';
    l_Records_To_Copy = *Zero;
  EndIf;

  // Retrieve members
  Reset ERRC0100;
  QUSLMBR(
    USRSPC_PROMOTER : // --> Qualified user space name
    'MBRL0330' :      // --> Format name
    p_File +
    p_Library :       // --> Qualified database file name
    '*ALL' :          // --> Member name
    '0' :             // --> Override processing
    ERRC0100          // <-> Error code
  );

  // The members of file &1 in library &2 could not be retrieved
  If ERRC0100.Exeption_Id <> *Blanks;
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0023' :
      p_File +
      p_Library :
      ERRC0100.Exeption_Id :
      ERRC0100.Exeption_Data
    );
    Return;
  EndIf;

  // Set pointer of generic header
  GHDR0300_Pointer = g_User_Space_Pointer;

  For l_Counter = 1 To GHDR0300.Number_Of_List_Entries By 1;

    // Set pointer of list data section
    MBRL0330_Pointer =
      g_User_Space_Pointer +
      GHDR0300.Offset_To_List_Data_Section +
      ((l_Counter - 1) * GHDR0300.Size_Of_Each_Entry);

    // Set pointer of member section
    MBRD0300_A_Pointer =
      g_User_Space_Pointer +
      MBRL0330.OffSet_To_Member_Description;

    Select;

      // Reference point is old file
    When p_Reference_Point = '*OLD';

      // Write member information
      Clear Members;
      Members.Job = p_Job;
      Members.Position = p_Position;
      Members.Area = p_Area;
      Members.Member = MBRD0300_A.Member;
      Members.Target_Area = '*NO';
      Members.From_Area = '*YES';
      If MBRD0300_A.Member_Text = *Blanks;
        Members.Text = '*BLANK';
      Else;
        Members.Text = MBRD0300_A.Member_Text;
      EndIf;
      Members.Records = MBRD0300_A.Number_Of_Current_Records;
      Members.Records_With_Duplicate_Key = *Zero;
      Exec SQL
        Insert Into Members Values (:Members);
      setIgnoreWarnings();
      SqlCheckState(sqlState);

      // Initialize records to copy
      If p_Reference_Point = '*OLD' And
            p_Type = '*PHYSICAL';
        l_Records_To_Copy += MBRD0300_A.Number_Of_Current_Records;
      EndIf;

      // Reference point is new file
    When p_Reference_Point = '*NEW';

      // Update member information
      l_Member = MBRD0300_A.Member;
      Exec SQL
        Update Members
        Set Target_Area = '*YES'
        Where Job = :p_Job And
              Position = :p_Position And
              Area = :p_Area And
              Member = :l_Member;
      setIgnoreWarnings();
      SqlCheckState(sqlState);

      // Get updated rows
      Exec SQL
        GET DIAGNOSTICS :l_Updated_Rows = ROW_COUNT;

      If l_Updated_Rows = *Zero;

        // Write member information
        Clear Members;
        Members.Job = p_Job;
        Members.Position = p_Position;
        Members.Area = p_Area;
        Members.Member = MBRD0300_A.Member;
        Members.Target_Area = '*YES';
        Members.From_Area = '*NO';
        Members.Text = *Blanks;
        Members.Records = *Zero;
        Members.Records_With_Duplicate_Key = *Zero;
        Exec SQL
          Insert Into Members Values (:Members);
        setIgnoreWarnings();
        SqlCheckState(sqlState);

      EndIf;

    EndSl;

  EndFor;

  // Initialize records to copy
  If p_Reference_Point = '*OLD' And
        p_Type = '*PHYSICAL';

    Exec SQL
      Update Areas
      Set Records_To_Copy = :l_Records_To_Copy
      Where Job = :p_Job And
            Position = :p_Position And
            Area = :p_Area;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC retrieve_Members;

//*=========================================================================================*
//* Create Members                                                                          *
//*=========================================================================================*

DCL-PROC create_Members;

  DCL-PI create_Members;
    p_Job
    CHAR(10) Const;
    p_Position
    ZONED(6) Const;
    p_Area
    CHAR(10) Const;
    p_Library
    CHAR(10) Const;
    p_Shadow_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
    p_Type
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Data_Members
        CHAR(1184);

  // Declare Cursor
  Exec SQL
    Declare Members_1 Cursor For
      Select *
      From Members
      Where Job = :p_Job And
            Position = :p_Position And
            Area = :p_Area And
            Target_Area = '*NO'
      Order By Job, Position, Area, Member
      For Read Only;

  // Open cursor
  Exec SQL
    Open Members_1;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Members'
      Exec SQL
        Fetch Next
        From Members_1
        Into :Members;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      Select;

        // Physical file
      When p_Type = '*PHYSICAL';

        // Create members
        EXCCMD_execute_Command(
          'ADDPFM ' +
          'FILE(' + %Trim(p_Shadow_Library) + '/' +
                    %Trim(p_File) + ') ' +
          'MBR(' + %Trim(Members.Member) + ') ' +
          'TEXT(''' + %Trim(Members.Text) + ''')'
        );

        // Logical file
      When p_Type = '*LOGICAL';

        // Get data members
        l_Data_Members = get_Logical_File_Data_Members(
          p_Job : p_Library : p_File : Members.Member
        );
        If RAPIDFIRE_is_Error(p_Job);
          Leave;
        EndIf;

        // Create members
        EXCCMD_execute_Command(
          'ADDLFM ' +
          'FILE(' + %Trim(p_Shadow_Library) + '/' +
                    %Trim(p_File) + ') ' +
          'MBR(' + %Trim(Members.Member) + ') ' +
          'DTAMBRS(' + %Trim(l_Data_Members) + ') ' +
          'TEXT(''' + %Trim(Members.Text) + ''')'
        );

      EndSl;

      // Member &1 could not be added to file &2 in library &3
      If EXCCMD_Message_Id <> *Blanks;
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0024' :
          Members.Member +
          p_File +
          p_Shadow_Library :
          EXCCMD_Message_Id :
          EXCCMD_Message_Data
        );
        Leave;
      EndIf;

    EndDo;

    // Close cursor
    Exec SQL
      Close Members_1;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC create_Members;

//*=========================================================================================*
//* Get Logical File Data Members                                                           *
//*=========================================================================================*

DCL-PROC get_Logical_File_Data_Members;

  DCL-PI get_Logical_File_Data_Members
         CHAR(1184);
    p_Job
    CHAR(10) Const;
    p_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
    p_Member
    CHAR(10) Const;
  END-PI;

  // Based on file

  DCL-DS l_Based_On_File
         Qualified;
    Qual_File
         Dim(32);
    Library
    CHAR(10) Overlay(Qual_File);
    File
    CHAR(10) Overlay(Qual_File : *Next);
    Member
    CHAR(10) Overlay(Qual_File : *Next);
    Number
    ZONED(2);
  END-DS;

  // Locale field declarations

  DCL-S l_Counter
        INT(10);
  DCL-S l_Counter_1
        INT(10);
  DCL-S l_Counter_2
        INT(10);
  DCL-S l_Data_Members
        CHAR(1184);
  DCL-S l_Break_Library
        CHAR(10);
  DCL-S l_Break_File
        CHAR(10);
  DCL-S l_Library
        CHAR(10);
  DCL-S l_Members
        CHAR(352);

  // Initialize data members
  l_Data_Members = *Blanks;

  // Initialize tables
  l_Based_On_File.Number = *Zero;
  l_Based_On_File.Library(*) = *HiVal;
  l_Based_On_File.File(*) = *HiVal;
  l_Based_On_File.Member(*) = *HiVal;

  // Get number of bytes needed
  Reset ERRC0100;
  QUSRMBRD(
    MBRD0300 :        // <-- Receiver variable
    %Size(MBRD0300) : // --> Length of receiver variable
    'MBRD0300' :      // --> Format name
    p_File +
    p_Library :       // --> Qualified database file name
    p_Member :        // --> Database member name
    '0' :             // --> Override processing
    ERRC0100          // <-> Error code
  );

  If ERRC0100.Exeption_Id = *Blanks;

    // Allocate heap storage
    QUSRMBRD_Heap_Pointer = %Alloc(MBRD0300.Bytes_Available);

    // Retrieve informations
    Reset ERRC0100;
    QUSRMBRD(
      QUSRMBRD_Heap :            // <-- Receiver variable
      MBRD0300.Bytes_Available : // --> Length of receiver variable
      'MBRD0300' :               // --> Format name
      p_File +
      p_Library :                // --> Qualified database file name
      p_Member :                 // --> Database member name
      '0' :                      // --> Override processing
      ERRC0100                   // <-> Error code
    );

    If ERRC0100.Exeption_Id = *Blanks;

      // Set pointer of information section
      MBRD0300_A_Pointer = QUSRMBRD_Heap_Pointer;

      For l_Counter = 1 To MBRD0300_A.Based_On_File_Number By 1;

        // Set pointer of based on file section
        MBRD0300_B_Pointer =
          QUSRMBRD_Heap_Pointer +
          %Size(MBRD0300_A) +
          ((l_Counter - 1) * %Size(MBRD0300_B));

        // Put members into table
        l_Based_On_File.Number+=1;
        l_Based_On_File.Library(l_Based_On_File.Number) =
          MBRD0300_B.Based_On_File_Library;
        l_Based_On_File.File(l_Based_On_File.Number) =
          MBRD0300_B.Based_On_File;
        l_Based_On_File.Member(l_Based_On_File.Number) =
          MBRD0300_B.Based_On_File_Member;

      EndFor;

    EndIf;

    // Deallocate heap storate
    DeAlloc QUSRMBRD_Heap_Pointer;

  EndIf;

  // The description of member &1 in file &2 in library &3 could not be retrieved
  If ERRC0100.Exeption_Id <> *Blanks;
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0025' :
      p_Member +
      p_File +
      p_Library :
      ERRC0100.Exeption_Id :
      ERRC0100.Exeption_Data
    );
    Return *Blanks;
  EndIf;

  // Sort table
  SortA l_Based_On_File.Qual_File;

  // Initialize break
  l_Break_Library = *Blanks;
  l_Break_File = *Blanks;

  For l_Counter_1 = 1 To l_Based_On_File.Number By 1;

    // Break
    If l_Break_Library <> l_Based_On_File.Library(l_Counter_1) Or
          l_Break_File <> l_Based_On_File.File(l_Counter_1);

      // Set break
      l_Break_Library = l_Based_On_File.Library(l_Counter_1);
      l_Break_File = l_Based_On_File.File(l_Counter_1);

      // Set library
      l_Library = l_Based_On_File.Library(l_Counter_1);

      Exec SQL
        Select SHADOW_LIBRARY Into :l_Library
        From Libraries
        Where Job = :p_Job And
              Library = :l_Library;
      setIgnoreWarnings();
      SqlCheckState(sqlState);

      // Start of expression
      l_Data_Members = %Trim(%Trim(l_Data_Members) + ' ' +
        '(' + %Trim(l_Based_On_File.File(l_Counter_1)) + ' (');

      // Middle of expression
      l_Members = *Blanks;
      l_Counter_2 = l_Counter_1;

      DoU l_Counter_2 >
            l_Based_On_File.Number Or
          l_Based_On_File.Library(l_Counter_2) <>
            l_Based_On_File.Library(l_Counter_1) Or
          l_Based_On_File.File(l_Counter_2) <>
            l_Based_On_File.File(l_Counter_1);

        l_Members = %Trim(%Trim(l_Members) + ' ' +
          %Trim(l_Based_On_File.Member(l_Counter_2)));
        l_Counter_2+= 1;

      EndDo;

      // End of expression
      l_Data_Members = %Trim(l_Data_Members) + %Trim(l_Members) + '))';

    EndIf;

  EndFor;

  // Return data members
  Return l_Data_Members;

END-PROC get_Logical_File_Data_Members;

//*=========================================================================================*
//* Create Work Files                                                                       *
//*=========================================================================================*

DCL-PROC create_Work_Files;

  DCL-PI create_Work_Files;
    p_Job
    CHAR(10) Const;
    p_Position
    ZONED(6) Const;
    p_Area
    CHAR(10) Const;
    p_Library
    CHAR(10) Const;
    p_Shadow_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_File_Records
        CHAR(10);
  DCL-S l_File_Records_Target
        CHAR(10);
  DCL-S l_File_Changes
        CHAR(10);
  DCL-S l_File_ChangesLog
        CHAR(10);
  DCL-S l_File_Duplicate_Record_Keys
        CHAR(10);
  DCL-S l_SQL_Statement
        CHAR(256);

  // Set file counter
  If g_File_Counter = 999;
    g_File_Counter = *Zero;
  EndIf;
  g_File_Counter+=1;

  // Build name of Workfile Records
  l_File_Records =
    '$R' + %Trim(%SubSt(p_File : 1 : 5)) +
    %EditC(g_File_Counter : 'X');

  l_File_Records_Target =
    '$T' + %Trim(%SubSt(p_File : 1 : 5)) +
    %EditC(g_File_Counter : 'X');

  // Build name of Workfile Changes
  l_File_Changes =
    '$C' + %Trim(%SubSt(p_File : 1 : 5)) +
    %EditC(g_File_Counter : 'X');

  // Build name of Workfile Changes Log
  l_File_ChangesLog =
    '$L' + %Trim(%SubSt(p_File : 1 : 5)) +
    %EditC(g_File_Counter : 'X');

  // Build name of Workfile Duplicate Record Keys
  l_File_Duplicate_Record_Keys =
    '$D' + %Trim(%SubSt(p_File : 1 : 5)) +
    %EditC(g_File_Counter : 'X');

  // Create Workfile Records
  EXCCMD_execute_Command(
    'CRTDUPOBJ ' +
    'OBJ(RECORDS) ' +
    'FROMLIB(' + %Trim(l_Rapid_Fire_Home_Library) + ') ' +
    'OBJTYPE(*FILE) ' +
    'TOLIB(' + %Trim(p_Shadow_Library) + ') ' +
    'NEWOBJ(' + %Trim(l_File_Records) + ') ' +
    'DATA(*NO) ' +
    'CST(*YES)'
  );

  // Workfile Records for file &1 in library &2 could not be created
  If EXCCMD_Message_Id <> *Blanks;
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0026' :
      p_File +
      p_Library :
      EXCCMD_Message_Id :
      EXCCMD_Message_Data
    );
    Return;
  EndIf;

  // Create Workfile Records Target
  l_SQL_Statement =
    'CREATE UNIQUE INDEX ' +
    '"' + %Trim(p_Shadow_Library) + '"' + '/' +
    '"' + %Trim(l_File_Records_Target) + '" ' +
    'ON ' +
    '"' + %Trim(p_Shadow_Library) + '"' + '/' +
    '"' + %Trim(l_File_Records) + '" ' +
    '(MEMBER ASC, TARGET_RECORD_NUMBER ASC)';
  Exec SQL
    Prepare File_Records_Target From :l_SQL_Statement;
  Exec SQL
    Execute File_Records_Target;

  // Workfile "Records Target" for file &1 in library &2 could not be created
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0055' :
      p_File +
      p_Library :
      'CPF9898' :
      'Error in command : ' + %Trim(l_SQL_Statement)
    );
    Return;
  EndIf;

  // Create Workfile Changes
  EXCCMD_execute_Command(
    'CRTDUPOBJ ' +
    'OBJ(CHANGES) ' +
    'FROMLIB(' + %Trim(l_Rapid_Fire_Home_Library) + ') ' +
    'OBJTYPE(*FILE) ' +
    'TOLIB(' + %Trim(p_Shadow_Library) + ') ' +
    'NEWOBJ(' + %Trim(l_File_Changes) + ') ' +
    'DATA(*NO) ' +
    'CST(*YES)'
  );

  // Workfile Changes for file &1 in library &2 could not be created
  If EXCCMD_Message_Id <> *Blanks;
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0027' :
      p_File +
      p_Library :
      EXCCMD_Message_Id :
      EXCCMD_Message_Data
    );
    Return;
  EndIf;

  // Create Workfile Changes Log
  EXCCMD_execute_Command(
    'CRTDUPOBJ ' +
    'OBJ(CHANGESLOG) ' +
    'FROMLIB(' + %Trim(l_Rapid_Fire_Home_Library) + ') ' +
    'OBJTYPE(*FILE) ' +
    'TOLIB(' + %Trim(p_Shadow_Library) + ') ' +
    'NEWOBJ(' + %Trim(l_File_ChangesLog) + ') ' +
    'DATA(*NO) ' +
    'CST(*YES)'
  );

  // Workfile Changes Log for file &1 in library &2 could not be created
  If EXCCMD_Message_Id <> *Blanks;
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0053' :
      p_File +
      p_Library :
      EXCCMD_Message_Id :
      EXCCMD_Message_Data
    );
    Return;
  EndIf;

  // Create Workfile Duplicate Record Keys
  EXCCMD_execute_Command(
    'CRTDUPOBJ ' +
    'OBJ(DUPRCDKEYS) ' +
    'FROMLIB(' + %Trim(l_Rapid_Fire_Home_Library) + ') ' +
    'OBJTYPE(*FILE) ' +
    'TOLIB(' + %Trim(p_Shadow_Library) + ') ' +
    'NEWOBJ(' + %Trim(l_File_Duplicate_Record_Keys) + ') ' +
    'DATA(*NO) ' +
    'CST(*YES)'
  );

  // Workfile Duplicate Record Keys for file &1 in library &2 could not be created
  If EXCCMD_Message_Id <> *Blanks;
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0064' :
      p_File +
      p_Library :
      EXCCMD_Message_Id :
      EXCCMD_Message_Data
    );
    Return;
  EndIf;

  // Store workfiles
  Exec SQL
    Update Areas
    Set Workfile_Records = :l_File_Records,
        Workfile_Records_Target = :l_File_Records_Target,
        Workfile_Changes = :l_File_Changes,
        Workfile_ChangesLog = :l_File_ChangesLog,
        Workfile_Duplicate_Record_Keys = :l_File_Duplicate_Record_Keys
    Where Job = :p_Job And
          Position = :p_Position And
          Area = :p_Area;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

END-PROC create_Work_Files;

//*=========================================================================================*
//* Promote                                                                                 *
//*=========================================================================================*

DCL-PROC PROMOTER_promote EXPORT;

  DCL-PI PROMOTER_promote;
    p_Job
    CHAR(10) Const;
    p_File_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Force_Ratio
        CHAR(10);

  // Retrieve file data
  Exec SQL
    Select * Into :Files
    From Files_With_Shadow_Library
    Where Job = :p_Job And
          Library = :p_File_Library And
          File = :p_File;
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);

    // Data for file &1 in library &2 could not be retrieved
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0046' :
      p_File +
      p_File_Library :
      ERRC0100.Exeption_Id :
      ERRC0100.Exeption_Data
    );

  Else;

    DoW g_Loop;

      // Get force ratio value of file
      l_Force_Ratio = get_Force_Ratio(
        p_Job :
        Files.Shadow_Library :
        Files.File
      );
      If RAPIDFIRE_is_Error(p_Job);
        Leave;
      EndIf;

      // Set force ratio value of file to *NONE
      If l_Force_Ratio <> '*NONE';
        set_Force_Ratio(
          p_Job :
          Files.Shadow_Library :
          Files.File :
          '*NONE'
        );
        If RAPIDFIRE_is_Error(p_Job);
          Leave;
        EndIf;
      EndIf;

      // Copy data
      call_Copy_Program(
        '*COPY' :
        Files.Job :
        Files.Position :
        Files.Area :
        Files.Library :
        Files.Shadow_Library :
        Files.File :
        Files.Workfile_Records :
        Files.Workfile_Records_Target :
        Files.Workfile_Changes :
        Files.Workfile_ChangesLog :
        Files.Workfile_Duplicate_Record_Keys :
        Files.Copy_Program_Library :
        Files.Copy_Program
      );
      If RAPIDFIRE_is_Error(p_Job);
        Leave;
      EndIf;

      // Set force ratio value of file to the origin value
      If l_Force_Ratio <> '*NONE';
        set_Force_Ratio(
          p_Job :
          Files.Shadow_Library :
          Files.File :
          l_Force_Ratio
        );
        If RAPIDFIRE_is_Error(p_Job);
          Leave;
        EndIf;
      EndIf;

      // Leave
      Leave;

    EndDo;

  EndIf;

END-PROC PROMOTER_promote;

//*=========================================================================================*
//* Get Force Ratio                                                                         *
//*=========================================================================================*

DCL-PROC get_Force_Ratio;

  DCL-PI get_Force_Ratio
         CHAR(10);
    p_Job
    CHAR(10) Const;
    p_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Force_Ratio
        CHAR(10);

  // Allocate heap storage
  MBRD0300_A_Pointer = %Alloc(%Len(MBRD0300_A));

  // Get number of bytes needed
  Reset ERRC0100;
  QUSRMBRD(
    MBRD0300_A :       // <-- Receiver variable
    %Len(MBRD0300_A) : // --> Length of receiver variable
    'MBRD0300' :       // --> Format name
    p_File +
    p_Library :        // --> Qualified database file name
    '*FIRST' :         // --> Database member name
    '0' :              // --> Override processing
    ERRC0100           // <-> Error code
  );

  If ERRC0100.Exeption_Id <> *Blanks;

    // The description file &1 in library &2 could not be retrieved
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0028' :
      p_File +
      p_Library :
      ERRC0100.Exeption_Id :
      ERRC0100.Exeption_Data
    );

    // Return force ratio
    Return *Blanks;

  Else;

    // Set force ratio
    If MBRD0300_A.Records_To_Force_A_Write = *Zero;
      l_Force_Ratio = '*NONE';
    Else;
      l_Force_Ratio = %Char(MBRD0300_A.Records_To_Force_A_Write);
    EndIf;

    // Deallocate heap storate
    DeAlloc MBRD0300_A_Pointer;

    // Return force ratio
    Return l_Force_Ratio;

  EndIf;

END-PROC get_Force_Ratio;

//*=========================================================================================*
//* Set Force Ratio                                                                         *
//*=========================================================================================*

DCL-PROC set_Force_Ratio;

  DCL-PI set_Force_Ratio;
    p_Job
    CHAR(10) Const;
    p_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
    p_Force_Ratio
    CHAR(10) Const;
  END-PI;

  // Set force ratio value of file
  EXCCMD_execute_Command(
    'CHGPF ' +
    'FILE(' + %Trim(p_Library) + '/' +
              %Trim(p_File) + ') ' +
    'FRCRATIO(' + %Trim(p_Force_Ratio) + ')'
  );

  // The parameter FRCRATIO in file &1 in library &2 could not be set to the value &3
  If EXCCMD_Message_Id <> *Blanks;
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0029' :
      p_File +
      p_Library +
      p_Force_Ratio :
      EXCCMD_Message_Id :
      EXCCMD_Message_Data
    );
    Return;
  EndIf;

END-PROC set_Force_Ratio;

//*=========================================================================================*
//* Apply Changes                                                                           *
//*=========================================================================================*

DCL-PROC PROMOTER_apply_Changes EXPORT;

  DCL-PI PROMOTER_apply_Changes;
    p_Job
    CHAR(10) Const;
    p_File_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
  END-PI;

  // Retrieve file data
  Exec SQL
    Select * Into :Files
    From Files_With_Shadow_Library
    Where Job = :p_Job And
          Library = :p_File_Library And
          File = :p_File;
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);

    // Data for file &1 in library &2 could not be retrieved
    RAPIDFIRE_log_Error(
      p_Job :
      'MSG0046' :
      p_File +
      p_File_Library :
      ERRC0100.Exeption_Id :
      ERRC0100.Exeption_Data
    );

  Else;

    // Apply changes
    call_Copy_Program(
      '*APPLY' :
      Files.Job :
      Files.Position :
      Files.Area :
      Files.Library :
      Files.Shadow_Library :
      Files.File :
      Files.Workfile_Records :
      Files.Workfile_Records_Target :
      Files.Workfile_Changes :
      Files.Workfile_ChangesLog :
      Files.Workfile_Duplicate_Record_Keys :
      Files.Copy_Program_Library :
      Files.Copy_Program
    );
    If RAPIDFIRE_is_Error(p_Job);
      // ...
    EndIf;

    // Shall job end?
    Exec SQL
      Select Status Into :g_Status
      From Jobs
      Where Job = :p_Job;
    setIgnoreWarnings();
    If Not (SqlCheckState(sqlState) And g_Status = '*RUN');

      // Update changes applied
      Exec SQL
        Update Areas
        Set Changes_Applied = Changes_To_Apply
        Where Job = :p_Job And
              Position = :Files.Position And
              Area = :Files.Area;
      setIgnoreWarnings();
      SqlCheckState(sqlState);

    EndIf;

  EndIf;

END-PROC PROMOTER_apply_Changes;

//*=========================================================================================*
//* Call Copy Program                                                                       *
//*=========================================================================================*

DCL-PROC call_Copy_Program;

  DCL-PI call_Copy_Program;
    p_Mode
    CHAR(10) Const;
    p_Job
    CHAR(10) Const;
    p_Position
    ZONED(6) Const;
    p_Area
    CHAR(10) Const;
    p_Library
    CHAR(10) Const;
    p_Shadow_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
    p_Workfile_Records
    CHAR(10) Const;
    p_Workfile_Records_Target
    CHAR(10) Const;
    p_Workfile_Changes
    CHAR(10) Const;
    p_Workfile_ChangesLog
    CHAR(10) Const;
    p_Workfile_Duplicate_Record_Keys
    CHAR(10) Const;
    p_Copy_Program_Library
    CHAR(10) Const;
    p_Copy_Program
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Mode
        CHAR(10);
  DCL-S l_Job
        CHAR(10);
  DCL-S l_Position
        ZONED(6);
  DCL-S l_Area
        CHAR(10);
  DCL-S l_Copy_Program
        CHAR(21);
  DCL-S l_Qual_Source
        CHAR(21);
  DCL-S l_Qual_Target
        CHAR(21);
  DCL-S l_Member
        CHAR(10);
  DCL-S l_Qual_Records
        CHAR(21);
  DCL-S l_Qual_Records_Target
        CHAR(21);
  DCL-S l_Qual_Changes
        CHAR(21);
  DCL-S l_Qual_Chg_Log
        CHAR(21);
  DCL-S l_Qual_Duplicate_Record_Keys
        CHAR(21);

  DCL-PR x_Copy_Program EXTPGM(l_Copy_Program);
    l_Mode_ LIKE(l_Mode);
    l_Job_ LIKE(l_Job);
    l_Position_ LIKE(l_Position);
    l_Area_ LIKE(l_Area);
    l_Qual_Source_ LIKE(l_Qual_Source);
    l_Qual_Target_ LIKE(l_Qual_Target);
    l_Member_ LIKE(l_Member);
    l_Qual_Records LIKE(l_Qual_Records);
    l_Qual_Records_Target LIKE(l_Qual_Records_Target);
    l_Qual_Changes LIKE(l_Qual_Changes);
    l_Qual_Chg_Log LIKE(l_Qual_Chg_Log);
    l_Qual_Duplicate_Record_Keys LIKE(l_Qual_Duplicate_Record_Keys);
  END-PR;

  // Prepare copy programm
  If p_Copy_Program = '*GEN';
    l_Copy_Program =
      %Trim(p_Shadow_Library) + '/' +
      %Trim(p_File);
  Else;
    l_Copy_Program =
      %Trim(p_Copy_Program_Library) + '/' +
      %Trim(p_Copy_Program);
  EndIf;

  // Prepare parameters for copy program
  l_Mode = p_Mode;
  l_Job = p_Job;
  l_Position = p_Position;
  l_Area = p_Area;
  l_Qual_Source =
    %Trim(p_Library) + '/' +
    %Trim(p_File);
  l_Qual_Target =
    %Trim(p_Shadow_Library) + '/' +
    %Trim(p_File);
  l_Qual_Records =
    %Trim(p_Shadow_Library) + '/' +
    %Trim(p_Workfile_Records);
  l_Qual_Records_Target =
    %Trim(p_Shadow_Library) + '/' +
    %Trim(p_Workfile_Records_Target);
  l_Qual_Changes =
    %Trim(p_Shadow_Library) + '/' +
    %Trim(p_Workfile_Changes);
  l_Qual_Chg_Log =
    %Trim(p_Shadow_Library) + '/' +
    %Trim(p_Workfile_ChangesLog);
  l_Qual_Duplicate_Record_Keys =
    %Trim(p_Shadow_Library) + '/' +
    %Trim(p_Workfile_Duplicate_Record_Keys);

  // Declare Cursor
  Exec SQL
    Declare Members_2 Cursor For
      Select * From Members
      Where Job = :p_Job And
            Position = :p_Position And
            Area = :p_Area
      Order By Job, Position, Area, Member
      For Read Only;

  // Open cursor
  Exec SQL
    Open Members_2;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Members'
      Exec SQL
        Fetch Next
        From Members_2
        Into :Members;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      // Prepare parameters for copy program
      l_Member = Members.Member;

      // Call copy program
      CallP(E) x_Copy_Program (
        l_Mode :
        l_Job :
        l_Position :
        l_Area :
        l_Qual_Source :
        l_Qual_Target :
        l_Member :
        l_Qual_Records :
        l_Qual_Records_Target :
        l_Qual_Changes :
        l_Qual_Chg_Log :
        l_Qual_Duplicate_Record_Keys
      );

      // Copy program for file &1 in library &2 crashed. Mode &3.
      If %Error;
        RAPIDFIRE_log_Error(
          p_Job :
          'MSG0030' :
          p_File +
          p_Library +
          p_Mode :
          *Blanks :
          *Blanks
        );
        Leave;
      Else;
        // Copy program reported an error
        If RAPIDFIRE_is_Error(p_Job);
          Leave;
        EndIf;
      EndIf;

    EndDo;

    // Close cursor
    Exec SQL
      Close Members_2;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC call_Copy_Program;

//*=========================================================================================*
//* Clean                                                                                   *
//*=========================================================================================*

DCL-PROC PROMOTER_clean EXPORT;

  DCL-PI PROMOTER_clean;
    p_Job
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Error CHAR(256);

  // Delete temporary data
  Exec SQL
    Delete
    From RFJobs
    Where Job = :p_Job;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

  Exec SQL
    Delete
    From Members
    Where Job = :p_Job;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

  Exec SQL
    Delete
    From Fields
    Where Job = :p_Job;
  setIgnoreWarnings();
  SqlCheckState(sqlState);

  // Declare Cursor
  Exec SQL
    Declare Files_5 Cursor For
      Select *
      From Files_With_Shadow_Library
      Where Job = :p_Job And
            Type = '*PHYSICAL'
      Order By Job, Position, Area
      For Read Only;

  // Open cursor
  Exec SQL
    Open Files_5;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Files'
      Exec SQL
        Fetch Next
        From Files_5
        Into :Files;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      If Files.Journaling = 'N';

        // End journaling of physical file
        EXCCMD_execute_Command(
          'ENDJRNPF ' +
          'FILE(' + %Trim(Files.Library) + '/' +
                    %Trim(Files.File) + ') ' +
          'JRN(*FILE)'
        );

        // Journaling for file &1 in library &2 could not be ended
        If EXCCMD_Message_Id <> *Blanks;
          RAPIDFIRE_log_Error(
            p_Job :
            'MSG0040' :
            Files.File +
            Files.Library :
            EXCCMD_Message_Id :
            EXCCMD_Message_Data
          );
        EndIf;

      EndIf;

      If Files.SDT_View_Target <> *Blanks And
         Files.SDT_View_Source <> *Blanks;

        // Delete SDT view in environment for "Special Data Types" like LOB and DATALINK
        PRCSDT_deleteEnvironment(
          p_Job :                 // --> Job
          Files.Position :        // --> Position
          Files.Area :            // --> Area
          'N' :                   // --> Delete Field List Target
          'N' :                   // --> Delete Field List Source
          'Y' :                   // --> Delete SDT View Target
          'Y' :                   // --> Delete SDT View Source
          Files.Shadow_Library :  // --> Shadow library
          Files.SDT_View_Target : // --> SDT View Target
          Files.SDT_View_Source : // --> SDT View Source
          l_Error                 // <-- Error
        );

      EndIf;

    EndDo;

    // Close cursor
    Exec SQL
      Close Files_5;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC PROMOTER_clean;

//*=========================================================================================*
//* Create user space                                                                       *
//*=========================================================================================*

DCL-PROC UserSpace_create EXPORT;

  DCL-PI *N LikeDs(ERRC0100);
    i_qUsrSpc CHAR(20) Const;
  END-PI;

  DCL-DS errorCode LikeDs(ERRC0100) Inz(*LikeDs);

  Reset errorCode;
  QUSCRTUS(
    i_qUsrSpc :    // --> Qualified user space name
    *Blanks :      // --> Extended attribute
    16384 :        // --> Initial size
    '0' :          // --> Initial value
    '*ALL' :       // --> Public authority
    *Blanks :      // --> Text description
    '*YES' :       // --> Replace
    errorCode      // <-> Error code
  );

  Return errorCode;

END-PROC;

//*=========================================================================================*
//* Delete user space.                                                                      *
//*=========================================================================================*

DCL-PROC UserSpace_delete EXPORT;

  DCL-PI *N LikeDs(ERRC0100);
    i_qUsrSpc CHAR(20) Const;
  END-PI;

  DCL-DS errorCode LikeDs(ERRC0100) Inz(*LikeDs);

  Reset errorCode;
  QUSDLTUS(
    i_qUsrSpc :  // --> Qualified user space name
    errorCode    // <-> Error code
  );

  Return errorCode;

END-PROC;

//*=========================================================================================*
//* Retrieve user space pointer.                                                            *
//*=========================================================================================*

DCL-PROC UserSpace_retrievePointer EXPORT;

  DCL-PI *N LikeDs(ERRC0100);
    i_qUsrSpc CHAR(20) Const;
    o_pUsrSpc POINTER;
  END-PI;

  DCL-DS errorCode LikeDs(ERRC0100) Inz(*LikeDs);

  Reset errorCode;
  QUSPTRUS(
    i_qUsrSpc :            // --> Qualified user space name
    g_User_Space_Pointer : // <-- Return pointer
    errorCode              // <-> Error code
  );

  Return errorCode;

END-PROC;

//*=========================================================================================*
//* Reapply All Changes                                                                     *
//*=========================================================================================*

DCL-PROC PROMOTER_reapply_All_Changes EXPORT;

  DCL-PI PROMOTER_reapply_All_Changes;
    p_Job
    CHAR(10) Const;
    p_Position
    ZONED(6) Const;
    p_Area
    CHAR(10) Const;
  END-PI;

  // Locale field declarations

  DCL-S l_Shadow_Library
        CHAR(10);
  DCL-S l_Workfile_Changes
        CHAR(10);
  DCL-S l_Workfile_ChangesLog
        CHAR(10);
  DCL-S l_Insert
        CHAR(256);
  DCL-S l_Inserted_Rows
        PACKED(10);

  Exec SQL
    Select Shadow_Library, Workfile_Changes, Workfile_ChangesLog
    Into :l_Shadow_Library, :l_Workfile_Changes, :l_Workfile_ChangesLog
    From Files_With_Shadow_Library
    Where Job = :p_Job And
          Position = :p_Position And
          Area = :p_Area;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    l_Insert =
      'Insert Into ' +
        quote(%Trim(l_Shadow_Library)) + '/' +
        quote(%Trim(l_Workfile_Changes)) +
      ' (Member, Record_Number) ' +
      'Select Member, Record_Number ' +
      'From ' +
        quote(%Trim(l_Shadow_Library)) + '/' +
        quote(%Trim(l_Workfile_ChangesLog));
    Exec SQL
      Prepare Changes From :l_Insert;
    Exec SQL
      Execute Changes;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

    Exec SQL
      GET DIAGNOSTICS :l_Inserted_Rows = ROW_COUNT;

    Exec SQL
      Update Areas
      Set Changes_To_Apply = :l_Inserted_Rows,
          Changes_Applied = 0
      Where Job = :p_Job And
            Position = :p_Position And
            Area = :p_Area;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

END-PROC PROMOTER_reapply_All_Changes;

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
//* Returns the "field copy mode"                                                           *
//*=========================================================================================*

DCL-PROC getFieldCopyMode;

  DCL-PI *N INT(10);
    p_conversion_Program
    CHAR(10) Const;
    p_srcFormatLevelId
    CHAR(13) Const;
    p_tgtFormatLevelId
    CHAR(13) Const;
  END-PI;

  If p_Conversion_Program <> '*NONE' or
     p_srcFormatLevelId <> p_tgtFormatLevelId;
    Return GEN_LVL_FIELD_COPY;
  Else;
    Return GEN_LVL_RECORD_COPY;
  Endif;

END-PROC;

//*=========================================================================================*
//* Returns the prefix for the source file fields.                                          *
//*=========================================================================================*

DCL-PROC getSourceFieldPrefix;

  DCL-PI *N CHAR(2);
    p_gen_Level
    INT(10) Const;
  END-PI;

  If (p_gen_Level = GEN_LVL_FIELD_COPY);
    Return SOURCE_PREFIX;
  Else;
    Return '';
  Endif;

END-PROC;

//*=========================================================================================*
//* Returns the prefix for the target file fields.                                          *
//*=========================================================================================*

DCL-PROC getTargetFieldPrefix;

  DCL-PI *N CHAR(2);
    p_gen_Level
    INT(10) Const;
  END-PI;

  If (p_gen_Level = GEN_LVL_FIELD_COPY);
    Return TARGET_PREFIX;
  Else;
    Return '';
  Endif;

END-PROC;

//*=========================================================================================*
//* Get Source Field Prefix Of Last Generated Copy Program                                  *
//*=========================================================================================*

DCL-PROC PROMOTER_get_Source_Field_Prefix_Of_Last_Generated_Copy_Program EXPORT;

  DCL-PI PROMOTER_get_Source_Field_Prefix_Of_Last_Generated_Copy_Program CHAR(2);
  END-PI;

  Return g_src_Pfx;

END-PROC PROMOTER_get_Source_Field_Prefix_Of_Last_Generated_Copy_Program;

//*=========================================================================================*
//* Get Target Field Prefix Of Last Generated Copy Program                                  *
//*=========================================================================================*

DCL-PROC PROMOTER_get_Target_Field_Prefix_Of_Last_Generated_Copy_Program EXPORT;

  DCL-PI PROMOTER_get_Target_Field_Prefix_Of_Last_Generated_Copy_Program CHAR(2);
  END-PI;

  Return g_tgt_Pfx;

END-PROC PROMOTER_get_Target_Field_Prefix_Of_Last_Generated_Copy_Program;

//*=========================================================================================*
//* Get Source Field Prefix                                                                 *
//*=========================================================================================*

DCL-PROC PROMOTER_get_Source_Field_Prefix EXPORT;

  DCL-PI PROMOTER_get_Source_Field_Prefix CHAR(2);
    p_Conversion_Program
    CHAR(10) Const;
    p_SrcLibrary
    CHAR(10) Const;
    p_SrcFile
    CHAR(10) Const;
    p_TgtLibrary
    CHAR(10) Const;
    p_TgtFile
    CHAR(10) Const;
    l_Src_Pfx
    CHAR(2);
  END-PI;

  DCL-S l_srcFormatLevelId Char(13);
  DCL-S l_tgtFormatLevelId Char(13);
  DCL-S l_gen_Level INT(10);

  RAPIDFIRE_set_Error(RAPIDFIRE_JOB_NONE: *off);

  l_srcFormatLevelId = getRecordFormatLevelId(p_SrcLibrary: p_SrcFile);
  l_tgtFormatLevelId = getRecordFormatLevelId(p_TgtLibrary: p_TgtFile);

  If (l_srcFormatLevelId <> '' and l_tgtFormatLevelId <> '');
    l_gen_Level = getFieldCopyMode(p_Conversion_Program: l_srcFormatLevelId: l_tgtFormatLevelId);
    l_Src_Pfx = getSourceFieldPrefix(l_gen_Level);
  EndIf;

  Return l_Src_Pfx;

END-PROC PROMOTER_get_Source_Field_Prefix;

//*=========================================================================================*
//* Get Target Field Prefix                                                                 *
//*=========================================================================================*

DCL-PROC PROMOTER_get_Target_Field_Prefix EXPORT;

  DCL-PI PROMOTER_get_Target_Field_Prefix CHAR(2);
    p_Conversion_Program
    CHAR(10) Const;
    p_SrcLibrary
    CHAR(10) Const;
    p_SrcFile
    CHAR(10) Const;
    p_TgtLibrary
    CHAR(10) Const;
    p_TgtFile
    CHAR(10) Const;
    l_Tgt_Pfx
    CHAR(2);
  END-PI;

  DCL-S l_srcFormatLevelId Char(13);
  DCL-S l_tgtFormatLevelId Char(13);
  DCL-S l_gen_Level INT(10);

  RAPIDFIRE_set_Error(RAPIDFIRE_JOB_NONE: *off);

  l_srcFormatLevelId = getRecordFormatLevelId(p_SrcLibrary: p_SrcFile);
  l_tgtFormatLevelId = getRecordFormatLevelId(p_TgtLibrary: p_TgtFile);

  If (l_srcFormatLevelId <> '' and l_tgtFormatLevelId <> '');
    l_gen_Level = getFieldCopyMode(p_Conversion_Program: l_srcFormatLevelId: l_tgtFormatLevelId);
    l_Tgt_Pfx = getTargetFieldPrefix(l_gen_Level);
  EndIf;

  Return l_Tgt_Pfx;

END-PROC PROMOTER_get_Target_Field_Prefix;

//*=========================================================================================*
//* Get Record FormatLevel ID.                                                              *
//*=========================================================================================*

DCL-PROC getRecordFormatLevelId;

  DCL-PI getRecordFormatLevelId CHAR(13);
    p_Library
    CHAR(10) Const;
    p_File
    CHAR(10) Const;
  END-PI;

  DCL-S l_Record_Format CHAR(10);
  DCL-S l_hFields Like(fldAttrs_handle_t) inz(*null);
  DCL-S l_FormatLevelId Char(13);
  DCL-C USRSPC_PROMOTER2 'PROMOTER2 QTEMP     ';

  DCL-S l_User_Space_Pointer Pointer Inz(*null);

  DoU '1';

    EXCCMD_execute_Command(
      'CHKOBJ ' +
      'OBJ(QTEMP/PROMOTER2) ' +
      'OBJTYPE(*USRSPC)'
    );

    // User space does not exit
    If EXCCMD_Message_Id <> *Blanks;
      UserSpace_create(USRSPC_PROMOTER2);
    EndIf;

    l_User_Space_Pointer = g_User_Space_Pointer;
    UserSpace_retrievePointer(
      USRSPC_PROMOTER2: g_User_Space_Pointer);

    l_Record_Format = get_Record_Format(
      RAPIDFIRE_JOB_NONE :
      p_Library :
      p_File :
      USRSPC_PROMOTER2
    );

    If (RAPIDFIRE_is_Error(RAPIDFIRE_JOB_NONE));
      Leave; // Cleanup & Exit
    Endif;

    l_hFields = FieldAttributes_new(p_File: p_Library:
                                       l_Record_Format: ERRC0100);
    If (FieldAttributes_isNull(l_hFields));
      Leave; // Cleanup & Exit
    EndIf;

    l_FormatLevelId = FieldAttributes_getFormatLevelId(l_hFields);

  EndDo;

  If (l_User_Space_Pointer <> *null);
    g_User_Space_Pointer = l_User_Space_Pointer;
  EndIf;

  If (l_hFields <> *null);
    FieldAttributes_delete(l_hFields);
    l_hFields = *null;
  EndIf;

  Return l_FormatLevelId;

END-PROC getRecordFormatLevelId;


