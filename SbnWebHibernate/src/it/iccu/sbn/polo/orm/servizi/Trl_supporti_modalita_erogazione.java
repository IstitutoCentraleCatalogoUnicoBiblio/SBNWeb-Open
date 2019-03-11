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
public class Trl_supporti_modalita_erogazione extends Tb_base {

	private static final long serialVersionUID = 1232489753855364938L;

	public Trl_supporti_modalita_erogazione() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cod_erog == null) ? 0 : cod_erog.hashCode());
		result = prime * result + ((id_supporti_biblioteca == null) ? 0 : id_supporti_biblioteca.hashCode());
		result = prime * result + id_supporti_biblioteca_id_supporti_biblioteca;
		return result;
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trl_supporti_modalita_erogazione))
			return false;
		Trl_supporti_modalita_erogazione Trl_supporti_modalita_erogazione = (Trl_supporti_modalita_erogazione)aObj;
		if (getCod_erog() != Trl_supporti_modalita_erogazione.getCod_erog())
			return false;
		if (getId_supporti_biblioteca() == null && Trl_supporti_modalita_erogazione.getId_supporti_biblioteca() != null)
			return false;
		if (!getId_supporti_biblioteca().equals(Trl_supporti_modalita_erogazione.getId_supporti_biblioteca()))
			return false;
		if (getId_supporti_biblioteca_id_supporti_biblioteca() != Trl_supporti_modalita_erogazione.getId_supporti_biblioteca_id_supporti_biblioteca())
			return false;
		return true;
	}

	private String cod_erog;

	private it.iccu.sbn.polo.orm.servizi.Tbl_supporti_biblioteca id_supporti_biblioteca;

	public void setCod_erog(String value) {
		this.cod_erog = value;
	}

	public String getCod_erog() {
		return cod_erog;
	}

	public void setId_supporti_biblioteca(it.iccu.sbn.polo.orm.servizi.Tbl_supporti_biblioteca value) {
		this.id_supporti_biblioteca = value;
		if (value != null) {
			id_supporti_biblioteca_id_supporti_biblioteca = value.getId_supporti_biblioteca();
		}
		else {
			id_supporti_biblioteca_id_supporti_biblioteca = 0;
		}
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_supporti_biblioteca getId_supporti_biblioteca() {
		return id_supporti_biblioteca;
	}

	public String toString() {
		return String.valueOf(getCod_erog() + " " + getId_supporti_biblioteca_id_supporti_biblioteca());
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


	private int id_supporti_biblioteca_id_supporti_biblioteca;

	public void setId_supporti_biblioteca_id_supporti_biblioteca(int value) {
		this.id_supporti_biblioteca_id_supporti_biblioteca = value;
	}

	public int getId_supporti_biblioteca_id_supporti_biblioteca() {
		return id_supporti_biblioteca_id_supporti_biblioteca;
	}


}
