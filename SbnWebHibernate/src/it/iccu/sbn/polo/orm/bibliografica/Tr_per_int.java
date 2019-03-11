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
 * COLLEGAMENTO PERSONAGGIO INTERPRETE (Musica)
 */
/**
 * ORM-Persistable Class
 */
public class Tr_per_int implements Serializable {

	private static final long serialVersionUID = -7777146798769033120L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_per_int))
			return false;
		Tr_per_int tr_per_int = (Tr_per_int)aObj;
		if (getV() == null)
			return false;
		if (!getV().getORMID().equals(tr_per_int.getV().getORMID()))
			return false;
		if (getId_personaggio() == null)
			return false;
		if (getId_personaggio().getORMID() != tr_per_int.getId_personaggio().getORMID())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getV() != null) {
			hashcode = hashcode + getV().getORMID().hashCode();
		}
		if (getId_personaggio() != null) {
			hashcode = hashcode + getId_personaggio().getORMID();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_autore v;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_personaggio id_personaggio;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

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

	public void setV(it.iccu.sbn.polo.orm.bibliografica.Tb_autore value) {
		this.v = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_autore getV() {
		return v;
	}

	public void setId_personaggio(it.iccu.sbn.polo.orm.bibliografica.Tb_personaggio value) {
		this.id_personaggio = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_personaggio getId_personaggio() {
		return id_personaggio;
	}

	public String toString() {
		return String.valueOf(((getV() == null) ? "" : String.valueOf(getV().getORMID())) + " " + ((getId_personaggio() == null) ? "" : String.valueOf(getId_personaggio().getORMID())));
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
