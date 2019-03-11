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
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class V_dat001b extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 4577674752692118113L;

	private String BID;

	private String NATURA;

	private String MATERIALE;

	private String DATA1;

	private String LIVELLO;

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
	}

	public void setNATURA(String value) {
		this.NATURA = value;
    this.settaParametro(KeyParameter.XXXnatura,value);
	}

	public String getNATURA() {
		return NATURA;
	}

	public void setMATERIALE(String value) {
		this.MATERIALE = value;
    this.settaParametro(KeyParameter.XXXmateriale,value);
	}

	public String getMATERIALE() {
		return MATERIALE;
	}

	public void setDATA1(String value) {
		this.DATA1 = value;
    this.settaParametro(KeyParameter.XXXdata1,value);
	}

	public String getDATA1() {
		return DATA1;
	}

	public void setLIVELLO(String value) {
		this.LIVELLO = value;
    this.settaParametro(KeyParameter.XXXlivello,value);
	}


 public String getLIVELLO() {
		return LIVELLO;
	}

	public String toString() {
		return String.valueOf(getBID());
	}

}
