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
import it.finsiel.sbn.polo.orm.Tb_titolo;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Ve_titolo_sog_mar extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -5730054774827405705L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_titolo_sog_mar))
			return false;
		Ve_titolo_sog_mar ve_titolo_sog_mar = (Ve_titolo_sog_mar)aObj;
		if (getCID() != null && !getCID().equals(ve_titolo_sog_mar.getCID()))
			return false;
		if (getBID() != null && !getBID().equals(ve_titolo_sog_mar.getBID()))
			return false;
		if (getMID() != null && !getMID().equals(ve_titolo_sog_mar.getMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCID().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getMID().hashCode();
		return hashcode;
	}

	private String CID;

	private String MID;

	private String DS_MARCA;

	public void setCID(String value) {
		this.CID = value;
    this.settaParametro(KeyParameter.XXXcid,value);
	}

	public String getCID() {
		return CID;
	}

	public void setMID(String value) {
		this.MID = value;
    this.settaParametro(KeyParameter.XXXmid,value);
	}

	public String getMID() {
		return MID;
	}

	public void setDS_MARCA(String value) {
		this.DS_MARCA = value;
    this.settaParametro(KeyParameter.XXXds_marca,value);
	}

 public String getDS_MARCA() {
		return DS_MARCA;
 }

	public String toString() {
		return String.valueOf(getCID() + " " + getBID() + " " + getMID());
	}

}
