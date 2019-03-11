/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 *
 * This is an automatic generated file. It will be regenerated every time
 * you generate persistence class.
 *
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: almaviva
 * License Type: Purchased
 */
package it.iccu.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * BIBLIOTECHE ABILITATE AD OPERARE NEL POLO (TPFSBI)
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_biblioteca_in_polo implements Serializable {

	private static final long serialVersionUID = 8174001268179823827L;

	public Tbf_biblioteca_in_polo() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_biblioteca_in_polo))
			return false;
		Tbf_biblioteca_in_polo tbf_biblioteca_in_polo = (Tbf_biblioteca_in_polo)aObj;
		if ((getCd_biblioteca() != null && !getCd_biblioteca().equals(tbf_biblioteca_in_polo.getCd_biblioteca())) || (getCd_biblioteca() == null && tbf_biblioteca_in_polo.getCd_biblioteca() != null))
			return false;
		if (getCd_polo() == null && tbf_biblioteca_in_polo.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(tbf_biblioteca_in_polo.getCd_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCd_biblioteca() == null ? 0 : getCd_biblioteca().hashCode());
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getORMID() == null ? 0 : getCd_polo().getORMID().hashCode());
		}
		return hashcode;
	}

	private String cd_biblioteca;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro id_parametro;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_polo cd_polo;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita id_gruppo_attivita_polo;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca id_biblioteca;

	private String ky_biblioteca;

	private String cd_ana_biblioteca;

	private String ds_biblioteca;

	private String ds_citta;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private String cd_sistema_metropolitano;

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_default_inven_schede Tbc_default_inven_schede;

	private java.util.Set Tbc_esemplare_titolo = new java.util.HashSet();

	private java.util.Set Tbc_provenienza_inventario = new java.util.HashSet();

	private java.util.Set Tbc_serie_inventariale = new java.util.HashSet();

	private java.util.Set Tbc_sezione_collocazione = new java.util.HashSet();

	private java.util.Set Tbf_contatore = new java.util.HashSet();

	private java.util.Set Tbf_profilo_abilitazione = new java.util.HashSet();

	private java.util.Set Tr_sistemi_classi_biblioteche = new java.util.HashSet();

	private java.util.Set Tr_soggettari_biblioteche = new java.util.HashSet();

	private java.util.Set Tr_thesauri_biblioteche = new java.util.HashSet();

	private java.util.Set Tr_aut_bib = new java.util.HashSet();

	private java.util.Set Tr_mar_bib = new java.util.HashSet();

	private java.util.Set Tr_tit_bib = new java.util.HashSet();

	private java.util.Set Trf_utente_professionale_biblioteca = new java.util.HashSet();

	private java.util.Set Tbf_modelli_stampe = new java.util.HashSet();

	private java.util.Set Tba_buono_ordine = new java.util.HashSet();

	private java.util.Set Tba_cambi_ufficiali = new java.util.HashSet();

	private java.util.Set Tba_fatture = new java.util.HashSet();

	private java.util.Set Tba_offerte_fornitore = new java.util.HashSet();

	private java.util.Set Tba_ordini = new java.util.HashSet();

	private java.util.Set Tba_sez_acquis_bibliografiche = new java.util.HashSet();

	private java.util.Set Tba_suggerimenti_bibliografici = new java.util.HashSet();

	private java.util.Set Tba_righe_fatture = new java.util.HashSet();

	private java.util.Set Tba_richieste_offerta = new java.util.HashSet();

	private java.util.Set Tba_parametri_buono_ordine = new java.util.HashSet();

	private java.util.Set Tbb_capitoli_bilanci = new java.util.HashSet();

	private java.util.Set Tbr_fornitori_biblioteche = new java.util.HashSet();

	private java.util.Set Tra_elementi_buono_ordine = new java.util.HashSet();

	private java.util.Set Tra_fornitori_offerte = new java.util.HashSet();

	private java.util.Set Tra_ordini_biblioteche = new java.util.HashSet();

	private java.util.Set Tra_sez_acquisizione_fornitori = new java.util.HashSet();

	private java.util.Set Tbl_documenti_lettori = new java.util.HashSet();

	private java.util.Set Tbl_utenti = new java.util.HashSet();

	private java.util.Set Tbl_modalita_pagamento = new java.util.HashSet();

	private java.util.Set Tbl_sale = new java.util.HashSet();

	private java.util.Set Tbl_supporti_biblioteca = new java.util.HashSet();

	private java.util.Set Tbl_modalita_erogazione = new java.util.HashSet();

	private java.util.Set Tbl_disponibilita_precatalogati = new java.util.HashSet();

	private java.util.Set Tbl_tipi_autorizzazioni = new java.util.HashSet();

	private java.util.Set Tbl_tipo_servizio = new java.util.HashSet();

	private java.util.Set Tbl_materie = new java.util.HashSet();

	private java.util.Set Tbl_occupazioni = new java.util.HashSet();

	private it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca Trl_utenti_biblioteca;

	private it.iccu.sbn.polo.orm.servizi.Tbl_parametri_biblioteca Tbl_parametri_biblioteca;

	private java.util.Set Tbl_specificita_titoli_studio = new java.util.HashSet();

	private java.util.Set Tbf_biblioteca_default = new java.util.HashSet();

	private java.util.Set Trf_attivita_affiliate = new java.util.HashSet();

	private java.util.Set Trf_attivita_affiliate1 = new java.util.HashSet();

	private java.util.Set Tbf_intranet_range = new java.util.HashSet();

	public void setCd_biblioteca(String value) {
		this.cd_biblioteca = value;
	}

	public String getCd_biblioteca() {
		return cd_biblioteca;
	}

	public void setKy_biblioteca(String value) {
		this.ky_biblioteca = value;
	}

	public String getKy_biblioteca() {
		return ky_biblioteca;
	}

	public void setCd_ana_biblioteca(String value) {
		this.cd_ana_biblioteca = value;
	}

	public String getCd_ana_biblioteca() {
		return cd_ana_biblioteca;
	}

	public void setDs_biblioteca(String value) {
		this.ds_biblioteca = value;
	}

	public String getDs_biblioteca() {
		return ds_biblioteca;
	}

	public void setDs_citta(String value) {
		this.ds_citta = value;
	}

	public String getDs_citta() {
		return ds_citta;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * Timestamp di inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * Timestamp di inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public void setUte_var(String value) {
		this.ute_var = value;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public String getUte_var() {
		return ute_var;
	}

	/**
	 * Timestamp di variazione
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * Timestamp di variazione
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	/**
	 * Flag di cancellazione logica
	 */
	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	/**
	 * Flag di cancellazione logica
	 */
	public char getFl_canc() {
		return fl_canc;
	}

	public void setId_biblioteca(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca value) {
		this.id_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca getId_biblioteca() {
		return id_biblioteca;
	}

	public void setId_gruppo_attivita_polo(it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita value) {
		this.id_gruppo_attivita_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita getId_gruppo_attivita_polo() {
		return id_gruppo_attivita_polo;
	}

	public void setCd_polo(it.iccu.sbn.polo.orm.amministrazione.Tbf_polo value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_polo getCd_polo() {
		return cd_polo;
	}

	public void setId_parametro(it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro value) {
		this.id_parametro = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro getId_parametro() {
		return id_parametro;
	}

	public void setTbc_default_inven_schede(it.iccu.sbn.polo.orm.documentofisico.Tbc_default_inven_schede value) {
		this.Tbc_default_inven_schede = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_default_inven_schede getTbc_default_inven_schede() {
		return Tbc_default_inven_schede;
	}

	public void setTbc_esemplare_titolo(java.util.Set value) {
		this.Tbc_esemplare_titolo = value;
	}

	public java.util.Set getTbc_esemplare_titolo() {
		return Tbc_esemplare_titolo;
	}

	public void setTbc_provenienza_inventario(java.util.Set value) {
		this.Tbc_provenienza_inventario = value;
	}

	public java.util.Set getTbc_provenienza_inventario() {
		return Tbc_provenienza_inventario;
	}


	public void setTbc_serie_inventariale(java.util.Set value) {
		this.Tbc_serie_inventariale = value;
	}

	public java.util.Set getTbc_serie_inventariale() {
		return Tbc_serie_inventariale;
	}


	public void setTbc_sezione_collocazione(java.util.Set value) {
		this.Tbc_sezione_collocazione = value;
	}

	public java.util.Set getTbc_sezione_collocazione() {
		return Tbc_sezione_collocazione;
	}


	public void setTbf_contatore(java.util.Set value) {
		this.Tbf_contatore = value;
	}

	public java.util.Set getTbf_contatore() {
		return Tbf_contatore;
	}


	public void setTbf_profilo_abilitazione(java.util.Set value) {
		this.Tbf_profilo_abilitazione = value;
	}

	public java.util.Set getTbf_profilo_abilitazione() {
		return Tbf_profilo_abilitazione;
	}


	public void setTr_sistemi_classi_biblioteche(java.util.Set value) {
		this.Tr_sistemi_classi_biblioteche = value;
	}

	public java.util.Set getTr_sistemi_classi_biblioteche() {
		return Tr_sistemi_classi_biblioteche;
	}


	public void setTr_soggettari_biblioteche(java.util.Set value) {
		this.Tr_soggettari_biblioteche = value;
	}

	public java.util.Set getTr_soggettari_biblioteche() {
		return Tr_soggettari_biblioteche;
	}


	public void setTr_thesauri_biblioteche(java.util.Set value) {
		this.Tr_thesauri_biblioteche = value;
	}

	public java.util.Set getTr_thesauri_biblioteche() {
		return Tr_thesauri_biblioteche;
	}


	public void setTr_aut_bib(java.util.Set value) {
		this.Tr_aut_bib = value;
	}

	public java.util.Set getTr_aut_bib() {
		return Tr_aut_bib;
	}


	public void setTr_mar_bib(java.util.Set value) {
		this.Tr_mar_bib = value;
	}

	public java.util.Set getTr_mar_bib() {
		return Tr_mar_bib;
	}


	public void setTr_tit_bib(java.util.Set value) {
		this.Tr_tit_bib = value;
	}

	public java.util.Set getTr_tit_bib() {
		return Tr_tit_bib;
	}


	public void setTrf_utente_professionale_biblioteca(java.util.Set value) {
		this.Trf_utente_professionale_biblioteca = value;
	}

	public java.util.Set getTrf_utente_professionale_biblioteca() {
		return Trf_utente_professionale_biblioteca;
	}

	public void setTba_buono_ordine(java.util.Set value) {
		this.Tba_buono_ordine = value;
	}

	public java.util.Set getTba_buono_ordine() {
		return Tba_buono_ordine;
	}


	public void setTba_cambi_ufficiali(java.util.Set value) {
		this.Tba_cambi_ufficiali = value;
	}

	public java.util.Set getTba_cambi_ufficiali() {
		return Tba_cambi_ufficiali;
	}


	public void setTba_fatture(java.util.Set value) {
		this.Tba_fatture = value;
	}

	public java.util.Set getTba_fatture() {
		return Tba_fatture;
	}


	public void setTba_offerte_fornitore(java.util.Set value) {
		this.Tba_offerte_fornitore = value;
	}

	public java.util.Set getTba_offerte_fornitore() {
		return Tba_offerte_fornitore;
	}


	public void setTba_ordini(java.util.Set value) {
		this.Tba_ordini = value;
	}

	public java.util.Set getTba_ordini() {
		return Tba_ordini;
	}


	public void setTba_sez_acquis_bibliografiche(java.util.Set value) {
		this.Tba_sez_acquis_bibliografiche = value;
	}

	public java.util.Set getTba_sez_acquis_bibliografiche() {
		return Tba_sez_acquis_bibliografiche;
	}


	public void setTba_suggerimenti_bibliografici(java.util.Set value) {
		this.Tba_suggerimenti_bibliografici = value;
	}

	public java.util.Set getTba_suggerimenti_bibliografici() {
		return Tba_suggerimenti_bibliografici;
	}


	public void setTba_righe_fatture(java.util.Set value) {
		this.Tba_righe_fatture = value;
	}

	public java.util.Set getTba_righe_fatture() {
		return Tba_righe_fatture;
	}


	public void setTba_richieste_offerta(java.util.Set value) {
		this.Tba_richieste_offerta = value;
	}

	public java.util.Set getTba_richieste_offerta() {
		return Tba_richieste_offerta;
	}


	public void setTba_parametri_buono_ordine(java.util.Set value) {
		this.Tba_parametri_buono_ordine = value;
	}

	public java.util.Set getTba_parametri_buono_ordine() {
		return Tba_parametri_buono_ordine;
	}


	public void setTbb_capitoli_bilanci(java.util.Set value) {
		this.Tbb_capitoli_bilanci = value;
	}

	public java.util.Set getTbb_capitoli_bilanci() {
		return Tbb_capitoli_bilanci;
	}


	public void setTbr_fornitori_biblioteche(java.util.Set value) {
		this.Tbr_fornitori_biblioteche = value;
	}

	public java.util.Set getTbr_fornitori_biblioteche() {
		return Tbr_fornitori_biblioteche;
	}


	public void setTra_elementi_buono_ordine(java.util.Set value) {
		this.Tra_elementi_buono_ordine = value;
	}

	public java.util.Set getTra_elementi_buono_ordine() {
		return Tra_elementi_buono_ordine;
	}


	public void setTra_fornitori_offerte(java.util.Set value) {
		this.Tra_fornitori_offerte = value;
	}

	public java.util.Set getTra_fornitori_offerte() {
		return Tra_fornitori_offerte;
	}


	public void setTra_ordini_biblioteche(java.util.Set value) {
		this.Tra_ordini_biblioteche = value;
	}

	public java.util.Set getTra_ordini_biblioteche() {
		return Tra_ordini_biblioteche;
	}


	public void setTra_sez_acquisizione_fornitori(java.util.Set value) {
		this.Tra_sez_acquisizione_fornitori = value;
	}

	public java.util.Set getTra_sez_acquisizione_fornitori() {
		return Tra_sez_acquisizione_fornitori;
	}


	public void setTbl_documenti_lettori(java.util.Set value) {
		this.Tbl_documenti_lettori = value;
	}

	public java.util.Set getTbl_documenti_lettori() {
		return Tbl_documenti_lettori;
	}


	public void setTbl_utenti(java.util.Set value) {
		this.Tbl_utenti = value;
	}

	public java.util.Set getTbl_utenti() {
		return Tbl_utenti;
	}


	public void setTbl_modalita_pagamento(java.util.Set value) {
		this.Tbl_modalita_pagamento = value;
	}

	public java.util.Set getTbl_modalita_pagamento() {
		return Tbl_modalita_pagamento;
	}


	public void setTbl_sale(java.util.Set value) {
		this.Tbl_sale = value;
	}

	public java.util.Set getTbl_sale() {
		return Tbl_sale;
	}


	public void setTbl_supporti_biblioteca(java.util.Set value) {
		this.Tbl_supporti_biblioteca = value;
	}

	public java.util.Set getTbl_supporti_biblioteca() {
		return Tbl_supporti_biblioteca;
	}


	public void setTbl_disponibilita_precatalogati(java.util.Set value) {
		this.Tbl_disponibilita_precatalogati = value;
	}

	public java.util.Set getTbl_disponibilita_precatalogati() {
		return Tbl_disponibilita_precatalogati;
	}


	public void setTbl_tipi_autorizzazioni(java.util.Set value) {
		this.Tbl_tipi_autorizzazioni = value;
	}

	public java.util.Set getTbl_tipi_autorizzazioni() {
		return Tbl_tipi_autorizzazioni;
	}


	public void setTbl_tipo_servizio(java.util.Set value) {
		this.Tbl_tipo_servizio = value;
	}

	public java.util.Set getTbl_tipo_servizio() {
		return Tbl_tipo_servizio;
	}


	public void setTbl_materie(java.util.Set value) {
		this.Tbl_materie = value;
	}

	public java.util.Set getTbl_materie() {
		return Tbl_materie;
	}


	public void setTbl_occupazioni(java.util.Set value) {
		this.Tbl_occupazioni = value;
	}

	public java.util.Set getTbl_occupazioni() {
		return Tbl_occupazioni;
	}


	public void setTrl_utenti_biblioteca(it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca value) {
		this.Trl_utenti_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca getTrl_utenti_biblioteca() {
		return Trl_utenti_biblioteca;
	}

	public void setTbl_parametri_biblioteca(it.iccu.sbn.polo.orm.servizi.Tbl_parametri_biblioteca value) {
		this.Tbl_parametri_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_parametri_biblioteca getTbl_parametri_biblioteca() {
		return Tbl_parametri_biblioteca;
	}

	public void setTbl_specificita_titoli_studio(java.util.Set value) {
		this.Tbl_specificita_titoli_studio = value;
	}

	public java.util.Set getTbl_specificita_titoli_studio() {
		return Tbl_specificita_titoli_studio;
	}

	public void setTbf_biblioteca_default(java.util.Set value) {
		this.Tbf_biblioteca_default = value;
	}

	public java.util.Set getTbf_biblioteca_default() {
		return Tbf_biblioteca_default;
	}

	public String toString() {
		return String.valueOf(getCd_biblioteca() + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getORMID())));
	}

	private boolean _saved = false;

	public void onSave() {
		_saved=true;
	}


	public void onLoad() {
		_saved=true;
	}


	public boolean isSaved() {
		return _saved;
	}

	public java.util.Set getTbf_modelli_stampe() {
		return Tbf_modelli_stampe;
	}

	public void setTbf_modelli_stampe(java.util.Set tbf_modelli_stampe) {
		Tbf_modelli_stampe = tbf_modelli_stampe;
	}

	public java.util.Set getTrf_attivita_affiliate() {
		return Trf_attivita_affiliate;
	}

	public void setTrf_attivita_affiliate(java.util.Set trf_attivita_affiliate) {
		Trf_attivita_affiliate = trf_attivita_affiliate;
	}

	public java.util.Set getTrf_attivita_affiliate1() {
		return Trf_attivita_affiliate1;
	}

	public void setTrf_attivita_affiliate1(java.util.Set trf_attivita_affiliate1) {
		Trf_attivita_affiliate1 = trf_attivita_affiliate1;
	}

	public java.util.Set getTbl_modalita_erogazione() {
		return Tbl_modalita_erogazione;
	}

	public void setTbl_modalita_erogazione(java.util.Set tbl_modalita_erogazione) {
		Tbl_modalita_erogazione = tbl_modalita_erogazione;
	}

	public String getCd_sistema_metropolitano() {
		return cd_sistema_metropolitano;
	}

	public void setCd_sistema_metropolitano(String cdSistemaMetropolitano) {
		cd_sistema_metropolitano = cdSistemaMetropolitano;
	}

	public java.util.Set getTbf_intranet_range() {
		return Tbf_intranet_range;
	}

	public void setTbf_intranet_range(java.util.Set tbfIntranetRange) {
		Tbf_intranet_range = tbfIntranetRange;
	}


}
