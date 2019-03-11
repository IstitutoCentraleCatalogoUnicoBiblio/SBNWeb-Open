-- SQL Manager 2007 for PostgreSQL 4.3.0.4 
-- ---------------------------------------
-- Host      : XXX.XXX.XXX.XXX
-- Database  : sbnwebDbXXXese
-- Version   : PostgreSQL 8.3.5 on x86_64-unknown-linux-gnu, compiled by GCC gcc (GCC) 4.1.2 20080704 (Red Hat 4.1.2-46)

CREATE SCHEMA sbnweb AUTHORIZATION sbnweb;

SET search_path = sbnweb, pg_catalog;

--
-- Definition for language plpgsql () : 
--
CREATE TRUSTED PROCEDURAL LANGUAGE plpgsql
   HANDLER "plpgsql_call_handler"
;
--
-- Definition for function isdigit (OID = 60048) : 
--
SET search_path = sbnweb, pg_catalog;
CREATE FUNCTION sbnweb.isdigit (text) RETURNS boolean
AS 
$body$
select $1 ~ '^(-)?[0-9]+$' as result
$body$
    LANGUAGE sql;
--
-- Definition for function percento (OID = 60049) : 
--
CREATE FUNCTION sbnweb.percento (bigint, bigint) RETURNS numeric
AS 
$body$
select CAST(CAST ($1 AS DECIMAL) / CAST ($2 AS DECIMAL) * 100  as numeric(10,2));
$body$
    LANGUAGE sql STRICT;
--
-- Definition for function tb_autore_tsvector (OID = 60050) : 
--
CREATE FUNCTION sbnweb.tb_autore_tsvector () RETURNS trigger
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
CREATE FUNCTION sbnweb.tb_fascicolo_anno_pub () RETURNS trigger
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
CREATE FUNCTION sbnweb.tbc_poss_prov_tidx () RETURNS trigger
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
CREATE FUNCTION sbnweb.listaidautori (flcon character, datad timestamp with time zone, dataa timestamp with time zone, lettd text, letta text, liv_autd character, liv_auta character, tp_nome character) RETURNS SETOF text
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
CREATE FUNCTION sbnweb.estrai_area_da_isbd (isbd_completo character varying, indice_aree character varying, tag_area character varying) RETURNS character varying
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
CREATE FUNCTION sbnweb.cerca_titolo_superiore (id_input character varying, nro_char integer) RETURNS character varying
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
CREATE FUNCTION sbnweb.concatena_aree (isbd character varying, indice_isbd character varying, tags character varying[], max_char_tit integer) RETURNS character varying
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
CREATE FUNCTION sbnweb.concatena_aree (isbd character varying, indice_isbd character varying, tags character varying[]) RETURNS character varying
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
CREATE FUNCTION sbnweb.concatena_aree (isbd character varying, indice_isbd character varying, tags character varying[], id_titolo character varying, pos_titolo integer, label_ini character varying, label_end character varying, nro_char_tit integer) RETURNS character varying
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
-- Definition for function calcola_import_id_link (OID = 265991) : 
--
CREATE FUNCTION sbnweb.calcola_import_id_link (import1_tag character varying, import1_dati text) RETURNS character varying
AS 
$body$
DECLARE
BEGIN
return  calcola_import_id_link ($1, $2, '');
END
$body$
    LANGUAGE plpgsql;
