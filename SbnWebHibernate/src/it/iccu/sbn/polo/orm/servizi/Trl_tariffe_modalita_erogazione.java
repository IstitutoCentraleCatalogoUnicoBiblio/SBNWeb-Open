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

import it.iccu.sbn.polo.orm.Tb_base;
/**
 * Tariffe delle modalità di erogazione
 */
/**
 * ORM-Persistable Class
 */
public class Trl_tariffe_modalita_erogazione extends Tb_base {

	private static final long serialVersionUID = -1275899888945719908L;

	public Trl_tariffe_modalita_erogazione() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cod_erog == null) ? 0 : cod_erog.hashCode());
		result = prime * result + ((id_tipo_servizio == null) ? 0 : id_tipo_servizio.hashCode());
		result = prime * result + id_tipo_servizio_id_tipo_servizio;
		return result;
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trl_tariffe_modalita_erogazione))
			return false;
		Trl_tariffe_modalita_erogazione trl_tariffe_modalita_erogazione = (Trl_tariffe_modalita_erogazione)aObj;
		if (getCod_erog() != trl_tariffe_modalita_erogazione.getCod_erog())
			return false;
		if (getId_tipo_servizio() == null && trl_tariffe_modalita_erogazione.getId_tipo_servizio() != null)
			return false;
		if (!getId_tipo_servizio().equals(trl_tariffe_modalita_erogazione.getId_tipo_servizio()))
			return false;
		if (getId_tipo_servizio_id_tipo_servizio() != trl_tariffe_modalita_erogazione.getId_tipo_servizio_id_tipo_servizio())
			return false;
		return true;
	}

	private String cod_erog;

	private it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio id_tipo_servizio;

	public void setCod_erog(String value) {
		this.cod_erog = value;
	}

	public String getCod_erog() {
		return cod_erog;
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
		return String.valueOf(getCod_erog() + " " + getId_tipo_servizio_id_tipo_servizio());
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
