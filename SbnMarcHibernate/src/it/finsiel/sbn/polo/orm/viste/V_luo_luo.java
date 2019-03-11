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
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class V_luo_luo extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 8553069386389968328L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof V_luo_luo))
			return false;
		V_luo_luo v_luo_luo = (V_luo_luo)aObj;
		if (getLID_1() != null && !getLID_1().equals(v_luo_luo.getLID_1()))
			return false;
		if (getLID_2() != null && !getLID_2().equals(v_luo_luo.getLID_2()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getLID_1().hashCode();
		hashcode = hashcode + getLID_2().hashCode();
		return hashcode;
	}

	private String LID_1;

	private String LID_2;

	private String TP_LEGAME;

	public void setLID_1(String value) {
		this.LID_1 = value;
    this.settaParametro(KeyParameter.XXXlid_1,value);
	}

	public String getLID_1() {
		return LID_1;
	}

	public void setLID_2(String value) {
		this.LID_2 = value;
    this.settaParametro(KeyParameter.XXXlid_2,value);
	}

	public String getLID_2() {
		return LID_2;
	}

	public void setTP_LEGAME(String value) {
		this.TP_LEGAME = value;
    this.settaParametro(KeyParameter.XXXtp_legame,value);
	}


 public String getTP_LEGAME() {
		return TP_LEGAME;
	}

	public String toString() {
		return String.valueOf(getLID_1() + " " + getLID_2());
	}
}
