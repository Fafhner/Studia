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
    

