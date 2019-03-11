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
package it.iccu.sbn.polo.orm.bibliografica;

import java.io.Serializable;
/**
 * PAROLE CHIAVE DELLE MARCHE EDITORIALI
 */
/**
 * ORM-Persistable Class
 */
public class Tb_parola implements Serializable {

	private static final long serialVersionUID = -9044438801640159172L;

	private byte id_parola;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_marca m;

	private String parola;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setId_parola(byte value) {
		this.id_parola = value;
	}

	public byte getId_parola() {
		return id_parola;
	}

	public byte getORMID() {
		return getId_parola();
	}

	/**
	 * Parola associata alla descrizione della marca
	 */
	public void setParola(String value) {
		this.parola = value;
	}

	/**
	 * Parola associata alla descrizione della marca
	 */
	public String getParola() {
		return parola;
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

	public void setM(it.iccu.sbn.polo.orm.bibliografica.Tb_marca value) {
		this.m = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_marca getM() {
		return m;
	}

	public String toString() {
		return String.valueOf(getId_parola());
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
