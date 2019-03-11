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
 * MARCHE DEL REPERTORIO
 */
/**
 * ORM-Persistable Class
 */
public class Tr_rep_mar implements Serializable {

	private static final long serialVersionUID = 8524051628721247322L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_rep_mar))
			return false;
		Tr_rep_mar tr_rep_mar = (Tr_rep_mar)aObj;
		if (getProgr_repertorio() != tr_rep_mar.getProgr_repertorio())
			return false;
		if (getM() == null)
			return false;
		if (!getM().getORMID().equals(tr_rep_mar.getM().getORMID()))
			return false;
		if (getId_repertorio() == null)
			return false;
		if (getId_repertorio().getORMID() != tr_rep_mar.getId_repertorio().getORMID())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getProgr_repertorio();
		if (getM() != null) {
			hashcode = hashcode + getM().getORMID().hashCode();
		}
		if (getId_repertorio() != null) {
			hashcode = hashcode + getId_repertorio().getORMID();
		}
		return hashcode;
	}

	private int progr_repertorio;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_marca m;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_repertorio id_repertorio;

	private String nota_rep_mar;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setProgr_repertorio(int value) {
		this.progr_repertorio = value;
	}

	public int getProgr_repertorio() {
		return progr_repertorio;
	}

	public void setNota_rep_mar(String value) {
		this.nota_rep_mar = value;
	}

	public String getNota_rep_mar() {
		return nota_rep_mar;
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

	public void setM(it.iccu.sbn.polo.orm.bibliografica.Tb_marca value) {
		this.m = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_marca getM() {
		return m;
	}

	public void setId_repertorio(it.iccu.sbn.polo.orm.bibliografica.Tb_repertorio value) {
		this.id_repertorio = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_repertorio getId_repertorio() {
		return id_repertorio;
	}

	public String toString() {
		return String.valueOf(getProgr_repertorio() + " " + ((getM() == null) ? "" : String.valueOf(getM().getORMID())) + " " + ((getId_repertorio() == null) ? "" : String.valueOf(getId_repertorio().getORMID())));
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
