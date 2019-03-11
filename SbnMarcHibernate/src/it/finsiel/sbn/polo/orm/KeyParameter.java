/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.finsiel.sbn.polo.orm;

public class KeyParameter {

	// NUOVI CAMPI POSTGRESS TB_AUTORE / TB_TITOLO / TB_MARCA / TB_DESCRITTORE
	// TB_SOGGETTO / TB_CLASSE / TR_TIT_TIT / TR_TIT_AUT
	public static final String XXXfl_condiviso = "XXXfl_condiviso";
	public static final String XXXts_condiviso = "XXXts_condiviso";
	public static final String XXXute_condiviso = "XXXute_condiviso";
	public static final String XXXfl_condiviso_legame = "XXXfl_condiviso_legame";
	public static final String XXXts_condiviso_legame = "XXXts_condiviso_legame";
	public static final String XXXute_condiviso_legame = "XXXute_condiviso_legame";

	// NUOVI CAMPI TB_CLASSE
	public static final String XXXky_classe_ord = "XXXky_classe_ord";
	public static final String XXXsuffisso = "XXXXXXsuffisso";
	public static final String XXXult_term = "XXXult_term";

	// NUOVI CAMPI TB_SOGGETTO
	public static final String XXXcat_sogg = "XXXcat_sogg";
	public static final String XXXky_primo_descr = "XXXky_primo_descr";
	public static final String XXXnota_soggetto = "XXXnota_soggetto";

	// NUOVA TABELLA TERMINE_THESAURO
	public static final String XXXtp_forma_the = "XXXtp_forma_the";

	// NUOVI CAMPI TB_TITOLO
	public static final String XXXcd_periodicita = "XXXcd_periodicita";
	// NUOVI CAMPI TR_DES_DES
	public static final String XXXnota_des_des = "XXXnota_des_des";

	// CAMPI RELAZIONE tr_soggettari_biblioteche
	public static final String XXXcd_sogg = "XXXcd_sogg";
	public static final String XXXfl_att = "XXXfl_att";
	public static final String XXXfl_auto_loc = "XXXfl_auto_loc";

	// CAMPI RELAZIONE tr_sistemi_classi_biblioteche
	public static final String XXXflg_att = "XXXflg_att";

	public static final String XXXfl_posizione = "XXXfl_posizione";

	// TR_TERMINI_TERMINI
	public static final String XXXtermini_termini = "XXXtermini_termini";

	// CAMPO AGGIUNTIVO IN TR_TIT_BIB
	public static final String XXXts_ins_prima_coll = "XXXts_ins_prima_coll";

	// CAMPO AGGIUNTIVO IN TR_TIT_CLA
	public static final String XXXnota_tit_cla = "XXXnota_tit_cla";
	// CAMPO AGGIUNTIVO IN TR_TIT_SOG
	public static final String XXXnota_tit_sog_bib = "XXXnota_tit_sog_bib";

	// almaviva5_20111013 thes_cla
	public static final String XXXposizione = "XXXposizione";
	public static final String XXXnota_the_cla = "XXXnota_the_cla";

	// almaviva5_20120512 evolutive CFI
	public static final String XXXcat_termine = "XXXcat_termine";

	// almaviva5_20140918 evolutive area zero
	// tb_risorsa_elettr
	public static final String XXXcd_compressione = "XXXcd_compressione";
	public static final String XXXcd_riformattazione = "XXXcd_riformattazione";
	public static final String XXXtp_risorsa = "XXXtp_risorsa";

	// tb_titset_1
	public static final String XXXs105_tp_testo_letterario = "XXXs105_tp_testo_letterario";

	public static final String XXXs125_indicatore_testo = "XXXs125_indicatore_testo";

	public static final String XXXs140_tp_testo_letterario = "XXXs140_tp_testo_letterario";

	public static final String XXXs181_tp_forma_contenuto_1 = "XXXs181_tp_forma_contenuto_1";
	public static final String XXXs181_cd_tipo_contenuto_1 = "XXXs181_cd_tipo_contenuto_1";
	public static final String XXXs181_cd_movimento_1 = "XXXs181_cd_movimento_1";
	public static final String XXXs181_cd_dimensione_1 = "XXXs181_cd_dimensione_1";
	public static final String XXXs181_cd_sensoriale_1_1 = "XXXs181_cd_sensoriale_1_1";
	public static final String XXXs181_cd_sensoriale_2_1 = "XXXs181_cd_sensoriale_2_1";
	public static final String XXXs181_cd_sensoriale_3_1 = "XXXs181_cd_sensoriale_3_1";
	public static final String XXXs181_tp_forma_contenuto_2 = "XXXs181_tp_forma_contenuto_2";
	public static final String XXXs181_cd_tipo_contenuto_2 = "XXXs181_cd_tipo_contenuto_2";
	public static final String XXXs181_cd_movimento_2 = "XXXs181_cd_movimento_2";
	public static final String XXXs181_cd_dimensione_2 = "XXXs181_cd_dimensione_2";
	public static final String XXXs181_cd_sensoriale_1_2 = "XXXs181_cd_sensoriale_1_2";
	public static final String XXXs181_cd_sensoriale_2_2 = "XXXs181_cd_sensoriale_2_2";
	public static final String XXXs181_cd_sensoriale_3_2 = "XXXs181_cd_sensoriale_3_2";
	public static final String XXXs182_tp_mediazione_1 = "XXXs182_tp_mediazione_1";
	public static final String XXXs182_tp_mediazione_2 = "XXXs182_tp_mediazione_2";

	// tb_disco_sonoro
	public static final String XXXcd_mater_accomp_1 = "XXXcd_mater_accomp_1";
	public static final String XXXcd_mater_accomp_2 = "XXXcd_mater_accomp_2";
	public static final String XXXcd_mater_accomp_3 = "XXXcd_mater_accomp_3";
	public static final String XXXcd_mater_accomp_4 = "XXXcd_mater_accomp_4";
	public static final String XXXcd_mater_accomp_5 = "XXXcd_mater_accomp_5";
	public static final String XXXcd_mater_accomp_6 = "XXXcd_mater_accomp_6";
	public static final String XXXdurata = "XXXdurata";

	// tb_audiovideo
	public static final String XXXcd_mat_accomp_1 = "XXXcd_mat_accomp_1";
	public static final String XXXcd_mat_accomp_2 = "XXXcd_mat_accomp_2";
	public static final String XXXcd_mat_accomp_3 = "XXXcd_mat_accomp_3";
	public static final String XXXcd_mat_accomp_4 = "XXXcd_mat_accomp_4";

	public static final String XXXcd_dimensione_disco = "XXXcd_dimensione_disco";
	public static final String XXXtp_materiale_disco = "XXXtp_materiale_disco";
	public static final String XXXtp_suono_disco = "XXXtp_suono_disco";
	public static final String XXXcd_livello_e = "XXXcd_livello_e";

	public static String BIB = "";
	public static final String ORDER = "";
	public static final String XXXcd_paese = "XXXcd_paese";
	public static final String XXXcd_paese_std = "XXXcd_paese_std";
	public static final String XXXlivello_aut_da = "XXXlivello_aut_da";
	public static final String XXXlivello_aut_a = "XXXlivello_aut_a";
	public static final String XXXdata_var_Da = "XXXdata_var_Da";
	public static final String XXXdata_var_A = "XXXdata_var_A";
	public static final String XXXdataInizio_Da = "XXXdataInizio_Da";
	public static final String XXXdataInizio_A = "XXXdataInizio_A";
	public static final String XXXdata_ins_A = "XXXdata_ins_A";
	public static final String XXXdata_ins_Da = "XXXdata_ins_Da";
	public static final String XXXdataInizioDa = "XXXdataInizioDa";
	public static final String XXXdataInizioA = "XXXdataInizioA";
	public static final String XXXdataFine_Da = "XXXdataFine_Da";
	public static final String XXXdataFine_A = "XXXdataFine_A";
	public static final String XXXdataFineDa = "XXXdataFineDa";
	public static final String XXXdataFineA = "XXXdataFineA";
	public static final String XXXaa_morte = "XXXaa_morte";
	public static final String XXXaa_nascita = "XXXaa_nascita";
	public static final String XXXute_var = "XXXute_var";
	public static final String XXXute_ins = "XXXute_ins";
	public static final String XXXlivello = "XXXlivello";
	public static final String XXXlivello_auf = "XXXlivello_auf";
	public static final String XXXlivello_lav = "XXXlivello_lav";
	public static final String XXXlivello_sup = "XXXlivello_sup";
	public static final String XXXlivello_max = "XXXlivello_max";
	public static final String XXXlivello_med = "XXXlivello_med";
	public static final String XXXlivello_min = "XXXlivello_min";
	public static final String XXXlivello_rec = "XXXlivello_rec";
	public static final String XXXnon_musicale = "XXXnon_musicale";
	public static final String XXXky_editore = "XXXky_editore";

	public static final String XXXtp_legame = "XXXtp_legame";
	public static final String XXXts_var = "XXXts_var";
	public static final String XXXts_var_da = "XXXts_var_da";
	public static final String XXXts_var_a = "XXXts_var_a";
	public static final String XXXvid_base = "XXXvid_base";
	public static final String XXXvid_coll = "XXXvid_coll";

	public static final String XXXdata_inizio_inserimento = "XXXdata_inizio_inserimento";
	public static final String XXXdata_fine_inserimento = "XXXdata_fine_inserimento";
	public static final String XXXdata_inizio_variazione = "XXXdata_inizio_variazione";
	public static final String XXXdata_fine_variazione = "XXXdata_fine_variazione";

	public static final String XXXdid_base = "XXXdid_base";
	public static final String XXXdid_coll = "XXXdid_coll";

	public static final String XXXlid_base = "XXXlid_base";
	public static final String XXXlid_coll = "XXXlid_coll";

	public static final String XXXfl_allinea_sbnmarc = "XXXfl_allinea_sbnmarc";

	public static final String XXXfl_canc = "XXXfl_canc";
	public static final String XXXtp_luogo = "XXXtp_luogo";

	public static final String XXXcd_natura_base = "XXXcd_natura_base";

	public static final String XXXnumeroLastraDa = "XXXnumeroLastraDa";
	public static final String XXXnumeroLastraA = "XXXnumeroLastraA";

	public static final String XXXsici = "XXXsici";

	public static final String XXXds_segn = "XXXds_segn";
	public static final String XXXds_fondo = "XXXds_fondo";

