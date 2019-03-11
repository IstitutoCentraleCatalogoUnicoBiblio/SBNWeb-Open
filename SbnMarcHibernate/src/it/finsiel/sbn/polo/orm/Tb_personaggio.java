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
public class Tb_personaggio extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 8171102010794577773L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_personaggio))
			return false;
		Tb_personaggio tb_par = (Tb_personaggio)aObj;
		if (getID_PERSONAGGIO() != tb_par.getID_PERSONAGGIO())
			return false;
		if (getBID() != null && !getBID().equals(tb_par.getBID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (int) getID_PERSONAGGIO();
		hashcode = hashcode + getBID().hashCode();
		return hashcode;
	}

	private long ID_PERSONAGGIO;

	private String BID;

	private String NOME_PERSONAGGIO;

	private String CD_TIMBRO_VOCALE;

	public void setID_PERSONAGGIO(long value) {
		this.ID_PERSONAGGIO = value;
    this.settaParametro(KeyParameter.XXXid_personaggio,value);
	}

	public long getID_PERSONAGGIO() {
		return ID_PERSONAGGIO;
	}

	public void setNOME_PERSONAGGIO(String value) {
		this.NOME_PERSONAGGIO = value;
    this.settaParametro(KeyParameter.XXXnome_personaggio,value);
	}

	public String getNOME_PERSONAGGIO() {
		return NOME_PERSONAGGIO;
	}

	public void setCD_TIMBRO_VOCALE(String value) {
		this.CD_TIMBRO_VOCALE = value;
    this.settaParametro(KeyParameter.XXXcd_timbro_vocale,value);
	}

	public String getCD_TIMBRO_VOCALE() {
		return CD_TIMBRO_VOCALE;
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}


 public String getBID() {
		return BID;
	}

	public String toString() {
		return String.valueOf(getID_PERSONAGGIO() + " "+ ((getBID() == null) ? "" : String.valueOf(getBID())));
	}

}
