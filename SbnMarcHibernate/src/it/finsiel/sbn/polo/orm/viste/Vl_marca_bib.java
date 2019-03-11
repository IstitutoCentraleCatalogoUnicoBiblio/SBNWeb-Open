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
import it.finsiel.sbn.polo.orm.Tb_marca;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_marca_bib extends Tb_marca implements Serializable {

	private static final long serialVersionUID = -7520261148114364335L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_marca_bib))
			return false;
		Vl_marca_bib vl_marca_bib = (Vl_marca_bib)aObj;
		if (getMID() != null && !getMID().equals(vl_marca_bib.getMID()))
			return false;
		if (getCD_POLO() != null && !getCD_POLO().equals(vl_marca_bib.getCD_POLO()))
			return false;
		if (getCD_BIBLIOTECA() != null && !getCD_BIBLIOTECA().equals(vl_marca_bib.getCD_BIBLIOTECA()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getMID().hashCode();
		hashcode = hashcode + getCD_POLO().hashCode();
		hashcode = hashcode + getCD_BIBLIOTECA().hashCode();
		return hashcode;
	}

	private String CD_POLO;

	private String CD_BIBLIOTECA;

	private String FL_ALLINEA;

	private String KY_BIBLIOTECA;

	private String CD_ANA_BIBLIOTECA;

	private String DS_BIBLIOTECA;

	private String DS_CITTA;

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

	public void setFL_ALLINEA(String value) {
		this.FL_ALLINEA = value;
    this.settaParametro(KeyParameter.XXXfl_allinea,value);
	}

	public String getFL_ALLINEA() {
		return FL_ALLINEA;
	}

	public void setKY_BIBLIOTECA(String value) {
		this.KY_BIBLIOTECA = value;
    this.settaParametro(KeyParameter.XXXky_biblioteca,value);
	}

	public String getKY_BIBLIOTECA() {
		return KY_BIBLIOTECA;
	}

	public void setCD_ANA_BIBLIOTECA(String value) {
		this.CD_ANA_BIBLIOTECA = value;
    this.settaParametro(KeyParameter.XXXcd_ana_biblioteca,value);
	}

	public String getCD_ANA_BIBLIOTECA() {
		return CD_ANA_BIBLIOTECA;
	}

	public void setDS_BIBLIOTECA(String value) {
		this.DS_BIBLIOTECA = value;
    this.settaParametro(KeyParameter.XXXds_biblioteca,value);
	}

	public String getDS_BIBLIOTECA() {
		return DS_BIBLIOTECA;
	}

	public void setDS_CITTA(String value) {
		this.DS_CITTA = value;
    this.settaParametro(KeyParameter.XXXds_citta,value);
	}


 public String getDS_CITTA() {
		return DS_CITTA;
	}

	public String toString() {
		return String.valueOf(getMID() + " " + getCD_POLO() + " " + getCD_BIBLIOTECA());
	}
}
