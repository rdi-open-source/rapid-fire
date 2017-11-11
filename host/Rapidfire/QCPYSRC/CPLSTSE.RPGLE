**FREE

DCL-PR CPLSTSE_open
       EXTPROC('CPLSTSE_open');
END-PR;

DCL-PR CPLSTSE_close
       EXTPROC('CPLSTSE_close');
END-PR;

DCL-PR CPLSTSE_completeStatusEntry
       EXTPROC('CPLSTSE_completeStatusEntry');
  *N CHAR(10)       CONST; // --> Job
  *N PACKED(6 : 0)  CONST; // --> Position
  *N CHAR(10)       CONST; // --> Area
  *N CHAR(10);             // <-- File
  *N CHAR(10);             // <-- Library
  *N PACKED(10 : 0);       // <-- Records in production library
  *N PACKED(10 : 0);       // <-- Records in shadow library
  *N PACKED(10 : 0);       // <-- Records to copy
  *N PACKED(10 : 0);       // <-- Records copied
  *N CHAR(11);             // <-- Estimated time
  *N PACKED(10 : 0);       // <-- Changes to apply
  *N PACKED(10 : 0);       // <-- Changes applied
  *N PACKED(3 : 0);        // <-- Percent done
END-PR;
