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
 * LINGUA PER LA QUALE E' PREVITA LA TRADUZIONE DELLE LABELS, DEI CODICI E DELLA DIAGNOSTICA SULL'INTERFACCIA (--)
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_lingua_supportata implements Serializable {

	private static final long serialVersionUID = 7633581654208357642L;

	public Tbf_lingua_supportata() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_lingua_supportata))
			return false;
		Tbf_lingua_supportata tbf_lingua_supportata = (Tbf_lingua_supportata)aObj;
		if ((getCd_lingua() != null && !getCd_lingua().equals(tbf_lingua_supportata.getCd_lingua())) || (getCd_lingua() == null && tbf_lingua_supportata.getCd_lingua() != null))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCd_lingua() == null ? 0 : getCd_lingua().hashCode());
		return hashcode;
	}

	private String cd_lingua;

	private String descr;

	private char fl_default;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tbf_dizionario_errori = new java.util.HashSet();

	private java.util.Set Trf_funzioni_denominazioni = new java.util.HashSet();

	public void setCd_lingua(String value) {
		this.cd_lingua = value;
	}

	public String getCd_lingua() {
		return cd_lingua;
	}

	public String getORMID() {
		return getCd_lingua();
	}

	/**
	 * Descrizione della lingua
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * Descrizione della lingua
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * Indicatore di lingua utilizzata di default (il valore S=default e' ammesso su una sola occorrenza della tabella)
	 */
	public void setFl_default(char value) {
		this.fl_default = value;
	}

	/**
	 * Indicatore di lingua utilizzata di default (il valore S=default e' ammesso su una sola occorrenza della tabella)
	 */
	public char getFl_default() {
		return fl_default;
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

	public void setTbf_dizionario_errori(java.util.Set value) {
		this.Tbf_dizionario_errori = value;
	}

	public java.util.Set getTbf_dizionario_errori() {
		return Tbf_dizionario_errori;
	}


	public void setTrf_funzioni_denominazioni(java.util.Set value) {
		this.Trf_funzioni_denominazioni = value;
	}

	public java.util.Set getTrf_funzioni_denominazioni() {
		return Trf_funzioni_denominazioni;
	}


	public String toString() {
		return String.valueOf(getCd_lingua());
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
