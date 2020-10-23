CREATE TABLE DOKUMENTY (
ID NUMBER(12) PRIMARY KEY,
DOKUMENT CLOB
);

DECLARE
c CLOB;
BEGIN
  FOR i in 1..10000 LOOP
    SELECT concat('Oto tekst. ', c) INTO c FROM dual;
  END LOOP;
  INSERT INTO DOKUMENTY VALUES (1, c);
END;
/

select * from dokumenty;
select UPPER(dokument) from dokumenty;
select LENGTH(dokument) from dokumenty;
select SUBSTR(dokument, 5, 1000) from dokumenty;
select DBMS_LOB.SUBSTR(dokument, 1000, 5) from dokumenty;

INSERT INTO DOKUMENTY VALUES(2, EMPTY_CLOB());
INSERT INTO DOKUMENTY VALUES(3, NULL);
commit;


select * from dokumenty;
select UPPER(dokument) from dokumenty;
select LENGTH(dokument) from dokumenty;
select SUBSTR(dokument, 5, 1000) from dokumenty;
select DBMS_LOB.SUBSTR(dokument, 1000, 5) from dokumenty;

select * from ALL_DIRECTORIES;

DECLARE
fils BFILE := BFILENAME('ZSBD_DIR','dokument.txt');
c CLOB;
doffset integer:=1;
soffset integer:=1;
langctx integer:=0;
warn integer:=null;
BEGIN
    SELECT dokument INTO c FROM dokumenty WHERE id=2 FOR UPDATE;
    
    DBMS_LOB.fileopen(fils, DBMS_LOB.file_readonly);
    DBMS_LOB.LOADCLOBFROMFILE(c, fils, DBMS_LOB.LOBMAXSIZE, doffset, soffset, 0, langctx, warn);
    DBMS_LOB.FILECLOSE(fils);
    commit;
    DBMS_OUTPUT.PUT_LINE(warn); 
END;
/

UPDATE DOKUMENTY SET
dokument = TO_CLOB(BFILENAME('ZSBD_DIR','dokument.txt'))
WHERE ID=3;

select * from DOKUMENTY;
select LENGTH(dokument) from DOKUMENTY;

DROP TABLE DOKUMENTY;


CREATE OR REPLACE PROCEDURE CLOB_CENSOR(c IN OUT CLOB, txt IN VARCHAR2) AS
dots VARCHAR2(100) := rpad('*', length(txt), '*');
pos INTEGER := 0;
BEGIN
    pos := DBMS_LOB.INSTR(c, txt, 1);
    WHILE pos != 0 LOOP
        DBMS_LOB.WRITE(c, length(dots), pos, dots);
        pos := DBMS_LOB.INSTR(c, txt, pos);
    END LOOP;
    commit;
END CLOB_CENSOR;
/

CREATE TABLE biographies AS SELECT * FROM ZSBD_TOOLS.BIOGRAPHIES;
SELECT * FROM biographies;

DECLARE
c CLOB;
BEGIN
SELECT BIO INTO c FROM biographies FOR UPDATE;
CLOB_CENSOR(c, 'Cimrman');
END;
/

SELECT bio FROM biographies;
DROP TABLE biographies;