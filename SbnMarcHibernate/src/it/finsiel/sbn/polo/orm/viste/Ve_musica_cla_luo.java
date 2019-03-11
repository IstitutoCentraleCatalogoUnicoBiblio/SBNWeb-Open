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
public class Ve_musica_cla_luo extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -709059878691661530L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_musica_cla_luo))
			return false;
		Ve_musica_cla_luo ve_musica_cla_luo = (Ve_musica_cla_luo)aObj;
		if (getCD_SISTEMA() != ve_musica_cla_luo.getCD_SISTEMA())
			return false;
		if (getCD_EDIZIONE() != ve_musica_cla_luo.getCD_EDIZIONE())
			return false;
		if (getCLASSE() != null && !getCLASSE().equals(ve_musica_cla_luo.getCLASSE()))
			return false;
		if (getBID() != null && !getBID().equals(ve_musica_cla_luo.getBID()))
			return false;
		if (getLID() != null && !getLID().equals(ve_musica_cla_luo.getLID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCD_SISTEMA().hashCode();
		hashcode = hashcode + getCD_EDIZIONE().hashCode();
		hashcode = hashcode + getCLASSE().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getLID().hashCode();
		return hashcode;
	}

	private String CD_SISTEMA;

	private String CD_EDIZIONE;

	private String CLASSE;

	private String CD_LIVELLO_M;

	private String DS_ORG_SINT;

	private String DS_ORG_ANAL;

	private String TP_ELABORAZIONE;

	private String CD_STESURA;

	private String FL_COMPOSITO;

	private String FL_PALINSESTO;

	private String DATAZIONE;

	private String CD_PRESENTAZIONE;

	private String CD_MATERIA;

	private String DS_ILLUSTRAZIONI;

	private String NOTAZIONE_MUSICALE;

	private String DS_LEGATURA;

	private String DS_CONSERVAZIONE;

	private String TP_TESTO_LETTER;

	private String LID;

	private String KY_NORM_LUOGO;

	public void setCD_SISTEMA(String value) {
		this.CD_SISTEMA = value;
    this.settaParametro(KeyParameter.XXXcd_sistema,value);
	}

	public String getCD_SISTEMA() {
		return CD_SISTEMA;
	}

	public void setCD_EDIZIONE(String value) {
		this.CD_EDIZIONE = value;
    this.settaParametro(KeyParameter.XXXcd_edizione,value);
	}

	public String getCD_EDIZIONE() {
		return CD_EDIZIONE;
	}

	public void setCLASSE(String value) {
		this.CLASSE = value;
    this.settaParametro(KeyParameter.XXXclasse,value);
	}

	public String getCLASSE() {
		return CLASSE;
	}

	public void setCD_LIVELLO_M(String value) {
		this.CD_LIVELLO_M = value;
    this.settaParametro(KeyParameter.XXXcd_livello_m,value);
	}

	public String getCD_LIVELLO_M() {
		return CD_LIVELLO_M;
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

	public void setDATAZIONE(String value) {
		this.DATAZIONE = value;
    this.settaParametro(KeyParameter.XXXdatazione,value);
	}

	public String getDATAZIONE() {
		return DATAZIONE;
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

	public void setLID(String value) {
		this.LID = value;
    this.settaParametro(KeyParameter.XXXlid,value);
	}

	public String getLID() {
		return LID;
	}

	public void setKY_NORM_LUOGO(String value) {
		this.KY_NORM_LUOGO = value;
    this.settaParametro(KeyParameter.XXXky_norm_luogo,value);
	}

 public String getKY_NORM_LUOGO() {
		return KY_NORM_LUOGO;
	}

	public String toString() {
		return String.valueOf(getCD_SISTEMA() + " " + getCD_EDIZIONE() + " " + getCLASSE() + " " + getBID() + " " + getLID());
	}

}
