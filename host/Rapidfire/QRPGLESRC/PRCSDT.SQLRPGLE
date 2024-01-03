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
//* Process special data types                                                              *
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

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,') AlwNull(*USRCTL);
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,PRCSDT
/Include QCPYSRC,FLDATTRS
/Include QCPYSRC,SQLSTS
/Include QCPYSRC,ERRC0100

// Global field declarations

DCL-S g_Loop IND Inz(*On);

//*-----------------------------------------------------------------------------------------*

// Set SQL options

Exec SQL
  Set Option
  DatFmt=*ISO,
  TimFmt=*ISO,
  Commit=*NONE,
  CloSQLCsr=*ENDMOD;

//*=========================================================================================*
//* Create Field List                                                                       *
//*=========================================================================================*

DCL-PROC PRCSDT_createFieldList EXPORT;

  DCL-PI PRCSDT_createFieldList IND; // <-- Success
    p_Job CHAR(10) Const;            // --> Job
    p_Position ZONED(6 : 0) Const;   // --> Position
    p_Area CHAR(10) Const;           // --> Area
    p_Source_Target CHAR(1) Const;   // --> Source/Target
    p_Library CHAR(10) Const;        // --> Library
    p_File CHAR(10) Const;           // --> File
    p_Error CHAR(1);                 // <-- Error
  END-PI;

  // Locale field declarations
  DCL-S l_hDSFields Like(fldAttrs_handle_t);
  DCL-DS l_dsField LikeDs(flda0300_t);
  DCL-S l_rcdFmt CHAR(10);
  DCL-S l_fmtLvlId CHAR(13);
  DCL-S l_field CHAR(10);
  DCL-S l_specialDataType CHAR(1);
  DCL-S l_defaultValue CHAR(1);
  DCL-S l_sequence ZONED(4 : 0);

  // Initialize error
  p_Error = 'N';

  // Initialize field lists
  l_hDSFields = *Null;

  // Produce field list of file
  l_hDSFields =
    FieldAttributes_new(
      p_File :
      p_Library :
      FLDATTRS_RCDFMT_FIRST :
      ERRC0100
    );

  If FieldAttributes_isNull(l_hDSFields);

    p_Error = '1';

  Else;

    // Write field list of file

    l_rcdFmt = FieldAttributes_getRecordFormat(l_hDSFields);

    l_fmtLvlId = FieldAttributes_getFormatLevelId(l_hDSFields);

    l_sequence = *Zero;

    FieldAttributes_rewind(l_hDSFields);

    Dow
      FieldAttributes_retrieve(
        l_hDSFields :
        FLDATTRS_FLD_NEXT :
        l_dsField :
        'FLDA0300'
      ) > 0;

      l_sequence += 1;

      l_field = l_dsField.name;

      If l_dsField.type = FLDATTRS_TYPE_LOB Or
         l_dsField.type = FLDATTRS_TYPE_DATALINK;
        l_specialDataType = 'Y';
      Else;
        l_specialDataType = 'N';
      EndIf;

      If l_dsField.alwNull And l_dsField.isNullDft;
        l_defaultValue = 'Y';
      Else;
        l_defaultValue = 'N';
      EndIf;

      Exec SQL
        Insert Into Fields
          (
           Job,
           Position,
           Area,
           Source_Target,
           Sequence,
           Record_Format,
           Format_Level_Id,
           Field,
           Special_Data_Type,
           Default_Value
          )
          Values (
           :p_Job,
           :p_Position,
           :p_Area,
           :p_Source_Target,
           :l_sequence,
           :l_rcdFmt,
           :l_fmtLvlId,
           :l_field,
           :l_specialDataType,
           :l_defaultValue
          );
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        p_Error = '2';
        Leave;
      EndIf;

    EndDo;

    If p_Error = 'N';

    EndIf;

  EndIf;

  // Delete field list of file
  If l_hDSFields <> *Null;
    FieldAttributes_delete(l_hDSFields);
    l_hDSFields = *Null;
  EndIf;

  // Return success
  If p_Error = 'N';
    Return *On;
  Else;
    Return *Off;
  EndIf;

END-PROC PRCSDT_createFieldList;

