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
import it.finsiel.sbn.polo.orm.Tb_soggetto;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_soggetto_des extends Tb_soggetto implements Serializable  {

	private static final long serialVersionUID = 7396972476287259687L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_soggetto_des))
			return false;
		Vl_soggetto_des vl_soggetto_des = (Vl_soggetto_des)aObj;
		if (getDID() != null && !getDID().equals(vl_soggetto_des.getDID()))
			return false;
		if (getCID() != null && !getCID().equals(vl_soggetto_des.getCID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getDID().hashCode();
		hashcode = hashcode + getCID().hashCode();
		return hashcode;
	}

	private String DID;

	private String FL_PRIMAVOCE;

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

	public void setDID(String value) {
		this.DID = value;
    this.settaParametro(KeyParameter.XXXdid,value);
	}

	public String getDID() {
		return DID;
	}

	public void setFL_PRIMAVOCE(String value) {
		this.FL_PRIMAVOCE = value;
    this.settaParametro(KeyParameter.XXXfl_primavoce,value);
	}

 public String getFL_PRIMAVOCE() {
		return FL_PRIMAVOCE;
 }



	public String toString() {
		return String.valueOf(getDID() + " " + getCID());
	}
}