	public static final String XXXimpronta1 = "XXXimpronta1";
	public static final String XXXimpronta2 = "XXXimpronta2";
	public static final String XXXimpronta3 = "XXXimpronta3";

	public static final String XXXtp_record = "XXXtp_record";
	public static final String XXXnota_numero_std = "XXXnota_numero_std";
	public static final String XXXaa_pubb_1 = "XXXaa_pubb_1";

	public static final String XXXky_cles1_t_uguale = "XXXky_cles1_t_uguale";
	public static final String XXXky_cles2_t_uguale = "XXXky_cles2_t_uguale";
	public static final String XXXky_clet1_t_uguale = "XXXky_clet1_t_uguale";
	public static final String XXXky_clet2_t_uguale = "XXXky_clet2_t_uguale";

	public static final String XXXfl_possesso = "XXXfl_possesso";
	public static final String XXXfl_gestione = "XXXfl_gestione";
	// tb_autore
	public static final String XXXisadn = "XXXisadn";
	public static final String XXXanno_mese_fine = "XXXanno_mese_fine";
	public static final String XXXanno_mese_inizio = "XXXanno_mese_inizio";
	public static final String XXXdata_anno = "XXXdata_anno";
	public static final String XXXanno_fine = "XXXanno_fine";
	public static final String XXXanno_inizio = "XXXanno_inizio";
	public static final String XXXky_el5null = "XXXky_el5null";
	public static final String XXXky_el5 = "XXXky_el5";
	public static final String XXXky_el4null = "XXXky_el4null";
	public static final String XXXky_el4 = "XXXky_el4";
	public static final String XXXky_el3null = "XXXky_el3null";
	public static final String XXXky_el3 = "XXXky_el3";
	public static final String XXXky_el2null = "XXXky_el2null";
	public static final String XXXky_el2 = "XXXky_el2";
	public static final String XXXky_el1null = "XXXky_el1null";
	public static final String XXXky_el1 = "XXXky_el1";
	public static final String XXXds_nome_cerca_simili = "XXXds_nome_cerca_simili";
	public static final String XXXparola4 = "XXXparola4";
	public static final String XXXparola3 = "XXXparola3";
	public static final String XXXparola2 = "XXXparola2";
	public static final String XXXparola1 = "XXXparola1";
	public static final String XXXky_cles1_a_puntuale = "XXXky_cles1_a_puntuale";
	public static final String XXXky_cles1_a_inizio = "XXXky_cles1_a_inizio";
	public static final String XXXtp_forma_conferma = "XXXtp_forma_conferma";
	public static final String XXXtp_nome_aut_g = "XXXtp_nome_aut_g";
	public static final String XXXtp_nome_aut_r = "XXXtp_nome_aut_r";
	public static final String XXXtp_nome_aut_e = "XXXtp_nome_aut_e";
	public static final String XXXtp_nome_aut_d = "XXXtp_nome_aut_d";
	public static final String XXXtp_nome_aut_c = "XXXtp_nome_aut_c";
	public static final String XXXtp_nome_aut_b = "XXXtp_nome_aut_b";
	public static final String XXXtp_nome_aut_a = "XXXtp_nome_aut_a";
	public static final String XXXautore_parentesi = "XXXautore_parentesi";
	public static final String XXXvid = "XXXvid";
	public static final String XXXcd_biblioteca = "XXXcd_biblioteca";
	public static final String XXXcd_polo = "XXXcd_polo";
	public static final String XXXtipo_forma_autore = "XXXtipo_forma_autore";
	public static final String XXXtipo_nome_autore = "XXXtipo_nome_autore";
	public static final String XXXtp_nome_aut = "XXXtp_nome_aut";
	// public static final String XXXdata_inizio_variazione =
	// "XXXdata_inizio_variazione";
	// public static final String XXXdata_fine_variazione =
	// "XXXdata_fine_variazione";
	// public static final String XXXdata_inizio_inserimento =
	// "XXXdata_inizio_inserimento";
	// public static final String XXXdata_fine_inserimento =
	// "XXXdata_fine_inserimento";
	public static final String XXXesporta_ts_var_e_ts_ins_da = "XXXesporta_ts_var_e_ts_ins_da";
	public static final String XXXtp_forma_aut = "XXXtp_forma_aut";
	public static final String XXXtipoNome1 = "XXXtipoNome1";
	public static final String XXXtipoNome2 = "XXXtipoNome2";
	public static final String XXXtipoNome3 = "XXXtipoNome3";
	public static final String XXXtipoNome4 = "XXXtipoNome4";
	public static final String XXXesporta_ts_var_da = "XXXesporta_ts_var_da";
	public static final String XXXesporta_ts_var_a = "XXXesporta_ts_var_a";
	public static final String XXXky_cles1_a = "XXXky_cles1_a";
	public static final String XXXds_nome_aut = "XXXds_nome_aut";
	public static final String XXXKy_cautun = "XXXKy_cautun";
	public static final String XXXKy_auteur = "XXXKy_auteur";
	public static final String XXXdata_var_da = "XXXdata_var_da";
	public static final String XXXdata_var_a = "XXXdata_var_a";
	public static final String XXXky_cles2_a = "XXXky_cles2_a";
	public static final String XXXky_cles1_a_fine = "XXXky_cles1_a_fine";
	public static final String XXXcd_livello = "XXXcd_livello";
	public static final String XXXtipoNome = "XXXtipoNome";
	public static final String XXXcles2_1 = "XXXcles2_1";
	public static final String XXXcles2_2 = "XXXcles2_2";
	public static final String XXXcles2_2null = "XXXcles2_2null";
	public static final String XXXky_cles2_anull = "XXXky_cles2_anull";

	public static final String XXXStringaLikeAutore = "XXXStringaLikeAutore";
	public static final String XXXStringaEsattaAutore = "XXXStringaEsattaAutore";
	public static final String XXXStringaLikeAutore1 = "XXXStringaLikeAutore1";
	public static final String XXXStringaLikeAutore2 = "XXXStringaLikeAutore2";
	public static final String XXXStringaEsattaAutore1 = "XXXStringaEsattaAutore1";
	public static final String XXXStringaEsattaAutore2 = "XXXStringaEsattaAutore2";
	public static final String XXXky_auteur = "XXXky_auteur";
	public static final String XXXky_cautun = "XXXky_cautun";
	public static final String XXXtp_responsabilita = "XXXtp_responsabilita";
	public static final String XXXcd_relazione = "XXXcd_relazione";

	public static final String XXXtp_responsabilita_or_1 = "XXXtp_responsabilita_or_1";
	public static final String XXXtp_responsabilita_or_2 = "XXXtp_responsabilita_or_2";

	// end tb_autore

	// tbf_biblioteca_in_polo
	// public static final String XXXcd_polo = "XXXcd_polo";
	// public static final String XXXcd_biblioteca = "XXXcd_biblioteca";
	public static final String XXXcd_ana_biblioteca = "XXXcd_ana_biblioteca";

	// tbf_biblioteca_in_polo

	// tb_cartografia -->
	public static final String XXXcoordOvest = "XXXcoordOvest";
	public static final String XXXcoordEst = "XXXcoordEst";
	public static final String XXXcoordNord = "XXXcoordNord";
	public static final String XXXcoordSud = "XXXcoordSud";
	public static final String XXXtipoScala = "XXXtipoScala";
	public static final String XXXmeridiano = "XXXmeridiano";
	public static final String XXXscalaOrizzontale = "XXXscalaOrizzontale";
	public static final String XXXscalaVerticale = "XXXscalaVerticale";
	public static final String XXXtp_proiezione = "XXXtp_proiezione";

	public static final String XXXbid = "XXXbid";

	// public void setBID(String value)
	// {
	// this.filtri.put(XXXbid,value);
	//
	// }

	// tb_cartografia -->

	// classe
	// public static final String XXXparola1 = "XXXparola1";
	// public static final String XXXparola2 = "XXXparola2";
	// public static final String XXXparola3 = "XXXparola3";
	// public static final String XXXparola4 = "XXXparola4";
	public static final String XXXsistema = "XXXsistema";
	public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXdata_inizio_inserimento =
	// "XXXdata_inizio_inserimento";
	// public static final String XXXdata_fine_inserimento =
	// "XXXdata_fine_inserimento";
	public static final String XXXclasse_inizio = "XXXclasse_inizio";
	public static final String XXXclasse_fine = "XXXclasse_fine";
	public static final String XXXcd_sistema = "XXXcd_sistema";
	public static final String XXXclasse = "XXXclasse";
	public static final String XXXstringaLike = "XXXstringaLike";

	// end classe

	// composizione
	public static final String XXXultima_variazione = "XXXultima_variazione";
	public static final String XXXlettera = "XXXlettera";

	public static final String XXXformaComposizione1 = "XXXformaComposizione1";
	public static final String XXXformaComposizione2 = "XXXformaComposizione2";
	public static final String XXXformaComposizione3 = "XXXformaComposizione3";
	public static final String XXXorganicoSinteticoComposizione = "XXXorganicoSinteticoComposizione";
	public static final String XXXorganicoAnaliticoComposizione = "XXXorganicoAnaliticoComposizione";
	public static final String XXXtonalita = "XXXtonalita";
	public static final String XXXnumeroOrdine = "XXXnumeroOrdine";
	public static final String XXXnumeroOpera = "XXXnumeroOpera";
	public static final String XXXnumeroCatalogo = "XXXnumeroCatalogo";
	public static final String XXXtitoloOrdinamento = "XXXtitoloOrdinamento";
	public static final String XXXtitoloEstratto = "XXXtitoloEstratto";
	public static final String XXXappellativo = "XXXappellativo";
	// public static final String XXXbid = "XXXbid";

	public static final String XXXtitoloOrdinamentoLungo = "XXXtitoloOrdinamentoLungo";
	public static final String XXXtitoloEstrattoLungo = "XXXtitoloEstrattoLungo";
	public static final String XXXappellativoLungo = "XXXappellativoLungo";
	public static final String XXXdata_composizione = "XXXdata_composizione";

	// end composizione

	// descrittore

