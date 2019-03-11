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

public class Ts_titolo_marc extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -1458964486053609308L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ts_titolo_marc))
			return false;
		Ts_titolo_marc ts_titolo_marc = (Ts_titolo_marc)aObj;
		if (getID_TAG_MARC() != ts_titolo_marc.getID_TAG_MARC())
			return false;
		if (getID_TITOLO_MARC() != ts_titolo_marc.getID_TITOLO_MARC())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (int) getID_TAG_MARC();
		hashcode = hashcode + (int) getID_TITOLO_MARC();
		return hashcode;
	}

	private long ID_TAG_MARC;

	private long ID_TITOLO_MARC;

	private String BID_MARC;

	private String IDPARTENZA;

	private String IDINDICE;

	private String NOTAID;

	private String TP_OPERAZIONE;

	private String KCERCA;

	private String STATO;

	private String TIPOMATERIALE;

	private String NATURA;

	private String LIVELLOAUT;

	private String TIPORECORD;

	private String LIVELLOGERARCHICO;

	public void setID_TAG_MARC(long value) {
		this.ID_TAG_MARC = value;
    this.settaParametro(KeyParameter.XXXid_tag_marc,value);
	}

	public long getID_TAG_MARC() {
		return ID_TAG_MARC;
	}

	public void setID_TITOLO_MARC(long value) {
		this.ID_TITOLO_MARC = value;
    this.settaParametro(KeyParameter.XXXid_titolo_marc,value);
	}

	public long getID_TITOLO_MARC() {
		return ID_TITOLO_MARC;
	}

	public void setBID_MARC(String value) {
		this.BID_MARC = value;
    this.settaParametro(KeyParameter.XXXbid_marc,value);
	}

	public String getBID_MARC() {
		return BID_MARC;
	}

	public void setIDPARTENZA(String value) {
		this.IDPARTENZA = value;
    this.settaParametro(KeyParameter.XXXidpartenza,value);
	}

	public String getIDPARTENZA() {
		return IDPARTENZA;
	}

	public void setIDINDICE(String value) {
		this.IDINDICE = value;
    this.settaParametro(KeyParameter.XXXidindice,value);
	}

	public String getIDINDICE() {
		return IDINDICE;
	}

	public void setNOTAID(String value) {
		this.NOTAID = value;
    this.settaParametro(KeyParameter.XXXnotaid,value);
	}

	public String getNOTAID() {
		return NOTAID;
	}

	public void setTP_OPERAZIONE(String value) {
		this.TP_OPERAZIONE = value;
    this.settaParametro(KeyParameter.XXXtp_operazione,value);
	}

	public String getTP_OPERAZIONE() {
		return TP_OPERAZIONE;
	}

	public void setKCERCA(String value) {
		this.KCERCA = value;
    this.settaParametro(KeyParameter.XXXkcerca,value);
	}

	public String getKCERCA() {
		return KCERCA;
	}

	public void setSTATO(String value) {
		this.STATO = value;
    this.settaParametro(KeyParameter.XXXstato,value);
	}

	public String getSTATO() {
		return STATO;
	}

	public void setTIPOMATERIALE(String value) {
		this.TIPOMATERIALE = value;
    this.settaParametro(KeyParameter.XXXtipomateriale,value);
	}

	public String getTIPOMATERIALE() {
		return TIPOMATERIALE;
	}

	public void setNATURA(String value) {
		this.NATURA = value;
    this.settaParametro(KeyParameter.XXXnatura,value);
	}

	public String getNATURA() {
		return NATURA;
	}

	public void setLIVELLOAUT(String value) {
		this.LIVELLOAUT = value;
    this.settaParametro(KeyParameter.XXXlivelloaut,value);
	}

	public String getLIVELLOAUT() {
		return LIVELLOAUT;
	}

	public void setTIPORECORD(String value) {
		this.TIPORECORD = value;
    this.settaParametro(KeyParameter.XXXtiporecord,value);
	}

	public String getTIPORECORD() {
		return TIPORECORD;
	}

	public void setLIVELLOGERARCHICO(String value) {
		this.LIVELLOGERARCHICO = value;
    this.settaParametro(KeyParameter.XXXlivellogerarchico,value);
	}


 public String getLIVELLOGERARCHICO() {
		return LIVELLOGERARCHICO;
	}

	public String toString() {
		return String.valueOf(getID_TAG_MARC() + " " + getID_TITOLO_MARC());
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
