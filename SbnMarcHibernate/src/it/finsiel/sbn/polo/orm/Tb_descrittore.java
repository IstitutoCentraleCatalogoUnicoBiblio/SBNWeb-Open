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
public class Tb_descrittore extends OggettoServerSbnMarc implements
		Serializable {

	private static final long serialVersionUID = -2429683804704791727L;

	private String DID;

	private String DS_DESCRITTORE;

	private String KY_NORM_DESCRITT;

	private String TP_FORMA_DES;

	private String CD_LIVELLO;

	private String CD_SOGGETTARIO;

	private String CD_EDIZIONE;

	private String CAT_TERMINE;

	private String NOTA_DESCRITTORE;

	// CAMPI NUOVA ISTANZA IN DB NON CI SONO TUTTI I TIMBRI
	private String FL_CONDIVISO;

	// private String UTE_CONDIVISO;
	// private Date TS_CONDIVISO;

	public String getFL_CONDIVISO() {
		return FL_CONDIVISO;
	}

	// public Date getTS_CONDIVISO() {
	// return TS_CONDIVISO;
	// }
	// public String getUTE_CONDIVISO() {
	// return UTE_CONDIVISO;
	// }

	public void setFL_CONDIVISO(String value) {
		this.FL_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXfl_condiviso, value);
	}

	// public void setTS_CONDIVISO(Date value) {
	// this.TS_CONDIVISO = value;
	// this.settaParametro(KeyParameter.XXXts_condiviso,value);
	// }
	//
	// public void setUTE_CONDIVISO(String value) {
	// this.UTE_CONDIVISO = value;
	// this.settaParametro(KeyParameter.XXXute_condiviso,value);
	// }

	public void setDID(String value) {
		this.DID = value;
		this.settaParametro(KeyParameter.XXXdid, value);
	}

	public String getDID() {
		return DID;
	}

	public void setDS_DESCRITTORE(String value) {
		this.DS_DESCRITTORE = value;
		this.settaParametro(KeyParameter.XXXds_descrittore, value);
	}

	public String getDS_DESCRITTORE() {
		return DS_DESCRITTORE;
	}

	public void setKY_NORM_DESCRITT(String value) {
		this.KY_NORM_DESCRITT = value;
		this.settaParametro(KeyParameter.XXXky_norm_descritt, value);
	}

	public String getKY_NORM_DESCRITT() {
		return KY_NORM_DESCRITT;
	}

	public void setTP_FORMA_DES(String value) {
		this.TP_FORMA_DES = value;
		this.settaParametro(KeyParameter.XXXtp_forma_des, value);
	}

	public String getTP_FORMA_DES() {
		return TP_FORMA_DES;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
		this.settaParametro(KeyParameter.XXXcd_livello, value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setCD_SOGGETTARIO(String value) {
		this.CD_SOGGETTARIO = value;
		this.settaParametro(KeyParameter.XXXcd_soggettario, value);
	}

	public String getCD_SOGGETTARIO() {
		return CD_SOGGETTARIO;
	}

	public void setNOTA_DESCRITTORE(String value) {
		this.NOTA_DESCRITTORE = value;
		this.settaParametro(KeyParameter.XXXnota_descrittore, value);
	}

	public String getNOTA_DESCRITTORE() {
		return NOTA_DESCRITTORE;
	}

	public void setCD_EDIZIONE(String value) {
		//almaviva5_20111130 evolutive CFI
		CD_EDIZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_edizione, value);
	}

	public String getCD_EDIZIONE() {
		return CD_EDIZIONE;
	}

	public String toString() {
		return String.valueOf(getDID());
	}

	public String getCAT_TERMINE() {
		return CAT_TERMINE;
	}

	public void setCAT_TERMINE(String value) {
		CAT_TERMINE = value;
		this.settaParametro(KeyParameter.XXXcat_termine, value);
	}
}
