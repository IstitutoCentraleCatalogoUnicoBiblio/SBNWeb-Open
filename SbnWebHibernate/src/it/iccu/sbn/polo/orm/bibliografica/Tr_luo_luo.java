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
 * COLLEGAMENTI TRA LUOGHI
 */
/**
 * ORM-Persistable Class
 */
public class Tr_luo_luo implements Serializable {

	private static final long serialVersionUID = 42274353300855099L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_luo_luo))
			return false;
		Tr_luo_luo tr_luo_luo = (Tr_luo_luo)aObj;
		if (getLid_base() == null)
			return false;
		if (!getLid_base().getORMID().equals(tr_luo_luo.getLid_base().getORMID()))
			return false;
		if (getLid_coll() == null)
			return false;
		if (!getLid_coll().getORMID().equals(tr_luo_luo.getLid_coll().getORMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getLid_base() != null) {
			hashcode = hashcode + getLid_base().getORMID().hashCode();
		}
		if (getLid_coll() != null) {
			hashcode = hashcode + getLid_coll().getORMID().hashCode();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_luogo lid_base;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_luogo lid_coll;

	private char tp_legame;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setTp_legame(char value) {
		this.tp_legame = value;
	}

	public char getTp_legame() {
		return tp_legame;
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

	public void setLid_base(it.iccu.sbn.polo.orm.bibliografica.Tb_luogo value) {
		this.lid_base = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_luogo getLid_base() {
		return lid_base;
	}

	public void setLid_coll(it.iccu.sbn.polo.orm.bibliografica.Tb_luogo value) {
		this.lid_coll = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_luogo getLid_coll() {
		return lid_coll;
	}

	public String toString() {
		return String.valueOf(((getLid_base() == null) ? "" : String.valueOf(getLid_base().getORMID())) + " " + ((getLid_coll() == null) ? "" : String.valueOf(getLid_coll().getORMID())));
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
