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
package it.iccu.sbn.polo.orm.acquisizioni;

import java.io.Serializable;
/**
 * Cambi ufficiali
 */
/**
 * ORM-Persistable Class
 */
public class Tba_cambi_ufficiali implements Serializable {

	private static final long serialVersionUID = -638573909886621462L;

	public Tba_cambi_ufficiali() {
	}

	private int id_valuta;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String valuta;

	private java.math.BigDecimal cambio;

	private java.util.Date data_var;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tba_ordini = new java.util.HashSet();

	private void setId_valuta(int value) {
		this.id_valuta = value;
	}

	public int getId_valuta() {
		return id_valuta;
	}

	public int getORMID() {
		return getId_valuta();
	}

	/**
	 * codice della valuta
	 */
	public void setValuta(String value) {
		this.valuta = value;
	}

	/**
	 * codice della valuta
	 */
	public String getValuta() {
		return valuta;
	}

	/**
	 * tasso di cambio
	 */
	public void setCambio(java.math.BigDecimal value) {
		this.cambio = value;
	}

	/**
	 * tasso di cambio
	 */
	public java.math.BigDecimal getCambio() {
		return cambio;
	}

	/**
	 * data di variazione
	 */
	public void setData_var(java.util.Date value) {
		this.data_var = value;
	}

	/**
	 * data di variazione
	 */
	public java.util.Date getData_var() {
		return data_var;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * data e ora d'inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * data e ora d'inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setTba_ordini(java.util.Set value) {
		this.Tba_ordini = value;
	}

	public java.util.Set getTba_ordini() {
		return Tba_ordini;
	}


	public String toString() {
		return String.valueOf(getId_valuta());
	}

}
