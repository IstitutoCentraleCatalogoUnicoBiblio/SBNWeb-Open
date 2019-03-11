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

import java.util.HashSet;
/**
 * ORM-Persistable Class
 */
public class Tb_titolo extends OggettoServerSbnMarc {

	private static final long serialVersionUID = -5216194930520081468L;

	private String BID;

	private String ISADN;

	private String TP_MATERIALE;

	private String TP_RECORD_UNI;

	private String CD_NATURA;

	private String CD_PAESE;

	private String CD_LINGUA_1;

	private String CD_LINGUA_2;

	private String CD_LINGUA_3;

	private String AA_PUBB_1;

	private String AA_PUBB_2;

	private String TP_AA_PUBB;

	private String CD_GENERE_1;

	private String CD_GENERE_2;

	private String CD_GENERE_3;

	private String CD_GENERE_4;

	private String KY_CLES1_T;

	private String KY_CLES2_T;

	private String KY_CLET1_T;

	private String KY_CLET2_T;

	private String KY_CLES1_CT;

	private String KY_CLES2_CT;

	private String KY_CLET1_CT;

	private String KY_CLET2_CT;

	private String CD_LIVELLO;

	private String FL_SPECIALE;

	private String ISBD;

	private String INDICE_ISBD;

	private String KY_EDITORE;

	private String CD_AGENZIA;

	private String CD_NORME_CAT;

	private String NOTA_INF_TIT;

	private String NOTA_CAT_TIT;

	private String BID_LINK;

	private String TP_LINK;

	private String UTE_FORZA_INS;

	private String UTE_FORZA_VAR;

