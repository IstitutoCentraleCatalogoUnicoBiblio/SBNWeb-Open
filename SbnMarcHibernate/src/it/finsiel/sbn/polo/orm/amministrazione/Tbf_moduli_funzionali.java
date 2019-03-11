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
package it.finsiel.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Tbf_moduli_funzionali implements Serializable {

	private static final long serialVersionUID = -18816463322925136L;

	public Tbf_moduli_funzionali() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_moduli_funzionali))
			return false;
		Tbf_moduli_funzionali tbf_moduli_funzionali = (Tbf_moduli_funzionali)aObj;
		if (getId_modulo() != tbf_moduli_funzionali.getId_modulo())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getId_modulo();
		return hashcode;
	}

	private int id_modulo;

	private String ds_modulo;

	private java.util.Set Tbf_attivita_sbnmarc = new java.util.HashSet();

	public void setId_modulo(int value) {
		this.id_modulo = value;
	}

	public int getId_modulo() {
		return id_modulo;
	}

	public int getORMID() {
		return getId_modulo();
	}

	public void setDs_modulo(String value) {
		this.ds_modulo = value;
	}

	public String getDs_modulo() {
		return ds_modulo;
	}

	public void setTbf_attivita_sbnmarc(java.util.Set value) {
		this.Tbf_attivita_sbnmarc = value;
	}

	public java.util.Set getTbf_attivita_sbnmarc() {
		return Tbf_attivita_sbnmarc;
	}


	public String toString() {
		return String.valueOf(getId_modulo());
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
