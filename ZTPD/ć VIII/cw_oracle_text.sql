/*********

æwiczenie 1

*********/


CREATE TABLE CYTATY AS SELECT * FROM ZSBD_TOOLS.CYTATY; 

select autor, tekst from cytaty 
where lower(tekst) LIKE '%pesymista%' or lower(tekst) LIKE '%‘optymista%';

create index CYTATY_IDX on CYTATY(tekst) indextype is CTXSYS.CONTEXT;

select autor, tekst from CYTATY c
where CONTAINS(c.tekst, 'optymista or pesymista')>0;

select autor, tekst from CYTATY c
where CONTAINS(c.tekst, 'pesymista not optymista')>0;

select autor, tekst from CYTATY c
where CONTAINS(c.tekst, 'near((pesymista, optymista), 3)')>0;

select autor, score(1) SCORE, tekst  from CYTATY c
where CONTAINS(c.tekst, '¿yci%', 1)>0
order by SCORE desc
FETCH FIRST 1 ROW ONLY;

select autor, tekst from CYTATY c
where CONTAINS(c.tekst, 'fuzzy(probelm)')>0;

insert into CYTATY (autor, id, tekst) values (
'Bertrand Russell',
100,
'To smutne, ¿e g³upcy s¹ tacy pewni siebie, a ludzie rozs¹dni tacy pe³ni w¹tpliwoœci.'
);
commit;


select autor, tekst from CYTATY c
where CONTAINS(c.tekst, 'g³upcy')>0;

select TOKEN_TEXT from dr$cytaty_idx$i
order by token_text;

drop index CYTATY_IDX;
create index CYTATY_IDX on CYTATY(tekst) indextype is CTXSYS.CONTEXT;

select autor, tekst from CYTATY c
where CONTAINS(c.tekst, 'g³upcy')>0;

drop index CYTATY_IDX;
drop table CYTATY;


/*********

æwiczenie 2

*********/

CREATE TABLE QUOTES AS SELECT AUTHOR, TEXT FROM ZSBD_TOOLS.QUOTES; 

create index QUOTES_IDX on QUOTES(TEXT) indextype is CTXSYS.CONTEXT;

select * from QUOTES q
where CONTAINS(q.text, 'work')>0;

select * from QUOTES q
where CONTAINS(q.text, '$work')>0;

select * from QUOTES q
where CONTAINS(q.text, 'working')>0;

select * from QUOTES q
where CONTAINS(q.text, '$working')>0;

select * from QUOTES q
where CONTAINS(q.text, 'it')>0;

select * from CTX_STOPLISTS; 
select * from CTX_STOPWORDS; 

drop index QUOTES_IDX;
create index QUOTES_IDX on QUOTES(TEXT) indextype is CTXSYS.CONTEXT
parameters ('stoplist ctxsys.empty_stoplist');

select * from QUOTES q
where CONTAINS(q.text, 'it')>0;

select * from QUOTES q
where CONTAINS(q.text, '(fool and humans) within sentence')>0;

begin 
ctx_ddl.create_section_group('nullgroup', 'NULL_SECTION_GROUP');
ctx_ddl.add_special_section('nullgroup',  'SENTENCE');
ctx_ddl.add_special_section('nullgroup',  'PARAGRAPH');
end;
/

drop index QUOTES_IDX;
create index QUOTES_IDX on QUOTES(TEXT) indextype is CTXSYS.CONTEXT
parameters ('stoplist ctxsys.empty_stoplist
             section group nullgroup');
             
select * from QUOTES q
where CONTAINS(q.text, '(fool and humans) within sentence')>0;

select * from QUOTES q
where CONTAINS(q.text, '(fool and computer) within sentence')>0;

select * from QUOTES q
where CONTAINS(q.text, 'humans')>0;


begin
ctx_ddl.create_preference('lex_z_m','BASIC_LEXER');
ctx_ddl.set_attribute('lex_z_m', 'printjoins', '_-');
ctx_ddl.set_attribute ('lex_z_m', 'index_text', 'YES');
end;
/

drop index QUOTES_IDX;
create index QUOTES_IDX on QUOTES(TEXT) indextype is CTXSYS.CONTEXT
parameters ('stoplist ctxsys.empty_stoplist
             section group nullgroup
             lexer lex_z_m');


select * from QUOTES q
where CONTAINS(q.text, 'humans')>0;

select * from QUOTES q
where CONTAINS(q.text, 'non\-humans')>0;

drop index QUOTES_IDX;
drop table QUOTES;

begin
CTX_DDL.DROP_PREFERENCE('nullgroup');
CTX_DDL.DROP_PREFERENCE('lex_z_m');
end;
/