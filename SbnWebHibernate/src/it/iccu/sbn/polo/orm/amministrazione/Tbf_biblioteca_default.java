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
public class Tbf_biblioteca_default implements Serializable {

	private static final long serialVersionUID = -861001769198340693L;

	public Tbf_biblioteca_default() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_biblioteca_default))
			return false;
		Tbf_biblioteca_default tbf_biblioteca_default = (Tbf_biblioteca_default)aObj;
		if (getId_default() == null && tbf_biblioteca_default.getId_default() != null)
			return false;
		if (!getId_default().equals(tbf_biblioteca_default.getId_default()))
			return false;
		if (getCd_biblioteca() == null && tbf_biblioteca_default.getCd_biblioteca() != null)
			return false;
		if (!getCd_biblioteca().equals(tbf_biblioteca_default.getCd_biblioteca()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_default() != null) {
			hashcode = hashcode + getId_default().getORMID();
		}
		if (getCd_biblioteca() != null) {
			hashcode = hashcode + (getCd_biblioteca().getCd_biblioteca() == null ? 0 : getCd_biblioteca().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_biblioteca().getCd_polo() == null ? 0 : getCd_biblioteca().getCd_polo().hashCode());
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_default id_default;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setId_default(it.iccu.sbn.polo.orm.amministrazione.Tbf_default value) {
		this.id_default = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_default getId_default() {
		return id_default;
	}

	public void setCd_biblioteca(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_biblioteca() {
		return cd_biblioteca;
	}

	public String toString() {
		return String.valueOf(((getId_default() == null) ? "" : String.valueOf(getId_default().getORMID())) + " " + ((getCd_biblioteca() == null) ? "" : String.valueOf(getCd_biblioteca().getCd_biblioteca()) + " " + String.valueOf(getCd_biblioteca().getCd_polo())));
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
