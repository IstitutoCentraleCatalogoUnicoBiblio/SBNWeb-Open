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
public class Tr_per_int extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 7766485999629164212L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_per_int))
			return false;
		Tr_per_int tr_per_int = (Tr_per_int)aObj;
		if (getVID() == null)
			return false;
		if (!getVID().equals(tr_per_int.getVID()))
			return false;
		if (getID_PERSONAGGIO() != tr_per_int.getID_PERSONAGGIO())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getVID() != null) {
			hashcode = hashcode + getVID().hashCode();
		}
		hashcode = hashcode + (int) getID_PERSONAGGIO();
		return hashcode;
	}

	private String VID;

	private long ID_PERSONAGGIO;

	public void setVID(String value) {
		this.VID = value;
    this.settaParametro(KeyParameter.XXXvid,value);
	}

	public String getVID() {
		return VID;
	}

	public void setID_PERSONAGGIO(long value) {
		this.ID_PERSONAGGIO = value;
    this.settaParametro(KeyParameter.XXXid_personaggio,value);
	}

 public long getID_PERSONAGGIO() {
		return ID_PERSONAGGIO;
	}

	public String toString() {
		return String.valueOf(((getVID() == null) ? "" : String.valueOf(getVID())) + " " + String.valueOf(getID_PERSONAGGIO()));
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
