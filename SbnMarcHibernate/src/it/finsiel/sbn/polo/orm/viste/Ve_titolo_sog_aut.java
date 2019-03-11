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
public class Ve_titolo_sog_aut extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -4661917584729617498L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_titolo_sog_aut))
			return false;
		Ve_titolo_sog_aut ve_titolo_sog_aut = (Ve_titolo_sog_aut)aObj;
		if (getCID() != null && !getCID().equals(ve_titolo_sog_aut.getCID()))
			return false;
		if (getBID() != null && !getBID().equals(ve_titolo_sog_aut.getBID()))
			return false;
		if (getTP_RESPONSABILITA() != ve_titolo_sog_aut.getTP_RESPONSABILITA())
			return false;
		if (getCD_RELAZIONE() != null && !getCD_RELAZIONE().equals(ve_titolo_sog_aut.getCD_RELAZIONE()))
			return false;
		if (getVID() != null && !getVID().equals(ve_titolo_sog_aut.getVID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCID().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getTP_RESPONSABILITA().hashCode();
		hashcode = hashcode + getCD_RELAZIONE().hashCode();
		hashcode = hashcode + getVID().hashCode();
		return hashcode;
	}

	private String CID;

	private String TP_RESPONSABILITA;

	private String CD_RELAZIONE;

	private String FL_SUPERFLUO;

	private String VID;

	private String KY_CAUTUN;

	private String KY_AUTEUR;

	private String KY_CLES1_A;

	private String KY_CLES2_A;

	public void setCID(String value) {
		this.CID = value;
    this.settaParametro(KeyParameter.XXXcid,value);
	}

	public String getCID() {
		return CID;
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

	public void setFL_SUPERFLUO(String value) {
		this.FL_SUPERFLUO = value;
    this.settaParametro(KeyParameter.XXXfl_superfluo,value);
	}

	public String getFL_SUPERFLUO() {
		return FL_SUPERFLUO;
	}

	public void setVID(String value) {
		this.VID = value;
    this.settaParametro(KeyParameter.XXXvid,value);
	}

	public String getVID() {
		return VID;
	}

	public void setKY_CAUTUN(String value) {
		this.KY_CAUTUN = value;
    this.settaParametro(KeyParameter.XXXky_cautun,value);
	}

	public String getKY_CAUTUN() {
		return KY_CAUTUN;
	}

	public void setKY_AUTEUR(String value) {
		this.KY_AUTEUR = value;
    this.settaParametro(KeyParameter.XXXky_auteur,value);
	}

	public String getKY_AUTEUR() {
		return KY_AUTEUR;
	}

	public void setKY_CLES1_A(String value) {
		this.KY_CLES1_A = value;
    this.settaParametro(KeyParameter.XXXky_cles1_a,value);
	}

	public String getKY_CLES1_A() {
		return KY_CLES1_A;
	}

	public void setKY_CLES2_A(String value) {
		this.KY_CLES2_A = value;
    this.settaParametro(KeyParameter.XXXky_cles2_a,value);
	}

 public String getKY_CLES2_A() {
		return KY_CLES2_A;
	}

	public String toString() {
		return String.valueOf(getCID() + " " + getBID() + " " + getTP_RESPONSABILITA() + " " + getCD_RELAZIONE() + " " + getVID());
	}

}
