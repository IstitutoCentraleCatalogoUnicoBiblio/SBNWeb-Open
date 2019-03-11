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
public class Tr_tit_sog_bib extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -7778388948176855806L;


	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_sog_bib))
			return false;
		Tr_tit_sog_bib tr_tit_sog_bib = (Tr_tit_sog_bib)aObj;
		if (getCID() == null)
			return false;
		if (!getCID().equals(tr_tit_sog_bib.getCID()))
			return false;
		if (getBID() == null)
			return false;
		if (!getBID().equals(tr_tit_sog_bib.getBID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCID() != null) {
			hashcode = hashcode + getCID().hashCode();
		}
		if (getBID() != null) {
			hashcode = hashcode + getBID().hashCode();
		}
		return hashcode;
	}

	private String BID;

	private String CID;

	private String CD_BIBLIOTECA;

	private String CD_POLO;

	private String CD_SOGG;

	private String NOTA_TIT_SOG_BIB;

	private short POSIZIONE;


	public String getCD_BIBLIOTECA() {
		return CD_BIBLIOTECA;
	}

	public void setCD_BIBLIOTECA(String value) {
		this.CD_BIBLIOTECA = value;
		this.settaParametro(KeyParameter.XXXcd_biblioteca,value);
	}

	public String getCD_POLO() {
		return CD_POLO;
	}

	public void setCD_POLO(String value) {
		this.CD_POLO = value;
		this.settaParametro(KeyParameter.XXXcd_polo,value);
	}

	public String getCD_SOGG() {
		return CD_SOGG;
	}

	public void setCD_SOGG(String value) {
		this.CD_SOGG = value;
		this.settaParametro(KeyParameter.XXXcd_sogg,value);
	}

	public String getNOTA_TIT_SOG_BIB() {
		return NOTA_TIT_SOG_BIB;
	}

	public void setNOTA_TIT_SOG_BIB(String value) {
		this.NOTA_TIT_SOG_BIB = value;
		this.settaParametro(KeyParameter.XXXnota_tit_sog_bib,value);
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
	}

	public void setCID(String value) {
		this.CID = value;
    this.settaParametro(KeyParameter.XXXcid,value);
	}


 public String getCID() {
		return CID;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())) + " " + ((getCID() == null) ? "" : String.valueOf(getCID())));
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

	public short getPOSIZIONE() {
		return POSIZIONE;
	}

	public void setPOSIZIONE(short posiz) {
		POSIZIONE = posiz;
		this.settaParametro(KeyParameter.XXXposizione, posiz);
	}

}
