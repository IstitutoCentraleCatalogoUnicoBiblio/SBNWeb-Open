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
package it.iccu.sbn.polo.orm.acquisizioni;

import java.io.Serializable;
/**
 * Parametri per buono d'ordine (intestazioni / fondo pagina)
 */
/**
 * ORM-Persistable Class
 */
public class Tba_parametri_buono_ordine implements Serializable {

	private static final long serialVersionUID = 2719951098943324035L;


	public Tba_parametri_buono_ordine() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tba_parametri_buono_ordine))
			return false;
		Tba_parametri_buono_ordine tba_parametri_buono_ordine = (Tba_parametri_buono_ordine)aObj;
		if (getCd_biblioteca() == null && tba_parametri_buono_ordine.getCd_biblioteca() != null)
			return false;
		if (!getCd_biblioteca().equals(tba_parametri_buono_ordine.getCd_biblioteca()))
			return false;
		if (getProgr() != tba_parametri_buono_ordine.getProgr())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_biblioteca() != null) {
			hashcode = hashcode + (getCd_biblioteca().getCd_biblioteca() == null ? 0 : getCd_biblioteca().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_biblioteca().getCd_polo() == null ? 0 : getCd_biblioteca().getCd_polo().hashCode());
		}
		hashcode = hashcode + (getProgr() == null ? 0 : getProgr().hashCode());
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private java.math.BigDecimal progr;

	private char codice_buono;

	private String descr_test;

	private String descr_foot;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private String descr_oggetto;

	private String descr_formulaintr;

	private char area_titolo;

	private char area_ediz;

	private char area_num;

	private char area_pub;

	private boolean logo = false;

	private String logo_img;

	private boolean prezzo = false;

	private boolean nprot = false;

	private boolean dataprot = false;

	private char rinnovo;

	private boolean firmadigit = false;

	private String firmadigit_img;

	private boolean ristampa = false;


	public void setProgr(java.math.BigDecimal value) {
		this.progr = value;
	}

	public java.math.BigDecimal getProgr() {
		return progr;
	}

	/**
	 * indicatore del tipo di numerazione del buono d'ordine (a=progressivo automatico; m=manuale)
	 */
	public void setCodice_buono(char value) {
		this.codice_buono = value;
	}

	/**
	 * indicatore del tipo di numerazione del buono d'ordine (a=progressivo automatico; m=manuale)
	 */
	public char getCodice_buono() {
		return codice_buono;
	}

	/**
	 * intestazione buono d'ordine
	 */
	public void setDescr_test(String value) {
		this.descr_test = value;
	}

	/**
	 * intestazione buono d'ordine
	 */
	public String getDescr_test() {
		return descr_test;
	}

	/**
	 * testo finale del buono d'ordine
	 */
	public void setDescr_foot(String value) {
		this.descr_foot = value;
	}

	/**
	 * testo finale del buono d'ordine
	 */
	public String getDescr_foot() {
		return descr_foot;
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

	public void setCd_biblioteca(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_biblioteca() {
		return cd_biblioteca;
	}

	public String toString() {
		return String.valueOf(((getCd_biblioteca() == null) ? "" : String.valueOf(getCd_biblioteca().getCd_biblioteca()) + " " + String.valueOf(getCd_biblioteca().getCd_polo())) + " " + getProgr());
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

	public char getArea_ediz() {
		return area_ediz;
	}

	public void setArea_ediz(char area_ediz) {
		this.area_ediz = area_ediz;
	}

	public char getArea_num() {
		return area_num;
	}

	public void setArea_num(char area_num) {
		this.area_num = area_num;
	}

	public char getArea_pub() {
		return area_pub;
	}

	public void setArea_pub(char area_pub) {
		this.area_pub = area_pub;
	}

	public char getArea_titolo() {
		return area_titolo;
	}

	public void setArea_titolo(char area_titolo) {
		this.area_titolo = area_titolo;
	}

	public boolean isDataprot() {
		return dataprot;
	}

	public void setDataprot(boolean dataprot) {
		this.dataprot = dataprot;
	}

	public String getDescr_formulaintr() {
		return descr_formulaintr;
	}

	public void setDescr_formulaintr(String descr_formulaintr) {
		this.descr_formulaintr = descr_formulaintr;
	}

	public String getDescr_oggetto() {
		return descr_oggetto;
	}

	public void setDescr_oggetto(String descr_oggetto) {
		this.descr_oggetto = descr_oggetto;
	}

	public boolean isFirmadigit() {
		return firmadigit;
	}

	public void setFirmadigit(boolean firmadigit) {
		this.firmadigit = firmadigit;
	}

	public String getFirmadigit_img() {
		return firmadigit_img;
	}

	public void setFirmadigit_img(String firmadigit_img) {
		this.firmadigit_img = firmadigit_img;
	}

	public boolean isLogo() {
		return logo;
	}

	public void setLogo(boolean logo) {
		this.logo = logo;
	}

	public String getLogo_img() {
		return logo_img;
	}

	public void setLogo_img(String logo_img) {
		this.logo_img = logo_img;
	}

	public boolean isNprot() {
		return nprot;
	}

	public void setNprot(boolean nprot) {
		this.nprot = nprot;
	}

	public boolean isPrezzo() {
		return prezzo;
	}

	public void setPrezzo(boolean prezzo) {
		this.prezzo = prezzo;
	}

	public char getRinnovo() {
		return rinnovo;
	}

	public void setRinnovo(char rinnovo) {
		this.rinnovo = rinnovo;
	}

	public boolean isRistampa() {
		return ristampa;
	}

	public void setRistampa(boolean ristampa) {
		this.ristampa = ristampa;
	}


}
