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
public class Vl_titolo_cla extends Tb_titolo implements Serializable  {

	private static final long serialVersionUID = 8389558602297605785L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_titolo_cla))
			return false;
		Vl_titolo_cla vl_titolo_cla = (Vl_titolo_cla)aObj;
		if (getCD_SISTEMA() != vl_titolo_cla.getCD_SISTEMA())
			return false;
		if (getCD_EDIZIONE() != vl_titolo_cla.getCD_EDIZIONE())
			return false;
		if (getCLASSE() != null && !getCLASSE().equals(vl_titolo_cla.getCLASSE()))
			return false;
		if (getBID() != null && !getBID().equals(vl_titolo_cla.getBID()))
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

	private String CD_SISTEMA;

	private String CD_EDIZIONE;

	private String CLASSE;

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

	public void setCD_SISTEMA(String value) {
		this.CD_SISTEMA = value;
    this.settaParametro(KeyParameter.XXXcd_sistema,value);
	}

	public String getCD_SISTEMA() {
		return CD_SISTEMA;
	}

	public void setCD_EDIZIONE(String value) {
		this.CD_EDIZIONE = value;
    this.settaParametro(KeyParameter.XXXcd_edizione,value);
	}

	public String getCD_EDIZIONE() {
		return CD_EDIZIONE;
	}

	public void setCLASSE(String value) {
		this.CLASSE = value;
    this.settaParametro(KeyParameter.XXXclasse,value);
	}

 public String getCLASSE() {
		return CLASSE;
 }


	public String toString() {
		return String.valueOf(getCD_SISTEMA() + " " + getCD_EDIZIONE() + " " + getCLASSE() + " " + getBID());
	}
}
