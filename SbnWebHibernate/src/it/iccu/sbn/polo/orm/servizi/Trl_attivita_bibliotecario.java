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
 * Attività del bibliotecario
 */
/**
 * ORM-Persistable Class
 */
public class Trl_attivita_bibliotecario implements Serializable {

	private static final long serialVersionUID = 473902509734967654L;

	public Trl_attivita_bibliotecario() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trl_attivita_bibliotecario))
			return false;
		Trl_attivita_bibliotecario trl_attivita_bibliotecario = (Trl_attivita_bibliotecario)aObj;
		if (getId_bibliotecario() == null && trl_attivita_bibliotecario.getId_bibliotecario() != null)
			return false;
		if (!getId_bibliotecario().equals(trl_attivita_bibliotecario.getId_bibliotecario()))
			return false;
		if (getId_iter_servizio() == null && trl_attivita_bibliotecario.getId_iter_servizio() != null)
			return false;
		if (!getId_iter_servizio().equals(trl_attivita_bibliotecario.getId_iter_servizio()))
			return false;
		if (getId_bibliotecario_id_utente_professionale() != trl_attivita_bibliotecario.getId_bibliotecario_id_utente_professionale())
			return false;
		if (getId_iter_servizio_id_iter_servizio() != trl_attivita_bibliotecario.getId_iter_servizio_id_iter_servizio())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_bibliotecario() != null) {
			hashcode = hashcode + (getId_bibliotecario().getORMID() == null ? 0 : getId_bibliotecario().getORMID().hashCode());
		}
		if (getId_iter_servizio() != null) {
			hashcode = hashcode + getId_iter_servizio().getORMID();
		}
		hashcode = hashcode + getId_bibliotecario_id_utente_professionale();
		hashcode = hashcode + getId_iter_servizio_id_iter_servizio();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario id_bibliotecario;

	private it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio id_iter_servizio;

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

	public void setId_bibliotecario(it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario value) {
		this.id_bibliotecario = value;
		if (value != null) {
			id_bibliotecario_id_utente_professionale = value.getId_utente_professionale().getId_utente_professionale();
		}
		else {
			id_bibliotecario_id_utente_professionale = 0;
		}
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario getId_bibliotecario() {
		return id_bibliotecario;
	}

	public void setId_iter_servizio(it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio value) {
		this.id_iter_servizio = value;
		if (value != null) {
			id_iter_servizio_id_iter_servizio = value.getId_iter_servizio();
		}
		else {
			id_iter_servizio_id_iter_servizio = 0;
		}
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio getId_iter_servizio() {
		return id_iter_servizio;
	}

	public String toString() {
		return String.valueOf(getId_bibliotecario_id_utente_professionale() + " " + getId_iter_servizio_id_iter_servizio());
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


	private int id_bibliotecario_id_utente_professionale;

	public void setId_bibliotecario_id_utente_professionale(int value) {
		this.id_bibliotecario_id_utente_professionale = value;
	}

	public int getId_bibliotecario_id_utente_professionale() {
		return id_bibliotecario_id_utente_professionale;
	}

	private int id_iter_servizio_id_iter_servizio;

	public void setId_iter_servizio_id_iter_servizio(int value) {
		this.id_iter_servizio_id_iter_servizio = value;
	}

	public int getId_iter_servizio_id_iter_servizio() {
		return id_iter_servizio_id_iter_servizio;
	}

}