	private java.util.Set TR_TIT_BIB = new HashSet();

//  CAMPI NUOVA ISTANZA
	private String CD_PERIODICITA;

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
	}

	public void setISADN(String value) {
		this.ISADN = value;
    this.settaParametro(KeyParameter.XXXisadn,value);
	}

	public String getISADN() {
		return ISADN;
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

	public void setCD_PAESE(String value) {
		this.CD_PAESE = value;
    this.settaParametro(KeyParameter.XXXcd_paese,value);
	}

	public String getCD_PAESE() {
		return CD_PAESE;
	}

	public void setCD_LINGUA_1(String value) {
		this.CD_LINGUA_1 = value;
    this.settaParametro(KeyParameter.XXXcd_lingua_1,value);
	}

	public String getCD_LINGUA_1() {
		return CD_LINGUA_1;
	}

	public void setCD_LINGUA_2(String value) {
		this.CD_LINGUA_2 = value;
    this.settaParametro(KeyParameter.XXXcd_lingua_2,value);
	}

	public String getCD_LINGUA_2() {
		return CD_LINGUA_2;
	}

	public void setCD_LINGUA_3(String value) {
		this.CD_LINGUA_3 = value;
    this.settaParametro(KeyParameter.XXXcd_lingua_3,value);
	}

	public String getCD_LINGUA_3() {
		return CD_LINGUA_3;
	}

	public void setAA_PUBB_1(String value) {
		this.AA_PUBB_1 = value;
    this.settaParametro(KeyParameter.XXXaa_pubb_1,value);
	}

	public String getAA_PUBB_1() {
		return AA_PUBB_1;
	}

	public void setAA_PUBB_2(String value) {
		this.AA_PUBB_2 = value;
    this.settaParametro(KeyParameter.XXXaa_pubb_2,value);
	}

	public String getAA_PUBB_2() {
		return AA_PUBB_2;
	}

	public void setTP_AA_PUBB(String value) {
		this.TP_AA_PUBB = value;
    this.settaParametro(KeyParameter.XXXtp_aa_pubb,value);
	}

	public String getTP_AA_PUBB() {
		return TP_AA_PUBB;
	}

	public void setCD_GENERE_1(String value) {
		this.CD_GENERE_1 = value;
    this.settaParametro(KeyParameter.XXXcd_genere_1,value);
	}

	public String getCD_GENERE_1() {
		return CD_GENERE_1;
	}

	public void setCD_GENERE_2(String value) {
		this.CD_GENERE_2 = value;
    this.settaParametro(KeyParameter.XXXcd_genere_2,value);
	}

	public String getCD_GENERE_2() {
		return CD_GENERE_2;
	}

	public void setCD_GENERE_3(String value) {
		this.CD_GENERE_3 = value;
    this.settaParametro(KeyParameter.XXXcd_genere_3,value);
	}

	public String getCD_GENERE_3() {
		return CD_GENERE_3;
	}

	public void setCD_GENERE_4(String value) {
		this.CD_GENERE_4 = value;
    this.settaParametro(KeyParameter.XXXcd_genere_4,value);
	}

	public String getCD_GENERE_4() {
		return CD_GENERE_4;
	}

	public void setKY_CLES1_T(String value) {
		this.KY_CLES1_T = value;
    this.settaParametro(KeyParameter.XXXky_cles1_t,value);
	}

	public String getKY_CLES1_T() {
		return KY_CLES1_T;
	}

	public void setKY_CLES2_T(String value) {
		this.KY_CLES2_T = value;
    this.settaParametro(KeyParameter.XXXky_cles2_t,value);
	}

	public String getKY_CLES2_T() {
		return KY_CLES2_T;
	}

	public void setKY_CLET1_T(String value) {
		this.KY_CLET1_T = value;
    this.settaParametro(KeyParameter.XXXky_clet1_t,value);
	}

	public String getKY_CLET1_T() {
		return KY_CLET1_T;
	}

	public void setKY_CLET2_T(String value) {
		this.KY_CLET2_T = value;
    this.settaParametro(KeyParameter.XXXky_clet2_t,value);
	}

	public String getKY_CLET2_T() {
		return KY_CLET2_T;
	}

	public void setKY_CLES1_CT(String value) {
		this.KY_CLES1_CT = value;
    this.settaParametro(KeyParameter.XXXky_cles1_ct,value);
	}

	public String getKY_CLES1_CT() {
		return KY_CLES1_CT;
	}

	public void setKY_CLES2_CT(String value) {
		this.KY_CLES2_CT = value;
    this.settaParametro(KeyParameter.XXXky_cles2_ct,value);
	}

	public String getKY_CLES2_CT() {
		return KY_CLES2_CT;
	}

	public void setKY_CLET1_CT(String value) {
		this.KY_CLET1_CT = value;
    this.settaParametro(KeyParameter.XXXky_clet1_ct,value);
	}

	public String getKY_CLET1_CT() {
		return KY_CLET1_CT;
	}

	public void setKY_CLET2_CT(String value) {
		this.KY_CLET2_CT = value;
    this.settaParametro(KeyParameter.XXXky_clet2_ct,value);
	}

	public String getKY_CLET2_CT() {
		return KY_CLET2_CT;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
    this.settaParametro(KeyParameter.XXXcd_livello,value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setFL_SPECIALE(String value) {
		this.FL_SPECIALE = value;
    this.settaParametro(KeyParameter.XXXfl_speciale,value);
	}

	public String getFL_SPECIALE() {
		return FL_SPECIALE;
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

	public void setKY_EDITORE(String value) {
		this.KY_EDITORE = value;
    this.settaParametro(KeyParameter.XXXky_editore,value);
	}

	public String getKY_EDITORE() {
		return KY_EDITORE;
	}

	public void setCD_AGENZIA(String value) {
		this.CD_AGENZIA = value;
    this.settaParametro(KeyParameter.XXXcd_agenzia,value);
	}

	public String getCD_AGENZIA() {
		return CD_AGENZIA;
	}

	public void setCD_NORME_CAT(String value) {
		this.CD_NORME_CAT = value;
    this.settaParametro(KeyParameter.XXXcd_norme_cat,value);
	}

	public String getCD_NORME_CAT() {
		return CD_NORME_CAT;
	}

	public void setNOTA_INF_TIT(String value) {
		this.NOTA_INF_TIT = value;
    this.settaParametro(KeyParameter.XXXnota_inf_tit,value);
	}

	public String getNOTA_INF_TIT() {
		return NOTA_INF_TIT;
	}

	public void setNOTA_CAT_TIT(String value) {
		this.NOTA_CAT_TIT = value;
    this.settaParametro(KeyParameter.XXXnota_cat_tit,value);
	}

	public String getNOTA_CAT_TIT() {
		return NOTA_CAT_TIT;
	}

	public void setBID_LINK(String value) {
		this.BID_LINK = value;
    this.settaParametro(KeyParameter.XXXbid_link,value);
	}

	public String getBID_LINK() {
		return BID_LINK;
	}

	public void setTP_LINK(String value) {
		this.TP_LINK = value;
    this.settaParametro(KeyParameter.XXXtp_link,value);
	}

	public String getTP_LINK() {
		return TP_LINK;
	}

	public void setUTE_FORZA_INS(String value) {
		this.UTE_FORZA_INS = value;
    this.settaParametro(KeyParameter.XXXute_forza_ins,value);
	}

	public String getUTE_FORZA_INS() {
		return UTE_FORZA_INS;
	}

	public void setUTE_FORZA_VAR(String value) {
		this.UTE_FORZA_VAR = value;
    this.settaParametro(KeyParameter.XXXute_forza_var,value);
	}

	public String getUTE_FORZA_VAR() {
		return UTE_FORZA_VAR;
	}

	public String toString() {
		return String.valueOf(getBID());
	}

    public java.util.Set getTR_TIT_BIB() {
        return TR_TIT_BIB;
    }

    public void setTR_TIT_BIB(java.util.Set tr_tit_bib) {
        TR_TIT_BIB = tr_tit_bib;
    }
	public String getCD_PERIODICITA() {
		return CD_PERIODICITA;
	}
	public void setCD_PERIODICITA(String value) {
		this.CD_PERIODICITA = value;
		this.settaParametro(KeyParameter.XXXcd_periodicita,value);
	}

	//almaviva5_20091008
	public void setFILTRO_LOC_BIB(Object[] locBib) {
		if (locBib != null && locBib.length > 0)
			this.settaParametro(KeyParameter.XXXcercaTitLocBib, locBib);
	}
}
