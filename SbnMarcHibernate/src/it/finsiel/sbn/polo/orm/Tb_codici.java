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

public class Tb_codici extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 4752248109516140495L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_codici))
			return false;
		Tb_codici tb_codici = (Tb_codici)aObj;
		if (getTP_TABELLA() != null && !getTP_TABELLA().equals(tb_codici.getTP_TABELLA()))
			return false;
		if (getCD_TABELLA() != null && !getCD_TABELLA().equals(tb_codici.getCD_TABELLA()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getTP_TABELLA().hashCode();
		hashcode = hashcode + getCD_TABELLA().hashCode();
		return hashcode;
	}

	private String TP_TABELLA;

	private String CD_TABELLA;

	private String DS_TABELLA;

	private String CD_UNIMARC;

	private String CD_MARC_21;

	private String TP_MATERIALE;

	private java.util.Date DT_FINE_VALIDITA;

	private String ds_cdsbn_ulteriore;

	private String cd_flg1;

	private String cd_flg2;

	private String cd_flg3;

	private String cd_flg4;

	private String cd_flg5;

	private String cd_flg6;

	private String cd_flg7;

	private String cd_flg8;

	private String cd_flg9;

	private String cd_flg10;

	private String cd_flg11;

	public void setTP_TABELLA(String value) {
		this.TP_TABELLA = value;
    this.settaParametro(KeyParameter.XXXtp_tabella,value);
	}

	public String getTP_TABELLA() {
		return TP_TABELLA;
	}

	public void setCD_TABELLA(String value) {
		this.CD_TABELLA = value;
    this.settaParametro(KeyParameter.XXXcd_tabella,value);
	}

	public String getCD_TABELLA() {
		return CD_TABELLA;
	}

	public void setDS_TABELLA(String value) {
		this.DS_TABELLA = value;
    this.settaParametro(KeyParameter.XXXds_tabella,value);
	}

	public String getDS_TABELLA() {
		return DS_TABELLA;
	}

	public void setCD_UNIMARC(String value) {
		this.CD_UNIMARC = value;
    this.settaParametro(KeyParameter.XXXcd_unimarc,value);
	}

	public String getCD_UNIMARC() {
		return CD_UNIMARC;
	}

	public void setCD_MARC_21(String value) {
		this.CD_MARC_21 = value;
    this.settaParametro(KeyParameter.XXXcd_marc_21,value);
	}

	public String getCD_MARC_21() {
		return CD_MARC_21;
	}

	public void setTP_MATERIALE(String value) {
		this.TP_MATERIALE = value;
    this.settaParametro(KeyParameter.XXXtp_materiale,value);
	}

	public String getTP_MATERIALE() {
		return TP_MATERIALE;
	}

	public void setDT_FINE_VALIDITA(java.util.Date value) {
		this.DT_FINE_VALIDITA = value;
    this.settaParametro(KeyParameter.XXXdt_fine_validita,value);
	}

 public java.util.Date getDT_FINE_VALIDITA() {
		return DT_FINE_VALIDITA;
	}

	public String toString() {
		return String.valueOf(getTP_TABELLA() + " " + getCD_TABELLA());
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

	public String getDs_cdsbn_ulteriore() {
		return ds_cdsbn_ulteriore;
	}

	public void setDs_cdsbn_ulteriore(String ds_cdsbn_ulteriore) {
		this.ds_cdsbn_ulteriore = ds_cdsbn_ulteriore;
	}

	public String getCd_flg1() {
		return cd_flg1;
	}

	public void setCd_flg1(String cd_flg1) {
		this.cd_flg1 = cd_flg1;
	}

	public String getCd_flg2() {
		return cd_flg2;
	}

	public void setCd_flg2(String cd_flg2) {
		this.cd_flg2 = cd_flg2;
	}

	public String getCd_flg3() {
		return cd_flg3;
	}

	public void setCd_flg3(String cd_flg3) {
		this.cd_flg3 = cd_flg3;
	}

	public String getCd_flg4() {
		return cd_flg4;
	}

	public void setCd_flg4(String cd_flg4) {
		this.cd_flg4 = cd_flg4;
	}

	public String getCd_flg5() {
		return cd_flg5;
	}

	public void setCd_flg5(String cd_flg5) {
		this.cd_flg5 = cd_flg5;
	}

	public String getCd_flg6() {
		return cd_flg6;
	}

	public void setCd_flg6(String cd_flg6) {
		this.cd_flg6 = cd_flg6;
	}

	public String getCd_flg7() {
		return cd_flg7;
	}

	public void setCd_flg7(String cd_flg7) {
		this.cd_flg7 = cd_flg7;
	}

	public String getCd_flg8() {
		return cd_flg8;
	}

	public void setCd_flg8(String cd_flg8) {
		this.cd_flg8 = cd_flg8;
	}

	public String getCd_flg9() {
		return cd_flg9;
	}

	public void setCd_flg9(String cd_flg9) {
		this.cd_flg9 = cd_flg9;
	}

	public String getCd_flg10() {
		return cd_flg10;
	}

	public void setCd_flg10(String cd_flg10) {
		this.cd_flg10 = cd_flg10;
	}

	public String getCd_flg11() {
		return cd_flg11;
	}

	public void setCd_flg11(String cd_flg11) {
		this.cd_flg11 = cd_flg11;
	}

	public String getCd_tabellaTrim() {
		return (CD_TABELLA != null) ? CD_TABELLA.trim() : null;
	}


}
