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
 * MICROFORMA
 */
/**
 * ORM-Persistable Class
 */
public class Tb_microforma implements Serializable {

	private static final long serialVersionUID = -1483610475968187733L;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private char cd_designazione;

	private Character cd_polarita;

	private Character cd_dimensione;

	private Character cd_riduzione;

	private String cd_riduzione_spec;

	private Character cd_colore;

	private Character cd_emulsione;

	private Character cd_generazione;

	private Character cd_base;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setCd_designazione(char value) {
		this.cd_designazione = value;
	}

	public char getCd_designazione() {
		return cd_designazione;
	}

	public void setCd_polarita(char value) {
		setCd_polarita(new Character(value));
	}

	public void setCd_polarita(Character value) {
		this.cd_polarita = value;
	}

	public Character getCd_polarita() {
		return cd_polarita;
	}

	public void setCd_dimensione(char value) {
		setCd_dimensione(new Character(value));
	}

	public void setCd_dimensione(Character value) {
		this.cd_dimensione = value;
	}

	public Character getCd_dimensione() {
		return cd_dimensione;
	}

	public void setCd_riduzione(char value) {
		setCd_riduzione(new Character(value));
	}

	public void setCd_riduzione(Character value) {
		this.cd_riduzione = value;
	}

	public Character getCd_riduzione() {
		return cd_riduzione;
	}

	public void setCd_riduzione_spec(String value) {
		this.cd_riduzione_spec = value;
	}

	public String getCd_riduzione_spec() {
		return cd_riduzione_spec;
	}

	public void setCd_colore(char value) {
		setCd_colore(new Character(value));
	}

	public void setCd_colore(Character value) {
		this.cd_colore = value;
	}

	public Character getCd_colore() {
		return cd_colore;
	}

	public void setCd_emulsione(char value) {
		setCd_emulsione(new Character(value));
	}

	public void setCd_emulsione(Character value) {
		this.cd_emulsione = value;
	}

	public Character getCd_emulsione() {
		return cd_emulsione;
	}

	public void setCd_generazione(char value) {
		setCd_generazione(new Character(value));
	}

	public void setCd_generazione(Character value) {
		this.cd_generazione = value;
	}

	public Character getCd_generazione() {
		return cd_generazione;
	}

	public void setCd_base(char value) {
		setCd_base(new Character(value));
	}

	public void setCd_base(Character value) {
		this.cd_base = value;
	}

	public Character getCd_base() {
		return cd_base;
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

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getORMID() {
		return getB();
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())));
	}

}
