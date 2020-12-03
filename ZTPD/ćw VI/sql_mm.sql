/*************

Æwiczenie 1

*************/

SELECT
    lpad('-', 2 *(level - 1), '|-')
    || t.owner
    || '.'
    || t.type_name
    || ' (FINAL:'
    || t.final
    || ', INSTANTIABLE:'
    || t.instantiable
    || ', ATTRIBUTES:'
    || t.attributes
    || ', METHODS:'
    || t.methods
    || ')'
FROM
    all_types t
START WITH
    t.type_name = 'ST_GEOMETRY'
CONNECT BY PRIOR t.type_name = t.supertype_namse
           AND PRIOR t.owner = t.owner;

CREATE TABLE myst_major_cities (
    fips_cntry  VARCHAR(2),
    city_name   VARCHAR(40),
    stgeom      st_point
);

DESC major_cities;

INSERT INTO myst_major_cities
    SELECT
        fips_cntry,
        city_name,
        st_point(geom)
    FROM
        major_cities; 





/*************

Æwiczenie 2

*************/
INSERT INTO myst_major_cities VALUES (
    'PL',
    'Szczyrk',
    TREAT(st_point.from_wkt('POINT(19.036107 49.718655)', 8307) AS st_point)
);

SELECT
    name,
    ( st_geometry(geom) ).get_wkt ( ) wkt
FROM
    rivers;

SELECT
    sdo_util.to_gmlgeometry(st_point.get_sdo_geom(stgeom))
FROM
    myst_major_cities
WHERE
    city_name = 'Szczyrk';



/*************

Æwiczenie 3

*************/


CREATE TABLE myst_country_boundaries (
    fips_cntry  VARCHAR(2),
    cntry_name  VARCHAR(40),
    stgeom      st_multipolygon
);

INSERT INTO myst_country_boundaries
    SELECT
        fips_cntry,
        cntry_name,
        st_multipolygon(geom)
    FROM
        country_boundaries;

SELECT
    typ_obiektu,
    COUNT(typ_obiektu) ile
FROM
    (
        SELECT
            b.stgeom.st_geometrytype() typ_obiektu
        FROM
            myst_country_boundaries b
    )
GROUP BY
    typ_obiektu;

SELECT
    b.stgeom.st_issimple()
FROM
    myst_country_boundaries b;

SELECT
    city_name,
    b.stgeom.st_srid() srid
FROM
    myst_major_cities b;


/*************

Æwiczenie 4

*************/

SELECT
    b.cntry_name,
    COUNT(*)
FROM
    myst_country_boundaries  b,
    myst_major_cities        c
WHERE
    c.stgeom.st_within(b.stgeom) = 1
GROUP BY
    b.cntry_name;

SELECT
    a.cntry_name    a_name,
    b.cntry_name    b_name
FROM
    myst_country_boundaries  a,
    myst_country_boundaries  b
WHERE
        a.stgeom.st_touches(b.stgeom) = 1
    AND b.cntry_name = 'Czech Republic';

SELECT DISTINCT
    b.cntry_name,
    r.name
FROM
    myst_country_boundaries  b,
    rivers                   r
WHERE
        b.cntry_name = 'Czech Republic'
    AND st_linestring(r.geom).st_intersects(b.stgeom) = 1;

SELECT
    TREAT(b.stgeom.st_union(c.stgeom) AS st_polygon).st_area() powierzchnia
FROM
    myst_country_boundaries  b,
    myst_country_boundaries  c
WHERE
        b.cntry_name = 'Czech Republic'
    AND c.cntry_name = 'Slovakia';

SELECT
    t.wegry_bez,
    t.wegry_bez.st_geometrytype()
FROM
    (
        SELECT
            TREAT(b.stgeom.st_difference(st_geometry(w.geom)) AS st_polygon) wegry_bez
        FROM
            myst_country_boundaries  b,
            water_bodies             w
        WHERE
                b.cntry_name = 'Hungary'
            AND w.name = 'Balaton'
    ) t;



/*************

Æwiczenie 5

*************/


INSERT INTO user_sdo_geom_metadata
    SELECT
        'MYST_MAJOR_CITIES',
        'STGEOM',
        t.diminfo,
        t.srid
    from   user_sdo_geom_metadata T
	where  T.table_name = 'MAJOR_CITIES';

INSERT INTO user_sdo_geom_metadata
    SELECT
        'MYST_COUNTRY_BOUNDARIES',
        'STGEOM',
        t.diminfo,
        t.srid
    FROM
        user_sdo_geom_metadata t
    WHERE
        t.table_name = 'COUNTRY_BOUNDARIES';

CREATE INDEX myst_major_cities_idx ON
    myst_major_cities (stgeom)
INDEXTYPE IS mdsys.spatial_index;


CREATE INDEX myst_country_boundaries_idx ON
    MYST_COUNTRY_BOUNDARIES (stgeom)
INDEXTYPE IS mdsys.spatial_index;

EXPLAIN PLAN FOR
SELECT
    b.cntry_name a_name,
    COUNT(*)
FROM
    myst_country_boundaries  b,
    myst_major_cities        c
WHERE
        sdo_within_distance(c.stgeom, b.stgeom, 'distance=100 unit=km') = 'TRUE'
    AND b.cntry_name = 'Poland'
GROUP BY
    b.cntry_name;
    
SELECT PLAN_TABLE_OUTPUT FROM TABLE(DBMS_XPLAN.DISPLAY());