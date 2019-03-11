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
public class Tr_rep_aut extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -3773660485745732196L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_rep_aut))
			return false;
		Tr_rep_aut tr_rep_aut = (Tr_rep_aut)aObj;
		if (getVID() == null)
			return false;
		if (!getVID().equals(tr_rep_aut.getVID()))
			return false;
		if (getID_REPERTORIO() != tr_rep_aut.getID_REPERTORIO())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getVID() != null) {
			hashcode = hashcode + getVID().hashCode();
		}
		hashcode = hashcode + (int) getID_REPERTORIO();
		return hashcode;
	}

	private String VID;

	private long ID_REPERTORIO;

	private String NOTE_REP_AUT;

	private String FL_TROVATO;

	public void setNOTE_REP_AUT(String value) {
		this.NOTE_REP_AUT = value;
    this.settaParametro(KeyParameter.XXXnote_rep_aut,value);
	}

	public String getNOTE_REP_AUT() {
		return NOTE_REP_AUT;
	}

	public void setFL_TROVATO(String value) {
		this.FL_TROVATO = value;
    this.settaParametro(KeyParameter.XXXfl_trovato,value);
	}

	public String getFL_TROVATO() {
		return FL_TROVATO;
	}

	public void setVID(String value) {
		this.VID = value;
    this.settaParametro(KeyParameter.XXXvid,value);
	}

	public String getVID() {
		return VID;
	}

	public void setID_REPERTORIO(long value) {
		this.ID_REPERTORIO = value;
    this.settaParametro(KeyParameter.XXXid_repertorio,value);
	}

 public long getID_REPERTORIO() {
		return ID_REPERTORIO;
	}

	public String toString() {
		return String.valueOf(((getVID() == null) ? "" : String.valueOf(getVID())) + " " + String.valueOf(getID_REPERTORIO()));
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
