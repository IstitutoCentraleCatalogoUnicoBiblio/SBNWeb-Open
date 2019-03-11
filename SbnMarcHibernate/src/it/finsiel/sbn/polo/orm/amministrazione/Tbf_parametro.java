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
public class Tbf_parametro implements Serializable {

	private static final long serialVersionUID = 1698110331054769532L;

	public Tbf_parametro() {
	}

	private int id_parametro;

	private String cd_livello;

	private String tp_ret_doc;

	private String tp_all_pref;

	private char cd_liv_ade;

	private char fl_spogli;

	private char fl_aut_superflui;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private char sololocale;

	private java.util.Set Tbf_polo = new java.util.HashSet();

	private java.util.Set Tbf_par_auth = new java.util.HashSet();

	private java.util.Set Tbf_par_mat = new java.util.HashSet();

	private java.util.Set Tbf_par_sem = new java.util.HashSet();

	private java.util.Set Tbf_biblioteca_in_polo = new java.util.HashSet();

	private java.util.Set Tbf_bibliotecario = new java.util.HashSet();

	public void setId_parametro(int value) {
		this.id_parametro = value;
	}

	public int getId_parametro() {
		return id_parametro;
	}

	public int getORMID() {
		return getId_parametro();
	}

	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	public String getCd_livello() {
		return cd_livello;
	}

	public void setTp_ret_doc(String value) {
		this.tp_ret_doc = value;
	}

	public String getTp_ret_doc() {
		return tp_ret_doc;
	}

	public void setTp_all_pref(String value) {
		this.tp_all_pref = value;
	}

	public String getTp_all_pref() {
		return tp_all_pref;
	}

	public void setCd_liv_ade(char value) {
		this.cd_liv_ade = value;
	}

	public char getCd_liv_ade() {
		return cd_liv_ade;
	}

	public void setFl_spogli(char value) {
		this.fl_spogli = value;
	}

	public char getFl_spogli() {
		return fl_spogli;
	}

	public void setFl_aut_superflui(char value) {
		this.fl_aut_superflui = value;
	}

	public char getFl_aut_superflui() {
		return fl_aut_superflui;
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

	public void setTbf_polo(java.util.Set value) {
		this.Tbf_polo = value;
	}

	public java.util.Set getTbf_polo() {
		return Tbf_polo;
	}


	public void setTbf_par_auth(java.util.Set value) {
		this.Tbf_par_auth = value;
	}

	public java.util.Set getTbf_par_auth() {
		return Tbf_par_auth;
	}


	public void setTbf_par_mat(java.util.Set value) {
		this.Tbf_par_mat = value;
	}

	public java.util.Set getTbf_par_mat() {
		return Tbf_par_mat;
	}


	public void setTbf_par_sem(java.util.Set value) {
		this.Tbf_par_sem = value;
	}

	public java.util.Set getTbf_par_sem() {
		return Tbf_par_sem;
	}


	public void setTbf_biblioteca_in_polo(java.util.Set value) {
		this.Tbf_biblioteca_in_polo = value;
	}

	public java.util.Set getTbf_biblioteca_in_polo() {
		return Tbf_biblioteca_in_polo;
	}


	public void setTbf_bibliotecario(java.util.Set value) {
		this.Tbf_bibliotecario = value;
	}

	public java.util.Set getTbf_bibliotecario() {
		return Tbf_bibliotecario;
	}


	public String toString() {
		return String.valueOf(getId_parametro());
	}

	public char getSololocale() {
		return sololocale;
	}

	public void setSololocale(char sololocale) {
		this.sololocale = sololocale;
	}

}
