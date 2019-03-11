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
 * COLLEGAMENTO TITOLO REPERTORIO
 */
/**
 * ORM-Persistable Class
 */
public class Tr_rep_tit implements Serializable {

	private static final long serialVersionUID = -8584030921485965725L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_rep_tit))
			return false;
		Tr_rep_tit tr_rep_tit = (Tr_rep_tit)aObj;
		if (getB() == null)
			return false;
		if (!getB().getORMID().equals(tr_rep_tit.getB().getORMID()))
			return false;
		if (getId_repertorio() == null)
			return false;
		if (getId_repertorio().getORMID() != tr_rep_tit.getId_repertorio().getORMID())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getB() != null) {
			hashcode = hashcode + getB().getORMID().hashCode();
		}
		if (getId_repertorio() != null) {
			hashcode = hashcode + getId_repertorio().getORMID();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_repertorio id_repertorio;

	private String nota_rep_tit;

	private char fl_trovato;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setNota_rep_tit(String value) {
		this.nota_rep_tit = value;
	}

	public String getNota_rep_tit() {
		return nota_rep_tit;
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

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public void setId_repertorio(it.iccu.sbn.polo.orm.bibliografica.Tb_repertorio value) {
		this.id_repertorio = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_repertorio getId_repertorio() {
		return id_repertorio;
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())) + " " + ((getId_repertorio() == null) ? "" : String.valueOf(getId_repertorio().getORMID())));
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
