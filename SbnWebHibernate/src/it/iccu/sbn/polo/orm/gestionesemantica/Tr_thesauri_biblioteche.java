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
package it.iccu.sbn.polo.orm.gestionesemantica;

import java.io.Serializable;
/**
 * SISTEMI DI THESAURO IN BIBLIOTECA (TPSTBI)
 */
/**
 * ORM-Persistable Class
 */
public class Tr_thesauri_biblioteche implements Serializable {

	private static final long serialVersionUID = -541300187470444725L;

	public Tr_thesauri_biblioteche() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tr_thesauri_biblioteche))
			return false;
		Tr_thesauri_biblioteche tr_thesauri_biblioteche = (Tr_thesauri_biblioteche)aObj;
		if (getCd_biblioteca() == null && tr_thesauri_biblioteche.getCd_biblioteca() != null)
			return false;
		if (!getCd_biblioteca().equals(tr_thesauri_biblioteche.getCd_biblioteca()))
			return false;
		if ((getCd_the() != null && !getCd_the().equals(tr_thesauri_biblioteche.getCd_the())) || (getCd_the() == null && tr_thesauri_biblioteche.getCd_the() != null))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_biblioteca() != null) {
			hashcode = hashcode + (getCd_biblioteca().getCd_biblioteca() == null ? 0 : getCd_biblioteca().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_biblioteca().getCd_polo() == null ? 0 : getCd_biblioteca().getCd_polo().hashCode());
		}
		hashcode = hashcode + (getCd_the() == null ? 0 : getCd_the().hashCode());
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private String cd_the;

	private char fl_att;

	private char fl_auto_loc;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private char sololocale;

	private java.util.Set Trs_termini_titoli_biblioteche = new java.util.HashSet();

	public void setCd_the(String value) {
		this.cd_the = value;
	}

	public String getCd_the() {
		return cd_the;
	}

	/**
	 * indicatore dell'attuale utilizzo del thesauro da parte della biblioteca
	 */
	public void setFl_att(char value) {
		this.fl_att = value;
	}

	/**
	 * indicatore dell'attuale utilizzo del thesauro da parte della biblioteca
	 */
	public char getFl_att() {
		return fl_att;
	}

	/**
	 * indicatore di localizzazione automatica legami a termini di thesauro inseriti da altra biblioteca
	 */
	public void setFl_auto_loc(char value) {
		this.fl_auto_loc = value;
	}

	/**
	 * indicatore di localizzazione automatica legami a termini di thesauro inseriti da altra biblioteca
	 */
	public char getFl_auto_loc() {
		return fl_auto_loc;
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

	public void setSololocale(char value) {
		this.sololocale = value;
	}

	public char getSololocale() {
		return sololocale;
	}

	public void setCd_biblioteca(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_biblioteca() {
		return cd_biblioteca;
	}

	public void setTrs_termini_titoli_biblioteche(java.util.Set value) {
		this.Trs_termini_titoli_biblioteche = value;
	}

	public java.util.Set getTrs_termini_titoli_biblioteche() {
		return Trs_termini_titoli_biblioteche;
	}


	public String toString() {
		return String.valueOf(((getCd_biblioteca() == null) ? "" : String.valueOf(getCd_biblioteca().getCd_biblioteca()) + " " + String.valueOf(getCd_biblioteca().getCd_polo())) + " " + getCd_the());
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
