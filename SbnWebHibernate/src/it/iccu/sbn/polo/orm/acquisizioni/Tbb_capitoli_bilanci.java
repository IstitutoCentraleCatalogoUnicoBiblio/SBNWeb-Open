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
 * Capitoli di bilancio
 */
/**
 * ORM-Persistable Class
 */
public class Tbb_capitoli_bilanci implements Serializable {

	private static final long serialVersionUID = 7176142793600659622L;

	public Tbb_capitoli_bilanci() {
	}

	private int id_capitoli_bilanci;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private java.math.BigDecimal esercizio;

	private java.math.BigDecimal capitolo;

	private java.math.BigDecimal budget;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tbb_bilanci = new java.util.HashSet();

	private void setId_capitoli_bilanci(int value) {
		this.id_capitoli_bilanci = value;
	}

	public int getId_capitoli_bilanci() {
		return id_capitoli_bilanci;
	}

	public int getORMID() {
		return getId_capitoli_bilanci();
	}

	/**
	 * anno di esercizio
	 */
	public void setEsercizio(java.math.BigDecimal value) {
		this.esercizio = value;
	}

	/**
	 * anno di esercizio
	 */
	public java.math.BigDecimal getEsercizio() {
		return esercizio;
	}

	/**
	 * codice identificativo del capitolo di bilancio
	 */
	public void setCapitolo(java.math.BigDecimal value) {
		this.capitolo = value;
	}

	/**
	 * codice identificativo del capitolo di bilancio
	 */
	public java.math.BigDecimal getCapitolo() {
		return capitolo;
	}

	/**
	 * budget assegnato
	 */
	public void setBudget(java.math.BigDecimal value) {
		this.budget = value;
	}

	/**
	 * budget assegnato
	 */
	public java.math.BigDecimal getBudget() {
		return budget;
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


	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setTbb_bilanci(java.util.Set value) {
		this.Tbb_bilanci = value;
	}

	public java.util.Set getTbb_bilanci() {
		return Tbb_bilanci;
	}


	public String toString() {
		return String.valueOf(getId_capitoli_bilanci());
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(char fl_canc) {
		this.fl_canc = fl_canc;
	}

}
