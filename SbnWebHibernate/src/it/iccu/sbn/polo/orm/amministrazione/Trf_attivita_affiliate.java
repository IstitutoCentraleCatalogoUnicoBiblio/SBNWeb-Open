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
public class Trf_attivita_affiliate implements Serializable {

	private static final long serialVersionUID = 1420514841786985629L;

	public Trf_attivita_affiliate() {
	}

	private int id;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita cd_attivita;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib_affiliata;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib_centro_sistema;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private Character fl_canc;

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
		setFl_canc(new Character(value));
	}

	public void setFl_canc(Character value) {
		this.fl_canc = value;
	}

	public Character getFl_canc() {
		return fl_canc;
	}

	private void setId(int value) {
		this.id = value;
	}

	public int getId() {
		return id;
	}

	public int getORMID() {
		return getId();
	}

	public void setCd_attivita(it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita value) {
		this.cd_attivita = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita getCd_attivita() {
		return cd_attivita;
	}

	public void setCd_bib_affiliata(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib_affiliata = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib_affiliata() {
		return cd_bib_affiliata;
	}

	public void setCd_bib_centro_sistema(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib_centro_sistema = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib_centro_sistema() {
		return cd_bib_centro_sistema;
	}

	public String toString() {
		return String.valueOf(getId());
	}

}
