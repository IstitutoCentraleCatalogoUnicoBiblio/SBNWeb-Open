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
package it.finsiel.sbn.polo.orm.viste;

import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.Tb_descrittore;

import java.io.Serializable;

/**
 * ORM-Persistable Class
 */
public class Vl_descrittore_sog extends Tb_descrittore implements Serializable {

	private static final long serialVersionUID = 8179151410732434968L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_descrittore_sog))
			return false;
		Vl_descrittore_sog vl_descrittore_sog = (Vl_descrittore_sog) aObj;
		if (getCID() != null && !getCID().equals(vl_descrittore_sog.getCID()))
			return false;
		if (getDID() != null && !getDID().equals(vl_descrittore_sog.getDID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCID().hashCode();
		hashcode = hashcode + getDID().hashCode();
		return hashcode;
	}

	private String CID;

	private String FL_PRIMAVOCE;

	private String FL_CONDIVISO;

	private int FL_POSIZIONE;

	public String getFL_CONDIVISO() {
		return FL_CONDIVISO;
	}

	public void setFL_CONDIVISO(String value) {
		this.FL_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXfl_condiviso, value);
	}

	public void setCID(String value) {
		this.CID = value;
		this.settaParametro(KeyParameter.XXXcid, value);
	}

	public String getCID() {
		return CID;
	}

	public void setFL_PRIMAVOCE(String value) {
		this.FL_PRIMAVOCE = value;
		this.settaParametro(KeyParameter.XXXfl_primavoce, value);
	}

	public String getFL_PRIMAVOCE() {
		return FL_PRIMAVOCE;
	}

	public void setFL_POSIZIONE(int value) {
		this.FL_POSIZIONE = value;
		this.settaParametro(KeyParameter.XXXfl_posizione, value);
	}

	public String toString() {
		return String.valueOf(getCID() + " " + getDID());
	}

	public int getFL_POSIZIONE() {
		return FL_POSIZIONE;
	}

}
