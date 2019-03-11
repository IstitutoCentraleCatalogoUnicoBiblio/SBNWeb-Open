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
public class Tbf_anagrafe_utenti_professionali implements Serializable {

	private static final long serialVersionUID = -1585148733731089194L;

	public Tbf_anagrafe_utenti_professionali() {
	}

	private int id_utente_professionale;

	private String nome;

	private String cognome;

	private java.sql.Timestamp ts_ins;

	private java.sql.Timestamp ts_var;

	private String ute_ins;

	private String ute_var;

	private char fl_canc;

	private java.util.Set Trf_utente_professionale_polo = new java.util.HashSet();

	private java.util.Set Trf_utente_professionale_biblioteca = new java.util.HashSet();

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_bibliotecario Tbf_bibliotecario;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web Tbf_utenti_professionali_web;

	private void setId_utente_professionale(int value) {
		this.id_utente_professionale = value;
	}

	public int getId_utente_professionale() {
		return id_utente_professionale;
	}

	public int getORMID() {
		return getId_utente_professionale();
	}

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setNome(String value) {
		this.nome = value;
	}

	public String getNome() {
		return nome;
	}

	public void setCognome(String value) {
		this.cognome = value;
	}

	public String getCognome() {
		return cognome;
	}

	public void setTrf_utente_professionale_polo(java.util.Set value) {
		this.Trf_utente_professionale_polo = value;
	}

	public java.util.Set getTrf_utente_professionale_polo() {
		return Trf_utente_professionale_polo;
	}


	public void setTrf_utente_professionale_biblioteca(java.util.Set value) {
		this.Trf_utente_professionale_biblioteca = value;
	}

	public java.util.Set getTrf_utente_professionale_biblioteca() {
		return Trf_utente_professionale_biblioteca;
	}


	public void setTbf_bibliotecario(it.finsiel.sbn.polo.orm.amministrazione.Tbf_bibliotecario value) {
		this.Tbf_bibliotecario = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_bibliotecario getTbf_bibliotecario() {
		return Tbf_bibliotecario;
	}

	public void setTbf_utenti_professionali_web(it.finsiel.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web value) {
		this.Tbf_utenti_professionali_web = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web getTbf_utenti_professionali_web() {
		return Tbf_utenti_professionali_web;
	}

	public String toString() {
		return String.valueOf(getId_utente_professionale());
	}

}
