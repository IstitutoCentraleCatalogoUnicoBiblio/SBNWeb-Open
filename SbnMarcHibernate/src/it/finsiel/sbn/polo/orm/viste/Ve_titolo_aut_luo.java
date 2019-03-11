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
public class Ve_titolo_aut_luo extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -731876333412160062L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_titolo_aut_luo))
			return false;
		Ve_titolo_aut_luo ve_titolo_aut_luo = (Ve_titolo_aut_luo)aObj;
		if (getVID() != null && !getVID().equals(ve_titolo_aut_luo.getVID()))
			return false;
		if (getTP_RESPONSABILITA() != ve_titolo_aut_luo.getTP_RESPONSABILITA())
			return false;
		if (getCD_RELAZIONE() != null && !getCD_RELAZIONE().equals(ve_titolo_aut_luo.getCD_RELAZIONE()))
			return false;
		if (getBID() != null && !getBID().equals(ve_titolo_aut_luo.getBID()))
			return false;
		if (getLID() != null && !getLID().equals(ve_titolo_aut_luo.getLID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getVID().hashCode();
		hashcode = hashcode + getTP_RESPONSABILITA().hashCode();
		hashcode = hashcode + getCD_RELAZIONE().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getLID().hashCode();
		return hashcode;
	}

	private String VID;

	private String TP_RESPONSABILITA;

	private String CD_RELAZIONE;

	private String NOTA_TIT_AUT;

	private String FL_INCERTO;

	private String FL_SUPERFLUO;

	private String CD_STRUMENTO_MUS;

	private String LID;

	private String KY_NORM_LUOGO;

	public void setVID(String value) {
		this.VID = value;
    this.settaParametro(KeyParameter.XXXvid,value);
	}

	public String getVID() {
		return VID;
	}

	public void setTP_RESPONSABILITA(String value) {
		this.TP_RESPONSABILITA = value;
    this.settaParametro(KeyParameter.XXXtp_responsabilita,value);
	}

	public String getTP_RESPONSABILITA() {
		return TP_RESPONSABILITA;
	}

	public void setCD_RELAZIONE(String value) {
		this.CD_RELAZIONE = value;
    this.settaParametro(KeyParameter.XXXcd_relazione,value);
	}

	public String getCD_RELAZIONE() {
		return CD_RELAZIONE;
	}

	public void setNOTA_TIT_AUT(String value) {
		this.NOTA_TIT_AUT = value;
    this.settaParametro(KeyParameter.XXXnota_tit_aut,value);
	}

	public String getNOTA_TIT_AUT() {
		return NOTA_TIT_AUT;
	}

	public void setFL_INCERTO(String value) {
		this.FL_INCERTO = value;
    this.settaParametro(KeyParameter.XXXfl_incerto,value);
	}

	public String getFL_INCERTO() {
		return FL_INCERTO;
	}

	public void setFL_SUPERFLUO(String value) {
		this.FL_SUPERFLUO = value;
    this.settaParametro(KeyParameter.XXXfl_superfluo,value);
	}

	public String getFL_SUPERFLUO() {
		return FL_SUPERFLUO;
	}

	public void setCD_STRUMENTO_MUS(String value) {
		this.CD_STRUMENTO_MUS = value;
    this.settaParametro(KeyParameter.XXXcd_strumento_mus,value);
	}

	public String getCD_STRUMENTO_MUS() {
		return CD_STRUMENTO_MUS;
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
		return String.valueOf(getVID() + " " + getTP_RESPONSABILITA() + " " + getCD_RELAZIONE() + " " + getBID() + " " + getLID());
	}

}
