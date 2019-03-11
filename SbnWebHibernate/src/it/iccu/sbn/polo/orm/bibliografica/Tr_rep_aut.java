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
 * COLLEGAMENTI AUTORE REPERTORIO
 */
/**
 * ORM-Persistable Class
 */
public class Tr_rep_aut implements Serializable {

	private static final long serialVersionUID = 7947683014167888664L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_rep_aut))
			return false;
		Tr_rep_aut tr_rep_aut = (Tr_rep_aut)aObj;
		if (getV() == null)
			return false;
		if (!getV().getORMID().equals(tr_rep_aut.getV().getORMID()))
			return false;
		if (getId_repertorio() == null)
			return false;
		if (getId_repertorio().getORMID() != tr_rep_aut.getId_repertorio().getORMID())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getV() != null) {
			hashcode = hashcode + getV().getORMID().hashCode();
		}
		if (getId_repertorio() != null) {
			hashcode = hashcode + getId_repertorio().getORMID();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_autore v;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_repertorio id_repertorio;

	private String note_rep_aut;

	private char fl_trovato;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setNote_rep_aut(String value) {
		this.note_rep_aut = value;
	}

	public String getNote_rep_aut() {
		return note_rep_aut;
	}

	public void setFl_trovato(char value) {
		this.fl_trovato = value;
	}

	public char getFl_trovato() {
		return fl_trovato;
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

	public void setV(it.iccu.sbn.polo.orm.bibliografica.Tb_autore value) {
		this.v = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_autore getV() {
		return v;
	}

	public void setId_repertorio(it.iccu.sbn.polo.orm.bibliografica.Tb_repertorio value) {
		this.id_repertorio = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_repertorio getId_repertorio() {
		return id_repertorio;
	}

	public String toString() {
		return String.valueOf(((getV() == null) ? "" : String.valueOf(getV().getORMID())) + " " + ((getId_repertorio() == null) ? "" : String.valueOf(getId_repertorio().getORMID())));
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
