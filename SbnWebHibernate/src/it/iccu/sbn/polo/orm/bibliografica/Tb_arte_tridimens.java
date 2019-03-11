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
 * OGGETTI D'ARTE TRIDIMENSIONALE
 */
/**
 * ORM-Persistable Class
 */
public class Tb_arte_tridimens implements Serializable {

	private static final long serialVersionUID = 5683490186709515778L;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String cd_designazione;

	private String tp_materiale_1;

	private String tp_materiale_2;

	private String tp_materiale_3;

	private Character cd_colore;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setCd_designazione(String value) {
		this.cd_designazione = value;
	}

	public String getCd_designazione() {
		return cd_designazione;
	}

	public void setTp_materiale_1(String value) {
		this.tp_materiale_1 = value;
	}

	public String getTp_materiale_1() {
		return tp_materiale_1;
	}

	public void setTp_materiale_2(String value) {
		this.tp_materiale_2 = value;
	}

	public String getTp_materiale_2() {
		return tp_materiale_2;
	}

	public void setTp_materiale_3(String value) {
		this.tp_materiale_3 = value;
	}

	public String getTp_materiale_3() {
		return tp_materiale_3;
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
