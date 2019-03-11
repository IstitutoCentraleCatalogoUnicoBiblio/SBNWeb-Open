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
public class Vf_titolo_aut extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = 3909040996162104517L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vf_titolo_aut))
			return false;
		Vf_titolo_aut vf_titolo_aut = (Vf_titolo_aut)aObj;
		if (getBID() != null && !getBID().equals(vf_titolo_aut.getBID()))
			return false;
		if (getTP_RESPONSABILITA() != vf_titolo_aut.getTP_RESPONSABILITA())
			return false;
		if (getCD_RELAZIONE() != null && !getCD_RELAZIONE().equals(vf_titolo_aut.getCD_RELAZIONE()))
			return false;
		if (getVID() != null && !getVID().equals(vf_titolo_aut.getVID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getTP_RESPONSABILITA().hashCode();
		hashcode = hashcode + getCD_RELAZIONE().hashCode();
		hashcode = hashcode + getVID().hashCode();
		return hashcode;
	}

	private String TP_RESPONSABILITA;

	private String CD_RELAZIONE;

	private String FL_SUPERFLUO;

	private java.util.Date TS_INS_TIT_AUT;

	private java.util.Date TS_VAR_TIT_AUT;

	private String VID;

	private String KY_CAUTUN;

	private String KY_AUTEUR;

	private String TP_NOME_AUT;

	private String KY_CLES1_A;

	private String KY_CLES2_A;

	private String CD_LIVELLO_AUT;

	private String DS_NOME_AUT;

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

	public void setTS_INS_TIT_AUT(java.util.Date value) {
		this.TS_INS_TIT_AUT = value;
    this.settaParametro(KeyParameter.XXXts_ins_tit_aut,value);
	}

	public java.util.Date getTS_INS_TIT_AUT() {
		return TS_INS_TIT_AUT;
	}

	public void setTS_VAR_TIT_AUT(java.util.Date value) {
		this.TS_VAR_TIT_AUT = value;
    this.settaParametro(KeyParameter.XXXts_var_tit_aut,value);
	}

	public java.util.Date getTS_VAR_TIT_AUT() {
		return TS_VAR_TIT_AUT;
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

	public void setTP_NOME_AUT(String value) {
		this.TP_NOME_AUT = value;
    this.settaParametro(KeyParameter.XXXtp_nome_aut,value);
	}

	public String getTP_NOME_AUT() {
		return TP_NOME_AUT;
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

	public void setCD_LIVELLO_AUT(String value) {
		this.CD_LIVELLO_AUT = value;
    this.settaParametro(KeyParameter.XXXcd_livello_aut,value);
	}

	public String getCD_LIVELLO_AUT() {
		return CD_LIVELLO_AUT;
	}

	public void setDS_NOME_AUT(String value) {
		this.DS_NOME_AUT = value;
    this.settaParametro(KeyParameter.XXXds_nome_aut,value);
	}

 public String getDS_NOME_AUT() {
		return DS_NOME_AUT;
 }

	public String toString() {
		return String.valueOf(getBID() + " " + getTP_RESPONSABILITA() + " " + getCD_RELAZIONE() + " " + getVID());
	}


}
