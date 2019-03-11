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
import it.finsiel.sbn.polo.orm.Tb_autore;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_autore_mar extends Tb_autore implements Serializable  {

	private static final long serialVersionUID = 1857658651129941567L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_autore_mar))
			return false;
		Vl_autore_mar vl_autore_mar = (Vl_autore_mar)aObj;
		if (getMID() != null && !getMID().equals(vl_autore_mar.getMID()))
			return false;
		if (getVID() != null && !getVID().equals(vl_autore_mar.getVID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getMID().hashCode();
		hashcode = hashcode + getVID().hashCode();
		return hashcode;
	}

	private String MID;

	private String NOTA_AUT_MAR;

	private String FL_CONDIVISO;

	private String UTE_CONDIVISO;

	private java.util.Date TS_CONDIVISO;

	public String getFL_CONDIVISO() {
		return FL_CONDIVISO;
	}

	public java.util.Date getTS_CONDIVISO() {
		return TS_CONDIVISO;
	}

	public String getUTE_CONDIVISO() {
		return UTE_CONDIVISO;
	}

	public void setFL_CONDIVISO(String value) {
		this.FL_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXfl_condiviso, value);
	}

	public void setTS_CONDIVISO(java.util.Date value) {
		this.TS_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXts_condiviso, value);
	}

	public void setUTE_CONDIVISO(String value) {
		this.UTE_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXute_condiviso, value);
	}

	public void setMID(String value) {
		this.MID = value;
    this.settaParametro(KeyParameter.XXXmid,value);
	}

	public String getMID() {
		return MID;
	}

	public void setNOTA_AUT_MAR(String value) {
		this.NOTA_AUT_MAR = value;
    this.settaParametro(KeyParameter.XXXnota_aut_mar,value);
	}

 public String getNOTA_AUT_MAR() {
		return NOTA_AUT_MAR;
 }



	public String toString() {
		return String.valueOf(getMID() + " " + getVID());
	}

}
