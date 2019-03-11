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
public class Ve_titolo_the_luo extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = 7587441145626482798L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_titolo_the_luo))
			return false;
		Ve_titolo_the_luo ve_titolo_the_luo = (Ve_titolo_the_luo)aObj;
		if (getDID() != null && !getDID().equals(ve_titolo_the_luo.getDID()))
			return false;
		if (getBID() != null && !getBID().equals(ve_titolo_the_luo.getBID()))
			return false;
		if (getLID() != null && !getLID().equals(ve_titolo_the_luo.getLID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getDID().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getLID().hashCode();
		return hashcode;
	}

	private String DID;

	private String LID;

	private String KY_NORM_LUOGO;

	public void setDID(String value) {
		this.DID = value;
    this.settaParametro(KeyParameter.XXXdid,value);
	}

	public String getDID() {
		return DID;
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
		return String.valueOf(getDID() + " " + getBID() + " " + getLID());
	}

}
