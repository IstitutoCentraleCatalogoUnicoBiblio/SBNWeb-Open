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
import it.finsiel.sbn.polo.orm.Tb_autore;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_autore_aut extends Tb_autore implements Serializable {

	private static final long serialVersionUID = 3155085821854956785L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_autore_aut))
			return false;
		Vl_autore_aut vl_autore_aut = (Vl_autore_aut)aObj;
		if (getVID_1() != null && !getVID_1().equals(vl_autore_aut.getVID_1()))
			return false;
		if (getTP_LEGAME() != vl_autore_aut.getTP_LEGAME())
			return false;
		if (getVID() != null && !getVID().equals(vl_autore_aut.getVID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getVID_1().hashCode();
		hashcode = hashcode + getTP_LEGAME().hashCode();
		hashcode = hashcode + getVID().hashCode();
		return hashcode;
	}

	private String VID_1;

	private String TP_LEGAME;

	private String NOTA_AUT_AUT;

	private String FL_CONDIVISO;

	private String UTE_CONDIVISO;

	private java.util.Date TS_CONDIVISO;

	public String getFL_CONDIVISO() {
		return FL_CONDIVISO;
	}

	public java.util.Date getTS_CONDIVISO() {
		return TS_CONDIVISO;
	}

	public String getUTE_CONDIVISO() {
		return UTE_CONDIVISO;
	}

	public void setFL_CONDIVISO(String value) {
		this.FL_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXfl_condiviso, value);
	}

	public void setTS_CONDIVISO(java.util.Date value) {
		this.TS_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXts_condiviso, value);
	}

	public void setUTE_CONDIVISO(String value) {
		this.UTE_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXute_condiviso, value);
	}

	public void setVID_1(String value) {
		this.VID_1 = value;
    this.settaParametro(KeyParameter.XXXvid_1,value);
	}

	public String getVID_1() {
		return VID_1;
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
		return String.valueOf(getVID_1() + " " + getTP_LEGAME() + " " + getVID());
	}

}
