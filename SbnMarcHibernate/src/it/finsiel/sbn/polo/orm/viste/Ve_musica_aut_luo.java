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
public class Ve_musica_aut_luo extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -8723183918425898959L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_musica_aut_luo))
			return false;
		Ve_musica_aut_luo ve_musica_aut_luo = (Ve_musica_aut_luo)aObj;
		if (getVID() != null && !getVID().equals(ve_musica_aut_luo.getVID()))
			return false;
		if (getTP_RESPONSABILITA() != ve_musica_aut_luo.getTP_RESPONSABILITA())
			return false;
		if (getCD_RELAZIONE() != null && !getCD_RELAZIONE().equals(ve_musica_aut_luo.getCD_RELAZIONE()))
			return false;
		if (getBID() != null && !getBID().equals(ve_musica_aut_luo.getBID()))
			return false;
		if (getLID() != null && !getLID().equals(ve_musica_aut_luo.getLID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getVID().hashCode();
		hashcode = hashcode + getTP_RESPONSABILITA().hashCode();
		hashcode = hashcode + getCD_RELAZIONE().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getLID().hashCode();
		return hashcode;
	}

	private String VID;

	private String TP_RESPONSABILITA;

	private String CD_RELAZIONE;

	private String NOTA_TIT_AUT;

	private String FL_INCERTO;

	private String FL_SUPERFLUO;

	private String CD_STRUMENTO_MUS;

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

	public void setVID(String value) {
		this.VID = value;
    this.settaParametro(KeyParameter.XXXvid,value);
	}

	public String getVID() {
		return VID;
	}

	public void setTP_RESPONSABILITA(String value) {
		this.TP_RESPONSABILITA = value;
    this.settaParametro(KeyParameter.XXXtp_responsabilita,value);
	}

	public String getTP_RESPONSABILITA() {
		return TP_RESPONSABILITA;
	}

	public void setCD_RELAZIONE(String value) {
		this.CD_RELAZIONE = value;
    this.settaParametro(KeyParameter.XXXcd_relazione,value);
	}

	public String getCD_RELAZIONE() {
		return CD_RELAZIONE;
	}

	public void setNOTA_TIT_AUT(String value) {
		this.NOTA_TIT_AUT = value;
    this.settaParametro(KeyParameter.XXXnota_tit_aut,value);
	}

	public String getNOTA_TIT_AUT() {
		return NOTA_TIT_AUT;
	}

	public void setFL_INCERTO(String value) {
		this.FL_INCERTO = value;
    this.settaParametro(KeyParameter.XXXfl_incerto,value);
	}

	public String getFL_INCERTO() {
		return FL_INCERTO;
	}

	public void setFL_SUPERFLUO(String value) {
		this.FL_SUPERFLUO = value;
    this.settaParametro(KeyParameter.XXXfl_superfluo,value);
	}

	public String getFL_SUPERFLUO() {
		return FL_SUPERFLUO;
	}

	public void setCD_STRUMENTO_MUS(String value) {
		this.CD_STRUMENTO_MUS = value;
    this.settaParametro(KeyParameter.XXXcd_strumento_mus,value);
	}

	public String getCD_STRUMENTO_MUS() {
		return CD_STRUMENTO_MUS;
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
		return String.valueOf(getVID() + " " + getTP_RESPONSABILITA() + " " + getCD_RELAZIONE() + " " + getBID() + " " + getLID());
	}

}
