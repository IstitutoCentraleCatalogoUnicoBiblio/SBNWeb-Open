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
import it.finsiel.sbn.polo.orm.Tb_descrittore;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_descrittore_des extends Tb_descrittore implements Serializable{

	private static final long serialVersionUID = 8528530337017124192L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_descrittore_des))
			return false;
		Vl_descrittore_des vl_descrittore_des = (Vl_descrittore_des)aObj;
		if (getDID_1() != null && !getDID_1().equals(vl_descrittore_des.getDID_1()))
			return false;
		if (getDID() != null && !getDID().equals(vl_descrittore_des.getDID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getDID_1().hashCode();
		hashcode = hashcode + getDID().hashCode();
		return hashcode;
	}

	private String DID_1;

	private String TP_LEGAME;

	private String FL_CONDIVISO;

	public String getFL_CONDIVISO() {
		return FL_CONDIVISO;
	}

	public void setFL_CONDIVISO(String value) {
		this.FL_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXfl_condiviso, value);
	}

	public void setDID_1(String value) {
		this.DID_1 = value;
    this.settaParametro(KeyParameter.XXXdid_1,value);
	}

	public String getDID_1() {
		return DID_1;
	}

	public void setTP_LEGAME(String value) {
		this.TP_LEGAME = value;
    this.settaParametro(KeyParameter.XXXtp_legame,value);
	}

 public String getTP_LEGAME() {
		return TP_LEGAME;
 }


 public String toString() {
		return String.valueOf(getDID_1() + " " + getDID());
 }

}

