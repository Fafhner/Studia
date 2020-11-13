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

    
