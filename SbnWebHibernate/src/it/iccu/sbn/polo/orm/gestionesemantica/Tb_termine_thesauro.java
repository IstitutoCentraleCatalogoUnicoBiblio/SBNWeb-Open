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
 * TERMINI DESCRITTORI DI THESAURO (TPSDTH))
 */
/**
 * ORM-Persistable Class
 */
public class Tb_termine_thesauro implements Serializable {

	private static final long serialVersionUID = 9101620647848620339L;

	private String did;

	private String cd_the;

	private String ds_termine_thesauro;

	private String nota_termine_thesauro;

	private String ky_termine_thesauro;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tr_termini_termini = new java.util.HashSet();

	private java.util.Set Tr_termini_termini1 = new java.util.HashSet();

	private java.util.Set Trs_termini_titoli_biblioteche = new java.util.HashSet();

	public void setDid(String value) {
		this.did = value;
	}

	public String getDid() {
		return did;
	}

	public String getORMID() {
		return getDid();
	}

	/**
	 * codice thesauro
	 */
	public void setCd_the(String value) {
		this.cd_the = value;
	}

	/**
	 * codice thesauro
	 */
	public String getCd_the() {
		return cd_the;
	}

	/**
	 * descrizione del termine di thesauro
	 */
	public void setDs_termine_thesauro(String value) {
		this.ds_termine_thesauro = value;
	}

	/**
	 * descrizione del termine di thesauro
	 */
	public String getDs_termine_thesauro() {
		return ds_termine_thesauro;
	}

	/**
	 * note al termine di thesauro
	 */
	public void setNota_termine_thesauro(String value) {
		this.nota_termine_thesauro = value;
	}

	/**
	 * note al termine di thesauro
	 */
	public String getNota_termine_thesauro() {
		return nota_termine_thesauro;
	}

	/**
	 * chiave di ricerca del termine di thesauro
	 */
	public void setKy_termine_thesauro(String value) {
		this.ky_termine_thesauro = value;
	}

	/**
	 * chiave di ricerca del termine di thesauro
	 */
	public String getKy_termine_thesauro() {
		return ky_termine_thesauro;
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

	public void setTr_termini_termini(java.util.Set value) {
		this.Tr_termini_termini = value;
	}

	public java.util.Set getTr_termini_termini() {
		return Tr_termini_termini;
	}


	public void setTr_termini_termini1(java.util.Set value) {
		this.Tr_termini_termini1 = value;
	}

	public java.util.Set getTr_termini_termini1() {
		return Tr_termini_termini1;
	}


	public void setTrs_termini_titoli_biblioteche(java.util.Set value) {
		this.Trs_termini_titoli_biblioteche = value;
	}

	public java.util.Set getTrs_termini_titoli_biblioteche() {
		return Trs_termini_titoli_biblioteche;
	}


	public String toString() {
		return String.valueOf(getDid());
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
