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
 * Calendario erogazione servizi
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_calendario_servizi implements Serializable {

	private static final long serialVersionUID = 7856187192630084996L;

	public Tbl_calendario_servizi() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbl_calendario_servizi))
			return false;
		Tbl_calendario_servizi tbl_calendario_servizi = (Tbl_calendario_servizi)aObj;
		if (getId_tipo_servizio() == null && tbl_calendario_servizi.getId_tipo_servizio() != null)
			return false;
		if (!getId_tipo_servizio().equals(tbl_calendario_servizi.getId_tipo_servizio()))
			return false;
		if (getProgr_fascia() != tbl_calendario_servizi.getProgr_fascia())
			return false;
		if (getId_tipo_servizio_id_tipo_servizio() != tbl_calendario_servizi.getId_tipo_servizio_id_tipo_servizio())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_tipo_servizio() != null) {
			hashcode = hashcode + getId_tipo_servizio().getORMID();
		}
		hashcode = hashcode + getProgr_fascia();
		hashcode = hashcode + getId_tipo_servizio_id_tipo_servizio();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio id_tipo_servizio;

	private int progr_fascia;

	private java.sql.Timestamp ore_in;

	private java.sql.Timestamp ore_fi;

	private java.util.Date data;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setProgr_fascia(int value) {
		this.progr_fascia = value;
	}

	public int getProgr_fascia() {
		return progr_fascia;
	}

	/**
	 * ora di inizio della fascia oraria di validita'
	 */
	public void setOre_in(java.sql.Timestamp value) {
		this.ore_in = value;
	}

	/**
	 * ora di inizio della fascia oraria di validita'
	 */
	public java.sql.Timestamp getOre_in() {
		return ore_in;
	}

	/**
	 * ora di fine della fascia oraria di validita'
	 */
	public void setOre_fi(java.sql.Timestamp value) {
		this.ore_fi = value;
	}

	/**
	 * ora di fine della fascia oraria di validita'
	 */
	public java.sql.Timestamp getOre_fi() {
		return ore_fi;
	}

	/**
	 * data del giorno
	 */
	public void setData(java.util.Date value) {
		this.data = value;
	}

	/**
	 * data del giorno
	 */
	public java.util.Date getData() {
		return data;
	}

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

	public void setId_tipo_servizio(it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio value) {
		this.id_tipo_servizio = value;
		if (value != null) {
			id_tipo_servizio_id_tipo_servizio = value.getId_tipo_servizio();
		}
		else {
			id_tipo_servizio_id_tipo_servizio = 0;
		}
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio getId_tipo_servizio() {
		return id_tipo_servizio;
	}

	public String toString() {
		return String.valueOf(getProgr_fascia() + " " + getId_tipo_servizio_id_tipo_servizio());
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


	private int id_tipo_servizio_id_tipo_servizio;

	public void setId_tipo_servizio_id_tipo_servizio(int value) {
		this.id_tipo_servizio_id_tipo_servizio = value;
	}

	public int getId_tipo_servizio_id_tipo_servizio() {
		return id_tipo_servizio_id_tipo_servizio;
	}

}
