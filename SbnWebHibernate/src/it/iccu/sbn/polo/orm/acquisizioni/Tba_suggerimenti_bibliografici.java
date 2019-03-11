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
 * Suggerimenti bibliotecari
 */
/**
 * ORM-Persistable Class
 */
public class Tba_suggerimenti_bibliografici implements Serializable {

	private static final long serialVersionUID = 7397384104320284146L;

	public Tba_suggerimenti_bibliografici() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tba_suggerimenti_bibliografici))
			return false;
		Tba_suggerimenti_bibliografici tba_suggerimenti_bibliografici = (Tba_suggerimenti_bibliografici)aObj;
		if (getCd_bib() == null && tba_suggerimenti_bibliografici.getCd_bib() != null)
			return false;
		if (!getCd_bib().equals(tba_suggerimenti_bibliografici.getCd_bib()))
			return false;
		if (getCod_sugg_bibl() != tba_suggerimenti_bibliografici.getCod_sugg_bibl())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_bib() != null) {
			hashcode = hashcode + (getCd_bib().getCd_biblioteca() == null ? 0 : getCd_bib().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_bib().getCd_polo() == null ? 0 : getCd_bib().getCd_polo().hashCode());
		}
		hashcode = hashcode + (getCod_sugg_bibl() == null ? 0 : getCod_sugg_bibl().hashCode());
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private java.math.BigDecimal cod_sugg_bibl;

	private java.util.Date data_sugg_bibl;

	private String note;

	private String msg_per_bibl;

	private String note_forn;

	private String cod_bib_cs;

	private String bid;

//	private java.math.BigDecimal cod_bibliotecario;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali cod_bibliotecario;

	private char stato_sugg;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche id_sez_acquis_bibliografiche;

	public void setCod_sugg_bibl(java.math.BigDecimal value) {
		this.cod_sugg_bibl = value;
	}

	public java.math.BigDecimal getCod_sugg_bibl() {
		return cod_sugg_bibl;
	}

	/**
	 * data del suggerimento
	 */
	public void setData_sugg_bibl(java.util.Date value) {
		this.data_sugg_bibl = value;
	}

	/**
	 * data del suggerimento
	 */
	public java.util.Date getData_sugg_bibl() {
		return data_sugg_bibl;
	}

	/**
	 * note relative al suggerimento del bibliotecario
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * note relative al suggerimento del bibliotecario
	 */
	public String getNote() {
		return note;
	}

	/**
	 * messaggio per il bibliotecario relativo al suggerimento
	 */
	public void setMsg_per_bibl(String value) {
		this.msg_per_bibl = value;
	}

	/**
	 * messaggio per il bibliotecario relativo al suggerimento
	 */
	public String getMsg_per_bibl() {
		return msg_per_bibl;
	}

	/**
	 * indicazioni di massima su uno o piu' fornitori per la creazione dell'ordine date dal bibliotecario
	 */
	public void setNote_forn(String value) {
		this.note_forn = value;
	}

	/**
	 * indicazioni di massima su uno o piu' fornitori per la creazione dell'ordine date dal bibliotecario
	 */
	public String getNote_forn() {
		return note_forn;
	}

	/**
	 * codice identificativo della biblioteca centro sistema di riferimento
	 */
	public void setCod_bib_cs(String value) {
		this.cod_bib_cs = value;
	}

	/**
	 * codice identificativo della biblioteca centro sistema di riferimento
	 */
	public String getCod_bib_cs() {
		return cod_bib_cs;
	}

	/**
	 * codice identificativo dell'oggetto bibliografico
	 */
	public void setBid(String value) {
		this.bid = value;
	}

	/**
	 * codice identificativo dell'oggetto bibliografico
	 */
	public String getBid() {
		return bid;
	}

	/**
	 * codice identificativo del bibliotecario
	 */
/*	public void setCod_bibliotecario(java.math.BigDecimal value) {
		this.cod_bibliotecario = value;
	}
*/
	/**
	 * codice identificativo del bibliotecario
	 */
/*	public java.math.BigDecimal getCod_bibliotecario() {
		return cod_bibliotecario;
	}
*/
	/**
	 * stato del suggerimento
	 */
	public void setStato_sugg(char value) {
		this.stato_sugg = value;
	}

	/**
	 * stato del suggerimento
	 */
	public char getStato_sugg() {
		return stato_sugg;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * data e ora d'inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * data e ora d'inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setId_sez_acquis_bibliografiche(it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche value) {
		this.id_sez_acquis_bibliografiche = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche getId_sez_acquis_bibliografiche() {
		return id_sez_acquis_bibliografiche;
	}

	public String toString() {
		return String.valueOf(((getCd_bib() == null) ? "" : String.valueOf(getCd_bib().getCd_biblioteca()) + " " + String.valueOf(getCd_bib().getCd_polo())) + " " + getCod_sugg_bibl());
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

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali getCod_bibliotecario() {
		return cod_bibliotecario;
	}

	public void setCod_bibliotecario(
			it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali cod_bibliotecario) {
		this.cod_bibliotecario = cod_bibliotecario;
	}


}
