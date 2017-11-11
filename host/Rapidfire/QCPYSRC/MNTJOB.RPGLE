**FREE

DCL-PR MNTJOB_openFiles
       EXTPROC('MNTJOB_openFiles');
END-PR;

DCL-PR MNTJOB_closeFiles
       EXTPROC('MNTJOB_closeFiles');
END-PR;

DCL-PR MNTJOB_initialize IND // <-- Success
       EXTPROC('MNTJOB_initialize');
  *N CHAR(10) CONST; // --> Mode
  *N CHAR(10) CONST; // --> Job
  *N CHAR(1);        // <-- Success
END-PR;

DCL-PR MNTJOB_getValues
       EXTPROC('MNTJOB_getValues');
  *N CHAR(10); // <-- Job
  *N CHAR(35); // <-- Description
  *N CHAR(10); // <-- Create environment
  *N CHAR(10); // <-- Job queue
  *N CHAR(10); // <-- Job queue (Library)
END-PR;

DCL-PR MNTJOB_setValues
       EXTPROC('MNTJOB_setValues');
  *N CHAR(10) CONST; // --> Job
  *N CHAR(35) CONST; // --> Description
  *N CHAR(10) CONST; // --> Create environment
  *N CHAR(10) CONST; // --> Job queue
  *N CHAR(10) CONST; // --> Job queue (Library)
END-PR;

DCL-PR MNTJOB_check IND // <-- Success
       EXTPROC('MNTJOB_check');
  *N CHAR(10);  // <-- Feld
  *N CHAR(256); // <-- Nachricht
  *N CHAR(1);   // <-- Success
END-PR;

DCL-PR MNTJOB_book
       EXTPROC('MNTJOB_book');
END-PR;
