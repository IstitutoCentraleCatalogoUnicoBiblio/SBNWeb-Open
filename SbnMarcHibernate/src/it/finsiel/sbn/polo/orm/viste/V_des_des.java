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
public class V_des_des extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -1982872446464067276L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof V_des_des))
			return false;
		V_des_des v_des_des = (V_des_des)aObj;
		if (getDID_1() != null && !getDID_1().equals(v_des_des.getDID_1()))
			return false;
		if (getDID_2() != null && !getDID_2().equals(v_des_des.getDID_2()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getDID_1().hashCode();
		hashcode = hashcode + getDID_2().hashCode();
		return hashcode;
	}

	private String DID_1;

	private String DID_2;

	private String TP_LEGAME;

	public void setDID_1(String value) {
		this.DID_1 = value;
    this.settaParametro(KeyParameter.XXXdid_1,value);
	}

	public String getDID_1() {
		return DID_1;
	}

	public void setDID_2(String value) {
		this.DID_2 = value;
    this.settaParametro(KeyParameter.XXXdid_2,value);
	}

	public String getDID_2() {
		return DID_2;
	}

	public void setTP_LEGAME(String value) {
		this.TP_LEGAME = value;
    this.settaParametro(KeyParameter.XXXtp_legame,value);
	}


 public String getTP_LEGAME() {
		return TP_LEGAME;
	}

	public String toString() {
		return String.valueOf(getDID_1() + " " + getDID_2());
	}

}
