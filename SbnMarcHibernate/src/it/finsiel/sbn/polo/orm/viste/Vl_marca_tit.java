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
import it.finsiel.sbn.polo.orm.Tb_marca;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_marca_tit extends Tb_marca implements Serializable {

	private static final long serialVersionUID = -1802325008327312753L;


	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_marca_tit))
			return false;
		Vl_marca_tit vl_marca_tit = (Vl_marca_tit)aObj;
		if (getBID() != null && !getBID().equals(vl_marca_tit.getBID()))
			return false;
		if (getMID() != null && !getMID().equals(vl_marca_tit.getMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getMID().hashCode();
		return hashcode;
	}

	private String BID;

	private String NOTA_TIT_MAR;

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

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
	}

	public void setNOTA_TIT_MAR(String value) {
		this.NOTA_TIT_MAR = value;
    this.settaParametro(KeyParameter.XXXnota_tit_mar,value);
	}

 public String getNOTA_TIT_MAR() {
		return NOTA_TIT_MAR;
 }



	public String toString() {
		return String.valueOf(getBID() + " " + getMID());
	}
}
