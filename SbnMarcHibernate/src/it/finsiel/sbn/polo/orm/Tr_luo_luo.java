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
public class Tr_luo_luo extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -6363322231666157631L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_luo_luo))
			return false;
		Tr_luo_luo tr_luo_luo = (Tr_luo_luo)aObj;
		if (getLID_BASE() == null)
			return false;
		if (!getLID_BASE().equals(tr_luo_luo.getLID_BASE()))
			return false;
		if (getLID_COLL() == null)
			return false;
		if (!getLID_COLL().equals(tr_luo_luo.getLID_COLL()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getLID_BASE().hashCode();
		hashcode = hashcode + getLID_COLL().hashCode();
		return hashcode;
	}

	private String LID_BASE;

	private String LID_COLL;

	private String TP_LEGAME;

	public void setTP_LEGAME(String value) {
		this.TP_LEGAME = value;
    this.settaParametro(KeyParameter.XXXtp_legame,value);
	}

	public String getTP_LEGAME() {
		return TP_LEGAME;
	}

	public void setLID_BASE(String value) {
		this.LID_BASE = value;
    this.settaParametro(KeyParameter.XXXlid_base,value);
	}

	public String getLID_BASE() {
		return LID_BASE;
	}

	public void setLID_COLL(String value) {
		this.LID_COLL = value;
    this.settaParametro(KeyParameter.XXXlid_coll,value);
	}

 public String getLID_COLL() {
		return LID_COLL;
 }



	public String toString() {
		return String.valueOf(((getLID_BASE() == null) ? "" : String.valueOf(getLID_BASE())) + " " + ((getLID_COLL() == null) ? "" : String.valueOf(getLID_COLL())));
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