	// public static final String XXXparola1 = "XXXparola1";
	// public static final String XXXparola2 = "XXXparola2";
	// public static final String XXXparola3 = "XXXparola3";
	// public static final String XXXparola4 = "XXXparola4";
	public static final String XXXds_soggetto = "XXXds_soggetto";
	public static final String XXXdid = "XXXdid";
	// public static final String XXXstringaLike = "XXXstringaLike";
	public static final String XXXstringaEsatta = "XXXstringaEsatta";
	public static final String XXXcd_soggettario = "XXXcd_soggettario";
	public static final String XXXky_norm_descritt = "XXXky_norm_descritt";

	// TermineThesauro Thesauro
	public static final String XXXcd_the = "XXXcd_the";
	public static final String XXXds_termine_thesauro = "XXXds_termine_thesauro";
	public static final String XXXnota_termine_thesauro = "XXXnota_termine_thesauro";
	public static final String XXXky_termine_thesauro = "XXXky_termine_thesauro";

	// Tr_termine_termine
	public static final String XXXnota_termini_termini = "XXXnota_termini_termini";
	public static final String XXXtipo_coll = "XXXtipo_coll";

	// trs_termini_titoli_biblioteche
	public static final String XXXcd_bibilioteca = "XXXcd_bibilioteca";
	public static final String XXXnota_termine_titoli_biblioteca = "XXXnota_termine_titoli_biblioteca";

	// grafica
	public static final String XXXtpMaterialeGra = "XXXtpMaterialeGra";
	public static final String XXXcdSupporto = "XXXcdSupporto";
	public static final String XXXcdColore = "XXXcdColore";
	public static final String XXXcdTecnicaDis_1 = "XXXcdTecnicaDis_1";
	public static final String XXXcdTecnicaDis_2 = "XXXcdTecnicaDis_2";
	public static final String XXXcdTecnicaDis_3 = "XXXcdTecnicaDis_3";
	public static final String XXXcdTecnicaSta_1 = "XXXcdTecnicaSta_1";
	public static final String XXXcdTecnicaSta_2 = "XXXcdTecnicaSta_2";
	public static final String XXXcdTecnicaSta_3 = "XXXcdTecnicaSta_3";
	public static final String XXXcdDesignFunz = "XXXcdDesignFunz";
	// public static final String XXXbid = "XXXbid";
	// gruppo
	public static final String XXXid_gruppo = "XXXid_gruppo";
	// impronta
	// public static final String XXXbid = "XXXbid";
	public static final String XXXimpronta_1 = "XXXimpronta_1";
	public static final String XXXimpronta_2 = "XXXimpronta_2";
	public static final String XXXimpronta_3 = "XXXimpronta_3";
	// incipit
	// public static final String XXXbid = "XXXbid";
	public static final String XXXnumero_mov = "XXXnumero_mov";
	public static final String XXXnumero_p_mov = "XXXnumero_p_mov";
	// luogo
	// public static final String XXXcd_livello = "XXXcd_livello";
	public static final String XXXStringaLikeLuogo = "XXXStringaLikeLuogo";
	public static final String XXXStringaEsattaLuogo = "XXXStringaEsattaLuogo";
	public static final String XXXlid = "XXXlid";
	public static final String XXXky_norm_luogo = "XXXky_norm_luogo";
	public static final String XXXds_luogo = "XXXds_luogo";
	public static final String XXXidDaModificare = "XXXidDaModificare";
	// marca

	// public static final String XXXparola1 = "XXXparola1";
	// public static final String XXXparola2 = "XXXparola2";
	// public static final String XXXparola3 = "XXXparola3";
	// public static final String XXXparola4 = "XXXparola4";
	public static final String XXXparola5 = "XXXparola5";
	public static final String XXXStringaEsattaMarca = "XXXStringaEsattaMarca";
	public static final String XXXStringaLikeMarca = "XXXStringaLikeMarca";
	public static final String XXXmid = "XXXmid";
	public static final String XXXmotto = "XXXmotto";

	// musica
	public static final String XXXtipoTesto = "XXXtipoTesto";
	public static final String XXXorganicoSintetico = "XXXorganicoSintetico";
	public static final String XXXorganicoAnalitico = "XXXorganicoAnalitico";
	public static final String XXXtipoElaborazione = "XXXtipoElaborazione";
	public static final String XXXpresentazione = "XXXpresentazione";
	// public static final String XXXbid = "XXXbid";
	// parola

	// public static final String XXXparola2 = "XXXparola2";
	// public static final String XXXparola3 = "XXXparola3";
	// public static final String XXXparola4 = "XXXparola4";
	public static final String XXXid_parola = "XXXid_parola";
	// public static final String XXXmid = "XXXmid";
	// public static final String XXXparola1 = "XXXparola1";
	public static final String XXXparola = "XXXparola";

	// repertorio

	// public static final String XXXparola2 = "XXXparola2";
	// public static final String XXXparola3 = "XXXparola3";
	// public static final String XXXparola4 = "XXXparola4";
	public static final String XXXid_repertorio = "XXXid_repertorio";
	public static final String XXXcd_sig_repertorio = "XXXcd_sig_repertorio";
	// public static final String XXXstringaLike = "XXXstringaLike";
	// public static final String XXXparola1 = "XXXparola1";
	// public static final String XXXstringaEsatta = "XXXstringaEsatta";
	// soggetto

	// public static final String XXXparola2 = "XXXparola2";
	// public static final String XXXparola3 = "XXXparola3";
	// public static final String XXXparola4 = "XXXparola4";
	// public static final String XXXdata_inizio_variazione =
	// "XXXdata_inizio_variazione";
	// public static final String XXXdata_fine_variazione =
	// "XXXdata_fine_variazione";
	// public static final String XXXds_soggetto = "XXXds_soggetto";

	public static final String XXXky_cles2_s = "XXXky_cles2_s";

	public static final String XXXky_cles2_sNULL = "XXXky_cles2_sNULL";
	public static final String XXXcid = "XXXcid";
	public static final String XXXcid_Diverso = "XXXcid_Diverso";
	// public static final String XXXstringaLike = "XXXstringaLike";
	// public static final String XXXparola1 = "XXXparola1";
	// public static final String XXXstringaEsatta = "XXXstringaEsatta";
	public static final String XXXky_cles1_s = "XXXky_cles1_s";

	// titolo

	public static final String XXXtp_materiale = "XXXtp_materiale";
	public static final String XXXtp_materiale1 = "XXXtp_materiale1";
	public static final String XXXtp_materiale2 = "XXXtp_materiale2";
	public static final String XXXtp_materiale3 = "XXXtp_materiale3";
	public static final String XXXtp_materiale4 = "XXXtp_materiale4";
	public static final String XXXtp_materiale5 = "XXXtp_materiale5";
	public static final String XXXcd_natura = "XXXcd_natura";
	public static final String XXXcd_natura1 = "XXXcd_natura1";
	public static final String XXXcd_natura2 = "XXXcd_natura2";
	public static final String XXXcd_natura3 = "XXXcd_natura3";
	public static final String XXXcd_natura4 = "XXXcd_natura4";
	public static final String XXXtp_record_uni = "XXXtp_record_uni";
	public static final String XXXtp_record_uni1 = "XXXtp_record_uni1";
	public static final String XXXtp_record_uni2 = "XXXtp_record_uni2";
	public static final String XXXtp_record_uni3 = "XXXtp_record_uni3";
	public static final String XXXtp_record_uni4 = "XXXtp_record_uni4";
	// public static final String XXXts_var_da = "XXXts_var_da";
	// public static final String XXXts_var_a = "XXXts_var_a";
	// public static final String XXXesporta_ts_var_da = "XXXesporta_ts_var_da";
	// public static final String XXXesporta_ts_var_e_ts_ins_da =
	// "XXXesporta_ts_var_e_ts_ins_da";
	// public static final String XXXesporta_ts_var_a = "XXXesporta_ts_var_a";
	public static final String XXXcd_livello_da = "XXXcd_livello_da";
	public static final String XXXcd_livello_a = "XXXcd_livello_a";
	public static final String XXXts_ins_da = "XXXts_ins_da";
	public static final String XXXts_ins_a = "XXXts_ins_a";

	public static final String XXXtp_aa_pubb = "XXXtp_aa_pubb";
	public static final String XXXaa_pubb_1_da = "XXXaa_pubb_1_da";
	public static final String XXXaa_pubb_1_a = "XXXaa_pubb_1_a";
	public static final String XXXaa_pubb_1_like = "XXXaa_pubb_1_like";
	public static final String XXXaa_pubb_2_da = "XXXaa_pubb_2_da";
	public static final String XXXaa_pubb_2_a = "XXXaa_pubb_2_a";
	public static final String XXXaa_pubb_2_like = "XXXaa_pubb_2_like";
	public static final String XXXcd_genere_1 = "XXXcd_genere_1";
	public static final String XXXcd_genere_2 = "XXXcd_genere_2";
	public static final String XXXcd_genere_3 = "XXXcd_genere_3";
	public static final String XXXcd_genere_4 = "XXXcd_genere_4";
	public static final String XXXcd_genere = "XXXcd_genere";
	public static final String XXXcd_lingua_1 = "XXXcd_lingua_1";
	public static final String XXXcd_lingua_2 = "XXXcd_lingua_2";
	public static final String XXXcd_lingua_3 = "XXXcd_lingua_3";
	public static final String XXXcd_lingua = "XXXcd_lingua";
	public static final String XXXstringaClet = "XXXstringaClet";

	// public static final String XXXbid = "XXXbid";
	public static final String XXXnaturaSoC = "XXXnaturaSoC";
	public static final String XXXnaturaMoW = "XXXnaturaMoW";
	public static final String XXXnaturaBoA = "XXXnaturaBoA";
	public static final String XXXaa_pubb_1_s = "XXXaa_pubb_1_s";

	public static final String XXXclet2_ricerca = "XXXclet2_ricerca";
	public static final String XXXstringaLike1 = "XXXstringaLike1";
	public static final String XXXstringaLike2 = "XXXstringaLike2";

	public static final String XXXstringaEsatta1 = "XXXstringaEsatta1";
	public static final String XXXstringaEsatta2 = "XXXstringaEsatta2";

	public static final String XXXcles1_0 = "XXXcles1_0";
	public static final String XXXcles2_0 = "XXXcles2_0";
	public static final String XXXcles1_1 = "XXXcles1_1";
	// public static final String XXXcles2_1 = "XXXcles2_1";
	public static final String XXXcles1_2 = "XXXcles1_2";
	// public static final String XXXcles2_2 = "XXXcles2_2";
	public static final String XXXcles1_3 = "XXXcles1_3";
	public static final String XXXcles2_3 = "XXXcles2_3";
	public static final String XXXcles1_4 = "XXXcles1_4";
	public static final String XXXcles2_4 = "XXXcles2_4";

