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
public class Ve_titolo_tit_c_mar extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = 4811059719358022665L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_titolo_tit_c_mar))
			return false;
		Ve_titolo_tit_c_mar ve_titolo_tit_c_mar = (Ve_titolo_tit_c_mar)aObj;
		if (getBID_COLL() != null && !getBID_COLL().equals(ve_titolo_tit_c_mar.getBID_COLL()))
			return false;
		if (getTP_LEGAME() != null && !getTP_LEGAME().equals(ve_titolo_tit_c_mar.getTP_LEGAME()))
			return false;
		if (getBID() != null && !getBID().equals(ve_titolo_tit_c_mar.getBID()))
			return false;
		if (getMID() != null && !getMID().equals(ve_titolo_tit_c_mar.getMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID_COLL().hashCode();
		hashcode = hashcode + getTP_LEGAME().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getMID().hashCode();
		return hashcode;
	}

	private String BID_COLL;

	private String TP_LEGAME;

	private String TP_LEGAME_MUSICA;

	private String CD_NATURA_BASE;

	private String CD_NATURA_COLL;

	private String SEQUENZA;

	private String NOTA_TIT_TIT;

	private String SEQUENZA_MUSICA;

	private String SICI;

	private String MID;

	private String DS_MARCA;

	public void setBID_COLL(String value) {
		this.BID_COLL = value;
    this.settaParametro(KeyParameter.XXXbid_coll,value);
	}

	public String getBID_COLL() {
		return BID_COLL;
	}

	public void setTP_LEGAME(String value) {
		this.TP_LEGAME = value;
    this.settaParametro(KeyParameter.XXXtp_legame,value);
	}

	public String getTP_LEGAME() {
		return TP_LEGAME;
	}

	public void setTP_LEGAME_MUSICA(String value) {
		this.TP_LEGAME_MUSICA = value;
    this.settaParametro(KeyParameter.XXXtp_legame_musica,value);
	}

	public String getTP_LEGAME_MUSICA() {
		return TP_LEGAME_MUSICA;
	}

	public void setCD_NATURA_BASE(String value) {
		this.CD_NATURA_BASE = value;
    this.settaParametro(KeyParameter.XXXcd_natura_base,value);
	}

	public String getCD_NATURA_BASE() {
		return CD_NATURA_BASE;
	}

	public void setCD_NATURA_COLL(String value) {
		this.CD_NATURA_COLL = value;
    this.settaParametro(KeyParameter.XXXcd_natura_coll,value);
	}

	public String getCD_NATURA_COLL() {
		return CD_NATURA_COLL;
	}

	public void setSEQUENZA(String value) {
		this.SEQUENZA = value;
    this.settaParametro(KeyParameter.XXXsequenza,value);
	}

	public String getSEQUENZA() {
		return SEQUENZA;
	}

	public void setNOTA_TIT_TIT(String value) {
		this.NOTA_TIT_TIT = value;
    this.settaParametro(KeyParameter.XXXnota_tit_tit,value);
	}

	public String getNOTA_TIT_TIT() {
		return NOTA_TIT_TIT;
	}

	public void setSEQUENZA_MUSICA(String value) {
		this.SEQUENZA_MUSICA = value;
    this.settaParametro(KeyParameter.XXXsequenza_musica,value);
	}

	public String getSEQUENZA_MUSICA() {
		return SEQUENZA_MUSICA;
	}

	public void setSICI(String value) {
		this.SICI = value;
    this.settaParametro(KeyParameter.XXXsici,value);
	}

	public String getSICI() {
		return SICI;
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
		return String.valueOf(getBID_COLL() + " " + getTP_LEGAME() + " " + getBID() + " " + getMID());
	}

}