--
-- Structure for table jms_messages (OID = 58393) : 
--
CREATE TABLE sbnweb.jms_messages (
    messageid integer NOT NULL,
    destination character varying(150) NOT NULL,
    txid integer,
    txop character(1),
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
    bid character(10) NOT NULL,
    ds_abstract character varying(2160) NOT NULL,
    cd_livello character(2) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_arte_tridimens (OID = 58414) : 
--
CREATE TABLE sbnweb.tb_arte_tridimens (
    bid character(10) NOT NULL,
    cd_designazione character(2) NOT NULL,
    tp_materiale_1 character(2),
    tp_materiale_2 character(2),
    tp_materiale_3 character(2),
    cd_colore character(1),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_audiovideo (OID = 58417) : 
--
CREATE TABLE sbnweb.tb_audiovideo (
    bid character(10) NOT NULL,
    tp_mater_audiovis character(1) NOT NULL,
    lunghezza character(50) NOT NULL,
    cd_colore character(1) NOT NULL,
    cd_suono character(1) NOT NULL,
    tp_media_suono character(1),
    cd_dimensione character(1),
    cd_forma_video character(1),
    cd_tecnica character(1),
    tp_formato_film character(1),
    cd_mat_accomp character(4),
    cd_forma_regist character(1),
    tp_formato_video character(1),
    cd_materiale_base character(1),
    cd_supporto_second character(1),
    cd_broadcast character(1),
    tp_generazione character(1),
    cd_elementi character(1),
    cd_categ_colore character(1),
    cd_polarita character(1),
    cd_pellicola character(1),
    tp_suono character(1),
    tp_stampa_film character(1),
    cd_deteriore character(1),
    cd_completo character(1),
    dt_ispezione timestamp(6) without time zone,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_autore (OID = 58420) : 
--
CREATE TABLE sbnweb.tb_autore (
    vid character(10) NOT NULL,
    isadn character(30),
    tp_forma_aut character(1) NOT NULL,
    ky_cautun character(6) NOT NULL,
    ky_auteur character(10) NOT NULL,
    ky_el1_a character(3),
    ky_el1_b character(3),
    ky_el2_a character(3),
    ky_el2_b character(3),
    tp_nome_aut character(1) NOT NULL,
    ky_el3 character(6),
    ky_el4 character(6),
    ky_el5 character(6),
    ky_cles1_a character(50) NOT NULL,
    ky_cles2_a character(30),
    cd_paese character(2),
    cd_lingua character(3),
    aa_nascita character(4),
    aa_morte character(4),
    cd_livello character(2) NOT NULL,
    fl_speciale character(1) NOT NULL,
    ds_nome_aut character varying(320) NOT NULL,
    cd_agenzia character(6),
    cd_norme_cat character varying(10),
    nota_aut character varying(320),
    nota_cat_aut character varying(1920),
    vid_link character(10),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    ute_forza_ins character(12),
    ute_forza_var character(12),
    fl_canc character(1) NOT NULL,
    fl_condiviso character(1) DEFAULT '''S''::bpchar'::bpchar NOT NULL,
    ute_condiviso character(12),
    ts_condiviso timestamp(6) without time zone,
    tidx_vector tsvector
) WITHOUT OIDS;
--
-- Structure for table tb_cartografia (OID = 58427) : 
--
CREATE TABLE sbnweb.tb_cartografia (
    bid character(10) NOT NULL,
    cd_livello character(2) NOT NULL,
    tp_pubb_gov character(1),
    cd_colore character(1),
    cd_meridiano character(2),
    cd_supporto_fisico character(2),
    cd_tecnica character(1),
    cd_forma_ripr character(1),
    cd_forma_pubb character(1),
    cd_altitudine character(1),
    cd_tiposcala character(1),
    tp_scala character(1),
    scala_oriz character varying(10),
    scala_vert character varying(10),
    longitudine_ovest character varying(8),
    longitudine_est character varying(8),
    latitudine_nord character varying(8),
    latitudine_sud character varying(8),
    tp_immagine character(1),
    cd_forma_cart character(1),
    cd_piattaforma character(1),
    cd_categ_satellite character(1),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_classe (OID = 58430) : 
--
CREATE TABLE sbnweb.tb_classe (
    cd_sistema character(3) NOT NULL,
    cd_edizione character(2) NOT NULL,
    classe character(31) NOT NULL,
    ds_classe character varying(240),
    cd_livello character(2) NOT NULL,
    fl_costruito character(1),
    fl_speciale character(1) NOT NULL,
    ky_classe_ord character(34),
    suffisso character varying(10),
    ult_term character varying(240),
    fl_condiviso character(1) DEFAULT '''N''::bpchar'::bpchar NOT NULL,
    ute_condiviso character(12),
    ts_condiviso timestamp(6) without time zone,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    tidx_vector tsvector
) WITHOUT OIDS;
--
-- Structure for table tb_codici (OID = 58437) : 
--
CREATE TABLE sbnweb.tb_codici (
    tp_tabella character(4) NOT NULL,
    cd_tabella character(14) NOT NULL,
    cd_valore character(10),
    ds_tabella character varying(160) NOT NULL,
    cd_unimarc character varying(10),
    cd_marc_21 character varying(10),
    tp_materiale character(1),
    dt_fine_validita date,
    ds_cdsbn_ulteriore character varying(255),
    cd_flg1 character varying(255),
    cd_flg2 character varying(255),
    cd_flg3 character varying(255),
    cd_flg4 character varying(255),
    cd_flg5 character varying(255),
    cd_flg6 character varying(255),
    cd_flg7 character varying(255),
    cd_flg8 character varying(255),
    cd_flg9 character varying(255),
    cd_flg10 character varying(255),
    cd_flg11 character varying(255)
) WITHOUT OIDS;
--
-- Structure for table tb_composizione (OID = 58443) : 
--
CREATE TABLE sbnweb.tb_composizione (
    bid character(10) NOT NULL,
    cd_forma_1 character(4),
    cd_forma_2 character(4),
    cd_forma_3 character(4),
    numero_ordine character(20),
    numero_opera character(20),
    numero_cat_tem character(20),
    cd_tonalita character(2),
    datazione character(10),
    aa_comp_1 character(4),
    aa_comp_2 character(4),
    ds_sezioni character(20),
    ky_ord_ric character(10),
    ky_est_ric character(10),
    ky_app_ric character(10),
    ky_ord_clet character(6),
    ky_est_clet character(6),
    ky_app_clet character(6),
    ky_ord_pre character(20),
    ky_est_pre character(20),
    ky_app_pre character(20),
    ky_ord_den character varying(234),
    ky_est_den character varying(234),
    ky_app_den character varying(234),
    ky_ord_nor_pre character varying(240),
    ky_est_nor_pre character varying(240),
    ky_app_nor_pre character varying(240),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_descrittore (OID = 58449) : 
--
CREATE TABLE sbnweb.tb_descrittore (
    did character(10) NOT NULL,
    ds_descrittore character varying(240) NOT NULL,
    ky_norm_descritt character varying(240) NOT NULL,
    nota_descrittore character varying(240),
    cd_soggettario character(3) NOT NULL,
    tp_forma_des character(1) NOT NULL,
    cd_livello character(2) NOT NULL,
    fl_condiviso character(1) DEFAULT '''N''::bpchar'::bpchar NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    tidx_vector tsvector,
    cd_edizione character(1),
    cat_termine character(1)
) WITHOUT OIDS;
--
-- Structure for table tb_disco_sonoro (OID = 58456) : 
--
CREATE TABLE sbnweb.tb_disco_sonoro (
    bid character(10) NOT NULL,
    cd_forma character(1) NOT NULL,
    cd_velocita character(1),
    tp_suono character(1),
    cd_pista character(1),
    cd_dimensione character(1),
    cd_larg_nastro character(1),
    cd_configurazione character(1),
    cd_mater_accomp character(6),
    cd_tecnica_regis character(1),
    cd_riproduzione character(1),
    tp_disco character(1),
    tp_taglio character(1),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_grafica (OID = 58459) : 
--
CREATE TABLE sbnweb.tb_grafica (
    bid character(10) NOT NULL,
    cd_livello character(2) NOT NULL,
    tp_materiale_gra character(1),
    cd_supporto character(1),
    cd_colore character(1),
    cd_tecnica_dis_1 character(2),
    cd_tecnica_dis_2 character(2),
    cd_tecnica_dis_3 character(2),
    cd_tecnica_sta_1 character(2),
    cd_tecnica_sta_2 character(2),
    cd_tecnica_sta_3 character(2),
    cd_design_funz character(2),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_impronta (OID = 58462) : 
--
CREATE TABLE sbnweb.tb_impronta (
    bid character(10) NOT NULL,
    impronta_1 character(10) NOT NULL,
    impronta_2 character(14) NOT NULL,
    impronta_3 character(8) NOT NULL,
    nota_impronta character varying(80),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_incipit (OID = 58465) : 
--
CREATE TABLE sbnweb.tb_incipit (
    bid character(10) NOT NULL,
    numero_mov character(2) NOT NULL,
    numero_p_mov character(2) NOT NULL,
    bid_letterario character(10),
    tp_indicatore character(1),
    numero_comp character(2),
    registro_mus character(9),
    nome_personaggio character varying(40),
    tempo_mus character varying(40),
    cd_forma character(4),
    cd_tonalita character(2),
    chiave_mus character(3),
    alterazione character(8),
    misura character(9),
    ds_contesto character varying(160),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_luogo (OID = 58468) : 
--
CREATE TABLE sbnweb.tb_luogo (
    lid character(10) NOT NULL,
    tp_forma character(1) NOT NULL,
    cd_livello character(2) NOT NULL,
    ds_luogo character varying(80) NOT NULL,
    ky_luogo character(30) NOT NULL,
    ky_norm_luogo character(80) NOT NULL,
    cd_paese character(2),
    nota_luogo character varying(320),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone DEFAULT now() NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone DEFAULT now() NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_loc_indice
--
CREATE TABLE sbnweb.tb_loc_indice (
  id_loc SERIAL, 
  cd_polo character(3) NOT NULL, 
  cd_biblioteca character(3) NOT NULL, 
  bid character(10) NOT NULL, 
  fl_stato character(1) DEFAULT '0' NOT NULL, 
  tp_loc SMALLINT DEFAULT 0 NOT NULL, 
  sbnmarc_xml TEXT, 
  ute_ins character(12) NOT NULL, 
  ts_ins TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  ute_var character(12) NOT NULL, 
  ts_var TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
  fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_marca (OID = 58476) : 
--
CREATE TABLE sbnweb.tb_marca (
    mid character(10) NOT NULL,
    cd_livello character(2) NOT NULL,
    fl_speciale character(1) NOT NULL,
    ds_marca character varying(160) NOT NULL,
    nota_marca character varying(320),
    ds_motto character varying(80),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    fl_condiviso character(1) DEFAULT '''S''::bpchar'::bpchar NOT NULL,
    ute_condiviso character(12),
    ts_condiviso timestamp(6) without time zone,
    tidx_vector tsvector
) WITHOUT OIDS;
--
-- Structure for table tb_microforma (OID = 58483) : 
--
CREATE TABLE sbnweb.tb_microforma (
    bid character(10) NOT NULL,
    cd_designazione character(1) NOT NULL,
    cd_polarita character(1),
    cd_dimensione character(1),
    cd_riduzione character(1),
    cd_riduzione_spec character(3),
    cd_colore character(1),
    cd_emulsione character(1),
    cd_generazione character(1),
    cd_base character(1),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_musica (OID = 58486) : 
--
CREATE TABLE sbnweb.tb_musica (
    bid character(10) NOT NULL,
    cd_livello character(2) NOT NULL,
    ds_org_sint character varying(80),
    ds_org_anal character varying(320),
    tp_elaborazione character(1),
    cd_stesura character(1),
    fl_composito character(1) NOT NULL,
    fl_palinsesto character(1) NOT NULL,
    datazione character(10),
    cd_presentazione character(2),
    cd_materia character(1),
    ds_illustrazioni character varying(120),
    notazione_musicale character varying(120),
    ds_legatura character varying(60),
    ds_conservazione character varying(100),
    tp_testo_letter character(1),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_nota (OID = 58492) : 
--
CREATE TABLE sbnweb.tb_nota (
    bid character(10) NOT NULL,
    tp_nota character(4) NOT NULL,
    progr_nota bigint NOT NULL,
    ds_nota character varying(1920) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_numero_std (OID = 58498) : 
--
CREATE TABLE sbnweb.tb_numero_std (
    bid character(10) NOT NULL,
    tp_numero_std character(2) NOT NULL,
    numero_std character(25) NOT NULL,
	numero_lastra TYPE NUMERIC(25,0),
    cd_paese character(2),
    nota_numero_std character varying(30),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    mid character(10) NOT NULL,
    parola character(10) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence seq_tb_personaggio (OID = 58507) : 
--
CREATE SEQUENCE sbnweb.seq_tb_personaggio
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tb_personaggio (OID = 58509) : 
--
CREATE TABLE sbnweb.tb_personaggio (
    id_personaggio integer DEFAULT nextval('seq_tb_personaggio'::regclass) NOT NULL,
    bid character(10) NOT NULL,
    nome_personaggio character(25) NOT NULL,
    cd_timbro_vocale character(5),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_rappresent (OID = 58513) : 
--
CREATE TABLE sbnweb.tb_rappresent (
    bid character(10) NOT NULL,
    tp_genere character(1),
    aa_rapp character(5),
    ds_periodo character(15),
    ds_teatro character(30),
    ds_luogo_rapp character(30),
    ds_occasione character varying(138),
    nota_rapp character varying(138),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_sig_repertorio character varying(30) NOT NULL,
    ds_repertorio character varying(1200) NOT NULL,
    tp_repertorio character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    tidx_vector tsvector
) WITHOUT OIDS;
--
-- Structure for table tb_risorsa_elettr (OID = 58525) : 
--
CREATE TABLE sbnweb.tb_risorsa_elettr (
    bid character(10) NOT NULL,
    tp_file character(1) NOT NULL,
    cd_designazione character(1),
    cd_colore character(1),
    cd_dimensione character(1),
    cd_suono character(1),
    cd_bit_immagine character(3),
    cd_formato_file character(1),
    cd_qualita character(1),
    cd_origine character(1),
    cd_base character(1),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tb_soggetto (OID = 58528) : 
--
CREATE TABLE sbnweb.tb_soggetto (
    cid character(10) NOT NULL,
    cd_soggettario character(3) NOT NULL,
    ds_soggetto character varying(240) NOT NULL,
    fl_speciale character(1) NOT NULL,
    ky_cles1_s character varying(240) NOT NULL,
    ky_primo_descr character varying(240) NOT NULL,
    cat_sogg character(1) NOT NULL,
    cd_livello character(2) NOT NULL,
    fl_condiviso character(1) DEFAULT '''S''::bpchar'::bpchar NOT NULL,
    ute_condiviso character(12) NOT NULL,
    ts_condiviso timestamp(6) without time zone NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    ky_cles2_s character varying(40),
    tidx_vector tsvector,
    nota_soggetto character varying(320),
    cd_edizione character(1)
) WITHOUT OIDS;
--
-- Structure for table tb_stat_parameter (OID = 58535) : 
--
CREATE TABLE sbnweb.tb_stat_parameter (
    id_stat integer NOT NULL,
    nome character varying(50) NOT NULL,
    tipo character varying(50) NOT NULL,
    valore character varying(100) NOT NULL,
    etichetta_nome character varying(50) NOT NULL,
    obbligatorio character(1) DEFAULT 'S'::bpchar NOT NULL
);
--
-- Structure for table tb_termine_thesauro (OID = 58539) : 
--
CREATE TABLE sbnweb.tb_termine_thesauro (
    did character(10) NOT NULL,
    cd_the character(3) NOT NULL,
    ds_termine_thesauro character varying(240) NOT NULL,
    nota_termine_thesauro character varying(240),
    ky_termine_thesauro character varying(240) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    tp_forma_the character(1) NOT NULL,
    cd_livello character(2) NOT NULL,
    fl_condiviso character(1) NOT NULL,
    tidx_vector tsvector
) WITHOUT OIDS;
--
-- Structure for table tb_titolo (OID = 58545) : 
--
CREATE TABLE sbnweb.tb_titolo (
    bid character(10) NOT NULL,
    isadn character(30),
    tp_materiale character(1) NOT NULL,
    tp_record_uni character(1),
    cd_natura character(1) NOT NULL,
    cd_paese character(2),
    cd_lingua_1 character(3),
    cd_lingua_2 character(3),
    cd_lingua_3 character(3),
    aa_pubb_1 character(4),
    aa_pubb_2 character(4),
    tp_aa_pubb character(1),
    cd_genere_1 character(2),
    cd_genere_2 character(2),
    cd_genere_3 character(2),
    cd_genere_4 character(2),
    ky_cles1_t character(6) NOT NULL,
    ky_cles2_t character(44) NOT NULL,
    ky_clet1_t character(3) NOT NULL,
    ky_clet2_t character(3) NOT NULL,
    ky_cles1_ct character(6) NOT NULL,
    ky_cles2_ct character(44) NOT NULL,
    ky_clet1_ct character(3) NOT NULL,
    ky_clet2_ct character(3) NOT NULL,
    cd_livello character(2) NOT NULL,
    fl_speciale character(1) NOT NULL,
    isbd character varying(1200) NOT NULL,
    indice_isbd character varying(80) NOT NULL,
    ky_editore character varying(80),
    cd_agenzia character(6),
    cd_norme_cat character varying(10),
    nota_inf_tit character varying(320),
    nota_cat_tit character varying(320),
    bid_link character(10),
    tp_link character(1),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    ute_forza_ins character(12),
    ute_forza_var character(12),
    fl_canc character(1) NOT NULL,
    cd_periodicita character(1),
    fl_condiviso character(1) DEFAULT '''S''::bpchar'::bpchar NOT NULL,
    ute_condiviso character(12) NOT NULL,
    ts_condiviso timestamp(6) without time zone NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    buono_ord character(9) NOT NULL,
    cod_fornitore integer NOT NULL,
    stato_buono character(1) NOT NULL,
    data_buono date NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(12) NOT NULL,
    cod_mat character(1)
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    valuta character(3) NOT NULL,
    cambio numeric(10,6) NOT NULL,
    data_var date NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    anno_fattura numeric(4,0) NOT NULL,
    progr_fattura integer NOT NULL,
    num_fattura character(20) NOT NULL,
    data_fattura date NOT NULL,
    data_reg date NOT NULL,
    importo numeric(15,3) NOT NULL,
    sconto numeric(5,2) NOT NULL,
    valuta character(3) NOT NULL,
    cambio numeric(10,6) NOT NULL,
    stato_fattura character(1) NOT NULL,
    tipo_fattura character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_fornitore integer NOT NULL,
    bid_p character(10) NOT NULL,
    tip_rec character(1) NOT NULL,
    natura character(1) NOT NULL,
    paese character(3) NOT NULL,
    lingua character(3) NOT NULL,
    tipo_data character(1) NOT NULL,
    data1 character(4) NOT NULL,
    num_standard character(11) NOT NULL,
    aut1 character varying(160) NOT NULL,
    k_aut1 character varying(80) NOT NULL,
    tip_aut1 character(1) NOT NULL,
    resp_aut1 character(1) NOT NULL,
    aut2 character varying(160) NOT NULL,
    k_aut2 character varying(80) NOT NULL,
    tip_aut2 character(1) NOT NULL,
    resp_aut2 character(1) NOT NULL,
    aut3 character varying(160) NOT NULL,
    k_aut3 character varying(80) NOT NULL,
    tip_aut3 character(1) NOT NULL,
    resp_aut3 character(1) NOT NULL,
    aut4 character varying(160) NOT NULL,
    k_aut4 character varying(80) NOT NULL,
    tip_aut4 character(1) NOT NULL,
    resp_aut4 character(1) NOT NULL,
    isbd_1 character varying(240) NOT NULL,
    isbd_2 character varying(240) NOT NULL,
    k_titolo character(50) NOT NULL,
    serie1 character varying(240) NOT NULL,
    k1_serie character(50) NOT NULL,
    num1_serie character(10) NOT NULL,
    serie2 character varying(240) NOT NULL,
    k2_serie character(50) NOT NULL,
    num2_serie character(10) NOT NULL,
    serie3 character varying(240) NOT NULL,
    k3_serie character(50) NOT NULL,
    num3_serie character(10) NOT NULL,
    tipo1_classe character(1) NOT NULL,
    classe1 character(31) NOT NULL,
    tipo2_classe character(1) NOT NULL,
    classe2 character(31) NOT NULL,
    tipo3_classe character(1) NOT NULL,
    classe3 character(31) NOT NULL,
    sog1 character varying(160) NOT NULL,
    k_sog1 character varying(120) NOT NULL,
    sog2 character varying(160) NOT NULL,
    k_sog2 character varying(120) NOT NULL,
    sog3 character varying(160) NOT NULL,
    k_sog3 character varying(120) NOT NULL,
    num_stand_pro character(35) NOT NULL,
    data_offerta date NOT NULL,
    num_offerta_g character(35) NOT NULL,
    num_linea numeric(6,0) NOT NULL,
    valuta character(3) NOT NULL,
    prezzo numeric(15,3) NOT NULL,
    tipo_prezzo character(3) NOT NULL,
    inf_sul_prezzo character varying(160) NOT NULL,
    altri_rif character varying(160) NOT NULL,
    note_edi character varying(160) NOT NULL,
    prot_edi character(35) NOT NULL,
    num_offerta_p character(35) NOT NULL,
    bid character(10) NOT NULL,
    stato_offerta character(1) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_ins character(12) NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_tip_ord character(1) NOT NULL,
    anno_ord numeric(4,0) NOT NULL,
    cod_ord integer NOT NULL,
    cod_fornitore integer NOT NULL,
    id_sez_acquis_bibliografiche integer,
    id_valuta integer,
    id_capitoli_bilanci integer,
    data_ins timestamp(6) without time zone NOT NULL,
    data_agg timestamp(6) without time zone NOT NULL,
    data_ord date,
    note character varying(160) NOT NULL,
    num_copie integer NOT NULL,
    continuativo character(1) NOT NULL,
    stato_ordine character(1) NOT NULL,
    tipo_doc_lett character(1),
    cod_doc_lett bigint,
    tipo_urgenza character(1) NOT NULL,
    cod_rich_off bigint,
    bid_p character(10),
    note_forn character varying(160) NOT NULL,
    tipo_invio character(1) NOT NULL,
    anno_1ord numeric(4,0),
    cod_1ord integer,
    prezzo numeric(15,3) NOT NULL,
    paese character(2) NOT NULL,
    cod_bib_sugg character(3),
    cod_sugg_bibl numeric(10,0),
    bid character(10),
    stato_abb character(1),
    cod_per_abb character(1),
    prezzo_lire numeric(15,3) NOT NULL,
    reg_trib character(50) NOT NULL,
    anno_abb numeric(4,0),
    num_fasc character(15) NOT NULL,
    data_fasc date,
    annata character(10) NOT NULL,
    num_vol_abb smallint,
    natura character(1) NOT NULL,
    data_fine date,
    stampato boolean DEFAULT false NOT NULL,
    rinnovato boolean DEFAULT false NOT NULL,
    data_chiusura_ord timestamp(0) without time zone,
    tbb_bilancicod_mat character(1),
    ute_ins character(12) DEFAULT 'uteins'::bpchar NOT NULL,
    ute_var character(12) DEFAULT 'utevar'::bpchar NOT NULL,
    ts_ins timestamp(0) without time zone DEFAULT now() NOT NULL,
    ts_var timestamp(0) without time zone DEFAULT now() NOT NULL,
    fl_canc character(1),
    cd_tipo_lav character(1)
) WITHOUT OIDS;
--
-- Structure for table tba_parametri_buono_ordine (OID = 58594) : 
--
CREATE TABLE sbnweb.tba_parametri_buono_ordine (
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    progr numeric(1,0) NOT NULL,
    codice_buono character(1) NOT NULL,
    descr_test character varying(240) NOT NULL,
    descr_foot character varying(240) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    descr_oggetto text,
    descr_formulaintr text,
    area_titolo character(1),
    area_ediz character(1),
    area_num character(1),
    area_pub character(1),
    logo boolean,
    logo_img character varying(100),
    prezzo boolean,
    nprot boolean,
    dataprot boolean,
    rinnovo character(1),
    firmadigit boolean,
    firmadigit_img character varying(100),
    ristampa boolean,
    xml_formulaintr text
) WITHOUT OIDS;
--
-- Structure for table tba_parametri_ordine (OID = 58600) : 
--
CREATE TABLE sbnweb.tba_parametri_ordine (
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    gest_bil character(1) NOT NULL,
    gest_sez character(1) NOT NULL,
    gest_prof character(1) NOT NULL,
    cod_prac numeric(10,0),
    cod_sezione character(7),
    esercizio numeric(4,0),
    capitolo numeric(4,0),
    cod_mat character(1),
    cod_fornitore_a integer,
    cod_fornitore_l integer,
    cod_fornitore_d integer,
    cod_fornitore_v integer,
    cod_fornitore_c integer,
    cod_fornitore_r integer,
    ordini_aperti character(1) NOT NULL,
    ordini_chiusi character(1) NOT NULL,
    ordini_annullati character(1) NOT NULL,
    allineamento_prezzo character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    cd_bib_google character varying(255),
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
    descr character(20) NOT NULL,
    paese character(2) NOT NULL,
    lingua character(3) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    ute_ins character(12) NOT NULL,
    ute_var character(12) NOT NULL,
    fl_canc character(1) NOT NULL,
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_rich_off bigint DEFAULT nextval('tba_richieste_offerta_cod_rich_off_seq'::regclass) NOT NULL,
    data_rich_off date NOT NULL,
    prezzo_rich numeric(15,3) NOT NULL,
    num_copie integer NOT NULL,
    note character varying(160) NOT NULL,
    stato_rich_off character(1) NOT NULL,
    bid character(10) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tba_righe_fatture (OID = 58615) : 
--
CREATE TABLE sbnweb.tba_righe_fatture (
    id_fattura integer NOT NULL,
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    riga_fattura smallint NOT NULL,
    id_ordine integer,
    id_capitoli_bilanci integer,
    cod_mat character(1),
    note character varying(160) NOT NULL,
    importo_riga numeric(15,3) NOT NULL,
    sconto_1 numeric(5,2) NOT NULL,
    sconto_2 numeric(5,2) NOT NULL,
    cod_iva character(2) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_sezione character(7) NOT NULL,
    nome character(30) NOT NULL,
    note character varying(255) NOT NULL,
    somma_disp numeric(15,3) NOT NULL,
    budget numeric(15,3) NOT NULL,
    anno_val numeric(4,0) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    data_val date
) WITHOUT OIDS;
--
-- Structure for table tba_suggerimenti_bibliografici (OID = 58624) : 
--
CREATE TABLE sbnweb.tba_suggerimenti_bibliografici (
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_sugg_bibl numeric(10,0) NOT NULL,
    data_sugg_bibl date NOT NULL,
    note character varying(160) NOT NULL,
    msg_per_bibl character varying(255) NOT NULL,
    note_forn character varying(160) NOT NULL,
    cod_bib_cs character(3) NOT NULL,
    bid character(10) NOT NULL,
    cod_bibliotecario numeric(9,0) NOT NULL,
    stato_sugg character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    id_sez_acquis_bibliografiche integer
) WITHOUT OIDS;
--
-- Structure for table tbb_bilanci (OID = 58630) : 
--
CREATE TABLE sbnweb.tbb_bilanci (
    cod_mat character(1) NOT NULL,
    id_capitoli_bilanci integer NOT NULL,
    id_buono_ordine integer,
    budget numeric(15,3) NOT NULL,
    ordinato numeric(15,3) NOT NULL,
    fatturato numeric(15,3) NOT NULL,
    pagato numeric(15,3) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    esercizio numeric(4,0) NOT NULL,
    capitolo numeric(16,0) NOT NULL,
    budget numeric(15,3) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(12) NOT NULL
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
    cd_sistema character(3),
    cd_edizione character(2),
    classe character(31),
    cd_biblioteca_sezione character(3) NOT NULL,
    cd_sez character(10) NOT NULL,
    cd_polo_sezione character(3) NOT NULL,
    cd_biblioteca_doc character(3),
    cd_polo_doc character(3),
    bid_doc character(10),
    cd_doc integer,
    key_loc integer DEFAULT nextval('tbc_collocazione_key_loc_seq'::regclass) NOT NULL,
    bid character(10) NOT NULL,
    cd_loc character(24) NOT NULL,
    spec_loc character(12) NOT NULL,
    consis character varying(255),
    tot_inv integer NOT NULL,
    indice character(31),
    ord_loc character(80) NOT NULL,
    ord_spec character(40) NOT NULL,
    tot_inv_prov integer,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_biblioteca character(3),
    cd_polo character(3),
    n_copie_tit integer NOT NULL,
    n_copie_edi integer NOT NULL,
    n_copie_poss integer NOT NULL,
    n_copie_richiamo integer NOT NULL,
    cd_unimarc character(8) NOT NULL,
    sch_autori character(1) NOT NULL,
    fl_coll_aut character(1) NOT NULL,
    fl_tipo_leg character(1) NOT NULL,
    sch_topog character(1) NOT NULL,
    fl_coll_top character(1) NOT NULL,
    sch_soggetti character(1) NOT NULL,
    fl_coll_sog character(1) NOT NULL,
    sch_classi character(1) NOT NULL,
    fl_coll_cla character(1) NOT NULL,
    sch_titoli character(1) NOT NULL,
    fl_coll_tit character(1) NOT NULL,
    sch_edit character(1) NOT NULL,
    fl_coll_edi character(1) NOT NULL,
    sch_poss character(1) NOT NULL,
    fl_coll_poss character(1) NOT NULL,
    flg_coll_richiamo character(1) NOT NULL,
    fl_non_inv character(1) NOT NULL,
    tipo character(2) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    n_serie integer NOT NULL,
    n_piste integer NOT NULL,
    n_copie integer NOT NULL,
    n_copie_aut integer NOT NULL,
    n_copie_top integer NOT NULL,
    n_copie_sog integer NOT NULL,
    n_copie_cla integer NOT NULL,
    id_modello integer,
    formato_stampa character(3) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbc_esemplare_titolo (OID = 58654) : 
--
CREATE TABLE sbnweb.tbc_esemplare_titolo (
    cd_doc integer NOT NULL,
    bid character(10) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    cons_doc character varying(800),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbc_inventario (OID = 58660) : 
--
CREATE TABLE sbnweb.tbc_inventario (
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cd_serie character(3) NOT NULL,
    cd_inven integer NOT NULL,
    cd_proven character(5),
    key_loc integer,
    bid character(10),
    cd_polo_proven character(3),
    cd_biblioteca_proven character(3),
    key_loc_old integer,
    seq_coll character(20) NOT NULL,
    precis_inv text,
    cd_sit character(1) NOT NULL,
    valore numeric(12,3) NOT NULL,
    importo numeric(12,3) NOT NULL,
    num_vol integer,
    tot_loc integer,
    tot_inter integer,
    anno_abb integer,
    flg_disp character(1) NOT NULL,
    flg_nuovo_usato character(1) NOT NULL,
    stato_con character(2) NOT NULL,
    cd_fornitore integer,
    cd_mat_inv character(2) NOT NULL,
    cd_bib_ord character(3),
    cd_tip_ord character(1),
    anno_ord integer,
    cd_ord integer,
    riga_ord integer,
    cd_bib_fatt character(3),
    anno_fattura integer,
    prg_fattura integer,
    riga_fattura integer,
    cd_no_disp character(2),
    cd_frui character(2),
    cd_carico character(1),
    num_carico integer,
    data_carico date,
    cd_polo_scar character(3),
    cd_bib_scar character(3),
    cd_scarico character(1),
    num_scarico integer,
    data_scarico date,
    data_delibera date,
    delibera_scar character(50),
    sez_old character(10),
    loc_old character(24),
    spec_old character(12),
    cd_supporto character(2),
    ute_ins_prima_coll character(12),
    ts_ins_prima_coll timestamp(6) without time zone,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    tipo_acquisizione character(1),
    supporto_copia character(2),
    digitalizzazione character(1),
    disp_copia_digitale character(2),
    id_accesso_remoto character(320),
    rif_teca_digitale character(4),
    cd_riproducibilita character(2),
    data_ingresso date,
    data_redisp_prev date,
    id_bib_scar integer
) WITHOUT OIDS;
--
-- Structure for table tbc_nota_inv (OID = 58666) : 
--
CREATE TABLE sbnweb.tbc_nota_inv (
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cd_serie character(3) NOT NULL,
    cd_inven integer NOT NULL,
    cd_nota character(4) NOT NULL,
    ds_nota_libera character varying(1920),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbc_possessore_provenienza (OID = 58672) : 
--
CREATE TABLE sbnweb.tbc_possessore_provenienza (
    pid character(10) NOT NULL,
    tp_forma_pp character(1) NOT NULL,
    ky_cautun character(6) NOT NULL,
    ky_auteur character(10) NOT NULL,
    ky_el1 character(6),
    ky_el2 character(6),
    tp_nome_pp character(1) NOT NULL,
    ky_el3 character(6),
    ky_el4 character(6),
    ky_el5 character(6),
    ky_cles1_a character(50) NOT NULL,
    ky_cles2_a character(30),
    note character varying(320),
    tot_inv integer,
    cd_livello character(2) NOT NULL,
    fl_speciale character(1),
    ds_nome_aut character varying(320) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    tidx_vector_ds_nome_aut tsvector
) WITHOUT OIDS;
--
-- Structure for table tbc_provenienza_inventario (OID = 58678) : 
--
CREATE TABLE sbnweb.tbc_provenienza_inventario (
    cd_proven character(5) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    descr character varying(255) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbc_serie_inventariale (OID = 58681) : 
--
CREATE TABLE sbnweb.tbc_serie_inventariale (
    cd_serie character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    descr character(80) NOT NULL,
    prg_inv_corrente integer NOT NULL,
    prg_inv_pregresso integer NOT NULL,
    num_man integer NOT NULL,
    inizio_man integer NOT NULL,
    fine_man integer NOT NULL,
    flg_chiusa character(1) NOT NULL,
    buono_carico integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
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
    fl_default character(1)
) WITHOUT OIDS;
--
-- Structure for table tbc_sezione_collocazione (OID = 58684) : 
--
CREATE TABLE sbnweb.tbc_sezione_collocazione (
    cd_sez character(10) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    note_sez character varying(255) NOT NULL,
    tot_inv integer NOT NULL,
    descr character(30) NOT NULL,
    cd_colloc character(1) NOT NULL,
    tipo_sez character(1) NOT NULL,
    cd_cla character(3) NOT NULL,
    tot_inv_max integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    nome character(20),
    cognome character(80),
    ts_ins timestamp(6) without time zone NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    ute_ins character(12) NOT NULL,
    ute_var character(12) NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_attivita (OID = 58693) : 
--
CREATE TABLE sbnweb.tbf_attivita (
    cd_attivita character(5) NOT NULL,
    id_attivita_sbnmarc integer NOT NULL,
    flg_menu character varying(1) NOT NULL,
    prg_ordimanento integer,
    cd_funzione_parent character(5),
    fl_auto_abilita_figli character(1) NOT NULL,
    fl_assegna_a_cds character(1) NOT NULL,
    url_servizio character varying(512),
    fl_titolo character(1),
    fl_autore character(1),
    fl_marca character(1),
    fl_luogo character(1),
    fl_soggetto character(1),
    fl_classe character(1),
    liv_autorita_da character(2),
    liv_autorita_a character(2),
    gestione_in_indice character(1),
    gestione_in_polo character(1),
    natura_a character(1),
    natura_b character(1),
    natura_c character(1),
    natura_d character(1),
    natura_m character(1),
    natura_n character(1),
    natura_p character(1),
    natura_s character(1),
    natura_t character(1),
    natura_w character(1),
    natura_r character(1),
    natura_x character(1),
    forma_accettata character(1),
    forma_rinvio character(1),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    classe_java_sbnmarc character varying(256)
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
    tp_attivita character(1),
    ds_attivita character varying(255),
    nota_tipo_attivita character varying(255),
    livello integer,
    codice_attivita character varying(10),
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
    nome_coda_output character varying(255) NOT NULL,
    visibilita character(1) DEFAULT 'P'::bpchar NOT NULL,
    class_name character varying(255) NOT NULL,
    cd_attivita character(5) NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_ana_biblioteca character(6),
    cd_polo character(3),
    cd_bib character(3),
    nom_biblioteca character(80) NOT NULL,
    unit_org character(50) NOT NULL,
    indirizzo character varying(80) NOT NULL,
    cpostale character(20),
    cap character(5) NOT NULL,
    telefono character(20),
    fax character(20),
    note character varying(160),
    p_iva character(18),
    cd_fiscale character(18),
    e_mail character varying(160),
    tipo_biblioteca character(1) NOT NULL,
    paese character(2) NOT NULL,
    provincia character(2) NOT NULL,
    cd_bib_cs character(3) NOT NULL,
    cd_bib_ut character(3),
    cd_utente integer,
    flag_bib character(1) NOT NULL,
    localita character(30) NOT NULL,
    chiave_bib character(50) NOT NULL,
    chiave_ente character(50) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    tidx_vector_nom_biblioteca tsvector,
    tidx_vector_indirizzo tsvector
) WITHOUT OIDS;
--
-- Structure for table tbf_biblioteca_default (OID = 58727) : 
--
CREATE TABLE sbnweb.tbf_biblioteca_default (
    id_default integer NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    value character varying(255) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_biblioteca_in_polo (OID = 58730) : 
--
CREATE TABLE sbnweb.tbf_biblioteca_in_polo (
    cd_biblioteca character(3) NOT NULL,
    id_parametro integer NOT NULL,
    cd_polo character(3) NOT NULL,
    id_gruppo_attivita_polo integer NOT NULL,
    id_biblioteca integer,
    ky_biblioteca character(13),
    cd_ana_biblioteca character(6),
    ds_biblioteca character varying(80),
    ds_citta character varying(30),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    cd_sistema_metropolitano character(3)
) WITHOUT OIDS;
--
-- Structure for table tbf_bibliotecario (OID = 58733) : 
--
CREATE TABLE sbnweb.tbf_bibliotecario (
    id_parametro integer NOT NULL,
    id_utente_professionale integer NOT NULL,
    id_profilo_abilitazione integer,
    cd_livello_gbantico character(2),
    cd_livello_gbmoderno character(2),
    cd_livello_gssoggetti character(2),
    cd_livello_gsclassi character(2),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_bibliotecario_default (OID = 58736) : 
--
CREATE TABLE sbnweb.tbf_bibliotecario_default (
    id_utente_professionale integer NOT NULL,
    id_default integer NOT NULL,
    value character varying(255) NOT NULL
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
    nome_jms character(255) NOT NULL,
    sincrona character(1) DEFAULT 'N'::bpchar NOT NULL,
    id_descrizione character(255),
    id_descr_orario_attivazione character varying(255),
    id_orario_di_attivazione character varying(255),
    cron_expression character varying(255) DEFAULT '0 0/5 * * * ? 9999'::character varying
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
    id_area_sezione character(32) NOT NULL,
    parent character(32),
    seq_ordinamento integer NOT NULL,
    codice_attivita character(5) NOT NULL,
    parametro_attivita character(6),
    codice_modulo smallint
) WITHOUT OIDS;
--
-- Structure for table tbf_config_statistiche (OID = 58756) : 
--
CREATE TABLE sbnweb.tbf_config_statistiche (
    id_stat  SERIAL NOT NULL,
    id_area_sezione character(32) NOT NULL,
    parent character(32),
    seq_ordinamento integer NOT NULL,
    codice_attivita character(5) NOT NULL,
    parametro_attivita character(6),
    codice_modulo smallint,
    nome_statistica character varying,
    tipo_query character(1),
    query text,
    colonne_output character varying,
    fl_polo_biblio character(1) DEFAULT 'P'::bpchar NOT NULL,
    fl_txt character(1) DEFAULT 'N'::bpchar NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_contatore (OID = 58763) : 
--
CREATE TABLE sbnweb.tbf_contatore (
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cd_cont character(3) NOT NULL,
    anno integer NOT NULL,
    key1 character(3) NOT NULL,
    ultimo_prg integer NOT NULL,
    lim_max integer NOT NULL,
    attivo character(1),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    "key" character varying(50) NOT NULL,
    tipo character varying(50) NOT NULL,
    lunghezza integer,
    id_etichetta character varying(255),
    codice_attivita character(5) NOT NULL,
    codice_tabella_validazione character(4),
    seq_ordinamento integer,
    tbf_gruppi_defaultid integer,
    tbf_config_default__id_config integer NOT NULL,
    bundle character varying(255) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_dizionario_errori (OID = 58775) : 
--
CREATE TABLE sbnweb.tbf_dizionario_errori (
    cd_lingua character(5) NOT NULL,
    cd_errore integer NOT NULL,
    ds_errore character varying(255) NOT NULL,
    ute_ins character(12),
    ts_ins timestamp(6) without time zone,
    ute_var character(12),
    ts_var timestamp(6) without time zone,
    fl_canc character(1) NOT NULL
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
    etichetta character varying(255),
    bundle character varying(255) NOT NULL
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
    cd_polo character(3) NOT NULL,
    id_gruppo_attivita_polo integer DEFAULT nextval('tbf_gruppo_attivita_id_gruppo_attivita_polo_seq'::regclass) NOT NULL,
    ds_name character varying(255)
) WITHOUT OIDS;
--
-- Structure for table tbf_intranet_range (OID = 58793) : 
--
CREATE TABLE sbnweb.tbf_intranet_range (
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    address character(39) NOT NULL,
    bitmask smallint NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp without time zone NOT NULL,
    fl_canc character(1) DEFAULT 'N'::bpchar NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_lingua_supportata (OID = 58797) : 
--
CREATE TABLE sbnweb.tbf_lingua_supportata (
    cd_lingua character(5) NOT NULL,
    descr character(80) NOT NULL,
    fl_default character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    smtp character varying(255) NOT NULL,
    pop3 character varying(255) NOT NULL,
    user_name character varying(255) NOT NULL,
    "password" character varying(255) NOT NULL,
    indirizzo character varying(255) NOT NULL,
    descrizione character varying(255),
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
    cd_bib character(3),
    modello character varying(30) NOT NULL,
    tipo_modello character(1) NOT NULL,
    campi_valori_del_form text NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    descr character varying(50),
    cd_polo character(3),
    cd_attivita character(5),
    subreport text,
    descr_bib character varying(80)
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
    nome character varying(80) NOT NULL,
    id_parametro integer NOT NULL,
    id_gruppo_attivita integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    nome character varying(80) NOT NULL,
    id_parametro integer NOT NULL,
    id_gruppo_attivita integer,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    ds_modulo character varying(255) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_par_auth (OID = 58836) : 
--
CREATE TABLE sbnweb.tbf_par_auth (
    cd_par_auth character(2) NOT NULL,
    id_parametro integer NOT NULL,
    tp_abil_auth character(1) NOT NULL,
    fl_abil_legame character(1) NOT NULL,
    fl_leg_auth character(1) NOT NULL,
    cd_livello character(2) NOT NULL,
    cd_contr_sim character(3) NOT NULL,
    fl_abil_forzat character(1) NOT NULL,
    sololocale character(1)
) WITHOUT OIDS;
--
-- Structure for table tbf_par_mat (OID = 58839) : 
--
CREATE TABLE sbnweb.tbf_par_mat (
    cd_par_mat character(1) NOT NULL,
    id_parametro integer NOT NULL,
    tp_abilitaz character(1) NOT NULL,
    cd_contr_sim character(3) NOT NULL,
    fl_abil_forzat character(1) NOT NULL,
    cd_livello character(2) NOT NULL,
    sololocale character(1)
) WITHOUT OIDS;
--
-- Structure for table tbf_par_sem (OID = 58842) : 
--
CREATE TABLE sbnweb.tbf_par_sem (
    tp_tabella_codici character(4) NOT NULL,
    cd_tabella_codici character(6) NOT NULL,
    id_parametro integer NOT NULL,
    sololocale character(1)
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
    cd_livello character(2) NOT NULL,
    tp_ret_doc character(3) NOT NULL,
    tp_all_pref character(2) NOT NULL,
    cd_liv_ade character(1) NOT NULL,
    fl_spogli character(1) NOT NULL,
    fl_aut_superflui character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    sololocale character(1)
) WITHOUT OIDS;
--
-- Structure for table tbf_polo (OID = 58851) : 
--
CREATE TABLE sbnweb.tbf_polo (
    id_parametro integer,
    cd_polo character(3) NOT NULL,
    url_indice character varying(255),
    username character varying(255),
    "password" character varying(255),
    denominazione character varying(255),
    ute_var character(12),
    ts_ins timestamp(6) without time zone,
    ute_ins character(12),
    ts_var timestamp(6) without time zone,
    url_polo character varying(255),
    username_polo character varying(6),
    id_gruppo_attivita integer,
    email character varying(255) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_polo_default (OID = 58857) : 
--
CREATE TABLE sbnweb.tbf_polo_default (
    id_default integer NOT NULL,
    cd_polo character(3) NOT NULL,
    value character varying(255) NOT NULL
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
    cod_attivita character(5) NOT NULL,
    cod_polo character(3) NOT NULL,
    cod_bib character(3) NOT NULL,
    tipo_output character(10) NOT NULL,
    dati bytea NOT NULL,
    ute_ins character(12) NOT NULL,
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
    cd_prof character(3),
    cd_biblioteca character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    dsc_profilo character(80) NOT NULL,
    nota_profilo character(80),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbf_utenti_professionali_web (OID = 58875) : 
--
CREATE TABLE sbnweb.tbf_utenti_professionali_web (
    id_utente_professionale integer NOT NULL,
    userid character(12) NOT NULL,
    "password" character(256) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    change_password character(1) NOT NULL,
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
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_attivita character(2) NOT NULL,
    cod_controllo smallint NOT NULL,
    fl_bloccante character(1) NOT NULL,
    messaggio character varying(255) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    fl_bloccante character(1) NOT NULL,
    messaggio character varying(255) NOT NULL,
    cod_controllo smallint NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_segn integer NOT NULL,
    segn_inizio character(80) NOT NULL,
    segn_fine character(80) NOT NULL,
    cod_no_disp character(2) NOT NULL,
    cod_frui character(2) NOT NULL,
    segn_da character(80) NOT NULL,
    segn_a character(80) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    tipo_doc_lett character(1) NOT NULL,
    cod_doc_lett bigint NOT NULL,
    id_utenti integer,
    titolo character varying(240) NOT NULL,
    luogo_edizione character(30),
    editore character(50),
    anno_edizione numeric(4,0),
    autore character varying(160),
    num_volume smallint,
    annata character(10),
    tipo_data character(1),
    prima_data numeric(4,0),
    seconda_data numeric(4,0),
    stato_catal character(1),
    natura character(1) NOT NULL,
    paese character(2),
    lingua character(3),
    fonte character(1) NOT NULL,
    stato_sugg character(1) NOT NULL,
    bid character(10),
    data_sugg_lett date NOT NULL,
    note character varying(160),
    msg_lettore character varying(255),
    segnatura character(40) NOT NULL,
    cod_bib_inv character(3),
    cod_serie character(3),
    cod_inven integer,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    ord_segnatura character(80),
    cd_no_disp character(2),
    cd_catfrui character(2),
    denom_biblioteca_doc character varying(160),
    tidx_vector tsvector
) WITHOUT OIDS;
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
    fonte character(1) NOT NULL,
    inventario character(12),
    precisazione text,
    cod_no_disp character(2),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cod_attivita character(2) NOT NULL,
    flag_stampa character(1) NOT NULL,
    num_pag smallint NOT NULL,
    testo character varying(240) NOT NULL,
    flg_abil character(1) NOT NULL,
    flg_rinn character(1) NOT NULL,
    stato_iter character(1) NOT NULL,
    obbl character(1) NOT NULL,
    cod_stato_rich character(1) NOT NULL,
    stato_mov character(1) NOT NULL,
    cod_stat_cir character(1) NOT NULL,
    cod_stato_ric_ill character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cod_mat character(2) NOT NULL,
    descr character(30) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbl_messaggio (OID = 58929) : 
--
CREATE TABLE sbnweb.tbl_messaggio (
    progr_msg integer NOT NULL,
    cod_rich_serv_richiesta bigint NOT NULL,
    note character varying(160) NOT NULL,
    flag_prov character(1) NOT NULL,
    data_invio timestamp(6) without time zone NOT NULL,
    cod_stat_cir character(1) NOT NULL,
    cod_stato_rich character(1) NOT NULL,
    cod_risp character(1) NOT NULL,
    ute_ins character(3) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(3) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_erog character(1) NOT NULL,
    tar_base numeric(10,3) NOT NULL,
    costo_unitario numeric(10,3) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_mod_pag smallint NOT NULL,
    descr character varying(255) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    professione character(1) NOT NULL,
    occupazione character(2) NOT NULL,
    descr character(50) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    num_lettere smallint NOT NULL,
    num_gg_ritardo1 smallint NOT NULL,
    num_gg_ritardo2 smallint NOT NULL,
    num_gg_ritardo3 smallint NOT NULL,
    num_prenotazioni smallint NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    ute_ins character(12) NOT NULL,
    ute_var character(12) NOT NULL,
    fl_catfrui_nosbndoc character(1) NOT NULL,
    cd_catfrui_nosbndoc character(2),
    cod_modalita_invio1_sollecito1 character(1),
    cod_modalita_invio2_sollecito1 character(1),
    cod_modalita_invio3_sollecito1 character(1),
    cod_modalita_invio1_sollecito2 character(1),
    cod_modalita_invio2_sollecito2 character(1),
    cod_modalita_invio3_sollecito2 character(1),
    cod_modalita_invio1_sollecito3 character(1),
    cod_modalita_invio2_sollecito3 character(1),
    cod_modalita_invio3_sollecito3 character(1),
    num_gg_validita_prelazione smallint NOT NULL,
    ammessa_autoregistrazione_utente character(1) NOT NULL,
    ammesso_inserimento_utente character(1) NOT NULL,
    anche_da_remoto character(1) NOT NULL,
    cd_catriprod_nosbndoc character(2),
	xml_modello_soll TEXT,
    fl_tipo_rinnovo CHAR(1) DEFAULT '0',
    num_gg_rinnovo_richiesta SMALLINT DEFAULT 0,
	fl_priorita_prenot CHAR(1) DEFAULT 'N',
	email_notifica VARCHAR(160)
) WITHOUT OIDS;
--
-- Structure for table tbl_penalita_servizio (OID = 58956) : 
--
CREATE TABLE sbnweb.tbl_penalita_servizio (
    id_servizio integer NOT NULL,
    gg_sosp smallint NOT NULL,
    coeff_sosp numeric(2,0) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    occupato character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
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
    cd_polo_inv character(3),
    cod_bib_inv character(3),
    cod_serie_inv character(3),
    cod_inven_inv integer,
    id_documenti_lettore integer,
    id_esemplare_documenti_lettore integer,
    id_utenti_biblioteca integer NOT NULL,
    id_modalita_pagamento integer,
    id_supporti_biblioteca integer,
    id_servizio integer NOT NULL,
    id_iter_servizio integer NOT NULL,
    fl_tipo_rec character(1) NOT NULL,
    note_bibliotecario character varying(255),
    costo_servizio numeric(12,3),
    num_fasc character(30),
    num_volume smallint,
    anno_period numeric(4,0),
    cod_tipo_serv_rich character(2),
    cod_tipo_serv_alt character(2),
    cod_stato_rich character(1) NOT NULL,
    cod_stato_mov character(1),
    data_in_eff timestamp(6) without time zone,
    data_fine_eff timestamp(6) without time zone,
    num_rinnovi smallint,
    bid character(10),
    cod_attivita character(2) NOT NULL,
    data_richiesta timestamp(6) without time zone NOT NULL,
    num_pezzi smallint,
    note_ut character varying(255),
    prezzo_max numeric(12,3),
    data_massima date,
    data_proroga date,
    data_in_prev timestamp(6) without time zone,
    data_fine_prev timestamp(6) without time zone,
    fl_svolg character(1) NOT NULL,
    copyright character(1),
    cod_erog character(1) NOT NULL,
    cod_risp character(1),
    fl_condiz character(1),
    fl_inoltro character(1),
    cod_bib_dest character(3),
    cod_bib_prelievo character(3),
    cod_bib_restituzione character(3),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_sala character(2) NOT NULL,
    descr character varying(160) NOT NULL,
    num_max_posti smallint NOT NULL,
    num_prg_posti smallint NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
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
    cod_servizio character(3) NOT NULL,
    descr character varying(255) NOT NULL,
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
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbl_servizio_web_dati_richiesti (OID = 58986) : 
--
CREATE TABLE sbnweb.tbl_servizio_web_dati_richiesti (
    campo_richiesta smallint NOT NULL,
    id_tipo_servizio integer NOT NULL,
    obbligatorio character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbl_solleciti (OID = 58989) : 
--
CREATE TABLE sbnweb.tbl_solleciti (
    progr_sollecito smallint NOT NULL,
    cod_rich_serv bigint NOT NULL,
    data_invio date NOT NULL,
    note character varying(160) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    tipo_invio character(1) DEFAULT 3 NOT NULL,
    esito character(1) DEFAULT 'S'::bpchar NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    tit_studio character(1) NOT NULL,
    specif_tit character(2) NOT NULL,
    descr character varying(250) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbl_storico_richieste_servizio (OID = 59000) : 
--
CREATE TABLE sbnweb.tbl_storico_richieste_servizio (
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_rich_serv numeric(12,0) NOT NULL,
    cod_bib_ut character(3),
    cod_utente numeric(10,0),
    cod_tipo_serv character varying(255) NOT NULL,
    data_richiesta date,
    num_volume smallint,
    num_fasc character(30),
    num_pezzi smallint,
    note_ut character varying(255),
    prezzo_max numeric(12,3),
    costo_servizio numeric(12,3),
    data_massima date,
    note_bibl character varying(255),
    data_proroga date,
    data_in_prev date,
    data_fine_prev date,
    flag_svolg character(1) NOT NULL,
    copyright character(1),
    descr_erog character varying(255),
    descr_stato_ric character varying(255),
    descr_supporto character varying(255),
    descr_risp character varying(255),
    descr_mod_pag character varying(255),
    flag_pren character(1) NOT NULL,
    tipo_doc_lett character(1),
    cod_doc_lett numeric(10,0) DEFAULT 0 NOT NULL,
    prg_esemplare smallint DEFAULT 0 NOT NULL,
    cod_serie character(3),
    cod_inven integer,
    flag_condiz character(1),
    cod_tipo_serv_alt character(2),
    descr_erog_alt character varying(255),
    cod_bib_dest character(3),
    titolo character varying(240),
    autore character varying(160),
    editore character(50),
    anno_edizione character(4),
    luogo_edizione character(30),
    annata character(10),
    num_vol_mon smallint,
    data_in_eff date,
    data_fine_eff date,
    num_rinnovi smallint,
    note_bibliotecario character varying(255),
    descr_stato_mov character varying(255),
    flag_tipo_serv character(1),
    num_solleciti smallint,
    data_ult_soll date,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_supporto character(2) NOT NULL,
    imp_unita numeric(10,3) NOT NULL,
    costo_fisso numeric(10,3) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_tipo_aut character(3) NOT NULL,
    descr character varying(160) NOT NULL,
    flag_aut character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_tipo_serv character(2) NOT NULL,
    num_max_mov smallint NOT NULL,
    ore_ridis smallint NOT NULL,
    penalita character(1) NOT NULL,
    coda_richieste character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    ins_richieste_utente character(1) NOT NULL,
    anche_da_remoto character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_utente character(25) NOT NULL,
    "password" character varying(255),
    cognome character varying(80) NOT NULL,
    nome character varying(25) NOT NULL,
    indirizzo_res character(50),
    citta_res character(50),
    cap_res character(5),
    tel_res character(20),
    fax_res character(20),
    indirizzo_dom character(50),
    citta_dom character(50),
    cap_dom character(5),
    tel_dom character(20),
    fax_dom character(20),
    sesso character(1) NOT NULL,
    data_nascita date NOT NULL,
    luogo_nascita character(30) NOT NULL,
    cod_fiscale character(16),
    cod_ateneo character(3),
    cod_matricola character(10),
    corso_laurea character(26),
    ind_posta_elettr character(80),
    persona_giuridica character(1),
    nome_referente character(50),
    data_reg date,
    credito_polo numeric(12,3),
    note_polo character(50),
    note character(50),
    cod_proven character(1),
    tipo_pers_giur character(1),
    paese_res character(2),
    paese_citt character(2),
    tipo_docum1 character(1),
    num_docum1 character(20),
    aut_ril_docum1 character(40),
    tipo_docum2 character(1),
    num_docum2 character(20),
    aut_ril_docum2 character(40),
    tipo_docum3 character(1),
    num_docum3 character(20),
    aut_ril_docum3 character(40),
    tipo_docum4 character(1),
    num_docum4 character(20),
    aut_ril_docum4 character(40),
    cod_bib character(3) NOT NULL,
    prov_dom character(2),
    prov_res character(2),
    cod_polo_bib character(3) NOT NULL,
    allinea character(1),
    chiave_ute character(130),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    change_password character(1) NOT NULL,
    last_access timestamp(6) without time zone NOT NULL,
    ts_var_password timestamp(6) without time zone NOT NULL,
    codice_anagrafe character(6),
    tit_studio character(1),
    professione character(1),
    tidx_vector tsvector
) WITHOUT OIDS;
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
    bid character(10) NOT NULL,
    fid integer NOT NULL,
    id_ordine integer,
    cd_bib_abb character(3),
    data_arrivo date,
    cd_polo_inv character(3),
    cd_bib_inv character(3),
    cd_serie character(3),
    cd_inven integer,
    grp_fasc integer,
    note character varying(240),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    cd_no_disp character(1)
) WITHOUT OIDS;
--
-- Structure for table tbp_fascicolo (OID = 59041) : 
--
CREATE TABLE sbnweb.tbp_fascicolo (
    bid character(10) NOT NULL,
    fid integer NOT NULL,
    sici character varying(80),
    ean character(13),
    data_conv_pub date DEFAULT to_date('19010101'::text, 'YYYYMMDD'::text) NOT NULL,
    cd_per character(1),
    cd_tipo_fasc character(1),
    data_pubb character varying(80),
    descrizione character varying(240),
    annata character(10),
    num_volume smallint,
    num_in_fasc integer,
    data_in_pubbl date,
    num_fi_fasc integer,
    data_fi_pubbl date,
    note character varying(240),
    num_alter character(15),
    bid_link character(10),
    fid_link integer,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    anno_pub smallint
) WITHOUT OIDS;
--
-- Structure for table tbq_batch_attivabili (OID = 59048) : 
--
CREATE TABLE sbnweb.tbq_batch_attivabili (
    cod_funz character(5) NOT NULL,
    descr character(32),
    descr_b character(20),
    server character(1),
    "path" character(30),
    job_name character(16),
    tipo character(4),
    queue_name character(16),
    flg_paral character(1),
    priorita character(1),
    flg_prior character(1),
    flg_canc character(1),
    flg_stampa character(1),
    flg_name character(20),
    flg_fname character(20),
    condiz character varying(255),
    canc character(1),
    cd_bib_richieste_batch character(3),
    msg_coda_jms_richieste_batch character varying(100),
    cod_funz_richieste_batch character(4),
    cd_polo_richieste_batch character(3) NOT NULL
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
    nom_fornitore character(50) NOT NULL,
    unit_org character(50) NOT NULL,
    indirizzo character varying(70) NOT NULL,
    cpostale character(20) NOT NULL,
    citta character(20) NOT NULL,
    cap character(10) NOT NULL,
    telefono character(20) NOT NULL,
    fax character(20) NOT NULL,
    note character varying(160) NOT NULL,
    p_iva character(18) NOT NULL,
    cod_fiscale character(18) NOT NULL,
    e_mail character(50) NOT NULL,
    paese character(2) NOT NULL,
    tipo_partner character(1) NOT NULL,
    provincia character(2) NOT NULL,
    cod_bib character(3) NOT NULL,
    chiave_for character(50) NOT NULL,
    cod_polo_bib character(3) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    tidx_vector tsvector,
	regione CHAR(2)
) WITHOUT OIDS;
--
-- Structure for table tbr_fornitori_biblioteche (OID = 59063) : 
--
CREATE TABLE sbnweb.tbr_fornitori_biblioteche (
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cod_fornitore integer NOT NULL,
    tipo_pagamento character(50) NOT NULL,
    cod_cliente character(40) NOT NULL,
    nom_contatto character(50) NOT NULL,
    tel_contatto character(20) NOT NULL,
    fax_contatto character(20) NOT NULL,
    valuta character(3) NOT NULL,
    cod_polo character(3) NOT NULL,
    allinea character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_aut_aut (OID = 59066) : 
--
CREATE TABLE sbnweb.tr_aut_aut (
    vid_base character(10) NOT NULL,
    vid_coll character(10) NOT NULL,
    tp_legame character(1) NOT NULL,
    nota_aut_aut character varying(320),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_aut_bib (OID = 59069) : 
--
CREATE TABLE sbnweb.tr_aut_bib (
    vid character(10) NOT NULL,
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    fl_allinea character(1) NOT NULL,
    fl_allinea_sbnmarc character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_aut_mar (OID = 59072) : 
--
CREATE TABLE sbnweb.tr_aut_mar (
    vid character(10) NOT NULL,
    mid character(10) NOT NULL,
    nota_aut_mar character varying(80),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_des_des (OID = 59075) : 
--
CREATE TABLE sbnweb.tr_des_des (
    did_base character(10) NOT NULL,
    did_coll character(10) NOT NULL,
    tp_legame character(5) NOT NULL,
    nota_des_des character varying(240),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_luo_luo (OID = 59078) : 
--
CREATE TABLE sbnweb.tr_luo_luo (
    lid_base character(10) NOT NULL,
    lid_coll character(10) NOT NULL,
    tp_legame character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_mar_bib (OID = 59081) : 
--
CREATE TABLE sbnweb.tr_mar_bib (
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    mid character(10) NOT NULL,
    fl_allinea character(1) NOT NULL,
    fl_allinea_sbnmarc character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_modello_sistemi_classi_biblioteche (OID = 59084) : 
--
CREATE TABLE sbnweb.tr_modello_sistemi_classi_biblioteche (
    id_modello integer NOT NULL,
    nome character(80) NOT NULL,
    cd_sistema character(3) NOT NULL,
    cd_edizione character(2) NOT NULL,
    sololocale character(1) NOT NULL,
    flg_att character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_modello_soggettari_biblioteche (OID = 59087) : 
--
CREATE TABLE sbnweb.tr_modello_soggettari_biblioteche (
    id_modello integer NOT NULL,
    nome character(80) NOT NULL,
    cd_sogg character(3) NOT NULL,
    solo_locale character(1),
    fl_att character(1) NOT NULL,
    fl_auto_loc character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_modello_thesauri_biblioteche (OID = 59090) : 
--
CREATE TABLE sbnweb.tr_modello_thesauri_biblioteche (
    id_modello integer NOT NULL,
    nome character(80) NOT NULL,
    cd_the character(3) NOT NULL,
    sololocale character(1),
    fl_att character(1) NOT NULL,
    fl_auto_loc character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_per_int (OID = 59093) : 
--
CREATE TABLE sbnweb.tr_per_int (
    vid character(10) NOT NULL,
    id_personaggio integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_rep_aut (OID = 59096) : 
--
CREATE TABLE sbnweb.tr_rep_aut (
    vid character(10) NOT NULL,
    id_repertorio integer NOT NULL,
    note_rep_aut character varying(160),
    fl_trovato character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_rep_mar (OID = 59099) : 
--
CREATE TABLE sbnweb.tr_rep_mar (
    progr_repertorio integer NOT NULL,
    mid character(10) NOT NULL,
    id_repertorio integer NOT NULL,
    nota_rep_mar character varying(160),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_rep_tit (OID = 59102) : 
--
CREATE TABLE sbnweb.tr_rep_tit (
    bid character(10) NOT NULL,
    id_repertorio integer NOT NULL,
    nota_rep_tit character varying(160),
    fl_trovato character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_sistemi_classi_biblioteche (OID = 59105) : 
--
CREATE TABLE sbnweb.tr_sistemi_classi_biblioteche (
    cd_biblioteca character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    cd_sistema character(3) NOT NULL,
    cd_edizione character(2) NOT NULL,
    flg_att character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    sololocale character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_sog_des (OID = 59108) : 
--
CREATE TABLE sbnweb.tr_sog_des (
    did character(10) NOT NULL,
    cid character(10) NOT NULL,
    fl_posizione integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    fl_primavoce character(1)
) WITHOUT OIDS;
--
-- Structure for table tr_soggettari_biblioteche (OID = 59111) : 
--
CREATE TABLE sbnweb.tr_soggettari_biblioteche (
    cd_sogg character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    fl_att character(1) NOT NULL,
    fl_auto_loc character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    sololocale character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_sogp_sogi (OID = 59114) : 
--
CREATE TABLE sbnweb.tr_sogp_sogi (
    cid_p character(10) NOT NULL,
    cid_i character(10) NOT NULL,
    bid character(10) NOT NULL,
    fl_imp_sog character(1) NOT NULL,
    fl_sog_mod_da character(1),
    fl_imp_tit_sog character(1),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_termini_termini (OID = 59117) : 
--
CREATE TABLE sbnweb.tr_termini_termini (
    did_base character(10) NOT NULL,
    did_coll character(10) NOT NULL,
    nota_termini_termini character varying(240),
    tipo_coll character(5) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_the_cla (OID = 59120) : 
--
CREATE TABLE sbnweb.tr_the_cla (
    cd_the character(3) NOT NULL,
    did character(10) NOT NULL,
    cd_sistema character(3) NOT NULL,
    cd_edizione character(2) NOT NULL,
    classe character(31) NOT NULL,
    nota_the_cla character varying(240),
    posizione smallint DEFAULT 0 NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_thesauri_biblioteche (OID = 59124) : 
--
CREATE TABLE sbnweb.tr_thesauri_biblioteche (
    cd_biblioteca character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    cd_the character(3) NOT NULL,
    fl_att character(1) NOT NULL,
    fl_auto_loc character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    sololocale character(1)
) WITHOUT OIDS;
--
-- Structure for table tr_tit_aut (OID = 59127) : 
--
CREATE TABLE sbnweb.tr_tit_aut (
    bid character(10) NOT NULL,
    vid character(10) NOT NULL,
    tp_responsabilita character(1) NOT NULL,
    cd_relazione character(3) NOT NULL,
    nota_tit_aut character varying(80),
    fl_incerto character(1) NOT NULL,
    fl_superfluo character(1) NOT NULL,
    cd_strumento_mus character(5),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    fl_condiviso character(1) DEFAULT '''s''::bpchar'::bpchar NOT NULL,
    ute_condiviso character(12),
    ts_condiviso timestamp(6) without time zone
) WITHOUT OIDS;
--
-- Structure for table tr_tit_bib (OID = 59131) : 
--
CREATE TABLE sbnweb.tr_tit_bib (
    bid character(10) NOT NULL,
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    fl_gestione character(1) NOT NULL,
    fl_disp_elettr character(1) NOT NULL,
    fl_allinea character(1) NOT NULL,
    fl_allinea_sbnmarc character(1) NOT NULL,
    fl_allinea_cla character(1) NOT NULL,
    fl_allinea_sog character(1) NOT NULL,
    fl_allinea_luo character(1) NOT NULL,
    fl_allinea_rep character(1) NOT NULL,
    fl_mutilo character(1) NOT NULL,
    ds_consistenza character varying(400),
    fl_possesso character(1) NOT NULL,
    ds_fondo character varying(55),
    ds_segn character varying(30),
    ds_antica_segn character varying(120),
    nota_tit_bib character varying(134),
    uri_copia character varying(320),
    tp_digitalizz character(1),
    ts_ins_prima_coll timestamp(6) without time zone,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_tit_cla (OID = 59137) : 
--
CREATE TABLE sbnweb.tr_tit_cla (
    bid character(10) NOT NULL,
    classe character(31) NOT NULL,
    cd_sistema character(3) NOT NULL,
    cd_edizione character(2) NOT NULL,
    nota_tit_cla character varying(240),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_tit_luo (OID = 59140) : 
--
CREATE TABLE sbnweb.tr_tit_luo (
    bid character(10) NOT NULL,
    lid character(10) NOT NULL,
    tp_luogo character(1) NOT NULL,
    nota_tit_luo character varying(80),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_tit_mar (OID = 59143) : 
--
CREATE TABLE sbnweb.tr_tit_mar (
    bid character(10) NOT NULL,
    mid character(10) NOT NULL,
    nota_tit_mar character varying(80),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_tit_sog_bib (OID = 59146) : 
--
CREATE TABLE sbnweb.tr_tit_sog_bib (
    cid character(10) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    cd_sogg character(3) NOT NULL,
    bid character(10) NOT NULL,
    nota_tit_sog_bib character varying(240),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    posizione smallint DEFAULT 1 NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tr_tit_tit (OID = 59149) : 
--
CREATE TABLE sbnweb.tr_tit_tit (
    bid_base character(10) NOT NULL,
    bid_coll character(10) NOT NULL,
    tp_legame character(2) NOT NULL,
    tp_legame_musica character(1),
    cd_natura_base character(1) NOT NULL,
    cd_natura_coll character(1) NOT NULL,
    sequenza character(10),
    nota_tit_tit character varying(80),
    sequenza_musica character varying(80),
    sici character varying(80),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    fl_condiviso character(1) DEFAULT '''s''::bpchar'::bpchar NOT NULL,
    ute_condiviso character(12),
    ts_condiviso timestamp(6) without time zone
) WITHOUT OIDS;
--
-- Structure for table tr_editore_titolo
--
CREATE TABLE sbnweb.tr_editore_titolo (
    cod_fornitore INTEGER NOT NULL, 
    bid CHAR(10) NOT NULL, 
    nota_legame VARCHAR(255), 
    ute_ins CHAR(12) NOT NULL, 
    ts_ins TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
    ute_var CHAR(12) NOT NULL, 
    ts_var TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL, 
    fl_canc CHAR(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tbr_editore_num_std (OID = 257749) : 
--
CREATE TABLE sbnweb.tbr_editore_num_std (
    cod_fornitore integer NOT NULL,
    numero_std character(10) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tra_elementi_buono_ordine (OID = 59153) : 
--
CREATE TABLE sbnweb.tra_elementi_buono_ordine (
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    buono_ord character(9) NOT NULL,
    cod_tip_ord character(1) NOT NULL,
    anno_ord numeric(4,0) NOT NULL,
    cod_ord integer NOT NULL,
    ute_ins character(12),
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tra_fornitori_offerte (OID = 59156) : 
--
CREATE TABLE sbnweb.tra_fornitori_offerte (
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_fornitore integer NOT NULL,
    cod_rich_off bigint NOT NULL,
    note character varying(160) NOT NULL,
    stato_gara character(1) NOT NULL,
    tipo_invio character(1) NOT NULL,
    data_invio date NOT NULL,
    risp_da_risp character varying(255) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tra_messaggi (OID = 59159) : 
--
CREATE TABLE sbnweb.tra_messaggi (
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_msg integer NOT NULL,
    data_msg date NOT NULL,
    note character(255),
    anno_fattura numeric(4,0),
    progr_fattura integer,
    cod_tip_ord character(1),
    anno_ord numeric(4,0),
    cod_ord integer,
    stato_msg character(1) NOT NULL,
    tipo_msg character(2) NOT NULL,
    tipo_invio character(1) NOT NULL,
    cod_fornitore integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tra_ordine_inventari (OID = 59162) : 
--
CREATE TABLE sbnweb.tra_ordine_inventari (
    id_ordine integer NOT NULL,
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cd_serie character(3) NOT NULL,
    cd_inven integer NOT NULL,
    data_uscita timestamp(0) without time zone,
    data_rientro timestamp(0) without time zone,
    ota_fornitore character varying(255),
    ute_ins character(12),
    ute_var character(12),
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cod_bib_ord character(3) NOT NULL,
    cod_tip_ord character(1) NOT NULL,
    anno_ord numeric(4,0) NOT NULL,
    cod_ord integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    ute_ins character(12) NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table tra_sez_acquisizione_fornitori (OID = 59171) : 
--
CREATE TABLE sbnweb.tra_sez_acquisizione_fornitori (
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cod_prac numeric(10,0) NOT NULL,
    cod_fornitore integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trc_formati_sezioni (OID = 59174) : 
--
CREATE TABLE sbnweb.trc_formati_sezioni (
    cd_formato character(2) NOT NULL,
    cd_polo_sezione character(3) NOT NULL,
    cd_bib_sezione character(3) NOT NULL,
    cd_sezione character(10) NOT NULL,
    prog_serie integer NOT NULL,
    prog_num integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    descr character(30),
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
    pid character(10) NOT NULL,
    cd_inven integer NOT NULL,
    cd_serie character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    cd_legame character(1) NOT NULL,
    nota character varying(320),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trc_possessori_possessori (OID = 59185) : 
--
CREATE TABLE sbnweb.trc_possessori_possessori (
    pid_base character(10) NOT NULL,
    pid_coll character(10) NOT NULL,
    tp_legame character(1) NOT NULL,
    nota character varying(80),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_attivita character(5) NOT NULL,
    cd_bib_affiliata character(3) NOT NULL,
    cd_polo_bib_affiliata character(3) NOT NULL,
    cd_bib_centro_sistema character(3) NOT NULL,
    cd_polo_bib_centro_sistema character(3) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(0) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc character(1)
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
    cd_polo character(3) NOT NULL,
    cd_attivita character(5) NOT NULL,
    id_attivita_polo integer DEFAULT nextval('trf_attivita_polo_id_attivita_polo_seq'::regclass) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trf_funzioni_denominazioni (OID = 59200) : 
--
CREATE TABLE sbnweb.trf_funzioni_denominazioni (
    cd_funzione character(5) NOT NULL,
    cd_lingua character(5) NOT NULL,
    ds_nome character varying(100) NOT NULL,
    ds_nome_breve character varying(30),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    fl_include character(1) NOT NULL
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
    cd_attivita character(5),
    id_profilo_abilitazione integer,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    tp_ruolo character(10),
    note_competenze character varying(160),
    ufficio_appart character(50)
) WITHOUT OIDS;
--
-- Structure for table trf_utente_professionale_polo (OID = 59221) : 
--
CREATE TABLE sbnweb.trf_utente_professionale_polo (
    id_utente_professionale integer NOT NULL,
    cd_polo character(3) NOT NULL,
    ute_var character(12),
    ts_ins timestamp(6) without time zone,
    ute_ins character(12),
    ts_var timestamp(6) without time zone,
    fl_canc character(1)
) WITHOUT OIDS;
--
-- Structure for table trl_attivita_bibliotecario (OID = 59224) : 
--
CREATE TABLE sbnweb.trl_attivita_bibliotecario (
    id_bibliotecario integer NOT NULL,
    id_iter_servizio integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trl_autorizzazioni_servizi (OID = 59227) : 
--
CREATE TABLE sbnweb.trl_autorizzazioni_servizi (
    id_tipi_autorizzazione integer NOT NULL,
    id_servizio integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    note character varying(160),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone,
    fl_canc character(1) NOT NULL,
    cod_tipo_aut character(3)
) WITHOUT OIDS;
--
-- Structure for table trl_materie_utenti (OID = 59233) : 
--
CREATE TABLE sbnweb.trl_materie_utenti (
    id_utenti integer NOT NULL,
    id_materia integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trl_posti_sala_utenti_biblioteca (OID = 59236) : 
--
CREATE TABLE sbnweb.trl_posti_sala_utenti_biblioteca (
    id_posti_sala integer NOT NULL,
    id_utenti_biblioteca integer NOT NULL,
    ts_ingresso timestamp(6) without time zone,
    ts_uscita timestamp(6) without time zone,
    ute_ins character(12) NOT NULL,
    ute_var character(12) NOT NULL,
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
    cd_polo character(3) NOT NULL,
    cd_bib character(3) NOT NULL,
    cd_relazione character varying(10) NOT NULL,
    tabella_di_relazione character varying(255) NOT NULL,
    id_relazione_tabella_di_relazione integer NOT NULL,
    tabella_tb_codici character varying(255) NOT NULL,
    id_relazione_tb_codici character varying(255) NOT NULL,
    uet_ins character(12) NOT NULL,
    ts_is timestamp(0) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(0) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trl_supporti_modalita_erogazione (OID = 59248) : 
--
CREATE TABLE sbnweb.trl_supporti_modalita_erogazione (
    cod_erog character(1) NOT NULL,
    id_supporti_biblioteca integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trl_tariffe_modalita_erogazione (OID = 59251) : 
--
CREATE TABLE sbnweb.trl_tariffe_modalita_erogazione (
    cod_erog character(1) NOT NULL,
    id_tipo_servizio integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    cd_polo character(3) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    id_specificita_titoli_studio integer,
    id_occupazioni integer,
    credito_bib numeric(12,3),
    note_bib text,
    data_inizio_aut date,
    data_fine_aut date,
    data_inizio_sosp date,
    data_fine_sosp date,
    cod_tipo_aut character(3),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trp_messaggio_fascicolo (OID = 59260) : 
--
CREATE TABLE sbnweb.trp_messaggio_fascicolo (
    cd_polo character(10) NOT NULL,
    cd_bib character(3) NOT NULL,
    cd_msg integer NOT NULL,
    bid character(10) NOT NULL,
    fid integer NOT NULL,
    id_ordine integer NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table trs_termini_titoli_biblioteche (OID = 59263) : 
--
CREATE TABLE sbnweb.trs_termini_titoli_biblioteche (
    bid character(10) NOT NULL,
    cd_biblioteca character(3) NOT NULL,
    cd_the character(3) NOT NULL,
    cd_polo character(3) NOT NULL,
    did character(10) NOT NULL,
    nota_termine_titoli_biblioteca character varying(240),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
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
    ky_link_multim character(10) NOT NULL,
    uri_multim character varying(320),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL,
    immagine oid
) WITHOUT OIDS;
--
-- Structure for table ts_note_proposta (OID = 59272) : 
--
CREATE TABLE sbnweb.ts_note_proposta (
    id_proposta integer NOT NULL,
    progr_risposta integer NOT NULL,
    ute_destinatario character(12) NOT NULL,
    note_pro character varying(320),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table ts_proposta (OID = 59275) : 
--
CREATE TABLE sbnweb.ts_proposta (
    ute_mittente character(12) NOT NULL,
    progr_proposta integer NOT NULL,
    ute_destinatario character(12) NOT NULL,
    bidvid character(10) NOT NULL,
    tp_messaggio character(1) NOT NULL,
    ds_proposta character varying(400) NOT NULL,
    dt_inoltro date NOT NULL,
    fl_canc character(1) NOT NULL
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
    ute_mittente character(12) NOT NULL,
    id_oggetto character varying(31) NOT NULL,
    tp_messaggio character(1) NOT NULL,
    ds_proposta character varying(400) NOT NULL,
    dt_inoltro date NOT NULL,
    cd_oggetto character(2) NOT NULL,
    cd_stato character(2) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
-- Structure for table ts_servizio (OID = 59284) : 
--
CREATE TABLE sbnweb.ts_servizio (
    cd_record character(4) NOT NULL,
    campo1 character varying(20),
    campo2 character varying(20),
    campo3 character varying(20)
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
    tp_stop_list character(1) NOT NULL,
    cd_lingua character(3) NOT NULL,
    parola character varying(80) NOT NULL,
    nota_stop_list character varying(320),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone DEFAULT now() NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone DEFAULT now() NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
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
    EXECUTE PROCEDURE tsvector_update_trigger ('tidx_vector', 'pg_catalog.italian', 'ds_classe', 'ult_term');
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
CREATE VIEW sbnweb.v_cartografia AS
SELECT c.cd_livello AS cd_livello_c, c.tp_pubb_gov, c.cd_colore,
    c.cd_meridiano, c.cd_supporto_fisico, c.cd_tecnica, c.cd_forma_ripr,
    c.cd_forma_pubb, c.cd_altitudine, c.cd_tiposcala, c.tp_scala,
    c.scala_oriz, c.scala_vert, c.longitudine_ovest, c.longitudine_est,
    c.latitudine_nord, c.latitudine_sud, c.tp_immagine, c.cd_forma_cart,
    c.cd_piattaforma, c.cd_categ_satellite, t.bid, t.isadn, t.tp_materiale,
    t.tp_record_uni, t.cd_natura, t.cd_paese, t.cd_lingua_1, t.cd_lingua_2,
    t.cd_lingua_3, t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1,
    t.cd_genere_2, t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t,
    t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t, t.ky_cles1_ct, t.ky_cles2_ct,
    t.ky_clet1_ct, t.ky_clet2_ct, t.cd_livello, t.fl_speciale, t.isbd,
    t.indice_isbd, t.ky_editore, t.cd_agenzia, t.cd_norme_cat,
    t.nota_inf_tit, t.nota_cat_tit, t.bid_link, t.tp_link, t.ute_ins,
    t.ts_ins, t.ute_var, t.ts_var, t.ute_forza_ins, t.ute_forza_var,
    t.fl_canc, t.fl_condiviso, t.ts_condiviso, t.ute_condiviso
FROM tb_cartografia c, tb_titolo t
WHERE (((t.bid = c.bid) AND (t.fl_canc <> 'S'::bpchar)) AND (c.fl_canc <>
    'S'::bpchar));

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
-- Definition for view vl_autore_tit (OID = 59336) : 
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
-- Definition for view vl_cartografia_aut (OID = 59341) : 
--
CREATE VIEW sbnweb.vl_cartografia_aut AS
SELECT ta.vid, ta.tp_responsabilita, ta.cd_relazione, ta.nota_tit_aut,
    ta.fl_incerto, ta.fl_superfluo, ta.cd_strumento_mus, ta.fl_condiviso AS
    fl_condiviso_legame, ta.ts_condiviso AS ts_condiviso_legame,
    ta.ute_condiviso AS ute_condiviso_legame, c.cd_livello_c,
    c.tp_pubb_gov, c.cd_colore, c.cd_meridiano, c.cd_supporto_fisico,
    c.cd_tecnica, c.cd_forma_ripr, c.cd_forma_pubb, c.cd_altitudine,
    c.cd_tiposcala, c.tp_scala, c.scala_oriz, c.scala_vert,
    c.longitudine_ovest, c.longitudine_est, c.latitudine_nord,
    c.latitudine_sud, c.tp_immagine, c.cd_forma_cart, c.cd_piattaforma,
    c.cd_categ_satellite, c.bid, c.isadn, c.tp_materiale, c.tp_record_uni,
    c.cd_natura, c.cd_paese, c.cd_lingua_1, c.cd_lingua_2, c.cd_lingua_3,
    c.aa_pubb_1, c.aa_pubb_2, c.tp_aa_pubb, c.cd_genere_1, c.cd_genere_2,
    c.cd_genere_3, c.cd_genere_4, c.ky_cles1_t, c.ky_cles2_t, c.ky_clet1_t,
    c.ky_clet2_t, c.ky_cles1_ct, c.ky_cles2_ct, c.ky_clet1_ct,
    c.ky_clet2_ct, c.cd_livello, c.fl_speciale, c.isbd, c.indice_isbd,
    c.ky_editore, c.cd_agenzia, c.cd_norme_cat, c.nota_inf_tit,
    c.nota_cat_tit, c.bid_link, c.tp_link, c.ute_ins, c.ts_ins, c.ute_var,
    c.ts_var, c.ute_forza_ins, c.ute_forza_var, c.fl_canc, c.fl_condiviso,
    c.ts_condiviso, c.ute_condiviso
FROM tr_tit_aut ta, v_cartografia c
WHERE (((ta.bid = c.bid) AND (ta.fl_canc <> 'S'::bpchar)) AND (c.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_cartografia_aut_aut (OID = 59346) : 
--
CREATE VIEW sbnweb.ve_cartografia_aut_aut AS
SELECT ca.vid, ca.tp_responsabilita, ca.cd_relazione, ca.nota_tit_aut,
    ca.fl_incerto, ca.fl_superfluo, ca.cd_strumento_mus, ca.cd_livello_c,
    ca.tp_pubb_gov, ca.cd_colore, ca.cd_meridiano, ca.cd_supporto_fisico,
    ca.cd_tecnica, ca.cd_forma_ripr, ca.cd_forma_pubb, ca.cd_altitudine,
    ca.cd_tiposcala, ca.tp_scala, ca.scala_oriz, ca.scala_vert,
    ca.longitudine_ovest, ca.longitudine_est, ca.latitudine_nord,
    ca.latitudine_sud, ca.tp_immagine, ca.cd_forma_cart, ca.cd_piattaforma,
    ca.cd_categ_satellite, ca.bid, ca.isadn, ca.tp_materiale,
    ca.tp_record_uni, ca.cd_natura, ca.cd_paese, ca.cd_lingua_1,
    ca.cd_lingua_2, ca.cd_lingua_3, ca.aa_pubb_1, ca.aa_pubb_2,
    ca.tp_aa_pubb, ca.cd_genere_1, ca.cd_genere_2, ca.cd_genere_3,
    ca.cd_genere_4, ca.ky_cles1_t, ca.ky_cles2_t, ca.ky_clet1_t,
    ca.ky_clet2_t, ca.ky_cles1_ct, ca.ky_cles2_ct, ca.ky_clet1_ct,
    ca.ky_clet2_ct, ca.cd_livello, ca.fl_speciale, ca.isbd, ca.indice_isbd,
    ca.ky_editore, ca.cd_agenzia, ca.cd_norme_cat, ca.nota_inf_tit,
    ca.nota_cat_tit, ca.bid_link, ca.tp_link, ca.ute_ins, ca.ts_ins,
    ca.ute_var, ca.ts_var, ca.ute_forza_ins, ca.ute_forza_var, ca.fl_canc,
    at.tp_responsabilita AS tp_responsabilita_f, at.cd_relazione AS
    cd_relazione_f, at.fl_superfluo AS fl_superfluo_f, at.vid AS vid_f,
    at.ky_cautun AS ky_cautun_f, at.ky_auteur AS ky_auteur_f, at.ky_cles1_a
    AS ky_cles1_a_f, at.ky_cles2_a AS ky_cles2_a_f
FROM vl_cartografia_aut ca, vl_autore_tit at
WHERE (ca.bid = at.bid);

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
-- Definition for view ve_cartografia_aut_luo (OID = 59355) : 
--
CREATE VIEW sbnweb.ve_cartografia_aut_luo AS
SELECT ca.vid, ca.tp_responsabilita, ca.cd_relazione, ca.nota_tit_aut,
    ca.fl_incerto, ca.fl_superfluo, ca.cd_strumento_mus, ca.cd_livello_c,
    ca.tp_pubb_gov, ca.cd_colore, ca.cd_meridiano, ca.cd_supporto_fisico,
    ca.cd_tecnica, ca.cd_forma_ripr, ca.cd_forma_pubb, ca.cd_altitudine,
    ca.cd_tiposcala, ca.tp_scala, ca.scala_oriz, ca.scala_vert,
    ca.longitudine_ovest, ca.longitudine_est, ca.latitudine_nord,
    ca.latitudine_sud, ca.tp_immagine, ca.cd_forma_cart, ca.cd_piattaforma,
    ca.cd_categ_satellite, ca.bid, ca.isadn, ca.tp_materiale,
    ca.tp_record_uni, ca.cd_natura, ca.cd_paese, ca.cd_lingua_1,
    ca.cd_lingua_2, ca.cd_lingua_3, ca.aa_pubb_1, ca.aa_pubb_2,
    ca.tp_aa_pubb, ca.cd_genere_1, ca.cd_genere_2, ca.cd_genere_3,
    ca.cd_genere_4, ca.ky_cles1_t, ca.ky_cles2_t, ca.ky_clet1_t,
    ca.ky_clet2_t, ca.ky_cles1_ct, ca.ky_cles2_ct, ca.ky_clet1_ct,
    ca.ky_clet2_ct, ca.cd_livello, ca.fl_speciale, ca.isbd, ca.indice_isbd,
    ca.ky_editore, ca.cd_agenzia, ca.cd_norme_cat, ca.nota_inf_tit,
    ca.nota_cat_tit, ca.bid_link, ca.tp_link, ca.ute_ins, ca.ts_ins,
    ca.ute_var, ca.ts_var, ca.ute_forza_ins, ca.ute_forza_var, ca.fl_canc,
    lt.lid, lt.ky_norm_luogo
FROM vl_cartografia_aut ca, vl_luogo_tit lt
WHERE (ca.bid = lt.bid);

--
-- Definition for view vl_cartografia_cla (OID = 59360) : 
--
CREATE VIEW sbnweb.vl_cartografia_cla AS
SELECT tc.cd_sistema, tc.cd_edizione, tc.classe, c.cd_livello_c,
    c.tp_pubb_gov, c.cd_colore, c.cd_meridiano, c.cd_supporto_fisico,
    c.cd_tecnica, c.cd_forma_ripr, c.cd_forma_pubb, c.cd_altitudine,
    c.cd_tiposcala, c.tp_scala, c.scala_oriz, c.scala_vert,
    c.longitudine_ovest, c.longitudine_est, c.latitudine_nord,
    c.latitudine_sud, c.tp_immagine, c.cd_forma_cart, c.cd_piattaforma,
    c.cd_categ_satellite, c.bid, c.isadn, c.tp_materiale, c.tp_record_uni,
    c.cd_natura, c.cd_paese, c.cd_lingua_1, c.cd_lingua_2, c.cd_lingua_3,
    c.aa_pubb_1, c.aa_pubb_2, c.tp_aa_pubb, c.cd_genere_1, c.cd_genere_2,
    c.cd_genere_3, c.cd_genere_4, c.ky_cles1_t, c.ky_cles2_t, c.ky_clet1_t,
    c.ky_clet2_t, c.ky_cles1_ct, c.ky_cles2_ct, c.ky_clet1_ct,
    c.ky_clet2_ct, c.cd_livello, c.fl_speciale, c.isbd, c.indice_isbd,
    c.ky_editore, c.cd_agenzia, c.cd_norme_cat, c.nota_inf_tit,
    c.nota_cat_tit, c.bid_link, c.tp_link, c.ute_ins, c.ts_ins, c.ute_var,
    c.ts_var, c.ute_forza_ins, c.ute_forza_var, c.fl_canc, c.fl_condiviso,
    c.ts_condiviso, c.ute_condiviso
FROM tr_tit_cla tc, v_cartografia c
WHERE (((tc.bid = c.bid) AND (tc.fl_canc <> 'S'::bpchar)) AND (c.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_cartografia_cla_aut (OID = 59365) : 
--
CREATE VIEW sbnweb.ve_cartografia_cla_aut AS
SELECT cc.cd_sistema, cc.cd_edizione, cc.classe, cc.cd_livello_c,
    cc.tp_pubb_gov, cc.cd_colore, cc.cd_meridiano, cc.cd_supporto_fisico,
    cc.cd_tecnica, cc.cd_forma_ripr, cc.cd_forma_pubb, cc.cd_altitudine,
    cc.cd_tiposcala, cc.tp_scala, cc.scala_oriz, cc.scala_vert,
    cc.longitudine_ovest, cc.longitudine_est, cc.latitudine_nord,
    cc.latitudine_sud, cc.tp_immagine, cc.cd_forma_cart, cc.cd_piattaforma,
    cc.cd_categ_satellite, cc.bid, cc.isadn, cc.tp_materiale,
    cc.tp_record_uni, cc.cd_natura, cc.cd_paese, cc.cd_lingua_1,
    cc.cd_lingua_2, cc.cd_lingua_3, cc.aa_pubb_1, cc.aa_pubb_2,
    cc.tp_aa_pubb, cc.cd_genere_1, cc.cd_genere_2, cc.cd_genere_3,
    cc.cd_genere_4, cc.ky_cles1_t, cc.ky_cles2_t, cc.ky_clet1_t,
    cc.ky_clet2_t, cc.ky_cles1_ct, cc.ky_cles2_ct, cc.ky_clet1_ct,
    cc.ky_clet2_ct, cc.cd_livello, cc.fl_speciale, cc.isbd, cc.indice_isbd,
    cc.ky_editore, cc.cd_agenzia, cc.cd_norme_cat, cc.nota_inf_tit,
    cc.nota_cat_tit, cc.bid_link, cc.tp_link, cc.ute_ins, cc.ts_ins,
    cc.ute_var, cc.ts_var, cc.ute_forza_ins, cc.ute_forza_var, cc.fl_canc,
    at.tp_responsabilita, at.cd_relazione, at.fl_superfluo, at.vid,
    at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_cartografia_cla cc, vl_autore_tit at
WHERE (cc.bid = at.bid);

--
-- Definition for view ve_cartografia_cla_luo (OID = 59370) : 
--
CREATE VIEW sbnweb.ve_cartografia_cla_luo AS
SELECT cc.cd_sistema, cc.cd_edizione, cc.classe, cc.cd_livello_c,
    cc.tp_pubb_gov, cc.cd_colore, cc.cd_meridiano, cc.cd_supporto_fisico,
    cc.cd_tecnica, cc.cd_forma_ripr, cc.cd_forma_pubb, cc.cd_altitudine,
    cc.cd_tiposcala, cc.tp_scala, cc.scala_oriz, cc.scala_vert,
    cc.longitudine_ovest, cc.longitudine_est, cc.latitudine_nord,
    cc.latitudine_sud, cc.tp_immagine, cc.cd_forma_cart, cc.cd_piattaforma,
    cc.cd_categ_satellite, cc.bid, cc.isadn, cc.tp_materiale,
    cc.tp_record_uni, cc.cd_natura, cc.cd_paese, cc.cd_lingua_1,
    cc.cd_lingua_2, cc.cd_lingua_3, cc.aa_pubb_1, cc.aa_pubb_2,
    cc.tp_aa_pubb, cc.cd_genere_1, cc.cd_genere_2, cc.cd_genere_3,
    cc.cd_genere_4, cc.ky_cles1_t, cc.ky_cles2_t, cc.ky_clet1_t,
    cc.ky_clet2_t, cc.ky_cles1_ct, cc.ky_cles2_ct, cc.ky_clet1_ct,
    cc.ky_clet2_ct, cc.cd_livello, cc.fl_speciale, cc.isbd, cc.indice_isbd,
    cc.ky_editore, cc.cd_agenzia, cc.cd_norme_cat, cc.nota_inf_tit,
    cc.nota_cat_tit, cc.bid_link, cc.tp_link, cc.ute_ins, cc.ts_ins,
    cc.ute_var, cc.ts_var, cc.ute_forza_ins, cc.ute_forza_var, cc.fl_canc,
    lt.lid, lt.ky_norm_luogo
FROM vl_cartografia_cla cc, vl_luogo_tit lt
WHERE (cc.bid = lt.bid);

--
-- Definition for view vl_cartografia_luo (OID = 59375) : 
--
CREATE VIEW sbnweb.vl_cartografia_luo AS
SELECT tl.lid, tl.tp_luogo, tl.nota_tit_luo, c.cd_livello_c, c.tp_pubb_gov,
    c.cd_colore, c.cd_meridiano, c.cd_supporto_fisico, c.cd_tecnica,
    c.cd_forma_ripr, c.cd_forma_pubb, c.cd_altitudine, c.cd_tiposcala,
    c.tp_scala, c.scala_oriz, c.scala_vert, c.longitudine_ovest,
    c.longitudine_est, c.latitudine_nord, c.latitudine_sud, c.tp_immagine,
    c.cd_forma_cart, c.cd_piattaforma, c.cd_categ_satellite, c.bid,
    c.isadn, c.tp_materiale, c.tp_record_uni, c.cd_natura, c.cd_paese,
    c.cd_lingua_1, c.cd_lingua_2, c.cd_lingua_3, c.aa_pubb_1, c.aa_pubb_2,
    c.tp_aa_pubb, c.cd_genere_1, c.cd_genere_2, c.cd_genere_3,
    c.cd_genere_4, c.ky_cles1_t, c.ky_cles2_t, c.ky_clet1_t, c.ky_clet2_t,
    c.ky_cles1_ct, c.ky_cles2_ct, c.ky_clet1_ct, c.ky_clet2_ct,
    c.cd_livello, c.fl_speciale, c.isbd, c.indice_isbd, c.ky_editore,
    c.cd_agenzia, c.cd_norme_cat, c.nota_inf_tit, c.nota_cat_tit,
    c.bid_link, c.tp_link, c.ute_ins, c.ts_ins, c.ute_var, c.ts_var,
    c.ute_forza_ins, c.ute_forza_var, c.fl_canc, c.fl_condiviso,
    c.ts_condiviso, c.ute_condiviso
FROM tr_tit_luo tl, v_cartografia c
WHERE (((tl.bid = c.bid) AND (tl.fl_canc <> 'S'::bpchar)) AND (c.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_cartografia_luo_aut (OID = 59380) : 
--
CREATE VIEW sbnweb.ve_cartografia_luo_aut AS
SELECT cl.lid, cl.tp_luogo, cl.nota_tit_luo, cl.cd_livello_c,
    cl.tp_pubb_gov, cl.cd_colore, cl.cd_meridiano, cl.cd_supporto_fisico,
    cl.cd_tecnica, cl.cd_forma_ripr, cl.cd_forma_pubb, cl.cd_altitudine,
    cl.cd_tiposcala, cl.tp_scala, cl.scala_oriz, cl.scala_vert,
    cl.longitudine_ovest, cl.longitudine_est, cl.latitudine_nord,
    cl.latitudine_sud, cl.tp_immagine, cl.cd_forma_cart, cl.cd_piattaforma,
    cl.cd_categ_satellite, cl.bid, cl.isadn, cl.tp_materiale,
    cl.tp_record_uni, cl.cd_natura, cl.cd_paese, cl.cd_lingua_1,
    cl.cd_lingua_2, cl.cd_lingua_3, cl.aa_pubb_1, cl.aa_pubb_2,
    cl.tp_aa_pubb, cl.cd_genere_1, cl.cd_genere_2, cl.cd_genere_3,
    cl.cd_genere_4, cl.ky_cles1_t, cl.ky_cles2_t, cl.ky_clet1_t,
    cl.ky_clet2_t, cl.ky_cles1_ct, cl.ky_cles2_ct, cl.ky_clet1_ct,
    cl.ky_clet2_ct, cl.cd_livello, cl.fl_speciale, cl.isbd, cl.indice_isbd,
    cl.ky_editore, cl.cd_agenzia, cl.cd_norme_cat, cl.nota_inf_tit,
    cl.nota_cat_tit, cl.bid_link, cl.tp_link, cl.ute_ins, cl.ts_ins,
    cl.ute_var, cl.ts_var, cl.ute_forza_ins, cl.ute_forza_var, cl.fl_canc,
    at.tp_responsabilita, at.cd_relazione, at.fl_superfluo, at.vid,
    at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_cartografia_luo cl, vl_autore_tit at
WHERE (cl.bid = at.bid);

--
-- Definition for view vl_cartografia_mar (OID = 59385) : 
--
CREATE VIEW sbnweb.vl_cartografia_mar AS
SELECT tm.mid, tm.nota_tit_mar, c.cd_livello_c, c.tp_pubb_gov, c.cd_colore,
    c.cd_meridiano, c.cd_supporto_fisico, c.cd_tecnica, c.cd_forma_ripr,
    c.cd_forma_pubb, c.cd_altitudine, c.cd_tiposcala, c.tp_scala,
    c.scala_oriz, c.scala_vert, c.longitudine_ovest, c.longitudine_est,
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
    c.fl_canc, c.fl_condiviso, c.ts_condiviso, c.ute_condiviso
FROM tr_tit_mar tm, v_cartografia c
WHERE (((tm.bid = c.bid) AND (tm.fl_canc <> 'S'::bpchar)) AND (c.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_cartografia_mar_aut (OID = 59390) : 
--
CREATE VIEW sbnweb.ve_cartografia_mar_aut AS
SELECT cm.mid, cm.nota_tit_mar, cm.cd_livello_c, cm.tp_pubb_gov,
    cm.cd_colore, cm.cd_meridiano, cm.cd_supporto_fisico, cm.cd_tecnica,
    cm.cd_forma_ripr, cm.cd_forma_pubb, cm.cd_altitudine, cm.cd_tiposcala,
    cm.tp_scala, cm.scala_oriz, cm.scala_vert, cm.longitudine_ovest,
    cm.longitudine_est, cm.latitudine_nord, cm.latitudine_sud,
    cm.tp_immagine, cm.cd_forma_cart, cm.cd_piattaforma,
    cm.cd_categ_satellite, cm.bid, cm.isadn, cm.tp_materiale,
    cm.tp_record_uni, cm.cd_natura, cm.cd_paese, cm.cd_lingua_1,
    cm.cd_lingua_2, cm.cd_lingua_3, cm.aa_pubb_1, cm.aa_pubb_2,
    cm.tp_aa_pubb, cm.cd_genere_1, cm.cd_genere_2, cm.cd_genere_3,
    cm.cd_genere_4, cm.ky_cles1_t, cm.ky_cles2_t, cm.ky_clet1_t,
    cm.ky_clet2_t, cm.ky_cles1_ct, cm.ky_cles2_ct, cm.ky_clet1_ct,
    cm.ky_clet2_ct, cm.cd_livello, cm.fl_speciale, cm.isbd, cm.indice_isbd,
    cm.ky_editore, cm.cd_agenzia, cm.cd_norme_cat, cm.nota_inf_tit,
    cm.nota_cat_tit, cm.bid_link, cm.tp_link, cm.ute_ins, cm.ts_ins,
    cm.ute_var, cm.ts_var, cm.ute_forza_ins, cm.ute_forza_var, cm.fl_canc,
    at.tp_responsabilita, at.cd_relazione, at.fl_superfluo, at.vid,
    at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_cartografia_mar cm, vl_autore_tit at
WHERE (cm.bid = at.bid);

--
-- Definition for view ve_cartografia_mar_luo (OID = 59395) : 
--
CREATE VIEW sbnweb.ve_cartografia_mar_luo AS
SELECT cm.mid, cm.nota_tit_mar, cm.cd_livello_c, cm.tp_pubb_gov,
    cm.cd_colore, cm.cd_meridiano, cm.cd_supporto_fisico, cm.cd_tecnica,
    cm.cd_forma_ripr, cm.cd_forma_pubb, cm.cd_altitudine, cm.cd_tiposcala,
    cm.tp_scala, cm.scala_oriz, cm.scala_vert, cm.longitudine_ovest,
    cm.longitudine_est, cm.latitudine_nord, cm.latitudine_sud,
    cm.tp_immagine, cm.cd_forma_cart, cm.cd_piattaforma,
    cm.cd_categ_satellite, cm.bid, cm.isadn, cm.tp_materiale,
    cm.tp_record_uni, cm.cd_natura, cm.cd_paese, cm.cd_lingua_1,
    cm.cd_lingua_2, cm.cd_lingua_3, cm.aa_pubb_1, cm.aa_pubb_2,
    cm.tp_aa_pubb, cm.cd_genere_1, cm.cd_genere_2, cm.cd_genere_3,
    cm.cd_genere_4, cm.ky_cles1_t, cm.ky_cles2_t, cm.ky_clet1_t,
    cm.ky_clet2_t, cm.ky_cles1_ct, cm.ky_cles2_ct, cm.ky_clet1_ct,
    cm.ky_clet2_ct, cm.cd_livello, cm.fl_speciale, cm.isbd, cm.indice_isbd,
    cm.ky_editore, cm.cd_agenzia, cm.cd_norme_cat, cm.nota_inf_tit,
    cm.nota_cat_tit, cm.bid_link, cm.tp_link, cm.ute_ins, cm.ts_ins,
    cm.ute_var, cm.ts_var, cm.ute_forza_ins, cm.ute_forza_var, cm.fl_canc,
    lt.lid, lt.ky_norm_luogo
FROM vl_cartografia_mar cm, vl_luogo_tit lt
WHERE (cm.bid = lt.bid);

--
-- Definition for view vl_cartografia_sog (OID = 59400) : 
--
CREATE VIEW sbnweb.vl_cartografia_sog AS
SELECT ts.cid, c.cd_livello_c, c.tp_pubb_gov, c.cd_colore, c.cd_meridiano,
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
    c.fl_canc, c.fl_condiviso, c.ts_condiviso, c.ute_condiviso
FROM tr_tit_sog_bib ts, v_cartografia c
WHERE (((ts.bid = c.bid) AND (ts.fl_canc <> 'S'::bpchar)) AND (c.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_cartografia_sog_aut (OID = 59405) : 
--
CREATE VIEW sbnweb.ve_cartografia_sog_aut AS
SELECT cs.cid, cs.cd_livello_c, cs.tp_pubb_gov, cs.cd_colore,
    cs.cd_meridiano, cs.cd_supporto_fisico, cs.cd_tecnica,
    cs.cd_forma_ripr, cs.cd_forma_pubb, cs.cd_altitudine, cs.cd_tiposcala,
    cs.tp_scala, cs.scala_oriz, cs.scala_vert, cs.longitudine_ovest,
    cs.longitudine_est, cs.latitudine_nord, cs.latitudine_sud,
    cs.tp_immagine, cs.cd_forma_cart, cs.cd_piattaforma,
    cs.cd_categ_satellite, cs.bid, cs.isadn, cs.tp_materiale,
    cs.tp_record_uni, cs.cd_natura, cs.cd_paese, cs.cd_lingua_1,
    cs.cd_lingua_2, cs.cd_lingua_3, cs.aa_pubb_1, cs.aa_pubb_2,
    cs.tp_aa_pubb, cs.cd_genere_1, cs.cd_genere_2, cs.cd_genere_3,
    cs.cd_genere_4, cs.ky_cles1_t, cs.ky_cles2_t, cs.ky_clet1_t,
    cs.ky_clet2_t, cs.ky_cles1_ct, cs.ky_cles2_ct, cs.ky_clet1_ct,
    cs.ky_clet2_ct, cs.cd_livello, cs.fl_speciale, cs.isbd, cs.indice_isbd,
    cs.ky_editore, cs.cd_agenzia, cs.cd_norme_cat, cs.nota_inf_tit,
    cs.nota_cat_tit, cs.bid_link, cs.tp_link, cs.ute_ins, cs.ts_ins,
    cs.ute_var, cs.ts_var, cs.ute_forza_ins, cs.ute_forza_var, cs.fl_canc,
    at.tp_responsabilita, at.cd_relazione, at.fl_superfluo, at.vid,
    at.ky_auteur, at.ky_cautun, at.ky_cles1_a, at.ky_cles2_a
FROM vl_cartografia_sog cs, vl_autore_tit at
WHERE (cs.bid = at.bid);

--
-- Definition for view ve_cartografia_sog_luo (OID = 59410) : 
--
CREATE VIEW sbnweb.ve_cartografia_sog_luo AS
SELECT cs.cid, cs.cd_livello_c, cs.tp_pubb_gov, cs.cd_colore,
    cs.cd_meridiano, cs.cd_supporto_fisico, cs.cd_tecnica,
    cs.cd_forma_ripr, cs.cd_forma_pubb, cs.cd_altitudine, cs.cd_tiposcala,
    cs.tp_scala, cs.scala_oriz, cs.scala_vert, cs.longitudine_ovest,
    cs.longitudine_est, cs.latitudine_nord, cs.latitudine_sud,
    cs.tp_immagine, cs.cd_forma_cart, cs.cd_piattaforma,
    cs.cd_categ_satellite, cs.bid, cs.isadn, cs.tp_materiale,
    cs.tp_record_uni, cs.cd_natura, cs.cd_paese, cs.cd_lingua_1,
    cs.cd_lingua_2, cs.cd_lingua_3, cs.aa_pubb_1, cs.aa_pubb_2,
    cs.tp_aa_pubb, cs.cd_genere_1, cs.cd_genere_2, cs.cd_genere_3,
    cs.cd_genere_4, cs.ky_cles1_t, cs.ky_cles2_t, cs.ky_clet1_t,
    cs.ky_clet2_t, cs.ky_cles1_ct, cs.ky_cles2_ct, cs.ky_clet1_ct,
    cs.ky_clet2_ct, cs.cd_livello, cs.fl_speciale, cs.isbd, cs.indice_isbd,
    cs.ky_editore, cs.cd_agenzia, cs.cd_norme_cat, cs.nota_inf_tit,
    cs.nota_cat_tit, cs.bid_link, cs.tp_link, cs.ute_ins, cs.ts_ins,
    cs.ute_var, cs.ts_var, cs.ute_forza_ins, cs.ute_forza_var, cs.fl_canc,
    lt.lid, lt.ky_norm_luogo
FROM vl_cartografia_sog cs, vl_luogo_tit lt
WHERE (cs.bid = lt.bid);

--
-- Definition for view vl_cartografia_the (OID = 59415) : 
--
CREATE VIEW sbnweb.vl_cartografia_the AS
SELECT ts.did, c.cd_livello_c, c.tp_pubb_gov, c.cd_colore, c.cd_meridiano,
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
    c.fl_canc, c.fl_condiviso, c.ts_condiviso, c.ute_condiviso
FROM trs_termini_titoli_biblioteche ts, v_cartografia c
WHERE (((ts.bid = c.bid) AND (ts.fl_canc <> 'S'::bpchar)) AND (c.fl_canc <>
    'S'::bpchar));

--
-- Definition for view ve_cartografia_the_aut (OID = 59420) : 
--
CREATE VIEW sbnweb.ve_cartografia_the_aut AS
SELECT cs.did, cs.cd_livello_c, cs.tp_pubb_gov, cs.cd_colore,
    cs.cd_meridiano, cs.cd_supporto_fisico, cs.cd_tecnica,
    cs.cd_forma_ripr, cs.cd_forma_pubb, cs.cd_altitudine, cs.cd_tiposcala,
    cs.tp_scala, cs.scala_oriz, cs.scala_vert, cs.longitudine_ovest,
    cs.longitudine_est, cs.latitudine_nord, cs.latitudine_sud,
    cs.tp_immagine, cs.cd_forma_cart, cs.cd_piattaforma,
    cs.cd_categ_satellite, cs.bid, cs.isadn, cs.tp_materiale,
    cs.tp_record_uni, cs.cd_natura, cs.cd_paese, cs.cd_lingua_1,
    cs.cd_lingua_2, cs.cd_lingua_3, cs.aa_pubb_1, cs.aa_pubb_2,
    cs.tp_aa_pubb, cs.cd_genere_1, cs.cd_genere_2, cs.cd_genere_3,
    cs.cd_genere_4, cs.ky_cles1_t, cs.ky_cles2_t, cs.ky_clet1_t,
    cs.ky_clet2_t, cs.ky_cles1_ct, cs.ky_cles2_ct, cs.ky_clet1_ct,
    cs.ky_clet2_ct, cs.cd_livello, cs.fl_speciale, cs.isbd, cs.indice_isbd,
    cs.ky_editore, cs.cd_agenzia, cs.cd_norme_cat, cs.nota_inf_tit,
    cs.nota_cat_tit, cs.bid_link, cs.tp_link, cs.ute_ins, cs.ts_ins,
    cs.ute_var, cs.ts_var, cs.ute_forza_ins, cs.ute_forza_var, cs.fl_canc,
    at.tp_responsabilita, at.cd_relazione, at.fl_superfluo, at.vid,
    at.ky_auteur, at.ky_cautun, at.ky_cles1_a, at.ky_cles2_a
FROM vl_cartografia_the cs, vl_autore_tit at
WHERE (cs.bid = at.bid);

--
-- Definition for view ve_cartografia_the_luo (OID = 59425) : 
--
CREATE VIEW sbnweb.ve_cartografia_the_luo AS
SELECT cs.did, cs.cd_livello_c, cs.tp_pubb_gov, cs.cd_colore,
    cs.cd_meridiano, cs.cd_supporto_fisico, cs.cd_tecnica,
    cs.cd_forma_ripr, cs.cd_forma_pubb, cs.cd_altitudine, cs.cd_tiposcala,
    cs.tp_scala, cs.scala_oriz, cs.scala_vert, cs.longitudine_ovest,
    cs.longitudine_est, cs.latitudine_nord, cs.latitudine_sud,
    cs.tp_immagine, cs.cd_forma_cart, cs.cd_piattaforma,
    cs.cd_categ_satellite, cs.bid, cs.isadn, cs.tp_materiale,
    cs.tp_record_uni, cs.cd_natura, cs.cd_paese, cs.cd_lingua_1,
    cs.cd_lingua_2, cs.cd_lingua_3, cs.aa_pubb_1, cs.aa_pubb_2,
    cs.tp_aa_pubb, cs.cd_genere_1, cs.cd_genere_2, cs.cd_genere_3,
    cs.cd_genere_4, cs.ky_cles1_t, cs.ky_cles2_t, cs.ky_clet1_t,
    cs.ky_clet2_t, cs.ky_cles1_ct, cs.ky_cles2_ct, cs.ky_clet1_ct,
    cs.ky_clet2_ct, cs.cd_livello, cs.fl_speciale, cs.isbd, cs.indice_isbd,
    cs.ky_editore, cs.cd_agenzia, cs.cd_norme_cat, cs.nota_inf_tit,
    cs.nota_cat_tit, cs.bid_link, cs.tp_link, cs.ute_ins, cs.ts_ins,
    cs.ute_var, cs.ts_var, cs.ute_forza_ins, cs.ute_forza_var, cs.fl_canc,
    lt.lid, lt.ky_norm_luogo
FROM vl_cartografia_the cs, vl_luogo_tit lt
WHERE (cs.bid = lt.bid);

--
-- Definition for view vl_cartografia_tit_c (OID = 59430) : 
--
CREATE VIEW sbnweb.vl_cartografia_tit_c AS
SELECT tt.bid_coll, tt.tp_legame, tt.tp_legame_musica, tt.cd_natura_base,
    tt.cd_natura_coll, tt.sequenza, tt.nota_tit_tit, tt.sequenza_musica,
    tt.sici, tt.fl_condiviso AS fl_condiviso_legame, tt.ts_condiviso AS
    ts_condiviso_legame, tt.ute_condiviso AS ute_condiviso_legame,
    c.cd_livello_c, c.tp_pubb_gov, c.cd_colore, c.cd_meridiano,
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
    c.fl_canc, c.fl_condiviso, c.ts_condiviso, c.ute_condiviso
FROM tr_tit_tit tt, v_cartografia c
WHERE (((tt.bid_base = c.bid) AND (tt.fl_canc <> 'S'::bpchar)) AND
    (c.fl_canc <> 'S'::bpchar));

--
-- Definition for view ve_cartografia_tit_c_aut (OID = 59435) : 
--
CREATE VIEW sbnweb.ve_cartografia_tit_c_aut AS
SELECT ct.bid_coll, ct.tp_legame, ct.tp_legame_musica, ct.cd_natura_base,
    ct.cd_natura_coll, ct.sequenza, ct.nota_tit_tit, ct.sequenza_musica,
    ct.sici, ct.cd_livello_c, ct.tp_pubb_gov, ct.cd_colore,
    ct.cd_meridiano, ct.cd_supporto_fisico, ct.cd_tecnica,
    ct.cd_forma_ripr, ct.cd_forma_pubb, ct.cd_altitudine, ct.cd_tiposcala,
    ct.tp_scala, ct.scala_oriz, ct.scala_vert, ct.longitudine_ovest,
    ct.longitudine_est, ct.latitudine_nord, ct.latitudine_sud,
    ct.tp_immagine, ct.cd_forma_cart, ct.cd_piattaforma,
    ct.cd_categ_satellite, ct.bid, ct.isadn, ct.tp_materiale,
    ct.tp_record_uni, ct.cd_natura, ct.cd_paese, ct.cd_lingua_1,
    ct.cd_lingua_2, ct.cd_lingua_3, ct.aa_pubb_1, ct.aa_pubb_2,
    ct.tp_aa_pubb, ct.cd_genere_1, ct.cd_genere_2, ct.cd_genere_3,
    ct.cd_genere_4, ct.ky_cles1_t, ct.ky_cles2_t, ct.ky_clet1_t,
    ct.ky_clet2_t, ct.ky_cles1_ct, ct.ky_cles2_ct, ct.ky_clet1_ct,
    ct.ky_clet2_ct, ct.cd_livello, ct.fl_speciale, ct.isbd, ct.indice_isbd,
    ct.ky_editore, ct.cd_agenzia, ct.cd_norme_cat, ct.nota_inf_tit,
    ct.nota_cat_tit, ct.bid_link, ct.tp_link, ct.ute_ins, ct.ts_ins,
    ct.ute_var, ct.ts_var, ct.ute_forza_ins, ct.ute_forza_var, ct.fl_canc,
    at.tp_responsabilita, at.cd_relazione, at.fl_superfluo, at.vid,
    at.ky_cautun, at.ky_auteur, at.ky_cles1_a, at.ky_cles2_a
FROM vl_cartografia_tit_c ct, vl_autore_tit at
WHERE (ct.bid = at.bid);

--
-- Definition for view ve_cartografia_tit_c_luo (OID = 59440) : 
--
CREATE VIEW sbnweb.ve_cartografia_tit_c_luo AS
SELECT ct.bid_coll, ct.tp_legame, ct.tp_legame_musica, ct.cd_natura_base,
    ct.cd_natura_coll, ct.sequenza, ct.nota_tit_tit, ct.sequenza_musica,
    ct.sici, ct.cd_livello_c, ct.tp_pubb_gov, ct.cd_colore,
    ct.cd_meridiano, ct.cd_supporto_fisico, ct.cd_tecnica,
    ct.cd_forma_ripr, ct.cd_forma_pubb, ct.cd_altitudine, ct.cd_tiposcala,
    ct.tp_scala, ct.scala_oriz, ct.scala_vert, ct.longitudine_ovest,
    ct.longitudine_est, ct.latitudine_nord, ct.latitudine_sud,
    ct.tp_immagine, ct.cd_forma_cart, ct.cd_piattaforma,
    ct.cd_categ_satellite, ct.bid, ct.isadn, ct.tp_materiale,
    ct.tp_record_uni, ct.cd_natura, ct.cd_paese, ct.cd_lingua_1,
    ct.cd_lingua_2, ct.cd_lingua_3, ct.aa_pubb_1, ct.aa_pubb_2,
    ct.tp_aa_pubb, ct.cd_genere_1, ct.cd_genere_2, ct.cd_genere_3,
    ct.cd_genere_4, ct.ky_cles1_t, ct.ky_cles2_t, ct.ky_clet1_t,
    ct.ky_clet2_t, ct.ky_cles1_ct, ct.ky_cles2_ct, ct.ky_clet1_ct,
    ct.ky_clet2_ct, ct.cd_livello, ct.fl_speciale, ct.isbd, ct.indice_isbd,
    ct.ky_editore, ct.cd_agenzia, ct.cd_norme_cat, ct.nota_inf_tit,
    ct.nota_cat_tit, ct.bid_link, ct.tp_link, ct.ute_ins, ct.ts_ins,
    ct.ute_var, ct.ts_var, ct.ute_forza_ins, ct.ute_forza_var, ct.fl_canc,
    lt.lid, lt.ky_norm_luogo
FROM vl_cartografia_tit_c ct, vl_luogo_tit lt
WHERE (ct.bid = lt.bid);

--
-- Definition for view vl_grafica_aut (OID = 59445) : 
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
-- Definition for view ve_grafica_aut_aut (OID = 59450) : 
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
-- Definition for view ve_grafica_aut_luo (OID = 59455) : 
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
-- Definition for view ve_grafica_cla_aut (OID = 59465) : 
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
-- Definition for view ve_grafica_luo_aut (OID = 59480) : 
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
-- Definition for view ve_grafica_mar_aut (OID = 59490) : 
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
-- Definition for view ve_grafica_sog_aut (OID = 59505) : 
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
-- Definition for view ve_grafica_the_aut (OID = 59520) : 
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
-- Definition for view ve_grafica_tit_c_aut (OID = 59535) : 
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
-- Definition for view vl_musica_aut (OID = 59545) : 
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
-- Definition for view ve_musica_aut_aut (OID = 59550) : 
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
-- Definition for view ve_musica_aut_com (OID = 59560) : 
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
-- Definition for view ve_musica_aut_luo (OID = 59565) : 
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
-- Definition for view ve_musica_cla_aut (OID = 59575) : 
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
-- Definition for view ve_musica_luo_aut (OID = 59595) : 
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
-- Definition for view ve_musica_mar_aut (OID = 59610) : 
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
-- Definition for view ve_musica_tit_c_aut (OID = 59630) : 
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
-- Definition for view vl_titolo_aut (OID = 59645) : 
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
-- Definition for view ve_titolo_aut_aut (OID = 59650) : 
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
-- Definition for view ve_titolo_aut_luo (OID = 59655) : 
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
-- Definition for view ve_titolo_aut_mar (OID = 59664) : 
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
-- Definition for view vl_titolo_num_std (OID = 59669) : 
--
CREATE VIEW sbnweb.vl_titolo_num_std(
    bid,
    tp_numero_std,
    numero_std,
    numero_lastra,
    cd_paese_std,
    nota_numero_std,
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
    fl_canc)
AS
  SELECT tb_numero_std.bid,
         tb_numero_std.tp_numero_std,
         tb_numero_std.numero_std,
         tb_numero_std.numero_lastra,
         tb_numero_std.cd_paese AS cd_paese_std,
         tb_numero_std.nota_numero_std,
         tb_titolo.isadn,
         tb_titolo.tp_materiale,
         tb_titolo.tp_record_uni,
         tb_titolo.cd_natura,
         tb_titolo.cd_paese,
         tb_titolo.cd_lingua_1,
         tb_titolo.cd_lingua_2,
         tb_titolo.cd_lingua_3,
         tb_titolo.aa_pubb_1,
         tb_titolo.aa_pubb_2,
         tb_titolo.tp_aa_pubb,
         tb_titolo.cd_genere_1,
         tb_titolo.cd_genere_2,
         tb_titolo.cd_genere_3,
         tb_titolo.cd_genere_4,
         tb_titolo.ky_cles1_t,
         tb_titolo.ky_cles2_t,
         tb_titolo.ky_clet1_t,
         tb_titolo.ky_clet2_t,
         tb_titolo.ky_cles1_ct,
         tb_titolo.ky_cles2_ct,
         tb_titolo.ky_clet1_ct,
         tb_titolo.ky_clet2_ct,
         tb_titolo.cd_livello,
         tb_titolo.fl_speciale,
         tb_titolo.isbd,
         tb_titolo.indice_isbd,
         tb_titolo.ky_editore,
         tb_titolo.cd_agenzia,
         tb_titolo.cd_norme_cat,
         tb_titolo.nota_cat_tit,
         tb_titolo.nota_inf_tit,
         tb_titolo.bid_link,
         tb_titolo.tp_link,
         tb_titolo.ute_ins,
         tb_titolo.ts_ins,
         tb_titolo.ute_var,
         tb_titolo.ts_var,
         tb_titolo.ute_forza_ins,
         tb_titolo.ute_forza_var,
         tb_titolo.fl_canc
  FROM tb_numero_std,
       tb_titolo
  WHERE tb_numero_std.bid = tb_titolo.bid AND
        tb_numero_std.fl_canc <> 'S' AND
        tb_titolo.fl_canc <> 'S';

--
-- Definition for view ve_titolo_aut_nstd (OID = 59674) : 
--
CREATE VIEW sbnweb.ve_titolo_aut_nstd(
    bid,
    tp_numero_std,
    numero_std,
    numero_lastra,
    cd_paese_std,
    nota_numero_std,
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
    ky_cles1_a,
    ky_cles2_a)
AS
  SELECT a.bid,
         a.tp_numero_std,
         a.numero_std,
         a.numero_lastra,
         a.cd_paese_std,
         a.nota_numero_std,
         a.isadn,
         a.tp_materiale,
         a.tp_record_uni,
         a.cd_natura,
         a.cd_paese,
         a.cd_lingua_1,
         a.cd_lingua_2,
         a.cd_lingua_3,
         a.aa_pubb_1,
         a.aa_pubb_2,
         a.tp_aa_pubb,
         a.cd_genere_1,
         a.cd_genere_2,
         a.cd_genere_3,
         a.cd_genere_4,
         a.ky_cles1_t,
         a.ky_cles2_t,
         a.ky_clet1_t,
         a.ky_clet2_t,
         a.ky_cles1_ct,
         a.ky_cles2_ct,
         a.ky_clet1_ct,
         a.ky_clet2_ct,
         a.cd_livello,
         a.fl_speciale,
         a.isbd,
         a.indice_isbd,
         a.ky_editore,
         a.cd_agenzia,
         a.cd_norme_cat,
         a.nota_cat_tit,
         a.nota_inf_tit,
         a.bid_link,
         a.tp_link,
         a.ute_ins,
         a.ts_ins,
         a.ute_var,
         a.ts_var,
         a.ute_forza_ins,
         a.ute_forza_var,
         a.fl_canc,
         b.ky_cles1_a,
         b.ky_cles2_a
  FROM vl_titolo_num_std a,
       vl_autore_tit b
  WHERE a.bid = b.bid;          


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
-- Definition for view ve_titolo_cla_aut (OID = 59684) : 
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
-- Definition for view ve_titolo_luo_aut (OID = 59704) : 
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
-- Definition for view ve_titolo_mar_aut (OID = 59719) : 
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
-- Definition for view ve_titolo_sog_aut (OID = 59734) : 
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
-- Definition for view ve_titolo_the_aut (OID = 59754) : 
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
-- Definition for view ve_titolo_tit_c_aut (OID = 59774) : 
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
-- Definition for view vf_cartografia_aut (OID = 59789) : 
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
-- Definition for view vf_cartografia_luo (OID = 59794) : 
--
CREATE VIEW sbnweb.vf_cartografia_luo AS
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
    c.fl_canc, l.lid, l.ky_norm_luogo
FROM v_cartografia c, tr_tit_luo tl, tb_luogo l
WHERE (((((tl.bid = c.bid) AND (l.lid = tl.lid)) AND (c.fl_canc <>
    'S'::bpchar)) AND (tl.fl_canc <> 'S'::bpchar)) AND (l.fl_canc <> 'S'::bpchar));

--
-- Definition for view vf_grafica_aut (OID = 59799) : 
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
-- Definition for view vf_musica_aut (OID = 59809) : 
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
-- Definition for view vf_musica_comaut (OID = 59819) : 
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
-- Definition for view vf_titolo_aut (OID = 59829) : 
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
    tb_classe.ute_condiviso, tr_tit_cla.bid
FROM tr_tit_cla, tb_classe
WHERE (((((tr_tit_cla.cd_sistema = tb_classe.cd_sistema) AND
    (tr_tit_cla.cd_edizione = tb_classe.cd_edizione)) AND
    (tr_tit_cla.classe = tb_classe.classe)) AND (tr_tit_cla.fl_canc <>
    'S'::bpchar)) AND (tb_classe.fl_canc <> 'S'::bpchar));

--
-- Definition for view vl_composizione_aut (OID = 59917) : 
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
-- Definition for view vl_export_doc (OID = 59946) : 
--
CREATE VIEW sbnweb.vl_export_doc AS
((
SELECT DISTINCT t.bid, t.isadn, t.tp_materiale, t.tp_record_uni,
    t.cd_natura, t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3,
    t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2,
    t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t,
    t.ky_clet2_t, t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct,
    t.ky_clet2_ct, t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd,
    t.ky_editore, t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit,
    t.nota_cat_tit, t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var,
    t.ts_var, t.ute_forza_ins, t.ute_forza_var, t.fl_canc,
    t.cd_periodicita, t.fl_condiviso, t.ute_condiviso, t.ts_condiviso,
    NULL::timestamp without time zone AS vl_autore_tit_ts_var, NULL::text
    AS vid, tb.fl_possesso, tb.fl_gestione, tb.cd_polo, tb.cd_biblioteca
FROM tb_titolo t, tr_tit_bib tb
WHERE ((t.bid = tb.bid) AND (((((t.cd_natura = 'M'::bpchar) OR (t.cd_natura
    = 'S'::bpchar)) OR (t.cd_natura = 'W'::bpchar)) OR (t.cd_natura =
    'N'::bpchar)) OR (t.cd_natura = 'C'::bpchar)))
ORDER BY t.bid, t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura,
    t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1,
    t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3,
    t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t,
    t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit, t.nota_cat_tit,
    t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var,
    t.ute_forza_ins, t.ute_forza_var, t.fl_canc, t.cd_periodicita,
    t.fl_condiviso, t.ute_condiviso, t.ts_condiviso, NULL::timestamp
    without time zone, NULL::text, tb.fl_possesso, tb.fl_gestione,
    tb.cd_polo, tb.cd_biblioteca)
UNION (
SELECT DISTINCT t.bid, t.isadn, t.tp_materiale, t.tp_record_uni,
    t.cd_natura, t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3,
    t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2,
    t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t,
    t.ky_clet2_t, t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct,
    t.ky_clet2_ct, t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd,
    t.ky_editore, t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit,
    t.nota_cat_tit, t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var,
    t.ts_var, t.ute_forza_ins, t.ute_forza_var, t.fl_canc,
    t.cd_periodicita, t.fl_condiviso, t.ute_condiviso, t.ts_condiviso,
    va.ts_var AS vl_autore_tit_ts_var, va.vid, tb.fl_possesso,
    tb.fl_gestione, tb.cd_polo, tb.cd_biblioteca
FROM tb_titolo t, tr_tit_bib tb, vl_autore_tit va
WHERE (((t.bid = tb.bid) AND (t.bid = va.bid)) AND (((((t.cd_natura =
    'M'::bpchar) OR (t.cd_natura = 'S'::bpchar)) OR (t.cd_natura =
    'W'::bpchar)) OR (t.cd_natura = 'N'::bpchar)) OR (t.cd_natura = 'C'::bpchar)))
ORDER BY t.bid, t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura,
    t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1,
    t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3,
    t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t,
    t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit, t.nota_cat_tit,
    t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var,
    t.ute_forza_ins, t.ute_forza_var, t.fl_canc, t.cd_periodicita,
    t.fl_condiviso, t.ute_condiviso, t.ts_condiviso, va.ts_var, va.vid,
    tb.fl_possesso, tb.fl_gestione, tb.cd_polo, tb.cd_biblioteca))
UNION (
SELECT DISTINCT t.bid, t.isadn, t.tp_materiale, t.tp_record_uni,
    t.cd_natura, t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3,
    t.aa_pubb_1, t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2,
    t.cd_genere_3, t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t,
    t.ky_clet2_t, t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct,
    t.ky_clet2_ct, t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd,
    t.ky_editore, t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit,
    t.nota_cat_tit, t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var,
    t.ts_var, t.ute_forza_ins, t.ute_forza_var, t.fl_canc,
    t.cd_periodicita, t.fl_condiviso, t.ute_condiviso, t.ts_condiviso,
    NULL::timestamp without time zone AS vl_autore_tit_ts_var, NULL::text
    AS vid, tb.fl_possesso, tb.fl_gestione, tb.cd_polo, tb.cd_biblioteca
FROM tb_titolo t, tr_tit_bib tb
WHERE (((t.bid = tb.bid) AND (((((t.cd_natura = 'M'::bpchar) OR
    (t.cd_natura = 'S'::bpchar)) OR (t.cd_natura = 'W'::bpchar)) OR
    (t.cd_natura = 'N'::bpchar)) OR (t.cd_natura = 'C'::bpchar))) AND
    (((tb.ts_var = tb.ts_ins) OR ((tb.ts_ins <> tb.ts_var) AND
    ((tb.ds_consistenza)::text > ' '::text))) OR (tb.fl_canc = 'S'::bpchar)))
ORDER BY t.bid, t.isadn, t.tp_materiale, t.tp_record_uni, t.cd_natura,
    t.cd_paese, t.cd_lingua_1, t.cd_lingua_2, t.cd_lingua_3, t.aa_pubb_1,
    t.aa_pubb_2, t.tp_aa_pubb, t.cd_genere_1, t.cd_genere_2, t.cd_genere_3,
    t.cd_genere_4, t.ky_cles1_t, t.ky_cles2_t, t.ky_clet1_t, t.ky_clet2_t,
    t.ky_cles1_ct, t.ky_cles2_ct, t.ky_clet1_ct, t.ky_clet2_ct,
    t.cd_livello, t.fl_speciale, t.isbd, t.indice_isbd, t.ky_editore,
    t.cd_agenzia, t.cd_norme_cat, t.nota_inf_tit, t.nota_cat_tit,
    t.bid_link, t.tp_link, t.ute_ins, t.ts_ins, t.ute_var, t.ts_var,
    t.ute_forza_ins, t.ute_forza_var, t.fl_canc, t.cd_periodicita,
    t.fl_condiviso, t.ute_condiviso, t.ts_condiviso, NULL::timestamp
    without time zone, NULL::text, tb.fl_possesso, tb.fl_gestione,
    tb.cd_polo, tb.cd_biblioteca);

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
    tb_luogo.ute_var, tb_luogo.ts_var, tb_luogo.fl_canc
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
-- Definition for view v_catalogo_editoria
--
CREATE VIEW sbnweb.v_catalogo_editoria (
    bid,
    isbd,
    indice_isbd,
    tp_aa_pubb,
    aa_pubb_1,
    aa_pubb_2,
    cd_lingua_1,
    cd_lingua_2,
    cd_lingua_3,
    cd_natura,
    tp_record_uni,
    ky_cles1_t,
    ky_cles2_t,
    fl_condiviso,
    isbn,
    cod_fornitore,
    nom_fornitore,
    paese,
    cod_regione,
    ds_regione,
    provincia,
    ds_provincia,
    cap,
    citta,
    chiave_for,
    isbn_editore,
    nota_legame)
AS
SELECT tit.bid,
            tit.isbd,
            tit.indice_isbd,
            tit.tp_aa_pubb,
            tit.aa_pubb_1,
            tit.aa_pubb_2,
            tit.cd_lingua_1,
            tit.cd_lingua_2,
            tit.cd_lingua_3,
            tit.cd_natura,
            tit.tp_record_uni,
            tit.ky_cles1_t,
            tit.ky_cles2_t,
            tit.fl_condiviso,
            nst.numero_std AS isbn,
            ff.cod_fornitore,
            ff.nom_fornitore,
            ff.paese,
            cod.cd_flg2 AS cod_regione,
            cod.cd_flg3 AS ds_regione,
            ff.provincia,
            cod.ds_tabella AS ds_provincia,
            ff.cap,
            ff.citta,
            ff.chiave_for,
            ens.numero_std AS isbn_editore,
            '[Legame implicito non modificabile]' AS nota_legame
FROM tb_numero_std nst
      JOIN tb_titolo tit ON tit.bid = nst.bid AND NOT tit.fl_canc = 'S'
   JOIN tbr_editore_num_std ens ON regexp_replace(ens.numero_std,
       '^978', '') =
       "substring"(regexp_replace(nst.numero_std, '^978',
       ''), 1, length(regexp_replace(ens.numero_std, '^978',
       ''))) AND NOT ens.fl_canc = 'S'
   JOIN tbr_fornitori ff ON ff.cod_fornitore = ens.cod_fornitore AND NOT
       ff.fl_canc = 'S'
   JOIN tb_codici cod ON cod.tp_tabella = 'RPRV' AND cod.cd_tabella =
       ff.provincia
WHERE NOT nst.fl_canc = 'S' AND nst.tp_numero_std = 'I' AND NOT
    tit.fl_canc = 'S' AND NOT ff.fl_canc = 'S'
UNION
SELECT tit.bid,
            tit.isbd,
            tit.indice_isbd,
            tit.tp_aa_pubb,
            tit.aa_pubb_1,
            tit.aa_pubb_2,
            tit.cd_lingua_1,
            tit.cd_lingua_2,
            tit.cd_lingua_3,
            tit.cd_natura,
            tit.tp_record_uni,
            tit.ky_cles1_t,
            tit.ky_cles2_t,
            tit.fl_condiviso,
            '' AS isbn,
            ff.cod_fornitore,
            ff.nom_fornitore,
            ff.paese,
            cod.cd_tabella AS cod_regione,
            cod.ds_tabella AS ds_regione,
            ff.provincia,
            ' ' AS ds_provincia,
            ff.cap,
            ff.citta,
            ff.chiave_for,
            '' AS isbn_editore,
            edt.nota_legame
FROM tr_editore_titolo edt
      JOIN tb_titolo tit ON tit.bid = edt.bid
   JOIN tbr_fornitori ff ON edt.cod_fornitore = ff.cod_fornitore
   JOIN tb_codici cod ON cod.tp_tabella = 'RREG' AND cod.cd_tabella = ff.regione
WHERE NOT edt.fl_canc = 'S' AND NOT tit.fl_canc = 'S' AND NOT
    ff.fl_canc = 'S';
	
--
-- Definition for view vl_richiesta_servizio
--
CREATE VIEW sbnweb.vl_richiesta_servizio
AS
 SELECT ts.cd_polo,
         ts.cd_bib,
         rs.cod_rich_serv,
         rs.cd_polo_inv,
         rs.cod_bib_inv,
         rs.cod_serie_inv,
         rs.cod_inven_inv,
         rs.id_documenti_lettore,
         rs.id_esemplare_documenti_lettore,
         rs.id_utenti_biblioteca,
         rs.id_modalita_pagamento,
         rs.id_supporti_biblioteca,
         rs.id_servizio,
         rs.id_iter_servizio,
         rs.fl_tipo_rec,
         rs.note_bibliotecario,
         rs.costo_servizio,
         rs.num_fasc,
         rs.num_volume,
         rs.anno_period,
         rs.cod_tipo_serv_rich,
         rs.cod_tipo_serv_alt,
         rs.cod_stato_rich,
         rs.cod_stato_mov,
         rs.data_in_eff,
         rs.data_fine_eff,
         rs.num_rinnovi,
         rs.data_richiesta,
         rs.num_pezzi,
         rs.note_ut,
         rs.prezzo_max,
         rs.data_massima,
         rs.data_proroga,
         rs.data_in_prev,
         rs.data_fine_prev,
         rs.fl_svolg,
         rs.copyright,
         rs.cod_erog,
         rs.cod_risp,
         rs.fl_condiz,
         rs.fl_inoltro,
         rs.cod_bib_dest,
         rs.cod_bib_prelievo,
         rs.cod_bib_restituzione,
         rs.ute_ins,
         rs.ts_ins,
         rs.ute_var,
         rs.ts_var,
         rs.fl_canc,
         ts.cod_tipo_serv,
         iter.progr_iter,
         iter.cod_attivita,

         (rs.id_documenti_lettore IS NULL) as fl_inventario,

         CASE
           WHEN rs.id_documenti_lettore IS NULL THEN
           		t.isbd
           ELSE
           		dl.titolo
         END AS isbd,

         t.indice_isbd,

         CASE
           WHEN rs.id_documenti_lettore IS NULL THEN
           		t.cd_natura
           ELSE
           		dl.natura
         END AS cd_natura,

         CASE
           WHEN rs.id_documenti_lettore IS NULL THEN
           		t.ky_cles1_t||t.ky_cles2_t
           ELSE
           		null
         END AS kcles,

         CASE
           WHEN rs.id_documenti_lettore IS NULL THEN
           		t.bid
           ELSE
           		dl.bid
         END AS bid,

         dl.cd_bib as bib_doc_lett,
         dl.tipo_doc_lett,
         dl.cod_doc_lett,
         dl.segnatura,
         c.cd_sez,
         c.cd_loc,
         c.spec_loc,
         i.seq_coll,
         i.anno_abb,
         u.cognome,
         u.nome,
         sol.progr_sollecito,
         sol.data_invio
  FROM tbl_richiesta_servizio rs
       JOIN tbl_iter_servizio iter ON iter.id_iter_servizio = rs.id_iter_servizio
       JOIN tbl_tipo_servizio ts ON ts.id_tipo_servizio = iter.id_tipo_servizio
       join trl_utenti_biblioteca ub on ub.id_utenti_biblioteca=rs.id_utenti_biblioteca
       join tbl_utenti u on u.id_utenti=ub.id_utenti

       left join tbc_inventario i on i.cd_polo=rs.cd_polo_inv
                and i.cd_bib=rs.cod_bib_inv
                and i.cd_serie=rs.cod_serie_inv
                and i.cd_inven=rs.cod_inven_inv

       left join tb_titolo t on t.bid=i.bid

       left join tbc_collocazione c on c.key_loc=i.key_loc

       left join tbl_documenti_lettori dl on dl.id_documenti_lettore=rs.id_documenti_lettore

       left join (select s.cod_rich_serv, s.progr_sollecito, s.data_invio
       		from tbl_solleciti s
            where s.esito='S'
            and s.fl_canc<>'S'
            order by s.data_invio desc
       ) as sol
       on sol.cod_rich_serv=rs.cod_rich_serv;
	   
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
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence tb_loc_indice_id_loc_seq 
--
CREATE SEQUENCE sbnweb.tb_loc_indice_id_loc_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
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
-- Definition for sequence import1_id_seq (OID = 186470) : 
--
CREATE SEQUENCE sbnweb.import1_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table import1 (OID = 186472) : 
--
CREATE TABLE sbnweb.import1 (
    id integer DEFAULT nextval('import1_id_seq'::regclass) NOT NULL,
    id_input character varying(30),
    leader character varying(24),
    tag character varying(3),
    indicatore1 character(1),
    indicatore2 character(1),
    id_link character varying(30),
    dati text,
    nr_richiesta numeric,
    stato_id_input character(1),
    stato_tag character(1),
    id_batch numeric
);
--
-- Definition for sequence import950_id_seq (OID = 186482) : 
--
CREATE SEQUENCE sbnweb.import950_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table import950 (OID = 186484) : 
--
CREATE TABLE sbnweb.import950 (
    id serial NOT NULL,
    id_import1 integer NOT NULL,
    id_input character varying(30),
    dati text,
    error numeric(1,0) NOT NULL,
    msg_error text,
    nr_richiesta_import numeric NOT NULL,
    nr_richiesta numeric NOT NULL
);
--
-- Definition for sequence import_id_link_id_record_seq (OID = 186493) : 
--
CREATE SEQUENCE sbnweb.import_id_link_id_record_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table import_id_link (OID = 186495) : 
--
CREATE TABLE sbnweb.import_id_link (
    id_record integer DEFAULT nextval('import_id_link_id_record_seq'::regclass) NOT NULL,
    id_input character varying(300),
    id_inserito character varying(50),
    fl_stato character(1),
    ute_ins character varying(12),
    ts_ins timestamp(6) without time zone,
    nr_richiesta numeric
) WITHOUT OIDS;

--
-- Definition for sequence tb_report_indice_id_seq (OID = 186503) : 
--
CREATE SEQUENCE sbnweb.tb_report_indice_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tb_report_indice (OID = 186505) : 
--
CREATE TABLE sbnweb.tb_report_indice (
    id integer DEFAULT nextval('tb_report_indice_id_seq'::regclass) NOT NULL,
    nome_lista character varying(50) NOT NULL,
    fl_canc character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone DEFAULT now() NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone DEFAULT now() NOT NULL,
    data_prod_lista character(10)
) WITHOUT OIDS;
--
-- Definition for sequence tb_report_indice_id_locali_id_seq (OID = 186513) : 
--
CREATE SEQUENCE sbnweb.tb_report_indice_id_locali_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tb_report_indice_id_locali (OID = 186515) : 
--
CREATE TABLE sbnweb.tb_report_indice_id_locali (
    id integer DEFAULT nextval('tb_report_indice_id_locali_id_seq'::regclass) NOT NULL,
    id_lista integer,
    id_oggetto_locale character(10),
    risultato_confronto character(1),
    stato_lavorazione character(1),
    tipo_trattamento_selezionato character(1),
    id_arrivo_fusione character(10),
    fl_canc character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone DEFAULT now() NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone DEFAULT now() NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tb_report_indice_id_arrivo_id_seq (OID = 186529) : 
--
CREATE SEQUENCE sbnweb.tb_report_indice_id_arrivo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tb_report_indice_id_arrivo (OID = 186531) : 
--
CREATE TABLE sbnweb.tb_report_indice_id_arrivo (
    id integer DEFAULT nextval('tb_report_indice_id_arrivo_id_seq'::regclass) NOT NULL,
    id_lista_ogg_loc integer NOT NULL,
    id_oggetto_locale character(10),
    id_arrivo_fusione character(10),
    tipologia_uguaglianza character(1),
    fl_canc character(1) NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone DEFAULT now() NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone DEFAULT now() NOT NULL
) WITHOUT OIDS;
--
-- Definition for sequence tbp_modello_previsionale_id_modello_seq (OID = 186544) : 
--
CREATE SEQUENCE sbnweb.tbp_modello_previsionale_id_modello_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tbp_modello_previsionale (OID = 186546) : 
--
CREATE TABLE sbnweb.tbp_modello_previsionale (
    id_modello integer DEFAULT nextval('tbp_modello_previsionale_id_modello_seq'::regclass) NOT NULL,
    nome_modello character varying(50) NOT NULL,
    descrizione character varying(255),
    xml_model text NOT NULL,
    ute_ins character(12) NOT NULL,
    ts_ins timestamp without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp without time zone NOT NULL,
    fl_canc character(1) DEFAULT 'N'::bpchar NOT NULL
);
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
    cart_name character varying(255),
    ute_ins character(12) NOT NULL,
    ts_ins timestamp(6) without time zone NOT NULL,
    ute_var character(12) NOT NULL,
    ts_var timestamp(6) without time zone NOT NULL,
    fl_canc character(1) NOT NULL
) WITHOUT OIDS;
--
--
CREATE OR REPLACE FUNCTION "sbnweb"."calcola_import_id_link" (import1_tag varchar, import1_dati text, import1_210 text) RETURNS varchar AS
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
LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;


--

CREATE OR REPLACE FUNCTION "sbnweb"."calcola_import_id_link" (import1_tag varchar, import1_dati text) RETURNS varchar AS
$body$
DECLARE
BEGIN
return  calcola_import_id_link ($1, $2, '');
END
$body$
LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;