	// public static final String XXXute_var = "XXXute_var";
	// public static final String XXXisadn = "XXXisadn";
	public static final String XXXky_cles1_t = "XXXky_cles1_t";
	public static final String XXXky_cles2_t = "XXXky_cles2_t";
	public static final String XXXky_clet1_t = "XXXky_clet1_t";
	public static final String XXXky_clet2_t = "XXXky_clet2_t";
	public static final String XXXisbd = "XXXisbd";
	public static final String XXXstringaClet1 = "XXXstringaClet1";
	public static final String XXXstringaClet2 = "XXXstringaClet2";
	// tr_aut_aut
	public static final String XXXvid_1 = "XXXvid_1";
	public static final String XXXvid_2 = "XXXvid_2";
	// tr_aut_bib

	// public static final String XXXcd_biblioteca = "XXXcd_biblioteca";
	// public static final String XXXcd_polo = "XXXcd_polo";
	// public static final String XXXvid = "XXXvid";
	public static final String XXXfl_allinea = "XXXfl_allinea";
	// tr_mar_bib

	// public static final String XXXcd_biblioteca = "XXXcd_biblioteca";
	// public static final String XXXcd_polo = "XXXcd_polo";
	// public static final String XXXmid = "XXXmid";
	// tr_rep_aut
	// public static final String XXXid_repertorio = "XXXid_repertorio";
	// public static final String XXXvid = "XXXvid";

	// tr_rep_mar
	public static final String XXXprogr_repertorio = "XXXprogr_repertorio";
	// public static final String XXXmid = "XXXmid";
	// public static final String XXXid_repertorio = "XXXid_repertorio";

	// tr_rep_tit
	// public static final String XXXbid = "XXXbid";
	// public static final String XXXid_repertorio = "XXXid_repertorio";

	// tr_tit_bib
	// public static final String XXXbid = "XXXbid";
	// public static final String XXXcd_polo = "XXXcd_polo";
	// public static final String XXXcd_biblioteca = "XXXcd_biblioteca";
	// public static final String XXXfl_allinea = "XXXfl_allinea";

	// tr_tit_luo
	// public static final String XXXbid = "XXXbid";
	// public static final String XXXlid = "XXXlid";

	// tr_tit_mar
	// public static final String XXXmid = "XXXmid";
	// public static final String XXXbid = "XXXbid";

	// tr_tit_tit
	public static final String XXXbid_coll = "XXXbid_coll";
	public static final String XXXbid_base = "XXXbid_base";

	// v_cartografia
	// public static final String XXXstringaClet = "XXXstringaClet";

	// v_musica
	// public static final String XXXstringaClet = "XXXstringaClet";
	// public static final String XXXisbd = "XXXisbd";

	// v_grafica
	// public static final String XXXstringaClet = "XXXstringaClet";
	// public static final String XXXisbd = "XXXisbd";

	// Ve_cartografia_aut_autCommonDao
	// public static final String XXXStringaLikeAutore = "XXXStringaLikeAutore";
	// public static final String XXXStringaEsattaAutore =
	// "XXXStringaEsattaAutore";
	// public static final String XXXStringaLikeAutore1 =
	// "XXXStringaLikeAutore1";
	// public static final String XXXStringaLikeAutore2 =
	// "XXXStringaLikeAutore2";
	// public static final String XXXStringaEsattaAutore1 =
	// "XXXStringaEsattaAutore1";
	// public static final String XXXStringaEsattaAutore2 =
	// "XXXStringaEsattaAutore2";
	// public static final String XXXky_auteur = "XXXky_auteur";
	// public static final String XXXky_cautun = "XXXky_cautun";
	// public static final String XXXtp_responsabilita = "XXXtp_responsabilita";
	// public static final String XXXcd_relazione = "XXXcd_relazione";
	// GENERAL
	// public static final String XXXvid = "XXXvid";

	// Ve_cartografia_aut_luoCommonDao
	// GENERAL
	// public static final String XXXvid = "xxxvid";

	// Ve_cartografia_cla_autCommonDao
	// Filtri
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";

	// Ve_cartografia_cla_luoCommonDao
	// FILTRI
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";

	// Ve_cartografia_luo_autCommonDao
	// FILTRI
	// public static final String XXXlid = "XXXlid";

	// Ve_cartografia_mar_autCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";

	// Ve_cartografia_mar_luoCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";

	// Ve_cartografia_sog_autCommonDao
	// FILTRI
	// public static final String XXXcid = "XXXcid";

	// Ve_cartografia_sog_luoCommonDao
	// FILTRI
	// public static final String XXXcid = "XXXcid";

	// Ve_cartografia_tit_c_autCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";

	// Ve_cartografia_tit_c_luoCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";

	// Ve_grafica_aut_autCommonDao
	// public static final String XXXStringaLikeAutore = "XXXStringaLikeAutore";
	// public static final String XXXStringaEsattaAutore =
	// "XXXStringaEsattaAutore";
	// public static final String XXXStringaLikeAutore1 =
	// "XXXStringaLikeAutore1";
	// public static final String XXXStringaLikeAutore2 =
	// "XXXStringaLikeAutore2";
	// public static final String XXXStringaEsattaAutore1 =
	// "XXXStringaEsattaAutore1";
	// public static final String XXXStringaEsattaAutore2 =
	// "XXXStringaEsattaAutore2";
	// public static final String XXXky_auteur = "XXXky_auteur";
	// public static final String XXXky_cautun = "XXXky_cautun";
	// public static final String XXXtp_responsabilita = "XXXtp_responsabilita";
	// public static final String XXXcd_relazione = "XXXcd_relazione";
	// FILTRI
	// public static final String XXXvid = "XXXvid";

	// Ve_grafica_aut_luoCommonDao
	// FILTRI
	// public static final String XXXvid = "XXXvid";

	// Ve_grafica_cla_autCommonDao
	// FILTRI
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";

	// Ve_cartografia_cla_luoCommonDao
	// FILTRI
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";

	// Ve_grafica_cla_luoCommonDao
	// FILTRI
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";

	// Ve_grafica_luo_autCommonDao
	// FILTRI
	// public static final String XXXlid = "XXXlid";

	// Ve_grafica_mar_autCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";

	// Ve_grafica_mar_luoCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";

	// Ve_grafica_sog_autCommonDao
	// FILTRI
	// public static final String XXXcid = "XXXcid";

	// Ve_grafica_sog_luoCommonDao
	// FILTRI
	// public static final String XXXcid = "XXXcid";

	// Ve_grafica_tit_c_autCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";

	// Ve_grafica_tit_c_luoCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";

	// Ve_musica_aut_autCommonDao
	// public static final String XXXStringaLikeAutore = "XXXStringaLikeAutore";
	// public static final String XXXStringaEsattaAutore =
	// "XXXStringaEsattaAutore";
	// public static final String XXXStringaLikeAutore1 =
	// "XXXStringaLikeAutore1";
	// public static final String XXXStringaLikeAutore2 =
	// "XXXStringaLikeAutore2";
	// public static final String XXXStringaEsattaAutore1 =
	// "XXXStringaEsattaAutore1";
	// public static final String XXXStringaEsattaAutore2 =
	// "XXXStringaEsattaAutore2";
	// public static final String XXXky_auteur = "XXXky_auteur";
	// public static final String XXXky_cautun = "XXXky_cautun";
	// public static final String XXXtp_responsabilita = "XXXtp_responsabilita";
	// public static final String XXXcd_relazione = "XXXcd_relazione";
	// FILTRI
	// public static final String XXXvid = "XXXvid";

	// Ve_musica_aut_comCommonDao
	// public static final String XXXnumeroOrdine = "XXXnumeroOrdine";
	// public static final String XXXnumeroOpera = "XXXnumeroOpera";
	// FILTRI
	// public static final String XXXvid = "XXXvid";

	// Ve_musica_aut_luoCommonDao
	// FILTRI
	// public static final String XXXvid = "XXXvid";

	// Ve_musica_cla_autCommonDao
	// FILTRI
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";

	// Ve_musica_cla_comCommonDao
	// public static final String XXXnumeroOrdine = "XXXnumeroOrdine";
	// public static final String XXXnumeroOpera = "XXXnumeroOpera";
	// FILTRI
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";

	// Ve_musica_cla_luoCommonDao
	// FILTRI
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";

	// Ve_musica_luo_autCommonDao extends Tb_titoloCommonDao {
	// FILTRI
	// public static final String XXXlid = "XXXlid";

	// Ve_musica_luo_autCommonDao
	// FILTRI
	// public static final String XXXlid = "XXXlid";

	// Ve_musica_luo_comCommonDao
	// public static final String XXXnumeroOrdine = "XXXnumeroOrdine";
	// public static final String XXXnumeroOpera = "XXXnumeroOpera";
	// FILTRI
	// public static final String XXXlid = "XXXlid";

	// Ve_musica_mar_autCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";

	// Ve_musica_mar_comCommonDao
	// public static final String XXXnumeroOrdine = "XXXnumeroOrdine";
	// public static final String XXXnumeroOpera = "XXXnumeroOpera";
	// FILTRI
	// public static final String XXXmid = "XXXmid";

	// Ve_musica_mar_luoCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";

	// Ve_musica_tit_c_autCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";

	// Ve_musica_tit_c_comCommonDao
	// public static final String XXXnumeroOrdine = "XXXnumeroOrdine";
	// public static final String XXXnumeroOpera = "XXXnumeroOpera";
	// FILTRI
	// public static final String XXXbid = "XXXbid";

	// Ve_musica_tit_c_luoCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";

	// Ve_titolo_aut_autCommonDao
	// public static final String XXXStringaLikeAutore = "XXXStringaLikeAutore";
	// public static final String XXXStringaEsattaAutore =
	// "XXXStringaEsattaAutore";
	// public static final String XXXStringaLikeAutore1 =
	// "XXXStringaLikeAutore1";
	// public static final String XXXStringaLikeAutore2 =
	// "XXXStringaLikeAutore2";
	// public static final String XXXStringaEsattaAutore1 =
	// "XXXStringaEsattaAutore1";
	// public static final String XXXStringaEsattaAutore2 =
	// "XXXStringaEsattaAutore2";
	// public static final String XXXky_auteur = "XXXky_auteur";
	// public static final String XXXky_cautun = "XXXky_cautun";
	// public static final String XXXtp_responsabilita = "XXXtp_responsabilita";
	// public static final String XXXcd_relazione = "XXXcd_relazione";
	// FILTRI
	// public static final String XXXvid = "XXXvid";

