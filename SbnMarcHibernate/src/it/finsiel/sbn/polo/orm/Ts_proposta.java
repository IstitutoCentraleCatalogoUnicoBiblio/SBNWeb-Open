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
public class Ts_proposta extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -1290570912599840435L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ts_proposta))
			return false;
		Ts_proposta ts_proposta = (Ts_proposta)aObj;
		if (getUTE_MITTENTE() != null && !getUTE_MITTENTE().equals(ts_proposta.getUTE_MITTENTE()))
			return false;
		if (getPROGR_PROPOSTA() != ts_proposta.getPROGR_PROPOSTA())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getUTE_MITTENTE().hashCode();
		hashcode = hashcode + (int) getPROGR_PROPOSTA();
		return hashcode;
	}

	private String UTE_MITTENTE;

	private long PROGR_PROPOSTA;

	private String UTE_DESTINATARIO;

	private String BIDVID;

	private String TP_MESSAGGIO;

	private String DS_PROPOSTA;

	private java.util.Date DT_INOLTRO;

	public void setUTE_MITTENTE(String value) {
		this.UTE_MITTENTE = value;
    this.settaParametro(KeyParameter.XXXute_mittente,value);
	}

	public String getUTE_MITTENTE() {
		return UTE_MITTENTE;
	}

	public void setPROGR_PROPOSTA(long value) {
		this.PROGR_PROPOSTA = value;
    this.settaParametro(KeyParameter.XXXprogr_proposta,value);
	}

	public long getPROGR_PROPOSTA() {
		return PROGR_PROPOSTA;
	}

	public void setUTE_DESTINATARIO(String value) {
		this.UTE_DESTINATARIO = value;
    this.settaParametro(KeyParameter.XXXute_destinatario,value);
	}

	public String getUTE_DESTINATARIO() {
		return UTE_DESTINATARIO;
	}

	public void setBIDVID(String value) {
		this.BIDVID = value;
    this.settaParametro(KeyParameter.XXXbidvid,value);
	}

	public String getBIDVID() {
		return BIDVID;
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

	public String toString() {
		return String.valueOf(getUTE_MITTENTE() + " " + getPROGR_PROPOSTA());
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
