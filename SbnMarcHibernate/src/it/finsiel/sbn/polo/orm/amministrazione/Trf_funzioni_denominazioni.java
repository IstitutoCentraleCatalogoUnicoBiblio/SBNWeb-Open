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
 * DESCRIZIONI IN LINGUA DELLE FUNZIONI GESTITE DALL'APPLICATIVO (--)
 */
/**
 * ORM-Persistable Class
 */
public class Trf_funzioni_denominazioni implements Serializable {

	private static final long serialVersionUID = 235483948732513864L;

	public Trf_funzioni_denominazioni() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trf_funzioni_denominazioni))
			return false;
		Trf_funzioni_denominazioni trf_funzioni_denominazioni = (Trf_funzioni_denominazioni)aObj;
		if (getCd_funzione() == null && trf_funzioni_denominazioni.getCd_funzione() != null)
			return false;
		if (!getCd_funzione().equals(trf_funzioni_denominazioni.getCd_funzione()))
			return false;
		if (getCd_lingua() == null && trf_funzioni_denominazioni.getCd_lingua() != null)
			return false;
		if (!getCd_lingua().equals(trf_funzioni_denominazioni.getCd_lingua()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_funzione() != null) {
			hashcode = hashcode + (getCd_funzione().getORMID() == null ? 0 : getCd_funzione().getORMID().hashCode());
		}
		if (getCd_lingua() != null) {
			hashcode = hashcode + (getCd_lingua().getORMID() == null ? 0 : getCd_lingua().getORMID().hashCode());
		}
		return hashcode;
	}

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita cd_funzione;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_lingua_supportata cd_lingua;

	private String ds_nome;

	private String ds_nome_breve;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	/**
	 * Descrizione estesa, nella lingua indicata dal relativo codice, della funzione codificata
	 */
	public void setDs_nome(String value) {
		this.ds_nome = value;
	}

	/**
	 * Descrizione estesa, nella lingua indicata dal relativo codice, della funzione codificata
	 */
	public String getDs_nome() {
		return ds_nome;
	}

	/**
	 * Descrizione breve, nella lingua indicata dal relativo codice, della funzione codificata
	 */
	public void setDs_nome_breve(String value) {
		this.ds_nome_breve = value;
	}

	/**
	 * Descrizione breve, nella lingua indicata dal relativo codice, della funzione codificata
	 */
	public String getDs_nome_breve() {
		return ds_nome_breve;
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

	public void setCd_funzione(it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita value) {
		this.cd_funzione = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita getCd_funzione() {
		return cd_funzione;
	}

	public String toString() {
		return String.valueOf(((getCd_funzione() == null) ? "" : String.valueOf(getCd_funzione().getORMID())) + " " + ((getCd_lingua() == null) ? "" : String.valueOf(getCd_lingua().getORMID())));
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
