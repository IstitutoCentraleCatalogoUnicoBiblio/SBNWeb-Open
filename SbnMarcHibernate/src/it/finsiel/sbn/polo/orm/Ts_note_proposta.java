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
public class Ts_note_proposta extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 2014380293427125096L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ts_note_proposta))
			return false;
		Ts_note_proposta ts_note_proposta = (Ts_note_proposta)aObj;
		if (getID_PROPOSTA() != ts_note_proposta.getID_PROPOSTA())
			return false;
		if (getPROGR_RISPOSTA() != ts_note_proposta.getPROGR_RISPOSTA())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (int) getID_PROPOSTA();
		hashcode = hashcode + (int) getPROGR_RISPOSTA();
		return hashcode;
	}

	private long ID_PROPOSTA;

	private long PROGR_RISPOSTA;

	private String UTE_DESTINATARIO;

	private String NOTE_PRO;

	public void setPROGR_RISPOSTA(long value) {
		this.PROGR_RISPOSTA = value;
    this.settaParametro(KeyParameter.XXXprogr_risposta,value);
	}

	public long getPROGR_RISPOSTA() {
		return PROGR_RISPOSTA;
	}

	public void setUTE_DESTINATARIO(String value) {
		this.UTE_DESTINATARIO = value;
    this.settaParametro(KeyParameter.XXXute_destinatario,value);
	}

	public String getUTE_DESTINATARIO() {
		return UTE_DESTINATARIO;
	}

	public void setNOTE_PRO(String value) {
		this.NOTE_PRO = value;
    this.settaParametro(KeyParameter.XXXnote_pro,value);
	}

	public String getNOTE_PRO() {
		return NOTE_PRO;
	}

	public void setID_PROPOSTA(long value) {
		this.ID_PROPOSTA = value;
    this.settaParametro(KeyParameter.XXXid_proposta,value);
	}


	public long getID_PROPOSTA() {
		return ID_PROPOSTA;
	}

	public String toString() {
		return String.valueOf(String.valueOf(getID_PROPOSTA()) + " " + getPROGR_RISPOSTA());
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
