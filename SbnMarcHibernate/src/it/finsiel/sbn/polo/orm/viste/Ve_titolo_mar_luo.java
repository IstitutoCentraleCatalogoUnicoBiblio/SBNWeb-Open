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
public class Ve_titolo_mar_luo extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -3174496187519399896L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_titolo_mar_luo))
			return false;
		Ve_titolo_mar_luo ve_titolo_mar_luo = (Ve_titolo_mar_luo)aObj;
		if (getMID() != null && !getMID().equals(ve_titolo_mar_luo.getMID()))
			return false;
		if (getBID() != null && !getBID().equals(ve_titolo_mar_luo.getBID()))
			return false;
		if (getLID() != null && !getLID().equals(ve_titolo_mar_luo.getLID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getMID().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getLID().hashCode();
		return hashcode;
	}

	private String MID;

	private String NOTA_TIT_MAR;

	private String LID;

	private String KY_NORM_LUOGO;

	public void setMID(String value) {
		this.MID = value;
    this.settaParametro(KeyParameter.XXXmid,value);
	}

	public String getMID() {
		return MID;
	}

	public void setNOTA_TIT_MAR(String value) {
		this.NOTA_TIT_MAR = value;
    this.settaParametro(KeyParameter.XXXnota_tit_mar,value);
	}

	public String getNOTA_TIT_MAR() {
		return NOTA_TIT_MAR;
	}

	public void setLID(String value) {
		this.LID = value;
    this.settaParametro(KeyParameter.XXXlid,value);
	}

	public String getLID() {
		return LID;
	}

	public void setKY_NORM_LUOGO(String value) {
		this.KY_NORM_LUOGO = value;
    this.settaParametro(KeyParameter.XXXky_norm_luogo,value);
	}

 public String getKY_NORM_LUOGO() {
		return KY_NORM_LUOGO;
 }


 public String toString() {
		return String.valueOf(getMID() + " " + getBID() + " " + getLID());
	}

}