	// Ve_titolo_aut_luoCommonDao
	// FILTRI
	// public static final String XXXvid = "XXXvid";

	// Ve_titolo_aut_marCommonDao
	// FILTRI
	// public static final String XXXvid = "XXXvid";

	// Ve_titolo_aut_nstdCommonDao
	// FILTRI
	public static final String XXXtp_numero_std = "XXXtp_numero_std";

	// Ve_titolo_cla_autCommonDao
	// FILTER
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";

	// Ve_titolo_cla_luoCommonDao
	// FILTRI
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";

	// Ve_titolo_cla_marCommonDao
	// FILTRI
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";

	// Ve_titolo_luo_autCommonDao
	// FILTRI
	// public static final String XXXlid = "XXXlid";

	// Ve_titolo_luo_marCommonDao
	// FILTRI
	// public static final String XXXlid = "XXXlid";

	// Ve_titolo_mar_autCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";

	// Ve_titolo_mar_luoCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";

	// Ve_titolo_sog_autCommonDao
	// FILTRI
	// public static final String XXXcid = "XXXcid";

	// Ve_titolo_sog_luoCommonDao
	// FILTRI
	// public static final String XXXcid = "XXXcid";

	// Ve_titolo_sog_marCommonDao
	// FILTRI
	// public static final String XXXcid = "XXXcid";

	// Ve_titolo_tit_c_autCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";

	// Ve_titolo_tit_c_luoCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";

	// Ve_titolo_tit_c_marCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";

	// Vf_cartografia_autCommonDao
	// public static final String XXXtipoScala = "XXXtipoScala";

	// Vf_cartografia_luoCommonDao
	// public static final String XXXtipoScala = "XXXtipoScala";

	// Vf_grafica_autCommonDao
	// public static final String XXXstringaClet = "XXXstringaClet";

	// Vf_grafica_luoCommonDao
	// public static final String XXXstringaClet = "XXXstringaClet";

	// Vf_musica_autCommonDao
	// public static final String XXXstringaClet = "XXXstringaClet";

	// Vf_musica_comCommonDao
	// public static final String XXXnumeroOrdine = "XXXnumeroOrdine";
	// public static final String XXXnumeroOpera = "XXXnumeroOpera";
	// public static final String XXXstringaClet = "XXXstringaClet";
	// public static final String XXXbid = "XXXbid";

	// Vf_musica_comautCommonDao
	// public static final String XXXnumeroOrdine = "XXXnumeroOrdine";
	// public static final String XXXnumeroOpera = "XXXnumeroOpera";
	// public static final String XXXstringaClet = "XXXstringaClet";

	// Vf_musica_luoCommonDao
	// public static final String XXXstringaClet = "XXXstringaClet";

	// Vf_titolo_autCommonDao
	// public static final String XXXstringaClet = "XXXstringaClet";

	// Vf_titolo_comCommonDao
	// public static final String XXXstringaClet = "XXXstringaClet";

	// Vf_titolo_luoCommonDao
	// public static final String XXXstringaClet = "XXXstringaClet";

	// Vf_titolo_marCommonDao
	// public static final String XXXstringaClet = "XXXstringaClet";

	// Vl_autore_autCommonDao
	// FILTRI
	// public static final String XXXvid_1 = "XXXvid_1";
	// public static final String XXXtp_legame = "XXXtp_legame";

	// Vl_autore_bibCommonDao
	// FILTRI
	// public static final String XXXvid = "XXXvid";
	// public static final String XXXcd_polo = "XXXcd_polo";
	// public static final String XXXcd_ana_biblioteca = "XXXcd_ana_biblioteca";
	// Vl_autore_marCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";
	// Vl_autore_titCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";
	public static final String XXXrelatorCode = "XXXrelatorCode";
	// public static final String XXXparola1 = "XXXparola1";
	// Vl_cartografia_autCommonDao
	// FILTRI
	// public static final String XXXvid = "XXXvid";
	// Vl_cartografia_claCommonDao
	// FILTRI
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXclasse = "XXXclasse";
	// Vl_cartografia_luoCommonDao
	// FILTRI
	// public static final String XXXlid = "XXXlid";
	// Vl_cartografia_marCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";
	// Vl_cartografia_sogCommonDao
	// FILTRI
	// public static final String XXXcid = "XXXcid";
	// Vl_cartografia_tit_cCommonDao
	// FILTRI
	// public static final String XXXbid_coll = "XXXbid_coll";
	// Vl_classe_titCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";
	// Vl_composizione_autCommonDao
	// FILTRI
	// public static final String XXXvid = "XXXvid";
	// Vl_descrittore_desCommonDao
	// FILTRI
	public static final String XXXdid_1 = "XXXdid_1";
	// public static final String XXXtp_legame = "XXXtp_legame";
	// Vl_descrittore_sogCommonDao
	// FILTRI
	// public static final String XXXcid = "XXXcid";
	// public static final String XXXdid = "XXXdid";
	// Vl_grafica_autCommonDao
	// FILTRI
	// public static final String XXXvid = "XXXvid";
	// Vl_grafica_claCommonDao
	// FILTRI
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXclasse = "XXXclasse";
	// Vl_grafica_luoCommonDao
	// FILTRI
	// public static final String XXXlid = "XXXlid";
	// Vl_grafica_marCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";
	// Vl_grafica_sogCommonDao
	// FILTRI
	// public static final String XXXcid = "XXXcid";
	// Vl_grafica_tit_cCommonDao
	// FILTRI
	// public static final String XXXbid_coll = "XXXbid_coll";
	// Vl_luogo_luoCommonDao
	// FILTRI
	public static final String XXXlid_1 = "XXXlid_1";
	// Vl_luogo_titCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";
	// Vl_marca_autCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";
	// public static final String XXXvid = "XXXvid";
	// Vl_marca_bibCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";
	// public static final String XXXcd_ana_biblioteca = "XXXcd_ana_biblioteca";
	// public static final String XXXcd_polo = "XXXcd_polo";
	// Vl_marca_parCommonDao
	// FILTRI
	// public static final String XXXparola = "XXXparola";
	// public static final String XXXmid = "XXXmid";
	// Vl_marca_titCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";
	// Vl_musica_autCommonDao
	// FILTRI
	// public static final String XXXvid = "XXXvid";

	// Vl_musica_bibCommonDao
	// FILTRI
	// public static final String XXXcd_polo = "XXXcd_polo";
	// public static final String XXXcd_biblioteca = "XXXcd_biblioteca";
	// Vl_musica_claCommonDao
	// FILTRI
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXclasse = "XXXclasse";
	// Vl_musica_luoCommonDao
	// FILTRI
	// public static final String XXXlid = "XXXlid";
	// Vl_musica_marCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";
	// Vl_musica_tit_cCommonDao
	// FILTRI
	// public static final String XXXbid_coll = "XXXbid_coll";
	// Vl_repertorio_marCommonDao
	// FILTRI e GENERICO
	// public static final String XXXmid = "XXXmid";
	// FILTRI
	// public static final String XXXcd_sig_repertorio = "XXXcd_sig_repertorio";
	// public static final String XXXprogr_repertorio = "XXXprogr_repertorio";

	// Vl_soggetto_desCommonDao
	// FILTRI
	// public static final String XXXdid = "XXXdid";
	// Vl_soggetto_titCommonDao
	// FILTRI
	// public static final String XXXbid = "XXXbid";

	// Vl_titolo_autCommonDao
	// FILTRI
	// public static final String XXXvid = "XXXvid";
	// Vl_titolo_bibCommonDao
	// FILTRI
	// public static final String XXXcd_polo = "XXXcd_polo";
	// Vl_titolo_claCommonDao
	// FILTRI
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXclasse = "XXXclasse";
	// Vl_titolo_luoCommonDao
	// FILTRI
	// public static final String XXXlid = "XXXlid";
	// Vl_titolo_marCommonDao
	// FILTRI
	// public static final String XXXmid = "XXXmid";

	// Vl_titolo_sogCommonDao
	// FILTRI
	// public static final String XXXcid = "XXXcid";
	// Vl_titolo_tit_bCommonDao
	// FILTRI
	public static final String XXXtp_legame_musica = "XXXtp_legame_musica";
	// public static final String XXXstringaClet1 = "XXXstringaClet1";
	// public static final String XXXstringaClet2 = "XXXstringaClet2";
	// public static final String XXXstringaEsatta1 = "XXXstringaEsatta1";
	// public static final String XXXstringaEsatta2 = "XXXstringaEsatta2";
	// public static final String XXXbid_base = "XXXbid_base";
	// public static final String XXXbid = "XXXbid";
	// public static final String XXXstringaLike1 = "XXXstringaLike1";
	// Vl_titolo_tit_cCommonDao
	// FILTRI
	// public static final String XXXbid_coll = "XXXbid_coll";
	// public static final String XXXcd_natura_base = "XXXcd_natura_base";

