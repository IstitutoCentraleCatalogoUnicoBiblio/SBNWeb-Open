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
public class V_composizione  extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -3721151258933243039L;

	private String DS_ORG_SINT;

	private String DS_ORG_ANAL;

	private String NUMERO_ORDINE;

	private String NUMERO_OPERA;

	private String NUMERO_CAT_TEM;

	private String CD_TONALITA;

	private String DATAZIONE;

	private String KY_ORD_RIC;

	private String KY_EST_RIC;

	private String KY_APP_RIC;

	private String KY_ORD_CLET;

	private String KY_EST_CLET;

	private String KY_APP_CLET;

	private String KY_ORD_PRE;

	private String KY_EST_PRE;

	private String KY_APP_PRE;

	private String KY_ORD_DEN;

	private String KY_EST_DEN;

	private String KY_APP_DEN;

	private String KY_ORD_NOR_PRE;

	private String KY_EST_NOR_PRE;

	private String KY_APP_NOR_PRE;

	private String CD_FORMA_1;

	private String CD_FORMA_2;

	private String CD_FORMA_3;

	private String AA_COMP_1;

	private String AA_COMP_2;

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

	public void setCD_TONALITA(String value) {
		this.CD_TONALITA = value;
    this.settaParametro(KeyParameter.XXXcd_tonalita,value);
	}

	public String getCD_TONALITA() {
		return CD_TONALITA;
	}

	public void setDATAZIONE(String value) {
		this.DATAZIONE = value;
    this.settaParametro(KeyParameter.XXXdatazione,value);
	}

	public String getDATAZIONE() {
		return DATAZIONE;
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

	public void setKY_APP_RIC(String value) {
		this.KY_APP_RIC = value;
    this.settaParametro(KeyParameter.XXXky_app_ric,value);
	}

	public String getKY_APP_RIC() {
		return KY_APP_RIC;
	}

	public void setKY_ORD_CLET(String value) {
		this.KY_ORD_CLET = value;
    this.settaParametro(KeyParameter.XXXky_ord_clet,value);
	}

	public String getKY_ORD_CLET() {
		return KY_ORD_CLET;
	}

	public void setKY_EST_CLET(String value) {
		this.KY_EST_CLET = value;
    this.settaParametro(KeyParameter.XXXky_est_clet,value);
	}

	public String getKY_EST_CLET() {
		return KY_EST_CLET;
	}

	public void setKY_APP_CLET(String value) {
		this.KY_APP_CLET = value;
    this.settaParametro(KeyParameter.XXXky_app_clet,value);
	}

	public String getKY_APP_CLET() {
		return KY_APP_CLET;
	}

	public void setKY_ORD_PRE(String value) {
		this.KY_ORD_PRE = value;
    this.settaParametro(KeyParameter.XXXky_ord_pre,value);
	}

	public String getKY_ORD_PRE() {
		return KY_ORD_PRE;
	}

	public void setKY_EST_PRE(String value) {
		this.KY_EST_PRE = value;
    this.settaParametro(KeyParameter.XXXky_est_pre,value);
	}

	public String getKY_EST_PRE() {
		return KY_EST_PRE;
	}

	public void setKY_APP_PRE(String value) {
		this.KY_APP_PRE = value;
    this.settaParametro(KeyParameter.XXXky_app_pre,value);
	}

	public String getKY_APP_PRE() {
		return KY_APP_PRE;
	}

	public void setKY_ORD_DEN(String value) {
		this.KY_ORD_DEN = value;
    this.settaParametro(KeyParameter.XXXky_ord_den,value);
	}

	public String getKY_ORD_DEN() {
		return KY_ORD_DEN;
	}

	public void setKY_EST_DEN(String value) {
		this.KY_EST_DEN = value;
    this.settaParametro(KeyParameter.XXXky_est_den,value);
	}

	public String getKY_EST_DEN() {
		return KY_EST_DEN;
	}

	public void setKY_APP_DEN(String value) {
		this.KY_APP_DEN = value;
    this.settaParametro(KeyParameter.XXXky_app_den,value);
	}

	public String getKY_APP_DEN() {
		return KY_APP_DEN;
	}

	public void setKY_ORD_NOR_PRE(String value) {
		this.KY_ORD_NOR_PRE = value;
    this.settaParametro(KeyParameter.XXXky_ord_nor_pre,value);
	}

	public String getKY_ORD_NOR_PRE() {
		return KY_ORD_NOR_PRE;
	}

	public void setKY_EST_NOR_PRE(String value) {
		this.KY_EST_NOR_PRE = value;
    this.settaParametro(KeyParameter.XXXky_est_nor_pre,value);
	}

	public String getKY_EST_NOR_PRE() {
		return KY_EST_NOR_PRE;
	}

	public void setKY_APP_NOR_PRE(String value) {
		this.KY_APP_NOR_PRE = value;
    this.settaParametro(KeyParameter.XXXky_app_nor_pre,value);
	}

	public String getKY_APP_NOR_PRE() {
		return KY_APP_NOR_PRE;
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

	public void setAA_COMP_1(String value) {
		this.AA_COMP_1 = value;
    this.settaParametro(KeyParameter.XXXaa_comp_1,value);
	}

	public String getAA_COMP_1() {
		return AA_COMP_1;
	}

	public void setAA_COMP_2(String value) {
		this.AA_COMP_2 = value;
    this.settaParametro(KeyParameter.XXXaa_comp_2,value);
	}

	public String getAA_COMP_2() {
		return AA_COMP_2;
	}

	public String toString() {
		return String.valueOf(getBID());
	}

}
