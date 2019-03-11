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
package it.iccu.sbn.polo.orm.bibliografica;

import java.io.Serializable;
/**
 * NOTE
 */
/**
 * ORM-Persistable Class
 */
public class Tb_nota implements Serializable {

	private static final long serialVersionUID = -3657670420479933634L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_nota))
			return false;
		Tb_nota tb_nota = (Tb_nota)aObj;
		if (getB() == null)
			return false;
		if (!getB().getORMID().equals(tb_nota.getB().getORMID()))
			return false;
		if (getTp_nota() != null && !getTp_nota().equals(tb_nota.getTp_nota()))
			return false;
		if (getProgr_nota() != null && !getProgr_nota().equals(tb_nota.getProgr_nota()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getB() != null) {
			hashcode = hashcode + getB().getORMID().hashCode();
		}
		hashcode = hashcode + getTp_nota().hashCode();
		hashcode = hashcode + getProgr_nota().hashCode();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String tp_nota;

	private String progr_nota;

	private String ds_nota;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setTp_nota(String value) {
		this.tp_nota = value;
	}

	public String getTp_nota() {
		return tp_nota;
	}

	public void setProgr_nota(String value) {
		this.progr_nota = value;
	}

	public String getProgr_nota() {
		return progr_nota;
	}

	public void setDs_nota(String value) {
		this.ds_nota = value;
	}

	public String getDs_nota() {
		return ds_nota;
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

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())) + " " + getTp_nota() + " " + getProgr_nota());
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
