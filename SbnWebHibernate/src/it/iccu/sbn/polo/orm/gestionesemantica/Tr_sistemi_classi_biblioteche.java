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
 * SISTEMA DI CLASSIFICAZIONE IN BIBLIOTECA (TPSSCB))
 */
/**
 * ORM-Persistable Class
 */
public class Tr_sistemi_classi_biblioteche implements Serializable {

	private static final long serialVersionUID = 327455715706228819L;

	public Tr_sistemi_classi_biblioteche() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tr_sistemi_classi_biblioteche))
			return false;
		Tr_sistemi_classi_biblioteche tr_sistemi_classi_biblioteche = (Tr_sistemi_classi_biblioteche)aObj;
		if (getCd_biblioteca() == null && tr_sistemi_classi_biblioteche.getCd_biblioteca() != null)
			return false;
		if (!getCd_biblioteca().equals(tr_sistemi_classi_biblioteche.getCd_biblioteca()))
			return false;
		if ((getCd_sistema() != null && !getCd_sistema().equals(tr_sistemi_classi_biblioteche.getCd_sistema())) || (getCd_sistema() == null && tr_sistemi_classi_biblioteche.getCd_sistema() != null))
			return false;
		if ((getCd_edizione() != null && !getCd_edizione().equals(tr_sistemi_classi_biblioteche.getCd_edizione())) || (getCd_edizione() == null && tr_sistemi_classi_biblioteche.getCd_edizione() != null))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_biblioteca() != null) {
			hashcode = hashcode + (getCd_biblioteca().getCd_biblioteca() == null ? 0 : getCd_biblioteca().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_biblioteca().getCd_polo() == null ? 0 : getCd_biblioteca().getCd_polo().hashCode());
		}
		hashcode = hashcode + (getCd_sistema() == null ? 0 : getCd_sistema().hashCode());
		hashcode = hashcode + (getCd_edizione() == null ? 0 : getCd_edizione().hashCode());
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private String cd_sistema;

	private String cd_edizione;

	private char flg_att;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private char sololocale;

	/**
	 * indicatore dell'attuale utilizzo del sistema di classificazione da parte della biblioteca
	 */
	public void setFlg_att(char value) {
		this.flg_att = value;
	}

	/**
	 * indicatore dell'attuale utilizzo del sistema di classificazione da parte della biblioteca
	 */
	public char getFlg_att() {
		return flg_att;
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

	public void setCd_sistema(String value) {
		this.cd_sistema = value;
	}

	public String getCd_sistema() {
		return cd_sistema;
	}

	public void setCd_edizione(String value) {
		this.cd_edizione = value;
	}

	public String getCd_edizione() {
		return cd_edizione;
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

	public String toString() {
		return String.valueOf(((getCd_biblioteca() == null) ? "" : String.valueOf(getCd_biblioteca().getCd_biblioteca()) + " " + String.valueOf(getCd_biblioteca().getCd_polo())) + " " + getCd_sistema() + " " + getCd_edizione());
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
