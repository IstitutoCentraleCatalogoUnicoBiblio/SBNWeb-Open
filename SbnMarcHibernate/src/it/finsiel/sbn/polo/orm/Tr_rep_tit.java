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
public class Tr_rep_tit extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 3307762451630646525L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_rep_tit))
			return false;
		Tr_rep_tit tr_rep_tit = (Tr_rep_tit)aObj;
		if (getBID() == null)
			return false;
		if (!getBID().equals(tr_rep_tit.getBID()))
			return false;

		if (getID_REPERTORIO() != tr_rep_tit.getID_REPERTORIO())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getBID() != null) {
			hashcode = hashcode + getBID().hashCode();
		}
		hashcode = hashcode + (int) getID_REPERTORIO();
		return hashcode;
	}

	private String BID;

	private long ID_REPERTORIO;

	private String NOTA_REP_TIT;

	private String FL_TROVATO;

	public void setNOTA_REP_TIT(String value) {
		this.NOTA_REP_TIT = value;
    this.settaParametro(KeyParameter.XXXnota_rep_tit,value);
	}

	public String getNOTA_REP_TIT() {
		return NOTA_REP_TIT;
	}

	public void setFL_TROVATO(String value) {
		this.FL_TROVATO = value;
    this.settaParametro(KeyParameter.XXXfl_trovato,value);
	}

	public String getFL_TROVATO() {
		return FL_TROVATO;
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
	}

	public void setID_REPERTORIO(long value) {
		this.ID_REPERTORIO = value;
    this.settaParametro(KeyParameter.XXXid_repertorio,value);
	}


 public long getID_REPERTORIO() {
		return ID_REPERTORIO;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())) + " " + String.valueOf(getID_REPERTORIO()));
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
