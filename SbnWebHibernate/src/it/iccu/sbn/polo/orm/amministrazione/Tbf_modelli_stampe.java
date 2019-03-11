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
 * Persistenza dei modelli di stampa dinamica e statica
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_modelli_stampe implements Serializable {

	private static final long serialVersionUID = 922748395415463550L;

	public Tbf_modelli_stampe() {
	}

	private int id_modello;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String modello;

	private char tipo_modello;

	private String campi_valori_del_form;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private String descr;

	private String cd_attivita;

	private String subreport;

	private String descr_bib;

	private java.util.Set Tbc_default_inven_schede = new java.util.HashSet();

	/**
	 * Nome del modello (univoco per bibolioteca)
	 */
	public void setModello(String value) {
		this.modello = value;
	}

	/**
	 * Nome del modello (univoco per bibolioteca)
	 */
	public String getModello() {
		return modello;
	}

	/**
	 * CSV dei campi. Eg "nomecampo:valore| " dove il pipe è il separatore di campo ed i : è il separatore del valore
	 */
	public void setCampi_valori_del_form(String value) {
		this.campi_valori_del_form = value;
	}

	/**
	 * CSV dei campi. Eg "nomecampo:valore| " dove il pipe è il separatore di campo ed i : è il separatore del valore
	 */
	public String getCampi_valori_del_form() {
		return campi_valori_del_form;
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

	/**
	 * C = comma separated values
	 * J = Jrxml
	 */
	public void setTipo_modello(char value) {
		this.tipo_modello = value;
	}

	/**
	 * C = comma separated values
	 * J = Jrxml
	 */
	public char getTipo_modello() {
		return tipo_modello;
	}

	private void setId_modello(int value) {
		this.id_modello = value;
	}

	public int getId_modello() {
		return id_modello;
	}

	public int getORMID() {
		return getId_modello();
	}

	public void setDescr(String value) {
		this.descr = value;
	}

	public String getDescr() {
		return descr;
	}

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setTbc_default_inven_schede(java.util.Set value) {
		this.Tbc_default_inven_schede = value;
	}

	public java.util.Set getTbc_default_inven_schede() {
		return Tbc_default_inven_schede;
	}


	public String toString() {
		return String.valueOf(getId_modello());
	}

	public String getCd_attivita() {
		return cd_attivita;
	}

	public void setCd_attivita(String cd_attivita) {
		this.cd_attivita = cd_attivita;
	}

	public String getSubreport() {
		return subreport;
	}

	public void setSubreport(String subreport) {
		this.subreport = subreport;
	}

	public String getDescr_bib() {
		return descr_bib;
	}

	public void setDescr_bib(String descr_bib) {
		this.descr_bib = descr_bib;
	}

}
