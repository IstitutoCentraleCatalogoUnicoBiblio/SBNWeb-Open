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
package it.iccu.sbn.polo.orm.servizi;

import java.io.Serializable;
/**
 * Materie d'interesse
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_materie implements Serializable {

	private static final long serialVersionUID = -5982416340008984901L;

	public Tbl_materie() {
	}

	private int id_materia;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private String cod_mat;

	private String descr;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Trl_materie_utenti = new java.util.HashSet();

	private void setId_materia(int value) {
		this.id_materia = value;
	}

	public int getId_materia() {
		return id_materia;
	}

	public int getORMID() {
		return getId_materia();
	}

	/**
	 * codice della materia di interesse per l'utente di una biblioteca
	 */
	public void setCod_mat(String value) {
		this.cod_mat = value;
	}

	/**
	 * codice della materia di interesse per l'utente di una biblioteca
	 */
	public String getCod_mat() {
		return cod_mat;
	}

	/**
	 * descrizione della materia di interesse
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * descrizione della materia di interesse
	 */
	public String getDescr() {
		return descr;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setCd_biblioteca(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_biblioteca() {
		return cd_biblioteca;
	}

	public void setTrl_materie_utenti(java.util.Set value) {
		this.Trl_materie_utenti = value;
	}

	public java.util.Set getTrl_materie_utenti() {
		return Trl_materie_utenti;
	}


	public String toString() {
		return String.valueOf(getId_materia());
	}

}
