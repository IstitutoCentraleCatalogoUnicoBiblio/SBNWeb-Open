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
import it.finsiel.sbn.polo.orm.Tb_repertorio;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_repertorio_mar extends Tb_repertorio implements Serializable {

	private static final long serialVersionUID = -2244531492214425862L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_repertorio_mar))
			return false;
		Vl_repertorio_mar vl_repertorio_mar = (Vl_repertorio_mar)aObj;
		if (getMID() != null && !getMID().equals(vl_repertorio_mar.getMID()))
			return false;
		if (getPROGR_REPERTORIO() != vl_repertorio_mar.getPROGR_REPERTORIO())
			return false;
		if (getID_REPERTORIO() != vl_repertorio_mar.getID_REPERTORIO())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getMID().hashCode();
		hashcode = hashcode + (int) getPROGR_REPERTORIO();
		hashcode = hashcode + (int) getID_REPERTORIO();
		return hashcode;
	}

	private String MID;

	private long PROGR_REPERTORIO;

	private String NOTA_REP_MAR;

	public void setMID(String value) {
		this.MID = value;
    this.settaParametro(KeyParameter.XXXmid,value);
	}

	public String getMID() {
		return MID;
	}

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

	public String toString() {
		return String.valueOf(getMID() + " " + getPROGR_REPERTORIO() + " " + getID_REPERTORIO());
	}
}
