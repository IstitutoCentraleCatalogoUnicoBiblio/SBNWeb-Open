-- SQL Manager 2007 for PostgreSQL 4.3.0.4
-- ---------------------------------------
-- Host      : XXX.XXX.XXX.XXX
-- Database  : sbnwebDbXXXese
-- Version   : PostgreSQL 8.3.5 on x86_64-unknown-linux-gnu, compiled by GCC gcc (GCC) 4.1.2 20080704 (Red Hat 4.1.2-46)

SET search_path = sbnweb, pg_catalog;

--
-- CREAZIONE INDICI
--
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
-- Definition for index tb_numero_std_idx_lastra (OID = 77938) : 
--
CREATE INDEX tb_numero_std_idx_lastra ON tb_numero_std USING btree (numero_lastra);
--
-- Definition for index tb_numero_std_idx_nstd (OID = 77939) : 
--
CREATE INDEX tb_numero_std_idx_nstd ON tb_numero_std USING btree (numero_std);
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
CREATE UNIQUE INDEX tbf_biblioteca_cdana_idx ON tbf_biblioteca USING btree (cd_ana_biblioteca);
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

CREATE INDEX inventario_idx ON sbnweb.trc_poss_prov_inventari
  USING btree (cd_polo, cd_biblioteca, cd_serie, cd_inven)
  WHERE (fl_canc <> 'S');
--
-- Definition for index id_relazione_tb_codici (OID = 77457) : 
--
ALTER TABLE ONLY trl_relazioni_servizi
    ADD CONSTRAINT id_relazione_tb_codici PRIMARY KEY (id) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index jms_messages_pkey (OID = 77459) : 
--
ALTER TABLE ONLY jms_messages
    ADD CONSTRAINT jms_messages_pkey PRIMARY KEY (messageid, destination);
--
-- Definition for index jms_transactions_pkey (OID = 77461) : 
--
ALTER TABLE ONLY jms_transactions
    ADD CONSTRAINT jms_transactions_pkey PRIMARY KEY (txid);
--
-- Definition for index pk_codici (OID = 77463) : 
--
ALTER TABLE ONLY tb_codici
    ADD CONSTRAINT pk_codici PRIMARY KEY (tp_tabella, cd_tabella) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index pk_link_multim (OID = 77473) : 
--
ALTER TABLE ONLY ts_link_multim
    ADD CONSTRAINT pk_link_multim PRIMARY KEY (id_link_multim) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index pk_nota_proposta (OID = 77475) : 
--
ALTER TABLE ONLY ts_note_proposta
    ADD CONSTRAINT pk_nota_proposta PRIMARY KEY (id_proposta, progr_risposta) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index pk_numero_dtd (OID = 77477) : 
--
ALTER TABLE ONLY tb_numero_std
    ADD CONSTRAINT pk_numero_dtd PRIMARY KEY (bid, tp_numero_std, numero_std) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index pk_proposta (OID = 77479) : 
--
ALTER TABLE ONLY ts_proposta
    ADD CONSTRAINT pk_proposta PRIMARY KEY (ute_mittente, progr_proposta) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index pk_propostamarc (OID = 77481) : 
--
ALTER TABLE ONLY ts_proposta_marc
    ADD CONSTRAINT pk_propostamarc PRIMARY KEY (id_proposta) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index pk_tr_sogp_sogi_idx (OID = 77483) : 
