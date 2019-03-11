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
public class Vl_luogo_luo extends Tb_luogo implements Serializable  {

	private static final long serialVersionUID = 1996438717608868205L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_luogo_luo))
			return false;
		Vl_luogo_luo vl_luogo_luo = (Vl_luogo_luo)aObj;
		if (getLID_1() != null && !getLID_1().equals(vl_luogo_luo.getLID_1()))
			return false;
		if (getLID() != null && !getLID().equals(vl_luogo_luo.getLID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getLID_1().hashCode();
		hashcode = hashcode + getLID().hashCode();
		return hashcode;
	}

	private String LID_1;

	private String TP_LEGAME;

	public void setLID_1(String value) {
		this.LID_1 = value;
    this.settaParametro(KeyParameter.XXXlid_1,value);
	}

	public String getLID_1() {
		return LID_1;
	}

	public void setTP_LEGAME(String value) {
		this.TP_LEGAME = value;
    this.settaParametro(KeyParameter.XXXtp_legame,value);
	}

 public String getTP_LEGAME() {
		return TP_LEGAME;
 }



	public String toString() {
		return String.valueOf(getLID_1() + " " + getLID());
	}

}
