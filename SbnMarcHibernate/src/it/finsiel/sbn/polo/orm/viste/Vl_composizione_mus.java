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
import it.finsiel.sbn.polo.orm.Tb_composizione;

import java.io.Serializable;
/**
 * ORM-Persistable Classù
 * QUESTA VISTA SEMBRA NON ESSERE MAI UTILIZZATA
 */
public class Vl_composizione_mus extends Tb_composizione implements Serializable{

	private static final long serialVersionUID = -7006336275134443635L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_composizione_mus))
			return false;
		Vl_composizione_mus vl_composizione_mus = (Vl_composizione_mus)aObj;
		if (getBID() != null && !getBID().equals(vl_composizione_mus.getBID()))
			return false;
		if (getBID_BASE() != null && !getBID_BASE().equals(vl_composizione_mus.getBID_BASE()))
			return false;
		if (getTP_LEGAME() != null && !getTP_LEGAME().equals(vl_composizione_mus.getTP_LEGAME()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getBID_BASE().hashCode();
		hashcode = hashcode + getTP_LEGAME().hashCode();
		return hashcode;
	}

	private String BID_BASE;

	private String TP_LEGAME;

	private String TP_LEGAME_MUSICA;

	private String CD_NATURA_BASE;

	private String CD_NATURA_COLL;

	private String SEQUENZA;

	private String NOTA_TIT_TIT;

	private String SEQUENZA_MUSICA;

	private String SICI;

	private String CD_LIVELLO;

	private String DS_ORG_SINT;

	private String DS_ORG_ANAL;

	private String TP_ELABORAZIONE;

	private String CD_STESURA;

	private String FL_COMPOSITO;

	private String FL_PALINSESTO;

	private String DATAZIONE_M;

	private String CD_PRESENTAZIONE;

	private String CD_MATERIA;

	private String DS_ILLUSTRAZIONI;

	private String NOTAZIONE_MUSICALE;

	private String DS_LEGATURA;

	private String DS_CONSERVAZIONE;

	private String TP_TESTO_LETTER;

	private String UTE_INS_M;

	private java.util.Date TS_INS_M;

	private String UTE_VAR_M;

	private java.util.Date TS_VAR_M;

	private String FL_CANC_M;

	public void setBID_BASE(String value) {
		this.BID_BASE = value;
    this.settaParametro(KeyParameter.XXXbid_base,value);
	}

	public String getBID_BASE() {
		return BID_BASE;
	}

	public void setTP_LEGAME(String value) {
		this.TP_LEGAME = value;
    this.settaParametro(KeyParameter.XXXtp_legame,value);
	}

	public String getTP_LEGAME() {
		return TP_LEGAME;
	}

	public void setTP_LEGAME_MUSICA(String value) {
		this.TP_LEGAME_MUSICA = value;
    this.settaParametro(KeyParameter.XXXtp_legame_musica,value);
	}

	public String getTP_LEGAME_MUSICA() {
		return TP_LEGAME_MUSICA;
	}

	public void setCD_NATURA_BASE(String value) {
		this.CD_NATURA_BASE = value;
    this.settaParametro(KeyParameter.XXXcd_natura_base,value);
	}

	public String getCD_NATURA_BASE() {
		return CD_NATURA_BASE;
	}

	public void setCD_NATURA_COLL(String value) {
		this.CD_NATURA_COLL = value;
    this.settaParametro(KeyParameter.XXXcd_natura_coll,value);
	}

	public String getCD_NATURA_COLL() {
		return CD_NATURA_COLL;
	}

	public void setSEQUENZA(String value) {
		this.SEQUENZA = value;
    this.settaParametro(KeyParameter.XXXsequenza,value);
	}

	public String getSEQUENZA() {
		return SEQUENZA;
	}

	public void setNOTA_TIT_TIT(String value) {
		this.NOTA_TIT_TIT = value;
    this.settaParametro(KeyParameter.XXXnota_tit_tit,value);
	}

	public String getNOTA_TIT_TIT() {
		return NOTA_TIT_TIT;
	}

	public void setSEQUENZA_MUSICA(String value) {
		this.SEQUENZA_MUSICA = value;
    this.settaParametro(KeyParameter.XXXsequenza_musica,value);
	}

	public String getSEQUENZA_MUSICA() {
		return SEQUENZA_MUSICA;
	}

	public void setSICI(String value) {
		this.SICI = value;
    this.settaParametro(KeyParameter.XXXsici,value);
	}

	public String getSICI() {
		return SICI;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
    this.settaParametro(KeyParameter.XXXcd_livello,value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setDS_ORG_SINT(String value) {
		this.DS_ORG_SINT = value;
    this.settaParametro(KeyParameter.XXXds_org_sint,value);
	}

	public String getDS_ORG_SINT() {
		return DS_ORG_SINT;
	}

	public void setDS_ORG_ANAL(String value) {
		this.DS_ORG_ANAL = value;
    this.settaParametro(KeyParameter.XXXds_org_anal,value);
	}

	public String getDS_ORG_ANAL() {
		return DS_ORG_ANAL;
	}

	public void setTP_ELABORAZIONE(String value) {
		this.TP_ELABORAZIONE = value;
    this.settaParametro(KeyParameter.XXXtp_elaborazione,value);
	}

	public String getTP_ELABORAZIONE() {
		return TP_ELABORAZIONE;
	}

	public void setCD_STESURA(String value) {
		this.CD_STESURA = value;
    this.settaParametro(KeyParameter.XXXcd_stesura,value);
	}

	public String getCD_STESURA() {
		return CD_STESURA;
	}

	public void setFL_COMPOSITO(String value) {
		this.FL_COMPOSITO = value;
    this.settaParametro(KeyParameter.XXXfl_composito,value);
	}

	public String getFL_COMPOSITO() {
		return FL_COMPOSITO;
	}

	public void setFL_PALINSESTO(String value) {
		this.FL_PALINSESTO = value;
    this.settaParametro(KeyParameter.XXXfl_palinsesto,value);
	}

	public String getFL_PALINSESTO() {
		return FL_PALINSESTO;
	}

	public void setDATAZIONE_M(String value) {
		this.DATAZIONE_M = value;
    this.settaParametro(KeyParameter.XXXdatazione_m,value);
	}

	public String getDATAZIONE_M() {
		return DATAZIONE_M;
	}

	public void setCD_PRESENTAZIONE(String value) {
		this.CD_PRESENTAZIONE = value;
    this.settaParametro(KeyParameter.XXXcd_presentazione,value);
	}

	public String getCD_PRESENTAZIONE() {
		return CD_PRESENTAZIONE;
	}

	public void setCD_MATERIA(String value) {
		this.CD_MATERIA = value;
    this.settaParametro(KeyParameter.XXXcd_materia,value);
	}

	public String getCD_MATERIA() {
		return CD_MATERIA;
	}

	public void setDS_ILLUSTRAZIONI(String value) {
		this.DS_ILLUSTRAZIONI = value;
    this.settaParametro(KeyParameter.XXXds_illustrazioni,value);
	}

	public String getDS_ILLUSTRAZIONI() {
		return DS_ILLUSTRAZIONI;
	}

	public void setNOTAZIONE_MUSICALE(String value) {
		this.NOTAZIONE_MUSICALE = value;
    this.settaParametro(KeyParameter.XXXnotazione_musicale,value);
	}

	public String getNOTAZIONE_MUSICALE() {
		return NOTAZIONE_MUSICALE;
	}

	public void setDS_LEGATURA(String value) {
		this.DS_LEGATURA = value;
    this.settaParametro(KeyParameter.XXXds_legatura,value);
	}

	public String getDS_LEGATURA() {
		return DS_LEGATURA;
	}

	public void setDS_CONSERVAZIONE(String value) {
		this.DS_CONSERVAZIONE = value;
    this.settaParametro(KeyParameter.XXXds_conservazione,value);
	}

	public String getDS_CONSERVAZIONE() {
		return DS_CONSERVAZIONE;
	}

	public void setTP_TESTO_LETTER(String value) {
		this.TP_TESTO_LETTER = value;
    this.settaParametro(KeyParameter.XXXtp_testo_letter,value);
	}

	public String getTP_TESTO_LETTER() {
		return TP_TESTO_LETTER;
	}

	public void setUTE_INS_M(String value) {
		this.UTE_INS_M = value;
    this.settaParametro(KeyParameter.XXXute_ins_m,value);
	}

	public String getUTE_INS_M() {
		return UTE_INS_M;
	}

	public void setTS_INS_M(java.util.Date value) {
		this.TS_INS_M = value;
    this.settaParametro(KeyParameter.XXXts_ins_m,value);
	}

	public java.util.Date getTS_INS_M() {
		return TS_INS_M;
	}

	public void setUTE_VAR_M(String value) {
		this.UTE_VAR_M = value;
    this.settaParametro(KeyParameter.XXXute_var_m,value);
	}

	public String getUTE_VAR_M() {
		return UTE_VAR_M;
	}

	public void setTS_VAR_M(java.util.Date value) {
		this.TS_VAR_M = value;
    this.settaParametro(KeyParameter.XXXts_var_m,value);
	}

	public java.util.Date getTS_VAR_M() {
		return TS_VAR_M;
	}

	public void setFL_CANC_M(String value) {
		this.FL_CANC_M = value;
    this.settaParametro(KeyParameter.XXXfl_canc_m,value);
	}

 public String getFL_CANC_M() {
		return FL_CANC_M;
	}

	public String toString() {
		return String.valueOf(getBID() + " " + getBID_BASE() + " " + getTP_LEGAME());
	}

}
