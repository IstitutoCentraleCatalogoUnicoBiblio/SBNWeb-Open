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
package it.finsiel.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * ELENCO DEI CODICI DI ERRORE E DELLA RELATIVA DESCRIZIONE NELLE DIVERSE LINGUE (--)
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_dizionario_errori implements Serializable {

	private static final long serialVersionUID = 3718049616355834992L;

	public Tbf_dizionario_errori() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_dizionario_errori))
			return false;
		Tbf_dizionario_errori tbf_dizionario_errori = (Tbf_dizionario_errori)aObj;
		if (getCd_lingua() == null && tbf_dizionario_errori.getCd_lingua() != null)
			return false;
		if (!getCd_lingua().equals(tbf_dizionario_errori.getCd_lingua()))
			return false;
		if (getCd_errore() != tbf_dizionario_errori.getCd_errore())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_lingua() != null) {
			hashcode = hashcode + (getCd_lingua().getORMID() == null ? 0 : getCd_lingua().getORMID().hashCode());
		}
		hashcode = hashcode + getCd_errore();
		return hashcode;
	}

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_lingua_supportata cd_lingua;

	private int cd_errore;

	private String ds_errore;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setCd_errore(int value) {
		this.cd_errore = value;
	}

	public int getCd_errore() {
		return cd_errore;
	}

	/**
	 * Descrizione, nella lingua indicata dal relativo codice, dell'errore codificato
	 */
	public void setDs_errore(String value) {
		this.ds_errore = value;
	}

	/**
	 * Descrizione, nella lingua indicata dal relativo codice, dell'errore codificato
	 */
	public String getDs_errore() {
		return ds_errore;
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

	public void setCd_lingua(it.finsiel.sbn.polo.orm.amministrazione.Tbf_lingua_supportata value) {
		this.cd_lingua = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_lingua_supportata getCd_lingua() {
		return cd_lingua;
	}

	public String toString() {
		return String.valueOf(((getCd_lingua() == null) ? "" : String.valueOf(getCd_lingua().getORMID())) + " " + getCd_errore());
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
