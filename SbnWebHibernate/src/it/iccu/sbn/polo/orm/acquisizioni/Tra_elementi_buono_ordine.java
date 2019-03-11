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
 * Elementi del buono d'ordine (ordini del buono)
 */
/**
 * ORM-Persistable Class
 */
public class Tra_elementi_buono_ordine implements Serializable {

	private static final long serialVersionUID = -5739210388924651324L;



	public Tra_elementi_buono_ordine() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tra_elementi_buono_ordine))
			return false;
		Tra_elementi_buono_ordine tra_elementi_buono_ordine = (Tra_elementi_buono_ordine)aObj;
		if (getCd_bib() == null && tra_elementi_buono_ordine.getCd_bib() != null)
			return false;
		if (!getCd_bib().equals(tra_elementi_buono_ordine.getCd_bib()))
			return false;
		if ((getBuono_ord() != null && !getBuono_ord().equals(tra_elementi_buono_ordine.getBuono_ord())) || (getBuono_ord() == null && tra_elementi_buono_ordine.getBuono_ord() != null))
			return false;
		if (getCod_tip_ord() != tra_elementi_buono_ordine.getCod_tip_ord())
			return false;
		if (getAnno_ord() != tra_elementi_buono_ordine.getAnno_ord())
			return false;
		if (getCod_ord() != tra_elementi_buono_ordine.getCod_ord())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_bib() != null) {
			hashcode = hashcode + (getCd_bib().getCd_biblioteca() == null ? 0 : getCd_bib().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_bib().getCd_polo() == null ? 0 : getCd_bib().getCd_polo().hashCode());
		}
		hashcode = hashcode + (getBuono_ord() == null ? 0 : getBuono_ord().hashCode());
		hashcode = hashcode + getCod_tip_ord();
		hashcode = hashcode + (getAnno_ord() == null ? 0 : getAnno_ord().hashCode());
		hashcode = hashcode + getCod_ord();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String buono_ord;

	private char cod_tip_ord;

	private java.math.BigDecimal anno_ord;

	private int cod_ord;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;



	public void setBuono_ord(String value) {
		this.buono_ord = value;
	}

	public String getBuono_ord() {
		return buono_ord;
	}

	public void setCod_tip_ord(char value) {
		this.cod_tip_ord = value;
	}

	public char getCod_tip_ord() {
		return cod_tip_ord;
	}

	public void setAnno_ord(java.math.BigDecimal value) {
		this.anno_ord = value;
	}

	public java.math.BigDecimal getAnno_ord() {
		return anno_ord;
	}

	public void setCod_ord(int value) {
		this.cod_ord = value;
	}

	public int getCod_ord() {
		return cod_ord;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

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

	public String toString() {
		return String.valueOf(((getCd_bib() == null) ? "" : String.valueOf(getCd_bib().getCd_biblioteca()) + " " + String.valueOf(getCd_bib().getCd_polo())) + " " + getBuono_ord() + " " + getCod_tip_ord() + " " + getAnno_ord() + " " + getCod_ord());
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




}
