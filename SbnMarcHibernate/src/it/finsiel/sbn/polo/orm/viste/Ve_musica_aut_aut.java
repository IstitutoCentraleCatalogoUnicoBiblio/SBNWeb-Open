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
public class Ve_musica_aut_aut extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = 1982077389360054603L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_musica_aut_aut))
			return false;
		Ve_musica_aut_aut ve_musica_aut_aut = (Ve_musica_aut_aut)aObj;
		if (getVID() != null && !getVID().equals(ve_musica_aut_aut.getVID()))
			return false;
		if (getTP_RESPONSABILITA() != ve_musica_aut_aut.getTP_RESPONSABILITA())
			return false;
		if (getCD_RELAZIONE() != null && !getCD_RELAZIONE().equals(ve_musica_aut_aut.getCD_RELAZIONE()))
			return false;
		if (getBID() != null && !getBID().equals(ve_musica_aut_aut.getBID()))
			return false;
		if (getTP_RESPONSABILITA_F() != ve_musica_aut_aut.getTP_RESPONSABILITA_F())
			return false;
		if (getCD_RELAZIONE_F() != null && !getCD_RELAZIONE_F().equals(ve_musica_aut_aut.getCD_RELAZIONE_F()))
			return false;
		if (getVID_F() != null && !getVID_F().equals(ve_musica_aut_aut.getVID_F()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getVID().hashCode();
		hashcode = hashcode + getTP_RESPONSABILITA().hashCode();
		hashcode = hashcode + getCD_RELAZIONE().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getTP_RESPONSABILITA_F().hashCode();
		hashcode = hashcode + getCD_RELAZIONE_F().hashCode();
		hashcode = hashcode + getVID_F().hashCode();
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

	private String TP_RESPONSABILITA_F;

	private String CD_RELAZIONE_F;

	private String FL_SUPERFLUO_F;

	private String VID_F;

	private String KY_CAUTUN_F;

	private String KY_AUTEUR_F;

	private String KY_CLES1_A_F;

	private String KY_CLES2_A_F;

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

	public void setTP_RESPONSABILITA_F(String value) {
		this.TP_RESPONSABILITA_F = value;
    this.settaParametro(KeyParameter.XXXtp_responsabilita_f,value);
	}

	public String getTP_RESPONSABILITA_F() {
		return TP_RESPONSABILITA_F;
	}

	public void setCD_RELAZIONE_F(String value) {
		this.CD_RELAZIONE_F = value;
    this.settaParametro(KeyParameter.XXXcd_relazione_f,value);
	}

	public String getCD_RELAZIONE_F() {
		return CD_RELAZIONE_F;
	}

	public void setFL_SUPERFLUO_F(String value) {
		this.FL_SUPERFLUO_F = value;
    this.settaParametro(KeyParameter.XXXfl_superfluo_f,value);
	}

	public String getFL_SUPERFLUO_F() {
		return FL_SUPERFLUO_F;
	}

	public void setVID_F(String value) {
		this.VID_F = value;
    this.settaParametro(KeyParameter.XXXvid_f,value);
	}

	public String getVID_F() {
		return VID_F;
	}

	public void setKY_CAUTUN_F(String value) {
		this.KY_CAUTUN_F = value;
    this.settaParametro(KeyParameter.XXXky_cautun_f,value);
	}

	public String getKY_CAUTUN_F() {
		return KY_CAUTUN_F;
	}

	public void setKY_AUTEUR_F(String value) {
		this.KY_AUTEUR_F = value;
    this.settaParametro(KeyParameter.XXXky_auteur_f,value);
	}

	public String getKY_AUTEUR_F() {
		return KY_AUTEUR_F;
	}

	public void setKY_CLES1_A_F(String value) {
		this.KY_CLES1_A_F = value;
    this.settaParametro(KeyParameter.XXXky_cles1_a_f,value);
	}

	public String getKY_CLES1_A_F() {
		return KY_CLES1_A_F;
	}

	public void setKY_CLES2_A_F(String value) {
		this.KY_CLES2_A_F = value;
    this.settaParametro(KeyParameter.XXXky_cles2_a_f,value);
	}


 public String getKY_CLES2_A_F() {
		return KY_CLES2_A_F;
	}

	public String toString() {
		return String.valueOf(getVID() + " " + getTP_RESPONSABILITA() + " " + getCD_RELAZIONE() + " " + getBID() + " " + getTP_RESPONSABILITA_F() + " " + getCD_RELAZIONE_F() + " " + getVID_F());
	}

}
