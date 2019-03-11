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
package it.iccu.sbn.polo.orm.gestionesemantica;

import java.io.Serializable;
/**
 * DESCRITTORI NEI SOGGETTI (TPSSDS))
 */
/**
 * ORM-Persistable Class
 */
public class Tr_sog_des implements Serializable {

	private static final long serialVersionUID = -7448577414454350185L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_sog_des))
			return false;
		Tr_sog_des tr_sog_des = (Tr_sog_des)aObj;
		if (getD() == null)
			return false;
		if (!getD().getORMID().equals(tr_sog_des.getD().getORMID()))
			return false;
		if (getC() == null)
			return false;
		if (!getC().getORMID().equals(tr_sog_des.getC().getORMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getD() != null) {
			hashcode = hashcode + getD().getORMID().hashCode();
		}
		if (getC() != null) {
			hashcode = hashcode + getC().getORMID().hashCode();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.gestionesemantica.Tb_descrittore d;

	private it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto c;

	private int fl_posizione;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private Character fl_primavoce;

	/**
	 * indicatore di legame inserito manualmente
	 */
	public void setFl_posizione(int value) {
		this.fl_posizione = value;
	}

	/**
	 * indicatore di legame inserito manualmente
	 */
	public int getFl_posizione() {
		return fl_posizione;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * Timestamp di inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * Timestamp di inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public void setUte_var(String value) {
		this.ute_var = value;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public String getUte_var() {
		return ute_var;
	}

	/**
	 * Timestamp di variazione
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * Timestamp di variazione
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	/**
	 * Flag di cancellazione logica
	 */
	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	/**
	 * Flag di cancellazione logica
	 */
	public char getFl_canc() {
		return fl_canc;
	}

	public void setFl_primavoce(char value) {
		setFl_primavoce(new Character(value));
	}

	public void setFl_primavoce(Character value) {
		this.fl_primavoce = value;
	}

	public Character getFl_primavoce() {
		return fl_primavoce;
	}

	public void setD(it.iccu.sbn.polo.orm.gestionesemantica.Tb_descrittore value) {
		this.d = value;
	}

	public it.iccu.sbn.polo.orm.gestionesemantica.Tb_descrittore getD() {
		return d;
	}

	public void setC(it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto value) {
		this.c = value;
	}

	public it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto getC() {
		return c;
	}

	public String toString() {
		return String.valueOf(((getD() == null) ? "" : String.valueOf(getD().getORMID())) + " " + ((getC() == null) ? "" : String.valueOf(getC().getORMID())));
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
