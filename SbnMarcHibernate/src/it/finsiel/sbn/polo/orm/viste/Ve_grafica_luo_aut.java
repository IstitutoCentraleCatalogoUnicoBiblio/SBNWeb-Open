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
public class Ve_grafica_luo_aut extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -878096972815422939L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_grafica_luo_aut))
			return false;
		Ve_grafica_luo_aut ve_grafica_luo_aut = (Ve_grafica_luo_aut)aObj;
		if (getLID() != null && !getLID().equals(ve_grafica_luo_aut.getLID()))
			return false;
		if (getTP_LUOGO() != ve_grafica_luo_aut.getTP_LUOGO())
			return false;
		if (getBID() != null && !getBID().equals(ve_grafica_luo_aut.getBID()))
			return false;
		if (getTP_RESPONSABILITA() != ve_grafica_luo_aut.getTP_RESPONSABILITA())
			return false;
		if (getCD_RELAZIONE() != null && !getCD_RELAZIONE().equals(ve_grafica_luo_aut.getCD_RELAZIONE()))
			return false;
		if (getVID() != null && !getVID().equals(ve_grafica_luo_aut.getVID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getLID().hashCode();
		hashcode = hashcode + getTP_LUOGO().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getTP_RESPONSABILITA().hashCode();
		hashcode = hashcode + getCD_RELAZIONE().hashCode();
		hashcode = hashcode + getVID().hashCode();
		return hashcode;
	}

	private String LID;

	private String TP_LUOGO;

	private String NOTA_TIT_LUO;

	private String CD_LIVELLO_G;

	private String TP_MATERIALE_GRA;

	private String CD_SUPPORTO;

	private String CD_COLORE;

	private String CD_TECNICA_DIS_1;

	private String CD_TECNICA_DIS_2;

	private String CD_TECNICA_DIS_3;

	private String CD_TECNICA_STA_1;

	private String CD_TECNICA_STA_2;

	private String CD_TECNICA_STA_3;

	private String CD_DESIGN_FUNZ;

	private String TP_RESPONSABILITA;

	private String CD_RELAZIONE;

	private String FL_SUPERFLUO;

	private String VID;

	private String KY_CAUTUN;

	private String KY_AUTEUR;

	private String KY_CLES1_A;

	private String KY_CLES2_A;

	public void setLID(String value) {
		this.LID = value;
    this.settaParametro(KeyParameter.XXXlid,value);
	}

	public String getLID() {
		return LID;
	}

	public void setTP_LUOGO(String value) {
		this.TP_LUOGO = value;
    this.settaParametro(KeyParameter.XXXtp_luogo,value);
	}

	public String getTP_LUOGO() {
		return TP_LUOGO;
	}

	public void setNOTA_TIT_LUO(String value) {
		this.NOTA_TIT_LUO = value;
    this.settaParametro(KeyParameter.XXXnota_tit_luo,value);
	}

	public String getNOTA_TIT_LUO() {
		return NOTA_TIT_LUO;
	}

	public void setCD_LIVELLO_G(String value) {
		this.CD_LIVELLO_G = value;
    this.settaParametro(KeyParameter.XXXcd_livello_g,value);
	}

	public String getCD_LIVELLO_G() {
		return CD_LIVELLO_G;
	}

	public void setTP_MATERIALE_GRA(String value) {
		this.TP_MATERIALE_GRA = value;
    this.settaParametro(KeyParameter.XXXtp_materiale_gra,value);
	}

	public String getTP_MATERIALE_GRA() {
		return TP_MATERIALE_GRA;
	}

	public void setCD_SUPPORTO(String value) {
		this.CD_SUPPORTO = value;
    this.settaParametro(KeyParameter.XXXcd_supporto,value);
	}

	public String getCD_SUPPORTO() {
		return CD_SUPPORTO;
	}

	public void setCD_COLORE(String value) {
		this.CD_COLORE = value;
    this.settaParametro(KeyParameter.XXXcd_colore,value);
	}

	public String getCD_COLORE() {
		return CD_COLORE;
	}

	public void setCD_TECNICA_DIS_1(String value) {
		this.CD_TECNICA_DIS_1 = value;
    this.settaParametro(KeyParameter.XXXcd_tecnica_dis_1,value);
	}

	public String getCD_TECNICA_DIS_1() {
		return CD_TECNICA_DIS_1;
	}

	public void setCD_TECNICA_DIS_2(String value) {
		this.CD_TECNICA_DIS_2 = value;
    this.settaParametro(KeyParameter.XXXcd_tecnica_dis_2,value);
	}

	public String getCD_TECNICA_DIS_2() {
		return CD_TECNICA_DIS_2;
	}

	public void setCD_TECNICA_DIS_3(String value) {
		this.CD_TECNICA_DIS_3 = value;
    this.settaParametro(KeyParameter.XXXcd_tecnica_dis_3,value);
	}

	public String getCD_TECNICA_DIS_3() {
		return CD_TECNICA_DIS_3;
	}

	public void setCD_TECNICA_STA_1(String value) {
		this.CD_TECNICA_STA_1 = value;
    this.settaParametro(KeyParameter.XXXcd_tecnica_sta_1,value);
	}

	public String getCD_TECNICA_STA_1() {
		return CD_TECNICA_STA_1;
	}

	public void setCD_TECNICA_STA_2(String value) {
		this.CD_TECNICA_STA_2 = value;
    this.settaParametro(KeyParameter.XXXcd_tecnica_sta_2,value);
	}

	public String getCD_TECNICA_STA_2() {
		return CD_TECNICA_STA_2;
	}

	public void setCD_TECNICA_STA_3(String value) {
		this.CD_TECNICA_STA_3 = value;
    this.settaParametro(KeyParameter.XXXcd_tecnica_sta_3,value);
	}

	public String getCD_TECNICA_STA_3() {
		return CD_TECNICA_STA_3;
	}

	public void setCD_DESIGN_FUNZ(String value) {
		this.CD_DESIGN_FUNZ = value;
    this.settaParametro(KeyParameter.XXXcd_design_funz,value);
	}

	public String getCD_DESIGN_FUNZ() {
		return CD_DESIGN_FUNZ;
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
		return String.valueOf(getLID() + " " + getTP_LUOGO() + " " + getBID() + " " + getTP_RESPONSABILITA() + " " + getCD_RELAZIONE() + " " + getVID());
	}

}
