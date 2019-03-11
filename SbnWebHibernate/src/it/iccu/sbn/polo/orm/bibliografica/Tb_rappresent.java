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
 * RAPPRESENTAZIONE
 */
/**
 * ORM-Persistable Class
 */
public class Tb_rappresent implements Serializable {

	private static final long serialVersionUID = 6876918101049063909L;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private Character tp_genere;

	private String aa_rapp;

	private String ds_periodo;

	private String ds_teatro;

	private String ds_luogo_rapp;

	private String ds_occasione;

	private String nota_rapp;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setTp_genere(char value) {
		setTp_genere(new Character(value));
	}

	public void setTp_genere(Character value) {
		this.tp_genere = value;
	}

	public Character getTp_genere() {
		return tp_genere;
	}

	public void setAa_rapp(String value) {
		this.aa_rapp = value;
	}

	public String getAa_rapp() {
		return aa_rapp;
	}

	public void setDs_periodo(String value) {
		this.ds_periodo = value;
	}

	public String getDs_periodo() {
		return ds_periodo;
	}

	public void setDs_teatro(String value) {
		this.ds_teatro = value;
	}

	public String getDs_teatro() {
		return ds_teatro;
	}

	public void setDs_luogo_rapp(String value) {
		this.ds_luogo_rapp = value;
	}

	public String getDs_luogo_rapp() {
		return ds_luogo_rapp;
	}

	public void setDs_occasione(String value) {
		this.ds_occasione = value;
	}

	public String getDs_occasione() {
		return ds_occasione;
	}

	public void setNota_rapp(String value) {
		this.nota_rapp = value;
	}

	public String getNota_rapp() {
		return nota_rapp;
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
