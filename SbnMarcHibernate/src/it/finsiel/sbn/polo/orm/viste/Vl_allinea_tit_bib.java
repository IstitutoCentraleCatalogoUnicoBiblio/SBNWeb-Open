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
public class Vl_allinea_tit_bib extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -6991753450274551131L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_allinea_tit_bib))
			return false;
		Vl_allinea_tit_bib vl_allinea_tit_bib = (Vl_allinea_tit_bib)aObj;
		if (getBID() != null && !getBID().equals(vl_allinea_tit_bib.getBID()))
			return false;
		if (getCD_POLO() != null && !getCD_POLO().equals(vl_allinea_tit_bib.getCD_POLO()))
			return false;
		if (getCD_BIBLIOTECA() != null && !getCD_BIBLIOTECA().equals(vl_allinea_tit_bib.getCD_BIBLIOTECA()))
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

	private String TP_MATERIALE;

	private String TP_RECORD_UNI;

	private String CD_NATURA;

	private String CD_LIVELLO;

	private String ISBD;

	private String INDICE_ISBD;

	private String TP_LINK;

	private String BID_LINK;

	private String FL_ALLINEA_SBNMARC;

	private String FL_ALLINEA;

	private String FL_ALLINEA_CLA;

	private String FL_ALLINEA_SOG;

	private String FL_ALLINEA_LUO;

	private String FL_ALLINEA_REP;

	private String FL_POSSESSO;

	private String FL_GESTIONE;

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

	public void setCD_BIBLIOTECA(String value) {
		this.CD_BIBLIOTECA = value;
    this.settaParametro(KeyParameter.XXXcd_biblioteca,value);
	}

	public String getCD_BIBLIOTECA() {
		return CD_BIBLIOTECA;
	}

	public void setTP_MATERIALE(String value) {
		this.TP_MATERIALE = value;
    this.settaParametro(KeyParameter.XXXtp_materiale,value);
	}

	public String getTP_MATERIALE() {
		return TP_MATERIALE;
	}

	public void setTP_RECORD_UNI(String value) {
		this.TP_RECORD_UNI = value;
    this.settaParametro(KeyParameter.XXXtp_record_uni,value);
	}

	public String getTP_RECORD_UNI() {
		return TP_RECORD_UNI;
	}

	public void setCD_NATURA(String value) {
		this.CD_NATURA = value;
    this.settaParametro(KeyParameter.XXXcd_natura,value);
	}

	public String getCD_NATURA() {
		return CD_NATURA;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
    this.settaParametro(KeyParameter.XXXcd_livello,value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setISBD(String value) {
		this.ISBD = value;
    this.settaParametro(KeyParameter.XXXisbd,value);
	}

	public String getISBD() {
		return ISBD;
	}

	public void setINDICE_ISBD(String value) {
		this.INDICE_ISBD = value;
    this.settaParametro(KeyParameter.XXXindice_isbd,value);
	}

	public String getINDICE_ISBD() {
		return INDICE_ISBD;
	}

	public void setTP_LINK(String value) {
		this.TP_LINK = value;
    this.settaParametro(KeyParameter.XXXtp_link,value);
	}

	public String getTP_LINK() {
		return TP_LINK;
	}

	public void setBID_LINK(String value) {
		this.BID_LINK = value;
    this.settaParametro(KeyParameter.XXXbid_link,value);
	}

	public String getBID_LINK() {
		return BID_LINK;
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

	public String toString() {
		return String.valueOf(getBID() + " " + getCD_POLO() + " " + getCD_BIBLIOTECA());
	}

}
