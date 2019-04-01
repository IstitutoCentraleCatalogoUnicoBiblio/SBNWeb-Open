-- SQL Manager for PostgreSQL 5.1.1.1
-- ---------------------------------------
-- Host      : XXX.XXX.XXX.XXX
-- Database  : sbnwebDbXXXese
-- Version   : PostgreSQL 8.3.5 on x86_64-unknown-linux-gnu, compiled by GCC gcc (GCC) 4.1.2 20080704 (Red Hat 4.1.2-46)

CREATE LANGUAGE plpgsql;

CREATE SCHEMA sbnweb AUTHORIZATION sbnweb;
SET check_function_bodies = false;
--
-- Definition for function isdigit (OID = 60048) : 
--
SET search_path = sbnweb, pg_catalog;
CREATE FUNCTION sbnweb.isdigit (
  text
)
RETURNS boolean
AS 
$body$
select $1 ~ '^(-)?[0-9]+$' as result
$body$
    LANGUAGE sql;
--
-- Definition for function percento (OID = 60049) : 
--
CREATE FUNCTION sbnweb.percento (
  bigint,
  bigint
)
RETURNS numeric
AS 
$body$
select CAST(CAST ($1 AS DECIMAL) / CAST ($2 AS DECIMAL) * 100  as numeric(10,2));
$body$
    LANGUAGE sql STRICT;
--
-- Definition for function tb_autore_tsvector (OID = 60050) : 
--
CREATE FUNCTION sbnweb.tb_autore_tsvector (
)
RETURNS trigger
AS 
$body$
DECLARE
    APP_ky_cles2_a char(30);
BEGIN
	IF NEW.ds_nome_aut IS NULL THEN
		RAISE EXCEPTION 'ds_nome_aut cannot be null';
    END IF;
    IF NEW.ky_cles2_a IS NULL THEN
		APP_ky_cles2_a := ' ';
    ELSE 
        APP_ky_cles2_a := NEW.ky_cles2_a;
    END IF;

    NEW.tidx_vector := to_tsvector(translate(NEW.ds_nome_aut, '<>', '  ')||' '||NEW.ky_cles1_a||APP_ky_cles2_a);
    RETURN NEW;
END;
$body$
    LANGUAGE plpgsql;
--
-- Definition for function tb_fascicolo_anno_pub (OID = 60051) : 
--
CREATE FUNCTION sbnweb.tb_fascicolo_anno_pub (
)
RETURNS trigger
AS 
$body$
BEGIN
	NEW.anno_pub := EXTRACT(year FROM NEW.data_conv_pub);
    RETURN NEW;
END;
$body$
    LANGUAGE plpgsql;
--
-- Definition for function tbc_poss_prov_tidx (OID = 60052) : 
--
CREATE FUNCTION sbnweb.tbc_poss_prov_tidx (
)
RETURNS trigger
AS 
$body$
DECLARE
    APP_ky_cles2_a char(30);
BEGIN
    IF NEW.ds_nome_aut IS NULL THEN
        RAISE EXCEPTION 'ds_nome_aut cannot be null';
    END IF;
    IF NEW.ky_cles2_a IS NULL THEN
        APP_ky_cles2_a := ' ';
    ELSE 
        APP_ky_cles2_a := NEW.ky_cles2_a;
    END IF;

    NEW.tidx_vector_ds_nome_aut := to_tsvector(translate(NEW.ds_nome_aut, '<>', '  ')||' '||NEW.ky_cles1_a||APP_ky_cles2_a);
    RETURN NEW;
END;
$body$
    LANGUAGE plpgsql;
--
-- Definition for function listaidautori (OID = 186632) : 
--
CREATE FUNCTION sbnweb.listaidautori (
  flcon character,
  datad timestamp with time zone,
  dataa timestamp with time zone,
  lettd text,
  letta text,
  liv_autd character,
  liv_auta character,
  tp_nome character
)
RETURNS SETOF text
AS 
$body$
    DECLARE
     fl_titleg char;
     idautore text;
    BEGIN
    IF fl_titleg='s' THEN
      FOR idautore IN
        SELECT CAST(au.VID as text) from "sbnweb"."tb_autore" au
        INNER JOIN "sbnweb"."tr_tit_aut" ta  on ta.vid = au.vid and not ta.fl_canc='S'
        WHERE au.fl_canc <> 'S'
          AND au.tp_forma_aut = 'A'
          AND (flcon='' or au.fl_condiviso= flcon)
          AND CAST(au.ts_ins as date) BETWEEN COALESCE( dataD, to_timestamp('10010101','YYYYMMDD')) AND COALESCE( dataA, to_timestamp('99990101','YYYYMMDD'))
          AND au.ky_cles1_a BETWEEN COALESCE(upper( lettD), '') AND CASE WHEN lettA ISNULL THEN 'ZZZZ' WHEN lettA='' THEN 'ZZZZ' ELSE UPPER( lettA) END
          AND au.cd_livello BETWEEN COALESCE( liv_autD, '') AND CASE WHEN liv_autA ISNULL THEN '97' WHEN liv_autA='' THEN '97' ELSE liv_autA END
          AND (tp_nome ISNULL OR tp_nome ='' OR CASE WHEN tp_nome='P' THEN au.tp_nome_aut in ('A','B','C','D') WHEN tp_nome='F' THEN au.tp_nome_aut in ('E','R','G') ELSE au.tp_nome_aut = tp_nome END)
          ORDER BY 1
        LOOP
        RETURN NEXT idautore ;
      END LOOP;
    ELSE
      IF fl_titleg='n' THEN
        FOR idautore IN
          SELECT CAST(au.VID as text) from "sbnweb"."tb_autore" au
          LEFT OUTER JOIN "sbnweb"."tr_tit_aut" ta  on ta.vid = au.vid and not ta.fl_canc='S'
          WHERE au.fl_canc <> 'S'
            AND au.tp_forma_aut = 'A'
            AND (flcon='' or au.fl_condiviso= flcon)
            AND CAST(au.ts_ins as date) BETWEEN COALESCE( dataD, to_timestamp('10010101','YYYYMMDD')) AND COALESCE( dataA, to_timestamp('99990101','YYYYMMDD'))
            AND au.ky_cles1_a BETWEEN COALESCE(upper( lettD), '') AND CASE WHEN lettA ISNULL THEN 'ZZZZ' WHEN lettA='' THEN 'ZZZZ' ELSE UPPER( lettA) END
            AND au.cd_livello BETWEEN COALESCE( liv_autD, '') AND CASE WHEN liv_autA ISNULL THEN '97' WHEN liv_autA='' THEN '97' ELSE liv_autA END
            AND (tp_nome ISNULL OR tp_nome ='' OR CASE WHEN tp_nome='P' THEN au.tp_nome_aut in ('A','B','C','D') WHEN tp_nome='F' THEN au.tp_nome_aut in ('E','R','G') ELSE au.tp_nome_aut = tp_nome END)
            AND ta.bid isnull
          ORDER BY 1
          LOOP
          RETURN NEXT idautore ;
        END LOOP;
    ELSE
         FOR idautore IN
           SELECT CAST(au.VID as text) from "sbnweb"."tb_autore" au
           WHERE au.fl_canc <> 'S'
             AND au.tp_forma_aut = 'A'
             AND (flcon='' or au.fl_condiviso= flcon)
             AND CAST(au.ts_ins as date) BETWEEN COALESCE( dataD, to_timestamp('10010101','YYYYMMDD')) AND COALESCE( dataA, to_timestamp('99990101','YYYYMMDD'))
             AND au.ky_cles1_a BETWEEN COALESCE(upper( lettD), '') AND CASE WHEN lettA ISNULL THEN 'ZZZZ' WHEN lettA='' THEN 'ZZZZ' ELSE UPPER( lettA) END
             AND au.cd_livello BETWEEN COALESCE( liv_autD, '') AND CASE WHEN liv_autA ISNULL THEN '97' WHEN liv_autA='' THEN '97' ELSE liv_autA END
             AND (tp_nome ISNULL OR tp_nome ='' OR CASE WHEN tp_nome='P' THEN au.tp_nome_aut in ('A','B','C','D') WHEN tp_nome='F' THEN au.tp_nome_aut in ('E','R','G') ELSE au.tp_nome_aut = tp_nome END)
           ORDER BY 1
           LOOP
           RETURN NEXT idautore ;
         END LOOP;
      END IF;
    END IF;
    RETURN ;
    END
    $body$
    LANGUAGE plpgsql;
--
-- Definition for function estrai_area_da_isbd (OID = 265984) : 
--
CREATE FUNCTION sbnweb.estrai_area_da_isbd (
  isbd_completo character varying,
  indice_aree character varying,
  tag_area character varying
)
RETURNS varchar
AS 
$body$
DECLARE
  indice_aree varchar;
  isbd_completo varchar;
  tag varchar;
  pos_area integer;
  pos_inizio integer;
  length_area integer;
  testo_area VARCHAR;  
BEGIN
isbd_completo = $1;
indice_aree = $2;
tag = $3 || '-';
pos_area = (select position(tag in indice_aree));
if pos_area > 0 then 
    pos_inizio = (select substring(indice_aree, pos_area + 4, 4)::integer);
    if (select length(indice_aree))> pos_area + 8 THEN
       length_area = (select (substring(indice_aree, pos_area + 13, 4)::integer - (pos_inizio + 3))::integer );
       testo_area = (select substring(isbd_completo, pos_inizio, length_area ));
    ELSE  
       testo_area = (select substring(isbd_completo, pos_inizio ));
    END IF;
else
    testo_area = '';
end if;

RETURN rtrim(testo_area);
END;
$body$
    LANGUAGE plpgsql;
--
-- Definition for function cerca_titolo_superiore (OID = 265985) : 
--
CREATE FUNCTION sbnweb.cerca_titolo_superiore (
  id_input character varying,
  nro_char integer
)
RETURNS varchar
AS 
$body$
DECLARE
  titolo_raggr varchar;
  id_base varchar;
  pos_aut integer;
    
BEGIN
id_base = $1;
titolo_raggr = 
   (SELECT  estrai_area_da_isbd(ti_funct.isbd, ti_funct.indice_isbd, '200')
    FROM "sbnweb"."tr_tit_tit" tt_funct
    JOIN tb_titolo ti_funct on ti_funct.bid=tt_funct.bid_coll
     where NOT tt_funct.fl_canc='S'::text
       AND tt_funct.bid_base = id_base
       AND tt_funct.cd_natura_base in ('W'::text, 'M'::text)
       AND tt_funct.tp_legame ='01'::text
       AND tt_funct.cd_natura_coll = 'M'::text
       LIMIT 1);
IF titolo_raggr is null THEN
   titolo_raggr = '';
ELSE
   pos_aut = (select position('/' in titolo_raggr));
   IF pos_aut > 0 THEN
      titolo_raggr= rtrim(SUBSTRING(titolo_raggr,1,pos_aut -1)) ;
   END IF;   
END IF;
IF $2 > 0 AND length(titolo_raggr)> $2 + 3 THEN
   titolo_raggr = substring(titolo_raggr,1,$2)||'...';
END IF;
RETURN titolo_raggr;
END
$body$
    LANGUAGE plpgsql;
--
-- Definition for function concatena_aree (OID = 265986) : 
--
CREATE FUNCTION sbnweb.concatena_aree (
  isbd character varying,
  indice_isbd character varying,
  tags character varying[],
  max_char_tit integer
)
RETURNS varchar
AS 
$body$
DECLARE
 stringa_isbd VARCHAR;
 indice_aree VARCHAR;
 input_tags VARCHAR[];
 stringa_output VARCHAR;
 posizione integer ;
 tot_tags integer ;
 area varchar;
 separatore varchar;
BEGIN
  stringa_isbd = $1;
  indice_aree=$2;
  input_tags=$3;
  tot_tags = array_upper(input_tags,1) ;
  posizione = 1;
  stringa_output = '';
  LOOP
    area = estrai_area_da_isbd(stringa_isbd, indice_aree, input_tags[posizione] );
    IF $4 > 0 and length(area) > $4 + 3 THEN
        area = substring(area, 1 , $4) || '...';
    END IF;
    IF area > '' THEN
       IF posizione = 1 THEN 
          stringa_output = area;
       ELSE 
          IF input_tags[posizione] = '300' THEN 
             separatore = ' ((';
          ELSE 
             separatore = ' - ';
          END IF;
          stringa_output = stringa_output||separatore||area;
       END IF;
    END IF;
    posizione = posizione +1;
    EXIT WHEN posizione > tot_tags;  
  END LOOP;
  return stringa_output;
  

END;
$body$
    LANGUAGE plpgsql;
--
-- Definition for function concatena_aree (OID = 265987) : 
--
CREATE FUNCTION sbnweb.concatena_aree (
  isbd character varying,
  indice_isbd character varying,
  tags character varying[]
)
RETURNS varchar
AS 
$body$
BEGIN
 return concatena_aree ($1,$2,$3, 0);
END;
$body$
    LANGUAGE plpgsql;
--
-- Definition for function concatena_aree (OID = 265988) : 
--
CREATE FUNCTION sbnweb.concatena_aree (
  isbd character varying,
  indice_isbd character varying,
  tags character varying[],
  id_titolo character varying,
  pos_titolo integer,
  label_ini character varying,
  label_end character varying,
  nro_char_tit integer
)
RETURNS varchar
AS 
$body$
DECLARE
 stringa_output VARCHAR;
 titolo varchar;
 aree varchar;
BEGIN
  titolo = cerca_titolo_superiore($4, $8);
  aree = concatena_aree ($1, $2, $3, $8);
   IF titolo > '' THEN
      IF $5 = 0 THEN
         stringa_output = $6||titolo||$7||aree;
      ELSE
         stringa_output = aree||$6||titolo||$7;
      END IF;
  ELSE
     stringa_output = aree;
  END IF;
  return stringa_output;
  

END;
$body$
    LANGUAGE plpgsql;
--
-- Definition for function calcola_import_id_link (OID = 265989) : 
--
CREATE FUNCTION sbnweb.calcola_import_id_link (
  import1_tag character varying,
  import1_dati text,
  import1_210 text
)
RETURNS varchar
AS 
$body$
DECLARE
id_link_id_input varchar;
appo_dati varchar;
appo_dati_2 varchar;
appo_cod_sist varchar;
appo_split VARCHAR [];
ind_split INTEGER;
max_array integer;
BEGIN
IF substring(import1_tag,1,1) = '6' THEN
   /*Trattamento etichette 6XX*/
   IF substring(import1_tag,2,1) < '7' then
      /*Trattamento soggetti*/
      appo_dati = regexp_replace(import1_dati, E'\\$.','');
      IF position('$3' in appo_dati) > 0 then
         appo_dati = split_part(appo_dati, '$3', 1)||
         case when position('$' in split_part(appo_dati, '$3', 2))>0 
              then SUBSTRING(split_part(appo_dati, '$3', 2),position('$' in split_part(appo_dati, '$3', 2)))
              else '' 
         END;
      END IF;  
      IF position('$2' in appo_dati) > 0 then
         appo_cod_sist = trim(split_part(split_part(appo_dati, '$2', 2),'$', 1));
         appo_dati = split_part(appo_dati, '$2', 1)||
         case when position('$' in split_part(appo_dati, '$2', 2))>0 
              then SUBSTRING(split_part(appo_dati, '$2', 2),position('$' in split_part(appo_dati, '$2', 2)))
              else '' 
         END;
         else 
         appo_cod_sist='FIR';
      END IF; 
      id_link_id_input = '6XX'||regexp_replace(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE
                         (appo_dati
                         , E'\x1BH',''), E'\x1BI','*'),'$b', ', '), '$c', ''), '$f', '')
                         , E'\\$.','- ','g');
      id_link_id_input = id_link_id_input||' ['||appo_cod_sist||']';
   ELSE
      /*Trattamento CLASSI*/
      IF substring(import1_tag,2,2) = '76' THEN
         /*CLASSI DEWEY*/
         appo_dati = import1_dati;
         appo_cod_sist = TRIM(SUBSTRING(split_part(split_part(appo_dati, '$v', 2),'$', 1),1,2));
         IF appo_cod_sist > ''  THEN
            appo_cod_sist = 'D'||LPAD(appo_cod_sist,2,'0');
   	     ELSE
            appo_cod_sist = 'D21';
         END IF;
         id_link_id_input='676'||appo_cod_sist||split_part(split_part(appo_dati, '$a', 2),'$', 1);
      ELSEIF substring(import1_tag,2,2) = '86' THEN
         /*CLASSI diverse da DEWEY*/
         appo_dati = import1_dati;
         appo_cod_sist = TRIM(SUBSTRING(split_part(split_part(appo_dati, '$2', 2),'$', 1),1,3));
         IF appo_cod_sist > ''  THEN
            appo_cod_sist = RPAD(appo_cod_sist,3,' ');
   	     ELSE
            appo_cod_sist = '???';
         END IF;
         id_link_id_input='686'||appo_cod_sist||split_part(split_part(appo_dati, '$a', 2),'$', 1);
      ELSE
         /*CLASSI non gestite*/
         id_link_id_input= import1_tag||': etichetta non gestita';
      END IF;   
   END IF;
ELSE
   	IF substring(import1_tag,1,1) = '7' THEN
        appo_dati = import1_dati;
        appo_split=regexp_split_to_array (appo_dati, E'\\$');
        ind_split=1;
        max_array=array_upper(appo_split,1);
        appo_dati='';
        WHILE not ind_split > max_array   LOOP
        appo_cod_sist=trim(appo_split[ind_split]);
    	IF length(appo_cod_sist) > 1 THEN
           IF upper(SUBSTRING(appo_cod_sist,1,1)) in ( 'A', 'B','C','D', 'E', 'F', 'G', 'H', 'P') then
        	 	appo_dati = appo_dati||' '||SUBSTRING(appo_cod_sist,2);
           END IF;
        END IF;
        ind_split=ind_split+1;
    	END loop;

    	/* 
       	IF position('$4' in import1_dati) > 0 then
        	appo_dati = split_part(import1_dati, '$4', 1)||
            CASE WHEN position('$' in split_part(import1_dati, '$4', 2))>0 
            THEN SUBSTRING(split_part(import1_dati, '$4', 2),position('$' in split_part(import1_dati, '$4', 2)))
            ELSE '' 
            END;
        ELSE
         	appo_dati=import1_dati;
    	END IF; 
     	IF position('$3' in appo_dati) > 0 then
       	appo_dati = split_part(appo_dati, '$3', 1)||
            CASE WHEN position('$' in split_part(appo_dati, '$3', 2))>0 
            THEN SUBSTRING(split_part(appo_dati, '$3', 2),position('$' in split_part(appo_dati, '$3', 2)))
            ELSE '' 
            END;        
        END IF;
       
		id_link_id_input =regexp_replace(REPLACE(REPLACE(REPLACE
                         (appo_dati, E'\x1BH',''), E'\x1BI',''), E'\x39',' ') , E'\\$.',' ', 'g');
         */
         id_link_id_input = SUBSTRING(import1_tag,1,2)||'X'||ltrim(appo_dati);
      ELSIF substring(import1_tag,1,1) = '4' THEN
        IF position ('$1200' in import1_dati) > 0 THEN 
           appo_dati = split_part(import1_dati, '$1200', 2);
           IF position ('$1' in appo_dati) > 0 THEN
           	  appo_dati_2 = split_part(appo_dati, '$1', 2);
              appo_dati = split_part(appo_dati, '$1', 1);
           END IF;
        ELSE
           appo_dati = import1_dati;
        END IF;
      	appo_split=regexp_split_to_array (appo_dati, E'\\$');
        ind_split=1;
        max_array=array_upper(appo_split,1);
        appo_dati='';
        WHILE not ind_split > max_array   LOOP
        appo_cod_sist=trim(appo_split[ind_split]);
    	IF length(appo_cod_sist) > 1 THEN
           IF upper(SUBSTRING(appo_cod_sist,1,1)) in ( 'A', 'B','C','E','I', 'F', 'H') then
        	 	appo_dati = appo_dati||' '||SUBSTRING(appo_cod_sist,2);
           END IF;
        END IF;
        ind_split=ind_split+1;
    	END loop;
        IF appo_dati_2 > '' THEN 
      		appo_split=regexp_split_to_array (appo_dati_2, E'\\$');
        	ind_split=1;
        	max_array=array_upper(appo_split,1);
        	WHILE NOT ind_split > max_array   LOOP
        	appo_cod_sist=trim(appo_split[ind_split]);
    		IF length(appo_cod_sist) > 1 THEN
           		IF upper(SUBSTRING(appo_cod_sist,1,1)) in ( 'I', 'H') then
        	 		appo_dati = appo_dati||' '||SUBSTRING(appo_cod_sist,2);
           		END IF;
            END IF;
            ind_split=ind_split+1;
    		END loop;
        END IF;
        IF import1_tag ='410' THEN
           IF import1_210 >'' then
              appo_split=regexp_split_to_array (import1_210, E'\\$');
         	  ind_split=1;
        	  max_array=array_upper(appo_split,1);
              appo_dati_2='';
        	  WHILE not ind_split > max_array   LOOP
              appo_cod_sist=trim(appo_split[ind_split]);
    	      IF length(appo_cod_sist) > 1 THEN
                 IF upper(SUBSTRING(appo_cod_sist,1,1)) in ( 'A', 'C') then
        	 	    appo_dati_2 = appo_dati_2||' '||SUBSTRING(appo_cod_sist,2);
                 END IF;
              END IF;
              ind_split=ind_split+1;
    		  END loop;
           	  appo_dati = appo_dati||' '||appo_dati_2;
           end if;
        END IF;
		id_link_id_input = import1_tag||Ltrim(appo_dati);
	  
      ELSIF substring(import1_tag,1,1) = '5' THEN
		
        appo_dati = import1_dati;
        appo_split=regexp_split_to_array (appo_dati, E'\\$');
        ind_split=1;
        max_array=array_upper(appo_split,1);
        appo_dati='';
        WHILE not ind_split > max_array   LOOP
        appo_cod_sist=trim(appo_split[ind_split]);
    	IF length(appo_cod_sist) > 1 THEN
           IF upper(SUBSTRING(appo_cod_sist,1,1)) in ( 'A', 'B', 'H', 'I', 'J', 'K', 'L') then
        	 	appo_dati = appo_dati||' '||SUBSTRING(appo_cod_sist,2);
           END IF;
        END IF;
        ind_split=ind_split+1;
    	END loop;
		id_link_id_input = case when SUBSTRING(import1_tag,1,2)='51' then '51x' 
                           else import1_tag END||ltrim(appo_dati);
	  ELSE 
		id_link_id_input = import1_tag ||': tag inatteso';
    END IF;
END IF;
id_link_id_input =translate(id_link_id_input, 'àáâãäèéêëìîïòóôõöùûüÁÂÃÄÀÈÉÊËÌÍÎÏÒÓÔÕÖÙÚÛÜç-,.:<>*"','aaaaaeeeeiiiooooouuuAAAAAEEEEIIIIOOOOOUUUUc ');

id_link_id_input =regexp_replace(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(
TRIM(id_link_id_input), E'\x1BH',''), E'\x1BI',''),
 ' ,', ','), ',,', ','), '..', '.'), E'\x3B',' '), ' +', ' ', 'g');

RETURN upper(id_link_id_input);
END
$body$
    LANGUAGE plpgsql;
--
-- Definition for function calcola_import_id_link (OID = 265991) : 
--
CREATE FUNCTION sbnweb.calcola_import_id_link (
  import1_tag character varying,
  import1_dati text
)
RETURNS varchar
AS 
$body$
DECLARE
BEGIN
return  calcola_import_id_link ($1, $2, '');
END
$body$
    LANGUAGE plpgsql;
--
-- Definition for function concatena_uri (OID = 600090) : 
--
CREATE FUNCTION sbnweb.concatena_uri (
  bid character varying,
  bib character varying,
  dti timestamp without time zone,
  dtf timestamp without time zone,
  dig character varying,
  teca character varying
)
RETURNS varchar
AS 
$body$
DECLARE
  uri VARCHAR;
  inv VARCHAR;
  dti TIMESTAMP;
  dtf TIMESTAMP;
  dig VARCHAR;
  teca VARCHAR;
  
  
BEGIN
	uri:='';
	for inv in (select invv.id_accesso_remoto 
    from sbnweb.tbc_inventario invv
    where invv.fl_canc<>'S' and invv.bid=$1 
    and ('' in ($2) or invv.cd_bib in ($2))
    and invv.id_accesso_remoto >' ' 
    and ('' in ($5) or invv.digitalizzazione in ($5))
    and ('' in ($6) or invv.rif_teca_digitale in ($6))
    and invv.ts_var between COALESCE($3, to_timestamp('10010101','YYYYMMDD')) 
		and COALESCE($4, to_timestamp('99990101','YYYYMMDD'))
    order by invv.id_accesso_remoto)
    LOOP
	    IF uri > '' THEN
			uri := uri||' | '||trim(inv);
    	ELSE
     		uri := trim(inv);
  		END IF;
    END Loop;
    
RETURN uri;   

END;
$body$
    LANGUAGE plpgsql STABLE;
	

--
-- Definition for function listaidautori_bib : 
--

CREATE OR REPLACE FUNCTION "sbnweb"."listaidautori_bib" (flcon char, datad timestamptz, dataa timestamptz, lettd text, letta text, liv_autd char, liv_auta char, tp_nome char, lista_bib text) RETURNS SETOF text AS
$body$
DECLARE
     fl_titleg char;
     idautore text;
	 app_polo char(3);
	 arr_bib text[];

    BEGIN

	IF lista_bib >'' THEN
	  app_polo = (SELECT po.cd_polo from "sbnweb"."tbf_polo" po);
	  arr_bib = string_to_array(lista_bib, ', ');
      
	  FOR idautore IN
        SELECT CAST(au.VID as text) from "sbnweb"."tb_autore" au
        INNER JOIN "sbnweb"."tr_tit_aut" ta  on ta.vid = au.vid and not ta.fl_canc='S'
		INNER JOIN "sbnweb"."tr_tit_bib" tb  on ta.bid = tb.bid and not tb.fl_canc='S'
        WHERE au.fl_canc <> 'S'
          AND au.tp_forma_aut = 'A'
          AND (flcon='' or au.fl_condiviso= flcon)
          AND CAST(au.ts_ins as date) BETWEEN COALESCE( dataD, to_timestamp('10010101','YYYYMMDD')) AND COALESCE( dataA, to_timestamp('99990101','YYYYMMDD'))
          AND au.ky_cles1_a BETWEEN COALESCE(upper( lettD), '') AND CASE WHEN lettA ISNULL THEN 'ZZZZ' WHEN lettA='' THEN 'ZZZZ' ELSE UPPER( lettA) END
          AND au.cd_livello BETWEEN COALESCE( liv_autD, '') AND CASE WHEN liv_autA ISNULL THEN '97' WHEN liv_autA='' THEN '97' ELSE liv_autA END
          AND (tp_nome ISNULL OR tp_nome ='' OR CASE WHEN tp_nome='P' THEN au.tp_nome_aut in ('A','B','C','D') WHEN tp_nome='F' THEN au.tp_nome_aut in ('E','R','G') ELSE au.tp_nome_aut = tp_nome END)
		  AND tb.cd_polo = app_polo 
		  AND CAST(tb.cd_biblioteca as text)  = ANY(arr_bib)
          ORDER BY 1
        LOOP
        RETURN NEXT idautore ;
      END LOOP;
	/*END IF;*/
     
    ELSE
     IF fl_titleg='s' THEN
      FOR idautore IN
        SELECT CAST(au.VID as text) from "sbnweb"."tb_autore" au
        INNER JOIN "sbnweb"."tr_tit_aut" ta  on ta.vid = au.vid and not ta.fl_canc='S'
        WHERE au.fl_canc <> 'S'
          AND au.tp_forma_aut = 'A'
          AND (flcon='' or au.fl_condiviso= flcon)
          AND CAST(au.ts_ins as date) BETWEEN COALESCE( dataD, to_timestamp('10010101','YYYYMMDD')) AND COALESCE( dataA, to_timestamp('99990101','YYYYMMDD'))
          AND au.ky_cles1_a BETWEEN COALESCE(upper( lettD), '') AND CASE WHEN lettA ISNULL THEN 'ZZZZ' WHEN lettA='' THEN 'ZZZZ' ELSE UPPER( lettA) END
          AND au.cd_livello BETWEEN COALESCE( liv_autD, '') AND CASE WHEN liv_autA ISNULL THEN '97' WHEN liv_autA='' THEN '97' ELSE liv_autA END
          AND (tp_nome ISNULL OR tp_nome ='' OR CASE WHEN tp_nome='P' THEN au.tp_nome_aut in ('A','B','C','D') WHEN tp_nome='F' THEN au.tp_nome_aut in ('E','R','G') ELSE au.tp_nome_aut = tp_nome END)
          ORDER BY 1
        LOOP
        RETURN NEXT idautore ;
      END LOOP;
     ELSE
      IF fl_titleg='n' THEN
        FOR idautore IN
          SELECT CAST(au.VID as text) from "sbnweb"."tb_autore" au
          LEFT OUTER JOIN "sbnweb"."tr_tit_aut" ta  on ta.vid = au.vid and not ta.fl_canc='S'
          WHERE au.fl_canc <> 'S'
            AND au.tp_forma_aut = 'A'
            AND (flcon='' or au.fl_condiviso= flcon)
            AND CAST(au.ts_ins as date) BETWEEN COALESCE( dataD, to_timestamp('10010101','YYYYMMDD')) AND COALESCE( dataA, to_timestamp('99990101','YYYYMMDD'))
            AND au.ky_cles1_a BETWEEN COALESCE(upper( lettD), '') AND CASE WHEN lettA ISNULL THEN 'ZZZZ' WHEN lettA='' THEN 'ZZZZ' ELSE UPPER( lettA) END
            AND au.cd_livello BETWEEN COALESCE( liv_autD, '') AND CASE WHEN liv_autA ISNULL THEN '97' WHEN liv_autA='' THEN '97' ELSE liv_autA END
            AND (tp_nome ISNULL OR tp_nome ='' OR CASE WHEN tp_nome='P' THEN au.tp_nome_aut in ('A','B','C','D') WHEN tp_nome='F' THEN au.tp_nome_aut in ('E','R','G') ELSE au.tp_nome_aut = tp_nome END)
            AND ta.bid isnull
          ORDER BY 1
          LOOP
          RETURN NEXT idautore ;
        END LOOP;
     ELSE
         FOR idautore IN
           SELECT CAST(au.VID as text) from "sbnweb"."tb_autore" au
           WHERE au.fl_canc <> 'S'
             AND au.tp_forma_aut = 'A'
             AND (flcon='' or au.fl_condiviso= flcon)
             AND CAST(au.ts_ins as date) BETWEEN COALESCE( dataD, to_timestamp('10010101','YYYYMMDD')) AND COALESCE( dataA, to_timestamp('99990101','YYYYMMDD'))
             AND au.ky_cles1_a BETWEEN COALESCE(upper( lettD), '') AND CASE WHEN lettA ISNULL THEN 'ZZZZ' WHEN lettA='' THEN 'ZZZZ' ELSE UPPER( lettA) END
             AND au.cd_livello BETWEEN COALESCE( liv_autD, '') AND CASE WHEN liv_autA ISNULL THEN '97' WHEN liv_autA='' THEN '97' ELSE liv_autA END
             AND (tp_nome ISNULL OR tp_nome ='' OR CASE WHEN tp_nome='P' THEN au.tp_nome_aut in ('A','B','C','D') WHEN tp_nome='F' THEN au.tp_nome_aut in ('E','R','G') ELSE au.tp_nome_aut = tp_nome END)
           ORDER BY 1
           LOOP
           RETURN NEXT idautore ;
         END LOOP;
      END IF;
     END IF;
    END IF; /* fine IF biblio */
    RETURN ;
    END
$body$
LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;
	
	
--
-- Structure for table jms_messages (OID = 58393) : 
--
CREATE TABLE sbnweb.jms_messages (
    messageid integer NOT NULL,
    destination varchar(150) NOT NULL,
    txid integer,
    txop char(1),
    messageblob bytea
) WITHOUT OIDS;
--
-- Definition for sequence jms_transactions_txid_seq (OID = 58399) : 
--
CREATE SEQUENCE sbnweb.jms_transactions_txid_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table jms_transactions (OID = 58401) : 
--
CREATE TABLE sbnweb.jms_transactions (
    txid integer DEFAULT nextval('jms_transactions_txid_seq'::regclass) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_abstract (OID = 58408) : 
--
CREATE TABLE sbnweb.tb_abstract (
    bid char(10) NOT NULL,
    ds_abstract varchar(2160) NOT NULL,
    cd_livello char(2) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_arte_tridimens (OID = 58414) : 
--
CREATE TABLE sbnweb.tb_arte_tridimens (
    bid char(10) NOT NULL,
    cd_designazione char(2) NOT NULL,
    tp_materiale_1 char(2),
    tp_materiale_2 char(2),
    tp_materiale_3 char(2),
    cd_colore char(1),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_autore (OID = 58420) : 
--
CREATE TABLE sbnweb.tb_autore (
    vid char(10) NOT NULL,
    isadn char(30),
    tp_forma_aut char(1) NOT NULL,
    ky_cautun char(6) NOT NULL,
    ky_auteur char(10) NOT NULL,
    ky_el1_a char(3),
    ky_el1_b char(3),
    ky_el2_a char(3),
    ky_el2_b char(3),
    tp_nome_aut char(1) NOT NULL,
    ky_el3 char(6),
    ky_el4 char(6),
    ky_el5 char(6),
    ky_cles1_a char(50) NOT NULL,
    ky_cles2_a char(30),
    cd_paese char(2),
    cd_lingua char(3),
    aa_nascita char(4),
    aa_morte char(4),
    cd_livello char(2) NOT NULL,
    fl_speciale char(1) NOT NULL,
    ds_nome_aut varchar(320) NOT NULL,
    cd_agenzia char(6),
    cd_norme_cat varchar(10),
    nota_aut varchar(320),
    nota_cat_aut varchar(1920),
    vid_link char(10),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    ute_forza_ins char(12),
    ute_forza_var char(12),
    fl_canc char(1) NOT NULL,
    fl_condiviso char(1) DEFAULT '''S''::bpchar'::bpchar NOT NULL,
    ute_condiviso char(12),
    ts_condiviso timestamp(6) without time zone,
    tidx_vector tsvector
) WITHOUT OIDS;
--
-- Structure for table tb_cartografia (OID = 58427) : 
--
CREATE TABLE sbnweb.tb_cartografia (
    bid char(10) NOT NULL,
    cd_livello char(2) NOT NULL,
    tp_pubb_gov char(1),
    cd_colore char(1),
    cd_meridiano char(2),
    cd_supporto_fisico char(2),
    cd_tecnica char(1),
    cd_forma_ripr char(1),
    cd_forma_pubb char(1),
    cd_altitudine char(1),
    cd_tiposcala char(1),
    tp_scala char(1),
    scala_oriz varchar(10),
    scala_vert varchar(10),
    longitudine_ovest varchar(8),
    longitudine_est varchar(8),
    latitudine_nord varchar(8),
    latitudine_sud varchar(8),
    tp_immagine char(1),
    cd_forma_cart char(1),
    cd_piattaforma char(1),
    cd_categ_satellite char(1),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    tp_proiezione CHAR(2)

) WITHOUT OIDS;


COMMENT ON COLUMN sbnweb.tb_cartografia.tp_proiezione IS 'proiezione delle carte';

--
-- Structure for table tb_classe (OID = 58430) : 
--
CREATE TABLE sbnweb.tb_classe (
    cd_sistema char(3) NOT NULL,
    cd_edizione char(2) NOT NULL,
    classe char(31) NOT NULL,
    ds_classe varchar(240),
    cd_livello char(2) NOT NULL,
    fl_costruito char(1),
    fl_speciale char(1) NOT NULL,
    ky_classe_ord char(34),
    suffisso varchar(10),
    ult_term varchar(240),
    fl_condiviso char(1) DEFAULT '''N''::bpchar'::bpchar NOT NULL,
    ute_condiviso char(12),
    ts_condiviso timestamp(6) without time zone,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    tidx_vector tsvector
) WITHOUT OIDS;
--
-- Structure for table tb_codici (OID = 58437) : 
--
CREATE TABLE sbnweb.tb_codici (
    tp_tabella char(4) NOT NULL,
    cd_tabella char(14) NOT NULL,
    cd_valore char(10),
    ds_tabella varchar(160) NOT NULL,
    cd_unimarc varchar(10),
    cd_marc_21 varchar(10),
    tp_materiale char(1),
    dt_fine_validita date,
    ds_cdsbn_ulteriore varchar(255),
    cd_flg1 varchar(255),
    cd_flg2 varchar(255),
    cd_flg3 varchar(255),
    cd_flg4 varchar(255),
    cd_flg5 varchar(255),
    cd_flg6 varchar(255),
    cd_flg7 varchar(255),
    cd_flg8 varchar(255),
    cd_flg9 varchar(255),
    cd_flg10 varchar(255),
    cd_flg11 varchar(255)
) WITHOUT OIDS;
--
-- Structure for table tb_composizione (OID = 58443) : 
--
CREATE TABLE sbnweb.tb_composizione (
    bid char(10) NOT NULL,
    cd_forma_1 char(4),
    cd_forma_2 char(4),
    cd_forma_3 char(4),
    numero_ordine char(20),
    numero_opera char(20),
    numero_cat_tem char(20),
    cd_tonalita char(2),
    datazione char(10),
    aa_comp_1 char(4),
    aa_comp_2 char(4),
    ds_sezioni char(20),
    ky_ord_ric char(10),
    ky_est_ric char(10),
    ky_app_ric char(10),
    ky_ord_clet char(6),
    ky_est_clet char(6),
    ky_app_clet char(6),
    ky_ord_pre char(20),
    ky_est_pre char(20),
    ky_app_pre char(20),
    ky_ord_den varchar(234),
    ky_est_den varchar(234),
    ky_app_den varchar(234),
    ky_ord_nor_pre varchar(240),
    ky_est_nor_pre varchar(240),
    ky_app_nor_pre varchar(240),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_descrittore (OID = 58449) : 
--
CREATE TABLE sbnweb.tb_descrittore (
    did char(10) NOT NULL,
    ds_descrittore varchar(240) NOT NULL,
    ky_norm_descritt varchar(240) NOT NULL,
    nota_descrittore varchar(240),
    cd_soggettario char(3) NOT NULL,
    tp_forma_des char(1) NOT NULL,
    cd_livello char(2) NOT NULL,
    fl_condiviso char(1) DEFAULT '''N''::bpchar'::bpchar NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    tidx_vector tsvector,
    cd_edizione char(1),
    cat_termine char(1)
) WITHOUT OIDS;
--
-- Structure for table tb_grafica (OID = 58459) : 
--
CREATE TABLE sbnweb.tb_grafica (
    bid char(10) NOT NULL,
    cd_livello char(2) NOT NULL,
    tp_materiale_gra char(1),
    cd_supporto char(1),
    cd_colore char(1),
    cd_tecnica_dis_1 char(2),
    cd_tecnica_dis_2 char(2),
    cd_tecnica_dis_3 char(2),
    cd_tecnica_sta_1 char(2),
    cd_tecnica_sta_2 char(2),
    cd_tecnica_sta_3 char(2),
    cd_design_funz char(2),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_impronta (OID = 58462) : 
--
CREATE TABLE sbnweb.tb_impronta (
    bid char(10) NOT NULL,
    impronta_1 char(10) NOT NULL,
    impronta_2 char(14) NOT NULL,
    impronta_3 char(8) NOT NULL,
    nota_impronta varchar(80),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_incipit (OID = 58465) : 
--
CREATE TABLE sbnweb.tb_incipit (
    bid char(10) NOT NULL,
    numero_mov char(2) NOT NULL,
    numero_p_mov char(2) NOT NULL,
    bid_letterario char(10),
    tp_indicatore char(1),
    numero_comp char(2),
    registro_mus char(9),
    nome_personaggio varchar(40),
    tempo_mus varchar(40),
    cd_forma char(4),
    cd_tonalita char(2),
    chiave_mus char(3),
    alterazione char(8),
    misura char(9),
    ds_contesto varchar(160),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_luogo (OID = 58468) : 
--
CREATE TABLE sbnweb.tb_luogo (
    lid char(10) NOT NULL,
    tp_forma char(1) NOT NULL,
    cd_livello char(2) NOT NULL,
    ds_luogo varchar(80) NOT NULL,
    ky_luogo char(30) NOT NULL,
    ky_norm_luogo char(80) NOT NULL,
    cd_paese char(2),
    nota_luogo varchar(320),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone DEFAULT now() NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone DEFAULT now() NOT NULL,
     fl_canc char(1) NOT NULL,
    nota_catalogatore varchar(320)
) WITHOUT OIDS;
--
-- Structure for table tb_marca (OID = 58476) : 
--
CREATE TABLE sbnweb.tb_marca (
    mid char(10) NOT NULL,
    cd_livello char(2) NOT NULL,
    fl_speciale char(1) NOT NULL,
    ds_marca varchar(160) NOT NULL,
    nota_marca varchar(320),
    ds_motto varchar(80),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    fl_condiviso char(1) DEFAULT '''S''::bpchar'::bpchar NOT NULL,
    ute_condiviso char(12),
    ts_condiviso timestamp(6) without time zone,
    tidx_vector tsvector
) WITHOUT OIDS;
--
-- Structure for table tb_microforma (OID = 58483) : 
--
CREATE TABLE sbnweb.tb_microforma (
    bid char(10) NOT NULL,
    cd_designazione char(1) NOT NULL,
    cd_polarita char(1),
    cd_dimensione char(1),
    cd_riduzione char(1),
    cd_riduzione_spec char(3),
    cd_colore char(1),
    cd_emulsione char(1),
    cd_generazione char(1),
    cd_base char(1),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_musica (OID = 58486) : 
--
CREATE TABLE sbnweb.tb_musica (
    bid char(10) NOT NULL,
    cd_livello char(2) NOT NULL,
    ds_org_sint varchar(80),
    ds_org_anal varchar(320),
    tp_elaborazione char(1),
    cd_stesura char(1),
    fl_composito char(1) NOT NULL,
    fl_palinsesto char(1) NOT NULL,
    datazione char(10),
    cd_presentazione char(2),
    cd_materia char(1),
    ds_illustrazioni varchar(120),
    notazione_musicale varchar(120),
    ds_legatura varchar(60),
    ds_conservazione varchar(100),
    tp_testo_letter char(1),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_nota (OID = 58492) : 
--
CREATE TABLE sbnweb.tb_nota (
    bid char(10) NOT NULL,
    tp_nota char(4) NOT NULL,
    progr_nota bigint NOT NULL,
    ds_nota varchar(1920) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_numero_std (OID = 58498) : 
--
CREATE TABLE sbnweb.tb_numero_std (
    bid char(10) NOT NULL,
    tp_numero_std char(2) NOT NULL,
    numero_std char(25) NOT NULL,
    numero_lastra numeric(25,0),
    cd_paese char(2),
    nota_numero_std varchar(30),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence seq_tb_parola (OID = 58501) : 
--
CREATE SEQUENCE sbnweb.seq_tb_parola
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tb_parola (OID = 58503) : 
--
CREATE TABLE sbnweb.tb_parola (
    id_parola integer DEFAULT nextval('seq_tb_parola'::regclass) NOT NULL,
    mid char(10) NOT NULL,
    parola char(10) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence seq_tb_personaggio (OID = 58507) : 
--
CREATE SEQUENCE sbnweb.seq_tb_personaggio
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tb_personaggio (OID = 58509) : 
--
CREATE TABLE sbnweb.tb_personaggio (
    id_personaggio integer DEFAULT nextval('seq_tb_personaggio'::regclass) NOT NULL,
    bid char(10) NOT NULL,
    nome_personaggio char(25) NOT NULL,
    cd_timbro_vocale char(14),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_rappresent (OID = 58513) : 
--
CREATE TABLE sbnweb.tb_rappresent (
    bid char(10) NOT NULL,
    tp_genere char(1),
    aa_rapp char(5),
    ds_periodo char(15),
    ds_teatro char(30),
    ds_luogo_rapp char(30),
    ds_occasione varchar(138),
    nota_rapp varchar(138),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tb_repertorio_id_repertorio_seq (OID = 58516) : 
--
CREATE SEQUENCE sbnweb.tb_repertorio_id_repertorio_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tb_repertorio (OID = 58518) : 
--
CREATE TABLE sbnweb.tb_repertorio (
    id_repertorio integer DEFAULT nextval('tb_repertorio_id_repertorio_seq'::regclass) NOT NULL,
    cd_sig_repertorio varchar(30) NOT NULL,
    ds_repertorio varchar(1200) NOT NULL,
    tp_repertorio char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    tidx_vector tsvector
) WITHOUT OIDS;
--
-- Structure for table tb_soggetto (OID = 58528) : 
--
CREATE TABLE sbnweb.tb_soggetto (
    cid char(10) NOT NULL,
    cd_soggettario char(3) NOT NULL,
    ds_soggetto varchar(240) NOT NULL,
    fl_speciale char(1) NOT NULL,
    ky_cles1_s varchar(240) NOT NULL,
    ky_primo_descr varchar(240) NOT NULL,
    cat_sogg char(1) NOT NULL,
    cd_livello char(2) NOT NULL,
    fl_condiviso char(1) DEFAULT '''S''::bpchar'::bpchar NOT NULL,
    ute_condiviso char(12) NOT NULL,
    ts_condiviso timestamp(6) without time zone NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    ky_cles2_s varchar(40),
    tidx_vector tsvector,
    nota_soggetto varchar(320),
    cd_edizione char(1)
) WITHOUT OIDS;
--
-- Structure for table tb_stat_parameter (OID = 58535) : 
--
CREATE TABLE sbnweb.tb_stat_parameter (
    id_stat integer NOT NULL,
    nome varchar(50) NOT NULL,
    tipo varchar(50) NOT NULL,
    valore varchar(100) NOT NULL,
    etichetta_nome varchar(50) NOT NULL,
    obbligatorio char(1) DEFAULT 'S'::bpchar NOT NULL
) WITH OIDS;
--
-- Structure for table tb_termine_thesauro (OID = 58539) : 
--
CREATE TABLE sbnweb.tb_termine_thesauro (
    did char(10) NOT NULL,
    cd_the char(3) NOT NULL,
    ds_termine_thesauro varchar(240) NOT NULL,
    nota_termine_thesauro varchar(240),
    ky_termine_thesauro varchar(240) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    tp_forma_the char(1) NOT NULL,
    cd_livello char(2) NOT NULL,
    fl_condiviso char(1) NOT NULL,
    tidx_vector tsvector
) WITHOUT OIDS;
--
-- Structure for table tb_titolo (OID = 58545) : 
--
CREATE TABLE sbnweb.tb_titolo (
    bid char(10) NOT NULL,
    isadn char(30),
    tp_materiale char(1) NOT NULL,
    tp_record_uni char(1),
    cd_natura char(1) NOT NULL,
    cd_paese char(2),
    cd_lingua_1 char(3),
    cd_lingua_2 char(3),
    cd_lingua_3 char(3),
    aa_pubb_1 char(4),
    aa_pubb_2 char(4),
    tp_aa_pubb char(1),
    cd_genere_1 char(2),
    cd_genere_2 char(2),
    cd_genere_3 char(2),
    cd_genere_4 char(2),
    ky_cles1_t char(6) NOT NULL,
    ky_cles2_t char(44) NOT NULL,
    ky_clet1_t char(3) NOT NULL,
    ky_clet2_t char(3) NOT NULL,
    ky_cles1_ct char(6) NOT NULL,
    ky_cles2_ct char(44) NOT NULL,
    ky_clet1_ct char(3) NOT NULL,
    ky_clet2_ct char(3) NOT NULL,
    cd_livello char(2) NOT NULL,
    fl_speciale char(1) NOT NULL,
    isbd varchar(1200) NOT NULL,
    indice_isbd varchar(80) NOT NULL,
    ky_editore varchar(80),
    cd_agenzia char(6),
    cd_norme_cat varchar(10),
    nota_inf_tit varchar(320),
    nota_cat_tit varchar(320),
    bid_link char(10),
    tp_link char(1),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    ute_forza_ins char(12),
    ute_forza_var char(12),
    fl_canc char(1) NOT NULL,
    cd_periodicita char(1),
    fl_condiviso char(1) DEFAULT '''S''::bpchar'::bpchar NOT NULL,
    ute_condiviso char(12) NOT NULL,
    ts_condiviso timestamp(6) without time zone NOT NULL
) WITHOUT OIDS;

CREATE TABLE sbnweb.tr_bid_altroid (
  bid CHAR(10) NOT NULL, 
  cd_istituzione CHAR(10) NOT NULL, 
  ist_id BIGINT NOT NULL, 
  ute_ins CHAR(12) NOT NULL, 
  ts_ins TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  ute_var CHAR(12) NOT NULL, 
  ts_var TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  fl_canc CHAR(1) NOT NULL, 
  CONSTRAINT tr_bid_altroid_pkey PRIMARY KEY(bid, cd_istituzione)
) WITHOUT OIDS;
--
-- Definition for sequence tba_buono_ordine_id_buono_ordine_seq (OID = 58552) : 
--
CREATE SEQUENCE sbnweb.tba_buono_ordine_id_buono_ordine_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tba_buono_ordine (OID = 58554) : 
--
CREATE TABLE sbnweb.tba_buono_ordine (
    id_buono_ordine integer DEFAULT nextval('tba_buono_ordine_id_buono_ordine_seq'::regclass) NOT NULL,
    id_capitoli_bilanci integer,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    buono_ord char(9) NOT NULL,
    cod_fornitore integer NOT NULL,
    stato_buono char(1) NOT NULL,
    data_buono date NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(12) NOT NULL,
    cod_mat char(1)
) WITHOUT OIDS;
--
-- Definition for sequence tba_cambi_ufficiali_id_valuta_seq (OID = 58558) : 
--
CREATE SEQUENCE sbnweb.tba_cambi_ufficiali_id_valuta_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tba_cambi_ufficiali (OID = 58560) : 
--
CREATE TABLE sbnweb.tba_cambi_ufficiali (
    id_valuta integer DEFAULT nextval('tba_cambi_ufficiali_id_valuta_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    valuta char(3) NOT NULL,
    cambio numeric(10,6) NOT NULL,
    data_var date NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tba_fatture_id_fattura_seq (OID = 58564) : 
--
CREATE SEQUENCE sbnweb.tba_fatture_id_fattura_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tba_fatture (OID = 58566) : 
--
CREATE TABLE sbnweb.tba_fatture (
    id_fattura integer DEFAULT nextval('tba_fatture_id_fattura_seq'::regclass) NOT NULL,
    cod_fornitore integer NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    anno_fattura numeric(4,0) NOT NULL,
    progr_fattura integer NOT NULL,
    num_fattura char(20) NOT NULL,
    data_fattura date NOT NULL,
    data_reg date NOT NULL,
    importo numeric(15,3) NOT NULL,
    sconto numeric(5,2) NOT NULL,
    valuta char(3) NOT NULL,
    cambio numeric(10,6) NOT NULL,
    stato_fattura char(1) NOT NULL,
    tipo_fattura char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tba_offerte_fornitore_id_offerte_fornitore_seq (OID = 58570) : 
--
CREATE SEQUENCE sbnweb.tba_offerte_fornitore_id_offerte_fornitore_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tba_offerte_fornitore (OID = 58572) : 
--
CREATE TABLE sbnweb.tba_offerte_fornitore (
    id_offerte_fornitore integer DEFAULT nextval('tba_offerte_fornitore_id_offerte_fornitore_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_fornitore integer NOT NULL,
    bid_p char(10) NOT NULL,
    tip_rec char(1) NOT NULL,
    natura char(1) NOT NULL,
    paese char(3) NOT NULL,
    lingua char(3) NOT NULL,
    tipo_data char(1) NOT NULL,
    data1 char(4) NOT NULL,
    num_standard char(11) NOT NULL,
    aut1 varchar(160) NOT NULL,
    k_aut1 varchar(80) NOT NULL,
    tip_aut1 char(1) NOT NULL,
    resp_aut1 char(1) NOT NULL,
    aut2 varchar(160) NOT NULL,
    k_aut2 varchar(80) NOT NULL,
    tip_aut2 char(1) NOT NULL,
    resp_aut2 char(1) NOT NULL,
    aut3 varchar(160) NOT NULL,
    k_aut3 varchar(80) NOT NULL,
    tip_aut3 char(1) NOT NULL,
    resp_aut3 char(1) NOT NULL,
    aut4 varchar(160) NOT NULL,
    k_aut4 varchar(80) NOT NULL,
    tip_aut4 char(1) NOT NULL,
    resp_aut4 char(1) NOT NULL,
    isbd_1 varchar(240) NOT NULL,
    isbd_2 varchar(240) NOT NULL,
    k_titolo char(50) NOT NULL,
    serie1 varchar(240) NOT NULL,
    k1_serie char(50) NOT NULL,
    num1_serie char(10) NOT NULL,
    serie2 varchar(240) NOT NULL,
    k2_serie char(50) NOT NULL,
    num2_serie char(10) NOT NULL,
    serie3 varchar(240) NOT NULL,
    k3_serie char(50) NOT NULL,
    num3_serie char(10) NOT NULL,
    tipo1_classe char(1) NOT NULL,
    classe1 char(31) NOT NULL,
    tipo2_classe char(1) NOT NULL,
    classe2 char(31) NOT NULL,
    tipo3_classe char(1) NOT NULL,
    classe3 char(31) NOT NULL,
    sog1 varchar(160) NOT NULL,
    k_sog1 varchar(120) NOT NULL,
    sog2 varchar(160) NOT NULL,
    k_sog2 varchar(120) NOT NULL,
    sog3 varchar(160) NOT NULL,
    k_sog3 varchar(120) NOT NULL,
    num_stand_pro char(35) NOT NULL,
    data_offerta date NOT NULL,
    num_offerta_g char(35) NOT NULL,
    num_linea numeric(6,0) NOT NULL,
    valuta char(3) NOT NULL,
    prezzo numeric(15,3) NOT NULL,
    tipo_prezzo char(3) NOT NULL,
    inf_sul_prezzo varchar(160) NOT NULL,
    altri_rif varchar(160) NOT NULL,
    note_edi varchar(160) NOT NULL,
    prot_edi char(35) NOT NULL,
    num_offerta_p char(35) NOT NULL,
    bid char(10) NOT NULL,
    stato_offerta char(1) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_ins char(12) NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tba_ordini_id_ordine_seq (OID = 58579) : 
--
CREATE SEQUENCE sbnweb.tba_ordini_id_ordine_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tba_ordini (OID = 58581) : 
--
CREATE TABLE sbnweb.tba_ordini (
    id_ordine integer DEFAULT nextval('tba_ordini_id_ordine_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_tip_ord char(1) NOT NULL,
    anno_ord numeric(4,0) NOT NULL,
    cod_ord integer NOT NULL,
    cod_fornitore integer NOT NULL,
    id_sez_acquis_bibliografiche integer,
    id_valuta integer,
    id_capitoli_bilanci integer,
    data_ins timestamp(6) without time zone NOT NULL,
    data_agg timestamp(6) without time zone NOT NULL,
    data_ord date,
    note varchar(160) NOT NULL,
    num_copie integer NOT NULL,
    continuativo char(1) NOT NULL,
    stato_ordine char(1) NOT NULL,
    tipo_doc_lett char(1),
    cod_doc_lett bigint,
    tipo_urgenza char(1) NOT NULL,
    cod_rich_off bigint,
    bid_p char(10),
    note_forn varchar(160) NOT NULL,
    tipo_invio char(1) NOT NULL,
    anno_1ord numeric(4,0),
    cod_1ord integer,
    prezzo numeric(15,3) NOT NULL,
    paese char(2) NOT NULL,
    cod_bib_sugg char(3),
    cod_sugg_bibl numeric(10,0),
    bid char(10),
    stato_abb char(1),
    cod_per_abb char(1),
    prezzo_lire numeric(15,3) NOT NULL,
    reg_trib char(50) NOT NULL,
    anno_abb numeric(4,0),
    num_fasc char(15) NOT NULL,
    data_fasc date,
    annata char(10) NOT NULL,
    num_vol_abb smallint,
    natura char(1) NOT NULL,
    data_fine date,
    stampato boolean DEFAULT false NOT NULL,
    rinnovato boolean DEFAULT false NOT NULL,
    data_chiusura_ord timestamp(0) without time zone,
    tbb_bilancicod_mat char(1),
    ute_ins char(12) DEFAULT 'uteins'::bpchar NOT NULL,
    ute_var char(12) DEFAULT 'utevar'::bpchar NOT NULL,
    ts_ins timestamp(0) without time zone DEFAULT now() NOT NULL,
    ts_var timestamp(0) without time zone DEFAULT now() NOT NULL,
    fl_canc char(1),
    cd_tipo_lav char(1)
) WITHOUT OIDS;
--
-- Structure for table tba_parametri_buono_ordine (OID = 58594) : 
--
CREATE TABLE sbnweb.tba_parametri_buono_ordine (
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    progr numeric(1,0) NOT NULL,
    codice_buono char(1) NOT NULL,
    descr_test varchar(240) NOT NULL,
    descr_foot varchar(240) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    descr_oggetto text,
    descr_formulaintr text,
    area_titolo char(1),
    area_ediz char(1),
    area_num char(1),
    area_pub char(1),
    logo boolean,
    logo_img varchar(100),
    prezzo boolean,
    nprot boolean,
    dataprot boolean,
    rinnovo char(1),
    firmadigit boolean,
    firmadigit_img varchar(100),
    ristampa boolean,
    xml_formulaintr text
) WITHOUT OIDS;
--
-- Structure for table tba_parametri_ordine (OID = 58600) : 
--
CREATE TABLE sbnweb.tba_parametri_ordine (
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    gest_bil char(1) NOT NULL,
    gest_sez char(1) NOT NULL,
    gest_prof char(1) NOT NULL,
    cod_prac numeric(10,0),
    cod_sezione char(7),
    esercizio numeric(4,0),
    capitolo numeric(4,0),
    cod_mat char(1),
    cod_fornitore_a integer,
    cod_fornitore_l integer,
    cod_fornitore_d integer,
    cod_fornitore_v integer,
    cod_fornitore_c integer,
    cod_fornitore_r integer,
    ordini_aperti char(1) NOT NULL,
    ordini_chiusi char(1) NOT NULL,
    ordini_annullati char(1) NOT NULL,
    allineamento_prezzo char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    cd_bib_google varchar(255),
    cd_forn_google integer
) WITHOUT OIDS;
--
-- Definition for sequence tba_profili_acquisto_cod_prac_seq (OID = 58603) : 
--
CREATE SEQUENCE sbnweb.tba_profili_acquisto_cod_prac_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tba_profili_acquisto (OID = 58605) : 
--
CREATE TABLE sbnweb.tba_profili_acquisto (
    cod_prac integer DEFAULT nextval('tba_profili_acquisto_cod_prac_seq'::regclass) NOT NULL,
    descr char(20) NOT NULL,
    paese char(2) NOT NULL,
    lingua char(3) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    ute_ins char(12) NOT NULL,
    ute_var char(12) NOT NULL,
    fl_canc char(1) NOT NULL,
    id_sez_acquis_bibliografiche integer NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tba_richieste_offerta_cod_rich_off_seq (OID = 58609) : 
--
CREATE SEQUENCE sbnweb.tba_richieste_offerta_cod_rich_off_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tba_richieste_offerta (OID = 58611) : 
--
CREATE TABLE sbnweb.tba_richieste_offerta (
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_rich_off bigint DEFAULT nextval('tba_richieste_offerta_cod_rich_off_seq'::regclass) NOT NULL,
    data_rich_off date NOT NULL,
    prezzo_rich numeric(15,3) NOT NULL,
    num_copie integer NOT NULL,
    note varchar(160) NOT NULL,
    stato_rich_off char(1) NOT NULL,
    bid char(10) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tba_righe_fatture (OID = 58615) : 
--
CREATE TABLE sbnweb.tba_righe_fatture (
    id_fattura integer NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    riga_fattura smallint NOT NULL,
    id_ordine integer,
    id_capitoli_bilanci integer,
    cod_mat char(1),
    note varchar(160) NOT NULL,
    importo_riga numeric(15,3) NOT NULL,
    sconto_1 numeric(5,2) NOT NULL,
    sconto_2 numeric(5,2) NOT NULL,
    cod_iva char(2) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    id_fattura_in_credito integer,
    riga_fattura_in_credito smallint
) WITHOUT OIDS;
--
-- Definition for sequence tba_sez_acquis_bibliografiche_id_sez_acquis_bibliografiche_seq (OID = 58618) : 
--
CREATE SEQUENCE sbnweb.tba_sez_acquis_bibliografiche_id_sez_acquis_bibliografiche_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tba_sez_acquis_bibliografiche (OID = 58620) : 
--
CREATE TABLE sbnweb.tba_sez_acquis_bibliografiche (
    id_sez_acquis_bibliografiche integer DEFAULT nextval('tba_sez_acquis_bibliografiche_id_sez_acquis_bibliografiche_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_sezione char(7) NOT NULL,
    nome char(30) NOT NULL,
    note varchar(255) NOT NULL,
    somma_disp numeric(15,3) NOT NULL,
    budget numeric(15,3) NOT NULL,
    anno_val numeric(4,0) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    data_val date
) WITHOUT OIDS;
--
-- Structure for table tba_suggerimenti_bibliografici (OID = 58624) : 
--
CREATE TABLE sbnweb.tba_suggerimenti_bibliografici (
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_sugg_bibl numeric(10,0) NOT NULL,
    data_sugg_bibl date NOT NULL,
    note varchar(160) NOT NULL,
    msg_per_bibl varchar(255) NOT NULL,
    note_forn varchar(160) NOT NULL,
    cod_bib_cs char(3) NOT NULL,
    bid char(10) NOT NULL,
    cod_bibliotecario numeric(9,0) NOT NULL,
    stato_sugg char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    id_sez_acquis_bibliografiche integer
) WITHOUT OIDS;
--
-- Structure for table tbb_bilanci (OID = 58630) : 
--
CREATE TABLE sbnweb.tbb_bilanci (
    cod_mat char(1) NOT NULL,
    id_capitoli_bilanci integer NOT NULL,
    id_buono_ordine integer,
    budget numeric(15,3) NOT NULL,
    ordinato numeric(15,3) NOT NULL,
    fatturato numeric(15,3) NOT NULL,
    pagato numeric(15,3) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    acquisito numeric(15,3)
) WITHOUT OIDS;
--
-- Definition for sequence tbb_capitoli_bilanci_id_capitoli_bilanci_seq (OID = 58633) : 
--
CREATE SEQUENCE sbnweb.tbb_capitoli_bilanci_id_capitoli_bilanci_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbb_capitoli_bilanci (OID = 58635) : 
--
CREATE TABLE sbnweb.tbb_capitoli_bilanci (
    id_capitoli_bilanci integer DEFAULT nextval('tbb_capitoli_bilanci_id_capitoli_bilanci_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    esercizio numeric(4,0) NOT NULL,
    capitolo numeric(16,0) NOT NULL,
    budget numeric(15,3) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(12) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbc_collocazione_key_loc_seq (OID = 58639) : 
--
CREATE SEQUENCE sbnweb.tbc_collocazione_key_loc_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbc_collocazione (OID = 58641) : 
--
CREATE TABLE sbnweb.tbc_collocazione (
    cd_sistema char(3),
    cd_edizione char(2),
    classe char(31),
    cd_biblioteca_sezione char(3) NOT NULL,
    cd_sez char(10) NOT NULL,
    cd_polo_sezione char(3) NOT NULL,
    cd_biblioteca_doc char(3),
    cd_polo_doc char(3),
    bid_doc char(10),
    cd_doc integer,
    key_loc integer DEFAULT nextval('tbc_collocazione_key_loc_seq'::regclass) NOT NULL,
    bid char(10) NOT NULL,
    cd_loc char(24) NOT NULL,
    spec_loc char(12) NOT NULL,
    consis varchar(255),
    tot_inv integer NOT NULL,
    indice char(31),
    ord_loc char(80) NOT NULL,
    ord_spec char(40) NOT NULL,
    tot_inv_prov integer,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbc_default_inven_schede_id_default_inven_scheda_seq (OID = 58648) : 
--
CREATE SEQUENCE sbnweb.tbc_default_inven_schede_id_default_inven_scheda_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbc_default_inven_schede (OID = 58650) : 
--
CREATE TABLE sbnweb.tbc_default_inven_schede (
    id_default_inven_scheda integer DEFAULT nextval('tbc_default_inven_schede_id_default_inven_scheda_seq'::regclass) NOT NULL,
    cd_biblioteca char(3),
    cd_polo char(3),
    n_copie_tit integer NOT NULL,
    n_copie_edi integer NOT NULL,
    n_copie_poss integer NOT NULL,
    n_copie_richiamo integer NOT NULL,
    cd_unimarc char(8) NOT NULL,
    sch_autori char(1) NOT NULL,
    fl_coll_aut char(1) NOT NULL,
    fl_tipo_leg char(1) NOT NULL,
    sch_topog char(1) NOT NULL,
    fl_coll_top char(1) NOT NULL,
    sch_soggetti char(1) NOT NULL,
    fl_coll_sog char(1) NOT NULL,
    sch_classi char(1) NOT NULL,
    fl_coll_cla char(1) NOT NULL,
    sch_titoli char(1) NOT NULL,
    fl_coll_tit char(1) NOT NULL,
    sch_edit char(1) NOT NULL,
    fl_coll_edi char(1) NOT NULL,
    sch_poss char(1) NOT NULL,
    fl_coll_poss char(1) NOT NULL,
    flg_coll_richiamo char(1) NOT NULL,
    fl_non_inv char(1) NOT NULL,
    tipo char(2) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    n_serie integer NOT NULL,
    n_piste integer NOT NULL,
    n_copie integer NOT NULL,
    n_copie_aut integer NOT NULL,
    n_copie_top integer NOT NULL,
    n_copie_sog integer NOT NULL,
    n_copie_cla integer NOT NULL,
    id_modello integer,
    formato_stampa char(3) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbc_esemplare_titolo (OID = 58654) : 
--
CREATE TABLE sbnweb.tbc_esemplare_titolo (
    cd_doc integer NOT NULL,
    bid char(10) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    cons_doc varchar(800),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbc_inventario (OID = 58660) : 
--
CREATE TABLE sbnweb.tbc_inventario (
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cd_serie char(3) NOT NULL,
    cd_inven integer NOT NULL,
    cd_proven char(5),
    key_loc integer,
    bid char(10),
    cd_polo_proven char(3),
    cd_biblioteca_proven char(3),
    key_loc_old integer,
    seq_coll char(20) NOT NULL,
    precis_inv text,
    cd_sit char(1) NOT NULL,
    valore numeric(12,3) NOT NULL,
    importo numeric(12,3) NOT NULL,
    num_vol integer,
    tot_loc integer,
    tot_inter integer,
    anno_abb integer,
    flg_disp char(1) NOT NULL,
    flg_nuovo_usato char(1) NOT NULL,
    stato_con char(2) NOT NULL,
    cd_fornitore integer,
    cd_mat_inv char(2) NOT NULL,
    cd_bib_ord char(3),
    cd_tip_ord char(1),
    anno_ord integer,
    cd_ord integer,
    riga_ord integer,
    cd_bib_fatt char(3),
    anno_fattura integer,
    prg_fattura integer,
    riga_fattura integer,
    cd_no_disp char(2),
    cd_frui char(2),
    cd_carico char(1),
    num_carico integer,
    data_carico date,
    cd_polo_scar char(3),
    cd_bib_scar char(3),
    cd_scarico char(1),
    num_scarico integer,
    data_scarico date,
    data_delibera date,
    delibera_scar char(50),
    sez_old char(10),
    loc_old char(24),
    spec_old char(12),
    cd_supporto char(2),
    ute_ins_prima_coll char(12),
    ts_ins_prima_coll timestamp(6) without time zone,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    tipo_acquisizione char(1),
    supporto_copia char(2),
    digitalizzazione char(1),
    disp_copia_digitale char(2),
    id_accesso_remoto char(1280),
    rif_teca_digitale char(4),
    cd_riproducibilita char(2),
    data_ingresso date,
    data_redisp_prev date,
    id_bib_scar integer
) WITHOUT OIDS;
--
-- Structure for table tbc_nota_inv (OID = 58666) : 
--
CREATE TABLE sbnweb.tbc_nota_inv (
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cd_serie char(3) NOT NULL,
    cd_inven integer NOT NULL,
    cd_nota char(4) NOT NULL,
    ds_nota_libera varchar(1920),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbc_possessore_provenienza (OID = 58672) : 
--
CREATE TABLE sbnweb.tbc_possessore_provenienza (
    pid char(10) NOT NULL,
    tp_forma_pp char(1) NOT NULL,
    ky_cautun char(6) NOT NULL,
    ky_auteur char(10) NOT NULL,
    ky_el1 char(6),
    ky_el2 char(6),
    tp_nome_pp char(1) NOT NULL,
    ky_el3 char(6),
    ky_el4 char(6),
    ky_el5 char(6),
    ky_cles1_a char(50) NOT NULL,
    ky_cles2_a char(30),
    note varchar(320),
    tot_inv integer,
    cd_livello char(2) NOT NULL,
    fl_speciale char(1),
    ds_nome_aut varchar(320) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    tidx_vector_ds_nome_aut tsvector
) WITHOUT OIDS;
--
-- Structure for table tbc_provenienza_inventario (OID = 58678) : 
--
CREATE TABLE sbnweb.tbc_provenienza_inventario (
    cd_proven char(5) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    descr varchar(255) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbc_serie_inventariale (OID = 58681) : 
--
CREATE TABLE sbnweb.tbc_serie_inventariale (
    cd_serie char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    descr char(80) NOT NULL,
    prg_inv_corrente integer NOT NULL,
    prg_inv_pregresso integer NOT NULL,
    num_man integer NOT NULL,
    inizio_man integer NOT NULL,
    fine_man integer NOT NULL,
    flg_chiusa char(1) NOT NULL,
    buono_carico integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    dt_ingr_inv_man date,
    dt_ingr_inv_preg date,
    dt_ingr_inv_ris1 date,
    inizio_man2 integer,
    fine_man2 integer,
    dt_ingr_inv_ris2 date,
    inizio_man3 integer,
    fine_man3 integer,
    dt_ingr_inv_ris3 date,
    inizio_man4 integer,
    fine_man4 integer,
    dt_ingr_inv_ris4 date,
    fl_default char(1)
) WITHOUT OIDS;
--
-- Structure for table tbc_sezione_collocazione (OID = 58684) : 
--
CREATE TABLE sbnweb.tbc_sezione_collocazione (
    cd_sez char(10) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    note_sez varchar(255) NOT NULL,
    tot_inv integer NOT NULL,
    descr char(30) NOT NULL,
    cd_colloc char(1) NOT NULL,
    tipo_sez char(1) NOT NULL,
    cd_cla char(3) NOT NULL,
    tot_inv_max integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbf_anagrafe_utenti_professionali_id_utente_professionale_seq (OID = 58687) : 
--
CREATE SEQUENCE sbnweb.tbf_anagrafe_utenti_professionali_id_utente_professionale_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_anagrafe_utenti_professionali (OID = 58689) : 
--
CREATE TABLE sbnweb.tbf_anagrafe_utenti_professionali (
    id_utente_professionale integer DEFAULT nextval('tbf_anagrafe_utenti_professionali_id_utente_professionale_seq'::regclass) NOT NULL,
    nome char(20),
    cognome char(80),
    ts_ins timestamp(6) without time zone NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    ute_ins char(12) NOT NULL,
    ute_var char(12) NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_attivita (OID = 58693) : 
--
CREATE TABLE sbnweb.tbf_attivita (
    cd_attivita char(5) NOT NULL,
    id_attivita_sbnmarc integer NOT NULL,
    flg_menu varchar(1) NOT NULL,
    prg_ordimanento integer,
    cd_funzione_parent char(5),
    fl_auto_abilita_figli char(1) NOT NULL,
    fl_assegna_a_cds char(1) NOT NULL,
    url_servizio varchar(512),
    fl_titolo char(1),
    fl_autore char(1),
    fl_marca char(1),
    fl_luogo char(1),
    fl_soggetto char(1),
    fl_classe char(1),
    liv_autorita_da char(2),
    liv_autorita_a char(2),
    gestione_in_indice char(1),
    gestione_in_polo char(1),
    natura_a char(1),
    natura_b char(1),
    natura_c char(1),
    natura_d char(1),
    natura_m char(1),
    natura_n char(1),
    natura_p char(1),
    natura_s char(1),
    natura_t char(1),
    natura_w char(1),
    natura_r char(1),
    natura_x char(1),
    forma_accettata char(1),
    forma_rinvio char(1),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    classe_java_sbnmarc varchar(256)
) WITHOUT OIDS;
--
-- Definition for sequence tbf_attivita_sbnmarc_id_attivita_sbnmarc_seq (OID = 58699) : 
--
CREATE SEQUENCE sbnweb.tbf_attivita_sbnmarc_id_attivita_sbnmarc_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_attivita_sbnmarc (OID = 58701) : 
--
CREATE TABLE sbnweb.tbf_attivita_sbnmarc (
    id_attivita_sbnmarc integer DEFAULT nextval('tbf_attivita_sbnmarc_id_attivita_sbnmarc_seq'::regclass) NOT NULL,
    id_tipo_attivita integer,
    tp_attivita char(1),
    ds_attivita varchar(255),
    nota_tipo_attivita varchar(255),
    livello integer,
    codice_attivita varchar(10),
    id_modulo integer
) WITHOUT OIDS;
--
-- Definition for sequence tbf_batch_servizi_id_batch_servizi_seq (OID = 58708) : 
--
CREATE SEQUENCE sbnweb.tbf_batch_servizi_id_batch_servizi_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_batch_servizi (OID = 58710) : 
--
CREATE TABLE sbnweb.tbf_batch_servizi (
    id_batch_servizi integer DEFAULT nextval('tbf_batch_servizi_id_batch_servizi_seq'::regclass) NOT NULL,
    id_coda_input integer NOT NULL,
    nome_coda_output varchar(255) NOT NULL,
    visibilita char(1) DEFAULT 'P'::bpchar NOT NULL,
    class_name varchar(255) NOT NULL,
    cd_attivita char(5) NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbf_biblioteca_id_biblioteca_seq (OID = 58718) : 
--
CREATE SEQUENCE sbnweb.tbf_biblioteca_id_biblioteca_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_biblioteca (OID = 58720) : 
--
CREATE TABLE sbnweb.tbf_biblioteca (
    id_biblioteca integer DEFAULT nextval('tbf_biblioteca_id_biblioteca_seq'::regclass) NOT NULL,
    cd_ana_biblioteca char(16),
    cd_polo char(3),
    cd_bib char(3),
    nom_biblioteca varchar(80) NOT NULL,
    unit_org char(50) NOT NULL,
    indirizzo varchar(80) NOT NULL,
    cpostale char(20),
    cap char(5) NOT NULL,
    telefono char(20),
    fax char(20),
    note varchar(160),
    p_iva char(18),
    cd_fiscale char(18),
    e_mail varchar(160),
    tipo_biblioteca char(1) NOT NULL,
    paese char(2) NOT NULL,
    provincia char(2) NOT NULL,
    cd_bib_cs char(3) NOT NULL,
    cd_bib_ut char(3),
    cd_utente integer,
    flag_bib char(1) NOT NULL,
    localita char(30) NOT NULL,
    chiave_bib char(50) NOT NULL,
    chiave_ente char(50) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    tidx_vector_nom_biblioteca tsvector,
    tidx_vector_indirizzo tsvector,
    CONSTRAINT tbf_biblioteca_isil_idx UNIQUE (paese, cd_ana_biblioteca)
) WITHOUT OIDS;
--
-- Structure for table tbf_biblioteca_default (OID = 58727) : 
--
CREATE TABLE sbnweb.tbf_biblioteca_default (
    id_default integer NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    value varchar(255) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_biblioteca_in_polo (OID = 58730) : 
--
CREATE TABLE sbnweb.tbf_biblioteca_in_polo (
    cd_biblioteca char(3) NOT NULL,
    id_parametro integer NOT NULL,
    cd_polo char(3) NOT NULL,
    id_gruppo_attivita_polo integer NOT NULL,
    id_biblioteca integer,
    ky_biblioteca char(13),
    cd_ana_biblioteca char(6),
    ds_biblioteca varchar(80),
    ds_citta varchar(30),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    cd_sistema_metropolitano char(3)
) WITHOUT OIDS;
--
-- Structure for table tbf_bibliotecario (OID = 58733) : 
--
CREATE TABLE sbnweb.tbf_bibliotecario (
    id_parametro integer NOT NULL,
    id_utente_professionale integer NOT NULL,
    id_profilo_abilitazione integer,
    cd_livello_gbantico char(2),
    cd_livello_gbmoderno char(2),
    cd_livello_gssoggetti char(2),
    cd_livello_gsclassi char(2),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_bibliotecario_default (OID = 58736) : 
--
CREATE TABLE sbnweb.tbf_bibliotecario_default (
    id_utente_professionale integer NOT NULL,
    id_default integer NOT NULL,
    value varchar(255) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbf_coda_jms_id_coda_seq (OID = 58739) : 
--
CREATE SEQUENCE sbnweb.tbf_coda_jms_id_coda_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_coda_jms (OID = 58741) : 
--
CREATE TABLE sbnweb.tbf_coda_jms (
    id_coda integer DEFAULT nextval('tbf_coda_jms_id_coda_seq'::regclass) NOT NULL,
    nome_jms char(255) NOT NULL,
    sincrona char(1) DEFAULT 'N'::bpchar NOT NULL,
    id_descrizione char(255),
    id_descr_orario_attivazione varchar(255),
    id_orario_di_attivazione varchar(255),
    cron_expression varchar(255) DEFAULT '0 0/5 * * * ? 9999'::character varying
) WITHOUT OIDS;
--
-- Definition for sequence tbf_config_default_id_config_seq (OID = 58750) : 
--
CREATE SEQUENCE sbnweb.tbf_config_default_id_config_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_config_default (OID = 58752) : 
--
CREATE TABLE sbnweb.tbf_config_default (
    id_config integer DEFAULT nextval('tbf_config_default_id_config_seq'::regclass) NOT NULL,
    id_area_sezione char(32) NOT NULL,
    parent char(32),
    seq_ordinamento integer NOT NULL,
    codice_attivita char(5) NOT NULL,
    parametro_attivita char(6),
    codice_modulo smallint
) WITHOUT OIDS;
--
-- Structure for table tbf_config_statistiche (OID = 58756) : 
--
CREATE TABLE sbnweb.tbf_config_statistiche (
    id_stat serial NOT NULL,
    id_area_sezione char(32) NOT NULL,
    parent char(32),
    seq_ordinamento integer NOT NULL,
    codice_attivita char(5) NOT NULL,
    parametro_attivita char(6),
    codice_modulo smallint,
    nome_statistica varchar,
    tipo_query char(1),
    query text,
    colonne_output varchar,
    fl_polo_biblio char(1) DEFAULT 'P'::bpchar NOT NULL,
    fl_txt char(1) DEFAULT 'N'::bpchar NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_contatore (OID = 58763) : 
--
CREATE TABLE sbnweb.tbf_contatore (
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cd_cont char(3) NOT NULL,
    anno integer NOT NULL,
    key1 char(3) NOT NULL,
    ultimo_prg integer NOT NULL,
    lim_max integer NOT NULL,
    attivo char(1),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbf_default_id_default_seq (OID = 58766) : 
--
CREATE SEQUENCE sbnweb.tbf_default_id_default_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_default (OID = 58768) : 
--
CREATE TABLE sbnweb.tbf_default (
    id_default integer DEFAULT nextval('tbf_default_id_default_seq'::regclass) NOT NULL,
    key varchar(50) NOT NULL,
    tipo varchar(50) NOT NULL,
    lunghezza integer,
    id_etichetta varchar(255),
    codice_attivita char(5) NOT NULL,
    codice_tabella_validazione char(4),
    seq_ordinamento integer,
    tbf_gruppi_defaultid integer,
    tbf_config_default__id_config integer NOT NULL,
    bundle varchar(255) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_dizionario_errori (OID = 58775) : 
--
CREATE TABLE sbnweb.tbf_dizionario_errori (
    cd_lingua char(5) NOT NULL,
    cd_errore integer NOT NULL,
    ds_errore varchar(255) NOT NULL,
    ute_ins char(12),
    ts_ins timestamp(6) without time zone,
    ute_var char(12),
    ts_var timestamp(6) without time zone,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbf_gruppi_default_id_seq (OID = 58778) : 
--
CREATE SEQUENCE sbnweb.tbf_gruppi_default_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_gruppi_default (OID = 58780) : 
--
CREATE TABLE sbnweb.tbf_gruppi_default (
    id integer DEFAULT nextval('tbf_gruppi_default_id_seq'::regclass) NOT NULL,
    etichetta varchar(255),
    bundle varchar(255) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbf_gruppo_attivita_id_gruppo_attivita_polo_seq (OID = 58787) : 
--
CREATE SEQUENCE sbnweb.tbf_gruppo_attivita_id_gruppo_attivita_polo_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_gruppo_attivita (OID = 58789) : 
--
CREATE TABLE sbnweb.tbf_gruppo_attivita (
    cd_polo char(3) NOT NULL,
    id_gruppo_attivita_polo integer DEFAULT nextval('tbf_gruppo_attivita_id_gruppo_attivita_polo_seq'::regclass) NOT NULL,
    ds_name varchar(255)
) WITHOUT OIDS;
--
-- Structure for table tbf_intranet_range (OID = 58793) : 
--
CREATE TABLE sbnweb.tbf_intranet_range (
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    address char(39) NOT NULL,
    bitmask smallint NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp without time zone NOT NULL,
    fl_canc char(1) DEFAULT 'N'::bpchar NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_lingua_supportata (OID = 58797) : 
--
CREATE TABLE sbnweb.tbf_lingua_supportata (
    cd_lingua char(5) NOT NULL,
    descr char(80) NOT NULL,
    fl_default char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbf_mail_id_seq (OID = 58800) : 
--
CREATE SEQUENCE sbnweb.tbf_mail_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_mail (OID = 58802) : 
--
CREATE TABLE sbnweb.tbf_mail (
    id integer DEFAULT nextval('tbf_mail_id_seq'::regclass) NOT NULL,
    smtp varchar(255) NOT NULL,
    pop3 varchar(255) NOT NULL,
    user_name varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    indirizzo varchar(255) NOT NULL,
    descrizione varchar(255),
	fl_forzatura CHAR(1) DEFAULT 'N'
) WITHOUT OIDS;
--
-- Definition for sequence tbf_modelli_stampe_id_modello_seq (OID = 58809) : 
--
CREATE SEQUENCE sbnweb.tbf_modelli_stampe_id_modello_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_modelli_stampe (OID = 58811) : 
--
CREATE TABLE sbnweb.tbf_modelli_stampe (
    id_modello integer DEFAULT nextval('tbf_modelli_stampe_id_modello_seq'::regclass) NOT NULL,
    cd_bib char(3),
    modello varchar(30) NOT NULL,
    tipo_modello char(1) NOT NULL,
    campi_valori_del_form text NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    descr varchar(50),
    cd_polo char(3),
    cd_attivita char(5),
    subreport text,
    descr_bib varchar(80)
) WITHOUT OIDS;
--
-- Definition for sequence tbf_modello_profilazione_biblioteca_id_modello_seq (OID = 58818) : 
--
CREATE SEQUENCE sbnweb.tbf_modello_profilazione_biblioteca_id_modello_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_modello_profilazione_biblioteca (OID = 58820) : 
--
CREATE TABLE sbnweb.tbf_modello_profilazione_biblioteca (
    id_modello integer DEFAULT nextval('tbf_modello_profilazione_biblioteca_id_modello_seq'::regclass) NOT NULL,
    nome varchar(80) NOT NULL,
    id_parametro integer NOT NULL,
    id_gruppo_attivita integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbf_modello_profilazione_bibliotecario_id_modello_seq (OID = 58824) : 
--
CREATE SEQUENCE sbnweb.tbf_modello_profilazione_bibliotecario_id_modello_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_modello_profilazione_bibliotecario (OID = 58826) : 
--
CREATE TABLE sbnweb.tbf_modello_profilazione_bibliotecario (
    id_modello integer DEFAULT nextval('tbf_modello_profilazione_bibliotecario_id_modello_seq'::regclass) NOT NULL,
    nome varchar(80) NOT NULL,
    id_parametro integer NOT NULL,
    id_gruppo_attivita integer,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbf_moduli_funzionali_id_modulo_seq (OID = 58830) : 
--
CREATE SEQUENCE sbnweb.tbf_moduli_funzionali_id_modulo_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_moduli_funzionali (OID = 58832) : 
--
CREATE TABLE sbnweb.tbf_moduli_funzionali (
    id_modulo integer DEFAULT nextval('tbf_moduli_funzionali_id_modulo_seq'::regclass) NOT NULL,
    ds_modulo varchar(255) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_par_auth (OID = 58836) : 
--
CREATE TABLE sbnweb.tbf_par_auth (
    cd_par_auth char(2) NOT NULL,
    id_parametro integer NOT NULL,
    tp_abil_auth char(1) NOT NULL,
    fl_abil_legame char(1) NOT NULL,
    fl_leg_auth char(1) NOT NULL,
    cd_livello char(2) NOT NULL,
    cd_contr_sim char(3) NOT NULL,
    fl_abil_forzat char(1) NOT NULL,
    sololocale char(1)
) WITHOUT OIDS;
--
-- Structure for table tbf_par_mat (OID = 58839) : 
--
CREATE TABLE sbnweb.tbf_par_mat (
    cd_par_mat char(1) NOT NULL,
    id_parametro integer NOT NULL,
    tp_abilitaz char(1) NOT NULL,
    cd_contr_sim char(3) NOT NULL,
    fl_abil_forzat char(1) NOT NULL,
    cd_livello char(2) NOT NULL,
    sololocale char(1)
) WITHOUT OIDS;
--
-- Structure for table tbf_par_sem (OID = 58842) : 
--
CREATE TABLE sbnweb.tbf_par_sem (
    tp_tabella_codici char(4) NOT NULL,
    cd_tabella_codici char(6) NOT NULL,
    id_parametro integer NOT NULL,
    sololocale char(1)
) WITHOUT OIDS;
--
-- Definition for sequence tbf_parametro_id_parametro_seq (OID = 58845) : 
--
CREATE SEQUENCE sbnweb.tbf_parametro_id_parametro_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_parametro (OID = 58847) : 
--
CREATE TABLE sbnweb.tbf_parametro (
    id_parametro integer DEFAULT nextval('tbf_parametro_id_parametro_seq'::regclass) NOT NULL,
    cd_livello char(2) NOT NULL,
    tp_ret_doc char(3) NOT NULL,
    tp_all_pref char(2) NOT NULL,
    cd_liv_ade char(1) NOT NULL,
    fl_spogli char(1) NOT NULL,
    fl_aut_superflui char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    sololocale char(1)
) WITHOUT OIDS;
--
-- Structure for table tbf_polo (OID = 58851) : 
--
CREATE TABLE sbnweb.tbf_polo (
    id_parametro integer,
    cd_polo char(3) NOT NULL,
    url_indice varchar(255),
    username varchar(255),
    password varchar(255),
    denominazione varchar(255),
    ute_var char(12),
    ts_ins timestamp(6) without time zone,
    ute_ins char(12),
    ts_var timestamp(6) without time zone,
    url_polo varchar(255),
    username_polo varchar(6),
    id_gruppo_attivita integer,
    email varchar(255) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_polo_default (OID = 58857) : 
--
CREATE TABLE sbnweb.tbf_polo_default (
    id_default integer NOT NULL,
    cd_polo char(3) NOT NULL,
    value varchar(255) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbf_procedure_output_id_seq (OID = 58860) : 
--
CREATE SEQUENCE sbnweb.tbf_procedure_output_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_procedure_output (OID = 58862) : 
--
CREATE TABLE sbnweb.tbf_procedure_output (
    id integer DEFAULT nextval('tbf_procedure_output_id_seq'::regclass) NOT NULL,
    cod_attivita char(5) NOT NULL,
    cod_polo char(3) NOT NULL,
    cod_bib char(3) NOT NULL,
    tipo_output char(10) NOT NULL,
    dati bytea NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbf_profilo_abilitazione_id_profilo_abilitazione_seq (OID = 58869) : 
--
CREATE SEQUENCE sbnweb.tbf_profilo_abilitazione_id_profilo_abilitazione_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbf_profilo_abilitazione (OID = 58871) : 
--
CREATE TABLE sbnweb.tbf_profilo_abilitazione (
    id_profilo_abilitazione integer DEFAULT nextval('tbf_profilo_abilitazione_id_profilo_abilitazione_seq'::regclass) NOT NULL,
    cd_prof char(3),
    cd_biblioteca char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    dsc_profilo char(80) NOT NULL,
    nota_profilo char(80),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_utenti_professionali_web (OID = 58875) : 
--
CREATE TABLE sbnweb.tbf_utenti_professionali_web (
    id_utente_professionale integer NOT NULL,
    userid char(12) NOT NULL,
    password char(256) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    change_password char(1) NOT NULL,
    last_access timestamp(6) without time zone NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbl_calendario_servizi (OID = 58878) : 
--
CREATE TABLE sbnweb.tbl_calendario_servizi (
    id_tipo_servizio integer NOT NULL,
    progr_fascia integer NOT NULL,
    ore_in timestamp(6) without time zone NOT NULL,
    ore_fi timestamp(6) without time zone NOT NULL,
    data date NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbl_controllo_attivita_id_controllo_attivita_seq (OID = 58881) : 
--
CREATE SEQUENCE sbnweb.tbl_controllo_attivita_id_controllo_attivita_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_controllo_attivita (OID = 58883) : 
--
CREATE TABLE sbnweb.tbl_controllo_attivita (
    id_controllo_attivita integer DEFAULT nextval('tbl_controllo_attivita_id_controllo_attivita_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_attivita char(2) NOT NULL,
    cod_controllo smallint NOT NULL,
    fl_bloccante char(1) NOT NULL,
    messaggio varchar(255) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbl_controllo_iter_id_controllo_iter_seq (OID = 58887) : 
--
CREATE SEQUENCE sbnweb.tbl_controllo_iter_id_controllo_iter_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_controllo_iter (OID = 58889) : 
--
CREATE TABLE sbnweb.tbl_controllo_iter (
    id_controllo_iter integer DEFAULT nextval('tbl_controllo_iter_id_controllo_iter_seq'::regclass) NOT NULL,
    id_iter_servizio integer NOT NULL,
    progr_fase smallint NOT NULL,
    fl_bloccante char(1) NOT NULL,
    messaggio varchar(255) NOT NULL,
    cod_controllo smallint NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbl_disponibilita_precataloga_id_disponibilita_precatalogat_seq (OID = 58893) : 
--
CREATE SEQUENCE sbnweb.tbl_disponibilita_precataloga_id_disponibilita_precatalogat_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_disponibilita_precatalogati (OID = 58895) : 
--
CREATE TABLE sbnweb.tbl_disponibilita_precatalogati (
    id_disponibilita_precatalogati integer DEFAULT nextval('tbl_disponibilita_precataloga_id_disponibilita_precatalogat_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_segn integer NOT NULL,
    segn_inizio char(80) NOT NULL,
    segn_fine char(80) NOT NULL,
    cod_no_disp char(2) NOT NULL,
    cod_frui char(2) NOT NULL,
    segn_da char(80) NOT NULL,
    segn_a char(80) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbl_documenti_lettori_id_documenti_lettore_seq (OID = 58899) : 
--
CREATE SEQUENCE sbnweb.tbl_documenti_lettori_id_documenti_lettore_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_documenti_lettori (OID = 58901) : 
--
CREATE TABLE sbnweb.tbl_documenti_lettori (
    id_documenti_lettore integer DEFAULT nextval('tbl_documenti_lettori_id_documenti_lettore_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    tipo_doc_lett char(1) NOT NULL,
    cod_doc_lett bigint NOT NULL,
    id_utenti integer,
    titolo varchar(240) NOT NULL,
    luogo_edizione char(30),
    editore char(50),
    anno_edizione numeric(4,0),
    autore varchar(160),
    num_volume VARCHAR(30),
    annata char(10),
    tipo_data char(1),
    prima_data numeric(4,0),
    seconda_data numeric(4,0),
    stato_catal char(1),
    natura char(1) NOT NULL,
    paese char(2),
    lingua char(3),
    fonte char(1) NOT NULL,
    stato_sugg char(1) NOT NULL,
    bid char(10),
    data_sugg_lett date NOT NULL,
    note varchar(160),
    msg_lettore varchar(255),
    segnatura char(40) NOT NULL,
    cod_bib_inv char(3),
    cod_serie char(3),
    cod_inven integer,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    ord_segnatura char(80),
    cd_no_disp char(2),
    cd_catfrui char(2),
    denom_biblioteca_doc varchar(160),
    tidx_vector tsvector,
    ente_curatore VARCHAR(50),
    fa_parte VARCHAR(50),
    fascicolo VARCHAR(50),
    data_pubb VARCHAR(20),
    autore_articolo VARCHAR(50),
    titolo_articolo VARCHAR(50),
    pagine VARCHAR(50),
    fonte_rif VARCHAR(50),
    tp_numero_std CHAR(1),
    numero_std CHAR(25),
    biblioteche TEXT,
    tp_record_uni CHAR(1)
) WITHOUT OIDS;

COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.tp_numero_std IS 'Tipo Numero Standard';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.numero_std IS 'Numero Standard';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.tp_record_uni IS 'Tipo record Unimarc';

--
-- Definition for sequence tbl_esemplare_documento_lettore_id_esemplare_seq (OID = 58908) : 
--
CREATE SEQUENCE sbnweb.tbl_esemplare_documento_lettore_id_esemplare_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_esemplare_documento_lettore (OID = 58910) : 
--
CREATE TABLE sbnweb.tbl_esemplare_documento_lettore (
    id_esemplare integer DEFAULT nextval('tbl_esemplare_documento_lettore_id_esemplare_seq'::regclass) NOT NULL,
    id_documenti_lettore integer NOT NULL,
    prg_esemplare smallint NOT NULL,
    fonte char(1) NOT NULL,
    inventario char(12),
    precisazione text,
    cod_no_disp char(2),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbl_iter_servizio_id_iter_servizio_seq (OID = 58917) : 
--
CREATE SEQUENCE sbnweb.tbl_iter_servizio_id_iter_servizio_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_iter_servizio (OID = 58919) : 
--
CREATE TABLE sbnweb.tbl_iter_servizio (
    id_iter_servizio integer DEFAULT nextval('tbl_iter_servizio_id_iter_servizio_seq'::regclass) NOT NULL,
    progr_iter smallint NOT NULL,
    id_tipo_servizio integer NOT NULL,
    cod_attivita char(2) NOT NULL,
    flag_stampa char(1) NOT NULL,
    num_pag smallint NOT NULL,
    testo varchar(240) NOT NULL,
    flg_abil char(1) NOT NULL,
    flg_rinn char(1) NOT NULL,
    stato_iter char(1) NOT NULL,
    obbl char(1) NOT NULL,
    cod_stato_rich char(1) NOT NULL,
    stato_mov char(1) NOT NULL,
    cod_stat_cir char(1) NOT NULL,
    cod_stato_ric_ill char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbl_materie_id_materia_seq (OID = 58923) : 
--
CREATE SEQUENCE sbnweb.tbl_materie_id_materia_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_materie (OID = 58925) : 
--
CREATE TABLE sbnweb.tbl_materie (
    id_materia integer DEFAULT nextval('tbl_materie_id_materia_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cod_mat char(3) NOT NULL,
    descr char(30) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbl_messaggio (OID = 58929) : 
--
CREATE TABLE sbnweb.tbl_messaggio (
id_messaggio SERIAL, 
id_dati_richiesta INTEGER NOT NULL, 
note VARCHAR(250) NOT NULL, 
fl_tipo CHAR(1), 
fl_condizione CHAR(2),
cd_stato CHAR(5), 
data_messaggio TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
ute_ins CHAR(12) NOT NULL, 
ts_ins TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
ute_var CHAR(12) NOT NULL, 
ts_var TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
fl_canc CHAR(1) NOT NULL, 
requester_id CHAR(12),
responder_id CHAR(12),
fl_ruolo CHAR(1),
CONSTRAINT tbl_messaggio_pkey PRIMARY KEY(id_messaggio)
) WITHOUT OIDS;

COMMENT ON TABLE sbnweb.tbl_messaggio IS 'Messaggio tra biblioteche';
COMMENT ON COLUMN sbnweb.tbl_messaggio.fl_tipo IS 'Tipo messaggio: I=Inviato, R=Ricevuto';
COMMENT ON COLUMN sbnweb.tbl_messaggio.fl_condizione IS 'Condizione posta dalla biblioteca';

--
-- Definition for sequence tbl_modalita_erogazione_id_modalita_erogazione_seq (OID = 58932) : 
--
CREATE SEQUENCE sbnweb.tbl_modalita_erogazione_id_modalita_erogazione_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_modalita_erogazione (OID = 58934) : 
--
CREATE TABLE sbnweb.tbl_modalita_erogazione (
    id_modalita_erogazione integer DEFAULT nextval('tbl_modalita_erogazione_id_modalita_erogazione_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_erog char(2) NOT NULL,
    tar_base numeric(10,3) NOT NULL,
    costo_unitario numeric(10,3) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    fl_svolg CHAR(1) DEFAULT 'L'
) WITHOUT OIDS;

COMMENT ON COLUMN sbnweb.tbl_modalita_erogazione.fl_svolg IS 'modalità locale o ILL'; 

--
-- Definition for sequence tbl_modalita_pagamento_id_modalita_pagamento_seq (OID = 58938) : 
--
CREATE SEQUENCE sbnweb.tbl_modalita_pagamento_id_modalita_pagamento_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_modalita_pagamento (OID = 58940) : 
--
CREATE TABLE sbnweb.tbl_modalita_pagamento (
    id_modalita_pagamento integer DEFAULT nextval('tbl_modalita_pagamento_id_modalita_pagamento_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_mod_pag smallint NOT NULL,
    descr varchar(255) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbl_occupazioni_id_occupazioni_seq (OID = 58944) : 
--
CREATE SEQUENCE sbnweb.tbl_occupazioni_id_occupazioni_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_occupazioni (OID = 58946) : 
--
CREATE TABLE sbnweb.tbl_occupazioni (
    id_occupazioni integer DEFAULT nextval('tbl_occupazioni_id_occupazioni_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    professione char(1) NOT NULL,
    occupazione char(2) NOT NULL,
    descr char(50) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbl_parametri_biblioteca_id_parametri_biblioteca_seq (OID = 58950) : 
--
CREATE SEQUENCE sbnweb.tbl_parametri_biblioteca_id_parametri_biblioteca_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_parametri_biblioteca (OID = 58952) : 
--
CREATE TABLE sbnweb.tbl_parametri_biblioteca (
    id_parametri_biblioteca integer DEFAULT nextval('tbl_parametri_biblioteca_id_parametri_biblioteca_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    num_lettere smallint NOT NULL,
    num_gg_ritardo1 smallint NOT NULL,
    num_gg_ritardo2 smallint NOT NULL,
    num_gg_ritardo3 smallint NOT NULL,
    num_prenotazioni smallint NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    ute_ins char(12) NOT NULL,
    ute_var char(12) NOT NULL,
    fl_catfrui_nosbndoc char(1) NOT NULL,
    cd_catfrui_nosbndoc char(2),
    cod_modalita_invio1_sollecito1 char(1),
    cod_modalita_invio2_sollecito1 char(1),
    cod_modalita_invio3_sollecito1 char(1),
    cod_modalita_invio1_sollecito2 char(1),
    cod_modalita_invio2_sollecito2 char(1),
    cod_modalita_invio3_sollecito2 char(1),
    cod_modalita_invio1_sollecito3 char(1),
    cod_modalita_invio2_sollecito3 char(1),
    cod_modalita_invio3_sollecito3 char(1),
    num_gg_validita_prelazione smallint NOT NULL,
    ammessa_autoregistrazione_utente char(1) NOT NULL,
    ammesso_inserimento_utente char(1) NOT NULL,
    anche_da_remoto char(1) NOT NULL,
    cd_catriprod_nosbndoc char(2),
	xml_modello_soll TEXT,
    fl_tipo_rinnovo CHAR(1) DEFAULT '0',
    num_gg_rinnovo_richiesta SMALLINT DEFAULT 0,
	fl_priorita_prenot CHAR(1) DEFAULT 'N',
	email_notifica VARCHAR(160),
	cd_cat_med_digit CHAR(2),
	id_autorizzazione_ill INTEGER,
	fl_att_servizi_ill CHAR(1) DEFAULT 'N'
) WITHOUT OIDS;



COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cd_cat_med_digit IS 'Categoria di mediazione associata agli inventari digitalizzati';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.id_autorizzazione_ill IS 'tipo autorizzazione assegnato alle biblioteche ILL';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.fl_att_servizi_ill IS 'La biblioteca gestisce i servizi ILL';

CREATE TABLE sbnweb.tbl_modello_calendario (
  id_modello SERIAL, 
  cd_polo CHAR(3) NOT NULL, 
  cd_bib CHAR(3) NOT NULL, 
  cd_tipo CHAR(1) NOT NULL, 
  id_sala INTEGER, 
  cd_cat_mediazione CHAR(2), 
  descrizione VARCHAR(255) NOT NULL, 
  dt_inizio DATE NOT NULL, 
  dt_fine DATE, 
  json_modello TEXT, 
  ute_ins CHAR(12) NOT NULL, 
  ts_ins TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  ute_var CHAR(12) NOT NULL, 
  ts_var TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  fl_canc CHAR(1) NOT NULL, 
  CONSTRAINT tbl_modello_calendario_pkey PRIMARY KEY(id_modello)
) WITHOUT OIDS;

--
-- Structure for table tbl_penalita_servizio (OID = 58956) : 
--
CREATE TABLE sbnweb.tbl_penalita_servizio (
    id_servizio integer NOT NULL,
    gg_sosp smallint NOT NULL,
    coeff_sosp numeric(2,0) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbl_posti_sala_id_posti_sala_seq (OID = 58959) : 
--
CREATE SEQUENCE sbnweb.tbl_posti_sala_id_posti_sala_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_posti_sala (OID = 58961) : 
--
CREATE TABLE sbnweb.tbl_posti_sala (
    id_posti_sala integer DEFAULT nextval('tbl_posti_sala_id_posti_sala_seq'::regclass) NOT NULL,
    id_sale integer NOT NULL,
    num_posto smallint NOT NULL,
    occupato char(1) NOT NULL DEFAULT 'N',
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    gruppo SMALLINT DEFAULT 1 NOT NULL
) WITHOUT OIDS;

COMMENT ON COLUMN sbnweb.tbl_posti_sala.occupato IS 'indica se il posto e'' occupato o libero';

--
-- Definition for sequence tbl_richiesta_servizio_cod_rich_serv_seq (OID = 58965) : 
--
CREATE SEQUENCE sbnweb.tbl_richiesta_servizio_cod_rich_serv_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_richiesta_servizio (OID = 58967) : 
--
CREATE TABLE sbnweb.tbl_richiesta_servizio (
    cod_rich_serv bigint DEFAULT nextval('tbl_richiesta_servizio_cod_rich_serv_seq'::regclass) NOT NULL,
    cd_polo_inv char(3),
    cod_bib_inv char(3),
    cod_serie_inv char(3),
    cod_inven_inv integer,
    id_documenti_lettore integer,
    id_esemplare_documenti_lettore integer,
    id_utenti_biblioteca integer NOT NULL,
    id_modalita_pagamento integer,
    id_supporti_biblioteca integer,
    id_servizio integer NOT NULL,
    id_iter_servizio integer NOT NULL,
    fl_tipo_rec char(1) NOT NULL,
    note_bibliotecario varchar(255),
    costo_servizio numeric(12,3),
    num_fasc char(30),
    num_volume VARCHAR(30),
    anno_period numeric(4,0),
    cod_tipo_serv_rich char(2),
    cod_tipo_serv_alt char(2),
    cod_stato_rich char(1) NOT NULL,
    cod_stato_mov char(1),
    data_in_eff timestamp(6) without time zone,
    data_fine_eff timestamp(6) without time zone,
    num_rinnovi smallint,
    bid char(10),
    cod_attivita char(2) NOT NULL,
    data_richiesta timestamp(6) without time zone NOT NULL,
    num_pezzi smallint,
    note_ut varchar(255),
    prezzo_max numeric(12,3),
    data_massima date,
    data_proroga date,
    data_in_prev timestamp(6) without time zone,
    data_fine_prev timestamp(6) without time zone,
    fl_svolg char(1) NOT NULL,
    copyright char(1),
    cod_erog char(2) NOT NULL,
    cod_risp char(1),
    fl_condiz char(1),
    fl_inoltro char(1),
    cod_bib_dest char(3),
    cod_bib_prelievo char(3),
    cod_bib_restituzione char(3),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    intervallo_copia varchar(30)
) WITHOUT OIDS;

CREATE TABLE sbnweb.tbl_dati_richiesta_ill (
	id_dati_richiesta SERIAL, 
	cod_rich_serv BIGINT, 
	id_documento INTEGER, 
	cd_polo_inv CHAR(3), 
	cd_bib_inv CHAR(3), 
	cd_serie CHAR(3), 
	cd_inven INTEGER, 
	transaction_id BIGINT NOT NULL, 
	requester_id CHAR(12) NOT NULL, 
	responder_id CHAR(12) NOT NULL, 
	fl_ruolo CHAR(1), 
	data_inizio TIMESTAMP(6) WITHOUT TIME ZONE,
	data_fine TIMESTAMP(6) WITHOUT TIME ZONE,
	cd_stato CHAR(5), 
	cd_servizio CHAR(2), 
	client_id CHAR(20), 
	client_name VARCHAR(160), 
	client_email VARCHAR(320), 
	via VARCHAR(160), 
	comune VARCHAR(160), 
	prov VARCHAR(60), 
	cap CHAR(7), 
	cd_paese CHAR(2), 
	requester_email VARCHAR(320), 
	dt_data_desiderata DATE, 
	dt_data_massima DATE, 
	dt_data_scadenza DATE,
	cod_erog CHAR(2), 
	cd_supporto CHAR(2), 
	cd_valuta CHAR(3), 
	importo NUMERIC(10,3),
	costo_max NUMERIC(10,3), 
	intervallo_copia VARCHAR(30),
	cod_rich_serv_old BIGINT,
	ts_last_cambio_stato TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
	biblioteche TEXT, 
	ute_ins CHAR(12) NOT NULL, 
	ts_ins TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
	ute_var CHAR(12) NOT NULL, 
	ts_var TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
	fl_canc CHAR(1) NOT NULL, 
	CONSTRAINT tbl_dati_richiesta_ill_pkey PRIMARY KEY(id_dati_richiesta)
) WITHOUT OIDS;

COMMENT ON TABLE sbnweb.tbl_dati_richiesta_ill IS 'Dati specifici della richiesta ILL';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.cod_rich_serv
IS 'Codice identificativo della richiesta/movimento/prenotazione/prelazione';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.id_documento
IS 'Dati del documento legato alla richiesta';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.transaction_id
IS 'Codice identificativo della richiesta sul server SBN ILL';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.requester_id
IS 'Codice ISIL della biblioteca richiedente';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.responder_id
IS 'Codice ISIL della biblioteca fornitrice';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.fl_ruolo
IS 'Ruolo della biblioteca: R=richiedente, F=fornitrice';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.data_inizio
IS 'Data inizio della richiesta';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.data_fine
IS 'Data fine della richiesta';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.cd_stato
IS 'Stato della richiesta ILL';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.cd_servizio
IS 'Codice del servizio ILL';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.client_id
IS 'Codice identificativo dell''utente';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.requester_email
IS 'Indirizzo e-mail della biblioteca richiedente';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.ts_last_cambio_stato
IS 'Timestamp ultimo cambio di stato sul server ILL';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.ute_ins
IS 'Utente che ha effettuato l''inserimento';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.ts_ins
IS 'Timestamp di inserimento';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.ute_var
IS 'Utente che ha effettuato l''ultimo aggiornamento';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.ts_var
IS 'Timestamp di aggiornamento';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.fl_canc
IS 'Flag di cancellazione logica';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.client_email
IS 'Indirizzo e-mail dell''utente';

COMMENT ON COLUMN sbnweb.tbl_dati_richiesta_ill.dt_data_scadenza
IS 'Data scadenza della richiesta';

CREATE TABLE sbnweb.tbl_biblioteca_ill (
  id_biblioteca_ill SERIAL, 
  cd_isil CHAR(16) NOT NULL, 
  id_biblioteca INTEGER, 
  id_utente INTEGER, 
  ds_biblioteca VARCHAR(160), 
  fl_ruolo CHAR(1) NOT NULL, 
  fl_servprestito CHAR(1), 
  fl_servdd CHAR(1), 
  ute_ins CHAR(12) NOT NULL, 
  ts_ins TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  ute_var CHAR(12) NOT NULL, 
  ts_var TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  fl_canc CHAR(1) NOT NULL, 
  CONSTRAINT tbl_biblioteca_ill_cd_isil_key UNIQUE(cd_isil),
  CONSTRAINT tbl_biblioteca_ill_pkey PRIMARY KEY(id_biblioteca_ill)
) WITHOUT OIDS;

--
-- Definition for sequence tbl_sale_id_sale_seq (OID = 58974) : 
--
CREATE SEQUENCE sbnweb.tbl_sale_id_sale_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_sale (OID = 58976) : 
--
CREATE TABLE sbnweb.tbl_sale (
    id_sale integer DEFAULT nextval('tbl_sale_id_sale_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_sala char(2) NOT NULL,
    descr varchar(160) NOT NULL,
    num_max_posti smallint NOT NULL,
    num_prg_posti smallint NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    durata_fascia SMALLINT DEFAULT 30 NOT NULL,
    num_max_fasce_prenot SMALLINT DEFAULT 3 NOT NULL,
    num_max_utenti_per_prenot SMALLINT DEFAULT 1 NOT NULL,
    fl_prenot_remoto CHAR(1) DEFAULT 'S' NOT NULL
) WITHOUT OIDS;

COMMENT ON COLUMN sbnweb.tbl_sale.durata_fascia IS 'Durata della fascia (in minuti)';
COMMENT ON COLUMN sbnweb.tbl_sale.num_max_fasce_prenot IS 'Numero massimo fasce prenotabili';
COMMENT ON COLUMN sbnweb.tbl_sale.num_max_utenti_per_prenot IS 'Numero massimo di utenti che possono condividere una stessa prenotazione';
COMMENT ON COLUMN sbnweb.tbl_sale.fl_prenot_remoto IS 'Indica se è possibile inserire prenotazioni da remoto';

CREATE TABLE sbnweb.trl_posto_sala_categoria_mediazione (
  id_posto_sala INTEGER NOT NULL,
  cd_cat_mediazione CHAR(2) NOT NULL,
  ute_ins CHAR(12) NOT NULL,
  ts_ins TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
  ute_var CHAR(12) NOT NULL,
  ts_var TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
  fl_canc CHAR(1) NOT NULL
) WITHOUT OIDS;

CREATE TABLE sbnweb.tbl_prenotazione_posto (
  id_prenotazione SERIAL, 
  cd_polo CHAR(3) NOT NULL, 
  cd_bib CHAR(3) NOT NULL, 
  id_posto INTEGER NOT NULL, 
  id_utente INTEGER NOT NULL, 
  cd_stato CHAR(1) NOT NULL, 
  ts_inizio TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  ts_fine TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  cd_cat_mediazione CHAR(2),
  ute_ins CHAR(12) NOT NULL, 
  ts_ins TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  ute_var CHAR(12) NOT NULL, 
  ts_var TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  fl_canc CHAR(1) NOT NULL, 
  CONSTRAINT tbl_prenotazione_posto_pkey PRIMARY KEY(id_prenotazione)
) WITHOUT OIDS;

CREATE INDEX prenot_data_range_idx ON sbnweb.tbl_prenotazione_posto
  USING btree (ts_inizio, ts_fine);

CREATE TABLE sbnweb.trl_richiesta_prenot_posto (
  id_richiesta INTEGER NOT NULL, 
  id_prenotazione INTEGER NOT NULL, 
  CONSTRAINT trl_richiesta_prenot_posto_pkey PRIMARY KEY(id_richiesta, id_prenotazione)
) WITHOUT OIDS;

COMMENT ON COLUMN sbnweb.trl_richiesta_prenot_posto.id_richiesta IS 'richiesta di servizio';
COMMENT ON COLUMN sbnweb.trl_richiesta_prenot_posto.id_prenotazione IS 'prenotazione del posto';

CREATE TABLE sbnweb.trl_utente_prenotazione_posto (
  id_utenti INTEGER NOT NULL,
  id_prenotazione INTEGER NOT NULL
) WITHOUT OIDS;

COMMENT ON COLUMN sbnweb.trl_utente_prenotazione_posto.id_utenti IS 'utente lettore';
COMMENT ON COLUMN sbnweb.trl_utente_prenotazione_posto.id_prenotazione IS 'prenotazione del posto';

--
-- Definition for sequence tbl_servizio_id_servizio_seq (OID = 58980) : 
--
CREATE SEQUENCE sbnweb.tbl_servizio_id_servizio_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_servizio (OID = 58982) : 
--
CREATE TABLE sbnweb.tbl_servizio (
    id_servizio integer DEFAULT nextval('tbl_servizio_id_servizio_seq'::regclass) NOT NULL,
    id_tipo_servizio integer NOT NULL,
    cod_servizio char(3) NOT NULL,
    descr varchar(255) NOT NULL,
    num_max_rich smallint NOT NULL,
    num_max_mov smallint NOT NULL,
    dur_mov smallint NOT NULL,
    dur_max_rinn1 smallint NOT NULL,
    dur_max_rinn2 smallint NOT NULL,
    dur_max_rinn3 smallint NOT NULL,
    num_max_riprod smallint NOT NULL,
    max_gg_ant smallint NOT NULL,
    max_gg_dep smallint NOT NULL,
    max_gg_cons smallint NOT NULL,
    n_max_pren numeric(3,0) NOT NULL,
    n_max_ggval_pren numeric(3,0) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    n_max_pren_posto SMALLINT,
    n_gg_prep_supp SMALLINT,
    ts_orario_limite_pren CHAR(5),
    n_gg_rest_ill SMALLINT
) WITHOUT OIDS;

COMMENT ON COLUMN sbnweb.tbl_servizio.n_max_pren_posto IS 'Numero massimo prenotazioni posti in un giorno per un dato utente';
COMMENT ON COLUMN sbnweb.tbl_servizio.n_gg_prep_supp IS 'numero di giorni che la biblioteca si riserva per la preparazione del supporto';
COMMENT ON COLUMN sbnweb.tbl_servizio.ts_orario_limite_pren IS 'Orario limite per inserire la prenotazione che sarà gestita in giornata';
COMMENT ON COLUMN sbnweb.tbl_servizio.n_gg_rest_ill IS 'Numero di giorni a disposizione della biblioteca richiedente per la restituzione alla fornitrice'; 

--
-- Structure for table tbl_servizio_web_dati_richiesti (OID = 58986) : 
--
CREATE TABLE sbnweb.tbl_servizio_web_dati_richiesti (
    campo_richiesta smallint NOT NULL,
    id_tipo_servizio integer NOT NULL,
    obbligatorio char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbl_solleciti (OID = 58989) : 
--
CREATE TABLE sbnweb.tbl_solleciti (
    progr_sollecito smallint NOT NULL,
    cod_rich_serv bigint NOT NULL,
    data_invio date NOT NULL,
    note varchar(160) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    tipo_invio char(1) DEFAULT 3 NOT NULL,
    esito char(1) DEFAULT 'S'::bpchar NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbl_specificita_titoli_studio_id_specificita_titoli_studio_seq (OID = 58994) : 
--
CREATE SEQUENCE sbnweb.tbl_specificita_titoli_studio_id_specificita_titoli_studio_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_specificita_titoli_studio (OID = 58996) : 
--
CREATE TABLE sbnweb.tbl_specificita_titoli_studio (
    id_specificita_titoli_studio integer DEFAULT nextval('tbl_specificita_titoli_studio_id_specificita_titoli_studio_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    tit_studio char(1) NOT NULL,
    specif_tit char(2) NOT NULL,
    descr varchar(250) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbl_storico_richieste_servizio (OID = 59000) : 
--
CREATE TABLE sbnweb.tbl_storico_richieste_servizio (
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_rich_serv numeric(12,0) NOT NULL,
    cod_bib_ut char(3),
    cod_utente numeric(10,0),
    cod_tipo_serv varchar(255) NOT NULL,
    data_richiesta date,
    num_volume smallint,
    num_fasc char(30),
    num_pezzi smallint,
    note_ut varchar(255),
    prezzo_max numeric(12,3),
    costo_servizio numeric(12,3),
    data_massima date,
    note_bibl varchar(255),
    data_proroga date,
    data_in_prev date,
    data_fine_prev date,
    flag_svolg char(1) NOT NULL,
    copyright char(1),
    descr_erog varchar(255),
    descr_stato_ric varchar(255),
    descr_supporto varchar(255),
    descr_risp varchar(255),
    descr_mod_pag varchar(255),
    flag_pren char(1) NOT NULL,
    tipo_doc_lett char(1),
    cod_doc_lett numeric(10,0) DEFAULT 0 NOT NULL,
    prg_esemplare smallint DEFAULT 0 NOT NULL,
    cod_serie char(3),
    cod_inven integer,
    flag_condiz char(1),
    cod_tipo_serv_alt char(2),
    descr_erog_alt varchar(255),
    cod_bib_dest char(3),
    titolo varchar(240),
    autore varchar(160),
    editore char(50),
    anno_edizione char(4),
    luogo_edizione char(30),
    annata char(10),
    num_vol_mon smallint,
    data_in_eff date,
    data_fine_eff date,
    num_rinnovi smallint,
    note_bibliotecario varchar(255),
    descr_stato_mov varchar(255),
    flag_tipo_serv char(1),
    num_solleciti smallint,
    data_ult_soll date,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    denominazione char(255),
    id_prenot_posto INTEGER,
    descr_stato_prenot_posto VARCHAR(255),
    descr_sala VARCHAR(255),
    ts_prenot_posto_inizio TIMESTAMP(6) WITHOUT TIME ZONE,
    ts_prenot_posto_fine TIMESTAMP(6) WITHOUT TIME ZONE

) WITHOUT OIDS;
--
-- Definition for sequence tbl_supporti_biblioteca_id_supporti_biblioteca_seq (OID = 59008) : 
--
CREATE SEQUENCE sbnweb.tbl_supporti_biblioteca_id_supporti_biblioteca_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_supporti_biblioteca (OID = 59010) : 
--
CREATE TABLE sbnweb.tbl_supporti_biblioteca (
    id_supporti_biblioteca integer DEFAULT nextval('tbl_supporti_biblioteca_id_supporti_biblioteca_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_supporto char(2) NOT NULL,
    imp_unita numeric(10,3) NOT NULL,
    costo_fisso numeric(10,3) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    fl_svolg CHAR(1) DEFAULT 'L'
) WITHOUT OIDS;

COMMENT ON COLUMN sbnweb.tbl_supporti_biblioteca.fl_svolg IS 'supporto locale o ILL';


--
-- Definition for sequence tbl_tipi_autorizzazioni_id_tipi_autorizzazione_seq (OID = 59014) : 
--
CREATE SEQUENCE sbnweb.tbl_tipi_autorizzazioni_id_tipi_autorizzazione_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_tipi_autorizzazioni (OID = 59016) : 
--
CREATE TABLE sbnweb.tbl_tipi_autorizzazioni (
    id_tipi_autorizzazione integer DEFAULT nextval('tbl_tipi_autorizzazioni_id_tipi_autorizzazione_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_tipo_aut char(3) NOT NULL,
    descr varchar(160) NOT NULL,
    flag_aut char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbl_tipo_servizio_id_tipo_servizio_seq (OID = 59020) : 
--
CREATE SEQUENCE sbnweb.tbl_tipo_servizio_id_tipo_servizio_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_tipo_servizio (OID = 59022) : 
--
CREATE TABLE sbnweb.tbl_tipo_servizio (
    id_tipo_servizio integer DEFAULT nextval('tbl_tipo_servizio_id_tipo_servizio_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_tipo_serv char(2) NOT NULL,
    num_max_mov smallint NOT NULL,
    ore_ridis smallint NOT NULL,
    penalita char(1) NOT NULL,
    coda_richieste char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    ins_richieste_utente char(1) NOT NULL,
    anche_da_remoto char(1) NOT NULL,
    cd_iso_ill char(2)
) WITHOUT OIDS;
--
-- Definition for sequence tbl_utenti_id_utenti_seq (OID = 59026) : 
--
CREATE SEQUENCE sbnweb.tbl_utenti_id_utenti_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbl_utenti (OID = 59028) : 
--
CREATE TABLE sbnweb.tbl_utenti (
    id_utenti integer DEFAULT nextval('tbl_utenti_id_utenti_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_utente char(25) NOT NULL,
    password varchar(255),
    cognome varchar(80) NOT NULL,
    nome varchar(50) NOT NULL,
    indirizzo_res varchar(105),
    citta_res char(50),
    cap_res varchar(10),
    tel_res char(20),
    fax_res char(20),
    indirizzo_dom varchar(105),
    citta_dom char(50),
    cap_dom varchar(10),
    tel_dom char(20),
    fax_dom char(20),
    sesso char(1) NOT NULL,
    data_nascita date NOT NULL,
    luogo_nascita char(30) NOT NULL,
    cod_fiscale char(16),
    cod_ateneo char(3),
    cod_matricola varchar(25),
    corso_laurea char(26),
    ind_posta_elettr char(80),
    persona_giuridica char(1),
    nome_referente char(50),
    data_reg date,
    credito_polo numeric(12,3),
    note_polo char(50),
    note char(50),
    cod_proven char(1),
    tipo_pers_giur char(1),
    paese_res char(2),
    paese_citt char(2),
    tipo_docum1 char(1),
    num_docum1 char(20),
    aut_ril_docum1 char(40),
    tipo_docum2 char(1),
    num_docum2 char(20),
    aut_ril_docum2 char(40),
    tipo_docum3 char(1),
    num_docum3 char(20),
    aut_ril_docum3 char(40),
    tipo_docum4 char(1),
    num_docum4 char(20),
    aut_ril_docum4 char(40),
    cod_bib char(3) NOT NULL,
    prov_dom char(2),
    prov_res char(2),
    cod_polo_bib char(3) NOT NULL,
    allinea char(1),
    chiave_ute varchar(130),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    change_password char(1) NOT NULL,
    last_access timestamp(6) without time zone NOT NULL,
    ts_var_password timestamp(6) without time zone NOT NULL,
    codice_anagrafe char(16),
    tit_studio char(1),
    professione char(1),
    tidx_vector tsvector,
	ind_posta_elettr2 char(80),
	cd_tipo_ute char(1) DEFAULT 'S'
) WITHOUT OIDS;


CREATE TABLE sbnweb.tbl_accesso_utente (
  id SERIAL, 
  cd_polo CHAR(3) NOT NULL, 
  cd_bib CHAR(3) NOT NULL, 
  id_utente INTEGER, 
  id_posto INTEGER, 
  id_tessera VARCHAR(50) NOT NULL, 
  fl_autenticato CHAR(1) DEFAULT 'S' NOT NULL, 
  ts_evento TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  fl_evento CHAR(1) NOT NULL, 
  fl_forzatura CHAR(1) DEFAULT 'N' NOT NULL,
  ute_ins CHAR(12) NOT NULL, 
  ts_ins TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  ute_var CHAR(12) NOT NULL, 
  ts_var TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  fl_canc CHAR(1) NOT NULL, 
  CONSTRAINT tbl_accesso_utente_pkey PRIMARY KEY(id)
) WITHOUT OIDS;

COMMENT ON COLUMN sbnweb.tbl_accesso_utente.id_tessera
IS 'Identificativo delle tessera';

COMMENT ON COLUMN sbnweb.tbl_accesso_utente.fl_autenticato
IS 'indicazione che l''identificativo fornito è presente in base dati';

COMMENT ON COLUMN sbnweb.tbl_accesso_utente.fl_evento
IS 'stato dell''accesso: E=Entrata; U=Uscita';

COMMENT ON COLUMN sbnweb.tbl_accesso_utente.fl_forzatura
IS 'Indicazione che l''evento è stato inserito normalmente o forzato dal sistema';

CREATE INDEX tessera_data_idx ON sbnweb.tbl_accesso_utente
  USING btree (id_tessera, ts_evento);

--
-- Definition for sequence tbp_esemplare_fascicolo_id_ese_fascicolo_seq (OID = 59035) : 
--
CREATE SEQUENCE sbnweb.tbp_esemplare_fascicolo_id_ese_fascicolo_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbp_esemplare_fascicolo (OID = 59037) : 
--
CREATE TABLE sbnweb.tbp_esemplare_fascicolo (
    id_ese_fascicolo integer DEFAULT nextval('tbp_esemplare_fascicolo_id_ese_fascicolo_seq'::regclass) NOT NULL,
    bid char(10) NOT NULL,
    fid integer NOT NULL,
    id_ordine integer,
    cd_bib_abb char(3),
    data_arrivo date,
    cd_polo_inv char(3),
    cd_bib_inv char(3),
    cd_serie char(3),
    cd_inven integer,
    grp_fasc integer,
    note varchar(240),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    cd_no_disp char(1)
) WITHOUT OIDS;
--
-- Structure for table tbp_fascicolo (OID = 59041) : 
--
CREATE TABLE sbnweb.tbp_fascicolo (
    bid char(10) NOT NULL,
    fid integer NOT NULL,
    sici varchar(80),
    ean char(13),
    data_conv_pub date DEFAULT to_date('19010101'::text, 'YYYYMMDD'::text) NOT NULL,
    cd_per char(1),
    cd_tipo_fasc char(1),
    data_pubb varchar(80),
    descrizione varchar(240),
    annata char(10),
    num_volume smallint,
    num_in_fasc integer,
    data_in_pubbl date,
    num_fi_fasc integer,
    data_fi_pubbl date,
    note varchar(240),
    num_alter char(15),
    bid_link char(10),
    fid_link integer,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    anno_pub smallint
) WITHOUT OIDS;
--
-- Structure for table tbq_batch_attivabili (OID = 59048) : 
--
CREATE TABLE sbnweb.tbq_batch_attivabili (
    cod_funz char(5) NOT NULL,
    descr char(32),
    descr_b char(20),
    server char(1),
    path char(30),
    job_name char(16),
    tipo char(4),
    queue_name char(16),
    flg_paral char(1),
    priorita char(1),
    flg_prior char(1),
    flg_canc char(1),
    flg_stampa char(1),
    flg_name char(20),
    flg_fname char(20),
    condiz varchar(255),
    canc char(1),
    cd_bib_richieste_batch char(3),
    msg_coda_jms_richieste_batch varchar(100),
    cod_funz_richieste_batch char(4),
    cd_polo_richieste_batch char(3) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbr_fornitori_cod_fornitore_seq (OID = 59054) : 
--
CREATE SEQUENCE sbnweb.tbr_fornitori_cod_fornitore_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbr_fornitori (OID = 59056) : 
--
CREATE TABLE sbnweb.tbr_fornitori (
    cod_fornitore integer DEFAULT nextval('tbr_fornitori_cod_fornitore_seq'::regclass) NOT NULL,
    nom_fornitore varchar(50) NOT NULL,
    unit_org char(50) NOT NULL,
    indirizzo varchar(70) NOT NULL,
    cpostale char(20) NOT NULL,
    citta char(20) NOT NULL,
    cap char(10) NOT NULL,
    telefono char(20) NOT NULL,
    fax char(20) NOT NULL,
    note varchar(160) NOT NULL,
    p_iva char(18) NOT NULL,
    cod_fiscale char(18) NOT NULL,
    e_mail char(50) NOT NULL,
    paese char(2) NOT NULL,
    tipo_partner char(1) NOT NULL,
    provincia char(2) NOT NULL,
    cod_bib char(3) NOT NULL,
    chiave_for char(50) NOT NULL,
    cod_polo_bib char(3) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    tidx_vector tsvector,
    regione char(2)
) WITHOUT OIDS;
--
-- Structure for table tbr_fornitori_biblioteche (OID = 59063) : 
--
CREATE TABLE sbnweb.tbr_fornitori_biblioteche (
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cod_fornitore integer NOT NULL,
    tipo_pagamento char(50) NOT NULL,
    cod_cliente char(40) NOT NULL,
    nom_contatto char(50) NOT NULL,
    tel_contatto char(20) NOT NULL,
    fax_contatto char(20) NOT NULL,
    valuta char(3) NOT NULL,
    cod_polo char(3) NOT NULL,
    allinea char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_aut_aut (OID = 59066) : 
--
CREATE TABLE sbnweb.tr_aut_aut (
    vid_base char(10) NOT NULL,
    vid_coll char(10) NOT NULL,
    tp_legame char(1) NOT NULL,
    nota_aut_aut varchar(320),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_aut_bib (OID = 59069) : 
--
CREATE TABLE sbnweb.tr_aut_bib (
    vid char(10) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    fl_allinea char(1) NOT NULL,
    fl_allinea_sbnmarc char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_aut_mar (OID = 59072) : 
--
CREATE TABLE sbnweb.tr_aut_mar (
    vid char(10) NOT NULL,
    mid char(10) NOT NULL,
    nota_aut_mar varchar(80),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_des_des (OID = 59075) : 
--
CREATE TABLE sbnweb.tr_des_des (
    did_base char(10) NOT NULL,
    did_coll char(10) NOT NULL,
    tp_legame char(5) NOT NULL,
    nota_des_des varchar(240),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_luo_luo (OID = 59078) : 
--
CREATE TABLE sbnweb.tr_luo_luo (
    lid_base char(10) NOT NULL,
    lid_coll char(10) NOT NULL,
    tp_legame char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_mar_bib (OID = 59081) : 
--
CREATE TABLE sbnweb.tr_mar_bib (
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    mid char(10) NOT NULL,
    fl_allinea char(1) NOT NULL,
    fl_allinea_sbnmarc char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_modello_sistemi_classi_biblioteche (OID = 59084) : 
--
CREATE TABLE sbnweb.tr_modello_sistemi_classi_biblioteche (
    id_modello integer NOT NULL,
    nome char(80) NOT NULL,
    cd_sistema char(3) NOT NULL,
    cd_edizione char(2) NOT NULL,
    sololocale char(1) NOT NULL,
    flg_att char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_modello_soggettari_biblioteche (OID = 59087) : 
--
CREATE TABLE sbnweb.tr_modello_soggettari_biblioteche (
    id_modello integer NOT NULL,
    nome char(80) NOT NULL,
    cd_sogg char(3) NOT NULL,
    solo_locale char(1),
    fl_att char(1) NOT NULL,
    fl_auto_loc char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_modello_thesauri_biblioteche (OID = 59090) : 
--
CREATE TABLE sbnweb.tr_modello_thesauri_biblioteche (
    id_modello integer NOT NULL,
    nome char(80) NOT NULL,
    cd_the char(3) NOT NULL,
    sololocale char(1),
    fl_att char(1) NOT NULL,
    fl_auto_loc char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_per_int (OID = 59093) : 
--
CREATE TABLE sbnweb.tr_per_int (
    vid char(10) NOT NULL,
    id_personaggio integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_rep_aut (OID = 59096) : 
--
CREATE TABLE sbnweb.tr_rep_aut (
    vid char(10) NOT NULL,
    id_repertorio integer NOT NULL,
    note_rep_aut varchar(160),
    fl_trovato char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;

--
-- Structure for table tr_rep_luo : 
--

CREATE TABLE sbnweb.tr_rep_luo
(
   lid            char(10)             NOT NULL,
   id_repertorio  integer              NOT NULL,
   nota_rep_luo   varchar(160),
   ute_ins        char(12)             NOT NULL,
   ts_ins         timestamp(1)         NOT NULL,
   ute_var        char(12)             NOT NULL,
   ts_var         timestamp(1)         NOT NULL,
   fl_canc        char(1)              NOT NULL
   ) WITHOUT OIDS;

--
-- Structure for table tr_rep_mar (OID = 59099) : 
--
CREATE TABLE sbnweb.tr_rep_mar (
    progr_repertorio integer NOT NULL,
    mid char(10) NOT NULL,
    id_repertorio integer NOT NULL,
    nota_rep_mar varchar(160),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_rep_tit (OID = 59102) : 
--
CREATE TABLE sbnweb.tr_rep_tit (
    bid char(10) NOT NULL,
    id_repertorio integer NOT NULL,
    nota_rep_tit varchar(160),
    fl_trovato char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_sistemi_classi_biblioteche (OID = 59105) : 
--
CREATE TABLE sbnweb.tr_sistemi_classi_biblioteche (
    cd_biblioteca char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_sistema char(3) NOT NULL,
    cd_edizione char(2) NOT NULL,
    flg_att char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    sololocale char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_sog_des (OID = 59108) : 
--
CREATE TABLE sbnweb.tr_sog_des (
    did char(10) NOT NULL,
    cid char(10) NOT NULL,
    fl_posizione integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    fl_primavoce char(1)
) WITHOUT OIDS;
--
-- Structure for table tr_soggettari_biblioteche (OID = 59111) : 
--
CREATE TABLE sbnweb.tr_soggettari_biblioteche (
    cd_sogg char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    fl_att char(1) NOT NULL,
    fl_auto_loc char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    sololocale char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_sogp_sogi (OID = 59114) : 
--
CREATE TABLE sbnweb.tr_sogp_sogi (
    cid_p char(10) NOT NULL,
    cid_i char(10) NOT NULL,
    bid char(10) NOT NULL,
    fl_imp_sog char(1) NOT NULL,
    fl_sog_mod_da char(1),
    fl_imp_tit_sog char(1),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_termini_termini (OID = 59117) : 
--
CREATE TABLE sbnweb.tr_termini_termini (
    did_base char(10) NOT NULL,
    did_coll char(10) NOT NULL,
    nota_termini_termini varchar(240),
    tipo_coll char(5) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_the_cla (OID = 59120) : 
--
CREATE TABLE sbnweb.tr_the_cla (
    cd_the char(3) NOT NULL,
    did char(10) NOT NULL,
    cd_sistema char(3) NOT NULL,
    cd_edizione char(2) NOT NULL,
    classe char(31) NOT NULL,
    nota_the_cla varchar(240),
    posizione smallint DEFAULT 0 NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_thesauri_biblioteche (OID = 59124) : 
--
CREATE TABLE sbnweb.tr_thesauri_biblioteche (
    cd_biblioteca char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_the char(3) NOT NULL,
    fl_att char(1) NOT NULL,
    fl_auto_loc char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    sololocale char(1)
) WITHOUT OIDS;
--
-- Structure for table tr_tit_aut (OID = 59127) : 
--
CREATE TABLE sbnweb.tr_tit_aut (
    bid char(10) NOT NULL,
    vid char(10) NOT NULL,
    tp_responsabilita char(1) NOT NULL,
    cd_relazione char(3) NOT NULL,
    nota_tit_aut varchar(80),
    fl_incerto char(1) NOT NULL,
    fl_superfluo char(1) NOT NULL,
    cd_strumento_mus char(14),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    fl_condiviso char(1) DEFAULT '''s''::bpchar'::bpchar NOT NULL,
    ute_condiviso char(12),
    ts_condiviso timestamp(6) without time zone
) WITHOUT OIDS;
--
-- Structure for table tr_tit_bib (OID = 59131) : 
--
CREATE TABLE sbnweb.tr_tit_bib (
    bid char(10) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    fl_gestione char(1) NOT NULL,
    fl_disp_elettr char(1) NOT NULL,
    fl_allinea char(1) NOT NULL,
    fl_allinea_sbnmarc char(1) NOT NULL,
    fl_allinea_cla char(1) NOT NULL,
    fl_allinea_sog char(1) NOT NULL,
    fl_allinea_luo char(1) NOT NULL,
    fl_allinea_rep char(1) NOT NULL,
    fl_mutilo char(1) NOT NULL,
    ds_consistenza varchar(400),
    fl_possesso char(1) NOT NULL,
    ds_fondo varchar(55),
    ds_segn varchar(30),
    ds_antica_segn varchar(120),
    nota_tit_bib varchar(134),
    uri_copia varchar(1280),
    tp_digitalizz char(1),
    ts_ins_prima_coll timestamp(6) without time zone,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_tit_cla (OID = 59137) : 
--
CREATE TABLE sbnweb.tr_tit_cla (
    bid char(10) NOT NULL,
    classe char(31) NOT NULL,
    cd_sistema char(3) NOT NULL,
    cd_edizione char(2) NOT NULL,
    nota_tit_cla varchar(240),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_tit_luo (OID = 59140) : 
--
CREATE TABLE sbnweb.tr_tit_luo (
    bid char(10) NOT NULL,
    lid char(10) NOT NULL,
    tp_luogo char(1) NOT NULL,
    nota_tit_luo varchar(80),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_tit_mar (OID = 59143) : 
--
CREATE TABLE sbnweb.tr_tit_mar (
    bid char(10) NOT NULL,
    mid char(10) NOT NULL,
    nota_tit_mar varchar(80),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_tit_sog_bib (OID = 59146) : 
--
CREATE TABLE sbnweb.tr_tit_sog_bib (
    cid char(10) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_sogg char(3) NOT NULL,
    bid char(10) NOT NULL,
    nota_tit_sog_bib varchar(240),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    posizione smallint DEFAULT 1 NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_tit_tit (OID = 59149) : 
--
CREATE TABLE sbnweb.tr_tit_tit (
    bid_base char(10) NOT NULL,
    bid_coll char(10) NOT NULL,
    tp_legame char(2) NOT NULL,
    tp_legame_musica char(1),
    cd_natura_base char(1) NOT NULL,
    cd_natura_coll char(1) NOT NULL,
    sequenza char(10),
    nota_tit_tit varchar(80),
    sequenza_musica varchar(80),
    sici varchar(80),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    fl_condiviso char(1) DEFAULT '''s''::bpchar'::bpchar NOT NULL,
    ute_condiviso char(12),
    ts_condiviso timestamp(6) without time zone
) WITHOUT OIDS;
--
-- Structure for table tra_elementi_buono_ordine (OID = 59153) : 
--
CREATE TABLE sbnweb.tra_elementi_buono_ordine (
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    buono_ord char(9) NOT NULL,
    cod_tip_ord char(1) NOT NULL,
    anno_ord numeric(4,0) NOT NULL,
    cod_ord integer NOT NULL,
    ute_ins char(12),
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tra_fornitori_offerte (OID = 59156) : 
--
CREATE TABLE sbnweb.tra_fornitori_offerte (
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_fornitore integer NOT NULL,
    cod_rich_off bigint NOT NULL,
    note varchar(160) NOT NULL,
    stato_gara char(1) NOT NULL,
    tipo_invio char(1) NOT NULL,
    data_invio date NOT NULL,
    risp_da_risp varchar(255) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tra_messaggi (OID = 59159) : 
--
CREATE TABLE sbnweb.tra_messaggi (
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_msg integer NOT NULL,
    data_msg date NOT NULL,
    note char(255),
    anno_fattura numeric(4,0),
    progr_fattura integer,
    cod_tip_ord char(1),
    anno_ord numeric(4,0),
    cod_ord integer,
    stato_msg char(1) NOT NULL,
    tipo_msg char(2) NOT NULL,
    tipo_invio char(1) NOT NULL,
    cod_fornitore integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tra_ordine_inventari (OID = 59162) : 
--
CREATE TABLE sbnweb.tra_ordine_inventari (
    id_ordine integer NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cd_serie char(3) NOT NULL,
    cd_inven integer NOT NULL,
    data_uscita timestamp(0) without time zone,
    data_rientro timestamp(0) without time zone,
    ota_fornitore varchar(255),
    ute_ins char(12),
    ute_var char(12),
    ts_ins timestamp(0) without time zone,
    ts_var timestamp(0) without time zone,
    data_rientro_presunta timestamp(0) without time zone,
    posizione smallint,
    volume smallint
) WITHOUT OIDS;
--
-- Structure for table tra_ordini_biblioteche (OID = 59165) : 
--
CREATE TABLE sbnweb.tra_ordini_biblioteche (
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cod_bib_ord char(3) NOT NULL,
    cod_tip_ord char(1) NOT NULL,
    anno_ord numeric(4,0) NOT NULL,
    cod_ord integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tra_sez_acq_storico (OID = 59168) : 
--
CREATE TABLE sbnweb.tra_sez_acq_storico (
    id_sez_acquis_bibliografiche integer NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    data_var_bdg date NOT NULL,
    budget_old numeric(15,3) NOT NULL,
    importo_diff numeric(15,3) NOT NULL,
    ute_ins char(12) NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tra_sez_acquisizione_fornitori (OID = 59171) : 
--
CREATE TABLE sbnweb.tra_sez_acquisizione_fornitori (
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cod_prac numeric(10,0) NOT NULL,
    cod_fornitore integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trc_formati_sezioni (OID = 59174) : 
--
CREATE TABLE sbnweb.trc_formati_sezioni (
    cd_formato char(2) NOT NULL,
    cd_polo_sezione char(3) NOT NULL,
    cd_bib_sezione char(3) NOT NULL,
    cd_sezione char(10) NOT NULL,
    prog_serie integer NOT NULL,
    prog_num integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    descr char(30),
    n_pezzi integer NOT NULL,
    n_pezzi_misc integer DEFAULT 20 NOT NULL,
    prog_serie_num1_misc integer DEFAULT 0 NOT NULL,
    progr_num1_misc integer DEFAULT 0 NOT NULL,
    prog_serie_num2_misc integer DEFAULT 0 NOT NULL,
    progr_num2_misc integer DEFAULT 0 NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trc_poss_prov_inventari (OID = 59182) : 
--
CREATE TABLE sbnweb.trc_poss_prov_inventari (
    pid char(10) NOT NULL,
    cd_inven integer NOT NULL,
    cd_serie char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_legame char(1) NOT NULL,
    nota varchar(320),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trc_possessori_possessori (OID = 59185) : 
--
CREATE TABLE sbnweb.trc_possessori_possessori (
    pid_base char(10) NOT NULL,
    pid_coll char(10) NOT NULL,
    tp_legame char(1) NOT NULL,
    nota varchar(80),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence trf_attivita_affiliate_id_seq (OID = 59188) : 
--
CREATE SEQUENCE sbnweb.trf_attivita_affiliate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table trf_attivita_affiliate (OID = 59190) : 
--
CREATE TABLE sbnweb.trf_attivita_affiliate (
    id integer DEFAULT nextval('trf_attivita_affiliate_id_seq'::regclass) NOT NULL,
    cd_attivita char(5) NOT NULL,
    cd_bib_affiliata char(3) NOT NULL,
    cd_polo_bib_affiliata char(3) NOT NULL,
    cd_bib_centro_sistema char(3) NOT NULL,
    cd_polo_bib_centro_sistema char(3) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc char(1)
) WITHOUT OIDS;
--
-- Definition for sequence trf_attivita_polo_id_attivita_polo_seq (OID = 59194) : 
--
CREATE SEQUENCE sbnweb.trf_attivita_polo_id_attivita_polo_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table trf_attivita_polo (OID = 59196) : 
--
CREATE TABLE sbnweb.trf_attivita_polo (
    cd_polo char(3) NOT NULL,
    cd_attivita char(5) NOT NULL,
    id_attivita_polo integer DEFAULT nextval('trf_attivita_polo_id_attivita_polo_seq'::regclass) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trf_funzioni_denominazioni (OID = 59200) : 
--
CREATE TABLE sbnweb.trf_funzioni_denominazioni (
    cd_funzione char(5) NOT NULL,
    cd_lingua char(5) NOT NULL,
    ds_nome varchar(100) NOT NULL,
    ds_nome_breve varchar(30),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trf_gruppo_attivita (OID = 59203) : 
--
CREATE TABLE sbnweb.trf_gruppo_attivita (
    id_gruppo_attivita_polo_base integer NOT NULL,
    id_gruppo_attivita_polo_coll integer NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trf_gruppo_attivita_polo (OID = 59206) : 
--
CREATE TABLE sbnweb.trf_gruppo_attivita_polo (
    id_attivita_polo integer NOT NULL,
    id_gruppo_attivita_polo integer NOT NULL,
    fl_include char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence trf_profilo_biblioteca_id_profilo_abilitazione_biblioteca_seq (OID = 59209) : 
--
CREATE SEQUENCE sbnweb.trf_profilo_biblioteca_id_profilo_abilitazione_biblioteca_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table trf_profilo_biblioteca (OID = 59211) : 
--
CREATE TABLE sbnweb.trf_profilo_biblioteca (
    id_profilo_abilitazione_biblioteca integer DEFAULT nextval('trf_profilo_biblioteca_id_profilo_abilitazione_biblioteca_seq'::regclass) NOT NULL,
    cd_attivita char(5),
    id_profilo_abilitazione integer,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trf_profilo_profilo (OID = 59215) : 
--
CREATE TABLE sbnweb.trf_profilo_profilo (
    id_profilo_abilitazione_base integer NOT NULL,
    id_profilo_abilitazione_coll integer NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trf_utente_professionale_biblioteca (OID = 59218) : 
--
CREATE TABLE sbnweb.trf_utente_professionale_biblioteca (
    id_utente_professionale integer NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    tp_ruolo char(10),
    note_competenze varchar(160),
    ufficio_appart char(50)
) WITHOUT OIDS;
--
-- Structure for table trf_utente_professionale_polo (OID = 59221) : 
--
CREATE TABLE sbnweb.trf_utente_professionale_polo (
    id_utente_professionale integer NOT NULL,
    cd_polo char(3) NOT NULL,
    ute_var char(12),
    ts_ins timestamp(6) without time zone,
    ute_ins char(12),
    ts_var timestamp(6) without time zone,
    fl_canc char(1)
) WITHOUT OIDS;
--
-- Structure for table trl_attivita_bibliotecario (OID = 59224) : 
--
CREATE TABLE sbnweb.trl_attivita_bibliotecario (
    id_bibliotecario integer NOT NULL,
    id_iter_servizio integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trl_autorizzazioni_servizi (OID = 59227) : 
--
CREATE TABLE sbnweb.trl_autorizzazioni_servizi (
    id_tipi_autorizzazione integer NOT NULL,
    id_servizio integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trl_diritti_utente (OID = 59230) : 
--
CREATE TABLE sbnweb.trl_diritti_utente (
    id_utenti integer NOT NULL,
    id_servizio integer NOT NULL,
    data_inizio_serv date,
    data_fine_serv date,
    data_inizio_sosp date,
    data_fine_sosp date,
    note varchar(160),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone,
    fl_canc char(1) NOT NULL,
    cod_tipo_aut char(3)
) WITHOUT OIDS;
--
-- Structure for table trl_materie_utenti (OID = 59233) : 
--
CREATE TABLE sbnweb.trl_materie_utenti (
    id_utenti integer NOT NULL,
    id_materia integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trl_posti_sala_utenti_biblioteca (OID = 59236) : 
--
CREATE TABLE sbnweb.trl_posti_sala_utenti_biblioteca (
    id_posti_sala integer NOT NULL,
    id_utenti_biblioteca integer NOT NULL,
    ts_ingresso timestamp(6) without time zone,
    ts_uscita timestamp(6) without time zone,
    ute_ins char(12) NOT NULL,
    ute_var char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence trl_relazioni_servizi_id_seq (OID = 59239) : 
--
CREATE SEQUENCE sbnweb.trl_relazioni_servizi_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table trl_relazioni_servizi (OID = 59241) : 
--
CREATE TABLE sbnweb.trl_relazioni_servizi (
    id integer DEFAULT nextval('trl_relazioni_servizi_id_seq'::regclass) NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_bib char(3) NOT NULL,
    cd_relazione varchar(10) NOT NULL,
    tabella_di_relazione varchar(255) NOT NULL,
    id_relazione_tabella_di_relazione integer NOT NULL,
    tabella_tb_codici varchar(255) NOT NULL,
    id_relazione_tb_codici varchar(255) NOT NULL,
    uet_ins char(12) NOT NULL,
    ts_is timestamp(0) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trl_supporti_modalita_erogazione (OID = 59248) : 
--
CREATE TABLE sbnweb.trl_supporti_modalita_erogazione (
    cod_erog char(2) NOT NULL,
    id_supporti_biblioteca integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trl_tariffe_modalita_erogazione (OID = 59251) : 
--
CREATE TABLE sbnweb.trl_tariffe_modalita_erogazione (
    cod_erog char(2) NOT NULL,
    id_tipo_servizio integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence trl_utenti_biblioteca_id_utenti_biblioteca_seq (OID = 59254) : 
--
CREATE SEQUENCE sbnweb.trl_utenti_biblioteca_id_utenti_biblioteca_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table trl_utenti_biblioteca (OID = 59256) : 
--
CREATE TABLE sbnweb.trl_utenti_biblioteca (
    id_utenti_biblioteca integer DEFAULT nextval('trl_utenti_biblioteca_id_utenti_biblioteca_seq'::regclass) NOT NULL,
    id_utenti integer NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    id_specificita_titoli_studio integer,
    id_occupazioni integer,
    credito_bib numeric(12,3),
    note_bib text,
    data_inizio_aut date,
    data_fine_aut date,
    data_inizio_sosp date,
    data_fine_sosp date,
    cod_tipo_aut char(3),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trp_messaggio_fascicolo (OID = 59260) : 
--
CREATE TABLE sbnweb.trp_messaggio_fascicolo (
    cd_polo char(10) NOT NULL,
    cd_bib char(3) NOT NULL,
    cd_msg integer NOT NULL,
    bid char(10) NOT NULL,
    fid integer NOT NULL,
    id_ordine integer NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trs_termini_titoli_biblioteche (OID = 59263) : 
--
CREATE TABLE sbnweb.trs_termini_titoli_biblioteche (
    bid char(10) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    cd_the char(3) NOT NULL,
    cd_polo char(3) NOT NULL,
    did char(10) NOT NULL,
    nota_termine_titoli_biblioteca varchar(240),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence ts_link_multim_id_link_multim_seq (OID = 59266) : 
--
CREATE SEQUENCE sbnweb.ts_link_multim_id_link_multim_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table ts_link_multim (OID = 59268) : 
--
CREATE TABLE sbnweb.ts_link_multim (
    id_link_multim integer DEFAULT nextval('ts_link_multim_id_link_multim_seq'::regclass) NOT NULL,
    ky_link_multim char(10) NOT NULL,
    uri_multim varchar(320),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
    immagine oid
) WITHOUT OIDS;
--
-- Structure for table ts_note_proposta (OID = 59272) : 
--
CREATE TABLE sbnweb.ts_note_proposta (
    id_proposta integer NOT NULL,
    progr_risposta integer NOT NULL,
    ute_destinatario char(12) NOT NULL,
    note_pro varchar(320),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table ts_proposta (OID = 59275) : 
--
CREATE TABLE sbnweb.ts_proposta (
    ute_mittente char(12) NOT NULL,
    progr_proposta integer NOT NULL,
    ute_destinatario char(12) NOT NULL,
    bidvid char(10) NOT NULL,
    tp_messaggio char(1) NOT NULL,
    ds_proposta varchar(400) NOT NULL,
    dt_inoltro date NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence ts_proposta_marc_id_proposta_seq (OID = 59278) : 
--
CREATE SEQUENCE sbnweb.ts_proposta_marc_id_proposta_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table ts_proposta_marc (OID = 59280) : 
--
CREATE TABLE sbnweb.ts_proposta_marc (
    id_proposta integer DEFAULT nextval('ts_proposta_marc_id_proposta_seq'::regclass) NOT NULL,
    ute_mittente char(12) NOT NULL,
    id_oggetto varchar(31) NOT NULL,
    tp_messaggio char(1) NOT NULL,
    ds_proposta varchar(400) NOT NULL,
    dt_inoltro date NOT NULL,
    cd_oggetto char(2) NOT NULL,
    cd_stato char(2) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table ts_servizio (OID = 59284) : 
--
CREATE TABLE sbnweb.ts_servizio (
    cd_record char(4) NOT NULL,
    campo1 varchar(20),
    campo2 varchar(20),
    campo3 varchar(20)
) WITHOUT OIDS;
--
-- Definition for sequence ts_stop_list_id_stop_list_seq (OID = 59287) : 
--
CREATE SEQUENCE sbnweb.ts_stop_list_id_stop_list_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table ts_stop_list (OID = 59289) : 
--
CREATE TABLE sbnweb.ts_stop_list (
    id_stop_list integer DEFAULT nextval('ts_stop_list_id_stop_list_seq'::regclass) NOT NULL,
    tp_stop_list char(1) NOT NULL,
    cd_lingua char(3) NOT NULL,
    parola varchar(80) NOT NULL,
    nota_stop_list varchar(320),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone DEFAULT now() NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone DEFAULT now() NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for view v_aut_aut (OID = 59295) : 
--
CREATE VIEW sbnweb.v_aut_aut AS
SELECT a1.vid_base AS vid_1, a1.vid_coll AS vid_2, a1.tp_legame, a1.nota_aut_aut
FROM tr_aut_aut a1
WHERE (a1.fl_canc <> 'S'::bpchar)
UNION
SELECT a2.vid_coll AS vid_1, a2.vid_base AS vid_2, a2.tp_legame, a2.nota_aut_aut
FROM tr_aut_aut a2
WHERE (a2.fl_canc <> 'S'::bpchar);

--
-- Definition for view v_cartografia (OID = 59299) : 
--
CREATE VIEW sbnweb.v_cartografia (
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    fl_condiviso,
    ts_condiviso,
    ute_condiviso)
AS
SELECT c.cd_livello AS cd_livello_c, c.tp_pubb_gov, c.cd_colore,
    c.cd_meridiano, c.cd_supporto_fisico, c.cd_tecnica, c.cd_forma_ripr,
    c.cd_forma_pubb, c.cd_altitudine, c.cd_tiposcala, c.tp_scala, c.scala_oriz,
    c.scala_vert, c.longitudine_ovest, c.longitudine_est, c.latitudine_nord,
    c.latitudine_sud, c.tp_immagine, c.cd_forma_cart, c.cd_piattaforma,
    c.cd_categ_satellite, c.tp_proiezione,
	t.bid, t.isadn, t.tp_materiale, t.tp_record_uni,
    t.cd_natura, t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3,
    t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2,
    t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t,
    t.ky_clet2_t, t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit, t.nota_cat_tit, t.bid_link,
    t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var, t.ute_forza_ins,
    t.ute_forza_var, t.fl_canc, t.fl_condiviso, t.ts_condiviso, t.ute_condiviso
FROM tb_cartografia c, tb_titolo t
WHERE t.bid = c.bid AND t.fl_canc <> 'S' AND c.fl_canc <> 'S';

--
-- Definition for view v_composizione (OID = 59304) : 
--
CREATE VIEW sbnweb.v_composizione AS
SELECT t.bid, t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura,
    t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1,
    t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3,
    t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t,
    t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit, t.nota_cat_tit,
    t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var,
    t.ute_forza_ins, t.ute_forza_var, t.fl_canc, t.fl_condiviso,
    t.ts_condiviso, t.ute_condiviso, m.ds_org_sint, m.ds_org_anal,
    c.numero_ordine, c.numero_opera, c.numero_cat_tem, c.cd_tonalita,
    c.datazione, c.ky_ord_ric, c.ky_est_ric, c.ky_app_ric, c.ky_ord_clet,
    c.ky_est_clet, c.ky_app_clet, c.ky_ord_pre, c.ky_est_pre, c.ky_app_pre,
    c.ky_ord_den, c.ky_est_den, c.ky_app_den, c.ky_ord_nor_pre,
    c.ky_est_nor_pre, c.ky_app_nor_pre, c.cd_forma_1, c.cd_forma_2,
    c.cd_forma_3, c.aa_comp_1, c.aa_comp_2
FROM tb_titolo t, tb_musica m, tb_composizione c
WHERE (((((m.bid = t.bid) AND (c.bid = t.bid)) AND (t.fl_canc <>
    'S'::bpchar)) AND (m.fl_canc <> 'S'::bpchar)) AND (c.fl_canc <> 'S'::bpchar));

--
-- Definition for view v_dat001b (OID = 59309) : 
--
CREATE VIEW sbnweb.v_dat001b AS
((
SELECT tb_titolo.bid, tb_titolo.cd_natura AS natura, tb_titolo.tp_materiale
    AS materiale, tb_titolo.aa_pubb_1 AS data1, tb_titolo.cd_livello AS livello
FROM tb_titolo
WHERE ((NOT (tb_titolo.cd_natura = 'M'::bpchar)) AND (NOT
    (tb_titolo.fl_canc = 'S'::bpchar)))
UNION
SELECT t.bid, t.cd_natura AS natura, t.tp_materiale AS materiale,
    t.aa_pubb_1 AS data1, t.cd_livello AS livello
FROM tb_titolo t
WHERE (((t.cd_natura = 'M'::bpchar) AND (NOT (t.fl_canc = 'S'::bpchar)))
    AND (NOT (EXISTS (
    SELECT tr.bid_base, tr.bid_coll, tr.tp_legame, tr.tp_legame_musica,
        tr.cd_natura_base, tr.cd_natura_coll, tr.sequenza, tr.nota_tit_tit,
        tr.sequenza_musica, tr.sici, tr.ute_ins, tr.ts_ins, tr.ute_var,
        tr.ts_var, tr.fl_canc, tr.fl_condiviso, tr.ute_condiviso, tr.ts_condiviso
    FROM tr_tit_tit tr
    WHERE ((((t.bid = tr.bid_coll) AND (tr.fl_canc <> 'S'::bpchar)) AND
        (tr.tp_legame = '01'::bpchar)) AND ((tr.cd_natura_base =
        'M'::bpchar) OR (tr.cd_natura_base = 'W'::bpchar)))
    )))))
UNION
SELECT tz.bid, 'Z' AS natura, tz.tp_materiale AS materiale, tz.aa_pubb_1 AS
    data1, tz.cd_livello AS livello
FROM tb_titolo tz
WHERE (((tz.cd_natura = 'M'::bpchar) AND (NOT (tz.fl_canc = 'S'::bpchar)))
    AND (EXISTS (
    SELECT trz.bid_base, trz.bid_coll, trz.tp_legame, trz.tp_legame_musica,
        trz.cd_natura_base, trz.cd_natura_coll, trz.sequenza,
        trz.nota_tit_tit, trz.sequenza_musica, trz.sici, trz.ute_ins,
        trz.ts_ins, trz.ute_var, trz.ts_var, trz.fl_canc, trz.fl_condiviso,
        trz.ute_condiviso, trz.ts_condiviso
    FROM tr_tit_tit trz
    WHERE ((((((tz.bid = trz.bid_coll) AND (trz.cd_natura_coll =
        'M'::bpchar)) AND (trz.tp_legame = '01'::bpchar)) AND
        ((trz.cd_natura_base = 'M'::bpchar) OR (trz.cd_natura_base =
        'W'::bpchar))) AND (NOT (trz.fl_canc = 'S'::bpchar))) AND (NOT (EXISTS (
        SELECT trb.bid_base, trb.bid_coll, trb.tp_legame,
            trb.tp_legame_musica, trb.cd_natura_base, trb.cd_natura_coll,
            trb.sequenza, trb.nota_tit_tit, trb.sequenza_musica, trb.sici,
            trb.ute_ins, trb.ts_ins, trb.ute_var, trb.ts_var, trb.fl_canc,
            trb.fl_condiviso, trb.ute_condiviso, trb.ts_condiviso
        FROM tr_tit_tit trb
        WHERE (((((trz.bid_coll = trb.bid_base) AND (trb.cd_natura_base =
            'M'::bpchar)) AND (trb.cd_natura_coll = 'M'::bpchar)) AND
            (trb.tp_legame = '01'::bpchar)) AND (NOT (trb.fl_canc = 'S'::bpchar)))
        ))))
    ))))
UNION
SELECT ty.bid, 'Y' AS natura, ty.tp_materiale AS materiale, ty.aa_pubb_1 AS
    data1, ty.cd_livello AS livello
FROM tb_titolo ty
WHERE (((ty.cd_natura = 'M'::bpchar) AND (NOT (ty.fl_canc = 'S'::bpchar)))
    AND (EXISTS (
    SELECT a.bid_base, a.bid_coll, a.tp_legame, a.tp_legame_musica,
        a.cd_natura_base, a.cd_natura_coll, a.sequenza, a.nota_tit_tit,
        a.sequenza_musica, a.sici, a.ute_ins, a.ts_ins, a.ute_var,
        a.ts_var, a.fl_canc, a.fl_condiviso, a.ute_condiviso,
        a.ts_condiviso, b.bid_base, b.bid_coll, b.tp_legame,
        b.tp_legame_musica, b.cd_natura_base, b.cd_natura_coll, b.sequenza,
        b.nota_tit_tit, b.sequenza_musica, b.sici, b.ute_ins, b.ts_ins,
        b.ute_var, b.ts_var, b.fl_canc, b.fl_condiviso, b.ute_condiviso,
        b.ts_condiviso
    FROM tr_tit_tit a, tr_tit_tit b
    WHERE (((((((((ty.bid = a.bid_coll) AND (a.bid_coll = b.bid_base)) AND
        (a.tp_legame = '01'::bpchar)) AND (b.tp_legame = '01'::bpchar)) AND
        ((a.cd_natura_base = 'M'::bpchar) OR (a.cd_natura_base =
        'W'::bpchar))) AND (a.cd_natura_coll = 'M'::bpchar)) AND
        (b.cd_natura_coll = 'M'::bpchar)) AND (NOT (a.fl_canc =
        'S'::bpchar))) AND (NOT (b.fl_canc = 'S'::bpchar)))
    )));

--
-- Definition for view v_des_des (OID = 59314) : 
--
CREATE VIEW sbnweb.v_des_des AS
SELECT d1.did_base AS did_1, d1.did_coll AS did_2, d1.tp_legame
FROM tr_des_des d1
WHERE (d1.fl_canc <> 'S'::bpchar);

--
-- Definition for view v_grafica (OID = 59318) : 
--
CREATE VIEW sbnweb.v_grafica AS
SELECT g.cd_livello AS cd_livello_g, g.tp_materiale_gra, g.cd_supporto,
    g.cd_colore, g.cd_tecnica_dis_1, g.cd_tecnica_dis_2,
    g.cd_tecnica_dis_3, g.cd_tecnica_sta_1, g.cd_tecnica_sta_2,
    g.cd_tecnica_sta_3, g.cd_design_funz, t.bid, t.isadn, t.tp_materiale,
    t.tp_record_uni, t.cd_natura, t.cd_paese, t.cd_lingua_1, t.cd_lingua_2,
    t.cd_lingua_3, t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1,
    t.cd_genere_2, t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t,
    t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t, t.ky_cles1_ct, t.ky_cles2_ct,
    t.ky_clet1_ct, t.ky_clet2_ct, t.cd_livello, t.fl_speciale, t.isbd,
    t.indice_isbd, t.ky_editore, t.cd_agenzia, t.cd_norme_cat,
    t.nota_inf_tit, t.nota_cat_tit, t.bid_link, t.tp_link, t.ute_ins,
    t.ts_ins, t.ute_var, t.ts_var, t.ute_forza_ins, t.ute_forza_var,
    t.fl_canc, t.fl_condiviso, t.ts_condiviso, t.ute_condiviso
FROM tb_grafica g, tb_titolo t
WHERE (((t.bid = g.bid) AND (t.fl_canc <> 'S'::bpchar)) AND (g.fl_canc <>
    'S'::bpchar));

--
-- Definition for view v_luo_luo (OID = 59323) : 
--
CREATE VIEW sbnweb.v_luo_luo AS
SELECT l1.lid_base AS lid_1, l1.lid_coll AS lid_2, l1.tp_legame
FROM tr_luo_luo l1
WHERE (l1.fl_canc <> 'S'::bpchar)
UNION
SELECT l2.lid_coll AS lid_1, l2.lid_base AS lid_2, l2.tp_legame
FROM tr_luo_luo l2
WHERE (l2.fl_canc <> 'S'::bpchar);

--
-- Definition for view v_musica (OID = 59327) : 
--
CREATE VIEW sbnweb.v_musica AS
SELECT m.cd_livello AS cd_livello_m, m.ds_org_sint, m.ds_org_anal,
    m.tp_elaborazione, m.cd_stesura, m.fl_composito, m.fl_palinsesto,
    m.datazione, m.cd_presentazione, m.cd_materia, m.ds_illustrazioni,
    m.notazione_musicale, m.ds_legatura, m.ds_conservazione,
    m.tp_testo_letter, t.bid, t.isadn, t.tp_materiale, t.tp_record_uni,
    t.cd_natura, t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3,
    t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2,
    t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t,
    t.ky_clet2_t, t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct,
    t.ky_clet2_ct, t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd,
    t.ky_editore, t.cd_agenzia, t.cd_norme_cat, t.nota_cat_tit,
    t.nota_inf_tit, t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var,
    t.ts_var, t.ute_forza_ins, t.ute_forza_var, t.fl_canc, t.fl_condiviso,
    t.ts_condiviso, t.ute_condiviso
FROM tb_musica m, tb_titolo t
WHERE (((t.bid = m.bid) AND (t.fl_canc <> 'S'::bpchar)) AND (m.fl_canc <>
    'S'::bpchar));

--
-- Definition for view v_termini_termini (OID = 59332) : 
--
CREATE VIEW sbnweb.v_termini_termini AS
SELECT d1.did_base AS did_1, d1.did_coll AS did_2, d1.tipo_coll
FROM tr_termini_termini d1
WHERE (d1.fl_canc <> 'S'::bpchar);

--
-- Definition for view vl_luogo_tit (OID = 59351) : 
--
CREATE VIEW sbnweb.vl_luogo_tit AS
SELECT tr_tit_luo.bid, tr_tit_luo.tp_luogo, tr_tit_luo.nota_tit_luo,
    tb_luogo.lid, tb_luogo.tp_forma, tb_luogo.cd_livello,
    tb_luogo.ds_luogo, tb_luogo.ky_luogo, tb_luogo.ky_norm_luogo,
    tb_luogo.cd_paese, tb_luogo.nota_luogo, tb_luogo.ute_ins,
    tb_luogo.ts_ins, tb_luogo.ute_var, tb_luogo.ts_var, tb_luogo.fl_canc
FROM tr_tit_luo, tb_luogo
WHERE (((tr_tit_luo.lid = tb_luogo.lid) AND (tr_tit_luo.fl_canc <>
    'S'::bpchar)) AND (tb_luogo.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_cartografia_cla (OID = 59360) : 
--
CREATE OR REPLACE VIEW sbnweb.vl_cartografia_cla(
    cd_sistema,
    cd_edizione,
    classe,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    fl_condiviso,
    ts_condiviso,
    ute_condiviso)
AS
  SELECT tc.cd_sistema,
         tc.cd_edizione,
         tc.classe,
         c.cd_livello_c,
         c.tp_pubb_gov,
         c.cd_colore,
         c.cd_meridiano,
         c.cd_supporto_fisico,
         c.cd_tecnica,
         c.cd_forma_ripr,
         c.cd_forma_pubb,
         c.cd_altitudine,
         c.cd_tiposcala,
         c.tp_scala,
         c.scala_oriz,
         c.scala_vert,
         c.longitudine_ovest,
         c.longitudine_est,
         c.latitudine_nord,
         c.latitudine_sud,
         c.tp_immagine,
         c.cd_forma_cart,
         c.cd_piattaforma,
         c.cd_categ_satellite,
		 c.tp_proiezione,
         c.bid,
         c.isadn,
         c.tp_materiale,
         c.tp_record_uni,
         c.cd_natura,
         c.cd_paese,
         c.cd_lingua_1,
         c.cd_lingua_2,
         c.cd_lingua_3,
         c.aa_pubb_1,
         c.aa_pubb_2,
         c.tp_aa_pubb,
         c.cd_genere_1,
         c.cd_genere_2,
         c.cd_genere_3,
         c.cd_genere_4,
         c.ky_cles1_t,
         c.ky_cles2_t,
         c.ky_clet1_t,
         c.ky_clet2_t,
         c.ky_cles1_ct,
         c.ky_cles2_ct,
         c.ky_clet1_ct,
         c.ky_clet2_ct,
         c.cd_livello,
         c.fl_speciale,
         c.isbd,
         c.indice_isbd,
         c.ky_editore,
         c.cd_agenzia,
         c.cd_norme_cat,
         c.nota_inf_tit,
         c.nota_cat_tit,
         c.bid_link,
         c.tp_link,
         c.ute_ins,
         c.ts_ins,
         c.ute_var,
         c.ts_var,
         c.ute_forza_ins,
         c.ute_forza_var,
         c.fl_canc,
         c.fl_condiviso,
         c.ts_condiviso,
         c.ute_condiviso
  FROM tr_tit_cla tc,
       v_cartografia c
  WHERE tc.bid = c.bid AND
        tc.fl_canc <> 'S'  AND
        c.fl_canc <> 'S' ;
		


--
-- Definition for view ve_cartografia_cla_luo (OID = 59370) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_cla_luo(
    cd_sistema,
    cd_edizione,
    classe,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    lid,
    ky_norm_luogo)
AS
  SELECT cc.cd_sistema,
         cc.cd_edizione,
         cc.classe,
         cc.cd_livello_c,
         cc.tp_pubb_gov,
         cc.cd_colore,
         cc.cd_meridiano,
         cc.cd_supporto_fisico,
         cc.cd_tecnica,
         cc.cd_forma_ripr,
         cc.cd_forma_pubb,
         cc.cd_altitudine,
         cc.cd_tiposcala,
         cc.tp_scala,
         cc.scala_oriz,
         cc.scala_vert,
         cc.longitudine_ovest,
         cc.longitudine_est,
         cc.latitudine_nord,
         cc.latitudine_sud,
         cc.tp_immagine,
         cc.cd_forma_cart,
         cc.cd_piattaforma,
         cc.cd_categ_satellite,
         cc.tp_proiezione,
         cc.bid,
         cc.isadn,
         cc.tp_materiale,
         cc.tp_record_uni,
         cc.cd_natura,
         cc.cd_paese,
         cc.cd_lingua_1,
         cc.cd_lingua_2,
         cc.cd_lingua_3,
         cc.aa_pubb_1,
         cc.aa_pubb_2,
         cc.tp_aa_pubb,
         cc.cd_genere_1,
         cc.cd_genere_2,
         cc.cd_genere_3,
         cc.cd_genere_4,
         cc.ky_cles1_t,
         cc.ky_cles2_t,
         cc.ky_clet1_t,
         cc.ky_clet2_t,
         cc.ky_cles1_ct,
         cc.ky_cles2_ct,
         cc.ky_clet1_ct,
         cc.ky_clet2_ct,
         cc.cd_livello,
         cc.fl_speciale,
         cc.isbd,
         cc.indice_isbd,
         cc.ky_editore,
         cc.cd_agenzia,
         cc.cd_norme_cat,
         cc.nota_inf_tit,
         cc.nota_cat_tit,
         cc.bid_link,
         cc.tp_link,
         cc.ute_ins,
         cc.ts_ins,
         cc.ute_var,
         cc.ts_var,
         cc.ute_forza_ins,
         cc.ute_forza_var,
         cc.fl_canc,
         lt.lid,
         lt.ky_norm_luogo
  FROM vl_cartografia_cla cc,
       vl_luogo_tit lt
  WHERE cc.bid = lt.bid;


--
-- Definition for view vl_cartografia_luo (OID = 59375) : 
--
CREATE OR REPLACE VIEW sbnweb.vl_cartografia_luo(
    lid,
    tp_luogo,
    nota_tit_luo,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    fl_condiviso,
    ts_condiviso,
    ute_condiviso)
AS
  SELECT tl.lid,
         tl.tp_luogo,
         tl.nota_tit_luo,
         c.cd_livello_c,
         c.tp_pubb_gov,
         c.cd_colore,
         c.cd_meridiano,
         c.cd_supporto_fisico,
         c.cd_tecnica,
         c.cd_forma_ripr,
         c.cd_forma_pubb,
         c.cd_altitudine,
         c.cd_tiposcala,
         c.tp_scala,
         c.scala_oriz,
         c.scala_vert,
         c.longitudine_ovest,
         c.longitudine_est,
         c.latitudine_nord,
         c.latitudine_sud,
         c.tp_immagine,
         c.cd_forma_cart,
         c.cd_piattaforma,
         c.cd_categ_satellite,
		 c.tp_proiezione,
         c.bid,
         c.isadn,
         c.tp_materiale,
         c.tp_record_uni,
         c.cd_natura,
         c.cd_paese,
         c.cd_lingua_1,
         c.cd_lingua_2,
         c.cd_lingua_3,
         c.aa_pubb_1,
         c.aa_pubb_2,
         c.tp_aa_pubb,
         c.cd_genere_1,
         c.cd_genere_2,
         c.cd_genere_3,
         c.cd_genere_4,
         c.ky_cles1_t,
         c.ky_cles2_t,
         c.ky_clet1_t,
         c.ky_clet2_t,
         c.ky_cles1_ct,
         c.ky_cles2_ct,
         c.ky_clet1_ct,
         c.ky_clet2_ct,
         c.cd_livello,
         c.fl_speciale,
         c.isbd,
         c.indice_isbd,
         c.ky_editore,
         c.cd_agenzia,
         c.cd_norme_cat,
         c.nota_inf_tit,
         c.nota_cat_tit,
         c.bid_link,
         c.tp_link,
         c.ute_ins,
         c.ts_ins,
         c.ute_var,
         c.ts_var,
         c.ute_forza_ins,
         c.ute_forza_var,
         c.fl_canc,
         c.fl_condiviso,
         c.ts_condiviso,
         c.ute_condiviso
  FROM tr_tit_luo tl,
       v_cartografia c
  WHERE tl.bid = c.bid AND
        tl.fl_canc <> 'S'  AND
        c.fl_canc <> 'S' ;
		


--
-- Definition for view vl_cartografia_mar (OID = 59385) : 
--
CREATE OR REPLACE VIEW sbnweb.vl_cartografia_mar(
    mid,
    nota_tit_mar,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    fl_condiviso,
    ts_condiviso,
    ute_condiviso)
AS
  SELECT tm.mid,
         tm.nota_tit_mar,
         c.cd_livello_c,
         c.tp_pubb_gov,
         c.cd_colore,
         c.cd_meridiano,
         c.cd_supporto_fisico,
         c.cd_tecnica,
         c.cd_forma_ripr,
         c.cd_forma_pubb,
         c.cd_altitudine,
         c.cd_tiposcala,
         c.tp_scala,
         c.scala_oriz,
         c.scala_vert,
         c.longitudine_ovest,
         c.longitudine_est,
         c.latitudine_nord,
         c.latitudine_sud,
         c.tp_immagine,
         c.cd_forma_cart,
         c.cd_piattaforma,
         c.cd_categ_satellite,
		 c.tp_proiezione,
         c.bid,
         c.isadn,
         c.tp_materiale,
         c.tp_record_uni,
         c.cd_natura,
         c.cd_paese,
         c.cd_lingua_1,
         c.cd_lingua_2,
         c.cd_lingua_3,
         c.aa_pubb_1,
         c.aa_pubb_2,
         c.tp_aa_pubb,
         c.cd_genere_1,
         c.cd_genere_2,
         c.cd_genere_3,
         c.cd_genere_4,
         c.ky_cles1_t,
         c.ky_cles2_t,
         c.ky_clet1_t,
         c.ky_clet2_t,
         c.ky_cles1_ct,
         c.ky_cles2_ct,
         c.ky_clet1_ct,
         c.ky_clet2_ct,
         c.cd_livello,
         c.fl_speciale,
         c.isbd,
         c.indice_isbd,
         c.ky_editore,
         c.cd_agenzia,
         c.cd_norme_cat,
         c.nota_inf_tit,
         c.nota_cat_tit,
         c.bid_link,
         c.tp_link,
         c.ute_ins,
         c.ts_ins,
         c.ute_var,
         c.ts_var,
         c.ute_forza_ins,
         c.ute_forza_var,
         c.fl_canc,
         c.fl_condiviso,
         c.ts_condiviso,
         c.ute_condiviso
  FROM tr_tit_mar tm,
       v_cartografia c
  WHERE tm.bid = c.bid AND
        tm.fl_canc <> 'S'  AND
        c.fl_canc <> 'S' ;
		


--
-- Definition for view ve_cartografia_mar_luo (OID = 59395) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_mar_luo(
    mid,
    nota_tit_mar,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    lid,
    ky_norm_luogo)
AS
  SELECT cm.mid,
         cm.nota_tit_mar,
         cm.cd_livello_c,
         cm.tp_pubb_gov,
         cm.cd_colore,
         cm.cd_meridiano,
         cm.cd_supporto_fisico,
         cm.cd_tecnica,
         cm.cd_forma_ripr,
         cm.cd_forma_pubb,
         cm.cd_altitudine,
         cm.cd_tiposcala,
         cm.tp_scala,
         cm.scala_oriz,
         cm.scala_vert,
         cm.longitudine_ovest,
         cm.longitudine_est,
         cm.latitudine_nord,
         cm.latitudine_sud,
         cm.tp_immagine,
         cm.cd_forma_cart,
         cm.cd_piattaforma,
         cm.cd_categ_satellite,
		 cm.tp_proiezione,
         cm.bid,
         cm.isadn,
         cm.tp_materiale,
         cm.tp_record_uni,
         cm.cd_natura,
         cm.cd_paese,
         cm.cd_lingua_1,
         cm.cd_lingua_2,
         cm.cd_lingua_3,
         cm.aa_pubb_1,
         cm.aa_pubb_2,
         cm.tp_aa_pubb,
         cm.cd_genere_1,
         cm.cd_genere_2,
         cm.cd_genere_3,
         cm.cd_genere_4,
         cm.ky_cles1_t,
         cm.ky_cles2_t,
         cm.ky_clet1_t,
         cm.ky_clet2_t,
         cm.ky_cles1_ct,
         cm.ky_cles2_ct,
         cm.ky_clet1_ct,
         cm.ky_clet2_ct,
         cm.cd_livello,
         cm.fl_speciale,
         cm.isbd,
         cm.indice_isbd,
         cm.ky_editore,
         cm.cd_agenzia,
         cm.cd_norme_cat,
         cm.nota_inf_tit,
         cm.nota_cat_tit,
         cm.bid_link,
         cm.tp_link,
         cm.ute_ins,
         cm.ts_ins,
         cm.ute_var,
         cm.ts_var,
         cm.ute_forza_ins,
         cm.ute_forza_var,
         cm.fl_canc,
         lt.lid,
         lt.ky_norm_luogo
  FROM vl_cartografia_mar cm,
       vl_luogo_tit lt
  WHERE cm.bid = lt.bid;
  


--
-- Definition for view vl_cartografia_sog (OID = 59400) : 
--
CREATE OR REPLACE VIEW sbnweb.vl_cartografia_sog(
    cid,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    fl_condiviso,
    ts_condiviso,
    ute_condiviso)
AS
  SELECT ts.cid,
         c.cd_livello_c,
         c.tp_pubb_gov,
         c.cd_colore,
         c.cd_meridiano,
         c.cd_supporto_fisico,
         c.cd_tecnica,
         c.cd_forma_ripr,
         c.cd_forma_pubb,
         c.cd_altitudine,
         c.cd_tiposcala,
         c.tp_scala,
         c.scala_oriz,
         c.scala_vert,
         c.longitudine_ovest,
         c.longitudine_est,
         c.latitudine_nord,
         c.latitudine_sud,
         c.tp_immagine,
         c.cd_forma_cart,
         c.cd_piattaforma,
         c.cd_categ_satellite,
		 c.tp_proiezione,
         c.bid,
         c.isadn,
         c.tp_materiale,
         c.tp_record_uni,
         c.cd_natura,
         c.cd_paese,
         c.cd_lingua_1,
         c.cd_lingua_2,
         c.cd_lingua_3,
         c.aa_pubb_1,
         c.aa_pubb_2,
         c.tp_aa_pubb,
         c.cd_genere_1,
         c.cd_genere_2,
         c.cd_genere_3,
         c.cd_genere_4,
         c.ky_cles1_t,
         c.ky_cles2_t,
         c.ky_clet1_t,
         c.ky_clet2_t,
         c.ky_cles1_ct,
         c.ky_cles2_ct,
         c.ky_clet1_ct,
         c.ky_clet2_ct,
         c.cd_livello,
         c.fl_speciale,
         c.isbd,
         c.indice_isbd,
         c.ky_editore,
         c.cd_agenzia,
         c.cd_norme_cat,
         c.nota_inf_tit,
         c.nota_cat_tit,
         c.bid_link,
         c.tp_link,
         c.ute_ins,
         c.ts_ins,
         c.ute_var,
         c.ts_var,
         c.ute_forza_ins,
         c.ute_forza_var,
         c.fl_canc,
         c.fl_condiviso,
         c.ts_condiviso,
         c.ute_condiviso
  FROM tr_tit_sog_bib ts,
       v_cartografia c
  WHERE ts.bid = c.bid AND
        ts.fl_canc <> 'S'  AND
        c.fl_canc <> 'S' ;


--
-- Definition for view ve_cartografia_sog_luo (OID = 59410) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_sog_luo(
    cid,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    lid,
    ky_norm_luogo)
AS
  SELECT cs.cid,
         cs.cd_livello_c,
         cs.tp_pubb_gov,
         cs.cd_colore,
         cs.cd_meridiano,
         cs.cd_supporto_fisico,
         cs.cd_tecnica,
         cs.cd_forma_ripr,
         cs.cd_forma_pubb,
         cs.cd_altitudine,
         cs.cd_tiposcala,
         cs.tp_scala,
         cs.scala_oriz,
         cs.scala_vert,
         cs.longitudine_ovest,
         cs.longitudine_est,
         cs.latitudine_nord,
         cs.latitudine_sud,
         cs.tp_immagine,
         cs.cd_forma_cart,
         cs.cd_piattaforma,
         cs.cd_categ_satellite,
		 cs.tp_proiezione,
         cs.bid,
         cs.isadn,
         cs.tp_materiale,
         cs.tp_record_uni,
         cs.cd_natura,
         cs.cd_paese,
         cs.cd_lingua_1,
         cs.cd_lingua_2,
         cs.cd_lingua_3,
         cs.aa_pubb_1,
         cs.aa_pubb_2,
         cs.tp_aa_pubb,
         cs.cd_genere_1,
         cs.cd_genere_2,
         cs.cd_genere_3,
         cs.cd_genere_4,
         cs.ky_cles1_t,
         cs.ky_cles2_t,
         cs.ky_clet1_t,
         cs.ky_clet2_t,
         cs.ky_cles1_ct,
         cs.ky_cles2_ct,
         cs.ky_clet1_ct,
         cs.ky_clet2_ct,
         cs.cd_livello,
         cs.fl_speciale,
         cs.isbd,
         cs.indice_isbd,
         cs.ky_editore,
         cs.cd_agenzia,
         cs.cd_norme_cat,
         cs.nota_inf_tit,
         cs.nota_cat_tit,
         cs.bid_link,
         cs.tp_link,
         cs.ute_ins,
         cs.ts_ins,
         cs.ute_var,
         cs.ts_var,
         cs.ute_forza_ins,
         cs.ute_forza_var,
         cs.fl_canc,
         lt.lid,
         lt.ky_norm_luogo
  FROM vl_cartografia_sog cs,
       vl_luogo_tit lt
  WHERE cs.bid = lt.bid;
  
--
-- Definition for view vl_cartografia_the (OID = 59415) : 
--
CREATE OR REPLACE VIEW sbnweb.vl_cartografia_the(
    did,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    fl_condiviso,
    ts_condiviso,
    ute_condiviso)
AS
  SELECT ts.did,
         c.cd_livello_c,
         c.tp_pubb_gov,
         c.cd_colore,
         c.cd_meridiano,
         c.cd_supporto_fisico,
         c.cd_tecnica,
         c.cd_forma_ripr,
         c.cd_forma_pubb,
         c.cd_altitudine,
         c.cd_tiposcala,
         c.tp_scala,
         c.scala_oriz,
         c.scala_vert,
         c.longitudine_ovest,
         c.longitudine_est,
         c.latitudine_nord,
         c.latitudine_sud,
         c.tp_immagine,
         c.cd_forma_cart,
         c.cd_piattaforma,
         c.cd_categ_satellite,
		 c.tp_proiezione,
         c.bid,
         c.isadn,
         c.tp_materiale,
         c.tp_record_uni,
         c.cd_natura,
         c.cd_paese,
         c.cd_lingua_1,
         c.cd_lingua_2,
         c.cd_lingua_3,
         c.aa_pubb_1,
         c.aa_pubb_2,
         c.tp_aa_pubb,
         c.cd_genere_1,
         c.cd_genere_2,
         c.cd_genere_3,
         c.cd_genere_4,
         c.ky_cles1_t,
         c.ky_cles2_t,
         c.ky_clet1_t,
         c.ky_clet2_t,
         c.ky_cles1_ct,
         c.ky_cles2_ct,
         c.ky_clet1_ct,
         c.ky_clet2_ct,
         c.cd_livello,
         c.fl_speciale,
         c.isbd,
         c.indice_isbd,
         c.ky_editore,
         c.cd_agenzia,
         c.cd_norme_cat,
         c.nota_inf_tit,
         c.nota_cat_tit,
         c.bid_link,
         c.tp_link,
         c.ute_ins,
         c.ts_ins,
         c.ute_var,
         c.ts_var,
         c.ute_forza_ins,
         c.ute_forza_var,
         c.fl_canc,
         c.fl_condiviso,
         c.ts_condiviso,
         c.ute_condiviso
  FROM trs_termini_titoli_biblioteche ts,
       v_cartografia c
  WHERE ts.bid = c.bid AND
        ts.fl_canc <> 'S'  AND
        c.fl_canc <> 'S' ;
		


--
-- Definition for view ve_cartografia_the_luo (OID = 59425) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_the_luo(
    did,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    lid,
    ky_norm_luogo)
AS
  SELECT cs.did,
         cs.cd_livello_c,
         cs.tp_pubb_gov,
         cs.cd_colore,
         cs.cd_meridiano,
         cs.cd_supporto_fisico,
         cs.cd_tecnica,
         cs.cd_forma_ripr,
         cs.cd_forma_pubb,
         cs.cd_altitudine,
         cs.cd_tiposcala,
         cs.tp_scala,
         cs.scala_oriz,
         cs.scala_vert,
         cs.longitudine_ovest,
         cs.longitudine_est,
         cs.latitudine_nord,
         cs.latitudine_sud,
         cs.tp_immagine,
         cs.cd_forma_cart,
         cs.cd_piattaforma,
         cs.cd_categ_satellite,
		 cs.tp_proiezione,
         cs.bid,
         cs.isadn,
         cs.tp_materiale,
         cs.tp_record_uni,
         cs.cd_natura,
         cs.cd_paese,
         cs.cd_lingua_1,
         cs.cd_lingua_2,
         cs.cd_lingua_3,
         cs.aa_pubb_1,
         cs.aa_pubb_2,
         cs.tp_aa_pubb,
         cs.cd_genere_1,
         cs.cd_genere_2,
         cs.cd_genere_3,
         cs.cd_genere_4,
         cs.ky_cles1_t,
         cs.ky_cles2_t,
         cs.ky_clet1_t,
         cs.ky_clet2_t,
         cs.ky_cles1_ct,
         cs.ky_cles2_ct,
         cs.ky_clet1_ct,
         cs.ky_clet2_ct,
         cs.cd_livello,
         cs.fl_speciale,
         cs.isbd,
         cs.indice_isbd,
         cs.ky_editore,
         cs.cd_agenzia,
         cs.cd_norme_cat,
         cs.nota_inf_tit,
         cs.nota_cat_tit,
         cs.bid_link,
         cs.tp_link,
         cs.ute_ins,
         cs.ts_ins,
         cs.ute_var,
         cs.ts_var,
         cs.ute_forza_ins,
         cs.ute_forza_var,
         cs.fl_canc,
         lt.lid,
         lt.ky_norm_luogo
  FROM vl_cartografia_the cs,
       vl_luogo_tit lt
  WHERE cs.bid = lt.bid;
  


--
-- Definition for view vl_cartografia_tit_c (OID = 59430) : 
--
CREATE OR REPLACE VIEW sbnweb.vl_cartografia_tit_c(
    bid_coll,
    tp_legame,
    tp_legame_musica,
    cd_natura_base,
    cd_natura_coll,
    sequenza,
    nota_tit_tit,
    sequenza_musica,
    sici,
    fl_condiviso_legame,
    ts_condiviso_legame,
    ute_condiviso_legame,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    fl_condiviso,
    ts_condiviso,
    ute_condiviso)
AS
  SELECT tt.bid_coll,
         tt.tp_legame,
         tt.tp_legame_musica,
         tt.cd_natura_base,
         tt.cd_natura_coll,
         tt.sequenza,
         tt.nota_tit_tit,
         tt.sequenza_musica,
         tt.sici,
         tt.fl_condiviso AS fl_condiviso_legame,
         tt.ts_condiviso AS ts_condiviso_legame,
         tt.ute_condiviso AS ute_condiviso_legame,
         c.cd_livello_c,
         c.tp_pubb_gov,
         c.cd_colore,
         c.cd_meridiano,
         c.cd_supporto_fisico,
         c.cd_tecnica,
         c.cd_forma_ripr,
         c.cd_forma_pubb,
         c.cd_altitudine,
         c.cd_tiposcala,
         c.tp_scala,
         c.scala_oriz,
         c.scala_vert,
         c.longitudine_ovest,
         c.longitudine_est,
         c.latitudine_nord,
         c.latitudine_sud,
         c.tp_immagine,
         c.cd_forma_cart,
         c.cd_piattaforma,
         c.cd_categ_satellite,
		 c.tp_proiezione,
         c.bid,
         c.isadn,
         c.tp_materiale,
         c.tp_record_uni,
         c.cd_natura,
         c.cd_paese,
         c.cd_lingua_1,
         c.cd_lingua_2,
         c.cd_lingua_3,
         c.aa_pubb_1,
         c.aa_pubb_2,
         c.tp_aa_pubb,
         c.cd_genere_1,
         c.cd_genere_2,
         c.cd_genere_3,
         c.cd_genere_4,
         c.ky_cles1_t,
         c.ky_cles2_t,
         c.ky_clet1_t,
         c.ky_clet2_t,
         c.ky_cles1_ct,
         c.ky_cles2_ct,
         c.ky_clet1_ct,
         c.ky_clet2_ct,
         c.cd_livello,
         c.fl_speciale,
         c.isbd,
         c.indice_isbd,
         c.ky_editore,
         c.cd_agenzia,
         c.cd_norme_cat,
         c.nota_inf_tit,
         c.nota_cat_tit,
         c.bid_link,
         c.tp_link,
         c.ute_ins,
         c.ts_ins,
         c.ute_var,
         c.ts_var,
         c.ute_forza_ins,
         c.ute_forza_var,
         c.fl_canc,
         c.fl_condiviso,
         c.ts_condiviso,
         c.ute_condiviso
  FROM tr_tit_tit tt,
       v_cartografia c
  WHERE tt.bid_base = c.bid AND
        tt.fl_canc <> 'S'  AND
        c.fl_canc <> 'S' ;


--
-- Definition for view ve_cartografia_tit_c_luo (OID = 59440) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_tit_c_luo(
    bid_coll,
    tp_legame,
    tp_legame_musica,
    cd_natura_base,
    cd_natura_coll,
    sequenza,
    nota_tit_tit,
    sequenza_musica,
    sici,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    lid,
    ky_norm_luogo)
AS
  SELECT ct.bid_coll,
         ct.tp_legame,
         ct.tp_legame_musica,
         ct.cd_natura_base,
         ct.cd_natura_coll,
         ct.sequenza,
         ct.nota_tit_tit,
         ct.sequenza_musica,
         ct.sici,
         ct.cd_livello_c,
         ct.tp_pubb_gov,
         ct.cd_colore,
         ct.cd_meridiano,
         ct.cd_supporto_fisico,
         ct.cd_tecnica,
         ct.cd_forma_ripr,
         ct.cd_forma_pubb,
         ct.cd_altitudine,
         ct.cd_tiposcala,
         ct.tp_scala,
         ct.scala_oriz,
         ct.scala_vert,
         ct.longitudine_ovest,
         ct.longitudine_est,
         ct.latitudine_nord,
         ct.latitudine_sud,
         ct.tp_immagine,
         ct.cd_forma_cart,
         ct.cd_piattaforma,
         ct.cd_categ_satellite,
		 ct.tp_proiezione,
         ct.bid,
         ct.isadn,
         ct.tp_materiale,
         ct.tp_record_uni,
         ct.cd_natura,
         ct.cd_paese,
         ct.cd_lingua_1,
         ct.cd_lingua_2,
         ct.cd_lingua_3,
         ct.aa_pubb_1,
         ct.aa_pubb_2,
         ct.tp_aa_pubb,
         ct.cd_genere_1,
         ct.cd_genere_2,
         ct.cd_genere_3,
         ct.cd_genere_4,
         ct.ky_cles1_t,
         ct.ky_cles2_t,
         ct.ky_clet1_t,
         ct.ky_clet2_t,
         ct.ky_cles1_ct,
         ct.ky_cles2_ct,
         ct.ky_clet1_ct,
         ct.ky_clet2_ct,
         ct.cd_livello,
         ct.fl_speciale,
         ct.isbd,
         ct.indice_isbd,
         ct.ky_editore,
         ct.cd_agenzia,
         ct.cd_norme_cat,
         ct.nota_inf_tit,
         ct.nota_cat_tit,
         ct.bid_link,
         ct.tp_link,
         ct.ute_ins,
         ct.ts_ins,
         ct.ute_var,
         ct.ts_var,
         ct.ute_forza_ins,
         ct.ute_forza_var,
         ct.fl_canc,
         lt.lid,
         lt.ky_norm_luogo
  FROM vl_cartografia_tit_c ct,
       vl_luogo_tit lt
  WHERE ct.bid = lt.bid;


--
-- Definition for view vl_grafica_cla (OID = 59460) : 
--
CREATE VIEW sbnweb.vl_grafica_cla AS
SELECT tc.cd_sistema, tc.cd_edizione, tc.classe, g.cd_livello_g,
    g.tp_materiale_gra, g.cd_supporto, g.cd_colore, g.cd_tecnica_dis_1,
    g.cd_tecnica_dis_2, g.cd_tecnica_dis_3, g.cd_tecnica_sta_1,
    g.cd_tecnica_sta_2, g.cd_tecnica_sta_3, g.cd_design_funz, g.bid,
    g.isadn, g.tp_materiale, g.tp_record_uni, g.cd_natura, g.cd_paese,
    g.cd_lingua_1, g.cd_lingua_2, g.cd_lingua_3, g.aa_pubb_1, g.aa_pubb_2,
    g.tp_aa_pubb, g.cd_genere_1, g.cd_genere_2, g.cd_genere_3,
    g.cd_genere_4, g.ky_cles1_t, g.ky_cles2_t, g.ky_clet1_t, g.ky_clet2_t,
    g.ky_cles1_ct, g.ky_cles2_ct, g.ky_clet1_ct, g.ky_clet2_ct,
    g.cd_livello, g.fl_speciale, g.isbd, g.indice_isbd, g.ky_editore,
    g.cd_agenzia, g.cd_norme_cat, g.nota_inf_tit, g.nota_cat_tit,
    g.bid_link, g.tp_link, g.ute_ins, g.ts_ins, g.ute_var, g.ts_var,
    g.ute_forza_ins, g.ute_forza_var, g.fl_canc, g.fl_condiviso,
    g.ts_condiviso, g.ute_condiviso
FROM tr_tit_cla tc, v_grafica g
WHERE (((tc.bid = g.bid) AND (tc.fl_canc <> 'S'::bpchar)) AND (g.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_grafica_cla_luo (OID = 59470) : 
--
CREATE VIEW sbnweb.ve_grafica_cla_luo AS
SELECT gc.cd_sistema, gc.cd_edizione, gc.classe, gc.cd_livello_g,
    gc.tp_materiale_gra, gc.cd_supporto, gc.cd_colore, gc.cd_tecnica_dis_1,
    gc.cd_tecnica_dis_2, gc.cd_tecnica_dis_3, gc.cd_tecnica_sta_1,
    gc.cd_tecnica_sta_2, gc.cd_tecnica_sta_3, gc.cd_design_funz, gc.bid,
    gc.isadn, gc.tp_materiale, gc.tp_record_uni, gc.cd_natura, gc.cd_paese,
    gc.cd_lingua_1, gc.cd_lingua_2, gc.cd_lingua_3, gc.aa_pubb_1,
    gc.aa_pubb_2, gc.tp_aa_pubb, gc.cd_genere_1, gc.cd_genere_2,
    gc.cd_genere_3, gc.cd_genere_4, gc.ky_cles1_t, gc.ky_cles2_t,
    gc.ky_clet1_t, gc.ky_clet2_t, gc.ky_cles1_ct, gc.ky_cles2_ct,
    gc.ky_clet1_ct, gc.ky_clet2_ct, gc.cd_livello, gc.fl_speciale, gc.isbd,
    gc.indice_isbd, gc.ky_editore, gc.cd_agenzia, gc.cd_norme_cat,
    gc.nota_inf_tit, gc.nota_cat_tit, gc.bid_link, gc.tp_link, gc.ute_ins,
    gc.ts_ins, gc.ute_var, gc.ts_var, gc.ute_forza_ins, gc.ute_forza_var,
    gc.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_grafica_cla gc, vl_luogo_tit lt
WHERE (gc.bid = lt.bid);

--
-- Definition for view vl_grafica_luo (OID = 59475) : 
--
CREATE VIEW sbnweb.vl_grafica_luo AS
SELECT tl.lid, tl.tp_luogo, tl.nota_tit_luo, g.cd_livello_g,
    g.tp_materiale_gra, g.cd_supporto, g.cd_colore, g.cd_tecnica_dis_1,
    g.cd_tecnica_dis_2, g.cd_tecnica_dis_3, g.cd_tecnica_sta_1,
    g.cd_tecnica_sta_2, g.cd_tecnica_sta_3, g.cd_design_funz, g.bid,
    g.isadn, g.tp_materiale, g.tp_record_uni, g.cd_natura, g.cd_paese,
    g.cd_lingua_1, g.cd_lingua_2, g.cd_lingua_3, g.aa_pubb_1, g.aa_pubb_2,
    g.tp_aa_pubb, g.cd_genere_1, g.cd_genere_2, g.cd_genere_3,
    g.cd_genere_4, g.ky_cles1_t, g.ky_cles2_t, g.ky_clet1_t, g.ky_clet2_t,
    g.ky_cles1_ct, g.ky_cles2_ct, g.ky_clet1_ct, g.ky_clet2_ct,
    g.cd_livello, g.fl_speciale, g.isbd, g.indice_isbd, g.ky_editore,
    g.cd_agenzia, g.cd_norme_cat, g.nota_inf_tit, g.nota_cat_tit,
    g.bid_link, g.tp_link, g.ute_ins, g.ts_ins, g.ute_var, g.ts_var,
    g.ute_forza_ins, g.ute_forza_var, g.fl_canc, g.fl_condiviso,
    g.ts_condiviso, g.ute_condiviso
FROM tr_tit_luo tl, v_grafica g
WHERE (((tl.bid = g.bid) AND (tl.fl_canc <> 'S'::bpchar)) AND (g.fl_canc <>
    'S'::bpchar));

--
-- Definition for view vl_grafica_mar (OID = 59485) : 
--
CREATE VIEW sbnweb.vl_grafica_mar AS
SELECT tm.mid, tm.nota_tit_mar, g.cd_livello_g, g.tp_materiale_gra,
    g.cd_supporto, g.cd_colore, g.cd_tecnica_dis_1, g.cd_tecnica_dis_2,
    g.cd_tecnica_dis_3, g.cd_tecnica_sta_1, g.cd_tecnica_sta_2,
    g.cd_tecnica_sta_3, g.cd_design_funz, g.bid, g.isadn, g.tp_materiale,
    g.tp_record_uni, g.cd_natura, g.cd_paese, g.cd_lingua_1, g.cd_lingua_2,
    g.cd_lingua_3, g.aa_pubb_1, g.aa_pubb_2, g.tp_aa_pubb, g.cd_genere_1,
    g.cd_genere_2, g.cd_genere_3, g.cd_genere_4, g.ky_cles1_t,
    g.ky_cles2_t, g.ky_clet1_t, g.ky_clet2_t, g.ky_cles1_ct, g.ky_cles2_ct,
    g.ky_clet1_ct, g.ky_clet2_ct, g.cd_livello, g.fl_speciale, g.isbd,
    g.indice_isbd, g.ky_editore, g.cd_agenzia, g.cd_norme_cat,
    g.nota_inf_tit, g.nota_cat_tit, g.bid_link, g.tp_link, g.ute_ins,
    g.ts_ins, g.ute_var, g.ts_var, g.ute_forza_ins, g.ute_forza_var,
    g.fl_canc, g.fl_condiviso, g.ts_condiviso, g.ute_condiviso
FROM tr_tit_mar tm, v_grafica g
WHERE (((tm.bid = g.bid) AND (tm.fl_canc <> 'S'::bpchar)) AND (g.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_grafica_mar_luo (OID = 59495) : 
--
CREATE VIEW sbnweb.ve_grafica_mar_luo AS
SELECT gm.mid, gm.nota_tit_mar, gm.cd_livello_g, gm.tp_materiale_gra,
    gm.cd_supporto, gm.cd_colore, gm.cd_tecnica_dis_1, gm.cd_tecnica_dis_2,
    gm.cd_tecnica_dis_3, gm.cd_tecnica_sta_1, gm.cd_tecnica_sta_2,
    gm.cd_tecnica_sta_3, gm.cd_design_funz, gm.bid, gm.isadn,
    gm.tp_materiale, gm.tp_record_uni, gm.cd_natura, gm.cd_paese,
    gm.cd_lingua_1, gm.cd_lingua_2, gm.cd_lingua_3, gm.aa_pubb_1,
    gm.aa_pubb_2, gm.tp_aa_pubb, gm.cd_genere_1, gm.cd_genere_2,
    gm.cd_genere_3, gm.cd_genere_4, gm.ky_cles1_t, gm.ky_cles2_t,
    gm.ky_clet1_t, gm.ky_clet2_t, gm.ky_cles1_ct, gm.ky_cles2_ct,
    gm.ky_clet1_ct, gm.ky_clet2_ct, gm.cd_livello, gm.fl_speciale, gm.isbd,
    gm.indice_isbd, gm.ky_editore, gm.cd_agenzia, gm.cd_norme_cat,
    gm.nota_inf_tit, gm.nota_cat_tit, gm.bid_link, gm.tp_link, gm.ute_ins,
    gm.ts_ins, gm.ute_var, gm.ts_var, gm.ute_forza_ins, gm.ute_forza_var,
    gm.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_grafica_mar gm, vl_luogo_tit lt
WHERE (gm.bid = lt.bid);

--
-- Definition for view vl_grafica_sog (OID = 59500) : 
--
CREATE VIEW sbnweb.vl_grafica_sog AS
SELECT ts.cid, g.cd_livello_g, g.tp_materiale_gra, g.cd_supporto,
    g.cd_colore, g.cd_tecnica_dis_1, g.cd_tecnica_dis_2,
    g.cd_tecnica_dis_3, g.cd_tecnica_sta_1, g.cd_tecnica_sta_2,
    g.cd_tecnica_sta_3, g.cd_design_funz, g.bid, g.isadn, g.tp_materiale,
    g.tp_record_uni, g.cd_natura, g.cd_paese, g.cd_lingua_1, g.cd_lingua_2,
    g.cd_lingua_3, g.aa_pubb_1, g.aa_pubb_2, g.tp_aa_pubb, g.cd_genere_1,
    g.cd_genere_2, g.cd_genere_3, g.cd_genere_4, g.ky_cles1_t,
    g.ky_cles2_t, g.ky_clet1_t, g.ky_clet2_t, g.ky_cles1_ct, g.ky_cles2_ct,
    g.ky_clet1_ct, g.ky_clet2_ct, g.cd_livello, g.fl_speciale, g.isbd,
    g.indice_isbd, g.ky_editore, g.cd_agenzia, g.cd_norme_cat,
    g.nota_inf_tit, g.nota_cat_tit, g.bid_link, g.tp_link, g.ute_ins,
    g.ts_ins, g.ute_var, g.ts_var, g.ute_forza_ins, g.ute_forza_var,
    g.fl_canc, g.fl_condiviso, g.ts_condiviso, g.ute_condiviso
FROM tr_tit_sog_bib ts, v_grafica g
WHERE (((ts.bid = g.bid) AND (ts.fl_canc <> 'S'::bpchar)) AND (g.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_grafica_sog_luo (OID = 59510) : 
--
CREATE VIEW sbnweb.ve_grafica_sog_luo AS
SELECT gs.cid, gs.cd_livello_g, gs.tp_materiale_gra, gs.cd_supporto,
    gs.cd_colore, gs.cd_tecnica_dis_1, gs.cd_tecnica_dis_2,
    gs.cd_tecnica_dis_3, gs.cd_tecnica_sta_1, gs.cd_tecnica_sta_2,
    gs.cd_tecnica_sta_3, gs.cd_design_funz, gs.bid, gs.isadn,
    gs.tp_materiale, gs.tp_record_uni, gs.cd_natura, gs.cd_paese,
    gs.cd_lingua_1, gs.cd_lingua_2, gs.cd_lingua_3, gs.aa_pubb_1,
    gs.aa_pubb_2, gs.tp_aa_pubb, gs.cd_genere_1, gs.cd_genere_2,
    gs.cd_genere_3, gs.cd_genere_4, gs.ky_cles1_t, gs.ky_cles2_t,
    gs.ky_clet1_t, gs.ky_clet2_t, gs.ky_cles1_ct, gs.ky_cles2_ct,
    gs.ky_clet1_ct, gs.ky_clet2_ct, gs.cd_livello, gs.fl_speciale, gs.isbd,
    gs.indice_isbd, gs.ky_editore, gs.cd_agenzia, gs.cd_norme_cat,
    gs.nota_inf_tit, gs.nota_cat_tit, gs.bid_link, gs.tp_link, gs.ute_ins,
    gs.ts_ins, gs.ute_var, gs.ts_var, gs.ute_forza_ins, gs.ute_forza_var,
    gs.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_grafica_sog gs, vl_luogo_tit lt
WHERE (gs.bid = lt.bid);

--
-- Definition for view vl_grafica_the (OID = 59515) : 
--
CREATE VIEW sbnweb.vl_grafica_the AS
SELECT ts.did, g.cd_livello_g, g.tp_materiale_gra, g.cd_supporto,
    g.cd_colore, g.cd_tecnica_dis_1, g.cd_tecnica_dis_2,
    g.cd_tecnica_dis_3, g.cd_tecnica_sta_1, g.cd_tecnica_sta_2,
    g.cd_tecnica_sta_3, g.cd_design_funz, g.bid, g.isadn, g.tp_materiale,
    g.tp_record_uni, g.cd_natura, g.cd_paese, g.cd_lingua_1, g.cd_lingua_2,
    g.cd_lingua_3, g.aa_pubb_1, g.aa_pubb_2, g.tp_aa_pubb, g.cd_genere_1,
    g.cd_genere_2, g.cd_genere_3, g.cd_genere_4, g.ky_cles1_t,
    g.ky_cles2_t, g.ky_clet1_t, g.ky_clet2_t, g.ky_cles1_ct, g.ky_cles2_ct,
    g.ky_clet1_ct, g.ky_clet2_ct, g.cd_livello, g.fl_speciale, g.isbd,
    g.indice_isbd, g.ky_editore, g.cd_agenzia, g.cd_norme_cat,
    g.nota_inf_tit, g.nota_cat_tit, g.bid_link, g.tp_link, g.ute_ins,
    g.ts_ins, g.ute_var, g.ts_var, g.ute_forza_ins, g.ute_forza_var,
    g.fl_canc, g.fl_condiviso, g.ts_condiviso, g.ute_condiviso
FROM trs_termini_titoli_biblioteche ts, v_grafica g
WHERE (((ts.bid = g.bid) AND (ts.fl_canc <> 'S'::bpchar)) AND (g.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_grafica_the_luo (OID = 59525) : 
--
CREATE VIEW sbnweb.ve_grafica_the_luo AS
SELECT gs.did, gs.cd_livello_g, gs.tp_materiale_gra, gs.cd_supporto,
    gs.cd_colore, gs.cd_tecnica_dis_1, gs.cd_tecnica_dis_2,
    gs.cd_tecnica_dis_3, gs.cd_tecnica_sta_1, gs.cd_tecnica_sta_2,
    gs.cd_tecnica_sta_3, gs.cd_design_funz, gs.bid, gs.isadn,
    gs.tp_materiale, gs.tp_record_uni, gs.cd_natura, gs.cd_paese,
    gs.cd_lingua_1, gs.cd_lingua_2, gs.cd_lingua_3, gs.aa_pubb_1,
    gs.aa_pubb_2, gs.tp_aa_pubb, gs.cd_genere_1, gs.cd_genere_2,
    gs.cd_genere_3, gs.cd_genere_4, gs.ky_cles1_t, gs.ky_cles2_t,
    gs.ky_clet1_t, gs.ky_clet2_t, gs.ky_cles1_ct, gs.ky_cles2_ct,
    gs.ky_clet1_ct, gs.ky_clet2_ct, gs.cd_livello, gs.fl_speciale, gs.isbd,
    gs.indice_isbd, gs.ky_editore, gs.cd_agenzia, gs.cd_norme_cat,
    gs.nota_inf_tit, gs.nota_cat_tit, gs.bid_link, gs.tp_link, gs.ute_ins,
    gs.ts_ins, gs.ute_var, gs.ts_var, gs.ute_forza_ins, gs.ute_forza_var,
    gs.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_grafica_the gs, vl_luogo_tit lt
WHERE (gs.bid = lt.bid);

--
-- Definition for view vl_grafica_tit_c (OID = 59530) : 
--
CREATE VIEW sbnweb.vl_grafica_tit_c AS
SELECT tt.bid_coll, tt.tp_legame, tt.tp_legame_musica, tt.cd_natura_base,
    tt.cd_natura_coll, tt.sequenza, tt.nota_tit_tit, tt.sequenza_musica,
    tt.sici, tt.fl_condiviso AS fl_condiviso_legame, tt.ts_condiviso AS
    ts_condiviso_legame, tt.ute_condiviso AS ute_condiviso_legame,
    g.cd_livello_g, g.tp_materiale_gra, g.cd_supporto, g.cd_colore,
    g.cd_tecnica_dis_1, g.cd_tecnica_dis_2, g.cd_tecnica_dis_3,
    g.cd_tecnica_sta_1, g.cd_tecnica_sta_2, g.cd_tecnica_sta_3,
    g.cd_design_funz, g.bid, g.isadn, g.tp_materiale, g.tp_record_uni,
    g.cd_natura, g.cd_paese, g.cd_lingua_1, g.cd_lingua_2, g.cd_lingua_3,
    g.aa_pubb_1, g.aa_pubb_2, g.tp_aa_pubb, g.cd_genere_1, g.cd_genere_2,
    g.cd_genere_3, g.cd_genere_4, g.ky_cles1_t, g.ky_cles2_t, g.ky_clet1_t,
    g.ky_clet2_t, g.ky_cles1_ct, g.ky_cles2_ct, g.ky_clet1_ct,
    g.ky_clet2_ct, g.cd_livello, g.fl_speciale, g.isbd, g.indice_isbd,
    g.ky_editore, g.cd_agenzia, g.cd_norme_cat, g.nota_inf_tit,
    g.nota_cat_tit, g.bid_link, g.tp_link, g.ute_ins, g.ts_ins, g.ute_var,
    g.ts_var, g.ute_forza_ins, g.ute_forza_var, g.fl_canc, g.fl_condiviso,
    g.ts_condiviso, g.ute_condiviso
FROM tr_tit_tit tt, v_grafica g
WHERE (((tt.bid_base = g.bid) AND (tt.fl_canc <> 'S'::bpchar)) AND
    (g.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_grafica_tit_c_luo (OID = 59540) : 
--
CREATE VIEW sbnweb.ve_grafica_tit_c_luo AS
SELECT gt.bid_coll, gt.tp_legame, gt.tp_legame_musica, gt.cd_natura_base,
    gt.cd_natura_coll, gt.sequenza, gt.nota_tit_tit, gt.sequenza_musica,
    gt.sici, gt.cd_livello_g, gt.tp_materiale_gra, gt.cd_supporto,
    gt.cd_colore, gt.cd_tecnica_dis_1, gt.cd_tecnica_dis_2,
    gt.cd_tecnica_dis_3, gt.cd_tecnica_sta_1, gt.cd_tecnica_sta_2,
    gt.cd_tecnica_sta_3, gt.cd_design_funz, gt.bid, gt.isadn,
    gt.tp_materiale, gt.tp_record_uni, gt.cd_natura, gt.cd_paese,
    gt.cd_lingua_1, gt.cd_lingua_2, gt.cd_lingua_3, gt.aa_pubb_1,
    gt.aa_pubb_2, gt.tp_aa_pubb, gt.cd_genere_1, gt.cd_genere_2,
    gt.cd_genere_3, gt.cd_genere_4, gt.ky_cles1_t, gt.ky_cles2_t,
    gt.ky_clet1_t, gt.ky_clet2_t, gt.ky_cles1_ct, gt.ky_cles2_ct,
    gt.ky_clet1_ct, gt.ky_clet2_ct, gt.cd_livello, gt.fl_speciale, gt.isbd,
    gt.indice_isbd, gt.ky_editore, gt.cd_agenzia, gt.cd_norme_cat,
    gt.nota_inf_tit, gt.nota_cat_tit, gt.bid_link, gt.tp_link, gt.ute_ins,
    gt.ts_ins, gt.ute_var, gt.ts_var, gt.ute_forza_ins, gt.ute_forza_var,
    gt.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_grafica_tit_c gt, vl_luogo_tit lt
WHERE (gt.bid = lt.bid);

--
-- Definition for view vl_composizione_mus (OID = 59555) : 
--
CREATE VIEW sbnweb.vl_composizione_mus AS
SELECT tt.bid_base, tt.tp_legame, tt.tp_legame_musica, tt.cd_natura_base,
    tt.cd_natura_coll, tt.sequenza, tt.nota_tit_tit, tt.sequenza_musica,
    tt.sici, tt.fl_condiviso, tt.ts_condiviso, tt.ute_condiviso,
    m.cd_livello, m.ds_org_sint, m.ds_org_anal, m.tp_elaborazione,
    m.cd_stesura, m.fl_composito, m.fl_palinsesto, m.datazione AS
    datazione_m, m.cd_presentazione, m.cd_materia, m.ds_illustrazioni,
    m.notazione_musicale, m.ds_legatura, m.ds_conservazione,
    m.tp_testo_letter, m.ute_ins AS ute_ins_m, m.ts_ins AS ts_ins_m,
    m.ute_var AS ute_var_m, m.ts_var AS ts_var_m, m.fl_canc AS fl_canc_m,
    c.bid, c.cd_forma_1, c.cd_forma_2, c.cd_forma_3, c.numero_ordine,
    c.numero_opera, c.numero_cat_tem, c.cd_tonalita, c.datazione,
    c.aa_comp_1, c.aa_comp_2, c.ds_sezioni, c.ky_ord_ric, c.ky_est_ric,
    c.ky_app_ric, c.ky_ord_clet, c.ky_est_clet, c.ky_app_clet,
    c.ky_ord_pre, c.ky_est_pre, c.ky_app_pre, c.ky_ord_den, c.ky_est_den,
    c.ky_app_den, c.ky_ord_nor_pre, c.ky_est_nor_pre, c.ky_app_nor_pre,
    c.ute_ins, c.ts_ins, c.ute_var, c.ts_var, c.fl_canc
FROM tr_tit_tit tt, tb_musica m, tb_composizione c
WHERE (((((tt.bid_coll = c.bid) AND (m.bid = c.bid)) AND (tt.fl_canc <>
    'S'::bpchar)) AND (c.fl_canc <> 'S'::bpchar)) AND (m.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_musica_cla (OID = 59570) : 
--
CREATE VIEW sbnweb.vl_musica_cla AS
SELECT tc.cd_sistema, tc.cd_edizione, tc.classe, m.cd_livello_m,
    m.ds_org_sint, m.ds_org_anal, m.tp_elaborazione, m.cd_stesura,
    m.fl_composito, m.fl_palinsesto, m.datazione, m.cd_presentazione,
    m.cd_materia, m.ds_illustrazioni, m.notazione_musicale, m.ds_legatura,
    m.ds_conservazione, m.tp_testo_letter, m.bid, m.isadn, m.tp_materiale,
    m.tp_record_uni, m.cd_natura, m.cd_paese, m.cd_lingua_1, m.cd_lingua_2,
    m.cd_lingua_3, m.aa_pubb_1, m.aa_pubb_2, m.tp_aa_pubb, m.cd_genere_1,
    m.cd_genere_2, m.cd_genere_3, m.cd_genere_4, m.ky_cles1_t,
    m.ky_cles2_t, m.ky_clet1_t, m.ky_clet2_t, m.ky_cles1_ct, m.ky_cles2_ct,
    m.ky_clet1_ct, m.ky_clet2_ct, m.cd_livello, m.fl_speciale, m.isbd,
    m.indice_isbd, m.ky_editore, m.cd_agenzia, m.cd_norme_cat,
    m.nota_cat_tit, m.nota_inf_tit, m.bid_link, m.tp_link, m.ute_ins,
    m.ts_ins, m.ute_var, m.ts_var, m.ute_forza_ins, m.ute_forza_var,
    m.fl_canc, m.fl_condiviso, m.ts_condiviso, m.ute_condiviso
FROM tr_tit_cla tc, v_musica m
WHERE (((tc.bid = m.bid) AND (tc.fl_canc <> 'S'::bpchar)) AND (m.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_musica_cla_com (OID = 59580) : 
--
CREATE VIEW sbnweb.ve_musica_cla_com AS
SELECT mc.cd_sistema, mc.cd_edizione, mc.classe, mc.cd_livello_m,
    mc.ds_org_sint, mc.ds_org_anal, mc.tp_elaborazione, mc.cd_stesura,
    mc.fl_composito, mc.fl_palinsesto, mc.datazione, mc.cd_presentazione,
    mc.cd_materia, mc.ds_illustrazioni, mc.notazione_musicale,
    mc.ds_legatura, mc.ds_conservazione, mc.tp_testo_letter, mc.bid,
    mc.isadn, mc.tp_materiale, mc.tp_record_uni, mc.cd_natura, mc.cd_paese,
    mc.cd_lingua_1, mc.cd_lingua_2, mc.cd_lingua_3, mc.aa_pubb_1,
    mc.aa_pubb_2, mc.tp_aa_pubb, mc.cd_genere_1, mc.cd_genere_2,
    mc.cd_genere_3, mc.cd_genere_4, mc.ky_cles1_t, mc.ky_cles2_t,
    mc.ky_clet1_t, mc.ky_clet2_t, mc.ky_cles1_ct, mc.ky_cles2_ct,
    mc.ky_clet1_ct, mc.ky_clet2_ct, mc.cd_livello, mc.fl_speciale, mc.isbd,
    mc.indice_isbd, mc.ky_editore, mc.cd_agenzia, mc.cd_norme_cat,
    mc.nota_cat_tit, mc.nota_inf_tit, mc.bid_link, mc.tp_link, mc.ute_ins,
    mc.ts_ins, mc.ute_var, mc.ts_var, mc.ute_forza_ins, mc.ute_forza_var,
    mc.fl_canc, cm.bid AS bid_cm, cm.ds_org_sint AS ds_org_sint_cm,
    cm.ds_org_anal AS ds_org_anal_cm, cm.cd_forma_1, cm.cd_forma_2,
    cm.cd_forma_3, cm.numero_ordine, cm.numero_opera, cm.numero_cat_tem,
    cm.cd_tonalita, cm.datazione AS datazione_cm, cm.ky_ord_ric,
    cm.ky_est_ric, cm.ky_app_ric
FROM vl_musica_cla mc, vl_composizione_mus cm
WHERE (mc.bid = cm.bid_base);

--
-- Definition for view ve_musica_cla_luo (OID = 59585) : 
--
CREATE VIEW sbnweb.ve_musica_cla_luo AS
SELECT mc.cd_sistema, mc.cd_edizione, mc.classe, mc.cd_livello_m,
    mc.ds_org_sint, mc.ds_org_anal, mc.tp_elaborazione, mc.cd_stesura,
    mc.fl_composito, mc.fl_palinsesto, mc.datazione, mc.cd_presentazione,
    mc.cd_materia, mc.ds_illustrazioni, mc.notazione_musicale,
    mc.ds_legatura, mc.ds_conservazione, mc.tp_testo_letter, mc.bid,
    mc.isadn, mc.tp_materiale, mc.tp_record_uni, mc.cd_natura, mc.cd_paese,
    mc.cd_lingua_1, mc.cd_lingua_2, mc.cd_lingua_3, mc.aa_pubb_1,
    mc.aa_pubb_2, mc.tp_aa_pubb, mc.cd_genere_1, mc.cd_genere_2,
    mc.cd_genere_3, mc.cd_genere_4, mc.ky_cles1_t, mc.ky_cles2_t,
    mc.ky_clet1_t, mc.ky_clet2_t, mc.ky_cles1_ct, mc.ky_cles2_ct,
    mc.ky_clet1_ct, mc.ky_clet2_ct, mc.cd_livello, mc.fl_speciale, mc.isbd,
    mc.indice_isbd, mc.ky_editore, mc.cd_agenzia, mc.cd_norme_cat,
    mc.nota_cat_tit, mc.nota_inf_tit, mc.bid_link, mc.tp_link, mc.ute_ins,
    mc.ts_ins, mc.ute_var, mc.ts_var, mc.ute_forza_ins, mc.ute_forza_var,
    mc.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_musica_cla mc, vl_luogo_tit lt
WHERE (mc.bid = lt.bid);

--
-- Definition for view vl_musica_luo (OID = 59590) : 
--
CREATE VIEW sbnweb.vl_musica_luo AS
SELECT tl.lid, tl.tp_luogo, tl.nota_tit_luo, m.cd_livello_m, m.ds_org_sint,
    m.ds_org_anal, m.tp_elaborazione, m.cd_stesura, m.fl_composito,
    m.fl_palinsesto, m.datazione, m.cd_presentazione, m.cd_materia,
    m.ds_illustrazioni, m.notazione_musicale, m.ds_legatura,
    m.ds_conservazione, m.tp_testo_letter, m.bid, m.isadn, m.tp_materiale,
    m.tp_record_uni, m.cd_natura, m.cd_paese, m.cd_lingua_1, m.cd_lingua_2,
    m.cd_lingua_3, m.aa_pubb_1, m.aa_pubb_2, m.tp_aa_pubb, m.cd_genere_1,
    m.cd_genere_2, m.cd_genere_3, m.cd_genere_4, m.ky_cles1_t,
    m.ky_cles2_t, m.ky_clet1_t, m.ky_clet2_t, m.ky_cles1_ct, m.ky_cles2_ct,
    m.ky_clet1_ct, m.ky_clet2_ct, m.cd_livello, m.fl_speciale, m.isbd,
    m.indice_isbd, m.ky_editore, m.cd_agenzia, m.cd_norme_cat,
    m.nota_cat_tit, m.nota_inf_tit, m.bid_link, m.tp_link, m.ute_ins,
    m.ts_ins, m.ute_var, m.ts_var, m.ute_forza_ins, m.ute_forza_var,
    m.fl_canc, m.fl_condiviso, m.ts_condiviso, m.ute_condiviso
FROM tr_tit_luo tl, v_musica m
WHERE (((tl.bid = m.bid) AND (tl.fl_canc <> 'S'::bpchar)) AND (m.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_musica_luo_com (OID = 59600) : 
--
CREATE VIEW sbnweb.ve_musica_luo_com AS
SELECT ml.lid, ml.tp_luogo, ml.nota_tit_luo, ml.cd_livello_m,
    ml.ds_org_sint, ml.ds_org_anal, ml.tp_elaborazione, ml.cd_stesura,
    ml.fl_composito, ml.fl_palinsesto, ml.datazione, ml.cd_presentazione,
    ml.cd_materia, ml.ds_illustrazioni, ml.notazione_musicale,
    ml.ds_legatura, ml.ds_conservazione, ml.tp_testo_letter, ml.bid,
    ml.isadn, ml.tp_materiale, ml.tp_record_uni, ml.cd_natura, ml.cd_paese,
    ml.cd_lingua_1, ml.cd_lingua_2, ml.cd_lingua_3, ml.aa_pubb_1,
    ml.aa_pubb_2, ml.tp_aa_pubb, ml.cd_genere_1, ml.cd_genere_2,
    ml.cd_genere_3, ml.cd_genere_4, ml.ky_cles1_t, ml.ky_cles2_t,
    ml.ky_clet1_t, ml.ky_clet2_t, ml.ky_cles1_ct, ml.ky_cles2_ct,
    ml.ky_clet1_ct, ml.ky_clet2_ct, ml.cd_livello, ml.fl_speciale, ml.isbd,
    ml.indice_isbd, ml.ky_editore, ml.cd_agenzia, ml.cd_norme_cat,
    ml.nota_cat_tit, ml.nota_inf_tit, ml.bid_link, ml.tp_link, ml.ute_ins,
    ml.ts_ins, ml.ute_var, ml.ts_var, ml.ute_forza_ins, ml.ute_forza_var,
    ml.fl_canc, cm.bid AS bid_cm, cm.ds_org_sint AS ds_org_sint_cm,
    cm.ds_org_anal AS ds_org_anal_cm, cm.cd_forma_1, cm.cd_forma_2,
    cm.cd_forma_3, cm.numero_ordine, cm.numero_opera, cm.numero_cat_tem,
    cm.datazione AS datazione_cm, cm.ky_app_ric, cm.ky_ord_ric, cm.ky_est_ric
FROM vl_musica_luo ml, vl_composizione_mus cm
WHERE (ml.bid = cm.bid_base);

--
-- Definition for view vl_musica_mar (OID = 59605) : 
--
CREATE VIEW sbnweb.vl_musica_mar AS
SELECT tm.mid, tm.nota_tit_mar, m.cd_livello_m, m.ds_org_sint,
    m.ds_org_anal, m.tp_elaborazione, m.cd_stesura, m.fl_composito,
    m.fl_palinsesto, m.datazione, m.cd_presentazione, m.cd_materia,
    m.ds_illustrazioni, m.notazione_musicale, m.ds_legatura,
    m.ds_conservazione, m.tp_testo_letter, m.bid, m.isadn, m.tp_materiale,
    m.tp_record_uni, m.cd_natura, m.cd_paese, m.cd_lingua_1, m.cd_lingua_2,
    m.cd_lingua_3, m.aa_pubb_1, m.aa_pubb_2, m.tp_aa_pubb, m.cd_genere_1,
    m.cd_genere_2, m.cd_genere_3, m.cd_genere_4, m.ky_cles1_t,
    m.ky_cles2_t, m.ky_clet1_t, m.ky_clet2_t, m.ky_cles1_ct, m.ky_cles2_ct,
    m.ky_clet1_ct, m.ky_clet2_ct, m.cd_livello, m.fl_speciale, m.isbd,
    m.indice_isbd, m.ky_editore, m.cd_agenzia, m.cd_norme_cat,
    m.nota_cat_tit, m.nota_inf_tit, m.bid_link, m.tp_link, m.ute_ins,
    m.ts_ins, m.ute_var, m.ts_var, m.ute_forza_ins, m.ute_forza_var,
    m.fl_canc, m.fl_condiviso, m.ts_condiviso, m.ute_condiviso
FROM tr_tit_mar tm, v_musica m
WHERE (((tm.bid = m.bid) AND (tm.fl_canc <> 'S'::bpchar)) AND (m.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_musica_mar_com (OID = 59615) : 
--
CREATE VIEW sbnweb.ve_musica_mar_com AS
SELECT mm.mid, mm.nota_tit_mar, mm.cd_livello_m, mm.ds_org_sint,
    mm.ds_org_anal, mm.tp_elaborazione, mm.cd_stesura, mm.fl_composito,
    mm.fl_palinsesto, mm.datazione, mm.cd_presentazione, mm.cd_materia,
    mm.ds_illustrazioni, mm.notazione_musicale, mm.ds_legatura,
    mm.ds_conservazione, mm.tp_testo_letter, mm.bid, mm.isadn,
    mm.tp_materiale, mm.tp_record_uni, mm.cd_natura, mm.cd_paese,
    mm.cd_lingua_1, mm.cd_lingua_2, mm.cd_lingua_3, mm.aa_pubb_1,
    mm.aa_pubb_2, mm.tp_aa_pubb, mm.cd_genere_1, mm.cd_genere_2,
    mm.cd_genere_3, mm.cd_genere_4, mm.ky_cles1_t, mm.ky_cles2_t,
    mm.ky_clet1_t, mm.ky_clet2_t, mm.ky_cles1_ct, mm.ky_cles2_ct,
    mm.ky_clet1_ct, mm.ky_clet2_ct, mm.cd_livello, mm.fl_speciale, mm.isbd,
    mm.indice_isbd, mm.ky_editore, mm.cd_agenzia, mm.cd_norme_cat,
    mm.nota_cat_tit, mm.nota_inf_tit, mm.bid_link, mm.tp_link, mm.ute_ins,
    mm.ts_ins, mm.ute_var, mm.ts_var, mm.ute_forza_ins, mm.ute_forza_var,
    mm.fl_canc, cm.bid AS bid_cm, cm.ds_org_sint AS ds_org_sint_cm,
    cm.ds_org_anal AS ds_org_anal_cm, cm.cd_forma_1, cm.cd_forma_2,
    cm.cd_forma_3, cm.numero_ordine, cm.numero_opera, cm.numero_cat_tem,
    cm.cd_tonalita, cm.datazione AS datazione_cm, cm.ky_ord_ric,
    cm.ky_est_ric, cm.ky_app_ric
FROM vl_musica_mar mm, vl_composizione_mus cm
WHERE (mm.bid = cm.bid_base);

--
-- Definition for view ve_musica_mar_luo (OID = 59620) : 
--
CREATE VIEW sbnweb.ve_musica_mar_luo AS
SELECT mm.mid, mm.nota_tit_mar, mm.cd_livello_m, mm.ds_org_sint,
    mm.ds_org_anal, mm.tp_elaborazione, mm.cd_stesura, mm.fl_composito,
    mm.fl_palinsesto, mm.datazione, mm.cd_presentazione, mm.cd_materia,
    mm.ds_illustrazioni, mm.notazione_musicale, mm.ds_legatura,
    mm.ds_conservazione, mm.tp_testo_letter, mm.bid, mm.isadn,
    mm.tp_materiale, mm.tp_record_uni, mm.cd_natura, mm.cd_paese,
    mm.cd_lingua_1, mm.cd_lingua_2, mm.cd_lingua_3, mm.aa_pubb_1,
    mm.aa_pubb_2, mm.tp_aa_pubb, mm.cd_genere_1, mm.cd_genere_2,
    mm.cd_genere_3, mm.cd_genere_4, mm.ky_cles1_t, mm.ky_cles2_t,
    mm.ky_clet1_t, mm.ky_clet2_t, mm.ky_cles1_ct, mm.ky_cles2_ct,
    mm.ky_clet1_ct, mm.ky_clet2_ct, mm.cd_livello, mm.fl_speciale, mm.isbd,
    mm.indice_isbd, mm.ky_editore, mm.cd_agenzia, mm.cd_norme_cat,
    mm.nota_cat_tit, mm.nota_inf_tit, mm.bid_link, mm.tp_link, mm.ute_ins,
    mm.ts_ins, mm.ute_var, mm.ts_var, mm.ute_forza_ins, mm.ute_forza_var,
    mm.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_musica_mar mm, vl_luogo_tit lt
WHERE (mm.bid = lt.bid);

--
-- Definition for view vl_musica_tit_c (OID = 59625) : 
--
CREATE VIEW sbnweb.vl_musica_tit_c AS
SELECT tt.bid_coll, tt.tp_legame, tt.tp_legame_musica, tt.cd_natura_base,
    tt.cd_natura_coll, tt.sequenza, tt.nota_tit_tit, tt.sequenza_musica,
    tt.sici, tt.fl_condiviso AS fl_condiviso_legame, tt.ts_condiviso AS
    ts_condiviso_legame, tt.ute_condiviso AS ute_condiviso_legame,
    m.cd_livello_m, m.ds_org_sint, m.ds_org_anal, m.tp_elaborazione,
    m.cd_stesura, m.fl_composito, m.fl_palinsesto, m.datazione,
    m.cd_presentazione, m.cd_materia, m.ds_illustrazioni,
    m.notazione_musicale, m.ds_legatura, m.ds_conservazione,
    m.tp_testo_letter, m.bid, m.isadn, m.tp_materiale, m.tp_record_uni,
    m.cd_natura, m.cd_paese, m.cd_lingua_1, m.cd_lingua_2, m.cd_lingua_3,
    m.aa_pubb_1, m.aa_pubb_2, m.tp_aa_pubb, m.cd_genere_1, m.cd_genere_2,
    m.cd_genere_3, m.cd_genere_4, m.ky_cles1_t, m.ky_cles2_t, m.ky_clet1_t,
    m.ky_clet2_t, m.ky_cles1_ct, m.ky_cles2_ct, m.ky_clet1_ct,
    m.ky_clet2_ct, m.cd_livello, m.fl_speciale, m.isbd, m.indice_isbd,
    m.ky_editore, m.cd_agenzia, m.cd_norme_cat, m.nota_cat_tit,
    m.nota_inf_tit, m.bid_link, m.tp_link, m.ute_ins, m.ts_ins, m.ute_var,
    m.ts_var, m.ute_forza_ins, m.ute_forza_var, m.fl_canc, m.fl_condiviso,
    m.ts_condiviso, m.ute_condiviso
FROM tr_tit_tit tt, v_musica m
WHERE (((tt.bid_base = m.bid) AND (tt.fl_canc <> 'S'::bpchar)) AND
    (m.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_musica_tit_c_com (OID = 59635) : 
--
CREATE VIEW sbnweb.ve_musica_tit_c_com AS
SELECT mt.bid_coll, mt.tp_legame, mt.tp_legame_musica, mt.cd_natura_base,
    mt.cd_natura_coll, mt.sequenza, mt.nota_tit_tit, mt.sequenza_musica,
    mt.sici, mt.cd_livello_m, mt.ds_org_sint, mt.ds_org_anal,
    mt.tp_elaborazione, mt.cd_stesura, mt.fl_composito, mt.fl_palinsesto,
    mt.datazione, mt.cd_presentazione, mt.cd_materia, mt.ds_illustrazioni,
    mt.notazione_musicale, mt.ds_legatura, mt.ds_conservazione,
    mt.tp_testo_letter, mt.bid, mt.isadn, mt.tp_materiale,
    mt.tp_record_uni, mt.cd_natura, mt.cd_paese, mt.cd_lingua_1,
    mt.cd_lingua_2, mt.cd_lingua_3, mt.aa_pubb_1, mt.aa_pubb_2,
    mt.tp_aa_pubb, mt.cd_genere_1, mt.cd_genere_2, mt.cd_genere_3,
    mt.cd_genere_4, mt.ky_cles1_t, mt.ky_cles2_t, mt.ky_clet1_t,
    mt.ky_clet2_t, mt.ky_cles1_ct, mt.ky_cles2_ct, mt.ky_clet1_ct,
    mt.ky_clet2_ct, mt.cd_livello, mt.fl_speciale, mt.isbd, mt.indice_isbd,
    mt.ky_editore, mt.cd_agenzia, mt.cd_norme_cat, mt.nota_cat_tit,
    mt.nota_inf_tit, mt.bid_link, mt.tp_link, mt.ute_ins, mt.ts_ins,
    mt.ute_var, mt.ts_var, mt.ute_forza_ins, mt.ute_forza_var, mt.fl_canc,
    cm.bid AS bid_cm, cm.ds_org_sint AS ds_org_sint_cm, cm.ds_org_anal AS
    ds_org_anal_cm, cm.cd_forma_1, cm.cd_forma_2, cm.cd_forma_3,
    cm.numero_ordine, cm.numero_opera, cm.numero_cat_tem, cm.cd_tonalita,
    cm.datazione AS datazione_cm, cm.ky_ord_ric, cm.ky_est_ric, cm.ky_app_ric
FROM vl_musica_tit_c mt, vl_composizione_mus cm
WHERE (mt.bid = cm.bid_base);

--
-- Definition for view ve_musica_tit_c_luo (OID = 59640) : 
--
CREATE VIEW sbnweb.ve_musica_tit_c_luo AS
SELECT mt.bid_coll, mt.tp_legame, mt.tp_legame_musica, mt.cd_natura_base,
    mt.cd_natura_coll, mt.sequenza, mt.nota_tit_tit, mt.sequenza_musica,
    mt.sici, mt.cd_livello_m, mt.ds_org_sint, mt.ds_org_anal,
    mt.tp_elaborazione, mt.cd_stesura, mt.fl_composito, mt.fl_palinsesto,
    mt.datazione, mt.cd_presentazione, mt.cd_materia, mt.ds_illustrazioni,
    mt.notazione_musicale, mt.ds_legatura, mt.ds_conservazione,
    mt.tp_testo_letter, mt.bid, mt.isadn, mt.tp_materiale,
    mt.tp_record_uni, mt.cd_natura, mt.cd_paese, mt.cd_lingua_1,
    mt.cd_lingua_2, mt.cd_lingua_3, mt.aa_pubb_1, mt.aa_pubb_2,
    mt.tp_aa_pubb, mt.cd_genere_1, mt.cd_genere_2, mt.cd_genere_3,
    mt.cd_genere_4, mt.ky_cles1_t, mt.ky_cles2_t, mt.ky_clet1_t,
    mt.ky_clet2_t, mt.ky_cles1_ct, mt.ky_cles2_ct, mt.ky_clet1_ct,
    mt.ky_clet2_ct, mt.cd_livello, mt.fl_speciale, mt.isbd, mt.indice_isbd,
    mt.ky_editore, mt.cd_agenzia, mt.cd_norme_cat, mt.nota_cat_tit,
    mt.nota_inf_tit, mt.bid_link, mt.tp_link, mt.ute_ins, mt.ts_ins,
    mt.ute_var, mt.ts_var, mt.ute_forza_ins, mt.ute_forza_var, mt.fl_canc,
    lt.lid, lt.ky_norm_luogo
FROM vl_musica_tit_c mt, vl_luogo_tit lt
WHERE (mt.bid = lt.bid);

--
-- Definition for view vl_marca_tit (OID = 59660) : 
--
CREATE VIEW sbnweb.vl_marca_tit AS
SELECT tr_tit_mar.bid, tr_tit_mar.nota_tit_mar, tb_marca.mid,
    tb_marca.cd_livello, tb_marca.fl_speciale, tb_marca.ds_marca,
    tb_marca.tidx_vector, tb_marca.nota_marca, tb_marca.ds_motto,
    tb_marca.ute_ins, tb_marca.ts_ins, tb_marca.ute_var, tb_marca.ts_var,
    tb_marca.fl_canc, tb_marca.fl_condiviso, tb_marca.ts_condiviso,
    tb_marca.ute_condiviso
FROM tr_tit_mar, tb_marca
WHERE (((tr_tit_mar.mid = tb_marca.mid) AND (tr_tit_mar.fl_canc <>
    'S'::bpchar)) AND (tb_marca.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_titolo_cla (OID = 59679) : 
--
CREATE VIEW sbnweb.vl_titolo_cla AS
SELECT tr_tit_cla.cd_sistema, tr_tit_cla.cd_edizione, tr_tit_cla.classe,
    tb_titolo.bid, tb_titolo.isadn, tb_titolo.tp_materiale,
    tb_titolo.tp_record_uni, tb_titolo.cd_natura, tb_titolo.cd_paese,
    tb_titolo.cd_lingua_1, tb_titolo.cd_lingua_2, tb_titolo.cd_lingua_3,
    tb_titolo.aa_pubb_1, tb_titolo.aa_pubb_2, tb_titolo.tp_aa_pubb,
    tb_titolo.cd_genere_1, tb_titolo.cd_genere_2, tb_titolo.cd_genere_3,
    tb_titolo.cd_genere_4, tb_titolo.ky_cles1_t, tb_titolo.ky_cles2_t,
    tb_titolo.ky_clet1_t, tb_titolo.ky_clet2_t, tb_titolo.ky_cles1_ct,
    tb_titolo.ky_cles2_ct, tb_titolo.ky_clet1_ct, tb_titolo.ky_clet2_ct,
    tb_titolo.cd_livello, tb_titolo.fl_speciale, tb_titolo.isbd,
    tb_titolo.indice_isbd, tb_titolo.ky_editore, tb_titolo.cd_agenzia,
    tb_titolo.cd_norme_cat, tb_titolo.nota_cat_tit, tb_titolo.nota_inf_tit,
    tb_titolo.bid_link, tb_titolo.tp_link, tb_titolo.ute_ins,
    tb_titolo.ts_ins, tb_titolo.ute_var, tb_titolo.ts_var,
    tb_titolo.ute_forza_ins, tb_titolo.ute_forza_var, tb_titolo.fl_canc,
    tb_titolo.fl_condiviso, tb_titolo.ts_condiviso, tb_titolo.ute_condiviso
FROM tr_tit_cla, tb_titolo
WHERE (((tr_tit_cla.bid = tb_titolo.bid) AND (tr_tit_cla.fl_canc <>
    'S'::bpchar)) AND (tb_titolo.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_titolo_cla_luo (OID = 59689) : 
--
CREATE VIEW sbnweb.ve_titolo_cla_luo AS
SELECT tc.cd_sistema, tc.cd_edizione, tc.classe, tc.bid, tc.isadn,
    tc.tp_materiale, tc.tp_record_uni, tc.cd_natura, tc.cd_paese,
    tc.cd_lingua_1, tc.cd_lingua_2, tc.cd_lingua_3, tc.aa_pubb_1,
    tc.aa_pubb_2, tc.tp_aa_pubb, tc.cd_genere_1, tc.cd_genere_2,
    tc.cd_genere_3, tc.cd_genere_4, tc.ky_cles1_t, tc.ky_cles2_t,
    tc.ky_clet1_t, tc.ky_clet2_t, tc.ky_cles1_ct, tc.ky_cles2_ct,
    tc.ky_clet1_ct, tc.ky_clet2_ct, tc.cd_livello, tc.fl_speciale, tc.isbd,
    tc.indice_isbd, tc.ky_editore, tc.cd_agenzia, tc.cd_norme_cat,
    tc.nota_cat_tit, tc.nota_inf_tit, tc.bid_link, tc.tp_link, tc.ute_ins,
    tc.ts_ins, tc.ute_var, tc.ts_var, tc.ute_forza_ins, tc.ute_forza_var,
    tc.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_titolo_cla tc, vl_luogo_tit lt
WHERE (tc.bid = lt.bid);

--
-- Definition for view ve_titolo_cla_mar (OID = 59694) : 
--
CREATE VIEW sbnweb.ve_titolo_cla_mar AS
SELECT tc.cd_sistema, tc.cd_edizione, tc.classe, tc.bid, tc.isadn,
    tc.tp_materiale, tc.tp_record_uni, tc.cd_natura, tc.cd_paese,
    tc.cd_lingua_1, tc.cd_lingua_2, tc.cd_lingua_3, tc.aa_pubb_1,
    tc.aa_pubb_2, tc.tp_aa_pubb, tc.cd_genere_1, tc.cd_genere_2,
    tc.cd_genere_3, tc.cd_genere_4, tc.ky_cles1_t, tc.ky_cles2_t,
    tc.ky_clet1_t, tc.ky_clet2_t, tc.ky_cles1_ct, tc.ky_cles2_ct,
    tc.ky_clet1_ct, tc.ky_clet2_ct, tc.cd_livello, tc.fl_speciale, tc.isbd,
    tc.indice_isbd, tc.ky_editore, tc.cd_agenzia, tc.cd_norme_cat,
    tc.nota_cat_tit, tc.nota_inf_tit, tc.bid_link, tc.tp_link, tc.ute_ins,
    tc.ts_ins, tc.ute_var, tc.ts_var, tc.ute_forza_ins, tc.ute_forza_var,
    tc.fl_canc, mt.mid, mt.ds_marca, mt.tidx_vector
FROM vl_titolo_cla tc, vl_marca_tit mt
WHERE (tc.bid = mt.bid);

--
-- Definition for view vl_titolo_luo (OID = 59699) : 
--
CREATE VIEW sbnweb.vl_titolo_luo AS
SELECT tr_tit_luo.lid, tr_tit_luo.tp_luogo, tr_tit_luo.nota_tit_luo,
    tb_titolo.bid, tb_titolo.isadn, tb_titolo.tp_materiale,
    tb_titolo.tp_record_uni, tb_titolo.cd_natura, tb_titolo.cd_paese,
    tb_titolo.cd_lingua_1, tb_titolo.cd_lingua_2, tb_titolo.cd_lingua_3,
    tb_titolo.aa_pubb_1, tb_titolo.aa_pubb_2, tb_titolo.tp_aa_pubb,
    tb_titolo.cd_genere_1, tb_titolo.cd_genere_2, tb_titolo.cd_genere_3,
    tb_titolo.cd_genere_4, tb_titolo.ky_cles1_t, tb_titolo.ky_cles2_t,
    tb_titolo.ky_clet1_t, tb_titolo.ky_clet2_t, tb_titolo.ky_cles1_ct,
    tb_titolo.ky_cles2_ct, tb_titolo.ky_clet1_ct, tb_titolo.ky_clet2_ct,
    tb_titolo.cd_livello, tb_titolo.fl_speciale, tb_titolo.isbd,
    tb_titolo.indice_isbd, tb_titolo.ky_editore, tb_titolo.cd_agenzia,
    tb_titolo.cd_norme_cat, tb_titolo.nota_cat_tit, tb_titolo.nota_inf_tit,
    tb_titolo.bid_link, tb_titolo.tp_link, tb_titolo.ute_ins,
    tb_titolo.ts_ins, tb_titolo.ute_var, tb_titolo.ts_var,
    tb_titolo.ute_forza_ins, tb_titolo.ute_forza_var, tb_titolo.fl_canc,
    tb_titolo.fl_condiviso, tb_titolo.ts_condiviso, tb_titolo.ute_condiviso
FROM tr_tit_luo, tb_titolo
WHERE (((tr_tit_luo.bid = tb_titolo.bid) AND (tr_tit_luo.fl_canc <>
    'S'::bpchar)) AND (tb_titolo.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_titolo_luo_mar (OID = 59709) : 
--
CREATE VIEW sbnweb.ve_titolo_luo_mar AS
SELECT tl.lid, tl.tp_luogo, tl.nota_tit_luo, tl.bid, tl.isadn,
    tl.tp_materiale, tl.tp_record_uni, tl.cd_natura, tl.cd_paese,
    tl.cd_lingua_1, tl.cd_lingua_2, tl.cd_lingua_3, tl.aa_pubb_1,
    tl.aa_pubb_2, tl.tp_aa_pubb, tl.cd_genere_1, tl.cd_genere_2,
    tl.cd_genere_3, tl.cd_genere_4, tl.ky_cles1_t, tl.ky_cles2_t,
    tl.ky_clet1_t, tl.ky_clet2_t, tl.ky_cles1_ct, tl.ky_cles2_ct,
    tl.ky_clet1_ct, tl.ky_clet2_ct, tl.cd_livello, tl.fl_speciale, tl.isbd,
    tl.indice_isbd, tl.ky_editore, tl.cd_agenzia, tl.cd_norme_cat,
    tl.nota_cat_tit, tl.nota_inf_tit, tl.bid_link, tl.tp_link, tl.ute_ins,
    tl.ts_ins, tl.ute_var, tl.ts_var, tl.ute_forza_ins, tl.ute_forza_var,
    tl.fl_canc, mt.mid, mt.ds_marca, mt.tidx_vector
FROM vl_titolo_luo tl, vl_marca_tit mt
WHERE (tl.bid = mt.bid);

--
-- Definition for view vl_titolo_mar (OID = 59714) : 
--
CREATE VIEW sbnweb.vl_titolo_mar AS
SELECT tr_tit_mar.mid, tr_tit_mar.nota_tit_mar, tb_titolo.bid,
    tb_titolo.isadn, tb_titolo.tp_materiale, tb_titolo.tp_record_uni,
    tb_titolo.cd_natura, tb_titolo.cd_paese, tb_titolo.cd_lingua_1,
    tb_titolo.cd_lingua_2, tb_titolo.cd_lingua_3, tb_titolo.aa_pubb_1,
    tb_titolo.aa_pubb_2, tb_titolo.tp_aa_pubb, tb_titolo.cd_genere_1,
    tb_titolo.cd_genere_2, tb_titolo.cd_genere_3, tb_titolo.cd_genere_4,
    tb_titolo.ky_cles1_t, tb_titolo.ky_cles2_t, tb_titolo.ky_clet1_t,
    tb_titolo.ky_clet2_t, tb_titolo.ky_cles1_ct, tb_titolo.ky_cles2_ct,
    tb_titolo.ky_clet1_ct, tb_titolo.ky_clet2_ct, tb_titolo.cd_livello,
    tb_titolo.fl_speciale, tb_titolo.isbd, tb_titolo.indice_isbd,
    tb_titolo.ky_editore, tb_titolo.cd_agenzia, tb_titolo.cd_norme_cat,
    tb_titolo.nota_cat_tit, tb_titolo.nota_inf_tit, tb_titolo.bid_link,
    tb_titolo.tp_link, tb_titolo.ute_ins, tb_titolo.ts_ins,
    tb_titolo.ute_var, tb_titolo.ts_var, tb_titolo.ute_forza_ins,
    tb_titolo.ute_forza_var, tb_titolo.fl_canc, tb_titolo.fl_condiviso,
    tb_titolo.ts_condiviso, tb_titolo.ute_condiviso
FROM tr_tit_mar, tb_titolo
WHERE (((tr_tit_mar.bid = tb_titolo.bid) AND (tr_tit_mar.fl_canc <>
    'S'::bpchar)) AND (tb_titolo.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_titolo_mar_luo (OID = 59724) : 
--
CREATE VIEW sbnweb.ve_titolo_mar_luo AS
SELECT tm.mid, tm.nota_tit_mar, tm.bid, tm.isadn, tm.tp_materiale,
    tm.tp_record_uni, tm.cd_natura, tm.cd_paese, tm.cd_lingua_1,
    tm.cd_lingua_2, tm.cd_lingua_3, tm.aa_pubb_1, tm.aa_pubb_2,
    tm.tp_aa_pubb, tm.cd_genere_1, tm.cd_genere_2, tm.cd_genere_3,
    tm.cd_genere_4, tm.ky_cles1_t, tm.ky_cles2_t, tm.ky_clet1_t,
    tm.ky_clet2_t, tm.ky_cles1_ct, tm.ky_cles2_ct, tm.ky_clet1_ct,
    tm.ky_clet2_ct, tm.cd_livello, tm.fl_speciale, tm.isbd, tm.indice_isbd,
    tm.ky_editore, tm.cd_agenzia, tm.cd_norme_cat, tm.nota_cat_tit,
    tm.nota_inf_tit, tm.bid_link, tm.tp_link, tm.ute_ins, tm.ts_ins,
    tm.ute_var, tm.ts_var, tm.ute_forza_ins, tm.ute_forza_var, tm.fl_canc,
    lt.lid, lt.ky_norm_luogo
FROM vl_titolo_mar tm, vl_luogo_tit lt
WHERE (tm.bid = lt.bid);

--
-- Definition for view vl_titolo_sog (OID = 59729) : 
--
CREATE VIEW sbnweb.vl_titolo_sog AS
SELECT tr_tit_sog_bib.cid, tr_tit_sog_bib.cd_polo,
    tr_tit_sog_bib.cd_biblioteca, tb_titolo.bid, tb_titolo.isadn,
    tb_titolo.tp_materiale, tb_titolo.tp_record_uni, tb_titolo.cd_natura,
    tb_titolo.cd_paese, tb_titolo.cd_lingua_1, tb_titolo.cd_lingua_2,
    tb_titolo.cd_lingua_3, tb_titolo.aa_pubb_1, tb_titolo.aa_pubb_2,
    tb_titolo.tp_aa_pubb, tb_titolo.cd_genere_1, tb_titolo.cd_genere_2,
    tb_titolo.cd_genere_3, tb_titolo.cd_genere_4, tb_titolo.ky_cles1_t,
    tb_titolo.ky_cles2_t, tb_titolo.ky_clet1_t, tb_titolo.ky_clet2_t,
    tb_titolo.ky_cles1_ct, tb_titolo.ky_cles2_ct, tb_titolo.ky_clet1_ct,
    tb_titolo.ky_clet2_ct, tb_titolo.cd_livello, tb_titolo.fl_speciale,
    tb_titolo.isbd, tb_titolo.indice_isbd, tb_titolo.ky_editore,
    tb_titolo.cd_agenzia, tb_titolo.cd_norme_cat, tb_titolo.nota_cat_tit,
    tb_titolo.nota_inf_tit, tb_titolo.bid_link, tb_titolo.tp_link,
    tb_titolo.ute_ins, tb_titolo.ts_ins, tb_titolo.ute_var,
    tb_titolo.ts_var, tb_titolo.ute_forza_ins, tb_titolo.ute_forza_var,
    tb_titolo.fl_canc, tb_titolo.fl_condiviso, tb_titolo.ts_condiviso,
    tb_titolo.ute_condiviso
FROM tr_tit_sog_bib, tb_titolo
WHERE (((tr_tit_sog_bib.bid = tb_titolo.bid) AND (tr_tit_sog_bib.fl_canc <>
    'S'::bpchar)) AND (tb_titolo.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_titolo_sog_luo (OID = 59739) : 
--
CREATE VIEW sbnweb.ve_titolo_sog_luo AS
SELECT ts.cid, ts.bid, ts.isadn, ts.tp_materiale, ts.tp_record_uni,
    ts.cd_natura, ts.cd_paese, ts.cd_lingua_1, ts.cd_lingua_2,
    ts.cd_lingua_3, ts.aa_pubb_1, ts.aa_pubb_2, ts.tp_aa_pubb,
    ts.cd_genere_1, ts.cd_genere_2, ts.cd_genere_3, ts.cd_genere_4,
    ts.ky_cles1_t, ts.ky_cles2_t, ts.ky_clet1_t, ts.ky_clet2_t,
    ts.ky_cles1_ct, ts.ky_cles2_ct, ts.ky_clet1_ct, ts.ky_clet2_ct,
    ts.cd_livello, ts.fl_speciale, ts.isbd, ts.indice_isbd, ts.ky_editore,
    ts.cd_agenzia, ts.cd_norme_cat, ts.nota_cat_tit, ts.nota_inf_tit,
    ts.bid_link, ts.tp_link, ts.ute_ins, ts.ts_ins, ts.ute_var, ts.ts_var,
    ts.ute_forza_ins, ts.ute_forza_var, ts.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_titolo_sog ts, vl_luogo_tit lt
WHERE (ts.bid = lt.bid);

--
-- Definition for view ve_titolo_sog_mar (OID = 59744) : 
--
CREATE VIEW sbnweb.ve_titolo_sog_mar AS
SELECT ts.cid, ts.bid, ts.isadn, ts.tp_materiale, ts.tp_record_uni,
    ts.cd_natura, ts.cd_paese, ts.cd_lingua_1, ts.cd_lingua_2,
    ts.cd_lingua_3, ts.aa_pubb_1, ts.aa_pubb_2, ts.tp_aa_pubb,
    ts.cd_genere_1, ts.cd_genere_2, ts.cd_genere_3, ts.cd_genere_4,
    ts.ky_cles1_t, ts.ky_cles2_t, ts.ky_clet1_t, ts.ky_clet2_t,
    ts.ky_cles1_ct, ts.ky_cles2_ct, ts.ky_clet1_ct, ts.ky_clet2_ct,
    ts.cd_livello, ts.fl_speciale, ts.isbd, ts.indice_isbd, ts.ky_editore,
    ts.cd_agenzia, ts.cd_norme_cat, ts.nota_cat_tit, ts.nota_inf_tit,
    ts.bid_link, ts.tp_link, ts.ute_ins, ts.ts_ins, ts.ute_var, ts.ts_var,
    ts.ute_forza_ins, ts.ute_forza_var, ts.fl_canc, mt.mid, mt.ds_marca,
    mt.tidx_vector
FROM vl_titolo_sog ts, vl_marca_tit mt
WHERE (ts.bid = mt.bid);

--
-- Definition for view vl_titolo_the (OID = 59749) : 
--
CREATE VIEW sbnweb.vl_titolo_the AS
SELECT trs_termini_titoli_biblioteche.did,
    trs_termini_titoli_biblioteche.cd_polo,
    trs_termini_titoli_biblioteche.cd_biblioteca, tb_titolo.bid,
    tb_titolo.isadn, tb_titolo.tp_materiale, tb_titolo.tp_record_uni,
    tb_titolo.cd_natura, tb_titolo.cd_paese, tb_titolo.cd_lingua_1,
    tb_titolo.cd_lingua_2, tb_titolo.cd_lingua_3, tb_titolo.aa_pubb_1,
    tb_titolo.aa_pubb_2, tb_titolo.tp_aa_pubb, tb_titolo.cd_genere_1,
    tb_titolo.cd_genere_2, tb_titolo.cd_genere_3, tb_titolo.cd_genere_4,
    tb_titolo.ky_cles1_t, tb_titolo.ky_cles2_t, tb_titolo.ky_clet1_t,
    tb_titolo.ky_clet2_t, tb_titolo.ky_cles1_ct, tb_titolo.ky_cles2_ct,
    tb_titolo.ky_clet1_ct, tb_titolo.ky_clet2_ct, tb_titolo.cd_livello,
    tb_titolo.fl_speciale, tb_titolo.isbd, tb_titolo.indice_isbd,
    tb_titolo.ky_editore, tb_titolo.cd_agenzia, tb_titolo.cd_norme_cat,
    tb_titolo.nota_cat_tit, tb_titolo.nota_inf_tit, tb_titolo.bid_link,
    tb_titolo.tp_link, tb_titolo.ute_ins, tb_titolo.ts_ins,
    tb_titolo.ute_var, tb_titolo.ts_var, tb_titolo.ute_forza_ins,
    tb_titolo.ute_forza_var, tb_titolo.fl_canc, tb_titolo.fl_condiviso,
    tb_titolo.ts_condiviso, tb_titolo.ute_condiviso
FROM trs_termini_titoli_biblioteche, tb_titolo
WHERE (((trs_termini_titoli_biblioteche.bid = tb_titolo.bid) AND
    (trs_termini_titoli_biblioteche.fl_canc <> 'S'::bpchar)) AND
    (tb_titolo.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_titolo_the_luo (OID = 59759) : 
--
CREATE VIEW sbnweb.ve_titolo_the_luo AS
SELECT ts.did, ts.bid, ts.isadn, ts.tp_materiale, ts.tp_record_uni,
    ts.cd_natura, ts.cd_paese, ts.cd_lingua_1, ts.cd_lingua_2,
    ts.cd_lingua_3, ts.aa_pubb_1, ts.aa_pubb_2, ts.tp_aa_pubb,
    ts.cd_genere_1, ts.cd_genere_2, ts.cd_genere_3, ts.cd_genere_4,
    ts.ky_cles1_t, ts.ky_cles2_t, ts.ky_clet1_t, ts.ky_clet2_t,
    ts.ky_cles1_ct, ts.ky_cles2_ct, ts.ky_clet1_ct, ts.ky_clet2_ct,
    ts.cd_livello, ts.fl_speciale, ts.isbd, ts.indice_isbd, ts.ky_editore,
    ts.cd_agenzia, ts.cd_norme_cat, ts.nota_cat_tit, ts.nota_inf_tit,
    ts.bid_link, ts.tp_link, ts.ute_ins, ts.ts_ins, ts.ute_var, ts.ts_var,
    ts.ute_forza_ins, ts.ute_forza_var, ts.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_titolo_the ts, vl_luogo_tit lt
WHERE (ts.bid = lt.bid);

--
-- Definition for view ve_titolo_the_mar (OID = 59764) : 
--
CREATE VIEW sbnweb.ve_titolo_the_mar AS
SELECT ts.did, ts.bid, ts.isadn, ts.tp_materiale, ts.tp_record_uni,
    ts.cd_natura, ts.cd_paese, ts.cd_lingua_1, ts.cd_lingua_2,
    ts.cd_lingua_3, ts.aa_pubb_1, ts.aa_pubb_2, ts.tp_aa_pubb,
    ts.cd_genere_1, ts.cd_genere_2, ts.cd_genere_3, ts.cd_genere_4,
    ts.ky_cles1_t, ts.ky_cles2_t, ts.ky_clet1_t, ts.ky_clet2_t,
    ts.ky_cles1_ct, ts.ky_cles2_ct, ts.ky_clet1_ct, ts.ky_clet2_ct,
    ts.cd_livello, ts.fl_speciale, ts.isbd, ts.indice_isbd, ts.ky_editore,
    ts.cd_agenzia, ts.cd_norme_cat, ts.nota_cat_tit, ts.nota_inf_tit,
    ts.bid_link, ts.tp_link, ts.ute_ins, ts.ts_ins, ts.ute_var, ts.ts_var,
    ts.ute_forza_ins, ts.ute_forza_var, ts.fl_canc, mt.mid, mt.ds_marca,
    mt.tidx_vector
FROM vl_titolo_the ts, vl_marca_tit mt
WHERE (ts.bid = mt.bid);

--
-- Definition for view vl_titolo_tit_c (OID = 59769) : 
--
CREATE VIEW sbnweb.vl_titolo_tit_c AS
SELECT tr_tit_tit.bid_coll, tr_tit_tit.tp_legame,
    tr_tit_tit.tp_legame_musica, tr_tit_tit.cd_natura_base,
    tr_tit_tit.cd_natura_coll, tr_tit_tit.sequenza,
    tr_tit_tit.nota_tit_tit, tr_tit_tit.sequenza_musica, tr_tit_tit.sici,
    tr_tit_tit.fl_condiviso AS fl_condiviso_legame, tr_tit_tit.ts_condiviso
    AS ts_condiviso_legame, tr_tit_tit.ute_condiviso AS
    ute_condiviso_legame, tb_titolo.bid, tb_titolo.isadn,
    tb_titolo.tp_materiale, tb_titolo.tp_record_uni, tb_titolo.cd_natura,
    tb_titolo.cd_paese, tb_titolo.cd_lingua_1, tb_titolo.cd_lingua_2,
    tb_titolo.cd_lingua_3, tb_titolo.aa_pubb_1, tb_titolo.aa_pubb_2,
    tb_titolo.tp_aa_pubb, tb_titolo.cd_genere_1, tb_titolo.cd_genere_2,
    tb_titolo.cd_genere_3, tb_titolo.cd_genere_4, tb_titolo.ky_cles1_t,
    tb_titolo.ky_cles2_t, tb_titolo.ky_clet1_t, tb_titolo.ky_clet2_t,
    tb_titolo.ky_cles1_ct, tb_titolo.ky_cles2_ct, tb_titolo.ky_clet1_ct,
    tb_titolo.ky_clet2_ct, tb_titolo.cd_livello, tb_titolo.fl_speciale,
    tb_titolo.isbd, tb_titolo.indice_isbd, tb_titolo.ky_editore,
    tb_titolo.cd_agenzia, tb_titolo.cd_norme_cat, tb_titolo.nota_cat_tit,
    tb_titolo.nota_inf_tit, tb_titolo.bid_link, tb_titolo.tp_link,
    tb_titolo.ute_ins, tb_titolo.ts_ins, tb_titolo.ute_var,
    tb_titolo.ts_var, tb_titolo.ute_forza_ins, tb_titolo.ute_forza_var,
    tb_titolo.fl_canc, tb_titolo.fl_condiviso, tb_titolo.ts_condiviso,
    tb_titolo.ute_condiviso
FROM tr_tit_tit, tb_titolo
WHERE (((tr_tit_tit.bid_base = tb_titolo.bid) AND (tr_tit_tit.fl_canc <>
    'S'::bpchar)) AND (tb_titolo.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_titolo_tit_c_luo (OID = 59779) : 
--
CREATE VIEW sbnweb.ve_titolo_tit_c_luo AS
SELECT tt.bid_coll, tt.tp_legame, tt.tp_legame_musica, tt.cd_natura_base,
    tt.cd_natura_coll, tt.sequenza, tt.nota_tit_tit, tt.sequenza_musica,
    tt.sici, tt.bid, tt.isadn, tt.tp_materiale, tt.tp_record_uni,
    tt.cd_natura, tt.cd_paese, tt.cd_lingua_1, tt.cd_lingua_2,
    tt.cd_lingua_3, tt.aa_pubb_1, tt.aa_pubb_2, tt.tp_aa_pubb,
    tt.cd_genere_1, tt.cd_genere_2, tt.cd_genere_3, tt.cd_genere_4,
    tt.ky_cles1_t, tt.ky_cles2_t, tt.ky_clet1_t, tt.ky_clet2_t,
    tt.ky_cles1_ct, tt.ky_cles2_ct, tt.ky_clet1_ct, tt.ky_clet2_ct,
    tt.cd_livello, tt.fl_speciale, tt.isbd, tt.indice_isbd, tt.ky_editore,
    tt.cd_agenzia, tt.cd_norme_cat, tt.nota_cat_tit, tt.nota_inf_tit,
    tt.bid_link, tt.tp_link, tt.ute_ins, tt.ts_ins, tt.ute_var, tt.ts_var,
    tt.ute_forza_ins, tt.ute_forza_var, tt.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_titolo_tit_c tt, vl_luogo_tit lt
WHERE (tt.bid = lt.bid);

--
-- Definition for view ve_titolo_tit_c_mar (OID = 59784) : 
--
CREATE VIEW sbnweb.ve_titolo_tit_c_mar AS
SELECT tt.bid_coll, tt.tp_legame, tt.tp_legame_musica, tt.cd_natura_base,
    tt.cd_natura_coll, tt.sequenza, tt.nota_tit_tit, tt.sequenza_musica,
    tt.sici, tt.bid, tt.isadn, tt.tp_materiale, tt.tp_record_uni,
    tt.cd_natura, tt.cd_paese, tt.cd_lingua_1, tt.cd_lingua_2,
    tt.cd_lingua_3, tt.aa_pubb_1, tt.aa_pubb_2, tt.tp_aa_pubb,
    tt.cd_genere_1, tt.cd_genere_2, tt.cd_genere_3, tt.cd_genere_4,
    tt.ky_cles1_t, tt.ky_cles2_t, tt.ky_clet1_t, tt.ky_clet2_t,
    tt.ky_cles1_ct, tt.ky_cles2_ct, tt.ky_clet1_ct, tt.ky_clet2_ct,
    tt.cd_livello, tt.fl_speciale, tt.isbd, tt.indice_isbd, tt.ky_editore,
    tt.cd_agenzia, tt.cd_norme_cat, tt.nota_cat_tit, tt.nota_inf_tit,
    tt.bid_link, tt.tp_link, tt.ute_ins, tt.ts_ins, tt.ute_var, tt.ts_var,
    tt.ute_forza_ins, tt.ute_forza_var, tt.fl_canc, mt.mid, mt.ds_marca,
    mt.tidx_vector
FROM vl_titolo_tit_c tt, vl_marca_tit mt
WHERE (tt.bid = tt.bid);

--
-- Definition for view vf_cartografia_luo (OID = 59794) : 
--
CREATE OR REPLACE VIEW sbnweb.vf_cartografia_luo(
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    lid,
    ky_norm_luogo)
AS
  SELECT c.cd_livello_c,
         c.tp_pubb_gov,
         c.cd_colore,
         c.cd_meridiano,
         c.cd_supporto_fisico,
         c.cd_tecnica,
         c.cd_forma_ripr,
         c.cd_forma_pubb,
         c.cd_altitudine,
         c.cd_tiposcala,
         c.tp_scala,
         c.scala_oriz,
         c.scala_vert,
         c.longitudine_ovest,
         c.longitudine_est,
         c.latitudine_nord,
         c.latitudine_sud,
         c.tp_immagine,
         c.cd_forma_cart,
         c.cd_piattaforma,
         c.cd_categ_satellite,
		 c.tp_proiezione,
         c.bid,
         c.isadn,
         c.tp_materiale,
         c.tp_record_uni,
         c.cd_natura,
         c.cd_paese,
         c.cd_lingua_1,
         c.cd_lingua_2,
         c.cd_lingua_3,
         c.aa_pubb_1,
         c.aa_pubb_2,
         c.tp_aa_pubb,
         c.cd_genere_1,
         c.cd_genere_2,
         c.cd_genere_3,
         c.cd_genere_4,
         c.ky_cles1_t,
         c.ky_cles2_t,
         c.ky_clet1_t,
         c.ky_clet2_t,
         c.ky_cles1_ct,
         c.ky_cles2_ct,
         c.ky_clet1_ct,
         c.ky_clet2_ct,
         c.cd_livello,
         c.fl_speciale,
         c.isbd,
         c.indice_isbd,
         c.ky_editore,
         c.cd_agenzia,
         c.cd_norme_cat,
         c.nota_inf_tit,
         c.nota_cat_tit,
         c.bid_link,
         c.tp_link,
         c.ute_ins,
         c.ts_ins,
         c.ute_var,
         c.ts_var,
         c.ute_forza_ins,
         c.ute_forza_var,
         c.fl_canc,
         l.lid,
         l.ky_norm_luogo
  FROM v_cartografia c,
       tr_tit_luo tl,
       tb_luogo l
  WHERE tl.bid = c.bid AND
        l.lid = tl.lid AND
        c.fl_canc <> 'S'  AND
        tl.fl_canc <> 'S'  AND
        l.fl_canc <> 'S' ;


--
-- Definition for view vf_grafica_luo (OID = 59804) : 
--
CREATE VIEW sbnweb.vf_grafica_luo AS
SELECT g.cd_livello_g, g.tp_materiale_gra, g.cd_supporto, g.cd_colore,
    g.cd_tecnica_dis_1, g.cd_tecnica_dis_2, g.cd_tecnica_dis_3,
    g.cd_tecnica_sta_1, g.cd_tecnica_sta_2, g.cd_tecnica_sta_3,
    g.cd_design_funz, g.bid, g.isadn, g.tp_materiale, g.tp_record_uni,
    g.cd_natura, g.cd_paese, g.cd_lingua_1, g.cd_lingua_2, g.cd_lingua_3,
    g.aa_pubb_1, g.aa_pubb_2, g.tp_aa_pubb, g.cd_genere_1, g.cd_genere_2,
    g.cd_genere_3, g.cd_genere_4, g.ky_cles1_t, g.ky_cles2_t, g.ky_clet1_t,
    g.ky_clet2_t, g.ky_cles1_ct, g.ky_cles2_ct, g.ky_clet1_ct,
    g.ky_clet2_ct, g.cd_livello, g.fl_speciale, g.isbd, g.indice_isbd,
    g.ky_editore, g.cd_agenzia, g.cd_norme_cat, g.nota_inf_tit,
    g.nota_cat_tit, g.bid_link, g.tp_link, g.ute_ins, g.ts_ins, g.ute_var,
    g.ts_var, g.ute_forza_ins, g.ute_forza_var, g.fl_canc, l.lid, l.ky_norm_luogo
FROM v_grafica g, tr_tit_luo tl, tb_luogo l
WHERE (((((tl.bid = g.bid) AND (l.lid = tl.lid)) AND (g.fl_canc <>
    'S'::bpchar)) AND (tl.fl_canc <> 'S'::bpchar)) AND (l.fl_canc <> 'S'::bpchar));

--
-- Definition for view vf_musica_com (OID = 59814) : 
--
CREATE VIEW sbnweb.vf_musica_com AS
SELECT mb.cd_livello_m, mb.ds_org_sint, mb.ds_org_anal, mb.tp_elaborazione,
    mb.cd_stesura, mb.fl_composito, mb.fl_palinsesto, mb.datazione,
    mb.cd_presentazione, mb.cd_materia, mb.ds_illustrazioni,
    mb.notazione_musicale, mb.ds_legatura, mb.ds_conservazione,
    mb.tp_testo_letter, mb.bid, mb.isadn, mb.tp_materiale,
    mb.tp_record_uni, mb.cd_natura, mb.cd_paese, mb.cd_lingua_1,
    mb.cd_lingua_2, mb.cd_lingua_3, mb.aa_pubb_1, mb.aa_pubb_2,
    mb.tp_aa_pubb, mb.cd_genere_1, mb.cd_genere_2, mb.cd_genere_3,
    mb.cd_genere_4, mb.ky_cles1_t, mb.ky_cles2_t, mb.ky_clet1_t,
    mb.ky_clet2_t, mb.ky_cles1_ct, mb.ky_cles2_ct, mb.ky_clet1_ct,
    mb.ky_clet2_ct, mb.cd_livello, mb.fl_speciale, mb.isbd, mb.indice_isbd,
    mb.ky_editore, mb.cd_agenzia, mb.cd_norme_cat, mb.nota_cat_tit,
    mb.nota_inf_tit, mb.bid_link, mb.tp_link, mb.ute_ins, mb.ts_ins,
    mb.ute_var, mb.ts_var, mb.ute_forza_ins, mb.ute_forza_var, mb.fl_canc,
    tt.tp_legame_musica, mc.ds_org_sint AS ds_org_sint_cm, mc.ds_org_anal
    AS ds_org_anal_cm, c.bid AS bid_cm, c.cd_forma_1, c.cd_forma_2,
    c.cd_forma_3, c.numero_ordine, c.numero_opera, c.numero_cat_tem,
    c.cd_tonalita, c.datazione AS datazione_cm, c.aa_comp_1, c.aa_comp_2,
    c.ky_ord_ric, c.ky_est_ric, c.ky_app_ric, c.ky_ord_nor_pre,
    c.ky_est_nor_pre, c.ky_app_nor_pre
FROM v_musica mb, tr_tit_tit tt, v_musica mc, tb_composizione c
WHERE (((((((mc.bid = c.bid) AND (tt.bid_coll = c.bid)) AND (mb.bid =
    tt.bid_base)) AND (mb.fl_canc <> 'S'::bpchar)) AND (mc.fl_canc <>
    'S'::bpchar)) AND (tt.fl_canc <> 'S'::bpchar)) AND (c.fl_canc <> 'S'::bpchar));

--
-- Definition for view vf_musica_luo (OID = 59824) : 
--
CREATE VIEW sbnweb.vf_musica_luo AS
SELECT m.cd_livello_m, m.ds_org_sint, m.ds_org_anal, m.tp_elaborazione,
    m.cd_stesura, m.fl_composito, m.fl_palinsesto, m.datazione,
    m.cd_presentazione, m.cd_materia, m.ds_illustrazioni,
    m.notazione_musicale, m.ds_legatura, m.ds_conservazione,
    m.tp_testo_letter, m.bid, m.isadn, m.tp_materiale, m.tp_record_uni,
    m.cd_natura, m.cd_paese, m.cd_lingua_1, m.cd_lingua_2, m.cd_lingua_3,
    m.aa_pubb_1, m.aa_pubb_2, m.tp_aa_pubb, m.cd_genere_1, m.cd_genere_2,
    m.cd_genere_3, m.cd_genere_4, m.ky_cles1_t, m.ky_cles2_t, m.ky_clet1_t,
    m.ky_clet2_t, m.ky_cles1_ct, m.ky_cles2_ct, m.ky_clet1_ct,
    m.ky_clet2_ct, m.cd_livello, m.fl_speciale, m.isbd, m.indice_isbd,
    m.ky_editore, m.cd_agenzia, m.cd_norme_cat, m.nota_cat_tit,
    m.nota_inf_tit, m.bid_link, m.tp_link, m.ute_ins, m.ts_ins, m.ute_var,
    m.ts_var, m.ute_forza_ins, m.ute_forza_var, m.fl_canc, l.lid, l.ky_norm_luogo
FROM v_musica m, tr_tit_luo tl, tb_luogo l
WHERE (((((tl.bid = m.bid) AND (l.lid = tl.lid)) AND (m.fl_canc <>
    'S'::bpchar)) AND (tl.fl_canc <> 'S'::bpchar)) AND (l.fl_canc <> 'S'::bpchar));

--
-- Definition for view vf_titolo_com (OID = 59834) : 
--
CREATE VIEW sbnweb.vf_titolo_com AS
SELECT c.bid AS bid_com, c.cd_forma_1, c.cd_forma_2, c.cd_forma_3,
    c.numero_ordine, c.numero_opera, c.numero_cat_tem, c.cd_tonalita,
    c.datazione, c.ky_ord_ric, c.ky_est_ric, c.ky_app_ric,
    tt.tp_legame_musica, t.bid, t.isadn, t.tp_materiale, t.tp_record_uni,
    t.cd_natura, t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3,
    t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2,
    t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t,
    t.ky_clet2_t, t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct,
    t.ky_clet2_ct, t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd,
    t.ky_editore, t.cd_agenzia, t.cd_norme_cat, t.nota_cat_tit,
    t.nota_inf_tit, t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var,
    t.ts_var, t.ute_forza_ins, t.ute_forza_var, t.fl_canc, m.ds_org_sint,
    m.ds_org_anal
FROM tb_composizione c, tr_tit_tit tt, tb_titolo t, v_musica m
WHERE (((((((tt.bid_coll = c.bid) AND (t.bid = tt.bid_base)) AND (m.bid =
    c.bid)) AND (c.fl_canc <> 'S'::bpchar)) AND (tt.fl_canc <>
    'S'::bpchar)) AND (t.fl_canc <> 'S'::bpchar)) AND (m.fl_canc <> 'S'::bpchar));

--
-- Definition for view vf_titolo_luo (OID = 59839) : 
--
CREATE VIEW sbnweb.vf_titolo_luo AS
SELECT t.bid, t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura,
    t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1,
    t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3,
    t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t,
    t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_cat_tit, t.nota_inf_tit,
    t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var,
    t.ute_forza_ins, t.ute_forza_var, t.fl_canc, l.lid, l.ky_norm_luogo
FROM tb_titolo t, tr_tit_luo tl, tb_luogo l
WHERE (((((tl.bid = t.bid) AND (l.lid = tl.lid)) AND (t.fl_canc <>
    'S'::bpchar)) AND (tl.fl_canc <> 'S'::bpchar)) AND (l.fl_canc <> 'S'::bpchar));

--
-- Definition for view vf_titolo_mar (OID = 59844) : 
--
CREATE VIEW sbnweb.vf_titolo_mar AS
SELECT t.bid, t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura,
    t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1,
    t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3,
    t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t,
    t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_cat_tit, t.nota_inf_tit,
    t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var,
    t.ute_forza_ins, t.ute_forza_var, t.fl_canc, m.mid, m.ds_marca, m.tidx_vector
FROM tb_titolo t, tr_tit_mar tm, tb_marca m
WHERE (((((tm.bid = t.bid) AND (m.mid = tm.mid)) AND (t.fl_canc <>
    'S'::bpchar)) AND (tm.fl_canc <> 'S'::bpchar)) AND (m.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_allinea_aut_bib (OID = 59849) : 
--
CREATE VIEW sbnweb.vl_allinea_aut_bib AS
SELECT tb_autore.vid, tb_autore.ds_nome_aut, tb_autore.tidx_vector,
    tb_autore.cd_livello, tb_autore.ts_var, tb_autore.fl_canc,
    tb_autore.fl_condiviso, tb_autore.ts_condiviso,
    tb_autore.ute_condiviso, tr_aut_bib.cd_polo, tr_aut_bib.cd_biblioteca,
    tr_aut_bib.fl_allinea_sbnmarc, tr_aut_bib.fl_allinea
FROM tb_autore, tr_aut_bib
WHERE (((tb_autore.vid = tr_aut_bib.vid) AND (tr_aut_bib.fl_canc <>
    'S'::bpchar)) AND ((tr_aut_bib.fl_allinea <> ' '::bpchar) OR
    (tr_aut_bib.fl_allinea_sbnmarc <> ' '::bpchar)));

--
-- Definition for view vl_allinea_mar_bib (OID = 59853) : 
--
CREATE VIEW sbnweb.vl_allinea_mar_bib AS
SELECT tb_marca.mid, tb_marca.ds_marca, tb_marca.tidx_vector,
    tb_marca.cd_livello, tb_marca.ts_var, tb_marca.fl_canc,
    tb_marca.fl_condiviso, tb_marca.ts_condiviso, tb_marca.ute_condiviso,
    tr_mar_bib.cd_polo, tr_mar_bib.cd_biblioteca,
    tr_mar_bib.fl_allinea_sbnmarc, tr_mar_bib.fl_allinea
FROM tb_marca, tr_mar_bib
WHERE (((tb_marca.mid = tr_mar_bib.mid) AND (tr_mar_bib.fl_canc <>
    'S'::bpchar)) AND ((tr_mar_bib.fl_allinea <> ' '::bpchar) OR
    (tr_mar_bib.fl_allinea_sbnmarc <> ' '::bpchar)));

--
-- Definition for view vl_allinea_tit_bib (OID = 59857) : 
--
CREATE VIEW sbnweb.vl_allinea_tit_bib AS
SELECT tb_titolo.bid, tb_titolo.tp_materiale, tb_titolo.tp_record_uni,
    tb_titolo.cd_natura, tb_titolo.cd_livello, tb_titolo.isbd,
    tb_titolo.indice_isbd, tb_titolo.tp_link, tb_titolo.bid_link,
    tb_titolo.ts_var, tb_titolo.fl_canc, tb_titolo.fl_condiviso,
    tb_titolo.ts_condiviso, tb_titolo.ute_condiviso, tr_tit_bib.cd_polo,
    tr_tit_bib.cd_biblioteca, tr_tit_bib.fl_allinea_sbnmarc,
    tr_tit_bib.fl_allinea, tr_tit_bib.fl_allinea_cla,
    tr_tit_bib.fl_allinea_sog, tr_tit_bib.fl_allinea_luo,
    tr_tit_bib.fl_allinea_rep, tr_tit_bib.fl_possesso, tr_tit_bib.fl_gestione
FROM tb_titolo, tr_tit_bib
WHERE ((tb_titolo.bid = tr_tit_bib.bid) AND (tr_tit_bib.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_autore_aut (OID = 59862) : 
--
CREATE VIEW sbnweb.vl_autore_aut AS
SELECT v_aut_aut.vid_1, v_aut_aut.tp_legame, v_aut_aut.nota_aut_aut,
    tb_autore.vid, tb_autore.isadn, tb_autore.tp_forma_aut,
    tb_autore.ky_cautun, tb_autore.ky_auteur, tb_autore.ky_el1_a,
    tb_autore.ky_el1_b, tb_autore.ky_el2_a, tb_autore.ky_el2_b,
    tb_autore.tp_nome_aut, tb_autore.ky_el3, tb_autore.ky_el4,
    tb_autore.ky_el5, tb_autore.ky_cles1_a, tb_autore.ky_cles2_a,
    tb_autore.cd_paese, tb_autore.cd_lingua, tb_autore.aa_nascita,
    tb_autore.aa_morte, tb_autore.cd_livello, tb_autore.fl_speciale,
    tb_autore.ds_nome_aut, tb_autore.tidx_vector, tb_autore.cd_agenzia,
    tb_autore.cd_norme_cat, tb_autore.nota_aut, tb_autore.nota_cat_aut,
    tb_autore.vid_link, tb_autore.ute_ins, tb_autore.ts_ins,
    tb_autore.ute_var, tb_autore.ts_var, tb_autore.ute_forza_ins,
    tb_autore.ute_forza_var, tb_autore.fl_canc, tb_autore.fl_condiviso,
    tb_autore.ts_condiviso, tb_autore.ute_condiviso
FROM tb_autore, v_aut_aut
WHERE ((v_aut_aut.vid_2 = tb_autore.vid) AND (tb_autore.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_autore_bib (OID = 59867) : 
--
CREATE VIEW sbnweb.vl_autore_bib AS
SELECT tr_aut_bib.vid, tr_aut_bib.fl_allinea,
    tr_aut_bib.fl_allinea_sbnmarc, tr_aut_bib.ute_ins, tr_aut_bib.ts_ins,
    tr_aut_bib.ute_var, tr_aut_bib.ts_var, tbf_biblioteca_in_polo.cd_polo,
    tbf_biblioteca_in_polo.cd_biblioteca,
    tbf_biblioteca_in_polo.ky_biblioteca,
    tbf_biblioteca_in_polo.cd_ana_biblioteca,
    tbf_biblioteca_in_polo.ds_biblioteca, tbf_biblioteca_in_polo.ds_citta
FROM tr_aut_bib, tbf_biblioteca_in_polo
WHERE (((tbf_biblioteca_in_polo.cd_polo = tr_aut_bib.cd_polo) AND
    (tbf_biblioteca_in_polo.cd_biblioteca = tr_aut_bib.cd_biblioteca)) AND
    (tr_aut_bib.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_autore_mar (OID = 59871) : 
--
CREATE VIEW sbnweb.vl_autore_mar AS
SELECT am.mid, am.nota_aut_mar, a.vid, a.isadn, a.tp_forma_aut,
    a.ky_cautun, a.ky_auteur, a.ky_el1_a, a.ky_el1_b, a.ky_el2_a,
    a.ky_el2_b, a.tp_nome_aut, a.ky_el3, a.ky_el4, a.ky_el5, a.ky_cles1_a,
    a.ky_cles2_a, a.cd_paese, a.cd_lingua, a.aa_nascita, a.aa_morte,
    a.cd_livello, a.fl_speciale, a.ds_nome_aut, a.tidx_vector,
    a.cd_agenzia, a.cd_norme_cat, a.nota_aut, a.nota_cat_aut, a.vid_link,
    a.ute_ins, a.ts_ins, a.ute_var, a.ts_var, a.ute_forza_ins,
    a.ute_forza_var, a.fl_canc, a.fl_condiviso, a.ts_condiviso, a.ute_condiviso
FROM tr_aut_mar am, tb_autore a
WHERE (((am.vid = a.vid) AND (am.fl_canc <> 'S'::bpchar)) AND (a.fl_canc <>
    'S'::bpchar));

--
-- Definition for view vl_autore_rep (OID = 59876) : 
--
CREATE VIEW sbnweb.vl_autore_rep AS
SELECT ra.id_repertorio, ra.note_rep_aut, ra.fl_trovato, a.vid, a.isadn,
    a.tp_forma_aut, a.ky_cautun, a.ky_auteur, a.ky_el1_a, a.ky_el1_b,
    a.ky_el2_a, a.ky_el2_b, a.tp_nome_aut, a.ky_el3, a.ky_el4, a.ky_el5,
    a.ky_cles1_a, a.ky_cles2_a, a.cd_paese, a.cd_lingua, a.aa_nascita,
    a.aa_morte, a.cd_livello, a.fl_speciale, a.ds_nome_aut, a.tidx_vector,
    a.cd_agenzia, a.cd_norme_cat, a.nota_aut, a.nota_cat_aut, a.vid_link,
    a.ute_ins, a.ts_ins, a.ute_var, a.ts_var, a.ute_forza_ins,
    a.ute_forza_var, a.fl_canc, a.fl_condiviso, a.ts_condiviso, a.ute_condiviso
FROM tr_rep_aut ra, tb_autore a
WHERE (((ra.vid = a.vid) AND (ra.fl_canc <> 'S'::bpchar)) AND (a.fl_canc <>
    'S'::bpchar));

--
-- Definition for view vl_autore_stat (OID = 59881) : 
--
CREATE VIEW sbnweb.vl_autore_stat AS
SELECT tb_autore.vid, tb_autore.tp_forma_aut, tb_autore.tp_nome_aut,
    "substring"((tb_autore.ute_ins)::text, 4, 3) AS bib_ute_ins, tb_autore.fl_canc
FROM tb_autore
WHERE (tb_autore.fl_canc <> 'S'::bpchar);

--
-- Definition for view vl_biblioteca_cla (OID = 59885) : 
--
CREATE VIEW sbnweb.vl_biblioteca_cla AS
SELECT tc.bid, tc.cd_sistema, tc.cd_edizione, tc.classe, tb.fl_mutilo,
    tb.ds_consistenza, tb.fl_possesso, tb.fl_gestione, tb.fl_disp_elettr,
    tb.fl_allinea, tb.fl_allinea_sbnmarc, tb.fl_allinea_cla,
    tb.fl_allinea_sog, tb.fl_allinea_luo, tb.fl_allinea_rep, tb.ds_fondo,
    tb.ds_segn, tb.ds_antica_segn, tb.nota_tit_bib, tb.uri_copia,
    tb.tp_digitalizz, tb.ute_ins, tb.ts_ins, tb.ute_var, tb.ts_var,
    b.cd_polo, b.cd_biblioteca, b.ky_biblioteca, b.cd_ana_biblioteca,
    b.ds_biblioteca, b.ds_citta
FROM tr_tit_cla tc, tr_tit_bib tb, tbf_biblioteca_in_polo b
WHERE (((((tc.bid = tb.bid) AND (b.cd_polo = tb.cd_polo)) AND
    (b.cd_biblioteca = tb.cd_biblioteca)) AND (tb.fl_canc <> 'S'::bpchar))
    AND (tc.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_biblioteca_luo (OID = 59890) : 
--
CREATE VIEW sbnweb.vl_biblioteca_luo AS
SELECT tl.bid, tl.lid, tl.tp_luogo, tl.nota_tit_luo, tb.fl_mutilo,
    tb.ds_consistenza, tb.fl_possesso, tb.fl_gestione, tb.fl_disp_elettr,
    tb.fl_allinea, tb.fl_allinea_sbnmarc, tb.fl_allinea_cla,
    tb.fl_allinea_sog, tb.fl_allinea_luo, tb.fl_allinea_rep, tb.ds_fondo,
    tb.ds_segn, tb.ds_antica_segn, tb.nota_tit_bib, tb.uri_copia,
    tb.tp_digitalizz, tb.ute_ins, tb.ts_ins, tb.ute_var, tb.ts_var,
    b.cd_polo, b.cd_biblioteca, b.ky_biblioteca, b.cd_ana_biblioteca,
    b.ds_biblioteca, b.ds_citta
FROM tr_tit_luo tl, tr_tit_bib tb, tbf_biblioteca_in_polo b
WHERE (((((tl.bid = tb.bid) AND (b.cd_polo = tb.cd_polo)) AND
    (b.cd_biblioteca = tb.cd_biblioteca)) AND (tb.fl_canc <> 'S'::bpchar))
    AND (tl.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_biblioteca_sog (OID = 59895) : 
--
CREATE VIEW sbnweb.vl_biblioteca_sog AS
SELECT ts.bid, ts.cid, tb.fl_mutilo, tb.ds_consistenza, tb.fl_possesso,
    tb.fl_gestione, tb.fl_disp_elettr, tb.fl_allinea,
    tb.fl_allinea_sbnmarc, tb.fl_allinea_cla, tb.fl_allinea_sog,
    tb.fl_allinea_luo, tb.fl_allinea_rep, tb.ds_fondo, tb.ds_segn,
    tb.ds_antica_segn, tb.nota_tit_bib, tb.uri_copia, tb.tp_digitalizz,
    tb.ute_ins, tb.ts_ins, tb.ute_var, tb.ts_var, b.cd_polo,
    b.cd_biblioteca, b.ky_biblioteca, b.cd_ana_biblioteca, b.ds_biblioteca,
    b.ds_citta
FROM tr_tit_sog_bib ts, tr_tit_bib tb, tbf_biblioteca_in_polo b
WHERE (((((ts.bid = tb.bid) AND (b.cd_polo = tb.cd_polo)) AND
    (b.cd_biblioteca = tb.cd_biblioteca)) AND (ts.fl_canc <> 'S'::bpchar))
    AND (tb.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_biblioteca_tit (OID = 59900) : 
--
CREATE VIEW sbnweb.vl_biblioteca_tit AS
SELECT tb.bid, tb.fl_mutilo, tb.ds_consistenza, tb.fl_possesso,
    tb.fl_gestione, tb.fl_disp_elettr, tb.fl_allinea,
    tb.fl_allinea_sbnmarc, tb.fl_allinea_cla, tb.fl_allinea_sog,
    tb.fl_allinea_luo, tb.fl_allinea_rep, tb.ds_fondo, tb.ds_segn,
    tb.ds_antica_segn, tb.nota_tit_bib, tb.uri_copia, tb.tp_digitalizz,
    tb.ute_ins, tb.ts_ins, tb.ute_var, tb.ts_var, b.cd_polo,
    b.cd_biblioteca, b.ky_biblioteca, b.cd_ana_biblioteca, b.ds_biblioteca,
    b.ds_citta
FROM tr_tit_bib tb, tbf_biblioteca_in_polo b
WHERE (((b.cd_polo = tb.cd_polo) AND (b.cd_biblioteca = tb.cd_biblioteca))
    AND (tb.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_classe_stat (OID = 59905) : 
--
CREATE VIEW sbnweb.vl_classe_stat AS
SELECT tb_classe.cd_sistema, tb_classe.cd_edizione, tb_classe.classe,
    "substring"((tb_classe.ute_ins)::text, 4, 3) AS bib_ute_ins, tb_classe.fl_canc
FROM tb_classe
WHERE (tb_classe.fl_canc <> 'S'::bpchar);

--
-- Definition for view vl_classe_the (OID = 59909) : 
--
CREATE VIEW sbnweb.vl_classe_the AS
SELECT tb_classe.cd_sistema, tb_classe.cd_edizione, tb_classe.classe,
    tb_classe.cd_livello, tb_classe.fl_costruito, tb_classe.fl_speciale,
    tb_classe.ds_classe, tb_classe.tidx_vector, tb_classe.ute_ins,
    tb_classe.ts_ins, tb_classe.ute_var, tb_classe.ts_var,
    tb_classe.fl_canc, tb_classe.fl_condiviso, tb_classe.ts_condiviso,
    tb_classe.ute_condiviso, tr_the_cla.cd_the, tr_the_cla.did,
    tr_the_cla.posizione
FROM tr_the_cla, tb_classe
WHERE (((((tr_the_cla.cd_sistema = tb_classe.cd_sistema) AND
    (tr_the_cla.cd_edizione = tb_classe.cd_edizione)) AND
    (tr_the_cla.classe = tb_classe.classe)) AND (tr_the_cla.fl_canc <>
    'S'::bpchar)) AND (tb_classe.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_classe_tit (OID = 59913) : 
--
CREATE VIEW sbnweb.vl_classe_tit AS
SELECT tb_classe.cd_sistema, tb_classe.cd_edizione, tb_classe.classe,
    tb_classe.cd_livello, tb_classe.fl_costruito, tb_classe.fl_speciale,
    tb_classe.ds_classe, tb_classe.tidx_vector, tb_classe.ute_ins,
    tb_classe.ts_ins, tb_classe.ute_var, tb_classe.ts_var,
    tb_classe.fl_canc, tb_classe.fl_condiviso, tb_classe.ts_condiviso,
    tb_classe.ute_condiviso, tr_tit_cla.bid, tr_tit_cla.nota_tit_cla
FROM tr_tit_cla, tb_classe
WHERE (((((tr_tit_cla.cd_sistema = tb_classe.cd_sistema) AND
    (tr_tit_cla.cd_edizione = tb_classe.cd_edizione)) AND
    (tr_tit_cla.classe = tb_classe.classe)) AND (tr_tit_cla.fl_canc <>
    'S'::bpchar)) AND (tb_classe.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_des_sog_freq (OID = 59922) : 
--
CREATE VIEW sbnweb.vl_des_sog_freq AS
SELECT a.did, a.fl_primavoce, c.ds_descrittore, c.ky_norm_descritt,
    c.tidx_vector AS des_vector, b.ds_soggetto, b.tidx_vector AS
    sog_vector, b.cid, b.cd_livello, b.cd_soggettario, b.fl_speciale,
    b.ky_cles1_s, b.ky_cles2_s, b.ute_ins, b.ts_ins, b.ute_var, b.ts_var,
    b.fl_canc, b.fl_condiviso, b.ts_condiviso, b.ute_condiviso
FROM tr_sog_des a, tb_soggetto b, tb_descrittore c
WHERE ((((a.cid = b.cid) AND (a.did = c.did)) AND (a.fl_canc <>
    'S'::bpchar)) AND (b.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_descrittore_stat (OID = 59942) : 
--
CREATE VIEW sbnweb.vl_descrittore_stat AS
SELECT tb_descrittore.did, tb_descrittore.tp_forma_des,
    "substring"((tb_descrittore.ute_ins)::text, 4, 3) AS bib_ute_ins,
    tb_descrittore.fl_canc
FROM tb_descrittore
WHERE (tb_descrittore.fl_canc <> 'S'::bpchar);

--
-- Definition for view vl_fascicolo_cod_doc (OID = 59951) : 
--
CREATE VIEW sbnweb.vl_fascicolo_cod_doc AS
SELECT ef.id_ese_fascicolo, ef.bid, ef.fid, ef.id_ordine, ef.cd_bib_abb,
    ef.data_arrivo, ef.cd_polo_inv, ef.cd_bib_inv, ef.cd_serie,
    ef.cd_inven, ef.grp_fasc, ef.note, ef.ute_ins, ef.ts_ins, ef.ute_var,
    ef.ts_var, ef.fl_canc, ef.cd_no_disp, i.key_loc, et.cd_polo,
    et.cd_biblioteca, et.bid AS bid_doc, et.cd_doc
FROM (((tbp_esemplare_fascicolo ef JOIN tbc_inventario i ON
    (((((((i.fl_canc <> 'S'::bpchar) AND (i.cd_sit = '2'::bpchar)) AND
    (ef.cd_polo_inv = i.cd_polo)) AND (ef.cd_bib_inv = i.cd_bib)) AND
    (ef.cd_serie = i.cd_serie)) AND (ef.cd_inven = i.cd_inven)))) JOIN
    tbc_collocazione c ON (((c.key_loc = i.key_loc) AND (c.fl_canc <>
    'S'::bpchar)))) JOIN tbc_esemplare_titolo et ON ((((((et.cd_polo =
    c.cd_polo_doc) AND (et.cd_biblioteca = c.cd_biblioteca_doc)) AND
    (et.bid = c.bid_doc)) AND (et.cd_doc = c.cd_doc)) AND (et.fl_canc <>
    'S'::bpchar))))
WHERE (((((i.fl_canc <> 'S'::bpchar) AND (ef.cd_polo_inv = i.cd_polo)) AND
    (ef.cd_bib_inv = i.cd_bib)) AND (ef.cd_serie = i.cd_serie)) AND
    (ef.cd_inven = i.cd_inven))
UNION
SELECT ef.id_ese_fascicolo, ef.bid, ef.fid, ef.id_ordine, ef.cd_bib_abb,
    ef.data_arrivo, ef.cd_polo_inv, ef.cd_bib_inv, ef.cd_serie,
    ef.cd_inven, ef.grp_fasc, ef.note, ef.ute_ins, ef.ts_ins, ef.ute_var,
    ef.ts_var, ef.fl_canc, ef.cd_no_disp, i.key_loc, et.cd_polo,
    et.cd_biblioteca, et.bid AS bid_doc, et.cd_doc
FROM ((((tbp_esemplare_fascicolo ef JOIN tba_ordini o ON (((o.id_ordine =
    ef.id_ordine) AND (o.fl_canc <> 'S'::bpchar)))) JOIN tbc_inventario i
    ON ((((((i.cd_bib_ord = o.cd_bib) AND (i.cd_tip_ord = o.cod_tip_ord))
    AND ((i.anno_ord)::numeric = o.anno_ord)) AND (i.cd_ord = o.cod_ord))
    AND (o.fl_canc <> 'S'::bpchar)))) JOIN tbc_collocazione c ON
    (((c.key_loc = i.key_loc) AND (c.fl_canc <> 'S'::bpchar)))) JOIN
    tbc_esemplare_titolo et ON ((((((et.cd_polo = c.cd_polo_doc) AND
    (et.cd_biblioteca = c.cd_biblioteca_doc)) AND (et.bid = c.bid_doc)) AND
    (et.cd_doc = c.cd_doc)) AND (et.fl_canc <> 'S'::bpchar))));

--
-- Definition for view vl_fascicolo_key_loc (OID = 59956) : 
--
CREATE VIEW sbnweb.vl_fascicolo_key_loc AS
SELECT ef.id_ese_fascicolo, ef.bid, ef.fid, ef.id_ordine, ef.cd_bib_abb,
    ef.data_arrivo, ef.cd_polo_inv, ef.cd_bib_inv, ef.cd_serie,
    ef.cd_inven, ef.grp_fasc, ef.note, ef.ute_ins, ef.ts_ins, ef.ute_var,
    ef.ts_var, ef.fl_canc, ef.cd_no_disp, i.key_loc
FROM (tbp_esemplare_fascicolo ef JOIN tbc_inventario i ON (((((((i.fl_canc
    <> 'S'::bpchar) AND (i.cd_sit = '2'::bpchar)) AND (ef.cd_polo_inv =
    i.cd_polo)) AND (ef.cd_bib_inv = i.cd_bib)) AND (ef.cd_serie =
    i.cd_serie)) AND (ef.cd_inven = i.cd_inven))))
UNION
SELECT ef.id_ese_fascicolo, ef.bid, ef.fid, ef.id_ordine, ef.cd_bib_abb,
    ef.data_arrivo, ef.cd_polo_inv, ef.cd_bib_inv, ef.cd_serie,
    ef.cd_inven, ef.grp_fasc, ef.note, ef.ute_ins, ef.ts_ins, ef.ute_var,
    ef.ts_var, ef.fl_canc, ef.cd_no_disp, i.key_loc
FROM ((tbp_esemplare_fascicolo ef JOIN tba_ordini o ON ((o.id_ordine =
    ef.id_ordine))) JOIN tbc_inventario i ON ((((((i.cd_bib_ord = o.cd_bib)
    AND (i.cd_tip_ord = o.cod_tip_ord)) AND ((i.anno_ord)::numeric =
    o.anno_ord)) AND (i.cd_ord = o.cod_ord)) AND (o.fl_canc <> 'S'::bpchar))));

--
-- Definition for view vl_luogo_luo (OID = 59961) : 
--

CREATE VIEW sbnweb.vl_luogo_luo AS
SELECT v_luo_luo.lid_1, v_luo_luo.tp_legame, tb_luogo.lid,
    tb_luogo.tp_forma, tb_luogo.cd_livello, tb_luogo.ds_luogo,
    tb_luogo.ky_luogo, tb_luogo.ky_norm_luogo, tb_luogo.cd_paese,
    tb_luogo.nota_luogo, tb_luogo.ute_ins, tb_luogo.ts_ins,
    tb_luogo.ute_var, tb_luogo.ts_var, tb_luogo.fl_canc, tb_luogo.nota_catalogatore
FROM v_luo_luo, tb_luogo
WHERE ((v_luo_luo.lid_2 = tb_luogo.lid) AND (tb_luogo.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_luogo_stat (OID = 59965) : 
--
CREATE VIEW sbnweb.vl_luogo_stat AS
SELECT tb_luogo.lid, tb_luogo.tp_forma,
    "substring"((tb_luogo.ute_ins)::text, 4, 3) AS bib_ute_ins, tb_luogo.fl_canc
FROM tb_luogo
WHERE (tb_luogo.fl_canc <> 'S'::bpchar);

--
-- Definition for view vl_marca_aut (OID = 59969) : 
--
CREATE VIEW sbnweb.vl_marca_aut AS
SELECT tr_aut_mar.vid, tr_aut_mar.nota_aut_mar, tb_marca.mid,
    tb_marca.cd_livello, tb_marca.fl_speciale, tb_marca.ds_marca,
    tb_marca.tidx_vector, tb_marca.nota_marca, tb_marca.ds_motto,
    tb_marca.ute_ins, tb_marca.ts_ins, tb_marca.ute_var, tb_marca.ts_var,
    tb_marca.fl_canc, tb_marca.fl_condiviso, tb_marca.ts_condiviso,
    tb_marca.ute_condiviso
FROM tr_aut_mar, tb_marca
WHERE (((tr_aut_mar.mid = tb_marca.mid) AND (tr_aut_mar.fl_canc <>
    'S'::bpchar)) AND (tb_marca.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_marca_bib (OID = 59973) : 
--
CREATE VIEW sbnweb.vl_marca_bib AS
SELECT tr_mar_bib.mid, tr_mar_bib.fl_allinea, tr_mar_bib.ute_ins,
    tr_mar_bib.ts_ins, tr_mar_bib.ute_var, tr_mar_bib.ts_var,
    tbf_biblioteca_in_polo.cd_polo, tbf_biblioteca_in_polo.cd_biblioteca,
    tbf_biblioteca_in_polo.ky_biblioteca,
    tbf_biblioteca_in_polo.cd_ana_biblioteca,
    tbf_biblioteca_in_polo.ds_biblioteca, tbf_biblioteca_in_polo.ds_citta
FROM tr_mar_bib, tbf_biblioteca_in_polo
WHERE (((tbf_biblioteca_in_polo.cd_polo = tr_mar_bib.cd_polo) AND
    (tbf_biblioteca_in_polo.cd_biblioteca = tr_mar_bib.cd_biblioteca)) AND
    (tr_mar_bib.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_marca_par (OID = 59977) : 
--
CREATE VIEW sbnweb.vl_marca_par AS
SELECT tb_parola.parola, tb_marca.mid, tb_marca.cd_livello,
    tb_marca.fl_speciale, tb_marca.ds_marca, tb_marca.tidx_vector,
    tb_marca.nota_marca, tb_marca.ds_motto, tb_marca.ute_ins,
    tb_marca.ts_ins, tb_marca.ute_var, tb_marca.ts_var, tb_marca.fl_canc,
    tb_marca.fl_condiviso, tb_marca.ts_condiviso, tb_marca.ute_condiviso
FROM tb_parola, tb_marca
WHERE ((tb_parola.mid = tb_marca.mid) AND (tb_marca.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_marca_rep (OID = 59981) : 
--
CREATE VIEW sbnweb.vl_marca_rep AS
SELECT tr_rep_mar.progr_repertorio, tr_rep_mar.nota_rep_mar, tb_marca.mid,
    tb_marca.cd_livello, tb_marca.fl_speciale, tb_marca.ds_marca,
    tb_marca.tidx_vector, tb_marca.nota_marca, tb_marca.ds_motto,
    tb_marca.ute_ins, tb_marca.ts_ins, tb_marca.ute_var, tb_marca.ts_var,
    tb_marca.fl_canc, tb_marca.fl_condiviso, tb_marca.ts_condiviso,
    tb_marca.ute_condiviso
FROM tr_rep_mar, tb_marca
WHERE (((tr_rep_mar.mid = tb_marca.mid) AND (tr_rep_mar.fl_canc <>
    'S'::bpchar)) AND (tb_marca.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_marca_stat (OID = 59985) : 
--
CREATE VIEW sbnweb.vl_marca_stat AS
SELECT tb_marca.mid, "substring"((tb_marca.ute_ins)::text, 4, 3) AS
    bib_ute_ins, tb_marca.fl_canc
FROM tb_marca
WHERE (tb_marca.fl_canc <> 'S'::bpchar);

--
-- Definition for view vl_musica_bib (OID = 59989) : 
--
CREATE VIEW sbnweb.vl_musica_bib AS
SELECT tb.bid, tb.cd_polo, tb.cd_biblioteca, tb.fl_mutilo,
    tb.ds_consistenza, tb.fl_possesso, tb.fl_gestione, tb.fl_disp_elettr,
    tb.fl_allinea, tb.fl_allinea_sbnmarc, tb.fl_allinea_cla,
    tb.fl_allinea_sog, tb.fl_allinea_luo, tb.fl_allinea_rep, tb.ds_fondo,
    tb.ds_segn, tb.ds_antica_segn, tb.nota_tit_bib, tb.uri_copia,
    tb.tp_digitalizz, m.cd_livello_m, m.ds_org_sint, m.ds_org_anal,
    m.tp_elaborazione, m.cd_stesura, m.fl_composito, m.fl_palinsesto,
    m.datazione, m.cd_presentazione, m.cd_materia, m.ds_illustrazioni,
    m.notazione_musicale, m.ds_legatura, m.ds_conservazione,
    m.tp_testo_letter, m.isadn, m.tp_materiale, m.tp_record_uni,
    m.cd_natura, m.cd_paese, m.cd_lingua_1, m.cd_lingua_2, m.cd_lingua_3,
    m.aa_pubb_1, m.aa_pubb_2, m.tp_aa_pubb, m.cd_genere_1, m.cd_genere_2,
    m.cd_genere_3, m.cd_genere_4, m.ky_cles1_t, m.ky_cles2_t, m.ky_clet1_t,
    m.ky_clet2_t, m.ky_cles1_ct, m.ky_cles2_ct, m.ky_clet1_ct,
    m.ky_clet2_ct, m.cd_livello, m.fl_speciale, m.isbd, m.indice_isbd,
    m.ky_editore, m.cd_agenzia, m.cd_norme_cat, m.nota_cat_tit,
    m.nota_inf_tit, m.bid_link, m.tp_link, m.ute_ins, m.ts_ins, m.ute_var,
    m.ts_var, m.ute_forza_ins, m.ute_forza_var, m.fl_canc, m.fl_condiviso,
    m.ts_condiviso, m.ute_condiviso
FROM tr_tit_bib tb, v_musica m
WHERE (((m.bid = tb.bid) AND (m.fl_canc <> 'S'::bpchar)) AND (tb.fl_canc <>
    'S'::bpchar));

--
-- Definition for view vl_musica_imp (OID = 59994) : 
--
CREATE VIEW sbnweb.vl_musica_imp AS
SELECT i.impronta_1, i.impronta_2, i.impronta_3, i.nota_impronta,
    m.cd_livello_m, m.ds_org_sint, m.ds_org_anal, m.tp_elaborazione,
    m.cd_stesura, m.fl_composito, m.fl_palinsesto, m.datazione,
    m.cd_presentazione, m.cd_materia, m.ds_illustrazioni,
    m.notazione_musicale, m.ds_legatura, m.ds_conservazione,
    m.tp_testo_letter, m.bid, m.isadn, m.tp_materiale, m.tp_record_uni,
    m.cd_natura, m.cd_paese, m.cd_lingua_1, m.cd_lingua_2, m.cd_lingua_3,
    m.aa_pubb_1, m.aa_pubb_2, m.tp_aa_pubb, m.cd_genere_1, m.cd_genere_2,
    m.cd_genere_3, m.cd_genere_4, m.ky_cles1_t, m.ky_cles2_t, m.ky_clet1_t,
    m.ky_clet2_t, m.ky_cles1_ct, m.ky_cles2_ct, m.ky_clet1_ct,
    m.ky_clet2_ct, m.cd_livello, m.fl_speciale, m.isbd, m.indice_isbd,
    m.ky_editore, m.cd_agenzia, m.cd_norme_cat, m.nota_cat_tit,
    m.nota_inf_tit, m.bid_link, m.tp_link, m.ute_ins, m.ts_ins, m.ute_var,
    m.ts_var, m.ute_forza_ins, m.ute_forza_var, m.fl_canc, m.fl_condiviso,
    m.ts_condiviso, m.ute_condiviso
FROM tb_impronta i, v_musica m
WHERE (((i.bid = m.bid) AND (i.fl_canc <> 'S'::bpchar)) AND (m.fl_canc <>
    'S'::bpchar));

--
-- Definition for view vl_repertorio_mar (OID = 59999) : 
--
CREATE VIEW sbnweb.vl_repertorio_mar AS
SELECT tr_rep_mar.mid, tr_rep_mar.progr_repertorio,
    tr_rep_mar.nota_rep_mar, tb_repertorio.id_repertorio,
    btrim((tb_repertorio.cd_sig_repertorio)::text, ' '::text) AS
    cd_sig_repertorio, tb_repertorio.ds_repertorio,
    tb_repertorio.tidx_vector, tb_repertorio.tp_repertorio,
    tb_repertorio.ute_ins, tb_repertorio.ts_ins, tb_repertorio.ute_var,
    tb_repertorio.ts_var, tb_repertorio.fl_canc
FROM tr_rep_mar, tb_repertorio
WHERE (((tr_rep_mar.id_repertorio = tb_repertorio.id_repertorio) AND
    (tr_rep_mar.fl_canc <> 'S'::bpchar)) AND (tb_repertorio.fl_canc <>
    'S'::bpchar));

--
-- Definition for view vl_soggetto_stat (OID = 60012) : 
--
CREATE VIEW sbnweb.vl_soggetto_stat AS
SELECT tb_soggetto.cid, tb_soggetto.cd_soggettario,
    "substring"((tb_soggetto.ute_ins)::text, 4, 3) AS bib_ute_ins,
    tb_soggetto.fl_canc
FROM tb_soggetto
WHERE (tb_soggetto.fl_canc <> 'S'::bpchar);

--
-- Definition for view vl_termini_termini (OID = 60016) : 
--
CREATE VIEW sbnweb.vl_termini_termini AS
SELECT dd.did_1, dd.tipo_coll, d.did, d.ds_termine_thesauro, d.tidx_vector,
    d.ky_termine_thesauro, d.tp_forma_the, d.cd_livello, d.cd_the,
    d.nota_termine_thesauro, d.ute_ins, d.ts_ins, d.ute_var, d.ts_var,
    d.fl_canc, d.fl_condiviso
FROM v_termini_termini dd, tb_termine_thesauro d
WHERE ((dd.did_2 = d.did) AND (d.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_thesauro_tit (OID = 60020) : 
--
CREATE VIEW sbnweb.vl_thesauro_tit AS
SELECT trs_termini_titoli_biblioteche.bid,
    trs_termini_titoli_biblioteche.cd_polo,
    trs_termini_titoli_biblioteche.cd_biblioteca, tb_termine_thesauro.did,
    tb_termine_thesauro.cd_livello, tb_termine_thesauro.cd_the,
    tb_termine_thesauro.ds_termine_thesauro,
    tb_termine_thesauro.tidx_vector,
    tb_termine_thesauro.ky_termine_thesauro, tb_termine_thesauro.ute_ins,
    tb_termine_thesauro.ts_ins, tb_termine_thesauro.ute_var,
    tb_termine_thesauro.ts_var, tb_termine_thesauro.fl_canc,
    tb_termine_thesauro.fl_condiviso,
    trs_termini_titoli_biblioteche.nota_termine_titoli_biblioteca
FROM trs_termini_titoli_biblioteche, tb_termine_thesauro
WHERE (((trs_termini_titoli_biblioteche.did = tb_termine_thesauro.did) AND
    (trs_termini_titoli_biblioteche.fl_canc <> 'S'::bpchar)) AND
    (tb_termine_thesauro.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_the_tit_bib (OID = 60024) : 
--
CREATE VIEW sbnweb.vl_the_tit_bib AS
SELECT the.did, tit.bid, the.cd_the, tit_bib.cd_polo, tit_bib.cd_biblioteca
FROM vl_titolo_the tit, vl_thesauro_tit the, tr_tit_bib tit_bib,
    tr_thesauri_biblioteche the_bib
WHERE ((((((((((((the.did = tit.did) AND (the.bid = tit.bid)) AND
    (the.cd_polo = tit.cd_polo)) AND (the.cd_biblioteca =
    tit.cd_biblioteca)) AND (tit.bid = tit_bib.bid)) AND (the.cd_the =
    the_bib.cd_the)) AND (tit_bib.cd_polo = the_bib.cd_polo)) AND
    (tit_bib.cd_biblioteca = the_bib.cd_biblioteca)) AND
    ((the_bib.fl_att)::text = (1)::text)) AND ((the_bib.fl_auto_loc)::text
    = (1)::text)) AND (the_bib.fl_canc <> 'S'::bpchar)) AND
    (tit_bib.fl_canc <> 'S'::bpchar))
UNION
SELECT the.did, tit.bid, the.cd_the, tit.cd_polo, tit.cd_biblioteca
FROM vl_titolo_the tit, vl_thesauro_tit the
WHERE ((((the.did = tit.did) AND (the.bid = tit.bid)) AND (the.cd_polo =
    tit.cd_polo)) AND (the.cd_biblioteca = tit.cd_biblioteca));

--
-- Definition for view vl_titolo_bib (OID = 60029) : 
--
CREATE VIEW sbnweb.vl_titolo_bib AS
SELECT tb.cd_polo, tb.cd_biblioteca, tb.fl_mutilo, tb.ds_consistenza,
    tb.fl_possesso, tb.fl_gestione, tb.fl_disp_elettr, tb.fl_allinea,
    tb.fl_allinea_sbnmarc, tb.fl_allinea_cla, tb.fl_allinea_sog,
    tb.fl_allinea_luo, tb.fl_allinea_rep, tb.ds_fondo, tb.ds_segn,
    tb.ds_antica_segn, tb.nota_tit_bib, tb.uri_copia, tb.tp_digitalizz,
    t.bid, t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura,
    t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1,
    t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3,
    t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t,
    t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit, t.nota_cat_tit,
    t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var,
    t.ute_forza_ins, t.ute_forza_var, t.fl_canc, t.fl_condiviso,
    t.ts_condiviso, t.ute_condiviso
FROM tr_tit_bib tb, tb_titolo t
WHERE (((tb.bid = t.bid) AND (tb.fl_canc <> 'S'::bpchar)) AND (t.fl_canc <>
    'S'::bpchar));

--
-- Definition for view vl_titolo_imp (OID = 60034) : 
--
CREATE VIEW sbnweb.vl_titolo_imp AS
SELECT i.bid, i.impronta_1, i.impronta_2, i.impronta_3, i.nota_impronta,
    t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura, t.cd_paese,
    t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1, t.aa_pubb_2,
    t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3,
    t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t,
    t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_cat_tit, t.nota_inf_tit,
    t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var,
    t.ute_forza_ins, t.ute_forza_var, t.fl_canc, t.fl_condiviso,
    t.ts_condiviso, t.ute_condiviso
FROM tb_impronta i, tb_titolo t
WHERE (((t.bid = i.bid) AND (i.fl_canc <> 'S'::bpchar)) AND (t.fl_canc <>
    'S'::bpchar));

--
-- Definition for view vl_titolo_stat (OID = 60039) : 
--
CREATE VIEW sbnweb.vl_titolo_stat AS
SELECT tb_titolo.bid, tb_titolo.tp_materiale, tb_titolo.tp_record_uni,
    tb_titolo.cd_natura, tb_titolo.cd_paese, tb_titolo.cd_lingua_1,
    tb_titolo.aa_pubb_1, tb_titolo.cd_genere_1, tb_titolo.cd_livello,
    "substring"((tb_titolo.ute_ins)::text, 4, 3) AS bib_ute_ins,
    tb_titolo.fl_canc, tb_titolo.fl_condiviso
FROM tb_titolo
WHERE (tb_titolo.fl_canc <> 'S'::bpchar);

--
-- Definition for view vl_titolo_tit_b (OID = 60043) : 
--
CREATE VIEW sbnweb.vl_titolo_tit_b AS
SELECT tr_tit_tit.bid_base, tr_tit_tit.tp_legame,
    tr_tit_tit.tp_legame_musica, tr_tit_tit.cd_natura_base,
    tr_tit_tit.cd_natura_coll, tr_tit_tit.sequenza,
    tr_tit_tit.nota_tit_tit, tr_tit_tit.sequenza_musica, tr_tit_tit.sici,
    tr_tit_tit.fl_condiviso AS fl_condiviso_legame, tr_tit_tit.ts_condiviso
    AS ts_condiviso_legame, tr_tit_tit.ute_condiviso AS
    ute_condiviso_legame, tb_titolo.bid, tb_titolo.isadn,
    tb_titolo.tp_materiale, tb_titolo.tp_record_uni, tb_titolo.cd_natura,
    tb_titolo.cd_paese, tb_titolo.cd_lingua_1, tb_titolo.cd_lingua_2,
    tb_titolo.cd_lingua_3, tb_titolo.aa_pubb_1, tb_titolo.aa_pubb_2,
    tb_titolo.tp_aa_pubb, tb_titolo.cd_genere_1, tb_titolo.cd_genere_2,
    tb_titolo.cd_genere_3, tb_titolo.cd_genere_4, tb_titolo.ky_cles1_t,
    tb_titolo.ky_cles2_t, tb_titolo.ky_clet1_t, tb_titolo.ky_clet2_t,
    tb_titolo.ky_cles1_ct, tb_titolo.ky_cles2_ct, tb_titolo.ky_clet1_ct,
    tb_titolo.ky_clet2_ct, tb_titolo.cd_livello, tb_titolo.fl_speciale,
    tb_titolo.isbd, tb_titolo.indice_isbd, tb_titolo.ky_editore,
    tb_titolo.cd_agenzia, tb_titolo.cd_norme_cat, tb_titolo.nota_cat_tit,
    tb_titolo.nota_inf_tit, tb_titolo.bid_link, tb_titolo.tp_link,
    tb_titolo.ute_ins, tb_titolo.ts_ins, tb_titolo.ute_var,
    tb_titolo.ts_var, tb_titolo.ute_forza_ins, tb_titolo.ute_forza_var,
    tb_titolo.fl_canc, tb_titolo.fl_condiviso, tb_titolo.ts_condiviso,
    tb_titolo.ute_condiviso
FROM tr_tit_tit, tb_titolo
WHERE (((tr_tit_tit.bid_coll = tb_titolo.bid) AND (tr_tit_tit.fl_canc <>
    'S'::bpchar)) AND (tb_titolo.fl_canc <> 'S'::bpchar));

--
-- Definition for sequence seq_cod_utente_lettore (OID = 60055) : 
--
CREATE SEQUENCE sbnweb.seq_cod_utente_lettore
    INCREMENT BY 1
    MAXVALUE 9999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_e_tb_titolo (OID = 60057) : 
--
CREATE SEQUENCE sbnweb.seq_e_tb_titolo
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_fid_fascicolo (OID = 60059) : 
--
CREATE SEQUENCE sbnweb.seq_fid_fascicolo
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_id_progressivi_batch (OID = 60061) : 
--
CREATE SEQUENCE sbnweb.seq_id_progressivi_batch
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_isadn_autore (OID = 60063) : 
--
CREATE SEQUENCE sbnweb.seq_isadn_autore
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_msm_tb_possessore (OID = 60065) : 
--
CREATE SEQUENCE sbnweb.seq_msm_tb_possessore
    INCREMENT BY 1
    MAXVALUE 999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tb_autore (OID = 60067) : 
--
CREATE SEQUENCE sbnweb.seq_tb_autore
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tb_descrittore (OID = 60069) : 
--
CREATE SEQUENCE sbnweb.seq_tb_descrittore
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tb_luogo (OID = 60071) : 
--
CREATE SEQUENCE sbnweb.seq_tb_luogo
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tb_marca (OID = 60073) : 
--
CREATE SEQUENCE sbnweb.seq_tb_marca
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tb_script (OID = 60075) : 
--
CREATE SEQUENCE sbnweb.seq_tb_script
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tb_soggetto (OID = 60077) : 
--
CREATE SEQUENCE sbnweb.seq_tb_soggetto
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tb_termine_thesauro (OID = 60079) : 
--
CREATE SEQUENCE sbnweb.seq_tb_termine_thesauro
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tb_tim (OID = 60081) : 
--
CREATE SEQUENCE sbnweb.seq_tb_tim
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tb_titolo (OID = 60083) : 
--
CREATE SEQUENCE sbnweb.seq_tb_titolo
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_ts_link_multim (OID = 60085) : 
--
CREATE SEQUENCE sbnweb.seq_ts_link_multim
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence tbf_batch_id_batch_seq (OID = 60087) : 
--
CREATE SEQUENCE sbnweb.tbf_batch_id_batch_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence users_tab_id_user_seq (OID = 60091) : 
--
CREATE SEQUENCE sbnweb.users_tab_id_user_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for view vl_descrittore_sog (OID = 184805) : 
--
CREATE VIEW sbnweb.vl_descrittore_sog AS
SELECT sd.cid, sd.fl_primavoce, sd.fl_posizione, d.did, d.ds_descrittore,
    d.tidx_vector, d.tp_forma_des, d.ky_norm_descritt, d.cd_livello,
    d.cd_soggettario, d.nota_descrittore, d.ute_ins, d.ts_ins, d.ute_var,
    d.ts_var, d.fl_canc, d.fl_condiviso, d.cd_edizione, d.cat_termine
FROM tr_sog_des sd, tb_descrittore d
WHERE (((sd.did = d.did) AND (sd.fl_canc <> 'S'::bpchar)) AND (d.fl_canc <>
    'S'::bpchar));

--
-- Definition for view vl_soggetto_des (OID = 184809) : 
--
CREATE VIEW sbnweb.vl_soggetto_des AS
SELECT tr_sog_des.did, tr_sog_des.fl_primavoce, tb_soggetto.cid,
    tb_soggetto.cd_livello, tb_soggetto.cd_soggettario,
    tb_soggetto.cd_edizione, tb_soggetto.fl_speciale,
    tb_soggetto.ds_soggetto, tb_soggetto.tidx_vector,
    tb_soggetto.ky_cles1_s, tb_soggetto.ky_cles2_s, tb_soggetto.ute_ins,
    tb_soggetto.ts_ins, tb_soggetto.ute_var, tb_soggetto.ts_var,
    tb_soggetto.fl_canc, tb_soggetto.fl_condiviso,
    tb_soggetto.ts_condiviso, tb_soggetto.ute_condiviso
FROM tr_sog_des, tb_soggetto
WHERE (((tr_sog_des.cid = tb_soggetto.cid) AND (tr_sog_des.fl_canc <>
    'S'::bpchar)) AND (tb_soggetto.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_descrittore_des (OID = 184813) : 
--
CREATE VIEW sbnweb.vl_descrittore_des AS
SELECT dd.did_1, dd.tp_legame, d.did, d.ds_descrittore, d.tidx_vector,
    d.ky_norm_descritt, d.tp_forma_des, d.cd_livello, d.cd_soggettario,
    d.nota_descrittore, d.ute_ins, d.ts_ins, d.ute_var, d.ts_var,
    d.fl_canc, d.fl_condiviso, d.cd_edizione, d.cat_termine
FROM v_des_des dd, tb_descrittore d
WHERE ((dd.did_2 = d.did) AND (d.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_des_sog_freq2 (OID = 184817) : 
--
CREATE VIEW sbnweb.vl_des_sog_freq2 AS
SELECT a.did AS d1, a.did_1 AS d2, b.did AS d3, b.cid
FROM vl_descrittore_des a, vl_soggetto_des b
WHERE (a.did = b.did);

--
-- Definition for view vl_soggetto_tit (OID = 184841) : 
--
CREATE VIEW sbnweb.vl_soggetto_tit AS
SELECT tr_tit_sog_bib.bid, tr_tit_sog_bib.cd_polo,
    tr_tit_sog_bib.cd_biblioteca, tb_soggetto.cid, tb_soggetto.cd_livello,
    tb_soggetto.cd_soggettario, tb_soggetto.cd_edizione,
    tb_soggetto.fl_speciale, tb_soggetto.ds_soggetto, tb_soggetto.cat_sogg,
    tb_soggetto.nota_soggetto, tb_soggetto.tidx_vector,
    tb_soggetto.ky_cles1_s, tb_soggetto.ky_cles2_s, tb_soggetto.ute_ins,
    tb_soggetto.ts_ins, tb_soggetto.ute_var, tb_soggetto.ts_var,
    tb_soggetto.fl_canc, tb_soggetto.fl_condiviso,
    tb_soggetto.ts_condiviso, tb_soggetto.ute_condiviso,
    tr_tit_sog_bib.nota_tit_sog_bib, tr_tit_sog_bib.posizione
FROM tr_tit_sog_bib, tb_soggetto
WHERE (((tr_tit_sog_bib.cid = tb_soggetto.cid) AND (tr_tit_sog_bib.fl_canc
    <> 'S'::bpchar)) AND (tb_soggetto.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_sog_tit_bib (OID = 184845) : 
--
CREATE VIEW sbnweb.vl_sog_tit_bib AS
SELECT sog.cid, tit.bid, sog.cd_soggettario, sog.cd_edizione,
    tit_bib.cd_polo, tit_bib.cd_biblioteca
FROM vl_titolo_sog tit, vl_soggetto_tit sog, tr_tit_bib tit_bib,
    tr_soggettari_biblioteche sog_bib
WHERE ((((((((((((sog.cid = tit.cid) AND (sog.bid = tit.bid)) AND
    (sog.cd_polo = tit.cd_polo)) AND (sog.cd_biblioteca =
    tit.cd_biblioteca)) AND (tit.bid = tit_bib.bid)) AND
    (sog.cd_soggettario = sog_bib.cd_sogg)) AND (tit_bib.cd_polo =
    sog_bib.cd_polo)) AND (tit_bib.cd_biblioteca = sog_bib.cd_biblioteca))
    AND (sog_bib.fl_att = '1'::bpchar)) AND (sog_bib.fl_auto_loc =
    '1'::bpchar)) AND (sog_bib.fl_canc <> 'S'::bpchar)) AND
    (tit_bib.fl_canc <> 'S'::bpchar))
UNION
SELECT sog.cid, tit.bid, sog.cd_soggettario, sog.cd_edizione, tit.cd_polo,
    tit.cd_biblioteca
FROM vl_titolo_sog tit, vl_soggetto_tit sog
WHERE ((((sog.cid = tit.cid) AND (sog.bid = tit.bid)) AND (sog.cd_polo =
    tit.cd_polo)) AND (sog.cd_biblioteca = tit.cd_biblioteca));

--
-- Structure for table import1 (OID = 186472) : 
--
CREATE TABLE sbnweb.import1 (
    id serial NOT NULL,
    id_input varchar(30),
    leader varchar(24),
    tag varchar(3),
    indicatore1 char(1),
    indicatore2 char(1),
    id_link varchar(30),
    dati text,
    nr_richiesta numeric,
    stato_id_input char(1),
    stato_tag char(1),
    id_batch numeric
) WITH OIDS;
--
-- Structure for table import950 (OID = 186484) : 
--
CREATE TABLE sbnweb.import950 (
    id SERIAL,
    id_import1 integer NOT NULL,
    id_input varchar(30),
    dati text,
    error numeric(1,0) NOT NULL,
    msg_error text,
    nr_richiesta_import numeric NOT NULL,
    nr_richiesta numeric NOT NULL
) WITH OIDS;
--
-- Structure for table import_id_link (OID = 186495) : 
--
CREATE TABLE sbnweb.import_id_link (
    id_record serial NOT NULL,
    id_input varchar(300),
    id_inserito varchar(50),
    fl_stato char(1),
    ute_ins varchar(12),
    ts_ins timestamp(6) without time zone,
    nr_richiesta numeric
) WITHOUT OIDS;
--
-- Structure for table tb_report_indice (OID = 186505) : 
--
CREATE TABLE sbnweb.tb_report_indice (
    id serial NOT NULL,
    nome_lista varchar(50) NOT NULL,
    fl_canc char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone DEFAULT now() NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone DEFAULT now() NOT NULL,
    data_prod_lista char(10)
) WITHOUT OIDS;
--
-- Structure for table tb_report_indice_id_locali (OID = 186515) : 
--
CREATE TABLE sbnweb.tb_report_indice_id_locali (
    id serial NOT NULL,
    id_lista integer,
    id_oggetto_locale char(10),
    risultato_confronto char(1),
    stato_lavorazione char(1),
    tipo_trattamento_selezionato char(1),
    id_arrivo_fusione char(10),
    fl_canc char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone DEFAULT now() NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone DEFAULT now() NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_report_indice_id_arrivo (OID = 186531) : 
--
CREATE TABLE sbnweb.tb_report_indice_id_arrivo (
    id serial NOT NULL,
    id_lista_ogg_loc integer NOT NULL,
    id_oggetto_locale char(10),
    id_arrivo_fusione char(10),
    tipologia_uguaglianza char(1),
    fl_canc char(1) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone DEFAULT now() NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone DEFAULT now() NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbp_modello_previsionale (OID = 186546) : 
--
CREATE TABLE sbnweb.tbp_modello_previsionale (
    id_modello serial NOT NULL,
    nome_modello varchar(50) NOT NULL,
    descrizione varchar(255),
    xml_model text NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp without time zone NOT NULL,
    fl_canc char(1) DEFAULT 'N'::bpchar NOT NULL
) WITH OIDS;
--
-- Definition for view vc_inventario_coll (OID = 186642) : 
--
CREATE VIEW sbnweb.vc_inventario_coll AS
SELECT tbc_inventario.cd_polo, tbc_inventario.cd_bib,
    tbc_inventario.cd_serie, tbc_inventario.cd_inven,
    tbc_inventario.cd_proven, tbc_inventario.key_loc, tbc_inventario.bid,
    tbc_inventario.cd_polo_proven, tbc_inventario.cd_biblioteca_proven,
    tbc_inventario.key_loc_old, tbc_inventario.seq_coll,
    tbc_inventario.precis_inv, tbc_inventario.cd_sit,
    tbc_inventario.valore, tbc_inventario.importo, tbc_inventario.num_vol,
    tbc_inventario.tot_loc, tbc_inventario.tot_inter,
    tbc_inventario.anno_abb, tbc_inventario.flg_disp,
    tbc_inventario.flg_nuovo_usato, tbc_inventario.stato_con,
    tbc_inventario.cd_fornitore, tbc_inventario.cd_mat_inv,
    tbc_inventario.cd_bib_ord, tbc_inventario.cd_tip_ord,
    tbc_inventario.anno_ord, tbc_inventario.cd_ord,
    tbc_inventario.riga_ord, tbc_inventario.cd_bib_fatt,
    tbc_inventario.anno_fattura, tbc_inventario.prg_fattura,
    tbc_inventario.riga_fattura, tbc_inventario.cd_no_disp,
    tbc_inventario.cd_frui, tbc_inventario.cd_carico,
    tbc_inventario.num_carico, tbc_inventario.data_carico,
    tbc_inventario.cd_polo_scar, tbc_inventario.cd_bib_scar,
    tbc_inventario.cd_scarico, tbc_inventario.num_scarico,
    tbc_inventario.data_scarico, tbc_inventario.data_delibera,
    tbc_inventario.delibera_scar, tbc_inventario.sez_old,
    tbc_inventario.loc_old, tbc_inventario.spec_old,
    tbc_inventario.cd_supporto, tbc_inventario.ute_ins_prima_coll,
    tbc_inventario.ts_ins_prima_coll, tbc_inventario.ute_ins,
    tbc_inventario.ts_ins, tbc_inventario.ute_var, tbc_inventario.ts_var,
    tbc_inventario.fl_canc, tbc_inventario.tipo_acquisizione,
    tbc_inventario.supporto_copia, tbc_inventario.digitalizzazione,
    tbc_inventario.disp_copia_digitale, tbc_inventario.id_accesso_remoto,
    tbc_inventario.rif_teca_digitale, tbc_inventario.cd_riproducibilita,
    tbc_inventario.data_ingresso, tbc_inventario.data_redisp_prev,
    tbc_inventario.id_bib_scar, tbc_collocazione.cd_sez,
    tbc_collocazione.cd_loc, tbc_collocazione.spec_loc,
    tbc_collocazione.ord_loc, tbc_collocazione.ord_spec,
    tbc_collocazione.bid AS bid_coll, tbc_collocazione.tot_inv
FROM (tbc_inventario JOIN tbc_collocazione ON (((tbc_inventario.key_loc =
    tbc_collocazione.key_loc) AND (tbc_collocazione.fl_canc <> 'S'::bpchar))));

--
-- Structure for table tra_ordine_carrello_spedizione (OID = 265972) : 
--
CREATE TABLE sbnweb.tra_ordine_carrello_spedizione (
    id_ordine integer NOT NULL,
    dt_spedizione date NOT NULL,
    prg_spedizione smallint NOT NULL,
    cart_name varchar(255),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_editore_titolo (OID = 407865) : 
--
CREATE TABLE sbnweb.tr_editore_titolo (
    cod_fornitore integer NOT NULL,
    bid char(10) NOT NULL,
    nota_legame varchar(255),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbr_editore_num_std (OID = 407880) : 
--
CREATE TABLE sbnweb.tbr_editore_num_std (
    cod_fornitore integer NOT NULL,
    numero_std char(10) NOT NULL,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for view vl_richiesta_servizio (OID = 528307) : 
--
CREATE VIEW sbnweb.vl_richiesta_servizio AS
SELECT ts.cd_polo, ts.cd_bib, rs.cod_rich_serv, rs.cd_polo_inv,
    rs.cod_bib_inv, rs.cod_serie_inv, rs.cod_inven_inv,
    rs.id_documenti_lettore, rs.id_esemplare_documenti_lettore,
    rs.id_utenti_biblioteca, rs.id_modalita_pagamento,
    rs.id_supporti_biblioteca, rs.id_servizio, rs.id_iter_servizio,
    rs.fl_tipo_rec, rs.note_bibliotecario, rs.costo_servizio, rs.num_fasc,
    rs.num_volume, rs.anno_period, rs.cod_tipo_serv_rich,
    rs.cod_tipo_serv_alt, rs.cod_stato_rich, rs.cod_stato_mov,
    rs.data_in_eff, rs.data_fine_eff, rs.num_rinnovi, rs.data_richiesta,
    rs.num_pezzi, rs.note_ut, rs.prezzo_max, rs.data_massima,
    rs.data_proroga, rs.data_in_prev, rs.data_fine_prev, rs.fl_svolg,
    rs.copyright, rs.cod_erog, rs.cod_risp, rs.fl_condiz, rs.fl_inoltro,
    rs.cod_bib_dest, rs.cod_bib_prelievo, rs.cod_bib_restituzione,
    rs.ute_ins, rs.ts_ins, rs.ute_var, rs.ts_var, rs.fl_canc,
    ts.cod_tipo_serv, iter.progr_iter, iter.cod_attivita,
    (rs.id_documenti_lettore IS NULL) AS fl_inventario, CASE WHEN
    (rs.id_documenti_lettore IS NULL) THEN t.isbd ELSE dl.titolo END AS
    isbd, t.indice_isbd, CASE WHEN (rs.id_documenti_lettore IS NULL) THEN
    t.cd_natura ELSE dl.natura END AS cd_natura, CASE WHEN
    (rs.id_documenti_lettore IS NULL) THEN ((t.ky_cles1_t)::text ||
    (t.ky_cles2_t)::text) ELSE NULL::text END AS kcles, CASE WHEN
    (rs.id_documenti_lettore IS NULL) THEN t.bid ELSE dl.bid END AS bid,
    dl.cd_bib AS bib_doc_lett, dl.tipo_doc_lett, dl.cod_doc_lett,
    dl.segnatura, c.cd_sez, c.cd_loc, c.spec_loc, i.seq_coll, i.anno_abb,
    u.cognome, u.nome, sol.progr_sollecito, sol.data_invio, rs.intervallo_copia
FROM (((((((((tbl_richiesta_servizio rs JOIN tbl_iter_servizio iter ON
    ((iter.id_iter_servizio = rs.id_iter_servizio))) JOIN tbl_tipo_servizio
    ts ON ((ts.id_tipo_servizio = iter.id_tipo_servizio))) JOIN
    trl_utenti_biblioteca ub ON ((ub.id_utenti_biblioteca =
    rs.id_utenti_biblioteca))) JOIN tbl_utenti u ON ((u.id_utenti =
    ub.id_utenti))) LEFT JOIN tbc_inventario i ON (((((i.cd_polo =
    rs.cd_polo_inv) AND (i.cd_bib = rs.cod_bib_inv)) AND (i.cd_serie =
    rs.cod_serie_inv)) AND (i.cd_inven = rs.cod_inven_inv)))) LEFT JOIN
    tb_titolo t ON ((t.bid = i.bid))) LEFT JOIN tbc_collocazione c ON
    ((c.key_loc = i.key_loc))) LEFT JOIN tbl_documenti_lettori dl ON
    ((dl.id_documenti_lettore = rs.id_documenti_lettore))) LEFT JOIN (
    SELECT s.cod_rich_serv, s.progr_sollecito, s.data_invio
    FROM tbl_solleciti s
    WHERE ((s.esito = 'S'::bpchar) AND (s.fl_canc <> 'S'::bpchar))
    ORDER BY s.data_invio DESC
    ) sol ON ((sol.cod_rich_serv = rs.cod_rich_serv)));

--
-- Structure for table tb_loc_indice (OID = 528322) : 
--
CREATE TABLE sbnweb.tb_loc_indice (
    id_loc serial NOT NULL,
    cd_polo char(3) NOT NULL,
    cd_biblioteca char(3) NOT NULL,
    bid char(10) NOT NULL,
    fl_stato char(1) DEFAULT '0'::bpchar NOT NULL,
    tp_loc smallint DEFAULT 0 NOT NULL,
    sbnmarc_xml text,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for view vl_titolo_num_std (OID = 600101) : 
--
CREATE VIEW sbnweb.vl_titolo_num_std AS
SELECT tb_numero_std.bid, tb_numero_std.tp_numero_std,
    tb_numero_std.numero_std, tb_numero_std.numero_lastra,
    tb_numero_std.cd_paese AS cd_paese_std, tb_numero_std.nota_numero_std,
    tb_titolo.isadn, tb_titolo.tp_materiale, tb_titolo.tp_record_uni,
    tb_titolo.cd_natura, tb_titolo.cd_paese, tb_titolo.cd_lingua_1,
    tb_titolo.cd_lingua_2, tb_titolo.cd_lingua_3, tb_titolo.aa_pubb_1,
    tb_titolo.aa_pubb_2, tb_titolo.tp_aa_pubb, tb_titolo.cd_genere_1,
    tb_titolo.cd_genere_2, tb_titolo.cd_genere_3, tb_titolo.cd_genere_4,
    tb_titolo.ky_cles1_t, tb_titolo.ky_cles2_t, tb_titolo.ky_clet1_t,
    tb_titolo.ky_clet2_t, tb_titolo.ky_cles1_ct, tb_titolo.ky_cles2_ct,
    tb_titolo.ky_clet1_ct, tb_titolo.ky_clet2_ct, tb_titolo.cd_livello,
    tb_titolo.fl_speciale, tb_titolo.isbd, tb_titolo.indice_isbd,
    tb_titolo.ky_editore, tb_titolo.cd_agenzia, tb_titolo.cd_norme_cat,
    tb_titolo.nota_cat_tit, tb_titolo.nota_inf_tit, tb_titolo.bid_link,
    tb_titolo.tp_link, tb_titolo.ute_ins, tb_titolo.ts_ins,
    tb_titolo.ute_var, tb_titolo.ts_var, tb_titolo.ute_forza_ins,
    tb_titolo.ute_forza_var, tb_titolo.fl_canc
FROM tb_numero_std, tb_titolo
WHERE (((tb_numero_std.bid = tb_titolo.bid) AND (tb_numero_std.fl_canc <>
    'S'::bpchar)) AND (tb_titolo.fl_canc <> 'S'::bpchar));

--
-- Definition for view v_catalogo_editoria (OID = 600111) : 
--
CREATE VIEW sbnweb.v_catalogo_editoria AS
SELECT tit.bid, tit.isbd, tit.indice_isbd, tit.tp_aa_pubb, tit.aa_pubb_1,
    tit.aa_pubb_2, tit.cd_lingua_1, tit.cd_lingua_2, tit.cd_lingua_3,
    tit.cd_natura, tit.tp_record_uni, tit.ky_cles1_t, tit.ky_cles2_t,
    tit.fl_condiviso, nst.numero_std AS isbn, ff.cod_fornitore,
    ff.nom_fornitore, ff.paese, cod.cd_flg2 AS cod_regione, cod.cd_flg3 AS
    ds_regione, ff.provincia, cod.ds_tabella AS ds_provincia, ff.cap,
    ff.citta, ff.chiave_for, ens.numero_std AS isbn_editore,
    '[Legame implicito non modificabile]' AS nota_legame
FROM ((((tb_numero_std nst JOIN tb_titolo tit ON (((tit.bid = nst.bid) AND
    (NOT (tit.fl_canc = 'S'::bpchar))))) JOIN tbr_editore_num_std ens ON
    (((regexp_replace((ens.numero_std)::text, '^978'::text, ''::text) =
    "substring"(regexp_replace((nst.numero_std)::text, '^978'::text,
    ''::text), 1, length(regexp_replace((ens.numero_std)::text,
    '^978'::text, ''::text)))) AND (NOT (ens.fl_canc = 'S'::bpchar)))))
    JOIN tbr_fornitori ff ON (((ff.cod_fornitore = ens.cod_fornitore) AND
    (NOT (ff.fl_canc = 'S'::bpchar))))) JOIN tb_codici cod ON
    (((cod.tp_tabella = 'RPRV'::bpchar) AND (cod.cd_tabella = ff.provincia))))
WHERE ((((NOT (nst.fl_canc = 'S'::bpchar)) AND (nst.tp_numero_std =
    'I'::bpchar)) AND (NOT (tit.fl_canc = 'S'::bpchar))) AND (NOT
    (ff.fl_canc = 'S'::bpchar)))
UNION
SELECT tit.bid, tit.isbd, tit.indice_isbd, tit.tp_aa_pubb, tit.aa_pubb_1,
    tit.aa_pubb_2, tit.cd_lingua_1, tit.cd_lingua_2, tit.cd_lingua_3,
    tit.cd_natura, tit.tp_record_uni, tit.ky_cles1_t, tit.ky_cles2_t,
    tit.fl_condiviso, '' AS isbn, ff.cod_fornitore, ff.nom_fornitore,
    ff.paese, cod.cd_tabella AS cod_regione, cod.ds_tabella AS ds_regione,
    ff.provincia, ' ' AS ds_provincia, ff.cap, ff.citta, ff.chiave_for, ''
    AS isbn_editore, edt.nota_legame
FROM (((tr_editore_titolo edt JOIN tb_titolo tit ON ((tit.bid = edt.bid)))
    JOIN tbr_fornitori ff ON ((edt.cod_fornitore = ff.cod_fornitore))) JOIN
    tb_codici cod ON (((cod.tp_tabella = 'RREG'::bpchar) AND
    (cod.cd_tabella = ff.regione))))
WHERE (((NOT (edt.fl_canc = 'S'::bpchar)) AND (NOT (tit.fl_canc =
    'S'::bpchar))) AND (NOT (ff.fl_canc = 'S'::bpchar)));

--
-- Structure for table tb_titset_1 (OID = 626295) : 
--
CREATE TABLE sbnweb.tb_titset_1 (
    bid char(10) NOT NULL,
    s105_tp_testo_letterario char(1),
    s125_indicatore_testo char(1),
    s140_tp_testo_letterario char(2),
    s181_tp_forma_contenuto_1 char(1),
    s181_cd_tipo_contenuto_1 char(1),
    s181_cd_movimento_1 char(1),
    s181_cd_dimensione_1 char(1),
    s181_cd_sensoriale_1_1 char(1),
    s181_cd_sensoriale_2_1 char(1),
    s181_cd_sensoriale_3_1 char(1),
    s181_tp_forma_contenuto_2 char(1),
    s181_cd_tipo_contenuto_2 char(1),
    s181_cd_movimento_2 char(1),
    s181_cd_dimensione_2 char(1),
    s181_cd_sensoriale_1_2 char(1),
    s181_cd_sensoriale_2_2 char(1),
    s181_cd_sensoriale_3_2 char(1),
    s182_tp_mediazione_1 char(1),
    s182_tp_mediazione_2 char(1),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL,
	s183_tp_supporto_1  varchar(2),
	s183_tp_supporto_2  varchar(2),
	s210_ind_pubblicato CHAR(1)
) WITHOUT OIDS;

COMMENT ON COLUMN sbnweb.tb_titset_1.s210_ind_pubblicato IS 'indicatore di documento non pubblicato';

CREATE TABLE sbnweb.tb_titset_2 (
  bid CHAR(10) NOT NULL, 
  s231_forma_opera CHAR(6), 
  s231_data_opera VARCHAR(20), 
  s231_altre_caratteristiche VARCHAR(50), 
  ute_ins CHAR(12) NOT NULL, 
  ts_ins TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  ute_var CHAR(12) NOT NULL, 
  ts_var TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  fl_canc CHAR(1) NOT NULL, 
  CONSTRAINT pk_titset_2 PRIMARY KEY(bid)
) WITHOUT OIDS;

--
-- Structure for table tb_risorsa_elettr (OID = 626305) : 
--
CREATE TABLE sbnweb.tb_risorsa_elettr (
    bid char(10) NOT NULL,
    cd_livello char(2) NOT NULL,
    tp_risorsa char(1) NOT NULL,
    cd_designazione char(1),
    cd_colore char(1),
    cd_dimensione char(1),
    cd_suono char(1),
    cd_bit_immagine char(3),
    cd_formato_file char(1),
    cd_qualita char(1),
    cd_origine char(1),
    cd_compressione char(1),
    cd_riformattazione char(1),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_disco_sonoro (OID = 626315) : 
--
CREATE TABLE sbnweb.tb_disco_sonoro (
    bid char(10) NOT NULL,
    cd_livello char(2) NOT NULL,
    cd_forma char(1) NOT NULL,
    cd_velocita char(1),
    tp_suono char(1),
    cd_pista char(1),
    cd_dimensione char(1),
    cd_larg_nastro char(1),
    cd_configurazione char(1),
    cd_mater_accomp_1 char(1),
    cd_mater_accomp_2 char(1),
    cd_mater_accomp_3 char(1),
    cd_mater_accomp_4 char(1),
    cd_mater_accomp_5 char(1),
    cd_mater_accomp_6 char(1),
    cd_tecnica_regis char(1),
    cd_riproduzione char(1),
    tp_disco char(1),
    tp_materiale char(1),
    tp_taglio char(1),
    durata char(6),
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_audiovideo (OID = 626325) : 
--
CREATE TABLE sbnweb.tb_audiovideo (
    bid char(10) NOT NULL,
    cd_livello char(2) NOT NULL,
    tp_mater_audiovis char(1) NOT NULL,
    lunghezza integer,
    cd_colore char(1),
    cd_suono char(1),
    tp_media_suono char(1),
    cd_dimensione char(1),
    cd_forma_video char(1),
    cd_tecnica char(1),
    tp_formato_film char(1),
    cd_mat_accomp_1 char(1),
    cd_mat_accomp_2 char(1),
    cd_mat_accomp_3 char(1),
    cd_mat_accomp_4 char(1),
    cd_forma_regist char(1),
    tp_formato_video char(1),
    cd_materiale_base char(1),
    cd_supporto_second char(1),
    cd_broadcast char(1),
    tp_generazione char(1),
    cd_elementi char(1),
    cd_categ_colore char(1),
    cd_polarita char(1),
    cd_pellicola char(1),
    tp_suono char(1),
    tp_stampa_film char(1),
    cd_deteriore char(1),
    cd_completo char(1),
    dt_ispezione integer,
    ute_ins char(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var char(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc char(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for view v_elettronico : 
--
CREATE OR REPLACE VIEW sbnweb.v_elettronico
(
  cd_livello_e,
  tp_risorsa,
  cd_designazione,
  cd_colore,
  cd_dimensione,
  cd_suono,
  cd_bit_immagine,
  cd_formato_file,
  cd_qualita,
  cd_origine,
  cd_compressione,
  cd_riformattazione,
  bid,
  isadn,
  tp_materiale,
  tp_record_uni,
  cd_natura,
  cd_paese,
  cd_lingua_1,
  cd_lingua_2,
  cd_lingua_3,
  aa_pubb_1,
  aa_pubb_2,
  tp_aa_pubb,
  cd_genere_1,
  cd_genere_2,
  cd_genere_3,
  cd_genere_4,
  ky_cles1_t,
  ky_cles2_t,
  ky_clet1_t,
  ky_clet2_t,
  ky_cles1_ct,
  ky_cles2_ct,
  ky_clet1_ct,
  ky_clet2_ct,
  cd_livello,
  fl_speciale,
  isbd,
  indice_isbd,
  ky_editore,
  cd_agenzia,
  cd_norme_cat,
  nota_inf_tit,
  nota_cat_tit,
  bid_link,
  tp_link,
  ute_ins,
  ts_ins,
  ute_var,
  ts_var,
  ute_forza_ins,
  ute_forza_var,
  fl_canc,
  cd_periodicita,
  fl_condiviso,
  ute_condiviso,
  ts_condiviso
)
AS 
SELECT e.cd_livello, e.tp_risorsa, e.cd_designazione, e.cd_colore, e.cd_dimensione, 
e.cd_suono, e.cd_bit_immagine, e.cd_formato_file, e.cd_qualita, e.cd_origine, e.cd_compressione, 
e.cd_riformattazione, t.bid, t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura, t.cd_paese, 
t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, 
t.cd_genere_2, t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t, 
t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct, t.cd_livello, t.fl_speciale, t.isbd, 
t.indice_isbd, t.ky_editore, t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit, t.nota_cat_tit, t.bid_link, 
t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var, t.ute_forza_ins, t.ute_forza_var, t.fl_canc,
t.cd_periodicita, t.fl_condiviso, t.ute_condiviso, t.ts_condiviso
       FROM tb_risorsa_elettr e, tb_titolo t
       WHERE t.bid = e.bid AND
t.fl_canc <> 'S' AND
e.fl_canc <> 'S';
--
-- Definition for view vl_autore_tit (OID = 626350) : 
--
CREATE VIEW sbnweb.vl_autore_tit AS
SELECT tr_tit_aut.bid, tr_tit_aut.tp_responsabilita,
    tr_tit_aut.cd_relazione, tr_tit_aut.nota_tit_aut,
    tr_tit_aut.fl_incerto, tr_tit_aut.fl_superfluo,
    tr_tit_aut.cd_strumento_mus, tr_tit_aut.fl_condiviso AS
    fl_condiviso_legame, tr_tit_aut.ts_condiviso AS ts_condiviso_legame,
    tr_tit_aut.ute_condiviso AS ute_condiviso_legame, tb_autore.vid,
    tb_autore.isadn, tb_autore.tp_forma_aut, tb_autore.ky_cautun,
    tb_autore.ky_auteur, tb_autore.ky_el1_a, tb_autore.ky_el1_b,
    tb_autore.ky_el2_a, tb_autore.ky_el2_b, tb_autore.tp_nome_aut,
    tb_autore.ky_el3, tb_autore.ky_el4, tb_autore.ky_el5,
    tb_autore.ky_cles1_a, tb_autore.ky_cles2_a, tb_autore.cd_paese,
    tb_autore.cd_lingua, tb_autore.aa_nascita, tb_autore.aa_morte,
    tb_autore.cd_livello, tb_autore.fl_speciale, tb_autore.ds_nome_aut,
    tb_autore.tidx_vector, tb_autore.cd_agenzia, tb_autore.cd_norme_cat,
    tb_autore.nota_aut, tb_autore.nota_cat_aut, tb_autore.vid_link,
    tb_autore.ute_ins, tb_autore.ts_ins, tb_autore.ute_var,
    tb_autore.ts_var, tb_autore.ute_forza_ins, tb_autore.ute_forza_var,
    tb_autore.fl_canc, tb_autore.fl_condiviso, tb_autore.ts_condiviso,
    tb_autore.ute_condiviso
FROM tr_tit_aut, tb_autore
WHERE (((tr_tit_aut.vid = tb_autore.vid) AND (tr_tit_aut.fl_canc <>
    'S'::bpchar)) AND (tb_autore.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_titolo_aut_nstd (OID = 626355) : 
--
CREATE VIEW sbnweb.ve_titolo_aut_nstd AS
SELECT a.bid, a.tp_numero_std, a.numero_std, a.numero_lastra,
    a.cd_paese_std, a.nota_numero_std, a.isadn, a.tp_materiale,
    a.tp_record_uni, a.cd_natura, a.cd_paese, a.cd_lingua_1, a.cd_lingua_2,
    a.cd_lingua_3, a.aa_pubb_1, a.aa_pubb_2, a.tp_aa_pubb, a.cd_genere_1,
    a.cd_genere_2, a.cd_genere_3, a.cd_genere_4, a.ky_cles1_t,
    a.ky_cles2_t, a.ky_clet1_t, a.ky_clet2_t, a.ky_cles1_ct, a.ky_cles2_ct,
    a.ky_clet1_ct, a.ky_clet2_ct, a.cd_livello, a.fl_speciale, a.isbd,
    a.indice_isbd, a.ky_editore, a.cd_agenzia, a.cd_norme_cat,
    a.nota_cat_tit, a.nota_inf_tit, a.bid_link, a.tp_link, a.ute_ins,
    a.ts_ins, a.ute_var, a.ts_var, a.ute_forza_ins, a.ute_forza_var,
    a.fl_canc, b.ky_cles1_a, b.ky_cles2_a
FROM vl_titolo_num_std a, vl_autore_tit b
WHERE (a.bid = b.bid);

--
-- Definition for view ve_titolo_tit_c_aut (OID = 626360) : 
--
CREATE VIEW sbnweb.ve_titolo_tit_c_aut AS
SELECT tt.bid_coll, tt.tp_legame, tt.tp_legame_musica, tt.cd_natura_base,
    tt.cd_natura_coll, tt.sequenza, tt.nota_tit_tit, tt.sequenza_musica,
    tt.sici, tt.bid, tt.isadn, tt.tp_materiale, tt.tp_record_uni,
    tt.cd_natura, tt.cd_paese, tt.cd_lingua_1, tt.cd_lingua_2,
    tt.cd_lingua_3, tt.aa_pubb_1, tt.aa_pubb_2, tt.tp_aa_pubb,
    tt.cd_genere_1, tt.cd_genere_2, tt.cd_genere_3, tt.cd_genere_4,
    tt.ky_cles1_t, tt.ky_cles2_t, tt.ky_clet1_t, tt.ky_clet2_t,
    tt.ky_cles1_ct, tt.ky_cles2_ct, tt.ky_clet1_ct, tt.ky_clet2_ct,
    tt.cd_livello, tt.fl_speciale, tt.isbd, tt.indice_isbd, tt.ky_editore,
    tt.cd_agenzia, tt.cd_norme_cat, tt.nota_cat_tit, tt.nota_inf_tit,
    tt.bid_link, tt.tp_link, tt.ute_ins, tt.ts_ins, tt.ute_var, tt.ts_var,
    tt.ute_forza_ins, tt.ute_forza_var, tt.fl_canc, at.tp_responsabilita,
    at.cd_relazione, at.fl_superfluo, at.vid, at.ky_cautun, at.ky_auteur,
    at.ky_cles1_a, at.ky_cles2_a
FROM vl_titolo_tit_c tt, vl_autore_tit at
WHERE (tt.bid = at.bid);

--
-- Definition for view ve_titolo_the_aut (OID = 626365) : 
--
CREATE VIEW sbnweb.ve_titolo_the_aut AS
SELECT ts.did, ts.bid, ts.isadn, ts.tp_materiale, ts.tp_record_uni,
    ts.cd_natura, ts.cd_paese, ts.cd_lingua_1, ts.cd_lingua_2,
    ts.cd_lingua_3, ts.aa_pubb_1, ts.aa_pubb_2, ts.tp_aa_pubb,
    ts.cd_genere_1, ts.cd_genere_2, ts.cd_genere_3, ts.cd_genere_4,
    ts.ky_cles1_t, ts.ky_cles2_t, ts.ky_clet1_t, ts.ky_clet2_t,
    ts.ky_cles1_ct, ts.ky_cles2_ct, ts.ky_clet1_ct, ts.ky_clet2_ct,
    ts.cd_livello, ts.fl_speciale, ts.isbd, ts.indice_isbd, ts.ky_editore,
    ts.cd_agenzia, ts.cd_norme_cat, ts.nota_cat_tit, ts.nota_inf_tit,
    ts.bid_link, ts.tp_link, ts.ute_ins, ts.ts_ins, ts.ute_var, ts.ts_var,
    ts.ute_forza_ins, ts.ute_forza_var, ts.fl_canc, at.tp_responsabilita,
    at.cd_relazione, at.fl_superfluo, at.vid, at.ky_cautun, at.ky_auteur,
    at.ky_cles1_a, at.ky_cles2_a
FROM vl_titolo_the ts, vl_autore_tit at
WHERE (ts.bid = at.bid);

--
-- Definition for view ve_titolo_sog_aut (OID = 626370) : 
--
CREATE VIEW sbnweb.ve_titolo_sog_aut AS
SELECT ts.cid, ts.bid, ts.isadn, ts.tp_materiale, ts.tp_record_uni,
    ts.cd_natura, ts.cd_paese, ts.cd_lingua_1, ts.cd_lingua_2,
    ts.cd_lingua_3, ts.aa_pubb_1, ts.aa_pubb_2, ts.tp_aa_pubb,
    ts.cd_genere_1, ts.cd_genere_2, ts.cd_genere_3, ts.cd_genere_4,
    ts.ky_cles1_t, ts.ky_cles2_t, ts.ky_clet1_t, ts.ky_clet2_t,
    ts.ky_cles1_ct, ts.ky_cles2_ct, ts.ky_clet1_ct, ts.ky_clet2_ct,
    ts.cd_livello, ts.fl_speciale, ts.isbd, ts.indice_isbd, ts.ky_editore,
    ts.cd_agenzia, ts.cd_norme_cat, ts.nota_cat_tit, ts.nota_inf_tit,
    ts.bid_link, ts.tp_link, ts.ute_ins, ts.ts_ins, ts.ute_var, ts.ts_var,
    ts.ute_forza_ins, ts.ute_forza_var, ts.fl_canc, at.tp_responsabilita,
    at.cd_relazione, at.fl_superfluo, at.vid, at.ky_cautun, at.ky_auteur,
    at.ky_cles1_a, at.ky_cles2_a
FROM vl_titolo_sog ts, vl_autore_tit at
WHERE (ts.bid = at.bid);

--
-- Definition for view ve_titolo_mar_aut (OID = 626375) : 
--
CREATE VIEW sbnweb.ve_titolo_mar_aut AS
SELECT tm.mid, tm.nota_tit_mar, tm.bid, tm.isadn, tm.tp_materiale,
    tm.tp_record_uni, tm.cd_natura, tm.cd_paese, tm.cd_lingua_1,
    tm.cd_lingua_2, tm.cd_lingua_3, tm.aa_pubb_1, tm.aa_pubb_2,
    tm.tp_aa_pubb, tm.cd_genere_1, tm.cd_genere_2, tm.cd_genere_3,
    tm.cd_genere_4, tm.ky_cles1_t, tm.ky_cles2_t, tm.ky_clet1_t,
    tm.ky_clet2_t, tm.ky_cles1_ct, tm.ky_cles2_ct, tm.ky_clet1_ct,
    tm.ky_clet2_ct, tm.cd_livello, tm.fl_speciale, tm.isbd, tm.indice_isbd,
    tm.ky_editore, tm.cd_agenzia, tm.cd_norme_cat, tm.nota_cat_tit,
    tm.nota_inf_tit, tm.bid_link, tm.tp_link, tm.ute_ins, tm.ts_ins,
    tm.ute_var, tm.ts_var, tm.ute_forza_ins, tm.ute_forza_var, tm.fl_canc,
    at.tp_responsabilita, at.cd_relazione, at.fl_superfluo, at.vid,
    at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_titolo_mar tm, vl_autore_tit at
WHERE (tm.bid = at.bid);

--
-- Definition for view ve_titolo_luo_aut (OID = 626380) : 
--
CREATE VIEW sbnweb.ve_titolo_luo_aut AS
SELECT tl.lid, tl.tp_luogo, tl.nota_tit_luo, tl.bid, tl.isadn,
    tl.tp_materiale, tl.tp_record_uni, tl.cd_natura, tl.cd_paese,
    tl.cd_lingua_1, tl.cd_lingua_2, tl.cd_lingua_3, tl.aa_pubb_1,
    tl.aa_pubb_2, tl.tp_aa_pubb, tl.cd_genere_1, tl.cd_genere_2,
    tl.cd_genere_3, tl.cd_genere_4, tl.ky_cles1_t, tl.ky_cles2_t,
    tl.ky_clet1_t, tl.ky_clet2_t, tl.ky_cles1_ct, tl.ky_cles2_ct,
    tl.ky_clet1_ct, tl.ky_clet2_ct, tl.cd_livello, tl.fl_speciale, tl.isbd,
    tl.indice_isbd, tl.ky_editore, tl.cd_agenzia, tl.cd_norme_cat,
    tl.nota_cat_tit, tl.nota_inf_tit, tl.bid_link, tl.tp_link, tl.ute_ins,
    tl.ts_ins, tl.ute_var, tl.ts_var, tl.ute_forza_ins, tl.ute_forza_var,
    tl.fl_canc, at.tp_responsabilita, at.cd_relazione, at.fl_superfluo,
    at.vid, at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_titolo_luo tl, vl_autore_tit at
WHERE (tl.bid = at.bid);

--
-- Definition for view ve_titolo_cla_aut (OID = 626385) : 
--
CREATE VIEW sbnweb.ve_titolo_cla_aut AS
SELECT tc.cd_sistema, tc.cd_edizione, tc.classe, tc.bid, tc.isadn,
    tc.tp_materiale, tc.tp_record_uni, tc.cd_natura, tc.cd_paese,
    tc.cd_lingua_1, tc.cd_lingua_2, tc.cd_lingua_3, tc.aa_pubb_1,
    tc.aa_pubb_2, tc.tp_aa_pubb, tc.cd_genere_1, tc.cd_genere_2,
    tc.cd_genere_3, tc.cd_genere_4, tc.ky_cles1_t, tc.ky_cles2_t,
    tc.ky_clet1_t, tc.ky_clet2_t, tc.ky_cles1_ct, tc.ky_cles2_ct,
    tc.ky_clet1_ct, tc.ky_clet2_ct, tc.cd_livello, tc.fl_speciale, tc.isbd,
    tc.indice_isbd, tc.ky_editore, tc.cd_agenzia, tc.cd_norme_cat,
    tc.nota_cat_tit, tc.nota_inf_tit, tc.bid_link, tc.tp_link, tc.ute_ins,
    tc.ts_ins, tc.ute_var, tc.ts_var, tc.ute_forza_ins, tc.ute_forza_var,
    tc.fl_canc, at.tp_responsabilita, at.cd_relazione, at.fl_superfluo,
    at.vid, at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_titolo_cla tc, vl_autore_tit at
WHERE (tc.bid = at.bid);

--
-- Definition for view vl_titolo_aut (OID = 626390) : 
--
CREATE VIEW sbnweb.vl_titolo_aut AS
SELECT tr_tit_aut.vid, tr_tit_aut.tp_responsabilita,
    tr_tit_aut.cd_relazione, tr_tit_aut.nota_tit_aut,
    tr_tit_aut.fl_incerto, tr_tit_aut.fl_superfluo,
    tr_tit_aut.cd_strumento_mus, tr_tit_aut.fl_condiviso AS
    fl_condiviso_legame, tr_tit_aut.ts_condiviso AS ts_condiviso_legame,
    tr_tit_aut.ute_condiviso AS ute_condiviso_legame, tb_titolo.bid,
    tb_titolo.isadn, tb_titolo.tp_materiale, tb_titolo.tp_record_uni,
    tb_titolo.cd_natura, tb_titolo.cd_paese, tb_titolo.cd_lingua_1,
    tb_titolo.cd_lingua_2, tb_titolo.cd_lingua_3, tb_titolo.aa_pubb_1,
    tb_titolo.aa_pubb_2, tb_titolo.tp_aa_pubb, tb_titolo.cd_genere_1,
    tb_titolo.cd_genere_2, tb_titolo.cd_genere_3, tb_titolo.cd_genere_4,
    tb_titolo.ky_cles1_t, tb_titolo.ky_cles2_t, tb_titolo.ky_clet1_t,
    tb_titolo.ky_clet2_t, tb_titolo.ky_cles1_ct, tb_titolo.ky_cles2_ct,
    tb_titolo.ky_clet1_ct, tb_titolo.ky_clet2_ct, tb_titolo.cd_livello,
    tb_titolo.fl_speciale, tb_titolo.isbd, tb_titolo.indice_isbd,
    tb_titolo.ky_editore, tb_titolo.cd_agenzia, tb_titolo.cd_norme_cat,
    tb_titolo.nota_cat_tit, tb_titolo.nota_inf_tit, tb_titolo.bid_link,
    tb_titolo.tp_link, tb_titolo.ute_ins, tb_titolo.ts_ins,
    tb_titolo.ute_var, tb_titolo.ts_var, tb_titolo.ute_forza_ins,
    tb_titolo.ute_forza_var, tb_titolo.fl_canc, tb_titolo.fl_condiviso,
    tb_titolo.ts_condiviso, tb_titolo.ute_condiviso
FROM tr_tit_aut, tb_titolo
WHERE (((tr_tit_aut.bid = tb_titolo.bid) AND (tr_tit_aut.fl_canc <>
    'S'::bpchar)) AND (tb_titolo.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_titolo_aut_aut (OID = 626395) : 
--
CREATE VIEW sbnweb.ve_titolo_aut_aut AS
SELECT ta.vid, ta.tp_responsabilita, ta.cd_relazione, ta.nota_tit_aut,
    ta.fl_incerto, ta.fl_superfluo, ta.cd_strumento_mus, ta.bid, ta.isadn,
    ta.tp_materiale, ta.tp_record_uni, ta.cd_natura, ta.cd_paese,
    ta.cd_lingua_1, ta.cd_lingua_2, ta.cd_lingua_3, ta.aa_pubb_1,
    ta.aa_pubb_2, ta.tp_aa_pubb, ta.cd_genere_1, ta.cd_genere_2,
    ta.cd_genere_3, ta.cd_genere_4, ta.ky_cles1_t, ta.ky_cles2_t,
    ta.ky_clet1_t, ta.ky_clet2_t, ta.ky_cles1_ct, ta.ky_cles2_ct,
    ta.ky_clet1_ct, ta.ky_clet2_ct, ta.cd_livello, ta.fl_speciale, ta.isbd,
    ta.indice_isbd, ta.ky_editore, ta.cd_agenzia, ta.cd_norme_cat,
    ta.nota_cat_tit, ta.nota_inf_tit, ta.bid_link, ta.tp_link, ta.ute_ins,
    ta.ts_ins, ta.ute_var, ta.ts_var, ta.ute_forza_ins, ta.ute_forza_var,
    ta.fl_canc, at.tp_responsabilita AS tp_responsabilita_f,
    at.cd_relazione AS cd_relazione_f, at.fl_superfluo AS fl_superfluo_f,
    at.vid AS vid_f, at.ky_cautun AS ky_cautun_f, at.ky_auteur AS
    ky_auteur_f, at.ky_cles1_a AS ky_cles1_a_f, at.ky_cles2_a AS ky_cles2_a_f
FROM vl_titolo_aut ta, vl_autore_tit at
WHERE (ta.bid = at.bid);

--
-- Definition for view ve_musica_tit_c_aut (OID = 626400) : 
--
CREATE VIEW sbnweb.ve_musica_tit_c_aut AS
SELECT mt.bid_coll, mt.tp_legame, mt.tp_legame_musica, mt.cd_natura_base,
    mt.cd_natura_coll, mt.sequenza, mt.nota_tit_tit, mt.sequenza_musica,
    mt.sici, mt.cd_livello_m, mt.ds_org_sint, mt.ds_org_anal,
    mt.tp_elaborazione, mt.cd_stesura, mt.fl_composito, mt.fl_palinsesto,
    mt.datazione, mt.cd_presentazione, mt.cd_materia, mt.ds_illustrazioni,
    mt.notazione_musicale, mt.ds_legatura, mt.ds_conservazione,
    mt.tp_testo_letter, mt.bid, mt.isadn, mt.tp_materiale,
    mt.tp_record_uni, mt.cd_natura, mt.cd_paese, mt.cd_lingua_1,
    mt.cd_lingua_2, mt.cd_lingua_3, mt.aa_pubb_1, mt.aa_pubb_2,
    mt.tp_aa_pubb, mt.cd_genere_1, mt.cd_genere_2, mt.cd_genere_3,
    mt.cd_genere_4, mt.ky_cles1_t, mt.ky_cles2_t, mt.ky_clet1_t,
    mt.ky_clet2_t, mt.ky_cles1_ct, mt.ky_cles2_ct, mt.ky_clet1_ct,
    mt.ky_clet2_ct, mt.cd_livello, mt.fl_speciale, mt.isbd, mt.indice_isbd,
    mt.ky_editore, mt.cd_agenzia, mt.cd_norme_cat, mt.nota_cat_tit,
    mt.nota_inf_tit, mt.bid_link, mt.tp_link, mt.ute_ins, mt.ts_ins,
    mt.ute_var, mt.ts_var, mt.ute_forza_ins, mt.ute_forza_var, mt.fl_canc,
    at.tp_responsabilita, at.cd_relazione, at.fl_superfluo, at.vid,
    at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_musica_tit_c mt, vl_autore_tit at
WHERE (mt.bid = at.bid);

--
-- Definition for view ve_musica_mar_aut (OID = 626405) : 
--
CREATE VIEW sbnweb.ve_musica_mar_aut AS
SELECT mm.mid, mm.nota_tit_mar, mm.cd_livello_m, mm.ds_org_sint,
    mm.ds_org_anal, mm.tp_elaborazione, mm.cd_stesura, mm.fl_composito,
    mm.fl_palinsesto, mm.datazione, mm.cd_presentazione, mm.cd_materia,
    mm.ds_illustrazioni, mm.notazione_musicale, mm.ds_legatura,
    mm.ds_conservazione, mm.tp_testo_letter, mm.bid, mm.isadn,
    mm.tp_materiale, mm.tp_record_uni, mm.cd_natura, mm.cd_paese,
    mm.cd_lingua_1, mm.cd_lingua_2, mm.cd_lingua_3, mm.aa_pubb_1,
    mm.aa_pubb_2, mm.tp_aa_pubb, mm.cd_genere_1, mm.cd_genere_2,
    mm.cd_genere_3, mm.cd_genere_4, mm.ky_cles1_t, mm.ky_cles2_t,
    mm.ky_clet1_t, mm.ky_clet2_t, mm.ky_cles1_ct, mm.ky_cles2_ct,
    mm.ky_clet1_ct, mm.ky_clet2_ct, mm.cd_livello, mm.fl_speciale, mm.isbd,
    mm.indice_isbd, mm.ky_editore, mm.cd_agenzia, mm.cd_norme_cat,
    mm.nota_cat_tit, mm.nota_inf_tit, mm.bid_link, mm.tp_link, mm.ute_ins,
    mm.ts_ins, mm.ute_var, mm.ts_var, mm.ute_forza_ins, mm.ute_forza_var,
    mm.fl_canc, at.tp_responsabilita, at.cd_relazione, at.fl_superfluo,
    at.vid, at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_musica_mar mm, vl_autore_tit at
WHERE (mm.bid = at.bid);

--
-- Definition for view ve_musica_luo_aut (OID = 626410) : 
--
CREATE VIEW sbnweb.ve_musica_luo_aut AS
SELECT ml.lid, ml.tp_luogo, ml.nota_tit_luo, ml.cd_livello_m,
    ml.ds_org_sint, ml.ds_org_anal, ml.tp_elaborazione, ml.cd_stesura,
    ml.fl_composito, ml.fl_palinsesto, ml.datazione, ml.cd_presentazione,
    ml.cd_materia, ml.ds_illustrazioni, ml.notazione_musicale,
    ml.ds_legatura, ml.ds_conservazione, ml.tp_testo_letter, ml.bid,
    ml.isadn, ml.tp_materiale, ml.tp_record_uni, ml.cd_natura, ml.cd_paese,
    ml.cd_lingua_1, ml.cd_lingua_2, ml.cd_lingua_3, ml.aa_pubb_1,
    ml.aa_pubb_2, ml.tp_aa_pubb, ml.cd_genere_1, ml.cd_genere_2,
    ml.cd_genere_3, ml.cd_genere_4, ml.ky_cles1_t, ml.ky_cles2_t,
    ml.ky_clet1_t, ml.ky_clet2_t, ml.ky_cles1_ct, ml.ky_cles2_ct,
    ml.ky_clet1_ct, ml.ky_clet2_ct, ml.cd_livello, ml.fl_speciale, ml.isbd,
    ml.indice_isbd, ml.ky_editore, ml.cd_agenzia, ml.cd_norme_cat,
    ml.nota_cat_tit, ml.nota_inf_tit, ml.bid_link, ml.tp_link, ml.ute_ins,
    ml.ts_ins, ml.ute_var, ml.ts_var, ml.ute_forza_ins, ml.ute_forza_var,
    ml.fl_canc, at.tp_responsabilita, at.cd_relazione, at.fl_superfluo,
    at.vid, at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_musica_luo ml, vl_autore_tit at
WHERE (ml.bid = at.bid);

--
-- Definition for view ve_musica_cla_aut (OID = 626415) : 
--
CREATE VIEW sbnweb.ve_musica_cla_aut AS
SELECT mc.cd_sistema, mc.cd_edizione, mc.classe, mc.cd_livello_m,
    mc.ds_org_sint, mc.ds_org_anal, mc.tp_elaborazione, mc.cd_stesura,
    mc.fl_composito, mc.fl_palinsesto, mc.datazione, mc.cd_presentazione,
    mc.cd_materia, mc.ds_illustrazioni, mc.notazione_musicale,
    mc.ds_legatura, mc.ds_conservazione, mc.tp_testo_letter, mc.bid,
    mc.isadn, mc.tp_materiale, mc.tp_record_uni, mc.cd_natura, mc.cd_paese,
    mc.cd_lingua_1, mc.cd_lingua_2, mc.cd_lingua_3, mc.aa_pubb_1,
    mc.aa_pubb_2, mc.tp_aa_pubb, mc.cd_genere_1, mc.cd_genere_2,
    mc.cd_genere_3, mc.cd_genere_4, mc.ky_cles1_t, mc.ky_cles2_t,
    mc.ky_clet1_t, mc.ky_clet2_t, mc.ky_cles1_ct, mc.ky_cles2_ct,
    mc.ky_clet1_ct, mc.ky_clet2_ct, mc.cd_livello, mc.fl_speciale, mc.isbd,
    mc.indice_isbd, mc.ky_editore, mc.cd_agenzia, mc.cd_norme_cat,
    mc.nota_cat_tit, mc.nota_inf_tit, mc.bid_link, mc.tp_link, mc.ute_ins,
    mc.ts_ins, mc.ute_var, mc.ts_var, mc.ute_forza_ins, mc.ute_forza_var,
    mc.fl_canc, at.tp_responsabilita, at.cd_relazione, at.fl_superfluo,
    at.vid, at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_musica_cla mc, vl_autore_tit at
WHERE (mc.bid = at.bid);

--
-- Definition for view vl_musica_aut (OID = 626420) : 
--
CREATE VIEW sbnweb.vl_musica_aut AS
SELECT ta.vid, ta.tp_responsabilita, ta.cd_relazione, ta.nota_tit_aut,
    ta.fl_incerto, ta.fl_superfluo, ta.cd_strumento_mus, ta.fl_condiviso AS
    fl_condiviso_legame, ta.ts_condiviso AS ts_condiviso_legame,
    ta.ute_condiviso AS ute_condiviso_legame, m.cd_livello_m,
    m.ds_org_sint, m.ds_org_anal, m.tp_elaborazione, m.cd_stesura,
    m.fl_composito, m.fl_palinsesto, m.datazione, m.cd_presentazione,
    m.cd_materia, m.ds_illustrazioni, m.notazione_musicale, m.ds_legatura,
    m.ds_conservazione, m.tp_testo_letter, m.bid, m.isadn, m.tp_materiale,
    m.tp_record_uni, m.cd_natura, m.cd_paese, m.cd_lingua_1, m.cd_lingua_2,
    m.cd_lingua_3, m.aa_pubb_1, m.aa_pubb_2, m.tp_aa_pubb, m.cd_genere_1,
    m.cd_genere_2, m.cd_genere_3, m.cd_genere_4, m.ky_cles1_t,
    m.ky_cles2_t, m.ky_clet1_t, m.ky_clet2_t, m.ky_cles1_ct, m.ky_cles2_ct,
    m.ky_clet1_ct, m.ky_clet2_ct, m.cd_livello, m.fl_speciale, m.isbd,
    m.indice_isbd, m.ky_editore, m.cd_agenzia, m.cd_norme_cat,
    m.nota_cat_tit, m.nota_inf_tit, m.bid_link, m.tp_link, m.ute_ins,
    m.ts_ins, m.ute_var, m.ts_var, m.ute_forza_ins, m.ute_forza_var,
    m.fl_canc, m.fl_condiviso, m.ts_condiviso, m.ute_condiviso
FROM tr_tit_aut ta, v_musica m
WHERE (((ta.bid = m.bid) AND (ta.fl_canc <> 'S'::bpchar)) AND (m.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_musica_aut_aut (OID = 626425) : 
--
CREATE VIEW sbnweb.ve_musica_aut_aut AS
SELECT ma.vid, ma.tp_responsabilita, ma.cd_relazione, ma.nota_tit_aut,
    ma.fl_incerto, ma.fl_superfluo, ma.cd_strumento_mus, ma.cd_livello_m,
    ma.ds_org_sint, ma.ds_org_anal, ma.tp_elaborazione, ma.cd_stesura,
    ma.fl_composito, ma.fl_palinsesto, ma.datazione, ma.cd_presentazione,
    ma.cd_materia, ma.ds_illustrazioni, ma.notazione_musicale,
    ma.ds_legatura, ma.ds_conservazione, ma.tp_testo_letter, ma.bid,
    ma.isadn, ma.tp_materiale, ma.tp_record_uni, ma.cd_natura, ma.cd_paese,
    ma.cd_lingua_1, ma.cd_lingua_2, ma.cd_lingua_3, ma.aa_pubb_1,
    ma.aa_pubb_2, ma.tp_aa_pubb, ma.cd_genere_1, ma.cd_genere_2,
    ma.cd_genere_3, ma.cd_genere_4, ma.ky_cles1_t, ma.ky_cles2_t,
    ma.ky_clet1_t, ma.ky_clet2_t, ma.ky_cles1_ct, ma.ky_cles2_ct,
    ma.ky_clet1_ct, ma.ky_clet2_ct, ma.cd_livello, ma.fl_speciale, ma.isbd,
    ma.indice_isbd, ma.ky_editore, ma.cd_agenzia, ma.cd_norme_cat,
    ma.nota_cat_tit, ma.nota_inf_tit, ma.bid_link, ma.tp_link, ma.ute_ins,
    ma.ts_ins, ma.ute_var, ma.ts_var, ma.ute_forza_ins, ma.ute_forza_var,
    ma.fl_canc, at.tp_responsabilita AS tp_responsabilita_f,
    at.cd_relazione AS cd_relazione_f, at.fl_superfluo AS fl_superfluo_f,
    at.vid AS vid_f, at.ky_cautun AS ky_cautun_f, at.ky_auteur AS
    ky_auteur_f, at.ky_cles1_a AS ky_cles1_a_f, at.ky_cles2_a AS ky_cles2_a_f
FROM vl_musica_aut ma, vl_autore_tit at
WHERE (ma.bid = at.bid);

--
-- Definition for view ve_grafica_tit_c_aut (OID = 626430) : 
--
CREATE VIEW sbnweb.ve_grafica_tit_c_aut AS
SELECT gt.bid_coll, gt.tp_legame, gt.tp_legame_musica, gt.cd_natura_base,
    gt.cd_natura_coll, gt.sequenza, gt.nota_tit_tit, gt.sequenza_musica,
    gt.sici, gt.cd_livello_g, gt.tp_materiale_gra, gt.cd_supporto,
    gt.cd_colore, gt.cd_tecnica_dis_1, gt.cd_tecnica_dis_2,
    gt.cd_tecnica_dis_3, gt.cd_tecnica_sta_1, gt.cd_tecnica_sta_2,
    gt.cd_tecnica_sta_3, gt.cd_design_funz, gt.bid, gt.isadn,
    gt.tp_materiale, gt.tp_record_uni, gt.cd_natura, gt.cd_paese,
    gt.cd_lingua_1, gt.cd_lingua_2, gt.cd_lingua_3, gt.aa_pubb_1,
    gt.aa_pubb_2, gt.tp_aa_pubb, gt.cd_genere_1, gt.cd_genere_2,
    gt.cd_genere_3, gt.cd_genere_4, gt.ky_cles1_t, gt.ky_cles2_t,
    gt.ky_clet1_t, gt.ky_clet2_t, gt.ky_cles1_ct, gt.ky_cles2_ct,
    gt.ky_clet1_ct, gt.ky_clet2_ct, gt.cd_livello, gt.fl_speciale, gt.isbd,
    gt.indice_isbd, gt.ky_editore, gt.cd_agenzia, gt.cd_norme_cat,
    gt.nota_inf_tit, gt.nota_cat_tit, gt.bid_link, gt.tp_link, gt.ute_ins,
    gt.ts_ins, gt.ute_var, gt.ts_var, gt.ute_forza_ins, gt.ute_forza_var,
    gt.fl_canc, at.tp_responsabilita, at.cd_relazione, at.fl_superfluo,
    at.vid, at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_grafica_tit_c gt, vl_autore_tit at
WHERE (gt.bid = at.bid);

--
-- Definition for view ve_grafica_the_aut (OID = 626435) : 
--
CREATE VIEW sbnweb.ve_grafica_the_aut AS
SELECT gs.did, gs.cd_livello_g, gs.tp_materiale_gra, gs.cd_supporto,
    gs.cd_colore, gs.cd_tecnica_dis_1, gs.cd_tecnica_dis_2,
    gs.cd_tecnica_dis_3, gs.cd_tecnica_sta_1, gs.cd_tecnica_sta_2,
    gs.cd_tecnica_sta_3, gs.cd_design_funz, gs.bid, gs.isadn,
    gs.tp_materiale, gs.tp_record_uni, gs.cd_natura, gs.cd_paese,
    gs.cd_lingua_1, gs.cd_lingua_2, gs.cd_lingua_3, gs.aa_pubb_1,
    gs.aa_pubb_2, gs.tp_aa_pubb, gs.cd_genere_1, gs.cd_genere_2,
    gs.cd_genere_3, gs.cd_genere_4, gs.ky_cles1_t, gs.ky_cles2_t,
    gs.ky_clet1_t, gs.ky_clet2_t, gs.ky_cles1_ct, gs.ky_cles2_ct,
    gs.ky_clet1_ct, gs.ky_clet2_ct, gs.cd_livello, gs.fl_speciale, gs.isbd,
    gs.indice_isbd, gs.ky_editore, gs.cd_agenzia, gs.cd_norme_cat,
    gs.nota_inf_tit, gs.nota_cat_tit, gs.bid_link, gs.tp_link, gs.ute_ins,
    gs.ts_ins, gs.ute_var, gs.ts_var, gs.ute_forza_ins, gs.ute_forza_var,
    gs.fl_canc, at.tp_responsabilita, at.cd_relazione, at.fl_superfluo,
    at.vid, at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_grafica_the gs, vl_autore_tit at
WHERE (gs.bid = at.bid);

--
-- Definition for view ve_grafica_sog_aut (OID = 626440) : 
--
CREATE VIEW sbnweb.ve_grafica_sog_aut AS
SELECT gs.cid, gs.cd_livello_g, gs.tp_materiale_gra, gs.cd_supporto,
    gs.cd_colore, gs.cd_tecnica_dis_1, gs.cd_tecnica_dis_2,
    gs.cd_tecnica_dis_3, gs.cd_tecnica_sta_1, gs.cd_tecnica_sta_2,
    gs.cd_tecnica_sta_3, gs.cd_design_funz, gs.bid, gs.isadn,
    gs.tp_materiale, gs.tp_record_uni, gs.cd_natura, gs.cd_paese,
    gs.cd_lingua_1, gs.cd_lingua_2, gs.cd_lingua_3, gs.aa_pubb_1,
    gs.aa_pubb_2, gs.tp_aa_pubb, gs.cd_genere_1, gs.cd_genere_2,
    gs.cd_genere_3, gs.cd_genere_4, gs.ky_cles1_t, gs.ky_cles2_t,
    gs.ky_clet1_t, gs.ky_clet2_t, gs.ky_cles1_ct, gs.ky_cles2_ct,
    gs.ky_clet1_ct, gs.ky_clet2_ct, gs.cd_livello, gs.fl_speciale, gs.isbd,
    gs.indice_isbd, gs.ky_editore, gs.cd_agenzia, gs.cd_norme_cat,
    gs.nota_inf_tit, gs.nota_cat_tit, gs.bid_link, gs.tp_link, gs.ute_ins,
    gs.ts_ins, gs.ute_var, gs.ts_var, gs.ute_forza_ins, gs.ute_forza_var,
    gs.fl_canc, at.tp_responsabilita, at.cd_relazione, at.fl_superfluo,
    at.vid, at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_grafica_sog gs, vl_autore_tit at
WHERE (gs.bid = at.bid);

--
-- Definition for view ve_grafica_mar_aut (OID = 626445) : 
--
CREATE VIEW sbnweb.ve_grafica_mar_aut AS
SELECT gm.mid, gm.nota_tit_mar, gm.cd_livello_g, gm.tp_materiale_gra,
    gm.cd_supporto, gm.cd_colore, gm.cd_tecnica_dis_1, gm.cd_tecnica_dis_2,
    gm.cd_tecnica_dis_3, gm.cd_tecnica_sta_1, gm.cd_tecnica_sta_2,
    gm.cd_tecnica_sta_3, gm.cd_design_funz, gm.bid, gm.isadn,
    gm.tp_materiale, gm.tp_record_uni, gm.cd_natura, gm.cd_paese,
    gm.cd_lingua_1, gm.cd_lingua_2, gm.cd_lingua_3, gm.aa_pubb_1,
    gm.aa_pubb_2, gm.tp_aa_pubb, gm.cd_genere_1, gm.cd_genere_2,
    gm.cd_genere_3, gm.cd_genere_4, gm.ky_cles1_t, gm.ky_cles2_t,
    gm.ky_clet1_t, gm.ky_clet2_t, gm.ky_cles1_ct, gm.ky_cles2_ct,
    gm.ky_clet1_ct, gm.ky_clet2_ct, gm.cd_livello, gm.fl_speciale, gm.isbd,
    gm.indice_isbd, gm.ky_editore, gm.cd_agenzia, gm.cd_norme_cat,
    gm.nota_inf_tit, gm.nota_cat_tit, gm.bid_link, gm.tp_link, gm.ute_ins,
    gm.ts_ins, gm.ute_var, gm.ts_var, gm.ute_forza_ins, gm.ute_forza_var,
    gm.fl_canc, at.tp_responsabilita, at.cd_relazione, at.fl_superfluo,
    at.vid, at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_grafica_mar gm, vl_autore_tit at
WHERE (gm.bid = at.bid);

--
-- Definition for view ve_grafica_luo_aut (OID = 626450) : 
--
CREATE VIEW sbnweb.ve_grafica_luo_aut AS
SELECT gl.lid, gl.tp_luogo, gl.nota_tit_luo, gl.cd_livello_g,
    gl.tp_materiale_gra, gl.cd_supporto, gl.cd_colore, gl.cd_tecnica_dis_1,
    gl.cd_tecnica_dis_2, gl.cd_tecnica_dis_3, gl.cd_tecnica_sta_1,
    gl.cd_tecnica_sta_2, gl.cd_tecnica_sta_3, gl.cd_design_funz, gl.bid,
    gl.isadn, gl.tp_materiale, gl.tp_record_uni, gl.cd_natura, gl.cd_paese,
    gl.cd_lingua_1, gl.cd_lingua_2, gl.cd_lingua_3, gl.aa_pubb_1,
    gl.aa_pubb_2, gl.tp_aa_pubb, gl.cd_genere_1, gl.cd_genere_2,
    gl.cd_genere_3, gl.cd_genere_4, gl.ky_cles1_t, gl.ky_cles2_t,
    gl.ky_clet1_t, gl.ky_clet2_t, gl.ky_cles1_ct, gl.ky_cles2_ct,
    gl.ky_clet1_ct, gl.ky_clet2_ct, gl.cd_livello, gl.fl_speciale, gl.isbd,
    gl.indice_isbd, gl.ky_editore, gl.cd_agenzia, gl.cd_norme_cat,
    gl.nota_inf_tit, gl.nota_cat_tit, gl.bid_link, gl.tp_link, gl.ute_ins,
    gl.ts_ins, gl.ute_var, gl.ts_var, gl.ute_forza_ins, gl.ute_forza_var,
    gl.fl_canc, at.tp_responsabilita, at.cd_relazione, at.fl_superfluo,
    at.vid, at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_grafica_luo gl, vl_autore_tit at
WHERE (gl.bid = at.bid);

--
-- Definition for view ve_grafica_cla_aut (OID = 626455) : 
--
CREATE VIEW sbnweb.ve_grafica_cla_aut AS
SELECT gc.cd_sistema, gc.cd_edizione, gc.classe, gc.cd_livello_g,
    gc.tp_materiale_gra, gc.cd_supporto, gc.cd_colore, gc.cd_tecnica_dis_1,
    gc.cd_tecnica_dis_2, gc.cd_tecnica_dis_3, gc.cd_tecnica_sta_1,
    gc.cd_tecnica_sta_2, gc.cd_tecnica_sta_3, gc.cd_design_funz, gc.bid,
    gc.isadn, gc.tp_materiale, gc.tp_record_uni, gc.cd_natura, gc.cd_paese,
    gc.cd_lingua_1, gc.cd_lingua_2, gc.cd_lingua_3, gc.aa_pubb_1,
    gc.aa_pubb_2, gc.tp_aa_pubb, gc.cd_genere_1, gc.cd_genere_2,
    gc.cd_genere_3, gc.cd_genere_4, gc.ky_cles1_t, gc.ky_cles2_t,
    gc.ky_clet1_t, gc.ky_clet2_t, gc.ky_cles1_ct, gc.ky_cles2_ct,
    gc.ky_clet1_ct, gc.ky_clet2_ct, gc.cd_livello, gc.fl_speciale, gc.isbd,
    gc.indice_isbd, gc.ky_editore, gc.cd_agenzia, gc.cd_norme_cat,
    gc.nota_inf_tit, gc.nota_cat_tit, gc.bid_link, gc.tp_link, gc.ute_ins,
    gc.ts_ins, gc.ute_var, gc.ts_var, gc.ute_forza_ins, gc.ute_forza_var,
    gc.fl_canc, at.tp_responsabilita, at.cd_relazione, at.fl_superfluo,
    at.vid, at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_grafica_cla gc, vl_autore_tit at
WHERE (gc.bid = at.bid);

--
-- Definition for view vl_grafica_aut (OID = 626460) : 
--
CREATE VIEW sbnweb.vl_grafica_aut AS
SELECT ta.vid, ta.tp_responsabilita, ta.cd_relazione, ta.nota_tit_aut,
    ta.fl_incerto, ta.fl_superfluo, ta.cd_strumento_mus, ta.fl_condiviso AS
    fl_condiviso_legame, ta.ts_condiviso AS ts_condiviso_legame,
    ta.ute_condiviso AS ute_condiviso_legame, g.cd_livello_g,
    g.tp_materiale_gra, g.cd_supporto, g.cd_colore, g.cd_tecnica_dis_1,
    g.cd_tecnica_dis_2, g.cd_tecnica_dis_3, g.cd_tecnica_sta_1,
    g.cd_tecnica_sta_2, g.cd_tecnica_sta_3, g.cd_design_funz, g.bid,
    g.isadn, g.tp_materiale, g.tp_record_uni, g.cd_natura, g.cd_paese,
    g.cd_lingua_1, g.cd_lingua_2, g.cd_lingua_3, g.aa_pubb_1, g.aa_pubb_2,
    g.tp_aa_pubb, g.cd_genere_1, g.cd_genere_2, g.cd_genere_3,
    g.cd_genere_4, g.ky_cles1_t, g.ky_cles2_t, g.ky_clet1_t, g.ky_clet2_t,
    g.ky_cles1_ct, g.ky_cles2_ct, g.ky_clet1_ct, g.ky_clet2_ct,
    g.cd_livello, g.fl_speciale, g.isbd, g.indice_isbd, g.ky_editore,
    g.cd_agenzia, g.cd_norme_cat, g.nota_inf_tit, g.nota_cat_tit,
    g.bid_link, g.tp_link, g.ute_ins, g.ts_ins, g.ute_var, g.ts_var,
    g.ute_forza_ins, g.ute_forza_var, g.fl_canc, g.fl_condiviso,
    g.ts_condiviso, g.ute_condiviso
FROM tr_tit_aut ta, v_grafica g
WHERE (((ta.bid = g.bid) AND (ta.fl_canc <> 'S'::bpchar)) AND (g.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_grafica_aut_aut (OID = 626465) : 
--
CREATE VIEW sbnweb.ve_grafica_aut_aut AS
SELECT ga.vid, ga.tp_responsabilita, ga.cd_relazione, ga.nota_tit_aut,
    ga.fl_incerto, ga.fl_superfluo, ga.cd_strumento_mus, ga.cd_livello_g,
    ga.tp_materiale_gra, ga.cd_supporto, ga.cd_colore, ga.cd_tecnica_dis_1,
    ga.cd_tecnica_dis_2, ga.cd_tecnica_dis_3, ga.cd_tecnica_sta_1,
    ga.cd_tecnica_sta_2, ga.cd_tecnica_sta_3, ga.cd_design_funz, ga.bid,
    ga.isadn, ga.tp_materiale, ga.tp_record_uni, ga.cd_natura, ga.cd_paese,
    ga.cd_lingua_1, ga.cd_lingua_2, ga.cd_lingua_3, ga.aa_pubb_1,
    ga.aa_pubb_2, ga.tp_aa_pubb, ga.cd_genere_1, ga.cd_genere_2,
    ga.cd_genere_3, ga.cd_genere_4, ga.ky_cles1_t, ga.ky_cles2_t,
    ga.ky_clet1_t, ga.ky_clet2_t, ga.ky_cles1_ct, ga.ky_cles2_ct,
    ga.ky_clet1_ct, ga.ky_clet2_ct, ga.cd_livello, ga.fl_speciale, ga.isbd,
    ga.indice_isbd, ga.ky_editore, ga.cd_agenzia, ga.cd_norme_cat,
    ga.nota_inf_tit, ga.nota_cat_tit, ga.bid_link, ga.tp_link, ga.ute_ins,
    ga.ts_ins, ga.ute_var, ga.ts_var, ga.ute_forza_ins, ga.ute_forza_var,
    ga.fl_canc, at.tp_responsabilita AS tp_responsabilita_f,
    at.cd_relazione AS cd_relazione_f, at.fl_superfluo AS fl_superfluo_f,
    at.vid AS vid_f, at.ky_cautun AS ky_cautun_f, at.ky_auteur AS
    ky_auteur_f, at.ky_cles1_a AS ky_cles1_a_f, at.ky_cles2_a AS ky_cles2_a_f
FROM vl_grafica_aut ga, vl_autore_tit at
WHERE (ga.bid = at.bid);

--
-- Definition for view ve_cartografia_tit_c_aut (OID = 626470) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_tit_c_aut(
    bid_coll,
    tp_legame,
    tp_legame_musica,
    cd_natura_base,
    cd_natura_coll,
    sequenza,
    nota_tit_tit,
    sequenza_musica,
    sici,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    tp_responsabilita,
    cd_relazione,
    fl_superfluo,
    vid,
    ky_cautun,
    ky_auteur,
    ky_cles1_a,
    ky_cles2_a)
AS
  SELECT ct.bid_coll,
         ct.tp_legame,
         ct.tp_legame_musica,
         ct.cd_natura_base,
         ct.cd_natura_coll,
         ct.sequenza,
         ct.nota_tit_tit,
         ct.sequenza_musica,
         ct.sici,
         ct.cd_livello_c,
         ct.tp_pubb_gov,
         ct.cd_colore,
         ct.cd_meridiano,
         ct.cd_supporto_fisico,
         ct.cd_tecnica,
         ct.cd_forma_ripr,
         ct.cd_forma_pubb,
         ct.cd_altitudine,
         ct.cd_tiposcala,
         ct.tp_scala,
         ct.scala_oriz,
         ct.scala_vert,
         ct.longitudine_ovest,
         ct.longitudine_est,
         ct.latitudine_nord,
         ct.latitudine_sud,
         ct.tp_immagine,
         ct.cd_forma_cart,
         ct.cd_piattaforma,
         ct.cd_categ_satellite,
		 ct.tp_proiezione,
         ct.bid,
         ct.isadn,
         ct.tp_materiale,
         ct.tp_record_uni,
         ct.cd_natura,
         ct.cd_paese,
         ct.cd_lingua_1,
         ct.cd_lingua_2,
         ct.cd_lingua_3,
         ct.aa_pubb_1,
         ct.aa_pubb_2,
         ct.tp_aa_pubb,
         ct.cd_genere_1,
         ct.cd_genere_2,
         ct.cd_genere_3,
         ct.cd_genere_4,
         ct.ky_cles1_t,
         ct.ky_cles2_t,
         ct.ky_clet1_t,
         ct.ky_clet2_t,
         ct.ky_cles1_ct,
         ct.ky_cles2_ct,
         ct.ky_clet1_ct,
         ct.ky_clet2_ct,
         ct.cd_livello,
         ct.fl_speciale,
         ct.isbd,
         ct.indice_isbd,
         ct.ky_editore,
         ct.cd_agenzia,
         ct.cd_norme_cat,
         ct.nota_inf_tit,
         ct.nota_cat_tit,
         ct.bid_link,
         ct.tp_link,
         ct.ute_ins,
         ct.ts_ins,
         ct.ute_var,
         ct.ts_var,
         ct.ute_forza_ins,
         ct.ute_forza_var,
         ct.fl_canc,
         at.tp_responsabilita,
         at.cd_relazione,
         at.fl_superfluo,
         at.vid,
         at.ky_cautun,
         at.ky_auteur,
         at.ky_cles1_a,
         at.ky_cles2_a
  FROM vl_cartografia_tit_c ct,
       vl_autore_tit at
  WHERE ct.bid = at.bid;


--
-- Definition for view ve_cartografia_the_aut (OID = 626475) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_the_aut(
    did,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    tp_responsabilita,
    cd_relazione,
    fl_superfluo,
    vid,
    ky_auteur,
    ky_cautun,
    ky_cles1_a,
    ky_cles2_a)
AS
  SELECT cs.did,
         cs.cd_livello_c,
         cs.tp_pubb_gov,
         cs.cd_colore,
         cs.cd_meridiano,
         cs.cd_supporto_fisico,
         cs.cd_tecnica,
         cs.cd_forma_ripr,
         cs.cd_forma_pubb,
         cs.cd_altitudine,
         cs.cd_tiposcala,
         cs.tp_scala,
         cs.scala_oriz,
         cs.scala_vert,
         cs.longitudine_ovest,
         cs.longitudine_est,
         cs.latitudine_nord,
         cs.latitudine_sud,
         cs.tp_immagine,
         cs.cd_forma_cart,
         cs.cd_piattaforma,
         cs.cd_categ_satellite,
		 cs.tp_proiezione,
         cs.bid,
         cs.isadn,
         cs.tp_materiale,
         cs.tp_record_uni,
         cs.cd_natura,
         cs.cd_paese,
         cs.cd_lingua_1,
         cs.cd_lingua_2,
         cs.cd_lingua_3,
         cs.aa_pubb_1,
         cs.aa_pubb_2,
         cs.tp_aa_pubb,
         cs.cd_genere_1,
         cs.cd_genere_2,
         cs.cd_genere_3,
         cs.cd_genere_4,
         cs.ky_cles1_t,
         cs.ky_cles2_t,
         cs.ky_clet1_t,
         cs.ky_clet2_t,
         cs.ky_cles1_ct,
         cs.ky_cles2_ct,
         cs.ky_clet1_ct,
         cs.ky_clet2_ct,
         cs.cd_livello,
         cs.fl_speciale,
         cs.isbd,
         cs.indice_isbd,
         cs.ky_editore,
         cs.cd_agenzia,
         cs.cd_norme_cat,
         cs.nota_inf_tit,
         cs.nota_cat_tit,
         cs.bid_link,
         cs.tp_link,
         cs.ute_ins,
         cs.ts_ins,
         cs.ute_var,
         cs.ts_var,
         cs.ute_forza_ins,
         cs.ute_forza_var,
         cs.fl_canc,
         at.tp_responsabilita,
         at.cd_relazione,
         at.fl_superfluo,
         at.vid,
         at.ky_auteur,
         at.ky_cautun,
         at.ky_cles1_a,
         at.ky_cles2_a
  FROM vl_cartografia_the cs,
       vl_autore_tit at
  WHERE cs.bid = at.bid;


--
-- Definition for view ve_cartografia_sog_aut (OID = 626480) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_sog_aut(
    cid,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    tp_responsabilita,
    cd_relazione,
    fl_superfluo,
    vid,
    ky_auteur,
    ky_cautun,
    ky_cles1_a,
    ky_cles2_a)
AS
  SELECT cs.cid,
         cs.cd_livello_c,
         cs.tp_pubb_gov,
         cs.cd_colore,
         cs.cd_meridiano,
         cs.cd_supporto_fisico,
         cs.cd_tecnica,
         cs.cd_forma_ripr,
         cs.cd_forma_pubb,
         cs.cd_altitudine,
         cs.cd_tiposcala,
         cs.tp_scala,
         cs.scala_oriz,
         cs.scala_vert,
         cs.longitudine_ovest,
         cs.longitudine_est,
         cs.latitudine_nord,
         cs.latitudine_sud,
         cs.tp_immagine,
         cs.cd_forma_cart,
         cs.cd_piattaforma,
         cs.cd_categ_satellite,
		 cs.tp_proiezione,
         cs.bid,
         cs.isadn,
         cs.tp_materiale,
         cs.tp_record_uni,
         cs.cd_natura,
         cs.cd_paese,
         cs.cd_lingua_1,
         cs.cd_lingua_2,
         cs.cd_lingua_3,
         cs.aa_pubb_1,
         cs.aa_pubb_2,
         cs.tp_aa_pubb,
         cs.cd_genere_1,
         cs.cd_genere_2,
         cs.cd_genere_3,
         cs.cd_genere_4,
         cs.ky_cles1_t,
         cs.ky_cles2_t,
         cs.ky_clet1_t,
         cs.ky_clet2_t,
         cs.ky_cles1_ct,
         cs.ky_cles2_ct,
         cs.ky_clet1_ct,
         cs.ky_clet2_ct,
         cs.cd_livello,
         cs.fl_speciale,
         cs.isbd,
         cs.indice_isbd,
         cs.ky_editore,
         cs.cd_agenzia,
         cs.cd_norme_cat,
         cs.nota_inf_tit,
         cs.nota_cat_tit,
         cs.bid_link,
         cs.tp_link,
         cs.ute_ins,
         cs.ts_ins,
         cs.ute_var,
         cs.ts_var,
         cs.ute_forza_ins,
         cs.ute_forza_var,
         cs.fl_canc,
         at.tp_responsabilita,
         at.cd_relazione,
         at.fl_superfluo,
         at.vid,
         at.ky_auteur,
         at.ky_cautun,
         at.ky_cles1_a,
         at.ky_cles2_a
  FROM vl_cartografia_sog cs,
       vl_autore_tit at
  WHERE cs.bid = at.bid;


--
-- Definition for view ve_cartografia_mar_aut (OID = 626485) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_mar_aut(
    mid,
    nota_tit_mar,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    tp_responsabilita,
    cd_relazione,
    fl_superfluo,
    vid,
    ky_cautun,
    ky_auteur,
    ky_cles1_a,
    ky_cles2_a)
AS
  SELECT cm.mid,
         cm.nota_tit_mar,
         cm.cd_livello_c,
         cm.tp_pubb_gov,
         cm.cd_colore,
         cm.cd_meridiano,
         cm.cd_supporto_fisico,
         cm.cd_tecnica,
         cm.cd_forma_ripr,
         cm.cd_forma_pubb,
         cm.cd_altitudine,
         cm.cd_tiposcala,
         cm.tp_scala,
         cm.scala_oriz,
         cm.scala_vert,
         cm.longitudine_ovest,
         cm.longitudine_est,
         cm.latitudine_nord,
         cm.latitudine_sud,
         cm.tp_immagine,
         cm.cd_forma_cart,
         cm.cd_piattaforma,
         cm.cd_categ_satellite,
		 cm.tp_proiezione,
         cm.bid,
         cm.isadn,
         cm.tp_materiale,
         cm.tp_record_uni,
         cm.cd_natura,
         cm.cd_paese,
         cm.cd_lingua_1,
         cm.cd_lingua_2,
         cm.cd_lingua_3,
         cm.aa_pubb_1,
         cm.aa_pubb_2,
         cm.tp_aa_pubb,
         cm.cd_genere_1,
         cm.cd_genere_2,
         cm.cd_genere_3,
         cm.cd_genere_4,
         cm.ky_cles1_t,
         cm.ky_cles2_t,
         cm.ky_clet1_t,
         cm.ky_clet2_t,
         cm.ky_cles1_ct,
         cm.ky_cles2_ct,
         cm.ky_clet1_ct,
         cm.ky_clet2_ct,
         cm.cd_livello,
         cm.fl_speciale,
         cm.isbd,
         cm.indice_isbd,
         cm.ky_editore,
         cm.cd_agenzia,
         cm.cd_norme_cat,
         cm.nota_inf_tit,
         cm.nota_cat_tit,
         cm.bid_link,
         cm.tp_link,
         cm.ute_ins,
         cm.ts_ins,
         cm.ute_var,
         cm.ts_var,
         cm.ute_forza_ins,
         cm.ute_forza_var,
         cm.fl_canc,
         at.tp_responsabilita,
         at.cd_relazione,
         at.fl_superfluo,
         at.vid,
         at.ky_cautun,
         at.ky_auteur,
         at.ky_cles1_a,
         at.ky_cles2_a
  FROM vl_cartografia_mar cm,
       vl_autore_tit at
  WHERE cm.bid = at.bid;
  


--
-- Definition for view ve_cartografia_luo_aut (OID = 626490) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_luo_aut(
    lid,
    tp_luogo,
    nota_tit_luo,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    tp_responsabilita,
    cd_relazione,
    fl_superfluo,
    vid,
    ky_cautun,
    ky_auteur,
    ky_cles1_a,
    ky_cles2_a)
AS
  SELECT cl.lid,
         cl.tp_luogo,
         cl.nota_tit_luo,
         cl.cd_livello_c,
         cl.tp_pubb_gov,
         cl.cd_colore,
         cl.cd_meridiano,
         cl.cd_supporto_fisico,
         cl.cd_tecnica,
         cl.cd_forma_ripr,
         cl.cd_forma_pubb,
         cl.cd_altitudine,
         cl.cd_tiposcala,
         cl.tp_scala,
         cl.scala_oriz,
         cl.scala_vert,
         cl.longitudine_ovest,
         cl.longitudine_est,
         cl.latitudine_nord,
         cl.latitudine_sud,
         cl.tp_immagine,
         cl.cd_forma_cart,
         cl.cd_piattaforma,
         cl.cd_categ_satellite,
		 cl.tp_proiezione,
         cl.bid,
         cl.isadn,
         cl.tp_materiale,
         cl.tp_record_uni,
         cl.cd_natura,
         cl.cd_paese,
         cl.cd_lingua_1,
         cl.cd_lingua_2,
         cl.cd_lingua_3,
         cl.aa_pubb_1,
         cl.aa_pubb_2,
         cl.tp_aa_pubb,
         cl.cd_genere_1,
         cl.cd_genere_2,
         cl.cd_genere_3,
         cl.cd_genere_4,
         cl.ky_cles1_t,
         cl.ky_cles2_t,
         cl.ky_clet1_t,
         cl.ky_clet2_t,
         cl.ky_cles1_ct,
         cl.ky_cles2_ct,
         cl.ky_clet1_ct,
         cl.ky_clet2_ct,
         cl.cd_livello,
         cl.fl_speciale,
         cl.isbd,
         cl.indice_isbd,
         cl.ky_editore,
         cl.cd_agenzia,
         cl.cd_norme_cat,
         cl.nota_inf_tit,
         cl.nota_cat_tit,
         cl.bid_link,
         cl.tp_link,
         cl.ute_ins,
         cl.ts_ins,
         cl.ute_var,
         cl.ts_var,
         cl.ute_forza_ins,
         cl.ute_forza_var,
         cl.fl_canc,
         at.tp_responsabilita,
         at.cd_relazione,
         at.fl_superfluo,
         at.vid,
         at.ky_cautun,
         at.ky_auteur,
         at.ky_cles1_a,
         at.ky_cles2_a
  FROM vl_cartografia_luo cl,
       vl_autore_tit at
  WHERE cl.bid = at.bid;
  


--
-- Definition for view ve_cartografia_cla_aut (OID = 626495) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_cla_aut(
    cd_sistema,
    cd_edizione,
    classe,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    tp_responsabilita,
    cd_relazione,
    fl_superfluo,
    vid,
    ky_cautun,
    ky_auteur,
    ky_cles1_a,
    ky_cles2_a)
AS
  SELECT cc.cd_sistema,
         cc.cd_edizione,
         cc.classe,
         cc.cd_livello_c,
         cc.tp_pubb_gov,
         cc.cd_colore,
         cc.cd_meridiano,
         cc.cd_supporto_fisico,
         cc.cd_tecnica,
         cc.cd_forma_ripr,
         cc.cd_forma_pubb,
         cc.cd_altitudine,
         cc.cd_tiposcala,
         cc.tp_scala,
         cc.scala_oriz,
         cc.scala_vert,
         cc.longitudine_ovest,
         cc.longitudine_est,
         cc.latitudine_nord,
         cc.latitudine_sud,
         cc.tp_immagine,
         cc.cd_forma_cart,
         cc.cd_piattaforma,
         cc.cd_categ_satellite,
		 cc.tp_proiezione,
         cc.bid,
         cc.isadn,
         cc.tp_materiale,
         cc.tp_record_uni,
         cc.cd_natura,
         cc.cd_paese,
         cc.cd_lingua_1,
         cc.cd_lingua_2,
         cc.cd_lingua_3,
         cc.aa_pubb_1,
         cc.aa_pubb_2,
         cc.tp_aa_pubb,
         cc.cd_genere_1,
         cc.cd_genere_2,
         cc.cd_genere_3,
         cc.cd_genere_4,
         cc.ky_cles1_t,
         cc.ky_cles2_t,
         cc.ky_clet1_t,
         cc.ky_clet2_t,
         cc.ky_cles1_ct,
         cc.ky_cles2_ct,
         cc.ky_clet1_ct,
         cc.ky_clet2_ct,
         cc.cd_livello,
         cc.fl_speciale,
         cc.isbd,
         cc.indice_isbd,
         cc.ky_editore,
         cc.cd_agenzia,
         cc.cd_norme_cat,
         cc.nota_inf_tit,
         cc.nota_cat_tit,
         cc.bid_link,
         cc.tp_link,
         cc.ute_ins,
         cc.ts_ins,
         cc.ute_var,
         cc.ts_var,
         cc.ute_forza_ins,
         cc.ute_forza_var,
         cc.fl_canc,
         at.tp_responsabilita,
         at.cd_relazione,
         at.fl_superfluo,
         at.vid,
         at.ky_cautun,
         at.ky_auteur,
         at.ky_cles1_a,
         at.ky_cles2_a
  FROM vl_cartografia_cla cc,
       vl_autore_tit at
  WHERE cc.bid = at.bid;
  


--
-- Definition for view vl_cartografia_aut (OID = 626500) : 
--
CREATE OR REPLACE VIEW sbnweb.vl_cartografia_aut(
    vid,
    tp_responsabilita,
    cd_relazione,
    nota_tit_aut,
    fl_incerto,
    fl_superfluo,
    cd_strumento_mus,
    fl_condiviso_legame,
    ts_condiviso_legame,
    ute_condiviso_legame,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    fl_condiviso,
    ts_condiviso,
    ute_condiviso)
AS
  SELECT ta.vid,
         ta.tp_responsabilita,
         ta.cd_relazione,
         ta.nota_tit_aut,
         ta.fl_incerto,
         ta.fl_superfluo,
         ta.cd_strumento_mus,
         ta.fl_condiviso AS fl_condiviso_legame,
         ta.ts_condiviso AS ts_condiviso_legame,
         ta.ute_condiviso AS ute_condiviso_legame,
         c.cd_livello_c,
         c.tp_pubb_gov,
         c.cd_colore,
         c.cd_meridiano,
         c.cd_supporto_fisico,
         c.cd_tecnica,
         c.cd_forma_ripr,
         c.cd_forma_pubb,
         c.cd_altitudine,
         c.cd_tiposcala,
         c.tp_scala,
         c.scala_oriz,
         c.scala_vert,
         c.longitudine_ovest,
         c.longitudine_est,
         c.latitudine_nord,
         c.latitudine_sud,
         c.tp_immagine,
         c.cd_forma_cart,
         c.cd_piattaforma,
         c.cd_categ_satellite,
		 c.tp_proiezione,
         c.bid,
         c.isadn,
         c.tp_materiale,
         c.tp_record_uni,
         c.cd_natura,
         c.cd_paese,
         c.cd_lingua_1,
         c.cd_lingua_2,
         c.cd_lingua_3,
         c.aa_pubb_1,
         c.aa_pubb_2,
         c.tp_aa_pubb,
         c.cd_genere_1,
         c.cd_genere_2,
         c.cd_genere_3,
         c.cd_genere_4,
         c.ky_cles1_t,
         c.ky_cles2_t,
         c.ky_clet1_t,
         c.ky_clet2_t,
         c.ky_cles1_ct,
         c.ky_cles2_ct,
         c.ky_clet1_ct,
         c.ky_clet2_ct,
         c.cd_livello,
         c.fl_speciale,
         c.isbd,
         c.indice_isbd,
         c.ky_editore,
         c.cd_agenzia,
         c.cd_norme_cat,
         c.nota_inf_tit,
         c.nota_cat_tit,
         c.bid_link,
         c.tp_link,
         c.ute_ins,
         c.ts_ins,
         c.ute_var,
         c.ts_var,
         c.ute_forza_ins,
         c.ute_forza_var,
         c.fl_canc,
         c.fl_condiviso,
         c.ts_condiviso,
         c.ute_condiviso
  FROM tr_tit_aut ta,
       v_cartografia c
  WHERE ta.bid = c.bid AND
        ta.fl_canc <> 'S'  AND
        c.fl_canc <> 'S' ;

--
-- Definition for view ve_cartografia_aut_aut (OID = 626505) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_aut_aut(
    vid,
    tp_responsabilita,
    cd_relazione,
    nota_tit_aut,
    fl_incerto,
    fl_superfluo,
    cd_strumento_mus,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    tp_responsabilita_f,
    cd_relazione_f,
    fl_superfluo_f,
    vid_f,
    ky_cautun_f,
    ky_auteur_f,
    ky_cles1_a_f,
    ky_cles2_a_f)
AS
  SELECT ca.vid,
         ca.tp_responsabilita,
         ca.cd_relazione,
         ca.nota_tit_aut,
         ca.fl_incerto,
         ca.fl_superfluo,
         ca.cd_strumento_mus,
         ca.cd_livello_c,
         ca.tp_pubb_gov,
         ca.cd_colore,
         ca.cd_meridiano,
         ca.cd_supporto_fisico,
         ca.cd_tecnica,
         ca.cd_forma_ripr,
         ca.cd_forma_pubb,
         ca.cd_altitudine,
         ca.cd_tiposcala,
         ca.tp_scala,
         ca.scala_oriz,
         ca.scala_vert,
         ca.longitudine_ovest,
         ca.longitudine_est,
         ca.latitudine_nord,
         ca.latitudine_sud,
         ca.tp_immagine,
         ca.cd_forma_cart,
         ca.cd_piattaforma,
         ca.cd_categ_satellite,
		 ca.tp_proiezione,
         ca.bid,
         ca.isadn,
         ca.tp_materiale,
         ca.tp_record_uni,
         ca.cd_natura,
         ca.cd_paese,
         ca.cd_lingua_1,
         ca.cd_lingua_2,
         ca.cd_lingua_3,
         ca.aa_pubb_1,
         ca.aa_pubb_2,
         ca.tp_aa_pubb,
         ca.cd_genere_1,
         ca.cd_genere_2,
         ca.cd_genere_3,
         ca.cd_genere_4,
         ca.ky_cles1_t,
         ca.ky_cles2_t,
         ca.ky_clet1_t,
         ca.ky_clet2_t,
         ca.ky_cles1_ct,
         ca.ky_cles2_ct,
         ca.ky_clet1_ct,
         ca.ky_clet2_ct,
         ca.cd_livello,
         ca.fl_speciale,
         ca.isbd,
         ca.indice_isbd,
         ca.ky_editore,
         ca.cd_agenzia,
         ca.cd_norme_cat,
         ca.nota_inf_tit,
         ca.nota_cat_tit,
         ca.bid_link,
         ca.tp_link,
         ca.ute_ins,
         ca.ts_ins,
         ca.ute_var,
         ca.ts_var,
         ca.ute_forza_ins,
         ca.ute_forza_var,
         ca.fl_canc,
         at.tp_responsabilita AS tp_responsabilita_f,
         at.cd_relazione AS cd_relazione_f,
         at.fl_superfluo AS fl_superfluo_f,
         at.vid AS vid_f,
         at.ky_cautun AS ky_cautun_f,
         at.ky_auteur AS ky_auteur_f,
         at.ky_cles1_a AS ky_cles1_a_f,
         at.ky_cles2_a AS ky_cles2_a_f
  FROM vl_cartografia_aut ca,
       vl_autore_tit at
  WHERE ca.bid = at.bid;


--
-- Definition for view ve_cartografia_aut_luo (OID = 626510) : 
--
CREATE OR REPLACE VIEW sbnweb.ve_cartografia_aut_luo(
    vid,
    tp_responsabilita,
    cd_relazione,
    nota_tit_aut,
    fl_incerto,
    fl_superfluo,
    cd_strumento_mus,
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
	tp_proiezione,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    lid,
    ky_norm_luogo)
AS
  SELECT ca.vid,
         ca.tp_responsabilita,
         ca.cd_relazione,
         ca.nota_tit_aut,
         ca.fl_incerto,
         ca.fl_superfluo,
         ca.cd_strumento_mus,
         ca.cd_livello_c,
         ca.tp_pubb_gov,
         ca.cd_colore,
         ca.cd_meridiano,
         ca.cd_supporto_fisico,
         ca.cd_tecnica,
         ca.cd_forma_ripr,
         ca.cd_forma_pubb,
         ca.cd_altitudine,
         ca.cd_tiposcala,
         ca.tp_scala,
         ca.scala_oriz,
         ca.scala_vert,
         ca.longitudine_ovest,
         ca.longitudine_est,
         ca.latitudine_nord,
         ca.latitudine_sud,
         ca.tp_immagine,
         ca.cd_forma_cart,
         ca.cd_piattaforma,
         ca.cd_categ_satellite,
		 ca.tp_proiezione,
         ca.bid,
         ca.isadn,
         ca.tp_materiale,
         ca.tp_record_uni,
         ca.cd_natura,
         ca.cd_paese,
         ca.cd_lingua_1,
         ca.cd_lingua_2,
         ca.cd_lingua_3,
         ca.aa_pubb_1,
         ca.aa_pubb_2,
         ca.tp_aa_pubb,
         ca.cd_genere_1,
         ca.cd_genere_2,
         ca.cd_genere_3,
         ca.cd_genere_4,
         ca.ky_cles1_t,
         ca.ky_cles2_t,
         ca.ky_clet1_t,
         ca.ky_clet2_t,
         ca.ky_cles1_ct,
         ca.ky_cles2_ct,
         ca.ky_clet1_ct,
         ca.ky_clet2_ct,
         ca.cd_livello,
         ca.fl_speciale,
         ca.isbd,
         ca.indice_isbd,
         ca.ky_editore,
         ca.cd_agenzia,
         ca.cd_norme_cat,
         ca.nota_inf_tit,
         ca.nota_cat_tit,
         ca.bid_link,
         ca.tp_link,
         ca.ute_ins,
         ca.ts_ins,
         ca.ute_var,
         ca.ts_var,
         ca.ute_forza_ins,
         ca.ute_forza_var,
         ca.fl_canc,
         lt.lid,
         lt.ky_norm_luogo
  FROM vl_cartografia_aut ca,
       vl_luogo_tit lt
  WHERE ca.bid = lt.bid;


--
-- Definition for view vl_composizione_aut (OID = 626515) : 
--
CREATE VIEW sbnweb.vl_composizione_aut AS
SELECT c.bid, c.isadn, c.tp_materiale, c.tp_record_uni, c.cd_natura,
    c.cd_paese, c.cd_lingua_1, c.cd_lingua_2, c.cd_lingua_3, c.aa_pubb_1,
    c.aa_pubb_2, c.tp_aa_pubb, c.cd_genere_1, c.cd_genere_2, c.cd_genere_3,
    c.cd_genere_4, c.ky_cles1_t, c.ky_cles2_t, c.ky_clet1_t, c.ky_clet2_t,
    c.ky_cles1_ct, c.ky_cles2_ct, c.ky_clet1_ct, c.ky_clet2_ct,
    c.cd_livello, c.fl_speciale, c.isbd, c.indice_isbd, c.ky_editore,
    c.cd_agenzia, c.cd_norme_cat, c.nota_inf_tit, c.nota_cat_tit,
    c.bid_link, c.tp_link, c.ute_ins, c.ts_ins, c.ute_var, c.ts_var,
    c.ute_forza_ins, c.ute_forza_var, c.fl_canc, c.ds_org_sint,
    c.ds_org_anal, c.numero_ordine, c.numero_opera, c.numero_cat_tem,
    c.cd_tonalita, c.datazione, c.ky_ord_ric, c.ky_est_ric, c.ky_app_ric,
    c.ky_ord_clet, c.ky_est_clet, c.ky_app_clet, c.ky_ord_pre,
    c.ky_est_pre, c.ky_app_pre, c.ky_ord_den, c.ky_est_den, c.ky_app_den,
    c.ky_ord_nor_pre, c.ky_est_nor_pre, c.ky_app_nor_pre, c.cd_forma_1,
    c.cd_forma_2, c.cd_forma_3, c.fl_condiviso, c.ts_condiviso,
    c.ute_condiviso, ta.vid, ta.tp_responsabilita, ta.cd_relazione,
    ta.fl_condiviso AS fl_condiviso_legame, ta.ts_condiviso AS
    ts_condiviso_legame, ta.ute_condiviso AS ute_condiviso_legame
FROM v_composizione c, tr_tit_aut ta
WHERE ((ta.bid = c.bid) AND (ta.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_grafica_aut_luo (OID = 626520) : 
--
CREATE VIEW sbnweb.ve_grafica_aut_luo AS
SELECT ga.vid, ga.tp_responsabilita, ga.cd_relazione, ga.nota_tit_aut,
    ga.fl_incerto, ga.fl_superfluo, ga.cd_strumento_mus, ga.cd_livello_g,
    ga.tp_materiale_gra, ga.cd_supporto, ga.cd_colore, ga.cd_tecnica_dis_1,
    ga.cd_tecnica_dis_2, ga.cd_tecnica_dis_3, ga.cd_tecnica_sta_1,
    ga.cd_tecnica_sta_2, ga.cd_tecnica_sta_3, ga.cd_design_funz, ga.bid,
    ga.isadn, ga.tp_materiale, ga.tp_record_uni, ga.cd_natura, ga.cd_paese,
    ga.cd_lingua_1, ga.cd_lingua_2, ga.cd_lingua_3, ga.aa_pubb_1,
    ga.aa_pubb_2, ga.tp_aa_pubb, ga.cd_genere_1, ga.cd_genere_2,
    ga.cd_genere_3, ga.cd_genere_4, ga.ky_cles1_t, ga.ky_cles2_t,
    ga.ky_clet1_t, ga.ky_clet2_t, ga.ky_cles1_ct, ga.ky_cles2_ct,
    ga.ky_clet1_ct, ga.ky_clet2_ct, ga.cd_livello, ga.fl_speciale, ga.isbd,
    ga.indice_isbd, ga.ky_editore, ga.cd_agenzia, ga.cd_norme_cat,
    ga.nota_inf_tit, ga.nota_cat_tit, ga.bid_link, ga.tp_link, ga.ute_ins,
    ga.ts_ins, ga.ute_var, ga.ts_var, ga.ute_forza_ins, ga.ute_forza_var,
    ga.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_grafica_aut ga, vl_luogo_tit lt
WHERE (ga.bid = lt.bid);

--
-- Definition for view ve_musica_aut_luo (OID = 626525) : 
--
CREATE VIEW sbnweb.ve_musica_aut_luo AS
SELECT ma.vid, ma.tp_responsabilita, ma.cd_relazione, ma.nota_tit_aut,
    ma.fl_incerto, ma.fl_superfluo, ma.cd_strumento_mus, ma.cd_livello_m,
    ma.ds_org_sint, ma.ds_org_anal, ma.tp_elaborazione, ma.cd_stesura,
    ma.fl_composito, ma.fl_palinsesto, ma.datazione, ma.cd_presentazione,
    ma.cd_materia, ma.ds_illustrazioni, ma.notazione_musicale,
    ma.ds_legatura, ma.ds_conservazione, ma.tp_testo_letter, ma.bid,
    ma.isadn, ma.tp_materiale, ma.tp_record_uni, ma.cd_natura, ma.cd_paese,
    ma.cd_lingua_1, ma.cd_lingua_2, ma.cd_lingua_3, ma.aa_pubb_1,
    ma.aa_pubb_2, ma.tp_aa_pubb, ma.cd_genere_1, ma.cd_genere_2,
    ma.cd_genere_3, ma.cd_genere_4, ma.ky_cles1_t, ma.ky_cles2_t,
    ma.ky_clet1_t, ma.ky_clet2_t, ma.ky_cles1_ct, ma.ky_cles2_ct,
    ma.ky_clet1_ct, ma.ky_clet2_ct, ma.cd_livello, ma.fl_speciale, ma.isbd,
    ma.indice_isbd, ma.ky_editore, ma.cd_agenzia, ma.cd_norme_cat,
    ma.nota_cat_tit, ma.nota_inf_tit, ma.bid_link, ma.tp_link, ma.ute_ins,
    ma.ts_ins, ma.ute_var, ma.ts_var, ma.ute_forza_ins, ma.ute_forza_var,
    ma.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_musica_aut ma, vl_luogo_tit lt
WHERE (ma.bid = lt.bid);

--
-- Definition for view ve_musica_aut_com (OID = 626530) : 
--
CREATE VIEW sbnweb.ve_musica_aut_com AS
SELECT ma.vid, ma.tp_responsabilita, ma.cd_relazione, ma.nota_tit_aut,
    ma.fl_incerto, ma.fl_superfluo, ma.cd_strumento_mus, ma.cd_livello_m,
    ma.ds_org_sint, ma.ds_org_anal, ma.tp_elaborazione, ma.cd_stesura,
    ma.fl_composito, ma.fl_palinsesto, ma.datazione, ma.cd_presentazione,
    ma.cd_materia, ma.ds_illustrazioni, ma.notazione_musicale,
    ma.ds_legatura, ma.ds_conservazione, ma.tp_testo_letter, ma.bid,
    ma.isadn, ma.tp_materiale, ma.tp_record_uni, ma.cd_natura, ma.cd_paese,
    ma.cd_lingua_1, ma.cd_lingua_2, ma.cd_lingua_3, ma.aa_pubb_1,
    ma.aa_pubb_2, ma.tp_aa_pubb, ma.cd_genere_1, ma.cd_genere_2,
    ma.cd_genere_3, ma.cd_genere_4, ma.ky_cles1_t, ma.ky_cles2_t,
    ma.ky_clet1_t, ma.ky_clet2_t, ma.ky_cles1_ct, ma.ky_cles2_ct,
    ma.ky_clet1_ct, ma.ky_clet2_ct, ma.cd_livello, ma.fl_speciale, ma.isbd,
    ma.indice_isbd, ma.ky_editore, ma.cd_agenzia, ma.cd_norme_cat,
    ma.nota_cat_tit, ma.nota_inf_tit, ma.bid_link, ma.tp_link, ma.ute_ins,
    ma.ts_ins, ma.ute_var, ma.ts_var, ma.ute_forza_ins, ma.ute_forza_var,
    ma.fl_canc, cm.bid AS bid_cm, cm.ds_org_sint AS ds_org_sint_cm,
    cm.ds_org_anal AS ds_org_anal_cm, cm.cd_forma_1, cm.cd_forma_2,
    cm.cd_forma_3, cm.numero_ordine, cm.numero_opera, cm.numero_cat_tem,
    cm.cd_tonalita, cm.datazione AS datazione_cm, cm.ky_ord_ric,
    cm.ky_est_ric, cm.ky_app_ric
FROM vl_musica_aut ma, vl_composizione_mus cm
WHERE (ma.bid = cm.bid_base);

--
-- Definition for view ve_titolo_aut_mar (OID = 626535) : 
--
CREATE VIEW sbnweb.ve_titolo_aut_mar AS
SELECT ta.vid, ta.tp_responsabilita, ta.cd_relazione, ta.nota_tit_aut,
    ta.fl_incerto, ta.fl_superfluo, ta.cd_strumento_mus, ta.bid, ta.isadn,
    ta.tp_materiale, ta.tp_record_uni, ta.cd_natura, ta.cd_paese,
    ta.cd_lingua_1, ta.cd_lingua_2, ta.cd_lingua_3, ta.aa_pubb_1,
    ta.aa_pubb_2, ta.tp_aa_pubb, ta.cd_genere_1, ta.cd_genere_2,
    ta.cd_genere_3, ta.cd_genere_4, ta.ky_cles1_t, ta.ky_cles2_t,
    ta.ky_clet1_t, ta.ky_clet2_t, ta.ky_cles1_ct, ta.ky_cles2_ct,
    ta.ky_clet1_ct, ta.ky_clet2_ct, ta.cd_livello, ta.fl_speciale, ta.isbd,
    ta.indice_isbd, ta.ky_editore, ta.cd_agenzia, ta.cd_norme_cat,
    ta.nota_cat_tit, ta.nota_inf_tit, ta.bid_link, ta.tp_link, ta.ute_ins,
    ta.ts_ins, ta.ute_var, ta.ts_var, ta.ute_forza_ins, ta.ute_forza_var,
    ta.fl_canc, mt.mid, mt.ds_marca, mt.tidx_vector
FROM vl_titolo_aut ta, vl_marca_tit mt
WHERE (ta.bid = mt.bid);

--
-- Definition for view ve_titolo_aut_luo (OID = 626540) : 
--
CREATE VIEW sbnweb.ve_titolo_aut_luo AS
SELECT ta.vid, ta.tp_responsabilita, ta.cd_relazione, ta.nota_tit_aut,
    ta.fl_incerto, ta.fl_superfluo, ta.cd_strumento_mus, ta.bid, ta.isadn,
    ta.tp_materiale, ta.tp_record_uni, ta.cd_natura, ta.cd_paese,
    ta.cd_lingua_1, ta.cd_lingua_2, ta.cd_lingua_3, ta.aa_pubb_1,
    ta.aa_pubb_2, ta.tp_aa_pubb, ta.cd_genere_1, ta.cd_genere_2,
    ta.cd_genere_3, ta.cd_genere_4, ta.ky_cles1_t, ta.ky_cles2_t,
    ta.ky_clet1_t, ta.ky_clet2_t, ta.ky_cles1_ct, ta.ky_cles2_ct,
    ta.ky_clet1_ct, ta.ky_clet2_ct, ta.cd_livello, ta.fl_speciale, ta.isbd,
    ta.indice_isbd, ta.ky_editore, ta.cd_agenzia, ta.cd_norme_cat,
    ta.nota_cat_tit, ta.nota_inf_tit, ta.bid_link, ta.tp_link, ta.ute_ins,
    ta.ts_ins, ta.ute_var, ta.ts_var, ta.ute_forza_ins, ta.ute_forza_var,
    ta.fl_canc, lt.lid, lt.ky_norm_luogo
FROM vl_titolo_aut ta, vl_luogo_tit lt
WHERE (ta.bid = lt.bid);

--
-- Definition for view vf_titolo_aut (OID = 626545) : 
--
CREATE VIEW sbnweb.vf_titolo_aut AS
SELECT t.bid, t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura,
    t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1,
    t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3,
    t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t,
    t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_cat_tit, t.nota_inf_tit,
    t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var,
    t.ute_forza_ins, t.ute_forza_var, t.fl_canc, ta.tp_responsabilita,
    ta.cd_relazione, ta.fl_superfluo, ta.ts_ins AS ts_ins_tit_aut,
    ta.ts_var AS ts_var_tit_aut, a.vid, a.ky_cautun, a.ky_auteur,
    a.tp_nome_aut, a.ky_cles1_a, a.ky_cles2_a, a.cd_livello AS
    cd_livello_aut, a.ds_nome_aut, a.tidx_vector
FROM tb_titolo t, tr_tit_aut ta, tb_autore a
WHERE (((((ta.bid = t.bid) AND (a.vid = ta.vid)) AND (t.fl_canc <>
    'S'::bpchar)) AND (ta.fl_canc <> 'S'::bpchar)) AND (a.fl_canc <> 'S'::bpchar));

--
-- Definition for view vf_musica_aut (OID = 626550) : 
--
CREATE VIEW sbnweb.vf_musica_aut AS
SELECT m.cd_livello_m, m.ds_org_sint, m.ds_org_anal, m.tp_elaborazione,
    m.cd_stesura, m.fl_composito, m.fl_palinsesto, m.datazione,
    m.cd_presentazione, m.cd_materia, m.ds_illustrazioni,
    m.notazione_musicale, m.ds_legatura, m.ds_conservazione,
    m.tp_testo_letter, m.bid, m.isadn, m.tp_materiale, m.tp_record_uni,
    m.cd_natura, m.cd_paese, m.cd_lingua_1, m.cd_lingua_2, m.cd_lingua_3,
    m.aa_pubb_1, m.aa_pubb_2, m.tp_aa_pubb, m.cd_genere_1, m.cd_genere_2,
    m.cd_genere_3, m.cd_genere_4, m.ky_cles1_t, m.ky_cles2_t, m.ky_clet1_t,
    m.ky_clet2_t, m.ky_cles1_ct, m.ky_cles2_ct, m.ky_clet1_ct,
    m.ky_clet2_ct, m.cd_livello, m.fl_speciale, m.isbd, m.indice_isbd,
    m.ky_editore, m.cd_agenzia, m.cd_norme_cat, m.nota_cat_tit,
    m.nota_inf_tit, m.bid_link, m.tp_link, m.ute_ins, m.ts_ins, m.ute_var,
    m.ts_var, m.ute_forza_ins, m.ute_forza_var, m.fl_canc,
    ta.tp_responsabilita, ta.cd_relazione, ta.fl_superfluo, a.vid,
    a.ky_cautun, a.ky_auteur, a.ky_cles1_a, a.ky_cles2_a
FROM v_musica m, tr_tit_aut ta, tb_autore a
WHERE (((((ta.bid = m.bid) AND (a.vid = ta.vid)) AND (m.fl_canc <>
    'S'::bpchar)) AND (ta.fl_canc <> 'S'::bpchar)) AND (a.fl_canc <> 'S'::bpchar));

--
-- Definition for view vf_musica_comaut (OID = 626555) : 
--
CREATE VIEW sbnweb.vf_musica_comaut AS
SELECT mc.cd_livello_m, mc.ds_org_sint, mc.ds_org_anal, mc.tp_elaborazione,
    mc.cd_stesura, mc.fl_composito, mc.fl_palinsesto, mc.datazione,
    mc.cd_presentazione, mc.cd_materia, mc.ds_illustrazioni,
    mc.notazione_musicale, mc.ds_legatura, mc.ds_conservazione,
    mc.tp_testo_letter, mc.bid, mc.isadn, mc.tp_materiale,
    mc.tp_record_uni, mc.cd_natura, mc.cd_paese, mc.cd_lingua_1,
    mc.cd_lingua_2, mc.cd_lingua_3, mc.aa_pubb_1, mc.aa_pubb_2,
    mc.tp_aa_pubb, mc.cd_genere_1, mc.cd_genere_2, mc.cd_genere_3,
    mc.cd_genere_4, mc.ky_cles1_t, mc.ky_cles2_t, mc.ky_clet1_t,
    mc.ky_clet2_t, mc.ky_cles1_ct, mc.ky_cles2_ct, mc.ky_clet1_ct,
    mc.ky_clet2_ct, mc.cd_livello, mc.fl_speciale, mc.isbd, mc.indice_isbd,
    mc.ky_editore, mc.cd_agenzia, mc.cd_norme_cat, mc.nota_cat_tit,
    mc.nota_inf_tit, mc.bid_link, mc.tp_link, mc.ute_ins, mc.ts_ins,
    mc.ute_var, mc.ts_var, mc.ute_forza_ins, mc.ute_forza_var, mc.fl_canc,
    mc.tp_legame_musica, mc.ds_org_sint_cm, mc.ds_org_anal_cm, mc.bid_cm,
    mc.cd_forma_1, mc.cd_forma_2, mc.cd_forma_3, mc.numero_ordine,
    mc.numero_opera, mc.numero_cat_tem, mc.cd_tonalita, mc.datazione_cm,
    mc.aa_comp_1, mc.aa_comp_2, mc.ky_ord_ric, mc.ky_est_ric,
    mc.ky_app_ric, ma.tp_responsabilita, ma.cd_relazione, ma.fl_superfluo,
    ma.vid, ma.ky_cautun, ma.ky_auteur, ma.ky_cles1_a, ma.ky_cles2_a,
    mc.ky_ord_nor_pre, mc.ky_est_nor_pre, mc.ky_app_nor_pre
FROM vf_musica_com mc, vf_musica_aut ma
WHERE (mc.bid = ma.bid);

--
-- Definition for view vf_grafica_aut (OID = 626560) : 
--
CREATE VIEW sbnweb.vf_grafica_aut AS
SELECT g.cd_livello_g, g.tp_materiale_gra, g.cd_supporto, g.cd_colore,
    g.cd_tecnica_dis_1, g.cd_tecnica_dis_2, g.cd_tecnica_dis_3,
    g.cd_tecnica_sta_1, g.cd_tecnica_sta_2, g.cd_tecnica_sta_3,
    g.cd_design_funz, g.bid, g.isadn, g.tp_materiale, g.tp_record_uni,
    g.cd_natura, g.cd_paese, g.cd_lingua_1, g.cd_lingua_2, g.cd_lingua_3,
    g.aa_pubb_1, g.aa_pubb_2, g.tp_aa_pubb, g.cd_genere_1, g.cd_genere_2,
    g.cd_genere_3, g.cd_genere_4, g.ky_cles1_t, g.ky_cles2_t, g.ky_clet1_t,
    g.ky_clet2_t, g.ky_cles1_ct, g.ky_cles2_ct, g.ky_clet1_ct,
    g.ky_clet2_ct, g.cd_livello, g.fl_speciale, g.isbd, g.indice_isbd,
    g.ky_editore, g.cd_agenzia, g.cd_norme_cat, g.nota_inf_tit,
    g.nota_cat_tit, g.bid_link, g.tp_link, g.ute_ins, g.ts_ins, g.ute_var,
    g.ts_var, g.ute_forza_ins, g.ute_forza_var, g.fl_canc,
    ta.tp_responsabilita, ta.cd_relazione, ta.fl_superfluo, a.vid,
    a.ky_cautun, a.ky_auteur, a.ky_cles1_a, a.ky_cles2_a
FROM v_grafica g, tr_tit_aut ta, tb_autore a
WHERE (((((ta.bid = g.bid) AND (a.vid = ta.vid)) AND (g.fl_canc <>
    'S'::bpchar)) AND (ta.fl_canc <> 'S'::bpchar)) AND (a.fl_canc <> 'S'::bpchar));

--
-- Definition for view vf_cartografia_aut (OID = 626565) : 
--
CREATE VIEW sbnweb.vf_cartografia_aut AS
SELECT c.cd_livello_c, c.tp_pubb_gov, c.cd_colore, c.cd_meridiano,
    c.cd_supporto_fisico, c.cd_tecnica, c.cd_forma_ripr, c.cd_forma_pubb,
    c.cd_altitudine, c.cd_tiposcala, c.tp_scala, c.scala_oriz,
    c.scala_vert, c.longitudine_ovest, c.longitudine_est,
    c.latitudine_nord, c.latitudine_sud, c.tp_immagine, c.cd_forma_cart,
    c.cd_piattaforma, c.cd_categ_satellite, c.bid, c.isadn, c.tp_materiale,
    c.tp_record_uni, c.cd_natura, c.cd_paese, c.cd_lingua_1, c.cd_lingua_2,
    c.cd_lingua_3, c.aa_pubb_1, c.aa_pubb_2, c.tp_aa_pubb, c.cd_genere_1,
    c.cd_genere_2, c.cd_genere_3, c.cd_genere_4, c.ky_cles1_t,
    c.ky_cles2_t, c.ky_clet1_t, c.ky_clet2_t, c.ky_cles1_ct, c.ky_cles2_ct,
    c.ky_clet1_ct, c.ky_clet2_ct, c.cd_livello, c.fl_speciale, c.isbd,
    c.indice_isbd, c.ky_editore, c.cd_agenzia, c.cd_norme_cat,
    c.nota_inf_tit, c.nota_cat_tit, c.bid_link, c.tp_link, c.ute_ins,
    c.ts_ins, c.ute_var, c.ts_var, c.ute_forza_ins, c.ute_forza_var,
    c.fl_canc, ta.tp_responsabilita, ta.cd_relazione, ta.fl_superfluo,
    a.vid, a.ky_cautun, a.ky_auteur, a.ky_cles1_a, a.ky_cles2_a
FROM v_cartografia c, tr_tit_aut ta, tb_autore a
WHERE (((((ta.bid = c.bid) AND (a.vid = ta.vid)) AND (c.fl_canc <>
    'S'::bpchar)) AND (ta.fl_canc <> 'S'::bpchar)) AND (a.fl_canc <> 'S'::bpchar));

--
-- Definition for view omuni (OID = 626570) : 
--
CREATE OR REPLACE VIEW sbnweb.v_daticomuni(
    s105_tp_testo_letterario,
    s125_indicatore_testo,
    s140_tp_testo_letterario,
    s181_tp_forma_contenuto_1,
    s181_cd_tipo_contenuto_1,
    s181_cd_movimento_1,
    s181_cd_dimensione_1,
    s181_cd_sensoriale_1_1,
    s181_cd_sensoriale_2_1,
    s181_cd_sensoriale_3_1,
    s181_tp_forma_contenuto_2,
    s181_cd_tipo_contenuto_2,
    s181_cd_movimento_2,
    s181_cd_dimensione_2,
    s181_cd_sensoriale_1_2,
    s181_cd_sensoriale_2_2,
    s181_cd_sensoriale_3_2,
    s182_tp_mediazione_1,
    s182_tp_mediazione_2,
    s183_tp_supporto_1,
    s183_tp_supporto_2,
	s210_ind_pubblicato,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    cd_periodicita,
    fl_condiviso,
    ute_condiviso,
    ts_condiviso)
AS
  SELECT e.s105_tp_testo_letterario,
         e.s125_indicatore_testo,
         e.s140_tp_testo_letterario,
         e.s181_tp_forma_contenuto_1,
         e.s181_cd_tipo_contenuto_1,
         e.s181_cd_movimento_1,
         e.s181_cd_dimensione_1,
         e.s181_cd_sensoriale_1_1,
         e.s181_cd_sensoriale_2_1,
         e.s181_cd_sensoriale_3_1,
         e.s181_tp_forma_contenuto_2,
         e.s181_cd_tipo_contenuto_2,
         e.s181_cd_movimento_2,
         e.s181_cd_dimensione_2,
         e.s181_cd_sensoriale_1_2,
         e.s181_cd_sensoriale_2_2,
         e.s181_cd_sensoriale_3_2,
         e.s182_tp_mediazione_1,
         e.s182_tp_mediazione_2,
         e.s183_tp_supporto_1,
         e.s183_tp_supporto_2,
		 e.s210_ind_pubblicato,
         t.bid,
         t.isadn,
         t.tp_materiale,
         t.tp_record_uni,
         t.cd_natura,
         t.cd_paese,
         t.cd_lingua_1,
         t.cd_lingua_2,
         t.cd_lingua_3,
         t.aa_pubb_1,
         t.aa_pubb_2,
         t.tp_aa_pubb,
         t.cd_genere_1,
         t.cd_genere_2,
         t.cd_genere_3,
         t.cd_genere_4,
         t.ky_cles1_t,
         t.ky_cles2_t,
         t.ky_clet1_t,
         t.ky_clet2_t,
         t.ky_cles1_ct,
         t.ky_cles2_ct,
         t.ky_clet1_ct,
         t.ky_clet2_ct,
         t.cd_livello,
         t.fl_speciale,
         t.isbd,
         t.indice_isbd,
         t.ky_editore,
         t.cd_agenzia,
         t.cd_norme_cat,
         t.nota_inf_tit,
         t.nota_cat_tit,
         t.bid_link,
         t.tp_link,
         t.ute_ins,
         t.ts_ins,
         t.ute_var,
         t.ts_var,
         t.ute_forza_ins,
         t.ute_forza_var,
         t.fl_canc,
         t.cd_periodicita,
         t.fl_condiviso,
         t.ute_condiviso,
         t.ts_condiviso
  FROM tb_titset_1 e,
       tb_titolo t
  WHERE t.bid = e.bid AND
        t.fl_canc <> 'S'  AND
        e.fl_canc <> 'S' ;	


--
-- Definition for view v_audiovisivo_com (OID = 626575) : 
--
CREATE VIEW sbnweb.v_audiovisivo_com (
    cd_livello_a,
    tp_mater_audiovis,
    lunghezza,
    cd_colore,
    cd_suono,
    tp_media_suono,
    cd_dimensione,
    cd_forma_video,
    cd_tecnica,
    tp_formato_film,
    cd_mat_accomp_1,
    cd_mat_accomp_2,
    cd_mat_accomp_3,
    cd_mat_accomp_4,
    cd_forma_regist,
    tp_formato_video,
    cd_materiale_base,
    cd_supporto_second,
    cd_broadcast,
    tp_generazione,
    cd_elementi,
    cd_categ_colore,
    cd_polarita,
    cd_pellicola,
    tp_suono,
    tp_stampa_film,
    dt_ispezione,
    cd_forma,
    cd_velocita,
    tp_suono_disco,
    cd_pista,
    cd_dimensione_disco,
    cd_larg_nastro,
    cd_configurazione,
    cd_mater_accomp_1,
    cd_mater_accomp_2,
    cd_mater_accomp_3,
    cd_mater_accomp_4,
    cd_mater_accomp_5,
    cd_mater_accomp_6,
    cd_tecnica_regis,
    cd_riproduzione,
    tp_disco,
    tp_materiale_disco,
    tp_taglio,
    durata,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    s105_tp_testo_letterario,
    s125_indicatore_testo,
    s140_tp_testo_letterario,
    s181_tp_forma_contenuto_1,
    s181_cd_tipo_contenuto_1,
    s181_cd_movimento_1,
    s181_cd_dimensione_1,
    s181_cd_sensoriale_1_1,
    s181_cd_sensoriale_2_1,
    s181_cd_sensoriale_3_1,
    s181_tp_forma_contenuto_2,
    s181_cd_tipo_contenuto_2,
    s181_cd_movimento_2,
    s181_cd_dimensione_2,
    s181_cd_sensoriale_1_2,
    s181_cd_sensoriale_2_2,
    s181_cd_sensoriale_3_2,
    s182_tp_mediazione_1,
    s182_tp_mediazione_2,
    cd_periodicita,
    fl_condiviso,
    ute_condiviso,
    ts_condiviso,
    s183_tp_supporto_1,
    s183_tp_supporto_2,
	s210_ind_pubblicato)
AS
SELECT a.cd_livello AS cd_livello_a, a.tp_mater_audiovis, a.lunghezza,
    a.cd_colore, a.cd_suono, a.tp_media_suono, a.cd_dimensione,
    a.cd_forma_video, a.cd_tecnica, a.tp_formato_film, a.cd_mat_accomp_1,
    a.cd_mat_accomp_2, a.cd_mat_accomp_3, a.cd_mat_accomp_4, a.cd_forma_regist,
    a.tp_formato_video, a.cd_materiale_base, a.cd_supporto_second,
    a.cd_broadcast, a.tp_generazione, a.cd_elementi, a.cd_categ_colore,
    a.cd_polarita, a.cd_pellicola, a.tp_suono, a.tp_stampa_film,
    a.dt_ispezione, d.cd_forma, d.cd_velocita, d.tp_suono AS tp_suono_disco,
    d.cd_pista, d.cd_dimensione AS cd_dimensione_disco, d.cd_larg_nastro,
    d.cd_configurazione, d.cd_mater_accomp_1, d.cd_mater_accomp_2,
    d.cd_mater_accomp_3, d.cd_mater_accomp_4, d.cd_mater_accomp_5,
    d.cd_mater_accomp_6, d.cd_tecnica_regis, d.cd_riproduzione, d.tp_disco,
    d.tp_materiale AS tp_materiale_disco, d.tp_taglio, d.durata, t.bid,
    t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura, t.cd_paese,
    t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1, t.aa_pubb_2,
    t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3, t.cd_genere_4,
    t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t, t.ky_cles1_ct,
    t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct, t.cd_livello, t.fl_speciale,
    t.isbd, t.indice_isbd, t.ky_editore, t.cd_agenzia, t.cd_norme_cat,
    t.nota_inf_tit, t.nota_cat_tit, t.bid_link, t.tp_link, t.ute_ins, t.ts_ins,
    t.ute_var, t.ts_var, t.ute_forza_ins, t.ute_forza_var, t.fl_canc,
    t.s105_tp_testo_letterario, t.s125_indicatore_testo,
    t.s140_tp_testo_letterario, t.s181_tp_forma_contenuto_1,
    t.s181_cd_tipo_contenuto_1, t.s181_cd_movimento_1, t.s181_cd_dimensione_1,
    t.s181_cd_sensoriale_1_1, t.s181_cd_sensoriale_2_1,
    t.s181_cd_sensoriale_3_1, t.s181_tp_forma_contenuto_2,
    t.s181_cd_tipo_contenuto_2, t.s181_cd_movimento_2, t.s181_cd_dimensione_2,
    t.s181_cd_sensoriale_1_2, t.s181_cd_sensoriale_2_2,
    t.s181_cd_sensoriale_3_2, t.s182_tp_mediazione_1, t.s182_tp_mediazione_2,
    t.cd_periodicita, t.fl_condiviso, t.ute_condiviso, t.ts_condiviso,
    t.s183_tp_supporto_1, t.s183_tp_supporto_2, t.s210_ind_pubblicato
FROM v_daticomuni t
   LEFT JOIN tb_audiovideo a ON a.bid = t.bid
   LEFT JOIN tb_disco_sonoro d ON d.bid = t.bid
WHERE t.fl_canc <> 'S' AND (a.fl_canc <> 'S' OR a.fl_canc IS
    NULL) AND (d.fl_canc <> 'S' OR d.fl_canc IS NULL) AND NOT
    (a.fl_canc IS NULL AND d.fl_canc IS NULL);

--
-- Definition for view v_cartografia_com (OID = 626580) : 
--
CREATE VIEW sbnweb.v_cartografia_com (
    cd_livello_c,
    tp_pubb_gov,
    cd_colore,
    cd_meridiano,
    cd_supporto_fisico,
    cd_tecnica,
    cd_forma_ripr,
    cd_forma_pubb,
    cd_altitudine,
    cd_tiposcala,
    tp_scala,
    scala_oriz,
    scala_vert,
    longitudine_ovest,
    longitudine_est,
    latitudine_nord,
    latitudine_sud,
    tp_immagine,
    cd_forma_cart,
    cd_piattaforma,
    cd_categ_satellite,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    s105_tp_testo_letterario,
    s125_indicatore_testo,
    s140_tp_testo_letterario,
    s181_tp_forma_contenuto_1,
    s181_cd_tipo_contenuto_1,
    s181_cd_movimento_1,
    s181_cd_dimensione_1,
    s181_cd_sensoriale_1_1,
    s181_cd_sensoriale_2_1,
    s181_cd_sensoriale_3_1,
    s181_tp_forma_contenuto_2,
    s181_cd_tipo_contenuto_2,
    s181_cd_movimento_2,
    s181_cd_dimensione_2,
    s181_cd_sensoriale_1_2,
    s181_cd_sensoriale_2_2,
    s181_cd_sensoriale_3_2,
    s182_tp_mediazione_1,
    s182_tp_mediazione_2,
    cd_periodicita,
    fl_condiviso,
    ute_condiviso,
    ts_condiviso,
    s183_tp_supporto_1,
    s183_tp_supporto_2,
	s210_ind_pubblicato)
AS
SELECT c.cd_livello AS cd_livello_c, c.tp_pubb_gov, c.cd_colore,
    c.cd_meridiano, c.cd_supporto_fisico, c.cd_tecnica, c.cd_forma_ripr,
    c.cd_forma_pubb, c.cd_altitudine, c.cd_tiposcala, c.tp_scala, c.scala_oriz,
    c.scala_vert, c.longitudine_ovest, c.longitudine_est, c.latitudine_nord,
    c.latitudine_sud, c.tp_immagine, c.cd_forma_cart, c.cd_piattaforma,
    c.cd_categ_satellite, t.bid, t.isadn, t.tp_materiale, t.tp_record_uni,
    t.cd_natura, t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3,
    t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2,
    t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t,
    t.ky_clet2_t, t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit, t.nota_cat_tit, t.bid_link,
    t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var, t.ute_forza_ins,
    t.ute_forza_var, t.fl_canc, t.s105_tp_testo_letterario,
    t.s125_indicatore_testo, t.s140_tp_testo_letterario,
    t.s181_tp_forma_contenuto_1, t.s181_cd_tipo_contenuto_1,
    t.s181_cd_movimento_1, t.s181_cd_dimensione_1, t.s181_cd_sensoriale_1_1,
    t.s181_cd_sensoriale_2_1, t.s181_cd_sensoriale_3_1,
    t.s181_tp_forma_contenuto_2, t.s181_cd_tipo_contenuto_2,
    t.s181_cd_movimento_2, t.s181_cd_dimensione_2, t.s181_cd_sensoriale_1_2,
    t.s181_cd_sensoriale_2_2, t.s181_cd_sensoriale_3_2, t.s182_tp_mediazione_1,
    t.s182_tp_mediazione_2, t.cd_periodicita, t.fl_condiviso, t.ute_condiviso,
    t.ts_condiviso, t.s183_tp_supporto_1, t.s183_tp_supporto_2, t.s210_ind_pubblicato
FROM tb_cartografia c, v_daticomuni t
WHERE t.bid = c.bid AND c.fl_canc <> 'S';

--
-- Definition for view v_grafica_com (OID = 626585) : 
--
CREATE VIEW sbnweb.v_grafica_com (
    cd_livello_g,
    tp_materiale_gra,
    cd_supporto,
    cd_colore,
    cd_tecnica_dis_1,
    cd_tecnica_dis_2,
    cd_tecnica_dis_3,
    cd_tecnica_sta_1,
    cd_tecnica_sta_2,
    cd_tecnica_sta_3,
    cd_design_funz,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_inf_tit,
    nota_cat_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    s105_tp_testo_letterario,
    s125_indicatore_testo,
    s140_tp_testo_letterario,
    s181_tp_forma_contenuto_1,
    s181_cd_tipo_contenuto_1,
    s181_cd_movimento_1,
    s181_cd_dimensione_1,
    s181_cd_sensoriale_1_1,
    s181_cd_sensoriale_2_1,
    s181_cd_sensoriale_3_1,
    s181_tp_forma_contenuto_2,
    s181_cd_tipo_contenuto_2,
    s181_cd_movimento_2,
    s181_cd_dimensione_2,
    s181_cd_sensoriale_1_2,
    s181_cd_sensoriale_2_2,
    s181_cd_sensoriale_3_2,
    s182_tp_mediazione_1,
    s182_tp_mediazione_2,
    cd_periodicita,
    fl_condiviso,
    ute_condiviso,
    ts_condiviso,
    s183_tp_supporto_1,
    s183_tp_supporto_2,
	s210_ind_pubblicato)
AS
SELECT g.cd_livello AS cd_livello_g, g.tp_materiale_gra, g.cd_supporto,
    g.cd_colore, g.cd_tecnica_dis_1, g.cd_tecnica_dis_2, g.cd_tecnica_dis_3,
    g.cd_tecnica_sta_1, g.cd_tecnica_sta_2, g.cd_tecnica_sta_3,
    g.cd_design_funz, t.bid, t.isadn, t.tp_materiale, t.tp_record_uni,
    t.cd_natura, t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3,
    t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2,
    t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t,
    t.ky_clet2_t, t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit, t.nota_cat_tit, t.bid_link,
    t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var, t.ute_forza_ins,
    t.ute_forza_var, t.fl_canc, t.s105_tp_testo_letterario,
    t.s125_indicatore_testo, t.s140_tp_testo_letterario,
    t.s181_tp_forma_contenuto_1, t.s181_cd_tipo_contenuto_1,
    t.s181_cd_movimento_1, t.s181_cd_dimensione_1, t.s181_cd_sensoriale_1_1,
    t.s181_cd_sensoriale_2_1, t.s181_cd_sensoriale_3_1,
    t.s181_tp_forma_contenuto_2, t.s181_cd_tipo_contenuto_2,
    t.s181_cd_movimento_2, t.s181_cd_dimensione_2, t.s181_cd_sensoriale_1_2,
    t.s181_cd_sensoriale_2_2, t.s181_cd_sensoriale_3_2, t.s182_tp_mediazione_1,
    t.s182_tp_mediazione_2, t.cd_periodicita, t.fl_condiviso, t.ute_condiviso,
    t.ts_condiviso, t.s183_tp_supporto_1, t.s183_tp_supporto_2, t.s210_ind_pubblicato
FROM tb_grafica g, v_daticomuni t
WHERE t.bid = g.bid AND g.fl_canc <> 'S';

--
-- Definition for view v_musica_com (OID = 626590) : 
--
CREATE VIEW sbnweb.v_musica_com (
    cd_livello_m,
    ds_org_sint,
    ds_org_anal,
    tp_elaborazione,
    cd_stesura,
    fl_composito,
    fl_palinsesto,
    datazione,
    cd_presentazione,
    cd_materia,
    ds_illustrazioni,
    notazione_musicale,
    ds_legatura,
    ds_conservazione,
    tp_testo_letter,
    bid,
    isadn,
    tp_materiale,
    tp_record_uni,
    cd_natura,
    cd_paese,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    aa_pubb_1,
    aa_pubb_2,
    tp_aa_pubb,
    cd_genere_1,
    cd_genere_2,
    cd_genere_3,
    cd_genere_4,
    ky_cles1_t,
    ky_cles2_t,
    ky_clet1_t,
    ky_clet2_t,
    ky_cles1_ct,
    ky_cles2_ct,
    ky_clet1_ct,
    ky_clet2_ct,
    cd_livello,
    fl_speciale,
    isbd,
    indice_isbd,
    ky_editore,
    cd_agenzia,
    cd_norme_cat,
    nota_cat_tit,
    nota_inf_tit,
    bid_link,
    tp_link,
    ute_ins,
    ts_ins,
    ute_var,
    ts_var,
    ute_forza_ins,
    ute_forza_var,
    fl_canc,
    s105_tp_testo_letterario,
    s125_indicatore_testo,
    s140_tp_testo_letterario,
    s181_tp_forma_contenuto_1,
    s181_cd_tipo_contenuto_1,
    s181_cd_movimento_1,
    s181_cd_dimensione_1,
    s181_cd_sensoriale_1_1,
    s181_cd_sensoriale_2_1,
    s181_cd_sensoriale_3_1,
    s181_tp_forma_contenuto_2,
    s181_cd_tipo_contenuto_2,
    s181_cd_movimento_2,
    s181_cd_dimensione_2,
    s181_cd_sensoriale_1_2,
    s181_cd_sensoriale_2_2,
    s181_cd_sensoriale_3_2,
    s182_tp_mediazione_1,
    s182_tp_mediazione_2,
    cd_periodicita,
    fl_condiviso,
    ute_condiviso,
    ts_condiviso,
    s183_tp_supporto_1,
    s183_tp_supporto_2,
	s210_ind_pubblicato)
AS
SELECT m.cd_livello AS cd_livello_m, m.ds_org_sint, m.ds_org_anal,
    m.tp_elaborazione, m.cd_stesura, m.fl_composito, m.fl_palinsesto,
    m.datazione, m.cd_presentazione, m.cd_materia, m.ds_illustrazioni,
    m.notazione_musicale, m.ds_legatura, m.ds_conservazione, m.tp_testo_letter,
    t.bid, t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura, t.cd_paese,
    t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1, t.aa_pubb_2,
    t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3, t.cd_genere_4,
    t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t, t.ky_cles1_ct,
    t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct, t.cd_livello, t.fl_speciale,
    t.isbd, t.indice_isbd, t.ky_editore, t.cd_agenzia, t.cd_norme_cat,
    t.nota_cat_tit, t.nota_inf_tit, t.bid_link, t.tp_link, t.ute_ins, t.ts_ins,
    t.ute_var, t.ts_var, t.ute_forza_ins, t.ute_forza_var, t.fl_canc,
    t.s105_tp_testo_letterario, t.s125_indicatore_testo,
    t.s140_tp_testo_letterario, t.s181_tp_forma_contenuto_1,
    t.s181_cd_tipo_contenuto_1, t.s181_cd_movimento_1, t.s181_cd_dimensione_1,
    t.s181_cd_sensoriale_1_1, t.s181_cd_sensoriale_2_1,
    t.s181_cd_sensoriale_3_1, t.s181_tp_forma_contenuto_2,
    t.s181_cd_tipo_contenuto_2, t.s181_cd_movimento_2, t.s181_cd_dimensione_2,
    t.s181_cd_sensoriale_1_2, t.s181_cd_sensoriale_2_2,
    t.s181_cd_sensoriale_3_2, t.s182_tp_mediazione_1, t.s182_tp_mediazione_2,
    t.cd_periodicita, t.fl_condiviso, t.ute_condiviso, t.ts_condiviso,
    t.s183_tp_supporto_1, t.s183_tp_supporto_2, t.s210_ind_pubblicato
FROM tb_musica m, v_daticomuni t
WHERE t.bid = m.bid AND m.fl_canc <> 'S';


--
-- Definition for view vl_audiovisivo_aut (OID = 626595) : 
--
CREATE VIEW sbnweb.vl_audiovisivo_aut AS
SELECT r.vid, r.tp_responsabilita, r.cd_relazione, r.nota_tit_aut,
    r.fl_incerto, r.fl_superfluo, r.cd_strumento_mus, v.tp_mater_audiovis,
    v.cd_forma_video, v.cd_tecnica, s.cd_forma, s.cd_velocita, t.bid,
    t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura, t.cd_paese,
    t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1, t.aa_pubb_2,
    t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3,
    t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t,
    t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit, t.nota_cat_tit,
    t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var,
    t.ute_forza_ins, t.ute_forza_var, t.fl_canc, t.cd_periodicita,
    t.fl_condiviso, t.ute_condiviso, t.ts_condiviso
FROM tr_tit_aut r, ((tb_titolo t LEFT JOIN tb_disco_sonoro s ON ((s.bid =
    t.bid))) LEFT JOIN tb_audiovideo v ON ((v.bid = t.bid)))
WHERE ((((((r.bid = t.bid) AND (r.fl_canc <> 'S'::bpchar)) AND (t.fl_canc
    <> 'S'::bpchar)) AND ((s.fl_canc <> 'S'::bpchar) OR (s.fl_canc IS
    NULL))) AND ((v.fl_canc <> 'S'::bpchar) OR (v.fl_canc IS NULL))) AND
    (NOT ((s.fl_canc IS NULL) AND (v.fl_canc IS NULL))));

--
-- Definition for view ve_audiovisivo_aut_aut (OID = 626600) : 
--
CREATE VIEW sbnweb.ve_audiovisivo_aut_aut AS
SELECT r.vid, r.tp_responsabilita, r.cd_relazione, r.nota_tit_aut,
    r.fl_incerto, r.fl_superfluo, r.cd_strumento_mus, t.bid, t.isadn,
    t.tp_materiale, t.tp_record_uni, t.cd_natura, t.cd_paese,
    t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1, t.aa_pubb_2,
    t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3,
    t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t,
    t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit, t.nota_cat_tit,
    t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var,
    t.ute_forza_ins, t.ute_forza_var, t.fl_canc, t.cd_periodicita,
    t.fl_condiviso, t.ute_condiviso, t.ts_condiviso
FROM tr_tit_aut r, tb_autore a, ((tb_titolo t LEFT JOIN tb_disco_sonoro s
    ON ((s.bid = t.bid))) LEFT JOIN tb_audiovideo v ON ((v.bid = t.bid)))
WHERE ((((((((r.vid = a.vid) AND (r.bid = t.bid)) AND (r.fl_canc <>
    'S'::bpchar)) AND (a.fl_canc <> 'S'::bpchar)) AND (t.fl_canc <>
    'S'::bpchar)) AND ((s.fl_canc <> 'S'::bpchar) OR (s.fl_canc IS NULL)))
    AND ((v.fl_canc <> 'S'::bpchar) OR (v.fl_canc IS NULL))) AND (NOT
    ((s.fl_canc IS NULL) AND (v.fl_canc IS NULL))));

--
-- Definition for view v_audiovisivo (OID = 626605) : 
--
CREATE VIEW sbnweb.v_audiovisivo AS
SELECT a.cd_livello AS cd_livello_a, a.tp_mater_audiovis, a.lunghezza,
    a.cd_colore, a.cd_suono, a.tp_media_suono, a.cd_dimensione,
    a.cd_forma_video, a.cd_tecnica, a.tp_formato_film, a.cd_mat_accomp_1,
    a.cd_mat_accomp_2, a.cd_mat_accomp_3, a.cd_mat_accomp_4,
    a.cd_forma_regist, a.tp_formato_video, a.cd_materiale_base,
    a.cd_supporto_second, a.cd_broadcast, a.tp_generazione, a.cd_elementi,
    a.cd_categ_colore, a.cd_polarita, a.cd_pellicola, a.tp_suono,
    a.tp_stampa_film, a.dt_ispezione, d.cd_forma, d.cd_velocita, d.tp_suono
    AS tp_suono_disco, d.cd_pista, d.cd_dimensione AS cd_dimensione_disco,
    d.cd_larg_nastro, d.cd_configurazione, d.cd_mater_accomp_1,
    d.cd_mater_accomp_2, d.cd_mater_accomp_3, d.cd_mater_accomp_4,
    d.cd_mater_accomp_5, d.cd_mater_accomp_6, d.cd_tecnica_regis,
    d.cd_riproduzione, d.tp_disco, d.tp_materiale AS tp_materiale_disco,
    d.tp_taglio, d.durata, t.bid, t.isadn, t.tp_materiale, t.tp_record_uni,
    t.cd_natura, t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3,
    t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2,
    t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t,
    t.ky_clet2_t, t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct,
    t.ky_clet2_ct, t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd,
    t.ky_editore, t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit,
    t.nota_cat_tit, t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var,
    t.ts_var, t.ute_forza_ins, t.ute_forza_var, t.fl_canc,
    t.cd_periodicita, t.fl_condiviso, t.ute_condiviso, t.ts_condiviso
FROM ((tb_titolo t LEFT JOIN tb_audiovideo a ON ((a.bid = t.bid))) LEFT
    JOIN tb_disco_sonoro d ON ((d.bid = t.bid)))
WHERE ((((t.fl_canc <> 'S'::bpchar) AND ((a.fl_canc <> 'S'::bpchar) OR
    (a.fl_canc IS NULL))) AND ((d.fl_canc <> 'S'::bpchar) OR (d.fl_canc IS
    NULL))) AND (NOT ((a.fl_canc IS NULL) AND (d.fl_canc IS NULL))));

--
-- Structure for table titolo (OID = 2214049) : 
--
-- Definition for index jms_messages_destination (OID = 77925) : 
--
CREATE INDEX jms_messages_destination ON jms_messages USING btree (destination);
--
-- Definition for index jms_messages_txop_txid (OID = 77926) : 
--
CREATE INDEX jms_messages_txop_txid ON jms_messages USING btree (txid);
--
-- Definition for index tb_autore_idx_auteur (OID = 77929) : 
--
CREATE INDEX tb_autore_idx_auteur ON tb_autore USING btree (ky_auteur);
--
-- Definition for index tb_autore_idx_cautun (OID = 77930) : 
--
CREATE INDEX tb_autore_idx_cautun ON tb_autore USING btree (ky_cautun, ky_el1_a, ky_el2_b);
--
-- Definition for index tb_autore_idx_ky_cles1_a (OID = 77931) : 
--
CREATE INDEX tb_autore_idx_ky_cles1_a ON tb_autore USING btree (ky_cles1_a);
--
-- Definition for index tb_autore_idx_ts_var (OID = 77932) : 
--
CREATE INDEX tb_autore_idx_ts_var ON tb_autore USING btree (ts_var);
--
-- Definition for index tb_descrittore_idx_ky_desc (OID = 77933) : 
--
CREATE INDEX tb_descrittore_idx_ky_desc ON tb_descrittore USING btree (ky_norm_descritt);
--
-- Definition for index tb_impronta_idx_imp1 (OID = 77934) : 
--
CREATE INDEX tb_impronta_idx_imp1 ON tb_impronta USING btree (impronta_1);
--
-- Definition for index tb_impronta_idx_imp2 (OID = 77935) : 
--
CREATE INDEX tb_impronta_idx_imp2 ON tb_impronta USING btree (impronta_2);
--
-- Definition for index tb_incipit_idx_bidlet (OID = 77936) : 
--
CREATE INDEX tb_incipit_idx_bidlet ON tb_incipit USING btree (bid_letterario);
--
-- Definition for index tb_luogo_idx_ky_luogo (OID = 77937) : 
--
CREATE INDEX tb_luogo_idx_ky_luogo ON tb_luogo USING btree (ky_luogo);
--
-- Definition for index tb_parola_idx_mid (OID = 77940) : 
--
CREATE INDEX tb_parola_idx_mid ON tb_parola USING btree (mid);
--
-- Definition for index tb_parola_idx_parola (OID = 77941) : 
--
CREATE INDEX tb_parola_idx_parola ON tb_parola USING btree (parola);
--
-- Definition for index tb_personaggio_idx_bid (OID = 77942) : 
--
CREATE INDEX tb_personaggio_idx_bid ON tb_personaggio USING btree (bid);
--
-- Definition for index tb_soggetto_idx_clses1 (OID = 77943) : 
--
CREATE INDEX tb_soggetto_idx_clses1 ON tb_soggetto USING btree (ky_cles1_s);
--
-- Definition for index tb_soggetto_tidx_vector_idx (OID = 77944) : 
--
CREATE INDEX tb_soggetto_tidx_vector_idx ON tb_soggetto USING gist (tidx_vector);
--
-- Definition for index tb_termine_thesauro_cd_the_did_key (OID = 77945) : 
--
CREATE UNIQUE INDEX tb_termine_thesauro_cd_the_did_key ON tb_termine_thesauro USING btree (cd_the, did);
--
-- Definition for index tb_titolo_idx_cles1t_clet2t_aapub (OID = 77946) : 
--
CREATE INDEX tb_titolo_idx_cles1t_clet2t_aapub ON tb_titolo USING btree (ky_cles1_t, ky_clet2_t, aa_pubb_1);
--
-- Definition for index tb_titolo_idx_ky_cles1_ct (OID = 77947) : 
--
CREATE INDEX tb_titolo_idx_ky_cles1_ct ON tb_titolo USING btree (ky_cles1_ct);
--
-- Definition for index tb_titolo_idx_ts_var (OID = 77948) : 
--
CREATE INDEX tb_titolo_idx_ts_var ON tb_titolo USING btree (ts_var);
--
-- Definition for index tba_ordini_idx_bid (OID = 77949) : 
--
CREATE INDEX tba_ordini_idx_bid ON tba_ordini USING btree (bid);
--
-- Definition for index tba_ordini_idx_sezione_acq (OID = 77950) : 
--
CREATE INDEX tba_ordini_idx_sezione_acq ON tba_ordini USING btree (id_sez_acquis_bibliografiche);
--
-- Definition for index tbc_collocazione_idx_bid (OID = 77951) : 
--
CREATE INDEX tbc_collocazione_idx_bid ON tbc_collocazione USING btree (bid, cd_biblioteca_sezione);
--
-- Definition for index tbc_collocazione_idx_coll (OID = 77953) : 
--
CREATE INDEX tbc_collocazione_idx_coll ON tbc_collocazione USING btree (cd_biblioteca_sezione, cd_sez, ord_loc, ord_spec);
--
-- Definition for index tbc_collocazione_idx_doc (OID = 77954) : 
--
CREATE INDEX tbc_collocazione_idx_doc ON tbc_collocazione USING btree (cd_biblioteca_doc, bid_doc, cd_doc);
--
-- Definition for index tbc_collocazione_ky_norm (OID = 77955) : 
--
CREATE INDEX tbc_collocazione_ky_norm ON tbc_collocazione USING btree (((rpad((ord_loc)::text, 80) || rpad((ord_spec)::text, 40))));
--
-- Definition for index tbc_esemplare_titolo_idx_bid (OID = 77956) : 
--
CREATE INDEX tbc_esemplare_titolo_idx_bid ON tbc_esemplare_titolo USING btree (bid, cd_biblioteca);
--
-- Definition for index tbc_inventario_idx_bid (OID = 77957) : 
--
CREATE INDEX tbc_inventario_idx_bid ON tbc_inventario USING btree (bid, cd_bib);
--
-- Definition for index tbc_inventario_idx_key_loc (OID = 77958) : 
--
CREATE INDEX tbc_inventario_idx_key_loc ON tbc_inventario USING btree (key_loc);
--
-- Definition for index tbc_inventario_idx_ordine (OID = 77959) : 
--
CREATE INDEX tbc_inventario_idx_ordine ON tbc_inventario USING btree (cd_bib_ord, cd_tip_ord, anno_ord, cd_ord);
--
-- Definition for index tbf_biblioteca_cdana_idx (OID = 77960) : 
--
--
-- Definition for index tbf_coda_jms_nome_jms_key (OID = 77961) : 
--
CREATE INDEX tbf_coda_jms_nome_jms_key ON tbf_coda_jms USING btree (nome_jms);
--
-- Definition for index tbf_utenti_professionali_web_userid (OID = 77962) : 
--
CREATE UNIQUE INDEX tbf_utenti_professionali_web_userid ON tbf_utenti_professionali_web USING btree (userid);
--
-- Definition for index tbl_solleciti_data_esito_idx (OID = 77963) : 
--
CREATE INDEX tbl_solleciti_data_esito_idx ON tbl_solleciti USING btree (cod_rich_serv, data_invio DESC) WHERE (esito = 'S'::bpchar);
--
-- Definition for index tbp_esemplare_fascicolo_bid_idx (OID = 77964) : 
--
CREATE INDEX tbp_esemplare_fascicolo_bid_idx ON tbp_esemplare_fascicolo USING btree (bid);
--
-- Definition for index tbp_esemplare_fascicolo_id_ordine_idx (OID = 77965) : 
--
CREATE INDEX tbp_esemplare_fascicolo_id_ordine_idx ON tbp_esemplare_fascicolo USING btree (id_ordine);
--
-- Definition for index tbp_esemplare_fascicolo_inventario_idx (OID = 77966) : 
--
CREATE INDEX tbp_esemplare_fascicolo_inventario_idx ON tbp_esemplare_fascicolo USING btree (cd_bib_inv, cd_serie, cd_inven);
--
-- Definition for index tbp_fascicolo_anno_pub_idx (OID = 77967) : 
--
CREATE INDEX tbp_fascicolo_anno_pub_idx ON tbp_fascicolo USING btree (anno_pub);
--
-- Definition for index tr_aut_aut_idx_vid_coll (OID = 77970) : 
--
CREATE INDEX tr_aut_aut_idx_vid_coll ON tr_aut_aut USING btree (vid_coll);
--
-- Definition for index tr_aut_mar_idx_mid (OID = 77971) : 
--
CREATE INDEX tr_aut_mar_idx_mid ON tr_aut_mar USING btree (mid);
--
-- Definition for index tr_des_des_idx_did_coll (OID = 77972) : 
--
CREATE INDEX tr_des_des_idx_did_coll ON tr_des_des USING btree (did_coll);
--
-- Definition for index tr_luo_luo_idx_lid_coll (OID = 77973) : 
--
CREATE INDEX tr_luo_luo_idx_lid_coll ON tr_luo_luo USING btree (lid_coll);
--
-- Definition for index tr_per_int_idx_id_pers (OID = 77974) : 
--
CREATE INDEX tr_per_int_idx_id_pers ON tr_per_int USING btree (id_personaggio);
--
-- Definition for index tr_rep_aut_idx_id_rep (OID = 77975) : 
--
CREATE INDEX tr_rep_aut_idx_id_rep ON tr_rep_aut USING btree (id_repertorio);
--
-- Definition for index tr_rep_mar_idx_id_rep (OID = 77976) : 
--
CREATE INDEX tr_rep_mar_idx_id_rep ON tr_rep_mar USING btree (id_repertorio);
--
-- Definition for index tr_rep_tit_idx_id_rep (OID = 77977) : 
--
CREATE INDEX tr_rep_tit_idx_id_rep ON tr_rep_tit USING btree (id_repertorio);
--
-- Definition for index tr_sog_des_idx_cid (OID = 77978) : 
--
CREATE INDEX tr_sog_des_idx_cid ON tr_sog_des USING btree (cid);
--
-- Definition for index tr_soggettari_biblioteche_sogg_idx (OID = 77979) : 
--
CREATE INDEX tr_soggettari_biblioteche_sogg_idx ON tr_soggettari_biblioteche USING btree (cd_sogg);
--
-- Definition for index tr_sogp_sogi_bid_idx (OID = 77980) : 
--
CREATE INDEX tr_sogp_sogi_bid_idx ON tr_sogp_sogi USING btree (bid);
--
-- Definition for index tr_sogp_sogi_cid_i_idx (OID = 77981) : 
--
CREATE INDEX tr_sogp_sogi_cid_i_idx ON tr_sogp_sogi USING btree (cid_i);
--
-- Definition for index tr_termini_termini_idx_did_coll (OID = 77982) : 
--
CREATE INDEX tr_termini_termini_idx_did_coll ON tr_termini_termini USING btree (did_coll);
--
-- Definition for index tr_tit_aut_idx_vid (OID = 77983) : 
--
CREATE INDEX tr_tit_aut_idx_vid ON tr_tit_aut USING btree (vid);
--
-- Definition for index tr_tit_cla_idx_classe (OID = 77984) : 
--
CREATE INDEX tr_tit_cla_idx_classe ON tr_tit_cla USING btree (cd_sistema, cd_edizione, classe);
--
-- Definition for index tr_tit_luo_idx_lid (OID = 77985) : 
--
CREATE INDEX tr_tit_luo_idx_lid ON tr_tit_luo USING btree (lid);
--
-- Definition for index tr_tit_mar_idx_mid (OID = 77986) : 
--
CREATE INDEX tr_tit_mar_idx_mid ON tr_tit_mar USING btree (mid);
--
-- Definition for index tr_tit_sog_bib_cid_bid_idx (OID = 77989) : 
--
CREATE INDEX tr_tit_sog_bib_cid_bid_idx ON tr_tit_sog_bib USING btree (cid, bid);
--
-- Definition for index tr_tit_sog_bib_bid_cid_idx (OID = 77991) : 
--
CREATE INDEX tr_tit_sog_bib_bid_cid_idx ON tr_tit_sog_bib USING btree (bid, cid);
--
-- Definition for index tr_tit_tit_idx_bid_coll (OID = 77992) : 
--
CREATE INDEX tr_tit_tit_idx_bid_coll ON tr_tit_tit USING btree (bid_coll);
--
-- Definition for index tr_tit_tit_idx_natura (OID = 77993) : 
--
CREATE INDEX tr_tit_tit_idx_natura ON tr_tit_tit USING btree (cd_natura_base, cd_natura_coll);
--
-- Definition for index tr_tit_tit_pkey (OID = 77994) : 
--
CREATE UNIQUE INDEX tr_tit_tit_pkey ON tr_tit_tit USING btree (bid_base, bid_coll, tp_legame);
--
-- Definition for index xpk_ordini (OID = 77995) : 
--
CREATE UNIQUE INDEX xpk_ordini ON tba_ordini USING btree (cod_tip_ord, anno_ord, cod_ord, cd_polo, cd_bib);
--
-- Definition for index tr_sog_des_fl_posizione_idx (OID = 184850) : 
--
CREATE INDEX tr_sog_des_fl_posizione_idx ON tr_sog_des USING btree (did, fl_posizione) WHERE ((fl_posizione > 0) AND (fl_primavoce <> 'M'::bpchar));
--
-- Definition for index import1_idx (OID = 186481) : 
--
CREATE INDEX import1_idx ON import1 USING btree (id_input, nr_richiesta);
--
-- Definition for index import_id_link_idx_id_input (OID = 186502) : 
--
CREATE UNIQUE INDEX import_id_link_idx_id_input ON import_id_link USING btree (nr_richiesta, id_record);
--
-- Definition for index tb_report_indice_id_locali_id_lista_id_oggetto_locale_key (OID = 186528) : 
--
CREATE UNIQUE INDEX tb_report_indice_id_locali_id_lista_id_oggetto_locale_key ON tb_report_indice_id_locali USING btree (id_lista, id_oggetto_locale);
--
-- Definition for index import1_id_input_idx (OID = 188354) : 
--
CREATE INDEX import1_id_input_idx ON import1 USING btree (id_input);
--
-- Definition for index tb_titolo_ute_ins_natura_idx (OID = 188355) : 
--
CREATE INDEX tb_titolo_ute_ins_natura_idx ON tb_titolo USING btree ("substring"((ute_ins)::text, 1, 6)) WHERE (cd_natura = 'R'::bpchar);
--
-- Definition for index tbc_inventario_num_carico_idx (OID = 188366) : 
--
CREATE INDEX tbc_inventario_num_carico_idx ON tbc_inventario USING btree (num_carico) WHERE (num_carico > 0);
--
-- Definition for index tbf_biblioteca_cdsbn_idx (OID = 188367) : 
--
CREATE UNIQUE INDEX tbf_biblioteca_cdsbn_idx ON tbf_biblioteca USING btree (cd_polo, cd_bib);
--
-- Definition for index tb_numero_std_idx_nstd (OID = 600091) : 
--
CREATE INDEX tb_numero_std_idx_nstd ON tb_numero_std USING btree (numero_std);
--
-- Definition for index tb_numero_std_idx_lastra (OID = 600092) : 
--
CREATE INDEX tb_numero_std_idx_lastra ON tb_numero_std USING btree (numero_lastra);
--
--
--
CREATE INDEX import_id_link_id_input_id_inserito_idx ON sbnweb.import_id_link
  USING btree (id_input, id_inserito);

CREATE INDEX import_id_link_id_input_idx ON sbnweb.import_id_link
  USING btree (id_input);

CREATE INDEX import1_idx1 ON sbnweb.import1
  USING btree (nr_richiesta,id_input,tag,stato_id_input);  
    
CREATE INDEX import1_idx2 ON sbnweb.import1
  USING btree (id_input)
  WHERE ((stato_id_input <> 'T') AND ((tag) = '200'));
  
CREATE INDEX inventario_idx ON sbnweb.trc_poss_prov_inventari
  USING btree (cd_polo, cd_biblioteca, cd_serie, cd_inven)
  WHERE (fl_canc <> 'S');
--
-- Definition for index id_relazione_tb_codici (OID = 77457) : 
--
ALTER TABLE ONLY trl_relazioni_servizi
    ADD CONSTRAINT id_relazione_tb_codici
    PRIMARY KEY (id);
--
-- Definition for index jms_messages_pkey (OID = 77459) : 
--
ALTER TABLE ONLY jms_messages
    ADD CONSTRAINT jms_messages_pkey
    PRIMARY KEY (messageid, destination);
--
-- Definition for index jms_transactions_pkey (OID = 77461) : 
--
ALTER TABLE ONLY jms_transactions
    ADD CONSTRAINT jms_transactions_pkey
    PRIMARY KEY (txid);
--
-- Definition for index pk_codici (OID = 77463) : 
--
ALTER TABLE ONLY tb_codici
    ADD CONSTRAINT pk_codici
    PRIMARY KEY (tp_tabella, cd_tabella);
--
-- Definition for index pk_link_multim (OID = 77473) : 
--
ALTER TABLE ONLY ts_link_multim
    ADD CONSTRAINT pk_link_multim
    PRIMARY KEY (id_link_multim);
--
-- Definition for index pk_nota_proposta (OID = 77475) : 
--
ALTER TABLE ONLY ts_note_proposta
    ADD CONSTRAINT pk_nota_proposta
    PRIMARY KEY (id_proposta, progr_risposta);
--
-- Definition for index pk_proposta (OID = 77479) : 
--
ALTER TABLE ONLY ts_proposta
    ADD CONSTRAINT pk_proposta
    PRIMARY KEY (ute_mittente, progr_proposta);
--
-- Definition for index pk_propostamarc (OID = 77481) : 
--
ALTER TABLE ONLY ts_proposta_marc
    ADD CONSTRAINT pk_propostamarc
    PRIMARY KEY (id_proposta);
--
-- Definition for index pk_tr_sogp_sogi_idx (OID = 77483) : 
--
ALTER TABLE ONLY tr_sogp_sogi
    ADD CONSTRAINT pk_tr_sogp_sogi_idx
    PRIMARY KEY (cid_p, cid_i, bid);
--
-- Definition for index pk_tsstoplist (OID = 77485) : 
--
ALTER TABLE ONLY ts_stop_list
    ADD CONSTRAINT pk_tsstoplist
    PRIMARY KEY (id_stop_list);
--
-- Definition for index tb_abstract_pkey (OID = 77487) : 
--
ALTER TABLE ONLY tb_abstract
    ADD CONSTRAINT tb_abstract_pkey
    PRIMARY KEY (bid);
--
-- Definition for index tb_arte_tridimens_pkey (OID = 77489) : 
--
ALTER TABLE ONLY tb_arte_tridimens
    ADD CONSTRAINT tb_arte_tridimens_pkey
    PRIMARY KEY (bid);
--
-- Definition for index tb_autore_pkey (OID = 77493) : 
--
ALTER TABLE ONLY tb_autore
    ADD CONSTRAINT tb_autore_pkey
    PRIMARY KEY (vid);
--
-- Definition for index tb_cartografia_pkey (OID = 77495) : 
--
ALTER TABLE ONLY tb_cartografia
    ADD CONSTRAINT tb_cartografia_pkey
    PRIMARY KEY (bid);
--
-- Definition for index tb_classe_pkey (OID = 77497) : 
--
ALTER TABLE ONLY tb_classe
    ADD CONSTRAINT tb_classe_pkey
    PRIMARY KEY (cd_sistema, cd_edizione, classe);
--
-- Definition for index tb_composizione_pkey (OID = 77499) : 
--
ALTER TABLE ONLY tb_composizione
    ADD CONSTRAINT tb_composizione_pkey
    PRIMARY KEY (bid);
--
-- Definition for index tb_descrittore_pkey (OID = 77501) : 
--
ALTER TABLE ONLY tb_descrittore
    ADD CONSTRAINT tb_descrittore_pkey
    PRIMARY KEY (did);
--
-- Definition for index tb_grafica_pkey (OID = 77505) : 
--
ALTER TABLE ONLY tb_grafica
    ADD CONSTRAINT tb_grafica_pkey
    PRIMARY KEY (bid);
--
-- Definition for index tb_impronta_pkey (OID = 77507) : 
--
ALTER TABLE ONLY tb_impronta
    ADD CONSTRAINT tb_impronta_pkey
    PRIMARY KEY (bid, impronta_1, impronta_2, impronta_3);
--
-- Definition for index tb_incipit_pkey (OID = 77509) : 
--
ALTER TABLE ONLY tb_incipit
    ADD CONSTRAINT tb_incipit_pkey
    PRIMARY KEY (bid, numero_mov, numero_p_mov);
--
-- Definition for index tb_luogo_pkey (OID = 77511) : 
--
ALTER TABLE ONLY tb_luogo
    ADD CONSTRAINT tb_luogo_pkey
    PRIMARY KEY (lid);
--
-- Definition for index tb_marca_pkey (OID = 77513) : 
--
ALTER TABLE ONLY tb_marca
    ADD CONSTRAINT tb_marca_pkey
    PRIMARY KEY (mid);
--
-- Definition for index tb_microforma_pkey (OID = 77515) : 
--
ALTER TABLE ONLY tb_microforma
    ADD CONSTRAINT tb_microforma_pkey
    PRIMARY KEY (bid);
--
-- Definition for index tb_musica_pkey (OID = 77517) : 
--
ALTER TABLE ONLY tb_musica
    ADD CONSTRAINT tb_musica_pkey
    PRIMARY KEY (bid);
--
-- Definition for index tb_nota_pkey (OID = 77519) : 
--
ALTER TABLE ONLY tb_nota
    ADD CONSTRAINT tb_nota_pkey
    PRIMARY KEY (bid, tp_nota, progr_nota);
--
-- Definition for index tb_parola_pkey (OID = 77521) : 
--
ALTER TABLE ONLY tb_parola
    ADD CONSTRAINT tb_parola_pkey
    PRIMARY KEY (id_parola);
--
-- Definition for index tb_personaggio_pkey (OID = 77523) : 
--
ALTER TABLE ONLY tb_personaggio
    ADD CONSTRAINT tb_personaggio_pkey
    PRIMARY KEY (id_personaggio);
--
-- Definition for index tb_rappresent_pkey (OID = 77525) : 
--
ALTER TABLE ONLY tb_rappresent
    ADD CONSTRAINT tb_rappresent_pkey
    PRIMARY KEY (bid);
--
-- Definition for index tb_repertorio_pkey (OID = 77527) : 
--
ALTER TABLE ONLY tb_repertorio
    ADD CONSTRAINT tb_repertorio_pkey
    PRIMARY KEY (id_repertorio);
--
-- Definition for index tb_soggetto_pkey (OID = 77531) : 
--
ALTER TABLE ONLY tb_soggetto
    ADD CONSTRAINT tb_soggetto_pkey
    PRIMARY KEY (cid);
--
-- Definition for index tb_stat_parameter_pkey (OID = 77533) : 
--
ALTER TABLE ONLY tb_stat_parameter
    ADD CONSTRAINT tb_stat_parameter_pkey
    PRIMARY KEY (nome, id_stat);
--
-- Definition for index tb_termine_thesauro_pkey (OID = 77535) : 
--
ALTER TABLE ONLY tb_termine_thesauro
    ADD CONSTRAINT tb_termine_thesauro_pkey
    PRIMARY KEY (did);
--
-- Definition for index tb_titolo_pkey (OID = 77537) : 
--
ALTER TABLE ONLY tb_titolo
    ADD CONSTRAINT tb_titolo_pkey
    PRIMARY KEY (bid);
--
-- Definition for index tba_buono_ordine_pkey (OID = 77539) : 
--
ALTER TABLE ONLY tba_buono_ordine
    ADD CONSTRAINT tba_buono_ordine_pkey
    PRIMARY KEY (id_buono_ordine);
--
-- Definition for index tba_cambi_ufficiali_pkey (OID = 77541) : 
--
ALTER TABLE ONLY tba_cambi_ufficiali
    ADD CONSTRAINT tba_cambi_ufficiali_pkey
    PRIMARY KEY (id_valuta);
--
-- Definition for index tba_fatture_pkey (OID = 77543) : 
--
ALTER TABLE ONLY tba_fatture
    ADD CONSTRAINT tba_fatture_pkey
    PRIMARY KEY (id_fattura);
--
-- Definition for index tba_offerte_fornitore_pkey (OID = 77545) : 
--
ALTER TABLE ONLY tba_offerte_fornitore
    ADD CONSTRAINT tba_offerte_fornitore_pkey
    PRIMARY KEY (id_offerte_fornitore);
--
-- Definition for index tba_ordini_pkey (OID = 77547) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT tba_ordini_pkey
    PRIMARY KEY (id_ordine);
--
-- Definition for index tba_parametri_buono_ordine_pkey (OID = 77549) : 
--
ALTER TABLE ONLY tba_parametri_buono_ordine
    ADD CONSTRAINT tba_parametri_buono_ordine_pkey
    PRIMARY KEY (cd_polo, cd_biblioteca, progr);
--
-- Definition for index tba_parametri_ordine_pkey (OID = 77551) : 
--
ALTER TABLE ONLY tba_parametri_ordine
    ADD CONSTRAINT tba_parametri_ordine_pkey
    PRIMARY KEY (cd_polo, cd_bib);
--
-- Definition for index tba_profili_acquisto_pkey (OID = 77553) : 
--
ALTER TABLE ONLY tba_profili_acquisto
    ADD CONSTRAINT tba_profili_acquisto_pkey
    PRIMARY KEY (cod_prac, id_sez_acquis_bibliografiche);
--
-- Definition for index tba_richieste_offerta_pkey (OID = 77555) : 
--
ALTER TABLE ONLY tba_richieste_offerta
    ADD CONSTRAINT tba_richieste_offerta_pkey
    PRIMARY KEY (cd_polo, cd_bib, cod_rich_off);
--
-- Definition for index tba_righe_fatture_pkey (OID = 77557) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT tba_righe_fatture_pkey
    PRIMARY KEY (id_fattura, riga_fattura);
--
-- Definition for index tba_sez_acquis_bibliografiche_pkey (OID = 77559) : 
--
ALTER TABLE ONLY tba_sez_acquis_bibliografiche
    ADD CONSTRAINT tba_sez_acquis_bibliografiche_pkey
    PRIMARY KEY (id_sez_acquis_bibliografiche);
--
-- Definition for index tba_suggerimenti_bibliografici_pkey (OID = 77561) : 
--
ALTER TABLE ONLY tba_suggerimenti_bibliografici
    ADD CONSTRAINT tba_suggerimenti_bibliografici_pkey
    PRIMARY KEY (cd_polo, cd_bib, cod_sugg_bibl);
--
-- Definition for index tbb_capitoli_bilanci_pkey (OID = 77563) : 
--
ALTER TABLE ONLY tbb_capitoli_bilanci
    ADD CONSTRAINT tbb_capitoli_bilanci_pkey
    PRIMARY KEY (id_capitoli_bilanci);
--
-- Definition for index tbc_collocazione_pkey (OID = 77565) : 
--
ALTER TABLE ONLY tbc_collocazione
    ADD CONSTRAINT tbc_collocazione_pkey
    PRIMARY KEY (key_loc);
--
-- Definition for index tbc_default_inven_schede_pkey (OID = 77567) : 
--
ALTER TABLE ONLY tbc_default_inven_schede
    ADD CONSTRAINT tbc_default_inven_schede_pkey
    PRIMARY KEY (id_default_inven_scheda);
--
-- Definition for index tbc_esemplare_titolo_pkey (OID = 77569) : 
--
ALTER TABLE ONLY tbc_esemplare_titolo
    ADD CONSTRAINT tbc_esemplare_titolo_pkey
    PRIMARY KEY (cd_doc, bid, cd_biblioteca, cd_polo);
--
-- Definition for index tbc_inventario_pkey (OID = 77571) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT tbc_inventario_pkey
    PRIMARY KEY (cd_polo, cd_bib, cd_serie, cd_inven);
--
-- Definition for index tbc_nota_inv_pkey (OID = 77573) : 
--
ALTER TABLE ONLY tbc_nota_inv
    ADD CONSTRAINT tbc_nota_inv_pkey
    PRIMARY KEY (cd_polo, cd_bib, cd_serie, cd_inven, cd_nota);
--
-- Definition for index tbc_possessore_provenienza_pkey (OID = 77575) : 
--
ALTER TABLE ONLY tbc_possessore_provenienza
    ADD CONSTRAINT tbc_possessore_provenienza_pkey
    PRIMARY KEY (pid);
--
-- Definition for index tbc_provenienza_inventario_pkey (OID = 77577) : 
--
ALTER TABLE ONLY tbc_provenienza_inventario
    ADD CONSTRAINT tbc_provenienza_inventario_pkey
    PRIMARY KEY (cd_proven, cd_biblioteca, cd_polo);
--
-- Definition for index tbc_serie_inventariale_pkey (OID = 77579) : 
--
ALTER TABLE ONLY tbc_serie_inventariale
    ADD CONSTRAINT tbc_serie_inventariale_pkey
    PRIMARY KEY (cd_serie, cd_biblioteca, cd_polo);
--
-- Definition for index tbc_sezione_collocazione_pkey (OID = 77581) : 
--
ALTER TABLE ONLY tbc_sezione_collocazione
    ADD CONSTRAINT tbc_sezione_collocazione_pkey
    PRIMARY KEY (cd_sez, cd_biblioteca, cd_polo);
--
-- Definition for index tbf_anagrafe_utenti_professionali_pkey (OID = 77583) : 
--
ALTER TABLE ONLY tbf_anagrafe_utenti_professionali
    ADD CONSTRAINT tbf_anagrafe_utenti_professionali_pkey
    PRIMARY KEY (id_utente_professionale);
--
-- Definition for index tbf_attivita_pkey (OID = 77585) : 
--
ALTER TABLE ONLY tbf_attivita
    ADD CONSTRAINT tbf_attivita_pkey
    PRIMARY KEY (cd_attivita);
--
-- Definition for index tbf_attivita_sbnmarc_pkey (OID = 77587) : 
--
ALTER TABLE ONLY tbf_attivita_sbnmarc
    ADD CONSTRAINT tbf_attivita_sbnmarc_pkey
    PRIMARY KEY (id_attivita_sbnmarc);
--
-- Definition for index tbf_batch_servizi_cd_attivita_key (OID = 77589) : 
--
ALTER TABLE ONLY tbf_batch_servizi
    ADD CONSTRAINT tbf_batch_servizi_cd_attivita_key
    UNIQUE (cd_attivita);
--
-- Definition for index tbf_batch_servizi_pkey (OID = 77591) : 
--
ALTER TABLE ONLY tbf_batch_servizi
    ADD CONSTRAINT tbf_batch_servizi_pkey
    PRIMARY KEY (id_batch_servizi);
--
-- Definition for index tbf_biblioteca_default_pkey (OID = 77593) : 
--
ALTER TABLE ONLY tbf_biblioteca_default
    ADD CONSTRAINT tbf_biblioteca_default_pkey
    PRIMARY KEY (id_default, cd_biblioteca, cd_polo);
--
-- Definition for index tbf_biblioteca_in_polo_pkey (OID = 77595) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT tbf_biblioteca_in_polo_pkey
    PRIMARY KEY (cd_biblioteca, cd_polo);
--
-- Definition for index tbf_biblioteca_pkey (OID = 77597) : 
--
ALTER TABLE ONLY tbf_biblioteca
    ADD CONSTRAINT tbf_biblioteca_pkey
    PRIMARY KEY (id_biblioteca);
--
-- Definition for index tbf_bibliotecario_default_pkey (OID = 77599) : 
--
ALTER TABLE ONLY tbf_bibliotecario_default
    ADD CONSTRAINT tbf_bibliotecario_default_pkey
    PRIMARY KEY (id_utente_professionale, id_default);
--
-- Definition for index tbf_bibliotecario_pkey (OID = 77601) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT tbf_bibliotecario_pkey
    PRIMARY KEY (id_utente_professionale);
--
-- Definition for index tbf_coda_jms_pkey (OID = 77603) : 
--
ALTER TABLE ONLY tbf_coda_jms
    ADD CONSTRAINT tbf_coda_jms_pkey
    PRIMARY KEY (id_coda);
--
-- Definition for index tbf_config_default_id_area_sezione_key (OID = 77605) : 
--
ALTER TABLE ONLY tbf_config_default
    ADD CONSTRAINT tbf_config_default_id_area_sezione_key
    UNIQUE (id_area_sezione);
--
-- Definition for index tbf_config_default_pkey (OID = 77607) : 
--
ALTER TABLE ONLY tbf_config_default
    ADD CONSTRAINT tbf_config_default_pkey
    PRIMARY KEY (id_config);
--
-- Definition for index tbf_config_statistiche_id_area_sezione_key (OID = 77609) : 
--
ALTER TABLE ONLY tbf_config_statistiche
    ADD CONSTRAINT tbf_config_statistiche_id_area_sezione_key
    UNIQUE (id_area_sezione);
--
-- Definition for index tbf_config_statistiche_pkey (OID = 77611) : 
--
ALTER TABLE ONLY tbf_config_statistiche
    ADD CONSTRAINT tbf_config_statistiche_pkey
    PRIMARY KEY (id_stat);
--
-- Definition for index tbf_contatore_pkey (OID = 77613) : 
--
ALTER TABLE ONLY tbf_contatore
    ADD CONSTRAINT tbf_contatore_pkey
    PRIMARY KEY (cd_polo, cd_biblioteca, cd_cont, anno, key1);
--
-- Definition for index tbf_default_key_key (OID = 77615) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT tbf_default_key_key
    UNIQUE (key);
--
-- Definition for index tbf_default_pkey (OID = 77617) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT tbf_default_pkey
    PRIMARY KEY (id_default);
--
-- Definition for index tbf_gruppi_default_pkey (OID = 77619) : 
--
ALTER TABLE ONLY tbf_gruppi_default
    ADD CONSTRAINT tbf_gruppi_default_pkey
    PRIMARY KEY (id);
--
-- Definition for index tbf_gruppo_attivita_pkey (OID = 77621) : 
--
ALTER TABLE ONLY tbf_gruppo_attivita
    ADD CONSTRAINT tbf_gruppo_attivita_pkey
    PRIMARY KEY (id_gruppo_attivita_polo);
--
-- Definition for index tbf_lingua_supportata_pkey (OID = 77623) : 
--
ALTER TABLE ONLY tbf_lingua_supportata
    ADD CONSTRAINT tbf_lingua_supportata_pkey
    PRIMARY KEY (cd_lingua);
--
-- Definition for index tbf_mail_pkey (OID = 77625) : 
--
ALTER TABLE ONLY tbf_mail
    ADD CONSTRAINT tbf_mail_pkey
    PRIMARY KEY (id);
--
-- Definition for index tbf_modelli_stampe_idx (OID = 77627) : 
--
ALTER TABLE ONLY tbf_modelli_stampe
    ADD CONSTRAINT tbf_modelli_stampe_idx
    UNIQUE (cd_polo, cd_bib, modello);
--
-- Definition for index tbf_modelli_stampe_pkey (OID = 77629) : 
--
ALTER TABLE ONLY tbf_modelli_stampe
    ADD CONSTRAINT tbf_modelli_stampe_pkey
    PRIMARY KEY (id_modello);
--
-- Definition for index tbf_modello_biblioteca_pkey (OID = 77631) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_biblioteca
    ADD CONSTRAINT tbf_modello_biblioteca_pkey
    PRIMARY KEY (id_modello);
--
-- Definition for index tbf_modello_bibliotecario_pkey (OID = 77633) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_bibliotecario
    ADD CONSTRAINT tbf_modello_bibliotecario_pkey
    PRIMARY KEY (id_modello);
--
-- Definition for index tbf_moduli_funzionali_pkey (OID = 77635) : 
--
ALTER TABLE ONLY tbf_moduli_funzionali
    ADD CONSTRAINT tbf_moduli_funzionali_pkey
    PRIMARY KEY (id_modulo);
--
-- Definition for index tbf_par_auth_pkey (OID = 77637) : 
--
ALTER TABLE ONLY tbf_par_auth
    ADD CONSTRAINT tbf_par_auth_pkey
    PRIMARY KEY (cd_par_auth, id_parametro);
--
-- Definition for index tbf_par_mat_pkey (OID = 77639) : 
--
ALTER TABLE ONLY tbf_par_mat
    ADD CONSTRAINT tbf_par_mat_pkey
    PRIMARY KEY (cd_par_mat, id_parametro);
--
-- Definition for index tbf_par_sem_pkey (OID = 77641) : 
--
ALTER TABLE ONLY tbf_par_sem
    ADD CONSTRAINT tbf_par_sem_pkey
    PRIMARY KEY (tp_tabella_codici, cd_tabella_codici, id_parametro);
--
-- Definition for index tbf_parametro_pkey (OID = 77643) : 
--
ALTER TABLE ONLY tbf_parametro
    ADD CONSTRAINT tbf_parametro_pkey
    PRIMARY KEY (id_parametro);
--
-- Definition for index tbf_polo_default_pkey (OID = 77645) : 
--
ALTER TABLE ONLY tbf_polo_default
    ADD CONSTRAINT tbf_polo_default_pkey
    PRIMARY KEY (id_default, cd_polo);
--
-- Definition for index tbf_polo_id_gruppo_attivita_key (OID = 77647) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT tbf_polo_id_gruppo_attivita_key
    UNIQUE (id_gruppo_attivita);
--
-- Definition for index tbf_polo_pkey (OID = 77649) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT tbf_polo_pkey
    PRIMARY KEY (cd_polo);
--
-- Definition for index tbf_procedure_output_pkey (OID = 77651) : 
--
ALTER TABLE ONLY tbf_procedure_output
    ADD CONSTRAINT tbf_procedure_output_pkey
    PRIMARY KEY (id);
--
-- Definition for index tbf_profilo_abilitazione_pkey (OID = 77653) : 
--
ALTER TABLE ONLY tbf_profilo_abilitazione
    ADD CONSTRAINT tbf_profilo_abilitazione_pkey
    PRIMARY KEY (id_profilo_abilitazione);
--
-- Definition for index tbf_utenti_professionali_web_pkey (OID = 77655) : 
--
ALTER TABLE ONLY tbf_utenti_professionali_web
    ADD CONSTRAINT tbf_utenti_professionali_web_pkey
    PRIMARY KEY (id_utente_professionale);
--
-- Definition for index tbl_calendario_servizi_pkey (OID = 77657) : 
--
ALTER TABLE ONLY tbl_calendario_servizi
    ADD CONSTRAINT tbl_calendario_servizi_pkey
    PRIMARY KEY (id_tipo_servizio, progr_fascia);
--
-- Definition for index tbl_controllo_attivita_pkey (OID = 77659) : 
--
ALTER TABLE ONLY tbl_controllo_attivita
    ADD CONSTRAINT tbl_controllo_attivita_pkey
    PRIMARY KEY (id_controllo_attivita);
--
-- Definition for index tbl_controllo_iter_pkey (OID = 77661) : 
--
ALTER TABLE ONLY tbl_controllo_iter
    ADD CONSTRAINT tbl_controllo_iter_pkey
    PRIMARY KEY (id_controllo_iter);
--
-- Definition for index tbl_disponibilita_precatalogati_pkey (OID = 77663) : 
--
ALTER TABLE ONLY tbl_disponibilita_precatalogati
    ADD CONSTRAINT tbl_disponibilita_precatalogati_pkey
    PRIMARY KEY (id_disponibilita_precatalogati);
--
-- Definition for index tbl_documenti_lettori_pkey (OID = 77665) : 
--
ALTER TABLE ONLY tbl_documenti_lettori
    ADD CONSTRAINT tbl_documenti_lettori_pkey
    PRIMARY KEY (id_documenti_lettore);
--
-- Definition for index tbl_esemplare_documento_lettore_pkey (OID = 77667) : 
--
ALTER TABLE ONLY tbl_esemplare_documento_lettore
    ADD CONSTRAINT tbl_esemplare_documento_lettore_pkey
    PRIMARY KEY (id_esemplare);
--
-- Definition for index tbl_iter_servizio_pkey (OID = 77669) : 
--
ALTER TABLE ONLY tbl_iter_servizio
    ADD CONSTRAINT tbl_iter_servizio_pkey
    PRIMARY KEY (id_iter_servizio);
--
-- Definition for index tbl_materie_pkey (OID = 77671) : 
--
ALTER TABLE ONLY tbl_materie
    ADD CONSTRAINT tbl_materie_pkey
    PRIMARY KEY (id_materia);
--
-- Definition for index tbl_modalita_erogazione_pkey (OID = 77675) : 
--
ALTER TABLE ONLY tbl_modalita_erogazione
    ADD CONSTRAINT tbl_modalita_erogazione_pkey
    PRIMARY KEY (id_modalita_erogazione);
--
-- Definition for index tbl_modalita_pagamento_pkey (OID = 77677) : 
--
ALTER TABLE ONLY tbl_modalita_pagamento
    ADD CONSTRAINT tbl_modalita_pagamento_pkey
    PRIMARY KEY (id_modalita_pagamento);
--
-- Definition for index tbl_occupazioni_pkey (OID = 77679) : 
--
ALTER TABLE ONLY tbl_occupazioni
    ADD CONSTRAINT tbl_occupazioni_pkey
    PRIMARY KEY (id_occupazioni);
--
-- Definition for index tbl_parametri_biblioteca_pkey (OID = 77681) : 
--
ALTER TABLE ONLY tbl_parametri_biblioteca
    ADD CONSTRAINT tbl_parametri_biblioteca_pkey
    PRIMARY KEY (id_parametri_biblioteca);
--
-- Definition for index tbl_penalita_servizio_pkey (OID = 77683) : 
--
ALTER TABLE ONLY tbl_penalita_servizio
    ADD CONSTRAINT tbl_penalita_servizio_pkey
    PRIMARY KEY (id_servizio);
--
-- Definition for index tbl_posti_sala_pkey (OID = 77685) : 
--
ALTER TABLE ONLY tbl_posti_sala
    ADD CONSTRAINT tbl_posti_sala_pkey
    PRIMARY KEY (id_posti_sala);
--
-- Definition for index tbl_richiesta_servizio_pkey (OID = 77687) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT tbl_richiesta_servizio_pkey
    PRIMARY KEY (cod_rich_serv);
--
-- Definition for index tbl_sale_pkey (OID = 77689) : 
--
ALTER TABLE ONLY tbl_sale
    ADD CONSTRAINT tbl_sale_pkey
    PRIMARY KEY (id_sale);

ALTER TABLE tbl_modello_calendario
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_polo, cd_bib)
    REFERENCES sbnweb.tbf_biblioteca_in_polo(cd_polo, cd_biblioteca)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;

ALTER TABLE tbl_modello_calendario
    ADD CONSTRAINT fk_sala FOREIGN KEY (id_sala)
    REFERENCES sbnweb.tbl_sale(id_sale)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;
--
-- Definition for index tbl_servizio_pkey (OID = 77691) : 
--
ALTER TABLE ONLY tbl_servizio
    ADD CONSTRAINT tbl_servizio_pkey
    PRIMARY KEY (id_servizio);
--
-- Definition for index tbl_servizio_web_dati_richiesti_pkey (OID = 77693) : 
--
ALTER TABLE ONLY tbl_servizio_web_dati_richiesti
    ADD CONSTRAINT tbl_servizio_web_dati_richiesti_pkey
    PRIMARY KEY (campo_richiesta, id_tipo_servizio);
--
-- Definition for index tbl_solleciti_pkey (OID = 77695) : 
--
ALTER TABLE ONLY tbl_solleciti
    ADD CONSTRAINT tbl_solleciti_pkey
    PRIMARY KEY (progr_sollecito, cod_rich_serv);
--
-- Definition for index tbl_specificita_titoli_studio_pkey (OID = 77697) : 
--
ALTER TABLE ONLY tbl_specificita_titoli_studio
    ADD CONSTRAINT tbl_specificita_titoli_studio_pkey
    PRIMARY KEY (id_specificita_titoli_studio);
--
-- Definition for index tbl_storico_richieste_servizio_pkey (OID = 77699) : 
--
ALTER TABLE ONLY tbl_storico_richieste_servizio
    ADD CONSTRAINT tbl_storico_richieste_servizio_pkey
    PRIMARY KEY (cd_polo, cd_bib, cod_rich_serv);
--
-- Definition for index tbl_supporti_biblioteca_pkey (OID = 77701) : 
--
ALTER TABLE ONLY tbl_supporti_biblioteca
    ADD CONSTRAINT tbl_supporti_biblioteca_pkey
    PRIMARY KEY (id_supporti_biblioteca);
--
-- Definition for index tbl_tipi_autorizzazioni_pkey (OID = 77703) : 
--
ALTER TABLE ONLY tbl_tipi_autorizzazioni
    ADD CONSTRAINT tbl_tipi_autorizzazioni_pkey
    PRIMARY KEY (id_tipi_autorizzazione);
	
ALTER TABLE tbl_parametri_biblioteca
    ADD CONSTRAINT fk_autorizzazione_ill FOREIGN KEY (id_autorizzazione_ill)
      REFERENCES sbnweb.tbl_tipi_autorizzazioni(id_tipi_autorizzazione)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
      NOT DEFERRABLE;	
--
-- Definition for index tbl_tipo_servizio_pkey (OID = 77705) : 
--
ALTER TABLE ONLY tbl_tipo_servizio
    ADD CONSTRAINT tbl_tipo_servizio_pkey
    PRIMARY KEY (id_tipo_servizio);
--
-- Definition for index tbl_utenti_cod_fiscale_key (OID = 77707) : 
--
ALTER TABLE ONLY tbl_utenti
    ADD CONSTRAINT tbl_utenti_cod_fiscale_key
    UNIQUE (cod_fiscale);
--
-- Definition for index tbl_utenti_ind_posta_elettr_key (OID = 77709) : 
--
ALTER TABLE ONLY tbl_utenti
    ADD CONSTRAINT tbl_utenti_ind_posta_elettr_key
    UNIQUE (ind_posta_elettr);
--
-- Definition for index tbl_utenti_pkey (OID = 77711) : 
--
ALTER TABLE ONLY tbl_utenti
    ADD CONSTRAINT tbl_utenti_pkey
    PRIMARY KEY (id_utenti);
--
-- Definition for index tbp_fascicolo_idx (OID = 77713) : 
--
ALTER TABLE ONLY tbp_fascicolo
    ADD CONSTRAINT tbp_fascicolo_idx
    PRIMARY KEY (bid, fid);
--
-- Definition for index tbq_batch_attivabili_pkey (OID = 77715) : 
--
ALTER TABLE ONLY tbq_batch_attivabili
    ADD CONSTRAINT tbq_batch_attivabili_pkey
    PRIMARY KEY (cod_funz);
--
-- Definition for index tbr_fornitori_biblioteche_pkey (OID = 77717) : 
--
ALTER TABLE ONLY tbr_fornitori_biblioteche
    ADD CONSTRAINT tbr_fornitori_biblioteche_pkey
    PRIMARY KEY (cd_polo, cd_biblioteca, cod_fornitore);
--
-- Definition for index tbr_fornitori_pkey (OID = 77719) : 
--
ALTER TABLE ONLY tbr_fornitori
    ADD CONSTRAINT tbr_fornitori_pkey
    PRIMARY KEY (cod_fornitore);
--
-- Definition for index tr_aut_aut_pkey (OID = 77721) : 
--
ALTER TABLE ONLY tr_aut_aut
    ADD CONSTRAINT tr_aut_aut_pkey
    PRIMARY KEY (vid_base, vid_coll, tp_legame);
--
-- Definition for index tr_aut_bib_pkey (OID = 77723) : 
--
ALTER TABLE ONLY tr_aut_bib
    ADD CONSTRAINT tr_aut_bib_pkey
    PRIMARY KEY (vid, cd_polo, cd_biblioteca);
--
-- Definition for index tr_aut_mar_pkey (OID = 77725) : 
--
ALTER TABLE ONLY tr_aut_mar
    ADD CONSTRAINT tr_aut_mar_pkey
    PRIMARY KEY (vid, mid);
--
-- Definition for index tr_des_des_pkey (OID = 77727) : 
--
ALTER TABLE ONLY tr_des_des
    ADD CONSTRAINT tr_des_des_pkey
    PRIMARY KEY (did_base, did_coll);
--
-- Definition for index tr_luo_luo_pkey (OID = 77729) : 
--
ALTER TABLE ONLY tr_luo_luo
    ADD CONSTRAINT tr_luo_luo_pkey
    PRIMARY KEY (lid_base, lid_coll);
--
-- Definition for index tr_mar_bib_pkey (OID = 77731) : 
--
ALTER TABLE ONLY tr_mar_bib
    ADD CONSTRAINT tr_mar_bib_pkey
    PRIMARY KEY (cd_polo, cd_biblioteca, mid);
--
-- Definition for index tr_modello_sistemi_classi_biblioteche_pkey (OID = 77733) : 
--
ALTER TABLE ONLY tr_modello_sistemi_classi_biblioteche
    ADD CONSTRAINT tr_modello_sistemi_classi_biblioteche_pkey
    PRIMARY KEY (id_modello, cd_sistema, cd_edizione);
--
-- Definition for index tr_modello_soggettari_biblioteche_pkey (OID = 77735) : 
--
ALTER TABLE ONLY tr_modello_soggettari_biblioteche
    ADD CONSTRAINT tr_modello_soggettari_biblioteche_pkey
    PRIMARY KEY (id_modello);
--
-- Definition for index tr_modello_thesauri_biblioteche_pkey (OID = 77737) : 
--
ALTER TABLE ONLY tr_modello_thesauri_biblioteche
    ADD CONSTRAINT tr_modello_thesauri_biblioteche_pkey
    PRIMARY KEY (id_modello);
--
-- Definition for index tr_per_int_pkey (OID = 77739) : 
--
ALTER TABLE ONLY tr_per_int
    ADD CONSTRAINT tr_per_int_pkey
    PRIMARY KEY (vid, id_personaggio);
--
-- Definition for index tr_rep_aut_pkey (OID = 77741) : 
--
ALTER TABLE ONLY tr_rep_aut
    ADD CONSTRAINT tr_rep_aut_pkey
    PRIMARY KEY (vid, id_repertorio);
--
-- Definition for index tr_rep_mar_pkey (OID = 77743) : 
--
ALTER TABLE ONLY tr_rep_mar
    ADD CONSTRAINT tr_rep_mar_pkey
    PRIMARY KEY (progr_repertorio, mid, id_repertorio);
--
-- Definition for index tr_rep_tit_pkey (OID = 77745) : 
--
ALTER TABLE ONLY tr_rep_tit
    ADD CONSTRAINT tr_rep_tit_pkey
    PRIMARY KEY (bid, id_repertorio);
--
-- Definition for index tr_sistemi_classi_biblioteche_idx (OID = 77747) : 
--
ALTER TABLE ONLY tr_sistemi_classi_biblioteche
    ADD CONSTRAINT tr_sistemi_classi_biblioteche_idx
    PRIMARY KEY (cd_biblioteca, cd_polo, cd_sistema, cd_edizione);
--
-- Definition for index tr_sog_des_pkey (OID = 77749) : 
--
ALTER TABLE ONLY tr_sog_des
    ADD CONSTRAINT tr_sog_des_pkey
    PRIMARY KEY (did, cid);
--
-- Definition for index tr_soggettari_biblioteche_pkey (OID = 77751) : 
--
ALTER TABLE ONLY tr_soggettari_biblioteche
    ADD CONSTRAINT tr_soggettari_biblioteche_pkey
    PRIMARY KEY (cd_sogg, cd_biblioteca, cd_polo);
--
-- Definition for index tr_termini_termini_pkey (OID = 77753) : 
--
ALTER TABLE ONLY tr_termini_termini
    ADD CONSTRAINT tr_termini_termini_pkey
    PRIMARY KEY (did_base, did_coll);
--
-- Definition for index tr_thesauri_biblioteche_pkey (OID = 77755) : 
--
ALTER TABLE ONLY tr_thesauri_biblioteche
    ADD CONSTRAINT tr_thesauri_biblioteche_pkey
    PRIMARY KEY (cd_biblioteca, cd_polo, cd_the);
--
-- Definition for index tr_tit_aut_pkey (OID = 77757) : 
--
ALTER TABLE ONLY tr_tit_aut
    ADD CONSTRAINT tr_tit_aut_pkey
    PRIMARY KEY (bid, vid, tp_responsabilita, cd_relazione);
--
-- Definition for index tr_tit_bib_pkey (OID = 77759) : 
--
ALTER TABLE ONLY tr_tit_bib
    ADD CONSTRAINT tr_tit_bib_pkey
    PRIMARY KEY (bid, cd_polo, cd_biblioteca);
--
-- Definition for index tr_tit_cla_pkey (OID = 77761) : 
--
ALTER TABLE ONLY tr_tit_cla
    ADD CONSTRAINT tr_tit_cla_pkey
    PRIMARY KEY (bid, classe, cd_sistema, cd_edizione);
--
-- Definition for index tr_tit_luo_pkey (OID = 77763) : 
--
ALTER TABLE ONLY tr_tit_luo
    ADD CONSTRAINT tr_tit_luo_pkey
    PRIMARY KEY (bid, lid, tp_luogo);
--
-- Definition for index tr_tit_mar_pkey (OID = 77767) : 
--
ALTER TABLE ONLY tr_tit_mar
    ADD CONSTRAINT tr_tit_mar_pkey
    PRIMARY KEY (bid, mid);
--
-- Definition for index tr_tit_sog_bib_pkey (OID = 77769) : 
--
ALTER TABLE ONLY tr_tit_sog_bib
    ADD CONSTRAINT tr_tit_sog_bib_pkey
    PRIMARY KEY (cid, cd_biblioteca, cd_polo, bid);
--
-- Definition for index tra_elementi_buono_ordine_pkey (OID = 77771) : 
--
ALTER TABLE ONLY tra_elementi_buono_ordine
    ADD CONSTRAINT tra_elementi_buono_ordine_pkey
    PRIMARY KEY (cd_polo, cd_bib, buono_ord, cod_tip_ord, anno_ord, cod_ord);
--
-- Definition for index tra_fornitori_offerte_pkey (OID = 77773) : 
--
ALTER TABLE ONLY tra_fornitori_offerte
    ADD CONSTRAINT tra_fornitori_offerte_pkey
    PRIMARY KEY (cd_polo, cd_bib, cod_fornitore, cod_rich_off);
--
-- Definition for index tra_messaggi_pkey (OID = 77775) : 
--
ALTER TABLE ONLY tra_messaggi
    ADD CONSTRAINT tra_messaggi_pkey
    PRIMARY KEY (cd_polo, cd_bib, cod_msg);
--
-- Definition for index tra_ordine_inventari_pkey (OID = 77777) : 
--
ALTER TABLE ONLY tra_ordine_inventari
    ADD CONSTRAINT tra_ordine_inventari_pkey
    PRIMARY KEY (id_ordine, cd_polo, cd_bib, cd_serie, cd_inven);
--
-- Definition for index tra_ordini_biblioteche_pkey (OID = 77779) : 
--
ALTER TABLE ONLY tra_ordini_biblioteche
    ADD CONSTRAINT tra_ordini_biblioteche_pkey
    PRIMARY KEY (cd_polo, cd_bib, cod_bib_ord, cod_tip_ord, anno_ord, cod_ord);
--
-- Definition for index tra_sez_acq_storico_pkey (OID = 77781) : 
--
ALTER TABLE ONLY tra_sez_acq_storico
    ADD CONSTRAINT tra_sez_acq_storico_pkey
    PRIMARY KEY (id_sez_acquis_bibliografiche, ts_ins);
--
-- Definition for index tra_sez_acquisizione_fornitori_pkey (OID = 77783) : 
--
ALTER TABLE ONLY tra_sez_acquisizione_fornitori
    ADD CONSTRAINT tra_sez_acquisizione_fornitori_pkey
    PRIMARY KEY (cd_polo, cd_biblioteca, cod_prac, cod_fornitore);
--
-- Definition for index trc_formati_sezioni_pkey (OID = 77785) : 
--
ALTER TABLE ONLY trc_formati_sezioni
    ADD CONSTRAINT trc_formati_sezioni_pkey
    PRIMARY KEY (cd_formato, cd_polo_sezione, cd_bib_sezione, cd_sezione);
--
-- Definition for index trc_poss_prov_inventari_pkey (OID = 77787) : 
--
ALTER TABLE ONLY trc_poss_prov_inventari
    ADD CONSTRAINT trc_poss_prov_inventari_pkey
    PRIMARY KEY (pid, cd_inven, cd_serie, cd_biblioteca, cd_polo, cd_legame);
--
-- Definition for index trf_attivita_affiliate_pkey (OID = 77789) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT trf_attivita_affiliate_pkey
    PRIMARY KEY (id);
--
-- Definition for index trf_attivita_polo_pkey (OID = 77791) : 
--
ALTER TABLE ONLY trf_attivita_polo
    ADD CONSTRAINT trf_attivita_polo_pkey
    PRIMARY KEY (id_attivita_polo);
--
-- Definition for index trf_funzioni_denominazioni_pkey (OID = 77793) : 
--
ALTER TABLE ONLY trf_funzioni_denominazioni
    ADD CONSTRAINT trf_funzioni_denominazioni_pkey
    PRIMARY KEY (cd_funzione, cd_lingua);
--
-- Definition for index trf_gruppo_attivita_pkey (OID = 77795) : 
--
ALTER TABLE ONLY trf_gruppo_attivita
    ADD CONSTRAINT trf_gruppo_attivita_pkey
    PRIMARY KEY (id_gruppo_attivita_polo_base, id_gruppo_attivita_polo_coll);
--
-- Definition for index trf_gruppo_attivita_polo_pkey (OID = 77797) : 
--
ALTER TABLE ONLY trf_gruppo_attivita_polo
    ADD CONSTRAINT trf_gruppo_attivita_polo_pkey
    PRIMARY KEY (id_attivita_polo, id_gruppo_attivita_polo);
--
-- Definition for index trf_profilo_biblioteca_pkey (OID = 77799) : 
--
ALTER TABLE ONLY trf_profilo_biblioteca
    ADD CONSTRAINT trf_profilo_biblioteca_pkey
    PRIMARY KEY (id_profilo_abilitazione_biblioteca);
--
-- Definition for index trf_profilo_profilo_pkey (OID = 77801) : 
--
ALTER TABLE ONLY trf_profilo_profilo
    ADD CONSTRAINT trf_profilo_profilo_pkey
    PRIMARY KEY (id_profilo_abilitazione_base, id_profilo_abilitazione_coll);
--
-- Definition for index trf_utente_professionale_biblioteca_pkey (OID = 77803) : 
--
ALTER TABLE ONLY trf_utente_professionale_biblioteca
    ADD CONSTRAINT trf_utente_professionale_biblioteca_pkey
    PRIMARY KEY (id_utente_professionale, cd_polo, cd_biblioteca);
--
-- Definition for index trf_utente_professionale_polo_pkey (OID = 77805) : 
--
ALTER TABLE ONLY trf_utente_professionale_polo
    ADD CONSTRAINT trf_utente_professionale_polo_pkey
    PRIMARY KEY (id_utente_professionale, cd_polo);
--
-- Definition for index trl_attivita_bibliotecario_pkey (OID = 77807) : 
--
ALTER TABLE ONLY trl_attivita_bibliotecario
    ADD CONSTRAINT trl_attivita_bibliotecario_pkey
    PRIMARY KEY (id_bibliotecario, id_iter_servizio);
--
-- Definition for index trl_autorizzazioni_servizi_pkey (OID = 77809) : 
--
ALTER TABLE ONLY trl_autorizzazioni_servizi
    ADD CONSTRAINT trl_autorizzazioni_servizi_pkey
    PRIMARY KEY (id_tipi_autorizzazione, id_servizio);
--
-- Definition for index trl_diritti_utente_pkey (OID = 77811) : 
--
ALTER TABLE ONLY trl_diritti_utente
    ADD CONSTRAINT trl_diritti_utente_pkey
    PRIMARY KEY (id_utenti, id_servizio);
--
-- Definition for index trl_materie_utenti_pkey (OID = 77813) : 
--
ALTER TABLE ONLY trl_materie_utenti
    ADD CONSTRAINT trl_materie_utenti_pkey
    PRIMARY KEY (id_utenti, id_materia);
--
-- Definition for index trl_posti_sala_utenti_biblioteca_pkey (OID = 77815) : 
--
ALTER TABLE ONLY trl_posti_sala_utenti_biblioteca
    ADD CONSTRAINT trl_posti_sala_utenti_biblioteca_pkey
    PRIMARY KEY (id_posti_sala, id_utenti_biblioteca);
--
-- Definition for index trl_supporti_modalita_erogazione_pkey (OID = 77817) : 
--
ALTER TABLE ONLY trl_supporti_modalita_erogazione
    ADD CONSTRAINT trl_supporti_modalita_erogazione_pkey
    PRIMARY KEY (cod_erog, id_supporti_biblioteca);
--
-- Definition for index trl_tariffe_modalita_erogazione_pkey (OID = 77819) : 
--
ALTER TABLE ONLY trl_tariffe_modalita_erogazione
    ADD CONSTRAINT trl_tariffe_modalita_erogazione_pkey
    PRIMARY KEY (cod_erog, id_tipo_servizio);
--
-- Definition for index trl_utenti_biblioteca_pkey (OID = 77821) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT trl_utenti_biblioteca_pkey
    PRIMARY KEY (id_utenti_biblioteca);
--
-- Definition for index trs_termini_titoli_biblioteche_pkey (OID = 77823) : 
--
ALTER TABLE ONLY trs_termini_titoli_biblioteche
    ADD CONSTRAINT trs_termini_titoli_biblioteche_pkey
    PRIMARY KEY (bid, cd_biblioteca, cd_polo, did);
--
-- Definition for index ts_servizio_pkey (OID = 77825) : 
--
ALTER TABLE ONLY ts_servizio
    ADD CONSTRAINT ts_servizio_pkey
    PRIMARY KEY (cd_record);
--
-- Definition for index unique_attivita_polo (OID = 77827) : 
--
ALTER TABLE ONLY trf_attivita_polo
    ADD CONSTRAINT unique_attivita_polo
    UNIQUE (cd_polo, cd_attivita);
--
-- Definition for index unique_parametri_biblioteca (OID = 77829) : 
--
ALTER TABLE ONLY tbl_parametri_biblioteca
    ADD CONSTRAINT unique_parametri_biblioteca
    UNIQUE (cd_polo, cd_bib);
--
-- Definition for index unique_tbc_default (OID = 77831) : 
--
ALTER TABLE ONLY tbc_default_inven_schede
    ADD CONSTRAINT unique_tbc_default
    UNIQUE (cd_biblioteca, cd_polo);
--
-- Definition for index univocita (OID = 77833) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT univocita
    UNIQUE (cd_attivita, cd_bib_affiliata, cd_bib_centro_sistema);
--
-- Definition for index xpk_bilanci (OID = 77835) : 
--
ALTER TABLE ONLY tbb_bilanci
    ADD CONSTRAINT xpk_bilanci
    PRIMARY KEY (cod_mat, id_capitoli_bilanci);
--
-- Definition for index xpk_buono_ordine (OID = 77837) : 
--
ALTER TABLE ONLY tba_buono_ordine
    ADD CONSTRAINT xpk_buono_ordine
    UNIQUE (buono_ord, cd_polo, cd_bib);
--
-- Definition for index xpk_cambi_ufficiali (OID = 77839) : 
--
ALTER TABLE ONLY tba_cambi_ufficiali
    ADD CONSTRAINT xpk_cambi_ufficiali
    UNIQUE (cd_polo, cd_bib, valuta);
--
-- Definition for index xpk_capitoli_bilanci (OID = 77841) : 
--
ALTER TABLE ONLY tbb_capitoli_bilanci
    ADD CONSTRAINT xpk_capitoli_bilanci
    UNIQUE (esercizio, capitolo, cd_polo, cd_bib);
--
-- Definition for index xpk_controllo_attivita (OID = 77845) : 
--
ALTER TABLE ONLY tbl_controllo_attivita
    ADD CONSTRAINT xpk_controllo_attivita
    UNIQUE (cd_bib, cd_polo, cod_attivita, cod_controllo);
--
-- Definition for index xpk_controllo_iter (OID = 77847) : 
--
ALTER TABLE ONLY tbl_controllo_iter
    ADD CONSTRAINT xpk_controllo_iter
    UNIQUE (id_iter_servizio, cod_controllo);
--
-- Definition for index xpk_disponibilita_precatalogati (OID = 77849) : 
--
ALTER TABLE ONLY tbl_disponibilita_precatalogati
    ADD CONSTRAINT xpk_disponibilita_precatalogati
    UNIQUE (cod_segn, cd_polo, cd_bib);
--
-- Definition for index xpk_dizionario_errori (OID = 77851) : 
--
ALTER TABLE ONLY tbf_dizionario_errori
    ADD CONSTRAINT xpk_dizionario_errori
    PRIMARY KEY (cd_lingua, cd_errore);
--
-- Definition for index xpk_documenti_lettori (OID = 77853) : 
--
ALTER TABLE ONLY tbl_documenti_lettori
    ADD CONSTRAINT xpk_documenti_lettori
    UNIQUE (tipo_doc_lett, cod_doc_lett, cd_polo, cd_bib);
--
-- Definition for index xpk_elementi_buono_ordine (OID = 77855) : 
--
ALTER TABLE ONLY tra_elementi_buono_ordine
    ADD CONSTRAINT xpk_elementi_buono_ordine
    UNIQUE (buono_ord, cod_tip_ord, anno_ord, cod_ord);
--
-- Definition for index xpk_esemplare_documento_lettore (OID = 77857) : 
--
ALTER TABLE ONLY tbl_esemplare_documento_lettore
    ADD CONSTRAINT xpk_esemplare_documento_lettore
    UNIQUE (prg_esemplare, id_documenti_lettore);
--
-- Definition for index xpk_fatture (OID = 77859) : 
--
ALTER TABLE ONLY tba_fatture
    ADD CONSTRAINT xpk_fatture
    UNIQUE (anno_fattura, progr_fattura, cd_polo, cd_bib);
--
-- Definition for index xpk_fornitori_offerte (OID = 77861) : 
--
ALTER TABLE ONLY tra_fornitori_offerte
    ADD CONSTRAINT xpk_fornitori_offerte
    UNIQUE (cod_fornitore, cod_rich_off);
--
-- Definition for index xpk_funzioni_denominazioni (OID = 77863) : 
--
ALTER TABLE ONLY trf_funzioni_denominazioni
    ADD CONSTRAINT xpk_funzioni_denominazioni
    UNIQUE (cd_lingua, cd_funzione);
--
-- Definition for index xpk_inventario (OID = 77865) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT xpk_inventario
    UNIQUE (cd_bib, cd_serie, cd_inven);
--
-- Definition for index xpk_iter (OID = 77867) : 
--
ALTER TABLE ONLY tbl_iter_servizio
    ADD CONSTRAINT xpk_iter
    UNIQUE (id_iter_servizio, progr_iter);
--
-- Definition for index xpk_materie (OID = 77869) : 
--
ALTER TABLE ONLY tbl_materie
    ADD CONSTRAINT xpk_materie
    UNIQUE (cod_mat, cd_polo, cd_biblioteca);
--
-- Definition for index xpk_messaggi (OID = 77871) : 
--
ALTER TABLE ONLY tra_messaggi
    ADD CONSTRAINT xpk_messaggi
    UNIQUE (cd_bib, cod_msg);
--
-- Definition for index xpk_modalita_erogazione (OID = 77873) : 
--
ALTER TABLE ONLY tbl_modalita_erogazione
    ADD CONSTRAINT xpk_modalita_erogazione
    UNIQUE (cd_bib, cod_erog, cd_polo);
--
-- Definition for index xpk_modalita_pagamento (OID = 77875) : 
--
ALTER TABLE ONLY tbl_modalita_pagamento
    ADD CONSTRAINT xpk_modalita_pagamento
    UNIQUE (cod_mod_pag, cd_polo, cd_bib);
--
-- Definition for index xpk_occupazioni (OID = 77877) : 
--
ALTER TABLE ONLY tbl_occupazioni
    ADD CONSTRAINT xpk_occupazioni
    UNIQUE (professione, occupazione, cd_polo, cd_biblioteca);
--
-- Definition for index xpk_offerte_fornitore (OID = 77879) : 
--
ALTER TABLE ONLY tba_offerte_fornitore
    ADD CONSTRAINT xpk_offerte_fornitore
    UNIQUE (bid_p, cd_polo, cd_bib);
--
-- Definition for index xpk_ordini_biblioteche (OID = 77881) : 
--
ALTER TABLE ONLY tra_ordini_biblioteche
    ADD CONSTRAINT xpk_ordini_biblioteche
    UNIQUE (cod_bib_ord, cod_tip_ord, anno_ord, cod_ord);
--
-- Definition for index xpk_possessori_possessori (OID = 77883) : 
--
ALTER TABLE ONLY trc_possessori_possessori
    ADD CONSTRAINT xpk_possessori_possessori
    PRIMARY KEY (pid_base, pid_coll);
--
-- Definition for index xpk_profiliabil_funzioni (OID = 77885) : 
--
ALTER TABLE ONLY trf_profilo_biblioteca
    ADD CONSTRAINT xpk_profiliabil_funzioni
    UNIQUE (cd_attivita, id_profilo_abilitazione_biblioteca);
--
-- Definition for index xpk_provenienza_inventario (OID = 77887) : 
--
ALTER TABLE ONLY tbc_provenienza_inventario
    ADD CONSTRAINT xpk_provenienza_inventario
    UNIQUE (cd_biblioteca, cd_proven);
--
-- Definition for index xpk_sale (OID = 77889) : 
--
ALTER TABLE ONLY tbl_sale
    ADD CONSTRAINT xpk_sale
    UNIQUE (cod_sala, cd_polo, cd_bib);
--
-- Definition for index xpk_serie_inventariale (OID = 77891) : 
--
ALTER TABLE ONLY tbc_serie_inventariale
    ADD CONSTRAINT xpk_serie_inventariale
    UNIQUE (cd_biblioteca, cd_serie);
--
-- Definition for index xpk_servizi (OID = 77893) : 
--
ALTER TABLE ONLY tbl_servizio
    ADD CONSTRAINT xpk_servizi
    UNIQUE (cod_servizio, id_tipo_servizio);
--
-- Definition for index xpk_sez_acquis_bibliografiche (OID = 77895) : 
--
ALTER TABLE ONLY tba_sez_acquis_bibliografiche
    ADD CONSTRAINT xpk_sez_acquis_bibliografiche
    UNIQUE (cd_polo, cd_bib, cod_sezione);
--
-- Definition for index xpk_sez_acquisizione_fornitori (OID = 77897) : 
--
ALTER TABLE ONLY tra_sez_acquisizione_fornitori
    ADD CONSTRAINT xpk_sez_acquisizione_fornitori
    UNIQUE (cod_prac, cod_fornitore);
--
-- Definition for index xpk_sezione_collocazione (OID = 77899) : 
--
ALTER TABLE ONLY tbc_sezione_collocazione
    ADD CONSTRAINT xpk_sezione_collocazione
    UNIQUE (cd_biblioteca, cd_sez);
--
-- Definition for index xpk_sig_repertorio (OID = 77901) : 
--
ALTER TABLE ONLY tb_repertorio
    ADD CONSTRAINT xpk_sig_repertorio
    UNIQUE (cd_sig_repertorio);
--
-- Definition for index xpk_supporti_biblioteca (OID = 77903) : 
--
ALTER TABLE ONLY tbl_supporti_biblioteca
    ADD CONSTRAINT xpk_supporti_biblioteca
    UNIQUE (cd_bib, cod_supporto, cd_polo);
--
-- Definition for index xpk_tbc_esemplare_titolo (OID = 77905) : 
--
ALTER TABLE ONLY tbc_esemplare_titolo
    ADD CONSTRAINT xpk_tbc_esemplare_titolo
    UNIQUE (cd_biblioteca, bid, cd_doc);
--
-- Definition for index xpk_tbf_intranet_range (OID = 77907) : 
--
ALTER TABLE ONLY tbf_intranet_range
    ADD CONSTRAINT xpk_tbf_intranet_range
    PRIMARY KEY (cd_bib, cd_polo, address);
--
-- Definition for index xpk_tipi_autorizzazioni (OID = 77909) : 
--
ALTER TABLE ONLY tbl_tipi_autorizzazioni
    ADD CONSTRAINT xpk_tipi_autorizzazioni
    UNIQUE (cod_tipo_aut, cd_polo, cd_bib);
--
-- Definition for index xpk_tipi_servizi (OID = 77911) : 
--
ALTER TABLE ONLY tbl_tipo_servizio
    ADD CONSTRAINT xpk_tipi_servizi
    UNIQUE (cod_tipo_serv, cd_polo, cd_bib);
--
-- Definition for index xpk_titoli_studio (OID = 77913) : 
--
ALTER TABLE ONLY tbl_specificita_titoli_studio
    ADD CONSTRAINT xpk_titoli_studio
    UNIQUE (tit_studio, specif_tit, cd_polo, cd_biblioteca);
--
-- Definition for index xpk_unique_tbf_profilo_abilitazione (OID = 77915) : 
--
ALTER TABLE ONLY tbf_profilo_abilitazione
    ADD CONSTRAINT xpk_unique_tbf_profilo_abilitazione
    UNIQUE (cd_prof, cd_biblioteca, cd_polo);
--
-- Definition for index xpk_utenti (OID = 77917) : 
--
ALTER TABLE ONLY tbl_utenti
    ADD CONSTRAINT xpk_utenti
    UNIQUE (cod_utente, cd_polo, cd_bib);
--
-- Definition for index xpk_utenti_biblioteca (OID = 77919) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT xpk_utenti_biblioteca
    UNIQUE (id_utenti, cd_polo, cd_biblioteca);
--
-- Definition for index xuk1_termine_thesauro (OID = 77921) : 
--
ALTER TABLE ONLY tb_termine_thesauro
    ADD CONSTRAINT xuk1_termine_thesauro
    UNIQUE (cd_the, ky_termine_thesauro);


CREATE UNIQUE INDEX gruppo_posto_idx ON sbnweb.tbl_posti_sala
  USING btree (id_sale, gruppo, num_posto);   
    
--
-- Definition for index fk_abilitazione (OID = 78010) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fk_abilitazione
    FOREIGN KEY (id_profilo_abilitazione) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fk_amministrazione (OID = 78015) : 
--
ALTER TABLE ONLY trf_utente_professionale_biblioteca
    ADD CONSTRAINT fk_amministrazione
    FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fk_amministrazione (OID = 78020) : 
--
ALTER TABLE ONLY trf_utente_professionale_polo
    ADD CONSTRAINT fk_amministrazione
    FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fk_anagrafe_utente_professionale (OID = 78025) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fk_anagrafe_utente_professionale
    FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fk_anagrafica_biblioteca (OID = 78030) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fk_anagrafica_biblioteca
    FOREIGN KEY (id_biblioteca) REFERENCES tbf_biblioteca(id_biblioteca);
--
-- Definition for index fk_attivita (OID = 78035) : 
--
ALTER TABLE ONLY trf_attivita_polo
    ADD CONSTRAINT fk_attivita
    FOREIGN KEY (cd_attivita) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fk_attivita (OID = 78040) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_attivita
    FOREIGN KEY (cd_attivita) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fk_attivita (OID = 78045) : 
--
ALTER TABLE ONLY trf_profilo_biblioteca
    ADD CONSTRAINT fk_attivita
    FOREIGN KEY (cd_attivita) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fk_attivita_polo (OID = 78050) : 
--
ALTER TABLE ONLY trf_gruppo_attivita_polo
    ADD CONSTRAINT fk_attivita_polo
    FOREIGN KEY (id_attivita_polo) REFERENCES trf_attivita_polo(id_attivita_polo);
--
-- Definition for index fk_attivita_sbnmarc (OID = 78055) : 
--
ALTER TABLE ONLY tbf_attivita
    ADD CONSTRAINT fk_attivita_sbnmarc
    FOREIGN KEY (id_attivita_sbnmarc) REFERENCES tbf_attivita_sbnmarc(id_attivita_sbnmarc);
--
-- Definition for index fk_autore (OID = 78060) : 
--
ALTER TABLE ONLY tr_aut_bib
    ADD CONSTRAINT fk_autore
    FOREIGN KEY (vid) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore (OID = 78065) : 
--
ALTER TABLE ONLY tr_aut_mar
    ADD CONSTRAINT fk_autore
    FOREIGN KEY (vid) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore (OID = 78070) : 
--
ALTER TABLE ONLY tr_per_int
    ADD CONSTRAINT fk_autore
    FOREIGN KEY (vid) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore (OID = 78075) : 
--
ALTER TABLE ONLY tr_rep_aut
    ADD CONSTRAINT fk_autore
    FOREIGN KEY (vid) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore (OID = 78080) : 
--
ALTER TABLE ONLY tr_tit_aut
    ADD CONSTRAINT fk_autore
    FOREIGN KEY (vid) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore_base (OID = 78085) : 
--
ALTER TABLE ONLY tr_aut_aut
    ADD CONSTRAINT fk_autore_base
    FOREIGN KEY (vid_base) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore_coll (OID = 78090) : 
--
ALTER TABLE ONLY tr_aut_aut
    ADD CONSTRAINT fk_autore_coll
    FOREIGN KEY (vid_coll) REFERENCES tb_autore(vid);
--
-- Definition for index fk_bib_polo_tbf_intranet_range (OID = 78095) : 
--
ALTER TABLE ONLY tbf_intranet_range
    ADD CONSTRAINT fk_bib_polo_tbf_intranet_range
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78100) : 
--
ALTER TABLE ONLY tbf_biblioteca_default
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78105) : 
--
ALTER TABLE ONLY tbf_contatore
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78110) : 
--
ALTER TABLE ONLY tbf_modelli_stampe
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78115) : 
--
ALTER TABLE ONLY tbf_profilo_abilitazione
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78120) : 
--
ALTER TABLE ONLY tbc_sezione_collocazione
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78125) : 
--
ALTER TABLE ONLY tbc_serie_inventariale
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78130) : 
--
ALTER TABLE ONLY tbc_provenienza_inventario
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78135) : 
--
ALTER TABLE ONLY tbc_esemplare_titolo
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78140) : 
--
ALTER TABLE ONLY tr_aut_bib
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78145) : 
--
ALTER TABLE ONLY tr_sistemi_classi_biblioteche
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78150) : 
--
ALTER TABLE ONLY tr_soggettari_biblioteche
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78155) : 
--
ALTER TABLE ONLY tr_thesauri_biblioteche
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78160) : 
--
ALTER TABLE ONLY tr_tit_bib
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78165) : 
--
ALTER TABLE ONLY tbl_modalita_erogazione
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_affiliata (OID = 78170) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_biblioteca_affiliata
    FOREIGN KEY (cd_bib_affiliata, cd_polo_bib_affiliata) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_cs (OID = 78175) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_biblioteca_cs
    FOREIGN KEY (cd_bib_centro_sistema, cd_polo_bib_centro_sistema) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_in_polo (OID = 78180) : 
--
ALTER TABLE ONLY tr_mar_bib
    ADD CONSTRAINT fk_biblioteca_in_polo
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_in_polo__tbl_controllo_attivita (OID = 78185) : 
--
ALTER TABLE ONLY tbl_controllo_attivita
    ADD CONSTRAINT fk_biblioteca_in_polo__tbl_controllo_attivita
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_in_polo__trl_relazione_servizi (OID = 78190) : 
--
ALTER TABLE ONLY trl_relazioni_servizi
    ADD CONSTRAINT fk_biblioteca_in_polo__trl_relazione_servizi
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_polo (OID = 78195) : 
--
ALTER TABLE ONLY trf_utente_professionale_biblioteca
    ADD CONSTRAINT fk_biblioteca_polo
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_bibliotecario (OID = 78200) : 
--
ALTER TABLE ONLY tbf_bibliotecario_default
    ADD CONSTRAINT fk_bibliotecario
    FOREIGN KEY (id_utente_professionale) REFERENCES tbf_bibliotecario(id_utente_professionale);
--
-- Definition for index fk_bilancio (OID = 78205) : 
--
ALTER TABLE ONLY tba_buono_ordine
    ADD CONSTRAINT fk_bilancio
    FOREIGN KEY (cod_mat, id_capitoli_bilanci) REFERENCES tbb_bilanci(cod_mat, id_capitoli_bilanci);
--
-- Definition for index fk_bilancio (OID = 78210) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT fk_bilancio
    FOREIGN KEY (tbb_bilancicod_mat, id_capitoli_bilanci) REFERENCES tbb_bilanci(cod_mat, id_capitoli_bilanci);
--
-- Definition for index fk_bilancio (OID = 78215) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT fk_bilancio
    FOREIGN KEY (cod_mat, id_capitoli_bilanci) REFERENCES tbb_bilanci(cod_mat, id_capitoli_bilanci);
--
-- Definition for index fk_cambio (OID = 78220) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT fk_cambio
    FOREIGN KEY (id_valuta) REFERENCES tba_cambi_ufficiali(id_valuta);
--
-- Definition for index fk_classe (OID = 78225) : 
--
ALTER TABLE ONLY tbc_collocazione
    ADD CONSTRAINT fk_classe
    FOREIGN KEY (cd_sistema, cd_edizione, classe) REFERENCES tb_classe(cd_sistema, cd_edizione, classe);
--
-- Definition for index fk_classe (OID = 78230) : 
--
ALTER TABLE ONLY tr_tit_cla
    ADD CONSTRAINT fk_classe
    FOREIGN KEY (cd_sistema, cd_edizione, classe) REFERENCES tb_classe(cd_sistema, cd_edizione, classe);
--
-- Definition for index fk_coda_jms (OID = 78235) : 
--
ALTER TABLE ONLY tbf_batch_servizi
    ADD CONSTRAINT fk_coda_jms
    FOREIGN KEY (id_coda_input) REFERENCES tbf_coda_jms(id_coda);
--
-- Definition for index fk_collocazione (OID = 78240) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT fk_collocazione
    FOREIGN KEY (key_loc) REFERENCES tbc_collocazione(key_loc);
--
-- Definition for index fk_config_default (OID = 78245) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT fk_config_default
    FOREIGN KEY (tbf_config_default__id_config) REFERENCES tbf_config_default(id_config);
--
-- Definition for index fk_default (OID = 78250) : 
--
ALTER TABLE ONLY tbf_biblioteca_default
    ADD CONSTRAINT fk_default
    FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fk_default (OID = 78255) : 
--
ALTER TABLE ONLY tbf_bibliotecario_default
    ADD CONSTRAINT fk_default
    FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fk_default (OID = 78260) : 
--
ALTER TABLE ONLY tbf_polo_default
    ADD CONSTRAINT fk_default
    FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fk_descrittore (OID = 78265) : 
--
ALTER TABLE ONLY tr_sog_des
    ADD CONSTRAINT fk_descrittore
    FOREIGN KEY (did) REFERENCES tb_descrittore(did);
--
-- Definition for index fk_descrittore_base (OID = 78270) : 
--
ALTER TABLE ONLY tr_des_des
    ADD CONSTRAINT fk_descrittore_base
    FOREIGN KEY (did_base) REFERENCES tb_descrittore(did);
--
-- Definition for index fk_descrittore_coll (OID = 78275) : 
--
ALTER TABLE ONLY tr_des_des
    ADD CONSTRAINT fk_descrittore_coll
    FOREIGN KEY (did_coll) REFERENCES tb_descrittore(did);
--
-- Definition for index fk_esemplare_titolo (OID = 78280) : 
--
ALTER TABLE ONLY tbc_collocazione
    ADD CONSTRAINT fk_esemplare_titolo
    FOREIGN KEY (cd_doc, bid_doc, cd_biblioteca_doc, cd_polo_doc) REFERENCES tbc_esemplare_titolo(cd_doc, bid, cd_biblioteca, cd_polo);
--
-- Definition for index fk_fornitore (OID = 78286) : 
--
ALTER TABLE ONLY tba_buono_ordine
    ADD CONSTRAINT fk_fornitore
    FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index fk_fornitore (OID = 78291) : 
--
ALTER TABLE ONLY tba_offerte_fornitore
    ADD CONSTRAINT fk_fornitore
    FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index fk_fornitore (OID = 78296) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT fk_fornitore
    FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index fk_fornitore (OID = 78301) : 
--
ALTER TABLE ONLY tba_fatture
    ADD CONSTRAINT fk_fornitore
    FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index fk_funzione (OID = 78306) : 
--
ALTER TABLE ONLY trf_funzioni_denominazioni
    ADD CONSTRAINT fk_funzione
    FOREIGN KEY (cd_funzione) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fk_grup_att_p_base (OID = 78311) : 
--
ALTER TABLE ONLY trf_gruppo_attivita
    ADD CONSTRAINT fk_grup_att_p_base
    FOREIGN KEY (id_gruppo_attivita_polo_base) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_grup_att_p_coll (OID = 78316) : 
--
ALTER TABLE ONLY trf_gruppo_attivita
    ADD CONSTRAINT fk_grup_att_p_coll
    FOREIGN KEY (id_gruppo_attivita_polo_coll) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_grup_att_polo (OID = 78321) : 
--
ALTER TABLE ONLY trf_gruppo_attivita_polo
    ADD CONSTRAINT fk_grup_att_polo
    FOREIGN KEY (id_gruppo_attivita_polo) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_gruppo_attivita (OID = 78326) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fk_gruppo_attivita
    FOREIGN KEY (id_gruppo_attivita_polo) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_gruppo_attivita (OID = 78331) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_biblioteca
    ADD CONSTRAINT fk_gruppo_attivita
    FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_gruppo_attivita (OID = 78336) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_bibliotecario
    ADD CONSTRAINT fk_gruppo_attivita
    FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_gruppo_attivita (OID = 78341) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT fk_gruppo_attivita
    FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_gruppo_default (OID = 78346) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT fk_gruppo_default
    FOREIGN KEY (tbf_gruppi_defaultid) REFERENCES tbf_gruppi_default(id);
--
-- Definition for index fk_inventario (OID = 78351) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fk_inventario
    FOREIGN KEY (cd_polo_inv, cod_bib_inv, cod_serie_inv, cod_inven_inv) REFERENCES tbc_inventario(cd_polo, cd_bib, cd_serie, cd_inven);
--
-- Definition for index fk_lingua (OID = 78356) : 
--
ALTER TABLE ONLY tbf_dizionario_errori
    ADD CONSTRAINT fk_lingua
    FOREIGN KEY (cd_lingua) REFERENCES tbf_lingua_supportata(cd_lingua);
--
-- Definition for index fk_lingua (OID = 78361) : 
--
ALTER TABLE ONLY trf_funzioni_denominazioni
    ADD CONSTRAINT fk_lingua
    FOREIGN KEY (cd_lingua) REFERENCES tbf_lingua_supportata(cd_lingua);
--
-- Definition for index fk_luogo (OID = 78366) : 
--
ALTER TABLE ONLY tr_tit_luo
    ADD CONSTRAINT fk_luogo
    FOREIGN KEY (lid) REFERENCES tb_luogo(lid);
--
-- Definition for index fk_luogo_base (OID = 78371) : 
--
ALTER TABLE ONLY tr_luo_luo
    ADD CONSTRAINT fk_luogo_base
    FOREIGN KEY (lid_base) REFERENCES tb_luogo(lid);
--
-- Definition for index fk_luogo_coll (OID = 78376) : 
--
ALTER TABLE ONLY tr_luo_luo
    ADD CONSTRAINT fk_luogo_coll
    FOREIGN KEY (lid_coll) REFERENCES tb_luogo(lid);
--
-- Definition for index fk_marca (OID = 78381) : 
--
ALTER TABLE ONLY tb_parola
    ADD CONSTRAINT fk_marca
    FOREIGN KEY (mid) REFERENCES tb_marca(mid);
--
-- Definition for index fk_marca (OID = 78386) : 
--
ALTER TABLE ONLY tr_aut_mar
    ADD CONSTRAINT fk_marca
    FOREIGN KEY (mid) REFERENCES tb_marca(mid);
--
-- Definition for index fk_marca (OID = 78391) : 
--
ALTER TABLE ONLY tr_mar_bib
    ADD CONSTRAINT fk_marca
    FOREIGN KEY (mid) REFERENCES tb_marca(mid);
--
-- Definition for index fk_marca (OID = 78396) : 
--
ALTER TABLE ONLY tr_rep_mar
    ADD CONSTRAINT fk_marca
    FOREIGN KEY (mid) REFERENCES tb_marca(mid);
--
-- Definition for index fk_marca (OID = 78401) : 
--
ALTER TABLE ONLY tr_tit_mar
    ADD CONSTRAINT fk_marca
    FOREIGN KEY (mid) REFERENCES tb_marca(mid);
--
-- Definition for index fk_ordine (OID = 78406) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT fk_ordine
    FOREIGN KEY (id_ordine) REFERENCES tba_ordini(id_ordine);
--
-- Definition for index fk_parametro (OID = 78411) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fk_parametro
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_parametro (OID = 78416) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fk_parametro
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_parametro (OID = 78421) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_biblioteca
    ADD CONSTRAINT fk_parametro
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_parametro (OID = 78426) : 
--
ALTER TABLE ONLY tbf_par_mat
    ADD CONSTRAINT fk_parametro
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_parametro (OID = 78431) : 
--
ALTER TABLE ONLY tbf_par_sem
    ADD CONSTRAINT fk_parametro
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_parametro (OID = 78436) : 
--
ALTER TABLE ONLY tbf_par_auth
    ADD CONSTRAINT fk_parametro
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_paramtetro (OID = 78441) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_bibliotecario
    ADD CONSTRAINT fk_paramtetro
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_personaggio (OID = 78446) : 
--
ALTER TABLE ONLY tr_per_int
    ADD CONSTRAINT fk_personaggio
    FOREIGN KEY (id_personaggio) REFERENCES tb_personaggio(id_personaggio);
--
-- Definition for index fk_polo (OID = 78451) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fk_polo
    FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_polo (OID = 78456) : 
--
ALTER TABLE ONLY tbf_gruppo_attivita
    ADD CONSTRAINT fk_polo
    FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_polo (OID = 78461) : 
--
ALTER TABLE ONLY tbf_polo_default
    ADD CONSTRAINT fk_polo
    FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_polo (OID = 78466) : 
--
ALTER TABLE ONLY trf_attivita_polo
    ADD CONSTRAINT fk_polo
    FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_poss_prov_base (OID = 78471) : 
--
ALTER TABLE ONLY trc_possessori_possessori
    ADD CONSTRAINT fk_poss_prov_base
    FOREIGN KEY (pid_base) REFERENCES tbc_possessore_provenienza(pid);
--
-- Definition for index fk_poss_prov_coll (OID = 78476) : 
--
ALTER TABLE ONLY trc_possessori_possessori
    ADD CONSTRAINT fk_poss_prov_coll
    FOREIGN KEY (pid_coll) REFERENCES tbc_possessore_provenienza(pid);
--
-- Definition for index fk_possessore_provenienza (OID = 78481) : 
--
ALTER TABLE ONLY trc_poss_prov_inventari
    ADD CONSTRAINT fk_possessore_provenienza
    FOREIGN KEY (pid) REFERENCES tbc_possessore_provenienza(pid);
--
-- Definition for index fk_profil_base (OID = 78486) : 
--
ALTER TABLE ONLY trf_profilo_profilo
    ADD CONSTRAINT fk_profil_base
    FOREIGN KEY (id_profilo_abilitazione_base) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fk_profil_coll (OID = 78491) : 
--
ALTER TABLE ONLY trf_profilo_profilo
    ADD CONSTRAINT fk_profil_coll
    FOREIGN KEY (id_profilo_abilitazione_coll) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fk_profilo_abilitazione (OID = 78496) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fk_profilo_abilitazione
    FOREIGN KEY (id_profilo_abilitazione) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fk_profilo_abilitazione (OID = 78501) : 
--
ALTER TABLE ONLY trf_profilo_biblioteca
    ADD CONSTRAINT fk_profilo_abilitazione
    FOREIGN KEY (id_profilo_abilitazione) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fk_provenienza_inventario (OID = 78506) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT fk_provenienza_inventario
    FOREIGN KEY (cd_proven, cd_biblioteca_proven, cd_polo_proven) REFERENCES tbc_provenienza_inventario(cd_proven, cd_biblioteca, cd_polo);
--
-- Definition for index fk_repertorio (OID = 78511) : 
--
ALTER TABLE ONLY tr_rep_aut
    ADD CONSTRAINT fk_repertorio
    FOREIGN KEY (id_repertorio) REFERENCES tb_repertorio(id_repertorio);
--
-- Definition for index fk_repertorio (OID = 78516) : 
--
ALTER TABLE ONLY tr_rep_mar
    ADD CONSTRAINT fk_repertorio
    FOREIGN KEY (id_repertorio) REFERENCES tb_repertorio(id_repertorio);
--
-- Definition for index fk_repertorio (OID = 78521) : 
--
ALTER TABLE ONLY tr_rep_tit
    ADD CONSTRAINT fk_repertorio
    FOREIGN KEY (id_repertorio) REFERENCES tb_repertorio(id_repertorio);
--
-- Definition for index fk_sale (OID = 78526) : 
--
ALTER TABLE ONLY tbl_posti_sala
    ADD CONSTRAINT fk_sale
    FOREIGN KEY (id_sale) REFERENCES tbl_sale(id_sale);

ALTER TABLE trl_posto_sala_categoria_mediazione
    ADD CONSTRAINT fk_posto FOREIGN KEY (id_posto_sala)
    REFERENCES sbnweb.tbl_posti_sala(id_posti_sala)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;
--
-- Definition for index fk_serie_inventariale (OID = 78531) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT fk_serie_inventariale
    FOREIGN KEY (cd_serie, cd_bib, cd_polo) REFERENCES tbc_serie_inventariale(cd_serie, cd_biblioteca, cd_polo);
--
-- Definition for index fk_sezione_acquisizione_bib (OID = 78536) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT fk_sezione_acquisizione_bib
    FOREIGN KEY (id_sez_acquis_bibliografiche) REFERENCES tba_sez_acquis_bibliografiche(id_sez_acquis_bibliografiche);
--
-- Definition for index fk_sezione_collocazione (OID = 78541) : 
--
ALTER TABLE ONLY tbc_collocazione
    ADD CONSTRAINT fk_sezione_collocazione
    FOREIGN KEY (cd_sez, cd_biblioteca_sezione, cd_polo_sezione) REFERENCES tbc_sezione_collocazione(cd_sez, cd_biblioteca, cd_polo);
--
-- Definition for index fk_soggetto (OID = 78546) : 
--
ALTER TABLE ONLY tr_sog_des
    ADD CONSTRAINT fk_soggetto
    FOREIGN KEY (cid) REFERENCES tb_soggetto(cid);
--
-- Definition for index fk_soggetto (OID = 78551) : 
--
ALTER TABLE ONLY tr_tit_sog_bib
    ADD CONSTRAINT fk_soggetto
    FOREIGN KEY (cid) REFERENCES tb_soggetto(cid);
--
-- Definition for index fk_tba_righe_fatture__tba_fatture (OID = 78556) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT fk_tba_righe_fatture__tba_fatture
    FOREIGN KEY (id_fattura) REFERENCES tba_fatture(id_fattura);
--
-- Definition for index fk_tba_righe_fatture__tba_fatture_credito (OID = 78561) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT fk_tba_righe_fatture__tba_fatture_credito
    FOREIGN KEY (id_fattura_in_credito) REFERENCES tba_fatture(id_fattura);
--
-- Definition for index fk_tba_righe_fatture__tbf_biblioteca_in_polo (OID = 78566) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT fk_tba_righe_fatture__tbf_biblioteca_in_polo
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_tbc_nota_inv__tbc_inventario (OID = 78571) : 
--
ALTER TABLE ONLY tbc_nota_inv
    ADD CONSTRAINT fk_tbc_nota_inv__tbc_inventario
    FOREIGN KEY (cd_polo, cd_bib, cd_serie, cd_inven) REFERENCES tbc_inventario(cd_polo, cd_bib, cd_serie, cd_inven);
--
-- Definition for index fk_tbf_biblioteca_default__tbf_biblioteca_in_polo (OID = 78576) : 
--
ALTER TABLE ONLY tbf_biblioteca_default
    ADD CONSTRAINT fk_tbf_biblioteca_default__tbf_biblioteca_in_polo
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_tbf_biblioteca_default__tbf_default (OID = 78581) : 
--
ALTER TABLE ONLY tbf_biblioteca_default
    ADD CONSTRAINT fk_tbf_biblioteca_default__tbf_default
    FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fk_tbf_biblioteca_in_polo__tbf_polo (OID = 78586) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fk_tbf_biblioteca_in_polo__tbf_polo
    FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_tbf_bibliotecario__tbf_bibliotecario_default (OID = 78591) : 
--
ALTER TABLE ONLY tbf_bibliotecario_default
    ADD CONSTRAINT fk_tbf_bibliotecario__tbf_bibliotecario_default
    FOREIGN KEY (id_utente_professionale) REFERENCES tbf_bibliotecario(id_utente_professionale);
--
-- Definition for index fk_tbf_default__tbf_bibliotecario_default (OID = 78596) : 
--
ALTER TABLE ONLY tbf_bibliotecario_default
    ADD CONSTRAINT fk_tbf_default__tbf_bibliotecario_default
    FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fk_tbf_default_inven_schede__tbf_biblioteca_in_polo (OID = 78601) : 
--
ALTER TABLE ONLY tbc_default_inven_schede
    ADD CONSTRAINT fk_tbf_default_inven_schede__tbf_biblioteca_in_polo
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_tbf_default_inven_schede__tbf_modelli_stampe (OID = 78606) : 
--
ALTER TABLE ONLY tbc_default_inven_schede
    ADD CONSTRAINT fk_tbf_default_inven_schede__tbf_modelli_stampe
    FOREIGN KEY (id_modello) REFERENCES tbf_modelli_stampe(id_modello);
--
-- Definition for index fk_tbf_gruppo_attivita (OID = 78611) : 
--
ALTER TABLE ONLY tbf_gruppo_attivita
    ADD CONSTRAINT fk_tbf_gruppo_attivita
    FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_tbf_modello_profilazione_biblioteca__tbf_gruppo_attivita (OID = 78616) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_biblioteca
    ADD CONSTRAINT fk_tbf_modello_profilazione_biblioteca__tbf_gruppo_attivita
    FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_tbf_modello_profilazione_biblioteca__tbf_parametro (OID = 78621) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_biblioteca
    ADD CONSTRAINT fk_tbf_modello_profilazione_biblioteca__tbf_parametro
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_tbf_modello_profilazione_bibliotecario__tbf_gruppo_attivita (OID = 78626) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_bibliotecario
    ADD CONSTRAINT fk_tbf_modello_profilazione_bibliotecario__tbf_gruppo_attivita
    FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_tbf_parametro (OID = 78636) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT fk_tbf_parametro
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_tbf_polo__tbf_parametro (OID = 78641) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT fk_tbf_polo__tbf_parametro
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_tbf_polo_default__tbf_polo (OID = 78646) : 
--
ALTER TABLE ONLY tbf_polo_default
    ADD CONSTRAINT fk_tbf_polo_default__tbf_polo
    FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_tbf_polo_tbf_gruppo_attivita (OID = 78651) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT fk_tbf_polo_tbf_gruppo_attivita
    FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_tbr_fornitori_biblioteche__tbf_biblioteca_in_polo (OID = 78656) : 
--
ALTER TABLE ONLY tbr_fornitori_biblioteche
    ADD CONSTRAINT fk_tbr_fornitori_biblioteche__tbf_biblioteca_in_polo
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_termine (OID = 78661) : 
--
ALTER TABLE ONLY trs_termini_titoli_biblioteche
    ADD CONSTRAINT fk_termine
    FOREIGN KEY (did) REFERENCES tb_termine_thesauro(did);
--
-- Definition for index fk_termine (OID = 78666) : 
--
ALTER TABLE ONLY tr_the_cla
    ADD CONSTRAINT fk_termine
    FOREIGN KEY (cd_the, did) REFERENCES tb_termine_thesauro(cd_the, did);
--
-- Definition for index fk_termine_base (OID = 78671) : 
--
ALTER TABLE ONLY tr_termini_termini
    ADD CONSTRAINT fk_termine_base
    FOREIGN KEY (did_base) REFERENCES tb_termine_thesauro(did);
--
-- Definition for index fk_termine_coll (OID = 78676) : 
--
ALTER TABLE ONLY tr_termini_termini
    ADD CONSTRAINT fk_termine_coll
    FOREIGN KEY (did_coll) REFERENCES tb_termine_thesauro(did);
--
-- Definition for index fk_thesauri_biblioteche (OID = 78681) : 
--
ALTER TABLE ONLY trs_termini_titoli_biblioteche
    ADD CONSTRAINT fk_thesauri_biblioteche
    FOREIGN KEY (cd_biblioteca, cd_polo, cd_the) REFERENCES tr_thesauri_biblioteche(cd_biblioteca, cd_polo, cd_the) DEFERRABLE INITIALLY DEFERRED;
--
-- Definition for index fk_titolo (OID = 78686) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);

ALTER TABLE ONLY sbnweb.tr_bid_altroid
  ADD CONSTRAINT fk_titolo FOREIGN KEY (bid)
    REFERENCES sbnweb.tb_titolo(bid)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;

COMMENT ON COLUMN sbnweb.tr_bid_altroid.bid
IS 'Identificativo del titolo in SBN';

COMMENT ON COLUMN sbnweb.tr_bid_altroid.cd_istituzione
IS 'Codice dell''ente cui appartiene il numero ID';

COMMENT ON COLUMN sbnweb.tr_bid_altroid.ist_id
IS 'ID assegnato dall''ente al titolo SBN';

--
-- Definition for index fk_titolo (OID = 78691) : 
--
ALTER TABLE ONLY tbc_esemplare_titolo
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78696) : 
--
ALTER TABLE ONLY tbc_collocazione
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78701) : 
--
ALTER TABLE ONLY tb_nota
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78706) : 
--
ALTER TABLE ONLY tr_tit_cla
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78711) : 
--
ALTER TABLE ONLY tb_abstract
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78716) : 
--
ALTER TABLE ONLY tb_arte_tridimens
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78726) : 
--
ALTER TABLE ONLY tb_cartografia
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78731) : 
--
ALTER TABLE ONLY tb_composizione
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78741) : 
--
ALTER TABLE ONLY tb_grafica
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78746) : 
--
ALTER TABLE ONLY tb_impronta
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78751) : 
--
ALTER TABLE ONLY tb_incipit
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78756) : 
--
ALTER TABLE ONLY tb_microforma
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78761) : 
--
ALTER TABLE ONLY tb_musica
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78766) : 
--
ALTER TABLE ONLY tb_numero_std
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78771) : 
--
ALTER TABLE ONLY tb_personaggio
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78776) : 
--
ALTER TABLE ONLY tb_rappresent
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78786) : 
--
ALTER TABLE ONLY tr_rep_tit
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78791) : 
--
ALTER TABLE ONLY tr_tit_aut
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78796) : 
--
ALTER TABLE ONLY tr_tit_bib
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78801) : 
--
ALTER TABLE ONLY tr_tit_mar
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78806) : 
--
ALTER TABLE ONLY tr_tit_sog_bib
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78811) : 
--
ALTER TABLE ONLY trs_termini_titoli_biblioteche
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78816) : 
--
ALTER TABLE ONLY tr_tit_luo
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo_base (OID = 78821) : 
--
ALTER TABLE ONLY tr_tit_tit
    ADD CONSTRAINT fk_titolo_base
    FOREIGN KEY (bid_base) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo_coll (OID = 78826) : 
--
ALTER TABLE ONLY tr_tit_tit
    ADD CONSTRAINT fk_titolo_coll
    FOREIGN KEY (bid_coll) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_trf_attivita_affiliate__tbf_attivita (OID = 78831) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_trf_attivita_affiliate__tbf_attivita
    FOREIGN KEY (cd_attivita) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fk_trf_attivita_affiliate__tbf_bilbioteca_in_polo (OID = 78836) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_trf_attivita_affiliate__tbf_bilbioteca_in_polo
    FOREIGN KEY (cd_bib_affiliata, cd_polo_bib_affiliata) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_trf_attivita_affiliate__tbf_bilbioteca_in_polo_cs (OID = 78841) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_trf_attivita_affiliate__tbf_bilbioteca_in_polo_cs
    FOREIGN KEY (cd_bib_centro_sistema, cd_polo_bib_centro_sistema) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_trf_attivita_polo__tbf_polo (OID = 78846) : 
--
ALTER TABLE ONLY trf_attivita_polo
    ADD CONSTRAINT fk_trf_attivita_polo__tbf_polo
    FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_trf_utente_professionale_polo__tbf_polo (OID = 78851) : 
--
ALTER TABLE ONLY trf_utente_professionale_polo
    ADD CONSTRAINT fk_trf_utente_professionale_polo__tbf_polo
    FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_ts_note_proposta (OID = 78861) : 
--
ALTER TABLE ONLY ts_note_proposta
    ADD CONSTRAINT fk_ts_note_proposta
    FOREIGN KEY (id_proposta) REFERENCES ts_proposta_marc(id_proposta);
--
-- Definition for index fk_utente (OID = 78866) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fk_utente
    FOREIGN KEY (id_utenti_biblioteca) REFERENCES trl_utenti_biblioteca(id_utenti_biblioteca);
--
-- Definition for index fk_dati_richiesta_ill : 
--	
ALTER TABLE tbl_messaggio
    ADD CONSTRAINT fk_dati_richiesta_ill
    FOREIGN KEY (id_dati_richiesta) REFERENCES sbnweb.tbl_dati_richiesta_ill(id_dati_richiesta)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;

ALTER TABLE tbl_dati_richiesta_ill
	ADD CONSTRAINT fk_documento FOREIGN KEY (id_documento)
	REFERENCES sbnweb.tbl_documenti_lettori(id_documenti_lettore)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE, 
	ADD CONSTRAINT fk_inventario FOREIGN KEY (cd_polo_inv, cd_bib_inv, cd_serie, cd_inven)
	REFERENCES sbnweb.tbc_inventario(cd_polo, cd_bib, cd_serie, cd_inven)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE, 
	ADD CONSTRAINT tbl_richiesta_servizio_fk FOREIGN KEY (cod_rich_serv)
	REFERENCES sbnweb.tbl_richiesta_servizio(cod_rich_serv)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE tbl_biblioteca_ill
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (id_biblioteca)
    REFERENCES sbnweb.tbf_biblioteca(id_biblioteca)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE, 
    ADD CONSTRAINT fk_utente FOREIGN KEY (id_utente)
    REFERENCES sbnweb.tbl_utenti(id_utenti)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;

ALTER TABLE tbl_prenotazione_posto
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_polo, cd_bib)
    REFERENCES sbnweb.tbf_biblioteca_in_polo(cd_polo, cd_biblioteca)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE, 
    ADD CONSTRAINT fk_posto FOREIGN KEY (id_posto)
    REFERENCES sbnweb.tbl_posti_sala(id_posti_sala)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE, 
    ADD CONSTRAINT fk_utente FOREIGN KEY (id_utente)
    REFERENCES sbnweb.tbl_utenti(id_utenti)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;

ALTER TABLE trl_richiesta_prenot_posto
    ADD CONSTRAINT fk_prenotazione FOREIGN KEY (id_prenotazione)
    REFERENCES sbnweb.tbl_prenotazione_posto(id_prenotazione)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE, 
    ADD CONSTRAINT fk_richiesta FOREIGN KEY (id_richiesta)
    REFERENCES sbnweb.tbl_richiesta_servizio(cod_rich_serv)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;

ALTER TABLE trl_utente_prenotazione_posto
    ADD CONSTRAINT fk_prenotazione FOREIGN KEY (id_prenotazione)
    REFERENCES sbnweb.tbl_prenotazione_posto(id_prenotazione)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE,
    ADD CONSTRAINT fk_utente FOREIGN KEY (id_utenti)
    REFERENCES sbnweb.tbl_utenti(id_utenti)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;

ALTER TABLE tbl_accesso_utente
    ADD CONSTRAINT fk_posto FOREIGN KEY (id_posto)
    REFERENCES sbnweb.tbl_posti_sala(id_posti_sala)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE, 
    ADD CONSTRAINT fk_utente FOREIGN KEY (id_utente)
    REFERENCES sbnweb.tbl_utenti(id_utenti)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE,
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_polo, cd_bib)
    REFERENCES sbnweb.tbf_biblioteca_in_polo(cd_polo, cd_biblioteca)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;
--
-- Definition for index fk_utente_professionale (OID = 78871) : 
--
ALTER TABLE ONLY tbf_utenti_professionali_web
    ADD CONSTRAINT fk_utente_professionale
    FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fktba_buono_611729 (OID = 78876) : 
--
ALTER TABLE ONLY tba_buono_ordine
    ADD CONSTRAINT fktba_buono_611729
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_cambi_245267 (OID = 78881) : 
--
ALTER TABLE ONLY tba_cambi_ufficiali
    ADD CONSTRAINT fktba_cambi_245267
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_fattur644716 (OID = 78886) : 
--
ALTER TABLE ONLY tba_fatture
    ADD CONSTRAINT fktba_fattur644716
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_offert971110 (OID = 78891) : 
--
ALTER TABLE ONLY tba_offerte_fornitore
    ADD CONSTRAINT fktba_offert971110
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_ordini880960 (OID = 78896) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT fktba_ordini880960
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_parame385714 (OID = 78901) : 
--
ALTER TABLE ONLY tba_parametri_buono_ordine
    ADD CONSTRAINT fktba_parame385714
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_profil407639 (OID = 78906) : 
--
ALTER TABLE ONLY tba_profili_acquisto
    ADD CONSTRAINT fktba_profil407639
    FOREIGN KEY (id_sez_acquis_bibliografiche) REFERENCES tba_sez_acquis_bibliografiche(id_sez_acquis_bibliografiche);
--
-- Definition for index fktba_richie478408 (OID = 78911) : 
--
ALTER TABLE ONLY tba_richieste_offerta
    ADD CONSTRAINT fktba_richie478408
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_sez_ac186160 (OID = 78916) : 
--
ALTER TABLE ONLY tba_sez_acquis_bibliografiche
    ADD CONSTRAINT fktba_sez_ac186160
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_sugger143075 (OID = 78921) : 
--
ALTER TABLE ONLY tba_suggerimenti_bibliografici
    ADD CONSTRAINT fktba_sugger143075
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_sugger779479 (OID = 78926) : 
--
ALTER TABLE ONLY tba_suggerimenti_bibliografici
    ADD CONSTRAINT fktba_sugger779479
    FOREIGN KEY (id_sez_acquis_bibliografiche) REFERENCES tba_sez_acquis_bibliografiche(id_sez_acquis_bibliografiche);
--
-- Definition for index fktbb_bilanc19911 (OID = 78931) : 
--
ALTER TABLE ONLY tbb_bilanci
    ADD CONSTRAINT fktbb_bilanc19911
    FOREIGN KEY (id_capitoli_bilanci) REFERENCES tbb_capitoli_bilanci(id_capitoli_bilanci);
--
-- Definition for index fktbb_bilanc588285 (OID = 78936) : 
--
ALTER TABLE ONLY tbb_bilanci
    ADD CONSTRAINT fktbb_bilanc588285
    FOREIGN KEY (id_buono_ordine) REFERENCES tba_buono_ordine(id_buono_ordine);
--
-- Definition for index fktbb_capito423841 (OID = 78941) : 
--
ALTER TABLE ONLY tbb_capitoli_bilanci
    ADD CONSTRAINT fktbb_capito423841
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbf_attivi513525 (OID = 78946) : 
--
ALTER TABLE ONLY tbf_attivita
    ADD CONSTRAINT fktbf_attivi513525
    FOREIGN KEY (id_attivita_sbnmarc) REFERENCES tbf_attivita_sbnmarc(id_attivita_sbnmarc);
--
-- Definition for index fktbf_attivi528588 (OID = 78951) : 
--
ALTER TABLE ONLY tbf_attivita_sbnmarc
    ADD CONSTRAINT fktbf_attivi528588
    FOREIGN KEY (id_modulo) REFERENCES tbf_moduli_funzionali(id_modulo);
--
-- Definition for index fktbf_batch_906013 (OID = 78956) : 
--
ALTER TABLE ONLY tbf_batch_servizi
    ADD CONSTRAINT fktbf_batch_906013
    FOREIGN KEY (id_coda_input) REFERENCES tbf_coda_jms(id_coda);
--
-- Definition for index fktbf_biblio257696 (OID = 78961) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fktbf_biblio257696
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fktbf_biblio263412 (OID = 78966) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fktbf_biblio263412
    FOREIGN KEY (id_gruppo_attivita_polo) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fktbf_biblio282801 (OID = 78971) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fktbf_biblio282801
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fktbf_biblio662399 (OID = 78976) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fktbf_biblio662399
    FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fktbf_defaul226174 (OID = 78981) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT fktbf_defaul226174
    FOREIGN KEY (tbf_config_default__id_config) REFERENCES tbf_config_default(id_config);
--
-- Definition for index fktbf_defaul612848 (OID = 78986) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT fktbf_defaul612848
    FOREIGN KEY (tbf_gruppi_defaultid) REFERENCES tbf_gruppi_default(id);
--
-- Definition for index fktbf_polo_d832889 (OID = 78991) : 
--
ALTER TABLE ONLY tbf_polo_default
    ADD CONSTRAINT fktbf_polo_d832889
    FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fktbf_utenti778235 (OID = 78996) : 
--
ALTER TABLE ONLY tbf_utenti_professionali_web
    ADD CONSTRAINT fktbf_utenti778235
    FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fktbl_calend852675 (OID = 79001) : 
--
ALTER TABLE ONLY tbl_calendario_servizi
    ADD CONSTRAINT fktbl_calend852675
    FOREIGN KEY (id_tipo_servizio) REFERENCES tbl_tipo_servizio(id_tipo_servizio);
--
-- Definition for index fktbl_contro119321 (OID = 79006) : 
--
ALTER TABLE ONLY tbl_controllo_iter
    ADD CONSTRAINT fktbl_contro119321
    FOREIGN KEY (id_iter_servizio) REFERENCES tbl_iter_servizio(id_iter_servizio);
--
-- Definition for index fktbl_dispon609206 (OID = 79011) : 
--
ALTER TABLE ONLY tbl_disponibilita_precatalogati
    ADD CONSTRAINT fktbl_dispon609206
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_docume207640 (OID = 79016) : 
--
ALTER TABLE ONLY tbl_documenti_lettori
    ADD CONSTRAINT fktbl_docume207640
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_docume481043 (OID = 79021) : 
--
ALTER TABLE ONLY tbl_documenti_lettori
    ADD CONSTRAINT fktbl_docume481043
    FOREIGN KEY (id_utenti) REFERENCES tbl_utenti(id_utenti);
--
-- Definition for index fktbl_esempl557991 (OID = 79026) : 
--
ALTER TABLE ONLY tbl_esemplare_documento_lettore
    ADD CONSTRAINT fktbl_esempl557991
    FOREIGN KEY (id_documenti_lettore) REFERENCES tbl_documenti_lettori(id_documenti_lettore);
--
-- Definition for index fktbl_iter_s982301 (OID = 79031) : 
--
ALTER TABLE ONLY tbl_iter_servizio
    ADD CONSTRAINT fktbl_iter_s982301
    FOREIGN KEY (id_tipo_servizio) REFERENCES tbl_tipo_servizio(id_tipo_servizio);
--
-- Definition for index fktbl_materi322980 (OID = 79036) : 
--
ALTER TABLE ONLY tbl_materie
    ADD CONSTRAINT fktbl_materi322980
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);

--
-- Definition for index fktbl_modali927831 (OID = 79046) : 
--
ALTER TABLE ONLY tbl_modalita_pagamento
    ADD CONSTRAINT fktbl_modali927831
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_occupa87068 (OID = 79051) : 
--
ALTER TABLE ONLY tbl_occupazioni
    ADD CONSTRAINT fktbl_occupa87068
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_parame979709 (OID = 79056) : 
--
ALTER TABLE ONLY tbl_parametri_biblioteca
    ADD CONSTRAINT fktbl_parame979709
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_penali472775 (OID = 79061) : 
--
ALTER TABLE ONLY tbl_penalita_servizio
    ADD CONSTRAINT fktbl_penali472775
    FOREIGN KEY (id_servizio) REFERENCES tbl_servizio(id_servizio);
--
-- Definition for index fktbl_richie37563 (OID = 79066) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie37563
    FOREIGN KEY (id_modalita_pagamento) REFERENCES tbl_modalita_pagamento(id_modalita_pagamento);
--
-- Definition for index fktbl_richie406785 (OID = 79071) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie406785
    FOREIGN KEY (id_supporti_biblioteca) REFERENCES tbl_supporti_biblioteca(id_supporti_biblioteca);
--
-- Definition for index fktbl_richie429472 (OID = 79076) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie429472
    FOREIGN KEY (id_esemplare_documenti_lettore) REFERENCES tbl_esemplare_documento_lettore(id_esemplare);
--
-- Definition for index fktbl_richie465824 (OID = 79081) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie465824
    FOREIGN KEY (id_iter_servizio) REFERENCES tbl_iter_servizio(id_iter_servizio);
--
-- Definition for index fktbl_richie852803 (OID = 79086) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie852803
    FOREIGN KEY (id_servizio) REFERENCES tbl_servizio(id_servizio);
--
-- Definition for index fktbl_richie949257 (OID = 79091) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie949257
    FOREIGN KEY (id_documenti_lettore) REFERENCES tbl_documenti_lettori(id_documenti_lettore);
--
-- Definition for index fktbl_sale66744 (OID = 79096) : 
--
ALTER TABLE ONLY tbl_sale
    ADD CONSTRAINT fktbl_sale66744
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_serviz725690 (OID = 79101) : 
--
ALTER TABLE ONLY tbl_servizio
    ADD CONSTRAINT fktbl_serviz725690
    FOREIGN KEY (id_tipo_servizio) REFERENCES tbl_tipo_servizio(id_tipo_servizio);
--
-- Definition for index fktbl_serviz805283 (OID = 79106) : 
--
ALTER TABLE ONLY tbl_servizio_web_dati_richiesti
    ADD CONSTRAINT fktbl_serviz805283
    FOREIGN KEY (id_tipo_servizio) REFERENCES tbl_tipo_servizio(id_tipo_servizio);
--
-- Definition for index fktbl_sollec949869 (OID = 79111) : 
--
ALTER TABLE ONLY tbl_solleciti
    ADD CONSTRAINT fktbl_sollec949869
    FOREIGN KEY (cod_rich_serv) REFERENCES tbl_richiesta_servizio(cod_rich_serv);
--
-- Definition for index fktbl_specif906127 (OID = 79116) : 
--
ALTER TABLE ONLY tbl_specificita_titoli_studio
    ADD CONSTRAINT fktbl_specif906127
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_suppor421679 (OID = 79121) : 
--
ALTER TABLE ONLY tbl_supporti_biblioteca
    ADD CONSTRAINT fktbl_suppor421679
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_tipi_a205716 (OID = 79126) : 
--
ALTER TABLE ONLY tbl_tipi_autorizzazioni
    ADD CONSTRAINT fktbl_tipi_a205716
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_tipo_s781992 (OID = 79131) : 
--
ALTER TABLE ONLY tbl_tipo_servizio
    ADD CONSTRAINT fktbl_tipo_s781992
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_utenti822434 (OID = 79136) : 
--
ALTER TABLE ONLY tbl_utenti
    ADD CONSTRAINT fktbl_utenti822434
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktra_elemen653206 (OID = 79141) : 
--
ALTER TABLE ONLY tra_elementi_buono_ordine
    ADD CONSTRAINT fktra_elemen653206
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktra_fornit439052 (OID = 79146) : 
--
ALTER TABLE ONLY tra_fornitori_offerte
    ADD CONSTRAINT fktra_fornit439052
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktra_ordine161951 (OID = 79151) : 
--
ALTER TABLE ONLY tra_ordine_inventari
    ADD CONSTRAINT fktra_ordine161951
    FOREIGN KEY (id_ordine) REFERENCES tba_ordini(id_ordine);
--
-- Definition for index fktra_ordine458689 (OID = 79156) : 
--
ALTER TABLE ONLY tra_ordine_inventari
    ADD CONSTRAINT fktra_ordine458689
    FOREIGN KEY (cd_polo, cd_bib, cd_serie, cd_inven) REFERENCES tbc_inventario(cd_polo, cd_bib, cd_serie, cd_inven);
--
-- Definition for index fktra_ordini871255 (OID = 79161) : 
--
ALTER TABLE ONLY tra_ordini_biblioteche
    ADD CONSTRAINT fktra_ordini871255
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktra_sez_ac542862 (OID = 79166) : 
--
ALTER TABLE ONLY tra_sez_acquisizione_fornitori
    ADD CONSTRAINT fktra_sez_ac542862
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktrc_format930264 (OID = 79171) : 
--
ALTER TABLE ONLY trc_formati_sezioni
    ADD CONSTRAINT fktrc_format930264
    FOREIGN KEY (cd_sezione, cd_bib_sezione, cd_polo_sezione) REFERENCES tbc_sezione_collocazione(cd_sez, cd_biblioteca, cd_polo);
--
-- Definition for index fktrc_poss_p572114 (OID = 79176) : 
--
ALTER TABLE ONLY trc_poss_prov_inventari
    ADD CONSTRAINT fktrc_poss_p572114
    FOREIGN KEY (cd_polo, cd_biblioteca, cd_serie, cd_inven) REFERENCES tbc_inventario(cd_polo, cd_bib, cd_serie, cd_inven);
--
-- Definition for index fktrf_gruppo396996 (OID = 79181) : 
--
ALTER TABLE ONLY trf_gruppo_attivita_polo
    ADD CONSTRAINT fktrf_gruppo396996
    FOREIGN KEY (id_gruppo_attivita_polo) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fktrf_gruppo699658 (OID = 79186) : 
--
ALTER TABLE ONLY trf_gruppo_attivita
    ADD CONSTRAINT fktrf_gruppo699658
    FOREIGN KEY (id_gruppo_attivita_polo_coll) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fktrf_gruppo71976 (OID = 79191) : 
--
ALTER TABLE ONLY trf_gruppo_attivita_polo
    ADD CONSTRAINT fktrf_gruppo71976
    FOREIGN KEY (id_attivita_polo) REFERENCES trf_attivita_polo(id_attivita_polo);
--
-- Definition for index fktrf_gruppo742693 (OID = 79196) : 
--
ALTER TABLE ONLY trf_gruppo_attivita
    ADD CONSTRAINT fktrf_gruppo742693
    FOREIGN KEY (id_gruppo_attivita_polo_base) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fktrf_profil391503 (OID = 79201) : 
--
ALTER TABLE ONLY trf_profilo_biblioteca
    ADD CONSTRAINT fktrf_profil391503
    FOREIGN KEY (cd_attivita) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fktrf_profil634088 (OID = 79206) : 
--
ALTER TABLE ONLY trf_profilo_profilo
    ADD CONSTRAINT fktrf_profil634088
    FOREIGN KEY (id_profilo_abilitazione_coll) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fktrf_profil677123 (OID = 79211) : 
--
ALTER TABLE ONLY trf_profilo_profilo
    ADD CONSTRAINT fktrf_profil677123
    FOREIGN KEY (id_profilo_abilitazione_base) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fktrl_attivi227853 (OID = 79216) : 
--
ALTER TABLE ONLY trl_attivita_bibliotecario
    ADD CONSTRAINT fktrl_attivi227853
    FOREIGN KEY (id_iter_servizio) REFERENCES tbl_iter_servizio(id_iter_servizio);
--
-- Definition for index fktrl_attivi318007 (OID = 79221) : 
--
ALTER TABLE ONLY trl_attivita_bibliotecario
    ADD CONSTRAINT fktrl_attivi318007
    FOREIGN KEY (id_bibliotecario) REFERENCES tbf_bibliotecario(id_utente_professionale);
--
-- Definition for index fktrl_autori405945 (OID = 79231) : 
--
ALTER TABLE ONLY trl_autorizzazioni_servizi
    ADD CONSTRAINT fktrl_autori405945
    FOREIGN KEY (id_servizio) REFERENCES tbl_servizio(id_servizio);
--
-- Definition for index fktrl_materi231212 (OID = 79246) : 
--
ALTER TABLE ONLY trl_materie_utenti
    ADD CONSTRAINT fktrl_materi231212
    FOREIGN KEY (id_utenti) REFERENCES tbl_utenti(id_utenti);
--
-- Definition for index fktrl_materi669672 (OID = 79251) : 
--
ALTER TABLE ONLY trl_materie_utenti
    ADD CONSTRAINT fktrl_materi669672
    FOREIGN KEY (id_materia) REFERENCES tbl_materie(id_materia);
--
-- Definition for index fktrl_posti_538726 (OID = 79256) : 
--
ALTER TABLE ONLY trl_posti_sala_utenti_biblioteca
    ADD CONSTRAINT fktrl_posti_538726
    FOREIGN KEY (id_utenti_biblioteca) REFERENCES trl_utenti_biblioteca(id_utenti_biblioteca);
--
-- Definition for index fktrl_posti_810844 (OID = 79261) : 
--
ALTER TABLE ONLY trl_posti_sala_utenti_biblioteca
    ADD CONSTRAINT fktrl_posti_810844
    FOREIGN KEY (id_posti_sala) REFERENCES tbl_posti_sala(id_posti_sala);
--
-- Definition for index fktrl_tariff204281 (OID = 79266) : 
--
ALTER TABLE ONLY trl_supporti_modalita_erogazione
    ADD CONSTRAINT fktrl_tariff204281
    FOREIGN KEY (id_supporti_biblioteca) REFERENCES tbl_supporti_biblioteca(id_supporti_biblioteca);
--
-- Definition for index fktrl_tariff209538 (OID = 79271) : 
--
ALTER TABLE ONLY trl_tariffe_modalita_erogazione
    ADD CONSTRAINT fktrl_tariff209538
    FOREIGN KEY (id_tipo_servizio) REFERENCES tbl_tipo_servizio(id_tipo_servizio);
--
-- Definition for index fktrl_utenti295593 (OID = 79276) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT fktrl_utenti295593
    FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktrl_utenti668699 (OID = 79281) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT fktrl_utenti668699
    FOREIGN KEY (id_utenti) REFERENCES tbl_utenti(id_utenti);
--
-- Definition for index fktrl_utenti927209 (OID = 79286) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT fktrl_utenti927209
    FOREIGN KEY (id_specificita_titoli_studio) REFERENCES tbl_specificita_titoli_studio(id_specificita_titoli_studio);
--
-- Definition for index fktrl_utenti935080 (OID = 79291) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT fktrl_utenti935080
    FOREIGN KEY (id_occupazioni) REFERENCES tbl_occupazioni(id_occupazioni);
--
-- Definition for index tbc_inventario_id_bib_scar (OID = 79296) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT tbc_inventario_id_bib_scar
    FOREIGN KEY (id_bib_scar) REFERENCES tbf_biblioteca(id_biblioteca);
--
-- Definition for index tbf_modelli_stampe_fk (OID = 79301) : 
--
ALTER TABLE ONLY tbf_modelli_stampe
    ADD CONSTRAINT tbf_modelli_stampe_fk
    FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index tbp_esemplare_fascicolo_fascicolo_fk (OID = 79306) : 
--
ALTER TABLE ONLY tbp_esemplare_fascicolo
    ADD CONSTRAINT tbp_esemplare_fascicolo_fascicolo_fk
    FOREIGN KEY (bid, fid) REFERENCES tbp_fascicolo(bid, fid) ON UPDATE CASCADE;
--
-- Definition for index tbp_esemplare_fascicolo_inventario_fk (OID = 79311) : 
--
ALTER TABLE ONLY tbp_esemplare_fascicolo
    ADD CONSTRAINT tbp_esemplare_fascicolo_inventario_fk
    FOREIGN KEY (cd_bib_inv, cd_serie, cd_inven) REFERENCES tbc_inventario(cd_bib, cd_serie, cd_inven);
--
-- Definition for index tbp_esemplare_fascicolo_ordine_fk (OID = 79316) : 
--
ALTER TABLE ONLY tbp_esemplare_fascicolo
    ADD CONSTRAINT tbp_esemplare_fascicolo_ordine_fk
    FOREIGN KEY (id_ordine) REFERENCES tba_ordini(id_ordine);
--
-- Definition for index tbp_fascicolo_titolo_fk (OID = 79321) : 
--
ALTER TABLE ONLY tbp_fascicolo
    ADD CONSTRAINT tbp_fascicolo_titolo_fk
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index tr_sogp_sogi_fk (OID = 79326) : 
--
ALTER TABLE ONLY tr_sogp_sogi
    ADD CONSTRAINT tr_sogp_sogi_fk
    FOREIGN KEY (cid_p) REFERENCES tb_soggetto(cid);
--
-- Definition for index import1_pkey (OID = 186479) : 
--
ALTER TABLE ONLY import1
    ADD CONSTRAINT import1_pkey
    PRIMARY KEY (id);
--
-- Definition for index tb_report_indice_pkey (OID = 186511) : 
--
ALTER TABLE ONLY tb_report_indice
    ADD CONSTRAINT tb_report_indice_pkey
    PRIMARY KEY (id);
--
-- Definition for index tb_report_indice_id_locali_pkey (OID = 186521) : 
--
ALTER TABLE ONLY tb_report_indice_id_locali
    ADD CONSTRAINT tb_report_indice_id_locali_pkey
    PRIMARY KEY (id);
--
-- Definition for index tb_report_indice_id_locali_fk (OID = 186523) : 
--
ALTER TABLE ONLY tb_report_indice_id_locali
    ADD CONSTRAINT tb_report_indice_id_locali_fk
    FOREIGN KEY (id_lista) REFERENCES tb_report_indice(id) ON UPDATE CASCADE;
--
-- Definition for index tb_report_indice_id_arrivo_pkey (OID = 186537) : 
--
ALTER TABLE ONLY tb_report_indice_id_arrivo
    ADD CONSTRAINT tb_report_indice_id_arrivo_pkey
    PRIMARY KEY (id);
--
-- Definition for index tb_report_indice_id_arrivo_fk (OID = 186539) : 
--
ALTER TABLE ONLY tb_report_indice_id_arrivo
    ADD CONSTRAINT tb_report_indice_id_arrivo_fk
    FOREIGN KEY (id_lista_ogg_loc) REFERENCES tb_report_indice_id_locali(id) ON UPDATE CASCADE DEFERRABLE INITIALLY DEFERRED;
--
-- Definition for index tbp_modello_previsionale_pkey (OID = 186554) : 
--
ALTER TABLE ONLY tbp_modello_previsionale
    ADD CONSTRAINT tbp_modello_previsionale_pkey
    PRIMARY KEY (id_modello);
--
-- Definition for index pk_tr_thes_cla (OID = 188364) : 
--
ALTER TABLE ONLY tr_the_cla
    ADD CONSTRAINT pk_tr_thes_cla
    PRIMARY KEY (cd_the, did, cd_sistema, cd_edizione, classe);
--
-- Definition for index tbp_esemplare_fascicolo_pkey (OID = 188368) : 
--
ALTER TABLE ONLY tbp_esemplare_fascicolo
    ADD CONSTRAINT tbp_esemplare_fascicolo_pkey
    PRIMARY KEY (id_ese_fascicolo);
--
-- Definition for index fktrl_autoriz_servizi (OID = 189713) : 
--
ALTER TABLE ONLY trl_autorizzazioni_servizi
    ADD CONSTRAINT fktrl_autoriz_servizi
    FOREIGN KEY (id_tipi_autorizzazione) REFERENCES tbl_tipi_autorizzazioni(id_tipi_autorizzazione);
--
-- Definition for index fktrl_diritti_servizio (OID = 189719) : 
--
ALTER TABLE ONLY trl_diritti_utente
    ADD CONSTRAINT fktrl_diritti_servizio
    FOREIGN KEY (id_servizio) REFERENCES tbl_servizio(id_servizio);
--
-- Definition for index fktrl_diritti_utenti (OID = 189727) : 
--
ALTER TABLE ONLY trl_diritti_utente
    ADD CONSTRAINT fktrl_diritti_utenti
    FOREIGN KEY (id_utenti) REFERENCES tbl_utenti(id_utenti);
--
-- Definition for index fk_tbf_modello_profilazione_bibliotecario__tbf_parametro (OID = 189882) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_bibliotecario
    ADD CONSTRAINT fk_tbf_modello_profilazione_bibliotecario__tbf_parametro
    FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index import950_pkey (OID = 215913) : 
--
ALTER TABLE ONLY import950
    ADD CONSTRAINT import950_pkey
    PRIMARY KEY (id);
--
-- Definition for index tra_ordine_carrello_spedizione_pkey (OID = 265975) : 
--
ALTER TABLE ONLY tra_ordine_carrello_spedizione
    ADD CONSTRAINT tra_ordine_carrello_spedizione_pkey
    PRIMARY KEY (id_ordine);
--
-- Definition for index tra_ordine_carrello_spedizione_idx (OID = 265977) : 
--
ALTER TABLE ONLY tra_ordine_carrello_spedizione
    ADD CONSTRAINT tra_ordine_carrello_spedizione_idx
    UNIQUE (id_ordine, dt_spedizione, prg_spedizione);
--
-- Definition for index tba_ordini_fk (OID = 265979) : 
--
ALTER TABLE ONLY tra_ordine_carrello_spedizione
    ADD CONSTRAINT tba_ordini_fk
    FOREIGN KEY (id_ordine) REFERENCES tba_ordini(id_ordine) DEFERRABLE INITIALLY DEFERRED;
--
-- Definition for index tr_editore_titolo_pk (OID = 407868) : 
--
ALTER TABLE ONLY tr_editore_titolo
    ADD CONSTRAINT tr_editore_titolo_pk
    PRIMARY KEY (cod_fornitore, bid);
--
-- Definition for index cod_fornitore_fk (OID = 407870) : 
--
ALTER TABLE ONLY tr_editore_titolo
    ADD CONSTRAINT cod_fornitore_fk
    FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index tb_titolo_fk (OID = 407875) : 
--
ALTER TABLE ONLY tr_editore_titolo
    ADD CONSTRAINT tb_titolo_fk
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index pk_editore_num_std (OID = 407883) : 
--
ALTER TABLE ONLY tbr_editore_num_std
    ADD CONSTRAINT pk_editore_num_std
    PRIMARY KEY (cod_fornitore, numero_std);
--
-- Definition for index fk_fornitore (OID = 407885) : 
--
ALTER TABLE ONLY tbr_editore_num_std
    ADD CONSTRAINT fk_fornitore
    FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index fk_biblioteca (OID = 528331) : 
--
ALTER TABLE ONLY tb_loc_indice
    ADD CONSTRAINT fk_biblioteca
    FOREIGN KEY (cd_polo, cd_biblioteca) REFERENCES tbf_biblioteca_in_polo(cd_polo, cd_biblioteca);
--
-- Definition for index fk_titolo (OID = 528336) : 
--
ALTER TABLE ONLY tb_loc_indice
    ADD CONSTRAINT fk_titolo
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index pk_numero_dtd (OID = 600093) : 
--
ALTER TABLE ONLY tb_numero_std
    ADD CONSTRAINT pk_numero_dtd
    PRIMARY KEY (bid, tp_numero_std, numero_std);
--
-- Definition for index xpktitset_1 (OID = 626298) : 
--
ALTER TABLE ONLY tb_titset_1
    ADD CONSTRAINT xpktitset_1
    PRIMARY KEY (bid);
--
-- Definition for index tit_to_set1 (OID = 626300) : 
--
ALTER TABLE ONLY tb_titset_1
    ADD CONSTRAINT tit_to_set1
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);

  
ALTER TABLE tb_titset_2
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid)
    REFERENCES sbnweb.tb_titolo(bid)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;
--
-- Definition for index xpkrisorsa_elettr (OID = 626308) : 
--
ALTER TABLE ONLY tb_risorsa_elettr
    ADD CONSTRAINT xpkrisorsa_elettr
    PRIMARY KEY (bid);
--
-- Definition for index tit_to_ris (OID = 626310) : 
--
ALTER TABLE ONLY tb_risorsa_elettr
    ADD CONSTRAINT tit_to_ris
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index xpkdisco_sonoro (OID = 626318) : 
--
ALTER TABLE ONLY tb_disco_sonoro
    ADD CONSTRAINT xpkdisco_sonoro
    PRIMARY KEY (bid);
--
-- Definition for index tit_to_dis (OID = 626320) : 
--
ALTER TABLE ONLY tb_disco_sonoro
    ADD CONSTRAINT tit_to_dis
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index xpkaudiovideo (OID = 626328) : 
--
ALTER TABLE ONLY tb_audiovideo
    ADD CONSTRAINT xpkaudiovideo
    PRIMARY KEY (bid);
--
-- Definition for index tit_to_aud (OID = 626330) : 
--
ALTER TABLE ONLY tb_audiovideo
    ADD CONSTRAINT tit_to_aud
    FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for trigger tb_autore_tidx_vectorupdate (OID = 77996) : 
--
CREATE TRIGGER tb_autore_tidx_vectorupdate
    BEFORE INSERT OR UPDATE ON tb_autore
    FOR EACH ROW
    EXECUTE PROCEDURE tb_autore_tsvector ();
--
-- Definition for trigger tb_classe_tidx_vectorupdate (OID = 77997) : 
--
CREATE TRIGGER tb_classe_tidx_vectorupdate
    BEFORE INSERT OR UPDATE ON tb_classe
    FOR EACH ROW
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector', 'pg_catalog.italian', 'ds_classe', 'ult_term', 'ky_classe_ord');
--
-- Definition for trigger tb_desrittore_tidx_vectorupdate (OID = 77998) : 
--
CREATE TRIGGER tb_desrittore_tidx_vectorupdate
    BEFORE INSERT OR UPDATE ON tb_descrittore
    FOR EACH ROW
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector', 'pg_catalog.italian', 'ds_descrittore');
--
-- Definition for trigger tb_marca_tidx_vectorupdate (OID = 77999) : 
--
CREATE TRIGGER tb_marca_tidx_vectorupdate
    BEFORE INSERT OR UPDATE ON tb_marca
    FOR EACH ROW
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector', 'pg_catalog.italian', 'ds_marca');
--
-- Definition for trigger tb_repertorio_tidx_vectorupdate (OID = 78000) : 
--
CREATE TRIGGER tb_repertorio_tidx_vectorupdate
    BEFORE INSERT OR UPDATE ON tb_repertorio
    FOR EACH ROW
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector', 'pg_catalog.italian', 'ds_repertorio');
--
-- Definition for trigger tb_soggetto_tidx_vectorupdate (OID = 78001) : 
--
CREATE TRIGGER tb_soggetto_tidx_vectorupdate
    BEFORE INSERT OR UPDATE ON tb_soggetto
    FOR EACH ROW
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector', 'pg_catalog.italian', 'ds_soggetto');
--
-- Definition for trigger tb_termine_thesauro_tidx_vectorupdate (OID = 78002) : 
--
CREATE TRIGGER tb_termine_thesauro_tidx_vectorupdate
    BEFORE INSERT OR UPDATE ON tb_termine_thesauro
    FOR EACH ROW
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector', 'pg_catalog.italian', 'ds_termine_thesauro');
--
-- Definition for trigger tbc_poss_prov_tidx (OID = 78003) : 
--
CREATE TRIGGER tbc_poss_prov_tidx
    BEFORE INSERT OR UPDATE ON tbc_possessore_provenienza
    FOR EACH ROW
    EXECUTE PROCEDURE tbc_poss_prov_tidx ();
--
-- Definition for trigger tbf_biblioteca_tidx_vector_indirizzo (OID = 78004) : 
--
CREATE TRIGGER tbf_biblioteca_tidx_vector_indirizzo
    BEFORE INSERT OR UPDATE ON tbf_biblioteca
    FOR EACH ROW
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector_indirizzo', 'pg_catalog.italian', 'indirizzo');
--
-- Definition for trigger tbf_biblioteca_tidx_vector_nom_biblioteca (OID = 78005) : 
--
CREATE TRIGGER tbf_biblioteca_tidx_vector_nom_biblioteca
    BEFORE INSERT OR UPDATE ON tbf_biblioteca
    FOR EACH ROW
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector_nom_biblioteca', 'pg_catalog.italian', 'nom_biblioteca');
--
-- Definition for trigger tbl_documenti_lettori_tidx_vectorupdate (OID = 78006) : 
--
CREATE TRIGGER tbl_documenti_lettori_tidx_vectorupdate
    BEFORE INSERT OR UPDATE ON tbl_documenti_lettori
    FOR EACH ROW
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector', 'pg_catalog.italian', 'titolo');
--
-- Definition for trigger tbp_fascicolo_anno_pub (OID = 78008) : 
--
CREATE TRIGGER tbp_fascicolo_anno_pub
    BEFORE INSERT OR UPDATE ON tbp_fascicolo
    FOR EACH ROW
    EXECUTE PROCEDURE tb_fascicolo_anno_pub ();
--
-- Definition for trigger tbr_fornitori_tidx_vectorupdate (OID = 78009) : 
--
CREATE TRIGGER tbr_fornitori_tidx_vectorupdate
    BEFORE INSERT OR UPDATE ON tbr_fornitori
    FOR EACH ROW
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector', 'pg_catalog.italian', 'chiave_for');
--
-- Definition for trigger tbl_utenti_tidx_vectorupdate (OID = 186556) : 
--
CREATE TRIGGER tbl_utenti_tidx_vectorupdate
    BEFORE INSERT OR UPDATE ON tbl_utenti
    FOR EACH ROW
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector', 'pg_catalog.italian', 'chiave_ute');
--
-- Comments
--
COMMENT ON SCHEMA public IS 'standard public schema';
COMMENT ON TABLE sbnweb.jms_messages IS ' Tabella contenente lo storico dei messaggi scambiati tra le code gestite dal server JMS';
COMMENT ON COLUMN sbnweb.jms_messages.messageid IS 'Identificativo univoco del messaggio';
COMMENT ON COLUMN sbnweb.jms_messages.destination IS 'Coda JMS definita come destinazione del messaggio';
COMMENT ON COLUMN sbnweb.jms_messages.txid IS 'Identificativo della transazione JMS cui appartiene il messaggio';
COMMENT ON COLUMN sbnweb.jms_messages.txop IS 'Tipo operazione eseguita su questo messaggio';
COMMENT ON COLUMN sbnweb.jms_messages.messageblob IS 'Corpo del messaggio';
COMMENT ON TABLE sbnweb.jms_transactions IS ' Tabella che contiene gli identificativi delle transazioni attualmente in corso sul server JMS';
COMMENT ON COLUMN sbnweb.jms_transactions.txid IS 'Identificativo della transazione JMS';
COMMENT ON TABLE sbnweb.tb_abstract IS 'ABSTRACT (TPSABS)';
COMMENT ON COLUMN sbnweb.tb_abstract.bid IS 'codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.tb_abstract.ds_abstract IS 'descrizione dell''abstract';
COMMENT ON COLUMN sbnweb.tb_abstract.cd_livello IS 'codice del livello di autorita''';
COMMENT ON COLUMN sbnweb.tb_abstract.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_abstract.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_abstract.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_abstract.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_abstract.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_arte_tridimens IS 'OGGETTI D''ARTE TRIDIMENSIONALE';
COMMENT ON COLUMN sbnweb.tb_arte_tridimens.bid IS 'Codice identificativo SBN del titolo';
COMMENT ON COLUMN sbnweb.tb_arte_tridimens.cd_designazione IS 'designazione specifica del materiale: 117$a/0-1';
COMMENT ON COLUMN sbnweb.tb_arte_tridimens.tp_materiale_1 IS 'tipo materiale: 117/$a/2-4';
COMMENT ON COLUMN sbnweb.tb_arte_tridimens.tp_materiale_2 IS 'tipo materiale: 117/$a/5-6';
COMMENT ON COLUMN sbnweb.tb_arte_tridimens.tp_materiale_3 IS 'tipo materiale: 117/$a/6-7';
COMMENT ON COLUMN sbnweb.tb_arte_tridimens.cd_colore IS 'codice colore: 117/$a/8';
COMMENT ON COLUMN sbnweb.tb_arte_tridimens.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_arte_tridimens.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_arte_tridimens.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_arte_tridimens.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_arte_tridimens.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_autore IS 'AUTORI';
COMMENT ON COLUMN sbnweb.tb_autore.vid IS 'Identificativo SBN dell''autore';
COMMENT ON COLUMN sbnweb.tb_autore.isadn IS 'Prot. SBN=NO - Chiave univoca in Authority File';
COMMENT ON COLUMN sbnweb.tb_autore.tp_forma_aut IS 'Forma (A=accettata, R=variante)';
COMMENT ON COLUMN sbnweb.tb_autore.ky_cautun IS 'Chiave autore 4 + 1 +1 per persone - 2 + 1 + 1 + 1 + 1 per enti';
COMMENT ON COLUMN sbnweb.tb_autore.ky_auteur IS 'Prima parola del nome autore';
COMMENT ON COLUMN sbnweb.tb_autore.ky_el1_a IS 'Primo elemento di raffinamento (primi 3 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_autore.ky_el1_b IS 'Primo elemento di raffinamento (ultimi 3 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_autore.ky_el2_a IS 'Secondo elemento di raffinamento (primi 3 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_autore.ky_el2_b IS 'Secondo elemento di raffinamento (ultimi 3 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_autore.tp_nome_aut IS 'Persona : A,B,C,D - Ente : E,G,R';
COMMENT ON COLUMN sbnweb.tb_autore.ky_el3 IS 'Terzo elemento di raffinamento';
COMMENT ON COLUMN sbnweb.tb_autore.ky_el4 IS 'Quarto elemento di raffinamento';
COMMENT ON COLUMN sbnweb.tb_autore.ky_el5 IS 'Quinto elemento di raffinamento';
COMMENT ON COLUMN sbnweb.tb_autore.ky_cles1_a IS 'Chiave lunga (primi 50 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_autore.ky_cles2_a IS 'Chiave lunga (ultimi 30 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_autore.cd_paese IS 'Prot. SBN=NO - Paese di nascita';
COMMENT ON COLUMN sbnweb.tb_autore.cd_lingua IS 'codice identificativo della lingua';
COMMENT ON COLUMN sbnweb.tb_autore.aa_nascita IS 'Prot. SBN=NO - Data nascita';
COMMENT ON COLUMN sbnweb.tb_autore.aa_morte IS 'Prot. SBN=NO - Data morte';
COMMENT ON COLUMN sbnweb.tb_autore.cd_livello IS 'Livello di autorita''';
COMMENT ON COLUMN sbnweb.tb_autore.fl_speciale IS 'Indicatore presenza chr. speciali in Descr_Aut';
COMMENT ON COLUMN sbnweb.tb_autore.ds_nome_aut IS 'Prot. SBN=160 chr - Descrizione autore';
COMMENT ON COLUMN sbnweb.tb_autore.cd_agenzia IS 'Prot. SBN=NO - Agenzia di catalogazione';
COMMENT ON COLUMN sbnweb.tb_autore.cd_norme_cat IS 'Prot. SBN=NO - Norme di catalogazione';
COMMENT ON COLUMN sbnweb.tb_autore.nota_aut IS 'Prot. SBN=80 chr. - Nota al nome autore';
COMMENT ON COLUMN sbnweb.tb_autore.nota_cat_aut IS 'Prot. SBN=NO - Note del catalogatore';
COMMENT ON COLUMN sbnweb.tb_autore.vid_link IS 'VID dell''autore accorpante o dell''attuale se cancellato';
COMMENT ON COLUMN sbnweb.tb_autore.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_autore.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_autore.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_autore.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_autore.ute_forza_ins IS 'Utente che ha forzato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_autore.ute_forza_var IS 'Utente che ha forzato la variazione';
COMMENT ON COLUMN sbnweb.tb_autore.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tb_autore.fl_condiviso IS 'Flag di condivisione gestione del legame con indice';
COMMENT ON COLUMN sbnweb.tb_autore.ute_condiviso IS 'Timestamp di condivisione gestione del legame con indice';
COMMENT ON COLUMN sbnweb.tb_autore.ts_condiviso IS 'Utente che ha attivato la gestione condivisa del legame con indice';
COMMENT ON COLUMN sbnweb.tb_autore.tidx_vector IS 'indice delle parole contenute nel nome dell''autore';
COMMENT ON TABLE sbnweb.tb_cartografia IS 'MATERIALE CARTOGRAFICO';
COMMENT ON COLUMN sbnweb.tb_cartografia.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_livello IS 'livello di autorita'' dei dati specifici della cartografia';
COMMENT ON COLUMN sbnweb.tb_cartografia.tp_pubb_gov IS 'tipo pubblicazione governativa: da 100 $a/20';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_colore IS 'indicatore di colore: da 120 $a/0';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_meridiano IS 'codice meridiano d''origine';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_supporto_fisico IS 'supporto fisico: da 121 $a/3-4';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_tecnica IS 'tecnica di creazione: 121 $a/5';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_forma_ripr IS 'forma della riproduzione: da 121 $a/6';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_forma_pubb IS 'forma della riproduzione: da 121 $a/8';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_altitudine IS 'Altitudine del sensore: da 121$b/0';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_tiposcala IS 'codice di tipo scala: da 123 indicatore1';
COMMENT ON COLUMN sbnweb.tb_cartografia.tp_scala IS 'tipo di scala: da 123$a';
COMMENT ON COLUMN sbnweb.tb_cartografia.scala_oriz IS 'costante per scala lineare orizzontale: da 123$b';
COMMENT ON COLUMN sbnweb.tb_cartografia.scala_vert IS 'costante per scala lineare verticale: da 123$c';
COMMENT ON COLUMN sbnweb.tb_cartografia.longitudine_ovest IS 'Massima estensione a ovest (longitudine): da 123 $d';
COMMENT ON COLUMN sbnweb.tb_cartografia.longitudine_est IS 'Massima estensione a est (longitudine): da 123 $e';
COMMENT ON COLUMN sbnweb.tb_cartografia.latitudine_nord IS 'Massima estensione a nord (latitudine) da 123 $f';
COMMENT ON COLUMN sbnweb.tb_cartografia.latitudine_sud IS 'Massima estensione a sud (latitudine) da 123 $g';
COMMENT ON COLUMN sbnweb.tb_cartografia.tp_immagine IS 'carattere dell''immagine: da 124$a';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_forma_cart IS 'forma del doc. cartografico: da 124$b';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_piattaforma IS 'posizione della piattaforma: da 124$d';
COMMENT ON COLUMN sbnweb.tb_cartografia.cd_categ_satellite IS 'categoria del satellite: da 124$e';
COMMENT ON COLUMN sbnweb.tb_cartografia.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_cartografia.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_cartografia.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_cartografia.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_cartografia.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_classe IS 'INDICI DI CLASSIFICAZIONI (TPSCLA)';
COMMENT ON COLUMN sbnweb.tb_classe.cd_sistema IS 'Codice del sistema di classificazione';
COMMENT ON COLUMN sbnweb.tb_classe.cd_edizione IS 'Edizione per il sistema di classificazione Dewey';
COMMENT ON COLUMN sbnweb.tb_classe.classe IS 'simbolo di classificazione';
COMMENT ON COLUMN sbnweb.tb_classe.ds_classe IS 'descrizione del simbolo di classificazione';
COMMENT ON COLUMN sbnweb.tb_classe.cd_livello IS 'codice del livello di autorita''';
COMMENT ON COLUMN sbnweb.tb_classe.fl_costruito IS 'Flag che indica che il simbolo e'' costruito a partire dal suo superiore gerarchico';
COMMENT ON COLUMN sbnweb.tb_classe.fl_speciale IS 'NON UTILIZZATO';
COMMENT ON COLUMN sbnweb.tb_classe.ky_classe_ord IS 'indice per ordinamento';
COMMENT ON COLUMN sbnweb.tb_classe.suffisso IS 'Suffisso alfabetico degli indici Deweey, valorizzabile solo per indici deweey per ordinamento separato';
COMMENT ON COLUMN sbnweb.tb_classe.ult_term IS 'ulteriori termini di ricerca';
COMMENT ON COLUMN sbnweb.tb_classe.fl_condiviso IS 'Flag di condivisione gestione del legame con indice';
COMMENT ON COLUMN sbnweb.tb_classe.ute_condiviso IS 'Utente che ha effettuato la condivisione con il sistema Indice';
COMMENT ON COLUMN sbnweb.tb_classe.ts_condiviso IS 'Timestamp di cndivisione con sistema indice';
COMMENT ON COLUMN sbnweb.tb_classe.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_classe.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_classe.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_classe.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_classe.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tb_classe.tidx_vector IS 'Indice delle parole contenute nella descrizione del simbolo';
COMMENT ON TABLE sbnweb.tb_codici IS 'Descrizione codici di validazione';
COMMENT ON COLUMN sbnweb.tb_codici.tp_tabella IS 'Codice tipo tabella: il valore ''0000'' indica che il record descrive una tabella il cui codice e'' riportato nel campo cd_tabella valori diversi da ''0000''';
COMMENT ON COLUMN sbnweb.tb_codici.cd_tabella IS 'Codice identificante la tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_valore IS 'Codice dell''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.ds_tabella IS 'Decrizione associata al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_unimarc IS 'Export Unimarc';
COMMENT ON COLUMN sbnweb.tb_codici.cd_marc_21 IS 'Export Marc 21';
COMMENT ON COLUMN sbnweb.tb_codici.tp_materiale IS 'Tipo materiale';
COMMENT ON COLUMN sbnweb.tb_codici.dt_fine_validita IS 'Data in cui codice a cessato di essere utilizzato';
COMMENT ON COLUMN sbnweb.tb_codici.ds_cdsbn_ulteriore IS 'Ulteriore decrizione associata al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_flg1 IS 'Primo eventuale attributo associato al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_flg2 IS 'Secondo eventuale attributo associato al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_flg3 IS 'Terzo eventuale attributo associato al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_flg4 IS 'Quarto eventuale attributo associato al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_flg5 IS 'Quinto eventuale attributo associato al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_flg6 IS 'Sesto eventuale attributo associato al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_flg7 IS 'Settimo eventuale attributo associato al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_flg8 IS 'Ottavo eventuale attributo associato al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_flg9 IS 'Nono eventuale attributo associato al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_flg10 IS 'Decimo eventuale attributo associato al codice identificante l''elemento di tabella';
COMMENT ON COLUMN sbnweb.tb_codici.cd_flg11 IS 'Undicesimo eventuale attributo associato al codice identificante l''elemento di tabella';
COMMENT ON TABLE sbnweb.tb_composizione IS 'COMPOSIZIONI MUSICALI';
COMMENT ON COLUMN sbnweb.tb_composizione.cd_forma_1 IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tb_composizione.cd_forma_2 IS '1^ forma musicale (Codice Unimarc)';
COMMENT ON COLUMN sbnweb.tb_composizione.cd_forma_3 IS '2^ forma musicale (Codice Unimarc)';
COMMENT ON COLUMN sbnweb.tb_composizione.numero_ordine IS '3^ forma musicale (Codice Unimarc)';
COMMENT ON COLUMN sbnweb.tb_composizione.numero_opera IS 'Numero d''ordine';
COMMENT ON COLUMN sbnweb.tb_composizione.numero_cat_tem IS 'Numero d''opera';
COMMENT ON COLUMN sbnweb.tb_composizione.cd_tonalita IS 'codice identificativo della tonalita''';
COMMENT ON COLUMN sbnweb.tb_composizione.datazione IS 'Datazione della composizione';
COMMENT ON COLUMN sbnweb.tb_composizione.aa_comp_1 IS 'Data 1 della composizione';
COMMENT ON COLUMN sbnweb.tb_composizione.aa_comp_2 IS 'Data 2 della composizione';
COMMENT ON COLUMN sbnweb.tb_composizione.ds_sezioni IS 'descrizione sezioni';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_ord_ric IS 'Titolo d''ordinamento : chiave ricerca';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_est_ric IS 'Titolo dell''estratto : chiave ricerca';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_app_ric IS 'Appellativo : chiave ricerca';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_ord_clet IS 'Titolo d''ordinamento : CLET';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_est_clet IS 'Titolo dell''estratto : CLET';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_app_clet IS 'Appellativo : CLET';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_ord_pre IS 'Titolo d''ordinamento : pretitolo';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_est_pre IS 'Titolo dell''estratto : pretitolo';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_app_pre IS 'Appellativo : pretitolo';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_ord_den IS 'Titolo d''ordinamento : stringa denormalizzata, senza pretitolo';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_est_den IS 'Titolo dell''estratto : stringa denormalizzata, senza pretitolo';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_app_den IS 'Appellativo : stringa denormalizzata, senza pretitolo';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_ord_nor_pre IS 'Titolo d''ordinamento : stringa normalizzata, con pretitolo';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_est_nor_pre IS 'Titolo dell''estratto : stringa normalizzata, con pretitolo';
COMMENT ON COLUMN sbnweb.tb_composizione.ky_app_nor_pre IS 'Appellativo : stringa normalizzata, con pretitolo';
COMMENT ON COLUMN sbnweb.tb_composizione.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_composizione.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_composizione.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_composizione.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_composizione.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_descrittore IS 'DESCRITTORI DI SOGGETTO (ex TPSDSO))';
COMMENT ON COLUMN sbnweb.tb_descrittore.did IS 'codice identificativo del descrittore';
COMMENT ON COLUMN sbnweb.tb_descrittore.ds_descrittore IS 'descrizione';
COMMENT ON COLUMN sbnweb.tb_descrittore.ky_norm_descritt IS 'chiave di ricerca';
COMMENT ON COLUMN sbnweb.tb_descrittore.nota_descrittore IS 'note al descrittore';
COMMENT ON COLUMN sbnweb.tb_descrittore.cd_soggettario IS 'codice di tipo di soggettario';
COMMENT ON COLUMN sbnweb.tb_descrittore.tp_forma_des IS 'Forma del nome del descrittore (''A''=Accettata, ''R''=Rinvio)';
COMMENT ON COLUMN sbnweb.tb_descrittore.cd_livello IS 'Livello autorita'' bibliografica';
COMMENT ON COLUMN sbnweb.tb_descrittore.fl_condiviso IS 'indicatore di descrizione acquisita a livello di polo (cioe'' non accettati a livello di indice); ammette i valori: 1 = si, 0 = no';
COMMENT ON COLUMN sbnweb.tb_descrittore.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_descrittore.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_descrittore.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_descrittore.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_descrittore.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tb_descrittore.tidx_vector IS 'Indice delle parole contenute nel descrittore';
COMMENT ON COLUMN sbnweb.tb_descrittore.cd_edizione IS 'Edizione del soggettario in cui e'' utilizzato il termine da cui discende il descrittore (gestita solo per il soggettario SBN cd_soggettario= FI ); valori ammessi: I, E, N validati su tabella codici  SEFI ';
COMMENT ON COLUMN sbnweb.tb_descrittore.cat_termine IS 'codice di categoria termine, validato su tabella codici SCSO';
COMMENT ON TABLE sbnweb.tb_grafica IS 'MATERIALE GRAFICO';
COMMENT ON COLUMN sbnweb.tb_grafica.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tb_grafica.cd_livello IS 'codice del livello di autorita''';
COMMENT ON COLUMN sbnweb.tb_grafica.tp_materiale_gra IS 'Designazione specifica del materiale grafico: da 116 $a/0';
COMMENT ON COLUMN sbnweb.tb_grafica.cd_supporto IS 'Supporto primario';
COMMENT ON COLUMN sbnweb.tb_grafica.cd_colore IS 'indicatore di colore: da 116$a/3';
COMMENT ON COLUMN sbnweb.tb_grafica.cd_tecnica_dis_1 IS 'indicatore di tecnica per disegni: da 116$a/4-5';
COMMENT ON COLUMN sbnweb.tb_grafica.cd_tecnica_dis_2 IS 'indicatore di tecnica per disegni: da 116$a/6-7';
COMMENT ON COLUMN sbnweb.tb_grafica.cd_tecnica_dis_3 IS 'indicatore di tecnica per disegni: da 116$a/8-9';
COMMENT ON COLUMN sbnweb.tb_grafica.cd_tecnica_sta_1 IS 'indicatore di tecnica per stampe: da 116$a/10-11';
COMMENT ON COLUMN sbnweb.tb_grafica.cd_tecnica_sta_2 IS 'indicatore di tecnica per stampe: da 116$a/12-13';
COMMENT ON COLUMN sbnweb.tb_grafica.cd_tecnica_sta_3 IS 'indicatore di tecnica per stampe: da 116$a/14-15';
COMMENT ON COLUMN sbnweb.tb_grafica.cd_design_funz IS 'designazione di funzione: da 116$a/16-17';
COMMENT ON COLUMN sbnweb.tb_grafica.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_grafica.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_grafica.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_grafica.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_grafica.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_impronta IS 'IMPRONTE';
COMMENT ON COLUMN sbnweb.tb_impronta.bid IS 'Codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.tb_impronta.impronta_1 IS 'Codice dell''impronta';
COMMENT ON COLUMN sbnweb.tb_impronta.impronta_2 IS 'Prima parte dell''impronta';
COMMENT ON COLUMN sbnweb.tb_impronta.impronta_3 IS 'Seconda parte dell''impronta';
COMMENT ON COLUMN sbnweb.tb_impronta.nota_impronta IS 'Nota al collegamento titolo impronta';
COMMENT ON COLUMN sbnweb.tb_impronta.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_impronta.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_impronta.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_impronta.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_impronta.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_incipit IS 'INCIPIT (SOLO MUSICA)';
COMMENT ON COLUMN sbnweb.tb_incipit.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tb_incipit.numero_mov IS 'Numero di movimento';
COMMENT ON COLUMN sbnweb.tb_incipit.numero_p_mov IS '?';
COMMENT ON COLUMN sbnweb.tb_incipit.bid_letterario IS 'Identificativo del titolo dell''incipit letterario.';
COMMENT ON COLUMN sbnweb.tb_incipit.tp_indicatore IS 'Indicatore (valori : space, P, S)';
COMMENT ON COLUMN sbnweb.tb_incipit.numero_comp IS 'Numero delle composizioni (new)';
COMMENT ON COLUMN sbnweb.tb_incipit.registro_mus IS 'registro musicale';
COMMENT ON COLUMN sbnweb.tb_incipit.nome_personaggio IS 'nome personaggio';
COMMENT ON COLUMN sbnweb.tb_incipit.tempo_mus IS '?';
COMMENT ON COLUMN sbnweb.tb_incipit.cd_forma IS '?';
COMMENT ON COLUMN sbnweb.tb_incipit.cd_tonalita IS '?';
COMMENT ON COLUMN sbnweb.tb_incipit.chiave_mus IS '?';
COMMENT ON COLUMN sbnweb.tb_incipit.alterazione IS 'Alterazione';
COMMENT ON COLUMN sbnweb.tb_incipit.misura IS '?';
COMMENT ON COLUMN sbnweb.tb_incipit.ds_contesto IS 'descrizione del contesto';
COMMENT ON COLUMN sbnweb.tb_incipit.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_incipit.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_incipit.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_incipit.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_incipit.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_luogo IS 'LUOGHI DI PUBBLICAZIONE';
COMMENT ON COLUMN sbnweb.tb_luogo.lid IS 'Identificativo del luogo';
COMMENT ON COLUMN sbnweb.tb_luogo.tp_forma IS 'Accettata/Rinvio (A=accettata; R=rinvio)';
COMMENT ON COLUMN sbnweb.tb_luogo.cd_livello IS 'Livello di autorita''';
COMMENT ON COLUMN sbnweb.tb_luogo.ds_luogo IS 'Descrizione del luogo';
COMMENT ON COLUMN sbnweb.tb_luogo.ky_luogo IS 'Chiave del luogo';
COMMENT ON COLUMN sbnweb.tb_luogo.ky_norm_luogo IS 'Stringa normalizzata del luogo';
COMMENT ON COLUMN sbnweb.tb_luogo.cd_paese IS 'codice identificativo paese';
COMMENT ON COLUMN sbnweb.tb_luogo.nota_luogo IS 'nota relativa al luogo';
COMMENT ON COLUMN sbnweb.tb_luogo.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_luogo.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_luogo.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_luogo.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_luogo.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_marca IS 'MARCHE EDITORIALI';
COMMENT ON COLUMN sbnweb.tb_marca.mid IS 'Identificativo della marca in SBN';
COMMENT ON COLUMN sbnweb.tb_marca.cd_livello IS 'Livello di autorita''';
COMMENT ON COLUMN sbnweb.tb_marca.fl_speciale IS 'Indicatore presenza chr. speciali in Marca';
COMMENT ON COLUMN sbnweb.tb_marca.ds_marca IS 'descrizione della marca';
COMMENT ON COLUMN sbnweb.tb_marca.nota_marca IS 'nota alla marca';
COMMENT ON COLUMN sbnweb.tb_marca.ds_motto IS 'Prot. SBN=NO';
COMMENT ON COLUMN sbnweb.tb_marca.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_marca.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_marca.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_marca.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_marca.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tb_marca.fl_condiviso IS 'Flag di condivisione gestione del legame con indice';
COMMENT ON COLUMN sbnweb.tb_marca.ute_condiviso IS 'Timestamp di condivisione gestione del legame con indice';
COMMENT ON COLUMN sbnweb.tb_marca.ts_condiviso IS 'Utente che ha attivato la gestione condivisa del legame con indice';
COMMENT ON COLUMN sbnweb.tb_marca.tidx_vector IS 'Indice delle parole contenute nel descrittore';
COMMENT ON TABLE sbnweb.tb_microforma IS 'MICROFORMA';
COMMENT ON COLUMN sbnweb.tb_microforma.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tb_microforma.cd_designazione IS 'designazione specifica del materiale: 130$a/0';
COMMENT ON COLUMN sbnweb.tb_microforma.cd_polarita IS 'polarita'': 130$a/1';
COMMENT ON COLUMN sbnweb.tb_microforma.cd_dimensione IS 'dimensione: 130$a/2';
COMMENT ON COLUMN sbnweb.tb_microforma.cd_riduzione IS 'riduzione: 130$a/3';
COMMENT ON COLUMN sbnweb.tb_microforma.cd_riduzione_spec IS 'specificita'' della riduzione: 130$a/4-6';
COMMENT ON COLUMN sbnweb.tb_microforma.cd_colore IS 'colore: 130$a/7';
COMMENT ON COLUMN sbnweb.tb_microforma.cd_emulsione IS 'emulsione: 130$a/8';
COMMENT ON COLUMN sbnweb.tb_microforma.cd_generazione IS 'generazione: 130$a/9';
COMMENT ON COLUMN sbnweb.tb_microforma.cd_base IS 'base della pellicola: 130$a/10';
COMMENT ON COLUMN sbnweb.tb_microforma.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_microforma.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_microforma.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_microforma.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_microforma.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_musica IS 'MUSICA';
COMMENT ON COLUMN sbnweb.tb_musica.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tb_musica.cd_livello IS 'livello di autorita''';
COMMENT ON COLUMN sbnweb.tb_musica.ds_org_sint IS 'Organico sintetico';
COMMENT ON COLUMN sbnweb.tb_musica.ds_org_anal IS 'Organico analitico';
COMMENT ON COLUMN sbnweb.tb_musica.tp_elaborazione IS 'Tipo elaborazione';
COMMENT ON COLUMN sbnweb.tb_musica.cd_stesura IS 'codice stesura';
COMMENT ON COLUMN sbnweb.tb_musica.fl_composito IS 'Indicatore di composito';
COMMENT ON COLUMN sbnweb.tb_musica.fl_palinsesto IS 'Indicatore di palinsesto';
COMMENT ON COLUMN sbnweb.tb_musica.datazione IS 'Datazione';
COMMENT ON COLUMN sbnweb.tb_musica.cd_presentazione IS 'Codice presentazione';
COMMENT ON COLUMN sbnweb.tb_musica.cd_materia IS 'Codice materia';
COMMENT ON COLUMN sbnweb.tb_musica.ds_illustrazioni IS 'Descrizione delle illustrazioni';
COMMENT ON COLUMN sbnweb.tb_musica.notazione_musicale IS 'Notazione musicale';
COMMENT ON COLUMN sbnweb.tb_musica.ds_legatura IS 'Descrizione della legatura';
COMMENT ON COLUMN sbnweb.tb_musica.ds_conservazione IS 'Descrizione della conservazione';
COMMENT ON COLUMN sbnweb.tb_musica.tp_testo_letter IS 'codice di tipo testo letterario: da 125$b';
COMMENT ON COLUMN sbnweb.tb_musica.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_musica.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_musica.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_musica.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_musica.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_nota IS 'NOTE AL TITOLO';
COMMENT ON COLUMN sbnweb.tb_nota.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tb_nota.tp_nota IS 'Tipo di nota';
COMMENT ON COLUMN sbnweb.tb_nota.progr_nota IS 'Progressivo della nota';
COMMENT ON COLUMN sbnweb.tb_nota.ds_nota IS 'Descrizione della nota';
COMMENT ON COLUMN sbnweb.tb_nota.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_nota.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_nota.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_nota.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_nota.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_numero_std IS 'NUMERI STANDARD';
COMMENT ON COLUMN sbnweb.tb_numero_std.bid IS 'Codice identificativo SBN del titolo';
COMMENT ON COLUMN sbnweb.tb_numero_std.tp_numero_std IS 'Tipo Numero standard';
COMMENT ON COLUMN sbnweb.tb_numero_std.numero_std IS 'Numero standard - Prot. SBN=10 chr.';
COMMENT ON COLUMN sbnweb.tb_numero_std.numero_lastra IS 'Parte numerica del numero di lastra';
COMMENT ON COLUMN sbnweb.tb_numero_std.cd_paese IS 'Prot. SBN=NO - Codice Paese della bibliografia';
COMMENT ON COLUMN sbnweb.tb_numero_std.nota_numero_std IS 'Nota al numero standard';
COMMENT ON COLUMN sbnweb.tb_numero_std.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_numero_std.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_numero_std.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_numero_std.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_numero_std.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_parola IS 'PAROLE CHIAVE DELLE MARCHE EDITORIALI';
COMMENT ON COLUMN sbnweb.tb_parola.id_parola IS 'Identificativo della parola';
COMMENT ON COLUMN sbnweb.tb_parola.mid IS 'Codice identificativo SBN della marca';
COMMENT ON COLUMN sbnweb.tb_parola.parola IS 'Parola associata alla descrizione della marca';
COMMENT ON COLUMN sbnweb.tb_parola.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_parola.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_parola.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_parola.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_parola.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_personaggio IS 'PERSONAGGIO';
COMMENT ON COLUMN sbnweb.tb_personaggio.id_personaggio IS 'Identificativo del personaggio';
COMMENT ON COLUMN sbnweb.tb_personaggio.bid IS 'Identificativo del documento SBN in cui e'' presente il personaggio';
COMMENT ON COLUMN sbnweb.tb_personaggio.nome_personaggio IS 'Nome del personaggio';
COMMENT ON COLUMN sbnweb.tb_personaggio.cd_timbro_vocale IS 'Codice del timbro vocale';
COMMENT ON COLUMN sbnweb.tb_personaggio.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_personaggio.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_personaggio.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_personaggio.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_personaggio.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_rappresent IS 'RAPPRESENTAZIONE';
COMMENT ON COLUMN sbnweb.tb_rappresent.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tb_rappresent.tp_genere IS 'Genere della rappresentazione';
COMMENT ON COLUMN sbnweb.tb_rappresent.aa_rapp IS 'Anno di rappresentazione';
COMMENT ON COLUMN sbnweb.tb_rappresent.ds_periodo IS 'Periodo dell''anno di rappresentazione';
COMMENT ON COLUMN sbnweb.tb_rappresent.ds_teatro IS 'Descrizione del teatro';
COMMENT ON COLUMN sbnweb.tb_rappresent.ds_luogo_rapp IS 'Descrizione luogo di rappresentazione';
COMMENT ON COLUMN sbnweb.tb_rappresent.ds_occasione IS 'Descrizione della rappresentazione';
COMMENT ON COLUMN sbnweb.tb_rappresent.nota_rapp IS 'Nota alla rappresentazione';
COMMENT ON COLUMN sbnweb.tb_rappresent.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_rappresent.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_rappresent.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_rappresent.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_rappresent.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_repertorio IS 'Repertori (marche/autori/titoli)';
COMMENT ON COLUMN sbnweb.tb_repertorio.id_repertorio IS 'Identificativo del repertorio';
COMMENT ON COLUMN sbnweb.tb_repertorio.cd_sig_repertorio IS 'Prot. SBN=1 chr.(per la marca) - Sigla del repertorio';
COMMENT ON COLUMN sbnweb.tb_repertorio.ds_repertorio IS 'Denominazione del repertorio';
COMMENT ON COLUMN sbnweb.tb_repertorio.tp_repertorio IS 'Tipo repertorio (A=autore/titolo M=marca)';
COMMENT ON COLUMN sbnweb.tb_repertorio.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_repertorio.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_repertorio.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_repertorio.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_repertorio.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tb_soggetto IS 'SOGGETTI (ex TPSSOG))';
COMMENT ON COLUMN sbnweb.tb_soggetto.cid IS 'codice identificativo univoco del soggetto';
COMMENT ON COLUMN sbnweb.tb_soggetto.cd_soggettario IS 'codice del tipo di soggettario';
COMMENT ON COLUMN sbnweb.tb_soggetto.ds_soggetto IS 'descrizione';
COMMENT ON COLUMN sbnweb.tb_soggetto.fl_speciale IS 'indicatore di presenza di caratteri speciali nella stringa del soggetto';
COMMENT ON COLUMN sbnweb.tb_soggetto.ky_cles1_s IS 'chiave di ricerca';
COMMENT ON COLUMN sbnweb.tb_soggetto.ky_primo_descr IS 'chiave di ordinamento del primo elemento di soggetto (descrittore)';
COMMENT ON COLUMN sbnweb.tb_soggetto.cat_sogg IS 'codice di categoria soggetto validato su tabella codici SCSO';
COMMENT ON COLUMN sbnweb.tb_soggetto.cd_livello IS 'codice del livello di autorita''';
COMMENT ON COLUMN sbnweb.tb_soggetto.fl_condiviso IS 'Indicatore di gestione condivisa con il sistema indice';
COMMENT ON COLUMN sbnweb.tb_soggetto.ute_condiviso IS 'Utente che ha effettuato la condivisione con il sistema Indice';
COMMENT ON COLUMN sbnweb.tb_soggetto.ts_condiviso IS 'Timestamp di cndivisione con sistema indice';
COMMENT ON COLUMN sbnweb.tb_soggetto.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_soggetto.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_soggetto.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_soggetto.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_soggetto.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tb_soggetto.ky_cles2_s IS 'NON UTILIZZATO';
COMMENT ON COLUMN sbnweb.tb_soggetto.tidx_vector IS 'Indice delle parole contenute nel soggetto';
COMMENT ON COLUMN sbnweb.tb_soggetto.nota_soggetto IS 'Nota alla descrizione del soggetto';
COMMENT ON COLUMN sbnweb.tb_soggetto.cd_edizione IS 'Edizione del Soggettario SBN';
COMMENT ON TABLE sbnweb.tb_stat_parameter IS 'Contiene le caratteristiche delle variabili utilizzate dalle statistiche';
COMMENT ON COLUMN sbnweb.tb_stat_parameter.id_stat IS 'Id della statistica cui si riferisce il parametro';
COMMENT ON COLUMN sbnweb.tb_stat_parameter.nome IS 'Nome della variabile da utilizzare nella query per applicare il filtro';
COMMENT ON COLUMN sbnweb.tb_stat_parameter.tipo IS 'tipo della variabile; tipi ammessi: int, string, data, listaBib, combo';
COMMENT ON COLUMN sbnweb.tb_stat_parameter.etichetta_nome IS 'Etichetta da associare al campo di mappa per l''acquisizione del valore da associare alla variabile';
COMMENT ON COLUMN sbnweb.tb_stat_parameter.obbligatorio IS 'indicatore di obbligatorieta'' nella valorizzazione della variabile: S obbligatorio, N facoltativo';
COMMENT ON TABLE sbnweb.tb_termine_thesauro IS 'TERMINI DESCRITTORI DI THESAURO (ex TPSDTH))';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.did IS 'codice identificativo del termine di thesauro';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.cd_the IS 'codice thesauro';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.ds_termine_thesauro IS 'descrizione del termine di thesauro';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.nota_termine_thesauro IS 'note al termine di thesauro';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.ky_termine_thesauro IS 'chiave di ricerca del termine di thesauro';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.tp_forma_the IS 'Forma del nome del termine di Thesauro (''A''=Accettata, ''R''=Rinvio)';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.cd_livello IS 'Livello autorita'' bibliografica';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.fl_condiviso IS 'Indicatore di gestione condivisa con il sistema indice';
COMMENT ON COLUMN sbnweb.tb_termine_thesauro.tidx_vector IS 'Indice delle parole contenute nel termine';
COMMENT ON TABLE sbnweb.tb_titolo IS 'OGGETTI BIBLIOGRAFICI';
COMMENT ON COLUMN sbnweb.tb_titolo.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tb_titolo.isadn IS 'Chiave univoca in Authority File';
COMMENT ON COLUMN sbnweb.tb_titolo.tp_materiale IS 'Tipo materiale in relaz. alle funzioni';
COMMENT ON COLUMN sbnweb.tb_titolo.tp_record_uni IS 'Genere materiale in UNIMARC';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_natura IS 'Natura della pubblicazione';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_paese IS 'Paese di pubblicazione';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_lingua_1 IS '1^ lingua di pubblicazione';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_lingua_2 IS '2^ lingua di pubblicazione';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_lingua_3 IS '3^ lingua di pubblicazione';
COMMENT ON COLUMN sbnweb.tb_titolo.aa_pubb_1 IS '1^ data di pubblicazione (DATA1)';
COMMENT ON COLUMN sbnweb.tb_titolo.aa_pubb_2 IS '2^ data di pubblicazione (DATA2)';
COMMENT ON COLUMN sbnweb.tb_titolo.tp_aa_pubb IS 'Tipo data';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_genere_1 IS 'Primo genere della pubblicazione';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_genere_2 IS 'Secondo genere della pubblicazione';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_genere_3 IS 'Terzo genere della pubblicazione';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_genere_4 IS 'Quarto genere della pubblicazione';
COMMENT ON COLUMN sbnweb.tb_titolo.ky_cles1_t IS 'CLES titolo proprio (primi 6 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_titolo.ky_cles2_t IS 'CLES titolo proprio (ultimi 44 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_titolo.ky_clet1_t IS 'CLET titolo proprio (primi 3 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_titolo.ky_clet2_t IS 'CLET titolo proprio (ultimi 3 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_titolo.ky_cles1_ct IS 'CLES complemento titolo (primi 6 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_titolo.ky_cles2_ct IS 'CLES complemento titolo (ultimi 44 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_titolo.ky_clet1_ct IS 'CLET complemento titolo (primi 3 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_titolo.ky_clet2_ct IS 'CLET complemento titolo (ultimi 3 chr. per indicizzazione)';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_livello IS 'Livello di autorita''';
COMMENT ON COLUMN sbnweb.tb_titolo.fl_speciale IS 'Indicatore presenza chr. speciali in ISBD';
COMMENT ON COLUMN sbnweb.tb_titolo.isbd IS 'Descrizione bibliografica';
COMMENT ON COLUMN sbnweb.tb_titolo.indice_isbd IS 'Informazioni per la suddivisione in aree ISBD';
COMMENT ON COLUMN sbnweb.tb_titolo.ky_editore IS 'Chiave dell''editore (estratto da ISBD)';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_agenzia IS 'Agenzia di catalogazione';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_norme_cat IS 'Norme di catalogazione';
COMMENT ON COLUMN sbnweb.tb_titolo.nota_inf_tit IS 'Note informative (solo per nature ''A'')';
COMMENT ON COLUMN sbnweb.tb_titolo.nota_cat_tit IS 'Note del catalogatore';
COMMENT ON COLUMN sbnweb.tb_titolo.bid_link IS 'BID del titolo accorpante o dell''attuale se cancellato';
COMMENT ON COLUMN sbnweb.tb_titolo.tp_link IS 'Tipo legame al BID_Link : (F)usione o (D)uplicazione';
COMMENT ON COLUMN sbnweb.tb_titolo.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_titolo.ts_ins IS 'Data di inserimento';
COMMENT ON COLUMN sbnweb.tb_titolo.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_titolo.ts_var IS 'Data di variazione';
COMMENT ON COLUMN sbnweb.tb_titolo.ute_forza_ins IS 'Utente che ha forzato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_titolo.ute_forza_var IS 'Utente che ha forzato la variazione';
COMMENT ON COLUMN sbnweb.tb_titolo.fl_canc IS 'Indicatore di cancellazione logica';
COMMENT ON COLUMN sbnweb.tb_titolo.cd_periodicita IS 'Codice di periodicita'' (solo per nature S)';
COMMENT ON COLUMN sbnweb.tb_titolo.fl_condiviso IS 'Indicatore di catalogazione condivisa con l''Indice: ''s''=condivisa, ''n''=solo locale';
COMMENT ON COLUMN sbnweb.tb_titolo.ute_condiviso IS 'Timestamp di condivisione gestione del legame con indice';
COMMENT ON COLUMN sbnweb.tb_titolo.ts_condiviso IS 'Utente che ha attivato la gestione condivisa del legame con indice';
COMMENT ON TABLE sbnweb.tba_buono_ordine IS 'Buono d''ordine';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.id_buono_ordine IS 'identificativo del buono d''ordine';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.id_capitoli_bilanci IS 'identificativo del capitolo di bilancio';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.buono_ord IS 'codice del buono d''ordine';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.cod_fornitore IS 'codice del fornitore';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.stato_buono IS 'stato del buono d''ordine';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.data_buono IS 'data del buono d''ordine';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.ute_ins IS 'identificativo dell''utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.ute_var IS 'identificativo dell''utente che ha effettuato l''ultima modifica';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.fl_canc IS 'flag di cancellazione logica del record';
COMMENT ON COLUMN sbnweb.tba_buono_ordine.cod_mat IS 'codice identificativo del tipo di materiale';
COMMENT ON TABLE sbnweb.tba_cambi_ufficiali IS 'Cambi ufficiali (ex TLACAM)';
COMMENT ON COLUMN sbnweb.tba_cambi_ufficiali.id_valuta IS 'identificativo della valuta';
COMMENT ON COLUMN sbnweb.tba_cambi_ufficiali.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tba_cambi_ufficiali.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tba_cambi_ufficiali.valuta IS 'codice della valuta';
COMMENT ON COLUMN sbnweb.tba_cambi_ufficiali.cambio IS 'tasso di cambio';
COMMENT ON COLUMN sbnweb.tba_cambi_ufficiali.data_var IS 'data di variazione';
COMMENT ON COLUMN sbnweb.tba_cambi_ufficiali.ute_ins IS 'identificativo dell''utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tba_cambi_ufficiali.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tba_cambi_ufficiali.ute_var IS 'identificativo dell''utente che ha effettuato l''ultima modifica';
COMMENT ON COLUMN sbnweb.tba_cambi_ufficiali.ts_var IS 'data e ora dell''ultimo aggiornamento ';
COMMENT ON COLUMN sbnweb.tba_cambi_ufficiali.fl_canc IS 'flag di cancellazione logica del record';
COMMENT ON TABLE sbnweb.tba_fatture IS 'Intestazione Fatture / Note di credito';
COMMENT ON COLUMN sbnweb.tba_fatture.id_fattura IS 'identificativo della fattura';
COMMENT ON COLUMN sbnweb.tba_fatture.cod_fornitore IS 'codice del fornitore';
COMMENT ON COLUMN sbnweb.tba_fatture.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tba_fatture.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tba_fatture.anno_fattura IS 'anno di registrazione della fattura';
COMMENT ON COLUMN sbnweb.tba_fatture.progr_fattura IS 'progressivo che identifica la fattura nell''ambito dell''anno ';
COMMENT ON COLUMN sbnweb.tba_fatture.num_fattura IS 'numero della fattura emessa dal fornitore';
COMMENT ON COLUMN sbnweb.tba_fatture.data_fattura IS 'data della fattura del fornitore';
COMMENT ON COLUMN sbnweb.tba_fatture.data_reg IS 'data di registrazione della fattura';
COMMENT ON COLUMN sbnweb.tba_fatture.importo IS 'importo totale della fattura';
COMMENT ON COLUMN sbnweb.tba_fatture.sconto IS 'sconto';
COMMENT ON COLUMN sbnweb.tba_fatture.valuta IS 'codice della valuta';
COMMENT ON COLUMN sbnweb.tba_fatture.cambio IS 'cambio corrente';
COMMENT ON COLUMN sbnweb.tba_fatture.stato_fattura IS 'stato della fattura';
COMMENT ON COLUMN sbnweb.tba_fatture.tipo_fattura IS 'codice della tipologia della fattura';
COMMENT ON COLUMN sbnweb.tba_fatture.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tba_fatture.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tba_offerte_fornitore IS 'Offerte del fornitore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.id_offerte_fornitore IS 'identificativo dell''offerta del fornitore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.cod_fornitore IS 'codice del fornitore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.bid_p IS 'codice identificativo dell''offerta del fornitore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.tip_rec IS 'codice identificativo del tipo provenienza dei dati';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.natura IS 'codice identificativo della natura bibliografica';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.paese IS 'codice identificativo del paese';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.lingua IS 'codice identificativo della lingua';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.tipo_data IS 'codice del tipo di data';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.data1 IS 'anno di pubblicazione';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.num_standard IS 'codice isbn/issn';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.aut1 IS 'primo autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.k_aut1 IS 'chiave normalizzata del primo autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.tip_aut1 IS 'tipologia dell''autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.resp_aut1 IS 'codice del tipo di responsabilita'' dell''autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.aut2 IS 'secondo autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.k_aut2 IS 'chiave normalizzata del secondo autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.tip_aut2 IS 'tipologia dell''autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.resp_aut2 IS 'codice del tipo di responsabilita'' dell''autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.aut3 IS 'terzo autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.k_aut3 IS 'chiave normalizzata del terzo autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.tip_aut3 IS 'tipologia dell''autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.resp_aut3 IS 'codice del tipo di responsabilita'' dell''autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.aut4 IS 'quarto autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.k_aut4 IS 'chiave normalizzata del quarto autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.tip_aut4 IS 'tipologia dell''autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.resp_aut4 IS 'codice del tipo di responsabilita'' dell''autore';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.isbd_1 IS 'descrizione in formato isbd';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.isbd_2 IS 'descrizione in formato isbd';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.k_titolo IS 'chiave normalizzata del titolo';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.serie1 IS 'collana';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.k1_serie IS 'chiave normalizzata della collana';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.num1_serie IS 'numero della collana';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.serie2 IS 'collana';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.k2_serie IS 'chiave normalizzata della collana';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.num2_serie IS 'numero della collana';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.serie3 IS 'collana';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.k3_serie IS 'chiave normalizzata della collana';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.num3_serie IS 'numero della collana';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.tipo1_classe IS 'codice del sistema di classificazione';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.classe1 IS 'codice identificativo della classe';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.tipo2_classe IS 'codice del sistema di classificazione';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.classe2 IS 'codice identificativo della classe';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.tipo3_classe IS 'codice del sistema di classificazione';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.classe3 IS 'codice identificativo della classe';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.sog1 IS 'soggetto';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.k_sog1 IS 'chiave normalizzata del soggetto';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.sog2 IS 'soggetto';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.k_sog2 IS 'chiave normalizzata del soggetto';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.sog3 IS 'soggetto';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.k_sog3 IS 'chiave normalizzata del soggetto';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.num_stand_pro IS 'numero standard assegnato dal libraio o dal db esterno';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.data_offerta IS 'data dell''offerta (edi)';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.num_offerta_g IS 'numero dell''offerta generale';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.num_linea IS 'numero di linea dell''offerta';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.valuta IS 'codice della valuta';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.prezzo IS 'prezzo';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.tipo_prezzo IS 'codice della tipologia di prezzo (ca=catalogo, ct=contratto, di=prezzo del distributore)';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.inf_sul_prezzo IS 'informazioni sul prezzo';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.altri_rif IS 'altri riferimenti';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.note_edi IS 'note edi';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.prot_edi IS 'coordinate messaggio edi';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.num_offerta_p IS 'numero dell''offerta particolare';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.bid IS 'codice identificativo dell''oggetto bibliograficoe';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.stato_offerta IS 'stato dell''offerta';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tba_offerte_fornitore.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tba_ordini IS 'Ordini';
COMMENT ON COLUMN sbnweb.tba_ordini.id_ordine IS 'codice identificativo dell''ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tba_ordini.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tba_ordini.cod_tip_ord IS 'codice identificativo della tipologia dell''ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.anno_ord IS 'anno di acquisizione dell''ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.cod_ord IS 'numero dell''ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.cod_fornitore IS 'codice del fornitore';
COMMENT ON COLUMN sbnweb.tba_ordini.id_sez_acquis_bibliografiche IS 'identificativo della sezione di acquisizione';
COMMENT ON COLUMN sbnweb.tba_ordini.id_valuta IS 'identificativo della valuta';
COMMENT ON COLUMN sbnweb.tba_ordini.id_capitoli_bilanci IS 'identificativo del capitolo di bilancio';
COMMENT ON COLUMN sbnweb.tba_ordini.data_ins IS 'data e ora d''inserimento (valorizzato all''atto della creazione come ts_ins, ma non utilizzato)';
COMMENT ON COLUMN sbnweb.tba_ordini.data_agg IS 'data e ora di stampa del buono d''ordine a cui appartiene l''ordine (utilizzata per la ristampa dell''ordine)';
COMMENT ON COLUMN sbnweb.tba_ordini.data_ord IS 'data invio dell''ordine: inserita uguale a ts_ins e aggiornata con la data di esecuzione della prima stampa (visualizzata in esame, stampa e ristampa ordine)';
COMMENT ON COLUMN sbnweb.tba_ordini.note IS 'osservazioni relative all''ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.num_copie IS 'numero di copie ordinate';
COMMENT ON COLUMN sbnweb.tba_ordini.continuativo IS 'flag indicante se l''ordine e'' continuativo';
COMMENT ON COLUMN sbnweb.tba_ordini.stato_ordine IS 'codice identificativo dello stato dell''ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.tipo_doc_lett IS 'codice identificativo della tipologia del documento lettore (s = suggerimento)';
COMMENT ON COLUMN sbnweb.tba_ordini.cod_doc_lett IS 'codice identificativo del documento suggerito dal lettore';
COMMENT ON COLUMN sbnweb.tba_ordini.tipo_urgenza IS 'codice identificativo della tipologia di urgenza';
COMMENT ON COLUMN sbnweb.tba_ordini.cod_rich_off IS 'codice identificativo della richiesta di offerta';
COMMENT ON COLUMN sbnweb.tba_ordini.bid_p IS 'codice identificativo dell''offerta del fornitore';
COMMENT ON COLUMN sbnweb.tba_ordini.note_forn IS 'note per il fornitore in riferimento all''ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.tipo_invio IS 'codice identificativo del tipo invio';
COMMENT ON COLUMN sbnweb.tba_ordini.anno_1ord IS 'anno di acquisizione del primo ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.cod_1ord IS 'codice identificativo del primo ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.prezzo IS 'prezzo in valuta';
COMMENT ON COLUMN sbnweb.tba_ordini.paese IS 'codice identificativo del paese';
COMMENT ON COLUMN sbnweb.tba_ordini.cod_bib_sugg IS 'codice identificativo della biblioteca dove e'' stato effettuato il suggerimento dal bibliotecario';
COMMENT ON COLUMN sbnweb.tba_ordini.cod_sugg_bibl IS 'codice identificativo del suggerimento del bibliotecario';
COMMENT ON COLUMN sbnweb.tba_ordini.bid IS 'codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.tba_ordini.stato_abb IS 'codice identificativo dello stato dell''abbonamento';
COMMENT ON COLUMN sbnweb.tba_ordini.cod_per_abb IS 'codice del periodo di validita'' dell''abbonamento';
COMMENT ON COLUMN sbnweb.tba_ordini.prezzo_lire IS 'prezzo in valuta';
COMMENT ON COLUMN sbnweb.tba_ordini.reg_trib IS 'numero della registrazione del tribunale per deposito legale, data di registrazione e citta'' dove si e'' effettuata la registrazione ';
COMMENT ON COLUMN sbnweb.tba_ordini.anno_abb IS 'anno dell''abbonamento';
COMMENT ON COLUMN sbnweb.tba_ordini.num_fasc IS 'numero del fascicolo (primo fascicolo se l''abbonamento e'' da aprire, ultimo fascicolo se l''abbonamento e'' da chiudere)';
COMMENT ON COLUMN sbnweb.tba_ordini.data_fasc IS 'data di pubblicazione del fascicolo (primo fascicolo se l''abbonamento e'' da aprire, ultimo fascicolo se l''abbonamento e'' da chiudere)';
COMMENT ON COLUMN sbnweb.tba_ordini.annata IS 'anno descrittivo dell''abbonamento';
COMMENT ON COLUMN sbnweb.tba_ordini.num_vol_abb IS 'numero del volume relativo all''abbonamento';
COMMENT ON COLUMN sbnweb.tba_ordini.natura IS 'codice identificativo della natura bibliografica';
COMMENT ON COLUMN sbnweb.tba_ordini.data_fine IS 'data di chiusura abbonamento';
COMMENT ON COLUMN sbnweb.tba_ordini.stampato IS 'flag di ordine stampato';
COMMENT ON COLUMN sbnweb.tba_ordini.rinnovato IS 'flag di ordine rinnovato';
COMMENT ON COLUMN sbnweb.tba_ordini.data_chiusura_ord IS 'data e ora di chiusura dell''ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.tbb_bilancicod_mat IS 'codice identificativo del tipo di materiale';
COMMENT ON COLUMN sbnweb.tba_ordini.ute_ins IS 'identificativo dell''utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tba_ordini.ute_var IS 'identificativo dell''utente che ha effettuato l''ultima modifica';
COMMENT ON COLUMN sbnweb.tba_ordini.ts_ins IS 'data e ora dell''inserimento dell''ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.ts_var IS 'data e ora dell''ultima modifica dell''ordine';
COMMENT ON COLUMN sbnweb.tba_ordini.fl_canc IS 'Indicatoe di cancellazione logica del record (S=cancellato)';
COMMENT ON COLUMN sbnweb.tba_ordini.cd_tipo_lav IS 'Tipo lavorazione per ordine di Rilegatura (CCND)';
COMMENT ON TABLE sbnweb.tba_parametri_buono_ordine IS 'Parametri per buono d''ordine (intestazioni / fondo pagina)';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.cd_biblioteca IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.progr IS 'progressivo delle righe dei dati di intestazione e fine stampa';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.codice_buono IS 'indicatore del tipo di numerazione del buono d''ordine (a=progressivo automatico; m=manuale)';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.descr_test IS 'testo della n.esima riga di intestazione buono d''ordine';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.descr_foot IS 'testo della n.esima riga dei dati di fine stampa del buono d''ordine';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.ute_ins IS 'identificativo dell''utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.ts_ins IS 'data e ora dell''inserimento dell''ordine';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.ute_var IS 'identificativo dell''utente che ha effettuato l''ultima modifica';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.ts_var IS 'data e ora dell''ultima modifica dell''ordine';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.fl_canc IS 'flag di cancellazione logica del record';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.descr_oggetto IS 'testo dell''oggetto distinto per lingua e tipologia d''ordine';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.descr_formulaintr IS 'testo della formula introduttiva distinto per lingua e tipologia d''ordine';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.area_titolo IS 'Area titolo ISBD S=si, N=no';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.area_ediz IS 'Area edizione ISBD S=si, N=no';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.area_num IS 'Area numerazione ISBD S=si, N=no';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.area_pub IS 'Area pubblicazione ISBD S=si, N=no';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.logo IS 'indicatore di presenza del logo';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.logo_img IS 'nome fisico del file di immagine logo di biblioteca';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.prezzo IS 'indicatore della presenza del prezzo degli ordini';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.nprot IS 'indicatore della presenza del numero di protocollo';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.dataprot IS 'indicatore dell''etichetta di data di protocollo';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.rinnovo IS 'Indicatore dell''ordine rinnovato O=originario P=precedente N=nessuno';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.firmadigit IS 'indicatore di presenza della firma digitale';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.firmadigit_img IS 'nome fisico del file di immagine della firma digitale';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.ristampa IS 'indicatore di ristampa';
COMMENT ON COLUMN sbnweb.tba_parametri_buono_ordine.xml_formulaintr IS 'xml del testo della formula introduttiva distinto per tipo lavorazione';
COMMENT ON TABLE sbnweb.tba_parametri_ordine IS ' ';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.gest_bil IS 'indicatore di gestione del bilancio da parte della biblioteca';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.gest_sez IS 'indicatore di gestione di sezioni di acquiszioni da parte della biblioteca';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.gest_prof IS 'indicatore di gestione dei profili di acquisto da parte della biblioteca';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cod_prac IS 'identificativo del profilo di acquisto di default';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cod_sezione IS 'identificativo della sezione di acquisizione di default per i nuovi ordini';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.esercizio IS 'identificativo dell''esercizio di default per i nuovi ordini';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.capitolo IS 'identificativo del capitolo di bilancio  di default per i nuovi ordini';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cod_mat IS 'identificativo del tipo di materiale di default per i nuovi ordini';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cod_fornitore_a IS 'codice del fornitore di default per i nuovi ordini di tipo A';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cod_fornitore_l IS 'codice del fornitore di default per i nuovi ordini di tipo L';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cod_fornitore_d IS 'codice del fornitore di default per i nuovi ordini di tipo D';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cod_fornitore_v IS 'codice del fornitore di default per i nuovi ordini di tipo V';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cod_fornitore_c IS 'codice del fornitore di default per i nuovi ordini di tipo C';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cod_fornitore_r IS 'codice del fornitore di default per i nuovi ordini di tipo R';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.ordini_aperti IS 'indicatore di visualizzazione di default di ordini con stato A';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.ordini_chiusi IS 'indicatore di visualizzazione di default di ordini con stato C';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.ordini_annullati IS 'indicatore di visualizzazione di default di ordini con stato N';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.allineamento_prezzo IS 'Indicazione della tipologia di adeguamento del prezzo previsto al prezzo reale: A automatico; R a richiesta; N non gestito';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.ute_ins IS 'identificativo dell''utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.ts_ins IS 'data e ora dell''inserimento dell''ordine';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.ute_var IS 'identificativo dell''utente che ha effettuato l''ultima modifica';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.ts_var IS 'data e ora dell''ultima modifica dell''ordine';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.fl_canc IS 'flag di cancellazione logica del record';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cd_bib_google IS 'Codice Google per la biblioteca';
COMMENT ON COLUMN sbnweb.tba_parametri_ordine.cd_forn_google IS 'codice fornitore per Google';
COMMENT ON TABLE sbnweb.tba_profili_acquisto IS 'Profili d''acquisto';
COMMENT ON COLUMN sbnweb.tba_profili_acquisto.cod_prac IS 'codice identificativo del profilo d''acquisto';
COMMENT ON COLUMN sbnweb.tba_profili_acquisto.descr IS 'descrizione del profilo d''acquisizione';
COMMENT ON COLUMN sbnweb.tba_profili_acquisto.paese IS 'codice identificativo del paese';
COMMENT ON COLUMN sbnweb.tba_profili_acquisto.lingua IS 'codice identificativo della lingua';
COMMENT ON COLUMN sbnweb.tba_profili_acquisto.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tba_profili_acquisto.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tba_profili_acquisto.id_sez_acquis_bibliografiche IS 'identificativo della sezione d''acquisto';
COMMENT ON TABLE sbnweb.tba_richieste_offerta IS 'Richieste d''offerta';
COMMENT ON COLUMN sbnweb.tba_richieste_offerta.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tba_richieste_offerta.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tba_richieste_offerta.cod_rich_off IS 'codice identificativo della richiesta d''offerta';
COMMENT ON COLUMN sbnweb.tba_richieste_offerta.data_rich_off IS 'data della richiesta d''offerta';
COMMENT ON COLUMN sbnweb.tba_richieste_offerta.prezzo_rich IS 'prezzo indicativo per iniziare la gara';
COMMENT ON COLUMN sbnweb.tba_richieste_offerta.num_copie IS 'numero di copie richieste in acquisto';
COMMENT ON COLUMN sbnweb.tba_richieste_offerta.note IS 'osservazioni relative all''ordine';
COMMENT ON COLUMN sbnweb.tba_richieste_offerta.stato_rich_off IS 'codice dello stato della richiesta di offerta';
COMMENT ON COLUMN sbnweb.tba_richieste_offerta.bid IS 'codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.tba_richieste_offerta.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tba_richieste_offerta.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tba_righe_fatture IS 'Righe delle Fatture / Note di credito';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.id_fattura IS 'identificativo della fattura';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.cd_biblioteca IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.riga_fattura IS 'numero della riga fattura';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.id_ordine IS 'identificativo dell''ordine';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.id_capitoli_bilanci IS 'identificativo del capitolo di bilancio';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.cod_mat IS 'codice identificativo del tipo di materiale';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.note IS 'note relative al materiale acquistato';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.importo_riga IS 'imponibile';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.sconto_1 IS 'primo sconto effettuato';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.sconto_2 IS 'secondo sconto effettuato';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.cod_iva IS 'codice identificativo dell''iva';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.id_fattura_in_credito IS 'ID della fattura cui si rimanda per giustificare la nota di credito';
COMMENT ON COLUMN sbnweb.tba_righe_fatture.riga_fattura_in_credito IS 'riga della fattura cui si rimanda per giustificare la nota di credito';
COMMENT ON TABLE sbnweb.tba_sez_acquis_bibliografiche IS 'Sezioni d''acquisizione bibliografiche';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.id_sez_acquis_bibliografiche IS 'identificativo della sezione di acquisizione';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.cod_sezione IS 'codice identificativo della sezione d''acquisizione';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.nome IS 'descrizione della sezione d''acquisizione';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.note IS 'note relative alla sezione d''acquisizione';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.somma_disp IS 'somma disponibile per la sezione relativa agli acquisti ';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.budget IS 'somma iniziale disponibile per la sezione relativa agli acquisti';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.anno_val IS 'anno di validita della sezione';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tba_sez_acquis_bibliografiche.data_val IS 'data di fine validita della sezione di acquisto';
COMMENT ON TABLE sbnweb.tba_suggerimenti_bibliografici IS 'Suggerimenti d''acquisto dei bibliotecari';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.cod_sugg_bibl IS 'codice identificativo del suggerimento del bibliotecario';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.data_sugg_bibl IS 'data del suggerimento';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.note IS 'note relative al suggerimento del bibliotecario';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.msg_per_bibl IS 'messaggio per il bibliotecario relativo al suggerimento';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.note_forn IS 'indicazioni di massima su uno o piu'' fornitori per la creazione dell''ordine date dal bibliotecario ';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.cod_bib_cs IS 'codice identificativo della biblioteca centro sistema di riferimento';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.bid IS 'codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.cod_bibliotecario IS 'codice identificativo del bibliotecario';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.stato_sugg IS 'stato del suggerimento';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tba_suggerimenti_bibliografici.id_sez_acquis_bibliografiche IS 'identificativo della sezione di acquisto';
COMMENT ON TABLE sbnweb.tbb_bilanci IS 'Capitoli di bilancio per esercizio';
COMMENT ON COLUMN sbnweb.tbb_bilanci.cod_mat IS 'codice identificativo del tipo di materiale';
COMMENT ON COLUMN sbnweb.tbb_bilanci.id_capitoli_bilanci IS 'identificativo del capitolo di bilancio';
COMMENT ON COLUMN sbnweb.tbb_bilanci.id_buono_ordine IS 'identificativo del buono d''ordine';
COMMENT ON COLUMN sbnweb.tbb_bilanci.budget IS 'budget assegnato';
COMMENT ON COLUMN sbnweb.tbb_bilanci.ordinato IS 'totale del materiale ordinato';
COMMENT ON COLUMN sbnweb.tbb_bilanci.fatturato IS 'totale del materiale fatturato';
COMMENT ON COLUMN sbnweb.tbb_bilanci.pagato IS 'totale del materiale fatturato e gia'' pagato';
COMMENT ON COLUMN sbnweb.tbb_bilanci.ts_ins IS 'data e ora d''inserimento;';
COMMENT ON COLUMN sbnweb.tbb_bilanci.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tbb_bilanci.acquisito IS 'totale del materiale acquisito';
COMMENT ON TABLE sbnweb.tbb_capitoli_bilanci IS 'Capitoli di bilancio per esercizio imputazione';
COMMENT ON COLUMN sbnweb.tbb_capitoli_bilanci.id_capitoli_bilanci IS 'identificativo del capitolo di bilancio';
COMMENT ON COLUMN sbnweb.tbb_capitoli_bilanci.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tbb_capitoli_bilanci.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tbb_capitoli_bilanci.esercizio IS 'anno di esercizio';
COMMENT ON COLUMN sbnweb.tbb_capitoli_bilanci.capitolo IS 'codice identificativo del capitolo di bilancio';
COMMENT ON COLUMN sbnweb.tbb_capitoli_bilanci.budget IS 'budget assegnato';
COMMENT ON COLUMN sbnweb.tbb_capitoli_bilanci.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbb_capitoli_bilanci.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tbc_collocazione IS 'COLLOCAZIONE (ex TLCLOC)';
COMMENT ON COLUMN sbnweb.tbc_collocazione.cd_sistema IS 'codice identificativo del sistema di classificazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.cd_edizione IS 'codice identificativo dell''edizione Dewey';
COMMENT ON COLUMN sbnweb.tbc_collocazione.classe IS 'codice classificazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.cd_biblioteca_sezione IS 'codice identificativo della biblioteca della sezione di collocazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.cd_sez IS 'codice identificativo della sezione di collocazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.cd_polo_sezione IS 'codice identificativo del polo della sezione di collocazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.cd_biblioteca_doc IS 'codice identificativo della biblioteca dell''esemplare di collocazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.cd_polo_doc IS 'codice identificativo del polo dell''esemplare di collocazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.bid_doc IS 'codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.tbc_collocazione.cd_doc IS 'progressivo di esemplare per titolo e biblioteca';
COMMENT ON COLUMN sbnweb.tbc_collocazione.key_loc IS 'chiave univoca di individuazione della collocazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.bid IS 'codice identificativo dell''oggetto bibliografico  del livello di collocazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.cd_loc IS 'collocazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.spec_loc IS 'specificazione della collocazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.consis IS 'consistenza della collocazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.tot_inv IS 'numero totale degli inventari collocati';
COMMENT ON COLUMN sbnweb.tbc_collocazione.indice IS 'simbolo di classificazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.ord_loc IS 'chiave per ordinamento alfabetico calcolata dalla stringa di collocazione cd_loc';
COMMENT ON COLUMN sbnweb.tbc_collocazione.ord_spec IS 'chiave per ordinamento alfabetico calcolata dalla stringa di specificazione spec_loc';
COMMENT ON COLUMN sbnweb.tbc_collocazione.tot_inv_prov IS 'numero di inventari spostati in una collocazione temporanea';
COMMENT ON COLUMN sbnweb.tbc_collocazione.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbc_collocazione.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbc_collocazione.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbc_collocazione.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbc_default_inven_schede IS 'DATI DI SERVIZIO RELATIVI ALL''INVENTARIO';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.id_default_inven_scheda IS 'codice identificativo univoco dell''oggetto';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.cd_biblioteca IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.n_copie_tit IS 'numero di copie di schede per catalogo titoli';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.n_copie_edi IS 'numero di copie di schede per catalogo editori';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.n_copie_poss IS 'numero di copie di schede per catalogo possessori';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.n_copie_richiamo IS 'numero di copie di schede di richiamo';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.cd_unimarc IS 'codice di default del tipo di scarico unimarc utilizzato dalla biblioteca';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.sch_autori IS 's=schede per catalogo autori';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.fl_coll_aut IS 's=stampa per collocazione su schede per autori';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.fl_tipo_leg IS 'flag dei tipi di legame da stampare sulle schede del catalogo autori (t=tutti, p=solo principali, r=solo richiami)';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.sch_topog IS 's=schede per catalogo topografico';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.fl_coll_top IS 's=stampa collocazione su schede per topografico';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.sch_soggetti IS 's=schede per catalogo soggetti';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.fl_coll_sog IS 's=stampa collocazione su schede per soggetti';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.sch_classi IS 's=schede per catalogo classi';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.fl_coll_cla IS 's=stampa collocazione su schede per classi';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.sch_titoli IS 's=schede per catalogo titoli';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.fl_coll_tit IS 's=stampa collocazione su schede per titoli';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.sch_edit IS 's=schede per catalogo editori';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.fl_coll_edi IS 's=stampa collocazione su schede per editori';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.sch_poss IS 's=schede per catalogo possessori';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.fl_coll_poss IS 's=stampa collocazione su schede per possessori';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.flg_coll_richiamo IS 's=stampa collocazione su schede di richiamo';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.fl_non_inv IS 's=stampa schede anche per notizie non possedute';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.tipo IS 'non utilizzato';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.n_serie IS 'indicatore di utilizzo di serie inventariali da parte della biblioteca (0=la biblioteca non gestisce serie, 1=la biblioteca gestisce le serie inventarialI)';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.n_piste IS 'numero di piste sul modulo etichette';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.n_copie IS 'numero di copie di una etichetta';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.n_copie_aut IS 'numero delle copie di schede per catalogo autori';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.n_copie_top IS 'numero di copie di schede per catalogo topografico';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.n_copie_sog IS 'numero di copie di schede per catalogo soggetti';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.n_copie_cla IS 'numero di copie di schede per catalogo classi';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.id_modello IS 'codice identificativo del modello di stampa';
COMMENT ON COLUMN sbnweb.tbc_default_inven_schede.formato_stampa IS 'estensione del formato di stampa';
COMMENT ON TABLE sbnweb.tbc_esemplare_titolo IS 'ESEMPLARE TITOLO (ex TLCDOC)';
COMMENT ON COLUMN sbnweb.tbc_esemplare_titolo.cd_doc IS 'progressivo di esemplare per titolo e biblioteca';
COMMENT ON COLUMN sbnweb.tbc_esemplare_titolo.bid IS 'codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.tbc_esemplare_titolo.cd_biblioteca IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tbc_esemplare_titolo.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tbc_esemplare_titolo.cons_doc IS 'Consistenza dell''esemplare titolo';
COMMENT ON COLUMN sbnweb.tbc_esemplare_titolo.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbc_esemplare_titolo.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbc_esemplare_titolo.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbc_esemplare_titolo.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbc_esemplare_titolo.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbc_inventario IS 'INVENTARI (ex TLCINV)';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_polo IS 'codice identificativo del polo della serie inventariale';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_bib IS 'codice identificativo della biblioteca della serie inventariale';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_serie IS 'codice identificativo della serie inventariale';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_inven IS 'numero d''inventario del documento';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_proven IS 'codice identificatico della provenienza dell''inventario';
COMMENT ON COLUMN sbnweb.tbc_inventario.key_loc IS 'codice identificativo univoco della collocazione';
COMMENT ON COLUMN sbnweb.tbc_inventario.bid IS 'codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_polo_proven IS 'codice identificatico del polo di provenienza dell''inventario';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_biblioteca_proven IS 'codice identificatico della biblioteca di provenienza dell''inventario';
COMMENT ON COLUMN sbnweb.tbc_inventario.key_loc_old IS 'chiave della collocazione definitiva (utilizzato nella funzione di spostamento temporaneo di collocazioni)';
COMMENT ON COLUMN sbnweb.tbc_inventario.seq_coll IS 'sequenza della collocazione';
COMMENT ON COLUMN sbnweb.tbc_inventario.precis_inv IS 'precisazione dell''inventario';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_sit IS 'codice identificativo della situazione amministrativa';
COMMENT ON COLUMN sbnweb.tbc_inventario.valore IS 'valore inventariale';
COMMENT ON COLUMN sbnweb.tbc_inventario.importo IS 'importo reale';
COMMENT ON COLUMN sbnweb.tbc_inventario.num_vol IS 'numero dei volumi in cui risultano rilegati i periodici aventi uno stesso numero di inventario';
COMMENT ON COLUMN sbnweb.tbc_inventario.tot_loc IS 'totale dei prestiti locali dell''inventario';
COMMENT ON COLUMN sbnweb.tbc_inventario.tot_inter IS 'totale dei prestiti interbibliotecari';
COMMENT ON COLUMN sbnweb.tbc_inventario.anno_abb IS 'anno di abbonamento dell''inventario (per periodici)';
COMMENT ON COLUMN sbnweb.tbc_inventario.flg_disp IS 'indicatore di disponibilia''/non disponibilita''';
COMMENT ON COLUMN sbnweb.tbc_inventario.flg_nuovo_usato IS 'indicatore di inventario acquisto usato ammette i valori '' '' =nuovo, ''u'' = usato';
COMMENT ON COLUMN sbnweb.tbc_inventario.stato_con IS 'codice dello stato di conservazione';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_fornitore IS 'codice del fornitore';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_mat_inv IS 'codice identificativo del materiale da inventariare';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_bib_ord IS 'codice identificativo della biblioteca che ha effettuato l''ordine';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_tip_ord IS 'codice identificativo della tipologia dell''ordine';
COMMENT ON COLUMN sbnweb.tbc_inventario.anno_ord IS 'anno d''acquisizione dell''ordine';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_ord IS 'codice identificativo dell''ordine';
COMMENT ON COLUMN sbnweb.tbc_inventario.riga_ord IS 'non utilizzato';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_bib_fatt IS 'codice identificativo della biblioteca che ha emesso la fattura';
COMMENT ON COLUMN sbnweb.tbc_inventario.anno_fattura IS 'anno di registrazione della fattura';
COMMENT ON COLUMN sbnweb.tbc_inventario.prg_fattura IS 'progressivo che identifica la fattura nell''ambito dell''anno';
COMMENT ON COLUMN sbnweb.tbc_inventario.riga_fattura IS 'numero della riga della fattura';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_no_disp IS 'codice di non disponibilita'' del documento';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_frui IS 'codice della fruizione: ammette i valori dati dalla combinazione dei vari tipi di servizio ammissibili per un inventario: ad esempio consultazione e prestito solo locale';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_carico IS 'codice di carico';
COMMENT ON COLUMN sbnweb.tbc_inventario.num_carico IS 'numero del buono di scarico o di carico';
COMMENT ON COLUMN sbnweb.tbc_inventario.data_carico IS 'data di scarico o carico';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_polo_scar IS 'codice polo della biblioteca verso cui si scarica';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_bib_scar IS 'codice identificativo della biblioteca verso cui si effettua lo scarico';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_scarico IS 'codice di scarico';
COMMENT ON COLUMN sbnweb.tbc_inventario.num_scarico IS 'numero del buono di scarico o di carico';
COMMENT ON COLUMN sbnweb.tbc_inventario.data_scarico IS 'data di scarico o carico';
COMMENT ON COLUMN sbnweb.tbc_inventario.data_delibera IS 'data della delibera di scarico';
COMMENT ON COLUMN sbnweb.tbc_inventario.delibera_scar IS 'testo della delibera di scarico';
COMMENT ON COLUMN sbnweb.tbc_inventario.sez_old IS 'sezione precedente';
COMMENT ON COLUMN sbnweb.tbc_inventario.loc_old IS 'collocazione precedente';
COMMENT ON COLUMN sbnweb.tbc_inventario.spec_old IS 'specificazione della collocazione precedente';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_supporto IS 'codice di tipo supporto';
COMMENT ON COLUMN sbnweb.tbc_inventario.ute_ins_prima_coll IS 'Utente che ha effettuato la prima collocazione dell''inventario';
COMMENT ON COLUMN sbnweb.tbc_inventario.ts_ins_prima_coll IS 'Timestamp di inserimento della prima collocazione';
COMMENT ON COLUMN sbnweb.tbc_inventario.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbc_inventario.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbc_inventario.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbc_inventario.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbc_inventario.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tbc_inventario.tipo_acquisizione IS 'Codice del tipo di acquisizione del materiale; se presente l''ordine deve essere coerente con tipo ordine';
COMMENT ON COLUMN sbnweb.tbc_inventario.supporto_copia IS 'Codice del tipo di supporto utilizzato per la copia del documento.';
COMMENT ON COLUMN sbnweb.tbc_inventario.digitalizzazione IS 'se diverso da null esprime la presenza di una digitalizzazione: 0=parziale, 1=completa, 2=born digital (l''orginale e'' gia'' digitale)';
COMMENT ON COLUMN sbnweb.tbc_inventario.disp_copia_digitale IS 'indica se e come e'' accessibile la copia digitale; valorizzato in base ai contenuti dell''apposita tabella codici';
COMMENT ON COLUMN sbnweb.tbc_inventario.id_accesso_remoto IS 'identificativo da fornire all''''applicazione che gestisce la teca delle copie digitali';
COMMENT ON COLUMN sbnweb.tbc_inventario.rif_teca_digitale IS 'codice identificativo della teca digitale; valori in tabella codici';
COMMENT ON COLUMN sbnweb.tbc_inventario.cd_riproducibilita IS 'codice che descrive il gruppo di supporti per i quali e'' ammesso servizio di riproduzione';
COMMENT ON COLUMN sbnweb.tbc_inventario.data_ingresso IS 'Data di ingresso in biblioteca';
COMMENT ON COLUMN sbnweb.tbc_inventario.data_redisp_prev IS 'Data di redisponibilita'' presunta';
COMMENT ON COLUMN sbnweb.tbc_inventario.id_bib_scar IS 'identificativo univoco della biblioteca di scarico';
COMMENT ON TABLE sbnweb.tbc_nota_inv IS 'Note codificate associate all''inventario';
COMMENT ON COLUMN sbnweb.tbc_nota_inv.cd_polo IS 'codice identificativo del polo della serie inventariale';
COMMENT ON COLUMN sbnweb.tbc_nota_inv.cd_bib IS 'codice identificativo del biblioteca della serie inventariale';
COMMENT ON COLUMN sbnweb.tbc_nota_inv.cd_serie IS 'codice identificativo della serie inventariale';
COMMENT ON COLUMN sbnweb.tbc_nota_inv.cd_inven IS 'codice identificativo dell''inventario';
COMMENT ON COLUMN sbnweb.tbc_nota_inv.cd_nota IS 'progressivo della nota all''inventario';
COMMENT ON COLUMN sbnweb.tbc_nota_inv.ds_nota_libera IS 'descrizione della nota all''inventario';
COMMENT ON COLUMN sbnweb.tbc_nota_inv.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbc_nota_inv.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbc_nota_inv.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbc_nota_inv.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbc_nota_inv.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbc_possessore_provenienza IS 'POSSESSORI E PROVENIENZE DI INVENTARIO';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.pid IS 'codice identificativo del possessore';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.tp_forma_pp IS 'codice identificativo della tipologia del nome';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ky_cautun IS 'chiave cautun';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ky_auteur IS 'chiave auteur';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ky_el1 IS 'primo elemento di ordinamento';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ky_el2 IS 'secondo elemento di ordinamento';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.tp_nome_pp IS 'codice forma. indica se il nome del possessore e'' in forma accettata o variante. ammette i valori: a=forma accettata, r=forma variante ';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ky_el3 IS 'terzo elemento di ordinamento';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ky_el4 IS 'quarto elemento di ordinamento';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ky_el5 IS 'quinto elemento di ordinamento';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ky_cles1_a IS 'note al possessore';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ky_cles2_a IS 'prima parte della chiave di ricerca possessori ottenuta dalla trasformazione in caratteri di ordinamento della descrizione';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.note IS 'seconda parte della chiave di ricerca possessori ottenuta dalla trasformazione in caratteri di ordinamento della descrizione';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.tot_inv IS 'numero totale degli inventari della base dati locale';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.cd_livello IS 'codice del livello di autorita''';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.fl_speciale IS 'indicatore della presenza di caratteri speciali';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ds_nome_aut IS 'descrizione del possessore';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tbc_possessore_provenienza.tidx_vector_ds_nome_aut IS 'indice delle parole contenute nel nome del possessore';
COMMENT ON TABLE sbnweb.tbc_provenienza_inventario IS 'PROVENIENZE INVENTARIO';
COMMENT ON COLUMN sbnweb.tbc_provenienza_inventario.cd_proven IS 'codice della provenienza dell''inventario (es. dono = dono, donoa = dono dell''autore, donoc = dono di enti pubblici, donoe = dono dell''editore) ';
COMMENT ON COLUMN sbnweb.tbc_provenienza_inventario.cd_biblioteca IS 'codice identificativo della biblioteca di provenienza dell''inventario';
COMMENT ON COLUMN sbnweb.tbc_provenienza_inventario.cd_polo IS 'codice identificativo del polo di provenienza dell''inventario';
COMMENT ON COLUMN sbnweb.tbc_provenienza_inventario.descr IS 'descrizione della provenienza dell''inventario';
COMMENT ON COLUMN sbnweb.tbc_provenienza_inventario.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbc_provenienza_inventario.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbc_provenienza_inventario.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbc_provenienza_inventario.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbc_provenienza_inventario.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbc_serie_inventariale IS 'SERIE INVENTARIALI';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.cd_serie IS 'codice identificativo di una suddivisione del posseduto della biblioteca, secondo l''organizzazione interna della stessa. se la biblioteca non gestisce serie inventariali, assume comunque valore space';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.cd_biblioteca IS 'codice identificativo della biblioteca della serie inventariale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.cd_polo IS 'codice identificativo del polo della serie inventariale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.descr IS 'descrizione della serie inventariale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.prg_inv_corrente IS 'progressivo per l''assegnazione automatica del n. inventario corrente';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.prg_inv_pregresso IS 'progressivo per l''assegnazione automatica del n. inventario pregresso';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.num_man IS 'numero di inventario di soglia per l''assegnazione manuale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.inizio_man IS 'numero di inizio intervallo per l''attribuzione manuale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.fine_man IS 'numero di fine intervallo per l''attribuzione manuale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.flg_chiusa IS 'indicatore di serie chiusa 1= serie non utilizzabile per assegnazione automatica';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.buono_carico IS 'progressivo del buono di carico';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.dt_ingr_inv_man IS 'Data convenzionale da assumere come data di ingresso di default per l''inserimento di inventari manuali (n.ro  num_man)';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.dt_ingr_inv_preg IS 'Data convenzionale da assumere come data di ingresso di default per l''inserimento di inventari pregressi';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.dt_ingr_inv_ris1 IS 'Data convenzionale di ingresso, default per l''inserimento di inventari nel primo intervallo riservato (inizo_man  n.ro  fine_man)';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.inizio_man2 IS 'numero di inizio secondo intervallo riservato per l''attribuzione manuale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.fine_man2 IS 'numero di fine secondo intervallo riservato per l''attribuzione manuale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.dt_ingr_inv_ris2 IS 'Data convenzionale di ingresso, default per l''inserimento di inventari nel secondo intervallo riservato (inizo_man2  n.ro  fine_man2)';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.inizio_man3 IS 'numero di inizio terzo intervallo riservato per l''attribuzione manuale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.fine_man3 IS 'numero di fine terzo intervallo riservato per l''attribuzione manuale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.dt_ingr_inv_ris3 IS 'Data convenzionale di ingresso, default per l''inserimento di inventari nel terzo intervallo riservato (inizo_man3  n.ro  fine_man3)';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.inizio_man4 IS 'numero di inizio quarto intervallo riservato per l''attribuzione manuale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.fine_man4 IS 'numero di fine quarto intervallo riservato per l''attribuzione manuale';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.dt_ingr_inv_ris4 IS 'Data convenzionale di ingresso, default per l''inserimento di inventari nel quarto intervallo riservato (inizo_man4  n.ro  fine_man4)';
COMMENT ON COLUMN sbnweb.tbc_serie_inventariale.fl_default IS 'Indicatore di serie da proporre come default in creazione nuovo inventario';
COMMENT ON TABLE sbnweb.tbc_sezione_collocazione IS 'SEZIONI DI COLLOCAZIONE';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.cd_sez IS 'nome della sezione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.cd_biblioteca IS 'codice identificativo della biblioteca della sezione di collocazione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.cd_polo IS 'codice identificativo del polo della sezione di collocazione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.note_sez IS 'note relative alla sezione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.tot_inv IS 'totale degli inventari collocati nella sezione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.descr IS 'denominazione della sezione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.cd_colloc IS 'codice tipo collocazione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.tipo_sez IS 'codice identificativo del tipo di sezione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.cd_cla IS 'codice del sistema di classificazione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.tot_inv_max IS 'numero di inventari previsti per la sezione di collocazione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbc_sezione_collocazione.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbf_anagrafe_utenti_professionali IS 'Anagrafe degli utenti professionali (bibliotecari)';
COMMENT ON TABLE sbnweb.tbf_attivita IS 'CODICI DELLE FUNZIONI GESTITE DALL''APPLICATIVO (TVFDTB CON COD_TAB=''69 '')';
COMMENT ON COLUMN sbnweb.tbf_attivita.cd_attivita IS 'Codice identificativo della funzione';
COMMENT ON COLUMN sbnweb.tbf_attivita.flg_menu IS 'Indicatore di funzione da attivare da menu''';
COMMENT ON COLUMN sbnweb.tbf_attivita.prg_ordimanento IS 'Ordinamento nel menu relativo di parent';
COMMENT ON COLUMN sbnweb.tbf_attivita.cd_funzione_parent IS 'Codice della funzione parent, significativo solo se flag_menu=''S''';
COMMENT ON COLUMN sbnweb.tbf_attivita.fl_auto_abilita_figli IS 'Se ''S''le funzioni di livello inferiore vengono abilitate all''atto dell''abilitazione della funzione in oggetto';
COMMENT ON COLUMN sbnweb.tbf_attivita.fl_assegna_a_cds IS 'Se ''N''la funzione non e'' demandabile a cds, se ''E'' e'' demandabile in modo esclusivo, se ''C'' e'' demandabile in modo condiviso';
COMMENT ON COLUMN sbnweb.tbf_attivita.url_servizio IS 'URL dell''action che svolge il servizio corrispondente alla richiesta';
COMMENT ON COLUMN sbnweb.tbf_attivita.fl_titolo IS 'Indicatore di funzione valida per titolo: se vale ''S'' la funzione deve essere presentata nel vai a di un titolo';
COMMENT ON COLUMN sbnweb.tbf_attivita.fl_autore IS 'Indicatore di funzione valida per titolo: se vale ''S'' la funzione deve essere presentata nel vai a di un autore';
COMMENT ON COLUMN sbnweb.tbf_attivita.fl_marca IS 'Indicatore di funzione valida per titolo: se vale ''S'' la funzione deve essere presentata nel vai a di una marca';
COMMENT ON COLUMN sbnweb.tbf_attivita.fl_luogo IS 'Indicatore di funzione valida per titolo: se vale ''S'' la funzione deve essere presentata nel vai a di un luogo';
COMMENT ON COLUMN sbnweb.tbf_attivita.fl_soggetto IS 'Indicatore di funzione valida per titolo: se vale ''S'' la funzione deve essere presentata nel vai a di un soggetto';
COMMENT ON COLUMN sbnweb.tbf_attivita.fl_classe IS 'Indicatore di funzione valida per titolo: se vale ''S'' la funzione deve essere presentata nel vai a di un classe';
COMMENT ON COLUMN sbnweb.tbf_attivita.liv_autorita_da IS 'Indica il livello di autorita'' minimo che deve avere l''oggetto corrente perche'' la funzione venga presentata nel menu ''Vai a''';
COMMENT ON COLUMN sbnweb.tbf_attivita.liv_autorita_a IS 'Indica il livello di autorita'' massimo che deve avere l''oggetto corrente perche'' la funzione venga presentata nel menu ''Vai a''';
COMMENT ON COLUMN sbnweb.tbf_attivita.gestione_in_indice IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' solo per oggetti gestiti in Indice, se ''N'' solo per oggetti non gestiti in Indice';
COMMENT ON COLUMN sbnweb.tbf_attivita.gestione_in_polo IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' solo per oggetti gestiti in polo, se ''N'' solo per oggetti non gestiti in polo';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_a IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura A, se ''N'' non deve essere presentata per titoli di natura A';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_b IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura B, se ''N'' non deve essere presentata per titoli di natura B';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_c IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura C, se ''N'' non deve essere presentata per titoli di natura C';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_d IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura D, se ''N'' non deve essere presentata per titoli di natura D';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_m IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura M, se ''N'' non deve essere presentata per titoli di natura M';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_n IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura N, se ''N'' non deve essere presentata per titoli di natura N';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_p IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura P, se ''N'' non deve essere presentata per titoli di natura P';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_s IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura S, se ''N'' non deve essere presentata per titoli di natura S';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_t IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura T, se ''N'' non deve essere presentata per titoli di natura T';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_w IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura W, se ''N'' non deve essere presentata per titoli di natura W';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_r IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura R, se ''N'' non deve essere presentata per titoli di natura R';
COMMENT ON COLUMN sbnweb.tbf_attivita.natura_x IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per titoli di natura X, se ''N'' non deve essere presentata per titoli di natura X';
COMMENT ON COLUMN sbnweb.tbf_attivita.forma_accettata IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per autori o luoghi in forma accettata, se ''N'' non deve essere presentata per autori o luoghi in forma accettata';
COMMENT ON COLUMN sbnweb.tbf_attivita.forma_rinvio IS 'Se ''S'' la funzione deve essere presentata nel menu ''Vai a'' per autori o luoghi in forma di rinvio, se ''N'' non deve essere presentata per autori o luoghi in forma di rinvio7';
COMMENT ON COLUMN sbnweb.tbf_attivita.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbf_attivita.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbf_attivita.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbf_attivita.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbf_attivita.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tbf_attivita.classe_java_sbnmarc IS 'Nome della classe java per l''attivita'' in SBNMARC';
COMMENT ON TABLE sbnweb.tbf_batch_servizi IS 'Tabella per gestire le procedure differite (batch)';
COMMENT ON COLUMN sbnweb.tbf_batch_servizi.id_batch_servizi IS 'Identificativo del servizio batch';
COMMENT ON COLUMN sbnweb.tbf_batch_servizi.id_coda_input IS 'Identificativo della categoria di esecuzione associata a questo servizio batch';
COMMENT ON COLUMN sbnweb.tbf_batch_servizi.nome_coda_output IS 'Nome della coda JMS di output per il servizio batch';
COMMENT ON COLUMN sbnweb.tbf_batch_servizi.visibilita IS 'Visibilita'' degli output del servizio batch (P=Polo, B=solo la Biblioteca richiedente)';
COMMENT ON COLUMN sbnweb.tbf_batch_servizi.class_name IS 'Nome canonico della classe Java che realizza il servizio batch';
COMMENT ON COLUMN sbnweb.tbf_batch_servizi.cd_attivita IS 'Codice dell''attivita'' assegnata al servizio batch';
COMMENT ON COLUMN sbnweb.tbf_batch_servizi.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbf_biblioteca IS 'ANAGRAFICA BIBLIOTECHE (TPRBIB)';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.id_biblioteca IS 'identificativo univoco della biblioteca';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.cd_ana_biblioteca IS 'codice della biblioteca nell''anagrafe centrale';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.cd_polo IS 'codice identificativo del polo di localizzazione';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.nom_biblioteca IS 'denominazione della biblioteca';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.unit_org IS 'denominazione dell''unita'' organizzativa della biblioteca';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.indirizzo IS 'indirizzo';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.cpostale IS 'casella postale';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.cap IS 'codice d''avviamento postale';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.telefono IS 'numero telefonico';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.fax IS 'numero del fax';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.note IS 'note relative alla biblioteca';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.p_iva IS 'partita iva';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.cd_fiscale IS 'codice fiscale';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.e_mail IS 'indirizzo elettronico';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.tipo_biblioteca IS 'codice identificativo della tipologia di biblioteca';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.paese IS 'codice identificativo del paese';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.provincia IS 'codice identificativo della provincia';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.cd_bib_cs IS 'codice identificativo della biblioteca centro sistema di riferimento (solo per biblioteche sbn)';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.cd_bib_ut IS 'prima parte del codice utente assegnato alla biblioteca in quanto utente';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.cd_utente IS 'seconda parte del codice utente assegnato alla biblioteca in quanto utente';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.flag_bib IS 'indica se la biblioteca e'' sbn - centro sistema (c), sbn - affiliata (a), non sbn (n)';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.localita IS 'localita''';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.chiave_bib IS 'chiave nome biblioteca';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.chiave_ente IS 'chiave ente di appartenenza';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbf_biblioteca.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbf_biblioteca_default IS ' ';
COMMENT ON TABLE sbnweb.tbf_biblioteca_in_polo IS 'BIBLIOTECHE ABILITATE AD OPERARE NEL POLO (TPFSBI)';
COMMENT ON COLUMN sbnweb.tbf_biblioteca_in_polo.cd_biblioteca IS 'Codice identificativo della biblioteca nell''ambito del polo';
COMMENT ON COLUMN sbnweb.tbf_biblioteca_in_polo.cd_polo IS 'Codice identificativo del polo, valorizzato con lo stesso codice per tutte le occorrenze delle tabelle';
COMMENT ON COLUMN sbnweb.tbf_biblioteca_in_polo.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbf_biblioteca_in_polo.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbf_biblioteca_in_polo.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbf_biblioteca_in_polo.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbf_biblioteca_in_polo.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tbf_biblioteca_in_polo.cd_sistema_metropolitano IS 'Codice identificativo dell''eventuale sistema metropolitano cui partecipa la biblioteca';
COMMENT ON TABLE sbnweb.tbf_bibliotecario IS 'DATI DI ABILITAZIONE DEL BIBLIOTECARI)';
COMMENT ON COLUMN sbnweb.tbf_bibliotecario.cd_livello_gbantico IS 'massimo livello di autorita'' utilizzabile dal bibliotecaro in catalogazione materiale antico';
COMMENT ON COLUMN sbnweb.tbf_bibliotecario.cd_livello_gbmoderno IS 'massimo livello di autorita'' utilizzabile dal bibliotecaro in catalogazione materiale moderno';
COMMENT ON COLUMN sbnweb.tbf_bibliotecario.cd_livello_gssoggetti IS 'massimo livello di autorita'' utilizzabile dal bibliotecaro in catalogazione soggetti';
COMMENT ON COLUMN sbnweb.tbf_bibliotecario.cd_livello_gsclassi IS 'massimo livello di autorita'' utilizzabile dal bibliotecaro in gestione indici di classificazione';
COMMENT ON COLUMN sbnweb.tbf_bibliotecario.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbf_bibliotecario.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbf_bibliotecario.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbf_bibliotecario.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbf_bibliotecario.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbf_bibliotecario_default IS ' ';
COMMENT ON TABLE sbnweb.tbf_coda_jms IS ' Categorie di esecuzione previste per i servizi batch';
COMMENT ON COLUMN sbnweb.tbf_coda_jms.id_coda IS 'Identificativo univoco della categoria di esecuzione';
COMMENT ON COLUMN sbnweb.tbf_coda_jms.nome_jms IS 'Nome della coda JMS';
COMMENT ON COLUMN sbnweb.tbf_coda_jms.sincrona IS 'Coda di tipo sincrona (S) o asincrona (N)';
COMMENT ON COLUMN sbnweb.tbf_coda_jms.id_descrizione IS 'Descrizione della categoria';
COMMENT ON COLUMN sbnweb.tbf_coda_jms.id_descr_orario_attivazione IS 'NON UTILIZZATO';
COMMENT ON COLUMN sbnweb.tbf_coda_jms.id_orario_di_attivazione IS 'NON UTILIZZATO';
COMMENT ON COLUMN sbnweb.tbf_coda_jms.cron_expression IS 'Espressione in sintassi cron che guida l''attivazione dei processi batch associati a questa categoria';
COMMENT ON TABLE sbnweb.tbf_config_default IS 'Configurazione dei  default gestiti dal sistema';
COMMENT ON COLUMN sbnweb.tbf_config_default.id_area_sezione IS 'eg: 00_GestBib (00_ per definire un''area) e 01_GestBibInterrrogazione (NN_ con nn diveso dad 00 per indicare una sezione. Una sezione ha un parent con codice area)';
COMMENT ON COLUMN sbnweb.tbf_config_default.parent IS 'Id del codice are a cui la sezione appartiene. Eg  la sezioen 01_GestBibInterrogazione appartiene a 00_GestBib (id_area_sezione)';
COMMENT ON COLUMN sbnweb.tbf_config_default.seq_ordinamento IS 'Ordine di prospettazione';
COMMENT ON COLUMN sbnweb.tbf_config_default.codice_attivita IS 'Codice di attivita'' di appartenenenza. Usato da solo o con il parametro per verificare abilitazione per uso del default';
COMMENT ON COLUMN sbnweb.tbf_config_default.parametro_attivita IS 'parametro da usarsi in congiunzione con in codice attivita'' per verificare se abilitati o meno ad usare il default';
COMMENT ON COLUMN sbnweb.tbf_config_default.codice_modulo IS 'Identificativo del modulo funzionale (eg. acquisizioni, servizi, ecc.) per abilitare o meno il default';
COMMENT ON TABLE sbnweb.tbf_config_statistiche IS 'Contiene le query utilizzate dalle funzioni statistiche';
COMMENT ON COLUMN sbnweb.tbf_config_statistiche.nome_statistica IS 'titolo della statistica';
COMMENT ON COLUMN sbnweb.tbf_config_statistiche.tipo_query IS '0: statica, 1: dinamica';
COMMENT ON COLUMN sbnweb.tbf_config_statistiche.query IS 'testo della query';
COMMENT ON COLUMN sbnweb.tbf_config_statistiche.colonne_output IS 'nomi colonne per layout';
COMMENT ON COLUMN sbnweb.tbf_config_statistiche.fl_polo_biblio IS 'indica se la query e'' a livello di polo o di biblioteca, vale P, polo, o B, biblioteca';
COMMENT ON COLUMN sbnweb.tbf_config_statistiche.fl_txt IS 'S = in formato testo N = non in formato testo';
COMMENT ON TABLE sbnweb.tbf_contatore IS 'CONTATORI APPLICATIVI (TLFCNT + TPFCNT)';
COMMENT ON COLUMN sbnweb.tbf_contatore.cd_cont IS 'Codice identificativo di contatore';
COMMENT ON COLUMN sbnweb.tbf_contatore.anno IS 'Anno di riferimento';
COMMENT ON COLUMN sbnweb.tbf_contatore.key1 IS 'Ulteriore codice identificativo';
COMMENT ON COLUMN sbnweb.tbf_contatore.ultimo_prg IS 'Ultimo progressivo utilizzato';
COMMENT ON COLUMN sbnweb.tbf_contatore.lim_max IS 'Valore massimo ammesso per il contatore';
COMMENT ON COLUMN sbnweb.tbf_contatore.attivo IS 'Indicatore di utilizzo da parte di altro utente (1=disponibile, 2=in aggiornamento)';
COMMENT ON COLUMN sbnweb.tbf_contatore.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbf_contatore.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbf_contatore.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbf_contatore.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbf_contatore.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbf_default IS ' ';
COMMENT ON COLUMN sbnweb.tbf_default.id_etichetta IS 'Potrebbe non esssere utilizzata se al posto usiamo il campo key per prendere l''etichetta in lingua a un file di properties';
COMMENT ON COLUMN sbnweb.tbf_default.codice_tabella_validazione IS 'Per prendere le liste (eg. lingua) dalle tabelle di validazione';
COMMENT ON COLUMN sbnweb.tbf_default.seq_ordinamento IS 'Identifica il posizionamento nella maschera di prospettazione';
COMMENT ON COLUMN sbnweb.tbf_default.bundle IS 'Indicazione del bundle per le properties';
COMMENT ON TABLE sbnweb.tbf_dizionario_errori IS 'ELENCO DEI CODICI DI ERRORE E DELLA RELATIVA DESCRIZIONE NELLE DIVERSE LINGUE (--)';
COMMENT ON COLUMN sbnweb.tbf_dizionario_errori.cd_errore IS 'Codice dell''errore';
COMMENT ON COLUMN sbnweb.tbf_dizionario_errori.ds_errore IS 'Descrizione, nella lingua indicata dal relativo codice, dell''errore codificato';
COMMENT ON COLUMN sbnweb.tbf_dizionario_errori.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbf_dizionario_errori.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbf_dizionario_errori.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbf_dizionario_errori.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbf_dizionario_errori.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbf_gruppi_default IS ' ';
COMMENT ON COLUMN sbnweb.tbf_gruppi_default.etichetta IS 'Etichetta da prospettare';
COMMENT ON TABLE sbnweb.tbf_gruppo_attivita IS ' ';
COMMENT ON TABLE sbnweb.tbf_intranet_range IS ' ';
COMMENT ON COLUMN sbnweb.tbf_intranet_range.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbf_intranet_range.cd_bib IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tbf_intranet_range.address IS 'Indirizzo di rete in formato IPv4 o IPv6';
COMMENT ON COLUMN sbnweb.tbf_intranet_range.bitmask IS 'Maschera in bit relativo al campo address (in codifica CIDR)';
COMMENT ON TABLE sbnweb.tbf_lingua_supportata IS 'LINGUA PER LA QUALE E'' PREVITA LA TRADUZIONE DELLE LABELS, DEI CODICI E DELLA DIAGNOSTICA SULL''INTERFACCIA (--)';
COMMENT ON COLUMN sbnweb.tbf_lingua_supportata.cd_lingua IS 'Codice della lingua';
COMMENT ON COLUMN sbnweb.tbf_lingua_supportata.descr IS 'Descrizione della lingua ';
COMMENT ON COLUMN sbnweb.tbf_lingua_supportata.fl_default IS 'Indicatore di lingua utilizzata di default (il valore S=default e'' ammesso su una sola occorrenza della tabella)';
COMMENT ON COLUMN sbnweb.tbf_lingua_supportata.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbf_lingua_supportata.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbf_lingua_supportata.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbf_lingua_supportata.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbf_lingua_supportata.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbf_mail IS 'Tabella per l''invio e ricezione di mail da parte del polo';
COMMENT ON COLUMN sbnweb.tbf_mail.smtp IS 'Server di invio mail';
COMMENT ON COLUMN sbnweb.tbf_mail.pop3 IS 'Server di rixcezione mail';
COMMENT ON COLUMN sbnweb.tbf_mail.user_name IS 'Nome utente registrato con il server di posta';
COMMENT ON COLUMN sbnweb.tbf_mail.password IS 'password utente registrato con server di posta';
COMMENT ON COLUMN sbnweb.tbf_mail.fl_forzatura IS 'Indica se l''indirizzo di polo deve essere forzato come mittente di tutte le mail inviate';
COMMENT ON TABLE sbnweb.tbf_modelli_stampe IS 'Persistenza dei modelli di stampa dinamica e statica';
COMMENT ON COLUMN sbnweb.tbf_modelli_stampe.id_modello IS 'Identificativo del modello di stampa';
COMMENT ON COLUMN sbnweb.tbf_modelli_stampe.cd_bib IS 'Codice della Biblioteca (solo nel caso di modelli per etichette)';
COMMENT ON COLUMN sbnweb.tbf_modelli_stampe.modello IS 'Nome del modello (univoco per bibolioteca)';
COMMENT ON COLUMN sbnweb.tbf_modelli_stampe.tipo_modello IS 'C = comma separated values J = Jrxml';
COMMENT ON COLUMN sbnweb.tbf_modelli_stampe.campi_valori_del_form IS 'Decodifica lineare del modello (solo nel caso di tipo_modello=C)';
COMMENT ON COLUMN sbnweb.tbf_modelli_stampe.descr IS 'Descrizione ulteriore per il modello';
COMMENT ON COLUMN sbnweb.tbf_modelli_stampe.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbf_modelli_stampe.cd_attivita IS 'codice della funzione che identifica la stampa cui si riferisce il modello';
COMMENT ON COLUMN sbnweb.tbf_modelli_stampe.subreport IS 'subreports eventualmente previsti dal modello';
COMMENT ON COLUMN sbnweb.tbf_modelli_stampe.descr_bib IS 'Intestazione della biblioteca (solo nel caso di modelli etichette)';
COMMENT ON TABLE sbnweb.tbf_modello_profilazione_biblioteca IS ' ';
COMMENT ON TABLE sbnweb.tbf_modello_profilazione_bibliotecario IS ' ';
COMMENT ON TABLE sbnweb.tbf_moduli_funzionali IS ' ';
COMMENT ON COLUMN sbnweb.tbf_moduli_funzionali.id_modulo IS 'Codice modulo funzionale distribuibile';
COMMENT ON TABLE sbnweb.tbf_par_auth IS ' ';
COMMENT ON COLUMN sbnweb.tbf_par_auth.tp_abil_auth IS 'S abilita gestione dell''autority';
COMMENT ON COLUMN sbnweb.tbf_par_auth.fl_abil_legame IS 'S consente di legare l''authority al documento';
COMMENT ON COLUMN sbnweb.tbf_par_auth.fl_leg_auth IS 'S visualizza legami ad auth nel reticolo del documento';
COMMENT ON TABLE sbnweb.tbf_par_mat IS ' ';
COMMENT ON TABLE sbnweb.tbf_par_sem IS ' ';
COMMENT ON TABLE sbnweb.tbf_parametro IS ' ';
COMMENT ON TABLE sbnweb.tbf_polo IS ' ';
COMMENT ON COLUMN sbnweb.tbf_polo.denominazione IS 'Denominazione del polo';
COMMENT ON TABLE sbnweb.tbf_polo_default IS ' ';
COMMENT ON TABLE sbnweb.tbf_procedure_output IS ' ';
COMMENT ON TABLE sbnweb.tbf_profilo_abilitazione IS 'DESCRIZIONE DEI PROFILI D''ABILITAZIONE DEI BIBLIOTECARI (TLFPRF)';
COMMENT ON COLUMN sbnweb.tbf_profilo_abilitazione.cd_prof IS 'Codice identificativo del profilo';
COMMENT ON COLUMN sbnweb.tbf_profilo_abilitazione.dsc_profilo IS 'Descrizione del profilo ';
COMMENT ON COLUMN sbnweb.tbf_profilo_abilitazione.nota_profilo IS 'Nota al profilo';
COMMENT ON COLUMN sbnweb.tbf_profilo_abilitazione.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbf_profilo_abilitazione.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbf_profilo_abilitazione.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbf_profilo_abilitazione.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbf_profilo_abilitazione.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbf_utenti_professionali_web IS ' ';
COMMENT ON TABLE sbnweb.tbl_calendario_servizi IS 'Calendario erogazione servizi';
COMMENT ON COLUMN sbnweb.tbl_calendario_servizi.progr_fascia IS 'progressivo che individua una fascia';
COMMENT ON COLUMN sbnweb.tbl_calendario_servizi.ore_in IS 'ora di inizio della fascia oraria di validita'' ';
COMMENT ON COLUMN sbnweb.tbl_calendario_servizi.ore_fi IS 'ora di fine della fascia oraria di validita''';
COMMENT ON COLUMN sbnweb.tbl_calendario_servizi.data IS 'data del giorno';
COMMENT ON COLUMN sbnweb.tbl_calendario_servizi.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbl_calendario_servizi.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tbl_controllo_attivita IS ' ';
COMMENT ON TABLE sbnweb.tbl_controllo_iter IS 'Fasi di controllo';
COMMENT ON COLUMN sbnweb.tbl_controllo_iter.id_controllo_iter IS 'Identificativo univoco del controllo iter';
COMMENT ON COLUMN sbnweb.tbl_controllo_iter.id_iter_servizio IS 'Identificativo del passo dell''iter cui si riferisce questo controllo';
COMMENT ON COLUMN sbnweb.tbl_controllo_iter.progr_fase IS 'progressivo che individua  la sequenza di controllo';
COMMENT ON COLUMN sbnweb.tbl_controllo_iter.fl_bloccante IS 'indica se l''esito positivo del controllo, blocca la fase successiva dell''iter di controlli';
COMMENT ON COLUMN sbnweb.tbl_controllo_iter.messaggio IS 'messaggio presentato all''utente nel caso il controllo abbia dato positivo';
COMMENT ON COLUMN sbnweb.tbl_controllo_iter.cod_controllo IS 'codice che identifica il controllo';
COMMENT ON COLUMN sbnweb.tbl_controllo_iter.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbl_controllo_iter.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbl_controllo_iter.ute_var IS 'Utente che ha effettuato l''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tbl_controllo_iter.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tbl_controllo_iter.fl_canc IS 'flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbl_disponibilita_precatalogati IS 'Disponibilita'' precatalogati';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.id_disponibilita_precatalogati IS 'Identificativo univoco del range di segnature';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.cd_bib IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.cod_segn IS 'codice che identifica univocamente i range di segnatura';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.segn_inizio IS 'segnatura iniziale';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.segn_fine IS 'segnatura finale';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.cod_no_disp IS 'codice di non disponibilita'' del documento';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.cod_frui IS 'codice della fruizione';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.segn_da IS 'estremo iniziale dell''intervallo (normalizzato)';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.segn_a IS 'estremo finale dell''intervallo (normalizzato)';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbl_disponibilita_precatalogati.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbl_documenti_lettori IS 'Documenti inseriti dai lettori (non catalogati in SBN)';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.id_documenti_lettore IS 'Identificativo univoco del documento lettore';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.cd_bib IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.tipo_doc_lett IS 'codice identificativo della tipologia del documento lettore';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.cod_doc_lett IS 'codice identificativo del documento lettore';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.id_utenti IS 'Identificativo dell''utente lettore che ha inserito il documento da remoto';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.titolo IS 'titolo proprio';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.luogo_edizione IS 'luogo di edizione';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.editore IS 'editore';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.anno_edizione IS 'anno di edizione';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.autore IS 'primo autore';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.num_volume IS 'numero del volume della monografia';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.annata IS 'anno descrittivo dell''abbonamento';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.tipo_data IS 'codice del tipo di data';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.prima_data IS 'prima data';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.seconda_data IS 'seconda data';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.stato_catal IS 'codice dello stato di catalogazione';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.natura IS 'codice identificativo della natura bibliografica';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.paese IS 'codice identificativo del paese';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.lingua IS 'codice identificativo della lingua';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.fonte IS 'codice identificativo della tipologia di riproduzione di un documento';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.stato_sugg IS 'stato del suggerimento (w = attesa di risposta, a = accettato, r = rifiutato, o = ordinato)';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.bid IS 'codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.data_sugg_lett IS 'data di registrazione del suggerimento';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.note IS 'note relative al suggerimento dato dal lettore';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.msg_lettore IS 'messaggio per il lettore relativo al suggerimento';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.segnatura IS 'numero di segnatura (campo obbligatorio per tipo_doc_lett=p)';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.cod_bib_inv IS 'codice identificativo della biblioteca che possiede il documento richiesto';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.cod_serie IS 'codice identificativo della serie inventariale del documento richiesto';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.cod_inven IS 'numero d''inventario del documento richiesto';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.ord_segnatura IS 'Normalizzazione del numero di segnatura';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.cd_no_disp IS 'codice del motivo di non disponibilita'' del documento';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.cd_catfrui IS 'codice della categoria di fruizione';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.denom_biblioteca_doc IS 'Denominazione della biblioteca che possiede il documento';
COMMENT ON COLUMN sbnweb.tbl_documenti_lettori.tidx_vector IS 'Indice delle parole contenute nel titolo del documento';
COMMENT ON TABLE sbnweb.tbl_esemplare_documento_lettore IS 'Esemplari relativi ai documenti inseriti dai lettori (non catalogati in SBN)';
COMMENT ON COLUMN sbnweb.tbl_esemplare_documento_lettore.id_esemplare IS 'Identificativo univoco dell''esemplare';
COMMENT ON COLUMN sbnweb.tbl_esemplare_documento_lettore.id_documenti_lettore IS 'Identificativo del documento cui si riferisce l''esemplare';
COMMENT ON COLUMN sbnweb.tbl_esemplare_documento_lettore.prg_esemplare IS 'Progressivo dell''esemplare (attualmente equivalente al campo ''id_esemplare'')';
COMMENT ON COLUMN sbnweb.tbl_esemplare_documento_lettore.fonte IS 'Provenienza dell''esemplare (tabella codici ''LFON'')';
COMMENT ON COLUMN sbnweb.tbl_esemplare_documento_lettore.inventario IS 'Descrizione della copia';
COMMENT ON COLUMN sbnweb.tbl_esemplare_documento_lettore.precisazione IS 'Precisazione inventario';
COMMENT ON COLUMN sbnweb.tbl_esemplare_documento_lettore.cod_no_disp IS 'Codice di eventuale non disponibilita'' (tabella codici ''CCND'')';
COMMENT ON TABLE sbnweb.tbl_iter_servizio IS 'Passi dell''iter del servizio';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.id_iter_servizio IS 'Identificativo univoco del passo dell''iter';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.progr_iter IS 'progressivo della sequenza dell''attivita''';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.id_tipo_servizio IS 'Identificativo del tipo servizio cui si riferisce l''iter';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.cod_attivita IS 'codice identificativo delle attivita'' possibili nell''iter di un servizio';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.flag_stampa IS 'indicatore di effettuazione di stampa assume i valori s, n';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.num_pag IS 'numero delle copie da stampare';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.testo IS 'testo a disposizione della biblioteca nella stampa';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.flg_abil IS 'flag indicante che l''attivita'' prevista nel passo puo'' essere svolta da tutti i bibliotecari o da alcuni in particolare. assume i valori t=tutti, n=solo quelli incaricati ';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.flg_rinn IS 'indicatore di possibilita'', per questa attivita'', di rinnovare il servizio cioe'' posticipare la data fine prevista del servizio. assume i valori s, n';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.stato_iter IS 'codice identificativo dello stato dell''iter del servizio';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.obbl IS 'indicatore dell''obbligatorieta'' della attivita''';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.cod_stato_rich IS 'codice dello stato della richiesta';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.stato_mov IS 'codice dello stato del movimento';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.cod_stat_cir IS 'codice dello stato di circolazione del materiale oggetto del servizio interbibliotecario';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.cod_stato_ric_ill IS 'codice dello stato della richiesta di servizio interbibliotecario';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbl_iter_servizio.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tbl_materie IS 'Materie d''interesse';
COMMENT ON COLUMN sbnweb.tbl_materie.id_materia IS 'Identificativo univoco della materia';
COMMENT ON COLUMN sbnweb.tbl_materie.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_materie.cd_biblioteca IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tbl_materie.cod_mat IS 'codice della materia di interesse per l''utente di una biblioteca';
COMMENT ON COLUMN sbnweb.tbl_materie.descr IS 'descrizione della materia di interesse';
COMMENT ON TABLE sbnweb.tbl_modalita_erogazione IS ' Modalita'' di erogazione definite in biblioteca';
COMMENT ON COLUMN sbnweb.tbl_modalita_erogazione.id_modalita_erogazione IS 'Identificativo univoco della modalita'' di erogazione';
COMMENT ON COLUMN sbnweb.tbl_modalita_erogazione.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_modalita_erogazione.cd_bib IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tbl_modalita_erogazione.cod_erog IS 'Codice della modalita'' di erogazione (tabella codici ''LMER'')';
COMMENT ON COLUMN sbnweb.tbl_modalita_erogazione.tar_base IS 'Tariffa base associata alla modalita''';
COMMENT ON COLUMN sbnweb.tbl_modalita_erogazione.costo_unitario IS 'Costo associato al singolo pezzo erogato con questa modalita''';
COMMENT ON TABLE sbnweb.tbl_modalita_pagamento IS 'Modalita'' di pagamento';
COMMENT ON COLUMN sbnweb.tbl_modalita_pagamento.id_modalita_pagamento IS 'Identificativo univoco della modalita'' di pagamento';
COMMENT ON COLUMN sbnweb.tbl_modalita_pagamento.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_modalita_pagamento.cd_bib IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tbl_modalita_pagamento.cod_mod_pag IS 'codice della modalita'' di pagamentio';
COMMENT ON COLUMN sbnweb.tbl_modalita_pagamento.descr IS 'descrizione della modalita'' di pagamento';
COMMENT ON COLUMN sbnweb.tbl_modalita_pagamento.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbl_modalita_pagamento.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tbl_occupazioni IS 'Occupazioni';
COMMENT ON COLUMN sbnweb.tbl_occupazioni.id_occupazioni IS 'Identificativo univoco dell''occupazione';
COMMENT ON COLUMN sbnweb.tbl_occupazioni.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_occupazioni.cd_biblioteca IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tbl_occupazioni.professione IS 'codice identificativo della professione';
COMMENT ON COLUMN sbnweb.tbl_occupazioni.occupazione IS 'codice dell''occupazione';
COMMENT ON COLUMN sbnweb.tbl_occupazioni.descr IS 'descrizione dell''occupazione';
COMMENT ON TABLE sbnweb.tbl_parametri_biblioteca IS ' Parametri generali dei servizi per la Biblioteca';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.id_parametri_biblioteca IS 'Identificativo univoco del parametro';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cd_bib IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.num_lettere IS 'Numero massimo di lettere di sollecito per richieste scadute';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.num_gg_ritardo1 IS 'Numero di giorni necessario all''invio della prima lettera di sollecito';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.num_gg_ritardo2 IS 'Numero di giorni necessario all''invio della seconda lettera di sollecito';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.num_gg_ritardo3 IS 'Numero di giorni necessario all''invio della terza lettera di sollecito';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.num_prenotazioni IS 'Numero massimo di prenotazioni per uno stesso documento';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.fl_catfrui_nosbndoc IS 'indicatore della modalita'' di associazione categoria di fruizione ai documenti non catalogati in SBN; N=la categoria di fruizione e'' la stessa per tutti i documenti non SBN; S=la categoria di fruizione dipende dall''intervallo in cui ricade la collocazione del documento.';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cd_catfrui_nosbndoc IS 'categoria di fruizione da assumere per documenti non catalogati in SBN nei casi in cui la categoria non sia stata gia'' associata al documento puntualmente o attraveso la collocazione';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cod_modalita_invio1_sollecito1 IS 'prima modalita'' di invio della prima lettera di sollecito (tabella codici ''LTIS'')';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cod_modalita_invio2_sollecito1 IS 'seconda modalita'' di invio della prima lettera di sollecito (tabella codici ''LTIS'')';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cod_modalita_invio3_sollecito1 IS 'terza modalita'' di invio della prima lettera di sollecito (tabella codici ''LTIS'')';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cod_modalita_invio1_sollecito2 IS 'prima modalita'' di invio della seconda lettera di sollecito (tabella codici ''LTIS'')';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cod_modalita_invio2_sollecito2 IS 'seconda modalita'' di invio della seconda lettera di sollecito (tabella codici ''LTIS'')';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cod_modalita_invio3_sollecito2 IS 'terza modalita'' di invio della seconda lettera di sollecito (tabella codici ''LTIS'')';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cod_modalita_invio1_sollecito3 IS 'prima modalita'' di invio della terza lettera di sollecito (tabella codici ''LTIS'')';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cod_modalita_invio2_sollecito3 IS 'seconda modalita'' di invio della terza lettera di sollecito (tabella codici ''LTIS'')';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cod_modalita_invio3_sollecito3 IS 'terza modalita'' di invio della terza lettera di sollecito (tabella codici ''LTIS'')';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.num_gg_validita_prelazione IS 'Numero massimo di giorni per cui un documento puo'' essere riservato';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.ammessa_autoregistrazione_utente IS 'E'' ammessa l''autoregistrazione dell''utente da remoto';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.ammesso_inserimento_utente IS 'E'' ammesso l''inserimento della richiesta da parte dell''utente';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.anche_da_remoto IS 'E'' ammesso l''inserimento della richiesta da parte dell''utente da remoto';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.cd_catriprod_nosbndoc IS 'Categoria di riproduzione associata ai documenti non catalogati (tabella codici ''LSUP'')';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.num_gg_rinnovo_richiesta IS 'numero di giorni prima della scadenza prevista in cui può essere richiesta la proroga.';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.fl_tipo_rinnovo IS 'Tipologia del rinnovo automatico: 0=mai; 1=sempre; 2=solo in assenza di prenotazioni.';  
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.xml_modello_soll IS 'Modello della lettera di sollecito';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.fl_priorita_prenot IS 'indica se la biblioteca gestisce o meno la priorità per le prenotazioni';
COMMENT ON COLUMN sbnweb.tbl_parametri_biblioteca.email_notifica IS 'indirizzo email che riceverà notifica all''inserimento di una nuova richiesta';
COMMENT ON TABLE sbnweb.tbl_penalita_servizio IS 'Penalita'' servizio';
COMMENT ON COLUMN sbnweb.tbl_penalita_servizio.id_servizio IS 'Identificativo del servizio cui si riferisce la penalita''';
COMMENT ON COLUMN sbnweb.tbl_penalita_servizio.gg_sosp IS 'numero di giorni di sospensione, tale numero e'' fisso ed e'' un valore, puo'' essere omesso nel caso in cui venga inserito il coefficente di sospensione';
COMMENT ON COLUMN sbnweb.tbl_penalita_servizio.coeff_sosp IS 'coefficiente per cui deve essere moltiplicato il numero dei giorni di ritardo. tale attributo non e'' valorizzato nel caso in cui sia valorizzato l''attributo gg sospensione ';
COMMENT ON COLUMN sbnweb.tbl_penalita_servizio.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbl_penalita_servizio.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tbl_posti_sala IS 'Posti in sala';
COMMENT ON COLUMN sbnweb.tbl_posti_sala.num_posto IS 'numero del posto della sala';
COMMENT ON COLUMN sbnweb.tbl_posti_sala.occupato IS 'indica se il posto e'' occupato o libero ';
COMMENT ON COLUMN sbnweb.tbl_posti_sala.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbl_posti_sala.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tbl_richiesta_servizio IS ' ';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_rich_serv IS 'Codice identificativo della richiesta/movimento/prenotazione/prelazione';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cd_polo_inv IS 'Codice Polo dell''inventario movimentato';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_bib_inv IS 'Codice della biblioteca che possiede il documento movimentato';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_serie_inv IS 'Codice della serie inventariale del documento movimentato';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_inven_inv IS 'Numero dell''inventario movimentato';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.id_documenti_lettore IS 'Identificativo univoco del documento non SBN movimentato';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.id_esemplare_documenti_lettore IS 'Identificativo univoco della copia del documento non SBN movimentato';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.id_utenti_biblioteca IS 'Identificativo dell''utente lettore che richiede il servizio';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.id_modalita_pagamento IS 'Identificativo della modalita'' di pagamento scelta per questo movimento';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.id_supporti_biblioteca IS 'Identificativo del supporto scelto per questo movimento; concorre al calcolo del costo servizio';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.id_servizio IS 'Identificativo del servizio richiesto sul documento movimentato';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.id_iter_servizio IS 'Identificativo del passo dell''iter in cui si trova il movimento';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.fl_tipo_rec IS 'Tipo record (tabella codici ''TRRS'')';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.note_bibliotecario IS 'Eventuali note del bibliotecario per l''utente lettore';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.costo_servizio IS 'costo effettivo del servizio richiesto (somma della tariffa della modalita'' di erogazione, del supporto e numero pezzi)';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.num_fasc IS 'Numero del fascicolo d''interesse (per richieste su periodici)';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.num_volume IS 'Numero del volume; tale dato e'' registrato dal bibliotecario nel caso in cui il fascicolo richiesto e'' rilegato insieme ad altri fascicoli';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.anno_period IS 'Annata del periodico';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_tipo_serv_rich IS 'NON UTILIZZATO';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_tipo_serv_alt IS 'NON UTILIZZATO';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_stato_rich IS 'Codice di stato della richiesta di servizio (tabella codici ''LSTS'')';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_stato_mov IS 'Codice di stato del movimento (tabella codici ''LSTM'')';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.data_in_eff IS 'Data di inizio effettiva del movimento';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.data_fine_eff IS 'Data di fine effettiva del movimento';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.num_rinnovi IS 'Numero di rinnovi accordati all''utente su questo documento (massimo 3 rinnovi)';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.bid IS 'Identificativo del documento richiesto, nel caso di materiale catalogato in SBN';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_attivita IS 'Codice dell''attivita'' in cui si trova il movimento (tabella codici ''LATT'')';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.data_richiesta IS 'Data di inserimento della richiesta (equivalente a ts_ins)';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.num_pezzi IS 'Numero di pezzi richiesti nel caso di servizio di riproduzione, concorre al calcolo del costo servizio';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.note_ut IS 'Eventuali note dell''utente lettore per il bibliotecario';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.prezzo_max IS 'Spesa massima sostenibile dall''utente lettore';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.data_massima IS 'Data limite d''interesse da parte dell''utente lettore';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.data_proroga IS 'Data proroga del movimento, richiesta da parte dell''utente lettore o accordata dal bibliotecario';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.data_in_prev IS 'Data di inizio prevista del movimento, dipendente dalla disponibilita'' del documento e dalle impostazioni della biblioteca per quanto riguarda prelazioni e prenotazioni';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.data_fine_prev IS 'Data fine prevista del movimento (calcolata come data inizio prevista + n. giorni di durata del servizio richiesto)';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.fl_svolg IS 'Area di svolgimento del servizio (L=Locale, I=Inter-bibliotecario)';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.copyright IS 'NON UTILIZZATO';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_erog IS 'Codice erogazione per il movimento (tabella codici ''LMER''); concorre al calcolo del costo servizio';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_risp IS 'NON UTILIZZATO';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.fl_condiz IS 'NON UTILIZZATO';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.fl_inoltro IS 'NON UTILIZZATO';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_bib_dest IS 'Biblioteca del sistema metropolitano che possiede il documento';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_bib_prelievo IS 'Biblioteca del sistema metropolitano indicata come consegnataria del documento';
COMMENT ON COLUMN sbnweb.tbl_richiesta_servizio.cod_bib_restituzione IS 'Biblioteca del sistema metropolitano indicata come destinataria della riconsegna';
COMMENT ON TABLE sbnweb.tbl_sale IS 'Sale';
COMMENT ON COLUMN sbnweb.tbl_sale.id_sale IS 'Identificativo univoco della sala';
COMMENT ON COLUMN sbnweb.tbl_sale.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_sale.cd_bib IS 'Codice della biblioteca';
COMMENT ON COLUMN sbnweb.tbl_sale.cod_sala IS 'codice della sala';
COMMENT ON COLUMN sbnweb.tbl_sale.descr IS 'descrizione della sala';
COMMENT ON COLUMN sbnweb.tbl_sale.num_max_posti IS 'numero massimo di posti disponibili';
COMMENT ON COLUMN sbnweb.tbl_sale.num_prg_posti IS 'numero dei posti occupati in sala';
COMMENT ON COLUMN sbnweb.tbl_sale.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbl_sale.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tbl_servizio IS 'SERVIZI EROGATI DALLA BIBLIOTECA';
COMMENT ON COLUMN sbnweb.tbl_servizio.id_servizio IS 'Identificativo univoco del servizio';
COMMENT ON COLUMN sbnweb.tbl_servizio.id_tipo_servizio IS 'Identificativo del Tipo Servizio cui si riferisce questo servizio';
COMMENT ON COLUMN sbnweb.tbl_servizio.cod_servizio IS 'codice del servizio ad es. : p3= prestito max 3 libri';
COMMENT ON COLUMN sbnweb.tbl_servizio.descr IS 'descrizione del servizio';
COMMENT ON COLUMN sbnweb.tbl_servizio.num_max_rich IS 'numero massimo di richieste possibili';
COMMENT ON COLUMN sbnweb.tbl_servizio.num_max_mov IS 'numero massimo di movimenti attivi previsti contemporaneamente';
COMMENT ON COLUMN sbnweb.tbl_servizio.dur_mov IS 'durata massima del servizio espressa in giorni';
COMMENT ON COLUMN sbnweb.tbl_servizio.dur_max_rinn1 IS 'durata massima del servizio espressa in giorni in caso di primo rinnovo';
COMMENT ON COLUMN sbnweb.tbl_servizio.dur_max_rinn2 IS 'durata massima del servizio espressa in giorni in caso di secondo rinnovo';
COMMENT ON COLUMN sbnweb.tbl_servizio.dur_max_rinn3 IS 'durata massima del servizio espressa in giorni in caso di terzo rinnovo';
COMMENT ON COLUMN sbnweb.tbl_servizio.num_max_riprod IS 'numero massimo di riproduzioni che una biblioteca puo'' effettuare giornalmente (per tipo servizio = fotoriproduzione, digitalizzazione, ...) ';
COMMENT ON COLUMN sbnweb.tbl_servizio.max_gg_ant IS 'numero massimo di giorni di anticipo con cui si puo'' effettuare  una richiesta (ad es.: tipo servizio = prelazione)';
COMMENT ON COLUMN sbnweb.tbl_servizio.max_gg_dep IS 'numero massimo di giorni di durata del deposito per lettura';
COMMENT ON COLUMN sbnweb.tbl_servizio.max_gg_cons IS 'numero massimo di giorni di ritardo nella consegna di materiale (ad es.: per quello fuori sede)';
COMMENT ON COLUMN sbnweb.tbl_servizio.n_max_pren IS 'numero massimo di prenotazioni';
COMMENT ON COLUMN sbnweb.tbl_servizio.n_max_ggval_pren IS 'numero massimo di giorni di validita'' della prenotazione';
COMMENT ON COLUMN sbnweb.tbl_servizio.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbl_servizio.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbl_servizio.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbl_servizio.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbl_servizio.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbl_servizio_web_dati_richiesti IS 'Tipo di servizio / dati richiesti';
COMMENT ON COLUMN sbnweb.tbl_servizio_web_dati_richiesti.campo_richiesta IS 'numero del campo della richiesta (tabella codici ''LMRW'')';
COMMENT ON COLUMN sbnweb.tbl_servizio_web_dati_richiesti.id_tipo_servizio IS 'Identificativo del tipo servizio cui si riferisce questa configurazione';
COMMENT ON COLUMN sbnweb.tbl_servizio_web_dati_richiesti.obbligatorio IS 'Flag che indica l''obbligatorieta'' o meno del campo in inserimento richiesta';
COMMENT ON COLUMN sbnweb.tbl_servizio_web_dati_richiesti.ts_ins IS 'data e ora di inserimento';
COMMENT ON TABLE sbnweb.tbl_solleciti IS 'Solleciti';
COMMENT ON COLUMN sbnweb.tbl_solleciti.progr_sollecito IS 'progressivo che individua una lettera di sollecito';
COMMENT ON COLUMN sbnweb.tbl_solleciti.cod_rich_serv IS 'Identificativo della richiesta cui si riferisce questo sollecito';
COMMENT ON COLUMN sbnweb.tbl_solleciti.data_invio IS 'data di invio di una lettera di sollecito';
COMMENT ON COLUMN sbnweb.tbl_solleciti.note IS 'note relative al sollecito';
COMMENT ON COLUMN sbnweb.tbl_solleciti.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbl_solleciti.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tbl_solleciti.tipo_invio IS 'Tipo invio utilizzato per questo sollecito (tabella codici ''LTIS'')';
COMMENT ON COLUMN sbnweb.tbl_solleciti.esito IS 'Falg che indica l''esito dell''invio (S=Inviato, N=Non inviato)';
COMMENT ON TABLE sbnweb.tbl_specificita_titoli_studio IS 'Specificazioni titoli studio';
COMMENT ON COLUMN sbnweb.tbl_specificita_titoli_studio.id_specificita_titoli_studio IS 'Identificativo univoco del titolo di studio';
COMMENT ON COLUMN sbnweb.tbl_specificita_titoli_studio.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_specificita_titoli_studio.cd_biblioteca IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tbl_specificita_titoli_studio.tit_studio IS 'codice identificativo del titolo di studio';
COMMENT ON COLUMN sbnweb.tbl_specificita_titoli_studio.specif_tit IS 'codice della specificazione del titolo di studio ';
COMMENT ON COLUMN sbnweb.tbl_specificita_titoli_studio.descr IS 'descrizione del titolo di studio';
COMMENT ON COLUMN sbnweb.tbl_specificita_titoli_studio.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbl_specificita_titoli_studio.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbl_specificita_titoli_studio.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbl_specificita_titoli_studio.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbl_specificita_titoli_studio.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbl_storico_richieste_servizio IS 'Storico richieste di servizio';
COMMENT ON TABLE sbnweb.tbl_supporti_biblioteca IS 'Supporti previsti in biblioteca';
COMMENT ON COLUMN sbnweb.tbl_supporti_biblioteca.id_supporti_biblioteca IS 'Identificativo univoco del supporto';
COMMENT ON COLUMN sbnweb.tbl_supporti_biblioteca.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_supporti_biblioteca.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tbl_supporti_biblioteca.cod_supporto IS 'codice del tipo di supporto';
COMMENT ON COLUMN sbnweb.tbl_supporti_biblioteca.imp_unita IS 'importo per pezzo';
COMMENT ON COLUMN sbnweb.tbl_supporti_biblioteca.costo_fisso IS 'Costo fisso del supporto';
COMMENT ON COLUMN sbnweb.tbl_supporti_biblioteca.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbl_supporti_biblioteca.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tbl_tipi_autorizzazioni IS 'PROFILI DI AUTORIZZAZIONE UTENTI AI SERVIZI';
COMMENT ON COLUMN sbnweb.tbl_tipi_autorizzazioni.id_tipi_autorizzazione IS 'Identificativo univoco del tipo autorizzazione';
COMMENT ON COLUMN sbnweb.tbl_tipi_autorizzazioni.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_tipi_autorizzazioni.cd_bib IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tbl_tipi_autorizzazioni.cod_tipo_aut IS 'codice identificativo del tipo di autorizzazione';
COMMENT ON COLUMN sbnweb.tbl_tipi_autorizzazioni.descr IS 'descrizione del tipo di autorizzazione';
COMMENT ON COLUMN sbnweb.tbl_tipi_autorizzazioni.flag_aut IS 'indica se il tipo di autorizzazione e'' predefinita cioe'' quella che viene associata ad un utente che non risulta registrato in nessuna biblioteca ';
COMMENT ON COLUMN sbnweb.tbl_tipi_autorizzazioni.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbl_tipi_autorizzazioni.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbl_tipi_autorizzazioni.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbl_tipi_autorizzazioni.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbl_tipi_autorizzazioni.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbl_tipo_servizio IS 'TIPI DI SERVIZIO EROGATI DALLA BIBLIOTECA';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.id_tipo_servizio IS 'Identificativo univoco del tipo servizio';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.cd_bib IS 'Codice della biblioteca';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.cod_tipo_serv IS 'codice tipo servizio';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.num_max_mov IS 'numero massimo di movimenti contemporaneamente attivi previsti';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.ore_ridis IS 'tempo in ore necessario per la ridisponibilita'' di un documento (ricollocazione). valore da utilizzare per il calcolo della data presunta di inizio del servizio.';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.penalita IS 'indicatore di tipo servizio con penalita'' per inosservanza del regolamento';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.coda_richieste IS 'numero massimo di richieste inevase';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.ins_richieste_utente IS 'Flag che indica la possibilita'' per l''utente lettore d''inserire una richiesta per questo tipo servizio';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.anche_da_remoto IS 'Flag che indica per l''utente lettore la possibilita'' di inserire la richiesta anche da remoto';
COMMENT ON COLUMN sbnweb.tbl_tipo_servizio.cd_iso_ill IS 'servizio ISO-ILL associato al servizio locale';
COMMENT ON TABLE sbnweb.tbl_utenti IS 'Utenti';
COMMENT ON COLUMN sbnweb.tbl_utenti.id_utenti IS 'Identificativo univoco dell''utente';
COMMENT ON COLUMN sbnweb.tbl_utenti.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_utenti.cd_bib IS 'Codice della biblioteca';
COMMENT ON COLUMN sbnweb.tbl_utenti.cod_utente IS 'seconda parte del codice identificativo dell''utente , costituita dal progressivo numerico assegnatogli dalla prima biblioteca del polo alla quale l''utente SI E'' ISCRITTO';
COMMENT ON COLUMN sbnweb.tbl_utenti.password IS 'password dell''utente';
COMMENT ON COLUMN sbnweb.tbl_utenti.cognome IS 'cognome dell''utente';
COMMENT ON COLUMN sbnweb.tbl_utenti.nome IS 'nome dell''''utente';
COMMENT ON COLUMN sbnweb.tbl_utenti.indirizzo_res IS 'indirizzo di residenza';
COMMENT ON COLUMN sbnweb.tbl_utenti.citta_res IS 'denominazione della citta'' di residenza';
COMMENT ON COLUMN sbnweb.tbl_utenti.cap_res IS 'codice di avviamento postale di residenza';
COMMENT ON COLUMN sbnweb.tbl_utenti.tel_res IS 'numero di telefono di residenza';
COMMENT ON COLUMN sbnweb.tbl_utenti.fax_res IS 'numero di fax di residenza';
COMMENT ON COLUMN sbnweb.tbl_utenti.indirizzo_dom IS 'indirizzo di domicilio';
COMMENT ON COLUMN sbnweb.tbl_utenti.citta_dom IS 'denominazione della citta'' di domicilio';
COMMENT ON COLUMN sbnweb.tbl_utenti.cap_dom IS 'codice di avviamento postale di domicilio';
COMMENT ON COLUMN sbnweb.tbl_utenti.tel_dom IS 'numero di telefono di domicilio';
COMMENT ON COLUMN sbnweb.tbl_utenti.fax_dom IS 'numero di fax di domicilio';
COMMENT ON COLUMN sbnweb.tbl_utenti.sesso IS 'sesso';
COMMENT ON COLUMN sbnweb.tbl_utenti.data_nascita IS 'data di nascita';
COMMENT ON COLUMN sbnweb.tbl_utenti.luogo_nascita IS 'denominazione del luogo di nascita';
COMMENT ON COLUMN sbnweb.tbl_utenti.cod_fiscale IS 'codice fiscale';
COMMENT ON COLUMN sbnweb.tbl_utenti.cod_ateneo IS 'codice dell''ateneo di appartenenza nel caso di studente/ssa universitario/a';
COMMENT ON COLUMN sbnweb.tbl_utenti.cod_matricola IS 'codice matricola universitaria';
COMMENT ON COLUMN sbnweb.tbl_utenti.corso_laurea IS 'descrizione del corso di laurea';
COMMENT ON COLUMN sbnweb.tbl_utenti.ind_posta_elettr IS 'indirizzo di posta elettronica primario';
COMMENT ON COLUMN sbnweb.tbl_utenti.ind_posta_elettr2 IS 'indirizzo di posta elettronico secondario';
COMMENT ON COLUMN sbnweb.tbl_utenti.persona_giuridica IS 'indica se l''utente e'' una persona giuridica ammette i valori s = si, n = no';
COMMENT ON COLUMN sbnweb.tbl_utenti.nome_referente IS 'nome del referente nel caso in cui l''utente sia una persona giuridica';
COMMENT ON COLUMN sbnweb.tbl_utenti.data_reg IS 'data della prima registrazione dell''utente da parte della biblioteca';
COMMENT ON COLUMN sbnweb.tbl_utenti.credito_polo IS 'credito a disposizione dell''utente per i servizi di polo';
COMMENT ON COLUMN sbnweb.tbl_utenti.note_polo IS 'note riguardanti l''utente a livello di polo';
COMMENT ON COLUMN sbnweb.tbl_utenti.note IS 'eventuali infrazioni effettuate a livello di polo';
COMMENT ON COLUMN sbnweb.tbl_utenti.cod_proven IS 'codice identificativo della provenienza dell''utente';
COMMENT ON COLUMN sbnweb.tbl_utenti.tipo_pers_giur IS 'codice identificativo del tipo di persona giuridica';
COMMENT ON COLUMN sbnweb.tbl_utenti.paese_res IS 'codice identificativo del paese di residenza';
COMMENT ON COLUMN sbnweb.tbl_utenti.paese_citt IS 'codice identificativo del paese di cittadinanza';
COMMENT ON COLUMN sbnweb.tbl_utenti.tipo_docum1 IS 'codice identificativo del tipo del primo documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.num_docum1 IS 'numero del primo documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.aut_ril_docum1 IS 'autorita'' di rilascio del primo documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.tipo_docum2 IS 'codice identificativo del tipo del secondo documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.num_docum2 IS 'numero del secondo documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.aut_ril_docum2 IS 'autorita'' di rilascio del secondo documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.tipo_docum3 IS 'codice identificativo del tipo del terzo documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.num_docum3 IS 'numero del terzo documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.aut_ril_docum3 IS 'autorita'' di rilascio del terzo documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.tipo_docum4 IS 'codice identificativo del tipo del quarto documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.num_docum4 IS 'numero del quarto documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.aut_ril_docum4 IS 'autorita'' di rilascio del quarto documento di riconoscimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.cod_bib IS 'per l''utente-biblioteca, codice identificativo della biblioteca corrispondente';
COMMENT ON COLUMN sbnweb.tbl_utenti.prov_dom IS 'provincia di domicilio';
COMMENT ON COLUMN sbnweb.tbl_utenti.prov_res IS 'provincia di residenza';
COMMENT ON COLUMN sbnweb.tbl_utenti.cod_polo_bib IS 'per l''utente-biblioteca, codice identificativo del polo della biblioteca corrispondente';
COMMENT ON COLUMN sbnweb.tbl_utenti.allinea IS 'autorizzazione alla biblioteca ad inviare SMS: ''X'' no SMS, ''F'' su fisso, ''M'' su mobile';
COMMENT ON COLUMN sbnweb.tbl_utenti.chiave_ute IS 'chiave nome utente';
COMMENT ON COLUMN sbnweb.tbl_utenti.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbl_utenti.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbl_utenti.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbl_utenti.fl_canc IS 'flag di cancellazione';
COMMENT ON COLUMN sbnweb.tbl_utenti.change_password IS 'Flag che indica la necessita'' di variare la password da parte dell''utente';
COMMENT ON COLUMN sbnweb.tbl_utenti.last_access IS 'Data di ultimo accesso al sistema da parte dell''utente';
COMMENT ON COLUMN sbnweb.tbl_utenti.ts_var_password IS 'Data di ultima variazione della password';
COMMENT ON COLUMN sbnweb.tbl_utenti.codice_anagrafe IS 'codice anagrafe della biblioteca registrata come utente';
COMMENT ON COLUMN sbnweb.tbl_utenti.tit_studio IS 'identificativo del titolo di studio';
COMMENT ON COLUMN sbnweb.tbl_utenti.professione IS 'identificativo della professione';
COMMENT ON COLUMN sbnweb.tbl_utenti.tidx_vector IS 'campo utilizzato per la ricerca testuale del nominativo';
COMMENT ON COLUMN sbnweb.tbl_utenti.cd_tipo_ute IS 'Tipo utente: S=SbnWeb; E=Esterno';
COMMENT ON TABLE sbnweb.tbp_esemplare_fascicolo IS 'Descrizione dei fascicoli di periodico';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.bid IS 'identificativo del periodico di cui fa parte il fascicolo';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.fid IS 'identificativo del fascicolo ';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.id_ordine IS 'codice identificativo dell''ordine di acquisizione';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.cd_bib_abb IS 'codice identificativo della biblioteca per cui e'' gestito l''abbonamento';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.data_arrivo IS 'data ricevimento del fascicolo';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.cd_polo_inv IS 'codice identificativo del polo cui appartiene la biblioteca cui si riferisce l''inventario fascicolo';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.cd_bib_inv IS 'codice identificativo della biblioteca a cui si riferisce l''inventario cui e'' associato il fascicolo';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.cd_serie IS 'codice della serie dell''inventario cui e'' associato il fascicolo';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.cd_inven IS 'n.ro dell''inventario cui e'' associato il fascicolo';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.grp_fasc IS 'prog.vo che identifica il gruppo di fascicoli, tra quelli associati all''inventario, di cui fa parte il fascicolo in esame';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.note IS 'note';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.ute_ins IS 'utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.ute_var IS 'utente che ha effettuato l''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.fl_canc IS 'indicatore di cancellazione logica';
COMMENT ON COLUMN sbnweb.tbp_esemplare_fascicolo.cd_no_disp IS 'Codice di non disponibilita'' dell''esemplare di fascicolo. ''S'' = Smarrito; ''R'' = in rilegatura';
COMMENT ON TABLE sbnweb.tbp_fascicolo IS 'Descrive la relazione fra i fascicoli di periodico e la sua descrizione bibliografica';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.bid IS 'codice identificativo dell''oggetto bibliografico relativo al periodico ';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.fid IS 'progressivo identificativo del fascicolo all''interno del periodico';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.sici IS 'Serial Item and Contribution Identifier (SICI) per il fascicolo';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.ean IS 'European Article Number';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.data_conv_pub IS 'Data convenzionale di pubblicazione del fascicolo la cui valorizzazione e'' controllata dal sistema con regole dipendenti dalla periodicita''';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.cd_per IS 'codice periodicita'' di pubblicazione del fascicolo';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.cd_tipo_fasc IS 'codice del tipo di fascicolo (semplice multiplo, ecc)';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.data_pubb IS 'data di pubblicazione rilevata dal fascicolo';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.descrizione IS 'Descrizione delle caratteristiche del fascicolo';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.annata IS 'anno descrittivo dell''abbonamento ';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.num_volume IS 'numero del volume nel caso in cui il fascicolo prevede un volume che lo contiene';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.num_in_fasc IS 'numero del fascicolo /numero iniziale del fascicolo per multipli quando ad un fascicolo corrispondono piu'' numeri del periodico';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.data_in_pubbl IS 'data iniziale di pubblicazione del fascicolo per multipli';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.num_fi_fasc IS 'numero finale del fascicolo per multipli quando ad un fascicolo corrispondono piu'' numeri del periodico';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.data_fi_pubbl IS 'data finale di pubblicazione del fascicolo per multipli';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.note IS 'note relative al fascicolo';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.num_alter IS 'numerazione alternativa del fascicolo fuori progressione';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.bid_link IS 'BID del periodico che ha sostituito quello corrente in caso di fusione di fascicolo (significativo solo in caso di fl_canc=S)';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.fid_link IS 'Fid del fascicolo che ha sostituito la descrizione corrente in caso di fusione di fascicolo (significativo solo in caso di fl_canc=S)';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.ute_ins IS 'utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.ts_ins IS 'data e ora di inserimento';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.ute_var IS 'utente che ha effettuato l''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tbp_fascicolo.fl_canc IS 'indicatore di cancellazione logica';
COMMENT ON TABLE sbnweb.tbq_batch_attivabili IS 'Processi batch attivabili';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.cod_funz IS 'codice identificativo della funzione';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.descr IS 'descrizione della funzione';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.descr_b IS 'descrizione breve della funzione';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.server IS 'server su cui risiede il processo batch da attivare (p, b)';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.path IS 'indirizzo del file da eseguire';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.job_name IS 'nome del file da eseguire';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.tipo IS 'estensione del file da eseguire';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.queue_name IS 'nome della coda bq+ a cui accodare l''esecuzione';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.flg_paral IS ' indicatore di esecuzione contemporanea con altri processi (1 = ammessa , 0= non ammessa)';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.priorita IS 'priorita'' di esecuzione (0=bassa, 1=media, 2=alta)';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.flg_prior IS 'indicatore di gestione priorita'' da parte dell''utente (0=non modificabile, 1 =modificabile)';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.flg_canc IS 'indicatore di possibilita'' di cancellazione durante l''esecuzione (0=no, 1=si)';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.flg_stampa IS 'indicatore di output in formato stampa (0=no, 1=si)';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.flg_name IS 'nome da assegnare al file di output';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.flg_fname IS 'indicatore di modificabilita'' del nome del file di output da parte dell''utente';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.condiz IS 'lista dei job che condizionano il processo, separati da virgola (,)';
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili.canc IS 'flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbr_fornitori IS 'Fornitori';
COMMENT ON COLUMN sbnweb.tbr_fornitori.cod_fornitore IS 'codice identificativo del fornitore';
COMMENT ON COLUMN sbnweb.tbr_fornitori.nom_fornitore IS 'denominazione del fornitore';
COMMENT ON COLUMN sbnweb.tbr_fornitori.unit_org IS 'unita'' organizzativa del fornitore';
COMMENT ON COLUMN sbnweb.tbr_fornitori.indirizzo IS 'indirizzo';
COMMENT ON COLUMN sbnweb.tbr_fornitori.cpostale IS 'casella postale';
COMMENT ON COLUMN sbnweb.tbr_fornitori.citta IS 'citta''';
COMMENT ON COLUMN sbnweb.tbr_fornitori.cap IS 'codice d''avviamento postale';
COMMENT ON COLUMN sbnweb.tbr_fornitori.telefono IS 'numero telefonico';
COMMENT ON COLUMN sbnweb.tbr_fornitori.fax IS 'numero del fax';
COMMENT ON COLUMN sbnweb.tbr_fornitori.note IS 'note riferite al fornitore';
COMMENT ON COLUMN sbnweb.tbr_fornitori.p_iva IS 'partita iva';
COMMENT ON COLUMN sbnweb.tbr_fornitori.cod_fiscale IS 'codice fiscale';
COMMENT ON COLUMN sbnweb.tbr_fornitori.e_mail IS 'indirizzo elettronico';
COMMENT ON COLUMN sbnweb.tbr_fornitori.paese IS 'codice identificativo del paese';
COMMENT ON COLUMN sbnweb.tbr_fornitori.tipo_partner IS 'codice identificativo della tipologia di partner';
COMMENT ON COLUMN sbnweb.tbr_fornitori.provincia IS 'codice identificativo della provincia';
COMMENT ON COLUMN sbnweb.tbr_fornitori.cod_bib IS 'per il fornitore-biblioteca, codice  identificativo della biblioteca corrispondente';
COMMENT ON COLUMN sbnweb.tbr_fornitori.chiave_for IS 'chiave nome fornitore';
COMMENT ON COLUMN sbnweb.tbr_fornitori.cod_polo_bib IS 'per il fornitore-biblioteca, codice identificativo del polo della biblioteca corrispondente ';
COMMENT ON COLUMN sbnweb.tbr_fornitori.ute_ins IS 'codice biblioteca che effettua inserimento';
COMMENT ON COLUMN sbnweb.tbr_fornitori.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbr_fornitori.ute_var IS 'codice biblioteca che effettua aggiornamento';
COMMENT ON COLUMN sbnweb.tbr_fornitori.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tbr_fornitori.tidx_vector IS 'campo utilizzato per la ricerca testuale del nome fornitore';
COMMENT ON TABLE sbnweb.tbr_fornitori_biblioteche IS 'Dati di fornitore in Biblioteca';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.cd_biblioteca IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.cod_fornitore IS 'codice identificativo del fornitore';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.tipo_pagamento IS 'descrizione del modo di pagamento o titolo c/c bancario con il quale il fornitore vuole essere pagato dalla biblioteca ';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.cod_cliente IS 'codice cliente che ha la biblioteca presso il fornitore';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.nom_contatto IS 'denominazione del personale da contattare nel caso in cui il fornitore sia un ente';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.tel_contatto IS 'numero di telefono del personale da contattare nel caso in cui il fornitore sia un ente';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.fax_contatto IS 'numero di fax del personale da contattare nel caso in cui il fornitore sia un ente';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.valuta IS 'codice della valuta di pagamento';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.cod_polo IS 'codice polo';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.allinea IS 'flag allinea';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tbr_fornitori_biblioteche.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tr_aut_aut IS 'COLLEGAMENTI TRA AUTORI';
COMMENT ON COLUMN sbnweb.tr_aut_aut.vid_base IS 'VID della forma accettata.
 Questo VID e'' sempre relativo ad un autore in forma Accettata.';
COMMENT ON COLUMN sbnweb.tr_aut_aut.vid_coll IS 'VID della forma variante (legame 8) o accettata (legame 4).';
COMMENT ON COLUMN sbnweb.tr_aut_aut.tp_legame IS 'Tipo rinvio : 4-Vedi anche ; 8-Vedi';
COMMENT ON COLUMN sbnweb.tr_aut_aut.nota_aut_aut IS 'Prot. SBN=80 chr.';
COMMENT ON COLUMN sbnweb.tr_aut_aut.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_aut_aut.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_aut_aut.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_aut_aut.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_aut_aut.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_aut_bib IS 'AUTORI IN BIBLIOTECA';
COMMENT ON COLUMN sbnweb.tr_aut_bib.vid IS 'VID della forma accettata.
 Questo VID e'' sempre relativo ad un autore in forma Accettata.';
COMMENT ON COLUMN sbnweb.tr_aut_bib.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tr_aut_bib.cd_biblioteca IS 'Codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tr_aut_bib.fl_allinea IS 'Flag allineamento (valori : space, R, S, Z)';
COMMENT ON COLUMN sbnweb.tr_aut_bib.fl_allinea_sbnmarc IS 'Flag allineamento a SBNMARC';
COMMENT ON COLUMN sbnweb.tr_aut_bib.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_aut_bib.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_aut_bib.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_aut_bib.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_aut_bib.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_aut_mar IS 'COLLEGAMENTI AUTORE MARCHE EDITORIALI';
COMMENT ON COLUMN sbnweb.tr_aut_mar.vid IS 'VID della forma accettata.
 Questo VID e'' sempre relativo ad un autore in forma Accettata.';
COMMENT ON COLUMN sbnweb.tr_aut_mar.mid IS 'Identificativo della marca in SBN';
COMMENT ON COLUMN sbnweb.tr_aut_mar.nota_aut_mar IS 'nota all''oggetto';
COMMENT ON COLUMN sbnweb.tr_aut_mar.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_aut_mar.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_aut_mar.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_aut_mar.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_aut_mar.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_des_des IS 'COLLEGAMENTI TRA DESCRITTORI DI SOGGETTO (TPSCDS))';
COMMENT ON COLUMN sbnweb.tr_des_des.did_base IS 'Identificativo del primo descrittore del legame';
COMMENT ON COLUMN sbnweb.tr_des_des.did_coll IS 'Identificativo del secondo descrittore del legame';
COMMENT ON COLUMN sbnweb.tr_des_des.tp_legame IS 'codice legame tra descrittori (tabella codici ''LEDD'')';
COMMENT ON COLUMN sbnweb.tr_des_des.nota_des_des IS 'note al legame';
COMMENT ON COLUMN sbnweb.tr_des_des.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_des_des.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_des_des.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_des_des.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_des_des.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_luo_luo IS 'COLLEGAMENTI TRA LUOGHI';
COMMENT ON COLUMN sbnweb.tr_luo_luo.lid_base IS 'Identificativo del luogo';
COMMENT ON COLUMN sbnweb.tr_luo_luo.lid_coll IS 'Identificativo del luogo in forma variante';
COMMENT ON COLUMN sbnweb.tr_luo_luo.tp_legame IS 'Tipo legame';
COMMENT ON COLUMN sbnweb.tr_luo_luo.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_luo_luo.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_luo_luo.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_luo_luo.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_luo_luo.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_mar_bib IS 'MARCHE EDITORIALI IN BIBLIOTECA';
COMMENT ON COLUMN sbnweb.tr_mar_bib.cd_polo IS 'codice identificativo del luogo';
COMMENT ON COLUMN sbnweb.tr_mar_bib.cd_biblioteca IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tr_mar_bib.mid IS 'identificativo della marca';
COMMENT ON COLUMN sbnweb.tr_mar_bib.fl_allinea IS 'flag di allineamento';
COMMENT ON COLUMN sbnweb.tr_mar_bib.fl_allinea_sbnmarc IS 'flag di allineamento a SBNMARC';
COMMENT ON COLUMN sbnweb.tr_mar_bib.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_mar_bib.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_mar_bib.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_mar_bib.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_mar_bib.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_modello_sistemi_classi_biblioteche IS 'Modello di profilazione dei sistemi delle classificazione per la biblioteca';
COMMENT ON COLUMN sbnweb.tr_modello_sistemi_classi_biblioteche.flg_att IS 'indicatore dell''attuale utilizzo del sistema di classificazione da parte della biblioteca';
COMMENT ON COLUMN sbnweb.tr_modello_sistemi_classi_biblioteche.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_modello_sistemi_classi_biblioteche.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_modello_sistemi_classi_biblioteche.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_modello_sistemi_classi_biblioteche.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_modello_sistemi_classi_biblioteche.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_modello_soggettari_biblioteche IS 'Modello di profilazione dei soggettari per la biblioteca';
COMMENT ON COLUMN sbnweb.tr_modello_soggettari_biblioteche.fl_att IS 'indicatore dell''attuale utilizzo del soggettario da parte della biblioteca';
COMMENT ON COLUMN sbnweb.tr_modello_soggettari_biblioteche.fl_auto_loc IS 'indicatore di localizzazione automatica legami a soggetti inseriti da altra biblioteca';
COMMENT ON COLUMN sbnweb.tr_modello_soggettari_biblioteche.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_modello_soggettari_biblioteche.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_modello_soggettari_biblioteche.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_modello_soggettari_biblioteche.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_modello_soggettari_biblioteche.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_modello_thesauri_biblioteche IS 'Modello di profilazione dei thesauri per la biblioteca';
COMMENT ON COLUMN sbnweb.tr_modello_thesauri_biblioteche.cd_the IS 'codice thesauro';
COMMENT ON COLUMN sbnweb.tr_modello_thesauri_biblioteche.fl_att IS 'indicatore dell''attuale utilizzo del thesauro da parte della biblioteca';
COMMENT ON COLUMN sbnweb.tr_modello_thesauri_biblioteche.fl_auto_loc IS 'indicatore di localizzazione automatica legami a termini di thesauro inseriti da altra biblioteca';
COMMENT ON COLUMN sbnweb.tr_modello_thesauri_biblioteche.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_modello_thesauri_biblioteche.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_modello_thesauri_biblioteche.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_modello_thesauri_biblioteche.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_modello_thesauri_biblioteche.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_per_int IS 'COLLEGAMENTO PERSONAGGIO INTERPRETE (Musica)';
COMMENT ON COLUMN sbnweb.tr_per_int.vid IS 'identificativo dell''autore';
COMMENT ON COLUMN sbnweb.tr_per_int.id_personaggio IS 'identificativo del personaggio';
COMMENT ON COLUMN sbnweb.tr_per_int.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_per_int.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_per_int.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_per_int.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_per_int.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_rep_aut IS 'COLLEGAMENTI AUTORE REPERTORIO';
COMMENT ON COLUMN sbnweb.tr_rep_aut.vid IS 'identificativo dell''autore';
COMMENT ON COLUMN sbnweb.tr_rep_aut.id_repertorio IS 'identificativo del repertorio';
COMMENT ON COLUMN sbnweb.tr_rep_aut.note_rep_aut IS 'note all''oggetto';
COMMENT ON COLUMN sbnweb.tr_rep_aut.fl_trovato IS 'Indicazione se l''autore e'' stato trovato o meno nel repertorio (Valori S=trovato; N=non trovato).';
COMMENT ON COLUMN sbnweb.tr_rep_aut.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_rep_aut.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_rep_aut.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_rep_aut.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_rep_aut.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_rep_mar IS 'MARCHE DEL REPERTORIO';
COMMENT ON COLUMN sbnweb.tr_rep_mar.progr_repertorio IS 'progressivo repertorio';
COMMENT ON COLUMN sbnweb.tr_rep_mar.mid IS 'Identificativo della marca';
COMMENT ON COLUMN sbnweb.tr_rep_mar.id_repertorio IS 'Progressivo del repertorio: impostato con la citazione';
COMMENT ON COLUMN sbnweb.tr_rep_mar.nota_rep_mar IS 'nota all''oggetto';
COMMENT ON COLUMN sbnweb.tr_rep_mar.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_rep_mar.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_rep_mar.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_rep_mar.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_rep_mar.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_rep_tit IS 'COLLEGAMENTO TITOLO REPERTORIO';
COMMENT ON COLUMN sbnweb.tr_rep_tit.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tr_rep_tit.id_repertorio IS 'Identificativo del repertorio';
COMMENT ON COLUMN sbnweb.tr_rep_tit.nota_rep_tit IS 'nota all''oggetto';
COMMENT ON COLUMN sbnweb.tr_rep_tit.fl_trovato IS 'Indica se il titolo e'' stato trovato o meno nel repertorio (Valori S=trovato; N=non trovato).';
COMMENT ON COLUMN sbnweb.tr_rep_tit.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_rep_tit.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_rep_tit.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_rep_tit.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_rep_tit.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_sistemi_classi_biblioteche IS 'SISTEMA DI CLASSIFICAZIONE IN BIBLIOTECA (TPSSCB))';
COMMENT ON COLUMN sbnweb.tr_sistemi_classi_biblioteche.cd_biblioteca IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tr_sistemi_classi_biblioteche.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tr_sistemi_classi_biblioteche.cd_sistema IS 'Codice del sistema di classificazione';
COMMENT ON COLUMN sbnweb.tr_sistemi_classi_biblioteche.cd_edizione IS 'Edizione per il sistema di classificazione Dewey';
COMMENT ON COLUMN sbnweb.tr_sistemi_classi_biblioteche.flg_att IS 'indicatore dell''attuale utilizzo del sistema di classificazione da parte della biblioteca';
COMMENT ON COLUMN sbnweb.tr_sistemi_classi_biblioteche.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_sistemi_classi_biblioteche.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_sistemi_classi_biblioteche.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_sistemi_classi_biblioteche.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_sistemi_classi_biblioteche.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tr_sistemi_classi_biblioteche.sololocale IS 'Indicatore dell''utilizzo del sistema di classificazione solo per il sistema locale (e non in Indice)';
COMMENT ON TABLE sbnweb.tr_sog_des IS 'DESCRITTORI NEI SOGGETTI (TPSSDS))';
COMMENT ON COLUMN sbnweb.tr_sog_des.did IS 'Identificativo del descrittore';
COMMENT ON COLUMN sbnweb.tr_sog_des.cid IS 'Identificativo del soggetto';
COMMENT ON COLUMN sbnweb.tr_sog_des.fl_posizione IS 'indicatore di legame inserito manualmente';
COMMENT ON COLUMN sbnweb.tr_sog_des.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_sog_des.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_sog_des.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_sog_des.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_sog_des.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tr_sog_des.fl_primavoce IS 'Indicatore della presenza del descrittore come prima voce nella descrizione del soggetto';
COMMENT ON TABLE sbnweb.tr_soggettari_biblioteche IS 'SOGGETTARI IN BIBLIOTECA (TPSSBI))';
COMMENT ON COLUMN sbnweb.tr_soggettari_biblioteche.cd_sogg IS 'codice del tipo soggettario';
COMMENT ON COLUMN sbnweb.tr_soggettari_biblioteche.cd_biblioteca IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tr_soggettari_biblioteche.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tr_soggettari_biblioteche.fl_att IS 'indicatore dell''attuale utilizzo del soggettario da parte della biblioteca';
COMMENT ON COLUMN sbnweb.tr_soggettari_biblioteche.fl_auto_loc IS 'indicatore di localizzazione automatica legami a soggetti inseriti da altra biblioteca';
COMMENT ON COLUMN sbnweb.tr_soggettari_biblioteche.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_soggettari_biblioteche.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_soggettari_biblioteche.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_soggettari_biblioteche.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_soggettari_biblioteche.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tr_soggettari_biblioteche.sololocale IS 'Indicatore dell''utilizzo del soggettario solo per il sistema locale (e non in Indice)';
COMMENT ON TABLE sbnweb.tr_sogp_sogi IS 'Tabella di corrispondenza soggetti & legami titolo soggetto di polo con soggetti & legami titolo soggetto di indice';
COMMENT ON COLUMN sbnweb.tr_sogp_sogi.cid_p IS 'Identificativo del soggetto in Polo';
COMMENT ON COLUMN sbnweb.tr_sogp_sogi.cid_i IS 'Identificativo del soggetto uguale in Indice';
COMMENT ON COLUMN sbnweb.tr_sogp_sogi.bid IS 'Identificativo del titolo legato sia in polo che in indice';
COMMENT ON COLUMN sbnweb.tr_sogp_sogi.fl_imp_sog IS 'I=soggetto importato da Indice; E=soggetto esportato in Indice';
COMMENT ON COLUMN sbnweb.tr_sogp_sogi.fl_sog_mod_da IS 'modificato dopo la condivisione: null=NO; I=da Indice; P=da Polo';
COMMENT ON COLUMN sbnweb.tr_sogp_sogi.fl_imp_tit_sog IS 'I=importato da Indice; E=esportato in Indice';
COMMENT ON COLUMN sbnweb.tr_sogp_sogi.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_sogp_sogi.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_sogp_sogi.ute_var IS 'Utente che ha effettuato l''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tr_sogp_sogi.ts_var IS 'Timestamp di aggiornamento';
COMMENT ON COLUMN sbnweb.tr_sogp_sogi.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_termini_termini IS 'COLLEGAMENTI TRA DESCRITTORI DI THESAURO (TPSCDT))';
COMMENT ON COLUMN sbnweb.tr_termini_termini.did_base IS 'Identificativo del primo termine';
COMMENT ON COLUMN sbnweb.tr_termini_termini.did_coll IS 'Identificativo del secondo termine';
COMMENT ON COLUMN sbnweb.tr_termini_termini.nota_termini_termini IS 'note al legame';
COMMENT ON COLUMN sbnweb.tr_termini_termini.tipo_coll IS 'codice legame tra termini (tabella codici ''STLT'')';
COMMENT ON COLUMN sbnweb.tr_termini_termini.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_termini_termini.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_termini_termini.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_termini_termini.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_termini_termini.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_the_cla IS 'Tabella dei legami Classi Materie ';
COMMENT ON COLUMN sbnweb.tr_the_cla.cd_the IS 'Codice del Thesauro';
COMMENT ON COLUMN sbnweb.tr_the_cla.did IS 'Codice identificativo del descrittore';
COMMENT ON COLUMN sbnweb.tr_the_cla.cd_sistema IS 'Codice del sistema di classificazione';
COMMENT ON COLUMN sbnweb.tr_the_cla.cd_edizione IS 'Edizione per il sistema di classificazione Dewey';
COMMENT ON COLUMN sbnweb.tr_the_cla.classe IS 'Simbolo di classificazione';
COMMENT ON COLUMN sbnweb.tr_the_cla.nota_the_cla IS 'Note al legame';
COMMENT ON COLUMN sbnweb.tr_the_cla.posizione IS 'Indica la posizione del legame';
COMMENT ON COLUMN sbnweb.tr_the_cla.ute_ins IS 'utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_the_cla.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tr_the_cla.ute_var IS 'utente che ha effettuato l''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tr_the_cla.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.tr_the_cla.fl_canc IS 'indicatore di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_thesauri_biblioteche IS 'SISTEMI DI THESAURO IN BIBLIOTECA (TPSTBI)';
COMMENT ON COLUMN sbnweb.tr_thesauri_biblioteche.cd_biblioteca IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tr_thesauri_biblioteche.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tr_thesauri_biblioteche.cd_the IS 'codice thesauro';
COMMENT ON COLUMN sbnweb.tr_thesauri_biblioteche.fl_att IS 'indicatore dell''attuale utilizzo del thesauro da parte della biblioteca';
COMMENT ON COLUMN sbnweb.tr_thesauri_biblioteche.fl_auto_loc IS 'indicatore di localizzazione automatica legami a termini di thesauro inseriti da altra biblioteca';
COMMENT ON COLUMN sbnweb.tr_thesauri_biblioteche.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_thesauri_biblioteche.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_thesauri_biblioteche.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_thesauri_biblioteche.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_thesauri_biblioteche.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tr_thesauri_biblioteche.sololocale IS 'Indicatore dell''utilizzo del thesauro solo per il sistema locale (e non in Indice)';
COMMENT ON TABLE sbnweb.tr_tit_aut IS 'AUTORI DI OGGETTI BIBLIOGRAFICI';
COMMENT ON COLUMN sbnweb.tr_tit_aut.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tr_tit_aut.vid IS 'Identificativo del''autore in SBN';
COMMENT ON COLUMN sbnweb.tr_tit_aut.tp_responsabilita IS 'Responsabilita'' (valore : 0, 1, 2, 3, 4)';
COMMENT ON COLUMN sbnweb.tr_tit_aut.cd_relazione IS 'Tipologia di contributo nella responsabilita''';
COMMENT ON COLUMN sbnweb.tr_tit_aut.nota_tit_aut IS 'nota all''oggetto';
COMMENT ON COLUMN sbnweb.tr_tit_aut.fl_incerto IS 'Indicatore di legame autore incerto, S=incerto, spazio=certo';
COMMENT ON COLUMN sbnweb.tr_tit_aut.fl_superfluo IS 'Indicatore di legame autore non obbligatorio, S=non obb, spazio=obbligatorio';
COMMENT ON COLUMN sbnweb.tr_tit_aut.cd_strumento_mus IS 'Strumento dell''interprete (musica)';
COMMENT ON COLUMN sbnweb.tr_tit_aut.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_aut.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_aut.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_tit_aut.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_tit_aut.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tr_tit_aut.fl_condiviso IS 'Flag di condivisione gestione del legame con indice';
COMMENT ON COLUMN sbnweb.tr_tit_aut.ute_condiviso IS 'Utente che ha attivato la gestione condivisa del legame con indice';
COMMENT ON COLUMN sbnweb.tr_tit_aut.ts_condiviso IS 'Timestamp di condivisione gestione del legame con indice';
COMMENT ON TABLE sbnweb.tr_tit_bib IS 'OGGETTO BIBLIOGRAFICO IN BIBLIOTECA';
COMMENT ON COLUMN sbnweb.tr_tit_bib.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tr_tit_bib.cd_polo IS 'Codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tr_tit_bib.cd_biblioteca IS 'Codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tr_tit_bib.fl_gestione IS 'Indica se il BID e'' gestito dal sistema locale';
COMMENT ON COLUMN sbnweb.tr_tit_bib.fl_disp_elettr IS 'Prot. SBN=NO Disponibilita'' in copia elettronica (spazio=NO; S=SI)';
COMMENT ON COLUMN sbnweb.tr_tit_bib.fl_allinea IS 'Flag allineamento (valori : space, C, S, Z) prot. Sbn';
COMMENT ON COLUMN sbnweb.tr_tit_bib.fl_allinea_sbnmarc IS 'Flag di allineamento prot. SbnMarc';
COMMENT ON COLUMN sbnweb.tr_tit_bib.fl_allinea_cla IS 'Flag di allineamento per modifica legame titolo-classe';
COMMENT ON COLUMN sbnweb.tr_tit_bib.fl_allinea_sog IS 'Flag di allineamento per modifica legame titolo-soggetto';
COMMENT ON COLUMN sbnweb.tr_tit_bib.fl_allinea_luo IS 'Flag di allineamento per modifica legame titolo-luogo';
COMMENT ON COLUMN sbnweb.tr_tit_bib.fl_allinea_rep IS 'Flag di allineamento per modifica legame titolo-repertorio';
COMMENT ON COLUMN sbnweb.tr_tit_bib.fl_mutilo IS 'flag mutilo';
COMMENT ON COLUMN sbnweb.tr_tit_bib.ds_consistenza IS 'descrizione consistenza';
COMMENT ON COLUMN sbnweb.tr_tit_bib.fl_possesso IS 'flag possesso';
COMMENT ON COLUMN sbnweb.tr_tit_bib.ds_fondo IS 'Prot. SBN=NO';
COMMENT ON COLUMN sbnweb.tr_tit_bib.ds_segn IS 'Prot. SBN=NO - Segnatura (man. mus.)';
COMMENT ON COLUMN sbnweb.tr_tit_bib.ds_antica_segn IS 'Prot. SBN=NO - Antica segnatura (man. mus.)';
COMMENT ON COLUMN sbnweb.tr_tit_bib.nota_tit_bib IS 'Prot. SBN=NO';
COMMENT ON COLUMN sbnweb.tr_tit_bib.uri_copia IS 'indirizzo uri della copia elettronica in biblioteca';
COMMENT ON COLUMN sbnweb.tr_tit_bib.tp_digitalizz IS 'tipo di digitalizzazione della copia';
COMMENT ON COLUMN sbnweb.tr_tit_bib.ts_ins_prima_coll IS 'data in cui e'' avvenuta la prima collocazione';
COMMENT ON COLUMN sbnweb.tr_tit_bib.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_bib.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_bib.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_tit_bib.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_tit_bib.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_tit_cla IS 'CLASSIFICAZIONI DI OGGETTI BIBLIOGRAFICI (TPSCOB))';
COMMENT ON COLUMN sbnweb.tr_tit_cla.bid IS 'codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.tr_tit_cla.classe IS 'Codice di classificazione';
COMMENT ON COLUMN sbnweb.tr_tit_cla.cd_sistema IS 'Codice identificativo del sistema di classificazione';
COMMENT ON COLUMN sbnweb.tr_tit_cla.cd_edizione IS 'Codice identificativo dell''edizione Dewey';
COMMENT ON COLUMN sbnweb.tr_tit_cla.nota_tit_cla IS 'note al legame';
COMMENT ON COLUMN sbnweb.tr_tit_cla.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_cla.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_cla.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_tit_cla.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_tit_cla.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_tit_luo IS 'LUOGO DI PUBBLICAZIONE DEL TITOLO';
COMMENT ON COLUMN sbnweb.tr_tit_luo.bid IS 'Codice identificativo SBN del titolo';
COMMENT ON COLUMN sbnweb.tr_tit_luo.lid IS 'codice identificativo del luogo';
COMMENT ON COLUMN sbnweb.tr_tit_luo.tp_luogo IS 'Accettata/Rinvio';
COMMENT ON COLUMN sbnweb.tr_tit_luo.nota_tit_luo IS 'nota all''oggetto';
COMMENT ON COLUMN sbnweb.tr_tit_luo.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_luo.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_luo.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_tit_luo.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_tit_luo.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_tit_mar IS 'MARCHE EDITORIALI DI OGGETTO BIBLIOGRAFICO';
COMMENT ON COLUMN sbnweb.tr_tit_mar.bid IS 'Codice identificativo SBN del titolo';
COMMENT ON COLUMN sbnweb.tr_tit_mar.mid IS 'Codice identificativo della marca';
COMMENT ON COLUMN sbnweb.tr_tit_mar.nota_tit_mar IS 'nota all''oggetto';
COMMENT ON COLUMN sbnweb.tr_tit_mar.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_mar.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_mar.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_tit_mar.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_tit_mar.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tr_tit_sog_bib IS 'SOGGETTI DI OGGETTI BIBLIOGRAFICI IN BIBLIOTECA (TPSSOO))';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.cid IS 'Identificativo del soggetto';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.cd_biblioteca IS 'Codice della Biblioteca';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.cd_sogg IS 'Codice del Soggettario';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.bid IS 'codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.nota_tit_sog_bib IS 'note al legame';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tr_tit_sog_bib.posizione IS 'Indica la posizione del legame';
COMMENT ON TABLE sbnweb.tr_tit_tit IS 'COLLEGAMENTI OGGETTI BIBLIOGRAFICI';
COMMENT ON COLUMN sbnweb.tr_tit_tit.bid_base IS 'Identificativo del titolo di partenza del legame';
COMMENT ON COLUMN sbnweb.tr_tit_tit.bid_coll IS 'Identificativo del titolo collegato nella forma variante';
COMMENT ON COLUMN sbnweb.tr_tit_tit.tp_legame IS 'Tipo di collegamento';
COMMENT ON COLUMN sbnweb.tr_tit_tit.tp_legame_musica IS 'Prot. SBN=NO - Ulteriore spec. del codice legame (mus.)';
COMMENT ON COLUMN sbnweb.tr_tit_tit.cd_natura_base IS 'Natura del titolo partenza';
COMMENT ON COLUMN sbnweb.tr_tit_tit.cd_natura_coll IS 'Natura del titolo collegato';
COMMENT ON COLUMN sbnweb.tr_tit_tit.sequenza IS 'Posiz. nella sequenza (allineam. a destra per ordinamento)';
COMMENT ON COLUMN sbnweb.tr_tit_tit.nota_tit_tit IS 'nota all''oggetto';
COMMENT ON COLUMN sbnweb.tr_tit_tit.sequenza_musica IS 'Prot. SBN=NO - Sequenza nel materiale musica';
COMMENT ON COLUMN sbnweb.tr_tit_tit.sici IS 'Identidicativo sici per fascicoli di periodico (solo per legami N01S)';
COMMENT ON COLUMN sbnweb.tr_tit_tit.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_tit.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_tit_tit.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_tit_tit.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_tit_tit.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tr_tit_tit.fl_condiviso IS 'Flag di condivisione gestione del legame con indice';
COMMENT ON COLUMN sbnweb.tr_tit_tit.ute_condiviso IS 'Timestamp di condivisione gestione del legame con indice';
COMMENT ON COLUMN sbnweb.tr_tit_tit.ts_condiviso IS 'Utente che ha attivato la gestione condivisa del legame con indice';
COMMENT ON TABLE sbnweb.tra_elementi_buono_ordine IS 'Elementi del buono d''ordine (ordini del buono)';
COMMENT ON COLUMN sbnweb.tra_elementi_buono_ordine.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tra_elementi_buono_ordine.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tra_elementi_buono_ordine.buono_ord IS 'identificativo del buono d''ordine';
COMMENT ON COLUMN sbnweb.tra_elementi_buono_ordine.cod_tip_ord IS 'codice del tipo d''ordine';
COMMENT ON COLUMN sbnweb.tra_elementi_buono_ordine.anno_ord IS 'anno dell''ordine';
COMMENT ON COLUMN sbnweb.tra_elementi_buono_ordine.cod_ord IS 'numero dell''ordine';
COMMENT ON TABLE sbnweb.tra_fornitori_offerte IS 'Fornitori che hanno risposto alla richiesta d''offerta (gara)';
COMMENT ON COLUMN sbnweb.tra_fornitori_offerte.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tra_fornitori_offerte.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tra_fornitori_offerte.cod_fornitore IS 'codice identificativo del fornitore';
COMMENT ON COLUMN sbnweb.tra_fornitori_offerte.cod_rich_off IS 'codice identificativo della richiesta d''offerta';
COMMENT ON COLUMN sbnweb.tra_fornitori_offerte.note IS 'note riferite al fornitore';
COMMENT ON COLUMN sbnweb.tra_fornitori_offerte.stato_gara IS 'stato del partecipante alla gara';
COMMENT ON COLUMN sbnweb.tra_fornitori_offerte.tipo_invio IS 'codice identificativo del tipo invio';
COMMENT ON COLUMN sbnweb.tra_fornitori_offerte.data_invio IS 'data di invio al fornitore della richiesta d''offerta ';
COMMENT ON COLUMN sbnweb.tra_fornitori_offerte.risp_da_risp IS 'messaggio di risposta del fornitore alla gara indetta ';
COMMENT ON COLUMN sbnweb.tra_fornitori_offerte.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tra_fornitori_offerte.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tra_messaggi IS 'Messaggi';
COMMENT ON COLUMN sbnweb.tra_messaggi.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tra_messaggi.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tra_messaggi.cod_msg IS 'codice identificativo del messaggio';
COMMENT ON COLUMN sbnweb.tra_messaggi.data_msg IS 'data del messaggio';
COMMENT ON COLUMN sbnweb.tra_messaggi.note IS 'testo del messaggio';
COMMENT ON COLUMN sbnweb.tra_messaggi.anno_fattura IS 'anno di registrazione della fattura';
COMMENT ON COLUMN sbnweb.tra_messaggi.progr_fattura IS 'progressivo che identifica la fattura nell''ambito dell''anno';
COMMENT ON COLUMN sbnweb.tra_messaggi.cod_tip_ord IS 'codice identificativo della tipologia dell''ordine';
COMMENT ON COLUMN sbnweb.tra_messaggi.anno_ord IS 'anno di acquisizione dell''ordine';
COMMENT ON COLUMN sbnweb.tra_messaggi.cod_ord IS 'codice identificativo dell''ordine';
COMMENT ON COLUMN sbnweb.tra_messaggi.stato_msg IS 'codice identificativo dello stato del messaggio';
COMMENT ON COLUMN sbnweb.tra_messaggi.tipo_msg IS 'codice identificativo della tipologia di messaggio';
COMMENT ON COLUMN sbnweb.tra_messaggi.tipo_invio IS 'codice identificativo del tipo invio';
COMMENT ON COLUMN sbnweb.tra_messaggi.cod_fornitore IS 'codice identificativo del fornitore';
COMMENT ON COLUMN sbnweb.tra_messaggi.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tra_messaggi.ts_var IS 'data e ora dell''ultimo aggiornamento;';
COMMENT ON TABLE sbnweb.tra_ordine_inventari IS ' ';
COMMENT ON COLUMN sbnweb.tra_ordine_inventari.id_ordine IS 'identificativo dell''ordine di tipo rilegatura';
COMMENT ON COLUMN sbnweb.tra_ordine_inventari.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tra_ordine_inventari.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tra_ordine_inventari.cd_serie IS 'codice identificativo della serie inventariale del materiale sottoposto alla rilegatura';
COMMENT ON COLUMN sbnweb.tra_ordine_inventari.cd_inven IS 'numero di inventario del materiale sottoposto alla rilegatura';
COMMENT ON COLUMN sbnweb.tra_ordine_inventari.data_uscita IS 'data e ora di uscita del materiale da sottoporre alla rilegatura';
COMMENT ON COLUMN sbnweb.tra_ordine_inventari.data_rientro IS 'data e ora di rientro effettiva del materiale sottoposto alla rilegatura';
COMMENT ON COLUMN sbnweb.tra_ordine_inventari.ota_fornitore IS 'note al fornitore che esegue la rilegatura';
COMMENT ON COLUMN sbnweb.tra_ordine_inventari.data_rientro_presunta IS 'data e ora di rientro prevista del materiale sottoposto alla rilegatura';
COMMENT ON COLUMN sbnweb.tra_ordine_inventari.posizione IS 'posizione dell''inventario nel carrello';
COMMENT ON COLUMN sbnweb.tra_ordine_inventari.volume IS 'volume a cui appartiene l''inventario';
COMMENT ON TABLE sbnweb.tra_ordini_biblioteche IS 'Ordini delle biblioteche';
COMMENT ON COLUMN sbnweb.tra_ordini_biblioteche.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tra_ordini_biblioteche.cd_bib IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tra_ordini_biblioteche.cod_bib_ord IS 'codice identificativo della biblioteca che effettua l''ordine';
COMMENT ON COLUMN sbnweb.tra_ordini_biblioteche.cod_tip_ord IS 'codice identificativo della tipologia dell''ordine';
COMMENT ON COLUMN sbnweb.tra_ordini_biblioteche.anno_ord IS 'anno di acquisizione dell''ordine';
COMMENT ON COLUMN sbnweb.tra_ordini_biblioteche.cod_ord IS 'codice identificativo dell''ordine';
COMMENT ON COLUMN sbnweb.tra_ordini_biblioteche.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tra_ordini_biblioteche.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.tra_sez_acq_storico IS ' ';
COMMENT ON COLUMN sbnweb.tra_sez_acq_storico.id_sez_acquis_bibliografiche IS 'identificativo della sezione d''acquisto';
COMMENT ON COLUMN sbnweb.tra_sez_acq_storico.data_var_bdg IS 'data di variazione del budget della sezione d''acquisizione';
COMMENT ON COLUMN sbnweb.tra_sez_acq_storico.budget_old IS 'valore del budget precedente alla variazione';
COMMENT ON COLUMN sbnweb.tra_sez_acq_storico.importo_diff IS 'differenza relativa di importo apportata al budget';
COMMENT ON TABLE sbnweb.tra_sez_acquisizione_fornitori IS 'Sezioni d''acquisizione di fornitori';
COMMENT ON COLUMN sbnweb.tra_sez_acquisizione_fornitori.cd_polo IS 'codice identificativo del polo';
COMMENT ON COLUMN sbnweb.tra_sez_acquisizione_fornitori.cd_biblioteca IS 'codice identificativo della biblioteca';
COMMENT ON COLUMN sbnweb.tra_sez_acquisizione_fornitori.cod_prac IS 'codice identificativo del profilo d''acquisto';
COMMENT ON COLUMN sbnweb.tra_sez_acquisizione_fornitori.cod_fornitore IS 'codice identificativo del fornitore';
COMMENT ON COLUMN sbnweb.tra_sez_acquisizione_fornitori.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.tra_sez_acquisizione_fornitori.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.trc_formati_sezioni IS 'FORMATI DEFINITI PER LA SEZIONE (solo per sezioni a formato)';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.cd_formato IS 'codice del formato del volume';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.cd_polo_sezione IS 'identificativo del polo della sezione di collocazione';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.cd_bib_sezione IS 'identificativo del polo della sezione di collocazione';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.cd_sezione IS 'nome della sezione di collocazione';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.prog_serie IS 'ultimo progressivo della serie per collocazione a formato';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.prog_num IS 'ultimo progressivo di collocazione interno alla serie';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.descr IS 'descrizione del formato del volume';
COMMENT ON COLUMN sbnweb.trc_formati_sezioni.n_pezzi IS 'numero massimo di pezzi collocabili all''interno della serie';
COMMENT ON TABLE sbnweb.trc_poss_prov_inventari IS 'POSSESSORI E PROVENIENZE INVENTARI (solo libro antico)';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.pid IS 'codice identificativo del possessore';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.cd_inven IS 'numero di inventario del documento';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.cd_serie IS 'codice identificativo della serie inventariale';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.cd_biblioteca IS 'codice identificativo della biblioteca della serie inventariale';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.cd_polo IS 'codice identificativo del polo della serie inventariale';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.cd_legame IS 'esplicita il legame tra inventario e proprietario; r = provenienza, p = possessore ';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.nota IS 'nota sul legame';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.trc_poss_prov_inventari.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.trc_possessori_possessori IS 'COLLEGAMENTI TRA POSSESSORI';
COMMENT ON COLUMN sbnweb.trc_possessori_possessori.pid_base IS 'codice identificativo del possessore';
COMMENT ON COLUMN sbnweb.trc_possessori_possessori.pid_coll IS 'codice identificativo del possessore collegato';
COMMENT ON COLUMN sbnweb.trc_possessori_possessori.tp_legame IS 'codice del collegamento tra possessori';
COMMENT ON COLUMN sbnweb.trc_possessori_possessori.nota IS 'nota al legame';
COMMENT ON COLUMN sbnweb.trc_possessori_possessori.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.trc_possessori_possessori.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.trc_possessori_possessori.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.trc_possessori_possessori.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.trc_possessori_possessori.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.trf_attivita_affiliate IS 'Lista delle funzioni demandate da una o piu'' biblioteche del polo (Bib. affiliate) ad altra biblioteca dello stesso polo (Bib. Centro di Sistema) ';
COMMENT ON COLUMN sbnweb.trf_attivita_affiliate.cd_attivita IS 'Codice dell''attivita'' demandata dalla biblioteca affiliata alla biblioteca CdS';
COMMENT ON COLUMN sbnweb.trf_attivita_affiliate.cd_bib_affiliata IS 'Codice della biblioteca affiliata';
COMMENT ON COLUMN sbnweb.trf_attivita_affiliate.cd_polo_bib_affiliata IS 'Codice del polo cui appartiene la biblioteca affiliata';
COMMENT ON COLUMN sbnweb.trf_attivita_affiliate.cd_bib_centro_sistema IS 'Codice della biblioteca Centro di Sistema';
COMMENT ON COLUMN sbnweb.trf_attivita_affiliate.cd_polo_bib_centro_sistema IS 'Codice del polo cui appartiene la biblioteca CdS';
COMMENT ON COLUMN sbnweb.trf_attivita_affiliate.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.trf_attivita_affiliate.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.trf_attivita_affiliate.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.trf_attivita_affiliate.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.trf_attivita_affiliate.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.trf_attivita_polo IS ' ';
COMMENT ON TABLE sbnweb.trf_funzioni_denominazioni IS 'DESCRIZIONI IN LINGUA DELLE FUNZIONI GESTITE DALL''APPLICATIVO (--)';
COMMENT ON COLUMN sbnweb.trf_funzioni_denominazioni.ds_nome IS 'Descrizione estesa, nella lingua indicata dal relativo codice, della funzione codificata';
COMMENT ON COLUMN sbnweb.trf_funzioni_denominazioni.ds_nome_breve IS 'Descrizione breve, nella lingua indicata dal relativo codice, della funzione codificata';
COMMENT ON COLUMN sbnweb.trf_funzioni_denominazioni.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.trf_funzioni_denominazioni.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.trf_funzioni_denominazioni.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.trf_funzioni_denominazioni.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.trf_funzioni_denominazioni.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.trf_gruppo_attivita IS ' ';
COMMENT ON TABLE sbnweb.trf_gruppo_attivita_polo IS ' ';
COMMENT ON COLUMN sbnweb.trf_gruppo_attivita_polo.fl_include IS 'Flag di inclusione od esclusione (S include, N esclude)';
COMMENT ON TABLE sbnweb.trf_profilo_biblioteca IS 'FUNZIONI ASSOCIATE AI PROFILI DI ABILITAZIONE (TLFPFU)';
COMMENT ON COLUMN sbnweb.trf_profilo_biblioteca.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.trf_profilo_biblioteca.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.trf_profilo_biblioteca.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.trf_profilo_biblioteca.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.trf_profilo_biblioteca.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.trf_profilo_profilo IS ' ';
COMMENT ON TABLE sbnweb.trf_utente_professionale_biblioteca IS ' ';
COMMENT ON COLUMN sbnweb.trf_utente_professionale_biblioteca.tp_ruolo IS 'null = dipendente della biblioteca admin = dipendente della biblioteca con diritti di amministratore semplice super Admin = dipendente della biblioteca con diritti amministratore con tutti i privilegi';
COMMENT ON TABLE sbnweb.trf_utente_professionale_polo IS ' ';
COMMENT ON TABLE sbnweb.trl_attivita_bibliotecario IS 'Attivita'' del bibliotecario';
COMMENT ON COLUMN sbnweb.trl_attivita_bibliotecario.id_bibliotecario IS 'codice identificativo del bibliotecario';
COMMENT ON COLUMN sbnweb.trl_attivita_bibliotecario.id_iter_servizio IS 'Identificativo dell''iter del servizio';
COMMENT ON COLUMN sbnweb.trl_attivita_bibliotecario.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.trl_attivita_bibliotecario.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.trl_autorizzazioni_servizi IS 'AUTORIZZAZIONI SERVIZI UTENTE';
COMMENT ON COLUMN sbnweb.trl_autorizzazioni_servizi.id_tipi_autorizzazione IS 'Identificativo del tipo autorizzazione';
COMMENT ON COLUMN sbnweb.trl_autorizzazioni_servizi.id_servizio IS 'Identificativo del servizio';
COMMENT ON COLUMN sbnweb.trl_autorizzazioni_servizi.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.trl_autorizzazioni_servizi.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.trl_autorizzazioni_servizi.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.trl_autorizzazioni_servizi.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.trl_autorizzazioni_servizi.fl_canc IS 'Flag di cancellazione logica valori ammessi S/N';
COMMENT ON TABLE sbnweb.trl_diritti_utente IS 'Diritti dell''utente';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.id_utenti IS 'Identificativo dell''utente';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.id_servizio IS 'Identificativo del servizio';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.data_inizio_serv IS 'data di inizio validita'' del servizio';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.data_fine_serv IS 'data di fine  validita'' del servizio';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.data_inizio_sosp IS 'data di inizio della sospensione'' del servizio';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.data_fine_sosp IS 'data fine della sospensione del servizio';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.note IS 'note relative al diritto dell''utente';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.fl_canc IS 'Flag di cancellazione logica valori ammessi S/N';
COMMENT ON COLUMN sbnweb.trl_diritti_utente.cod_tipo_aut IS 'codice autorizzazione da cui e'' stato ereditato il diritto';
COMMENT ON TABLE sbnweb.trl_materie_utenti IS 'Materia d''interesse degli utenti';
COMMENT ON COLUMN sbnweb.trl_materie_utenti.id_utenti IS 'Identificativo dell''utente';
COMMENT ON COLUMN sbnweb.trl_materie_utenti.id_materia IS 'Identificativo della materia d''interesse';
COMMENT ON COLUMN sbnweb.trl_materie_utenti.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.trl_materie_utenti.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.trl_posti_sala_utenti_biblioteca IS ' ';
COMMENT ON TABLE sbnweb.trl_relazioni_servizi IS 'Tabella che mette in relazione le tabelle dei servizi con le tabelle dei codici';
COMMENT ON TABLE sbnweb.trl_supporti_modalita_erogazione IS ' ';
COMMENT ON COLUMN sbnweb.trl_supporti_modalita_erogazione.cod_erog IS 'Identificativo della modalita'' di erogazione (tabella codici ''LMER'')';
COMMENT ON COLUMN sbnweb.trl_supporti_modalita_erogazione.id_supporti_biblioteca IS 'Identificativo del supporto in biblioteca';
COMMENT ON TABLE sbnweb.trl_tariffe_modalita_erogazione IS 'Tariffe delle modalita'' di erogazione';
COMMENT ON COLUMN sbnweb.trl_tariffe_modalita_erogazione.cod_erog IS 'codice della modalita'' di erogazione (tabella codici ''LMER'')';
COMMENT ON COLUMN sbnweb.trl_tariffe_modalita_erogazione.id_tipo_servizio IS 'Identificativo del tipo servizio';
COMMENT ON COLUMN sbnweb.trl_tariffe_modalita_erogazione.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.trl_tariffe_modalita_erogazione.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON TABLE sbnweb.trl_utenti_biblioteca IS 'Utenti di biblioteca';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.id_utenti_biblioteca IS 'Identificativo univoco dell''utente in biblioteca';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.id_utenti IS 'Identificativo dell''utente';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.cd_biblioteca IS 'Codice della biblioteca';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.id_specificita_titoli_studio IS 'Identificativo del titolo di studio';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.id_occupazioni IS 'Identificativo dell''occupazione';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.credito_bib IS 'credito a disposizione dell''utente per i servizi di biblioteca';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.note_bib IS 'note della biblioteca riguardanti l''utente';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.data_inizio_aut IS 'data d''inizio dell''autorizzazione concessa all''utente';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.data_fine_aut IS 'data di scadenza dell''autorizzazione concessa all''utente';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.data_inizio_sosp IS 'data di inizio della sospensione dell''utente. la sospensione a questo livello disabilita tutti i servizi.';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.data_fine_sosp IS 'data di scadenza  della sospensione  dell''utente. la scadenza di tale sospensione riabilita l''utente a tutti i servizi';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.cod_tipo_aut IS 'codice identificativo del tipo autorizzazione';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.ute_var IS 'utente che varia';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.trl_utenti_biblioteca.fl_canc IS 'Flag di cancellazione logica valori ammessi S/N';
COMMENT ON TABLE sbnweb.trp_messaggio_fascicolo IS 'Comunicazioni a fornitori relative a fascicoli: registra la relazione tra la comunicazione al fornitore e il fascicolo di periodico cui la comunicazione si riferisce';
COMMENT ON COLUMN sbnweb.trp_messaggio_fascicolo.cd_polo IS 'codice polo della biblioteca che ha inviato il messaggio';
COMMENT ON COLUMN sbnweb.trp_messaggio_fascicolo.cd_bib IS 'codice identificativo della biblioteca che ha inviato il messaggio';
COMMENT ON COLUMN sbnweb.trp_messaggio_fascicolo.cd_msg IS 'identificativo della comunicazione';
COMMENT ON COLUMN sbnweb.trp_messaggio_fascicolo.bid IS 'BID del periodico cui si riferisce il fascicolo';
COMMENT ON COLUMN sbnweb.trp_messaggio_fascicolo.fid IS 'progressivo identificativo del fascicolo cui si riferisce la comunicazione al fornitore';
COMMENT ON COLUMN sbnweb.trp_messaggio_fascicolo.id_ordine IS 'codice identificativo dell''ordine di acquisizione';
COMMENT ON COLUMN sbnweb.trp_messaggio_fascicolo.ute_ins IS 'utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.trp_messaggio_fascicolo.ts_ins IS 'data e ora d''inserimento';
COMMENT ON COLUMN sbnweb.trp_messaggio_fascicolo.ute_var IS 'utente che ha effettuato l''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.trp_messaggio_fascicolo.ts_var IS 'data e ora dell''ultimo aggiornamento';
COMMENT ON COLUMN sbnweb.trp_messaggio_fascicolo.fl_canc IS 'indicatore di cancellazione logica';
COMMENT ON TABLE sbnweb.trs_termini_titoli_biblioteche IS 'TERMINI DI THESAURO DI OGGETTI BIBLIOGRAFICI IN BIBLIOTECA (TPSDTB))';
COMMENT ON COLUMN sbnweb.trs_termini_titoli_biblioteche.bid IS 'codice identificativo dell''oggetto bibliografico';
COMMENT ON COLUMN sbnweb.trs_termini_titoli_biblioteche.cd_biblioteca IS 'Codice della biblioteca';
COMMENT ON COLUMN sbnweb.trs_termini_titoli_biblioteche.cd_the IS 'Codice del Thesauro';
COMMENT ON COLUMN sbnweb.trs_termini_titoli_biblioteche.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.trs_termini_titoli_biblioteche.did IS 'Identificativo del termine';
COMMENT ON COLUMN sbnweb.trs_termini_titoli_biblioteche.nota_termine_titoli_biblioteca IS 'nota al collegamento tra l''oggetto bibliografico e il termine di thesauro';
COMMENT ON COLUMN sbnweb.trs_termini_titoli_biblioteche.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.trs_termini_titoli_biblioteche.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.trs_termini_titoli_biblioteche.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.trs_termini_titoli_biblioteche.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.trs_termini_titoli_biblioteche.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.ts_link_multim IS ' Dati per l''aggancio delle risorse digitali ad un oggetto bibliografico';
COMMENT ON COLUMN sbnweb.ts_link_multim.id_link_multim IS 'Identificativo della relazione';
COMMENT ON COLUMN sbnweb.ts_link_multim.ky_link_multim IS 'Identificativo dell''oggetto bibliografico cui si riferisce la risorsa digitale';
COMMENT ON COLUMN sbnweb.ts_link_multim.uri_multim IS 'URI alla risorsa digitale';
COMMENT ON COLUMN sbnweb.ts_link_multim.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.ts_link_multim.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.ts_link_multim.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.ts_link_multim.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.ts_link_multim.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.ts_link_multim.immagine IS 'Identificativo dell''immagine nei casi in cui e'' registrata direttamente nel DataBase';
COMMENT ON TABLE sbnweb.ts_note_proposta IS ' ';
COMMENT ON TABLE sbnweb.ts_proposta IS ' ';
COMMENT ON TABLE sbnweb.ts_proposta_marc IS ' ';
COMMENT ON TABLE sbnweb.ts_servizio IS ' ';
COMMENT ON TABLE sbnweb.ts_stop_list IS ' Lista delle parole da ignorare nel calcolo delle chiavi CLES';
COMMENT ON COLUMN sbnweb.ts_stop_list.id_stop_list IS 'Identificativo dell''elemento della lista';
COMMENT ON COLUMN sbnweb.ts_stop_list.tp_stop_list IS 'Tipo di elemento: ''A'' articolo, ''F'' forme, ''U'' ,''D'' ,''E'' ';
COMMENT ON COLUMN sbnweb.ts_stop_list.cd_lingua IS 'Codice della lingua dell''elemento';
COMMENT ON COLUMN sbnweb.ts_stop_list.parola IS 'Parola di cui e'' costituito l''elemento';
COMMENT ON COLUMN sbnweb.ts_stop_list.nota_stop_list IS 'Eventuale nota eplicativa';
COMMENT ON COLUMN sbnweb.ts_stop_list.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.ts_stop_list.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.ts_stop_list.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.ts_stop_list.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.ts_stop_list.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.import1 IS 'Tabella per il caricamento dei file unimarc per la funzione di import';
COMMENT ON COLUMN sbnweb.import1.id IS 'progressivo identificativo del record nella tabella import1';
COMMENT ON COLUMN sbnweb.import1.id_input IS 'identificativo della documento (bid o equivalente)';
COMMENT ON COLUMN sbnweb.import1.leader IS 'dati relativi  al record guida';
COMMENT ON COLUMN sbnweb.import1.tag IS 'tipo di etichetta';
COMMENT ON COLUMN sbnweb.import1.indicatore1 IS 'primo indicatore  etichetta unimarc ';
COMMENT ON COLUMN sbnweb.import1.indicatore2 IS 'secondo indicatore etichetta unimarc ';
COMMENT ON COLUMN sbnweb.import1.id_link IS 'identificativo della notizia,autore,soggetto,ecc legata al documento';
COMMENT ON COLUMN sbnweb.import1.dati IS 'dati relativi alla specifica etichetta';
COMMENT ON COLUMN sbnweb.import1.nr_richiesta IS 'numero della prenotazione  dell import Unimarc';
COMMENT ON COLUMN sbnweb.import1.stato_id_input IS 'P=Presente; I=Da caricare/inserire; C=Da catturare;';
COMMENT ON COLUMN sbnweb.import1.stato_tag IS 'numero della prenotazione  dell import Unimarc';
COMMENT ON TABLE sbnweb.import950 IS 'Tabella per il caricamento delle Etichette 950 (inventari-collocazioni-esemplari)';
COMMENT ON COLUMN sbnweb.import950.id IS 'progressivo identificativo del record nella tabella import950';
COMMENT ON COLUMN sbnweb.import950.id_import1 IS 'progressivo identificativo del record nella tabella import1';
COMMENT ON COLUMN sbnweb.import950.id_input IS 'identificativo della documento (bid o equivalente)';
COMMENT ON COLUMN sbnweb.import950.dati IS 'dati relativi alla etichetta 950';
COMMENT ON COLUMN sbnweb.import950.error IS 'indicatore dell errore nell elaborazione del record';
COMMENT ON COLUMN sbnweb.import950.msg_error IS 'descrizione dell errore nell elaborazione del record';
COMMENT ON COLUMN sbnweb.import950.nr_richiesta_import IS 'numero della prenotazione  dell import Unimarc';
COMMENT ON COLUMN sbnweb.import950.nr_richiesta IS 'numero della prenotazione  della elaborazione delle etichette 950';
COMMENT ON TABLE sbnweb.import_id_link IS 'Tabella di corrispondenza Identificativo unimarc - BID ';
COMMENT ON COLUMN sbnweb.import_id_link.id_record IS 'Identificativo di sistema del record';
COMMENT ON COLUMN sbnweb.import_id_link.id_input IS 'Identificativo del documento sul file UNIMARC (parte dati del tag 001 o descrizione utilizzata per il confronto con le notizie dello stesso tipo nello stesso file)';
COMMENT ON COLUMN sbnweb.import_id_link.id_inserito IS 'BID/VID/MID assegnato all''atto dell''inserimento sul DB di polo';
COMMENT ON COLUMN sbnweb.import_id_link.fl_stato IS 'esito dela ricerca per BID sul db di POLO';
COMMENT ON COLUMN sbnweb.import_id_link.ute_ins IS 'utente che ha attivato l''elaborazione differita';
COMMENT ON COLUMN sbnweb.import_id_link.ts_ins IS 'time stamp di inserimento del record';
COMMENT ON COLUMN sbnweb.import_id_link.nr_richiesta IS 'n_ro della richiesta che ha caricato in import1 il file UNIMARC in cui e'' presente la notizia';
COMMENT ON TABLE sbnweb.tb_report_indice IS 'Tabella per il caricamento delle liste di corrispondenza prodotte da Import Indice';
COMMENT ON COLUMN sbnweb.tb_report_indice.id IS 'Identificativo numerico progressivo (sequence)';
COMMENT ON COLUMN sbnweb.tb_report_indice.nome_lista IS 'Nome lista restituito dalle funzioni di confronto dall''Indice';
COMMENT ON COLUMN sbnweb.tb_report_indice.fl_canc IS 'Flag cancellazione logica lista (S/N)';
COMMENT ON COLUMN sbnweb.tb_report_indice.data_prod_lista IS 'data di caricamento della lista nel sistema di polo';
COMMENT ON TABLE sbnweb.tb_report_indice_id_locali IS 'Tabella degli identificativi inviati all''Indice per la procedura di Import ';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_locali.id IS 'Identificativo numerico progressivo (sequence)';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_locali.id_lista IS 'Identificativo lista (foreign-Key su tb_report_indice - ID)';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_locali.id_oggetto_locale IS 'Identificativo oggetto locale';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_locali.risultato_confronto IS 'Risultato confronto con Indice (S=simile,U=uguale, N=nuovo)';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_locali.stato_lavorazione IS 'Stato di lavorazione dell''oggetto (1=da trattare,2=in trattamento,3=escluso,4=trattato)';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_locali.tipo_trattamento_selezionato IS 'Tipo di trattamento effettuato sull''oggetto (1=fusione on line,2=fusione batch, 3=catalogato in Indice)';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_locali.id_arrivo_fusione IS 'Identificativo oggetto condiviso selezionato da lista o altra modalita'' scelto per arrivo Fusione';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_locali.fl_canc IS 'Flag di cancellazione (S/N)';
COMMENT ON TABLE sbnweb.tb_report_indice_id_arrivo IS 'Tabella delle corrispondenze tra indentificativi inviati all''Indice per la procedura di Import e Bid di Indice ';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_arrivo.id IS 'Identificativo numerico progressivo (sequence)';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_arrivo.id_lista_ogg_loc IS 'id del record relativo al documento confrontato nella lista principale (tb_report_indice)';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_arrivo.id_oggetto_locale IS 'Identificativo oggetto locale (foreign-Key su tb_report_indice_id_locali - idOggettoLocale)';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_arrivo.id_arrivo_fusione IS 'Identificativo oggetto condiviso proposto dall''indice';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_arrivo.tipologia_uguaglianza IS 'Risultato confronto con Indice (S=simile, U=uguale)';
COMMENT ON COLUMN sbnweb.tb_report_indice_id_arrivo.fl_canc IS 'Flag di cancellazione (S/N)';
COMMENT ON TABLE sbnweb.tbp_modello_previsionale IS 'Non utilizzata in attesa di completamento evolutiva';
COMMENT ON COLUMN sbnweb.tra_ordine_carrello_spedizione.id_ordine IS 'identificativo dell''ordine di tipo rilegatura';
COMMENT ON COLUMN sbnweb.tra_ordine_carrello_spedizione.dt_spedizione IS 'Data di spedizione al fornitore';
COMMENT ON COLUMN sbnweb.tra_ordine_carrello_spedizione.prg_spedizione IS 'Progressivo della spedizione';
COMMENT ON COLUMN sbnweb.tra_ordine_carrello_spedizione.cart_name IS 'Identificativo del carrello per Google';
COMMENT ON TABLE sbnweb.tr_editore_titolo IS 'Legami espliciti Editore-Titolo';
COMMENT ON COLUMN sbnweb.tr_editore_titolo.cod_fornitore IS 'Codice identificativo del fornitore';
COMMENT ON COLUMN sbnweb.tr_editore_titolo.bid IS 'Codice identificativo SBN del titolo';
COMMENT ON COLUMN sbnweb.tr_editore_titolo.nota_legame IS 'Nota al legame editore-titolo';
COMMENT ON COLUMN sbnweb.tr_editore_titolo.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tr_editore_titolo.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tr_editore_titolo.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tr_editore_titolo.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tr_editore_titolo.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON TABLE sbnweb.tbr_editore_num_std IS 'Numeri Standard per Editore';
COMMENT ON COLUMN sbnweb.tbr_editore_num_std.cod_fornitore IS 'Codice identificativo del fornitore';
COMMENT ON COLUMN sbnweb.tbr_editore_num_std.numero_std IS 'Numero standard - Prot. SBN=10 chr.';
COMMENT ON COLUMN sbnweb.tbr_editore_num_std.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tbr_editore_num_std.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tbr_editore_num_std.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tbr_editore_num_std.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tbr_editore_num_std.fl_canc IS 'Flag di cancellazione logica';
COMMENT ON COLUMN sbnweb.tb_loc_indice.cd_polo IS 'Codice del polo';
COMMENT ON COLUMN sbnweb.tb_loc_indice.cd_biblioteca IS 'Codice della biblioteca';
