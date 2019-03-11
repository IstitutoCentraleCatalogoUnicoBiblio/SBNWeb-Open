/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 *
 * This is an automatic generated file. It will be regenerated every time
 * you generate persistence class.
 *
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: antoniospatera@libero.it
 * License Type: Evaluation
 */
package it.finsiel.sbn.polo.orm;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Tb_impronta extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -1033376981927767200L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_impronta))
			return false;
		Tb_impronta tb_impronta = (Tb_impronta)aObj;
		if (getBID() == null)
			return false;
		if (!getBID().equals(tb_impronta.getBID()))
			return false;
		if (getIMPRONTA_1() != null && !getIMPRONTA_1().equals(tb_impronta.getIMPRONTA_1()))
			return false;
		if (getIMPRONTA_2() != null && !getIMPRONTA_2().equals(tb_impronta.getIMPRONTA_2()))
			return false;
		if (getIMPRONTA_3() != null && !getIMPRONTA_3().equals(tb_impronta.getIMPRONTA_3()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getBID() != null) {
			hashcode = hashcode + getBID().hashCode();
		}
		hashcode = hashcode + getIMPRONTA_1().hashCode();
		hashcode = hashcode + getIMPRONTA_2().hashCode();
		hashcode = hashcode + getIMPRONTA_3().hashCode();
		return hashcode;
	}

	private String BID;

	private String IMPRONTA_1;

	private String IMPRONTA_2;

	private String IMPRONTA_3;

	private String NOTA_IMPRONTA;

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

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}
	public String getBID() {
		return this.BID;
	}


	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())) + " " + getIMPRONTA_1() + " " + getIMPRONTA_2() + " " + getIMPRONTA_3());
	}
}
