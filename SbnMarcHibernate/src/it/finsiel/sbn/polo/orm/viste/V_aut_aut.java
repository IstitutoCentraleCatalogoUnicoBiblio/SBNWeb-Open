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
public class V_aut_aut extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 6634580406749840327L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof V_aut_aut))
			return false;
		V_aut_aut v_aut_aut = (V_aut_aut)aObj;
		if (getVID_1() != null && !getVID_1().equals(v_aut_aut.getVID_1()))
			return false;
		if (getVID_2() != null && !getVID_2().equals(v_aut_aut.getVID_2()))
			return false;
		if (getTP_LEGAME() != v_aut_aut.getTP_LEGAME())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getVID_1().hashCode();
		hashcode = hashcode + getVID_2().hashCode();
		hashcode = hashcode + getTP_LEGAME().hashCode();
		return hashcode;
	}

	private String VID_1;

	private String VID_2;

	private String TP_LEGAME;

	private String NOTA_AUT_AUT;

	public void setVID_1(String value) {
		this.VID_1 = value;
    this.settaParametro(KeyParameter.XXXvid_1,value);
	}

	public String getVID_1() {
		return VID_1;
	}

	public void setVID_2(String value) {
		this.VID_2 = value;
    this.settaParametro(KeyParameter.XXXvid_2,value);
	}

	public String getVID_2() {
		return VID_2;
	}

	public void setTP_LEGAME(String value) {
		this.TP_LEGAME = value;
    this.settaParametro(KeyParameter.XXXtp_legame,value);
	}

	public String getTP_LEGAME() {
		return TP_LEGAME;
	}

	public void setNOTA_AUT_AUT(String value) {
		this.NOTA_AUT_AUT = value;
    this.settaParametro(KeyParameter.XXXnota_aut_aut,value);
	}


 public String getNOTA_AUT_AUT() {
		return NOTA_AUT_AUT;
	}

	public String toString() {
		return String.valueOf(getVID_1() + " " + getVID_2() + " " + getTP_LEGAME());
	}

}
