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
public class Vf_titolo_com extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = -2956650739256972787L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vf_titolo_com))
			return false;
		Vf_titolo_com vf_titolo_com = (Vf_titolo_com)aObj;
		if (getBID_COM() != null && !getBID_COM().equals(vf_titolo_com.getBID_COM()))
			return false;
		if (getBID() != null && !getBID().equals(vf_titolo_com.getBID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID_COM().hashCode();
		hashcode = hashcode + getBID().hashCode();
		return hashcode;
	}

	private String BID_COM;

	private String CD_FORMA_1;

	private String CD_FORMA_2;

	private String CD_FORMA_3;

	private String NUMERO_ORDINE;

	private String NUMERO_OPERA;

	private String NUMERO_CAT_TEM;

	private String CD_TONALITA;

	private String DATAZIONE;

	private String KY_ORD_RIC;

	private String KY_EST_RIC;

	private String KY_APP_RIC;

	private String TP_LEGAME_MUSICA;

	private String DS_ORG_SINT;

	private String DS_ORG_ANAL;

	public void setBID_COM(String value) {
		this.BID_COM = value;
    this.settaParametro(KeyParameter.XXXbid_com,value);
	}

	public String getBID_COM() {
		return BID_COM;
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

	public void setTP_LEGAME_MUSICA(String value) {
		this.TP_LEGAME_MUSICA = value;
    this.settaParametro(KeyParameter.XXXtp_legame_musica,value);
	}

	public String getTP_LEGAME_MUSICA() {
		return TP_LEGAME_MUSICA;
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

	public String toString() {
		return String.valueOf(getBID_COM() + " " + getBID());
	}

}
