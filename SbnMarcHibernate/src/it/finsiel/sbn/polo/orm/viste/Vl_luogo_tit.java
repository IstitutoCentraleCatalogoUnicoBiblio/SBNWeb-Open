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
import it.finsiel.sbn.polo.orm.Tb_luogo;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_luogo_tit extends Tb_luogo implements Serializable  {

	private static final long serialVersionUID = 7410185568248405885L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_luogo_tit))
			return false;
		Vl_luogo_tit vl_luogo_tit = (Vl_luogo_tit)aObj;
		if (getBID() != null && !getBID().equals(vl_luogo_tit.getBID()))
			return false;
		if (getTP_LUOGO() != vl_luogo_tit.getTP_LUOGO())
			return false;
		if (getLID() != null && !getLID().equals(vl_luogo_tit.getLID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getTP_LUOGO().hashCode();
		hashcode = hashcode + getLID().hashCode();
		return hashcode;
	}

	private String BID;

	private String TP_LUOGO;

	private String NOTA_TIT_LUO;

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
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
		return String.valueOf(getBID() + " " + getTP_LUOGO() + " " + getLID());
	}
}
