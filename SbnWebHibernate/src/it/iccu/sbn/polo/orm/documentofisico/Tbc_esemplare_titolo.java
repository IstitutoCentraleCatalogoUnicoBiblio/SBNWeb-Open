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

import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;

import java.io.Serializable;
/**
 * ESEMPLARE TITOLO (DOC)
 */
/**
 * ORM-Persistable Class
 */
public class Tbc_esemplare_titolo implements Serializable {

	private static final long serialVersionUID = -4286819538824762909L;

	public Tbc_esemplare_titolo() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbc_esemplare_titolo))
			return false;
		Tbc_esemplare_titolo tbc_esemplare_titolo = (Tbc_esemplare_titolo)aObj;
		if (getCd_doc() != tbc_esemplare_titolo.getCd_doc())
			return false;
		if (getB() == null && tbc_esemplare_titolo.getB() != null)
			return false;
		if (!getB().equals(tbc_esemplare_titolo.getB()))
			return false;
		if (getCd_polo() == null && tbc_esemplare_titolo.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(tbc_esemplare_titolo.getCd_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCd_doc();
		if (getB() != null) {
			hashcode = hashcode + (getB().getORMID() == null ? 0 : getB().getORMID().hashCode());
		}
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getCd_biblioteca() == null ? 0 : getCd_polo().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_polo().getCd_polo() == null ? 0 : getCd_polo().getCd_polo().hashCode());
		}
		return hashcode;
	}

	private int cd_doc;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_polo;

	private String cons_doc;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private Tb_titolo tb_titolo;

	private java.util.Set Tbc_collocazione = new java.util.HashSet();

	public void setCd_doc(int value) {
		this.cd_doc = value;
	}

	public int getCd_doc() {
		return cd_doc;
	}

	/**
	 * Consistenza dell'esemplare titolo
	 */
	public void setCons_doc(String value) {
		this.cons_doc = value;
	}

	/**
	 * Consistenza dell'esemplare titolo
	 */
	public String getCons_doc() {
		return cons_doc;
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

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public void setTbc_collocazione(java.util.Set value) {
		this.Tbc_collocazione = value;
	}

	public java.util.Set getTbc_collocazione() {
		return Tbc_collocazione;
	}


	public String toString() {
		return String.valueOf(getCd_doc() + " " + ((getB() == null) ? "" : String.valueOf(getB().getORMID())) + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getCd_biblioteca()) + " " + String.valueOf(getCd_polo().getCd_polo())));
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

	public Tb_titolo getTb_titolo() {
		return tb_titolo;
	}

	public void setTb_titolo(Tb_titolo tb_titolo) {
		this.tb_titolo = tb_titolo;
	}


}
