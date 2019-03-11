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
public class Ts_id_errati extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -3084081755123102655L;

	private String ID;

	private String NOTAID;

	public void setID(String value) {
		this.ID = value;
    this.settaParametro(KeyParameter.XXXid,value);
	}

	public String getID() {
		return ID;
	}

	public String getORMID() {
		return getID();
	}

	public void setNOTAID(String value) {
		this.NOTAID = value;
    this.settaParametro(KeyParameter.XXXnotaid,value);
	}


 public String getNOTAID() {
		return NOTAID;
	}

	public String toString() {
		return String.valueOf(getID());
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
