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
 * DISCO SONORO
 */
/**
 * ORM-Persistable Class
 */
public class Tb_disco_sonoro implements Serializable {

	private static final long serialVersionUID = 3174448834250234804L;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private char cd_forma;

	private Character cd_velocita;

	private Character tp_suono;

	private Character cd_pista;

	private Character cd_dimensione;

	private Character cd_larg_nastro;

	private Character cd_configurazione;

	private String cd_mater_accomp;

	private Character cd_tecnica_regis;

	private Character cd_riproduzione;

	private Character tp_disco;

	private Character tp_taglio;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setCd_forma(char value) {
		this.cd_forma = value;
	}

	public char getCd_forma() {
		return cd_forma;
	}

	public void setCd_velocita(char value) {
		setCd_velocita(new Character(value));
	}

	public void setCd_velocita(Character value) {
		this.cd_velocita = value;
	}

	public Character getCd_velocita() {
		return cd_velocita;
	}

	public void setTp_suono(char value) {
		setTp_suono(new Character(value));
	}

	public void setTp_suono(Character value) {
		this.tp_suono = value;
	}

	public Character getTp_suono() {
		return tp_suono;
	}

	public void setCd_pista(char value) {
		setCd_pista(new Character(value));
	}

	public void setCd_pista(Character value) {
		this.cd_pista = value;
	}

	public Character getCd_pista() {
		return cd_pista;
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

	public void setCd_larg_nastro(char value) {
		setCd_larg_nastro(new Character(value));
	}

	public void setCd_larg_nastro(Character value) {
		this.cd_larg_nastro = value;
	}

	public Character getCd_larg_nastro() {
		return cd_larg_nastro;
	}

	public void setCd_configurazione(char value) {
		setCd_configurazione(new Character(value));
	}

	public void setCd_configurazione(Character value) {
		this.cd_configurazione = value;
	}

	public Character getCd_configurazione() {
		return cd_configurazione;
	}

	public void setCd_mater_accomp(String value) {
		this.cd_mater_accomp = value;
	}

	public String getCd_mater_accomp() {
		return cd_mater_accomp;
	}

	public void setCd_tecnica_regis(char value) {
		setCd_tecnica_regis(new Character(value));
	}

	public void setCd_tecnica_regis(Character value) {
		this.cd_tecnica_regis = value;
	}

	public Character getCd_tecnica_regis() {
		return cd_tecnica_regis;
	}

	public void setCd_riproduzione(char value) {
		setCd_riproduzione(new Character(value));
	}

	public void setCd_riproduzione(Character value) {
		this.cd_riproduzione = value;
	}

	public Character getCd_riproduzione() {
		return cd_riproduzione;
	}

	public void setTp_disco(char value) {
		setTp_disco(new Character(value));
	}

	public void setTp_disco(Character value) {
		this.tp_disco = value;
	}

	public Character getTp_disco() {
		return tp_disco;
	}

	public void setTp_taglio(char value) {
		setTp_taglio(new Character(value));
	}

	public void setTp_taglio(Character value) {
		this.tp_taglio = value;
	}

	public Character getTp_taglio() {
		return tp_taglio;
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
