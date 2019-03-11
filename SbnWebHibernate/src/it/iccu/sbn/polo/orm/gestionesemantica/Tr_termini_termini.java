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
 * COLLEGAMENTI TRA DESCRITTORI DI THESAURO (TPSCDT))
 */
/**
 * ORM-Persistable Class
 */
public class Tr_termini_termini implements Serializable {

	private static final long serialVersionUID = -5601908006901782285L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_termini_termini))
			return false;
		Tr_termini_termini tr_termini_termini = (Tr_termini_termini)aObj;
		if (getDid_base() == null)
			return false;
		if (!getDid_base().getORMID().equals(tr_termini_termini.getDid_base().getORMID()))
			return false;
		if (getDid_coll() == null)
			return false;
		if (!getDid_coll().getORMID().equals(tr_termini_termini.getDid_coll().getORMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getDid_base() != null) {
			hashcode = hashcode + getDid_base().getORMID().hashCode();
		}
		if (getDid_coll() != null) {
			hashcode = hashcode + getDid_coll().getORMID().hashCode();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.gestionesemantica.Tb_termine_thesauro did_base;

	private it.iccu.sbn.polo.orm.gestionesemantica.Tb_termine_thesauro did_coll;

	private String nota_termini_termini;

	private String tipo_coll;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	/**
	 * note al legame
	 */
	public void setNota_termini_termini(String value) {
		this.nota_termini_termini = value;
	}

	/**
	 * note al legame
	 */
	public String getNota_termini_termini() {
		return nota_termini_termini;
	}

	/**
	 * codice legame tra descrittori
	 */
	public void setTipo_coll(String value) {
		this.tipo_coll = value;
	}

	/**
	 * codice legame tra descrittori
	 */
	public String getTipo_coll() {
		return tipo_coll;
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

	public void setDid_base(it.iccu.sbn.polo.orm.gestionesemantica.Tb_termine_thesauro value) {
		this.did_base = value;
	}

	public it.iccu.sbn.polo.orm.gestionesemantica.Tb_termine_thesauro getDid_base() {
		return did_base;
	}

	public void setDid_coll(it.iccu.sbn.polo.orm.gestionesemantica.Tb_termine_thesauro value) {
		this.did_coll = value;
	}

	public it.iccu.sbn.polo.orm.gestionesemantica.Tb_termine_thesauro getDid_coll() {
		return did_coll;
	}

	public String toString() {
		return String.valueOf(((getDid_base() == null) ? "" : String.valueOf(getDid_base().getORMID())) + " " + ((getDid_coll() == null) ? "" : String.valueOf(getDid_coll().getORMID())));
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
