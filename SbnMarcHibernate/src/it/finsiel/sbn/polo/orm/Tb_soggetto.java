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
public class Tb_soggetto extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 5985486580288078766L;

	private String CID;

	private String CD_LIVELLO;

	private String CD_SOGGETTARIO;

	private String CD_EDIZIONE;

	private String FL_SPECIALE;

	private String DS_SOGGETTO;

	private String KY_CLES1_S;

	private String KY_CLES2_S;

	// NUOVI CAMPI
	private String FL_CONDIVISO;
	private String UTE_CONDIVISO;
	private java.util.Date TS_CONDIVISO;
	private String KY_PRIMO_DESCR;
	private String CAT_SOGG;
	private String NOTA_SOGGETTO;


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
		this.settaParametro(KeyParameter.XXXfl_condiviso,value);
	}

	public void setTS_CONDIVISO(java.util.Date value) {
		this.TS_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXts_condiviso,value);
	}

	public void setUTE_CONDIVISO(String value) {
		this.UTE_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXute_condiviso,value);
	}

	public String getCAT_SOGG() {
		return CAT_SOGG;
	}
	public void setCAT_SOGG(String value) {
		this.CAT_SOGG = value;
		this.settaParametro(KeyParameter.XXXcat_sogg,value);
	}
	public String getKY_PRIMO_DESCR() {
		return KY_PRIMO_DESCR;
	}
	public void setKY_PRIMO_DESCR(String value) {
		this.KY_PRIMO_DESCR = value;
		this.settaParametro(KeyParameter.XXXky_primo_descr,value);
	}



	public void setCID(String value) {
		this.CID = value;
    this.settaParametro(KeyParameter.XXXcid,value);
	}

	public String getCID() {
		return CID;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
    this.settaParametro(KeyParameter.XXXcd_livello,value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setCD_SOGGETTARIO(String value) {
		this.CD_SOGGETTARIO = value;
    this.settaParametro(KeyParameter.XXXcd_soggettario,value);
	}

	public String getCD_SOGGETTARIO() {
		return CD_SOGGETTARIO;
	}

	public void setCD_EDIZIONE(String value) {
		//almaviva5_20111122 evolutive CFI
		CD_EDIZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_edizione, value);
	}

	public String getCD_EDIZIONE() {
		return CD_EDIZIONE;
	}

	public void setFL_SPECIALE(String value) {
		this.FL_SPECIALE = value;
    this.settaParametro(KeyParameter.XXXfl_speciale,value);
	}

	public String getFL_SPECIALE() {
		return FL_SPECIALE;
	}

	public void setDS_SOGGETTO(String value) {
		this.DS_SOGGETTO = value;
    this.settaParametro(KeyParameter.XXXds_soggetto,value);
	}

	public String getDS_SOGGETTO() {
		return DS_SOGGETTO;
	}

	public void setKY_CLES1_S(String value) {
		this.KY_CLES1_S = value;
    this.settaParametro(KeyParameter.XXXky_cles1_s,value);
	}

	public String getKY_CLES1_S() {
		return KY_CLES1_S;
	}

	public void setKY_CLES2_S(String value) {
		this.KY_CLES2_S = value;
    this.settaParametro(KeyParameter.XXXky_cles2_s,value);
	}

	public String getKY_CLES2_S() {
		return KY_CLES2_S;
	}

	public void setNOTA_SOGGETTO(String value) {
		this.NOTA_SOGGETTO = value;
    this.settaParametro(KeyParameter.XXXnota_soggetto,value);
	}

	public String getNOTA_SOGGETTO() {
		return NOTA_SOGGETTO;
	}

	public String toString() {
		return String.valueOf(getCID());
	}

}
