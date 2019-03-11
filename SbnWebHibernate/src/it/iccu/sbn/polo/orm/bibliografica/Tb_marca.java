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
 * MARCHE EDITORIALI
 */
/**
 * ORM-Persistable Class
 */
public class Tb_marca implements Serializable {

	private static final long serialVersionUID = -2965544540481930780L;

	private String mid;

	private String cd_livello;

	private char fl_speciale;

	private String ds_marca;

	private String nota_marca;

	private String ds_motto;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private char fl_condiviso;

	private String ute_condiviso;

	private java.sql.Timestamp ts_condiviso;

	private java.util.Set Tb_parola = new java.util.HashSet();

	private java.util.Set Tr_aut_mar = new java.util.HashSet();

	private java.util.Set Tr_mar_bib = new java.util.HashSet();

	private java.util.Set Tr_rep_mar = new java.util.HashSet();

	private java.util.Set Tr_tit_mar = new java.util.HashSet();

	public void setMid(String value) {
		this.mid = value;
	}

	public String getMid() {
		return mid;
	}

	public String getORMID() {
		return getMid();
	}

	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	public String getCd_livello() {
		return cd_livello;
	}

	public void setFl_speciale(char value) {
		this.fl_speciale = value;
	}

	public char getFl_speciale() {
		return fl_speciale;
	}

	public void setDs_marca(String value) {
		this.ds_marca = value;
	}

	public String getDs_marca() {
		return ds_marca;
	}

	public void setNota_marca(String value) {
		this.nota_marca = value;
	}

	public String getNota_marca() {
		return nota_marca;
	}

	public void setDs_motto(String value) {
		this.ds_motto = value;
	}

	public String getDs_motto() {
		return ds_motto;
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

	/**
	 * Flag di condivisione gestione del legame con indice
	 */
	public void setFl_condiviso(char value) {
		this.fl_condiviso = value;
	}

	/**
	 * Flag di condivisione gestione del legame con indice
	 */
	public char getFl_condiviso() {
		return fl_condiviso;
	}

	/**
	 * Timestamp di condivisione gestione del legame con indice
	 */
	public void setUte_condiviso(String value) {
		this.ute_condiviso = value;
	}

	/**
	 * Timestamp di condivisione gestione del legame con indice
	 */
	public String getUte_condiviso() {
		return ute_condiviso;
	}

	/**
	 * Utente che ha attivato la gestione condivisa del legame con indice
	 */
	public void setTs_condiviso(java.sql.Timestamp value) {
		this.ts_condiviso = value;
	}

	/**
	 * Utente che ha attivato la gestione condivisa del legame con indice
	 */
	public java.sql.Timestamp getTs_condiviso() {
		return ts_condiviso;
	}

	public void setTb_parola(java.util.Set value) {
		this.Tb_parola = value;
	}

	public java.util.Set getTb_parola() {
		return Tb_parola;
	}


	public void setTr_aut_mar(java.util.Set value) {
		this.Tr_aut_mar = value;
	}

	public java.util.Set getTr_aut_mar() {
		return Tr_aut_mar;
	}


	public void setTr_mar_bib(java.util.Set value) {
		this.Tr_mar_bib = value;
	}

	public java.util.Set getTr_mar_bib() {
		return Tr_mar_bib;
	}


	public void setTr_rep_mar(java.util.Set value) {
		this.Tr_rep_mar = value;
	}

	public java.util.Set getTr_rep_mar() {
		return Tr_rep_mar;
	}


	public void setTr_tit_mar(java.util.Set value) {
		this.Tr_tit_mar = value;
	}

	public java.util.Set getTr_tit_mar() {
		return Tr_tit_mar;
	}


	public String toString() {
		return String.valueOf(getMid());
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
