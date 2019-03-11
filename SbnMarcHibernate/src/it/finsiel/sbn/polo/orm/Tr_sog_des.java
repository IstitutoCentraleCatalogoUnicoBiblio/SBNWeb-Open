/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 *
 * This is an automatic generated file. It will be regenerated every time
 * you generate persistence class.
 *
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: antoniospatera@libero.it
 * License Type: Evaluation
 */
package it.finsiel.sbn.polo.orm;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Tr_sog_des extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 8906264352506201959L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_sog_des))
			return false;
		Tr_sog_des tr_sog_des = (Tr_sog_des)aObj;
		if (getDID() == null)
			return false;
		if (!getDID().equals(tr_sog_des.getDID()))
			return false;
		if (getCID() == null)
			return false;
		if (!getCID().equals(tr_sog_des.getCID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getDID() != null) {
			hashcode = hashcode + getDID().hashCode();
		}
		if (getCID() != null) {
			hashcode = hashcode + getCID().hashCode();
		}
		return hashcode;
	}

	private String DID;

	private String CID;

	private String FL_PRIMAVOCE;

	private int FL_POSIZIONE;

	public void setFL_PRIMAVOCE(String value) {
		this.FL_PRIMAVOCE = value;
    this.settaParametro(KeyParameter.XXXfl_primavoce,value);
	}

	public String getFL_PRIMAVOCE() {
		return FL_PRIMAVOCE;
	}

	public void setDID(String value) {
		this.DID = value;
    this.settaParametro(KeyParameter.XXXdid,value);
	}

	public String getDID() {
		return DID;
	}

	public void setCID(String value) {
		this.CID = value;
    this.settaParametro(KeyParameter.XXXcid,value);
	}


 public String getCID() {
		return CID;
	}

	public String toString() {
		return String.valueOf(((getDID() == null) ? "" : String.valueOf(getDID())) + " " + ((getCID() == null) ? "" : String.valueOf(getCID())));
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

	public int getFL_POSIZIONE() {
		return FL_POSIZIONE;
	}

	public void setFL_POSIZIONE(int value) {
		this.FL_POSIZIONE = value;
		this.settaParametro(KeyParameter.XXXfl_posizione,value);
	}


}