//*=========================================================================================*
//* Delete Field List                                                                       *
//*=========================================================================================*

DCL-PROC PRCSDT_deleteFieldList EXPORT;

  DCL-PI PRCSDT_deleteFieldList IND; // <-- Success
    p_Job CHAR(10) Const;            // --> Job
    p_Position ZONED(6 : 0) Const;   // --> Position
    p_Area CHAR(10) Const;           // --> Area
    p_Source_Target CHAR(1) Const;   // --> Source/Target
    p_Error CHAR(1);                 // <-- Error
  END-PI;

  Exec SQL
    Delete From Fields
    Where Job = :p_Job And
          Position = :p_Position And
          Area = :p_Area And
          Source_Target = :p_Source_Target;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);
    p_Error = 'N';
    Return *On;
  Else;
    p_Error = 'Y';
    Return *Off;
  EndIf;

END-PROC PRCSDT_deleteFieldList;

//*=========================================================================================*
//* Contains SDT Fields                                                                     *
//*=========================================================================================*

DCL-PROC PRCSDT_containsSDTFields EXPORT;

  DCL-PI PRCSDT_containsSDTFields IND; // <-- Result
    p_Job CHAR(10) Const;              // --> Job
    p_Position ZONED(6 : 0) Const;     // --> Position
    p_Area CHAR(10) Const;             // --> Area
    p_Source_Target CHAR(1) Const;     // --> Source/Target
  END-PI;

  // Locale field declarations
  DCL-S l_SDT ZONED(3 : 0);

  // Does file contains a field with a special data type?
  Exec SQL
    Select Count(*) Into :l_SDT
    From Fields
    Where Job = :p_Job And
          Position = :p_Position And
          Area = :p_Area And
          Source_Target = :p_Source_Target And
          Special_Data_Type = 'Y';
  setIgnoreWarnings();
  If SqlCheckState(sqlState) And l_SDT <> *Zero;
    Return *On;
  Else;
    Return *Off;
  EndIf;

END-PROC PRCSDT_containsSDTFields;

//*=========================================================================================*
//* Does All SDT Fields Have Defaults                                                       *
//*=========================================================================================*

DCL-PROC PRCSDT_doesAllSDTFieldsHaveDefaults EXPORT;

  DCL-PI PRCSDT_doesAllSDTFieldsHaveDefaults IND; // <-- Result
    p_Job CHAR(10) Const;                         // --> Job
    p_Position ZONED(6 : 0) Const;                // --> Position
    p_Area CHAR(10) Const;                        // --> Area
    p_Source_Target CHAR(1) Const;                // --> Source/Target
  END-PI;

  // Locale field declarations
  DCL-S l_SDT ZONED(3 : 0);

  // Does all SDT fields in target file have defaults?
  Exec SQL
    Select Count(*) Into :l_SDT
    From Fields
    Where Job = :p_Job And
          Position = :p_Position And
          Area = :p_Area And
          Source_Target = :p_Source_Target And
          Special_Data_Type = 'Y' And
          Default_Value = 'N';
  setIgnoreWarnings();
  If SqlCheckState(sqlState) And l_SDT = *Zero;
    Return *On;
  Else;
    Return *Off;
  EndIf;

END-PROC PRCSDT_doesAllSDTFieldsHaveDefaults;

//*=========================================================================================*
//* Get SDT View                                                                            *
//*=========================================================================================*

DCL-PROC PRCSDT_getSDTView EXPORT;

  DCL-PI PRCSDT_getSDTView CHAR(10); // <-- SDT View
    p_Job CHAR(10) Const;            // --> Job
    p_Position ZONED(6 : 0) Const;   // --> Position
    p_Area CHAR(10) Const;           // --> Area
    p_Source_Target CHAR(1) Const;   // --> Source/Target
    p_File CHAR(10) Const;           // --> File
  END-PI;

  Return
    '$V' +
    p_Source_Target +
    %Trim(%SubSt(p_File : 1 : 4)) +
    %SubSt(%EditC(p_Position : 'X') : %Len(p_Position) - 3 + 1 : 3);

END-PROC PRCSDT_getSDTView;

//*=========================================================================================*
//* Create Special Data Type View                                                           *
//*=========================================================================================*

