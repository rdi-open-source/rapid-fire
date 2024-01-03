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
//* Execute Command                                                                         *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*
//*  >>PRE-COMPILER<<                                                                       *
//*    >>CRTCMD<< CRTRPGMOD    MODULE(&LI/&OB) +                                            *
//*                              SRCFILE(&SL/&SF) SRCMBR(&SM);                              *
//*    >>IMPORTANT<<                                                                        *
//*      >>PARM<<  DBGVIEW(*ALL);                                                           *
//*      >>PARM<<  OPTION(*EVENTF);                                                         *
//*      >>PARM<<  TGTRLS(&TR);                                                             *
//*    >>END-IMPORTANT<<                                                                    *
//*    >>EXECUTE<<                                                                          *
//*  >>END-PRE-COMPILER<<                                                                   *
//*-----------------------------------------------------------------------------------------*

// Control

CTL-OPT NoMain;
CTL-OPT DatFmt(*ISO) TimFmt(*ISO) DecEdit('0,');
CTL-OPT Copyright('(C) Copyright Task Force IT-Consulting GmbH');
CTL-OPT ExtBinInt(*YES);

//*-----------------------------------------------------------------------------------------*

// Includes

/Include QCPYSRC,QCAPCMD

/Include QCPYSRC,QMHRCVPM

/Include QCPYSRC,QMHMOVPM

/Include QCPYSRC,CPOP0100

/Include QCPYSRC,RCVM0100

/Include QCPYSRC,ERRC0100

//*-----------------------------------------------------------------------------------------*

// Fields to export

DCL-S EXCCMD_Message_Id
      CHAR(7) Export;                                                    // Message-Id.
DCL-S EXCCMD_Message_Data
      CHAR(256) Export;                                                  // Message data

//*=========================================================================================*
//* Execute command                                                                         *
//*=========================================================================================*

DCL-PROC EXCCMD_execute_Command EXPORT;

  DCL-PI EXCCMD_execute_Command;
    p_Command
    CHAR(4096) Const;                                                      // --> Command
  END-PI;

  // Locale field declarations

  DCL-S l_CCS
        CHAR(1);                                                           // Changed command s
  DCL-S l_Length_of_CCS
        INT(10);                                                           // Length of changed

  // Initialize export fields
  EXCCMD_Message_Id = *Blanks;
  EXCCMD_Message_Data = *Blanks;

  // Initialize options control block
  CPOP0100.Type_Of_Command_Processing = 0;
  CPOP0100.DBCS_Data_Handling = '0';
  CPOP0100.Prompter_Action = '2';
  CPOP0100.Command_String_Syntax = '0';
  CPOP0100.Message_Retrieve_Key = *Blanks;

  // Process command
  Clear ERRC0100;
  CallP(E) QCAPCMD(
    p_Command :        // --> Source command string
    %Size(p_Command) : // --> Length of source command string
    CPOP0100 :         // --> Options control block
    %Size(CPOP0100) :  // --> Options control block length
    'CPOP0100' :       // --> Options control block format
    l_CCS :            // <-- Changed command string
    %Size(l_CCS) :     // --> Length available for CCS
    l_Length_of_CCS :  // <-- Length available for CCS to return
    ERRC0100           // <-> Error code
  );

  // An error has occured
  If %Error;

    // Retrieve program message
    Clear ERRC0100;
    CallP(E) QMHRCVPM(
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

    // Set export fields
    If %Error;
      EXCCMD_Message_Id = '*ERROR';
      EXCCMD_Message_Data = *Blanks;
    Else;
      EXCCMD_Message_Id = RCVM0100.Message_Id;
      EXCCMD_Message_Data = RCVM0100.Message_Text;
    EndIf;

  EndIf;

  // Move program message
  Clear ERRC0100;
  CallP(E) QMHMOVPM(
    *Blanks :           // --> Message key
    '*COMP     ' +
    '*DIAG     ' +
    '*ESCAPE   ' +
    '*INFO     ' :      // --> Message types
    4 :                 // --> Number of message types
    '*' :               // --> To call stack entry
    1 :                 // --> To call stack counter
    ERRC0100 :          // <-> Error code
    %Size('*') :        // --> Length of to call stack entry
    '*NONE     *NONE' : // --> To call stack entry qualification
    '*CHAR' :           // --> To call stack entry data type
    '*' :               // --> From call stack entry adress
    0                   // --> From call stack counter
  );

END-PROC EXCCMD_execute_Command;

//*=========================================================================================*
//* Get message id.                                                                         *
//*=========================================================================================*

DCL-PROC EXCCMD_get_Message_Id EXPORT;

  DCL-PI EXCCMD_get_Message_Id
         CHAR(7);                                                          // Message-Id.
  END-PI;

  Return EXCCMD_Message_Id;

END-PROC EXCCMD_get_Message_Id;

//*=========================================================================================*
//* Get message data                                                                        *
//*=========================================================================================*

DCL-PROC EXCCMD_get_Message_Data EXPORT;

  DCL-PI EXCCMD_get_Message_Data
         CHAR(256);                                                        // Message data
  END-PI;

  Return EXCCMD_Message_Data;

END-PROC EXCCMD_get_Message_Data;
