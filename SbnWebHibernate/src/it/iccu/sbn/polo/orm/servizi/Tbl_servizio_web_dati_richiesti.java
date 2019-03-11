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
 * Tipo di servizio / dati richiesti
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_servizio_web_dati_richiesti extends Tb_base {

	private static final long serialVersionUID = 6934046318893780106L;

	public Tbl_servizio_web_dati_richiesti() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbl_servizio_web_dati_richiesti))
			return false;
		Tbl_servizio_web_dati_richiesti tbl_servizio_web_dati_richiesti = (Tbl_servizio_web_dati_richiesti)aObj;
		if (getCampo_richiesta() != tbl_servizio_web_dati_richiesti.getCampo_richiesta())
			return false;
		if (getId_tipo_servizio() == null && tbl_servizio_web_dati_richiesti.getId_tipo_servizio() != null)
			return false;
		if (!getId_tipo_servizio().equals(tbl_servizio_web_dati_richiesti.getId_tipo_servizio()))
			return false;
		if (getId_tipo_servizio_id_tipo_servizio() != tbl_servizio_web_dati_richiesti.getId_tipo_servizio_id_tipo_servizio())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCampo_richiesta();
		if (getId_tipo_servizio() != null) {
			hashcode = hashcode + getId_tipo_servizio().getORMID();
		}
		hashcode = hashcode + getId_tipo_servizio_id_tipo_servizio();
		return hashcode;
	}

	private short campo_richiesta;

	private it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio id_tipo_servizio;

	private char obbligatorio;

	public void setCampo_richiesta(short value) {
		this.campo_richiesta = value;
	}

	public short getCampo_richiesta() {
		return campo_richiesta;
	}

	public void setObbligatorio(char value) {
		this.obbligatorio = value;
	}

	public char getObbligatorio() {
		return obbligatorio;
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
		return String.valueOf(getCampo_richiesta() + " " + getId_tipo_servizio_id_tipo_servizio());
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