DCL-PROC PRCSDT_createSDTView EXPORT;

  DCL-PI PRCSDT_createSDTView IND;      // <-- Success
    p_Job CHAR(10) Const;               // --> Job
    p_Position ZONED(6 : 0) Const;      // --> Position
    p_Area CHAR(10) Const;              // --> Area
    p_Source_Target CHAR(1) Const;      // --> Source/Target
    p_Library CHAR(10) Const;           // --> Library
    p_File CHAR(10) Const;              // --> File
    p_Shadow_Library CHAR(10) Const;    // --> Shadow library
    p_SDTView CHAR(10) Const;           // --> SDT View
    p_SDTView_Record_Format CHAR(10);   // <-- SDT View Record Format
    p_SDTView_Format_Level_Id CHAR(13); // <-- SDT View Format Level Id.
    p_Error CHAR(1);                    // <-- Error
  END-PI;

  // Locale field declarations
  DCL-S l_Fields VARCHAR(4096);
  DCL-S l_SQLStatement VARCHAR(4096);
  DCL-S l_SDTView_Record_Format CHAR(10);
  DCL-S l_SDTView_Format_Level_Id CHAR(13);
  DCL-S l_Field CHAR(10);
  DCL-S l_hDSFields Like(fldAttrs_handle_t);
  DCL-DS l_dsField LikeDs(flda0300_t);

  // Initialize error
  p_Error = 'N';

  // Initialise fields
  l_Fields = '';

  // Declare Cursor
  Exec SQL
    Declare Fields Cursor For
      Select Record_Format, Format_Level_Id, Field
      From Fields
      Where Job = :p_Job And
            Position = :p_Position And
            Area = :p_Area And
            Source_Target = :p_Source_Target And
            Special_Data_Type = 'N'
      Order By Job, Position, Area, Source_Target, Sequence
      For Read Only;

  // Open cursor
  Exec SQL
    Open Fields;
  setIgnoreWarnings();
  If SqlCheckState(sqlState);

    DoW g_Loop;

      // Read 'Fields'
      Exec SQL
        Fetch Next
        From Fields
        Into :l_SDTView_Record_Format,
             :l_SDTView_Format_Level_Id,
             :l_Field;

      // End of file
      setIgnoreWarnings();
      If Not SqlCheckState(sqlState);
        Leave;
      EndIf;

      // Set fields
      If l_Fields <> '';
        l_Fields += ', ';
      EndIf;
      l_Fields += '"' + %Trim(l_Field) + '"';

    EndDo;

    // Close cursor
    Exec SQL
      Close Fields;
    setIgnoreWarnings();
    SqlCheckState(sqlState);

  EndIf;

  // Create SQL Statement
  l_SQLStatement =
    'CREATE VIEW ' +
    '"' + %Trim(p_Shadow_Library) + '"' + '/' +
    '"' + %Trim(p_SDTView) + '" ' +
    'AS ' +
    '(' +
    'SELECT ' +
    l_Fields + ' ' +
    'FROM ' +
    '"' + %Trim(p_Library) + '"' + '/' +
    '"' + %Trim(p_File) + '"' +
    ') ' +
    'RCDFMT ' + '"' + %Trim(l_SDTView_Record_Format) + '"';

  // Execute SQL Statement
  Exec SQL
    Prepare SDTView From :l_SQLStatement;
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);
    p_Error = '1';
  Else;
    Exec SQL
      Execute SDTView;
    setIgnoreWarnings();
    If Not SqlCheckState(sqlState);
      p_Error = '2';
    EndIf;
  EndIf;

  // Get record format and format level id.
  If p_Error = 'N';
    l_hDSFields =
      FieldAttributes_new(
        p_SDTView :
        p_Shadow_Library :
        FLDATTRS_RCDFMT_FIRST :
        ERRC0100
      );
    If FieldAttributes_isNull(l_hDSFields);
      p_Error = '3';
    Else;
      p_SDTView_Record_Format =
        FieldAttributes_getRecordFormat(l_hDSFields);
      p_SDTView_Format_Level_Id =
        FieldAttributes_getFormatLevelId(l_hDSFields);
    EndIf;
  EndIf;

  // Return success
  If p_Error = 'N';
    Return *On;
  Else;
    Return *Off;
  EndIf;

