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
package it.iccu.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * CONTATORI APPLICATIVI (TLFCNT + TPFCNT)
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_contatore implements Serializable {

	private static final long serialVersionUID = 5509437857811103191L;

	public Tbf_contatore() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_contatore))
			return false;
		Tbf_contatore tbf_contatore = (Tbf_contatore)aObj;
		if (getCd_polo() == null && tbf_contatore.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(tbf_contatore.getCd_polo()))
			return false;
		if ((getCd_cont() != null && !getCd_cont().equals(tbf_contatore.getCd_cont())) || (getCd_cont() == null && tbf_contatore.getCd_cont() != null))
			return false;
		if (getAnno() != tbf_contatore.getAnno())
			return false;
		if ((getKey1() != null && !getKey1().equals(tbf_contatore.getKey1())) || (getKey1() == null && tbf_contatore.getKey1() != null))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getCd_biblioteca() == null ? 0 : getCd_polo().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_polo().getCd_polo() == null ? 0 : getCd_polo().getCd_polo().hashCode());
		}
		hashcode = hashcode + (getCd_cont() == null ? 0 : getCd_cont().hashCode());
		hashcode = hashcode + getAnno();
		hashcode = hashcode + (getKey1() == null ? 0 : getKey1().hashCode());
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_polo;

	private String cd_cont;

	private int anno;

	private String key1;

	private int ultimo_prg;

	private int lim_max;

	private Character attivo;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setCd_cont(String value) {
		this.cd_cont = value;
	}

	public String getCd_cont() {
		return cd_cont;
	}

	public void setAnno(int value) {
		this.anno = value;
	}

	public int getAnno() {
		return anno;
	}

	public void setKey1(String value) {
		this.key1 = value;
	}

	public String getKey1() {
		return key1;
	}

	/**
	 * Ultimo progressivo utilizzato
	 */
	public void setUltimo_prg(int value) {
		this.ultimo_prg = value;
	}

	/**
	 * Ultimo progressivo utilizzato
	 */
	public int getUltimo_prg() {
		return ultimo_prg;
	}

	/**
	 * Valore massimo ammesso per il contatore
	 */
	public void setLim_max(int value) {
		this.lim_max = value;
	}

	/**
	 * Valore massimo ammesso per il contatore
	 */
	public int getLim_max() {
		return lim_max;
	}

	/**
	 * Indicatore di utilizzo da parte di altro utente (1=disponibile, 2=in aggiornamento)
	 */
	public void setAttivo(char value) {
		setAttivo(new Character(value));
	}

	/**
	 * Indicatore di utilizzo da parte di altro utente (1=disponibile, 2=in aggiornamento)
	 */
	public void setAttivo(Character value) {
		this.attivo = value;
	}

	/**
	 * Indicatore di utilizzo da parte di altro utente (1=disponibile, 2=in aggiornamento)
	 */
	public Character getAttivo() {
		return attivo;
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

	public String toString() {
		return String.valueOf(((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getCd_biblioteca()) + " " + String.valueOf(getCd_polo().getCd_polo())) + " " + getCd_cont() + " " + getAnno() + " " + getKey1());
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
