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
public class Tr_tit_mar extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 2636048631101344466L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_mar))
			return false;
		Tr_tit_mar tr_tit_mar = (Tr_tit_mar)aObj;
		if (getBID() == null)
			return false;
		if (!getBID().equals(tr_tit_mar.getBID()))
			return false;
		if (getMID() == null)
			return false;
		if (!getMID().equals(tr_tit_mar.getMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getBID() != null) {
			hashcode = hashcode + getBID().hashCode();
		}
		if (getMID() != null) {
			hashcode = hashcode + getMID().hashCode();
		}
		return hashcode;
	}

	private String BID;

	private String MID;

	private String NOTA_TIT_MAR;

	public void setNOTA_TIT_MAR(String value) {
		this.NOTA_TIT_MAR = value;
    this.settaParametro(KeyParameter.XXXnota_tit_mar,value);
	}

	public String getNOTA_TIT_MAR() {
		return NOTA_TIT_MAR;
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
	}

	public void setMID(String value) {
		this.MID = value;
    this.settaParametro(KeyParameter.XXXmid,value);
	}


 public String getMID() {
		return MID;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())) + " " + ((getMID() == null) ? "" : String.valueOf(getMID())));
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
