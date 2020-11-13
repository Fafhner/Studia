INSERT INTO user_sdo_geom_metadata VALUES (
    'FIGURY',
    'KSZTALT',
    mdsys.sdo_dim_array(
        mdsys.sdo_dim_element('X', 0, 9, 0.01), 
        mdsys.sdo_dim_element('Y', 0, 9, 0.01)),
    NULL
);

select SDO_TUNE.ESTIMATE_RTREE_INDEX_SIZE(3000000,8192,10,2,0) from dual;


create index FIGURY_KSZTALT_IDX on FIGURY(KSZTALT)INDEXTYPE IS MDSYS.SPATIAL_INDEX_V2;

select ID from FIGURY 
where SDO_FILTER(KSZTALT, SDO_GEOMETRY(2001, null, SDO_POINT_TYPE(3,3,null),null,null)) = 'TRUE';

select ID from FIGURY 
where SDO_RELATE(KSZTALT,SDO_GEOMETRY(2001,null,SDO_POINT_TYPE(3,3,null),null,null),'mask=ANYINTERACT') = 'TRUE';