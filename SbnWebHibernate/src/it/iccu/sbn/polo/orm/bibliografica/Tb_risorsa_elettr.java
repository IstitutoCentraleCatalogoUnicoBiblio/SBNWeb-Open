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
 * RISORSA ELETTRONICA
 */
/**
 * ORM-Persistable Class
 */
public class Tb_risorsa_elettr implements Serializable {

	private static final long serialVersionUID = -3302280038937307935L;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private char tp_file;

	private Character cd_designazione;

	private Character cd_colore;

	private Character cd_dimensione;

	private Character cd_suono;

	private String cd_bit_immagine;

	private Character cd_formato_file;

	private Character cd_qualita;

	private Character cd_origine;

	private Character cd_base;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setTp_file(char value) {
		this.tp_file = value;
	}

	public char getTp_file() {
		return tp_file;
	}

	public void setCd_designazione(char value) {
		setCd_designazione(new Character(value));
	}

	public void setCd_designazione(Character value) {
		this.cd_designazione = value;
	}

	public Character getCd_designazione() {
		return cd_designazione;
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

	public void setCd_dimensione(char value) {
		setCd_dimensione(new Character(value));
	}

	public void setCd_dimensione(Character value) {
		this.cd_dimensione = value;
	}

	public Character getCd_dimensione() {
		return cd_dimensione;
	}

	public void setCd_suono(char value) {
		setCd_suono(new Character(value));
	}

	public void setCd_suono(Character value) {
		this.cd_suono = value;
	}

	public Character getCd_suono() {
		return cd_suono;
	}

	public void setCd_bit_immagine(String value) {
		this.cd_bit_immagine = value;
	}

	public String getCd_bit_immagine() {
		return cd_bit_immagine;
	}

	public void setCd_formato_file(char value) {
		setCd_formato_file(new Character(value));
	}

	public void setCd_formato_file(Character value) {
		this.cd_formato_file = value;
	}

	public Character getCd_formato_file() {
		return cd_formato_file;
	}

	public void setCd_qualita(char value) {
		setCd_qualita(new Character(value));
	}

	public void setCd_qualita(Character value) {
		this.cd_qualita = value;
	}

	public Character getCd_qualita() {
		return cd_qualita;
	}

	public void setCd_origine(char value) {
		setCd_origine(new Character(value));
	}

	public void setCd_origine(Character value) {
		this.cd_origine = value;
	}

	public Character getCd_origine() {
		return cd_origine;
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
