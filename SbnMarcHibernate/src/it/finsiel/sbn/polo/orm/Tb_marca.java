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
public class Tb_marca extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 8057001579563024272L;

	private String MID;

	private String CD_LIVELLO;

	private String FL_SPECIALE;

	private String DS_MARCA;

	private String NOTA_MARCA;

	private String DS_MOTTO;

	//	 CAMPI NUOVA ISTANZA
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

	public void setMID(String value) {
		this.MID = value;
    this.settaParametro(KeyParameter.XXXmid,value);
	}

	public String getMID() {
		return MID;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
    this.settaParametro(KeyParameter.XXXcd_livello,value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setFL_SPECIALE(String value) {
		this.FL_SPECIALE = value;
    this.settaParametro(KeyParameter.XXXfl_speciale,value);
	}

	public String getFL_SPECIALE() {
		return FL_SPECIALE;
	}

	public void setDS_MARCA(String value) {
		this.DS_MARCA = value;
    this.settaParametro(KeyParameter.XXXds_marca,value);
	}

	public String getDS_MARCA() {
		return DS_MARCA;
	}

	public void setNOTA_MARCA(String value) {
		this.NOTA_MARCA = value;
    this.settaParametro(KeyParameter.XXXnota_marca,value);
	}

	public String getNOTA_MARCA() {
		return NOTA_MARCA;
	}

	public void setDS_MOTTO(String value) {
		this.DS_MOTTO = value;
    this.settaParametro(KeyParameter.XXXds_motto,value);
	}

	public String getDS_MOTTO() {
		return DS_MOTTO;
	}

	public String toString() {
		return String.valueOf(getMID());
	}
}
