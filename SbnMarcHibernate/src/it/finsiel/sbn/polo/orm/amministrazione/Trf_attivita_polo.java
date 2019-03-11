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
public class Trf_attivita_polo implements Serializable {

	private static final long serialVersionUID = -4446273801971061818L;

	public Trf_attivita_polo() {
	}

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo cd_polo;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita cd_attivita;

	private int id_attivita_polo;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Trf_gruppo_attivita_polo = new java.util.HashSet();

	private void setId_attivita_polo(int value) {
		this.id_attivita_polo = value;
	}

	public int getId_attivita_polo() {
		return id_attivita_polo;
	}

	public int getORMID() {
		return getId_attivita_polo();
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

	public void setCd_polo(it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo value) {
		this.cd_polo = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo getCd_polo() {
		return cd_polo;
	}

	public void setCd_attivita(it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita value) {
		this.cd_attivita = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita getCd_attivita() {
		return cd_attivita;
	}

	public void setTrf_gruppo_attivita_polo(java.util.Set value) {
		this.Trf_gruppo_attivita_polo = value;
	}

	public java.util.Set getTrf_gruppo_attivita_polo() {
		return Trf_gruppo_attivita_polo;
	}


	public String toString() {
		return String.valueOf(getId_attivita_polo());
	}

}
