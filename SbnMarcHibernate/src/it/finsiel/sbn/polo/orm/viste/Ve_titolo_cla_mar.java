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
public class Ve_titolo_cla_mar extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -2176598959657141523L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_titolo_cla_mar))
			return false;
		Ve_titolo_cla_mar ve_titolo_cla_mar = (Ve_titolo_cla_mar)aObj;
		if (getCD_SISTEMA() != ve_titolo_cla_mar.getCD_SISTEMA())
			return false;
		if (getCD_EDIZIONE() != ve_titolo_cla_mar.getCD_EDIZIONE())
			return false;
		if (getCLASSE() != null && !getCLASSE().equals(ve_titolo_cla_mar.getCLASSE()))
			return false;
		if (getBID() != null && !getBID().equals(ve_titolo_cla_mar.getBID()))
			return false;
		if (getMID() != null && !getMID().equals(ve_titolo_cla_mar.getMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCD_SISTEMA().hashCode();
		hashcode = hashcode + getCD_EDIZIONE().hashCode();
		hashcode = hashcode + getCLASSE().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getMID().hashCode();
		return hashcode;
	}

	private String CD_SISTEMA;

	private String CD_EDIZIONE;

	private String CLASSE;

	private String MID;

	private String DS_MARCA;

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
		return String.valueOf(getCD_SISTEMA() + " " + getCD_EDIZIONE() + " " + getCLASSE() + " " + getBID() + " " + getMID());
	}

}
