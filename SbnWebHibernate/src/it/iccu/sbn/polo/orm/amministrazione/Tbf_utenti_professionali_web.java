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
package it.iccu.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Tbf_utenti_professionali_web implements Serializable {

	private static final long serialVersionUID = -4549720355597821040L;

	public Tbf_utenti_professionali_web() {
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali id_utente_professionale;

	private String userid;

	private String password;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private char change_password;

	private java.sql.Timestamp last_access;

	public void setUserid(String value) {
		this.userid = value;
	}

	public String getUserid() {
		return userid;
	}

	public void setPassword(String value) {
		this.password = value;
	}

	public String getPassword() {
		return password;
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

	public void setChange_password(char value) {
		this.change_password = value;
	}

	public char getChange_password() {
		return change_password;
	}

	public void setLast_access(java.sql.Timestamp value) {
		this.last_access = value;
	}

	public java.sql.Timestamp getLast_access() {
		return last_access;
	}

	public void setId_utente_professionale(it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali value) {
		this.id_utente_professionale = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali getId_utente_professionale() {
		return id_utente_professionale;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali getORMID() {
		return getId_utente_professionale();
	}

	public String toString() {
		return String.valueOf(((getId_utente_professionale() == null) ? "" : String.valueOf(getId_utente_professionale().getORMID())));
	}

}
