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
 * PROFILI D ABILITAZIONE DEI BIBLIOTECARI (TLFPRF)
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_profilo_abilitazione implements Serializable {

	private static final long serialVersionUID = 5888195548427699458L;

	public Tbf_profilo_abilitazione() {
	}

	private int id_profilo_abilitazione;

	private String cd_prof;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_polo;

	private String dsc_profilo;

	private String nota_profilo;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set cd_prof_base = new java.util.HashSet();

	private java.util.Set cd_prof_coll = new java.util.HashSet();

	private java.util.Set Tbf_bibliotecario = new java.util.HashSet();

	private java.util.Set Trf_profilo_biblioteca = new java.util.HashSet();

	/**
	 * Codice identificativo del profilo
	 */
	public void setCd_prof(String value) {
		this.cd_prof = value;
	}

	/**
	 * Codice identificativo del profilo
	 */
	public String getCd_prof() {
		return cd_prof;
	}

	/**
	 * Descrizione del profilo
	 */
	public void setDsc_profilo(String value) {
		this.dsc_profilo = value;
	}

	/**
	 * Descrizione del profilo
	 */
	public String getDsc_profilo() {
		return dsc_profilo;
	}

	/**
	 * Nota al profilo
	 */
	public void setNota_profilo(String value) {
		this.nota_profilo = value;
	}

	/**
	 * Nota al profilo
	 */
	public String getNota_profilo() {
		return nota_profilo;
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

	private void setId_profilo_abilitazione(int value) {
		this.id_profilo_abilitazione = value;
	}

	public int getId_profilo_abilitazione() {
		return id_profilo_abilitazione;
	}

	public int getORMID() {
		return getId_profilo_abilitazione();
	}

	public void setCd_prof_base(java.util.Set value) {
		this.cd_prof_base = value;
	}

	public java.util.Set getCd_prof_base() {
		return cd_prof_base;
	}


	public void setCd_polo(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_polo() {
		return cd_polo;
	}

	public void setCd_prof_coll(java.util.Set value) {
		this.cd_prof_coll = value;
	}

	public java.util.Set getCd_prof_coll() {
		return cd_prof_coll;
	}


	public void setTbf_bibliotecario(java.util.Set value) {
		this.Tbf_bibliotecario = value;
	}

	public java.util.Set getTbf_bibliotecario() {
		return Tbf_bibliotecario;
	}


	public void setTrf_profilo_biblioteca(java.util.Set value) {
		this.Trf_profilo_biblioteca = value;
	}

	public java.util.Set getTrf_profilo_biblioteca() {
		return Trf_profilo_biblioteca;
	}


	public String toString() {
		return String.valueOf(getId_profilo_abilitazione());
	}

}