END-PROC PRCSDT_createSDTView;

//*=========================================================================================*
//* Delete Special Data Type View                                                           *
//*=========================================================================================*

DCL-PROC PRCSDT_deleteSDTView EXPORT;

  DCL-PI PRCSDT_deleteSDTView IND;   // <-- Success
    p_Job CHAR(10) Const;            // --> Job
    p_Position ZONED(6 : 0) Const;   // --> Position
    p_Area CHAR(10) Const;           // --> Area
    p_Source_Target CHAR(1) Const;   // --> Source/Target
    p_Shadow_Library CHAR(10) Const; // --> Shadow library
    p_SDTView CHAR(10) Const;        // --> SDT View
    p_Error CHAR(1);                 // <-- Error
  END-PI;

  // Locale field declarations
  DCL-S l_SQLStatement VARCHAR(256);

  // Initialize error
  p_Error = 'N';

  // Create SQL Statement
  l_SQLStatement =
    'DROP VIEW ' +
    '"' + %Trim(p_Shadow_Library) + '"' + '/' +
    '"' + %Trim(p_SDTView) + '"';

  // Execute SQL Statement
  Exec SQL
    Prepare SDTView From :l_SQLStatement;
  setIgnoreWarnings();
  If Not SqlCheckState(sqlState);
    p_Error = '1';
  Else;
    Exec SQL
      Execute SDTView;
    setIgnoreWarnings();
    If Not SqlCheckState(sqlState);
      p_Error = '2';
    EndIf;
  EndIf;

  // Return success
  If p_Error = 'N';
    Return *On;
  Else;
    Return *Off;
  EndIf;

END-PROC PRCSDT_deleteSDTView;

//*=========================================================================================*
//* Create Environment                                                                      *
//*=========================================================================================*

