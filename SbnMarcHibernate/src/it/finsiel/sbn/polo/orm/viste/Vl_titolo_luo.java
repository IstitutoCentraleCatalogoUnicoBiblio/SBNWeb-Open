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
public class Vl_titolo_luo extends Tb_titolo implements Serializable  {

	private static final long serialVersionUID = -1717638779684847273L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_titolo_luo))
			return false;
		Vl_titolo_luo vl_titolo_luo = (Vl_titolo_luo)aObj;
		if (getLID() != null && !getLID().equals(vl_titolo_luo.getLID()))
			return false;
		if (getTP_LUOGO() != vl_titolo_luo.getTP_LUOGO())
			return false;
		if (getBID() != null && !getBID().equals(vl_titolo_luo.getBID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getLID().hashCode();
		hashcode = hashcode + getTP_LUOGO().hashCode();
		hashcode = hashcode + getBID().hashCode();
		return hashcode;
	}

	private String LID;

	private String TP_LUOGO;

	private String NOTA_TIT_LUO;

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

	public void setLID(String value) {
		this.LID = value;
    this.settaParametro(KeyParameter.XXXlid,value);
	}

	public String getLID() {
		return LID;
	}

	public void setTP_LUOGO(String value) {
		this.TP_LUOGO = value;
    this.settaParametro(KeyParameter.XXXtp_luogo,value);
	}

	public String getTP_LUOGO() {
		return TP_LUOGO;
	}

	public void setNOTA_TIT_LUO(String value) {
		this.NOTA_TIT_LUO = value;
    this.settaParametro(KeyParameter.XXXnota_tit_luo,value);
	}


 public String getNOTA_TIT_LUO() {
		return NOTA_TIT_LUO;
	}

	public String toString() {
		return String.valueOf(getLID() + " " + getTP_LUOGO() + " " + getBID());
	}

}