	// Tb_notaResult
	public static final String XXXtp_nota = "XXXtp_nota";
	// public static final String XXXbid = "XXXbid";
	public static final String XXXprogr_nota = "XXXprogr_nota";
	// Tb_numero_stdResult
	// public static final String XXXbid = "XXXbid";
	public static final String XXXnumero_std = "XXXnumero_std";
	// public static final String XXXtp_numero_std = "XXXtp_numero_std";
	// Tb_par_authResult
	public static final String XXXcd_par_auth = "XXXcd_par_auth";
	public static final String XXXid_parametro = "XXXid_parametro";
	// Tb_par_matResult
	public static final String XXXcd_par_mat = "XXXcd_par_mat";
	// public static final String XXXid_parametro = "XXXid_parametro";
	// Tb_par_semResult
	public static final String XXXcd_tabella_codici = "XXXcd_tabella_codici";
	// public static final String XXXid_parametro = "XXXid_parametro";
	public static final String XXXtp_tabella_codici = "XXXtp_tabella_codici";
	// Tb_parametroResult
	// public static final String XXXid_parametro = "XXXid_parametro";
	// Tb_personaggioResult
	public static final String XXXid_personaggio = "XXXid_personaggio";
	// public static final String XXXbid = "XXXbid";
	// Tb_rappresentResult
	// public static final String XXXbid = "XXXbid";
	// Tb_serverResult
	public static final String XXXid_server = "XXXid_server";
	// Tb_tp_attivitaResult
	public static final String XXXid_tipo_attivita = "XXXid_tipo_attivita";
	// Tb_tp_problemaResult
	public static final String XXXid_problema = "XXXid_problema";
	public static final String XXXds_tipo_problema = "XXXds_tipo_problema";
	// Tb_utenteResult
	public static final String XXXcd_utente_amm = "XXXcd_utente_amm";
	// Tr_att_attResult
	public static final String XXXid_tipo_att_base = "XXXid_tipo_att_base";
	public static final String XXXid_tipo_att_coll = "XXXid_tipo_att_coll";
	// Tr_aut_marResult
	// public static final String XXXmid = "XXXmid";
	// public static final String XXXvid = "XXXvid";
	// Tr_per_intResult
	// public static final String XXXid_personaggio = "XXXid_personaggio";
	// public static final String XXXvid = "XXXvid";
	// Tr_sog_desResult
	// public static final String XXXdid = "XXXdid";
	// public static final String XXXcid = "XXXcid";
	// Tr_tit_autResult
	// public static final String XXXvid = "XXXvid";
	// public static final String XXXbid = "XXXbid";
	// public static final String XXXcd_relazione = "XXXcd_relazione";
	// public static final String XXXtp_responsabilita = "XXXtp_responsabilita";
	// Tr_tit_claResult
	// public static final String XXXbid = "XXXbid";
	// public static final String XXXcd_edizione = "XXXcd_edizione";
	// public static final String XXXcd_sistema = "XXXcd_sistema";
	// public static final String XXXclasse = "XXXclasse";
	// Tr_tit_sog_bibResult
	// public static final String XXXcid = "XXXcid";
	// public static final String XXXbid = "XXXbid";

	// Ts_link_multimCommonDao
	public static final String XXXid_link_multim = "XXXid_link_multim";
	public static final String XXXky_link_multim = "XXXky_link_multim";

	// TABELLA ABSTRACT
	public static final String XXXds_abstract = "XXXds_abstract";

