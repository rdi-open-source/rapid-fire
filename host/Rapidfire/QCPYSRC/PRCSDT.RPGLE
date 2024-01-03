**FREE

//*-----------------------------------------------------------------------------------------*
//*                                                                                         *
//* Copyright (c) 2019 Task Force IT-Consulting GmbH, Waltrop (Germany)                     *
//* This software and the accompanying materials are made available                         *
//* under the terms of the GNU General Public License version 2.0 (GPLv2)                   *
//* which accompanies this distribution and is available at                                 *
//* http://www.gnu.org/licenses/gpl-2.0.html                                                *
//*                                                                                         *
//*-----------------------------------------------------------------------------------------*

DCL-PR PRCSDT_createFieldList IND; // <-- Success
  *N CHAR(10) Const;               // --> Job
  *N ZONED(6) Const;               // --> Position
  *N CHAR(10) Const;               // --> Area
  *N CHAR(1) Const;                // --> Source/Target
  *N CHAR(10) Const;               // --> Library
  *N CHAR(10) Const;               // --> File
  *N CHAR(1);                      // <-- Error
END-PR;

DCL-PR PRCSDT_deleteFieldList IND; // <-- Success
  *N CHAR(10) Const;               // --> Job
  *N ZONED(6) Const;               // --> Position
  *N CHAR(10) Const;               // --> Area
  *N CHAR(1) Const;                // --> Source/Target
  *N CHAR(1);                      // <-- Error
END-PR;

DCL-PR PRCSDT_containsSDTFields IND; // <-- Result
  *N CHAR(10) Const;                 // --> Job
  *N ZONED(6 : 0) Const;             // --> Position
  *N CHAR(10) Const;                 // --> Area
  *N CHAR(1) Const;                  // --> Source/Target
END-PR;

DCL-PR PRCSDT_doesAllSDTFieldsHaveDefaults IND; // <-- Result
  *N CHAR(10) Const;                            // --> Job
  *N ZONED(6 : 0) Const;                        // --> Position
  *N CHAR(10) Const;                            // --> Area
  *N CHAR(1) Const;                             // --> Source/Target
END-PR;

DCL-PR PRCSDT_getSDTView CHAR(10); // <-- SDT View
  *N CHAR(10) Const;               // --> Job
  *N ZONED(6 : 0) Const;           // --> Position
  *N CHAR(10) Const;               // --> Area
  *N CHAR(1) Const;                // --> Source/Target
  *N CHAR(10) Const;               // --> File
END-PR;

DCL-PR PRCSDT_createSDTView IND; // <-- Success
  *N CHAR(10) Const;             // --> Job
  *N ZONED(6) Const;             // --> Position
  *N CHAR(10) Const;             // --> Area
  *N CHAR(1) Const;              // --> Source/Target
  *N CHAR(10) Const;             // --> Library
  *N CHAR(10) Const;             // --> File
  *N CHAR(10) Const;             // --> Shadow library
  *N CHAR(10) Const;             // --> SDT View
  *N CHAR(10);                   // <-- SDT View Record Format
  *N CHAR(13);                   // <-- SDT View Format Level Id.
  *N CHAR(1);                    // <-- Error
END-PR;

DCL-PR PRCSDT_deleteSDTView IND; // <-- Success
  *N CHAR(10) Const;             // --> Job
  *N ZONED(6) Const;             // --> Position
  *N CHAR(10) Const;             // --> Area
  *N CHAR(1) Const;              // --> Source/Target
  *N CHAR(10) Const;             // --> Shadow library
  *N CHAR(10) Const;             // --> SDT View
  *N CHAR(1);                    // <-- Error
END-PR;

DCL-PR PRCSDT_createEnvironment IND; // <-- Success
  *N CHAR(10) Const;                 // --> Job
  *N ZONED(6 : 0) Const;             // --> Position
  *N CHAR(10) Const;                 // --> Area
  *N CHAR(10) Const;                 // --> Library
  *N CHAR(10) Const;                 // --> Shadow library
  *N CHAR(10) Const;                 // --> File
  *N CHAR(1) Const;                  // --> SDT View Processing
  *N CHAR(10);                       // <-- SDT View Target
  *N CHAR(10);                       // <-- SDT View Record Format Target
  *N CHAR(13);                       // <-- SDT View Format Level Id Target
  *N CHAR(10);                       // <-- SDT View Source
  *N CHAR(10);                       // <-- SDT View Record Format Source
  *N CHAR(13);                       // <-- SDT View Format Level Id Source
  *N CHAR(256);                      // <-- Error
END-PR;

DCL-PR PRCSDT_deleteEnvironment IND; // <-- Success
  *N CHAR(10) Const;                 // --> Job
  *N ZONED(6 : 0) Const;             // --> Position
  *N CHAR(10) Const;                 // --> Area
  *N CHAR(1) Const;                  // --> Delete Field List Target
  *N CHAR(1) Const;                  // --> Delete Field List Source
  *N CHAR(1) Const;                  // --> Delete SDT View Target
  *N CHAR(1) Const;                  // --> Delete SDT View Source
  *N CHAR(10) Const;                 // --> Shadow library
  *N CHAR(10) Const;                 // --> SDT View Target
  *N CHAR(10) Const;                 // --> SDT View Source
  *N CHAR(256);                      // <-- Error
END-PR;
