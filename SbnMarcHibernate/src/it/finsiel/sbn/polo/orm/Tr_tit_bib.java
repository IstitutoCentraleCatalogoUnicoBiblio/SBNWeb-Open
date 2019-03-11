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
public class Tr_tit_bib extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -5642968675771747655L;


	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_bib))
			return false;
		Tr_tit_bib tr_tit_bib = (Tr_tit_bib)aObj;
		if (getBID() == null)
			return false;
		if (!getBID().equals(tr_tit_bib.getBID()))
			return false;

		if (getCD_POLO() == null)
			return false;
		if (!getCD_POLO().equals(tr_tit_bib.getCD_POLO()))
			return false;

		if (getCD_BIBLIOTECA() == null)
			return false;
		if (!getCD_BIBLIOTECA().equals(tr_tit_bib.getCD_BIBLIOTECA()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getCD_POLO().hashCode();
		hashcode = hashcode + getCD_BIBLIOTECA().hashCode();
		return hashcode;
	}

	private String BID;

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

	private java.util.Date TS_INS_PRIMA_COLL;


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

	public void setTS_INS_PRIMA_COLL(java.util.Date value) {
		this.TS_INS_PRIMA_COLL = value;
    this.settaParametro(KeyParameter.XXXts_ins_prima_coll,value);
	}

	public java.util.Date getTS_INS_PRIMA_COLL() {
		return TS_INS_PRIMA_COLL;
	}



	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
	}

	public void setCD_POLO(String value) {
		this.CD_POLO = value;
    this.settaParametro(KeyParameter.XXXcd_polo,value);
	}

	public String getCD_POLO() {
		return CD_POLO;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())) + " " + ((getCD_POLO() == null) ? "" : String.valueOf(getCD_POLO()) + " " + String.valueOf(getCD_BIBLIOTECA())));
	}

	private boolean _saved = false;

	public void onSave() {
		_saved=true;
	}


	public void onLoad() {
		_saved=true;
	}


	public boolean isSaved() {
		return _saved;
	}

	public String getCD_BIBLIOTECA() {
		return CD_BIBLIOTECA;
	}

	public void setCD_BIBLIOTECA(String cd_biblioteca) {
		CD_BIBLIOTECA = cd_biblioteca;
    this.settaParametro(KeyParameter.XXXcd_biblioteca,cd_biblioteca);
	}


}
