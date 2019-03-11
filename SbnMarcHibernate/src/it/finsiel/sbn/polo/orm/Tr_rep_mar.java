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
public class Tr_rep_mar extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 209483479939243061L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_rep_mar))
			return false;
		Tr_rep_mar tr_rep_mar = (Tr_rep_mar)aObj;
		if (getMID() == null)
			return false;
		if (!getMID().equals(tr_rep_mar.getMID()))
			return false;
		if (getID_REPERTORIO() != tr_rep_mar.getID_REPERTORIO())
			return false;
		if (getPROGR_REPERTORIO() != tr_rep_mar.getPROGR_REPERTORIO())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getMID() != null) {
			hashcode = hashcode + getMID().hashCode();
		}
		hashcode = hashcode + (int) getID_REPERTORIO();
		hashcode = hashcode + (int) getPROGR_REPERTORIO();
		return hashcode;
	}

	private String MID;

	private long ID_REPERTORIO;

	private long PROGR_REPERTORIO;

	private String NOTA_REP_MAR;

	public void setPROGR_REPERTORIO(long value) {
		this.PROGR_REPERTORIO = value;
    this.settaParametro(KeyParameter.XXXprogr_repertorio,value);
	}

	public long getPROGR_REPERTORIO() {
		return PROGR_REPERTORIO;
	}

	public void setNOTA_REP_MAR(String value) {
		this.NOTA_REP_MAR = value;
    this.settaParametro(KeyParameter.XXXnota_rep_mar,value);
	}

	public String getNOTA_REP_MAR() {
		return NOTA_REP_MAR;
	}

	public void setMID(String value) {
		this.MID = value;
    this.settaParametro(KeyParameter.XXXmid,value);
	}

	public String getMID() {
		return MID;
	}

	public void setID_REPERTORIO(long value) {
		this.ID_REPERTORIO = value;
    this.settaParametro(KeyParameter.XXXid_repertorio,value);
	}


 public long getID_REPERTORIO() {
		return ID_REPERTORIO;
	}

	public String toString() {
		return String.valueOf(((getMID() == null) ? "" : String.valueOf(getMID())) + " " + String.valueOf(getID_REPERTORIO()) + " " + getPROGR_REPERTORIO());
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


}
