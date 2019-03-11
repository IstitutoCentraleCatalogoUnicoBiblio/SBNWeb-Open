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
package it.iccu.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Trf_gruppo_attivita_polo implements Serializable {

	private static final long serialVersionUID = 8911034067911518242L;

	public Trf_gruppo_attivita_polo() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trf_gruppo_attivita_polo))
			return false;
		Trf_gruppo_attivita_polo trf_gruppo_attivita_polo = (Trf_gruppo_attivita_polo)aObj;
		if (getId_attivita_polo() == null && trf_gruppo_attivita_polo.getId_attivita_polo() != null)
			return false;
		if (!getId_attivita_polo().equals(trf_gruppo_attivita_polo.getId_attivita_polo()))
			return false;
		if (getId_gruppo_attivita_polo() == null && trf_gruppo_attivita_polo.getId_gruppo_attivita_polo() != null)
			return false;
		if (!getId_gruppo_attivita_polo().equals(trf_gruppo_attivita_polo.getId_gruppo_attivita_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_attivita_polo() != null) {
			hashcode = hashcode + getId_attivita_polo().getORMID();
		}
		if (getId_gruppo_attivita_polo() != null) {
			hashcode = hashcode + getId_gruppo_attivita_polo().getORMID();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Trf_attivita_polo id_attivita_polo;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita id_gruppo_attivita_polo;

	private char fl_include;

	/**
	 * Flag di inclusione od esclusione (S include, N esclude)
	 */
	public void setFl_include(char value) {
		this.fl_include = value;
	}

	/**
	 * Flag di inclusione od esclusione (S include, N esclude)
	 */
	public char getFl_include() {
		return fl_include;
	}

	public void setId_gruppo_attivita_polo(it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita value) {
		this.id_gruppo_attivita_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita getId_gruppo_attivita_polo() {
		return id_gruppo_attivita_polo;
	}

	public void setId_attivita_polo(it.iccu.sbn.polo.orm.amministrazione.Trf_attivita_polo value) {
		this.id_attivita_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Trf_attivita_polo getId_attivita_polo() {
		return id_attivita_polo;
	}

	public String toString() {
		return String.valueOf(((getId_attivita_polo() == null) ? "" : String.valueOf(getId_attivita_polo().getORMID())) + " " + ((getId_gruppo_attivita_polo() == null) ? "" : String.valueOf(getId_gruppo_attivita_polo().getORMID())));
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