DCL-PROC PRCSDT_createEnvironment EXPORT;

  DCL-PI PRCSDT_createEnvironment IND;     // <-- Success
    p_Job CHAR(10) Const;                  // --> Job
    p_Position ZONED(6 : 0) Const;         // --> Position
    p_Area CHAR(10) Const;                 // --> Area
    p_Library CHAR(10) Const;              // --> Library
    p_Shadow_Library CHAR(10) Const;       // --> Shadow library
    p_File CHAR(10) Const;                 // --> File
    p_SDTViewProcessing CHAR(1) Const;     // --> SDT View Processing
    p_SDTViewTarget CHAR(10);              // <-- SDT View Target
    p_SDTViewRecordFormatTarget CHAR(10);  // <-- SDT View Record Format Target
    p_SDTViewFormatLevelIdTarget CHAR(13); // <-- SDT View Format Level Id Target
    p_SDTViewSource CHAR(10);              // <-- SDT View Source
    p_SDTViewRecordFormatSource CHAR(10);  // <-- SDT View Record Format Source
    p_SDTViewFormatLevelIdSource CHAR(13); // <-- SDT View Format Level Id Source
    p_Error CHAR(256);                     // <-- Error
  END-PI;

  DCL-S l_Error CHAR(1);
  DCL-S l_createFieldListTarget CHAR(1);
  DCL-S l_createFieldListSource CHAR(1);
  DCL-S l_createSDTViewTarget CHAR(1);
  DCL-S l_createSDTViewSource CHAR(1);
  DCL-S l_SDTViewTarget CHAR(10);
  DCL-S l_SDTViewRecordFormatTarget CHAR(10);
  DCL-S l_SDTViewFormatLevelIdTarget CHAR(13);
  DCL-S l_SDTViewSource CHAR(10);
  DCL-S l_SDTViewRecordFormatSource CHAR(10);
  DCL-S l_SDTViewFormatLevelIdSource CHAR(13);
  DCL-S l_ErrorXXX CHAR(256);

  // Initialize return fields
  p_SDTViewTarget = '*NONE';
  p_SDTViewRecordFormatTarget = *Blanks;
  p_SDTViewFormatLevelIdTarget = *Blanks;
  p_SDTViewSource = '*NONE';
  p_SDTViewRecordFormatSource = *Blanks;
  p_SDTViewFormatLevelIdSource = *Blanks;
  p_Error = *Blanks;

  // Initialize working fields
  l_createFieldListTarget = 'N';
  l_createFieldListSource = 'N';
  l_createSDTViewTarget = 'N';
  l_createSDTViewSource = 'N';

  DoU 1 = 1;

    // Create field list for target file
    If PRCSDT_createFieldList(
      p_Job :
      p_Position :
      p_Area :
      'T' :
      p_Shadow_Library :
      p_File :
      l_Error
    );
      l_createFieldListTarget = 'Y';
    Else;
      p_Error =
        'Could not create field list for ' +
        'file ' + %Trim(p_File) + ' in library ' + %Trim(p_Shadow_Library) + '. ' +
        'Reason code: ' + %Trim(l_Error) + '.';
      Leave;
    EndIf;

    // Create field list for source file
    If PRCSDT_createFieldList(
      p_Job :
      p_Position :
      p_Area :
      'S' :
      p_Library :
      p_File :
      l_Error
    );
      l_createFieldListSource = 'Y';
    Else;
      p_Error =
        'Could not create field list for ' +
        'file ' + %Trim(p_File) + ' in library ' + %Trim(p_Library) + '. ' +
        'Reason code: ' + %Trim(l_Error) + '.';
      Leave;
    EndIf;

    If p_SDTViewProcessing = 'Y';

      // Check, if target file contains SDT fields
      If PRCSDT_containsSDTFields(
        p_Job :
        p_Position :
        p_Area :
        'T'
      );

        // Check, if all SDT fields in target file have defaults
        If Not PRCSDT_doesAllSDTFieldsHaveDefaults(
          p_Job :
          p_Position :
          p_Area :
          'T'
        );
          p_Error =
            'Not all SDT fields (LOB, DATALINK ... ) ' +
            'in file ' + %Trim(p_File) + ' in library ' + %Trim(p_Shadow_Library) + ' ' +
            'have defaults.';
          Leave;
        EndIf;

        // Get name of SDT View for target file
        l_SDTViewTarget =
          PRCSDT_getSDTView(
            p_Job :
            p_Position :
            p_Area :
            'T' :
            p_File
          );

        // Create SDT View for target file
        If PRCSDT_createSDTView(
          p_Job :
          p_Position :
          p_Area :
          'T' :
          p_Shadow_Library :
          p_File :
          p_Shadow_Library :
          l_SDTViewTarget :
          l_SDTViewRecordFormatTarget :
          l_SDTViewFormatLevelIdTarget :
          l_Error
        );
          l_createSDTViewTarget = 'Y';
        Else;
          p_Error =
            'Could not create SDT view for ' +
            'file ' + %Trim(p_File) + ' in library ' + %Trim(p_Shadow_Library) + '. ' +
            'Reason code: ' + %Trim(l_Error) + '.';
          Leave;
        EndIf;

      EndIf;

      // Check, if source file contains SDT fields
      If PRCSDT_containsSDTFields(
        p_Job :
        p_Position :
        p_Area :
        'S'
      );

        // Get name of SDT View for source file
        l_SDTViewSource =
          PRCSDT_getSDTView(
            p_Job :
            p_Position :
            p_Area :
            'S' :
            p_File
          );

        // Create SDT View for source file
        If PRCSDT_createSDTView(
          p_Job :
          p_Position :
          p_Area :
          'S' :
          p_Library :
          p_File :
          p_Shadow_Library :
          l_SDTViewSource :
          l_SDTViewRecordFormatSource :
          l_SDTViewFormatLevelIdSource :
          l_Error
        );
          l_createSDTViewSource = 'Y';
        Else;
          p_Error =
            'Could not create SDT view for ' +
            'file ' + %Trim(p_File) + ' in library ' + %Trim(p_Library) + '. ' +
            'Reason code: ' + %Trim(l_Error) + '.';
          Leave;
        EndIf;

      EndIf;

    EndIf;

  EndDo;

  If p_Error = *Blanks;

    // Return SDT Views;
    If l_createSDTViewTarget = 'Y';
      p_SDTViewTarget = l_SDTViewTarget;
      p_SDTViewRecordFormatTarget = l_SDTViewRecordFormatTarget;
      p_SDTViewFormatLevelIdTarget = l_SDTViewFormatLevelIdTarget;
    EndIf;
    If l_createSDTViewSource = 'Y';
      p_SDTViewSource = l_SDTViewSource;
      p_SDTViewRecordFormatSource = l_SDTViewRecordFormatSource;
      p_SDTViewFormatLevelIdSource = l_SDTViewFormatLevelIdSource;
    EndIf;

    // Return with success
    Return *On;

  Else;

    // Delete environment
    PRCSDT_deleteEnvironment(
      p_Job :
      p_Position :
      p_Area :
      l_createFieldListTarget :
      l_createFieldListSource :
      l_createSDTViewTarget :
      l_createSDTViewSource :
      p_Shadow_Library :
      l_SDTViewTarget :
      l_SDTViewSource :
      l_ErrorXXX
    );

    // Return without success
    Return *Off;

  EndIf;

