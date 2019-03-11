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
 * Diritti dell'utente
 */
/**
 * ORM-Persistable Class
 */
public class Trl_diritti_utente implements Serializable {

	private static final long serialVersionUID = -6166291447050117230L;

	public Trl_diritti_utente() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trl_diritti_utente))
			return false;
		Trl_diritti_utente trl_diritti_utente = (Trl_diritti_utente)aObj;
		if (getId_utenti() == null && trl_diritti_utente.getId_utenti() != null)
			return false;
		if (!getId_utenti().equals(trl_diritti_utente.getId_utenti()))
			return false;
		if (getId_servizio() == null && trl_diritti_utente.getId_servizio() != null)
			return false;
		if (!getId_servizio().equals(trl_diritti_utente.getId_servizio()))
			return false;
		if (getId_utenti_id_utenti() != trl_diritti_utente.getId_utenti_id_utenti())
			return false;
		if (getId_servizio_id_servizio() != trl_diritti_utente.getId_servizio_id_servizio())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_utenti() != null) {
			hashcode = hashcode + getId_utenti().getORMID();
		}
		if (getId_servizio() != null) {
			hashcode = hashcode + getId_servizio().getORMID();
		}
		hashcode = hashcode + getId_utenti_id_utenti();
		hashcode = hashcode + getId_servizio_id_servizio();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.servizi.Tbl_utenti id_utenti;

	private it.iccu.sbn.polo.orm.servizi.Tbl_servizio id_servizio;

	private java.util.Date data_inizio_serv;

	private java.util.Date data_fine_serv;

	private java.util.Date data_inizio_sosp;

	private java.util.Date data_fine_sosp;

	private String note;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private String cod_tipo_aut;

	/**
	 * data di inizio validita' del servizio
	 */
	public void setData_inizio_serv(java.util.Date value) {
		this.data_inizio_serv = value;
	}

	/**
	 * data di inizio validita' del servizio
	 */
	public java.util.Date getData_inizio_serv() {
		return data_inizio_serv;
	}

	/**
	 * data di fine  validita' del servizio
	 */
	public void setData_fine_serv(java.util.Date value) {
		this.data_fine_serv = value;
	}

	/**
	 * data di fine  validita' del servizio
	 */
	public java.util.Date getData_fine_serv() {
		return data_fine_serv;
	}

	/**
	 * data di inizio della sospensione' del servizio
	 */
	public void setData_inizio_sosp(java.util.Date value) {
		this.data_inizio_sosp = value;
	}

	/**
	 * data di inizio della sospensione' del servizio
	 */
	public java.util.Date getData_inizio_sosp() {
		return data_inizio_sosp;
	}

	/**
	 * data fine della sospensione del servizio
	 */
	public void setData_fine_sosp(java.util.Date value) {
		this.data_fine_sosp = value;
	}

	/**
	 * data fine della sospensione del servizio
	 */
	public java.util.Date getData_fine_sosp() {
		return data_fine_sosp;
	}

	/**
	 * note relative al diritto dell'utente
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * note relative al diritto dell'utente
	 */
	public String getNote() {
		return note;
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
	 * Flag di cancellazione logica valori ammessi S/N
	 */
	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	/**
	 * Flag di cancellazione logica valori ammessi S/N
	 */
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

	public void setId_servizio(it.iccu.sbn.polo.orm.servizi.Tbl_servizio value) {
		this.id_servizio = value;
		if (value != null) {
			id_servizio_id_servizio = value.getId_servizio();
		}
		else {
			id_servizio_id_servizio = 0;
		}
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_servizio getId_servizio() {
		return id_servizio;
	}

	public String toString() {
		return String.valueOf(getId_utenti_id_utenti() + " " + getId_servizio_id_servizio());
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

	private int id_servizio_id_servizio;

	public void setId_servizio_id_servizio(int value) {
		this.id_servizio_id_servizio = value;
	}

	public int getId_servizio_id_servizio() {
		return id_servizio_id_servizio;
	}

	public String getCod_tipo_aut() {
		return cod_tipo_aut;
	}

	public void setCod_tipo_aut(String cod_tipo_aut) {
		this.cod_tipo_aut = cod_tipo_aut;
	}



}
