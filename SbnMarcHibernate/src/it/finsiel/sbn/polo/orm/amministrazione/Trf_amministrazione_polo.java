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
public class Trf_amministrazione_polo implements Serializable {

	private static final long serialVersionUID = -5847647803845211477L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Trf_amministrazione_polo))
			return false;
		Trf_amministrazione_polo trf_amministrazione_polo = (Trf_amministrazione_polo)aObj;
		if (getId_amministratore() == null)
			return false;
		if (getId_amministratore().getORMID() != trf_amministrazione_polo.getId_amministratore().getORMID())
			return false;
		if (getCd_polo() == null)
			return false;
		if (!getCd_polo().getORMID().equals(trf_amministrazione_polo.getCd_polo().getORMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_amministratore() != null) {
			hashcode = hashcode + getId_amministratore().getORMID();
		}
		if (getCd_polo() != null) {
			hashcode = hashcode + getCd_polo().getORMID().hashCode();
		}
		return hashcode;
	}

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_amministrazione id_amministratore;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo cd_polo;

	private Character fl_canc;

	public void setFl_canc(char value) {
		setFl_canc(new Character(value));
	}

	public void setFl_canc(Character value) {
		this.fl_canc = value;
	}

	public Character getFl_canc() {
		return fl_canc;
	}

	public void setId_amministratore(it.finsiel.sbn.polo.orm.amministrazione.Tbf_amministrazione value) {
		this.id_amministratore = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_amministrazione getId_amministratore() {
		return id_amministratore;
	}

	public void setCd_polo(it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo value) {
		this.cd_polo = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo getCd_polo() {
		return cd_polo;
	}

	public String toString() {
		return String.valueOf(((getId_amministratore() == null) ? "" : String.valueOf(getId_amministratore().getORMID())) + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getORMID())));
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
