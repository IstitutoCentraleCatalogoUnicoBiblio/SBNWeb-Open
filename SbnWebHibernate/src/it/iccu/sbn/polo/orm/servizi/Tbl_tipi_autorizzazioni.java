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
 * PROFILI DI AUTORIZZAZIONE UTENTI AI SERVIZI
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_tipi_autorizzazioni implements Serializable {

	private static final long serialVersionUID = 8608257425129802494L;

	public Tbl_tipi_autorizzazioni() {
	}

	private int id_tipi_autorizzazione;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String cod_tipo_aut;

	private String descr;

	private char flag_aut;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Trl_autorizzazioni_servizi = new java.util.HashSet();

	private void setId_tipi_autorizzazione(int value) {
		this.id_tipi_autorizzazione = value;
	}

	public int getId_tipi_autorizzazione() {
		return id_tipi_autorizzazione;
	}

	public int getORMID() {
		return getId_tipi_autorizzazione();
	}

	/**
	 * codice identificativo del tipo di autorizzazione
	 */
	public void setCod_tipo_aut(String value) {
		this.cod_tipo_aut = value;
	}

	/**
	 * codice identificativo del tipo di autorizzazione
	 */
	public String getCod_tipo_aut() {
		return cod_tipo_aut;
	}

	/**
	 * descrizione del tipo di autorizzazione
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * descrizione del tipo di autorizzazione
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * indica se il tipo di autorizzazione e' predefinita cioe' quella che viene associata ad un utente che non risulta registrato in nessuna biblioteca
	 */
	public void setFlag_aut(char value) {
		this.flag_aut = value;
	}

	/**
	 * indica se il tipo di autorizzazione e' predefinita cioe' quella che viene associata ad un utente che non risulta registrato in nessuna biblioteca
	 */
	public char getFlag_aut() {
		return flag_aut;
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

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setTrl_autorizzazioni_servizi(java.util.Set value) {
		this.Trl_autorizzazioni_servizi = value;
	}

	public java.util.Set getTrl_autorizzazioni_servizi() {
		return Trl_autorizzazioni_servizi;
	}


	public String toString() {
		return String.valueOf(getId_tipi_autorizzazione());
	}

}
