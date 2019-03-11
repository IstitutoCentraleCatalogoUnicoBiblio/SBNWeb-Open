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
public class Ve_grafica_mar_luo extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -5428936926462044154L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_grafica_mar_luo))
			return false;
		Ve_grafica_mar_luo ve_grafica_mar_luo = (Ve_grafica_mar_luo)aObj;
		if (getMID() != null && !getMID().equals(ve_grafica_mar_luo.getMID()))
			return false;
		if (getBID() != null && !getBID().equals(ve_grafica_mar_luo.getBID()))
			return false;
		if (getLID() != null && !getLID().equals(ve_grafica_mar_luo.getLID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getMID().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getLID().hashCode();
		return hashcode;
	}

	private String MID;

	private String NOTA_TIT_MAR;

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

	private String BID;

	private String ISADN;

	private String TP_MATERIALE;

	private String TP_RECORD_UNI;

	private String CD_NATURA;

	private String CD_PAESE;

	private String CD_LINGUA_1;

	private String CD_LINGUA_2;

	private String CD_LINGUA_3;

	private String AA_PUBB_1;

	private String AA_PUBB_2;

	private String TP_AA_PUBB;

	private String CD_GENERE_1;

	private String CD_GENERE_2;

	private String CD_GENERE_3;

	private String CD_GENERE_4;

	private String KY_CLES1_T;

	private String KY_CLES2_T;

	private String KY_CLET1_T;

	private String KY_CLET2_T;

	private String KY_CLES1_CT;

	private String KY_CLES2_CT;

	private String KY_CLET1_CT;

	private String KY_CLET2_CT;

	private String CD_LIVELLO;

	private String FL_SPECIALE;

	private String ISBD;

	private String INDICE_ISBD;

	private String KY_EDITORE;

	private String CD_AGENZIA;

	private String CD_NORME_CAT;

	private String NOTA_INF_TIT;

	private String NOTA_CAT_TIT;

	private String BID_LINK;

	private String TP_LINK;

	private String UTE_FORZA_INS;

	private String UTE_FORZA_VAR;

	private String LID;

	private String KY_NORM_LUOGO;

	public void setMID(String value) {
		this.MID = value;
    this.settaParametro(KeyParameter.XXXmid,value);
	}

	public String getMID() {
		return MID;
	}

	public void setNOTA_TIT_MAR(String value) {
		this.NOTA_TIT_MAR = value;
    this.settaParametro(KeyParameter.XXXnota_tit_mar,value);
	}

	public String getNOTA_TIT_MAR() {
		return NOTA_TIT_MAR;
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
		return String.valueOf(getMID() + " " + getBID() + " " + getLID());
	}

}
