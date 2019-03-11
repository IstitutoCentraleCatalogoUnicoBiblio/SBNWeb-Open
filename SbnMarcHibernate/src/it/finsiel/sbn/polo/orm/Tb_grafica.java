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
public class Tb_grafica extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 8810700757465381086L;

	private String BID;

	private String CD_LIVELLO;

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

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
    this.settaParametro(KeyParameter.XXXcd_livello,value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
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

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}


 public String getBID() {
		return BID;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())));
	}

}
