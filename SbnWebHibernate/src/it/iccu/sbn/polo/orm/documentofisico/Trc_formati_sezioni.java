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
 * FORMATI DEFINITI PER LA SEZIONE (solo per sezioni a formato)
 */
/**
 * ORM-Persistable Class
 */
public class Trc_formati_sezioni implements Serializable {

	private static final long serialVersionUID = 3343186189457150213L;

	public Trc_formati_sezioni() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trc_formati_sezioni))
			return false;
		Trc_formati_sezioni trc_formati_sezioni = (Trc_formati_sezioni)aObj;
		if (getCd_sezione() == null && trc_formati_sezioni.getCd_sezione() != null)
			return false;
		if (!getCd_sezione().equals(trc_formati_sezioni.getCd_sezione()))
			return false;
		if ((getCd_formato() != null && !getCd_formato().equals(trc_formati_sezioni.getCd_formato())) || (getCd_formato() == null && trc_formati_sezioni.getCd_formato() != null))
			return false;
		if (getProg_serie() != trc_formati_sezioni.getProg_serie())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_sezione() != null) {
			hashcode = hashcode + (getCd_sezione().getCd_sez() == null ? 0 : getCd_sezione().getCd_sez().hashCode());
			hashcode = hashcode + (getCd_sezione().getCd_polo() == null ? 0 : getCd_sezione().getCd_polo().hashCode());
		}
		hashcode = hashcode + (getCd_formato() == null ? 0 : getCd_formato().hashCode());
		hashcode = hashcode + getProg_serie();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione cd_sezione;

	private String cd_formato;

	private int prog_serie;

	private int prog_num;

	private int n_pezzi;

	private String descr;
	private int n_pezzi_misc;

	private int prog_serie_num1_misc;
	private int progr_num1_misc;
	private int prog_serie_num2_misc;
	private int progr_num2_misc;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setProg_serie(int value) {
		this.prog_serie = value;
	}

	public int getProg_serie() {
		return prog_serie;
	}

	/**
	 * ultimo progressivo di collocazione interno alla serie
	 */
	public void setProg_num(int value) {
		this.prog_num = value;
	}

	/**
	 * ultimo progressivo di collocazione interno alla serie
	 */
	public int getProg_num() {
		return prog_num;
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

	public void setCd_formato(String value) {
		this.cd_formato = value;
	}

	public String getCd_formato() {
		return cd_formato;
	}

	public void setDescr(String value) {
		this.descr = value;
	}

	public String getDescr() {
		return descr;
	}

	public void setCd_sezione(it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione value) {
		this.cd_sezione = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione getCd_sezione() {
		return cd_sezione;
	}

	public String toString() {
		return String.valueOf(((getCd_sezione() == null) ? "" : String.valueOf(getCd_sezione().getCd_sez()) + " " + String.valueOf(getCd_sezione().getCd_polo())) + " " + getCd_formato() + " " + getProg_serie());
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

	public int getN_pezzi() {
		return n_pezzi;
	}

	public void setN_pezzi(int n_pezzi) {
		this.n_pezzi = n_pezzi;
	}

	public int getN_pezzi_misc() {
		return n_pezzi_misc;
	}

	public void setN_pezzi_misc(int nPezziMisc) {
		n_pezzi_misc = nPezziMisc;
	}

	public int getProgr_num1_misc() {
		return progr_num1_misc;
	}

	public void setProgr_num1_misc(int progrNum1Misc) {
		progr_num1_misc = progrNum1Misc;
	}

	public int getProgr_num2_misc() {
		return progr_num2_misc;
	}

	public void setProgr_num2_misc(int progrNum2Misc) {
		progr_num2_misc = progrNum2Misc;
	}

	public int getProg_serie_num1_misc() {
		return prog_serie_num1_misc;
	}

	public void setProg_serie_num1_misc(int progSerieNum1Misc) {
		prog_serie_num1_misc = progSerieNum1Misc;
	}

	public int getProg_serie_num2_misc() {
		return prog_serie_num2_misc;
	}

	public void setProg_serie_num2_misc(int progSerieNum2Misc) {
		prog_serie_num2_misc = progSerieNum2Misc;
	}


}
