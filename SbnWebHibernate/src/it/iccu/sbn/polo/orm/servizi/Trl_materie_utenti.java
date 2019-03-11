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
package it.iccu.sbn.polo.orm.servizi;

import java.io.Serializable;
/**
 * Materia d'interesse degli utenti
 */
/**
 * ORM-Persistable Class
 */
public class Trl_materie_utenti implements Serializable {

	private static final long serialVersionUID = -5144969468719883731L;

	public Trl_materie_utenti() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trl_materie_utenti))
			return false;
		Trl_materie_utenti trl_materie_utenti = (Trl_materie_utenti)aObj;
		if (getId_utenti() == null && trl_materie_utenti.getId_utenti() != null)
			return false;
		if (!getId_utenti().equals(trl_materie_utenti.getId_utenti()))
			return false;
		if (getId_materia() == null && trl_materie_utenti.getId_materia() != null)
			return false;
		if (!getId_materia().equals(trl_materie_utenti.getId_materia()))
			return false;
		if (getId_utenti_id_utenti() != trl_materie_utenti.getId_utenti_id_utenti())
			return false;
		if (getId_materia_id_materia() != trl_materie_utenti.getId_materia_id_materia())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_utenti() != null) {
			hashcode = hashcode + getId_utenti().getORMID();
		}
		if (getId_materia() != null) {
			hashcode = hashcode + getId_materia().getORMID();
		}
		hashcode = hashcode + getId_utenti_id_utenti();
		hashcode = hashcode + getId_materia_id_materia();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.servizi.Tbl_utenti id_utenti;

	private it.iccu.sbn.polo.orm.servizi.Tbl_materie id_materia;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * data e ora d'inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * data e ora d'inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setId_utenti(it.iccu.sbn.polo.orm.servizi.Tbl_utenti value) {
		this.id_utenti = value;
		if (value != null) {
			id_utenti_id_utenti = value.getId_utenti();
		}
		else {
			id_utenti_id_utenti = 0;
		}
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_utenti getId_utenti() {
		return id_utenti;
	}

	public void setId_materia(it.iccu.sbn.polo.orm.servizi.Tbl_materie value) {
		this.id_materia = value;
		if (value != null) {
			id_materia_id_materia = value.getId_materia();
		}
		else {
			id_materia_id_materia = 0;
		}
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_materie getId_materia() {
		return id_materia;
	}

	public String toString() {
		return String.valueOf(getId_utenti_id_utenti() + " " + getId_materia_id_materia());
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


	private int id_utenti_id_utenti;

	public void setId_utenti_id_utenti(int value) {
		this.id_utenti_id_utenti = value;
	}

	public int getId_utenti_id_utenti() {
		return id_utenti_id_utenti;
	}

	private int id_materia_id_materia;

	public void setId_materia_id_materia(int value) {
		this.id_materia_id_materia = value;
	}

	public int getId_materia_id_materia() {
		return id_materia_id_materia;
	}

}
