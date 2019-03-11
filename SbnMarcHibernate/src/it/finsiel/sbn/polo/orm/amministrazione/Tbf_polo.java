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
package it.finsiel.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Tbf_polo implements Serializable {

	private static final long serialVersionUID = -9087128429727257860L;

	public Tbf_polo() {
	}

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro id_parametro;

	private String cd_polo;

	private String url_indice;

	private String username;

	private String password;

	private String denominazione;

	private String ute_var;

	private java.sql.Timestamp ts_ins;

	private String ute_ins;

	private java.sql.Timestamp ts_var;

	private String url_polo;

	private String username_polo;

	private java.util.Set Tbf_gruppo_attivita = new java.util.HashSet();

	private java.util.Set Tbf_biblioteca_in_polo = new java.util.HashSet();

	private java.util.Set Trf_utente_professionale_polo = new java.util.HashSet();

	private java.util.Set Trf_attivita_polo = new java.util.HashSet();

	public void setCd_polo(String value) {
		this.cd_polo = value;
	}

	public String getCd_polo() {
		return cd_polo;
	}

	public String getORMID() {
		return getCd_polo();
	}

	public void setUrl_indice(String value) {
		this.url_indice = value;
	}

	public String getUrl_indice() {
		return url_indice;
	}

	public void setUsername(String value) {
		this.username = value;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String value) {
		this.password = value;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * Denominazione del polo
	 */
	public void setDenominazione(String value) {
		this.denominazione = value;
	}

	/**
	 * Denominazione del polo
	 */
	public String getDenominazione() {
		return denominazione;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setUrl_polo(String value) {
		this.url_polo = value;
	}

	public String getUrl_polo() {
		return url_polo;
	}

	public void setUsername_polo(String value) {
		this.username_polo = value;
	}

	public String getUsername_polo() {
		return username_polo;
	}

	public void setId_parametro(it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro value) {
		this.id_parametro = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro getId_parametro() {
		return id_parametro;
	}

	public void setTbf_gruppo_attivita(java.util.Set value) {
		this.Tbf_gruppo_attivita = value;
	}

	public java.util.Set getTbf_gruppo_attivita() {
		return Tbf_gruppo_attivita;
	}


	public void setTbf_biblioteca_in_polo(java.util.Set value) {
		this.Tbf_biblioteca_in_polo = value;
	}

	public java.util.Set getTbf_biblioteca_in_polo() {
		return Tbf_biblioteca_in_polo;
	}


	public void setTrf_utente_professionale_polo(java.util.Set value) {
		this.Trf_utente_professionale_polo = value;
	}

	public java.util.Set getTrf_utente_professionale_polo() {
		return Trf_utente_professionale_polo;
	}


	public void setTrf_attivita_polo(java.util.Set value) {
		this.Trf_attivita_polo = value;
	}

	public java.util.Set getTrf_attivita_polo() {
		return Trf_attivita_polo;
	}


	public String toString() {
		return String.valueOf(getCd_polo());
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