--
ALTER TABLE ONLY tr_sogp_sogi
    ADD CONSTRAINT pk_tr_sogp_sogi_idx PRIMARY KEY (cid_p, cid_i, bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index pk_tsstoplist (OID = 77485) : 
--
ALTER TABLE ONLY ts_stop_list
    ADD CONSTRAINT pk_tsstoplist PRIMARY KEY (id_stop_list) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_abstract_pkey (OID = 77487) : 
--
ALTER TABLE ONLY tb_abstract
    ADD CONSTRAINT tb_abstract_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_arte_tridimens_pkey (OID = 77489) : 
--
ALTER TABLE ONLY tb_arte_tridimens
    ADD CONSTRAINT tb_arte_tridimens_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_audiovideo_pkey (OID = 77491) : 
--
ALTER TABLE ONLY tb_audiovideo
    ADD CONSTRAINT tb_audiovideo_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_autore_pkey (OID = 77493) : 
--
ALTER TABLE ONLY tb_autore
    ADD CONSTRAINT tb_autore_pkey PRIMARY KEY (vid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_cartografia_pkey (OID = 77495) : 
--
ALTER TABLE ONLY tb_cartografia
    ADD CONSTRAINT tb_cartografia_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_classe_pkey (OID = 77497) : 
--
ALTER TABLE ONLY tb_classe
    ADD CONSTRAINT tb_classe_pkey PRIMARY KEY (cd_sistema, cd_edizione, classe) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_composizione_pkey (OID = 77499) : 
--
ALTER TABLE ONLY tb_composizione
    ADD CONSTRAINT tb_composizione_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_descrittore_pkey (OID = 77501) : 
--
ALTER TABLE ONLY tb_descrittore
    ADD CONSTRAINT tb_descrittore_pkey PRIMARY KEY (did) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_disco_sonoro_pkey (OID = 77503) : 
--
ALTER TABLE ONLY tb_disco_sonoro
    ADD CONSTRAINT tb_disco_sonoro_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_grafica_pkey (OID = 77505) : 
--
ALTER TABLE ONLY tb_grafica
    ADD CONSTRAINT tb_grafica_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_impronta_pkey (OID = 77507) : 
--
ALTER TABLE ONLY tb_impronta
    ADD CONSTRAINT tb_impronta_pkey PRIMARY KEY (bid, impronta_1, impronta_2, impronta_3) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_incipit_pkey (OID = 77509) : 
--
ALTER TABLE ONLY tb_incipit
    ADD CONSTRAINT tb_incipit_pkey PRIMARY KEY (bid, numero_mov, numero_p_mov) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_luogo_pkey (OID = 77511) : 
--
ALTER TABLE ONLY tb_luogo
    ADD CONSTRAINT tb_luogo_pkey PRIMARY KEY (lid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_marca_pkey (OID = 77513) : 
--
ALTER TABLE ONLY tb_marca
    ADD CONSTRAINT tb_marca_pkey PRIMARY KEY (mid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_microforma_pkey (OID = 77515) : 
--
ALTER TABLE ONLY tb_microforma
    ADD CONSTRAINT tb_microforma_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_musica_pkey (OID = 77517) : 
--
ALTER TABLE ONLY tb_musica
    ADD CONSTRAINT tb_musica_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_nota_pkey (OID = 77519) : 
--
ALTER TABLE ONLY tb_nota
    ADD CONSTRAINT tb_nota_pkey PRIMARY KEY (bid, tp_nota, progr_nota);
--
-- Definition for index tb_parola_pkey (OID = 77521) : 
--
ALTER TABLE ONLY tb_parola
    ADD CONSTRAINT tb_parola_pkey PRIMARY KEY (id_parola) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_personaggio_pkey (OID = 77523) : 
--
ALTER TABLE ONLY tb_personaggio
    ADD CONSTRAINT tb_personaggio_pkey PRIMARY KEY (id_personaggio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_rappresent_pkey (OID = 77525) : 
--
ALTER TABLE ONLY tb_rappresent
    ADD CONSTRAINT tb_rappresent_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_repertorio_pkey (OID = 77527) : 
--
ALTER TABLE ONLY tb_repertorio
    ADD CONSTRAINT tb_repertorio_pkey PRIMARY KEY (id_repertorio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_risorsa_elettr_pkey (OID = 77529) : 
--
ALTER TABLE ONLY tb_risorsa_elettr
    ADD CONSTRAINT tb_risorsa_elettr_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_soggetto_pkey (OID = 77531) : 
--
ALTER TABLE ONLY tb_soggetto
    ADD CONSTRAINT tb_soggetto_pkey PRIMARY KEY (cid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_stat_parameter_pkey (OID = 77533) : 
--
ALTER TABLE ONLY tb_stat_parameter
    ADD CONSTRAINT tb_stat_parameter_pkey PRIMARY KEY (nome, id_stat);
--
-- Definition for index tb_termine_thesauro_pkey (OID = 77535) : 
--
ALTER TABLE ONLY tb_termine_thesauro
    ADD CONSTRAINT tb_termine_thesauro_pkey PRIMARY KEY (did) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tb_titolo_pkey (OID = 77537) : 
--
ALTER TABLE ONLY tb_titolo
    ADD CONSTRAINT tb_titolo_pkey PRIMARY KEY (bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_buono_ordine_pkey (OID = 77539) : 
--
ALTER TABLE ONLY tba_buono_ordine
    ADD CONSTRAINT tba_buono_ordine_pkey PRIMARY KEY (id_buono_ordine) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_cambi_ufficiali_pkey (OID = 77541) : 
--
ALTER TABLE ONLY tba_cambi_ufficiali
    ADD CONSTRAINT tba_cambi_ufficiali_pkey PRIMARY KEY (id_valuta) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_fatture_pkey (OID = 77543) : 
--
ALTER TABLE ONLY tba_fatture
    ADD CONSTRAINT tba_fatture_pkey PRIMARY KEY (id_fattura) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_offerte_fornitore_pkey (OID = 77545) : 
--
ALTER TABLE ONLY tba_offerte_fornitore
    ADD CONSTRAINT tba_offerte_fornitore_pkey PRIMARY KEY (id_offerte_fornitore) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_ordini_pkey (OID = 77547) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT tba_ordini_pkey PRIMARY KEY (id_ordine) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_parametri_buono_ordine_pkey (OID = 77549) : 
--
ALTER TABLE ONLY tba_parametri_buono_ordine
    ADD CONSTRAINT tba_parametri_buono_ordine_pkey PRIMARY KEY (cd_polo, cd_biblioteca, progr) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_parametri_ordine_pkey (OID = 77551) : 
--
ALTER TABLE ONLY tba_parametri_ordine
    ADD CONSTRAINT tba_parametri_ordine_pkey PRIMARY KEY (cd_polo, cd_bib) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_profili_acquisto_pkey (OID = 77553) : 
--
ALTER TABLE ONLY tba_profili_acquisto
    ADD CONSTRAINT tba_profili_acquisto_pkey PRIMARY KEY (cod_prac, id_sez_acquis_bibliografiche) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_richieste_offerta_pkey (OID = 77555) : 
--
ALTER TABLE ONLY tba_richieste_offerta
    ADD CONSTRAINT tba_richieste_offerta_pkey PRIMARY KEY (cd_polo, cd_bib, cod_rich_off) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_righe_fatture_pkey (OID = 77557) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT tba_righe_fatture_pkey PRIMARY KEY (id_fattura, riga_fattura) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_sez_acquis_bibliografiche_pkey (OID = 77559) : 
--
ALTER TABLE ONLY tba_sez_acquis_bibliografiche
    ADD CONSTRAINT tba_sez_acquis_bibliografiche_pkey PRIMARY KEY (id_sez_acquis_bibliografiche) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tba_suggerimenti_bibliografici_pkey (OID = 77561) : 
--
ALTER TABLE ONLY tba_suggerimenti_bibliografici
    ADD CONSTRAINT tba_suggerimenti_bibliografici_pkey PRIMARY KEY (cd_polo, cd_bib, cod_sugg_bibl) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbb_capitoli_bilanci_pkey (OID = 77563) : 
--
ALTER TABLE ONLY tbb_capitoli_bilanci
    ADD CONSTRAINT tbb_capitoli_bilanci_pkey PRIMARY KEY (id_capitoli_bilanci) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbc_collocazione_pkey (OID = 77565) : 
--
ALTER TABLE ONLY tbc_collocazione
    ADD CONSTRAINT tbc_collocazione_pkey PRIMARY KEY (key_loc) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbc_default_inven_schede_pkey (OID = 77567) : 
--
ALTER TABLE ONLY tbc_default_inven_schede
    ADD CONSTRAINT tbc_default_inven_schede_pkey PRIMARY KEY (id_default_inven_scheda) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbc_esemplare_titolo_pkey (OID = 77569) : 
--
ALTER TABLE ONLY tbc_esemplare_titolo
    ADD CONSTRAINT tbc_esemplare_titolo_pkey PRIMARY KEY (cd_doc, bid, cd_biblioteca, cd_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbc_inventario_pkey (OID = 77571) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT tbc_inventario_pkey PRIMARY KEY (cd_polo, cd_bib, cd_serie, cd_inven) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbc_nota_inv_pkey (OID = 77573) : 
--
ALTER TABLE ONLY tbc_nota_inv
    ADD CONSTRAINT tbc_nota_inv_pkey PRIMARY KEY (cd_polo, cd_bib, cd_serie, cd_inven, cd_nota) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbc_possessore_provenienza_pkey (OID = 77575) : 
--
ALTER TABLE ONLY tbc_possessore_provenienza
    ADD CONSTRAINT tbc_possessore_provenienza_pkey PRIMARY KEY (pid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbc_provenienza_inventario_pkey (OID = 77577) : 
--
ALTER TABLE ONLY tbc_provenienza_inventario
    ADD CONSTRAINT tbc_provenienza_inventario_pkey PRIMARY KEY (cd_proven, cd_biblioteca, cd_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbc_serie_inventariale_pkey (OID = 77579) : 
--
ALTER TABLE ONLY tbc_serie_inventariale
    ADD CONSTRAINT tbc_serie_inventariale_pkey PRIMARY KEY (cd_serie, cd_biblioteca, cd_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbc_sezione_collocazione_pkey (OID = 77581) : 
--
ALTER TABLE ONLY tbc_sezione_collocazione
    ADD CONSTRAINT tbc_sezione_collocazione_pkey PRIMARY KEY (cd_sez, cd_biblioteca, cd_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_anagrafe_utenti_professionali_pkey (OID = 77583) : 
--
ALTER TABLE ONLY tbf_anagrafe_utenti_professionali
    ADD CONSTRAINT tbf_anagrafe_utenti_professionali_pkey PRIMARY KEY (id_utente_professionale) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_attivita_pkey (OID = 77585) : 
--
ALTER TABLE ONLY tbf_attivita
    ADD CONSTRAINT tbf_attivita_pkey PRIMARY KEY (cd_attivita) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_attivita_sbnmarc_pkey (OID = 77587) : 
--
ALTER TABLE ONLY tbf_attivita_sbnmarc
    ADD CONSTRAINT tbf_attivita_sbnmarc_pkey PRIMARY KEY (id_attivita_sbnmarc) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_batch_servizi_cd_attivita_key (OID = 77589) : 
--
ALTER TABLE ONLY tbf_batch_servizi
    ADD CONSTRAINT tbf_batch_servizi_cd_attivita_key UNIQUE (cd_attivita);
--
-- Definition for index tbf_batch_servizi_pkey (OID = 77591) : 
--
ALTER TABLE ONLY tbf_batch_servizi
    ADD CONSTRAINT tbf_batch_servizi_pkey PRIMARY KEY (id_batch_servizi);
--
-- Definition for index tbf_biblioteca_default_pkey (OID = 77593) : 
--
ALTER TABLE ONLY tbf_biblioteca_default
    ADD CONSTRAINT tbf_biblioteca_default_pkey PRIMARY KEY (id_default, cd_biblioteca, cd_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_biblioteca_in_polo_pkey (OID = 77595) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT tbf_biblioteca_in_polo_pkey PRIMARY KEY (cd_biblioteca, cd_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_biblioteca_pkey (OID = 77597) : 
--
ALTER TABLE ONLY tbf_biblioteca
    ADD CONSTRAINT tbf_biblioteca_pkey PRIMARY KEY (id_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_bibliotecario_default_pkey (OID = 77599) : 
--
ALTER TABLE ONLY tbf_bibliotecario_default
    ADD CONSTRAINT tbf_bibliotecario_default_pkey PRIMARY KEY (id_utente_professionale, id_default) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_bibliotecario_pkey (OID = 77601) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT tbf_bibliotecario_pkey PRIMARY KEY (id_utente_professionale) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_coda_jms_pkey (OID = 77603) : 
--
ALTER TABLE ONLY tbf_coda_jms
    ADD CONSTRAINT tbf_coda_jms_pkey PRIMARY KEY (id_coda);
--
-- Definition for index tbf_config_default_id_area_sezione_key (OID = 77605) : 
--
ALTER TABLE ONLY tbf_config_default
    ADD CONSTRAINT tbf_config_default_id_area_sezione_key UNIQUE (id_area_sezione) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_config_default_pkey (OID = 77607) : 
--
ALTER TABLE ONLY tbf_config_default
    ADD CONSTRAINT tbf_config_default_pkey PRIMARY KEY (id_config) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_config_statistiche_id_area_sezione_key (OID = 77609) : 
--
ALTER TABLE ONLY tbf_config_statistiche
    ADD CONSTRAINT tbf_config_statistiche_id_area_sezione_key UNIQUE (id_area_sezione);
--
-- Definition for index tbf_config_statistiche_pkey (OID = 77611) : 
--
ALTER TABLE ONLY tbf_config_statistiche
    ADD CONSTRAINT tbf_config_statistiche_pkey PRIMARY KEY (id_stat);
--
-- Definition for index tbf_contatore_pkey (OID = 77613) : 
--
ALTER TABLE ONLY tbf_contatore
    ADD CONSTRAINT tbf_contatore_pkey PRIMARY KEY (cd_polo, cd_biblioteca, cd_cont, anno, key1) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_default_key_key (OID = 77615) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT tbf_default_key_key UNIQUE ("key") USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_default_pkey (OID = 77617) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT tbf_default_pkey PRIMARY KEY (id_default) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_gruppi_default_pkey (OID = 77619) : 
--
ALTER TABLE ONLY tbf_gruppi_default
    ADD CONSTRAINT tbf_gruppi_default_pkey PRIMARY KEY (id) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_gruppo_attivita_pkey (OID = 77621) : 
--
ALTER TABLE ONLY tbf_gruppo_attivita
    ADD CONSTRAINT tbf_gruppo_attivita_pkey PRIMARY KEY (id_gruppo_attivita_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_lingua_supportata_pkey (OID = 77623) : 
--
ALTER TABLE ONLY tbf_lingua_supportata
    ADD CONSTRAINT tbf_lingua_supportata_pkey PRIMARY KEY (cd_lingua) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_mail_pkey (OID = 77625) : 
--
ALTER TABLE ONLY tbf_mail
    ADD CONSTRAINT tbf_mail_pkey PRIMARY KEY (id) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_modelli_stampe_idx (OID = 77627) : 
--
ALTER TABLE ONLY tbf_modelli_stampe
    ADD CONSTRAINT tbf_modelli_stampe_idx UNIQUE (cd_polo, cd_bib, modello);
--
-- Definition for index tbf_modelli_stampe_pkey (OID = 77629) : 
--
ALTER TABLE ONLY tbf_modelli_stampe
    ADD CONSTRAINT tbf_modelli_stampe_pkey PRIMARY KEY (id_modello) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_modello_biblioteca_pkey (OID = 77631) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_biblioteca
    ADD CONSTRAINT tbf_modello_biblioteca_pkey PRIMARY KEY (id_modello) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_modello_bibliotecario_pkey (OID = 77633) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_bibliotecario
    ADD CONSTRAINT tbf_modello_bibliotecario_pkey PRIMARY KEY (id_modello) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_moduli_funzionali_pkey (OID = 77635) : 
--
ALTER TABLE ONLY tbf_moduli_funzionali
    ADD CONSTRAINT tbf_moduli_funzionali_pkey PRIMARY KEY (id_modulo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_par_auth_pkey (OID = 77637) : 
--
ALTER TABLE ONLY tbf_par_auth
    ADD CONSTRAINT tbf_par_auth_pkey PRIMARY KEY (cd_par_auth, id_parametro) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_par_mat_pkey (OID = 77639) : 
--
ALTER TABLE ONLY tbf_par_mat
    ADD CONSTRAINT tbf_par_mat_pkey PRIMARY KEY (cd_par_mat, id_parametro) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_par_sem_pkey (OID = 77641) : 
--
ALTER TABLE ONLY tbf_par_sem
    ADD CONSTRAINT tbf_par_sem_pkey PRIMARY KEY (tp_tabella_codici, cd_tabella_codici, id_parametro) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_parametro_pkey (OID = 77643) : 
--
ALTER TABLE ONLY tbf_parametro
    ADD CONSTRAINT tbf_parametro_pkey PRIMARY KEY (id_parametro) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_polo_default_pkey (OID = 77645) : 
--
ALTER TABLE ONLY tbf_polo_default
    ADD CONSTRAINT tbf_polo_default_pkey PRIMARY KEY (id_default, cd_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_polo_id_gruppo_attivita_key (OID = 77647) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT tbf_polo_id_gruppo_attivita_key UNIQUE (id_gruppo_attivita) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_polo_pkey (OID = 77649) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT tbf_polo_pkey PRIMARY KEY (cd_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_procedure_output_pkey (OID = 77651) : 
--
ALTER TABLE ONLY tbf_procedure_output
    ADD CONSTRAINT tbf_procedure_output_pkey PRIMARY KEY (id) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_profilo_abilitazione_pkey (OID = 77653) : 
--
ALTER TABLE ONLY tbf_profilo_abilitazione
    ADD CONSTRAINT tbf_profilo_abilitazione_pkey PRIMARY KEY (id_profilo_abilitazione) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbf_utenti_professionali_web_pkey (OID = 77655) : 
--
ALTER TABLE ONLY tbf_utenti_professionali_web
    ADD CONSTRAINT tbf_utenti_professionali_web_pkey PRIMARY KEY (id_utente_professionale) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_calendario_servizi_pkey (OID = 77657) : 
--
ALTER TABLE ONLY tbl_calendario_servizi
    ADD CONSTRAINT tbl_calendario_servizi_pkey PRIMARY KEY (id_tipo_servizio, progr_fascia) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_controllo_attivita_pkey (OID = 77659) : 
--
ALTER TABLE ONLY tbl_controllo_attivita
    ADD CONSTRAINT tbl_controllo_attivita_pkey PRIMARY KEY (id_controllo_attivita) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_controllo_iter_pkey (OID = 77661) : 
--
ALTER TABLE ONLY tbl_controllo_iter
    ADD CONSTRAINT tbl_controllo_iter_pkey PRIMARY KEY (id_controllo_iter) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_disponibilita_precatalogati_pkey (OID = 77663) : 
--
ALTER TABLE ONLY tbl_disponibilita_precatalogati
    ADD CONSTRAINT tbl_disponibilita_precatalogati_pkey PRIMARY KEY (id_disponibilita_precatalogati) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_documenti_lettori_pkey (OID = 77665) : 
--
ALTER TABLE ONLY tbl_documenti_lettori
    ADD CONSTRAINT tbl_documenti_lettori_pkey PRIMARY KEY (id_documenti_lettore) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_esemplare_documento_lettore_pkey (OID = 77667) : 
--
ALTER TABLE ONLY tbl_esemplare_documento_lettore
    ADD CONSTRAINT tbl_esemplare_documento_lettore_pkey PRIMARY KEY (id_esemplare) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_iter_servizio_pkey (OID = 77669) : 
--
ALTER TABLE ONLY tbl_iter_servizio
    ADD CONSTRAINT tbl_iter_servizio_pkey PRIMARY KEY (id_iter_servizio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_materie_pkey (OID = 77671) : 
--
ALTER TABLE ONLY tbl_materie
    ADD CONSTRAINT tbl_materie_pkey PRIMARY KEY (id_materia) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_messaggio_pkey (OID = 77673) : 
--
ALTER TABLE ONLY tbl_messaggio
    ADD CONSTRAINT tbl_messaggio_pkey PRIMARY KEY (progr_msg, cod_rich_serv_richiesta) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_modalita_erogazione_pkey (OID = 77675) : 
--
ALTER TABLE ONLY tbl_modalita_erogazione
    ADD CONSTRAINT tbl_modalita_erogazione_pkey PRIMARY KEY (id_modalita_erogazione);
--
-- Definition for index tbl_modalita_pagamento_pkey (OID = 77677) : 
--
ALTER TABLE ONLY tbl_modalita_pagamento
    ADD CONSTRAINT tbl_modalita_pagamento_pkey PRIMARY KEY (id_modalita_pagamento) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_occupazioni_pkey (OID = 77679) : 
--
ALTER TABLE ONLY tbl_occupazioni
    ADD CONSTRAINT tbl_occupazioni_pkey PRIMARY KEY (id_occupazioni) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_parametri_biblioteca_pkey (OID = 77681) : 
--
ALTER TABLE ONLY tbl_parametri_biblioteca
    ADD CONSTRAINT tbl_parametri_biblioteca_pkey PRIMARY KEY (id_parametri_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_penalita_servizio_pkey (OID = 77683) : 
--
ALTER TABLE ONLY tbl_penalita_servizio
    ADD CONSTRAINT tbl_penalita_servizio_pkey PRIMARY KEY (id_servizio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_posti_sala_pkey (OID = 77685) : 
--
ALTER TABLE ONLY tbl_posti_sala
    ADD CONSTRAINT tbl_posti_sala_pkey PRIMARY KEY (id_posti_sala) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_richiesta_servizio_pkey (OID = 77687) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT tbl_richiesta_servizio_pkey PRIMARY KEY (cod_rich_serv) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_sale_pkey (OID = 77689) : 
--
ALTER TABLE ONLY tbl_sale
    ADD CONSTRAINT tbl_sale_pkey PRIMARY KEY (id_sale) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_servizio_pkey (OID = 77691) : 
--
ALTER TABLE ONLY tbl_servizio
    ADD CONSTRAINT tbl_servizio_pkey PRIMARY KEY (id_servizio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_servizio_web_dati_richiesti_pkey (OID = 77693) : 
--
ALTER TABLE ONLY tbl_servizio_web_dati_richiesti
    ADD CONSTRAINT tbl_servizio_web_dati_richiesti_pkey PRIMARY KEY (campo_richiesta, id_tipo_servizio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_solleciti_pkey (OID = 77695) : 
--
ALTER TABLE ONLY tbl_solleciti
    ADD CONSTRAINT tbl_solleciti_pkey PRIMARY KEY (progr_sollecito, cod_rich_serv);
--
-- Definition for index tbl_specificita_titoli_studio_pkey (OID = 77697) : 
--
ALTER TABLE ONLY tbl_specificita_titoli_studio
    ADD CONSTRAINT tbl_specificita_titoli_studio_pkey PRIMARY KEY (id_specificita_titoli_studio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_storico_richieste_servizio_pkey (OID = 77699) : 
--
ALTER TABLE ONLY tbl_storico_richieste_servizio
    ADD CONSTRAINT tbl_storico_richieste_servizio_pkey PRIMARY KEY (cd_polo, cd_bib, cod_rich_serv) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_supporti_biblioteca_pkey (OID = 77701) : 
--
ALTER TABLE ONLY tbl_supporti_biblioteca
    ADD CONSTRAINT tbl_supporti_biblioteca_pkey PRIMARY KEY (id_supporti_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_tipi_autorizzazioni_pkey (OID = 77703) : 
--
ALTER TABLE ONLY tbl_tipi_autorizzazioni
    ADD CONSTRAINT tbl_tipi_autorizzazioni_pkey PRIMARY KEY (id_tipi_autorizzazione) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_tipo_servizio_pkey (OID = 77705) : 
--
ALTER TABLE ONLY tbl_tipo_servizio
    ADD CONSTRAINT tbl_tipo_servizio_pkey PRIMARY KEY (id_tipo_servizio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbl_utenti_cod_fiscale_key (OID = 77707) : 
--
ALTER TABLE ONLY tbl_utenti
    ADD CONSTRAINT tbl_utenti_cod_fiscale_key UNIQUE (cod_fiscale);
--
-- Definition for index tbl_utenti_ind_posta_elettr_key (OID = 77709) : 
--
ALTER TABLE ONLY tbl_utenti
    ADD CONSTRAINT tbl_utenti_ind_posta_elettr_key UNIQUE (ind_posta_elettr);
--
-- Definition for index tbl_utenti_pkey (OID = 77711) : 
--
ALTER TABLE ONLY tbl_utenti
    ADD CONSTRAINT tbl_utenti_pkey PRIMARY KEY (id_utenti) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbp_fascicolo_idx (OID = 77713) : 
--
ALTER TABLE ONLY tbp_fascicolo
    ADD CONSTRAINT tbp_fascicolo_idx PRIMARY KEY (bid, fid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbq_batch_attivabili_pkey (OID = 77715) : 
--
ALTER TABLE ONLY tbq_batch_attivabili
    ADD CONSTRAINT tbq_batch_attivabili_pkey PRIMARY KEY (cod_funz) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbr_fornitori_biblioteche_pkey (OID = 77717) : 
--
ALTER TABLE ONLY tbr_fornitori_biblioteche
    ADD CONSTRAINT tbr_fornitori_biblioteche_pkey PRIMARY KEY (cd_polo, cd_biblioteca, cod_fornitore) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tbr_fornitori_pkey (OID = 77719) : 
--
ALTER TABLE ONLY tbr_fornitori
    ADD CONSTRAINT tbr_fornitori_pkey PRIMARY KEY (cod_fornitore) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_aut_aut_pkey (OID = 77721) : 
--
ALTER TABLE ONLY tr_aut_aut
    ADD CONSTRAINT tr_aut_aut_pkey PRIMARY KEY (vid_base, vid_coll, tp_legame) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_aut_bib_pkey (OID = 77723) : 
--
ALTER TABLE ONLY tr_aut_bib
    ADD CONSTRAINT tr_aut_bib_pkey PRIMARY KEY (vid, cd_polo, cd_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_aut_mar_pkey (OID = 77725) : 
--
ALTER TABLE ONLY tr_aut_mar
    ADD CONSTRAINT tr_aut_mar_pkey PRIMARY KEY (vid, mid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_des_des_pkey (OID = 77727) : 
--
ALTER TABLE ONLY tr_des_des
    ADD CONSTRAINT tr_des_des_pkey PRIMARY KEY (did_base, did_coll) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_luo_luo_pkey (OID = 77729) : 
--
ALTER TABLE ONLY tr_luo_luo
    ADD CONSTRAINT tr_luo_luo_pkey PRIMARY KEY (lid_base, lid_coll) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_mar_bib_pkey (OID = 77731) : 
--
ALTER TABLE ONLY tr_mar_bib
    ADD CONSTRAINT tr_mar_bib_pkey PRIMARY KEY (cd_polo, cd_biblioteca, mid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_modello_sistemi_classi_biblioteche_pkey (OID = 77733) : 
--
ALTER TABLE ONLY tr_modello_sistemi_classi_biblioteche
    ADD CONSTRAINT tr_modello_sistemi_classi_biblioteche_pkey PRIMARY KEY (id_modello, cd_sistema, cd_edizione) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_modello_soggettari_biblioteche_pkey (OID = 77735) : 
--
ALTER TABLE ONLY tr_modello_soggettari_biblioteche
    ADD CONSTRAINT tr_modello_soggettari_biblioteche_pkey PRIMARY KEY (id_modello) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_modello_thesauri_biblioteche_pkey (OID = 77737) : 
--
ALTER TABLE ONLY tr_modello_thesauri_biblioteche
    ADD CONSTRAINT tr_modello_thesauri_biblioteche_pkey PRIMARY KEY (id_modello) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_per_int_pkey (OID = 77739) : 
--
ALTER TABLE ONLY tr_per_int
    ADD CONSTRAINT tr_per_int_pkey PRIMARY KEY (vid, id_personaggio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_rep_aut_pkey (OID = 77741) : 
--
ALTER TABLE ONLY tr_rep_aut
    ADD CONSTRAINT tr_rep_aut_pkey PRIMARY KEY (vid, id_repertorio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_rep_mar_pkey (OID = 77743) : 
--
ALTER TABLE ONLY tr_rep_mar
    ADD CONSTRAINT tr_rep_mar_pkey PRIMARY KEY (progr_repertorio, mid, id_repertorio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_rep_tit_pkey (OID = 77745) : 
--
ALTER TABLE ONLY tr_rep_tit
    ADD CONSTRAINT tr_rep_tit_pkey PRIMARY KEY (bid, id_repertorio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_sistemi_classi_biblioteche_idx (OID = 77747) : 
--
ALTER TABLE ONLY tr_sistemi_classi_biblioteche
    ADD CONSTRAINT tr_sistemi_classi_biblioteche_idx PRIMARY KEY (cd_biblioteca, cd_polo, cd_sistema, cd_edizione) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_sog_des_pkey (OID = 77749) : 
--
ALTER TABLE ONLY tr_sog_des
    ADD CONSTRAINT tr_sog_des_pkey PRIMARY KEY (did, cid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_soggettari_biblioteche_pkey (OID = 77751) : 
--
ALTER TABLE ONLY tr_soggettari_biblioteche
    ADD CONSTRAINT tr_soggettari_biblioteche_pkey PRIMARY KEY (cd_sogg, cd_biblioteca, cd_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_termini_termini_pkey (OID = 77753) : 
--
ALTER TABLE ONLY tr_termini_termini
    ADD CONSTRAINT tr_termini_termini_pkey PRIMARY KEY (did_base, did_coll) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_thesauri_biblioteche_pkey (OID = 77755) : 
--
ALTER TABLE ONLY tr_thesauri_biblioteche
    ADD CONSTRAINT tr_thesauri_biblioteche_pkey PRIMARY KEY (cd_biblioteca, cd_polo, cd_the) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_tit_aut_pkey (OID = 77757) : 
--
ALTER TABLE ONLY tr_tit_aut
    ADD CONSTRAINT tr_tit_aut_pkey PRIMARY KEY (bid, vid, tp_responsabilita, cd_relazione) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_tit_bib_pkey (OID = 77759) : 
--
ALTER TABLE ONLY tr_tit_bib
    ADD CONSTRAINT tr_tit_bib_pkey PRIMARY KEY (bid, cd_polo, cd_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_tit_cla_pkey (OID = 77761) : 
--
ALTER TABLE ONLY tr_tit_cla
    ADD CONSTRAINT tr_tit_cla_pkey PRIMARY KEY (bid, classe, cd_sistema, cd_edizione) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_tit_luo_pkey (OID = 77763) : 
--
ALTER TABLE ONLY tr_tit_luo
    ADD CONSTRAINT tr_tit_luo_pkey PRIMARY KEY (bid, lid, tp_luogo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_tit_mar_pkey (OID = 77767) : 
--
ALTER TABLE ONLY tr_tit_mar
    ADD CONSTRAINT tr_tit_mar_pkey PRIMARY KEY (bid, mid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tr_tit_sog_bib_pkey (OID = 77769) : 
--
ALTER TABLE ONLY tr_tit_sog_bib
    ADD CONSTRAINT tr_tit_sog_bib_pkey PRIMARY KEY (cid, cd_biblioteca, cd_polo, bid) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tra_elementi_buono_ordine_pkey (OID = 77771) : 
--
ALTER TABLE ONLY tra_elementi_buono_ordine
    ADD CONSTRAINT tra_elementi_buono_ordine_pkey PRIMARY KEY (cd_polo, cd_bib, buono_ord, cod_tip_ord, anno_ord, cod_ord) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tra_fornitori_offerte_pkey (OID = 77773) : 
--
ALTER TABLE ONLY tra_fornitori_offerte
    ADD CONSTRAINT tra_fornitori_offerte_pkey PRIMARY KEY (cd_polo, cd_bib, cod_fornitore, cod_rich_off) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tra_messaggi_pkey (OID = 77775) : 
--
ALTER TABLE ONLY tra_messaggi
    ADD CONSTRAINT tra_messaggi_pkey PRIMARY KEY (cd_polo, cd_bib, cod_msg) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tra_ordine_inventari_pkey (OID = 77777) : 
--
ALTER TABLE ONLY tra_ordine_inventari
    ADD CONSTRAINT tra_ordine_inventari_pkey PRIMARY KEY (id_ordine, cd_polo, cd_bib, cd_serie, cd_inven) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tra_ordini_biblioteche_pkey (OID = 77779) : 
--
ALTER TABLE ONLY tra_ordini_biblioteche
    ADD CONSTRAINT tra_ordini_biblioteche_pkey PRIMARY KEY (cd_polo, cd_bib, cod_bib_ord, cod_tip_ord, anno_ord, cod_ord) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index tra_sez_acq_storico_pkey (OID = 77781) : 
--
ALTER TABLE ONLY tra_sez_acq_storico
    ADD CONSTRAINT tra_sez_acq_storico_pkey PRIMARY KEY (id_sez_acquis_bibliografiche, ts_ins);
--
-- Definition for index tra_sez_acquisizione_fornitori_pkey (OID = 77783) : 
--
ALTER TABLE ONLY tra_sez_acquisizione_fornitori
    ADD CONSTRAINT tra_sez_acquisizione_fornitori_pkey PRIMARY KEY (cd_polo, cd_biblioteca, cod_prac, cod_fornitore) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trc_formati_sezioni_pkey (OID = 77785) : 
--
ALTER TABLE ONLY trc_formati_sezioni
    ADD CONSTRAINT trc_formati_sezioni_pkey PRIMARY KEY (cd_formato, cd_polo_sezione, cd_bib_sezione, cd_sezione) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trc_poss_prov_inventari_pkey (OID = 77787) : 
--
ALTER TABLE ONLY trc_poss_prov_inventari
    ADD CONSTRAINT trc_poss_prov_inventari_pkey PRIMARY KEY (pid, cd_inven, cd_serie, cd_biblioteca, cd_polo, cd_legame) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trf_attivita_affiliate_pkey (OID = 77789) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT trf_attivita_affiliate_pkey PRIMARY KEY (id) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trf_attivita_polo_pkey (OID = 77791) : 
--
ALTER TABLE ONLY trf_attivita_polo
    ADD CONSTRAINT trf_attivita_polo_pkey PRIMARY KEY (id_attivita_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trf_funzioni_denominazioni_pkey (OID = 77793) : 
--
ALTER TABLE ONLY trf_funzioni_denominazioni
    ADD CONSTRAINT trf_funzioni_denominazioni_pkey PRIMARY KEY (cd_funzione, cd_lingua) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trf_gruppo_attivita_pkey (OID = 77795) : 
--
ALTER TABLE ONLY trf_gruppo_attivita
    ADD CONSTRAINT trf_gruppo_attivita_pkey PRIMARY KEY (id_gruppo_attivita_polo_base, id_gruppo_attivita_polo_coll) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trf_gruppo_attivita_polo_pkey (OID = 77797) : 
--
ALTER TABLE ONLY trf_gruppo_attivita_polo
    ADD CONSTRAINT trf_gruppo_attivita_polo_pkey PRIMARY KEY (id_attivita_polo, id_gruppo_attivita_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trf_profilo_biblioteca_pkey (OID = 77799) : 
--
ALTER TABLE ONLY trf_profilo_biblioteca
    ADD CONSTRAINT trf_profilo_biblioteca_pkey PRIMARY KEY (id_profilo_abilitazione_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trf_profilo_profilo_pkey (OID = 77801) : 
--
ALTER TABLE ONLY trf_profilo_profilo
    ADD CONSTRAINT trf_profilo_profilo_pkey PRIMARY KEY (id_profilo_abilitazione_base, id_profilo_abilitazione_coll) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trf_utente_professionale_biblioteca_pkey (OID = 77803) : 
--
ALTER TABLE ONLY trf_utente_professionale_biblioteca
    ADD CONSTRAINT trf_utente_professionale_biblioteca_pkey PRIMARY KEY (id_utente_professionale, cd_polo, cd_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trf_utente_professionale_polo_pkey (OID = 77805) : 
--
ALTER TABLE ONLY trf_utente_professionale_polo
    ADD CONSTRAINT trf_utente_professionale_polo_pkey PRIMARY KEY (id_utente_professionale, cd_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trl_attivita_bibliotecario_pkey (OID = 77807) : 
--
ALTER TABLE ONLY trl_attivita_bibliotecario
    ADD CONSTRAINT trl_attivita_bibliotecario_pkey PRIMARY KEY (id_bibliotecario, id_iter_servizio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trl_autorizzazioni_servizi_pkey (OID = 77809) : 
--
ALTER TABLE ONLY trl_autorizzazioni_servizi
    ADD CONSTRAINT trl_autorizzazioni_servizi_pkey PRIMARY KEY (id_tipi_autorizzazione, id_servizio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trl_diritti_utente_pkey (OID = 77811) : 
--
ALTER TABLE ONLY trl_diritti_utente
    ADD CONSTRAINT trl_diritti_utente_pkey PRIMARY KEY (id_utenti, id_servizio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trl_materie_utenti_pkey (OID = 77813) : 
--
ALTER TABLE ONLY trl_materie_utenti
    ADD CONSTRAINT trl_materie_utenti_pkey PRIMARY KEY (id_utenti, id_materia) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trl_posti_sala_utenti_biblioteca_pkey (OID = 77815) : 
--
ALTER TABLE ONLY trl_posti_sala_utenti_biblioteca
    ADD CONSTRAINT trl_posti_sala_utenti_biblioteca_pkey PRIMARY KEY (id_posti_sala, id_utenti_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trl_supporti_modalita_erogazione_pkey (OID = 77817) : 
--
ALTER TABLE ONLY trl_supporti_modalita_erogazione
    ADD CONSTRAINT trl_supporti_modalita_erogazione_pkey PRIMARY KEY (cod_erog, id_supporti_biblioteca);
--
-- Definition for index trl_tariffe_modalita_erogazione_pkey (OID = 77819) : 
--
ALTER TABLE ONLY trl_tariffe_modalita_erogazione
    ADD CONSTRAINT trl_tariffe_modalita_erogazione_pkey PRIMARY KEY (cod_erog, id_tipo_servizio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trl_utenti_biblioteca_pkey (OID = 77821) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT trl_utenti_biblioteca_pkey PRIMARY KEY (id_utenti_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index trs_termini_titoli_biblioteche_pkey (OID = 77823) : 
--
ALTER TABLE ONLY trs_termini_titoli_biblioteche
    ADD CONSTRAINT trs_termini_titoli_biblioteche_pkey PRIMARY KEY (bid, cd_biblioteca, cd_polo, did) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index ts_servizio_pkey (OID = 77825) : 
--
ALTER TABLE ONLY ts_servizio
    ADD CONSTRAINT ts_servizio_pkey PRIMARY KEY (cd_record) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index unique_attivita_polo (OID = 77827) : 
--
ALTER TABLE ONLY trf_attivita_polo
    ADD CONSTRAINT unique_attivita_polo UNIQUE (cd_polo, cd_attivita) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index unique_parametri_biblioteca (OID = 77829) : 
--
ALTER TABLE ONLY tbl_parametri_biblioteca
    ADD CONSTRAINT unique_parametri_biblioteca UNIQUE (cd_polo, cd_bib) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index unique_tbc_default (OID = 77831) : 
--
ALTER TABLE ONLY tbc_default_inven_schede
    ADD CONSTRAINT unique_tbc_default UNIQUE (cd_biblioteca, cd_polo);
--
-- Definition for index univocita (OID = 77833) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT univocita UNIQUE (cd_attivita, cd_bib_affiliata, cd_bib_centro_sistema) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_bilanci (OID = 77835) : 
--
ALTER TABLE ONLY tbb_bilanci
    ADD CONSTRAINT xpk_bilanci PRIMARY KEY (cod_mat, id_capitoli_bilanci) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_buono_ordine (OID = 77837) : 
--
ALTER TABLE ONLY tba_buono_ordine
    ADD CONSTRAINT xpk_buono_ordine UNIQUE (buono_ord, cd_polo, cd_bib);
--
-- Definition for index xpk_cambi_ufficiali (OID = 77839) : 
--
ALTER TABLE ONLY tba_cambi_ufficiali
    ADD CONSTRAINT xpk_cambi_ufficiali UNIQUE (cd_polo, cd_bib, valuta);
--
-- Definition for index xpk_capitoli_bilanci (OID = 77841) : 
--
ALTER TABLE ONLY tbb_capitoli_bilanci
    ADD CONSTRAINT xpk_capitoli_bilanci UNIQUE (esercizio, capitolo, cd_polo, cd_bib);
--
-- Definition for index xpk_contatore (OID = 77843) : 
--
ALTER TABLE ONLY tbf_contatore
    ADD CONSTRAINT xpk_contatore UNIQUE (cd_polo, cd_cont, anno, key1);
--
-- Definition for index xpk_controllo_attivita (OID = 77845) : 
--
ALTER TABLE ONLY tbl_controllo_attivita
    ADD CONSTRAINT xpk_controllo_attivita UNIQUE (cd_bib, cd_polo, cod_attivita, cod_controllo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_controllo_iter (OID = 77847) : 
--
ALTER TABLE ONLY tbl_controllo_iter
    ADD CONSTRAINT xpk_controllo_iter UNIQUE (id_iter_servizio, cod_controllo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_disponibilita_precatalogati (OID = 77849) : 
--
ALTER TABLE ONLY tbl_disponibilita_precatalogati
    ADD CONSTRAINT xpk_disponibilita_precatalogati UNIQUE (cod_segn, cd_polo, cd_bib) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_dizionario_errori (OID = 77851) : 
--
ALTER TABLE ONLY tbf_dizionario_errori
    ADD CONSTRAINT xpk_dizionario_errori PRIMARY KEY (cd_lingua, cd_errore) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_documenti_lettori (OID = 77853) : 
--
ALTER TABLE ONLY tbl_documenti_lettori
    ADD CONSTRAINT xpk_documenti_lettori UNIQUE (tipo_doc_lett, cod_doc_lett, cd_polo, cd_bib) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_elementi_buono_ordine (OID = 77855) : 
--
ALTER TABLE ONLY tra_elementi_buono_ordine
    ADD CONSTRAINT xpk_elementi_buono_ordine UNIQUE (buono_ord, cod_tip_ord, anno_ord, cod_ord);
--
-- Definition for index xpk_esemplare_documento_lettore (OID = 77857) : 
--
ALTER TABLE ONLY tbl_esemplare_documento_lettore
    ADD CONSTRAINT xpk_esemplare_documento_lettore UNIQUE (prg_esemplare, id_documenti_lettore) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_fatture (OID = 77859) : 
--
ALTER TABLE ONLY tba_fatture
    ADD CONSTRAINT xpk_fatture UNIQUE (anno_fattura, progr_fattura, cd_polo, cd_bib);
--
-- Definition for index xpk_fornitori_offerte (OID = 77861) : 
--
ALTER TABLE ONLY tra_fornitori_offerte
    ADD CONSTRAINT xpk_fornitori_offerte UNIQUE (cod_fornitore, cod_rich_off);
--
-- Definition for index xpk_funzioni_denominazioni (OID = 77863) : 
--
ALTER TABLE ONLY trf_funzioni_denominazioni
    ADD CONSTRAINT xpk_funzioni_denominazioni UNIQUE (cd_lingua, cd_funzione) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_inventario (OID = 77865) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT xpk_inventario UNIQUE (cd_bib, cd_serie, cd_inven);
--
-- Definition for index xpk_iter (OID = 77867) : 
--
ALTER TABLE ONLY tbl_iter_servizio
    ADD CONSTRAINT xpk_iter UNIQUE (id_iter_servizio, progr_iter) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_materie (OID = 77869) : 
--
ALTER TABLE ONLY tbl_materie
    ADD CONSTRAINT xpk_materie UNIQUE (cod_mat, cd_polo, cd_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_messaggi (OID = 77871) : 
--
ALTER TABLE ONLY tra_messaggi
    ADD CONSTRAINT xpk_messaggi UNIQUE (cd_bib, cod_msg);
--
-- Definition for index xpk_modalita_erogazione (OID = 77873) : 
--
ALTER TABLE ONLY tbl_modalita_erogazione
    ADD CONSTRAINT xpk_modalita_erogazione UNIQUE (cd_bib, cod_erog, cd_polo);
--
-- Definition for index xpk_modalita_pagamento (OID = 77875) : 
--
ALTER TABLE ONLY tbl_modalita_pagamento
    ADD CONSTRAINT xpk_modalita_pagamento UNIQUE (cod_mod_pag, cd_polo, cd_bib) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_occupazioni (OID = 77877) : 
--
ALTER TABLE ONLY tbl_occupazioni
    ADD CONSTRAINT xpk_occupazioni UNIQUE (professione, occupazione, cd_polo, cd_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_offerte_fornitore (OID = 77879) : 
--
ALTER TABLE ONLY tba_offerte_fornitore
    ADD CONSTRAINT xpk_offerte_fornitore UNIQUE (bid_p, cd_polo, cd_bib);
--
-- Definition for index xpk_ordini_biblioteche (OID = 77881) : 
--
ALTER TABLE ONLY tra_ordini_biblioteche
    ADD CONSTRAINT xpk_ordini_biblioteche UNIQUE (cod_bib_ord, cod_tip_ord, anno_ord, cod_ord);
--
-- Definition for index xpk_possessori_possessori (OID = 77883) : 
--
ALTER TABLE ONLY trc_possessori_possessori
    ADD CONSTRAINT xpk_possessori_possessori PRIMARY KEY (pid_base, pid_coll) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_profiliabil_funzioni (OID = 77885) : 
--
ALTER TABLE ONLY trf_profilo_biblioteca
    ADD CONSTRAINT xpk_profiliabil_funzioni UNIQUE (cd_attivita, id_profilo_abilitazione_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_provenienza_inventario (OID = 77887) : 
--
ALTER TABLE ONLY tbc_provenienza_inventario
    ADD CONSTRAINT xpk_provenienza_inventario UNIQUE (cd_biblioteca, cd_proven);
--
-- Definition for index xpk_sale (OID = 77889) : 
--
ALTER TABLE ONLY tbl_sale
    ADD CONSTRAINT xpk_sale UNIQUE (cod_sala, cd_polo, cd_bib) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_serie_inventariale (OID = 77891) : 
--
ALTER TABLE ONLY tbc_serie_inventariale
    ADD CONSTRAINT xpk_serie_inventariale UNIQUE (cd_biblioteca, cd_serie);
--
-- Definition for index xpk_servizi (OID = 77893) : 
--
ALTER TABLE ONLY tbl_servizio
    ADD CONSTRAINT xpk_servizi UNIQUE (cod_servizio, id_tipo_servizio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_sez_acquis_bibliografiche (OID = 77895) : 
--
ALTER TABLE ONLY tba_sez_acquis_bibliografiche
    ADD CONSTRAINT xpk_sez_acquis_bibliografiche UNIQUE (cd_polo, cd_bib, cod_sezione);
--
-- Definition for index xpk_sez_acquisizione_fornitori (OID = 77897) : 
--
ALTER TABLE ONLY tra_sez_acquisizione_fornitori
    ADD CONSTRAINT xpk_sez_acquisizione_fornitori UNIQUE (cod_prac, cod_fornitore);
--
-- Definition for index xpk_sezione_collocazione (OID = 77899) : 
--
ALTER TABLE ONLY tbc_sezione_collocazione
    ADD CONSTRAINT xpk_sezione_collocazione UNIQUE (cd_biblioteca, cd_sez);
--
-- Definition for index xpk_sig_repertorio (OID = 77901) : 
--
ALTER TABLE ONLY tb_repertorio
    ADD CONSTRAINT xpk_sig_repertorio UNIQUE (cd_sig_repertorio) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_supporti_biblioteca (OID = 77903) : 
--
ALTER TABLE ONLY tbl_supporti_biblioteca
    ADD CONSTRAINT xpk_supporti_biblioteca UNIQUE (cd_bib, cod_supporto, cd_polo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_tbc_esemplare_titolo (OID = 77905) : 
--
ALTER TABLE ONLY tbc_esemplare_titolo
    ADD CONSTRAINT xpk_tbc_esemplare_titolo UNIQUE (cd_biblioteca, bid, cd_doc);
--
-- Definition for index xpk_tbf_intranet_range (OID = 77907) : 
--
ALTER TABLE ONLY tbf_intranet_range
    ADD CONSTRAINT xpk_tbf_intranet_range PRIMARY KEY (cd_bib, cd_polo, address);
--
-- Definition for index xpk_tipi_autorizzazioni (OID = 77909) : 
--
ALTER TABLE ONLY tbl_tipi_autorizzazioni
    ADD CONSTRAINT xpk_tipi_autorizzazioni UNIQUE (cod_tipo_aut, cd_polo, cd_bib) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_tipi_servizi (OID = 77911) : 
--
ALTER TABLE ONLY tbl_tipo_servizio
    ADD CONSTRAINT xpk_tipi_servizi UNIQUE (cod_tipo_serv, cd_polo, cd_bib) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_titoli_studio (OID = 77913) : 
--
ALTER TABLE ONLY tbl_specificita_titoli_studio
    ADD CONSTRAINT xpk_titoli_studio UNIQUE (tit_studio, specif_tit, cd_polo, cd_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_unique_tbf_profilo_abilitazione (OID = 77915) : 
--
ALTER TABLE ONLY tbf_profilo_abilitazione
    ADD CONSTRAINT xpk_unique_tbf_profilo_abilitazione UNIQUE (cd_prof, cd_biblioteca, cd_polo);
--
-- Definition for index xpk_utenti (OID = 77917) : 
--
ALTER TABLE ONLY tbl_utenti
    ADD CONSTRAINT xpk_utenti UNIQUE (cod_utente, cd_polo, cd_bib) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xpk_utenti_biblioteca (OID = 77919) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT xpk_utenti_biblioteca UNIQUE (id_utenti, cd_polo, cd_biblioteca) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index xuk1_termine_thesauro (OID = 77921) : 
--
ALTER TABLE ONLY tb_termine_thesauro
    ADD CONSTRAINT xuk1_termine_thesauro UNIQUE (cd_the, ky_termine_thesauro);
--
-- Definition for index xunique_posto_sala_num (OID = 77923) : 
--
ALTER TABLE ONLY tbl_posti_sala
    ADD CONSTRAINT xunique_posto_sala_num UNIQUE (id_sale, num_posto) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index fk_abilitazione (OID = 78010) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fk_abilitazione FOREIGN KEY (id_profilo_abilitazione) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fk_amministrazione (OID = 78015) : 
--
ALTER TABLE ONLY trf_utente_professionale_biblioteca
    ADD CONSTRAINT fk_amministrazione FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fk_amministrazione (OID = 78020) : 
--
ALTER TABLE ONLY trf_utente_professionale_polo
    ADD CONSTRAINT fk_amministrazione FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fk_anagrafe_utente_professionale (OID = 78025) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fk_anagrafe_utente_professionale FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fk_anagrafica_biblioteca (OID = 78030) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fk_anagrafica_biblioteca FOREIGN KEY (id_biblioteca) REFERENCES tbf_biblioteca(id_biblioteca);
--
-- Definition for index fk_attivita (OID = 78035) : 
--
ALTER TABLE ONLY trf_attivita_polo
    ADD CONSTRAINT fk_attivita FOREIGN KEY (cd_attivita) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fk_attivita (OID = 78040) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_attivita FOREIGN KEY (cd_attivita) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fk_attivita (OID = 78045) : 
--
ALTER TABLE ONLY trf_profilo_biblioteca
    ADD CONSTRAINT fk_attivita FOREIGN KEY (cd_attivita) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fk_attivita_polo (OID = 78050) : 
--
ALTER TABLE ONLY trf_gruppo_attivita_polo
    ADD CONSTRAINT fk_attivita_polo FOREIGN KEY (id_attivita_polo) REFERENCES trf_attivita_polo(id_attivita_polo);
--
-- Definition for index fk_attivita_sbnmarc (OID = 78055) : 
--
ALTER TABLE ONLY tbf_attivita
    ADD CONSTRAINT fk_attivita_sbnmarc FOREIGN KEY (id_attivita_sbnmarc) REFERENCES tbf_attivita_sbnmarc(id_attivita_sbnmarc);
--
-- Definition for index fk_autore (OID = 78060) : 
--
ALTER TABLE ONLY tr_aut_bib
    ADD CONSTRAINT fk_autore FOREIGN KEY (vid) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore (OID = 78065) : 
--
ALTER TABLE ONLY tr_aut_mar
    ADD CONSTRAINT fk_autore FOREIGN KEY (vid) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore (OID = 78070) : 
--
ALTER TABLE ONLY tr_per_int
    ADD CONSTRAINT fk_autore FOREIGN KEY (vid) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore (OID = 78075) : 
--
ALTER TABLE ONLY tr_rep_aut
    ADD CONSTRAINT fk_autore FOREIGN KEY (vid) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore (OID = 78080) : 
--
ALTER TABLE ONLY tr_tit_aut
    ADD CONSTRAINT fk_autore FOREIGN KEY (vid) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore_base (OID = 78085) : 
--
ALTER TABLE ONLY tr_aut_aut
    ADD CONSTRAINT fk_autore_base FOREIGN KEY (vid_base) REFERENCES tb_autore(vid);
--
-- Definition for index fk_autore_coll (OID = 78090) : 
--
ALTER TABLE ONLY tr_aut_aut
    ADD CONSTRAINT fk_autore_coll FOREIGN KEY (vid_coll) REFERENCES tb_autore(vid);
--
-- Definition for index fk_bib_polo_tbf_intranet_range (OID = 78095) : 
--
ALTER TABLE ONLY tbf_intranet_range
    ADD CONSTRAINT fk_bib_polo_tbf_intranet_range FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78100) : 
--
ALTER TABLE ONLY tbf_biblioteca_default
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78105) : 
--
ALTER TABLE ONLY tbf_contatore
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78110) : 
--
ALTER TABLE ONLY tbf_modelli_stampe
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78115) : 
--
ALTER TABLE ONLY tbf_profilo_abilitazione
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78120) : 
--
ALTER TABLE ONLY tbc_sezione_collocazione
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78125) : 
--
ALTER TABLE ONLY tbc_serie_inventariale
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78130) : 
--
ALTER TABLE ONLY tbc_provenienza_inventario
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78135) : 
--
ALTER TABLE ONLY tbc_esemplare_titolo
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78140) : 
--
ALTER TABLE ONLY tr_aut_bib
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78145) : 
--
ALTER TABLE ONLY tr_sistemi_classi_biblioteche
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78150) : 
--
ALTER TABLE ONLY tr_soggettari_biblioteche
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78155) : 
--
ALTER TABLE ONLY tr_thesauri_biblioteche
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78160) : 
--
ALTER TABLE ONLY tr_tit_bib
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca (OID = 78165) : 
--
ALTER TABLE ONLY tbl_modalita_erogazione
    ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_affiliata (OID = 78170) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_biblioteca_affiliata FOREIGN KEY (cd_bib_affiliata, cd_polo_bib_affiliata) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_cs (OID = 78175) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_biblioteca_cs FOREIGN KEY (cd_bib_centro_sistema, cd_polo_bib_centro_sistema) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_in_polo (OID = 78180) : 
--
ALTER TABLE ONLY tr_mar_bib
    ADD CONSTRAINT fk_biblioteca_in_polo FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_in_polo__tbl_controllo_attivita (OID = 78185) : 
--
ALTER TABLE ONLY tbl_controllo_attivita
    ADD CONSTRAINT fk_biblioteca_in_polo__tbl_controllo_attivita FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_in_polo__trl_relazione_servizi (OID = 78190) : 
--
ALTER TABLE ONLY trl_relazioni_servizi
    ADD CONSTRAINT fk_biblioteca_in_polo__trl_relazione_servizi FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_biblioteca_polo (OID = 78195) : 
--
ALTER TABLE ONLY trf_utente_professionale_biblioteca
    ADD CONSTRAINT fk_biblioteca_polo FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_bibliotecario (OID = 78200) : 
--
ALTER TABLE ONLY tbf_bibliotecario_default
    ADD CONSTRAINT fk_bibliotecario FOREIGN KEY (id_utente_professionale) REFERENCES tbf_bibliotecario(id_utente_professionale);
--
-- Definition for index fk_bilancio (OID = 78205) : 
--
ALTER TABLE ONLY tba_buono_ordine
    ADD CONSTRAINT fk_bilancio FOREIGN KEY (cod_mat, id_capitoli_bilanci) REFERENCES tbb_bilanci(cod_mat, id_capitoli_bilanci);
--
-- Definition for index fk_bilancio (OID = 78210) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT fk_bilancio FOREIGN KEY (tbb_bilancicod_mat, id_capitoli_bilanci) REFERENCES tbb_bilanci(cod_mat, id_capitoli_bilanci);
--
-- Definition for index fk_bilancio (OID = 78215) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT fk_bilancio FOREIGN KEY (cod_mat, id_capitoli_bilanci) REFERENCES tbb_bilanci(cod_mat, id_capitoli_bilanci);
--
-- Definition for index fk_cambio (OID = 78220) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT fk_cambio FOREIGN KEY (id_valuta) REFERENCES tba_cambi_ufficiali(id_valuta);
--
-- Definition for index fk_classe (OID = 78225) : 
--
ALTER TABLE ONLY tbc_collocazione
    ADD CONSTRAINT fk_classe FOREIGN KEY (cd_sistema, cd_edizione, classe) REFERENCES tb_classe(cd_sistema, cd_edizione, classe);
--
-- Definition for index fk_classe (OID = 78230) : 
--
ALTER TABLE ONLY tr_tit_cla
    ADD CONSTRAINT fk_classe FOREIGN KEY (cd_sistema, cd_edizione, classe) REFERENCES tb_classe(cd_sistema, cd_edizione, classe);
--
-- Definition for index fk_coda_jms (OID = 78235) : 
--
ALTER TABLE ONLY tbf_batch_servizi
    ADD CONSTRAINT fk_coda_jms FOREIGN KEY (id_coda_input) REFERENCES tbf_coda_jms(id_coda);
--
-- Definition for index fk_collocazione (OID = 78240) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT fk_collocazione FOREIGN KEY (key_loc) REFERENCES tbc_collocazione(key_loc);
--
-- Definition for index fk_config_default (OID = 78245) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT fk_config_default FOREIGN KEY (tbf_config_default__id_config) REFERENCES tbf_config_default(id_config);
--
-- Definition for index fk_default (OID = 78250) : 
--
ALTER TABLE ONLY tbf_biblioteca_default
    ADD CONSTRAINT fk_default FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fk_default (OID = 78255) : 
--
ALTER TABLE ONLY tbf_bibliotecario_default
    ADD CONSTRAINT fk_default FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fk_default (OID = 78260) : 
--
ALTER TABLE ONLY tbf_polo_default
    ADD CONSTRAINT fk_default FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fk_descrittore (OID = 78265) : 
--
ALTER TABLE ONLY tr_sog_des
    ADD CONSTRAINT fk_descrittore FOREIGN KEY (did) REFERENCES tb_descrittore(did);
--
-- Definition for index fk_descrittore_base (OID = 78270) : 
--
ALTER TABLE ONLY tr_des_des
    ADD CONSTRAINT fk_descrittore_base FOREIGN KEY (did_base) REFERENCES tb_descrittore(did);
--
-- Definition for index fk_descrittore_coll (OID = 78275) : 
--
ALTER TABLE ONLY tr_des_des
    ADD CONSTRAINT fk_descrittore_coll FOREIGN KEY (did_coll) REFERENCES tb_descrittore(did);
--
-- Definition for index fk_esemplare_titolo (OID = 78280) : 
--
ALTER TABLE ONLY tbc_collocazione
    ADD CONSTRAINT fk_esemplare_titolo FOREIGN KEY (cd_doc, bid_doc, cd_biblioteca_doc, cd_polo_doc) REFERENCES tbc_esemplare_titolo(cd_doc, bid, cd_biblioteca, cd_polo);
--
-- Definition for index fk_fornitore (OID = 78286) : 
--
ALTER TABLE ONLY tba_buono_ordine
    ADD CONSTRAINT fk_fornitore FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index fk_fornitore (OID = 78291) : 
--
ALTER TABLE ONLY tba_offerte_fornitore
    ADD CONSTRAINT fk_fornitore FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index fk_fornitore (OID = 78296) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT fk_fornitore FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index fk_fornitore (OID = 78301) : 
--
ALTER TABLE ONLY tba_fatture
    ADD CONSTRAINT fk_fornitore FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index fk_funzione (OID = 78306) : 
--
ALTER TABLE ONLY trf_funzioni_denominazioni
    ADD CONSTRAINT fk_funzione FOREIGN KEY (cd_funzione) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fk_grup_att_p_base (OID = 78311) : 
--
ALTER TABLE ONLY trf_gruppo_attivita
    ADD CONSTRAINT fk_grup_att_p_base FOREIGN KEY (id_gruppo_attivita_polo_base) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_grup_att_p_coll (OID = 78316) : 
--
ALTER TABLE ONLY trf_gruppo_attivita
    ADD CONSTRAINT fk_grup_att_p_coll FOREIGN KEY (id_gruppo_attivita_polo_coll) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_grup_att_polo (OID = 78321) : 
--
ALTER TABLE ONLY trf_gruppo_attivita_polo
    ADD CONSTRAINT fk_grup_att_polo FOREIGN KEY (id_gruppo_attivita_polo) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_gruppo_attivita (OID = 78326) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fk_gruppo_attivita FOREIGN KEY (id_gruppo_attivita_polo) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_gruppo_attivita (OID = 78331) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_biblioteca
    ADD CONSTRAINT fk_gruppo_attivita FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_gruppo_attivita (OID = 78336) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_bibliotecario
    ADD CONSTRAINT fk_gruppo_attivita FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_gruppo_attivita (OID = 78341) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT fk_gruppo_attivita FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_gruppo_default (OID = 78346) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT fk_gruppo_default FOREIGN KEY (tbf_gruppi_defaultid) REFERENCES tbf_gruppi_default(id);
--
-- Definition for index fk_inventario (OID = 78351) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fk_inventario FOREIGN KEY (cd_polo_inv, cod_bib_inv, cod_serie_inv, cod_inven_inv) REFERENCES tbc_inventario(cd_polo, cd_bib, cd_serie, cd_inven);
--
-- Definition for index fk_lingua (OID = 78356) : 
--
ALTER TABLE ONLY tbf_dizionario_errori
    ADD CONSTRAINT fk_lingua FOREIGN KEY (cd_lingua) REFERENCES tbf_lingua_supportata(cd_lingua);
--
-- Definition for index fk_lingua (OID = 78361) : 
--
ALTER TABLE ONLY trf_funzioni_denominazioni
    ADD CONSTRAINT fk_lingua FOREIGN KEY (cd_lingua) REFERENCES tbf_lingua_supportata(cd_lingua);
--
-- Definition for index fk_luogo (OID = 78366) : 
--
ALTER TABLE ONLY tr_tit_luo
    ADD CONSTRAINT fk_luogo FOREIGN KEY (lid) REFERENCES tb_luogo(lid);
--
-- Definition for index fk_luogo_base (OID = 78371) : 
--
ALTER TABLE ONLY tr_luo_luo
    ADD CONSTRAINT fk_luogo_base FOREIGN KEY (lid_base) REFERENCES tb_luogo(lid);
--
-- Definition for index fk_luogo_coll (OID = 78376) : 
--
ALTER TABLE ONLY tr_luo_luo
    ADD CONSTRAINT fk_luogo_coll FOREIGN KEY (lid_coll) REFERENCES tb_luogo(lid);
--
-- Definition for index fk_biblioteca 
--	
ALTER TABLE ONLY tb_loc_indice
	ADD CONSTRAINT fk_biblioteca FOREIGN KEY (cd_polo, cd_biblioteca) REFERENCES tbf_biblioteca_in_polo(cd_polo, cd_biblioteca);
--
-- Definition for index fk_titolo 
--	
ALTER TABLE tb_loc_indice 
	ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);	
--
-- Definition for index fk_marca (OID = 78381) : 
--
ALTER TABLE ONLY tb_parola
    ADD CONSTRAINT fk_marca FOREIGN KEY (mid) REFERENCES tb_marca(mid);
--
-- Definition for index fk_marca (OID = 78386) : 
--
ALTER TABLE ONLY tr_aut_mar
    ADD CONSTRAINT fk_marca FOREIGN KEY (mid) REFERENCES tb_marca(mid);
--
-- Definition for index fk_marca (OID = 78391) : 
--
ALTER TABLE ONLY tr_mar_bib
    ADD CONSTRAINT fk_marca FOREIGN KEY (mid) REFERENCES tb_marca(mid);
--
-- Definition for index fk_marca (OID = 78396) : 
--
ALTER TABLE ONLY tr_rep_mar
    ADD CONSTRAINT fk_marca FOREIGN KEY (mid) REFERENCES tb_marca(mid);
--
-- Definition for index fk_marca (OID = 78401) : 
--
ALTER TABLE ONLY tr_tit_mar
    ADD CONSTRAINT fk_marca FOREIGN KEY (mid) REFERENCES tb_marca(mid);
--
-- Definition for index fk_ordine (OID = 78406) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT fk_ordine FOREIGN KEY (id_ordine) REFERENCES tba_ordini(id_ordine);
--
-- Definition for index fk_parametro (OID = 78411) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fk_parametro FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_parametro (OID = 78416) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fk_parametro FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_parametro (OID = 78421) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_biblioteca
    ADD CONSTRAINT fk_parametro FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_parametro (OID = 78426) : 
--
ALTER TABLE ONLY tbf_par_mat
    ADD CONSTRAINT fk_parametro FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_parametro (OID = 78431) : 
--
ALTER TABLE ONLY tbf_par_sem
    ADD CONSTRAINT fk_parametro FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_parametro (OID = 78436) : 
--
ALTER TABLE ONLY tbf_par_auth
    ADD CONSTRAINT fk_parametro FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_paramtetro (OID = 78441) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_bibliotecario
    ADD CONSTRAINT fk_paramtetro FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_personaggio (OID = 78446) : 
--
ALTER TABLE ONLY tr_per_int
    ADD CONSTRAINT fk_personaggio FOREIGN KEY (id_personaggio) REFERENCES tb_personaggio(id_personaggio);
--
-- Definition for index fk_polo (OID = 78451) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fk_polo FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_polo (OID = 78456) : 
--
ALTER TABLE ONLY tbf_gruppo_attivita
    ADD CONSTRAINT fk_polo FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_polo (OID = 78461) : 
--
ALTER TABLE ONLY tbf_polo_default
    ADD CONSTRAINT fk_polo FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_polo (OID = 78466) : 
--
ALTER TABLE ONLY trf_attivita_polo
    ADD CONSTRAINT fk_polo FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_poss_prov_base (OID = 78471) : 
--
ALTER TABLE ONLY trc_possessori_possessori
    ADD CONSTRAINT fk_poss_prov_base FOREIGN KEY (pid_base) REFERENCES tbc_possessore_provenienza(pid);
--
-- Definition for index fk_poss_prov_coll (OID = 78476) : 
--
ALTER TABLE ONLY trc_possessori_possessori
    ADD CONSTRAINT fk_poss_prov_coll FOREIGN KEY (pid_coll) REFERENCES tbc_possessore_provenienza(pid);
--
-- Definition for index fk_possessore_provenienza (OID = 78481) : 
--
ALTER TABLE ONLY trc_poss_prov_inventari
    ADD CONSTRAINT fk_possessore_provenienza FOREIGN KEY (pid) REFERENCES tbc_possessore_provenienza(pid);
--
-- Definition for index fk_profil_base (OID = 78486) : 
--
ALTER TABLE ONLY trf_profilo_profilo
    ADD CONSTRAINT fk_profil_base FOREIGN KEY (id_profilo_abilitazione_base) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fk_profil_coll (OID = 78491) : 
--
ALTER TABLE ONLY trf_profilo_profilo
    ADD CONSTRAINT fk_profil_coll FOREIGN KEY (id_profilo_abilitazione_coll) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fk_profilo_abilitazione (OID = 78496) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fk_profilo_abilitazione FOREIGN KEY (id_profilo_abilitazione) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fk_profilo_abilitazione (OID = 78501) : 
--
ALTER TABLE ONLY trf_profilo_biblioteca
    ADD CONSTRAINT fk_profilo_abilitazione FOREIGN KEY (id_profilo_abilitazione) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fk_provenienza_inventario (OID = 78506) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT fk_provenienza_inventario FOREIGN KEY (cd_proven, cd_biblioteca_proven, cd_polo_proven) REFERENCES tbc_provenienza_inventario(cd_proven, cd_biblioteca, cd_polo);
--
-- Definition for index fk_repertorio (OID = 78511) : 
--
ALTER TABLE ONLY tr_rep_aut
    ADD CONSTRAINT fk_repertorio FOREIGN KEY (id_repertorio) REFERENCES tb_repertorio(id_repertorio);
--
-- Definition for index fk_repertorio (OID = 78516) : 
--
ALTER TABLE ONLY tr_rep_mar
    ADD CONSTRAINT fk_repertorio FOREIGN KEY (id_repertorio) REFERENCES tb_repertorio(id_repertorio);
--
-- Definition for index fk_repertorio (OID = 78521) : 
--
ALTER TABLE ONLY tr_rep_tit
    ADD CONSTRAINT fk_repertorio FOREIGN KEY (id_repertorio) REFERENCES tb_repertorio(id_repertorio);
--
-- Definition for index fk_sale (OID = 78526) : 
--
ALTER TABLE ONLY tbl_posti_sala
    ADD CONSTRAINT fk_sale FOREIGN KEY (id_sale) REFERENCES tbl_sale(id_sale);
--
-- Definition for index fk_serie_inventariale (OID = 78531) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT fk_serie_inventariale FOREIGN KEY (cd_serie, cd_bib, cd_polo) REFERENCES tbc_serie_inventariale(cd_serie, cd_biblioteca, cd_polo);
--
-- Definition for index fk_sezione_acquisizione_bib (OID = 78536) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT fk_sezione_acquisizione_bib FOREIGN KEY (id_sez_acquis_bibliografiche) REFERENCES tba_sez_acquis_bibliografiche(id_sez_acquis_bibliografiche);
--
-- Definition for index fk_sezione_collocazione (OID = 78541) : 
--
ALTER TABLE ONLY tbc_collocazione
    ADD CONSTRAINT fk_sezione_collocazione FOREIGN KEY (cd_sez, cd_biblioteca_sezione, cd_polo_sezione) REFERENCES tbc_sezione_collocazione(cd_sez, cd_biblioteca, cd_polo);
--
-- Definition for index fk_soggetto (OID = 78546) : 
--
ALTER TABLE ONLY tr_sog_des
    ADD CONSTRAINT fk_soggetto FOREIGN KEY (cid) REFERENCES tb_soggetto(cid);
--
-- Definition for index fk_soggetto (OID = 78551) : 
--
ALTER TABLE ONLY tr_tit_sog_bib
    ADD CONSTRAINT fk_soggetto FOREIGN KEY (cid) REFERENCES tb_soggetto(cid);
--
-- Definition for index fk_tba_righe_fatture__tba_fatture (OID = 78556) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT fk_tba_righe_fatture__tba_fatture FOREIGN KEY (id_fattura) REFERENCES tba_fatture(id_fattura);
--
-- Definition for index fk_tba_righe_fatture__tba_fatture_credito (OID = 78561) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT fk_tba_righe_fatture__tba_fatture_credito FOREIGN KEY (id_fattura_in_credito) REFERENCES tba_fatture(id_fattura);
--
-- Definition for index fk_tba_righe_fatture__tbf_biblioteca_in_polo (OID = 78566) : 
--
ALTER TABLE ONLY tba_righe_fatture
    ADD CONSTRAINT fk_tba_righe_fatture__tbf_biblioteca_in_polo FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_tbc_nota_inv__tbc_inventario (OID = 78571) : 
--
ALTER TABLE ONLY tbc_nota_inv
    ADD CONSTRAINT fk_tbc_nota_inv__tbc_inventario FOREIGN KEY (cd_polo, cd_bib, cd_serie, cd_inven) REFERENCES tbc_inventario(cd_polo, cd_bib, cd_serie, cd_inven);
--
-- Definition for index fk_tbf_biblioteca_default__tbf_biblioteca_in_polo (OID = 78576) : 
--
ALTER TABLE ONLY tbf_biblioteca_default
    ADD CONSTRAINT fk_tbf_biblioteca_default__tbf_biblioteca_in_polo FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_tbf_biblioteca_default__tbf_default (OID = 78581) : 
--
ALTER TABLE ONLY tbf_biblioteca_default
    ADD CONSTRAINT fk_tbf_biblioteca_default__tbf_default FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fk_tbf_biblioteca_in_polo__tbf_polo (OID = 78586) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fk_tbf_biblioteca_in_polo__tbf_polo FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_tbf_bibliotecario__tbf_bibliotecario_default (OID = 78591) : 
--
ALTER TABLE ONLY tbf_bibliotecario_default
    ADD CONSTRAINT fk_tbf_bibliotecario__tbf_bibliotecario_default FOREIGN KEY (id_utente_professionale) REFERENCES tbf_bibliotecario(id_utente_professionale);
--
-- Definition for index fk_tbf_default__tbf_bibliotecario_default (OID = 78596) : 
--
ALTER TABLE ONLY tbf_bibliotecario_default
    ADD CONSTRAINT fk_tbf_default__tbf_bibliotecario_default FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fk_tbf_default_inven_schede__tbf_biblioteca_in_polo (OID = 78601) : 
--
ALTER TABLE ONLY tbc_default_inven_schede
    ADD CONSTRAINT fk_tbf_default_inven_schede__tbf_biblioteca_in_polo FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_tbf_default_inven_schede__tbf_modelli_stampe (OID = 78606) : 
--
ALTER TABLE ONLY tbc_default_inven_schede
    ADD CONSTRAINT fk_tbf_default_inven_schede__tbf_modelli_stampe FOREIGN KEY (id_modello) REFERENCES tbf_modelli_stampe(id_modello);
--
-- Definition for index fk_tbf_gruppo_attivita (OID = 78611) : 
--
ALTER TABLE ONLY tbf_gruppo_attivita
    ADD CONSTRAINT fk_tbf_gruppo_attivita FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_tbf_modello_profilazione_biblioteca__tbf_gruppo_attivita (OID = 78616) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_biblioteca
    ADD CONSTRAINT fk_tbf_modello_profilazione_biblioteca__tbf_gruppo_attivita FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_tbf_modello_profilazione_biblioteca__tbf_parametro (OID = 78621) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_biblioteca
    ADD CONSTRAINT fk_tbf_modello_profilazione_biblioteca__tbf_parametro FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_tbf_modello_profilazione_bibliotecario__tbf_gruppo_attivita (OID = 78626) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_bibliotecario
    ADD CONSTRAINT fk_tbf_modello_profilazione_bibliotecario__tbf_gruppo_attivita FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_tbf_parametro (OID = 78636) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT fk_tbf_parametro FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_tbf_polo__tbf_parametro (OID = 78641) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT fk_tbf_polo__tbf_parametro FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fk_tbf_polo_default__tbf_polo (OID = 78646) : 
--
ALTER TABLE ONLY tbf_polo_default
    ADD CONSTRAINT fk_tbf_polo_default__tbf_polo FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_tbf_polo_tbf_gruppo_attivita (OID = 78651) : 
--
ALTER TABLE ONLY tbf_polo
    ADD CONSTRAINT fk_tbf_polo_tbf_gruppo_attivita FOREIGN KEY (id_gruppo_attivita) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fk_tbr_fornitori_biblioteche__tbf_biblioteca_in_polo (OID = 78656) : 
--
ALTER TABLE ONLY tbr_fornitori_biblioteche
    ADD CONSTRAINT fk_tbr_fornitori_biblioteche__tbf_biblioteca_in_polo FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_termine (OID = 78661) : 
--
ALTER TABLE ONLY trs_termini_titoli_biblioteche
    ADD CONSTRAINT fk_termine FOREIGN KEY (did) REFERENCES tb_termine_thesauro(did);
--
-- Definition for index fk_termine (OID = 78666) : 
--
ALTER TABLE ONLY tr_the_cla
    ADD CONSTRAINT fk_termine FOREIGN KEY (cd_the, did) REFERENCES tb_termine_thesauro(cd_the, did);
--
-- Definition for index fk_termine_base (OID = 78671) : 
--
ALTER TABLE ONLY tr_termini_termini
    ADD CONSTRAINT fk_termine_base FOREIGN KEY (did_base) REFERENCES tb_termine_thesauro(did);
--
-- Definition for index fk_termine_coll (OID = 78676) : 
--
ALTER TABLE ONLY tr_termini_termini
    ADD CONSTRAINT fk_termine_coll FOREIGN KEY (did_coll) REFERENCES tb_termine_thesauro(did);
--
-- Definition for index fk_thesauri_biblioteche (OID = 78681) : 
--
ALTER TABLE ONLY trs_termini_titoli_biblioteche
    ADD CONSTRAINT fk_thesauri_biblioteche FOREIGN KEY (cd_biblioteca, cd_polo, cd_the) REFERENCES tr_thesauri_biblioteche(cd_biblioteca, cd_polo, cd_the) DEFERRABLE INITIALLY DEFERRED;
--
-- Definition for index fk_titolo (OID = 78686) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78691) : 
--
ALTER TABLE ONLY tbc_esemplare_titolo
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78696) : 
--
ALTER TABLE ONLY tbc_collocazione
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78701) : 
--
ALTER TABLE ONLY tb_nota
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78706) : 
--
ALTER TABLE ONLY tr_tit_cla
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78711) : 
--
ALTER TABLE ONLY tb_abstract
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78716) : 
--
ALTER TABLE ONLY tb_arte_tridimens
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78721) : 
--
ALTER TABLE ONLY tb_audiovideo
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78726) : 
--
ALTER TABLE ONLY tb_cartografia
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78731) : 
--
ALTER TABLE ONLY tb_composizione
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78736) : 
--
ALTER TABLE ONLY tb_disco_sonoro
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78741) : 
--
ALTER TABLE ONLY tb_grafica
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78746) : 
--
ALTER TABLE ONLY tb_impronta
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78751) : 
--
ALTER TABLE ONLY tb_incipit
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78756) : 
--
ALTER TABLE ONLY tb_microforma
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78761) : 
--
ALTER TABLE ONLY tb_musica
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78766) : 
--
ALTER TABLE ONLY tb_numero_std
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78771) : 
--
ALTER TABLE ONLY tb_personaggio
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78776) : 
--
ALTER TABLE ONLY tb_rappresent
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78781) : 
--
ALTER TABLE ONLY tb_risorsa_elettr
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78786) : 
--
ALTER TABLE ONLY tr_rep_tit
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78791) : 
--
ALTER TABLE ONLY tr_tit_aut
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78796) : 
--
ALTER TABLE ONLY tr_tit_bib
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78801) : 
--
ALTER TABLE ONLY tr_tit_mar
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78806) : 
--
ALTER TABLE ONLY tr_tit_sog_bib
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78811) : 
--
ALTER TABLE ONLY trs_termini_titoli_biblioteche
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo (OID = 78816) : 
--
ALTER TABLE ONLY tr_tit_luo
    ADD CONSTRAINT fk_titolo FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo_base (OID = 78821) : 
--
ALTER TABLE ONLY tr_tit_tit
    ADD CONSTRAINT fk_titolo_base FOREIGN KEY (bid_base) REFERENCES tb_titolo(bid);
--
-- Definition for index fk_titolo_coll (OID = 78826) : 
--
ALTER TABLE ONLY tr_tit_tit
    ADD CONSTRAINT fk_titolo_coll FOREIGN KEY (bid_coll) REFERENCES tb_titolo(bid);
--
-- Definition for index tr_editore_titolo_pk (OID = 257762) : 
--
ALTER TABLE ONLY tr_editore_titolo
    ADD CONSTRAINT tr_editore_titolo_pk PRIMARY KEY (cod_fornitore, bid);
--
-- Definition for index cod_fornitore_fk (OID = 257764) : 
--
ALTER TABLE ONLY tr_editore_titolo
    ADD CONSTRAINT cod_fornitore_fk FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index tb_titolo_fk (OID = 257769) : 
--
ALTER TABLE ONLY tr_editore_titolo
    ADD CONSTRAINT tb_titolo_fk FOREIGN KEY (bid) REFERENCES tb_titolo(bid);	
--
-- Definition for index pk_editore_num_std (OID = 257752) : 
--
ALTER TABLE ONLY tbr_editore_num_std
    ADD CONSTRAINT pk_editore_num_std PRIMARY KEY (cod_fornitore, numero_std);
--
-- Definition for index fk_fornitore (OID = 257754) : 
--
ALTER TABLE ONLY tbr_editore_num_std
    ADD CONSTRAINT fk_fornitore FOREIGN KEY (cod_fornitore) REFERENCES tbr_fornitori(cod_fornitore);
--
-- Definition for index fk_trf_attivita_affiliate__tbf_attivita (OID = 78831) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_trf_attivita_affiliate__tbf_attivita FOREIGN KEY (cd_attivita) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fk_trf_attivita_affiliate__tbf_bilbioteca_in_polo (OID = 78836) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_trf_attivita_affiliate__tbf_bilbioteca_in_polo FOREIGN KEY (cd_bib_affiliata, cd_polo_bib_affiliata) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_trf_attivita_affiliate__tbf_bilbioteca_in_polo_cs (OID = 78841) : 
--
ALTER TABLE ONLY trf_attivita_affiliate
    ADD CONSTRAINT fk_trf_attivita_affiliate__tbf_bilbioteca_in_polo_cs FOREIGN KEY (cd_bib_centro_sistema, cd_polo_bib_centro_sistema) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fk_trf_attivita_polo__tbf_polo (OID = 78846) : 
--
ALTER TABLE ONLY trf_attivita_polo
    ADD CONSTRAINT fk_trf_attivita_polo__tbf_polo FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_trf_utente_professionale_polo__tbf_polo (OID = 78851) : 
--
ALTER TABLE ONLY trf_utente_professionale_polo
    ADD CONSTRAINT fk_trf_utente_professionale_polo__tbf_polo FOREIGN KEY (cd_polo) REFERENCES tbf_polo(cd_polo);
--
-- Definition for index fk_ts_note_proposta (OID = 78861) : 
--
ALTER TABLE ONLY ts_note_proposta
    ADD CONSTRAINT fk_ts_note_proposta FOREIGN KEY (id_proposta) REFERENCES ts_proposta_marc(id_proposta);
--
-- Definition for index fk_utente (OID = 78866) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fk_utente FOREIGN KEY (id_utenti_biblioteca) REFERENCES trl_utenti_biblioteca(id_utenti_biblioteca);
--
-- Definition for index fk_utente_professionale (OID = 78871) : 
--
ALTER TABLE ONLY tbf_utenti_professionali_web
    ADD CONSTRAINT fk_utente_professionale FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fktba_buono_611729 (OID = 78876) : 
--
ALTER TABLE ONLY tba_buono_ordine
    ADD CONSTRAINT fktba_buono_611729 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_cambi_245267 (OID = 78881) : 
--
ALTER TABLE ONLY tba_cambi_ufficiali
    ADD CONSTRAINT fktba_cambi_245267 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_fattur644716 (OID = 78886) : 
--
ALTER TABLE ONLY tba_fatture
    ADD CONSTRAINT fktba_fattur644716 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_offert971110 (OID = 78891) : 
--
ALTER TABLE ONLY tba_offerte_fornitore
    ADD CONSTRAINT fktba_offert971110 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_ordini880960 (OID = 78896) : 
--
ALTER TABLE ONLY tba_ordini
    ADD CONSTRAINT fktba_ordini880960 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_parame385714 (OID = 78901) : 
--
ALTER TABLE ONLY tba_parametri_buono_ordine
    ADD CONSTRAINT fktba_parame385714 FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_profil407639 (OID = 78906) : 
--
ALTER TABLE ONLY tba_profili_acquisto
    ADD CONSTRAINT fktba_profil407639 FOREIGN KEY (id_sez_acquis_bibliografiche) REFERENCES tba_sez_acquis_bibliografiche(id_sez_acquis_bibliografiche);
--
-- Definition for index fktba_richie478408 (OID = 78911) : 
--
ALTER TABLE ONLY tba_richieste_offerta
    ADD CONSTRAINT fktba_richie478408 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_sez_ac186160 (OID = 78916) : 
--
ALTER TABLE ONLY tba_sez_acquis_bibliografiche
    ADD CONSTRAINT fktba_sez_ac186160 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_sugger143075 (OID = 78921) : 
--
ALTER TABLE ONLY tba_suggerimenti_bibliografici
    ADD CONSTRAINT fktba_sugger143075 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktba_sugger779479 (OID = 78926) : 
--
ALTER TABLE ONLY tba_suggerimenti_bibliografici
    ADD CONSTRAINT fktba_sugger779479 FOREIGN KEY (id_sez_acquis_bibliografiche) REFERENCES tba_sez_acquis_bibliografiche(id_sez_acquis_bibliografiche);
--
-- Definition for index fktbb_bilanc19911 (OID = 78931) : 
--
ALTER TABLE ONLY tbb_bilanci
    ADD CONSTRAINT fktbb_bilanc19911 FOREIGN KEY (id_capitoli_bilanci) REFERENCES tbb_capitoli_bilanci(id_capitoli_bilanci);
--
-- Definition for index fktbb_bilanc588285 (OID = 78936) : 
--
ALTER TABLE ONLY tbb_bilanci
    ADD CONSTRAINT fktbb_bilanc588285 FOREIGN KEY (id_buono_ordine) REFERENCES tba_buono_ordine(id_buono_ordine);
--
-- Definition for index fktbb_capito423841 (OID = 78941) : 
--
ALTER TABLE ONLY tbb_capitoli_bilanci
    ADD CONSTRAINT fktbb_capito423841 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbf_attivi513525 (OID = 78946) : 
--
ALTER TABLE ONLY tbf_attivita
    ADD CONSTRAINT fktbf_attivi513525 FOREIGN KEY (id_attivita_sbnmarc) REFERENCES tbf_attivita_sbnmarc(id_attivita_sbnmarc);
--
-- Definition for index fktbf_attivi528588 (OID = 78951) : 
--
ALTER TABLE ONLY tbf_attivita_sbnmarc
    ADD CONSTRAINT fktbf_attivi528588 FOREIGN KEY (id_modulo) REFERENCES tbf_moduli_funzionali(id_modulo);
--
-- Definition for index fktbf_batch_906013 (OID = 78956) : 
--
ALTER TABLE ONLY tbf_batch_servizi
    ADD CONSTRAINT fktbf_batch_906013 FOREIGN KEY (id_coda_input) REFERENCES tbf_coda_jms(id_coda);
--
-- Definition for index fktbf_biblio257696 (OID = 78961) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fktbf_biblio257696 FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fktbf_biblio263412 (OID = 78966) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fktbf_biblio263412 FOREIGN KEY (id_gruppo_attivita_polo) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fktbf_biblio282801 (OID = 78971) : 
--
ALTER TABLE ONLY tbf_biblioteca_in_polo
    ADD CONSTRAINT fktbf_biblio282801 FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index fktbf_biblio662399 (OID = 78976) : 
--
ALTER TABLE ONLY tbf_bibliotecario
    ADD CONSTRAINT fktbf_biblio662399 FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fktbf_defaul226174 (OID = 78981) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT fktbf_defaul226174 FOREIGN KEY (tbf_config_default__id_config) REFERENCES tbf_config_default(id_config);
--
-- Definition for index fktbf_defaul612848 (OID = 78986) : 
--
ALTER TABLE ONLY tbf_default
    ADD CONSTRAINT fktbf_defaul612848 FOREIGN KEY (tbf_gruppi_defaultid) REFERENCES tbf_gruppi_default(id);
--
-- Definition for index fktbf_polo_d832889 (OID = 78991) : 
--
ALTER TABLE ONLY tbf_polo_default
    ADD CONSTRAINT fktbf_polo_d832889 FOREIGN KEY (id_default) REFERENCES tbf_default(id_default);
--
-- Definition for index fktbf_utenti778235 (OID = 78996) : 
--
ALTER TABLE ONLY tbf_utenti_professionali_web
    ADD CONSTRAINT fktbf_utenti778235 FOREIGN KEY (id_utente_professionale) REFERENCES tbf_anagrafe_utenti_professionali(id_utente_professionale);
--
-- Definition for index fktbl_calend852675 (OID = 79001) : 
--
ALTER TABLE ONLY tbl_calendario_servizi
    ADD CONSTRAINT fktbl_calend852675 FOREIGN KEY (id_tipo_servizio) REFERENCES tbl_tipo_servizio(id_tipo_servizio);
--
-- Definition for index fktbl_contro119321 (OID = 79006) : 
--
ALTER TABLE ONLY tbl_controllo_iter
    ADD CONSTRAINT fktbl_contro119321 FOREIGN KEY (id_iter_servizio) REFERENCES tbl_iter_servizio(id_iter_servizio);
--
-- Definition for index fktbl_dispon609206 (OID = 79011) : 
--
ALTER TABLE ONLY tbl_disponibilita_precatalogati
    ADD CONSTRAINT fktbl_dispon609206 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_docume207640 (OID = 79016) : 
--
ALTER TABLE ONLY tbl_documenti_lettori
    ADD CONSTRAINT fktbl_docume207640 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_docume481043 (OID = 79021) : 
--
ALTER TABLE ONLY tbl_documenti_lettori
    ADD CONSTRAINT fktbl_docume481043 FOREIGN KEY (id_utenti) REFERENCES tbl_utenti(id_utenti);
--
-- Definition for index fktbl_esempl557991 (OID = 79026) : 
--
ALTER TABLE ONLY tbl_esemplare_documento_lettore
    ADD CONSTRAINT fktbl_esempl557991 FOREIGN KEY (id_documenti_lettore) REFERENCES tbl_documenti_lettori(id_documenti_lettore);
--
-- Definition for index fktbl_iter_s982301 (OID = 79031) : 
--
ALTER TABLE ONLY tbl_iter_servizio
    ADD CONSTRAINT fktbl_iter_s982301 FOREIGN KEY (id_tipo_servizio) REFERENCES tbl_tipo_servizio(id_tipo_servizio);
--
-- Definition for index fktbl_materi322980 (OID = 79036) : 
--
ALTER TABLE ONLY tbl_materie
    ADD CONSTRAINT fktbl_materi322980 FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_messag757532 (OID = 79041) : 
--
ALTER TABLE ONLY tbl_messaggio
    ADD CONSTRAINT fktbl_messag757532 FOREIGN KEY (cod_rich_serv_richiesta) REFERENCES tbl_richiesta_servizio(cod_rich_serv);
--
-- Definition for index fktbl_modali927831 (OID = 79046) : 
--
ALTER TABLE ONLY tbl_modalita_pagamento
    ADD CONSTRAINT fktbl_modali927831 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_occupa87068 (OID = 79051) : 
--
ALTER TABLE ONLY tbl_occupazioni
    ADD CONSTRAINT fktbl_occupa87068 FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_parame979709 (OID = 79056) : 
--
ALTER TABLE ONLY tbl_parametri_biblioteca
    ADD CONSTRAINT fktbl_parame979709 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_penali472775 (OID = 79061) : 
--
ALTER TABLE ONLY tbl_penalita_servizio
    ADD CONSTRAINT fktbl_penali472775 FOREIGN KEY (id_servizio) REFERENCES tbl_servizio(id_servizio);
--
-- Definition for index fktbl_richie37563 (OID = 79066) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie37563 FOREIGN KEY (id_modalita_pagamento) REFERENCES tbl_modalita_pagamento(id_modalita_pagamento);
--
-- Definition for index fktbl_richie406785 (OID = 79071) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie406785 FOREIGN KEY (id_supporti_biblioteca) REFERENCES tbl_supporti_biblioteca(id_supporti_biblioteca);
--
-- Definition for index fktbl_richie429472 (OID = 79076) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie429472 FOREIGN KEY (id_esemplare_documenti_lettore) REFERENCES tbl_esemplare_documento_lettore(id_esemplare);
--
-- Definition for index fktbl_richie465824 (OID = 79081) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie465824 FOREIGN KEY (id_iter_servizio) REFERENCES tbl_iter_servizio(id_iter_servizio);
--
-- Definition for index fktbl_richie852803 (OID = 79086) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie852803 FOREIGN KEY (id_servizio) REFERENCES tbl_servizio(id_servizio);
--
-- Definition for index fktbl_richie949257 (OID = 79091) : 
--
ALTER TABLE ONLY tbl_richiesta_servizio
    ADD CONSTRAINT fktbl_richie949257 FOREIGN KEY (id_documenti_lettore) REFERENCES tbl_documenti_lettori(id_documenti_lettore);
--
-- Definition for index fktbl_sale66744 (OID = 79096) : 
--
ALTER TABLE ONLY tbl_sale
    ADD CONSTRAINT fktbl_sale66744 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_serviz725690 (OID = 79101) : 
--
ALTER TABLE ONLY tbl_servizio
    ADD CONSTRAINT fktbl_serviz725690 FOREIGN KEY (id_tipo_servizio) REFERENCES tbl_tipo_servizio(id_tipo_servizio);
--
-- Definition for index fktbl_serviz805283 (OID = 79106) : 
--
ALTER TABLE ONLY tbl_servizio_web_dati_richiesti
    ADD CONSTRAINT fktbl_serviz805283 FOREIGN KEY (id_tipo_servizio) REFERENCES tbl_tipo_servizio(id_tipo_servizio);
--
-- Definition for index fktbl_sollec949869 (OID = 79111) : 
--
ALTER TABLE ONLY tbl_solleciti
    ADD CONSTRAINT fktbl_sollec949869 FOREIGN KEY (cod_rich_serv) REFERENCES tbl_richiesta_servizio(cod_rich_serv);
--
-- Definition for index fktbl_specif906127 (OID = 79116) : 
--
ALTER TABLE ONLY tbl_specificita_titoli_studio
    ADD CONSTRAINT fktbl_specif906127 FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_suppor421679 (OID = 79121) : 
--
ALTER TABLE ONLY tbl_supporti_biblioteca
    ADD CONSTRAINT fktbl_suppor421679 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_tipi_a205716 (OID = 79126) : 
--
ALTER TABLE ONLY tbl_tipi_autorizzazioni
    ADD CONSTRAINT fktbl_tipi_a205716 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_tipo_s781992 (OID = 79131) : 
--
ALTER TABLE ONLY tbl_tipo_servizio
    ADD CONSTRAINT fktbl_tipo_s781992 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktbl_utenti822434 (OID = 79136) : 
--
ALTER TABLE ONLY tbl_utenti
    ADD CONSTRAINT fktbl_utenti822434 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktra_elemen653206 (OID = 79141) : 
--
ALTER TABLE ONLY tra_elementi_buono_ordine
    ADD CONSTRAINT fktra_elemen653206 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktra_fornit439052 (OID = 79146) : 
--
ALTER TABLE ONLY tra_fornitori_offerte
    ADD CONSTRAINT fktra_fornit439052 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktra_ordine161951 (OID = 79151) : 
--
ALTER TABLE ONLY tra_ordine_inventari
    ADD CONSTRAINT fktra_ordine161951 FOREIGN KEY (id_ordine) REFERENCES tba_ordini(id_ordine);
--
-- Definition for index fktra_ordine458689 (OID = 79156) : 
--
ALTER TABLE ONLY tra_ordine_inventari
    ADD CONSTRAINT fktra_ordine458689 FOREIGN KEY (cd_polo, cd_bib, cd_serie, cd_inven) REFERENCES tbc_inventario(cd_polo, cd_bib, cd_serie, cd_inven);
--
-- Definition for index fktra_ordini871255 (OID = 79161) : 
--
ALTER TABLE ONLY tra_ordini_biblioteche
    ADD CONSTRAINT fktra_ordini871255 FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktra_sez_ac542862 (OID = 79166) : 
--
ALTER TABLE ONLY tra_sez_acquisizione_fornitori
    ADD CONSTRAINT fktra_sez_ac542862 FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktrc_format930264 (OID = 79171) : 
--
ALTER TABLE ONLY trc_formati_sezioni
    ADD CONSTRAINT fktrc_format930264 FOREIGN KEY (cd_sezione, cd_bib_sezione, cd_polo_sezione) REFERENCES tbc_sezione_collocazione(cd_sez, cd_biblioteca, cd_polo);
--
-- Definition for index fktrc_poss_p572114 (OID = 79176) : 
--
ALTER TABLE ONLY trc_poss_prov_inventari
    ADD CONSTRAINT fktrc_poss_p572114 FOREIGN KEY (cd_polo, cd_biblioteca, cd_serie, cd_inven) REFERENCES tbc_inventario(cd_polo, cd_bib, cd_serie, cd_inven);
--
-- Definition for index fktrf_gruppo396996 (OID = 79181) : 
--
ALTER TABLE ONLY trf_gruppo_attivita_polo
    ADD CONSTRAINT fktrf_gruppo396996 FOREIGN KEY (id_gruppo_attivita_polo) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fktrf_gruppo699658 (OID = 79186) : 
--
ALTER TABLE ONLY trf_gruppo_attivita
    ADD CONSTRAINT fktrf_gruppo699658 FOREIGN KEY (id_gruppo_attivita_polo_coll) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fktrf_gruppo71976 (OID = 79191) : 
--
ALTER TABLE ONLY trf_gruppo_attivita_polo
    ADD CONSTRAINT fktrf_gruppo71976 FOREIGN KEY (id_attivita_polo) REFERENCES trf_attivita_polo(id_attivita_polo);
--
-- Definition for index fktrf_gruppo742693 (OID = 79196) : 
--
ALTER TABLE ONLY trf_gruppo_attivita
    ADD CONSTRAINT fktrf_gruppo742693 FOREIGN KEY (id_gruppo_attivita_polo_base) REFERENCES tbf_gruppo_attivita(id_gruppo_attivita_polo);
--
-- Definition for index fktrf_profil391503 (OID = 79201) : 
--
ALTER TABLE ONLY trf_profilo_biblioteca
    ADD CONSTRAINT fktrf_profil391503 FOREIGN KEY (cd_attivita) REFERENCES tbf_attivita(cd_attivita);
--
-- Definition for index fktrf_profil634088 (OID = 79206) : 
--
ALTER TABLE ONLY trf_profilo_profilo
    ADD CONSTRAINT fktrf_profil634088 FOREIGN KEY (id_profilo_abilitazione_coll) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fktrf_profil677123 (OID = 79211) : 
--
ALTER TABLE ONLY trf_profilo_profilo
    ADD CONSTRAINT fktrf_profil677123 FOREIGN KEY (id_profilo_abilitazione_base) REFERENCES tbf_profilo_abilitazione(id_profilo_abilitazione);
--
-- Definition for index fktrl_attivi227853 (OID = 79216) : 
--
ALTER TABLE ONLY trl_attivita_bibliotecario
    ADD CONSTRAINT fktrl_attivi227853 FOREIGN KEY (id_iter_servizio) REFERENCES tbl_iter_servizio(id_iter_servizio);
--
-- Definition for index fktrl_attivi318007 (OID = 79221) : 
--
ALTER TABLE ONLY trl_attivita_bibliotecario
    ADD CONSTRAINT fktrl_attivi318007 FOREIGN KEY (id_bibliotecario) REFERENCES tbf_bibliotecario(id_utente_professionale);
--
-- Definition for index fktrl_autori405945 (OID = 79231) : 
--
ALTER TABLE ONLY trl_autorizzazioni_servizi
    ADD CONSTRAINT fktrl_autori405945 FOREIGN KEY (id_servizio) REFERENCES tbl_servizio(id_servizio);
--
-- Definition for index fktrl_materi231212 (OID = 79246) : 
--
ALTER TABLE ONLY trl_materie_utenti
    ADD CONSTRAINT fktrl_materi231212 FOREIGN KEY (id_utenti) REFERENCES tbl_utenti(id_utenti);
--
-- Definition for index fktrl_materi669672 (OID = 79251) : 
--
ALTER TABLE ONLY trl_materie_utenti
    ADD CONSTRAINT fktrl_materi669672 FOREIGN KEY (id_materia) REFERENCES tbl_materie(id_materia);
--
-- Definition for index fktrl_posti_538726 (OID = 79256) : 
--
ALTER TABLE ONLY trl_posti_sala_utenti_biblioteca
    ADD CONSTRAINT fktrl_posti_538726 FOREIGN KEY (id_utenti_biblioteca) REFERENCES trl_utenti_biblioteca(id_utenti_biblioteca);
--
-- Definition for index fktrl_posti_810844 (OID = 79261) : 
--
ALTER TABLE ONLY trl_posti_sala_utenti_biblioteca
    ADD CONSTRAINT fktrl_posti_810844 FOREIGN KEY (id_posti_sala) REFERENCES tbl_posti_sala(id_posti_sala);
--
-- Definition for index fktrl_tariff204281 (OID = 79266) : 
--
ALTER TABLE ONLY trl_supporti_modalita_erogazione
    ADD CONSTRAINT fktrl_tariff204281 FOREIGN KEY (id_supporti_biblioteca) REFERENCES tbl_supporti_biblioteca(id_supporti_biblioteca);
--
-- Definition for index fktrl_tariff209538 (OID = 79271) : 
--
ALTER TABLE ONLY trl_tariffe_modalita_erogazione
    ADD CONSTRAINT fktrl_tariff209538 FOREIGN KEY (id_tipo_servizio) REFERENCES tbl_tipo_servizio(id_tipo_servizio);
--
-- Definition for index fktrl_utenti295593 (OID = 79276) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT fktrl_utenti295593 FOREIGN KEY (cd_biblioteca, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index fktrl_utenti668699 (OID = 79281) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT fktrl_utenti668699 FOREIGN KEY (id_utenti) REFERENCES tbl_utenti(id_utenti);
--
-- Definition for index fktrl_utenti927209 (OID = 79286) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT fktrl_utenti927209 FOREIGN KEY (id_specificita_titoli_studio) REFERENCES tbl_specificita_titoli_studio(id_specificita_titoli_studio);
--
-- Definition for index fktrl_utenti935080 (OID = 79291) : 
--
ALTER TABLE ONLY trl_utenti_biblioteca
    ADD CONSTRAINT fktrl_utenti935080 FOREIGN KEY (id_occupazioni) REFERENCES tbl_occupazioni(id_occupazioni);
--
-- Definition for index tbc_inventario_id_bib_scar (OID = 79296) : 
--
ALTER TABLE ONLY tbc_inventario
    ADD CONSTRAINT tbc_inventario_id_bib_scar FOREIGN KEY (id_bib_scar) REFERENCES tbf_biblioteca(id_biblioteca);
--
-- Definition for index tbf_modelli_stampe_fk (OID = 79301) : 
--
ALTER TABLE ONLY tbf_modelli_stampe
    ADD CONSTRAINT tbf_modelli_stampe_fk FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
--
-- Definition for index tbp_esemplare_fascicolo_fascicolo_fk (OID = 79306) : 
--
ALTER TABLE ONLY tbp_esemplare_fascicolo
    ADD CONSTRAINT tbp_esemplare_fascicolo_fascicolo_fk FOREIGN KEY (bid, fid) REFERENCES tbp_fascicolo(bid, fid) ON UPDATE CASCADE;
--
-- Definition for index tbp_esemplare_fascicolo_inventario_fk (OID = 79311) : 
--
ALTER TABLE ONLY tbp_esemplare_fascicolo
    ADD CONSTRAINT tbp_esemplare_fascicolo_inventario_fk FOREIGN KEY (cd_bib_inv, cd_serie, cd_inven) REFERENCES tbc_inventario(cd_bib, cd_serie, cd_inven);
--
-- Definition for index tbp_esemplare_fascicolo_ordine_fk (OID = 79316) : 
--
ALTER TABLE ONLY tbp_esemplare_fascicolo
    ADD CONSTRAINT tbp_esemplare_fascicolo_ordine_fk FOREIGN KEY (id_ordine) REFERENCES tba_ordini(id_ordine);
--
-- Definition for index tbp_fascicolo_titolo_fk (OID = 79321) : 
--
ALTER TABLE ONLY tbp_fascicolo
    ADD CONSTRAINT tbp_fascicolo_titolo_fk FOREIGN KEY (bid) REFERENCES tb_titolo(bid);
--
-- Definition for index tr_sogp_sogi_fk (OID = 79326) : 
--
ALTER TABLE ONLY tr_sogp_sogi
    ADD CONSTRAINT tr_sogp_sogi_fk FOREIGN KEY (cid_p) REFERENCES tb_soggetto(cid);
--
-- Definition for index import1_pkey (OID = 186479) : 
--
ALTER TABLE ONLY import1
    ADD CONSTRAINT import1_pkey PRIMARY KEY (id);
--
-- Definition for index tb_report_indice_pkey (OID = 186511) : 
--
ALTER TABLE ONLY tb_report_indice
    ADD CONSTRAINT tb_report_indice_pkey PRIMARY KEY (id);
--
-- Definition for index tb_report_indice_id_locali_pkey (OID = 186521) : 
--
ALTER TABLE ONLY tb_report_indice_id_locali
    ADD CONSTRAINT tb_report_indice_id_locali_pkey PRIMARY KEY (id);
--
-- Definition for index tb_report_indice_id_locali_fk (OID = 186523) : 
--
ALTER TABLE ONLY tb_report_indice_id_locali
    ADD CONSTRAINT tb_report_indice_id_locali_fk FOREIGN KEY (id_lista) REFERENCES tb_report_indice(id) ON UPDATE CASCADE;
--
-- Definition for index tb_report_indice_id_arrivo_pkey (OID = 186537) : 
--
ALTER TABLE ONLY tb_report_indice_id_arrivo
    ADD CONSTRAINT tb_report_indice_id_arrivo_pkey PRIMARY KEY (id);
--
-- Definition for index tb_report_indice_id_arrivo_fk (OID = 186539) : 
--
ALTER TABLE ONLY tb_report_indice_id_arrivo
    ADD CONSTRAINT tb_report_indice_id_arrivo_fk FOREIGN KEY (id_lista_ogg_loc) REFERENCES tb_report_indice_id_locali(id) ON UPDATE CASCADE DEFERRABLE INITIALLY DEFERRED;
--
-- Definition for index tbp_modello_previsionale_pkey (OID = 186554) : 
--
ALTER TABLE ONLY tbp_modello_previsionale
    ADD CONSTRAINT tbp_modello_previsionale_pkey PRIMARY KEY (id_modello);
--
-- Definition for index pk_tr_thes_cla (OID = 188364) : 
--
ALTER TABLE ONLY tr_the_cla
    ADD CONSTRAINT pk_tr_thes_cla PRIMARY KEY (cd_the, did, cd_sistema, cd_edizione, classe);
--
-- Definition for index tbp_esemplare_fascicolo_pkey (OID = 188368) : 
--
ALTER TABLE ONLY tbp_esemplare_fascicolo
    ADD CONSTRAINT tbp_esemplare_fascicolo_pkey PRIMARY KEY (id_ese_fascicolo) USING INDEX TABLESPACE tbs_indici_ese;
--
-- Definition for index fktrl_autoriz_servizi (OID = 189713) : 
--
ALTER TABLE ONLY trl_autorizzazioni_servizi
    ADD CONSTRAINT fktrl_autoriz_servizi FOREIGN KEY (id_tipi_autorizzazione) REFERENCES tbl_tipi_autorizzazioni(id_tipi_autorizzazione);
--
-- Definition for index fktrl_diritti_servizio (OID = 189719) : 
--
ALTER TABLE ONLY trl_diritti_utente
    ADD CONSTRAINT fktrl_diritti_servizio FOREIGN KEY (id_servizio) REFERENCES tbl_servizio(id_servizio);
--
-- Definition for index fktrl_diritti_utenti (OID = 189727) : 
--
ALTER TABLE ONLY trl_diritti_utente
    ADD CONSTRAINT fktrl_diritti_utenti FOREIGN KEY (id_utenti) REFERENCES tbl_utenti(id_utenti);
--
-- Definition for index fk_tbf_modello_profilazione_bibliotecario__tbf_parametro (OID = 189882) : 
--
ALTER TABLE ONLY tbf_modello_profilazione_bibliotecario
    ADD CONSTRAINT fk_tbf_modello_profilazione_bibliotecario__tbf_parametro FOREIGN KEY (id_parametro) REFERENCES tbf_parametro(id_parametro);
--
-- Definition for index import950_pkey (OID = 215913) : 
--
ALTER TABLE ONLY import950
    ADD CONSTRAINT import950_pkey PRIMARY KEY (id);
--
-- Definition for index tra_ordine_carrello_spedizione_pkey (OID = 265975) : 
--
ALTER TABLE ONLY tra_ordine_carrello_spedizione
    ADD CONSTRAINT tra_ordine_carrello_spedizione_pkey PRIMARY KEY (id_ordine);
--
-- Definition for index tra_ordine_carrello_spedizione_idx (OID = 265977) : 
--
ALTER TABLE ONLY tra_ordine_carrello_spedizione
    ADD CONSTRAINT tra_ordine_carrello_spedizione_idx UNIQUE (id_ordine, dt_spedizione, prg_spedizione);
--
-- Definition for index tba_ordini_fk (OID = 265979) : 
--
ALTER TABLE ONLY tra_ordine_carrello_spedizione
    ADD CONSTRAINT tba_ordini_fk FOREIGN KEY (id_ordine) REFERENCES tba_ordini(id_ordine) DEFERRABLE INITIALLY DEFERRED;
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
COMMENT ON TABLE sbnweb.tb_audiovideo IS 'AUDIOVISIVI';
COMMENT ON COLUMN sbnweb.tb_audiovideo.bid IS 'Codice identificativo SBN del titolo';
COMMENT ON COLUMN sbnweb.tb_audiovideo.tp_mater_audiovis IS 'tipo materiale: 115$a/0';
COMMENT ON COLUMN sbnweb.tb_audiovideo.lunghezza IS 'lunghezza: 115$a/1-3';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_colore IS 'Colore: 115$a/4';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_suono IS 'suono: 115$a/5';
COMMENT ON COLUMN sbnweb.tb_audiovideo.tp_media_suono IS 'supporto sonoro: 115$a/6';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_dimensione IS 'supporto sonoro: 115$a/6';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_forma_video IS 'forma della realizzazione: 115$a/8';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_tecnica IS 'tecnica: 115$a/9';
COMMENT ON COLUMN sbnweb.tb_audiovideo.tp_formato_film IS 'formato della presentazione: 115$a/10';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_mat_accomp IS 'materiale di accompagnamento: 115$a/11-14';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_forma_regist IS 'forma della videoregistrazione: 115$a/15';
COMMENT ON COLUMN sbnweb.tb_audiovideo.tp_formato_video IS 'formato della presentazione - video: 115$a/16';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_materiale_base IS 'codice materiale base: 115$a/17';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_supporto_second IS 'supporto secondario proiezioni: 115$a/18';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_broadcast IS 'tipo sistema: 115$a/19';
COMMENT ON COLUMN sbnweb.tb_audiovideo.tp_generazione IS 'generazione del film: 115$b/0';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_elementi IS 'elementi del film: 115$b/1';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_categ_colore IS 'categoria del colore per film: 115$b/2';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_polarita IS 'polarita'' del film: 115$b/3';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_pellicola IS 'tipo  pellicola del film: 115$b/4';
COMMENT ON COLUMN sbnweb.tb_audiovideo.tp_suono IS 'tipo  pellicola del film: 115$b/4';
COMMENT ON COLUMN sbnweb.tb_audiovideo.tp_stampa_film IS 'tipo di stampa del film: 115$b/6';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_deteriore IS 'stato di deterioramento del film: 115$b/7';
COMMENT ON COLUMN sbnweb.tb_audiovideo.cd_completo IS 'indicatore di completezza del film: 115$b/8';
COMMENT ON COLUMN sbnweb.tb_audiovideo.dt_ispezione IS 'data di ultima ispezione del film: 115$b/9-14';
COMMENT ON COLUMN sbnweb.tb_audiovideo.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_audiovideo.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_audiovideo.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_audiovideo.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_audiovideo.fl_canc IS 'flag di cancellazione logica';
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
COMMENT ON TABLE sbnweb.tb_disco_sonoro IS 'DISCO SONORO';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.cd_forma IS 'forma: 126/$a/0';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.cd_velocita IS 'velocita'': 126/$a/1';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.tp_suono IS 'tipo suono:126/$a/2';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.cd_pista IS 'tipo di pista: 126/$a/3';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.cd_dimensione IS 'dimensione: 126/$a/4';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.cd_larg_nastro IS 'tipo di nastro: 126/$a/5';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.cd_configurazione IS 'configurazione del nastro: 126/$a/6';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.cd_mater_accomp IS 'codici di materiale di accompagnamento: 126$a/7-12';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.cd_tecnica_regis IS 'tecnica di registrazione: 126/$a/13';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.cd_riproduzione IS 'codice di tipo riproduzione: 126$a/14';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.tp_disco IS 'tipo di disco: 126$b/0';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.tp_taglio IS 'tipo taglio: 126$b/2';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_disco_sonoro.fl_canc IS 'Flag di cancellazione logica';
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
COMMENT ON TABLE sbnweb.tb_loc_indice IS 'LOCALIZZAZIONI IN ATTESA DI INVIO ALL''INDICE';
COMMENT ON COLUMN sbnweb.tb_loc_indice.cd_polo IS 'Codice del polo';
COMMENT ON COLUMN sbnweb.tb_loc_indice.cd_biblioteca IS 'Codice della biblioteca';
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
COMMENT ON TABLE sbnweb.tb_risorsa_elettr IS 'RISORSA ELETTRONICA';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.bid IS 'Identificativo del titolo in SBN';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.tp_file IS 'tipo di computer file: 135$a/0';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.cd_designazione IS 'designazione specifica del materiale: 135$a/1';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.cd_colore IS 'colore: 135$a/2';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.cd_dimensione IS 'dimensione: 135$a/3';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.cd_suono IS 'suono: 135$a/4';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.cd_bit_immagine IS 'profondita'' dell''immagine: 135$a/5-7';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.cd_formato_file IS 'numero di formati file: 135$a/8';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.cd_qualita IS 'assicurazione di qualita'': 135$a/9';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.cd_origine IS 'origine della copia: 135$a/10';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.cd_base IS 'tipo di computer file: 135$a/11';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.ute_ins IS 'Utente che ha effettuato l''inserimento';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.ts_ins IS 'Timestamp di inserimento';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.ute_var IS 'Utente che ha effettuato la variazione';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.ts_var IS 'Timestamp di variazione';
COMMENT ON COLUMN sbnweb.tb_risorsa_elettr.fl_canc IS 'Flag di cancellazione logica';
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
COMMENT ON TABLE sbnweb.tbl_messaggio IS 'Messaggio tra biblioteche';
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
COMMENT ON TABLE sbnweb.tbl_utenti IS 'Utenti';
COMMENT ON COLUMN sbnweb.tbl_utenti.id_utenti IS 'Identificativo univoco dell''utente';
COMMENT ON COLUMN sbnweb.tbl_utenti.cd_polo IS 'Codice del Polo';
COMMENT ON COLUMN sbnweb.tbl_utenti.cd_bib IS 'Codice della biblioteca';
COMMENT ON COLUMN sbnweb.tbl_utenti.cod_utente IS 'seconda parte del codice identificativo dell''utente , costituita dal progressivo numerico assegnatogli dalla prima biblioteca del polo alla quale l''utente SI E'' ISCRITTO';
COMMENT ON COLUMN sbnweb.tbl_utenti."password" IS 'password dell''utente';
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
COMMENT ON COLUMN sbnweb.tbl_utenti.ind_posta_elettr IS 'indirizzo di posta elettronica';
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
COMMENT ON COLUMN sbnweb.tbq_batch_attivabili."path" IS 'indirizzo del file da eseguire';
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
COMMENT ON FUNCTION sbnweb.listaidautori (flcon character, datad timestamp with time zone, dataa timestamp with time zone, lettd text, letta text, liv_autd character, liv_auta character, tp_nome character) IS 'estrae una lista di VID condizionata dai parametri forniti in input
    Predisposta per gestire anche il fl_legtit attualmente oscurato
    Se fl_legtit=''n'' o ''s'' applica il filtro sugli autori in forma accettata';
COMMENT ON COLUMN sbnweb.tra_ordine_carrello_spedizione.id_ordine IS 'identificativo dell''ordine di tipo rilegatura';
COMMENT ON COLUMN sbnweb.tra_ordine_carrello_spedizione.dt_spedizione IS 'Data di spedizione al fornitore';
COMMENT ON COLUMN sbnweb.tra_ordine_carrello_spedizione.prg_spedizione IS 'Progressivo della spedizione';
COMMENT ON COLUMN sbnweb.tra_ordine_carrello_spedizione.cart_name IS 'Identificativo del carrello per Google';
