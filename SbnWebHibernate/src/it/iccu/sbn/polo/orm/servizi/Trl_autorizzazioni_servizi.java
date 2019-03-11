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
 * AUTORIZZAZIONI SERVIZI UTENTE
 */
/**
 * ORM-Persistable Class
 */
public class Trl_autorizzazioni_servizi extends Tb_base {

	private static final long serialVersionUID = 4327766866709686239L;

	public Trl_autorizzazioni_servizi() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trl_autorizzazioni_servizi))
			return false;
		Trl_autorizzazioni_servizi trl_autorizzazioni_servizi = (Trl_autorizzazioni_servizi)aObj;
		if (getId_tipi_autorizzazione() == null && trl_autorizzazioni_servizi.getId_tipi_autorizzazione() != null)
			return false;
		if (!getId_tipi_autorizzazione().equals(trl_autorizzazioni_servizi.getId_tipi_autorizzazione()))
			return false;
		if (getId_servizio() == null && trl_autorizzazioni_servizi.getId_servizio() != null)
			return false;
		if (!getId_servizio().equals(trl_autorizzazioni_servizi.getId_servizio()))
			return false;
		if (getId_tipi_autorizzazione_id_tipi_autorizzazione() != trl_autorizzazioni_servizi.getId_tipi_autorizzazione_id_tipi_autorizzazione())
			return false;
		if (getId_servizio_id_servizio() != trl_autorizzazioni_servizi.getId_servizio_id_servizio())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_tipi_autorizzazione() != null) {
			hashcode = hashcode + getId_tipi_autorizzazione().getORMID();
		}
		if (getId_servizio() != null) {
			hashcode = hashcode + getId_servizio().getORMID();
		}
		hashcode = hashcode + getId_tipi_autorizzazione_id_tipi_autorizzazione();
		hashcode = hashcode + getId_servizio_id_servizio();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.servizi.Tbl_tipi_autorizzazioni id_tipi_autorizzazione;

	private it.iccu.sbn.polo.orm.servizi.Tbl_servizio id_servizio;

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

	public void setId_tipi_autorizzazione(it.iccu.sbn.polo.orm.servizi.Tbl_tipi_autorizzazioni value) {
		this.id_tipi_autorizzazione = value;
		if (value != null) {
			id_tipi_autorizzazione_id_tipi_autorizzazione = value.getId_tipi_autorizzazione();
		}
		else {
			id_tipi_autorizzazione_id_tipi_autorizzazione = 0;
		}
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_tipi_autorizzazioni getId_tipi_autorizzazione() {
		return id_tipi_autorizzazione;
	}

	public String toString() {
		return String.valueOf(getId_tipi_autorizzazione_id_tipi_autorizzazione() + " " + getId_servizio_id_servizio());
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


	private int id_tipi_autorizzazione_id_tipi_autorizzazione;

	public void setId_tipi_autorizzazione_id_tipi_autorizzazione(int value) {
		this.id_tipi_autorizzazione_id_tipi_autorizzazione = value;
	}

	public int getId_tipi_autorizzazione_id_tipi_autorizzazione() {
		return id_tipi_autorizzazione_id_tipi_autorizzazione;
	}

	private int id_servizio_id_servizio;

	public void setId_servizio_id_servizio(int value) {
		this.id_servizio_id_servizio = value;
	}

	public int getId_servizio_id_servizio() {
		return id_servizio_id_servizio;
	}

}
