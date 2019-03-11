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
public class Ve_titolo_cla_luo extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -7494587030553897953L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_titolo_cla_luo))
			return false;
		Ve_titolo_cla_luo ve_titolo_cla_luo = (Ve_titolo_cla_luo)aObj;
		if (getCD_SISTEMA() != ve_titolo_cla_luo.getCD_SISTEMA())
			return false;
		if (getCD_EDIZIONE() != ve_titolo_cla_luo.getCD_EDIZIONE())
			return false;
		if (getCLASSE() != null && !getCLASSE().equals(ve_titolo_cla_luo.getCLASSE()))
			return false;
		if (getBID() != null && !getBID().equals(ve_titolo_cla_luo.getBID()))
			return false;
		if (getLID() != null && !getLID().equals(ve_titolo_cla_luo.getLID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCD_SISTEMA().hashCode();
		hashcode = hashcode + getCD_EDIZIONE().hashCode();
		hashcode = hashcode + getCLASSE().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getLID().hashCode();
		return hashcode;
	}

	private String CD_SISTEMA;

	private String CD_EDIZIONE;

	private String CLASSE;

	private String LID;

	private String KY_NORM_LUOGO;

	public void setCD_SISTEMA(String value) {
		this.CD_SISTEMA = value;
    this.settaParametro(KeyParameter.XXXcd_sistema,value);
	}

	public String getCD_SISTEMA() {
		return CD_SISTEMA;
	}

	public void setCD_EDIZIONE(String value) {
		this.CD_EDIZIONE = value;
    this.settaParametro(KeyParameter.XXXcd_edizione,value);
	}

	public String getCD_EDIZIONE() {
		return CD_EDIZIONE;
	}

	public void setCLASSE(String value) {
		this.CLASSE = value;
    this.settaParametro(KeyParameter.XXXclasse,value);
	}

	public String getCLASSE() {
		return CLASSE;
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
		return String.valueOf(getCD_SISTEMA() + " " + getCD_EDIZIONE() + " " + getCLASSE() + " " + getBID() + " " + getLID());
	}

}
