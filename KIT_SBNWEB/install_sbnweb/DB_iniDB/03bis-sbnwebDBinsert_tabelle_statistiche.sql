
/* tbf_config_statistiche - statistiche */

BEGIN;

SET search_path = sbnweb, pg_catalog;

INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(1, '00_GestBib', NULL, 3, 'IC000', NULL, 0, NULL, NULL, NULL, NULL, 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(2, '00_GestSem', NULL, 4, 'IC002', NULL, 0, NULL, NULL, NULL, NULL, 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(3, '00_GDocFis', NULL, 5, 'C0000', NULL, 0, NULL, NULL, NULL, NULL, 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(4, '00_GAcq', NULL, 6, 'A0000', NULL, 0, NULL, NULL, NULL, NULL, 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(5, '00_GSer', NULL, 7, 'L0000', NULL, 0, NULL, NULL, NULL, NULL, 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(8, '00_CatPolo1', '00_CatPolo', 1, 'X0000', NULL, NULL, 'Consistenza_catalogo', '0', '(select ''Titoli condivisi > 04'', count(tb) as cnt from Tb_titolo tb 
where tb.fl_canc <> ''S'' and tb.fl_condiviso=''s'' 
and tb.cd_livello <> ''01'' and tb.cd_livello <> ''04''
UNION  
select ''Titoli locali > 04'', count(tb) as cnt from Tb_titolo tb 
where tb.fl_canc <> ''S'' and tb.fl_condiviso=''n'' 
and tb.cd_livello <> ''01'' and tb.cd_livello <> ''04''
UNION
select ''Titoli da 01 a 04'', count(tb) as cnt from Tb_titolo tb 
where tb.fl_canc <> ''S'' and tb.cd_livello in (''01'', ''04'')
UNION
select ''Autori'', count(au) as cnt from tb_autore au 
where au.fl_canc <> ''S'' 
UNION
select ''Marche'', count(ma) as cnt from tb_marca ma 
where ma.fl_canc <> ''S'' 
UNION
select ''Luoghi'', count(lu) as cnt from tb_luogo lu 
where lu.fl_canc <> ''S'' 
UNION
select ''Soggetti'', count(so) as cnt from tb_soggetto so 
where so.fl_canc <> ''S'' 
UNION
select ''Classi'', count(cl) as cnt from tb_classe cl 
where cl.fl_canc <> ''S'') order by 1 ', 'tabella|totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(9, '00_CatPolo3', '00_CatPolo', 3, 'IC000', NULL, NULL, 'Totale_documenti_suddivisi_per_tipo_materiale_e_condivisione', '0', '((select 
 case t.tp_materiale isnull when true then ''tipo materiale non indicato'' else
        case trim(t.tp_materiale)='''' when true then ''tipo materiale non indicato'' else
        lower(cod.ds_tabella) end  end as materiale
,sum(case when t.fl_condiviso = ''n'' THEN 1 else 0 END) as locale
,percento (sum(case when t.fl_condiviso = ''n'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when t.fl_condiviso = ''s'' THEN 1 else 0 END) as condiviso
,percento (sum(case when t.fl_condiviso = ''s'' THEN 1 else 0 END), count(*)) as perc_condiviso
,count(t) as cnt
,to_char(CAST((count(t) * 100) AS DECIMAL) / (select count(*)  
from tb_titolo t1 where t1.fl_canc <> ''S'' and t1.cd_livello <> ''01'' and t1.cd_livello <> ''04'' 
and t1.cd_natura in (''M'',''W'',''S'',''N'')
 ), ''900.99'') as percentuale
from tb_titolo t
	left outer join tb_codici cod on t.tp_materiale = cod.cd_tabella and tp_tabella = ''MATE''
where fl_canc<>''S'' and cd_livello<>''01'' and cd_livello<>''04'' and cd_natura in (''M'',''W'',''S'',''N'')
group by 1 order by 1)
UNION
(select chr(160)||chr(160)||''totale''
,sum(case when tb.fl_condiviso = ''n'' THEN 1 else 0 END) as locale
,percento (sum(case when tb.fl_condiviso = ''n'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when tb.fl_condiviso = ''s'' THEN 1 else 0 END) as condiviso
,percento (sum(case when tb.fl_condiviso = ''s'' THEN 1 else 0 END), count(*)) as perc_condiviso
,count(tb) as cnt,
to_char(CAST ((count(tb) * 100) AS DECIMAL) / (select count(*) 
from Tb_titolo tb where tb.fl_canc <> ''S'' 
and tb.cd_livello <> ''01'' and tb.cd_livello <> ''04'' and tb.cd_natura in (''M'',''W'',''S'',''N'')
 ), ''990.99'') as percentuale
from Tb_titolo tb 
where tb.cd_livello <> ''01'' and tb.cd_livello <> ''04'' 
and not tb.fl_canc = ''S'' and tb.cd_natura in (''M'',''W'',''S'',''N'')
group by 1)) order by 1', 'materiale|locale|%_locale|condiviso|%_condiviso|totale|%_sul_totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(13, '00_CatPolo5', '00_CatPolo', 5, 'IC000', NULL, NULL, 'Totale_documenti_creati_e_catturati_suddivisi_per_natura_e_tipo_materiale', '0', '((select 
 case t.tp_materiale isnull when true then ''tipo materiale non indicato'' else
        case trim(t.tp_materiale)='''' when true then ''tipo materiale non indicato'' else
        lower(cod.ds_tabella) end  end as tipo_mat
,CAST(t.cd_natura as text) as natura
,sum(case when substring(t.bid,1,3)=(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END) as creati
,percento (sum(case when substring(t.bid,1,3)=(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END), count(*)) as perc_creati
,sum(case when substring(t.bid,1,3)<>(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END) as catturati
,percento (sum(case when substring(t.bid,1,3)<>(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END), count(*)) as perc_catturati
,count(t) as totale
,to_char(CAST((count(t) * 100) AS DECIMAL) / (select count(*)  
from tb_titolo t1 where t1.fl_canc <> ''S'' and t1.cd_livello <> ''01'' and t1.cd_livello <> ''04'' 
and t1.cd_natura in (''M'',''W'',''S'',''N'')
 ), ''900.99'') as percentuale
from tb_titolo t
	left outer join tb_codici cod on t.tp_materiale = cod.cd_tabella and tp_tabella = ''MATE''
where fl_canc<>''S'' and cd_livello<>''01'' and cd_livello<>''04'' and cd_natura in (''M'',''W'',''S'',''N'')
group by 1,2 order by 1,2)
UNION
(select chr(160)||chr(160)||''totale'', null
,sum(case when substring(tb.bid,1,3)=(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END) as creati
,percento (sum(case when substring(tb.bid,1,3)=(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END), count(*)) as perc_creati
,sum(case when substring(tb.bid,1,3)<>(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END) as catturati
,percento (sum(case when substring(tb.bid,1,3)<>(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END), count(*)) as perc_catturati
,count(tb) as totale,
to_char(CAST ((count(tb) * 100) AS DECIMAL) / (select count(*) 
from Tb_titolo tb where tb.fl_canc <> ''S'' 
and tb.cd_livello <> ''01'' and tb.cd_livello <> ''04'' and tb.cd_natura in (''M'',''W'',''S'',''N'')
 ), ''990.99'') as percentuale
from Tb_titolo tb 
where tb.cd_livello <> ''01'' and tb.cd_livello <> ''04'' 
and not tb.fl_canc = ''S'' and tb.cd_natura in (''M'',''W'',''S'',''N'')
group by 1)) order by 1', 'tipo_materiale|natura|creati|%_creati|catturati|%_catturati|totale|%_sul_totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(19, '00_CatPolo2', '00_CatPolo', 2, 'IC000', NULL, NULL, 'Totale_titoli_suddivisi_per_natura', '0', '(select CAST(_ti.cd_natura as text) as natura 
,count(*) as cnt
,to_char(CAST ((count(_ti) * 100) AS DECIMAL) / (select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04''), ''900.99'') as percentuale
from tb_titolo _ti 
where _ti.fl_canc <>''S'' and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04''
group by 1 order by 1)
UNION
(select chr(160)||''Totale polo'' 
,count(*) as cnt
,to_char(CAST ((count(_ti) * 100) AS DECIMAL) / (select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04''), ''900.99'') as percentuale
from tb_titolo _ti 
where _ti.fl_canc <>''S'' and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'')', 'natura|titoli|%', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(20, '00_CatPolo6', '00_CatPolo', 6, 'IC000', NULL, NULL, 'Totale_documenti_suddivisi_per_genere', '0', '(select case t.cd_genere_1 isnull when true then chr(160)||''Valore non impostato'' else 
        case trim(t.cd_genere_1)='''' when true then chr(160)||''Valore non indicato'' else
        CAST(c.ds_tabella as text)  end  end 
,count(*),
to_char(CAST((count(t) * 100) AS DECIMAL) / (select count(*)  
from Tb_titolo t where fl_canc <> ''S'' and cd_livello <> ''01'' and cd_livello <> ''04'' 
and cd_natura in (''M'',''W'',''S'',''C'',''N'',''T'') ), ''900.99'') as percentuale
from tb_titolo t
 left outer join tb_codici c on t.cd_genere_1 = c.cd_tabella and tp_tabella = ''GEPU''
where t.fl_canc <> ''S''
and t.cd_livello <> ''01'' and t.cd_livello <> ''04'' and cd_natura in (''M'',''W'',''S'',''C'',''N'',''T'')
group by 1 order by 1)
UNION
(select chr(160)||chr(160)||''Totale polo'',
count(*),
to_char(CAST ((count(t) * 100) AS DECIMAL) / (select count(*)  
from Tb_titolo t where fl_canc <> ''S'' and cd_livello <> ''01'' 
and cd_livello <> ''04'' and cd_natura in (''M'',''W'',''S'',''C'',''N'',''T'') ), ''900.99'') as percentuale
from tb_titolo t
where t.fl_canc <> ''S'' and t.cd_livello <> ''01'' and t.cd_livello <> ''04'' 
and cd_natura in (''M'',''W'',''S'',''C'',''N'',''T''))
UNION
(select  chr(160)||chr(160)||chr(160)||''Generi presenti''
,count(distinct t.cd_genere_1),
null
from tb_titolo t)', 'genere (primo codice)|documenti|%', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(34, '00_CatPolo15', '00_CatPolo', 18, 'IC000', NULL, NULL, 'Lista_Autori_da_VID_a_VID', '1', 'select  cast(au.VID as text), au.tp_forma_aut, au.tp_nome_aut,
cast (au.cd_livello as text), au.ds_nome_aut   from tb_autore au
where SUBSTRING(au.vid,0,length(:1VIDDa)+1) >= UPPER(:1VIDDa)    
and SUBSTRING(au.vid,0,length(:2VIDA)+1) <= UPPER(:2VIDA)
and not  au.fl_canc=''S''
order by 1;
', 'VID|Forma|Tipo_Nome|Livello|Nome', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(45, '00_GAcq3', '00_GAcq', 3, 'A0000', NULL, NULL, 'Totale_suggerimenti_di_bibliotecari_divisi_per_biblioteca_e_stato', '1', '(select CAST(sug.cd_bib as text) as biblioteca
,sum(case  when sug.stato_sugg=''A'' then 1 else 0 END) as tot_sugg_accettati
,percento (sum(case  when sug.stato_sugg=''A'' THEN 1 else 0 END), count(*)) as perc_sugg_accettati 
,sum(case  when sug.stato_sugg=''O'' THEN 1 else 0 END) as tot_sugg_ordinati
,percento (sum(case  when sug.stato_sugg=''O'' THEN 1 else 0 END), count(*)) as perc_sugg_ordinati 
,sum(case  when sug.stato_sugg=''R'' THEN 1 else 0 END) as tot_sugg_rifiutati
,percento (sum(case  when sug.stato_sugg=''R'' THEN 1 else 0 END), count(*)) as perc_sugg_rifiutati
,sum(case  when sug.stato_sugg=''W'' THEN 1 else 0 END) as tot_sugg_in_attesa
,percento (sum(case  when sug.stato_sugg=''W'' THEN 1 else 0 END), count(*)) as perc_sugg_in_attesa
,count(*) as totale_suggerimenti
,percento(count(*),
	(select count(*) from tba_suggerimenti_bibliografici sug where sug.fl_canc<>''S'' and sug.stato_sugg in (''A'' , ''O'', ''R'', ''W'') and 
	sug.data_sugg_bibl between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
	and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
    )) as perc_su_tot_polo
from tba_suggerimenti_bibliografici sug
where sug.fl_canc<>''S'' and sug.stato_sugg in (''A'' , ''O'', ''R'', ''W'') and 
	sug.data_sugg_bibl between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
	and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
 group by sug.cd_bib  order by sug.cd_bib)
UNION
(select CAST(''polo'' as text)
,sum(case  when sug.stato_sugg=''A'' then 1 else 0 END) as tot_sugg_accettati
,percento (sum(case  when sug.stato_sugg=''A'' THEN 1 else 0 END), count(*)) as perc_sugg_accettati 
,sum(case  when sug.stato_sugg=''O'' THEN 1 else 0 END) as tot_sugg_ordinati
,percento (sum(case  when sug.stato_sugg=''O'' THEN 1 else 0 END), count(*)) as perc_sugg_ordinati 
,sum(case  when sug.stato_sugg=''R'' THEN 1 else 0 END) as tot_sugg_rifiutati
,percento (sum(case  when sug.stato_sugg=''R'' THEN 1 else 0 END), count(*)) as perc_sugg_rifiutati
,sum(case  when sug.stato_sugg=''W'' THEN 1 else 0 END) as tot_sugg_in_attesa
,percento (sum(case  when sug.stato_sugg=''W'' THEN 1 else 0 END), count(*)) as perc_sugg_in_attesa
,count(*) as totale_suggerimenti
, 100
from tba_suggerimenti_bibliografici sug
where sug.fl_canc<>''S'' and sug.stato_sugg in (''A'' , ''O'', ''R'', ''W'') and 
	sug.data_sugg_bibl between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
	and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
) ', 'biblioteca|accettati|%_A_su_totale_bib|ordinati|%_O_su_totale_bib|rifiutati|%_R_su_totale_bib|in_attesa|%_W_su_totale_bib|totale_suggerimenti|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(37, '00_CatPolo7', '00_CatPolo', 7, 'IC000', NULL, NULL, 'Totale_documenti_suddivisi_per_paese_di_pubblicazione', '0', '((select
case t.cd_paese isnull when true then chr(160)||chr(160)||''Paese non indicato'' else
        case trim(t.cd_paese)='''' when true then chr(160)||chr(160)||''Paese non indicato'' else
        cod.ds_tabella end  end 
,case t.cd_paese isnull when true then '''' else
        case trim(t.cd_paese)='''' when true then '''' else
        CAST (cd_paese AS text) end  end
,count(*) as cnt 
,to_char(CAST((count(t) * 100) AS DECIMAL) / (select count(*)  
from tb_titolo t1 where t1.fl_canc <> ''S''  
and t1.cd_natura in (''M'', ''W'', ''S'', ''C'') 
and t1.cd_livello <> ''01'' and t1.cd_livello <> ''04''), ''900.99'') as percentuale
from tb_titolo t
	left outer join tb_codici cod on t.cd_paese = cod.cd_tabella and cod.tp_tabella = ''PAES''
where t.fl_canc <> ''S'' 
and t.cd_natura in (''M'', ''W'', ''S'', ''C'')  
and t.cd_livello <> ''01'' and t.cd_livello <> ''04''
group by 1,2)
UNION
(select  chr(160)||chr(160)||''Totale dei titoli'', chr(160)||'''', count(*) as cnt
,to_char(CAST((count(t) * 100) AS DECIMAL) / (select count(*)  
from tb_titolo t1 where t1.fl_canc <> ''S'' 
and t1.cd_natura in (''M'', ''W'', ''S'', ''C'') 
and t1.cd_livello <> ''01'' and t1.cd_livello<>''04'' ), ''900.99'') as percentuale
from tb_titolo t
where t.fl_canc <> ''S'' 
and t.cd_natura in (''M'', ''W'', ''S'', ''C'') 
and t.cd_livello <> ''01'' and t.cd_livello <> ''04'')
UNION
(select  chr(160)||chr(160)||chr(160)||''Paesi presenti'', chr(160)||'''',
count(distinct t.cd_paese), null  
from tb_titolo t where fl_canc <> ''S'' 
and cd_natura in (''M'', ''W'', ''S'', ''C'') 
and cd_livello <> ''01'' and cd_livello <> ''04''))
order by 1', 'paese|codice paese|documenti|%', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(38, '00_CatPolo8', '00_CatPolo', 8, 'IC000', NULL, NULL, 'Totale_documenti_suddivisi_per_lingua_di_pubblicazione', '0', '((select 
 case t.cd_lingua_1 isnull when true then chr(160)||''Lingua non indicata'' else
        case trim(t.cd_lingua_1)='''' when true then chr(160)||''Lingua non indicata'' else
        cod.ds_tabella end  end
,case t.cd_lingua_1 isnull when true then '''' else
        case trim(t.cd_lingua_1)='''' when true then '''' else
        CAST (cd_lingua_1 AS text) end  end
,count(*) as cnt 
,to_char(CAST((count(t) * 100) AS DECIMAL) / (select count(*)  
from tb_titolo t1 where t1.fl_canc <> ''S'' 
and t1.cd_natura in (''M'', ''W'', ''S'', ''N'', ''T'') 
and t1.cd_livello <> ''01'' and t1.cd_livello <> ''04'' ), ''900.99'') as percentuale
from tb_titolo t
	left outer join tb_codici cod on cd_lingua_1 = cod.cd_tabella and tp_tabella = ''LING''
where fl_canc<>''S'' 
and cd_natura in (''M'', ''W'', ''S'', ''N'', ''T'') 
and cd_livello<>''01'' and cd_livello<>''04''
group by 1,2)
UNION
(select  chr(160)||chr(160)||''Totale dei titoli'', chr(160)||'''', count(*) as cnt 
,to_char(CAST((count(t) * 100) AS DECIMAL) / (select count(*)  
from tb_titolo t1 where t1.fl_canc <> ''S'' 
and t1.cd_natura in (''M'', ''W'', ''S'', ''N'', ''T'') 
and t1.cd_livello <> ''01'' and t1.cd_livello<>''04''), ''900.99'') as percentuale
from tb_titolo t
where fl_canc<>''S'' 
and cd_natura in (''M'', ''W'', ''S'', ''N'', ''T'') 
and cd_livello<>''01'' and cd_livello<>''04'')
UNION
(select  chr(160)||chr(160)||chr(160)||''Lingue presenti'', chr(160)||'''', count(distinct t.cd_lingua_1), null  
from Tb_titolo t where fl_canc<>''S'' 
and cd_natura in (''M'', ''W'', ''S'', ''N'', ''T'') 
and cd_livello<>''01'' and cd_livello<>''04'' ))
order by 1', 'lingua|codice lingua (primo codice)|documenti|%', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(41, '00_GPeriodici', NULL, 8, 'P0000', NULL, 0, NULL, NULL, NULL, NULL, 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(42, '00_GDocFis1', '00_GDocFis', 1, 'C0000', NULL, NULL, 'Totale_inventari_divisi_per_biblioteca_e_stato', '0', '(select CAST(inv.cd_bib as text) as Biblioteca 
,sum(case  when inv.cd_sit=''1'' then 1 else 0 END) as tot_precisati 
,percento (sum(case  when inv.cd_sit=''1'' THEN 1 else 0 END), count(*)) as perc_precisati 
,sum(case  when inv.cd_sit=''2'' THEN 1 else 0 END) as tot_collocati 
,percento (sum(case  when inv.cd_sit=''2'' THEN 1 else 0 END), count(*)) as perc_collocati 
,sum(case  when inv.cd_sit=''3'' THEN 1 else 0 END) as tot_dismessi 
,percento (sum(case  when inv.cd_sit=''3'' THEN 1 else 0 END), count(*)) as perc_dismessi 
,count(*) as totale 
, percento(count(*),(select count(*)  
from tbc_inventario inv1 where inv1.fl_canc<>''S''  )) as perc_su_tot_polo  
from tbc_inventario inv 
where inv.fl_canc<>''S''  
 group by inv.cd_bib  order by inv.cd_bib) 
union 
(select CAST(''polo'' as text) 
,sum(case  when inv.cd_sit=''1'' then 1 else 0 END) as tot_precisati 
,percento (sum(case  when inv.cd_sit=''1'' THEN 1 else 0 END), count(*)) as perc_precisati 
,sum(case  when inv.cd_sit=''2'' THEN 1 else 0 END) as tot_collocati 
,percento (sum(case  when inv.cd_sit=''2'' THEN 1 else 0 END), count(*)) as perc_collocati 
,sum(case  when inv.cd_sit=''3'' THEN 1 else 0 END) as tot_dismessi 
,percento (sum(case  when inv.cd_sit=''3'' THEN 1 else 0 END), count(*)) as perc_dismessi 
,count(*) as totale 
,case  
(select count(*) from tbc_inventario inv1 where inv1.fl_canc<>''S''  ) = 0 when true then 100 
else  
percento(count(*),(select count(*) from tbc_inventario inv1 where inv1.fl_canc<>''S''  )) end as perc_totale 
from tbc_inventario inv 
where inv.fl_canc<>''S'') 
 
', 'biblioteca|precisati|%_precisati_su_totale_bib|collocati|%_collocati_su_totale_bib|dismessi|%_dismessi_su_totale_bib|totale_inventari|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(43, '00_GAcq1', '00_GAcq', 1, 'A0000', NULL, NULL, 'Totale_ordini_divisi_per_biblioteca_e_stato', '1', '(select CAST(acq.cd_bib as text) as biblioteca 
,sum(case  when acq.stato_ordine=''A'' then 1 else 0 END) as tot_ordini_aperti 
,percento (sum(case  when acq.stato_ordine=''A'' THEN 1 else 0 END), count(*)) as perc_ordini_aperti 
 ,sum(case  when acq.stato_ordine=''C'' THEN 1 else 0 END) as tot_ordini_chiusi 
,percento (sum(case  when acq.stato_ordine=''C'' THEN 1 else 0 END), count(*)) as perc_ordini_chiusi 
 ,sum(case  when acq.stato_ordine=''N'' THEN 1 else 0 END) as tot_ordini_annullati 
,percento (sum(case  when acq.stato_ordine=''N'' THEN 1 else 0 END), count(*)) as perc_ordini_annullati 
,count(*) as totale_ordini 
,percento(count(*), 
(select count(*) from tba_ordini acq2 where acq2.fl_canc<>''S'' 
     and acq2.data_ord between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
     and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')))) as perc_su_tot_polo 
from tba_ordini acq 
where acq.fl_canc<>''S''  
and acq.data_ord between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
 group by acq.cd_bib  order by acq.cd_bib) 
UNION 
(select CAST(''polo'' as text) 
,sum(case  when acq.stato_ordine=''A'' then 1 else 0 END) as tot_ordini_aperti 
,percento (sum(case  when acq.stato_ordine=''A'' THEN 1 else 0 END), count(*)) as perc_ordini_aperti  
 ,sum(case  when acq.stato_ordine=''C'' THEN 1 else 0 END) as tot_ordini_chiusi 
,percento (sum(case  when acq.stato_ordine=''C'' THEN 1 else 0 END), count(*)) as perc_ordini_chiusi  
 ,sum(case  when acq.stato_ordine=''N'' THEN 1 else 0 END) as tot_ordini_annullati 
,percento (sum(case  when acq.stato_ordine=''N'' THEN 1 else 0 END), count(*)) as perc_ordini_annullati 
,count(*) as totale_ordini 
, 100 
from tba_ordini acq 
where acq.fl_canc<>''S'' 
and acq.data_ord between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
) 
', 'biblioteca|aperti|%_aperti_su_totale_bib|chiusi|%_chiusi_su_totale_bib|annullati|%_annullati_su_totale_bib|totale_ordini|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(44, '00_GAcq2', '00_GAcq', 2, 'A0000', NULL, NULL, 'Totale_ordini_divisi_per_biblioteca_e_tipologia', '1', '(select CAST(acq.cd_bib as text) as biblioteca 
,sum(case  when acq.cod_tip_ord=''A'' then 1 else 0 END) as tot_ordini_acq 
,percento (sum(case  when acq.cod_tip_ord=''A'' THEN 1 else 0 END), count(*)) as perc_ordini_acq  
,sum(case  when acq.cod_tip_ord=''C'' then 1 else 0 END) as tot_ordini_cam 
,percento (sum(case  when acq.cod_tip_ord=''C'' then 1 else 0 END), count(*)) as perc_ordini_cam 
,sum(case  when acq.cod_tip_ord=''D'' then 1 else 0 END) as tot_ordini_don 
,percento (sum(case  when acq.cod_tip_ord=''D'' then 1 else 0 END), count(*)) as perc_ordini_don 
,sum(case  when acq.cod_tip_ord=''L'' then 1 else 0 END) as tot_ordini_dep 
,percento (sum(case  when acq.cod_tip_ord=''L'' then 1 else 0 END), count(*)) as perc_ordini_dep 
,sum(case  when acq.cod_tip_ord=''R'' then 1 else 0 END) as tot_ordini_ril 
,percento (sum(case  when acq.cod_tip_ord=''R'' then 1 else 0 END), count(*)) as perc_ordini_ril 
,sum(case  when acq.cod_tip_ord=''V'' then 1 else 0 END) as tot_ordini_vtr 
,percento (sum(case  when acq.cod_tip_ord=''V'' then 1 else 0 END), count(*)) as perc_ordini_vtr 
,count(*) as totale_ordini 
,percento(count(*), 
 (select count(*) from tba_ordini acq2 
   where acq2.fl_canc<>''S''  
   and acq2.data_ord between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
   and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')))) as perc_su_tot_polo 
from tba_ordini acq 
where acq.fl_canc<>''S'' 
and acq.data_ord between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
 group by 1   
 order by 1) 
UNION 
(select CAST(''polo'' as text) 
,sum(case  when acq2.cod_tip_ord=''A'' then 1 else 0 END) as tot_ordini_acq 
,percento (sum(case  when acq2.cod_tip_ord=''A'' THEN 1 else 0 END), count(*)) as perc_ordini_acq  
,sum(case  when acq2.cod_tip_ord=''C'' then 1 else 0 END) as tot_ordini_cam 
,percento (sum(case  when acq2.cod_tip_ord=''C'' then 1 else 0 END), count(*)) as perc_ordini_cam 
,sum(case  when acq2.cod_tip_ord=''D'' then 1 else 0 END) as tot_ordini_don 
,percento (sum(case  when acq2.cod_tip_ord=''D'' then 1 else 0 END), count(*)) as perc_ordini_don 
,sum(case  when acq2.cod_tip_ord=''L'' then 1 else 0 END) as tot_ordini_dep 
,percento (sum(case  when acq2.cod_tip_ord=''L'' then 1 else 0 END), count(*)) as perc_ordini_dep 
,sum(case  when acq2.cod_tip_ord=''R'' then 1 else 0 END) as tot_ordini_ril 
,percento (sum(case  when acq2.cod_tip_ord=''R'' then 1 else 0 END), count(*)) as perc_ordini_ril 
,sum(case  when acq2.cod_tip_ord=''V'' then 1 else 0 END) as tot_ordini_vtr 
,percento (sum(case  when acq2.cod_tip_ord=''V'' then 1 else 0 END), count(*)) as perc_ordini_vtr 
,count(*) as totale_ordini 
, 100 
from tba_ordini acq2 
where acq2.fl_canc<>''S'' 
and acq2.data_ord between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
) 

', 'biblioteca|Acquisto|%_A_su_totale_bib|Cambio|%_C_su_totale_bib|Dono|%_D_su_totale_bib|Dep_Legale|%_L_su_totale_bib|Rilegatura|%_R_su_totale_bib|Visione_Tratt|%_V_su_totale_bib|Totale_ordini|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(113, '00_GSer23', '00_GSer', 15, 'L0000', NULL, NULL, 'Totale_utenti_del_polo_suddivisi_per_biblioteca_di_registrazione', '0', '(select CAST (utb.cd_biblioteca AS text),count(*) 
,percento(count(*),(select count(distinct id_utenti) 
from trl_utenti_biblioteca utb1
where utb1.fl_canc<>''S''))
from trl_utenti_biblioteca utb 
where utb.fl_canc<>''S''
group by 1 order by 1)
UNION
(select chr(160)||''Totale del polo'',
count(distinct id_utenti),null
from trl_utenti_biblioteca utb 
where utb.fl_canc<>''S''
)', 'biblioteca|utenti|%_utenti_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(46, '00_GAcq4', '00_GAcq', 4, 'A0000', NULL, NULL, 'Totale_suggerimenti_di_lettori_divisi_per_biblioteca_e_stato', '1', '(select CAST(doc.cd_bib as text) as biblioteca
,sum(case  when doc.stato_sugg=''A'' then 1 else 0 END) as tot_sugg_accettati 
,percento (sum(case  when doc.stato_sugg=''A'' THEN 1 else 0 END), count(*)) as perc_sugg_accettati 
,sum(case  when doc.stato_sugg=''O'' THEN 1 else 0 END) as tot_sugg_ordinati 
,percento (sum(case  when doc.stato_sugg=''O'' THEN 1 else 0 END), count(*)) as perc_sugg_ordinati  
,sum(case  when doc.stato_sugg=''R'' THEN 1 else 0 END) as tot_sugg_rifiutati 
,percento (sum(case  when doc.stato_sugg=''R'' THEN 1 else 0 END), count(*)) as perc_sugg_rifiutati 
,sum(case  when doc.stato_sugg=''W'' THEN 1 else 0 END) as tot_sugg_in_attesa 
,percento (sum(case  when doc.stato_sugg=''W'' THEN 1 else 0 END), count(*)) as perc_sugg_in_attesa 
,count(*) as totale_suggerimenti 
,percento(count(*), 
 (select count(*) from tbl_documenti_lettori doc where doc.fl_canc<>''S'' and doc.stato_sugg in (''A'' , ''O'', ''R'', ''W'') and 
 doc.data_sugg_lett between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
 and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')))) as perc_su_tot_polo 
from tbl_documenti_lettori doc 
where doc.fl_canc<>''S'' and doc.stato_sugg in (''A'' , ''O'', ''R'', ''W'') and  
 doc.data_sugg_lett between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
 and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
 group by doc.cd_bib  order by doc.cd_bib) 
UNION 
(select CAST(''polo'' as text) 
,sum(case  when doc.stato_sugg=''A'' then 1 else 0 END) as tot_sugg_accettati 
,percento (sum(case  when doc.stato_sugg=''A'' THEN 1 else 0 END), count(*)) as perc_sugg_accettati  
,sum(case  when doc.stato_sugg=''O'' THEN 1 else 0 END) as tot_sugg_ordinati 
,percento (sum(case  when doc.stato_sugg=''O'' THEN 1 else 0 END), count(*)) as perc_sugg_ordinati  
,sum(case  when doc.stato_sugg=''R'' THEN 1 else 0 END) as tot_sugg_rifiutati 
,percento (sum(case  when doc.stato_sugg=''R'' THEN 1 else 0 END), count(*)) as perc_sugg_rifiutati 
,sum(case  when doc.stato_sugg=''W'' THEN 1 else 0 END) as tot_sugg_in_attesa 
,percento (sum(case  when doc.stato_sugg=''W'' THEN 1 else 0 END), count(*)) as perc_sugg_in_attesa 
,count(*) as totale_suggerimenti 
, 100 
from tbl_documenti_lettori doc 
where doc.fl_canc<>''S'' and doc.stato_sugg in (''A'' , ''O'', ''R'', ''W'') and 
 doc.data_sugg_lett between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
 and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))   
) order by 1 ', 'biblioteca|accettati|%_A_su_totale_bib|ordinati|%_O_su_totale_bib|rifiutati|%_R_su_totale_bib|in_attesa|%_W_su_totale_bib|totale_suggerimenti|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(47, '00_GDocFis2', '00_GDocFis', 2, 'C0000', NULL, NULL, 'Totale_inventari_della_biblioteca_divisi_per_tipo_acq_e_natura', '1', '(select 
case inv.tipo_acquisizione isnull when true then chr(160)||''non impostato'' 
else case trim(inv.tipo_acquisizione)='''' when true then chr(160)||''a spazio''
  else CAST(inv.tipo_acquisizione as text)||  '' ('' ||
  lower((select ds_tabella from tb_codici where tp_tabella=''CTAC'' and cd_tabella=inv.tipo_acquisizione)) || '')''
  end  end as tipo_acq 
,sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END) as natura_M_W
,percento (sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END), count(*)) as perc_natura_M_W
,sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END) as natura_S
,percento (sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END), count(*)) as perc_natura_S
,sum(case when tit.cd_natura = ''C'' THEN 1 else 0 END) as natura_C
,percento (sum(case when tit.cd_natura = ''C'' THEN 1 else 0 END), count(*)) as perc_natura_C
,count(*) as totale_inv
,percento (count(*),
(select count(*) from tbc_inventario inv1, tb_titolo tit where inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2'' 
and inv1.bid = tit.bid and tit.cd_natura in (''M'',''W'',''S'',''C'') 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and (:3tpAcq='''' or inv1.tipo_acquisizione=:3tpAcq) 
and (inv1.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv1.data_ingresso isnull and CAST(inv1.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) )))) 
from tbc_inventario inv, tb_titolo tit
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and inv.bid = tit.bid and tit.cd_natura in (''M'',''W'',''S'',''C'') 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (:3tpAcq='''' or inv.tipo_acquisizione=:3tpAcq) 
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) )) 
group by inv.tipo_acquisizione  order by 1)
UNION
(select chr(160)||chr(160)||''Totale della bib''
,sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END) as natura_M_W
,percento (sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END), count(*)) as perc_natura_M_W
,sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END) as natura_S
,percento (sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END), count(*)) as perc_natura_S
,sum(case when tit.cd_natura = ''C'' THEN 1 else 0 END) as natura_C
,percento (sum(case when tit.cd_natura = ''C'' THEN 1 else 0 END), count(*)) as perc_natura_C
,count(*) as totale_inv
, 100
from tbc_inventario inv, tb_titolo tit
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and inv.bid = tit.bid and tit.cd_natura in (''M'',''W'',''S'',''C'') 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (:3tpAcq='''' or inv.tipo_acquisizione=:3tpAcq) 
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) )) 
group by 1 order by 1)
UNION
(select chr(160)||chr(160)||chr(160)||''Totale polo''
,sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END) as natura_M_W
,percento (sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END), count(*)) as perc_natura_M_W
,sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END) as natura_S
,percento (sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END), count(*)) as perc_natura_S
,sum(case when tit.cd_natura = ''C'' THEN 1 else 0 END) as natura_C
,percento (sum(case when tit.cd_natura = ''C'' THEN 1 else 0 END), count(*)) as perc_natura_C
,count(*) as totale_inv
, 100
from tbc_inventario inv, tb_titolo tit
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and inv.bid = tit.bid and tit.cd_natura in (''M'',''W'',''S'',''C'') 
and (:3tpAcq='''' or inv.tipo_acquisizione=:3tpAcq) 
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) )) 
group by 1 order by 1)
order by 1', 'tipo_acq|monografie|%_monografie|periodici|%_periodici|collezioni|%_collezioni|totale_inv|%_su_totale_inv', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(50, '00_GDocFis5', '00_GDocFis', 3, 'C0000', NULL, NULL, 'Totale_inventari_con_legame_a_possessore_o_provenienza', '0', '(select CAST(pp.cd_biblioteca as text) as Biblioteca  
,sum(case  when pp.cd_legame=''P'' THEN 1 else 0 END) as tot_leg_poss 
,percento (sum(case  when pp.cd_legame=''P'' THEN 1 else 0 END), count(*)) as perc_leg_poss 
,sum(case  when pp.cd_legame=''R'' THEN 1 else 0 END) as tot_leg_prov  
,percento (sum(case  when pp.cd_legame=''R'' THEN 1 else 0 END), count(*)) as perc_leg_prov 
,count(*) as totale_legami 
, percento(count(*),(select count(*) from trc_poss_prov_inventari pp1  
  where pp1.fl_canc<>''S''  )) as perc_su_tot_polo    
from trc_poss_prov_inventari pp where pp.fl_canc<>''S''   
group by pp.cd_biblioteca  order by pp.cd_biblioteca) 
UNION 
(select CAST(chr(160)||''Polo'' as text) as Biblioteca  
,sum(case  when pp.cd_legame=''P'' THEN 1 else 0 END) as tot_leg_poss 
,percento (sum(case  when pp.cd_legame=''P'' THEN 1 else 0 END), count(*)) as perc_leg_poss 
,sum(case  when pp.cd_legame=''R'' THEN 1 else 0 END) as tot_leg_prov  
,percento (sum(case  when pp.cd_legame=''R'' THEN 1 else 0 END), count(*)) as perc_leg_prov 
,count(*) as totale_legami 
,case  
 (select count(*) from trc_poss_prov_inventari pp1  
  where pp1.fl_canc<>''S'' ) = 0 when true then 100 
else  
 percento(count(*),(select count(*) from trc_poss_prov_inventari pp1  
  where pp1.fl_canc<>''S''  )) end as  perc_su_tot_polo    
from trc_poss_prov_inventari pp where pp.fl_canc<>''S'')  
  
 
', 'biblioteca|totale_legami_possessori|%_poss_su_totale_bib|totale_legami_provenienze|%_prov_su_totale_bib|totale_legami|%_su_totale_polo
', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(51, '00_GDocFis6', '00_GDocFis', 4, 'C0000', NULL, NULL, 'Totale_inventari_divisi_per_tipo_record', '1', '(select CAST(inv.cd_bib as text) as biblioteca
, case tit.tp_record_uni isnull when true then chr(160)||''non impostato'' 
  else case trim(tit.tp_record_uni)='''' when true then chr(160)||''a spazio''
  else CAST(tit.tp_record_uni as text)||  '' ('' ||
  lower((select ds_tabella from tb_codici where tp_tabella=''GEUN'' and cd_tabella=tit.tp_record_uni)) || '')''
  end  end as tipo_record
,count(*) as totale_inv
,percento (count(*),(select count(inv1.cd_bib) from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2'' 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and (:1tpRec='''' or tit.tp_record_uni=:1tpRec) 
and 
(inv1.data_ingresso between COALESCE(:2dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:3dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv1.data_ingresso isnull and CAST(inv1.ts_var as date) between COALESCE(:2dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:3dataA, to_timestamp(''99990101'',''YYYYMMDD'')) ))
group by inv1.cd_bib) 
) as perc_totale_inv_bib
,sum(case when not TRIM(inv.id_accesso_remoto) = '''' THEN 1 else 0 END) as tot_digit
,percento (sum(case when not TRIM(inv.id_accesso_remoto) = '''' THEN 1 else 0 END), 
(select count(inv1.cd_bib) from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2'' 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and (:1tpRec='''' or tit.tp_record_uni=:1tpRec)
and (inv1.data_ingresso between COALESCE(:2dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:3dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv1.data_ingresso isnull and CAST(inv1.ts_var as date) between COALESCE(:2dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:3dataA, to_timestamp(''99990101'',''YYYYMMDD'')) ))
group by inv1.cd_bib) ) as perc_digit
from tbc_inventario inv, tb_titolo tit
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and inv.bid = tit.bid 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (:1tpRec='''' or tit.tp_record_uni=:1tpRec) 
and (inv.data_ingresso between COALESCE(:2dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:3dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:2dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:3dataA, to_timestamp(''99990101'',''YYYYMMDD'')) ))
group by inv.cd_bib,tit.tp_record_uni  order by 1,2)
UNION
(select CAST(inv.cd_bib as text) as biblioteca
, chr(160)||chr(160)||chr(160)||''totale della bib''
, count(*) as totale_inv
, null
, sum(case when not TRIM(inv.id_accesso_remoto) = '''' THEN 1 else 0 END) as digitalizzati
, null
from tbc_inventario inv, tb_titolo tit
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and inv.bid = tit.bid 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (:1tpRec='''' or tit.tp_record_uni=:1tpRec) 
and (inv.data_ingresso between COALESCE(:2dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:3dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:2dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:3dataA, to_timestamp(''99990101'',''YYYYMMDD'')) ))
group by 1  order by 1)
UNION
(select ''polo'', chr(160)||chr(160)||chr(160)||''totale''
, count(*) as totale_inv
, null
, sum(case when not TRIM(inv.id_accesso_remoto) = '''' THEN 1 else 0 END) as digitalizzati
, null
from tbc_inventario inv, tb_titolo tit
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and inv.bid = tit.bid 
and (:1tpRec='''' or tit.tp_record_uni=:1tpRec) 
and (inv.data_ingresso between COALESCE(:2dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:3dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:2dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:3dataA, to_timestamp(''99990101'',''YYYYMMDD'')) ))
)', 'biblioteca|tipo_record|totale_inv|%_su_totale_inv_bib|totale_digit|%_digit_su_totale_inv_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(52, '00_GDocFis7', '00_GDocFis', 5, 'C0000', NULL, NULL, 'Totale_inventari_della_biblioteca_divisi_per_codice_provenienza', '1', '((select 
 case inv.cd_proven isnull when true then chr(160)||''non impostato'' else
 case trim(inv.cd_proven)='''' when true then chr(160)||''a spazio'' else
 CAST(inv.cd_proven as text)||  '' ('' ||
  lower((select trim(pi.descr) from tbc_provenienza_inventario pi 
  where pi.cd_proven=inv.cd_proven and pi.cd_polo=inv.cd_polo and pi.cd_biblioteca=inv.cd_bib)) || '')''
  end end as cod_proven
,count(*) as tot_per_cod
,percento (count(*),
(select count(*) from tbc_inventario inv1 where inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2'' 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and (inv1.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv1.data_ingresso isnull and CAST(inv1.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) )  
)) as perc_totale_inv_bib
from tbc_inventario inv
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) )  
group by 1  order by 1)
UNION
(select 
chr(160)||chr(160)||chr(160)||''totale della bib''
,count(*) as tot_per_cod
,100
from tbc_inventario inv
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
group by 1 order by 1))
order by 1', 'cod_proven|totale_per_cod_proven|%_sul_totale_inv_bib
', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(53, '00_GDocFis8', '00_GDocFis', 6, 'C0000', NULL, NULL, 'Totale_inventari_della_biblioteca_divisi_per_serie_inventariale', '1', '(
(select case inv.cd_serie isnull when true then chr(160)||''valore non impostato'' else
 CAST(inv.cd_serie as text) end as serie
,count(*) as totale_inv
,percento (count(*),(select count(inv1.cd_bib) from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2'' 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and (inv1.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv1.data_ingresso isnull and CAST(inv1.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
group by inv1.cd_bib) 
) as perc_totale_inv_bib
from tbc_inventario inv
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
group by inv.cd_bib,inv.cd_serie order by 1,2)
UNION
(select chr(160)||chr(160)||''totale della bib''
, count(*) as totale_inv
, null
from tbc_inventario inv
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
group by 1  order by 1)
) 
UNION
(select chr(160)||chr(160)||chr(160)||''totale del polo''
, count(*) as totale_inv
, null
from tbc_inventario inv
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
) order by 1
', 'serie|totale_inv|%_sul_totale', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(54, '00_GDocFis9', '00_GDocFis', 7, 'C0000', NULL, NULL, 'Totale_inventari_divisi_per_biblioteca_e_categoria_di_fruizione', '1', '((select CAST(inv.cd_bib as text) as biblioteca
,case inv.cd_frui isnull when true then chr(160)||''valore non impostato'' 
  else case trim(inv.cd_frui)='''' when true then chr(160)||''valore non presente''
  else CAST(inv.cd_frui as text)||  '' ('' ||
  lower((select ds_tabella from tb_codici where tp_tabella=''LCFR'' and cd_tabella=inv.cd_frui)) || '')''
  end  end as cat_fruizione
,count(*) as totale_inv
,percento (count(*),(select count(inv1.cd_bib) from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2''  
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
group by inv1.cd_bib) 
) as perc_totale_inv_bib
from tbc_inventario inv
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
group by inv.cd_bib,inv.cd_frui order by 1,2)
UNION
(select CAST(inv.cd_bib as text) as biblioteca
, chr(160)||chr(160)||''totale della bib''
, count(*) as totale_inv
, null
from tbc_inventario inv
where inv.fl_canc <> ''S''  and inv.cd_sit = ''2'' 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
group by 1  order by 1)
) 
order by 1', 'biblioteca|cat_fruizione|totale_inv|%_sul_totale_inv_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(55, '00_GDocFis10', '00_GDocFis', 8, 'C0000', NULL, NULL, 'Totale_inventari_divisi_per_biblioteca_e_motivo_non_disponibilità', '1', '((select CAST(inv.cd_bib as text) as biblioteca
,case inv.cd_no_disp isnull when true then chr(160)||''valore non impostato'' 
  else case trim(inv.cd_no_disp)='''' when true then chr(160)||''valore non presente''
  else CAST(inv.cd_no_disp as text)||  '' ('' ||
  lower((select ds_tabella from tb_codici where tp_tabella=''CCND'' and cd_tabella=inv.cd_no_disp)) || '')''
  end  end as non_disp
,count(*) as totale_inv
,percento (count(*),(select count(inv1.cd_bib) from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2''  
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
group by inv1.cd_bib) 
) as perc_totale_inv_bib
from tbc_inventario inv
where inv.fl_canc <> ''S''  and inv.cd_sit = ''2'' 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
group by inv.cd_bib,inv.cd_no_disp order by 1,2)
UNION
(select CAST(inv.cd_bib as text) as biblioteca
, chr(160)||chr(160)||''totale della bib''
, count(*) as totale_inv
, null
from tbc_inventario inv
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2''  
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
group by 1  order by 1)
) 
order by 1', 'biblioteca|motivo_non_disponibilità|totale_inv|%_sul_totale_inv_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(56, '00_GDocFis11', '00_GDocFis', 9, 'C0000', NULL, NULL, 'Totale_inventari_divisi_per_biblioteca_e_natura', '1', '(select CAST(inv.cd_bib as text) as biblioteca 
,sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END) as monografie
,percento (sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END), count(*)) as perc_monografie
,sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END) as periodici
,percento (sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END), count(*)) as perc_periodici
,count(*) as totale_inv
from tbc_inventario inv, tb_titolo tit
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and inv.bid = tit.bid 
and tit.cd_natura in (''M'',''W'',''S'')
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (
inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and 
CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  )) 
group by 1 order by 1) 
UNION
(select CAST(inv.cd_bib as text) as biblioteca 
,sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END) as monografie 
,percento (sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END), count(*)) as perc_monografie 
,sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END) as periodici 
,percento (sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END), count(*)) as perc_periodici 
,count(*) as totale_inv 
from tbc_inventario inv, tb_titolo tit 
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2''  
and inv.bid = tit.bid 
and tit.cd_natura in (''M'',''W'',''S'') 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))  
and (
inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and 
CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  )) 
group by 1) 
UNION
(select CAST(''polo'' as text) as biblioteca 
,sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END) as monografie 
,percento (sum(case when tit.cd_natura in (''M'', ''W'') THEN 1 else 0 END), count(*)) as perc_monografie 
,sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END) as periodici 
,percento (sum(case when tit.cd_natura = ''S'' THEN 1 else 0 END), count(*)) as perc_periodici 
,count(*) as totale_inv 
from tbc_inventario inv, tb_titolo tit 
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2''  
and inv.bid = tit.bid 
and tit.cd_natura in (''M'',''W'',''S'') 
and (
inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
))  
group by 1) 
order by 1', 'biblioteca|monografie|%_mon_su_totale_inv_bib|periodici|%_per_su_totale_inv_bib|totale_inv', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(57, '00_GDocFis12', '00_GDocFis', 10, 'C0000', NULL, NULL, 'Totale_inventari_con_i_tipi_record_in_colonna', '1', '(select CAST(inv.cd_bib as text) as Biblioteca
,sum(case  when tit.tp_record_uni=''a'' then 1 else 0 END) as tot_a
,percento (sum(case  when tit.tp_record_uni=''a'' THEN 1 else 0 END), count(*)) as perc_a
,sum(case  when tit.tp_record_uni=''b'' then 1 else 0 END) as tot_b
,percento (sum(case  when tit.tp_record_uni=''b'' THEN 1 else 0 END), count(*)) as perc_b
,sum(case  when tit.tp_record_uni=''c'' then 1 else 0 END) as tot_c
,percento (sum(case  when tit.tp_record_uni=''c'' THEN 1 else 0 END), count(*)) as perc_c
,sum(case  when tit.tp_record_uni=''d'' then 1 else 0 END) as tot_d
,percento (sum(case  when tit.tp_record_uni=''d'' THEN 1 else 0 END), count(*)) as perc_d
,sum(case  when tit.tp_record_uni=''e'' then 1 else 0 END) as tot_e
,percento (sum(case  when tit.tp_record_uni=''e'' THEN 1 else 0 END), count(*)) as perc_e
,sum(case  when tit.tp_record_uni=''f'' then 1 else 0 END) as tot_f
,percento (sum(case  when tit.tp_record_uni=''f'' THEN 1 else 0 END), count(*)) as perc_f
,sum(case  when tit.tp_record_uni=''g'' then 1 else 0 END) as tot_g
,percento (sum(case  when tit.tp_record_uni=''g'' THEN 1 else 0 END), count(*)) as perc_g
,sum(case  when tit.tp_record_uni=''i'' then 1 else 0 END) as tot_i
,percento (sum(case  when tit.tp_record_uni=''i'' THEN 1 else 0 END), count(*)) as perc_i
,sum(case  when tit.tp_record_uni=''j'' then 1 else 0 END) as tot_j
,percento (sum(case  when tit.tp_record_uni=''j'' THEN 1 else 0 END), count(*)) as perc_j
,sum(case  when tit.tp_record_uni=''k'' then 1 else 0 END) as tot_k
,percento (sum(case  when tit.tp_record_uni=''k'' THEN 1 else 0 END), count(*)) as perc_k
,sum(case  when tit.tp_record_uni=''l'' then 1 else 0 END) as tot_l
,percento (sum(case  when tit.tp_record_uni=''l'' THEN 1 else 0 END), count(*)) as perc_l
,sum(case  when tit.tp_record_uni=''m'' then 1 else 0 END) as tot_m
,percento (sum(case  when tit.tp_record_uni=''m'' THEN 1 else 0 END), count(*)) as perc_m
,sum(case  when tit.tp_record_uni=''r'' then 1 else 0 END) as tot_r
,percento (sum(case  when tit.tp_record_uni=''r'' THEN 1 else 0 END), count(*)) as perc_r
,sum(case  when not tit.tp_record_uni in (''a'',''b'',''c'',''d'',''e'',''f'',''g'',''i'',''j'',''k'',''l'',''m'',''r'') then 1 else 0 END) as tot_null
,percento (sum(case  when not tit.tp_record_uni in (''a'',''b'',''c'',''d'',''e'',''f'',''g'',''i'',''j'',''k'',''l'',''m'',''r'') THEN 1 else 0 END), count(*)) as perc_null
,count(*) as totale_inv
,sum(case when not TRIM(inv.id_accesso_remoto) = '''' THEN 1 else 0 END) as tot_digit
,percento (sum(case when not TRIM(inv.id_accesso_remoto) = '''' THEN 1 else 0 END), 
(select count(inv1.cd_bib) from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2'' 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and (inv1.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv1.data_ingresso isnull and CAST(inv1.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) group by inv1.cd_bib) ) as perc_digit
from tbc_inventario inv, tb_titolo tit
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and inv.bid = tit.bid 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
group by inv.cd_bib order by inv.cd_bib
)
UNION
(select ''Polo''
,sum(case  when tit.tp_record_uni=''a'' then 1 else 0 END) as tot_a
,percento (sum(case  when tit.tp_record_uni=''a'' THEN 1 else 0 END), count(*)) as perc_a
,sum(case  when tit.tp_record_uni=''b'' then 1 else 0 END) as tot_b
,percento (sum(case  when tit.tp_record_uni=''b'' THEN 1 else 0 END), count(*)) as perc_b
,sum(case  when tit.tp_record_uni=''c'' then 1 else 0 END) as tot_c
,percento (sum(case  when tit.tp_record_uni=''c'' THEN 1 else 0 END), count(*)) as perc_c
,sum(case  when tit.tp_record_uni=''d'' then 1 else 0 END) as tot_d
,percento (sum(case  when tit.tp_record_uni=''d'' THEN 1 else 0 END), count(*)) as perc_d
,sum(case  when tit.tp_record_uni=''e'' then 1 else 0 END) as tot_e
,percento (sum(case  when tit.tp_record_uni=''e'' THEN 1 else 0 END), count(*)) as perc_e
,sum(case  when tit.tp_record_uni=''f'' then 1 else 0 END) as tot_f
,percento (sum(case  when tit.tp_record_uni=''f'' THEN 1 else 0 END), count(*)) as perc_f
,sum(case  when tit.tp_record_uni=''g'' then 1 else 0 END) as tot_g
,percento (sum(case  when tit.tp_record_uni=''g'' THEN 1 else 0 END), count(*)) as perc_g
,sum(case  when tit.tp_record_uni=''i'' then 1 else 0 END) as tot_i
,percento (sum(case  when tit.tp_record_uni=''i'' THEN 1 else 0 END), count(*)) as perc_i
,sum(case  when tit.tp_record_uni=''j'' then 1 else 0 END) as tot_j
,percento (sum(case  when tit.tp_record_uni=''j'' THEN 1 else 0 END), count(*)) as perc_j
,sum(case  when tit.tp_record_uni=''k'' then 1 else 0 END) as tot_k
,percento (sum(case  when tit.tp_record_uni=''k'' THEN 1 else 0 END), count(*)) as perc_k
,sum(case  when tit.tp_record_uni=''l'' then 1 else 0 END) as tot_l
,percento (sum(case  when tit.tp_record_uni=''l'' THEN 1 else 0 END), count(*)) as perc_l
,sum(case  when tit.tp_record_uni=''m'' then 1 else 0 END) as tot_m
,percento (sum(case  when tit.tp_record_uni=''m'' THEN 1 else 0 END), count(*)) as perc_m
,sum(case  when tit.tp_record_uni=''r'' then 1 else 0 END) as tot_r
,percento (sum(case  when tit.tp_record_uni=''r'' THEN 1 else 0 END), count(*)) as perc_r
,sum(case  when not tit.tp_record_uni in (''a'',''b'',''c'',''d'',''e'',''f'',''g'',''i'',''j'',''k'',''l'',''m'',''r'') then 1 else 0 END) as tot_null
,percento (sum(case  when not tit.tp_record_uni in (''a'',''b'',''c'',''d'',''e'',''f'',''g'',''i'',''j'',''k'',''l'',''m'',''r'') THEN 1 else 0 END), count(*)) as perc_null
,count(*) as totale_inv
,sum(case when not TRIM(inv.id_accesso_remoto) = '''' THEN 1 else 0 END) as tot_digit
,null
from tbc_inventario inv, tb_titolo tit
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and inv.bid = tit.bid 
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
) order by 1', 'biblioteca|testo_a_stampa|%_testo_a_stampa|testo_man|%_testo_man|musica_a_stampa|%_musica_a_stampa|musica_man|%_musica_man|cart_a_stampa|%_cart_a_stampa|cart_man|%_cart_man|mater_video|%_mater_video|reg_son_non_mus|%_reg_son_non_mus|reg_son_mus|%_reg_son_mus|mater_grafico|%_mater_grafico|ris_elettr|%_ris_elettr|mater_multim|%_mater_multim|ogg_tre_dimens|%_ogg_tre_dimens|non_impost|%_non_impost|totale_inv|tot_digit|%_digit', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(58, '00_CatPolo4', '00_CatPolo', 4, 'IC000', NULL, NULL, 'Totale_documenti_suddivisi_per_tipo_record_e_condivisione', '0', '((select 
 case t.tp_record_uni isnull when true then chr(160)||''tipo record non indicato'' else
        case trim(t.tp_record_uni)='''' when true then chr(160)||''tipo record non indicato'' else
        lower(cod.ds_tabella) end  end as materiale
,sum(case when t.fl_condiviso = ''n'' THEN 1 else 0 END) as locale
,percento (sum(case when t.fl_condiviso = ''n'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when t.fl_condiviso = ''s'' THEN 1 else 0 END) as condiviso
,percento (sum(case when t.fl_condiviso = ''s'' THEN 1 else 0 END), count(*)) as perc_condiviso
,count(t) as cnt
,to_char(CAST((count(t) * 100) AS DECIMAL) / (select count(*)  
from tb_titolo t1 where t1.fl_canc <> ''S'' 
and t1.cd_livello <> ''01'' and t1.cd_livello <> ''04'' and t1.cd_natura in (''M'',''W'',''S'',''N'') ), ''900.99'') as percentuale
from tb_titolo t
	left outer join tb_codici cod on t.tp_record_uni = cod.cd_tabella and tp_tabella = ''GEUN''
where t.fl_canc<>''S'' and t.cd_livello<>''01'' and t.cd_livello <> ''04''
 and t.cd_natura in (''M'',''W'',''S'',''N'') 
group by 1 order by 1)
UNION
(select chr(160)||chr(160)||''totale''
,sum(case when tb.fl_condiviso = ''n'' THEN 1 else 0 END) as locale
,percento (sum(case when tb.fl_condiviso = ''n'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when tb.fl_condiviso = ''s'' THEN 1 else 0 END) as condiviso
,percento (sum(case when tb.fl_condiviso = ''s'' THEN 1 else 0 END), count(*)) as perc_condiviso
,count(tb) as cnt,
to_char(CAST ((count(tb) * 100) AS DECIMAL) / (select count(*) 
from Tb_titolo tb where tb.fl_canc <> ''S'' 
and tb.cd_livello <> ''01'' and tb.cd_livello <> ''04'' and tb.cd_natura in (''M'',''W'',''S'',''N'') ), ''990.99'') as percentuale
from Tb_titolo tb 
where tb.cd_livello <> ''01'' and tb.cd_livello <> ''04'' 
and not tb.fl_canc = ''S'' and tb.cd_natura in (''M'',''W'',''S'',''N'')
group by 1)) order by 1
', 'tipo_record|locale|%_locale|condiviso|%_condiviso|totale|%_sul_totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(59, '00_CatPolo', NULL, 1, 'IC000', NULL, 0, NULL, NULL, NULL, NULL, 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(60, '00_CatBib', NULL, 2, 'IC000', NULL, 0, NULL, NULL, NULL, NULL, 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(62, '00_GDocFis13', '00_GDocFis', 11, 'C0000', NULL, NULL, 'Totale_valore_inventariale_diviso_per_biblioteca', '1', '(select 
CAST(inv.cd_bib as text) as biblioteca
,count(*) as cnt
,sum(inv.valore) as valore
from tbc_inventario inv 
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2''  
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
group by 1 order by 1)
UNION
(select ''polo''
, count(*) as totale_inv
,sum(inv.valore) as valore
from tbc_inventario inv
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
)', 'biblioteca|totale_inventari|valore_inventariale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(63, '00_GDocFis14', '00_GDocFis', 12, 'C0000', NULL, NULL, 'Totale_valore_inventariale_della biblioteca_diviso_per_tipo_materiale_inventariabile', '1', '(select 
case inv.cd_mat_inv isnull when true then chr(160)||''valore non impostato'' 
  else case trim(inv.cd_mat_inv)='''' when true then chr(160)||''valore non impostato''
  else lower((select ds_tabella from tb_codici where tp_tabella=''CTMI'' and cd_tabella=inv.cd_mat_inv))
  end  end as materiale
,count(*) as totale_inv
,percento (count(*),(select count(inv1.cd_bib) from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2'' 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and (inv1.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv1.data_ingresso isnull and CAST(inv1.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) group by inv1.cd_bib)) as perc_tot_inv_bib
,sum(inv.valore) as valore
,to_char(CAST ((sum(inv.valore) * 100) AS DECIMAL) / (select sum(inv1.valore)   
from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2'' 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and (inv1.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv1.data_ingresso isnull and CAST(inv1.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
group by inv1.cd_bib), ''900.9999'') as percentuale 
from tbc_inventario inv 
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
group by inv.cd_bib,inv.cd_mat_inv order by 1)
UNION
(select chr(160)||chr(160)||''totale della bib''
,count(*) as totale_inv
,percento (count(*),(select count(inv1.cd_bib) from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2'' 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and (inv1.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv1.data_ingresso isnull and CAST(inv1.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
group by inv1.cd_bib)) as perc_tot_inv_bib
,sum(inv.valore) as valore
,to_char(CAST ((sum(inv.valore) * 100) AS DECIMAL) / (select sum(inv1.valore)   
from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit = ''2'' 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and (inv1.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv1.data_ingresso isnull and CAST(inv1.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
group by inv1.cd_bib), ''900'') as percentuale 
from tbc_inventario inv 
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) ) 
group by inv.cd_bib) 
order by 1
', 'tipo_materiale_inv|totale_inv|%_totale_inv_bib|valore_inventariale|%_sul_valore_inv', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(133, '00_CatPolo21', '00_CatPolo', 21, 'IC000', NULL, NULL, 'Lista_identificativi_forme_accettate_prive_di_legami_a_titoli ', '0', 'select  CAST(au.VID as text) from  tb_autore au where au.fl_canc <> ''S''  and   au.tp_forma_aut = ''A'' and not exists ( select tta.vid from tr_tit_aut tta  where tta.fl_canc <> ''S''  and au.vid = tta.vid)  order by vid', 'VID', 'P', 'S');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(64, '00_GDocFis15', '00_GDocFis', 13, 'C0000', NULL, NULL, 'Totale_documenti_collocati_in_un_dato_periodo', '1', '(select CAST(inv.cd_bib as text) as biblioteca
,count(inv) as cnt
from tbc_inventario inv, tb_titolo tit 
where inv.fl_canc <> ''S'' and inv.cd_sit=''2'' 
and inv.bid = tit.bid and tit.cd_natura in (''M'',''W'',''S'')
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and CAST(inv.ts_ins_prima_coll as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 1 order by 1)
UNION
(select 
''polo''
,count(*) as cnt
from tbc_inventario inv, tb_titolo tit 
where inv.fl_canc <> ''S'' and inv.cd_sit=''2'' 
and inv.bid = tit.bid and tit.cd_natura in (''M'',''W'',''S'')
and CAST(inv.ts_ins_prima_coll as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)', 'biblioteca|documenti_collocati', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(65, '00_GDocFis16', '00_GDocFis', 14, 'C0000', NULL, NULL, 'Totale_inventari_della_biblioteca_collocati_divisi_per_tipo_materiale_catalografico', '1', '(select 
case tit.tp_materiale isnull when true then chr(160)||''tipo non impostato'' 
  else case trim(tit.tp_materiale)='''' when true then chr(160)||''tipo non impostato''
  else lower((select ds_tabella from tb_codici where tp_tabella=''MATE'' and cd_tabella=tit.tp_materiale))
  end  end as materiale
,count(*) as totale_inv
,percento (count(*),(select count(inv1.cd_bib) from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit=''2'' 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and CAST(inv1.ts_ins_prima_coll as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
group by inv1.cd_bib)) as perc_totale_inv_bib
FROM tbc_inventario inv, tb_titolo tit 
WHERE inv.FL_CANC <> ''S'' AND inv.cd_sit=''2'' and inv.bid = tit.bid 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and CAST(inv.ts_ins_prima_coll as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
GROUP BY inv.cd_bib,tit.tp_materiale 
order by 1)  
UNION
(select chr(160)||chr(160)||''totale della bib''
,count(*) as totale_inv
,percento (count(*),(select count(inv1.cd_bib) from tbc_inventario inv1 
where inv1.cd_bib = inv.cd_bib and inv1.fl_canc <> ''S'' and inv1.cd_sit=''2'' 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and CAST(inv1.ts_ins_prima_coll as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
group by inv1.cd_bib)) as perc_totale_inv_bib
FROM tbc_inventario inv, tb_titolo tit 
WHERE inv.FL_CANC <> ''S'' AND inv.cd_sit=''2'' and inv.bid = tit.bid 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and CAST(inv.ts_ins_prima_coll as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
group by inv.cd_bib)
', 'tipo_materiale_cat|totale_inv|%_su_totale_inv', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(66, '00_CatBib1', '00_CatBib', 1, 'IC000', NULL, NULL, 'Totale_titoli_suddivisi_per_biblioteca_e_natura', '1', '((select tt.bib_ute_ins as biblioteca
, CAST(tt.cd_natura as text) as natura
, count(*) as cnt
,percento (count(tt), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  tt.bib_ute_ins
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
 and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
from vl_titolo_stat tt 
where tt.cd_livello <> ''01'' and tt.cd_livello<>''04'' 
and ('''' in (:0lista) or tt.bib_ute_ins in (:0lista))
group by 1,2
order by 1,2)
UNION
(select t.bib_ute_ins as biblioteca 
,chr(160)||chr(160)||''Totale titoli della bib'' 
, count(*) as cnt
,percento (count(t), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  t.bib_ute_ins
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
 and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by t1.bib_ute_ins) ) as perc_tot_bib
 from vl_titolo_stat t
where t.cd_livello <> ''01'' and t.cd_livello <> ''04''
and ('''' in (:0lista) or t.bib_ute_ins in (:0lista))
group by 1)) order by 1,2', 'biblioteca|natura|totale|%_su_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(67, '00_CatPolo9', '00_CatPolo', 9, 'IC000', NULL, NULL, 'Totale_documenti_per_un_dato_periodo_di_pubblicazione', '1', 'select  count(t) as cnt
from tb_titolo t
where t.fl_canc<>''S'' and t.cd_livello<>''01'' and t.cd_livello<>''04''
and t.cd_natura in (''M'',''W'',''S'',''C'')
and not t.aa_pubb_1 isnull 
and t.aa_pubb_1 between COALESCE(cast(:1daAnno as text),''    '') 
and COALESCE(cast(:2aAnno as text), ''9999'') ', 'totale_documenti', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(68, '00_CatPolo10', '00_CatPolo', 10, 'IC000', NULL, NULL, 'Totale_autori_suddivisi_per_forma_e_tipo_nome', '0', 'select
CAST(au.tp_nome_aut as text)||  '' ('' ||
lower((select ds_tabella from tb_codici where tp_tabella=''AUTO'' and cd_tabella=au.tp_nome_aut)) 
|| '')'' as tipo_nome
,sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END) as formaA
,percento (sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END), count(*)) as perc_formaA
,sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END) as formaR
,percento (sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END), count(*)) as perc_formaR
from tb_autore au 
where au.fl_canc <> ''S'' 
group by 1 order by 1)
UNION
(select chr(160)||''tutti i tipi'' 
,sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END) as formaA
,percento (sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END), count(*)) as perc_formaA
,sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END) as formaR
,percento (sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END), count(*)) as perc_formaR
from tb_autore au 
where au.fl_canc <> ''S'')
', 'tipo_nome|forma_accettata|%_forma_accettata|forma_rinvio|%_forma_rinvio', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(69, '00_CatPolo11', '00_CatPolo', 11, 'IC000', NULL, NULL, 'Totale_luoghi_suddivisi_per_forma', '0', 'select
case lu.tp_forma = ''A'' when true then ''accettata'' 
  else case lu.tp_forma=''R'' when true then ''rinvio''
  end end as forma
,count(*) as totale_luoghi
,percento (count(*),(select count(lu1) from tb_luogo lu1 
where lu1.fl_canc <> ''S'')
) as perc_tot_luoghi
from tb_luogo lu 
where lu.fl_canc <> ''S''
group by lu.tp_forma order by 1)
UNION
(select ''totale''
,count(*) as totale_luoghi
,percento (count(*),(select count(lu1) from tb_luogo lu1 
where lu1.fl_canc <> ''S'')
) as perc_tot_luoghi
from tb_luogo lu 
where lu.fl_canc <> ''S''
)', 'forma|totale_luoghi|%_sul_totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(70, '00_CatPolo12', '00_CatPolo', 12, 'IC000', NULL, NULL, 'Totale_soggetti_suddivisi_per_soggettario', '0', '(select CAST(so.cd_soggettario as text) as soggettario
,count(*) as totale_sogg
,percento (count(*),(select count(so1) from tb_soggetto so1 
where so1.fl_canc <> ''S'')
) as perc_tot_sogg
from tb_soggetto so 
where so.fl_canc <> ''S''
group by so.cd_soggettario order by 1)
UNION
(select ''totale''
,count(*) as totale_sogg
,percento (count(*),(select count(so1) from tb_soggetto so1 
where so1.fl_canc <> ''S'')
) as perc_tot_sogg
from tb_soggetto so 
where so.fl_canc <> ''S''
)
', 'soggettario|totale_soggetti|%_sul_totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(71, '00_CatPolo13', '00_CatPolo', 13, 'IC000', NULL, NULL, 'Totale_descrittori_suddivisi_per_forma', '0', 'select
case de.tp_forma_des = ''A'' when true then ''accettata'' 
  else case de.tp_forma_des=''R'' when true then ''rinvio''
  end end as forma
,count(*) as totale_descr
,percento (count(*),(select count(de1) from tb_descrittore de1 
where de1.fl_canc <> ''S'')
) as perc_tot_descr 
from tb_descrittore de 
where de.fl_canc <> ''S''
group by de.tp_forma_des order by 1)
UNION
(select ''totale''
,count(*) as totale_descr
,percento (count(*),(select count(de1) from tb_descrittore de1 
where de1.fl_canc <> ''S'')
) as perc_tot_descr 
from tb_descrittore de 
where de.fl_canc <> ''S''
)', 'forma|totale_descr|%_sul_totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(73, '00_CatBib2', '00_CatBib', 2, 'IC000', NULL, NULL, 'Totale_documenti_suddivisi_per_biblioteca_tipo_materiale_e_condivisione', '1', '((select t.bib_ute_ins as biblioteca
,case t.tp_materiale isnull when true then ''tipo materiale non indicato'' else
        case trim(t.tp_materiale)='''' when true then ''tipo materiale non indicato'' else
        lower(cod.ds_tabella) end  end as materiale
,sum(case when t.fl_condiviso = ''n'' THEN 1 else 0 END) as locale
,percento (sum(case when t.fl_condiviso = ''n'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when t.fl_condiviso = ''s'' THEN 1 else 0 END) as condiviso
,percento (sum(case when t.fl_condiviso = ''s'' THEN 1 else 0 END), count(*)) as perc_condiviso
,count(t) as cnt
,percento (count(t), (select count(t1) from vl_titolo_stat t1 
where t1.bib_ute_ins =  t.bib_ute_ins
 and t1.cd_natura in (''M'',''W'',''S'',''N'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
 and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
from vl_titolo_stat t
	left outer join tb_codici cod on t.tp_materiale = cod.cd_tabella and tp_tabella = ''MATE''
where cd_livello<>''01'' and cd_livello<>''04'' 
and cd_natura in (''M'',''W'',''S'',''N'')
and ('''' in (:0lista) or t.bib_ute_ins in (:0lista))
group by 1,2 order by 1,2)
UNION
(select tb.bib_ute_ins as biblioteca
,chr(160)||chr(160)||''totale biblioteca''
,sum(case when tb.fl_condiviso = ''n'' THEN 1 else 0 END) as locale
,percento (sum(case when tb.fl_condiviso = ''n'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when tb.fl_condiviso = ''s'' THEN 1 else 0 END) as condiviso
,percento (sum(case when tb.fl_condiviso = ''s'' THEN 1 else 0 END), count(*)) as perc_condiviso
,count(tb) as cnt
,percento (count(tb), (select count(t1) from vl_titolo_stat t1 
where t1.bib_ute_ins =  tb.bib_ute_ins
 and t1.cd_natura in (''M'',''W'',''S'',''N'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
 and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by t1.bib_ute_ins) ) as perc_tot_bib
from vl_titolo_stat tb 
where tb.cd_livello <> ''01'' and tb.cd_livello <> ''04'' 
and tb.cd_natura in (''M'',''W'',''S'',''N'')
and ('''' in (:0lista) or tb.bib_ute_ins in (:0lista))
group by 1)) order by 1', 'biblioteca|materiale|locale|%_locale|condiviso|%_condiviso|totale|%_su_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(74, '00_CatBib3', '00_CatBib', 3, 'IC000', NULL, NULL, 'Totale_documenti_suddivisi_per_biblioteca_tipo_record_e_condivisione', '1', '((select t.bib_ute_ins as biblioteca
, case t.tp_record_uni isnull when true then chr(160)||''tipo record non indicato'' else
        case trim(t.tp_record_uni)='''' when true then chr(160)||''tipo record non indicato'' else
        lower(cod.ds_tabella) end  end as tiporecord
,sum(case when t.fl_condiviso = ''n'' THEN 1 else 0 END) as locale
,percento (sum(case when t.fl_condiviso = ''n'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when t.fl_condiviso = ''s'' THEN 1 else 0 END) as condiviso
,percento (sum(case when t.fl_condiviso = ''s'' THEN 1 else 0 END), count(*)) as perc_condiviso
,count(t) as cnt
,percento (count(t), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  t.bib_ute_ins
 and t1.cd_natura in (''M'',''W'',''S'',''N'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
 and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
from vl_titolo_stat t
	left outer join tb_codici cod on t.tp_record_uni = cod.cd_tabella and tp_tabella = ''GEUN''
where cd_livello<>''01'' and cd_livello<>''04'' 
and cd_natura in (''M'',''W'',''S'',''N'')
and ('''' in (:0lista) or t.bib_ute_ins in (:0lista))
group by 1,2 order by 1,2)
UNION
(select tb.bib_ute_ins as biblioteca
,chr(160)||chr(160)||''totale biblioteca''
,sum(case when tb.fl_condiviso = ''n'' THEN 1 else 0 END) as locale
,percento (sum(case when tb.fl_condiviso = ''n'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when tb.fl_condiviso = ''s'' THEN 1 else 0 END) as condiviso
,percento (sum(case when tb.fl_condiviso = ''s'' THEN 1 else 0 END), count(*)) as perc_condiviso
,count(tb) as cnt
,percento (count(tb), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  tb.bib_ute_ins
 and t1.cd_natura in (''M'',''W'',''S'',''N'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
  and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by t1.bib_ute_ins) ) as perc_tot_bib
from vl_titolo_stat tb 
where tb.cd_livello <> ''01'' and tb.cd_livello <> ''04'' 
and tb.cd_natura in (''M'',''W'',''S'',''N'')
and ('''' in (:0lista) or tb.bib_ute_ins in (:0lista))
group by 1)) order by 1', 'biblioteca|tipo_record|locale|%_locale|condiviso|%_condiviso|totale|%_su_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(75, '00_CatBib4', '00_CatBib', 4, 'IC000', NULL, NULL, 'Totale_documenti_creati_e_catturati_suddivisi_per_biblioteca_natura_e_tipo_materiale', '1', '((select t.bib_ute_ins as biblioteca 
,case t.tp_materiale isnull when true then ''tipo materiale non indicato'' else
        case trim(t.tp_materiale)='''' when true then ''tipo materiale non indicato'' else
        lower(cod.ds_tabella) end  end as materiale
,CAST(t.cd_natura as text) as natura
,sum(case when substring(t.bid,1,3)=(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END) as creati
,percento (sum(case when substring(t.bid,1,3)=(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END), count(*)) as perc_creati
,sum(case when substring(t.bid,1,3)<>(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END) as catturati
,percento (sum(case when substring(t.bid,1,3)<>(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END), count(*)) as perc_catturati
,count(t) as totale
,percento (count(t), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  t.bib_ute_ins
 and t1.cd_natura in (''M'',''W'',''S'',''N'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
  and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
from vl_titolo_stat t
	left outer join tb_codici cod on t.tp_materiale = cod.cd_tabella and tp_tabella = ''MATE''
where cd_livello<>''01'' and cd_livello<>''04'' 
and cd_natura in (''M'',''W'',''S'',''N'')
and ('''' in (:0lista) or t.bib_ute_ins in (:0lista))
group by 1,2,3 order by 1,2,3)
UNION
(select tb.bib_ute_ins as biblioteca
,chr(160)||chr(160)||''totale biblioteca'', null
,sum(case when substring(tb.bid,1,3)=(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END) as creati
,percento (sum(case when substring(tb.bid,1,3)=(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END), count(*)) as perc_creati
,sum(case when substring(tb.bid,1,3)<>(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END) as catturati
,percento (sum(case when substring(tb.bid,1,3)<>(select p.cd_polo from Tbf_polo p) THEN 1 else 0 END), count(*)) as perc_catturati
,count(tb) as totale
,percento (count(tb), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  tb.bib_ute_ins
 and t1.cd_natura in (''M'',''W'',''S'',''N'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
  and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by t1.bib_ute_ins) ) as perc_tot_bib
from vl_titolo_stat tb 
where tb.cd_livello <> ''01'' and tb.cd_livello <> ''04'' 
and tb.cd_natura in (''M'',''W'',''S'',''N'')
and ('''' in (:0lista) or tb.bib_ute_ins in (:0lista))
group by 1)) order by 1', 'biblioteca|materiale|natura|creati|%_creati|catturati|%_catturati|totale|%_su_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(76, '00_CatBib5', '00_CatBib', 5, 'IC000', NULL, NULL, 'Totale_documenti_suddivisi_per_biblioteca_e_genere', '1', '(select t.bib_ute_ins as biblioteca
,case t.cd_genere_1 isnull when true then chr(160)||''Valore non impostato'' else 
        case trim(t.cd_genere_1)='''' when true then chr(160)||''Valore non indicato'' else
        CAST(c.ds_tabella as text)  end  end 
,count(*)
,percento (count(t), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  t.bib_ute_ins
 and t1.cd_natura in (''M'',''W'',''S'',''C'',''N'',''T'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
 and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
 from vl_titolo_stat t
 left outer join tb_codici c on t.cd_genere_1 = c.cd_tabella and tp_tabella = ''GEPU''
where t.cd_livello <> ''01'' and t.cd_livello <> ''04'' 
and cd_natura in (''M'',''W'',''S'',''C'',''N'',''T'')
and ('''' in (:0lista) or t.bib_ute_ins in (:0lista))
group by 1,2 order by 1,2)
UNION
(select t.bib_ute_ins as biblioteca 
,chr(160)||chr(160)||''Totale titoli della bib'' 
, count(*) as cnt
,percento (count(t), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  t.bib_ute_ins
 and t1.cd_natura in (''M'',''W'',''S'',''C'',''N'',''T'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
 and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
 from vl_titolo_stat t
where t.cd_natura in (''M'',''W'',''S'',''C'',''N'',''T'') 
and t.cd_livello <> ''01'' and t.cd_livello <> ''04''
and ('''' in (:0lista) or t.bib_ute_ins in (:0lista))
group by 1)
UNION
(select  chr(160)||chr(160)||'''', chr(160)||chr(160)||chr(160)||''Generi presenti nel polo'' 
,count(distinct t.cd_genere_1),
null
from vl_titolo_stat t)', 'biblioteca|genere (primo codice)|documenti|%_sul_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(77, '00_CatBib6', '00_CatBib', 6, 'IC000', NULL, NULL, 'Totale_documenti_suddivisi_per_biblioteca_e_paese_di_pubblicazione', '1', '((select t.bib_ute_ins as biblioteca
,case t.cd_paese isnull when true then chr(160)||chr(160)||''Paese non indicato'' else
        case trim(t.cd_paese)='''' when true then chr(160)||chr(160)||''Paese non indicato'' else
        cod.ds_tabella end  end 
,case t.cd_paese isnull when true then '''' else
        case trim(t.cd_paese)='''' when true then '''' else
        CAST (cd_paese AS text) end  end
,count(*) as cnt 
,percento (count(t), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  t.bib_ute_ins
 and t1.cd_natura in (''M'',''W'',''S'',''C'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
 and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
 from vl_titolo_stat t
	left outer join tb_codici cod on t.cd_paese = cod.cd_tabella and cod.tp_tabella = ''PAES''
where t.cd_natura in (''M'', ''W'', ''S'', ''C'')  
and t.cd_livello <> ''01'' and t.cd_livello <> ''04''
and ('''' in (:0lista) or t.bib_ute_ins in (:0lista))
group by 1,2,3 order by 1,2)
UNION
(select t.bib_ute_ins as biblioteca 
,chr(160)||chr(160)||''Totale titoli della bib'', chr(160)||'''', count(*) as cnt
,percento (count(t), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  t.bib_ute_ins
 and t1.cd_natura in (''M'',''W'',''S'',''C'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
 and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
 from vl_titolo_stat t
where t.cd_natura in (''M'', ''W'', ''S'', ''C'') 
and t.cd_livello <> ''01'' and t.cd_livello <> ''04''
and ('''' in (:0lista) or t.bib_ute_ins in (:0lista))
group by 1)
UNION
(select chr(160)||chr(160)||'''', chr(160)||chr(160)||chr(160)||''Paesi presenti nel polo'', '' '',
count(distinct t.cd_paese), null  
from vl_titolo_stat t 
where cd_natura in (''M'', ''W'', ''S'', ''C'') 
and cd_livello <> ''01'' and cd_livello <> ''04''
)) order by 1,2', 'biblioteca|paese|codice paese|documenti|%_su_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(78, '00_CatBib7', '00_CatBib', 7, 'IC000', NULL, NULL, 'Totale_documenti_suddivisi_per_biblioteca_e_lingua_di_pubblicazione', '1', '((select t.bib_ute_ins as biblioteca 
,case t.cd_lingua_1 isnull when true then chr(160)||''Lingua non indicata'' else
        case trim(t.cd_lingua_1)='''' when true then chr(160)||''Lingua non indicata'' else
        cod.ds_tabella end  end
,case t.cd_lingua_1 isnull when true then '''' else
        case trim(t.cd_lingua_1)='''' when true then '''' else
        CAST (cd_lingua_1 AS text) end  end
,count(*) as cnt 
,percento (count(t), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  t.bib_ute_ins
 and t1.cd_natura in (''M'', ''W'', ''S'', ''N'', ''T'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
 and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
 from vl_titolo_stat t
	left outer join tb_codici cod on cd_lingua_1 = cod.cd_tabella and tp_tabella = ''LING''
where t.cd_natura in (''M'', ''W'', ''S'', ''N'', ''T'')  
and t.cd_livello <> ''01'' and t.cd_livello <> ''04''
and ('''' in (:0lista) or t.bib_ute_ins in (:0lista))
group by 1,2,3 order by 1,2)
UNION
(select t.bib_ute_ins as biblioteca 
,chr(160)||chr(160)||''Totale titoli della bib'', chr(160)||'''', count(*) as cnt
,percento (count(t), (select count(t1) from vl_titolo_stat t1 
 where t1.bib_ute_ins =  t.bib_ute_ins
 and t1.cd_natura in (''M'', ''W'', ''S'', ''N'', ''T'') 
 and t1.cd_livello<>''01'' and t1.cd_livello<>''04'' 
 and ('''' in (:0lista) or t1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
 from vl_titolo_stat t
where t.cd_natura in (''M'', ''W'', ''S'', ''N'', ''T'') 
and t.cd_livello <> ''01'' and t.cd_livello <> ''04''
and ('''' in (:0lista) or t.bib_ute_ins in (:0lista))
group by 1)
UNION
(select chr(160)||chr(160)||'''', chr(160)||chr(160)||chr(160)||''Lingue presenti nel polo'', '' '',
count(distinct t.cd_lingua_1), null  
from vl_titolo_stat t 
where cd_natura in (''M'', ''W'', ''S'', ''N'', ''T'') 
and cd_livello <> ''01'' and cd_livello <> ''04''
)) order by 1,2', 'biblioteca|lingua|codice lingua (primo codice)|documenti|%_su_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(79, '00_CatBib8', '00_CatBib', 8, 'IC000', NULL, NULL, 'Totale_documenti_per_un_dato_periodo_di_pubblicazione_suddivisi_per_biblioteca', '1', '(select t.bib_ute_ins as biblioteca 
,count(t) as cnt
from vl_titolo_stat t
where t.cd_livello<>''01'' and t.cd_livello<>''04''
and t.cd_natura in (''M'',''W'',''S'',''C'')
and not t.aa_pubb_1 isnull 
and t.aa_pubb_1 between COALESCE(cast(:1daAnno as text),''    '') 
and COALESCE(cast(:2aAnno as text), ''9999'') 
and ('''' in (:0lista) or t.bib_ute_ins in (:0lista)) 
group by 1 order by 1)
UNION
(select chr(160)||''Totale del polo''
,count(t) as cnt
from vl_titolo_stat t
where t.cd_livello<>''01'' and t.cd_livello<>''04''
and t.cd_natura in (''M'',''W'',''S'',''C'')
and not t.aa_pubb_1 isnull 
and t.aa_pubb_1 between COALESCE(cast(:1daAnno as text),''    '') 
and COALESCE(cast(:2aAnno as text), ''9999''))
order by 1 ', 'biblioteca|totale_documenti', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(80, '00_CatBib9', '00_CatBib', 9, 'IC000', NULL, NULL, 'Totale_autori_suddivisi_per_biblioteca_forma_e_tipo_nome', '1', '((select au.bib_ute_ins as biblioteca 
,CAST(au.tp_nome_aut as text)||  '' ('' ||
lower((select ds_tabella from tb_codici where tp_tabella=''AUTO'' and cd_tabella=au.tp_nome_aut)) 
|| '')'' as tipo_nome
,sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END) as formaA
,percento (sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END), count(*)) as perc_formaA
,sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END) as formaR
,percento (sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END), count(*)) as perc_formaR
,count(au) as cnt
,percento (count(au), (select count(au1) from vl_autore_stat au1 
 where au1.bib_ute_ins =  au.bib_ute_ins
 and ('''' in (:0lista) or au1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
from vl_autore_stat au 
where ('''' in (:0lista) or au.bib_ute_ins in (:0lista))
group by 1,2 order by 1,2)
UNION
(select au.bib_ute_ins || '' - totale'' as biblioteca
,chr(160)||''tutti i tipi'' 
,sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END) as formaA
,percento (sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END), count(*)) as perc_formaA
,sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END) as formaR
,percento (sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END), count(*)) as perc_formaR
,count(au) as cnt
,percento (count(au), (select count(au1) from vl_autore_stat au1 
 where au1.bib_ute_ins =  au.bib_ute_ins
 and ('''' in (:0lista) or au1.bib_ute_ins in (:0lista))
 group by bib_ute_ins) ) as perc_tot_bib
from vl_autore_stat au 
where ('''' in (:0lista) or au.bib_ute_ins in (:0lista))
group by au.bib_ute_ins)
UNION
(select chr(160)||chr(160)||chr(160)||''Totale del polo'', chr(160)||''tutti i tipi''
,sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END) as formaA
,percento (sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END), count(*)) as perc_formaA
,sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END) as formaR
,percento (sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END), count(*)) as perc_formaR
,count(au) as cnt
,NULL
from vl_autore_stat au )
) order by 1', 'biblioteca|tipo_nome|forma_accettata|%_forma_accettata|forma_rinvio|%_forma_rinvio|totale_del_tipo|%_su_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(81, '00_CatBib10', '00_CatBib', 10, 'IC000', NULL, NULL, 'Totale_luoghi_suddivisi_per_biblioteca_e_forma', '1', '((select au.bib_ute_ins as biblioteca 
,sum(case when au.tp_forma = ''A'' THEN 1 else 0 END) as formaA
,percento (sum(case when au.tp_forma = ''A'' THEN 1 else 0 END), count(*)) as perc_formaA
,sum(case when au.tp_forma = ''R'' THEN 1 else 0 END) as formaR
,percento (sum(case when au.tp_forma = ''R'' THEN 1 else 0 END), count(*)) as perc_formaR
,count(au) as tot_luoghi
from vl_luogo_stat au 
where ('''' in (:0lista) or au.bib_ute_ins in (:0lista))
group by 1 order by 1)
UNION
(select chr(160)||chr(160)||chr(160)||''Totale del polo''
,sum(case when au.tp_forma = ''A'' THEN 1 else 0 END) as formaA
,percento (sum(case when au.tp_forma = ''A'' THEN 1 else 0 END), count(*)) as perc_formaA
,sum(case when au.tp_forma = ''R'' THEN 1 else 0 END) as formaR
,percento (sum(case when au.tp_forma = ''R'' THEN 1 else 0 END), count(*)) as perc_formaR
,count(au) as cnt
from vl_luogo_stat au )
) order by 1

', 'biblioteca|forma_accettata|%_forma_accettata|forma_rinvio|%_forma_rinvio|totale_luoghi', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(82, '00_CatBib11', '00_CatBib', 11, 'IC000', NULL, NULL, 'Totale_soggetti_suddivisi_per_biblioteca_e_soggettario', '1', '((select so.bib_ute_ins as biblioteca
,CAST(so.cd_soggettario as text) as soggettario
,count(*) as totale_sogg
,percento (count(*),(select count(so1) from vl_soggetto_stat so1 
 where so1.bib_ute_ins =  so.bib_ute_ins
group by so.bib_ute_ins )) as perc_tot_sogg
from vl_soggetto_stat so 
where ('''' in (:0lista) or so.bib_ute_ins in (:0lista))
group by 1,2 order by 1,2)
UNION
(select so.bib_ute_ins || '' - totale'' as biblioteca , '' ''
,count(*) as totale_sogg
,percento (count(*),(select count(so1) from vl_soggetto_stat so1 
 where so1.bib_ute_ins =  so.bib_ute_ins
)) as perc_tot_sogg
from vl_soggetto_stat so
where ('''' in (:0lista) or so.bib_ute_ins in (:0lista))
group by so.bib_ute_ins
)
UNION
(select chr(160)||chr(160)||chr(160)||''Totale del polo'', '' ''
,count(so) as cnt, null
from vl_soggetto_stat so )
) order by 1', 'biblioteca|soggettario|totale_soggetti|%_su_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(83, '00_CatBib12', '00_CatBib', 12, 'IC000', NULL, NULL, 'Totale_descrittori_suddivisi_per_biblioteca_e_forma', '1', '((select de.bib_ute_ins as biblioteca
,sum(case when de.tp_forma_des = ''A'' THEN 1 else 0 END) as formaA
,percento (sum(case when de.tp_forma_des = ''A'' THEN 1 else 0 END), count(*)) as perc_formaA
,sum(case when de.tp_forma_des = ''R'' THEN 1 else 0 END) as formaR
,percento (sum(case when de.tp_forma_des = ''R'' THEN 1 else 0 END), count(*)) as perc_formaR
,count(*) as totale_descr
,percento (count(de), (select count(de1) from vl_descrittore_stat de1) ) as perc_tot_polo
from vl_descrittore_stat de 
where ('''' in (:0lista) or de.bib_ute_ins in (:0lista))
group by 1 order by 1)
UNION
(select chr(160)||chr(160)||chr(160)||''Totale del polo''
,sum(case when de.tp_forma_des = ''A'' THEN 1 else 0 END) as formaA
,percento (sum(case when de.tp_forma_des = ''A'' THEN 1 else 0 END), count(*)) as perc_formaA
,sum(case when de.tp_forma_des = ''R'' THEN 1 else 0 END) as formaR
,percento (sum(case when de.tp_forma_des = ''R'' THEN 1 else 0 END), count(*)) as perc_formaR
,count(*) as totale_descr
,percento (count(de), (select count(de1) from vl_descrittore_stat de1) ) as perc_tot_polo
from vl_descrittore_stat de)
) order by 1', 'biblioteca|forma_accettata|%_forma_accettata|forma_rinvio|%forma_rinvio|totale_descr|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(85, '00_CatBib14', '00_CatBib', 14, 'IC000', NULL, NULL, 'Totale_marche_suddivise_per_biblioteca', '1', '((select ma.bib_ute_ins as biblioteca 
,count(ma) as tot_marche
,percento (count(*),(select count(ma1) from vl_marca_stat ma1)) as perc_tot_polo
from vl_marca_stat ma 
where ('''' in (:0lista) or ma.bib_ute_ins in (:0lista))
group by 1 order by 1)
UNION
(select chr(160)||chr(160)||chr(160)||''Totale del polo''
,count(ma) as cnt
,percento (count(*),(select count(ma1) from vl_marca_stat ma1)) as perc_tot_polo
from vl_marca_stat ma )
) order by 1', 'biblioteca|totale_marche|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(86, '00_GestBib1', '00_GestBib', 1, 'IC000', NULL, NULL, 'Totale_titoli_inseriti_in_un_dato_periodo_suddivisi_per_natura', '1', '(select CAST(_ti.cd_natura as text) as natura 
,count(*) as cnt
,percento(count(_ti),(select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'')) as percentuale
from tb_titolo _ti 
where _ti.fl_canc <>''S'' and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04''
and CAST(_ti.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 1 order by 1)
UNION
(select chr(160)||''Totale polo'' 
,count(*) as cnt
,case (select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'') = 0 when true then 100
else 
percento(count(*),(select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'')) end as percentuale  
from tb_titolo _ti 
where _ti.fl_canc <>''S'' and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04''
and CAST(_ti.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)
', 'natura|titoli|%', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(134, '00_CatPolo22', '00_CatPolo', 22, 'IC000', NULL, NULL, 'Lista_identificativi_forme_di_rinvio_prive_di_legami', '0', 'select  CAST(au.VID as text) from  tb_autore au where au.fl_canc <> ''S''  and   au.tp_forma_aut = ''R'' and not exists ( select taa.vid_coll from tr_aut_aut taa  where taa.fl_canc <> ''S'' and au.vid = taa.vid_coll) order by vid', 'VID', 'P', 'S');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(87, '00_GestBib2', '00_GestBib', 2, 'IC000', NULL, NULL, 'Totale_autori_inseriti_in_un_dato_periodo_suddivisi_per_forma_e_tipo_nome', '1', 'select
CAST(au.tp_nome_aut as text)||  '' ('' ||
lower((select ds_tabella from tb_codici where tp_tabella=''AUTO'' and cd_tabella=au.tp_nome_aut)) 
|| '')'' as tipo_nome
,sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END) as formaA
,percento (sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END), count(*)) as perc_formaA
,sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END) as formaR
,percento (sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END), count(*)) as perc_formaR
,count(au) as cnt
,percento (count(au), (select count(au1) from tb_autore au1 
 where au1.fl_canc <>''S'')) as perc_tot_polo
from tb_autore au 
where au.fl_canc <> ''S'' 
and CAST(au.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 1 order by 1)
UNION
(select chr(160)||''Tutti i tipi'' 
,sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END) as formaA
,percento (sum(case when au.tp_forma_aut = ''A'' THEN 1 else 0 END), count(*)) as perc_formaA
,sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END) as formaR
,percento (sum(case when au.tp_forma_aut = ''R'' THEN 1 else 0 END), count(*)) as perc_formaR
,count(au) as cnt
,case 
(select count(au1) from tb_autore au1 
 where au1.fl_canc <>''S'' ) = 0 when true then 100
else 
percento(count(*),(select count(au1) from tb_autore au1 
 where au1.fl_canc <>''S'' )) end as perc_tot_polo
from tb_autore au 
where au.fl_canc <> ''S''
and CAST(au.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) 
', 'tipo_nome|forma_accettata|%_forma_accettata|forma_rinvio|%_forma_rinvio|totale|%_su_totale_polo
', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(88, '00_GestBib3', '00_GestBib', 3, 'IC000', NULL, NULL, 'Totale_marche_inserite_in_un_dato_periodo', '1', 'select  count(ma) as tot_marche
from tb_marca ma 
where CAST(ma.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
', 'totale_marche', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(89, '00_GestBib4', '00_GestBib', 4, 'IC000', NULL, NULL, 'Totale_luoghi_inseriti_in_un_dato_periodo_suddivisi_per_forma', '1', 'select
case lu.tp_forma = ''A'' when true then ''accettata'' 
  else case lu.tp_forma=''R'' when true then ''rinvio''
  end end as forma
,count(*) as totale_luoghi
,percento (count(*),(select count(lu1) from tb_luogo lu1 
where lu1.fl_canc <> ''S''
and CAST(lu1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')))
) as perc_tot_luoghi
from tb_luogo lu 
where lu.fl_canc <> ''S'' 
and CAST(lu.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by lu.tp_forma order by 1)
UNION
(select ''totale''
,count(*) as totale_luoghi
,case (select count(lu1) from tb_luogo lu1 
where lu1.fl_canc <> ''S'' 
and CAST(lu1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) = 0 when true then 100
else 
percento(count(*),(select count(lu1) from tb_luogo lu1 
where lu1.fl_canc <> ''S'' 
and CAST(lu1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')))) end as perc_tot_luoghi  
from tb_luogo lu 
where lu.fl_canc <> ''S'' 
and CAST(lu.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)', 'forma|totale_luoghi|%_sul_totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(91, '00_GestSem2', '00_GestSem', 2, 'IC000', NULL, NULL, 'Totale_descrittori_inseriti_in_un_dato_periodo_suddivisi_per_forma', '1', 'select
CAST(de.cd_soggettario as text) ||
 CAST(case de.cd_edizione isnull when true then '''' else '' - ''||de.cd_edizione end as text) as sogg_edizione,
case de.tp_forma_des = ''A'' when true then ''accettata'' 
  else case de.tp_forma_des=''R'' when true then ''rinvio''
  end end as forma
,count(*) as totale_descr
,percento (count(*),(select count(de1) from tb_descrittore de1 
where de1.fl_canc <> ''S'' 
and CAST(de1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_tot_descr 
from tb_descrittore de 
where de.fl_canc <> ''S'' 
and CAST(de.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 1,2 order by 1,2)
UNION
(select ''totale'', null
,count(*) as totale_descr
,100
from tb_descrittore de 
where de.fl_canc <> ''S''
and CAST(de.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)', 'soggettario-edizione|forma|totale|%_sul_totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(159, '00_GMonitoraggio10', '00_GMonitoraggio', 10, 'T0100', NULL, NULL, 'Lista_dei_bid_collocati_in_un_dato_periodo_con_indicazione_del_gruppo_operatori_che_li_hanno_inseriti', '1', '(select 
CAST(tb.bid as text), CAST(bbb.cd_biblioteca as text), CAST(bbb.ufficio_appart as text), 
tb.cd_natura, tb.tp_record_uni, tb.tp_materiale, CAST(tb.aa_pubb_1 as text), 
CAST(inv.cd_bib||'' ''||inv.cd_serie||'' ''||inv.cd_inven as text) as inven, 
CAST(col.cd_sez||'' ''||col.cd_loc||'' ''||col.spec_loc as text) || 
	CAST(case (trim(inv.seq_coll) > '''' and inv.seq_coll notnull) when true then ''/''||inv.seq_coll
    	 else '''' end as text) as colloc,
CAST(tb.isbd as text)
from tb_titolo tb
inner join tbf_utenti_professionali_web utp on utp.userid = substring(tb.ute_ins,7,char_length(tb.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
inner join tbc_inventario inv on inv.bid=tb.bid 
inner join tbc_collocazione col on inv.key_loc=col.key_loc 
where inv.fl_canc<>''S'' and inv.cd_sit=''2'' and col.fl_canc<>''S'' 
and ('''' in (:0lista) or inv.cd_bib in (:0lista)) 
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista)) 
and CAST(col.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end 
order by 1,8);', 'bid|biblioteca|ufficio|natura|tipo_record|tipo_materiale|data1|inventario|collocazione|isbd', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(93, '00_GestSem4', '00_GestSem', 4, 'IC002', NULL, NULL, 'Totale_legami_titolo_soggetto_in_un_dato_periodo_suddivisi_per_biblioteca', '1', '(select CAST(ts.cd_biblioteca as text) as Biblioteca 
,count(*) as totale_legami
,percento(count(*),(select count(*) from tr_tit_sog_bib ts1 
	where ts1.fl_canc<>''S'' 
and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)) as perc_su_tot_polo   
from tr_tit_sog_bib ts where ts.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
group by ts.cd_biblioteca  order by ts.cd_biblioteca) 
UNION
(select chr(160)||''Polo''  as Biblioteca 
,count(*) as totale_legami
,100 
from tr_tit_sog_bib ts where ts.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
) 
', 'biblioteca|totale_legami|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(96, '00_GestSem7', '00_GestSem', 7, 'IC002', NULL, NULL, 'Totale_titoli_classificati_in_un_dato_periodo_suddivisi_per_sistema_di_classificazione', '1', '(select CAST(ts.cd_sistema as text) as sistema 
,count(distinct ts.cd_sistema||ts.bid) as tot_tit_class
,percento(count(distinct ts.cd_sistema||ts.bid),(select count(distinct ts1.cd_sistema||ts1.bid) 
from tr_tit_cla ts1 
	where ts1.fl_canc<>''S'' 
    and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
  )) as perc_su_tot_tit_class_polo   
,percento(count(distinct ts.cd_sistema||ts.bid),(select count(tt.bid) 
from tb_titolo tt where tt.fl_canc<>''S'' and  
tt.cd_livello <>''01'' and tt.cd_livello <>''04''
and CAST(tt.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_su_tot_tit_polo  
from tr_tit_cla ts
where ts.fl_canc <> ''S''  
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
group by 1 order by 1) 
UNION
(select chr(160)||''Polo''  as sistema 
,count(distinct cd_sistema||bid) as tot_tit_class
,percento(count(distinct cd_sistema||bid),(select count(distinct cd_sistema||bid) from tr_tit_cla ts1 
	where ts1.fl_canc<>''S'' 
    and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
 )) as perc_su_tot_tit_class_polo 
 ,percento(count(distinct ts.cd_sistema||ts.bid),(select count(tt.bid) 
from tb_titolo tt where tt.fl_canc<>''S'' and  
tt.cd_livello <>''01'' and tt.cd_livello <>''04''
and CAST(tt.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_su_tot_tit_polo  
from tr_tit_cla ts where ts.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)
', 'sistema_di_classif|titoli_classificati|%_su_tot_tit_classif_polo|%_su_tot_titoli_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(97, '00_GestSem8', '00_GestSem', 8, 'IC002', NULL, NULL, 'Totale_legami_tra_termini_di_thesauro_in_un_dato_periodo_suddivisi_per_tipo_legame', '1', 'select
case ts.tipo_coll isnull when true then chr(160)||''tipo legame non indicato'' else
        case trim(ts.tipo_coll)='''' when true then chr(160)||''tipo legame non indicato'' else
        ts.tipo_coll ||'' - '' ||lower(cod.ds_tabella) end  end as tipo_coll
,count(*) as totale_legami
,percento(count(*),(select count(*) from tr_termini_termini ts1 
	where ts1.fl_canc<>''S'' 
    and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
  )) as perc_su_tot_polo   
from tr_termini_termini ts 
	left outer join tb_codici cod on ts.tipo_coll = cod.cd_tabella and tp_tabella = ''STLT''
where ts.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
group by 1  order by 1) 
UNION
(select chr(160)||''Polo''  as Biblioteca 
,count(*) as totale_legami
,100   
from tr_termini_termini ts where ts.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)
', 'tipo_legame|totale_legami|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(98, '00_GestSem9', '00_GestSem', 9, 'IC002', NULL, NULL, 'Totale_legami_tra_descrittori_in_un_dato_periodo_suddivisi_per_tipo_legame', '1', 'select
case ts.tp_legame isnull when true then chr(160)||''tipo legame non indicato'' else
        case trim(ts.tp_legame)='''' when true then chr(160)||''tipo legame non indicato'' else
        ts.tp_legame ||'' - '' ||lower(cod.ds_tabella) end  end as tipo_legame
,count(*) as totale_legami
,percento(count(*),(select count(*) from tr_des_des ts1 
	where ts1.fl_canc<>''S'' 
    and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
  )) as perc_su_tot_polo   
from tr_des_des ts 
	left outer join tb_codici cod on ts.tp_legame = cod.cd_tabella and tp_tabella = ''LEDD''
where ts.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
group by 1  order by 1) 
UNION
(select chr(160)||''Polo''  as Biblioteca 
,count(*) as totale_legami
,100  
from tr_des_des ts where ts.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)
', 'tipo_legame|totale_legami|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(94, '00_GestSem5', '00_GestSem', 5, 'IC002', NULL, NULL, 'Totale_titoli_soggettati_in_un_dato_periodo_suddivisi_per_soggettario', '1', '(select CAST(so.cd_soggettario as text) ||
 CAST(case so.cd_edizione isnull when true then '''' else '' - ''||so.cd_edizione end as text) as sogg_edizione 
,count(distinct cd_sogg||bid) as tot_tit_sogg
,percento(count(distinct cd_sogg||bid),(select count(distinct cd_sogg||bid) from tr_tit_sog_bib ts1 
	where ts1.fl_canc<>''S'' 
and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)) as perc_su_tot_tit_sogg_polo 
,percento(count(distinct ts.cd_sogg||ts.bid),(select count(tt.bid) 
from tb_titolo tt where tt.fl_canc<>''S'' and  
tt.cd_livello <>''01'' and tt.cd_livello <>''04''
and CAST(tt.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_su_tot_tit_polo  
from tr_tit_sog_bib ts 
inner join tb_soggetto so on so.cid=ts.cid and so.cd_soggettario=ts.cd_sogg
where ts.fl_canc<>''S'' and so.fl_canc<>''S''
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
group by 1 order by 1) 
UNION
(select chr(160)||''Polo''  as Soggettario 
,count(distinct cd_sogg||bid) as tot_tit_sogg
,percento(count(distinct cd_sogg||bid),(select count(distinct cd_sogg||bid) from tr_tit_sog_bib ts1 
	where ts1.fl_canc<>''S'' 
    and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
 )) as perc_su_tot_tit_sogg_polo 
,percento(count(distinct ts.cd_sogg||ts.bid),(select count(tt.bid) 
from tb_titolo tt where tt.fl_canc<>''S'' and  
tt.cd_livello <>''01'' and tt.cd_livello <>''04''
and CAST(tt.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_su_tot_tit_polo  
from tr_tit_sog_bib ts 
inner join tb_soggetto so on so.cid=ts.cid 
where ts.fl_canc<>''S'' and so.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)', 'soggettario|titoli_soggettati|%_su_tot_tit_sogg_polo|%_su_tot_titoli_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(99, '00_GSer9', '00_GSer', 1, 'L0000', NULL, NULL, 'Movimenti_archiviati_erogati_suddivisi_per_biblioteca_famiglia_e_svolgimento', '1', '(select CAST(sto.cd_bib as text) as biblioteca
,case co.cd_flg3 isnull when true then chr(160)||''famiglia non impostata'' 
	else case trim(co.cd_flg3)='''' when true then chr(160)||''famiglia non impostata''
  	else co.cd_flg3 end  end as famiglia 
,sum(case when sto.flag_svolg = ''L'' THEN 1 else 0 END) as locale
,percento (sum(case when sto.flag_svolg = ''L'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when sto.flag_svolg = ''I'' THEN 1 else 0 END) as ILL
,percento (sum(case when sto.flag_svolg = ''I'' THEN 1 else 0 END), count(*)) as perc_ill
,count(sto) as tot_famiglia
,percento(count(*),(select count(*) 
from tbl_storico_richieste_servizio sto1 where sto1.cd_bib=sto.cd_bib
and sto1.fl_canc<>''S''
and sto1.flag_pren = ''R'' 
and sto1.descr_stato_mov = ''chiuso''
and sto1.descr_stato_ric = ''conclusa'' 
and sto1.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_su_tot_bib 
from tbl_storico_richieste_servizio sto
	left outer join tb_codici co on SUBSTRING(sto.cod_tipo_serv,0,3) = co.cd_tabella 
    and co.tp_tabella = ''LTSE''
where sto.fl_canc<>''S'' 
and sto.flag_pren = ''R'' 
and sto.descr_stato_mov = ''chiuso''
and sto.descr_stato_ric = ''conclusa'' 
and ('''' in (:0lista) or sto.cd_bib in (:0lista))
and sto.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by sto.cd_bib,2
order by sto.cd_bib,2)
UNION
(select CAST(sto.cd_bib as text) as biblioteca
, chr(160)||chr(160)||''tutte le famiglie''
,sum(case when sto.flag_svolg = ''L'' THEN 1 else 0 END) as locale
,percento (sum(case when sto.flag_svolg = ''L'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when sto.flag_svolg = ''I'' THEN 1 else 0 END) as ILL
,percento (sum(case when sto.flag_svolg = ''I'' THEN 1 else 0 END), count(*)) as perc_ill
,count(sto) as tot_famiglia
,case 
(select count(*) 
from tbl_storico_richieste_servizio sto1 where sto1.cd_bib=sto.cd_bib
and sto1.fl_canc<>''S''
and sto1.flag_pren = ''R'' 
and sto1.descr_stato_mov = ''chiuso''
and sto1.descr_stato_ric = ''conclusa'' 
and sto1.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
) = 0 when true then 100
else 
percento(count(*),(select count(*) 
from tbl_storico_richieste_servizio sto1 where sto1.cd_bib=sto.cd_bib
and sto1.fl_canc<>''S''
and sto1.flag_pren = ''R'' 
and sto1.descr_stato_mov = ''chiuso''
and sto1.descr_stato_ric = ''conclusa'' 
and sto1.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) end as perc_su_tot_bib 
from tbl_storico_richieste_servizio sto
where sto.fl_canc<>''S''
and sto.flag_pren = ''R'' 
and sto.descr_stato_mov = ''chiuso''
and sto.descr_stato_ric = ''conclusa'' 
and ('''' in (:0lista) or sto.cd_bib in (:0lista))
and sto.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by sto.cd_bib)
', 'biblioteca|famiglia|locale|%_locale|ILL|%_ILL|totale_famiglia|%_su_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(102, '00_GSer12', '00_GSer', 4, 'L0000', NULL, NULL, 'Totale_utenti_suddivisi_per_provenienza', '1', '(select 
 case a.cod_proven isnull when true then chr(160)||''provenienza non indicata'' else
 case trim(a.cod_proven)='''' when true then chr(160)||''provenienza non indicata'' else
'' ''||CAST(a.cod_proven AS text)||'' ('' ||lower(cod.ds_tabella)||'')'' end  end as provenienza, 
 count(*),
percento(count(*),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
--and a1.persona_giuridica=''N'' 
)) as perc_fascia_su_tot_bib
from tbl_utenti a
	left outer join tb_codici cod on a.cod_proven = cod.cd_tabella and tp_tabella = ''RPRU''
where
a.id_utenti in (select b.id_utenti from trl_utenti_biblioteca b 
where 
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and b.fl_canc<>''S'') and a.fl_canc<>''S''
--and a.persona_giuridica=''N''
group by 1 order by 1)
UNION
(Select chr(160)||chr(160)||''Totale della biblioteca'', 
 count(*),
 case (select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
--and a1.persona_giuridica=''N'' 
) = 0 when true then 100
else 
percento(count(*),(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
--and a1.persona_giuridica=''N'' 
)) end as perc_fascia_su_tot_bib
from tbl_utenti a
where
a.id_utenti in (select b.id_utenti from trl_utenti_biblioteca b 
where 
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and b.fl_canc<>''S'') and a.fl_canc<>''S''
--and a.persona_giuridica=''N''
)
order by 1;


', 'provenienza|totale|%_su_totale_bib', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(104, '00_GSer14', '00_GSer', 6, 'L0000', NULL, NULL, 'Totale_utenti_suddivisi_per_titolo_di_studio', '1', '(select  
	case a.tit_studio isnull when true then chr(160)||''Titolo di studio non indicato'' else
	case trim(a.tit_studio)='''' when true then chr(160)||''Titolo di studio non indicato'' else
    case (select count(*) from tb_codici cod 
    where a.tit_studio = cod.cd_tabella and tp_tabella = ''RTST'') = 0 when true   then 
    chr(160)||''Codice titolo di studio non previsto''
    else
    CAST(a.tit_studio as text)||'' ('' || 
    lower((select ds_tabella from tb_codici where tp_tabella=''RTST'' and cd_tabella=a.tit_studio)) ||'')'' end end  end, 
count(*), 
percento(count(*), (select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) as perc_titolo_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where
a.id_utenti = b.id_utenti  and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S'' 
group by 1
order by 1)
UNION
(Select  
chr(160)||chr(160)||''Totale della biblioteca'',
count(*), 
case (select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S''
) = 0 when true then 100
else 
percento(count(*),(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S''
)) end as perc_titolo_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where
a.id_utenti = b.id_utenti  and
('''' in (:0lista) or b.cd_biblioteca in (:0lista)) 
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S'' 
)
', 'titolo_di_studio|totale|%_su_totale_bib', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(107, '00_GSer17', '00_GSer', 9, 'L0000', NULL, NULL, 'Totale_utenti_suddivisi_per_occupazione', '1', '(select 
         case when id_occupazioni isnull then chr(160)||''Occupazione non indicata'' 
         when trim(CAST(id_occupazioni as text))='''' then chr(160)||''Occupazione non indicata'' 
         when (select id_occupazioni from tbl_occupazioni oo
    	 where oo.id_occupazioni=b.id_occupazioni and fl_canc<>''S'') isnull 
          then CAST(id_occupazioni as text)|| '' ('' || lower((select descr from tbl_occupazioni 
    	where id_occupazioni=b.id_occupazioni)) || '') - occ. soppressa''
    else 
         CAST(id_occupazioni as text)|| '' ('' ||
	lower((select descr from tbl_occupazioni  where id_occupazioni=b.id_occupazioni AND fl_canc<>''S''))  || '')'' 
     end, 
count(distinct a.id_utenti), 
percento(count(distinct a.id_utenti), (select count(distinct a1.id_utenti) from tbl_utenti a1, trl_utenti_biblioteca b1 
where
a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) as perc_spec_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where
a.id_utenti = b.id_utenti  and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S'' 
group by 1
order by 1)
UNION
(Select  
chr(160)||chr(160)||''Totale della biblioteca'',
count(distinct a.id_utenti), 
case (select count(distinct a1.id_utenti) from tbl_utenti a1, trl_utenti_biblioteca b1 
where
a1.id_utenti = b1.id_utenti and
 ('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
) = 0 when true then 100
else 
percento(count(distinct a.id_utenti), (select count(distinct a1.id_utenti) from tbl_utenti a1, trl_utenti_biblioteca b1 
where
a1.id_utenti = b1.id_utenti and
 ('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) end as perc_spec_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where
a.id_utenti = b.id_utenti  and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S'' 
)
', 'occupazione|totale|%_su_totale_bib', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(108, '00_GSer18', '00_GSer', 10, 'L0000', NULL, NULL, 'Distribuzione_delle_preferenze_per_materie_di_interesse', '1', '(select 
	case when c.id_materia isnull then chr(160)||''Materia non indicata'' 
	     when trim(CAST(c.id_materia as text))='''' then chr(160)||''Materia non indicata'' 
         when (select id_materia from tbl_materie 
    	 where id_materia=c.id_materia and fl_canc<>''S'') isnull 
            then (select cod_mat || '' ('' || descr from tbl_materie 
    			where id_materia=c.id_materia) 
            	|| '') - mat. soppressa''
    else 
       (select cod_mat || '' ('' || descr from tbl_materie 
    		where id_materia=c.id_materia AND fl_canc<>''S'') 
            || '')'' 
     end, 
count(distinct CAST(a.id_utenti as text)||CAST(c.id_materia as text)), 
percento(count(distinct CAST(a.id_utenti as text)||CAST(c.id_materia as text)), 
(select count(distinct CAST(a1.id_utenti as text)||CAST(c1.id_materia as text)) 
from tbl_utenti a1, trl_utenti_biblioteca b1, trl_materie_utenti c1, tbl_materie m1  
where
a1.id_utenti = b1.id_utenti and a1.id_utenti = c1.id_utenti and
m1.id_materia = c1.id_materia and m1.cd_biblioteca=b1.cd_biblioteca and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' and c1.fl_canc<>''S'' 
)) as perc_spec_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b, trl_materie_utenti c, tbl_materie m 
where
a.id_utenti = b.id_utenti  and a.id_utenti = c.id_utenti and 
m.id_materia = c.id_materia and m.cd_biblioteca=b.cd_biblioteca and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S'' and c.fl_canc<>''S''
group by 1
order by 1)
UNION
(select 
	case when tab.fl_canc=''S'' then 
    	cod_mat || '' ('' || descr || '') - mat. senza pref.''
    else 
        cod_mat || '' ('' || descr || '')'' 
     end ,
     0,0
from tbl_materie as tab left outer join trl_materie_utenti ib 
on tab.id_materia = ib.id_materia where 
ib.id_materia isnull and tab.id_materia notnull and 
('''' in (:0lista) or tab.cd_biblioteca in (:0lista))
)
UNION
(Select  
chr(160)||chr(160)||''Totale pref della biblioteca'',
count(distinct CAST(a.id_utenti as text)||CAST(c.id_materia as text)), 
100 
from tbl_utenti a, trl_utenti_biblioteca b, trl_materie_utenti c, tbl_materie m 
where
a.id_utenti = b.id_utenti  and a.id_utenti = c.id_utenti and 
m.id_materia = c.id_materia and m.cd_biblioteca=b.cd_biblioteca and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S'' and c.fl_canc<>''S'' 
)
order by 1', 'materia|totale_preferenze|%_su_totale_pref', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(109, '00_GSer19', '00_GSer', 11, 'L0000', NULL, NULL, 'Composizione_utenza_del_polo', '0', '(select chr(160)||'''' || CAST (utb.cd_biblioteca AS text),
sum(case when ute.persona_giuridica = ''N'' THEN 1 else 0 END) as persone,
percento(sum(case when ute.persona_giuridica = ''N'' THEN 1 else 0 END),
(select count(distinct utb1.id_utenti) from tbl_utenti ute1,trl_utenti_biblioteca utb1 
 where ute1.id_utenti=utb1.id_utenti and ute1.fl_canc<>''S'' and utb1.fl_canc<>''S'' 
)) as perc_pers_su_num_utenti_polo,
sum(case when ute.persona_giuridica = ''S'' THEN 1 else 0 END) as enti,
percento(sum(case when ute.persona_giuridica = ''S'' THEN 1 else 0 END),
(select count(distinct utb1.id_utenti) from tbl_utenti ute1,trl_utenti_biblioteca utb1 
 where ute1.id_utenti=utb1.id_utenti and ute1.fl_canc<>''S'' and utb1.fl_canc<>''S'' 
)) as perc_enti_su_num_utenti_polo,
count(distinct utb.id_utenti),
percento(count(distinct utb.id_utenti),
(select count(distinct utb1.id_utenti) from tbl_utenti ute1,trl_utenti_biblioteca utb1 
 where ute1.id_utenti=utb1.id_utenti and ute1.fl_canc<>''S'' and utb1.fl_canc<>''S'' 
)) as perc_su_num_utenti_polo
from tbl_utenti ute, trl_utenti_biblioteca utb where 
ute.fl_canc<>''S'' and utb.fl_canc<>''S'' 
and ute.id_utenti = utb.id_utenti 
group by 1 order by 1)
UNION
(Select chr(160)||chr(160)||''Numero di utenti del polo'', 
sum(case when ute.persona_giuridica = ''N'' THEN 1 else 0 END) as persone,
percento(sum(case when ute.persona_giuridica = ''N'' THEN 1 else 0 END),
(select count(distinct utb1.id_utenti) from tbl_utenti ute1,trl_utenti_biblioteca utb1 
 where ute1.id_utenti=utb1.id_utenti and ute1.fl_canc<>''S'' and utb1.fl_canc<>''S'' 
)) as perc_pers_su_num_utenti_polo,
sum(case when ute.persona_giuridica = ''S'' THEN 1 else 0 END) as enti,
percento(sum(case when ute.persona_giuridica = ''S'' THEN 1 else 0 END),
(select count(distinct utb1.id_utenti) from tbl_utenti ute1,trl_utenti_biblioteca utb1 
 where ute1.id_utenti=utb1.id_utenti and ute1.fl_canc<>''S'' and utb1.fl_canc<>''S'' 
)) as perc_enti_su_num_utenti_polo,
count(distinct ute.id_utenti),
case (select count(distinct utb1.id_utenti) from tbl_utenti ute1,trl_utenti_biblioteca utb1 
 where ute1.id_utenti=utb1.id_utenti and ute1.fl_canc<>''S'' and utb1.fl_canc<>''S'' 
) = 0 when true then 100
else 
percento(count(distinct ute.id_utenti),
(select count(distinct utb1.id_utenti) from tbl_utenti ute1,trl_utenti_biblioteca utb1 
 where ute1.id_utenti=utb1.id_utenti and ute1.fl_canc<>''S'' and utb1.fl_canc<>''S'' 
)) end as perc_su_num_utenti_polo
from tbl_utenti ute
where ute.fl_canc<>''S'' and 
ute.id_utenti in (select utb.id_utenti from trl_utenti_biblioteca utb 
where utb.fl_canc<>''S'')
) order by 1', 'biblioteca|persone|%_persone_su_utenti_di_polo|enti|%_enti_su_utenti_di_polo|totale_bib|%_su_utenti_di_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(111, '00_GSer21', '00_GSer', 13, 'L0000', NULL, NULL, 'Movimenti_archiviati_erogati_in_un_dato_anno_e_famiglia_servizio_suddivisi_per_categoria_di_autorizzazione_degli_utenti', '1', '(select
case ute.cod_tipo_aut isnull when true then chr(160)||''cat. non impostata'' else
case trim(ute.cod_tipo_aut)='''' when true then chr(160)||''cat. non impostata'' else
case (select count(*) from tbl_tipi_autorizzazioni ta where ta.cod_tipo_aut=ute.cod_tipo_aut 
	and ta.cd_bib=ute.cd_biblioteca and ta.fl_canc<>''S'')
      = 0 when true then chr(160)||''cat. non prevista''
else 
    '' ''|| CAST(ute.cod_tipo_aut as text)|| '' ('' ||
	lower((select descr from tbl_tipi_autorizzazioni ta where 
    ta.cod_tipo_aut=ute.cod_tipo_aut and ta.cd_bib=ute.cd_biblioteca and ta.fl_canc<>''S''))  || '')'' 
end  end end as cod_tipo_aut        
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end) as Gen
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end), count(*)) as perc_gen
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end) as Feb
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end), count(*)) as perc_feb
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end) as Mar
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end), count(*)) as perc_mar
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end) as Apr
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end), count(*)) as perc_apr
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end) as Mag
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end), count(*)) as perc_mag
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end) as Giu
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end), count(*)) as perc_giu
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end) as Lug
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end), count(*)) as perc_lug
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end) as Ago
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end), count(*)) as perc_ago
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end) as Sett
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end), count(*)) as perc_set
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end) as Ott
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end), count(*)) as perc_ott
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end) as Nov
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end), count(*)) as perc_nov
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end) as Dic
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end), count(*)) as perc_dic
,count(*) as tot_categoria
,percento(count(*),(select count(*) 
from tbl_storico_richieste_servizio sto1 
 left outer join trl_utenti_biblioteca ute1   
 on ute1.id_utenti = sto1.cod_utente and ute1.cd_biblioteca = sto1.cod_bib_ut
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff  )) as perc_su_tot_anno 
from tbl_storico_richieste_servizio sto 
 left outer join trl_utenti_biblioteca ute   
 on ute.id_utenti = sto.cod_utente and ute.cd_biblioteca = sto.cod_bib_ut
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto.fl_canc<>''S'' and ute.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto.data_in_eff) = :1annoInEff
group by 1 order by 1)
UNION
(Select chr(160)||chr(160)||''TOTALE''
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end) as Gen
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end), count(*)) as perc_gen
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end) as Feb
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end), count(*)) as perc_feb
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end) as Mar
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end), count(*)) as perc_mar
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end) as Apr
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end), count(*)) as perc_apr
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end) as Mag
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end), count(*)) as perc_mag
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end) as Giu
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end), count(*)) as perc_giu
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end) as Lug
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end), count(*)) as perc_lug
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end) as Ago
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end), count(*)) as perc_ago
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end) as Sett
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end), count(*)) as perc_set
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end) as Ott
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end), count(*)) as perc_ott
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end) as Nov
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end), count(*)) as perc_nov
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end) as Dic
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end), count(*)) as perc_dic
,count(*) as tot_categoria
,case (select count(*) 
from tbl_storico_richieste_servizio sto1 
 left outer join trl_utenti_biblioteca ute1   
 on ute1.id_utenti = sto1.cod_utente and ute1.cd_biblioteca=sto1.cod_bib_ut
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer 
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S''  
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff  ) = 0 when true then 100
else 
percento(count(*),
(select count(*) 
from tbl_storico_richieste_servizio sto1 
 left outer join trl_utenti_biblioteca ute1   
 on ute1.id_utenti = sto1.cod_utente and ute1.cd_biblioteca=sto1.cod_bib_ut
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S''  
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff  )) 
end as perc_su_tot_anno 
from tbl_storico_richieste_servizio sto 
 left outer join trl_utenti_biblioteca ute   
 on ute.id_utenti = sto.cod_utente and ute.cd_biblioteca = sto.cod_bib_ut
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer 
and sto.fl_canc<>''S'' and ute.fl_canc<>''S''  
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto.data_in_eff) = :1annoInEff
)', 'categoria|gen|%_gen|feb|%_feb|mar|%_mar|apr|%_apr|mag|%_mag|giu|%_giu|lug|%_lug|ago|%_ago|sett|%_set|ott|%_ott|nov|%_nov|dic|%_dic|tot_categoria|%_su_tot_anno', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(120, '00_CatPolo16', '00_CatPolo', 20, 'IC000', NULL, NULL, 'Lista_autori_in_forma_accettata', '1', 'select  CAST(au.VID as text), au.ds_nome_aut, CAST(au.tp_nome_aut as text) 
from tb_autore au 
where au.fl_canc <> ''S'' and au.tp_forma_aut=''A''
and (:1flcon='''' or au.fl_condiviso=:1flcon) 
and CAST(au.ts_ins as date) between COALESCE(:2dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
                  and COALESCE(:3dataA, to_timestamp(''99990101'',''YYYYMMDD''))   
and au.ky_cles1_a between COALESCE(upper(:4lettD), '''')  
and case when :5lettA isnull then ''ZZZZ'' 
		 when :5lettA='''' then ''ZZZZ'' 
		 else upper(:5lettA||''ZZZZ'') end     
and au.cd_livello between COALESCE(:6liv_autD, '''') 
and case when :7liv_autA isnull then ''97'' 
		 when :7liv_autA='''' then ''97'' 
         else :7liv_autA end   
and (:8tp_nome isnull or :8tp_nome ='''' 
or case when :8tp_nome=''P'' then au.tp_nome_aut in (''A'',''B'',''C'',''D'') 
		when :8tp_nome=''F'' then au.tp_nome_aut in (''E'',''R'',''G'') 
     	else au.tp_nome_aut =:8tp_nome end)
order by au.ky_cles1_a;
', 'VID|Nome|Tipo_Nome', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(114, '00_GSer24', '00_GSer', 16, 'L0000', NULL, NULL, 'Movimenti_archiviati_locali_e_ill_suddivisi_per_stato_richiesta', '1', '(select 
case sto.descr_stato_ric isnull when true then chr(160)||''stato richiesta non impostato'' 
	else case trim(sto.descr_stato_ric)='''' when true then chr(160)||''stato richiesta non impostato''
  	else case (sto.descr_stato_ric = ''respinta'' and 
    	CAST(sto.ts_var as date) <= sto.data_in_prev)  
    	when true then ''respinta negativa''
  	else case (sto.descr_stato_ric = ''respinta'' and 
    	CAST(sto.ts_var as date) > sto.data_in_prev)
    	when true then ''giacenza''
else sto.descr_stato_ric end  end end end as stato_richiesta
,sum(case when sto.flag_svolg = ''L'' THEN 1 else 0 END) as locale
,case sum(case when sto.flag_svolg = ''L'' THEN 1 else 0 END) = 0
 	when true then 0
else 
	percento (sum(case when sto.flag_svolg = ''L'' THEN 1 else 0 END), count(*)) 
end as perc_locale
,sum(case when sto.flag_svolg = ''I'' THEN 1 else 0 END) as ill
,case sum(case when sto.flag_svolg = ''I'' THEN 1 else 0 END) = 0
 	when true then 0
else 
	percento (sum(case when sto.flag_svolg = ''I'' THEN 1 else 0 END), count(*)) 
end as perc_ill
,count(*),
percento(count(*),
(select count(*) 
from tbl_storico_richieste_servizio sto2 where 
sto2.fl_canc<>''S''
and sto2.flag_pren = ''R'' 
and sto2.descr_stato_mov = ''chiuso''
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto2.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer 
and ('''' in (:0lista) or sto2.cod_bib_ut in (:0lista)) 
and sto2.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_su_tot_bib
from tbl_storico_richieste_servizio sto where 
sto.fl_canc<>''S''
and sto.flag_pren = ''R'' 
and sto.descr_stato_mov = ''chiuso''
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer 
and ('''' in (:0lista) or sto.cod_bib_ut in (:0lista))
and sto.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
 group by 1)
 UNION
(select 
chr(160)||''Totale''
,sum(case when sto.flag_svolg = ''L'' THEN 1 else 0 END) as locale
,case sum(case when sto.flag_svolg = ''L'' THEN 1 else 0 END) = 0
 	when true then 0
else 
	percento (sum(case when sto.flag_svolg = ''L'' THEN 1 else 0 END), count(*)) 
end as perc_locale
,sum(case when sto.flag_svolg = ''I'' THEN 1 else 0 END) as ill
,case sum(case when sto.flag_svolg = ''I'' THEN 1 else 0 END) = 0
 	when true then 0
else 
	percento (sum(case when sto.flag_svolg = ''I'' THEN 1 else 0 END), count(*)) 
end as perc_ill
,count(*),
percento(count(*),
(select count(*) 
from tbl_storico_richieste_servizio sto2 where 
sto2.fl_canc<>''S''
and sto2.flag_pren = ''R'' 
and sto2.descr_stato_mov = ''chiuso''
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto2.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer  
and ('''' in (:0lista) or sto2.cod_bib_ut in (:0lista)) 
and sto2.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_su_tot_bib
from tbl_storico_richieste_servizio sto where 
sto.fl_canc<>''S''
and sto.flag_pren = ''R'' 
and sto.descr_stato_mov = ''chiuso''
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer 
and ('''' in (:0lista) or sto.cod_bib_ut in (:0lista))
and sto.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
) order by 1
', 'stato_richiesta|locale|%_locale|ILL|%_ILL|totale_stato|%_su_totale_bib', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(115, '00_GSer25', '00_GSer', 17, 'L0000', NULL, NULL, 'Riproduzioni_effettuate_divise_per_supporto_(movimenti_archiviati)', '1', '(select 
case when sto.descr_supporto isnull then chr(160)||''Supporto non indicato'' 
     when trim(sto.descr_supporto)='''' then chr(160)||''Supporto non indicato'' 
     else sto.descr_supporto end as supporto     	
,count(*) as tot_rich_per_supporto
,sum(sto.num_pezzi) as num_pezzi
from tbl_storico_richieste_servizio sto
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista))
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''RP''
and sto.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa'' 
and sto.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
group by 1 order by 1)
UNION
(select 
chr(160)||''Totale richieste evase''     	
,count(*),null
from tbl_storico_richieste_servizio sto
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista))
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''RP''
and sto.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa''
and sto.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)
', 'supporto|richieste|pezzi', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(124, '00_CatPolo17', '00_CatPolo', 15, 'IC000', NULL, NULL, 'Periodici_suddivisi_per_presenza_o_assenza_di_fascicoli', '0', '(select ''Periodici con fascicoli'', count(distinct _ti.bid) as cnt
,percento(count(distinct _ti.bid),(select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' and _ti1.cd_natura = ''S'')) as percentuale
from tb_titolo _ti 
where exists (select _fa.fid from tbp_fascicolo _fa where _ti.bid=_fa.bid and _fa.fl_canc <>''S'' ) 
and _ti.fl_canc <>''S'' 
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'' ) 
UNION
(select ''Periodici senza fascicoli'', count(distinct _ti.bid) as cnt
,percento(count(distinct _ti.bid),(select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' and _ti1.cd_natura = ''S'')) as percentuale
from tb_titolo _ti 
where not exists (select _fa.fid from tbp_fascicolo _fa where _ti.bid=_fa.bid and _fa.fl_canc <>''S'' ) 
and _ti.fl_canc <>''S'' 
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'') 
UNION
(select chr(160)||''Totale'' 
,count(*) as cnt, 100  
from tb_titolo _ti 
where _ti.fl_canc <>''S'' 
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'')', 'tipologia|totale|%', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(126, '00_CatBib15', '00_CatBib', 15, 'IC000', NULL, NULL, 'Periodici_della_biblioteca_suddivisi_per_presenza_o_assenza_di_fascicoli', '1', '(select ''Periodici con fascicoli'', count(distinct _ti.bid) as cnt
,percento(count(distinct _ti.bid),(select count(*)  
from tb_titolo _ti1, tr_tit_bib _tb1 
where _ti1.bid = _tb1.bid 
and ('''' in (:0lista) or _tb1.cd_biblioteca in (:0lista))
and _ti1.fl_canc <>''S'' and _tb1.fl_canc<>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' and _ti1.cd_natura = ''S'')) as percentuale
,percento(count(distinct _ti.bid),(select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' and _ti1.cd_natura = ''S'')) as percentuale_polo
from tb_titolo _ti, tr_tit_bib _tb 
where _ti.bid =_tb.bid 
and ('''' in (:0lista) or _tb.cd_biblioteca in (:0lista))
and _ti.fl_canc <>''S'' and _tb.fl_canc<>''S''
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'' 
and exists (select _fa.fid from tbp_fascicolo _fa where _ti.bid=_fa.bid and _fa.fl_canc <>''S'' )) 
UNION
(select ''Periodici senza fascicoli'', count(distinct _ti.bid) as cnt
,percento(count(distinct _ti.bid),(select count(*)  
from tb_titolo _ti1, tr_tit_bib _tb1 
where _ti1.bid = _tb1.bid 
and ('''' in (:0lista) or _tb1.cd_biblioteca in (:0lista))
and _ti1.fl_canc <>''S'' and _tb1.fl_canc<>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' and _ti1.cd_natura = ''S'')) as percentuale
,percento(count(distinct _ti.bid),(select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' and _ti1.cd_natura = ''S'')) as percentuale_polo
from tb_titolo _ti, tr_tit_bib _tb 
where _ti.bid =_tb.bid 
and ('''' in (:0lista) or _tb.cd_biblioteca in (:0lista))
and _ti.fl_canc <>''S'' and _tb.fl_canc<>''S''
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'' 
and not exists (select _fa.fid from tbp_fascicolo _fa where _ti.bid=_fa.bid and _fa.fl_canc <>''S''))  
UNION
(select chr(160)||''Totale biblioteca'' 
,count(*) as cnt, 100
,percento(count(distinct _ti.bid),(select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' and _ti1.cd_natura = ''S'')) as percentuale_polo  
from tb_titolo _ti, tr_tit_bib _tb 
where _ti.bid =_tb.bid 
and ('''' in (:0lista) or _tb.cd_biblioteca in (:0lista))
and _ti.fl_canc <>''S'' and _tb.fl_canc<>''S''
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'')
UNION
(select chr(160)||''Totale polo'' 
,count(*) as cnt, null, null  
from tb_titolo _ti 
where _ti.fl_canc <>''S'' 
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'')


', 'tipologia|totale|%_su_totale_bib|%_su_totale_polo', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(127, '00_GPeriodici1', '00_GPeriodici', 1, 'P0000', NULL, NULL, 'Periodici_suddivisi_per_presenza_o_assenza_di_esemplari_di_fascicolo', '1', '(select ''Periodici con esemplari di fascicolo'', 
count(distinct _ti.bid) as cnt
,percento(count(distinct _ti.bid),(select count(*)  
from tb_titolo _ti1, tr_tit_bib _tb1 
where _ti1.bid = _tb1.bid 
and ('''' in (:0lista) or _tb1.cd_biblioteca in (:0lista))
and _ti1.fl_canc <>''S'' and _tb1.fl_canc<>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' and _ti1.cd_natura = ''S'')) as perc_tot_bib
from tb_titolo _ti, tr_tit_bib _tb 
where _ti.bid = _tb.bid 
and ('''' in (:0lista) or _tb.cd_biblioteca in (:0lista))
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' and _ti.cd_natura = ''S'' 
and exists 
(select _fa.fid from tbp_esemplare_fascicolo _fa 
where _ti.bid=_fa.bid and _fa.fl_canc <>''S'' 
and (_fa.cd_bib_inv = :0lista or _fa.cd_bib_abb = :0lista)
 )) 
UNION
(select ''Periodici senza esemplari di fascicolo'', 
count(distinct _ti.bid) as cnt
,percento(count(distinct _ti.bid),(select count(distinct _ti1.bid)  
from tb_titolo _ti1, tr_tit_bib _tb1 
where _ti1.bid = _tb1.bid 
and ('''' in (:0lista) or _tb1.cd_biblioteca in (:0lista))
and _ti1.fl_canc <>''S'' and _tb1.fl_canc<>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' and _ti1.cd_natura = ''S'')) as perc_tot_bib
from tb_titolo _ti, tr_tit_bib _tb 
where _ti.bid = _tb.bid 
and ('''' in (:0lista) or _tb.cd_biblioteca in (:0lista))
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' and _ti.cd_natura = ''S'' 
and not exists 
(select _fa.fid from tbp_esemplare_fascicolo _fa 
where _ti.bid=_fa.bid and _fa.fl_canc <>''S'' 
and (_fa.cd_bib_inv = :0lista or _fa.cd_bib_abb = :0lista)
))
UNION
(select chr(160)||''Totale biblioteca'' 
,count(*) as cnt, 100
from tb_titolo _ti, tr_tit_bib _tb 
where _ti.bid =_tb.bid 
and ('''' in (:0lista) or _tb.cd_biblioteca in (:0lista))
and _ti.fl_canc <>''S'' and _tb.fl_canc<>''S''
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'')', 'tipologia|totale|%', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(128, '00_CatPolo18', '00_CatPolo', 16, 'IC000', NULL, NULL, 'Periodici_suddivisi_tra_correnti_e_cessati', '0', '(select ''Periodici correnti'', count(_ti.bid) as cnt
,percento(count(_ti.bid),(select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S'' and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' 
and _ti1.cd_natura = ''S'')) as percentuale
from tb_titolo _ti 
where _ti.fl_canc <>''S'' and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'' and _ti.tp_aa_pubb <> ''B'') 
UNION
(select ''Periodici cessati'', count(_ti.bid) as cnt
,percento(count(_ti.bid),(select count(*)  
from tb_titolo _ti1
where _ti1.fl_canc <>''S'' and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' 
and _ti1.cd_natura = ''S'')) as percentuale
from tb_titolo _ti 
where _ti.fl_canc <>''S'' and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'' and _ti.tp_aa_pubb = ''B'') 
UNION
(select chr(160)||''Totale'' 
,count(*) as cnt, 100  
from tb_titolo _ti 
where _ti.fl_canc <>''S'' and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'')', 'tipologia|totale|%', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(129, '00_CatBib16', '00_CatBib', 16, 'IC000', NULL, NULL, 'Periodici_della_biblioteca_suddivisi_tra_correnti_e_cessati', '1', '(select ''Periodici correnti'', count(distinct _ti.bid) as cnt
,percento(count(distinct _ti.bid),(select count(*)  
from tb_titolo _ti1, tr_tit_bib _tb1 
where _ti1.bid = _tb1.bid 
and ('''' in (:0lista) or _tb1.cd_biblioteca in (:0lista))
and _ti1.fl_canc <>''S'' and _tb1.fl_canc<>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' and _ti1.cd_natura = ''S'')) as percentuale
from tb_titolo _ti, tr_tit_bib _tb 
where _ti.bid =_tb.bid 
and ('''' in (:0lista) or _tb.cd_biblioteca in (:0lista))
and _ti.fl_canc <>''S'' and _tb.fl_canc<>''S''
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'' and _ti.tp_aa_pubb <>''B'') 
UNION
(select ''Periodici cessati'', count(distinct _ti.bid) as cnt
,percento(count(distinct _ti.bid),(select count(*)  
from tb_titolo _ti1, tr_tit_bib _tb1 
where _ti1.bid = _tb1.bid 
and ('''' in (:0lista) or _tb1.cd_biblioteca in (:0lista))
and _ti1.fl_canc <>''S'' and _tb1.fl_canc<>''S''
and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' and _ti1.cd_natura = ''S'')) as percentuale
from tb_titolo _ti, tr_tit_bib _tb 
where _ti.bid =_tb.bid 
and ('''' in (:0lista) or _tb.cd_biblioteca in (:0lista))
and _ti.fl_canc <>''S'' and _tb.fl_canc<>''S''
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'' and _ti.tp_aa_pubb = ''B'')
UNION
(select chr(160)||''Totale biblioteca'' 
,count(*) as cnt, 100
from tb_titolo _ti, tr_tit_bib _tb 
where _ti.bid =_tb.bid 
and ('''' in (:0lista) or _tb.cd_biblioteca in (:0lista))
and _ti.fl_canc <>''S'' and _tb.fl_canc<>''S''
and _ti.cd_livello <>''01'' and _ti.cd_livello <>''04'' 
and _ti.cd_natura = ''S'')

', 'tipologia|totale|%', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(131, '00_CatPolo20', '00_CatPolo', 17, 'IC000', NULL, NULL, 'Periodici_con_o_senza_legami_a_spogli', '1', '(select CAST(_bi.cd_biblioteca as text) as biblioteca 
,sum(case when (_tb.bid in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END) as leg_sp
,percento (sum(case when (_tb.bid in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END), count(*)) as perc_leg_sp_su_totbib 
,sum(case when (_tb.bid not in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END) as non_leg_sp 
,percento (sum(case when (_tb.bid not in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END), count(*)) as perc_non_leg_sp_su_totbib 
,count(distinct _tb.bid) as tot_per_con_leg
,percento(count(distinct _tb.bid),(select count(distinct _ti1.bid)  
from tb_titolo _ti1, tr_tit_bib _bi1
where _ti1.bid = _bi1.bid and _bi1.fl_canc<>''S''
and _ti1.fl_canc<>''S'' and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' 
and _ti1.cd_natura = ''S'')) as perc_su_tot_loc_polo
from tb_titolo _tb, tr_tit_bib _bi
where _tb.bid = _bi.bid and _bi.fl_canc<>''S''
and _tb.fl_canc<>''S'' and _tb.cd_livello <>''01'' and _tb.cd_livello <>''04'' 
and _tb.cd_natura = ''S'' 
and ('''' in (:0lista) or _bi.cd_biblioteca in (:0lista))
group by 1)
UNION
(select chr(160)||''Polo'' 
,sum(case when (_tb.bid in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END) as leg_sp 
,percento (sum(case when (_tb.bid in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END), count(*)) as perc_leg_sp_su_totbib 
,sum(case when (_tb.bid not in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END) as non_leg_sp 
,percento (sum(case when (_tb.bid not in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END), count(*)) as perc_non_leg_sp_su_totbib 
,count(distinct _tb.bid) as tot_per_con_leg
,percento(count(distinct _tb.bid),(select count(distinct _ti1.bid)  
from tb_titolo _ti1, tr_tit_bib _bi1
where _ti1.bid = _bi1.bid and _bi1.fl_canc<>''S''
and _ti1.fl_canc<>''S'' and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' 
and _ti1.cd_natura = ''S'')) as perc_per_su_tot_loc_polo 
from tb_titolo _tb where 
_tb.fl_canc<>''S'' and _tb.cd_livello <>''01'' and _tb.cd_livello <>''04'' 
and _tb.cd_natura = ''S'' 
and exists (select * from tr_tit_bib _bi
where _tb.bid = _bi.bid and _bi.fl_canc<>''S''))
', 'biblioteca|legati_spogli|%_legati_sp_su_totale_bib|non_legati_spogli|%_non_legati_sp_su_totale_bib|totale_periodici|%_su_totale_loc_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(132, '00_CatBib17', '00_CatBib', 17, 'IC000', NULL, NULL, 'Periodici_della_biblioteca_con_o_senza_legami_a_spogli', '1', '(select CAST(_bi.cd_biblioteca as text) as biblioteca 
,sum(case when (_tb.bid in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END) as leg_sp
,percento (sum(case when (_tb.bid in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END), count(*)) as perc_leg_sp_su_totbib 
,sum(case when (_tb.bid not in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END) as non_leg_sp 
,percento (sum(case when (_tb.bid not in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END), count(*)) as perc_non_leg_sp_su_totbib 
,count(_tb.bid) as tot_per_con_leg
,percento(count(_tb.bid),(select count(_ti1.bid)  
from tb_titolo _ti1, tr_tit_bib _bi1
where _ti1.bid = _bi1.bid and _bi1.fl_canc<>''S''
and _ti1.fl_canc<>''S'' and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' 
and _ti1.cd_natura = ''S'')) as perc_su_tot_loc_polo
from tb_titolo _tb, tr_tit_bib _bi
where _tb.bid = _bi.bid and _bi.fl_canc<>''S''
and _tb.fl_canc<>''S'' and _tb.cd_livello <>''01'' and _tb.cd_livello <>''04'' 
and _tb.cd_natura = ''S'' 
and ('''' in (:0lista) or _bi.cd_biblioteca in (:0lista))
group by 1)
UNION
(select chr(160)||''Polo'' 
,sum(case when (_tb.bid in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END) as leg_sp 
,percento (sum(case when (_tb.bid in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END), count(*)) as perc_leg_sp_su_totbib 
,sum(case when (_tb.bid not in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END) as non_leg_sp 
,percento (sum(case when (_tb.bid not in (select tt1.bid_coll from tr_tit_tit tt1 
where tt1.fl_canc<>''S'' and tt1.tp_legame=''01'' 
and tt1.cd_natura_base = ''N'')) THEN 1 else 0 END), count(*)) as perc_non_leg_sp_su_totbib 
,count(_tb.bid) as tot_per_con_leg
,percento(count(_tb.bid),(select count(_ti1.bid)  
from tb_titolo _ti1, tr_tit_bib _bi1
where _ti1.bid = _bi1.bid and _bi1.fl_canc<>''S''
and _ti1.fl_canc<>''S'' and _ti1.cd_livello <>''01'' and _ti1.cd_livello <>''04'' 
and _ti1.cd_natura = ''S'')) as perc_per_su_tot_loc_polo 
from tb_titolo _tb, tr_tit_bib _bi
where _tb.bid = _bi.bid and _bi.fl_canc<>''S''
and _tb.fl_canc<>''S'' and _tb.cd_livello <>''01'' and _tb.cd_livello <>''04'' 
and _tb.cd_natura = ''S'' )', 'biblioteca|legati_spogli|%_legati_sp_su_totale_bib|non_legati_spogli|%_non_legati_sp_su_totale_bib|totale_periodici|%_su_totale_loc_polo', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(136, '00_GestSem11', '00_GestSem', 11, 'IC002', NULL, NULL, 'Lista_termini_di_thesauro', '1', 'select CAST(ttt.did as text),CAST(ttt.cd_the as text),ttt.ds_termine_thesauro,ttt.nota_termine_thesauro,ttt.tp_forma_the,ttt.fl_condiviso,cast (ttc.posizione as text),
 CAST(case when substr(ttc.cd_sistema,1,1) = ''D'' THEN ''DEWEY'' else ttc.cd_sistema end as text),
 CAST(ttc.cd_edizione as text),replace(ttc.classe , ''.'', ''. '')
 from "sbnweb"."tb_termine_thesauro" ttt 
 left outer  join tr_the_cla ttc on ttt.did = ttc.did
 and ttt.cd_the = ttc.cd_the
 and ttt.fl_canc <> ''S''
 and ttc.fl_canc <> ''S''
 where ttt.fl_canc <> ''S''  
 and (ttt.cd_the=:1Thesaurus)
 and upper(ttt.ds_termine_thesauro) 
 BETWEEN COALESCE(upper(:2terDa), '''') 
 and case when :3terA  isnull then ''ZZZZ'' when :3terA=''''  then ''ZZZZ'' else upper(:3terA||''ZZZZ'') end
 ORDER BY 3,1,ttc.posizione;

', 'Identificativo|Thesauro|Termine|Nota|Forma|Condiviso|Posizione|Sistema|Edizione|Classe_collegata', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(143, '00_GMonitoraggio1', '00_GMonitoraggio', 1, 'T0100', NULL, NULL, 'Bid_catturati_o_creati_in_un_dato_periodo_suddivisi_per_tipo_materiale_e_gruppo_operatori', '1', '(select 
CAST(bbb.cd_biblioteca as text), CAST(bbb.ufficio_appart as text), 
tb.tp_materiale, 
case substring(tb.bid,1,3)=(select po.cd_polo from tbf_polo po) when true then ''Creati'' else ''Catturati'' END,
count(*) 
from tb_titolo tb
inner join tbf_utenti_professionali_web utp on utp.userid = substring(tb.ute_ins,7,char_length(tb.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where tb.fl_canc<>''S''
and not tb.tp_materiale isnull and  tb.tp_materiale >'' ''
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(tb.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
group by 1,2,3,4
order by 1,2,3,4)
UNION
(select 
CAST(bbb.cd_biblioteca as text), null, 
null, 
case substring(tb.bid,1,3)=(select po.cd_polo from tbf_polo po) when true then ''Totale creati'' else ''Totale catturati'' END,
count(*) 
from tb_titolo tb 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(tb.ute_ins,7,char_length(tb.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where tb.fl_canc<>''S''
and not tb.tp_materiale isnull and  tb.tp_materiale >'' ''
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(tb.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 1,4
order by 1,4)
UNION
(select 
chr(160)||''Polo'', null, 
null, 
case substring(tb.bid,1,3)=(select po.cd_polo from tbf_polo po) when true then ''Totale creati'' else ''Totale catturati'' END,
count(*) 
from tb_titolo tb 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(tb.ute_ins,7,char_length(tb.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where tb.fl_canc<>''S''
and CAST(tb.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 4
order by 4);', 'biblioteca|ufficio|tipo_materiale|tipologia|totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(144, '00_GMonitoraggio2', '00_GMonitoraggio', 2, 'T0100', NULL, NULL, 'Bid_catturati_o_creati_in_un_dato_periodo_suddivisi_per_natura_e_gruppo_operatori', '1', '(select 
CAST(bbb.cd_biblioteca as text), CAST(bbb.ufficio_appart as text), 
tb.cd_natura, 
case substring(tb.bid,1,3)=(select po.cd_polo from tbf_polo po) when true then ''Creati'' else ''Catturati'' END,
count(*) 
from tb_titolo tb
inner join tbf_utenti_professionali_web utp on utp.userid = substring(tb.ute_ins,7,char_length(tb.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where tb.fl_canc<>''S''
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(tb.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
group by 1,2,3,4
order by 1,2,3,4)
UNION
(select 
CAST(bbb.cd_biblioteca as text), null, 
null, 
case substring(tb.bid,1,3)=(select po.cd_polo from tbf_polo po) when true then ''Totale creati'' else ''Totale catturati'' END,
count(*) 
from tb_titolo tb 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(tb.ute_ins,7,char_length(tb.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where tb.fl_canc<>''S''
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(tb.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 1,4
order by 1,4)
UNION
(select 
chr(160)||''Polo'', null, 
null, 
case substring(tb.bid,1,3)=(select po.cd_polo from tbf_polo po) when true then ''Totale creati'' else ''Totale catturati'' END,
count(*) 
from tb_titolo tb 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(tb.ute_ins,7,char_length(tb.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where tb.fl_canc<>''S''
and CAST(tb.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 4
order by 4);', 'biblioteca|ufficio|natura|tipologia|totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(145, '00_GestSem1b', '00_GestSem', 1, 'IC000', NULL, NULL, 'Totale_soggetti_inseriti_o_variati_in_un_dato_periodo_suddivisi_per_soggettario', '1', '(select CAST(so.cd_soggettario as text) ||
 CAST(case so.cd_edizione isnull when true then '''' else '' - ''||so.cd_edizione end as text) as sogg_edizione 
,sum(case when (CAST(so.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) THEN 1 else 0 END) as inseriti
,percento(sum(case when (CAST(so.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) THEN 1 else 0 END), 
(select count(so1) from tb_soggetto so1 
where so1.fl_canc <> ''S'' 
and CAST(so1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')))
) as perc_inseriti_su_totale_inseriti
,sum(case when (CAST(so.ts_var as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) THEN 1 else 0 END) as variati 
,percento(sum(case when (CAST(so.ts_var as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) THEN 1 else 0 END), 
(select count(so1) from tb_soggetto so1 
where so1.fl_canc <> ''S'' 
and CAST(so1.ts_var as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')))
) as perc_variati_su_totale_variati
from tb_soggetto so 
where so.fl_canc <> ''S''
group by 1)
UNION
(select ''Totale'' 
,sum(case when (CAST(so.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) THEN 1 else 0 END) as inseriti
,percento(sum(case when (CAST(so.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) THEN 1 else 0 END), 
(select count(so1) from tb_soggetto so1 
where so1.fl_canc <> ''S'' 
and CAST(so1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')))
) as perc_inseriti
,sum(case when (CAST(so.ts_var as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) THEN 1 else 0 END) as variati 
,percento(sum(case when (CAST(so.ts_var as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))) THEN 1 else 0 END), 
(select count(so1) from tb_soggetto so1 
where so1.fl_canc <> ''S'' 
and CAST(so1.ts_var as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')))
) as perc_variati
from tb_soggetto so 
where so.fl_canc <> ''S''
)', 'soggettario - edizione|inseriti|%_su_totale_inseriti|variati|%_su_totale_variati', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(146, '00_GMonitoraggio3', '00_GMonitoraggio', 3, 'T0100', NULL, NULL, 'Bid_con_legami_a_soggetti_inseriti_in_un_dato_periodo_suddivisi_per_gruppo_operatori', '1', '(select CAST(bbb.cd_biblioteca as text) as Biblioteca, 
CAST(bbb.ufficio_appart as text),
count(distinct ts.bid) as totale_bid
,percento(count(distinct ts.bid),(select count(distinct ts1.bid||bbb1.ufficio_appart) 
from tr_tit_sog_bib ts1 
inner join tbf_utenti_professionali_web utp1 on utp1.userid = substring(ts1.ute_ins,7,char_length(ts1.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb1 on bbb1.id_utente_professionale=utp1.id_utente_professionale
where ts1.fl_canc<>''S'' 
and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)) as perc_su_tot_polo   
from tr_tit_sog_bib ts 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(ts.ute_ins,7,char_length(ts.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where ts.fl_canc<>''S'' 
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
group by 1,2  order by 1,2) 
UNION
(select CAST(bbb.cd_biblioteca as text)||'' (totale)'', null, 
count(distinct ts.bid||bbb.ufficio_appart) as totale_bid
,percento(count(distinct ts.bid||bbb.ufficio_appart),(select count(distinct ts1.bid||bbb1.ufficio_appart) 
from tr_tit_sog_bib ts1 
inner join tbf_utenti_professionali_web utp1 on utp1.userid = substring(ts1.ute_ins,7,char_length(ts1.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb1 on bbb1.id_utente_professionale=utp1.id_utente_professionale
where ts1.fl_canc<>''S'' 
and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)) as perc_su_tot_polo   
from tr_tit_sog_bib ts 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(ts.ute_ins,7,char_length(ts.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where ts.fl_canc<>''S'' 
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
group by 1 order by 1) 
UNION
(select ''Totale''||chr(160)||''polo'', null, 
count(distinct ts.bid||bbb.ufficio_appart) as totale_bid
,percento(count(distinct ts.bid||bbb.ufficio_appart),(select count(distinct ts1.bid||bbb1.ufficio_appart) 
from tr_tit_sog_bib ts1 
inner join tbf_utenti_professionali_web utp1 on utp1.userid = substring(ts1.ute_ins,7,char_length(ts1.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb1 on bbb1.id_utente_professionale=utp1.id_utente_professionale
where ts1.fl_canc<>''S'' 
and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)) as perc_su_tot_polo   
from tr_tit_sog_bib ts 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(ts.ute_ins,7,char_length(ts.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where ts.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
) 
', 'biblioteca|ufficio|totale_bid|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(147, '00_GMonitoraggio4', '00_GMonitoraggio', 4, 'T0100', NULL, NULL, 'Bid_con_legami_a_classi_inseriti_in_un_dato_periodo_suddivisi_per_gruppo_operatori', '1', '(select CAST(bbb.cd_biblioteca as text) as Biblioteca, 
CAST(bbb.ufficio_appart as text),
count(distinct ts.bid) as totale_bid
,percento(count(distinct ts.bid),(select count(distinct ts1.bid||bbb1.ufficio_appart) from tr_tit_cla ts1 
inner join tbf_utenti_professionali_web utp1 on utp1.userid = substring(ts1.ute_ins,7,char_length(ts1.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb1 on bbb1.id_utente_professionale=utp1.id_utente_professionale
where ts1.fl_canc<>''S'' 
and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)) as perc_su_tot_polo   
from tr_tit_cla ts 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(ts.ute_ins,7,char_length(ts.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where ts.fl_canc<>''S'' 
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
group by 1,2  order by 1,2) 
UNION
(select CAST(bbb.cd_biblioteca as text)||'' (totale)'', null, 
count(distinct ts.bid||bbb.ufficio_appart) as totale_bid
,percento(count(distinct ts.bid||bbb.ufficio_appart),(select count(distinct ts1.bid||bbb1.ufficio_appart) 
from tr_tit_cla ts1 
inner join tbf_utenti_professionali_web utp1 on utp1.userid = substring(ts1.ute_ins,7,char_length(ts1.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb1 on bbb1.id_utente_professionale=utp1.id_utente_professionale
where ts1.fl_canc<>''S'' 
and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)) as perc_su_tot_polo   
from tr_tit_cla ts 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(ts.ute_ins,7,char_length(ts.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where ts.fl_canc<>''S'' 
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
group by 1 order by 1) 
UNION
(select ''Totale''||chr(160)||''polo'', null, 
count(distinct ts.bid||bbb.ufficio_appart) as totale_bid
,percento(count(distinct ts.bid||bbb.ufficio_appart),(select count(distinct ts1.bid||bbb1.ufficio_appart) 
from tr_tit_cla ts1 
inner join tbf_utenti_professionali_web utp1 on utp1.userid = substring(ts1.ute_ins,7,char_length(ts1.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb1 on bbb1.id_utente_professionale=utp1.id_utente_professionale
where ts1.fl_canc<>''S'' 
and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)) as perc_su_tot_polo   
from tr_tit_cla ts 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(ts.ute_ins,7,char_length(ts.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where ts.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
)', 'biblioteca|ufficio|totale_bid|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(148, '00_GMonitoraggio5', '00_GMonitoraggio', 5, 'T0100', NULL, NULL, 'Inventari_inseriti_in_un_dato_periodo_suddivisi_per_tipo_materiale_natura_e_gruppo_operatori', '1', '(select 
CAST(bbb.cd_biblioteca as text), CAST(bbb.ufficio_appart as text), 
tit.tp_materiale, tit.cd_natura,
count(*) as totale,
percento (count(inv), (select count(bbb1.cd_biblioteca) from tbc_inventario inv1
inner join tb_titolo tit1 on inv1.bid = tit1.bid 
inner join tbf_utenti_professionali_web utp1 on utp1.userid = substring(inv1.ute_ins,7,char_length(inv1.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb1 on bbb1.id_utente_professionale=utp1.id_utente_professionale
where 
bbb.cd_biblioteca=bbb1.cd_biblioteca
and 
tit1.fl_canc<>''S'' and inv1.fl_canc<>''S'' and inv1.cd_sit=''2''  
and not tit1.tp_materiale isnull and  tit1.tp_materiale >'' '' 
and ('''' in (:0lista) or bbb1.cd_biblioteca in (:0lista))
and CAST(inv1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb1.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
group by bbb1.cd_biblioteca)) as percentuale
FROM tbc_inventario inv
inner join tb_titolo tit on inv.bid = tit.bid 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(inv.ute_ins,7,char_length(inv.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where tit.fl_canc<>''S'' and inv.fl_canc<>''S'' and inv.cd_sit=''2''  
and not tit.tp_materiale isnull and  tit.tp_materiale >'' ''
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(inv.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
group by bbb.cd_biblioteca,2,3,4
order by 1,2,3,4)
UNION
(select 
CAST(bbb.cd_biblioteca as text)||'' (totale)'', null, null, null, count(*),
percento (count(inv), (select count(bbb1.cd_biblioteca) from tbc_inventario inv1
inner join tb_titolo tit1 on inv1.bid = tit1.bid 
inner join tbf_utenti_professionali_web utp1 on utp1.userid = substring(inv1.ute_ins,7,char_length(inv1.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb1 on bbb1.id_utente_professionale=utp1.id_utente_professionale
where 
bbb.cd_biblioteca=bbb1.cd_biblioteca
and 
tit1.fl_canc<>''S'' and inv1.fl_canc<>''S'' and inv1.cd_sit=''2''  
and not tit1.tp_materiale isnull and  tit1.tp_materiale >'' '' 
and ('''' in (:0lista) or bbb1.cd_biblioteca in (:0lista))
and CAST(inv1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb1.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
group by bbb1.cd_biblioteca)) as percentuale
FROM tbc_inventario inv
inner join tb_titolo tit on inv.bid = tit.bid 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(inv.ute_ins,7,char_length(inv.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where tit.fl_canc<>''S'' and inv.fl_canc<>''S'' and inv.cd_sit=''2''  
and not tit.tp_materiale isnull and  tit.tp_materiale >'' ''
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(inv.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
group by bbb.cd_biblioteca
order by bbb.cd_biblioteca)
UNION
(select 
''Totale''||chr(160)||''polo'', null, null, null, count(*), null 
FROM tbc_inventario inv
inner join tb_titolo tit on inv.bid = tit.bid 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(inv.ute_ins,7,char_length(inv.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where tit.fl_canc<>''S'' and inv.fl_canc<>''S'' and inv.cd_sit=''2''  
and not tit.tp_materiale isnull and  tit.tp_materiale >'' ''
and CAST(inv.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 1
order by 1)', 'biblioteca|ufficio|tipo_materiale|natura|totale|%_su_tot_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(149, '00_GMonitoraggio6', '00_GMonitoraggio', 6, 'T0100', NULL, NULL, 'Lista_dei_bid_creati_in_un_dato_periodo_con_indicazione_del_gruppo_operatori', '1', '(select 
CAST(tb.bid as text), CAST(bbb.cd_biblioteca as text), CAST(bbb.ufficio_appart as text), 
tb.cd_natura, tb.tp_record_uni, tb.tp_materiale, CAST(tb.aa_pubb_1 as text), 
CAST(tb.isbd as text)
from tb_titolo tb
inner join tbf_utenti_professionali_web utp on utp.userid = substring(tb.ute_ins,7,char_length(tb.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where tb.fl_canc<>''S''
and substring(tb.bid,1,3)=(select po.cd_polo from tbf_polo po)
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(tb.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
order by 1);', 'bid|biblioteca|ufficio|natura|tipo_record|tipo_materiale|data1|isbd', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(150, '00_GMonitoraggio7', '00_GMonitoraggio', 7, 'T0100', NULL, NULL, 'Lista_dei_vid_creati_in_un_dato_periodo_con_indicazione_del_gruppo_operatori', '1', '(select 
CAST(ta.vid as text), CAST(bbb.cd_biblioteca as text), CAST(bbb.ufficio_appart as text), 
ta.tp_forma_aut, ta.tp_nome_aut, CAST(ta.ds_nome_aut as text)  
from tb_autore ta
inner join tbf_utenti_professionali_web utp on utp.userid = substring(ta.ute_ins,7,char_length(ta.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where ta.fl_canc<>''S''
and substring(ta.vid,1,3)=(select po.cd_polo from tbf_polo po)
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(ta.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
order by 1);', 'vid|biblioteca|ufficio|forma|tipo_nome|nome ', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(151, '00_GMonitoraggio8', '00_GMonitoraggio', 8, 'T0100', NULL, NULL, 'Lista_dei_soggetti_creati_in_un_dato_periodo_con_indicazione_del_gruppo_operatori', '1', '(select 
CAST(ta.cid as text), CAST(bbb.cd_biblioteca as text), CAST(bbb.ufficio_appart as text)
, CAST(ta.cd_soggettario as text), CAST(ta.cd_edizione as text), CAST(ta.ds_soggetto as text)
from tb_soggetto ta
inner join tbf_utenti_professionali_web utp on utp.userid = substring(ta.ute_ins,7,char_length(ta.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where ta.fl_canc<>''S''
and substring(ta.cid,1,3)=(select po.cd_polo from tbf_polo po)
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(ta.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
order by 1);', 'cid|biblioteca|ufficio|soggettario|edizione|testo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(158, '00_GMonitoraggio9', '00_GMonitoraggio', 9, 'T0100', NULL, NULL, 'Lista_dei_bid_con_legami_a_possessori_o_provenienze_inseriti_in_un_dato_periodo_con_indicazione_del_gruppo_operatori', '1', '(select distinct(CAST(inv.bid as text)), 
CAST(bbb.cd_biblioteca as text), CAST(bbb.ufficio_appart as text),
CAST(tpp.cd_biblioteca||'' ''||tpp.cd_serie||'' ''||tpp.cd_inven as text) as inven, 
CAST(tpp.pid as text), CAST(pp.ds_nome_aut as text),
CAST(tit.isbd as text)
from trc_poss_prov_inventari tpp
inner join tbf_utenti_professionali_web utp on utp.userid = substring(tpp.ute_ins,7,char_length(tpp.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
inner join tbc_inventario inv 
on inv.cd_bib||inv.cd_serie||inv.cd_inven=tpp.cd_biblioteca||tpp.cd_serie||tpp.cd_inven 
inner join tbc_possessore_provenienza pp 
on pp.pid=tpp.pid 
inner join tb_titolo tit on inv.bid=tit.bid 
where tpp.fl_canc<>''S'' and inv.bid notnull and inv.fl_canc<>''S''
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista)) 
and CAST(tpp.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
order by 1,4);', 'bid|biblioteca|ufficio|inventario|pid|possessore|isbd

', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(160, '00_GMonitoraggio', NULL, 9, 'T0100', NULL, 0, NULL, NULL, NULL, NULL, 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(164, '00_GDocFis17', '00_GDocFis', 15, 'C0000', NULL, NULL, 'Lista_dei_bid_con_un_dato_codice_provenienza', '1', 'select  
CAST(inv.cd_bib||'' ''||inv.cd_serie||'' ''||inv.cd_inven as text) as inven,
CAST(inv.bid as text), CAST(tit.isbd as text)
from tbc_inventario inv, tb_titolo tit
where inv.fl_canc <> ''S'' and inv.cd_sit = ''2'' and tit.fl_canc <> ''S''
and inv.bid = tit.bid 
and inv.cd_proven notnull and inv.cd_biblioteca_proven notnull  
and ('''' in (:0lista) or inv.cd_biblioteca_proven in (:0lista))
and trim(inv.cd_proven) = upper(:3proven)
and (inv.data_ingresso between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
or 
(inv.data_ingresso isnull and CAST(inv.ts_var as date) between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) )) 
order by 1;

', 'inventario|bid|isbd', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(165, '00_CatBib18', '00_CatBib', 18, 'IC000', NULL, NULL, 'Lista_dei_bid_con_risorse_digitali_associate', '1', 'select DISTINCT(CAST(invv.bid as text)),
CAST(invv.cd_polo||invv.cd_bib as text),
CAST(invv.digitalizzazione as text),
(select sbnweb.concatena_uri
	(cast(invv.bid as varchar),
     cast(:0lista as varchar),
 	 cast(:1dataD as TIMESTAMP),cast(:2dataA as TIMESTAMP),
     cast(:3tpDigit as varchar),
     cast(:4progetto as varchar)))
from sbnweb.tbc_inventario invv 
where invv.fl_canc<>''S'' and invv.digitalizzazione > '' '' 
and invv.id_accesso_remoto > '' ''
and ('''' in (:0lista) or invv.cd_bib in (:0lista)) 
and (:3tpDigit='''' or invv.digitalizzazione=:3tpDigit) 
and (:4progetto='''' or invv.rif_teca_digitale=:4progetto) 
and invv.ts_var between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
order by 1,3;', 'BID|codice SBN della Biblioteca|codice tipo di digitalizzazione|URI della risorsa elettronica ', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(100, '00_GSer10', '00_GSer', 2, 'L0000', NULL, NULL, 'Totale_utenti_suddivisi_per_fasce_di_eta_e_genere', '1', '(select 
case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) <= 14 then '' <= 14'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 15 and 26  then '' 15 - 25'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 26 and 41 then '' 26 - 40'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 41 and 66 then '' 41 - 65'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) >  65 then chr(160)||''> 65'' 
else ''altro''  end  as fascia, 
sum(case when sesso = ''F'' THEN 1 else 0 END) as femmine,
percento(sum(case when sesso = ''F'' THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) as perc_F_su_totbib,
sum(case when sesso = ''M'' THEN 1 else 0 END) as maschi,
percento(sum(case when sesso = ''M'' THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) as perc_M_su_totbib,
count(*),
percento(count(*),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) as perc_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where
a.id_utenti = b.id_utenti  and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S''
group by 1 order by 1)
UNION
(Select  chr(160)||chr(160)||''Totale della bib '' as fascia, 
sum(case when sesso = ''F'' THEN 1 else 0 END) as femmine,
percento(sum(case when sesso = ''F'' THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) as perc_F_su_totbib,
sum(case when sesso = ''M'' THEN 1 else 0 END) as maschi,
percento(sum(case when sesso = ''M'' THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) as perc_M_su_totbib,
count(*) 
,case (select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
) = 0 when true then 100
else 
percento(count(*),(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) end as perc_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where
a.id_utenti = b.id_utenti  and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S''
)', 'fascia_età|femmine|%_F_su_totale_bib|maschi|%_M_su_totale_bib|totale_fascia|%_su_totale_bib', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(101, '00_GSer11', '00_GSer', 3, 'L0000', NULL, NULL, 'Totale_utenti_suddivisi_per_fasce_di_eta_e_genere_in_un_dato_periodo_di_iscrizione', '1', '(select 
case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) <= 14 then '' <= 14'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 15 and 26  then '' 15 - 25'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 26 and 41 then '' 26 - 40'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 41 and 66 then '' 41 - 65'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) >  65 then chr(160)||''> 65'' 
else ''other''  end  as fascia, 
sum(case when sesso = ''F'' THEN 1 else 0 END) as femmine,
percento(sum(case when sesso = ''F'' THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.data_reg between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_F_su_tot_bib,
sum(case when sesso = ''M'' THEN 1 else 0 END) as maschi,
percento(sum(case when sesso = ''M'' THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.data_reg between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_M_su_tot_bib,
count(*),
percento(count(*),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.data_reg between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_fascia_su_tot_bib  
from tbl_utenti a, trl_utenti_biblioteca b 
where
a.id_utenti = b.id_utenti  and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S''
and data_reg between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 1 order by 1)
UNION
(Select chr(160)||chr(160)||''Totale della bib nel periodo'' as fascia, 
sum(case when sesso = ''F'' THEN 1 else 0 END) as femmine,
percento(sum(case when sesso = ''F'' THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.data_reg between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_F_su_tot_bib,
sum(case when sesso = ''M'' THEN 1 else 0 END) as maschi,
percento(sum(case when sesso = ''M'' THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.data_reg between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_M_su_tot_bib,
count(*)
,case (select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.data_reg between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
) = 0 when true then 100
else 
percento(count(*),(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.data_reg between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) end as perc_fascia_su_tot_bib  
from tbl_utenti a, trl_utenti_biblioteca b 
where
a.id_utenti = b.id_utenti  and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S''
and a.data_reg between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)
', 'fascia_età|femmine|%_F_su_totale_bib|maschi|%_M_su_totale_bib|totale_fascia|%_su_totale_bib', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(103, '00_GSer13', '00_GSer', 5, 'L0000', NULL, NULL, 'Totale_utenti_suddivisi_per_cittadinanza_e fasce_di_eta', '1', '(select 
	case paese_citt isnull when true then chr(160)||''Paese non indicato'' else
	case trim(paese_citt)='''' when true then chr(160)||''Paese non indicato'' else
    case (select count(*) from tb_codici cod 
    where a.paese_citt = cod.cd_tabella and tp_tabella = ''PAES'') = 0 when true   then 
    chr(160)||''Codice paese non previsto''
    else
    CAST(paese_citt as text)||'' ('' || 
    (select cod.ds_tabella from tb_codici cod 
    where a.paese_citt = cod.cd_tabella and tp_tabella = ''PAES'') ||'')'' end  end end,
    sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) <= 14 THEN 1 else 0 END) as meno_14,
percento(sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) <= 14 THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) as perc_14_su_totbib,
sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 15 and 26 THEN 1 else 0 END) as tra_15_25,
percento(sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 15 and 26 THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) as perc_15_su_totbib,
sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 26 and 41 THEN 1 else 0 END) as tra_26_40,
percento(sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 26 and 41 THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) as perc_26_su_totbib,
sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 41 and 66 THEN 1 else 0 END) as tra_41_65,
percento(sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 41 and 66 THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) as perc_41_su_totbib,
sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) > 65 THEN 1 else 0 END) as sopra_65,
percento(sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) > 65 THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) as perc_65_su_totbib,
count(*),
percento(count(*), 
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) as perc_citt_su_totbib
from tbl_utenti a
	left outer join tb_codici cod on a.paese_citt = cod.cd_tabella and tp_tabella = ''PAES''
where
a.id_utenti in (select b.id_utenti from trl_utenti_biblioteca b 
where 
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and b.fl_canc<>''S'') and a.fl_canc<>''S''
and a.persona_giuridica=''N''
group by 1 order by 1)
UNION
(Select chr(160)||''Totale della biblioteca'',
sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) <= 14 THEN 1 else 0 END) as meno_14,
percento(sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) <= 14 THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) as perc_14_su_totbib,
sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 15 and 26 THEN 1 else 0 END) as tra_15_25,
percento(sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 15 and 26 THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) as perc_15_su_totbib,
sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 26 and 41 THEN 1 else 0 END) as tra_26_40,
percento(sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 26 and 41 THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) as perc_26_su_totbib,
sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 41 and 66 THEN 1 else 0 END) as tra_41_65,
percento(sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 41 and 66 THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) as perc_41_su_totbib,
sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) > 65 THEN 1 else 0 END) as sopra_65,
percento(sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) > 65 THEN 1 else 0 END),
(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) as perc_65_su_totbib,
count(*),
case (select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
) = 0 when true then 100
else 
percento(count(*),(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.persona_giuridica=''N'' 
)) end as perc_citt_su_totbib
from tbl_utenti a
where
a.id_utenti in (select b.id_utenti from trl_utenti_biblioteca b 
where 
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and b.fl_canc<>''S'') and a.fl_canc<>''S''
and a.persona_giuridica=''N''
) order by 1;', 'cittadinanza|<=_14|%_<=_14|15-25|%_15-25|26-40|%_26-40|41-65|%_41-65|>_65|%_>_65|totale_citt|%_su_totale_bib', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(105, '00_GSer15', '00_GSer', 7, 'L0000', NULL, NULL, 'Totale_utenti_suddivisi_per_specificita_del_titolo_di_studio', '1', '(select 
case when id_specificita_titoli_studio isnull then chr(160)||''Specificità non indicata'' 
         when trim(CAST(id_specificita_titoli_studio as text))='''' then chr(160)||''Specificità non indicata'' 
         when (select id_specificita_titoli_studio from tbl_specificita_titoli_studio 
    	where id_specificita_titoli_studio=b.id_specificita_titoli_studio and fl_canc<>''S'') isnull 
            then CAST(id_specificita_titoli_studio as text)|| '' ('' ||
	lower((select descr from tbl_specificita_titoli_studio where id_specificita_titoli_studio=b.id_specificita_titoli_studio))  || '') - spec. soppressa''
    else 
         CAST(id_specificita_titoli_studio as text)|| '' ('' ||
	lower((select descr from tbl_specificita_titoli_studio where id_specificita_titoli_studio=b.id_specificita_titoli_studio))  || '')'' 
     end, 
count(distinct a.id_utenti), 
percento(count(distinct a.id_utenti), (select count(distinct a1.id_utenti) from tbl_utenti a1, trl_utenti_biblioteca b1 
where
a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) as perc_spec_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where
a.id_utenti = b.id_utenti  and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S'' 
group by 1
order by 1)
UNION
(Select  
chr(160)||chr(160)||''Totale della biblioteca'',
count(distinct a.id_utenti), 
case (select count(distinct a1.id_utenti) from tbl_utenti a1, trl_utenti_biblioteca b1 
where
a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
) = 0 when true then 100
else 
percento(count(distinct a.id_utenti), (select count(distinct a1.id_utenti) from tbl_utenti a1, trl_utenti_biblioteca b1 
where
a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) end as  perc_spec_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where
a.id_utenti = b.id_utenti and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S'' 
)
', 'specificità_titolo_di_studio|totale|%_su_totale_bib', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(110, '00_GSer20', '00_GSer', 12, 'L0000', NULL, NULL, 'Movimenti_archiviati_erogati_in_un_dato_anno_e_famiglia_servizio_suddivisi_per_fasce_di_eta_degli_utenti', '1', '(select 
case when EXTRACT(MONTH FROM data_in_eff) = 1 then  ''01 - Gennaio'' 
     when EXTRACT(MONTH FROM data_in_eff) = 2 then  ''02 - Febbraio''
     when EXTRACT(MONTH FROM data_in_eff) = 3 then  ''03 - Marzo''
     when EXTRACT(MONTH FROM data_in_eff) = 4 then  ''04 - Aprile''
     when EXTRACT(MONTH FROM data_in_eff) = 5 then  ''05 - Maggio''
     when EXTRACT(MONTH FROM data_in_eff) = 6 then  ''06 - Giugno''
     when EXTRACT(MONTH FROM data_in_eff) = 7 then  ''07 - Luglio''
     when EXTRACT(MONTH FROM data_in_eff) = 8 then  ''08 - Agosto''
     when EXTRACT(MONTH FROM data_in_eff) = 9 then  ''09 - Settembre''
     when EXTRACT(MONTH FROM data_in_eff) = 10 then ''10 - Ottobre''
     when EXTRACT(MONTH FROM data_in_eff) = 11 then ''11 - Novembre''
     when EXTRACT(MONTH FROM data_in_eff) = 12 then ''12 - Dicembre''
else ''altro''  end  as mese
,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) <= 14 then 1 else 0 end) as meno14
,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) <= 14 then 1 else 0 end), count(*)) as perc_meno14_totmese
,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 15 and 26 then 1 else 0 end) as a15_25
,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 15 and 26 then 1 else 0 end), count(*)) as perc_15_25_totmese
,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 26 and 41 then 1 else 0 end) as a26_40
,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 26 and 41 then 1 else 0 end), count(*)) as perc_26_40_totmese
,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 41 and 66 then 1 else 0 end) as a41_65
,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 41 and 66 then 1 else 0 end), count(*)) as perc_41_65_totmese
,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) > 65 then 1 else 0 end) as sopra_65
,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) > 65 then 1 else 0 end), count(*)) as perc_sopra_65_totmese
--,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) isnull then 1 else 0 end) as non_det
--,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) isnull then 1 else 0 end), count(*)) as perc_non_det
,count(*) as tot_mese
,percento(count(*),(select count(*) from tbl_storico_richieste_servizio sto1 
left outer join tbl_utenti ute1 
on ute1.id_utenti = sto1.cod_utente 
where 
('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer 
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S''
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff )) as perc_su_tot_anno 
from tbl_storico_richieste_servizio sto 
left outer join tbl_utenti ute 
on ute.id_utenti = sto.cod_utente 
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto.fl_canc<>''S'' and ute.fl_canc<>''S''
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto.data_in_eff) = :1annoInEff 
group by 1 order by 1)
UNION
(Select chr(160)||chr(160)||''TOTALE ANNO''
,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) <= 14 then 1 else 0 end) as meno14
,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) <= 14 then 1 else 0 end), count(*)) as perc_meno14_totmese
,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 15 and 26 then 1 else 0 end) as a15_25
,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 15 and 26 then 1 else 0 end), count(*)) as perc_15_25_totmese
,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 26 and 41 then 1 else 0 end) as a26_40
,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 26 and 41 then 1 else 0 end), count(*)) as perc_26_40_totmese
,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 41 and 66 then 1 else 0 end) as a41_65
,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 41 and 66 then 1 else 0 end), count(*)) as perc_41_65_totmese
,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) > 65 then 1 else 0 end) as sopra_65
,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) > 65 then 1 else 0 end), count(*)) as perc_sopra_65_totmese
--,sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) isnull then 1 else 0 end) as non_det
--,percento (sum(case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) isnull then 1 else 0 end), count(*)) as perc_non_det
,count(*) as tot_mese 
,case (select count(*) from tbl_storico_richieste_servizio sto1 
left outer join tbl_utenti ute1 
on ute1.id_utenti = sto1.cod_utente 
where 
('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer 
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S''
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff ) = 0 when true then 100
else 
percento(count(*),
(select count(*) from tbl_storico_richieste_servizio sto1 
left outer join tbl_utenti ute1 
on ute1.id_utenti = sto1.cod_utente 
where 
('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer 
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S''
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff  )) 
end as perc_su_tot_anno 
from tbl_storico_richieste_servizio sto 
left outer join tbl_utenti ute 
on ute.id_utenti = sto.cod_utente 
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer 
and sto.fl_canc<>''S'' and ute.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto.data_in_eff) = :1annoInEff )', 'mese|<=_14|%_<_14_su_totmese|15-25|%_15-25_su_totmese|26-40|%_26-40_su_totmese|41-65|%_41-65_su_totmese|>_65|%_>_65_su_totmese|tot_mese|%_su_tot_anno', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(166, '00_GSer30', '00_GSer', 21, 'L0000', NULL, NULL, 'Movimenti_correnti_erogati_suddivisi_per_biblioteca_famiglia_e_svolgimento', '1', '((select CAST(ser.cod_bib_dest as text) as biblioteca,
 case co.cd_flg3 isnull when true then chr(160)||''famiglia non impostata'' 
	else case trim(co.cd_flg3)='''' when true then chr(160)||''famiglia non impostata''
  	else co.cd_flg3 end  end as famiglia 
,sum(case when ser.fl_svolg = ''L'' THEN 1 else 0 END) as locale
,percento (sum(case when ser.fl_svolg = ''L'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when ser.fl_svolg = ''I'' THEN 1 else 0 END) as ILL
,percento (sum(case when ser.fl_svolg = ''I'' THEN 1 else 0 END), count(*)) as perc_ill
,count(ser) as tot_famiglia
,percento(count(*),(select count(*) 
from tbl_richiesta_servizio ser1 where ser1.cod_bib_dest=ser.cod_bib_dest
and ser1.fl_canc<>''S''
and ser1.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_su_tot_bib 
from tbl_richiesta_servizio ser
	left outer join tbl_servizio see on ser.id_servizio=see.id_servizio 
	left outer join tbl_tipo_servizio tip on see.id_tipo_servizio=tip.id_tipo_servizio 
	left outer join tb_codici co on tip.cod_tipo_serv = co.cd_tabella 
    	and co.tp_tabella = ''LTSE''
where ser.fl_canc<>''S'' 
and ('''' in (:0lista) or ser.cod_bib_dest in (:0lista))
and ('''' in (:3famSer) or co.cd_flg3 in (:3famSer))
and ser.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by ser.cod_bib_dest,2
order by ser.cod_bib_dest,2)
UNION
(select CAST(ser.cod_bib_dest as text) as biblioteca
,chr(160)||chr(160)||''tutte le famiglie''
,sum(case when ser.fl_svolg = ''L'' THEN 1 else 0 END) as locale
,percento (sum(case when ser.fl_svolg = ''L'' THEN 1 else 0 END), count(*)) as perc_locale
,sum(case when ser.fl_svolg = ''I'' THEN 1 else 0 END) as ILL
,percento (sum(case when ser.fl_svolg = ''I'' THEN 1 else 0 END), count(*)) as perc_ill
,count(ser) as tot_famiglia
,case 
(select count(*) 
from tbl_richiesta_servizio ser1 where ser1.cod_bib_dest=ser.cod_bib_dest
and ser1.fl_canc<>''S''
and ser1.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
) = 0 when true then 100
else 
percento(count(*),(select count(*) 
from tbl_richiesta_servizio ser1 where ser1.cod_bib_dest=ser.cod_bib_dest
and ser1.fl_canc<>''S''
and ser1.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) end as perc_su_tot_bib 
from tbl_richiesta_servizio ser
where ser.fl_canc<>''S'' 
and ('''' in (:0lista) or ser.cod_bib_dest in (:0lista))
and ser.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by ser.cod_bib_dest)
order by 1,2)', 'biblioteca|famiglia|locale|%_locale|ILL|%_ILL|totale_famiglia|%_su_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(112, '00_GSer22', '00_GSer', 14, 'L0000', NULL, NULL, 'Movimenti_archiviati_erogati_in_un_dato_anno_e_famiglia_servizio_suddivisi_per_professione_degli_utenti', '1', '(select
case when (select a.professione from tbl_utenti a where a.id_utenti = sto.cod_utente) isnull 
	then chr(160)||''Professione non indicata'' 
	when trim((select a.professione from tbl_utenti a where a.id_utenti = sto.cod_utente))='''' 
    then chr(160)||''Professione non indicata'' 
         when (select count(*) from tbl_utenti a 
        	left outer join tb_codici on cd_tabella=a.professione where tp_tabella=''RPRF''
			and a.id_utenti = sto.cod_utente )
      = 0 then chr(160)||''Professione non prevista''
    else 
    	CAST((select '' ''||a.professione||'' (''||ds_tabella||'')'' 
        from tbl_utenti a 
        	left outer join tb_codici on cd_tabella=a.professione where tp_tabella=''RPRF''
			and a.id_utenti = sto.cod_utente ) as text) 
    end as professione
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end) as Gen
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end), count(*)) as perc_gen
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end) as Feb
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end), count(*)) as perc_feb
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end) as Mar
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end), count(*)) as perc_mar
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end) as Apr
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end), count(*)) as perc_apr
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end) as Mag
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end), count(*)) as perc_mag
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end) as Giu
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end), count(*)) as perc_giu
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end) as Lug
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end), count(*)) as perc_lug
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end) as Ago
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end), count(*)) as perc_ago
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end) as Sett
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end), count(*)) as perc_set
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end) as Ott
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end), count(*)) as perc_ott
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end) as Nov
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end), count(*)) as perc_nov
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end) as Dic
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end), count(*)) as perc_dic
,count(*) as tot_prof
,percento(count(*),(select count(*) 
from tbl_storico_richieste_servizio sto1 
 left outer join trl_utenti_biblioteca ute1   
 on ute1.id_utenti = sto1.cod_utente and ute1.cd_biblioteca = sto1.cod_bib_ut
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff  )) as perc_su_tot_anno 
from tbl_storico_richieste_servizio sto 
 left outer join trl_utenti_biblioteca ute   
 on ute.id_utenti = sto.cod_utente and ute.cd_biblioteca = sto.cod_bib_ut
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto.fl_canc<>''S'' and ute.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto.data_in_eff) = :1annoInEff
group by 1 order by 1)
UNION
(Select chr(160)||chr(160)||''TOTALE''
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end) as Gen
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end), count(*)) as perc_gen
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end) as Feb
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end), count(*)) as perc_feb
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end) as Mar
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end), count(*)) as perc_mar
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end) as Apr
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end), count(*)) as perc_apr
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end) as Mag
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end), count(*)) as perc_mag
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end) as Giu
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end), count(*)) as perc_giu
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end) as Lug
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end), count(*)) as perc_lug
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end) as Ago
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end), count(*)) as perc_ago
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end) as Sett
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end), count(*)) as perc_set
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end) as Ott
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end), count(*)) as perc_ott
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end) as Nov
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end), count(*)) as perc_nov
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end) as Dic
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end), count(*)) as perc_dic
,count(*) as tot_occ_prof
,case (select count(*) 
from tbl_storico_richieste_servizio sto1 
 left outer join trl_utenti_biblioteca ute1   
 on ute1.id_utenti = sto1.cod_utente and ute1.cd_biblioteca = sto1.cod_bib_ut
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff  ) = 0 when true then 100
else 
percento(count(*),(select count(*) 
from tbl_storico_richieste_servizio sto1 
 left outer join trl_utenti_biblioteca ute1   
 on ute1.id_utenti = sto1.cod_utente and ute1.cd_biblioteca = sto1.cod_bib_ut
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S''
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff  )) 
end as perc_su_tot_anno 
from tbl_storico_richieste_servizio sto 
 left outer join trl_utenti_biblioteca ute   
 on ute.id_utenti = sto.cod_utente and ute.cd_biblioteca = sto.cod_bib_ut
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto.fl_canc<>''S'' and ute.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto.data_in_eff) = :1annoInEff
)', 'professione|gen|%_gen|feb|%_feb|mar|%_mar|apr|%_apr|mag|%_mag|giu|%_giu|lug|%_lug|ago|%_ago|sett|%_set|ott|%_ott|nov|%_nov|dic|%_dic|tot_prof|%_su_tot_anno', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(177, '00_GestSem10', '00_GestSem', 10, 'IC002', NULL, NULL, 'Lista_classificazioni', '1', 'select case cl.cd_edizione when ''''  then 
(select CAST(_co.cd_unimarc as text) from tb_codici _co 
         where _co.tp_tabella=''SCLA'' 
         and  _co.cd_tabella=cl.cd_sistema)
else ''D''
end  as sistema,
case cl.cd_edizione when ''''  then ''''
ELSE
(select
 CAST(_co.cd_unimarc as text) from tb_codici _co 
         where _co.tp_tabella=''ECLA'' 
         and  _co.cd_tabella=cl.cd_edizione)
end  as edizione,
replace(cl.classe , ''.'', ''. ''), cl.ds_classe,cl.fl_costruito  from "sbnweb"."tb_classe" cl 
 WHERE cl.fl_canc <> ''S'' 
AND((:1sisCla = ''D'' and NOT :2ediCla = '''' AND cl.cd_sistema=:1sisCla||:2ediCla)
    OR (:1sisCla = ''D'' and :2ediCla = '''' AND SUBSTRING(cl.cd_sistema,1,1)=:1sisCla)
    OR (NOT :1sisCla = ''D''  AND cl.cd_sistema =:1sisCla))
AND cl.ts_ins BETWEEN COALESCE( :3dataInsD, to_timestamp(''10010101'',''YYYYMMDD'')) AND COALESCE( :4dataInsA, to_timestamp(''99990101'',''YYYYMMDD''))
AND cl.ts_ins BETWEEN COALESCE( :5dataVarD, to_timestamp(''10010101'',''YYYYMMDD'')) AND COALESCE( :6dataVarA, to_timestamp(''99990101'',''YYYYMMDD''))
AND ('''' = :7condiviso OR cl.fl_condiviso=:7condiviso)
AND upper(cl.classe) 
BETWEEN COALESCE(upper(:8claDa), '''') 
and case when :9claA  isnull then ''ZZZZ'' when :9claA=''''  then ''ZZZZ'' else upper(:9claA||''ZZZZ'') end
order by 1, 2, 3; 

', 'Sistema|Edizione|Notazione|Intestazione|Costruito', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(174, '00_GestSem3', '00_GestSem', 3, 'IC000', NULL, NULL, 'Totale_classi_inserite_in_un_dato_periodo_suddivise_per_sistema_e_edizione', '1', '(select case cl.cd_edizione when ''''  then 
(select CAST(_co.cd_unimarc as text) from tb_codici _co 
         where _co.tp_tabella=''SCLA'' 
         and  _co.cd_tabella=cl.cd_sistema)
else ''D''
end  as sistema,
case cl.cd_edizione when ''''  then ''''
ELSE
(select
 CAST(_co.cd_unimarc as text) from tb_codici _co 
         where _co.tp_tabella=''ECLA'' 
         and  _co.cd_tabella=cl.cd_edizione)
end  as edizione 
,count(*) as totale_classi
,percento (count(*),(select count(cl1) from tb_classe cl1 
where cl1.fl_canc <> ''S'' 
and CAST(cl1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_tot_classi
from tb_classe cl 
where cl.fl_canc <> ''S''
and CAST(cl.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by cl.cd_sistema, cl.cd_edizione order by 1,2)
UNION
(select chr(160)||chr(160)||''totale'', null
,count(*) as totale_classi
,100
from tb_classe cl 
where cl.fl_canc <> ''S''
and CAST(cl.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
) order by 1,2
', 'sistema|edizione|totale_classi|%_sul_totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(175, '00_GestSem6', '00_GestSem', 6, 'IC002', NULL, NULL, 'Totale_legami_titolo_classe_in_un_dato_periodo_suddivisi_per_sistema_di_classificazione', '1', '(select case ts.cd_edizione when ''''  then 
(select CAST(_co.cd_unimarc as text) from tb_codici _co 
         where _co.tp_tabella=''SCLA'' 
         and  _co.cd_tabella=ts.cd_sistema)
else ''D''
end  as sistema,
case ts.cd_edizione when ''''  then ''''
ELSE
(select
 CAST(_co.cd_unimarc as text) from tb_codici _co 
         where _co.tp_tabella=''ECLA'' 
         and  _co.cd_tabella=ts.cd_edizione)
end  as edizione 
,count(*) as totale_legami
,percento(count(*),(select count(*) from tr_tit_cla ts1 
	where ts1.fl_canc<>''S'' 
    and CAST(ts1.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
  )) as perc_su_tot_polo   
from tr_tit_cla ts where ts.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))  
group by 1,2  order by 1,2) 
UNION
(select chr(160)||''Totale del polo''  as polo 
,'' ''
,count(*) as totale_legami
,100   
from tr_tit_cla ts where ts.fl_canc<>''S'' 
and CAST(ts.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
) order by 1,2
', 'sistema|edizione|totale_legami|%_su_totale_polo', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(179, '00_CatBib13', '00_CatBib', 13, 'IC000', NULL, NULL, 'Totale_classi_suddivise_per_biblioteca_sistema_e_edizione', '1', '((select cl.bib_ute_ins as biblioteca, 
(select case cl.cd_edizione when ''''  then 
(select CAST(_co.cd_unimarc as text) from tb_codici _co 
         where _co.tp_tabella=''SCLA'' 
         and  _co.cd_tabella=cl.cd_sistema)
else ''D''
end)  as sistema,
case cl.cd_edizione when ''''  then ''''
ELSE
(select
CAST(_co.cd_unimarc as text) from tb_codici _co 
         where _co.tp_tabella=''ECLA'' 
         and  _co.cd_tabella=cl.cd_edizione)
end  as edizione
,count(*) as totale_classi
,percento (count(*),(select count(cl1) from vl_classe_stat cl1 
where cl1.bib_ute_ins =  cl.bib_ute_ins)
) as perc_tot_classi
from vl_classe_stat cl 
where ('''' in (:0lista) or cl.bib_ute_ins in (:0lista))
group by 1,2,3 order by 1,2,3)
UNION
(select cl.bib_ute_ins as biblioteca 
,chr(160)||''Totale classi della bib'', chr(160)||'''', count(*) as cnt
,percento (count(cl), (select count(cl1) from vl_classe_stat cl1 
 where cl1.bib_ute_ins =  cl.bib_ute_ins
and ('''' in (:0lista) or cl1.bib_ute_ins in (:0lista))
group by bib_ute_ins) ) as perc_tot_bib
from vl_classe_stat cl
where ('''' in (:0lista) or cl.bib_ute_ins in (:0lista))
group by 1)
) 
order by 1,2,3', 'biblioteca|sistema|edizione|totale_classi|%_su_totale_bib', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(180, '00_CatPolo14', '00_CatPolo', 14, 'IC000', NULL, NULL, 'Totale_classi_suddivise_per_sistema_e_edizione', '0', '(select CAST(cl.cd_sistema as text) as sistema
,CAST(cl.cd_edizione as text) as edizione
,count(*) as totale_classi
,percento (count(*),(select count(cl1) from tb_classe cl1 
where cl1.fl_canc <> ''S'')
) as perc_tot_classi
from tb_classe cl 
where cl.fl_canc <> ''S''
group by cl.cd_sistema, cl.cd_edizione order by 1,2)
UNION
(select chr(160)||chr(160)||''totale'', null
,count(*) as totale_classi
,percento (count(*),(select count(cl1) from tb_classe cl1 
where cl1.fl_canc <> ''S'')
) as perc_tot_classi
from tb_classe cl 
where cl.fl_canc <> ''S''
)', 'sistema|edizione|totale_classi|%_sul_totale', 'P', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(130, '00_CatPolo19', '00_CatPolo', 19, 'IC000', NULL, NULL, 'Lista_identificativi_autore_in_forma_accettata', '1', 'select * from listaIdAutori_bib(:1flcon, :2dataD, :3dataA, :4lettD, :5lettA, :6liv_autD,:7liv_autA, :8tp_nome, :9lista)', 'VID', 'P', 'S');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(106, '00_GSer16', '00_GSer', 8, 'L0000', NULL, NULL, 'Totale_utenti_suddivisi_per_professione', '1', '(select  
	case a.professione isnull when true then chr(160)||''Professione non indicata'' else
	case trim(a.professione)='''' when true then chr(160)||''Professione non indicata'' else
    CAST(a.professione as text)||'' ('' || 
    lower((select ds_tabella from tb_codici where tp_tabella=''RPRF'' and cd_tabella=a.professione)) ||'')'' end  end, 
count(*), 
percento(count(*), (select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
)) as perc_prof_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where a.id_utenti = b.id_utenti and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S''
group by 1
order by 1)
UNION
(Select  
chr(160)||chr(160)||''Totale della biblioteca'',
count(*), 
case (select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S''
) = 0 when true then 100
else 
percento(count(*),(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S''
)) end as perc_prof_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where a.id_utenti = b.id_utenti and 
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S''
) order by 1', 'professione|totale|%_su_totale_bib', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(119, '00_GSer29', '00_GSer', 19, 'L0000', NULL, NULL, 'Totale_richieste_archiviate_suddivise_per_classi_Dewey_e_materiale_inventariabile', '1', 'select
case when inv.cd_mat_inv isnull then chr(160)||''Mat.inv. non indicato'' 
     when trim(CAST(inv.cd_mat_inv as text))='''' then chr(160)||''Mat.inv. non indicato'' 
     when (select count(*) from tb_codici tab where 
    inv.cd_mat_inv = tab.cd_tabella and tab.tp_tabella = ''CTMI'')
      = 0 then chr(160)||''Mat.inv. non previsto''
    else 
	CAST(inv.cd_mat_inv as text) || '' ('' ||
	lower((select tab.ds_tabella from tb_codici tab where 
    inv.cd_mat_inv = tab.cd_tabella and tab.tp_tabella = ''CTMI'')) ||'')'' end as materiale
,sum(case when substring(tc.classe, 0,2) = ''0'' then 1 else 0 END) as tot_000
,sum(case when substring(tc.classe, 0,2) = ''1'' then 1 else 0 END) as tot_100
,sum(case when substring(tc.classe, 0,2) = ''2'' then 1 else 0 END) as tot_200
,sum(case when substring(tc.classe, 0,2) = ''3'' then 1 else 0 END) as tot_300
,sum(case when substring(tc.classe, 0,2) = ''4'' then 1 else 0 END) as tot_400
,sum(case when substring(tc.classe, 0,2) = ''5'' then 1 else 0 END) as tot_500
,sum(case when substring(tc.classe, 0,2) = ''6'' then 1 else 0 END) as tot_600
,sum(case when substring(tc.classe, 0,2) = ''7'' then 1 else 0 END) as tot_700
,sum(case when substring(tc.classe, 0,2) = ''8'' then 1 else 0 END) as tot_800
,sum(case when substring(tc.classe, 0,2) = ''9'' then 1 else 0 END) as tot_900
,count(*)
,case (select count(*) 
from tbc_inventario inv1, tr_tit_cla tc1, tbl_storico_richieste_servizio sto1  
where inv1.bid = tc1.bid 
and tc1.fl_canc <> ''S'' and inv1.fl_canc <> ''S''
and inv1.cd_bib||inv1.cd_serie||inv1.cd_inven = sto1.cd_bib||sto1.cod_serie||sto1.cod_inven 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and sto1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa''
and sto1.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)
 = 0 when true then 100
else 
percento(count(*),(select count(*) 
from tbc_inventario inv1, tr_tit_cla tc1, tbl_storico_richieste_servizio sto1  
where inv1.bid = tc1.bid 
and tc1.fl_canc <> ''S'' and inv1.fl_canc <> ''S''
and inv1.cd_bib||inv1.cd_serie||inv1.cd_inven = sto1.cd_bib||sto1.cod_serie||sto1.cod_inven 
and ('''' in (:0lista) or inv1.cd_bib in (:0lista))
and sto1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa''
and sto1.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
 ))  
end as perc_su_tot
from tbc_inventario inv, tr_tit_cla tc, tbl_storico_richieste_servizio sto  
where inv.bid = tc.bid 
and tc.fl_canc <> ''S'' and inv.fl_canc <> ''S''
and inv.cd_bib||inv.cd_serie||inv.cd_inven = sto.cd_bib||sto.cod_serie||sto.cod_inven 
and ('''' in (:0lista) or inv.cd_bib in (:0lista))
and sto.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa''
and sto.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
group by 1
UNION
(select chr(160)||''Totale''  as materiale
,sum(case when substring(tc.classe, 0,2) = ''0'' then 1 else 0 END) as tot_000
,sum(case when substring(tc.classe, 0,2) = ''1'' then 1 else 0 END) as tot_100
,sum(case when substring(tc.classe, 0,2) = ''2'' then 1 else 0 END) as tot_200
,sum(case when substring(tc.classe, 0,2) = ''3'' then 1 else 0 END) as tot_300
,sum(case when substring(tc.classe, 0,2) = ''4'' then 1 else 0 END) as tot_400
,sum(case when substring(tc.classe, 0,2) = ''5'' then 1 else 0 END) as tot_500
,sum(case when substring(tc.classe, 0,2) = ''6'' then 1 else 0 END) as tot_600
,sum(case when substring(tc.classe, 0,2) = ''7'' then 1 else 0 END) as tot_700
,sum(case when substring(tc.classe, 0,2) = ''8'' then 1 else 0 END) as tot_800
,sum(case when substring(tc.classe, 0,2) = ''9'' then 1 else 0 END) as tot_900
,count(*)
,case (select count(*) 
from tbc_inventario inv1, tr_tit_cla tc1, tbl_storico_richieste_servizio sto1  
where inv1.bid = tc1.bid 
and tc1.fl_canc <> ''S'' and inv1.fl_canc <> ''S''
and inv1.cd_bib||inv1.cd_serie||inv1.cd_inven = sto1.cd_bib||sto1.cod_serie||sto1.cod_inven 
and ('''' in (:0lista) or sto1.cd_bib in (:0lista))
and sto1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa''
and sto1.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)
 = 0 when true then 100
else 
percento(count(*),(select count(*) 
from tbc_inventario inv1, tr_tit_cla tc1, tbl_storico_richieste_servizio sto1  
where inv1.bid = tc1.bid 
and tc1.fl_canc <> ''S'' and inv1.fl_canc <> ''S''
and inv1.cd_bib||inv1.cd_serie||inv1.cd_inven = sto1.cd_bib||sto1.cod_serie||sto1.cod_inven 
and ('''' in (:0lista) or sto1.cd_bib in (:0lista))
and sto1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa''
and sto1.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
 ))  
end as perc_su_tot
from tbc_inventario inv, tr_tit_cla tc, tbl_storico_richieste_servizio sto  
where inv.bid = tc.bid 
and tc.fl_canc <> ''S'' and inv.fl_canc <> ''S''
and inv.cd_bib||inv.cd_serie||inv.cd_inven = sto.cd_bib||sto.cod_serie||sto.cod_inven 
and ('''' in (:0lista) or sto.cd_bib in (:0lista))
and sto.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa''
and sto.data_in_eff between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
)
order by 1,2', 'materiale_inv|cl_000|cl_100|cl_200|cl_300|cl_400|cl_500|cl_600|cl_700|cl_800|cl_900|totale|%_su_totale_bib
', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(118, '00_GSer28', '00_GSer', 20, 'L0000', NULL, NULL, 'Movimenti_archiviati_erogati_in_un_dato_anno_e_famiglia_servizio_suddivisi_per_occupazione_degli_utenti', '1', '(select
case when id_occupazioni isnull then chr(160)||''Occupazione non indicata'' 
     when trim(CAST(id_occupazioni as text))='''' then chr(160)||''Occupazione non indicata'' 
     when (select count(*) from tbl_occupazioni occ 
    	where occ.id_occupazioni=ute.id_occupazioni and occ.cd_biblioteca=ute.cd_biblioteca 
        and occ.fl_canc<>''S'')
      = 0 then chr(160)||''Occupazione non prevista''
    else 
    '' ''|| CAST(id_occupazioni as text)|| '' ('' ||
	lower((select descr from tbl_occupazioni occ 
    	where occ.id_occupazioni=ute.id_occupazioni and occ.cd_biblioteca=ute.cd_biblioteca 
        and occ.fl_canc<>''S''))  || '')'' 
     end as occupazione    	
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end) as Gen
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end), count(*)) as perc_gen
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end) as Feb
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end), count(*)) as perc_feb
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end) as Mar
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end), count(*)) as perc_mar
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end) as Apr
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end), count(*)) as perc_apr
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end) as Mag
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end), count(*)) as perc_mag
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end) as Giu
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end), count(*)) as perc_giu
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end) as Lug
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end), count(*)) as perc_lug
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end) as Ago
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end), count(*)) as perc_ago
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end) as Sett
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end), count(*)) as perc_set
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end) as Ott
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end), count(*)) as perc_ott
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end) as Nov
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end), count(*)) as perc_nov
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end) as Dic
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end), count(*)) as perc_dic
,count(*) as tot_occ
,percento(count(*),(select count(*) 
from tbl_storico_richieste_servizio sto1 
 left outer join trl_utenti_biblioteca ute1   
 on ute1.id_utenti = sto1.cod_utente and ute1.cd_biblioteca = sto1.cod_bib_ut
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff  )) as perc_su_tot_anno 
from tbl_storico_richieste_servizio sto 
 left outer join trl_utenti_biblioteca ute   
 on ute.id_utenti = sto.cod_utente and ute.cd_biblioteca = sto.cod_bib_ut
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto.fl_canc<>''S'' and ute.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto.data_in_eff) = :1annoInEff
group by 1 order by 1)
UNION
(Select chr(160)||chr(160)||''TOTALE'' 
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end) as Gen
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 1 then 1 else 0 end), count(*)) as perc_gen
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end) as Feb
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 2 then 1 else 0 end), count(*)) as perc_feb
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end) as Mar
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 3 then 1 else 0 end), count(*)) as perc_mar
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end) as Apr
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 4 then 1 else 0 end), count(*)) as perc_apr
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end) as Mag
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 5 then 1 else 0 end), count(*)) as perc_mag
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end) as Giu
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 6 then 1 else 0 end), count(*)) as perc_giu
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end) as Lug
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 7 then 1 else 0 end), count(*)) as perc_lug
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end) as Ago
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 8 then 1 else 0 end), count(*)) as perc_ago
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end) as Sett
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 9 then 1 else 0 end), count(*)) as perc_set
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end) as Ott
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 10 then 1 else 0 end), count(*)) as perc_ott
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end) as Nov
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 11 then 1 else 0 end), count(*)) as perc_nov
,sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end) as Dic
,percento (sum(case when EXTRACT(MONTH FROM data_in_eff) = 12 then 1 else 0 end), count(*)) as perc_dic
,count(*) as tot_occ_prof
,case (select count(*) 
from tbl_storico_richieste_servizio sto1 
 left outer join trl_utenti_biblioteca ute1   
 on ute1.id_utenti = sto1.cod_utente and ute1.cd_biblioteca = sto1.cod_bib_ut
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff  ) = 0 when true then 100
else 
percento(count(*),(select count(*) 
from tbl_storico_richieste_servizio sto1 
 left outer join trl_utenti_biblioteca ute1   
 on ute1.id_utenti = sto1.cod_utente and ute1.cd_biblioteca = sto1.cod_bib_ut
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto1.fl_canc<>''S'' and ute1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto1.data_in_eff) = :1annoInEff  )) 
end as perc_su_tot_anno 
from tbl_storico_richieste_servizio sto 
 left outer join trl_utenti_biblioteca ute   
 on ute.id_utenti = sto.cod_utente and ute.cd_biblioteca = sto.cod_bib_ut
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = :2famSer
and sto.fl_canc<>''S'' and ute.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa'' 
and EXTRACT(YEAR FROM sto.data_in_eff) = :1annoInEff
)', 'occupazione|gen|%_gen|feb|%_feb|mar|%_mar|apr|%_apr|mag|%_mag|giu|%_giu|lug|%_lug|ago|%_ago|sett|%_set|ott|%_ott|nov|%_nov|dic|%_dic|tot_occ|%_su_tot_anno', 'B', 'N');
INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(116, '00_GSer26', '00_GSer', 18, 'L0000', NULL, NULL, 'Numero_di_ritardi_e_solleciti_con_percentuale_su_totale_prestiti_(movimenti_archiviati)', '1', '(select 
case when sto.num_solleciti = 1 then ''prestiti con 1 sollecito'' 
     when sto.num_solleciti = 2 then ''prestiti con 2 solleciti'' 
     when sto.num_solleciti > 2 then ''prestiti con più di 2 solleciti'' 
     else ''altro'' end as supporto
,count(*)   
,case (select count(*) from tbl_storico_richieste_servizio sto1
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista))
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''PR''
and sto1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa''
and sto1.data_fine_prev between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) )
 = 0 when true then 100
else 
percento(count(*),(select count(*) from tbl_storico_richieste_servizio sto1
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista))
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''PR''
and sto1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa''
and sto1.data_fine_prev between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) ))  
end as perc_soll_su_tot_prest 
from tbl_storico_richieste_servizio sto
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista))
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''PR'' 
and sto.fl_canc<>''S'' and sto.num_solleciti > 0
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa''
and sto.data_fine_prev between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) 
group by 1) 
UNION
(select ''ritardi'', count(*), 
case (select count(*) from tbl_storico_richieste_servizio sto1
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''PR'' 
and sto1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa'' 
and sto1.data_fine_prev between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) )
 = 0 when true then 100
else 
percento(count(*),(select count(*) from tbl_storico_richieste_servizio sto1
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''PR'' 
and sto1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa''
and sto1.data_fine_prev between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) ))  
end as perc_rit_su_tot_prest 
from tbl_storico_richieste_servizio sto 
where sto.data_in_eff > sto.data_fine_prev 
and ('''' in (:0lista) or sto.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''PR'' 
and sto.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa''
and sto.data_fine_prev between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) )
UNION
(select chr(160)||''totale prestiti '', count(*), null 
from tbl_storico_richieste_servizio sto 
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista))
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''PR''
and sto.fl_canc<>''S'' 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa''
and sto.data_fine_prev between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) )
UNION
(select ''prestiti conclusi correttamente'',count(*) 
,case (select count(*) from tbl_storico_richieste_servizio sto1
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista))
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''PR''
and sto1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa''
and sto1.data_fine_prev between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) )
 = 0 when true then 100
else 
percento(count(*),(select count(*) from tbl_storico_richieste_servizio sto1
where ('''' in (:0lista) or sto1.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto1.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''PR''
and sto1.fl_canc<>''S'' 
and sto1.flag_pren = ''R'' and sto1.descr_stato_mov = ''chiuso'' and sto1.descr_stato_ric = ''conclusa''
and sto1.data_fine_prev between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) ))  
end as perc_soll_su_tot_prest 
from tbl_storico_richieste_servizio sto 
where ('''' in (:0lista) or sto.cod_bib_ut in (:0lista)) 
and (select tab.cd_flg3 from tb_codici tab where 
SUBSTRING(sto.cod_tipo_serv,0,3) = tab.cd_tabella and tab.tp_tabella = ''LTSE'')
 = ''PR''
and sto.fl_canc<>''S'' and num_solleciti = 0 and sto.data_fine_prev >= sto.data_in_eff 
and sto.flag_pren = ''R'' and sto.descr_stato_mov = ''chiuso'' and sto.descr_stato_ric = ''conclusa''
and sto.data_fine_prev between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD'')) ) 
order by 1
', 'tipo|totale|%_su_totale_prestiti', 'B', 'N');

SELECT setval('sbnweb.tbf_config_statistiche_id_stat_seq', coalesce( (SELECT MAX(id_stat) FROM sbnweb.tbf_config_statistiche), 1 ), true);

/* tb_stat_parameter - parametri delle statistiche */ 

INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(34, '1VIDDa', 'string', '', 'Dal VID', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(34, '2VIDA', 'string', '', 'Al VID', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(43, '1dataD', 'data', '', 'Dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(43, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(44, '1dataD', 'data', '', 'Dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(44, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(45, '1dataD', 'data', '', 'Dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(45, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(46, '1dataD', 'data', '', 'Dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(46, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(47, '3tpAcq', 'combo', 'CTAC', 'Tipo Acquisizione', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(47, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(47, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(47, '1dataD', 'data', '', 'Data ingresso dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(51, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(51, '2dataD', 'data', '', 'Data ingresso dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(51, '3dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(51, '1tpRec', 'combo', 'GEUN', 'Tipo record', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(52, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(52, '1dataD', 'data', '', 'Data ingresso dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(52, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(53, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(53, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(53, '1dataD', 'data', '', 'Data ingresso dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(54, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(55, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(56, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(56, '1dataD', 'data', '', 'Data ingresso dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(56, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(57, '1dataD', 'data', '', 'Data ingresso dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(57, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(57, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(62, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(62, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(62, '1dataD', 'data', '', 'Data ingresso dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(63, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(63, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(63, '1dataD', 'data', '', 'Data ingresso dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(64, '1dataD', 'data', '', 'Data collocazione dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(64, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(64, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(65, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(65, '1dataD', 'data', '', 'Data collocazione dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(65, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(66, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(67, '2aAnno', 'int4', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(67, '1daAnno', 'int4', '', 'Prima data di pubblicazione dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(73, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(74, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(75, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(76, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(77, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(78, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(79, '1daAnno', 'int4', '', 'Prima data di pubblicazione dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(79, '2aAnno', 'int4', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(79, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(80, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(81, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(82, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(83, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(85, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(86, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(86, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(87, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(87, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(88, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(88, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(89, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(89, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(91, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(91, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(93, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(93, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(94, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(94, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(96, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(96, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(97, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(97, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(98, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(98, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(99, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(99, '1dataD', 'data', '', 'Data inizio effettiva dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(99, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(100, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(101, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(101, '1dataD', 'data', '', 'Data iscrizione dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(101, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(102, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(103, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(104, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(105, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(107, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(108, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(110, '1annoInEff', 'int4', '', 'Anno', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(110, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(110, '2famSer', 'comboLibera', 'PR-Prestito,CO-Consultazione,RP-Riproduzione', 'Famiglia servizio', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(111, '2famSer', 'comboLibera', 'PR-Prestito,CO-Consultazione,RP-Riproduzione', 'Famiglia servizio', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(111, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(111, '1annoInEff', 'int4', '', 'Anno', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(112, '1annoInEff', 'int4', '', 'Anno', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(112, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(112, '2famSer', 'comboLibera', 'PR-Prestito,CO-Consultazione,RP-Riproduzione', 'Famiglia servizio', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(106, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(114, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(114, '2famSer', 'comboLibera', 'PR-Prestito,CO-Consultazione,RP-Riproduzione', 'Famiglia servizio', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(115, '1dataD', 'data', '', 'Data inizio effettiva dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(115, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(115, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(116, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(116, '1dataD', 'data', '', 'Data inizio effettiva dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(116, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(118, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(118, '2famSer', 'comboLibera', 'PR-Prestito,CO-Consultazione,RP-Riproduzione', 'Famiglia servizio', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(118, '1annoInEff', 'int4', '', 'Anno', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(119, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(119, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(119, '1dataD', 'data', '', 'Data inizio effettiva dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(120, '3dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(120, '2dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(120, '8tp_nome', 'comboLibera', 'G-G,R-R,E-E,D-D,C-C,B-B,A-A,F-Collettivi,P-Personali', 'Tipo nome', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(120, '1flcon', 'comboLibera', 'n-Locale,s-Condiviso', 'Livello di condivisione', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(120, '7liv_autA', 'comboLibera', '97-97 A.F.,96-96 In lavoraz,95-95 Super,90-90 Max,71-71 Med,51-51 Min,05-05 Rec,04-04 Loc,01-01 Sug', 'A', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(120, '5lettA', 'string', '', 'A', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(120, '4lettD', 'string', '', 'Estremi alfabetici da', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(126, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(127, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(129, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(130, '5lettA', 'string', '', 'A ', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(130, '2dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(130, '3dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(130, '7liv_autA', 'comboLibera', '97-97 A.F.,96-96 In lavoraz,95-95 Super,90-90 Max,71-71 Med,51-51 Min,05-05 Rec,04-04 Loc,01-01 Sug', 'A', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(130, '1flcon', 'comboLibera', 'n-Locale,s-Condiviso', 'Livello di condivisione', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(130, '8tp_nome', 'comboLibera', 'G-G,R-R,E-E,D-D,C-C,B-B,A-A,F-Collettivi,P-Personali', 'Tipo nome ', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(130, '4lettD', 'string', '', 'Estremi alfabetici da ', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(131, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(132, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(136, '3terA', 'string', '', 'al termine', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(136, '2terDa', 'string', '', 'Dal termine', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(136, '1Thesaurus', 'combo', 'STHE', 'Thesaurus', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(143, '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(143, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(143, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(143, '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(144, '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(144, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(144, '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(144, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(145, '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(145, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(146, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(146, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(146, '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(146, '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(147, '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(147, '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(147, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(147, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(148, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(148, '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(148, '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(148, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(149, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(149, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(149, '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(149, '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(150, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(150, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(150, '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(150, '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(151, '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(151, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(151, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(151, '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(158, '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(158, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(158, '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(158, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(159, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(159, '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(159, '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(159, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(164, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(164, '1dataD', 'data', '', 'Data ingresso dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(164, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(164, '3proven', 'string', '', 'Codice di provenienza', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(165, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(165, '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(165, '1dataD', 'data', '', 'Data aggiornamento inventario dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(165, '3tpDigit', 'combo', 'CDIG', 'Tipo di digitalizzazione', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(130, '6liv_autD', 'comboLibera', '97-97 A.F.,96-96 In lavoraz,95-95 Super,90-90 Max,71-71 Med,51-51 Min,05-05 Rec,04-04 Loc,01-01 Sug', 'Livello di Autorit� da', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(120, '6liv_autD', 'comboLibera', '97-97 A.F.,96-96 In lavoraz,95-95 Super,90-90 Max,71-71 Med,51-51 Min,05-05 Rec,04-04 Loc,01-01 Sug', 'Livello di Autorit� da', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(165, '4progetto', 'combo', 'CTEC', 'Progetto', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(166, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(166, '1dataD', 'data', '', 'Data inizio effettiva dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(166, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(166, '3famSer', 'comboLibera', 'PR-Prestito,CO-Consultazione,RP-Riproduzione', 'Famiglia servizio', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(114, '1dataD', 'data', '', 'Data inizio effettiva dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(114, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(174, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(174, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(175, '1dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(175, '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(177, '8claDa', 'string', '', 'Dalla classe', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(177, '1sisCla', 'combo', 'SCLA', 'Sistema', 'S');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(177, '2ediCla', 'combo', 'ECLA', 'Edizione', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(177, '3dataInsD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(177, '4dataInsA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(177, '5dataVarD', 'data', '', 'Data aggiornamento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(177, '6dataVarA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(177, '7condiviso', 'comboLibera', 's-Condivise,n-Locali', 'Condivise con Indice', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(177, '9claA', 'string', '', 'alla Classe', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(179, '0lista', 'filtroListaBib', '', 'Lista Biblioteche', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(167, '3dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(167, '1flcon', 'comboLibera', 'n-Locale,s-Condiviso', 'Livello di condivisione', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(167, '8tp_nome', 'comboLibera', 'G-G,R-R,E-E,D-D,C-C,B-B,A-A,F-Collettivi,P-Personali', 'Tipo nome', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(167, '6liv_autD', 'comboLibera', '97-97 A.F.,96-96 In lavoraz,95-95 Super,90-90 Max,71-71 Med,51-51 Min,05-05 Rec,04-04 Loc,01-01 Sug', 'Livello di Autorit� da', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(167, '4lettD', 'string', '', 'Estremi alfabetici da', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(167, '5lettA', 'string', '', 'A', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(167, '7liv_autA', 'comboLibera', '97-97 A.F.,96-96 In lavoraz,95-95 Super,90-90 Max,71-71 Med,51-51 Min,05-05 Rec,04-04 Loc,01-01 Sug', 'A', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(167, '2dataD', 'data', '', 'Data inserimento dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(130, '9lista', 'filtroListaBibNoSplit', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES(167, '9lista', 'filtroListaBibNoSplit', '', 'Biblioteca', 'N');

/* aggiornamento nome per alcune statistiche area SERVIZI */

UPDATE sbnweb.tbf_config_statistiche SET nome_statistica='Movimenti_archiviati_erogati_suddivisi_per_biblioteca_famiglia_e_svolgimento'
WHERE parent='00_GSer' and seq_ordinamento=1;
UPDATE sbnweb.tbf_config_statistiche SET nome_statistica='Movimenti_archiviati_erogati_in_un_dato_anno_e_famiglia_servizio_suddivisi_per_fasce_di_eta_degli_utenti'  
where parent='00_GSer' and seq_ordinamento=12;
UPDATE sbnweb.tbf_config_statistiche SET nome_statistica='Movimenti_archiviati_erogati_in_un_dato_anno_e_famiglia_servizio_suddivisi_per_categoria_di_autorizzazione_degli_utenti' 
where parent='00_GSer' and seq_ordinamento=13;
UPDATE sbnweb.tbf_config_statistiche SET nome_statistica='Movimenti_archiviati_erogati_in_un_dato_anno_e_famiglia_servizio_suddivisi_per_professione_degli_utenti' 
where parent='00_GSer' and seq_ordinamento=14;
UPDATE sbnweb.tbf_config_statistiche SET nome_statistica='Movimenti_archiviati_locali_e_ill_suddivisi_per_stato_richiesta' 
where parent='00_GSer' and seq_ordinamento=16;
UPDATE sbnweb.tbf_config_statistiche SET nome_statistica='Totale_richieste_archiviate_suddivise_per_classi_Dewey_e_materiale_inventariabile' 
where parent='00_GSer' and seq_ordinamento=19;
UPDATE sbnweb.tbf_config_statistiche SET nome_statistica='Movimenti_archiviati_erogati_in_un_dato_anno_e_famiglia_servizio_suddivisi_per_occupazione_degli_utenti' 
where parent='00_GSer' and seq_ordinamento=20;
UPDATE sbnweb.tbf_config_statistiche SET nome_statistica='Riproduzioni_effettuate_divise_per_supporto_(movimenti_archiviati)' 
where parent='00_GSer' and seq_ordinamento=17;
UPDATE sbnweb.tbf_config_statistiche SET nome_statistica='Numero_di_ritardi_e_solleciti_con_percentuale_su_totale_prestiti_(movimenti_archiviati)' 
where parent='00_GSer' and seq_ordinamento=18;


/* nuova statistica SERVIZI - 22 Totale_utenti_suddivisi_per_professione_in_un_dato_periodo_di_iscrizione */

INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(nextval('tbf_config_statistiche_id_stat_seq'), 
'00_GSer22bz', '00_GSer', 22, 'L0000', NULL, NULL, 'Totale_utenti_suddivisi_per_professione_in_un_dato_periodo_di_iscrizione', '1', 
'(select  
	case a.professione isnull when true then chr(160)||''Professione non indicata'' else
	case trim(a.professione)='''' when true then chr(160)||''Professione non indicata'' else
    CAST(a.professione as text)||'' ('' || 
    lower((select ds_tabella from tb_codici where tp_tabella=''RPRF'' and cd_tabella=a.professione)) ||'')'' end  end, 
count(*), 
percento(count(*), (select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.data_reg between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) as perc_prof_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where a.id_utenti = b.id_utenti and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S'' 
and a.data_reg between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 1
order by 1)
UNION
(Select  
chr(160)||chr(160)||''Totale della biblioteca'',
count(*), 
case (select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S''
and a1.data_reg between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
) = 0 when true then 100
else 
percento(count(*),(select count(*) from tbl_utenti a1, trl_utenti_biblioteca b1 
where a1.id_utenti = b1.id_utenti and
('''' in (:0lista) or b1.cd_biblioteca in (:0lista))
and a1.persona_giuridica=''N'' and a1.fl_canc<>''S'' and b1.fl_canc<>''S'' 
and a1.data_reg between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
)) end as perc_prof_su_totbib
from tbl_utenti a, trl_utenti_biblioteca b 
where a.id_utenti = b.id_utenti and 
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S''
and a.data_reg between COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
) order by 1', 'professione|totale|%_su_totale_bib', 'B', 'N');

/* parametri per la 22 */

INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=22), '1dataD', 'data', '', 'Data iscrizione dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=22), '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=22), '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');


/* nuova statistica SERVIZI - 23 Totale soggetti legati a richieste erogate in un dato periodo */

INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(nextval('tbf_config_statistiche_id_stat_seq'), 
'00_GSer23bz', '00_GSer', 23, 'L0000', NULL, NULL, 
'Totale_soggetti_legati_a_richieste_erogate_in_un_dato_periodo', '1', 
'select lista.sog, CAST(sum(lista.nro) as INTEGER) from
((select case when min(_so.ds_soggetto) isnull then chr(160)||''Richieste non legate a soggetto'' 
else min(_so.ds_soggetto) end as sog, count(*) as nro
from sbnweb.tbl_storico_richieste_servizio _rs
inner join sbnweb.tbc_inventario _in on _rs.cd_bib=_in.cd_bib and _rs.cod_serie= _in.cd_serie and _rs.cod_inven=_in.cd_inven
left outer join sbnweb.tr_tit_sog_bib _ts on _ts.bid=_in.bid and not _ts.fl_canc=''S''
left outer join sbnweb.tb_soggetto _so on _so.cid=_ts.cid
where  
('''' in (:0lista) or  _rs.cod_bib_dest in (:0lista)) 
and _rs.data_richiesta between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and _rs.cod_doc_lett isnull
and _rs.flag_svolg = ''L''
and not lower(_rs.descr_stato_ric) like ''%respinta%''
group by _so.ky_cles1_s||case when _so.ky_cles2_s isnull then '''' else _so.ky_cles2_s end)
union
(select case when min(_so.ds_soggetto) isnull then chr(160)||''Richieste non legate a soggetto'' 
else min(_so.ds_soggetto) end as sog, count(*) as nro
from sbnweb.tbl_richiesta_servizio _rs
inner join sbnweb.tbc_inventario _in on _rs.cod_bib_inv=_in.cd_bib and _rs.cod_serie_inv= _in.cd_serie and _rs.cod_inven_inv=_in.cd_inven
left outer join sbnweb.tr_tit_sog_bib _ts on _ts.bid=_in.bid and not _ts.fl_canc=''S''
left outer join sbnweb.tb_soggetto _so on _so.cid=_ts.cid
where  
('''' in (:0lista) or  _rs.cod_bib_dest in (:0lista)) 
and _rs.data_richiesta between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and _rs.id_documenti_lettore isnull
and _rs.fl_svolg = ''L''
and not _rs.cod_stato_rich = ''B''
group by  _so.ky_cles1_s||case when _so.ky_cles2_s isnull then '''' else _so.ky_cles2_s end)) lista
group by lista.sog 
order by 2 desc, lower(lista.sog);', 
'descrizione soggetto|numero richieste', 'B', 'N');

/* parametri per la 23 */

INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=23), '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=23), '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=23), '1dataD', 'data', '', 'Dal', 'N');

/* nuova statistica SERVIZI - 24 Elenco utenti non attivi nel periodo indicato  */

INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(nextval('tbf_config_statistiche_id_stat_seq'), 
'00_GSer24bz', '00_GSer', 24, 'L0000', NULL, NULL, 
'Elenco_utenti_non_attivi_nel_periodo_indicato', '1', 
'select initcap(ute.cognome) || '' '' || initcap(ute.nome) AS nome,CAST(cod_utente AS text)
from sbnweb.trl_utenti_biblioteca uu, sbnweb.tbl_utenti ute where 
uu.id_utenti = ute.id_utenti 
and uu.fl_canc<>''S'' and ('''' in (:0lista) or uu.cd_biblioteca in (:0lista))
and uu.id_utenti_biblioteca not in 
(select sto.cod_utente 
from sbnweb.tbl_storico_richieste_servizio sto 
where ('''' in (:0lista) or sto.cod_bib_dest in (:0lista))
and sto.fl_canc<>''S'' 
and sto.data_richiesta between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
UNION
select rs.id_utenti_biblioteca 
from sbnweb.tbl_richiesta_servizio rs
where ('''' in (:0lista) or rs.cod_bib_dest in (:0lista))
and rs.fl_canc<>''S'' 
and rs.data_richiesta between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
) order by 1;', 
'nome|codice utente', 'B', 'N');

/* parametri per la 24 */

INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=24), '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=24), '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=24), '1dataD', 'data', '', 'Dal', 'N');


/* nuova statistica SERVIZI - 25 Totale utenti suddivisi per professione genere e fascia di et� in un dato periodo di iscrizione */

INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(nextval('tbf_config_statistiche_id_stat_seq'), '00_GSer25bz', '00_GSer', 25, 
'L0000', NULL, NULL, 
'Totale_utenti_suddivisi_per_professione_genere_e_fascia_di_et�_in_un_dato_periodo_di_iscrizione', '1', 
'(select 
case when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) <= 14 then ''  <= 14'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 15 and 26  then '' 15 - 25'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 26 and 41 then '' 26 - 40'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) between 41 and 66 then '' 41 - 65'' 
     when CAST(substring(CAST(age(data_nascita) as text), 0,3) as int) >  65 then chr(160)||''> 65'' 
else ''other''  end  as fascia, sesso, 
case a.professione isnull when true then chr(160)||''Professione non indicata'' else
	case trim(a.professione)='''' when true then chr(160)||''Professione non indicata'' else
    CAST(a.professione as text)||'' ('' || 
    lower((select ds_tabella from tb_codici where tp_tabella=''RPRF'' and cd_tabella=a.professione)) ||'')'' end  end, 
count(*)
from tbl_utenti a, trl_utenti_biblioteca b 
where 
a.id_utenti = b.id_utenti  and
('''' in (:0lista) or b.cd_biblioteca in (:0lista))
and a.persona_giuridica=''N'' and a.fl_canc<>''S'' and b.fl_canc<>''S''
and data_reg between COALESCE(:1dataD,to_timestamp(''10010101'',''YYYYMMDD'')) 
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
group by 1, 2, 3 order by 1, 2, 3);',
'fascia_et�|genere|professione|totale',
'B', 'N');

/* parametri per la 25  */

INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=25), '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=25), '1dataD', 'data', '', 'Dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GSer' and seq_ordinamento=25), '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');



/* statistiche su richiesta del polo NAP - Area MONITORAGGIO */

/* aggiornamento nome statistica 10 */
 
 UPDATE sbnweb.tbf_config_statistiche 
 SET nome_statistica='Lista_dei_bid_collocati_in_un_dato_periodo_con_indicazione_del_gruppo_operatori_che_li_hanno_inseriti'
 WHERE parent='00_GMonitoraggio' and seq_ordinamento=10;


/* nuova statistica monitoraggio - 11 - Totale delle collocazioni in un dato periodo con indicazione del gruppo operatori */

INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(nextval('tbf_config_statistiche_id_stat_seq'), '00_GMonitoraggio11bz', '00_GMonitoraggio', 11, 'T0100', NULL, NULL, 
'Totale_delle_collocazioni_in_un_dato_periodo_con_indicazione_del_gruppo_operatori_che_hanno_collocato', '1', 
'(select 
CAST(bbb.cd_biblioteca as text), 
case bbb.ufficio_appart isnull when true then chr(160)||''Ufficio non indicato'' else
	case trim(bbb.ufficio_appart)='''' when true then chr(160)||''Ufficio non indicato'' else
    CAST(bbb.ufficio_appart as text) end  end,
count(*) 
from tb_titolo tb
inner join tbc_inventario inv on inv.bid=tb.bid 
inner join tbc_collocazione col on inv.key_loc=col.key_loc 
inner join tbf_utenti_professionali_web utp on utp.userid = substring(col.ute_ins,7,char_length(col.ute_ins)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where inv.fl_canc<>''S'' and inv.cd_sit=''2'' and col.fl_canc<>''S'' 
and ('''' in (:0lista) or inv.cd_bib in (:0lista)) 
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista)) 
and CAST(col.ts_ins as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end 
group by bbb.cd_biblioteca, bbb.ufficio_appart
order by 1,2);',
'biblioteca|ufficio|totale', 'P', 'N');

/* parametri per la 11 del monitoraggio */

INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GMonitoraggio' and seq_ordinamento=11), '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GMonitoraggio' and seq_ordinamento=11), '1dataD', 'data', '', 'Data dal', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GMonitoraggio' and seq_ordinamento=11), '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GMonitoraggio' and seq_ordinamento=11), '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');


/* nuova statistica monitoraggio - 12 Totale dei documenti condivisi in un dato periodo con indicazione del gruppo operatori */

INSERT INTO sbnweb.tbf_config_statistiche
(id_stat, id_area_sezione, parent, seq_ordinamento, codice_attivita, parametro_attivita, codice_modulo, nome_statistica, tipo_query, query, colonne_output, fl_polo_biblio, fl_txt)
VALUES(nextval('tbf_config_statistiche_id_stat_seq'), '00_GMonitoraggio12bz', '00_GMonitoraggio', 12, 'T0100', NULL, NULL, 
'Totale_dei_documenti_condivisi_in_un_dato_periodo_con_indicazione_del_gruppo_operatori_che_hanno_condiviso', '1', 
'(select 
CAST(bbb.cd_biblioteca as text), 
case bbb.ufficio_appart isnull when true then chr(160)||''Ufficio non indicato'' else
	case trim(bbb.ufficio_appart)='''' when true then chr(160)||''Ufficio non indicato'' else
    CAST(bbb.ufficio_appart as text) end  end,
case substring(tb.bid,1,3)=(select po.cd_polo from tbf_polo po) when true then ''Creati'' else ''Catturati'' END,
count(*) 
from tb_titolo tb
inner join tbf_utenti_professionali_web utp on utp.userid = substring(tb.ute_condiviso,7,char_length(tb.ute_condiviso)-6) 
inner join trf_utente_professionale_biblioteca bbb on bbb.id_utente_professionale=utp.id_utente_professionale
where tb.fl_canc<>''S'' 
and tb.fl_condiviso=''s''
and not tb.tp_materiale isnull and  tb.tp_materiale >'' ''
and ('''' in (:0lista) or bbb.cd_biblioteca in (:0lista))
and CAST(tb.ts_condiviso as date) BETWEEN COALESCE(:1dataD, to_timestamp(''10010101'',''YYYYMMDD''))
and COALESCE(:2dataA, to_timestamp(''99990101'',''YYYYMMDD''))
and upper(bbb.ufficio_appart) like 
case when not :3ufficio isnull then ''%''||:3ufficio||''%'' else ''%'' end
group by 1,2,3
order by 1,2,3);',
'biblioteca|ufficio|tipologia|totale', 'P', 'N');

/* parametri per la 12 del monitoraggio */

INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GMonitoraggio' and seq_ordinamento=12), '3ufficio', 'string', '', 'Ufficio di appartenenza', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GMonitoraggio' and seq_ordinamento=12), '2dataA', 'data', '', 'Al', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GMonitoraggio' and seq_ordinamento=12), '0lista', 'filtroListaBib', '', 'Biblioteca', 'N');
INSERT INTO sbnweb.tb_stat_parameter
(id_stat, nome, tipo, valore, etichetta_nome, obbligatorio)
VALUES((select id_stat from sbnweb.tbf_config_statistiche WHERE parent='00_GMonitoraggio' and seq_ordinamento=12), '1dataD', 'data', '', 'Data dal', 'N');

COMMIT;