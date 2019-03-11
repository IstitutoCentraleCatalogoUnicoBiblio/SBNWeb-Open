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
public class Tr_aut_aut extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -1798841506323779313L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_aut_aut))
			return false;
		Tr_aut_aut tr_aut_aut = (Tr_aut_aut)aObj;
		if (getVID_BASE() == null)
			return false;
		if (!getVID_BASE().equals(tr_aut_aut.getVID_BASE()))
			return false;
		if (getVID_COLL() == null)
			return false;
		if (!getVID_COLL().equals(tr_aut_aut.getVID_COLL()))
			return false;
		if (getTP_LEGAME() != tr_aut_aut.getTP_LEGAME())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getVID_BASE().hashCode();
		hashcode = hashcode + getVID_COLL().hashCode();
		hashcode = hashcode + getTP_LEGAME().hashCode();
		return hashcode;
	}

	private String VID_BASE;

	private String VID_COLL;

	private String TP_LEGAME;

	private String NOTA_AUT_AUT;

	public void setTP_LEGAME(String value) {
		this.TP_LEGAME = value;
    this.settaParametro(KeyParameter.XXXtp_legame,value);
	}

	public String getTP_LEGAME() {
		return TP_LEGAME;
	}

	public void setNOTA_AUT_AUT(String value) {
		this.NOTA_AUT_AUT = value;
    this.settaParametro(KeyParameter.XXXnota_aut_aut,value);
	}

	public String getNOTA_AUT_AUT() {
		return NOTA_AUT_AUT;
	}

	public void setVID_BASE(String value) {
		this.VID_BASE = value;
    this.settaParametro(KeyParameter.XXXvid_base,value);
	}

	public String getVID_BASE() {
		return VID_BASE;
	}

	public void setVID_COLL(String value) {
		this.VID_COLL = value;
    this.settaParametro(KeyParameter.XXXvid_coll,value);
	}

	public String getVID_COLL() {
		return VID_COLL;
	}

	public String toString() {
		return String.valueOf(((getVID_BASE() == null) ? "" : String.valueOf(getVID_BASE())) + " " + ((getVID_COLL() == null) ? "" : String.valueOf(getVID_COLL())) + " " + getTP_LEGAME());
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
