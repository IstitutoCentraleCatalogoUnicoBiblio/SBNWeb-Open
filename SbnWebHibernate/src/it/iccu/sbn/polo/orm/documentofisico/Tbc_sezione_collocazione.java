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
package it.iccu.sbn.polo.orm.documentofisico;

import java.io.Serializable;
/**
 * SEZIONI DI COLLOCAZIONE
 */
/**
 * ORM-Persistable Class
 */
public class Tbc_sezione_collocazione implements Serializable {

	private static final long serialVersionUID = -7038968459160571782L;

	public Tbc_sezione_collocazione() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbc_sezione_collocazione))
			return false;
		Tbc_sezione_collocazione tbc_sezione_collocazione = (Tbc_sezione_collocazione)aObj;
		if ((getCd_sez() != null && !getCd_sez().equals(tbc_sezione_collocazione.getCd_sez())) || (getCd_sez() == null && tbc_sezione_collocazione.getCd_sez() != null))
			return false;
		if (getCd_polo() == null && tbc_sezione_collocazione.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(tbc_sezione_collocazione.getCd_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCd_sez() == null ? 0 : getCd_sez().hashCode());
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getCd_biblioteca() == null ? 0 : getCd_polo().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_polo().getCd_polo() == null ? 0 : getCd_polo().getCd_polo().hashCode());
		}
		return hashcode;
	}

	private String cd_sez;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_polo;

	private String note_sez;

	private int tot_inv;

	private String descr;

	private char cd_colloc;

	private char tipo_sez;

	private String cd_cla;

	private int tot_inv_max;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tbc_collocazione = new java.util.HashSet();

	private java.util.Set Trc_formati_sezioni = new java.util.HashSet();

	public void setCd_sez(String value) {
		this.cd_sez = value;
	}

	public String getCd_sez() {
		return cd_sez;
	}

	/**
	 * note relative alla sezione
	 */
	public void setNote_sez(String value) {
		this.note_sez = value;
	}

	/**
	 * note relative alla sezione
	 */
	public String getNote_sez() {
		return note_sez;
	}

	/**
	 * totale degli inventari collocati nella sezione
	 */
	public void setTot_inv(int value) {
		this.tot_inv = value;
	}

	/**
	 * totale degli inventari collocati nella sezione
	 */
	public int getTot_inv() {
		return tot_inv;
	}

	/**
	 * denominazione della sezione
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * denominazione della sezione
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * codice tipo collocazione
	 */
	public void setCd_colloc(char value) {
		this.cd_colloc = value;
	}

	/**
	 * codice tipo collocazione
	 */
	public char getCd_colloc() {
		return cd_colloc;
	}

	/**
	 * codice identificativo del tipo di sezione
	 */
	public void setTipo_sez(char value) {
		this.tipo_sez = value;
	}

	/**
	 * codice identificativo del tipo di sezione
	 */
	public char getTipo_sez() {
		return tipo_sez;
	}

	/**
	 * codice del sistema di classificazione
	 */
	public void setCd_cla(String value) {
		this.cd_cla = value;
	}

	/**
	 * codice del sistema di classificazione
	 */
	public String getCd_cla() {
		return cd_cla;
	}

	/**
	 * numero di inventari previsti per la sezione di collocazione
	 */
	public void setTot_inv_max(int value) {
		this.tot_inv_max = value;
	}

	/**
	 * numero di inventari previsti per la sezione di collocazione
	 */
	public int getTot_inv_max() {
		return tot_inv_max;
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

	public void setCd_polo(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_polo() {
		return cd_polo;
	}

	public void setTbc_collocazione(java.util.Set value) {
		this.Tbc_collocazione = value;
	}

	public java.util.Set getTbc_collocazione() {
		return Tbc_collocazione;
	}


	public void setTrc_formati_sezioni(java.util.Set value) {
		this.Trc_formati_sezioni = value;
	}

	public java.util.Set getTrc_formati_sezioni() {
		return Trc_formati_sezioni;
	}


	public String toString() {
		return String.valueOf(getCd_sez() + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getCd_biblioteca()) + " " + String.valueOf(getCd_polo().getCd_polo())));
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
