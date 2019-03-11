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
import java.util.HashSet;
import java.util.Set;
/**
 * Messaggi
 */
/**
 * ORM-Persistable Class
 */
public class Tra_messaggi implements Serializable {

	private static final long serialVersionUID = 7761136075119448146L;

	public Tra_messaggi() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tra_messaggi))
			return false;
		Tra_messaggi tra_messaggi = (Tra_messaggi)aObj;
/*		if ((getCd_polo() != null && !getCd_polo().equals(tra_messaggi.getCd_polo())) || (getCd_polo() == null && tra_messaggi.getCd_polo() != null))
			return false;
		if ((getCd_bib() != null && !getCd_bib().equals(tra_messaggi.getCd_bib())) || (getCd_bib() == null && tra_messaggi.getCd_bib() != null))
			return false;
*/		if (getCd_bib() == null && tra_messaggi.getCd_bib() != null)
			return false;
		if (!getCd_bib().equals(tra_messaggi.getCd_bib()))
			return false;

		if (getCod_msg() != tra_messaggi.getCod_msg())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
/*		hashcode = hashcode + (getCd_polo() == null ? 0 : getCd_polo().hashCode());
		hashcode = hashcode + (getCd_bib() == null ? 0 : getCd_bib().hashCode());
*/		if (getCd_bib() != null) {
			hashcode = hashcode + (getCd_bib().getCd_biblioteca() == null ? 0 : getCd_bib().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_bib().getCd_polo() == null ? 0 : getCd_bib().getCd_polo().hashCode());
		}


		hashcode = hashcode + getCod_msg();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

//	private String cd_polo;
//
//	private String cd_bib;

	private int cod_msg;

	private java.util.Date data_msg;

	private String note;

	private java.math.BigDecimal anno_fattura;

	private int progr_fattura;

	private char cod_tip_ord;

	private java.math.BigDecimal anno_ord;

	private int cod_ord;

	private char stato_msg;

	private String tipo_msg;

	private char tipo_invio;

//	private int cod_fornitore;

	private it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori cod_fornitore;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private Set Trp_messaggio_fascicolo = new HashSet();

/*	public void setCd_polo(String value) {
		this.cd_polo = value;
	}

	public String getCd_polo() {
		return cd_polo;
	}

	public void setCd_bib(String value) {
		this.cd_bib = value;
	}

	public String getCd_bib() {
		return cd_bib;
	}*/

	public void setCod_msg(int value) {
		this.cod_msg = value;
	}

	public int getCod_msg() {
		return cod_msg;
	}

	/**
	 * data del messaggio
	 */
	public void setData_msg(java.util.Date value) {
		this.data_msg = value;
	}

	/**
	 * data del messaggio
	 */
	public java.util.Date getData_msg() {
		return data_msg;
	}

	/**
	 * testo del messaggio
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * testo del messaggio
	 */
	public String getNote() {
		return note;
	}

	/**
	 * anno di registrazione della fattura
	 */
	public void setAnno_fattura(java.math.BigDecimal value) {
		this.anno_fattura = value;
	}

	/**
	 * anno di registrazione della fattura
	 */
	public java.math.BigDecimal getAnno_fattura() {
		return anno_fattura;
	}

	/**
	 * progressivo che identifica la fattura nell'ambito dell'anno
	 */
	public void setProgr_fattura(int value) {
		this.progr_fattura = value;
	}

	/**
	 * progressivo che identifica la fattura nell'ambito dell'anno
	 */
	public int getProgr_fattura() {
		return progr_fattura;
	}

	/**
	 * codice identificativo della tipologia dell'ordine
	 */
	public void setCod_tip_ord(char value) {
		this.cod_tip_ord = value;
	}

	/**
	 * codice identificativo della tipologia dell'ordine
	 */
	public char getCod_tip_ord() {
		return cod_tip_ord;
	}

	/**
	 * anno di acquisizione dell'ordine
	 */
	public void setAnno_ord(java.math.BigDecimal value) {
		this.anno_ord = value;
	}

	/**
	 * anno di acquisizione dell'ordine
	 */
	public java.math.BigDecimal getAnno_ord() {
		return anno_ord;
	}

	/**
	 * codice identificativo dell'ordine
	 */
	public void setCod_ord(int value) {
		this.cod_ord = value;
	}

	/**
	 * codice identificativo dell'ordine
	 */
	public int getCod_ord() {
		return cod_ord;
	}

	/**
	 * codice identificativo dello stato del messaggio
	 */
	public void setStato_msg(char value) {
		this.stato_msg = value;
	}

	/**
	 * codice identificativo dello stato del messaggio
	 */
	public char getStato_msg() {
		return stato_msg;
	}

	/**
	 * codice identificativo della tipologia di messaggio
	 */
	public void setTipo_msg(String value) {
		this.tipo_msg = value;
	}

	/**
	 * codice identificativo della tipologia di messaggio
	 */
	public String getTipo_msg() {
		return tipo_msg;
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
	 * codice identificativo del fornitore
	 */
/*	public void setCod_fornitore(int value) {
		this.cod_fornitore = value;
	}
*/
	/**
	 * codice identificativo del fornitore
	 */
/*	public int getCod_fornitore() {
		return cod_fornitore;
	}
*/
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
	 * data e ora dell'ultimo aggiornamento;
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * data e ora dell'ultimo aggiornamento;
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

/*	public String toString() {
		return String.valueOf(getCd_polo() + " " + getCd_bib() + " " + getCod_msg());
	}*/

	public String toString() {
		return String.valueOf(((getCd_bib() == null) ? "" : String.valueOf(getCd_bib().getCd_biblioteca()) + " " + String.valueOf(getCd_bib().getCd_polo())) + " " + getCod_msg());
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

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setCd_bib(
			it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib) {
		this.cd_bib = cd_bib;
	}

	public Set getTrp_messaggio_fascicolo() {
		return Trp_messaggio_fascicolo;
	}

	public void setTrp_messaggio_fascicolo(Set trp_messaggio_fascicolo) {
		Trp_messaggio_fascicolo = trp_messaggio_fascicolo;
	}


}