	// Variabili non gestite?
	public static final String XXXds_classe = "XXXds_classe";
	public static final String XXXfl_costruito = "XXXfl_costruito";
	public static final String XXXaa_comp_1 = "XXXaa_comp_1";
	public static final String XXXaa_comp_2 = "XXXaa_comp_2";
	public static final String XXXaa_pubb_2 = "XXXaa_pubb_2";
	public static final String XXXaa_rapp = "XXXaa_rapp";
	public static final String XXXalterazione = "XXXalterazione";
	public static final String XXXbibl = "XXXbibl";
	public static final String XXXbid_cm = "XXXbid_cm";
	public static final String XXXbid_com = "XXXbid_com";
	public static final String XXXbid_leg_marc = "XXXbid_leg_marc";
	public static final String XXXbid_letterario = "XXXbid_letterario";
	public static final String XXXbid_link = "XXXbid_link";
	public static final String XXXbid_marc = "XXXbid_marc";
	public static final String XXXbidvid = "XXXbidvid";
	public static final String XXXcampo1 = "XXXcampo1";
	public static final String XXXcampo2 = "XXXcampo2";
	public static final String XXXcampo3 = "XXXcampo3";
	public static final String XXXcd_agenzia = "XXXcd_agenzia";
	public static final String XXXcd_altitudine = "XXXcd_altitudine";
	public static final String XXXcd_base = "XXXcd_base";
	public static final String XXXcd_bit_immagine = "XXXcd_bit_immagine";
	public static final String XXXcd_broadcast = "XXXcd_broadcast";
	public static final String XXXcd_categ_colore = "XXXcd_categ_colore";
	public static final String XXXcd_categ_satellite = "XXXcd_categ_satellite";
	public static final String XXXcd_colore = "XXXcd_colore";
	public static final String XXXcd_completo = "XXXcd_completo";
	public static final String XXXcd_configurazione = "XXXcd_configurazione";
	public static final String XXXcd_contr_sim = "XXXcd_contr_sim";
	public static final String XXXcd_design_funz = "XXXcd_design_funz";
	public static final String XXXcd_designazione = "XXXcd_designazione";
	public static final String XXXcd_deteriore = "XXXcd_deteriore";
	public static final String XXXcd_dimensione = "XXXcd_dimensione";
	public static final String XXXcd_elementi = "XXXcd_elementi";
	public static final String XXXcd_emulsione = "XXXcd_emulsione";
	public static final String XXXcd_forma = "XXXcd_forma";
	public static final String XXXcd_forma_1 = "XXXcd_forma_1";
	public static final String XXXcd_forma_2 = "XXXcd_forma_2";
	public static final String XXXcd_forma_3 = "XXXcd_forma_3";
	public static final String XXXcd_forma_cart = "XXXcd_forma_cart";
	public static final String XXXcd_forma_pubb = "XXXcd_forma_pubb";
	public static final String XXXcd_forma_regist = "XXXcd_forma_regist";
	public static final String XXXcd_forma_ripr = "XXXcd_forma_ripr";
	public static final String XXXcd_forma_video = "XXXcd_forma_video";
	public static final String XXXcd_formato_file = "XXXcd_formato_file";
	public static final String XXXcd_generazione = "XXXcd_generazione";
	public static final String XXXcd_larg_nastro = "XXXcd_larg_nastro";
	public static final String XXXcd_liv_ade = "XXXcd_liv_ade";
	public static final String XXXcd_livello_aut = "XXXcd_livello_aut";
	public static final String XXXcd_livello_c = "XXXcd_livello_c";
	public static final String XXXcd_livello_g = "XXXcd_livello_g";
	public static final String XXXcd_livello_m = "XXXcd_livello_m";
	public static final String XXXcd_marc_21 = "XXXcd_marc_21";
	public static final String XXXcd_mat_accomp = "XXXcd_mat_accomp";
	public static final String XXXcd_mater_accomp = "XXXcd_mater_accomp";
	public static final String XXXcd_materia = "XXXcd_materia";
	public static final String XXXcd_materiale_base = "XXXcd_materiale_base";
	public static final String XXXcd_meridiano = "XXXcd_meridiano";
	public static final String XXXcd_natura_coll = "XXXcd_natura_coll";
	public static final String XXXcd_norme_cat = "XXXcd_norme_cat";
	public static final String XXXcd_oggetto = "XXXcd_oggetto";
	public static final String XXXcd_origine = "XXXcd_origine";
	public static final String XXXcd_pellicola = "XXXcd_pellicola";
	public static final String XXXcd_piattaforma = "XXXcd_piattaforma";
	public static final String XXXcd_pista = "XXXcd_pista";
	public static final String XXXcd_polarita = "XXXcd_polarita";
	public static final String XXXcd_presentazione = "XXXcd_presentazione";
	public static final String XXXcd_qualita = "XXXcd_qualita";
	public static final String XXXcd_record = "XXXcd_record";
	public static final String XXXcd_relazione_f = "XXXcd_relazione_f";
	public static final String XXXcd_riduzione = "XXXcd_riduzione";
	public static final String XXXcd_riduzione_spec = "XXXcd_riduzione_spec";
	public static final String XXXcd_riproduzione = "XXXcd_riproduzione";
	public static final String XXXcd_stato = "XXXcd_stato";
	public static final String XXXcd_stesura = "XXXcd_stesura";
	public static final String XXXcd_strumento_mus = "XXXcd_strumento_mus";
	public static final String XXXcd_suono = "XXXcd_suono";
	public static final String XXXcd_supporto = "XXXcd_supporto";
	public static final String XXXcd_supporto_fisico = "XXXcd_supporto_fisico";
	public static final String XXXcd_supporto_second = "XXXcd_supporto_second";
	public static final String XXXcd_tabella = "XXXcd_tabella";
	public static final String XXXcd_tecnica = "XXXcd_tecnica";
	public static final String XXXcd_tecnica_dis_1 = "XXXcd_tecnica_dis_1";
	public static final String XXXcd_tecnica_dis_2 = "XXXcd_tecnica_dis_2";
	public static final String XXXcd_tecnica_dis_3 = "XXXcd_tecnica_dis_3";
	public static final String XXXcd_tecnica_regis = "XXXcd_tecnica_regis";
	public static final String XXXcd_tecnica_sta_1 = "XXXcd_tecnica_sta_1";
	public static final String XXXcd_tecnica_sta_2 = "XXXcd_tecnica_sta_2";
	public static final String XXXcd_tecnica_sta_3 = "XXXcd_tecnica_sta_3";
	public static final String XXXcd_timbro_vocale = "XXXcd_timbro_vocale";
	public static final String XXXcd_tiposcala = "XXXcd_tiposcala";
	public static final String XXXcd_tonalita = "XXXcd_tonalita";
	public static final String XXXcd_trans = "XXXcd_trans";
	public static final String XXXcd_unimarc = "XXXcd_unimarc";
	public static final String XXXcd_utente = "XXXcd_utente";
	public static final String XXXcd_velocita = "XXXcd_velocita";
	public static final String XXXchiave_mus = "XXXchiave_mus";
	public static final String XXXcid_acc = "XXXcid_acc";
	public static final String XXXcid_var = "XXXcid_var";
	public static final String XXXdata = "XXXdata";
	public static final String XXXdata1 = "XXXdata1";
	public static final String XXXdatazione = "XXXdatazione";
	public static final String XXXdatazione_cm = "XXXdatazione_cm";
	public static final String XXXdatazione_m = "XXXdatazione_m";
	public static final String XXXdiagn = "XXXdiagn";
	public static final String XXXdid_2 = "XXXdid_2";
	public static final String XXXds_antica_segn = "XXXds_antica_segn";
	public static final String XXXds_att_att = "XXXds_att_att";
	public static final String XXXds_attivita = "XXXds_attivita";
	public static final String XXXds_biblioteca = "XXXds_biblioteca";
	public static final String XXXds_citta = "XXXds_citta";
	public static final String XXXds_conservazione = "XXXds_conservazione";
	public static final String XXXds_consistenza = "XXXds_consistenza";
	public static final String XXXds_contesto = "XXXds_contesto";
	public static final String XXXds_descrittore = "XXXds_descrittore";
	public static final String XXXds_gru_gru = "XXXds_gru_gru";
	public static final String XXXds_gruppo = "XXXds_gruppo";
	public static final String XXXds_illustrazioni = "XXXds_illustrazioni";
	public static final String XXXds_legatura = "XXXds_legatura";
	public static final String XXXds_luogo_rapp = "XXXds_luogo_rapp";
	public static final String XXXds_marca = "XXXds_marca";
	public static final String XXXds_motto = "XXXds_motto";
	public static final String XXXds_nota = "XXXds_nota";
	public static final String XXXds_occasione = "XXXds_occasione";
	public static final String XXXds_org_anal = "XXXds_org_anal";
	public static final String XXXds_org_anal_cm = "XXXds_org_anal_cm";
	public static final String XXXds_org_sint = "XXXds_org_sint";
	public static final String XXXds_org_sint_cm = "XXXds_org_sint_cm";
	public static final String XXXds_periodo = "XXXds_periodo";
	public static final String XXXds_polobib = "XXXds_polobib";
	public static final String XXXds_proposta = "XXXds_proposta";
	public static final String XXXds_repertorio = "XXXds_repertorio";
	public static final String XXXds_server = "XXXds_server";
	public static final String XXXds_sezioni = "XXXds_sezioni";
	public static final String XXXds_tabella = "XXXds_tabella";
	public static final String XXXds_teatro = "XXXds_teatro";
	public static final String XXXds_utente = "XXXds_utente";
	public static final String XXXdt_attivita = "XXXdt_attivita";
	public static final String XXXdt_fine_validita = "XXXdt_fine_validita";
	public static final String XXXdt_inoltro = "XXXdt_inoltro";
	public static final String XXXdt_ispezione = "XXXdt_ispezione";
	public static final String XXXe_mail_1 = "XXXe_mail_1";
	public static final String XXXe_mail_2 = "XXXe_mail_2";
	public static final String XXXfax = "XXXfax";
	public static final String XXXfl_abil_forzat = "XXXfl_abil_forzat";
	public static final String XXXfl_abil_legame = "XXXfl_abil_legame";
	public static final String XXXfl_allinea_cla = "XXXfl_allinea_cla";
	public static final String XXXfl_allinea_luo = "XXXfl_allinea_luo";
	public static final String XXXfl_allinea_rep = "XXXfl_allinea_rep";
	public static final String XXXfl_allinea_sog = "XXXfl_allinea_sog";
	public static final String XXXfl_aut_superflui = "XXXfl_aut_superflui";
	public static final String XXXfl_canc_m = "XXXfl_canc_m";
	public static final String XXXfl_composito = "XXXfl_composito";
	public static final String XXXfl_disp_elettr = "XXXfl_disp_elettr";
	public static final String XXXfl_esito = "XXXfl_esito";
	public static final String XXXfl_incerto = "XXXfl_incerto";
	public static final String XXXfl_leg_auth = "XXXfl_leg_auth";
	public static final String XXXfl_mutilo = "XXXfl_mutilo";
	public static final String XXXfl_palinsesto = "XXXfl_palinsesto";
	public static final String XXXfl_primavoce = "XXXfl_primavoce";
	public static final String XXXfl_speciale = "XXXfl_speciale";
	public static final String XXXfl_spogli = "XXXfl_spogli";
	public static final String XXXfl_sub_menu = "XXXfl_sub_menu";
	public static final String XXXfl_superfluo = "XXXfl_superfluo";
	public static final String XXXfl_superfluo_f = "XXXfl_superfluo_f";
	public static final String XXXfl_trovato = "XXXfl_trovato";
	public static final String XXXfreq = "XXXfreq";
	public static final String XXXfreq_attivita = "XXXfreq_attivita";
	public static final String XXXfreq_attivita_ok = "XXXfreq_attivita_ok";
	public static final String XXXgravita = "XXXgravita";
	public static final String XXXid = "XXXid";
	public static final String XXXid_att_pro = "XXXid_att_pro";
	public static final String XXXid_gruppo_base = "XXXid_gruppo_base";
	public static final String XXXid_gruppo_coll = "XXXid_gruppo_coll";
	public static final String XXXid_leg = "XXXid_leg";
	public static final String XXXid_oggetto = "XXXid_oggetto";
	public static final String XXXid_proposta = "XXXid_proposta";
	public static final String XXXid_script = "XXXid_script";
	public static final String XXXid_stop_list = "XXXid_stop_list";
	public static final String XXXid_tag_marc = "XXXid_tag_marc";
	public static final String XXXid_tim = "XXXid_tim";
	public static final String XXXid_tim_sto = "XXXid_tim_sto";
	public static final String XXXid_titolo_marc = "XXXid_titolo_marc";
	public static final String XXXidarrivo = "XXXidarrivo";
	public static final String XXXidindice = "XXXidindice";
	public static final String XXXidpartenza = "XXXidpartenza";
	public static final String XXXimmagine = "XXXimmagine";
	public static final String XXXindicatori = "XXXindicatori";
	public static final String XXXindice_isbd = "XXXindice_isbd";
	public static final String XXXkcerca = "XXXkcerca";
	public static final String XXXky_app_clet = "XXXky_app_clet";
	public static final String XXXky_app_den = "XXXky_app_den";
	public static final String XXXky_app_nor_pre = "XXXky_app_nor_pre";
	public static final String XXXky_app_pre = "XXXky_app_pre";
	public static final String XXXky_app_ric = "XXXky_app_ric";
	public static final String XXXky_auteur_f = "XXXky_auteur_f";
	public static final String XXXky_biblioteca = "XXXky_biblioteca";
	public static final String XXXky_cautun_f = "XXXky_cautun_f";
	public static final String XXXky_cles1_a_f = "XXXky_cles1_a_f";
	public static final String XXXky_cles1_ct = "XXXky_cles1_ct";
	public static final String XXXky_cles2_a_f = "XXXky_cles2_a_f";
	public static final String XXXky_cles2_ct = "XXXky_cles2_ct";
	public static final String XXXky_clet1_ct = "XXXky_clet1_ct";
	public static final String XXXky_clet2_ct = "XXXky_clet2_ct";
	public static final String XXXky_el1_a = "XXXky_el1_a";
	public static final String XXXky_el1_b = "XXXky_el1_b";
	public static final String XXXky_el2_a = "XXXky_el2_a";
	public static final String XXXky_el2_b = "XXXky_el2_b";
	public static final String XXXky_est_clet = "XXXky_est_clet";
	public static final String XXXky_est_den = "XXXky_est_den";
	public static final String XXXky_est_nor_pre = "XXXky_est_nor_pre";
	public static final String XXXky_est_pre = "XXXky_est_pre";
	public static final String XXXky_est_ric = "XXXky_est_ric";
	public static final String XXXky_luogo = "XXXky_luogo";
	public static final String XXXky_ord_clet = "XXXky_ord_clet";
	public static final String XXXky_ord_den = "XXXky_ord_den";
	public static final String XXXky_ord_nor_pre = "XXXky_ord_nor_pre";
	public static final String XXXky_ord_pre = "XXXky_ord_pre";
	public static final String XXXky_ord_ric = "XXXky_ord_ric";
	public static final String XXXlatitudine_nord = "XXXlatitudine_nord";
	public static final String XXXlatitudine_sud = "XXXlatitudine_sud";
	public static final String XXXlid_2 = "XXXlid_2";
	public static final String XXXlivelloaut = "XXXlivelloaut";
	public static final String XXXlivellogerarchico = "XXXlivellogerarchico";
	public static final String XXXlongitudine_est = "XXXlongitudine_est";
	public static final String XXXlongitudine_ovest = "XXXlongitudine_ovest";
	public static final String XXXlunghezza = "XXXlunghezza";
	public static final String XXXmateriale = "XXXmateriale";
	public static final String XXXmisura = "XXXmisura";
	public static final String XXXnatura = "XXXnatura";
	public static final String XXXnaturaleg = "XXXnaturaleg";
	public static final String XXXnome_personaggio = "XXXnome_personaggio";
	public static final String XXXnome_tag = "XXXnome_tag";
	public static final String XXXnome_tag_legame = "XXXnome_tag_legame";
	public static final String XXXnota_att_att = "XXXnota_att_att";
	public static final String XXXnota_att_pro = "XXXnota_att_pro";
	public static final String XXXnota_aut = "XXXnota_aut";
	public static final String XXXnota_aut_aut = "XXXnota_aut_aut";
	public static final String XXXnota_aut_mar = "XXXnota_aut_mar";
	public static final String XXXnota_cat_aut = "XXXnota_cat_aut";
	public static final String XXXnota_cat_tit = "XXXnota_cat_tit";
	public static final String XXXnota_descrittore = "XXXnota_descrittore";
	public static final String XXXnota_gru_gru = "XXXnota_gru_gru";
	public static final String XXXnota_gruppo = "XXXnota_gruppo";
	public static final String XXXnota_impronta = "XXXnota_impronta";
	public static final String XXXnota_inf_tit = "XXXnota_inf_tit";
	public static final String XXXnota_luogo = "XXXnota_luogo";
	public static final String XXXnota_marca = "XXXnota_marca";
	public static final String XXXnota_programma = "XXXnota_programma";
	public static final String XXXnota_rapp = "XXXnota_rapp";
	public static final String XXXnota_rep_mar = "XXXnota_rep_mar";
	public static final String XXXnota_rep_tit = "XXXnota_rep_tit";
	public static final String XXXnota_server = "XXXnota_server";
	public static final String XXXnota_stop_list = "XXXnota_stop_list";
	public static final String XXXnota_tipo_attivita = "XXXnota_tipo_attivita";
	public static final String XXXnota_tipo_problema = "XXXnota_tipo_problema";
	public static final String XXXnota_tit_aut = "XXXnota_tit_aut";
	public static final String XXXnota_tit_bib = "XXXnota_tit_bib";
	public static final String XXXnota_tit_luo = "XXXnota_tit_luo";
	public static final String XXXnota_tit_mar = "XXXnota_tit_mar";
	public static final String XXXnota_tit_tit = "XXXnota_tit_tit";
	public static final String XXXnota_utente = "XXXnota_utente";
	public static final String XXXnotaid = "XXXnotaid";
	public static final String XXXnotazione_musicale = "XXXnotazione_musicale";
	public static final String XXXnote_pro = "XXXnote_pro";
	public static final String XXXnote_rep_aut = "XXXnote_rep_aut";
	public static final String XXXnumero_cat_tem = "XXXnumero_cat_tem";
	public static final String XXXnumero_comp = "XXXnumero_comp";
	public static final String XXXnumero_lastra = "XXXnumero_lastra";
	public static final String XXXnumero_opera = "XXXnumero_opera";
	public static final String XXXnumero_ordine = "XXXnumero_ordine";
	public static final String XXXoggetto = "XXXoggetto";
	public static final String XXXparametri_input = "XXXparametri_input";
	public static final String XXXpolo = "XXXpolo";
	public static final String XXXprogr_proposta = "XXXprogr_proposta";
	public static final String XXXprogr_risposta = "XXXprogr_risposta";
	public static final String XXXregistro_mus = "XXXregistro_mus";
	public static final String XXXscala_oriz = "XXXscala_oriz";
	public static final String XXXscala_vert = "XXXscala_vert";
	public static final String XXXsequenza = "XXXsequenza";
	public static final String XXXsequenza_musica = "XXXsequenza_musica";
	public static final String XXXsottocampo = "XXXsottocampo";
	public static final String XXXstato = "XXXstato";
	public static final String XXXtelefono_1 = "XXXtelefono_1";
	public static final String XXXtelefono_2 = "XXXtelefono_2";
	public static final String XXXtempo_mus = "XXXtempo_mus";
	public static final String XXXtime_max = "XXXtime_max";
	public static final String XXXtime_med = "XXXtime_med";
	public static final String XXXtime_min = "XXXtime_min";
	public static final String XXXtipomateriale = "XXXtipomateriale";
	public static final String XXXtiporecord = "XXXtiporecord";
	public static final String XXXtp_abil_auth = "XXXtp_abil_auth";
	public static final String XXXtp_abilitaz = "XXXtp_abilitaz";
	public static final String XXXtp_all_pref = "XXXtp_all_pref";
	public static final String XXXtp_attivita = "XXXtp_attivita";
	public static final String XXXtp_digitalizz = "XXXtp_digitalizz";
	public static final String XXXtp_disco = "XXXtp_disco";
	public static final String XXXtp_elaborazione = "XXXtp_elaborazione";
	public static final String XXXtp_file = "XXXtp_file";
	public static final String XXXtp_forma = "XXXtp_forma";
	public static final String XXXtp_forma_des = "XXXtp_forma_des";
	public static final String XXXtp_formato_film = "XXXtp_formato_film";
	public static final String XXXtp_formato_video = "XXXtp_formato_video";
	public static final String XXXtp_generazione = "XXXtp_generazione";
	public static final String XXXtp_genere = "XXXtp_genere";
	public static final String XXXtp_id = "XXXtp_id";
	public static final String XXXtp_immagine = "XXXtp_immagine";
	public static final String XXXtp_indicatore = "XXXtp_indicatore";
	public static final String XXXtp_link = "XXXtp_link";
	public static final String XXXtp_mater_audiovis = "XXXtp_mater_audiovis";
	public static final String XXXtp_materiale_1 = "XXXtp_materiale_1";
	public static final String XXXtp_materiale_2 = "XXXtp_materiale_2";
	public static final String XXXtp_materiale_3 = "XXXtp_materiale_3";
	public static final String XXXtp_materiale_gra = "XXXtp_materiale_gra";
	public static final String XXXtp_media_suono = "XXXtp_media_suono";
	public static final String XXXtp_messaggio = "XXXtp_messaggio";
	public static final String XXXtp_operazione = "XXXtp_operazione";
	public static final String XXXtp_pubb_gov = "XXXtp_pubb_gov";
	public static final String XXXtp_repertorio = "XXXtp_repertorio";
	public static final String XXXtp_responsabilita_f = "XXXtp_responsabilita_f";
	public static final String XXXtp_ret_doc = "XXXtp_ret_doc";
	public static final String XXXtp_scala = "XXXtp_scala";
	public static final String XXXtp_stampa_film = "XXXtp_stampa_film";
	public static final String XXXtp_stop_list = "XXXtp_stop_list";
	public static final String XXXtp_sub_menu = "XXXtp_sub_menu";
	public static final String XXXtp_suono = "XXXtp_suono";
	public static final String XXXtp_tabella = "XXXtp_tabella";
	public static final String XXXtp_taglio = "XXXtp_taglio";
	public static final String XXXtp_testo_letter = "XXXtp_testo_letter";
	public static final String XXXtrans = "XXXtrans";
	public static final String XXXts_end = "XXXts_end";
	public static final String XXXts_ins = "XXXts_ins";
	public static final String XXXts_ins_m = "XXXts_ins_m";
	public static final String XXXts_ins_tit_aut = "XXXts_ins_tit_aut";
	public static final String XXXts_note_proposta = "XXXts_note_proposta";
	public static final String XXXts_start = "XXXts_start";
	public static final String XXXts_var_m = "XXXts_var_m";
	public static final String XXXts_var_tit_aut = "XXXts_var_tit_aut";
	public static final String XXXuri_copia = "XXXuri_copia";
	public static final String XXXuri_multim = "XXXuri_multim";
	public static final String XXXute_destinatario = "XXXute_destinatario";
	public static final String XXXute_forza_ins = "XXXute_forza_ins";
	public static final String XXXute_forza_var = "XXXute_forza_var";
	public static final String XXXute_ins_m = "XXXute_ins_m";
	public static final String XXXute_mittente = "XXXute_mittente";
	public static final String XXXute_var_m = "XXXute_var_m";
	public static final String XXXvalore_sottocampo = "XXXvalore_sottocampo";
	public static final String XXXvid_f = "XXXvid_f";
	public static final String XXXvid_link = "XXXvid_link";
	public static final int XXXcdBiblio = 0;
	public static final String XXXcdPaese = "XXXcdPaese";
	public static final String XXXtipoData = "XXXtipoData";
	public static final String XXXcdPolo = "XXXcdPolo";
	public static final String XXXcdBiblioteca = "XXXcdBiblioteca";
	public static final String XXXpossesso = "XXXpossesso";
	public static final String XXXgestione = "XXXgestione";
	public static final String XXXky_cles2_2null = "XXXky_cles2_2null";
	public static final String XXXvid_base_new = "XXXvid_base_new";
	public static final String XXXvid_coll_new = "XXXvid_coll_new";
	public static final String XXXdataComposizione = "XXXdataComposizione";
	public static final String XXXStringaEsatta = "XXXStringaEsatta";
	public static final String XXXStringaLike = "XXXStringaLike";
	public static final String XXXcd_sistemaPartenza = "XXXcd_sistemaPartenza";
	public static final String XXXcd_edizionePartenza = "XXXcd_edizionePartenza";
	public static final String XXXclassePartenza = "XXXclassePartenza";
	public static final String XXXcd_sistemaArrivo = "XXXcd_sistemaArrivo";
	public static final String XXXcd_edizioneArrivo = "XXXcd_edizioneArrivo";
	public static final String XXXclasseArrivo = "XXXclasseArrivo";
	public static final String XXXinizio_intervallo = "XXXinizio_intervallo";
	public static final String XXXfine_intervallo = "XXXfine_intervallo";
	public static final String XXXid_proposta_string = "XXXid_proposta_string";
	public static final String XXXdata_Da = "XXXdata_Da";
	public static final String XXXdata_A = "XXXdata_A";
	public static final String XXXidPartenza = "XXXidPartenza";
	public static final String XXXidArrivo = "XXXidArrivo";
	public static final String XXXOPAC = "XXXOPAC";
	public static final String XXXNOOPAC = "XXXNOOPAC";

