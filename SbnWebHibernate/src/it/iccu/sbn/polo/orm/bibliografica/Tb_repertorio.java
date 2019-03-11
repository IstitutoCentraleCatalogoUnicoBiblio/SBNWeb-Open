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
 * Repertori (marche/autori/titoli)
 */
/**
 * ORM-Persistable Class
 */
public class Tb_repertorio implements Serializable {

	private static final long serialVersionUID = -5016164266641234619L;

	private int id_repertorio;

	private String cd_sig_repertorio;

	private String ds_repertorio;

	private char tp_repertorio;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tr_rep_tit = new java.util.HashSet();

	private java.util.Set Tr_rep_mar = new java.util.HashSet();

	private java.util.Set Tr_rep_aut = new java.util.HashSet();

	private void setId_repertorio(int value) {
		this.id_repertorio = value;
	}

	public int getId_repertorio() {
		return id_repertorio;
	}

	public int getORMID() {
		return getId_repertorio();
	}

	public void setCd_sig_repertorio(String value) {
		this.cd_sig_repertorio = value;
	}

	public String getCd_sig_repertorio() {
		return cd_sig_repertorio;
	}

	public void setDs_repertorio(String value) {
		this.ds_repertorio = value;
	}

	public String getDs_repertorio() {
		return ds_repertorio;
	}

	public void setTp_repertorio(char value) {
		this.tp_repertorio = value;
	}

	public char getTp_repertorio() {
		return tp_repertorio;
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

	public void setTr_rep_tit(java.util.Set value) {
		this.Tr_rep_tit = value;
	}

	public java.util.Set getTr_rep_tit() {
		return Tr_rep_tit;
	}


	public void setTr_rep_mar(java.util.Set value) {
		this.Tr_rep_mar = value;
	}

	public java.util.Set getTr_rep_mar() {
		return Tr_rep_mar;
	}


	public void setTr_rep_aut(java.util.Set value) {
		this.Tr_rep_aut = value;
	}

	public java.util.Set getTr_rep_aut() {
		return Tr_rep_aut;
	}


	public String toString() {
		return String.valueOf(getId_repertorio());
	}

}
