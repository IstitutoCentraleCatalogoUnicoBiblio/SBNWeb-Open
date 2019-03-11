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
import it.finsiel.sbn.polo.orm.Tb_classe;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_classe_tit extends Tb_classe implements Serializable  {

	private static final long serialVersionUID = -4566540825794709444L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_classe_tit))
			return false;
		Vl_classe_tit vl_classe_tit = (Vl_classe_tit)aObj;
		if (getCD_SISTEMA() != vl_classe_tit.getCD_SISTEMA())
			return false;
		if (getCD_EDIZIONE() != vl_classe_tit.getCD_EDIZIONE())
			return false;
		if (getCLASSE() != null && !getCLASSE().equals(vl_classe_tit.getCLASSE()))
			return false;
		if (getBID() != null && !getBID().equals(vl_classe_tit.getBID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCD_SISTEMA().hashCode();
		hashcode = hashcode + getCD_EDIZIONE().hashCode();
		hashcode = hashcode + getCLASSE().hashCode();
		hashcode = hashcode + getBID().hashCode();
		return hashcode;
	}

	private String BID;

	private String FL_CONDIVISO;

	private String UTE_CONDIVISO;

	private java.util.Date TS_CONDIVISO;

	private String NOTA_TIT_CLA;

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
    this.settaParametro(KeyParameter.XXXbid, value);
	}

	public String getBID() {
		return BID;
	}

	public String getNOTA_TIT_CLA() {
		return NOTA_TIT_CLA;
	}

	public void setNOTA_TIT_CLA(String value) {
		this.NOTA_TIT_CLA = value;
		this.settaParametro(KeyParameter.XXXnota_tit_cla, value);
	}

	public String toString() {
		return String.valueOf(getCD_SISTEMA() + " " + getCD_EDIZIONE() + " " + getCLASSE() + " " + getBID());
	}

}
