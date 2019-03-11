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
public class Tr_mar_bib extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 4727420094843551484L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_mar_bib))
			return false;
		Tr_mar_bib tr_mar_bib = (Tr_mar_bib)aObj;
		if (getCD_POLO() == null)
			return false;
		if (!getCD_POLO().equals(tr_mar_bib.getCD_POLO()))
			return false;

		if (getCD_BIBLIOTECA() == null)
			return false;
		if (!getCD_BIBLIOTECA().equals(tr_mar_bib.getCD_BIBLIOTECA()))
			return false;

		if (getMID() == null)
			return false;
		if (!getMID().equals(tr_mar_bib.getMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCD_POLO().hashCode();
		hashcode = hashcode + getCD_BIBLIOTECA().hashCode();
		hashcode = hashcode + getMID().hashCode();
		return hashcode;
	}

	private String CD_POLO;

	private String CD_BIBLIOTECA;

	private String MID;

	private String FL_ALLINEA;

	private String FL_ALLINEA_SBNMARC;

	public void setFL_ALLINEA(String value) {
		this.FL_ALLINEA = value;
    this.settaParametro(KeyParameter.XXXfl_allinea,value);
	}

	public String getFL_ALLINEA() {
		return FL_ALLINEA;
	}

	public void setFL_ALLINEA_SBNMARC(String value) {
		this.FL_ALLINEA_SBNMARC = value;
    this.settaParametro(KeyParameter.XXXfl_allinea_sbnmarc,value);
	}

	public String getFL_ALLINEA_SBNMARC() {
		return FL_ALLINEA_SBNMARC;
	}

	public void setMID(String value) {
		this.MID = value;
    this.settaParametro(KeyParameter.XXXmid,value);
	}

	public String getMID() {
		return MID;
	}

	public void setCD_POLO(String value) {
		this.CD_POLO = value;
    this.settaParametro(KeyParameter.XXXcd_polo,value);
	}

	public String getCD_POLO() {
		return CD_POLO;
	}

	public String toString() {
		return String.valueOf(((getCD_POLO() == null) ? "" : String.valueOf(getCD_POLO()) + " " + String.valueOf(getCD_BIBLIOTECA())) + " " + ((getMID() == null) ? "" : String.valueOf(getMID())));
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

	public String getCD_BIBLIOTECA() {
		return CD_BIBLIOTECA;
	}

	public void setCD_BIBLIOTECA(String cd_biblioteca) {
		CD_BIBLIOTECA = cd_biblioteca;
    this.settaParametro(KeyParameter.XXXcd_biblioteca,cd_biblioteca);
	}


}