END-PROC PRCSDT_createEnvironment;

//*=========================================================================================*
//* Delete Environment                                                                      *
//*=========================================================================================*

DCL-PROC PRCSDT_deleteEnvironment EXPORT;

  DCL-PI PRCSDT_deleteEnvironment IND;     // <-- Success
    p_Job CHAR(10) Const;                  // --> Job
    p_Position ZONED(6 : 0) Const;         // --> Position
    p_Area CHAR(10) Const;                 // --> Area
    p_DeleteFieldListTarget CHAR(1) Const; // --> Delete Field List Target
    p_DeleteFieldListSource CHAR(1) Const; // --> Delete Field List Source
    p_DeleteSDTViewTarget CHAR(1) Const;   // --> Delete SDT View Target
    p_DeleteSDTViewSource CHAR(1) Const;   // --> Delete SDT View Source
    p_Shadow_Library CHAR(10) Const;       // --> Shadow library
    p_SDTViewTarget CHAR(10) Const;        // --> SDT View Target
    p_SDTViewSource CHAR(10) Const;        // --> SDT View Source
    p_Error CHAR(256);                     // <-- Error
  END-PI;

  DCL-S l_Error CHAR(1);

  // Initialize return fields
  p_Error = *Blanks;

  // Delete SDT View for source file
  If p_DeleteSDTViewSource = 'Y' And
     p_SDTViewSource <> '*NONE';
    If Not PRCSDT_deleteSDTView(
      p_Job :
      p_Position :
      p_Area :
      'S' :
      p_Shadow_Library :
      p_SDTViewSource :
      l_Error
    );
      p_Error = %Trim(p_Error) + ' PRCSDT_deleteSDTView(Source)';
    EndIf;
  EndIf;

  // Delete SDT View for target file
  If p_DeleteSDTViewTarget = 'Y' And
     p_SDTViewTarget <> '*NONE';
    If Not PRCSDT_deleteSDTView(
      p_Job :
      p_Position :
      p_Area :
      'T' :
      p_Shadow_Library :
      p_SDTViewTarget :
      l_Error
    );
      p_Error = %Trim(p_Error) + ' PRCSDT_deleteSDTView(Target)';
    EndIf;
  EndIf;

  // Delete field list for source file
  If p_DeleteFieldListSource = 'Y';
    If Not PRCSDT_deleteFieldList(
      p_Job :
      p_Position :
      p_Area :
      'S' :
      l_Error
    );
      p_Error = %Trim(p_Error) + ' PRCSDT_deleteFieldList(Source)';
    EndIf;
  EndIf;

  // Delete field list for target file
  If p_DeleteFieldListTarget = 'Y';
    If Not PRCSDT_deleteFieldList(
      p_Job :
      p_Position :
      p_Area :
      'T' :
      l_Error
    );
      p_Error = %Trim(p_Error) + ' PRCSDT_deleteFieldList(Target)';
    EndIf;
  EndIf;

  If p_Error = *Blanks;

    // Return with success
    Return *On;

  Else;

    // Complete error text
    p_Error = 'The following procedures detected errors: ' + %Trim(p_Error);

    // Return without success
    Return *Off;

  EndIf;

END-PROC PRCSDT_deleteEnvironment;
