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
 * Richieste d'offerta
 */
/**
 * ORM-Persistable Class
 */
public class Tba_richieste_offerta implements Serializable {

	private static final long serialVersionUID = -862055615199502936L;

	public Tba_richieste_offerta() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tba_richieste_offerta))
			return false;
		Tba_richieste_offerta tba_richieste_offerta = (Tba_richieste_offerta)aObj;
		if (getCd_bib() == null && tba_richieste_offerta.getCd_bib() != null)
			return false;
		if (!getCd_bib().equals(tba_richieste_offerta.getCd_bib()))
			return false;
		if (getCod_rich_off() != tba_richieste_offerta.getCod_rich_off())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_bib() != null) {
			hashcode = hashcode + (getCd_bib().getCd_biblioteca() == null ? 0 : getCd_bib().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_bib().getCd_polo() == null ? 0 : getCd_bib().getCd_polo().hashCode());
		}
		hashcode = hashcode + (int) getCod_rich_off();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private long cod_rich_off;

	private java.util.Date data_rich_off;

	private java.math.BigDecimal prezzo_rich;

	private int num_copie;

	private String note;

	private char stato_rich_off;

//	private String bid;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo bid;

	private java.util.Set Tra_fornitori_offerte = new java.util.HashSet();

	public void setCod_rich_off(long value) {
		this.cod_rich_off = value;
	}

	public long getCod_rich_off() {
		return cod_rich_off;
	}

	/**
	 * data della richiesta d'offerta
	 */
	public void setData_rich_off(java.util.Date value) {
		this.data_rich_off = value;
	}

	/**
	 * data della richiesta d'offerta
	 */
	public java.util.Date getData_rich_off() {
		return data_rich_off;
	}

	/**
	 * prezzo indicativo per iniziare la gara
	 */
	public void setPrezzo_rich(java.math.BigDecimal value) {
		this.prezzo_rich = value;
	}

	/**
	 * prezzo indicativo per iniziare la gara
	 */
	public java.math.BigDecimal getPrezzo_rich() {
		return prezzo_rich;
	}

	/**
	 * numero di copie richieste in acquisto
	 */
	public void setNum_copie(int value) {
		this.num_copie = value;
	}

	/**
	 * numero di copie richieste in acquisto
	 */
	public int getNum_copie() {
		return num_copie;
	}

	/**
	 * osservazioni relative all'ordine
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * osservazioni relative all'ordine
	 */
	public String getNote() {
		return note;
	}

	/**
	 * codice dello stato della richiesta di offerta
	 */
	public void setStato_rich_off(char value) {
		this.stato_rich_off = value;
	}

	/**
	 * codice dello stato della richiesta di offerta
	 */
	public char getStato_rich_off() {
		return stato_rich_off;
	}

	/**
	 * codice identificativo dell'oggetto bibliografico
	 */
/*	public void setBid(String value) {
		this.bid = value;
	}*/

	/**
	 * codice identificativo dell'oggetto bibliografico
	 */
/*	public String getBid() {
		return bid;
	}*/

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
		return String.valueOf(((getCd_bib() == null) ? "" : String.valueOf(getCd_bib().getCd_biblioteca()) + " " + String.valueOf(getCd_bib().getCd_polo())) + " " + getCod_rich_off());
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

	public java.util.Set getTra_fornitori_offerte() {
		return Tra_fornitori_offerte;
	}

	public void setTra_fornitori_offerte(java.util.Set tra_fornitori_offerte) {
		Tra_fornitori_offerte = tra_fornitori_offerte;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getBid() {
		return bid;
	}

	public void setBid(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo bid) {
		this.bid = bid;
	}



}
