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
 * COLLEGAMENTI TRA POSSESSORI
 */
/**
 * ORM-Persistable Class
 */
public class Trc_possessori_possessori implements Serializable {

	private static final long serialVersionUID = -810872922516409186L;

	public Trc_possessori_possessori() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trc_possessori_possessori))
			return false;
		Trc_possessori_possessori trc_possessori_possessori = (Trc_possessori_possessori)aObj;
		if (getPid_base() == null && trc_possessori_possessori.getPid_base() != null)
			return false;
		if (!getPid_base().equals(trc_possessori_possessori.getPid_base()))
			return false;
		if (getPid_coll() == null && trc_possessori_possessori.getPid_coll() != null)
			return false;
		if (!getPid_coll().equals(trc_possessori_possessori.getPid_coll()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getPid_base() != null) {
			hashcode = hashcode + (getPid_base().getORMID() == null ? 0 : getPid_base().getORMID().hashCode());
		}
		if (getPid_coll() != null) {
			hashcode = hashcode + (getPid_coll().getORMID() == null ? 0 : getPid_coll().getORMID().hashCode());
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza pid_base;

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza pid_coll;

	private char tp_legame;

	private String nota;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	/**
	 * codice del collegamento tra autori
	 */
	public void setTp_legame(char value) {
		this.tp_legame = value;
	}

	/**
	 * codice del collegamento tra autori
	 */
	public char getTp_legame() {
		return tp_legame;
	}

	/**
	 * nota al legame
	 */
	public void setNota(String value) {
		this.nota = value;
	}

	/**
	 * nota al legame
	 */
	public String getNota() {
		return nota;
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

	public void setPid_base(it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza value) {
		this.pid_base = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza getPid_base() {
		return pid_base;
	}

	public void setPid_coll(it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza value) {
		this.pid_coll = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza getPid_coll() {
		return pid_coll;
	}

	public String toString() {
		return String.valueOf(((getPid_base() == null) ? "" : String.valueOf(getPid_base().getORMID())) + " " + ((getPid_coll() == null) ? "" : String.valueOf(getPid_coll().getORMID())));
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
