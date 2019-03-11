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
 * Bilanci
 */
/**
 * ORM-Persistable Class
 */
public class Tbb_bilanci implements Serializable {

	private static final long serialVersionUID = -2665171462557342956L;

	public Tbb_bilanci() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbb_bilanci))
			return false;
		Tbb_bilanci tbb_bilanci = (Tbb_bilanci)aObj;
		if (getCod_mat() != tbb_bilanci.getCod_mat())
			return false;
		if (getId_capitoli_bilanci() == null && tbb_bilanci.getId_capitoli_bilanci() != null)
			return false;
		if (!getId_capitoli_bilanci().equals(tbb_bilanci.getId_capitoli_bilanci()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCod_mat();
		if (getId_capitoli_bilanci() != null) {
			hashcode = hashcode + getId_capitoli_bilanci().getORMID();
		}
		return hashcode;
	}

	private char cod_mat;

	private it.iccu.sbn.polo.orm.acquisizioni.Tbb_capitoli_bilanci id_capitoli_bilanci;

	private it.iccu.sbn.polo.orm.acquisizioni.Tba_buono_ordine id_buono_ordine;

	private java.math.BigDecimal budget;

	private java.math.BigDecimal ordinato;

	private java.math.BigDecimal fatturato;

	private java.math.BigDecimal pagato;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.math.BigDecimal acquisito;

	private java.util.Set Tba_righe_fatture = new java.util.HashSet();

	private java.util.Set Tba_buono_ordine = new java.util.HashSet();

	private java.util.Set Tba_ordini = new java.util.HashSet();

	public void setCod_mat(char value) {
		this.cod_mat = value;
	}

	public char getCod_mat() {
		return cod_mat;
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

	/**
	 * totale del materiale ordinato
	 */
	public void setOrdinato(java.math.BigDecimal value) {
		this.ordinato = value;
	}

	/**
	 * totale del materiale ordinato
	 */
	public java.math.BigDecimal getOrdinato() {
		return ordinato;
	}

	/**
	 * totale del materiale fatturato
	 */
	public void setFatturato(java.math.BigDecimal value) {
		this.fatturato = value;
	}

	/**
	 * totale del materiale fatturato
	 */
	public java.math.BigDecimal getFatturato() {
		return fatturato;
	}

	/**
	 * totale del materiale fatturato e gia' pagato
	 */
	public void setPagato(java.math.BigDecimal value) {
		this.pagato = value;
	}

	/**
	 * totale del materiale fatturato e gia' pagato
	 */
	public java.math.BigDecimal getPagato() {
		return pagato;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * data e ora d'inserimento;
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * data e ora d'inserimento;
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

	public void setId_capitoli_bilanci(it.iccu.sbn.polo.orm.acquisizioni.Tbb_capitoli_bilanci value) {
		this.id_capitoli_bilanci = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tbb_capitoli_bilanci getId_capitoli_bilanci() {
		return id_capitoli_bilanci;
	}

	public void setId_buono_ordine(it.iccu.sbn.polo.orm.acquisizioni.Tba_buono_ordine value) {
		this.id_buono_ordine = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tba_buono_ordine getId_buono_ordine() {
		return id_buono_ordine;
	}

	public void setTba_righe_fatture(java.util.Set value) {
		this.Tba_righe_fatture = value;
	}

	public java.util.Set getTba_righe_fatture() {
		return Tba_righe_fatture;
	}


	public void setTba_buono_ordine(java.util.Set value) {
		this.Tba_buono_ordine = value;
	}

	public java.util.Set getTba_buono_ordine() {
		return Tba_buono_ordine;
	}


	public void setTba_ordini(java.util.Set value) {
		this.Tba_ordini = value;
	}

	public java.util.Set getTba_ordini() {
		return Tba_ordini;
	}


	public String toString() {
		return String.valueOf(getCod_mat() + " " + ((getId_capitoli_bilanci() == null) ? "" : String.valueOf(getId_capitoli_bilanci().getORMID())));
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

	public java.math.BigDecimal getAcquisito() {
		return acquisito;
	}

	public void setAcquisito(java.math.BigDecimal acquisito) {
		this.acquisito = acquisito;
	}


}
