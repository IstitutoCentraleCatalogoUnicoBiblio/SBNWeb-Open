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
public class Ve_musica_luo_com extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = 7526417525725791504L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_musica_luo_com))
			return false;
		Ve_musica_luo_com ve_musica_luo_com = (Ve_musica_luo_com)aObj;
		if (getLID() != null && !getLID().equals(ve_musica_luo_com.getLID()))
			return false;
		if (getTP_LUOGO() != ve_musica_luo_com.getTP_LUOGO())
			return false;
		if (getBID() != null && !getBID().equals(ve_musica_luo_com.getBID()))
			return false;
		if (getBID_CM() != null && !getBID_CM().equals(ve_musica_luo_com.getBID_CM()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getLID().hashCode();
		hashcode = hashcode + getTP_LUOGO().hashCode();
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getBID_CM().hashCode();
		return hashcode;
	}

	private String LID;

	private String TP_LUOGO;

	private String NOTA_TIT_LUO;

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

	private String BID_CM;

	private String DS_ORG_SINT_CM;

	private String DS_ORG_ANAL_CM;

	private String CD_FORMA_1;

	private String CD_FORMA_2;

	private String CD_FORMA_3;

	private String NUMERO_ORDINE;

	private String NUMERO_OPERA;

	private String NUMERO_CAT_TEM;

	private String DATAZIONE_CM;

	private String KY_APP_RIC;

	private String KY_ORD_RIC;

	private String KY_EST_RIC;

	public void setLID(String value) {
		this.LID = value;
    this.settaParametro(KeyParameter.XXXlid,value);
	}

	public String getLID() {
		return LID;
	}

	public void setTP_LUOGO(String value) {
		this.TP_LUOGO = value;
    this.settaParametro(KeyParameter.XXXtp_luogo,value);
	}

	public String getTP_LUOGO() {
		return TP_LUOGO;
	}

	public void setNOTA_TIT_LUO(String value) {
		this.NOTA_TIT_LUO = value;
    this.settaParametro(KeyParameter.XXXnota_tit_luo,value);
	}

	public String getNOTA_TIT_LUO() {
		return NOTA_TIT_LUO;
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

	public void setBID_CM(String value) {
		this.BID_CM = value;
    this.settaParametro(KeyParameter.XXXbid_cm,value);
	}

	public String getBID_CM() {
		return BID_CM;
	}

	public void setDS_ORG_SINT_CM(String value) {
		this.DS_ORG_SINT_CM = value;
    this.settaParametro(KeyParameter.XXXds_org_sint_cm,value);
	}

	public String getDS_ORG_SINT_CM() {
		return DS_ORG_SINT_CM;
	}

	public void setDS_ORG_ANAL_CM(String value) {
		this.DS_ORG_ANAL_CM = value;
    this.settaParametro(KeyParameter.XXXds_org_anal_cm,value);
	}

	public String getDS_ORG_ANAL_CM() {
		return DS_ORG_ANAL_CM;
	}

	public void setCD_FORMA_1(String value) {
		this.CD_FORMA_1 = value;
    this.settaParametro(KeyParameter.XXXcd_forma_1,value);
	}

	public String getCD_FORMA_1() {
		return CD_FORMA_1;
	}

	public void setCD_FORMA_2(String value) {
		this.CD_FORMA_2 = value;
    this.settaParametro(KeyParameter.XXXcd_forma_2,value);
	}

	public String getCD_FORMA_2() {
		return CD_FORMA_2;
	}

	public void setCD_FORMA_3(String value) {
		this.CD_FORMA_3 = value;
    this.settaParametro(KeyParameter.XXXcd_forma_3,value);
	}

	public String getCD_FORMA_3() {
		return CD_FORMA_3;
	}

	public void setNUMERO_ORDINE(String value) {
		this.NUMERO_ORDINE = value;
    this.settaParametro(KeyParameter.XXXnumero_ordine,value);
	}

	public String getNUMERO_ORDINE() {
		return NUMERO_ORDINE;
	}

	public void setNUMERO_OPERA(String value) {
		this.NUMERO_OPERA = value;
    this.settaParametro(KeyParameter.XXXnumero_opera,value);
	}

	public String getNUMERO_OPERA() {
		return NUMERO_OPERA;
	}

	public void setNUMERO_CAT_TEM(String value) {
		this.NUMERO_CAT_TEM = value;
    this.settaParametro(KeyParameter.XXXnumero_cat_tem,value);
	}

	public String getNUMERO_CAT_TEM() {
		return NUMERO_CAT_TEM;
	}

	public void setDATAZIONE_CM(String value) {
		this.DATAZIONE_CM = value;
    this.settaParametro(KeyParameter.XXXdatazione_cm,value);
	}

	public String getDATAZIONE_CM() {
		return DATAZIONE_CM;
	}

	public void setKY_APP_RIC(String value) {
		this.KY_APP_RIC = value;
    this.settaParametro(KeyParameter.XXXky_app_ric,value);
	}

	public String getKY_APP_RIC() {
		return KY_APP_RIC;
	}

	public void setKY_ORD_RIC(String value) {
		this.KY_ORD_RIC = value;
    this.settaParametro(KeyParameter.XXXky_ord_ric,value);
	}

	public String getKY_ORD_RIC() {
		return KY_ORD_RIC;
	}

	public void setKY_EST_RIC(String value) {
		this.KY_EST_RIC = value;
    this.settaParametro(KeyParameter.XXXky_est_ric,value);
	}


 public String getKY_EST_RIC() {
		return KY_EST_RIC;
	}

	public String toString() {
		return String.valueOf(getLID() + " " + getTP_LUOGO() + " " + getBID() + " " + getBID_CM());
	}

}
