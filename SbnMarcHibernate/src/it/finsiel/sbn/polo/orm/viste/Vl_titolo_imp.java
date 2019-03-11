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
public class Vl_titolo_imp extends Tb_titolo implements Serializable  {

	private static final long serialVersionUID = -2430297871653695070L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_titolo_imp))
			return false;
		Vl_titolo_imp vl_titolo_imp = (Vl_titolo_imp)aObj;
		if (getBID() != null && !getBID().equals(vl_titolo_imp.getBID()))
			return false;
		if (getIMPRONTA_1() != null && !getIMPRONTA_1().equals(vl_titolo_imp.getIMPRONTA_1()))
			return false;
		if (getIMPRONTA_2() != null && !getIMPRONTA_2().equals(vl_titolo_imp.getIMPRONTA_2()))
			return false;
		if (getIMPRONTA_3() != null && !getIMPRONTA_3().equals(vl_titolo_imp.getIMPRONTA_3()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getIMPRONTA_1().hashCode();
		hashcode = hashcode + getIMPRONTA_2().hashCode();
		hashcode = hashcode + getIMPRONTA_3().hashCode();
		return hashcode;
	}

	private String IMPRONTA_1;

	private String IMPRONTA_2;

	private String IMPRONTA_3;

	private String NOTA_IMPRONTA;

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

	public void setIMPRONTA_1(String value) {
		this.IMPRONTA_1 = value;
    this.settaParametro(KeyParameter.XXXimpronta_1,value);
	}

	public String getIMPRONTA_1() {
		return IMPRONTA_1;
	}

	public void setIMPRONTA_2(String value) {
		this.IMPRONTA_2 = value;
    this.settaParametro(KeyParameter.XXXimpronta_2,value);
	}

	public String getIMPRONTA_2() {
		return IMPRONTA_2;
	}

	public void setIMPRONTA_3(String value) {
		this.IMPRONTA_3 = value;
    this.settaParametro(KeyParameter.XXXimpronta_3,value);
	}

	public String getIMPRONTA_3() {
		return IMPRONTA_3;
	}

	public void setNOTA_IMPRONTA(String value) {
		this.NOTA_IMPRONTA = value;
    this.settaParametro(KeyParameter.XXXnota_impronta,value);
	}

 public String getNOTA_IMPRONTA() {
		return NOTA_IMPRONTA;
	}

	public String toString() {
		return String.valueOf(getBID() + " " + getIMPRONTA_1() + " " + getIMPRONTA_2() + " " + getIMPRONTA_3());
	}

}
