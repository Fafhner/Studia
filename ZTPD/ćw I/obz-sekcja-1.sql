/*

1

 */

CREATE TYPE Samochod AS OBJECT (
MARKA VARCHAR2(20),
MODEL VARCHAR2(20),
KILOMETRY NUMBER,
DATA_PRODUKCJI DATE,
CENA NUMBER(10,2)
);

CREATE TABLE Samochody OF Samochod;
INSERT INTO Samochody VALUES (NEW Samochod('FIAT', 'BRAVA', 60000, TO_DATE('30-11-1999', 'dd-mm-yy'), 25000));
INSERT INTO Samochody VALUES (NEW Samochod('MAZDA', '323', 12000, TO_DATE('10-05-1997', 'dd-mm-yy'), 45000));
INSERT INTO Samochody VALUES (NEW Samochod('FORD', 'MONDEO', 80000, TO_DATE('22-09-2000', 'dd-mm-yy'), 52000));


/*

2

 */
 
CREATE TABLE WLASCICIELE(
IMIE VARCHAR2(100),
NAZWISKO VARCHAR2(100),
SAMOCHOD samochod
);

INSERT INTO Wlasciciele (imie, nazwisko, samochod) VALUES
('Jan', 'Kowalski', NEW SAMOCHOD('FIAT', 'SEICENTO', 30000, TO_DATE('02-12-0010', 'dd-mm-yyyy'), 19500) )
INSERT INTO Wlasciciele (imie, nazwisko, samochod) VALUES
('Adam', 'Nowak', NEW SAMOCHOD('Opel', 'Astra', 34000, TO_DATE('01-06-0009', 'dd-mm-yyyy'), 33700) )


/*

3

 */
 
ALTER TYPE Samochod REPLACE AS OBJECT (
MARKA VARCHAR2(20),
MODEL VARCHAR2(20),
KILOMETRY NUMBER,
DATA_PRODUKCJI DATE,
CENA NUMBER(10,2),
MEMBER FUNCTION wiek RETURN NUMBER,
MEMBER FUNCTION kilometry_na_wiek RETURN NUMBER,
MEMBER FUNCTION wartosc RETURN NUMBER
);



CREATE OR REPLACE TYPE BODY Samochod  AS

MEMBER FUNCTION wiek RETURN NUMBER IS
 BEGIN 
    RETURN (EXTRACT (YEAR FROM CURRENT_DATE)) - (EXTRACT (YEAR FROM DATA_PRODUKCJI));
 END wiek;

MEMBER FUNCTION kilometry_na_wiek RETURN NUMBER IS
 BEGIN 
    RETURN FLOOR(KILOMETRY/10000);
 END kilometry_na_wiek;
 
MEMBER FUNCTION wartosc RETURN NUMBER IS 
 BEGIN 
    RETURN ROUND(CENA*POWER(0.9, wiek()+kilometry_na_wiek()), 2);
 END wartosc;
END;


/*

4

 */
 
ALTER TYPE SAMOCHOD ADD MAP MEMBER FUNCTION map_samochod RETURN NUMBER CASCADE;

CREATE OR REPLACE TYPE BODY Samochod  AS

MEMBER FUNCTION wiek RETURN NUMBER IS
 BEGIN 
    RETURN (EXTRACT (YEAR FROM CURRENT_DATE)) - (EXTRACT (YEAR FROM DATA_PRODUKCJI));
 END wiek;
 

MEMBER FUNCTION kilometry_na_wiek RETURN NUMBER IS
 BEGIN 
    RETURN FLOOR(KILOMETRY/10000);
 END kilometry_na_wiek;
 
MEMBER FUNCTION wartosc RETURN NUMBER IS 
 BEGIN 
    RETURN ROUND(CENA*POWER(0.9, wiek()+kilometry_na_wiek()), 2);
 END wartosc;
 
 MAP MEMBER FUNCTION odwzoruj RETURN NUMBER IS 
 BEGIN 
    RETURN wiek()+kilometry_na_wiek();
 END odwzoruj;
END;




/*

5

*/

CREATE OR REPLACE TYPE WLASCICIEL AS OBJECT (
IMIE VARCHAR2(100), NAZWISKO VARCHAR2(100), AUTO samochod);

ALTER TYPE Samochod ADD ATTRIBUTE Wlasc REF Wlasciciel CASCADE;

CREATE OR REPLACE TABLE WlascicieleTab OF Wlasciciel;
CREATE OR REPLACE TABLE SamochodyTab OF Samochod;
ALTER TABLE SamochodyTab ADD SCOPE FOR(Wlasc)IS WlascicieleTAB;

INSERT INTO WlascicieleTAB VALUES (NEW Wlasciciel('Jan', 'Kowalski'));
INSERT INTO WlascicieleTAB VALUES (NEW Wlasciciel('Adam', 'Nowak'));

INSERT INTO SamochodyTab VALUES (NEW Samochod('FIAT', 'BRAVA', 60000, TO_DATE('30-11-1999', 'dd-mm-yy'), 25000, NULL));
INSERT INTO SamochodyTab VALUES (NEW Samochod('MAZDA', '323', 12000, TO_DATE('10-05-1997', 'dd-mm-yy'), 45000, NULL));
INSERT INTO SamochodyTab VALUES (NEW Samochod('FORD', 'MONDEO', 80000, TO_DATE('22-09-2000', 'dd-mm-yy'), 52000, NULL));

UPDATE SamochodyTab s SET s.wlasc = (SELECT REF(w) FROM WlascicieleTAB w WHERE w.nazwisko = 'Nowak' )
where s.marka = 'FIAT';

UPDATE SamochodyTab s SET s.wlasc = (SELECT REF(w) FROM WlascicieleTAB w WHERE w.nazwisko = 'Nowak' )
where s.marka = 'MAZDA';

UPDATE SamochodyTab s SET s.wlasc = (SELECT REF(w) FROM WlascicieleTAB w WHERE w.nazwisko = 'Kowalski' )
where s.marka = 'FORD';
