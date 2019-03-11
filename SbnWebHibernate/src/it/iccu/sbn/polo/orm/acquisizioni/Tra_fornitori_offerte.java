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
 * Fornitori che hanno risposto alla richiesta d'offerta (gara)
 */
/**
 * ORM-Persistable Class
 */
public class Tra_fornitori_offerte implements Serializable {

	private static final long serialVersionUID = -6025112907376593469L;

	public Tra_fornitori_offerte() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tra_fornitori_offerte))
			return false;
		Tra_fornitori_offerte tra_fornitori_offerte = (Tra_fornitori_offerte)aObj;
		if (getCd_bib() == null && tra_fornitori_offerte.getCd_bib() != null)
			return false;
		if (!getCd_bib().equals(tra_fornitori_offerte.getCd_bib()))
			return false;
		if (getCod_fornitore() != tra_fornitori_offerte.getCod_fornitore())
			return false;
		if (getCod_rich_off() != tra_fornitori_offerte.getCod_rich_off())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_bib() != null) {
			hashcode = hashcode + (getCd_bib().getCd_biblioteca() == null ? 0 : getCd_bib().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_bib().getCd_polo() == null ? 0 : getCd_bib().getCd_polo().hashCode());
		}
		hashcode = hashcode + getCod_fornitore().getCod_fornitore();
		hashcode = hashcode + (int) getCod_rich_off();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

//	private int cod_fornitore;
	private it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori  cod_fornitore;

	private long cod_rich_off;


	private String note;

	private char stato_gara;

	private char tipo_invio;

	private java.util.Date data_invio;

	private String risp_da_risp;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

/*	public void setCod_fornitore(int value) {
		this.cod_fornitore = value;
	}

	public int getCod_fornitore() {
		return cod_fornitore;
	}*/

	public void setCod_rich_off(long value) {
		this.cod_rich_off = value;
	}

	public long getCod_rich_off() {
		return cod_rich_off;
	}

	/**
	 * note riferite al fornitore
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * note riferite al fornitore
	 */
	public String getNote() {
		return note;
	}

	/**
	 * stato del partecipante alla gara
	 */
	public void setStato_gara(char value) {
		this.stato_gara = value;
	}

	/**
	 * stato del partecipante alla gara
	 */
	public char getStato_gara() {
		return stato_gara;
	}

	/**
	 * codice identificativo del tipo invio
	 */
	public void setTipo_invio(char value) {
		this.tipo_invio = value;
	}

	/**
	 * codice identificativo del tipo invio
	 */
	public char getTipo_invio() {
		return tipo_invio;
	}

	/**
	 * data di invio al fornitore della richiesta d'offerta
	 */
	public void setData_invio(java.util.Date value) {
		this.data_invio = value;
	}

	/**
	 * data di invio al fornitore della richiesta d'offerta
	 */
	public java.util.Date getData_invio() {
		return data_invio;
	}

	/**
	 * messaggio di risposta del fornitore alla gara indetta
	 */
	public void setRisp_da_risp(String value) {
		this.risp_da_risp = value;
	}

	/**
	 * messaggio di risposta del fornitore alla gara indetta
	 */
	public String getRisp_da_risp() {
		return risp_da_risp;
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

	public String toString() {
		return String.valueOf(((getCd_bib() == null) ? "" : String.valueOf(getCd_bib().getCd_biblioteca()) + " " + String.valueOf(getCd_bib().getCd_polo())) + " " + getCod_fornitore() + " " + getCod_rich_off());
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

	public it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori getCod_fornitore() {
		return cod_fornitore;
	}

	public void setCod_fornitore(
			it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori cod_fornitore) {
		this.cod_fornitore = cod_fornitore;
	}




}
