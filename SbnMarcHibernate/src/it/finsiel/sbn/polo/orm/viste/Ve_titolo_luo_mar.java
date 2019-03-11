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
public class Ve_titolo_luo_mar extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = 657404255737749942L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_titolo_luo_mar))
			return false;
		Ve_titolo_luo_mar ve_titolo_luo_mar = (Ve_titolo_luo_mar)aObj;
		if (getLID() != null && !getLID().equals(ve_titolo_luo_mar.getLID()))
			return false;
		if (getTP_LUOGO() != ve_titolo_luo_mar.getTP_LUOGO())
			return false;
		if (getBID() != null && !getBID().equals(ve_titolo_luo_mar.getBID()))
			return false;
		if (getMID() != null && !getMID().equals(ve_titolo_luo_mar.getMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getLID().hashCode();
		hashcode = hashcode + getTP_LUOGO().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getMID().hashCode();
		return hashcode;
	}

	private String LID;

	private String TP_LUOGO;

	private String NOTA_TIT_LUO;

	private String MID;

	private String DS_MARCA;

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
		return String.valueOf(getLID() + " " + getTP_LUOGO() + " " + getBID() + " " + getMID());
	}

}
