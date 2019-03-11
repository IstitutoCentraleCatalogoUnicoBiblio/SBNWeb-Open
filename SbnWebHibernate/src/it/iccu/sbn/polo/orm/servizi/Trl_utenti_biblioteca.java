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
package it.iccu.sbn.polo.orm.servizi;

import java.io.Serializable;
/**
 * Utenti di biblioteca
 */
/**
 * ORM-Persistable Class
 */
public class Trl_utenti_biblioteca implements Serializable {

	private static final long serialVersionUID = -4709646825784939302L;

	public Trl_utenti_biblioteca() {
	}

	private int id_utenti_biblioteca;

	private it.iccu.sbn.polo.orm.servizi.Tbl_utenti id_utenti;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private it.iccu.sbn.polo.orm.servizi.Tbl_specificita_titoli_studio id_specificita_titoli_studio;

	private it.iccu.sbn.polo.orm.servizi.Tbl_occupazioni id_occupazioni;

	private java.math.BigDecimal credito_bib;

	private String note_bib;

	private java.util.Date data_inizio_aut;

	private java.util.Date data_fine_aut;

	private java.util.Date data_inizio_sosp;

	private java.util.Date data_fine_sosp;

	private String cod_tipo_aut;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tbl_richiesta_servizio = new java.util.HashSet();

	private java.util.Set Trl_posti_sala_utenti_biblioteca = new java.util.HashSet();

	private void setId_utenti_biblioteca(int value) {
		this.id_utenti_biblioteca = value;
	}

	public int getId_utenti_biblioteca() {
		return id_utenti_biblioteca;
	}

	public int getORMID() {
		return getId_utenti_biblioteca();
	}

	/**
	 * credito a disposizione dell'utente per i servizi di biblioteca
	 */
	public void setCredito_bib(java.math.BigDecimal value) {
		this.credito_bib = value;
	}

	/**
	 * credito a disposizione dell'utente per i servizi di biblioteca
	 */
	public java.math.BigDecimal getCredito_bib() {
		return credito_bib;
	}

	/**
	 * note della biblioteca riguardanti l'utente
	 */
	public void setNote_bib(String value) {
		this.note_bib = value;
	}

	/**
	 * note della biblioteca riguardanti l'utente
	 */
	public String getNote_bib() {
		return note_bib;
	}

	/**
	 * data d'inizio dell'autorizzazione concessa all'utente
	 */
	public void setData_inizio_aut(java.util.Date value) {
		this.data_inizio_aut = value;
	}

	/**
	 * data d'inizio dell'autorizzazione concessa all'utente
	 */
	public java.util.Date getData_inizio_aut() {
		return data_inizio_aut;
	}

	/**
	 * data di scadenza dell'autorizzazione concessa all'utente
	 */
	public void setData_fine_aut(java.util.Date value) {
		this.data_fine_aut = value;
	}

	/**
	 * data di scadenza dell'autorizzazione concessa all'utente
	 */
	public java.util.Date getData_fine_aut() {
		return data_fine_aut;
	}

	/**
	 * data di inizio della sospensione dell'utente. la sospensione a questo livello disabilita tutti i servizi.
	 */
	public void setData_inizio_sosp(java.util.Date value) {
		this.data_inizio_sosp = value;
	}

	/**
	 * data di inizio della sospensione dell'utente. la sospensione a questo livello disabilita tutti i servizi.
	 */
	public java.util.Date getData_inizio_sosp() {
		return data_inizio_sosp;
	}

	/**
	 * data di scadenza  della sospensione  dell'utente. la scadenza di tale sospensione riabilita l'utente a tutti i servizi
	 */
	public void setData_fine_sosp(java.util.Date value) {
		this.data_fine_sosp = value;
	}

	/**
	 * data di scadenza  della sospensione  dell'utente. la scadenza di tale sospensione riabilita l'utente a tutti i servizi
	 */
	public java.util.Date getData_fine_sosp() {
		return data_fine_sosp;
	}

	/**
	 * codice identificativo del tipo autorizzazione
	 */
	public void setCod_tipo_aut(String value) {
		this.cod_tipo_aut = value;
	}

	/**
	 * codice identificativo del tipo autorizzazione
	 */
	public String getCod_tipo_aut() {
		return cod_tipo_aut;
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
	 * utente che varia
	 */
	public void setUte_var(String value) {
		this.ute_var = value;
	}

	/**
	 * utente che varia
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
	 * Flag di cancellazione logica valori ammessi S/N
	 */
	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	/**
	 * Flag di cancellazione logica valori ammessi S/N
	 */
	public char getFl_canc() {
		return fl_canc;
	}

	public void setCd_biblioteca(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_biblioteca() {
		return cd_biblioteca;
	}

	public void setId_utenti(it.iccu.sbn.polo.orm.servizi.Tbl_utenti value) {
		this.id_utenti = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_utenti getId_utenti() {
		return id_utenti;
	}

	public void setId_occupazioni(it.iccu.sbn.polo.orm.servizi.Tbl_occupazioni value) {
		this.id_occupazioni = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_occupazioni getId_occupazioni() {
		return id_occupazioni;
	}

	public void setId_specificita_titoli_studio(it.iccu.sbn.polo.orm.servizi.Tbl_specificita_titoli_studio value) {
		this.id_specificita_titoli_studio = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_specificita_titoli_studio getId_specificita_titoli_studio() {
		return id_specificita_titoli_studio;
	}

	public void setTbl_richiesta_servizio(java.util.Set value) {
		this.Tbl_richiesta_servizio = value;
	}

	public java.util.Set getTbl_richiesta_servizio() {
		return Tbl_richiesta_servizio;
	}


	public void setTrl_posti_sala_utenti_biblioteca(java.util.Set value) {
		this.Trl_posti_sala_utenti_biblioteca = value;
	}

	public java.util.Set getTrl_posti_sala_utenti_biblioteca() {
		return Trl_posti_sala_utenti_biblioteca;
	}


	public String toString() {
		return String.valueOf(getId_utenti_biblioteca());
	}

}
