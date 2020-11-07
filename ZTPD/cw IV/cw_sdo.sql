CREATE TABLE FIGURY(
ID NUMBER(1),
KSZTALT MDSYS.SDO_GEOMETRY
);

INSERT INTO FIGURY VALUES (1, MDSYS.SDO_GEOMETRY(
            2003, NULL, NULL,
            MDSYS.SDO_ELEM_INFO_ARRAY(
                1, 1003, 4
            ),
            MDSYS.SDO_ORDINATE_ARRAY(
                5, 3,
                7, 5,
                5, 7
            ))
);


INSERT INTO FIGURY VALUES (2, MDSYS.SDO_GEOMETRY(
            2003, NULL, NULL,
            MDSYS.SDO_ELEM_INFO_ARRAY(
                1, 1003, 3
            ),
            MDSYS.SDO_ORDINATE_ARRAY(
                1, 1,
                5, 5
            ))
);


INSERT INTO FIGURY VALUES (3, MDSYS.SDO_GEOMETRY(
            2006, NULL, NULL,
            MDSYS.SDO_ELEM_INFO_ARRAY(
                1, 4, 2,
                1, 2, 1,
                3, 2, 2
            ),
            MDSYS.SDO_ORDINATE_ARRAY(
                3, 2,
                6, 2,
                7, 3,
                8, 2,
                7, 3
            ))
);

INSERT INTO FIGURY VALUES(4, MDSYS.SDO_GEOMETRY(2003,NULL,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),MDSYS.SDO_ORDINATE_ARRAY(5,1,8,1,8,6,5,7,5,1)));

select SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(ksztalt, 0.1) from figury;
commit;
