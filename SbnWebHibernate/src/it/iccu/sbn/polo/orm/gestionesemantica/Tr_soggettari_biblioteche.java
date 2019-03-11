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
 * SOGGETTARI IN BIBLIOTECA (TPSSBI))
 */
/**
 * ORM-Persistable Class
 */
public class Tr_soggettari_biblioteche implements Serializable {

	private static final long serialVersionUID = 4563936901906326647L;

	public Tr_soggettari_biblioteche() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tr_soggettari_biblioteche))
			return false;
		Tr_soggettari_biblioteche tr_soggettari_biblioteche = (Tr_soggettari_biblioteche)aObj;
		if ((getCd_sogg() != null && !getCd_sogg().equals(tr_soggettari_biblioteche.getCd_sogg())) || (getCd_sogg() == null && tr_soggettari_biblioteche.getCd_sogg() != null))
			return false;
		if (getCd_biblioteca() == null && tr_soggettari_biblioteche.getCd_biblioteca() != null)
			return false;
		if (!getCd_biblioteca().equals(tr_soggettari_biblioteche.getCd_biblioteca()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCd_sogg() == null ? 0 : getCd_sogg().hashCode());
		if (getCd_biblioteca() != null) {
			hashcode = hashcode + (getCd_biblioteca().getCd_biblioteca() == null ? 0 : getCd_biblioteca().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_biblioteca().getCd_polo() == null ? 0 : getCd_biblioteca().getCd_polo().hashCode());
		}
		return hashcode;
	}

	private String cd_sogg;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private char fl_att;

	private char fl_auto_loc;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private char sololocale;

	private java.util.Set Tr_tit_sog_bib = new java.util.HashSet();

	public void setCd_sogg(String value) {
		this.cd_sogg = value;
	}

	public String getCd_sogg() {
		return cd_sogg;
	}

	/**
	 * indicatore dell'attuale utilizzo del soggettario da parte della biblioteca
	 */
	public void setFl_att(char value) {
		this.fl_att = value;
	}

	/**
	 * indicatore dell'attuale utilizzo del soggettario da parte della biblioteca
	 */
	public char getFl_att() {
		return fl_att;
	}

	/**
	 * indicatore di localizzazione automatica legami a soggetti inseriti da altra biblioteca
	 */
	public void setFl_auto_loc(char value) {
		this.fl_auto_loc = value;
	}

	/**
	 * indicatore di localizzazione automatica legami a soggetti inseriti da altra biblioteca
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

	public void setTr_tit_sog_bib(java.util.Set value) {
		this.Tr_tit_sog_bib = value;
	}

	public java.util.Set getTr_tit_sog_bib() {
		return Tr_tit_sog_bib;
	}


	public String toString() {
		return String.valueOf(getCd_sogg() + " " + ((getCd_biblioteca() == null) ? "" : String.valueOf(getCd_biblioteca().getCd_biblioteca()) + " " + String.valueOf(getCd_biblioteca().getCd_polo())));
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
