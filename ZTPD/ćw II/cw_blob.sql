CREATE TABLE MOVIES (
 ID NUMBER(12)PRIMARY KEY,
 TITLE VARCHAR2(400)NOT NULL,
 CATEGORY VARCHAR2(50),
 YEAR CHAR(4),
 CAST  VARCHAR2(4000),
 DIRECTOR  VARCHAR2(4000),
 STORY VARCHAR2(4000),
 PRICE NUMBER(5,2),
 COVER BLOB, 
 MIME_TYPE  VARCHAR2(50)
);

SELECT * FROM DESCRIPTIONS ;

/*
COLUMN YEAR CHAR(4) in MOVIES
COLUMN YEAR CHAR(12) in DESCRIPTIONS
*/
INSERT INTO movies
 SELECT d.ID, d.TITLE, d.CATEGORY, CAST(d.YEAR as CHAR(4)), d.CAST, d.DIRECTOR, 
        d.STORY, d.PRICE, c.IMAGE, c.MIME_TYPE
 FROM DESCRIPTIONS d LEFT OUTER JOIN COVERS c on d.ID = c.MOVIE_ID;
 
 
 
SELECT id, title FROM MOVIES WHERE cover is null;

SELECT id, title, DBMS_LOB.getlength(cover) FROM MOVIES WHERE cover is not null;
SELECT id, title, DBMS_LOB.getlength(cover) FROM MOVIES WHERE cover is null;


select directory_name, directory_path FROM ALL_DIRECTORIES


UPDATE MOVIES
 SET COVER = EMPTY_BLOB(),
     MIME_TYPE = 'image/jpeg'
 WHERE id=66;
 
 
 SELECT id, title, DBMS_LOB.getlength(cover) FROM MOVIES WHERE id=65 or id=66;
 
 
DECLARE
 cover_file BFILE :=BFILENAME('ZSBD_DIR','escape.jpg');
 cover_table BLOB;
 BEGIN
   SELECT cover INTO cover_table FROM movies WHERE id=66 FOR UPDATE;
   
   DBMS_LOB.FILEOPEN(cover_file, DBMS_LOB.file_readonly);
   DBMS_LOB.LOADFROMFILE(cover_table, cover_file, DBMS_LOB.GETLENGTH(cover_file));
   DBMS_LOB.FILECLOSE(cover_file);
 END;
 /
 
 CREATE TABLE TEMP_COVERS (
 movie_id NUMBER(12),
 image BFILE,
 mime_type VARCHAR2(50));
 


INSERT INTO temp_covers VALUES (65, BFILENAME('ZSBD_DIR','eagles.jpg'), 'image/jpeg');
commit;

SELECT movie_id, DBMS_LOB.GETLENGTH(image) filesize FROM temp_covers;
 
 
 
 
 
 DECLARE
  cover_file BFILE;
  mime_type VARCHAR2(50);
  temp_blob BLOB;
 BEGIN
    SELECT image, mime_type into cover_file, mime_type FROM TEMP_COVERS;
    
   DBMS_LOB.createtemporary(temp_blob,TRUE);
   DBMS_LOB.FILEOPEN(cover_file, DBMS_LOB.file_readonly);
    DBMS_LOB.LOADFROMFILE(temp_blob, cover_file, DBMS_LOB.GETLENGTH(cover_file));
   DBMS_LOB.FILECLOSE(cover_file);
   
   UPDATE MOVIES
   SET
    mime_type = mime_type,
    cover = temp_blob
    WHERE id=65;
    
    DBMS_LOB.freetemporary(temp_blob);
   COMMIT;
 END;
 /
 
 
  SELECT id, title, DBMS_LOB.getlength(cover) FROM MOVIES WHERE id=65 or id=66;
 