	// almaviva5_20091007 ricerca titoli con filtro su loc. possesso
	public static final String XXXcercaTitLocBib = "XXXcercaTitLocBib";

	// almaviva5_20091030 ricerca soggettazione solo se soggettario gestiti in
	// biblioteca (fl_att==1)
	public static final String XXXcercaPerFiltroUteSoggGestione = "XXXcercaPerFiltroUteSogg";

	// almaviva5_20091117 ricerca soggetti solo se soggettario visibile in biblioteca
	public static final String XXXcercaPerFiltroSoggInBib = "XXXcercaPerFiltroSoggInBib";

	// almaviva5_20091125 ricerca solo soggetti utilizzati (legati a titoli localizzati)
	public static final String XXXcercaPerFiltroSoggUtilizzatiInBib = "XXXcercaPerFiltroSoggUtilizzatiInBib";

	// almaviva5_20120322 evolutive CFI
	public static final String XXXcd_edizione_IN = "XXXcd_edizione_IN";
	public static final String XXXdid_IN = "XXXdid_IN";

	// almaviva5_20150224 evolutiva area zero
	//filtri area comune
	public static final String XXXtestoLetterarioModerno = "XXXtestoLetterarioModerno";
	public static final String XXXtestoLetterarioAntico = "XXXtestoLetterarioAntico";
	public static final String XXXformaContenuto = "XXXformaContenuto";
	public static final String XXXtipoMediazione = "XXXtipoMediazione";

	//filtri audiovisivo
	public static final String XXXtipoVideo = "XXXtipoVideo";
	public static final String XXXformaPubblicazioneDistribuzione = "XXXformaPubblicazioneDistribuzione";
	public static final String XXXtecnicaVideoregistrazione = "XXXtecnicaVideoregistrazione";
	public static final String XXXformaPubblicazione = "XXXformaPubblicazione";
	public static final String XXXvelocita = "XXXvelocita";

	//almaviva5_20150923 sbnmarc v2.01
	public static final String XXXnota_rep_luo = "XXXnota_rep_luo";
	public static final String XXXs183_tp_supporto_1 = "XXXs183_tp_supporto_1";
	public static final String XXXs183_tp_supporto_2 = "XXXs183_tp_supporto_2";
	public static final String XXXnota_catalogatore = "XXXnota_catalogatore";

	//almaviva5_20160718 sbnmarc v2.03
	//public static final String XXXs231_nome_sezione = "XXXs231_nome_sezione";
	public static final String XXXs231_data_opera = "XXXs231_data_opera";
	//public static final String XXXs231_lingua_originale = "XXXs231_lingua_originale";
	public static final String XXXs231_altre_caratteristiche = "XXXs231_altre_caratteristiche";
	//public static final String XXXs231_numero_sezione = "XXXs231_numero_sezione";
	public static final String XXXs231_forma_opera = "XXXs231_forma_opera";
	//public static final String XXXs231_paese_opera = "XXXs231_paese_opera";
	public static final String XXXs210_ind_pubblicato = "XXXs210_ind_pubblicato";


}
