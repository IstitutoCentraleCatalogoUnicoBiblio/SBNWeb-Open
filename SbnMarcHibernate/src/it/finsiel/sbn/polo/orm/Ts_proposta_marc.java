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
public class Ts_proposta_marc extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -7831699969107964915L;

	private long ID_PROPOSTA;

	private String UTE_MITTENTE;

	private String ID_OGGETTO;

	private String TP_MESSAGGIO;

	private String DS_PROPOSTA;

	private java.util.Date DT_INOLTRO;

	private String CD_OGGETTO;

	private String CD_STATO;

	private java.util.Set TS_NOTE_PROPOSTA = new java.util.HashSet();

	public void setID_PROPOSTA(long value) {
		this.ID_PROPOSTA = value;
    this.settaParametro(KeyParameter.XXXid_proposta,value);
	}

	public long getID_PROPOSTA() {
		return ID_PROPOSTA;
	}

	public void setUTE_MITTENTE(String value) {
		this.UTE_MITTENTE = value;
    this.settaParametro(KeyParameter.XXXute_mittente,value);
	}

	public String getUTE_MITTENTE() {
		return UTE_MITTENTE;
	}

	public void setID_OGGETTO(String value) {
		this.ID_OGGETTO = value;
    this.settaParametro(KeyParameter.XXXid_oggetto,value);
	}

	public String getID_OGGETTO() {
		return ID_OGGETTO;
	}

	public void setTP_MESSAGGIO(String value) {
		this.TP_MESSAGGIO = value;
    this.settaParametro(KeyParameter.XXXtp_messaggio,value);
	}

	public String getTP_MESSAGGIO() {
		return TP_MESSAGGIO;
	}

	public void setDS_PROPOSTA(String value) {
		this.DS_PROPOSTA = value;
    this.settaParametro(KeyParameter.XXXds_proposta,value);
	}

	public String getDS_PROPOSTA() {
		return DS_PROPOSTA;
	}

	public void setDT_INOLTRO(java.util.Date value) {
		this.DT_INOLTRO = value;
    this.settaParametro(KeyParameter.XXXdt_inoltro,value);
	}

	public java.util.Date getDT_INOLTRO() {
		return DT_INOLTRO;
	}

	public void setCD_OGGETTO(String value) {
		this.CD_OGGETTO = value;
    this.settaParametro(KeyParameter.XXXcd_oggetto,value);
	}

	public String getCD_OGGETTO() {
		return CD_OGGETTO;
	}

	public void setCD_STATO(String value) {
		this.CD_STATO = value;
    this.settaParametro(KeyParameter.XXXcd_stato,value);
	}

	public String getCD_STATO() {
		return CD_STATO;
	}

	public void setTS_NOTE_PROPOSTA(java.util.Set value) {
		this.TS_NOTE_PROPOSTA = value;
    this.settaParametro(KeyParameter.XXXts_note_proposta,value);
	}


	public java.util.Set getTS_NOTE_PROPOSTA() {
		return TS_NOTE_PROPOSTA;
	}


	public String toString() {
		return String.valueOf(getID_PROPOSTA());
	}

}
