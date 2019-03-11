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
public class Vl_titolo_bib extends Tb_titolo implements Serializable  {

	private static final long serialVersionUID = 5442644572272016246L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_titolo_bib))
			return false;
		Vl_titolo_bib vl_titolo_bib = (Vl_titolo_bib)aObj;
		if (getCD_POLO() != null && !getCD_POLO().equals(vl_titolo_bib.getCD_POLO()))
			return false;
		if (getCD_BIBLIOTECA() != null && !getCD_BIBLIOTECA().equals(vl_titolo_bib.getCD_BIBLIOTECA()))
			return false;
		if (getBID() != null && !getBID().equals(vl_titolo_bib.getBID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCD_POLO().hashCode();
		hashcode = hashcode + getCD_BIBLIOTECA().hashCode();
		hashcode = hashcode + getBID().hashCode();
		return hashcode;
	}

	private String CD_POLO;

	private String CD_BIBLIOTECA;

	private String FL_MUTILO;

	private String DS_CONSISTENZA;

	private String FL_POSSESSO;

	private String FL_GESTIONE;

	private String FL_DISP_ELETTR;

	private String FL_ALLINEA;

	private String FL_ALLINEA_SBNMARC;

	private String FL_ALLINEA_CLA;

	private String FL_ALLINEA_SOG;

	private String FL_ALLINEA_LUO;

	private String FL_ALLINEA_REP;

	private String DS_FONDO;

	private String DS_SEGN;

	private String DS_ANTICA_SEGN;

	private String NOTA_TIT_BIB;

	private String URI_COPIA;

	private String TP_DIGITALIZZ;

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

	public void setFL_MUTILO(String value) {
		this.FL_MUTILO = value;
    this.settaParametro(KeyParameter.XXXfl_mutilo,value);
	}

	public String getFL_MUTILO() {
		return FL_MUTILO;
	}

	public void setDS_CONSISTENZA(String value) {
		this.DS_CONSISTENZA = value;
    this.settaParametro(KeyParameter.XXXds_consistenza,value);
	}

	public String getDS_CONSISTENZA() {
		return DS_CONSISTENZA;
	}

	public void setFL_POSSESSO(String value) {
		this.FL_POSSESSO = value;
    this.settaParametro(KeyParameter.XXXfl_possesso,value);
	}

	public String getFL_POSSESSO() {
		return FL_POSSESSO;
	}

	public void setFL_GESTIONE(String value) {
		this.FL_GESTIONE = value;
    this.settaParametro(KeyParameter.XXXfl_gestione,value);
	}

	public String getFL_GESTIONE() {
		return FL_GESTIONE;
	}

	public void setFL_DISP_ELETTR(String value) {
		this.FL_DISP_ELETTR = value;
    this.settaParametro(KeyParameter.XXXfl_disp_elettr,value);
	}

	public String getFL_DISP_ELETTR() {
		return FL_DISP_ELETTR;
	}

	public void setFL_ALLINEA(String value) {
		this.FL_ALLINEA = value;
    this.settaParametro(KeyParameter.XXXfl_allinea,value);
	}

	public String getFL_ALLINEA() {
		return FL_ALLINEA;
	}

	public void setFL_ALLINEA_SBNMARC(String value) {
		this.FL_ALLINEA_SBNMARC = value;
    this.settaParametro(KeyParameter.XXXfl_allinea_sbnmarc,value);
	}

	public String getFL_ALLINEA_SBNMARC() {
		return FL_ALLINEA_SBNMARC;
	}

	public void setFL_ALLINEA_CLA(String value) {
		this.FL_ALLINEA_CLA = value;
    this.settaParametro(KeyParameter.XXXfl_allinea_cla,value);
	}

	public String getFL_ALLINEA_CLA() {
		return FL_ALLINEA_CLA;
	}

	public void setFL_ALLINEA_SOG(String value) {
		this.FL_ALLINEA_SOG = value;
    this.settaParametro(KeyParameter.XXXfl_allinea_sog,value);
	}

	public String getFL_ALLINEA_SOG() {
		return FL_ALLINEA_SOG;
	}

	public void setFL_ALLINEA_LUO(String value) {
		this.FL_ALLINEA_LUO = value;
    this.settaParametro(KeyParameter.XXXfl_allinea_luo,value);
	}

	public String getFL_ALLINEA_LUO() {
		return FL_ALLINEA_LUO;
	}

	public void setFL_ALLINEA_REP(String value) {
		this.FL_ALLINEA_REP = value;
    this.settaParametro(KeyParameter.XXXfl_allinea_rep,value);
	}

	public String getFL_ALLINEA_REP() {
		return FL_ALLINEA_REP;
	}

	public void setDS_FONDO(String value) {
		this.DS_FONDO = value;
    this.settaParametro(KeyParameter.XXXds_fondo,value);
	}

	public String getDS_FONDO() {
		return DS_FONDO;
	}

	public void setDS_SEGN(String value) {
		this.DS_SEGN = value;
    this.settaParametro(KeyParameter.XXXds_segn,value);
	}

	public String getDS_SEGN() {
		return DS_SEGN;
	}

	public void setDS_ANTICA_SEGN(String value) {
		this.DS_ANTICA_SEGN = value;
    this.settaParametro(KeyParameter.XXXds_antica_segn,value);
	}

	public String getDS_ANTICA_SEGN() {
		return DS_ANTICA_SEGN;
	}

	public void setNOTA_TIT_BIB(String value) {
		this.NOTA_TIT_BIB = value;
    this.settaParametro(KeyParameter.XXXnota_tit_bib,value);
	}

	public String getNOTA_TIT_BIB() {
		return NOTA_TIT_BIB;
	}

	public void setURI_COPIA(String value) {
		this.URI_COPIA = value;
    this.settaParametro(KeyParameter.XXXuri_copia,value);
	}

	public String getURI_COPIA() {
		return URI_COPIA;
	}

	public void setTP_DIGITALIZZ(String value) {
		this.TP_DIGITALIZZ = value;
    this.settaParametro(KeyParameter.XXXtp_digitalizz,value);
	}


 public String getTP_DIGITALIZZ() {
		return TP_DIGITALIZZ;
	}

	public String toString() {
		return String.valueOf(getCD_POLO() + " " + getCD_BIBLIOTECA() + " " + getBID());
	}

}
