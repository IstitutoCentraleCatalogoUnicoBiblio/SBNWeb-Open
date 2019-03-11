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
public class Ts_tag_arrivo extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 6777408503985840025L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ts_tag_arrivo))
			return false;
		Ts_tag_arrivo ts_tag_arrivo = (Ts_tag_arrivo)aObj;
		if (getID_TAG_MARC() != ts_tag_arrivo.getID_TAG_MARC())
			return false;
		if (getID_LEG() != null && !getID_LEG().equals(ts_tag_arrivo.getID_LEG()))
			return false;
		if (getIDARRIVO() != null && !getIDARRIVO().equals(ts_tag_arrivo.getIDARRIVO()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (int) getID_TAG_MARC();
		hashcode = hashcode + getID_LEG().hashCode();
		hashcode = hashcode + getIDARRIVO().hashCode();
		return hashcode;
	}

	private long ID_TAG_MARC;

	private String ID_LEG;

	private String IDARRIVO;

	private String NOTAID;

	private String IDINDICE;

	private String TP_ID;

	private String KCERCA;

	private String NATURA;

	public void setID_TAG_MARC(long value) {
		this.ID_TAG_MARC = value;
    this.settaParametro(KeyParameter.XXXid_tag_marc,value);
	}

	public long getID_TAG_MARC() {
		return ID_TAG_MARC;
	}

	public void setID_LEG(String value) {
		this.ID_LEG = value;
    this.settaParametro(KeyParameter.XXXid_leg,value);
	}

	public String getID_LEG() {
		return ID_LEG;
	}

	public void setIDARRIVO(String value) {
		this.IDARRIVO = value;
    this.settaParametro(KeyParameter.XXXidarrivo,value);
	}

	public String getIDARRIVO() {
		return IDARRIVO;
	}

	public void setNOTAID(String value) {
		this.NOTAID = value;
    this.settaParametro(KeyParameter.XXXnotaid,value);
	}

	public String getNOTAID() {
		return NOTAID;
	}

	public void setIDINDICE(String value) {
		this.IDINDICE = value;
    this.settaParametro(KeyParameter.XXXidindice,value);
	}

	public String getIDINDICE() {
		return IDINDICE;
	}

	public void setTP_ID(String value) {
		this.TP_ID = value;
    this.settaParametro(KeyParameter.XXXtp_id,value);
	}

	public String getTP_ID() {
		return TP_ID;
	}

	public void setKCERCA(String value) {
		this.KCERCA = value;
    this.settaParametro(KeyParameter.XXXkcerca,value);
	}

	public String getKCERCA() {
		return KCERCA;
	}

	public void setNATURA(String value) {
		this.NATURA = value;
    this.settaParametro(KeyParameter.XXXnatura,value);
	}


 public String getNATURA() {
		return NATURA;
	}

	public String toString() {
		return String.valueOf(getID_TAG_MARC() + " " + getID_LEG() + " " + getIDARRIVO());
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
