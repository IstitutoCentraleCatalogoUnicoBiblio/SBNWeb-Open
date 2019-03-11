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
package it.iccu.sbn.polo.orm.servizi;

import java.io.Serializable;
/**
 * Tabella che mette in relazione le tabelle dei servizi con le tabelle dei codici
 */
/**
 * ORM-Persistable Class
 */
public class Trl_relazioni_servizi implements Serializable {

	private static final long serialVersionUID = -7154617266335011488L;

	public Trl_relazioni_servizi() {
	}

	private int id;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String cd_relazione;

	private String tabella_di_relazione;

	private int id_relazione_tabella_di_relazione;

	private String tabella_tb_codici;

	private String id_relazione_tb_codici;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private void setId(int value) {
		this.id = value;
	}

	public int getId() {
		return id;
	}

	public int getORMID() {
		return getId();
	}

	public void setTabella_di_relazione(String value) {
		this.tabella_di_relazione = value;
	}

	public String getTabella_di_relazione() {
		return tabella_di_relazione;
	}

	public void setId_relazione_tabella_di_relazione(int value) {
		this.id_relazione_tabella_di_relazione = value;
	}

	public int getId_relazione_tabella_di_relazione() {
		return id_relazione_tabella_di_relazione;
	}

	public void setTabella_tb_codici(String value) {
		this.tabella_tb_codici = value;
	}

	public String getTabella_tb_codici() {
		return tabella_tb_codici;
	}

	public void setId_relazione_tb_codici(String value) {
		this.id_relazione_tb_codici = value;
	}

	public String getId_relazione_tb_codici() {
		return id_relazione_tb_codici;
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

	public void setCd_relazione(String value) {
		this.cd_relazione = value;
	}

	public String getCd_relazione() {
		return cd_relazione;
	}

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public String toString() {
		return String.valueOf(getId());
	}

}
