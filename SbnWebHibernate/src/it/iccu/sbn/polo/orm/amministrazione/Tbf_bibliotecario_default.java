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
public class Tbf_bibliotecario_default implements Serializable {

	private static final long serialVersionUID = 2133931665739431222L;

	public Tbf_bibliotecario_default() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_bibliotecario_default))
			return false;
		Tbf_bibliotecario_default tbf_bibliotecario_default = (Tbf_bibliotecario_default)aObj;
		if (getId_utente_professionale() == null && tbf_bibliotecario_default.getId_utente_professionale() != null)
			return false;
		if (!getId_utente_professionale().equals(tbf_bibliotecario_default.getId_utente_professionale()))
			return false;
		if (getId_default() == null && tbf_bibliotecario_default.getId_default() != null)
			return false;
		if (!getId_default().equals(tbf_bibliotecario_default.getId_default()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_utente_professionale() != null) {
			hashcode = hashcode + (getId_utente_professionale().getORMID() == null ? 0 : getId_utente_professionale().getORMID().hashCode());
		}
		if (getId_default() != null) {
			hashcode = hashcode + getId_default().getORMID();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario id_utente_professionale;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_default id_default;

	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setId_utente_professionale(it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario value) {
		this.id_utente_professionale = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario getId_utente_professionale() {
		return id_utente_professionale;
	}

	public void setId_default(it.iccu.sbn.polo.orm.amministrazione.Tbf_default value) {
		this.id_default = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_default getId_default() {
		return id_default;
	}

	public String toString() {
		return String.valueOf(((getId_utente_professionale() == null) ? "" : String.valueOf(getId_utente_professionale().getORMID())) + " " + ((getId_default() == null) ? "" : String.valueOf(getId_default().getORMID())));
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
