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
public class Tr_des_des extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -3689214777919448540L;



	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_des_des))
			return false;
		Tr_des_des tr_des_des = (Tr_des_des)aObj;
		if (getDID_BASE() == null)
			return false;
		if (!getDID_BASE().equals(tr_des_des.getDID_BASE()))
			return false;
		if (getDID_COLL() == null)
			return false;
		if (!getDID_COLL().equals(tr_des_des.getDID_COLL()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getDID_BASE() != null) {
			hashcode = hashcode + getDID_BASE().hashCode();
		}
		if (getDID_COLL() != null) {
			hashcode = hashcode + getDID_COLL().hashCode();
		}
		return hashcode;
	}

	private String DID_BASE;

	private String DID_COLL;

	private String TP_LEGAME;

	private String NOTA_DES_DES;



	public void setTP_LEGAME(String value) {
		this.TP_LEGAME = value;
    this.settaParametro(KeyParameter.XXXtp_legame,value);
	}

	public String getTP_LEGAME() {
		return TP_LEGAME;
	}

	public void setDID_BASE(String value) {
		this.DID_BASE = value;
    this.settaParametro(KeyParameter.XXXdid_base,value);
	}

	public String getDID_BASE() {
		return DID_BASE;
	}

	public void setDID_COLL(String value) {
		this.DID_COLL = value;
    this.settaParametro(KeyParameter.XXXdid_coll,value);
	}


 public String getDID_COLL() {
		return DID_COLL;
	}


	public String toString() {
		return String.valueOf(((getDID_BASE() == null) ? "" : String.valueOf(getDID_BASE())) + " " + ((getDID_COLL() == null) ? "" : String.valueOf(getDID_COLL())));
	}

	public void setNOTA_DES_DES(String value) {
		this.NOTA_DES_DES = value;
    this.settaParametro(KeyParameter.XXXnota_des_des,value);
	}

	public String getNOTA_DES_DES() {
		return NOTA_DES_DES;
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
