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
public class Tb_parola extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 6292775083250219106L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_parola))
			return false;
		Tb_parola tb_par = (Tb_parola)aObj;
		if (getID_PAROLA() != tb_par.getID_PAROLA())
			return false;
		if (getMID() != null && !getMID().equals(tb_par.getMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (int) getID_PAROLA();
		hashcode = hashcode + getMID().hashCode();
		return hashcode;
	}

	private long ID_PAROLA;

	private String MID;

	private String PAROLA;

	public void setID_PAROLA(long value) {
		this.ID_PAROLA = value;
    this.settaParametro(KeyParameter.XXXid_parola,value);
	}

	public long getID_PAROLA() {
		return ID_PAROLA;
	}

	public void setPAROLA(String value) {
		this.PAROLA = value;
    this.settaParametro(KeyParameter.XXXparola,value);
	}

	public String getPAROLA() {
		return PAROLA;
	}

	public void setMID(String value) {
		this.MID = value;
    this.settaParametro(KeyParameter.XXXmid,value);
	}

 public String getMID() {
		return MID;
	}

	public String toString() {
		return String.valueOf(getID_PAROLA() + " "+ ((getMID() == null) ? "" : String.valueOf(getMID())));
	}

}
