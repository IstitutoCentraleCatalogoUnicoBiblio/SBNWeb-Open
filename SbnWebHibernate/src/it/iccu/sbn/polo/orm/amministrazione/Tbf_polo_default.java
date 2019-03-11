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
public class Tbf_polo_default implements Serializable {

	private static final long serialVersionUID = 7301380541110543021L;

	public Tbf_polo_default() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_polo_default))
			return false;
		Tbf_polo_default tbf_polo_default = (Tbf_polo_default)aObj;
		if (getId_default() == null && tbf_polo_default.getId_default() != null)
			return false;
		if (!getId_default().equals(tbf_polo_default.getId_default()))
			return false;
		if (getCd_polo() == null && tbf_polo_default.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(tbf_polo_default.getCd_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_default() != null) {
			hashcode = hashcode + getId_default().getORMID();
		}
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getORMID() == null ? 0 : getCd_polo().getORMID().hashCode());
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_default id_default;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_polo cd_polo;

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

	public void setCd_polo(it.iccu.sbn.polo.orm.amministrazione.Tbf_polo value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_polo getCd_polo() {
		return cd_polo;
	}

	public String toString() {
		return String.valueOf(((getId_default() == null) ? "" : String.valueOf(getId_default().getORMID())) + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getORMID())));
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
