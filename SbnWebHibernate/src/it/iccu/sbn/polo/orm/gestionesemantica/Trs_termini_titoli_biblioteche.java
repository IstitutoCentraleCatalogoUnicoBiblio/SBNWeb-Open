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
 * TERMINI DI THESAURO DI OGGETTI BIBLIOGRAFICI IN BIBLIOTECA (TPSDTB))
 */
/**
 * ORM-Persistable Class
 */
public class Trs_termini_titoli_biblioteche implements Serializable {

	private static final long serialVersionUID = 5263181432050469140L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Trs_termini_titoli_biblioteche))
			return false;
		Trs_termini_titoli_biblioteche trs_termini_titoli_biblioteche = (Trs_termini_titoli_biblioteche)aObj;
		if (getB() == null)
			return false;
		if (!getB().getORMID().equals(trs_termini_titoli_biblioteche.getB().getORMID()))
			return false;
		if (getD() == null)
			return false;
		if (!getD().getORMID().equals(trs_termini_titoli_biblioteche.getD().getORMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getB() != null) {
			hashcode = hashcode + getB().getORMID().hashCode();
		}
		if (getD() != null) {
			hashcode = hashcode + getD().getORMID().hashCode();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private it.iccu.sbn.polo.orm.gestionesemantica.Tr_thesauri_biblioteche cd_the;

	private it.iccu.sbn.polo.orm.gestionesemantica.Tb_termine_thesauro d;

	private String nota_termine_titoli_biblioteca;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	/**
	 * nota al collegamento tra l'oggetto bibliografico e il termine di thesauro
	 */
	public void setNota_termine_titoli_biblioteca(String value) {
		this.nota_termine_titoli_biblioteca = value;
	}

	/**
	 * nota al collegamento tra l'oggetto bibliografico e il termine di thesauro
	 */
	public String getNota_termine_titoli_biblioteca() {
		return nota_termine_titoli_biblioteca;
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

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public void setCd_the(it.iccu.sbn.polo.orm.gestionesemantica.Tr_thesauri_biblioteche value) {
		this.cd_the = value;
	}

	public it.iccu.sbn.polo.orm.gestionesemantica.Tr_thesauri_biblioteche getCd_the() {
		return cd_the;
	}

	public void setD(it.iccu.sbn.polo.orm.gestionesemantica.Tb_termine_thesauro value) {
		this.d = value;
	}

	public it.iccu.sbn.polo.orm.gestionesemantica.Tb_termine_thesauro getD() {
		return d;
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())) + " " + ((getD() == null) ? "" : String.valueOf(getD().getORMID())));
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
