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
public class Tr_aut_mar extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 3406602364139003875L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_aut_mar))
			return false;
		Tr_aut_mar tr_aut_mar = (Tr_aut_mar)aObj;
		if (getVID() == null)
			return false;
		if (!getVID().equals(tr_aut_mar.getVID()))
			return false;
		if (getMID() == null)
			return false;
		if (!getMID().equals(tr_aut_mar.getMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getVID() != null) {
			hashcode = hashcode + getVID().hashCode();
		}
		if (getMID() != null) {
			hashcode = hashcode + getMID().hashCode();
		}
		return hashcode;
	}

	private String VID;

	private String MID;

	private String NOTA_AUT_MAR;

	public void setNOTA_AUT_MAR(String value) {
		this.NOTA_AUT_MAR = value;
    this.settaParametro(KeyParameter.XXXnota_aut_mar,value);
	}

	public String getNOTA_AUT_MAR() {
		return NOTA_AUT_MAR;
	}

	public void setVID(String value) {
		this.VID = value;
    this.settaParametro(KeyParameter.XXXvid,value);
	}

	public String getVID() {
		return VID;
	}

	public void setMID(String value) {
		this.MID = value;
    this.settaParametro(KeyParameter.XXXmid,value);
	}


 public String getMID() {
		return MID;
 }



	public String toString() {
		return String.valueOf(((getVID() == null) ? "" : String.valueOf(getVID())) + " " + ((getMID() == null) ? "" : String.valueOf(getMID())));
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
