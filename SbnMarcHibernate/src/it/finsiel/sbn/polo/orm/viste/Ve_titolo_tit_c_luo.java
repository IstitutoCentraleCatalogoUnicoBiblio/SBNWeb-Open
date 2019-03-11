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
public class Ve_titolo_tit_c_luo extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -1543273998359714276L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_titolo_tit_c_luo))
			return false;
		Ve_titolo_tit_c_luo ve_titolo_tit_c_luo = (Ve_titolo_tit_c_luo)aObj;
		if (getBID_COLL() != null && !getBID_COLL().equals(ve_titolo_tit_c_luo.getBID_COLL()))
			return false;
		if (getTP_LEGAME() != null && !getTP_LEGAME().equals(ve_titolo_tit_c_luo.getTP_LEGAME()))
			return false;
		if (getBID() != null && !getBID().equals(ve_titolo_tit_c_luo.getBID()))
			return false;
		if (getLID() != null && !getLID().equals(ve_titolo_tit_c_luo.getLID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID_COLL().hashCode();
		hashcode = hashcode + getTP_LEGAME().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getLID().hashCode();
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

	private String LID;

	private String KY_NORM_LUOGO;

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
		return String.valueOf(getBID_COLL() + " " + getTP_LEGAME() + " " + getBID() + " " + getLID());
	}

}
