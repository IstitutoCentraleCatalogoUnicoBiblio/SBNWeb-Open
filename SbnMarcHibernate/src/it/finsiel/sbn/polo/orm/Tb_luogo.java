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

public class Tb_luogo extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -7728353522246210041L;

	private String LID;

	private String TP_FORMA;

	private String CD_LIVELLO;

	private String DS_LUOGO;

	private String KY_LUOGO;

	private String KY_NORM_LUOGO;

	private String CD_PAESE;

	private String NOTA_LUOGO;

	private String NOTA_CATALOGATORE;

	public void setLID(String value) {
		this.LID = value;
    this.settaParametro(KeyParameter.XXXlid,value);
	}

	public String getLID() {
		return LID;
	}

	public void setTP_FORMA(String value) {
		this.TP_FORMA = value;
    this.settaParametro(KeyParameter.XXXtp_forma,value);
	}

	public String getTP_FORMA() {
		return TP_FORMA;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
    this.settaParametro(KeyParameter.XXXcd_livello,value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setDS_LUOGO(String value) {
		this.DS_LUOGO = value;
    this.settaParametro(KeyParameter.XXXds_luogo,value);
	}

	public String getDS_LUOGO() {
		return DS_LUOGO;
	}

	public void setKY_LUOGO(String value) {
		this.KY_LUOGO = value;
    this.settaParametro(KeyParameter.XXXky_luogo,value);
	}

	public String getKY_LUOGO() {
		return KY_LUOGO;
	}

	public void setKY_NORM_LUOGO(String value) {
		this.KY_NORM_LUOGO = value;
    this.settaParametro(KeyParameter.XXXky_norm_luogo,value);
	}

	public String getKY_NORM_LUOGO() {
		return KY_NORM_LUOGO;
	}

	public void setCD_PAESE(String value) {
		this.CD_PAESE = value;
    this.settaParametro(KeyParameter.XXXcd_paese,value);
	}

	public String getCD_PAESE() {
		return CD_PAESE;
	}

	public void setNOTA_LUOGO(String value) {
		this.NOTA_LUOGO = value;
    this.settaParametro(KeyParameter.XXXnota_luogo,value);
	}

	public String getNOTA_LUOGO() {
		return NOTA_LUOGO;
	}

	public String toString() {
		return String.valueOf(getLID());
	}

	public String getNOTA_CATALOGATORE() {
		return NOTA_CATALOGATORE;
	}

	public void setNOTA_CATALOGATORE(String value) {
		NOTA_CATALOGATORE = value;
		this.settaParametro(KeyParameter.XXXnota_catalogatore, value);
	}
}
