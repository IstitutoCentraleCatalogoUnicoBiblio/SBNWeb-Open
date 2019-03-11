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
public class Tb_microforma extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -2901652119079493421L;

	private String BID;

	private String CD_DESIGNAZIONE;

	private String CD_POLARITA;

	private String CD_DIMENSIONE;

	private String CD_RIDUZIONE;

	private String CD_RIDUZIONE_SPEC;

	private String CD_COLORE;

	private String CD_EMULSIONE;

	private String CD_GENERAZIONE;

	private String CD_BASE;

	public void setCD_DESIGNAZIONE(String value) {
		this.CD_DESIGNAZIONE = value;
    this.settaParametro(KeyParameter.XXXcd_designazione,value);
	}

	public String getCD_DESIGNAZIONE() {
		return CD_DESIGNAZIONE;
	}

	public void setCD_POLARITA(String value) {
		this.CD_POLARITA = value;
    this.settaParametro(KeyParameter.XXXcd_polarita,value);
	}

	public String getCD_POLARITA() {
		return CD_POLARITA;
	}

	public void setCD_DIMENSIONE(String value) {
		this.CD_DIMENSIONE = value;
    this.settaParametro(KeyParameter.XXXcd_dimensione,value);
	}

	public String getCD_DIMENSIONE() {
		return CD_DIMENSIONE;
	}

	public void setCD_RIDUZIONE(String value) {
		this.CD_RIDUZIONE = value;
    this.settaParametro(KeyParameter.XXXcd_riduzione,value);
	}

	public String getCD_RIDUZIONE() {
		return CD_RIDUZIONE;
	}

	public void setCD_RIDUZIONE_SPEC(String value) {
		this.CD_RIDUZIONE_SPEC = value;
    this.settaParametro(KeyParameter.XXXcd_riduzione_spec,value);
	}

	public String getCD_RIDUZIONE_SPEC() {
		return CD_RIDUZIONE_SPEC;
	}

	public void setCD_COLORE(String value) {
		this.CD_COLORE = value;
    this.settaParametro(KeyParameter.XXXcd_colore,value);
	}

	public String getCD_COLORE() {
		return CD_COLORE;
	}

	public void setCD_EMULSIONE(String value) {
		this.CD_EMULSIONE = value;
    this.settaParametro(KeyParameter.XXXcd_emulsione,value);
	}

	public String getCD_EMULSIONE() {
		return CD_EMULSIONE;
	}

	public void setCD_GENERAZIONE(String value) {
		this.CD_GENERAZIONE = value;
    this.settaParametro(KeyParameter.XXXcd_generazione,value);
	}

	public String getCD_GENERAZIONE() {
		return CD_GENERAZIONE;
	}

	public void setCD_BASE(String value) {
		this.CD_BASE = value;
    this.settaParametro(KeyParameter.XXXcd_base,value);
	}

	public String getCD_BASE() {
		return CD_BASE;
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}


 public String getBID() {
		return BID;
 }
 public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())));
	}

}
