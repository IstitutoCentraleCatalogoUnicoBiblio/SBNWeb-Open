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
 * COLLEGAMENTI TRA AUTORI
 */
/**
 * ORM-Persistable Class
 */
public class Tr_aut_aut implements Serializable {

	private static final long serialVersionUID = -7150774739533939216L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_aut_aut))
			return false;
		Tr_aut_aut tr_aut_aut = (Tr_aut_aut)aObj;
		if (getVid_base() == null)
			return false;
		if (!getVid_base().getORMID().equals(tr_aut_aut.getVid_base().getORMID()))
			return false;
		if (getVid_coll() == null)
			return false;
		if (!getVid_coll().getORMID().equals(tr_aut_aut.getVid_coll().getORMID()))
			return false;
		if (getTp_legame() != tr_aut_aut.getTp_legame())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getVid_base() != null) {
			hashcode = hashcode + getVid_base().getORMID().hashCode();
		}
		if (getVid_coll() != null) {
			hashcode = hashcode + getVid_coll().getORMID().hashCode();
		}
		hashcode = hashcode + getTp_legame();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_autore vid_base;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_autore vid_coll;

	private char tp_legame;

	private String nota_aut_aut;

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

	public void setNota_aut_aut(String value) {
		this.nota_aut_aut = value;
	}

	public String getNota_aut_aut() {
		return nota_aut_aut;
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

	public void setVid_base(it.iccu.sbn.polo.orm.bibliografica.Tb_autore value) {
		this.vid_base = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_autore getVid_base() {
		return vid_base;
	}

	public void setVid_coll(it.iccu.sbn.polo.orm.bibliografica.Tb_autore value) {
		this.vid_coll = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_autore getVid_coll() {
		return vid_coll;
	}

	public String toString() {
		return String.valueOf(((getVid_base() == null) ? "" : String.valueOf(getVid_base().getORMID())) + " " + ((getVid_coll() == null) ? "" : String.valueOf(getVid_coll().getORMID())) + " " + getTp_legame());
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
