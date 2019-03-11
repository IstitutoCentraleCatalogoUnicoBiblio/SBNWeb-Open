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
public class Tb_nota extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -232143022281024283L;

	public Tb_nota() {

	}

	//almaviva5_20090918 #3068 clone constructor
	public Tb_nota(Tb_nota o) {
		BID = o.BID;
		TP_NOTA = o.TP_NOTA;
		PROGR_NOTA = o.PROGR_NOTA;
		DS_NOTA = o.DS_NOTA;
		setUTE_INS(o.getUTE_INS());
		setTS_INS(o.getTS_INS());
		setUTE_VAR(o.getUTE_VAR());
		setTS_VAR(o.getTS_VAR());
		setFL_CANC(o.getFL_CANC());

		setFL_CONDIVISO(o.getFL_CONDIVISO());
		setUTE_CONDIVISO(o.getUTE_CONDIVISO());
		setTS_CONDIVISO(o.getTS_CONDIVISO());
	}

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_nota))
			return false;
		Tb_nota tb_nota = (Tb_nota)aObj;
		if (getBID() == null)
			return false;
		if (!getBID().equals(tb_nota.getBID()))
			return false;
		if (getTP_NOTA() != null && !getTP_NOTA().equals(tb_nota.getTP_NOTA()))
			return false;
		if (getPROGR_NOTA() != tb_nota.getPROGR_NOTA())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getBID() != null) {
			hashcode = hashcode + getBID().hashCode();
		}
		hashcode = hashcode + getTP_NOTA().hashCode();
		hashcode = hashcode + (int) getPROGR_NOTA();
		return hashcode;
	}

	private String BID;

	private String TP_NOTA;

	private long PROGR_NOTA;

	private String DS_NOTA;

	public void setTP_NOTA(String value) {
		this.TP_NOTA = value;
    this.settaParametro(KeyParameter.XXXtp_nota,value);
	}

	public String getTP_NOTA() {
		return TP_NOTA;
	}

	public void setPROGR_NOTA(long value) {
		this.PROGR_NOTA = value;
    this.settaParametro(KeyParameter.XXXprogr_nota,value);
	}

	public long getPROGR_NOTA() {
		return PROGR_NOTA;
	}

	public void setDS_NOTA(String value) {
		this.DS_NOTA = value;
    this.settaParametro(KeyParameter.XXXds_nota,value);
	}

	public String getDS_NOTA() {
		return DS_NOTA;
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}


 public String getBID() {
		return BID;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())) + " " + getTP_NOTA() + " " + getPROGR_NOTA());
	}
}
