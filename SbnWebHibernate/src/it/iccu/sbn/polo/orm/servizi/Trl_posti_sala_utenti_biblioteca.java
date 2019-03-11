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
 * ORM-Persistable Class
 */
public class Trl_posti_sala_utenti_biblioteca implements Serializable {

	private static final long serialVersionUID = -7452285078983017985L;

	public Trl_posti_sala_utenti_biblioteca() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trl_posti_sala_utenti_biblioteca))
			return false;
		Trl_posti_sala_utenti_biblioteca trl_posti_sala_utenti_biblioteca = (Trl_posti_sala_utenti_biblioteca)aObj;
		if (getId_posti_sala() == null && trl_posti_sala_utenti_biblioteca.getId_posti_sala() != null)
			return false;
		if (!getId_posti_sala().equals(trl_posti_sala_utenti_biblioteca.getId_posti_sala()))
			return false;
		if (getId_utenti_biblioteca() == null && trl_posti_sala_utenti_biblioteca.getId_utenti_biblioteca() != null)
			return false;
		if (!getId_utenti_biblioteca().equals(trl_posti_sala_utenti_biblioteca.getId_utenti_biblioteca()))
			return false;
		if (getId_posti_sala_id_posti_sala() != trl_posti_sala_utenti_biblioteca.getId_posti_sala_id_posti_sala())
			return false;
		if (getId_utenti_biblioteca_id_utenti_biblioteca() != trl_posti_sala_utenti_biblioteca.getId_utenti_biblioteca_id_utenti_biblioteca())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_posti_sala() != null) {
			hashcode = hashcode + getId_posti_sala().getORMID();
		}
		if (getId_utenti_biblioteca() != null) {
			hashcode = hashcode + getId_utenti_biblioteca().getORMID();
		}
		hashcode = hashcode + getId_posti_sala_id_posti_sala();
		hashcode = hashcode + getId_utenti_biblioteca_id_utenti_biblioteca();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.servizi.Tbl_posti_sala id_posti_sala;

	private it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca id_utenti_biblioteca;

	private java.sql.Timestamp ts_ingresso;

	private java.sql.Timestamp ts_uscita;

	private String ute_ins;

	private String ute_var;

	private java.sql.Timestamp ts_ins;

	private java.sql.Timestamp ts_var;

	public void setTs_ingresso(java.sql.Timestamp value) {
		this.ts_ingresso = value;
	}

	public java.sql.Timestamp getTs_ingresso() {
		return ts_ingresso;
	}

	public void setTs_uscita(java.sql.Timestamp value) {
		this.ts_uscita = value;
	}

	public java.sql.Timestamp getTs_uscita() {
		return ts_uscita;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setId_posti_sala(it.iccu.sbn.polo.orm.servizi.Tbl_posti_sala value) {
		this.id_posti_sala = value;
		if (value != null) {
			id_posti_sala_id_posti_sala = value.getId_posti_sala();
		}
		else {
			id_posti_sala_id_posti_sala = 0;
		}
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_posti_sala getId_posti_sala() {
		return id_posti_sala;
	}

	public void setId_utenti_biblioteca(it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca value) {
		this.id_utenti_biblioteca = value;
		if (value != null) {
			id_utenti_biblioteca_id_utenti_biblioteca = value.getId_utenti_biblioteca();
		}
		else {
			id_utenti_biblioteca_id_utenti_biblioteca = 0;
		}
	}

	public it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca getId_utenti_biblioteca() {
		return id_utenti_biblioteca;
	}

	public String toString() {
		return String.valueOf(getId_posti_sala_id_posti_sala() + " " + getId_utenti_biblioteca_id_utenti_biblioteca());
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


	private int id_posti_sala_id_posti_sala;

	public void setId_posti_sala_id_posti_sala(int value) {
		this.id_posti_sala_id_posti_sala = value;
	}

	public int getId_posti_sala_id_posti_sala() {
		return id_posti_sala_id_posti_sala;
	}

	private int id_utenti_biblioteca_id_utenti_biblioteca;

	public void setId_utenti_biblioteca_id_utenti_biblioteca(int value) {
		this.id_utenti_biblioteca_id_utenti_biblioteca = value;
	}

	public int getId_utenti_biblioteca_id_utenti_biblioteca() {
		return id_utenti_biblioteca_id_utenti_biblioteca;
	}

}
