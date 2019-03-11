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
package it.iccu.sbn.polo.orm.bibliografica;

import java.io.Serializable;
/**
 * AUTORI IN BIBLIOTECA
 */
/**
 * ORM-Persistable Class
 */
public class Tr_aut_bib implements Serializable {

	private static final long serialVersionUID = -6640680547368506324L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_aut_bib))
			return false;
		Tr_aut_bib tr_aut_bib = (Tr_aut_bib)aObj;
		if (getV() == null)
			return false;
		if (!getV().getORMID().equals(tr_aut_bib.getV().getORMID()))
			return false;
		if (getCd_polo() == null)
			return false;
		if (getCd_polo().getCd_polo() != tr_aut_bib.getCd_polo().getCd_polo())
			return false;
		if (!getCd_polo().getCd_biblioteca().equals(tr_aut_bib.getCd_polo().getCd_biblioteca()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getV() != null) {
			hashcode = hashcode + getV().getORMID().hashCode();
		}
		if (getCd_polo() != null) {
			hashcode = hashcode + getCd_polo().getCd_polo().hashCode();
			hashcode = hashcode + getCd_polo().getCd_biblioteca().hashCode();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_autore v;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_polo;

	private char fl_allinea;

	private char fl_allinea_sbnmarc;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setFl_allinea(char value) {
		this.fl_allinea = value;
	}

	public char getFl_allinea() {
		return fl_allinea;
	}

	public void setFl_allinea_sbnmarc(char value) {
		this.fl_allinea_sbnmarc = value;
	}

	public char getFl_allinea_sbnmarc() {
		return fl_allinea_sbnmarc;
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

	public void setV(it.iccu.sbn.polo.orm.bibliografica.Tb_autore value) {
		this.v = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_autore getV() {
		return v;
	}

	public void setCd_polo(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_polo() {
		return cd_polo;
	}

	public String toString() {
		return String.valueOf(((getV() == null) ? "" : String.valueOf(getV().getORMID())) + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getCd_polo()) + " " + String.valueOf(getCd_polo().getCd_biblioteca())));
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
