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
import it.finsiel.sbn.polo.orm.Tb_soggetto;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_soggetto_tit extends Tb_soggetto implements Serializable  {

	private static final long serialVersionUID = -889257141711731265L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_soggetto_tit))
			return false;
		Vl_soggetto_tit vl_soggetto_tit = (Vl_soggetto_tit)aObj;
		if (getBID() != null && !getBID().equals(vl_soggetto_tit.getBID()))
			return false;
		if (getCID() != null && !getCID().equals(vl_soggetto_tit.getCID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getCID().hashCode();
		return hashcode;
	}

	private String BID;

	private String FL_CONDIVISO;

	private String UTE_CONDIVISO;

	private String NOTA_TIT_SOG_BIB;

	private java.util.Date TS_CONDIVISO;

	private short POSIZIONE;

	public String getFL_CONDIVISO() {
		return FL_CONDIVISO;
	}

	public java.util.Date getTS_CONDIVISO() {
		return TS_CONDIVISO;
	}

	public String getUTE_CONDIVISO() {
		return UTE_CONDIVISO;
	}
	public String getNOTA_TIT_SOG_BIB() {
		return NOTA_TIT_SOG_BIB;
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
	public void setNOTA_TIT_SOG_BIB(String value) {
		this.NOTA_TIT_SOG_BIB = value;
		this.settaParametro(KeyParameter.XXXnota_tit_sog_bib, value);
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

 public String getBID() {
		return BID;
	}

	public String toString() {
		return String.valueOf(getBID() + " " + getCID());
	}

	public short getPOSIZIONE() {
		return POSIZIONE;
	}

	public void setPOSIZIONE(short posiz) {
		POSIZIONE = posiz;
		this.settaParametro(KeyParameter.XXXposizione, posiz);
	}
}
