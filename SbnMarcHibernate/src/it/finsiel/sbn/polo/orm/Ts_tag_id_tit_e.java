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
public class Ts_tag_id_tit_e extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -6552516397679441545L;

	private long ID_TAG_MARC;

	private String BID_MARC;

	private String STATO;

	private String TIPOMATERIALE;

	private String NATURA;

	private String LIVELLOAUT;

	private String TIPORECORD;

	private String LIVELLOGERARCHICO;

	private void setID_TAG_MARC(long value) {
		this.ID_TAG_MARC = value;
    this.settaParametro(KeyParameter.XXXid_tag_marc,value);
	}

	public long getID_TAG_MARC() {
		return ID_TAG_MARC;
	}

	public long getORMID() {
		return getID_TAG_MARC();
	}

	public void setBID_MARC(String value) {
		this.BID_MARC = value;
    this.settaParametro(KeyParameter.XXXbid_marc,value);
	}

	public String getBID_MARC() {
		return BID_MARC;
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
		return String.valueOf(getID_TAG_MARC());
	}

}
