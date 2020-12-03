/**********

Ćwiczenie 1
***********/

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






/**********

Ćwiczenie 2
***********/


select A.CITY_NAME, SDO_NN_DISTANCE(1) DISTANCE 
from  MAJOR_CITIES A 
where A.CITY_NAME != 'Warsaw' AND SDO_NN(
    GEOM, (SELECT GEOM FROM MAJOR_CITIES WHERE CITY_NAME='Warsaw'),
    'sdo_num_res=10 unit=km', 1) = 'TRUE';


select A.CITY_NAME 
from  MAJOR_CITIES A 
where A.CITY_NAME != 'Warsaw' AND SDO_WITHIN_DISTANCE(
    GEOM, (SELECT GEOM FROM MAJOR_CITIES WHERE CITY_NAME='Warsaw'),
    'distance=100 unit=km') = 'TRUE';
    
select cntry_name, city_name
from MAJOR_CITIES
where SDO_RELATE(
    GEOM,
    (select GEOM FROM COUNTRY_BOUNDARIES where cntry_name = 'Slovakia'),
    'mask=inside') = 'TRUE';


select B.cntry_name, SDO_GEOM.SDO_DISTANCE(A.GEOM, B.GEOM, 0.1, 'unit=km') DISTANCE
from COUNTRY_BOUNDARIES A, COUNTRY_BOUNDARIES B
where a.cntry_name = 'Poland' AND SDO_RELATE(
    A.GEOM, 
    B.GEOM, 
    'mask=EQUAL+TOUCH') != 'TRUE';
    



/**********

Ćwiczenie 3
***********/



select B.cntry_name, SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(A.GEOM, B.GEOM, 1), 1, 'unit=km') DISTANCE
from COUNTRY_BOUNDARIES A, COUNTRY_BOUNDARIES B
where a.cntry_name = 'Poland' AND SDO_RELATE(
    A.GEOM, 
    B.GEOM, 
    'mask=TOUCH') = 'TRUE';
    
SELECT a.cntry_name FROM COUNTRY_BOUNDARIES A
ORDER BY SDO_GEOM.SDO_AREA(GEOM) DESC
FETCH FIRST 1 ROW ONLY;

select SDO_GEOM.SDO_AREA(
    SDO_GEOM.SDO_MBR(SDO_GEOM.SDO_UNION(A.GEOM, B.GEOM, 1)),
    1, 'unit=SQ_KM') SQ_KM 
from MAJOR_CITIES A, MAJOR_CITIES B
where a.city_name = 'Warsaw' and b.city_name = 'Lodz';


select SDO_GEOM.SDO_UNION(A.GEOM, B.GEOM, 1).GET_GTYPE() GTYPE
from COUNTRY_BOUNDARIES A, MAJOR_CITIES B
where a.cntry_name = 'Poland' and b.city_name = 'Lodz';



select CITY_NAME, COUNTRY_NAME FROM (
    select a.city_name CITY_NAME, b.cntry_name COUNTRY_NAME , 
        SDO_GEOM.SDO_DISTANCE(a.geom, SDO_GEOM.SDO_CENTROID(b.geom, 1), 1, 'unit=km') DISTANCE
    from  MAJOR_CITIES A, COUNTRY_BOUNDARIES B
    where a.cntry_name = b.cntry_name
    order by DISTANCE
    FETCH FIRST 1 ROW ONLY
    );
    

select b.name, sum(SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(A.GEOM, B.GEOM, 1), 1, 'unit=km')) LENGTH
from  COUNTRY_BOUNDARIES A, RIVERS B
where a.cntry_name='Poland' and SDO_RELATE(a.geom, b.geom, 'mask=ANYINTERACT')='TRUE'
group by b.name;

    
