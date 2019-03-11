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
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_allinea_aut_bib extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -2983679862144940078L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_allinea_aut_bib))
			return false;
		Vl_allinea_aut_bib vl_allinea_aut_bib = (Vl_allinea_aut_bib)aObj;
		if (getCD_POLO() != null && !getCD_POLO().equals(vl_allinea_aut_bib.getCD_POLO()))
			return false;
		if (getCD_BIBLIOTECA() != null && !getCD_BIBLIOTECA().equals(vl_allinea_aut_bib.getCD_BIBLIOTECA()))
			return false;
		if (getVID() != null && !getVID().equals(vl_allinea_aut_bib.getVID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCD_POLO().hashCode();
		hashcode = hashcode + getCD_BIBLIOTECA().hashCode();
		hashcode = hashcode + getVID().hashCode();
		return hashcode;
	}

	private String CD_POLO;

	private String CD_BIBLIOTECA;

	private String VID;

	private String DS_NOME_AUT;

	private String CD_LIVELLO;

	private String FL_ALLINEA_SBNMARC;

	private String FL_ALLINEA;

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
		this.settaParametro(KeyParameter.XXXfl_condiviso, value);
	}

	public void setTS_CONDIVISO(java.util.Date value) {
		this.TS_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXts_condiviso, value);
	}

	public void setUTE_CONDIVISO(String value) {
		this.UTE_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXute_condiviso, value);
	}

	public void setCD_POLO(String value) {
		this.CD_POLO = value;
    this.settaParametro(KeyParameter.XXXcd_polo,value);
	}

	public String getCD_POLO() {
		return CD_POLO;
	}

	public void setCD_BIBLIOTECA(String value) {
		this.CD_BIBLIOTECA = value;
    this.settaParametro(KeyParameter.XXXcd_biblioteca,value);
	}

	public String getCD_BIBLIOTECA() {
		return CD_BIBLIOTECA;
	}

	public void setVID(String value) {
		this.VID = value;
    this.settaParametro(KeyParameter.XXXvid,value);
	}

	public String getVID() {
		return VID;
	}

	public void setDS_NOME_AUT(String value) {
		this.DS_NOME_AUT = value;
    this.settaParametro(KeyParameter.XXXds_nome_aut,value);
	}

	public String getDS_NOME_AUT() {
		return DS_NOME_AUT;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
    this.settaParametro(KeyParameter.XXXcd_livello,value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setFL_ALLINEA_SBNMARC(String value) {
		this.FL_ALLINEA_SBNMARC = value;
    this.settaParametro(KeyParameter.XXXfl_allinea_sbnmarc,value);
	}

	public String getFL_ALLINEA_SBNMARC() {
		return FL_ALLINEA_SBNMARC;
	}

	public void setFL_ALLINEA(String value) {
		this.FL_ALLINEA = value;
    this.settaParametro(KeyParameter.XXXfl_allinea,value);
	}

 public String getFL_ALLINEA() {
		return FL_ALLINEA;
	}

	public String toString() {
		return String.valueOf(getCD_POLO() + " " + getCD_BIBLIOTECA() + " " + getVID());
	}


}
