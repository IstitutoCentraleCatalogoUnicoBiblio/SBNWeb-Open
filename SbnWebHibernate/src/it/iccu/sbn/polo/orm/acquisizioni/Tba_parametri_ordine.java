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
 * Parametri per ordine
 */
/**
 * ORM-Persistable Class
 */
public class Tba_parametri_ordine implements Serializable {

	private static final long serialVersionUID = -450095551950825557L;

	public Tba_parametri_ordine() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tba_parametri_ordine))
			return false;
		Tba_parametri_ordine tba_parametri_ordine = (Tba_parametri_ordine) aObj;
		if (getCd_biblioteca() == null
				&& tba_parametri_ordine.getCd_biblioteca() != null)
			return false;
		if (!getCd_biblioteca().equals(tba_parametri_ordine.getCd_biblioteca()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_biblioteca() != null) {
			hashcode = hashcode
					+ (getCd_biblioteca().getCd_biblioteca() == null ? 0
							: getCd_biblioteca().getCd_biblioteca().hashCode());
			hashcode = hashcode
					+ (getCd_biblioteca().getCd_polo() == null ? 0
							: getCd_biblioteca().getCd_polo().hashCode());
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private char gest_bil;

	private char gest_sez;

	private char gest_prof;

	private java.math.BigDecimal cod_prac;

	private String cod_sezione;

	private java.math.BigDecimal esercizio;

	private java.math.BigDecimal capitolo;

	private String cod_mat;

	private Integer cod_fornitore_a;

	private Integer cod_fornitore_l;

	private Integer cod_fornitore_d;

	private Integer cod_fornitore_v;

	private Integer cod_fornitore_c;

	private Integer cod_fornitore_r;

	private String ordini_aperti;

	private String ordini_chiusi;

	private String ordini_annullati;

	private String allineamento_prezzo;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private String cd_bib_google;

	private Integer cd_forn_google;

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

	public void setCd_biblioteca(
			it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_biblioteca() {
		return cd_biblioteca;
	}

	public String toString() {
		return String.valueOf(((getCd_biblioteca() == null) ? "" : String
				.valueOf(getCd_biblioteca().getCd_biblioteca())
				+ " "
				+ String.valueOf(getCd_biblioteca().getCd_polo())));
	}

	private boolean _saved = false;

	public void onSave() {
		_saved = true;
	}

	public void onLoad() {
		_saved = true;
	}

	public boolean isSaved() {
		return _saved;
	}

	public String getAllineamento_prezzo() {
		return allineamento_prezzo;
	}

	public void setAllineamento_prezzo(String allineamento_prezzo) {
		this.allineamento_prezzo = allineamento_prezzo;
	}

	public java.math.BigDecimal getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(java.math.BigDecimal capitolo) {
		this.capitolo = capitolo;
	}

	public String getCod_mat() {
		return cod_mat;
	}

	public void setCod_mat(String cod_mat) {
		this.cod_mat = cod_mat;
	}

	public java.math.BigDecimal getCod_prac() {
		return cod_prac;
	}

	public void setCod_prac(java.math.BigDecimal cod_prac) {
		this.cod_prac = cod_prac;
	}

	public String getCod_sezione() {
		return cod_sezione;
	}

	public void setCod_sezione(String cod_sezione) {
		this.cod_sezione = cod_sezione;
	}

	public java.math.BigDecimal getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(java.math.BigDecimal esercizio) {
		this.esercizio = esercizio;
	}

	public char getGest_bil() {
		return gest_bil;
	}

	public void setGest_bil(char gest_bil) {
		this.gest_bil = gest_bil;
	}

	public char getGest_prof() {
		return gest_prof;
	}

	public void setGest_prof(char gest_prof) {
		this.gest_prof = gest_prof;
	}

	public char getGest_sez() {
		return gest_sez;
	}

	public void setGest_sez(char gest_sez) {
		this.gest_sez = gest_sez;
	}

	public String getOrdini_annullati() {
		return ordini_annullati;
	}

	public void setOrdini_annullati(String ordini_annullati) {
		this.ordini_annullati = ordini_annullati;
	}

	public String getOrdini_aperti() {
		return ordini_aperti;
	}

	public void setOrdini_aperti(String ordini_aperti) {
		this.ordini_aperti = ordini_aperti;
	}

	public String getOrdini_chiusi() {
		return ordini_chiusi;
	}

	public void setOrdini_chiusi(String ordini_chiusi) {
		this.ordini_chiusi = ordini_chiusi;
	}

	public Integer getCod_fornitore_a() {
		return cod_fornitore_a;
	}

	public void setCod_fornitore_a(Integer cod_fornitore_a) {
		this.cod_fornitore_a = cod_fornitore_a;
	}

	public Integer getCod_fornitore_c() {
		return cod_fornitore_c;
	}

	public void setCod_fornitore_c(Integer cod_fornitore_c) {
		this.cod_fornitore_c = cod_fornitore_c;
	}

	public Integer getCod_fornitore_d() {
		return cod_fornitore_d;
	}

	public void setCod_fornitore_d(Integer cod_fornitore_d) {
		this.cod_fornitore_d = cod_fornitore_d;
	}

	public Integer getCod_fornitore_l() {
		return cod_fornitore_l;
	}

	public void setCod_fornitore_l(Integer cod_fornitore_l) {
		this.cod_fornitore_l = cod_fornitore_l;
	}

	public Integer getCod_fornitore_r() {
		return cod_fornitore_r;
	}

	public void setCod_fornitore_r(Integer cod_fornitore_r) {
		this.cod_fornitore_r = cod_fornitore_r;
	}

	public Integer getCod_fornitore_v() {
		return cod_fornitore_v;
	}

	public void setCod_fornitore_v(Integer cod_fornitore_v) {
		this.cod_fornitore_v = cod_fornitore_v;
	}

	public String getCd_bib_google() {
		return cd_bib_google;
	}

	public void setCd_bib_google(String cd_bib_google) {
		this.cd_bib_google = cd_bib_google;
	}

	public Integer getCd_forn_google() {
		return cd_forn_google;
	}

	public void setCd_forn_google(Integer cd_forn_google) {
		this.cd_forn_google = cd_forn_google;
	}

}
