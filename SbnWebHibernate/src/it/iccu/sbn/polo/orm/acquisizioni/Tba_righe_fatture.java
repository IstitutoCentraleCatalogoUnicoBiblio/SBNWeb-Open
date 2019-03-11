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
 * Righe delle fatture
 */
/**
 * ORM-Persistable Class
 */
public class Tba_righe_fatture implements Serializable {

	private static final long serialVersionUID = 864076981109258986L;

	public Tba_righe_fatture() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tba_righe_fatture))
			return false;
		Tba_righe_fatture tba_righe_fatture = (Tba_righe_fatture)aObj;
		if (getId_fattura() == null && tba_righe_fatture.getId_fattura() != null)
			return false;
		if (!getId_fattura().equals(tba_righe_fatture.getId_fattura()))
			return false;
		if (getCd_biblioteca() == null && tba_righe_fatture.getCd_biblioteca() != null)
			return false;
		if (!getCd_biblioteca().equals(tba_righe_fatture.getCd_biblioteca()))
			return false;
		if (getRiga_fattura() != tba_righe_fatture.getRiga_fattura())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_fattura() != null) {
			hashcode = hashcode + getId_fattura().getORMID();
		}
		if (getCd_biblioteca() != null) {
			hashcode = hashcode + (getCd_biblioteca().getCd_biblioteca() == null ? 0 : getCd_biblioteca().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_biblioteca().getCd_polo() == null ? 0 : getCd_biblioteca().getCd_polo().hashCode());
		}
		hashcode = hashcode + getRiga_fattura();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.acquisizioni.Tba_fatture id_fattura;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private short riga_fattura;

	private it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini id_ordine;

	private it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci cod_mat;

	private String note;

	private java.math.BigDecimal importo_riga;

	private java.math.BigDecimal sconto_1;

	private java.math.BigDecimal sconto_2;

	private String cod_iva;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private Integer id_fattura_in_credito;

	private Integer riga_fattura_in_credito;

	private java.math.BigDecimal importo_tot_riga;

	public void setRiga_fattura(short value) {
		this.riga_fattura = value;
	}

	public short getRiga_fattura() {
		return riga_fattura;
	}

	/**
	 * note relative al materiale acquistato
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * note relative al materiale acquistato
	 */
	public String getNote() {
		return note;
	}

	/**
	 * imponibile
	 */
	public void setImporto_riga(java.math.BigDecimal value) {
		this.importo_riga = value;
	}

	/**
	 * imponibile
	 */
	public java.math.BigDecimal getImporto_riga() {
		return importo_riga;
	}

	/**
	 * primo sconto effettuato
	 */
	public void setSconto_1(java.math.BigDecimal value) {
		this.sconto_1 = value;
	}

	/**
	 * primo sconto effettuato
	 */
	public java.math.BigDecimal getSconto_1() {
		return sconto_1;
	}

	/**
	 * secondo sconto effettuato
	 */
	public void setSconto_2(java.math.BigDecimal value) {
		this.sconto_2 = value;
	}

	/**
	 * secondo sconto effettuato
	 */
	public java.math.BigDecimal getSconto_2() {
		return sconto_2;
	}

	/**
	 * codice identificativo dell'iva
	 */
	public void setCod_iva(String value) {
		this.cod_iva = value;
	}

	/**
	 * codice identificativo dell'iva
	 */
	public String getCod_iva() {
		return cod_iva;
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

	public void setCod_mat(it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci value) {
		this.cod_mat = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci getCod_mat() {
		return cod_mat;
	}

	public void setCd_biblioteca(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_biblioteca() {
		return cd_biblioteca;
	}

	public void setId_fattura(it.iccu.sbn.polo.orm.acquisizioni.Tba_fatture value) {
		this.id_fattura = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tba_fatture getId_fattura() {
		return id_fattura;
	}

	public void setId_ordine(it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini value) {
		this.id_ordine = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini getId_ordine() {
		return id_ordine;
	}

	public String toString() {
		return String.valueOf(((getId_fattura() == null) ? "" : String.valueOf(getId_fattura().getORMID())) + " " + ((getCd_biblioteca() == null) ? "" : String.valueOf(getCd_biblioteca().getCd_biblioteca()) + " " + String.valueOf(getCd_biblioteca().getCd_polo())) + " " + getRiga_fattura());
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


	public java.math.BigDecimal getImporto_tot_riga() {
		return importo_tot_riga;
	}

	public void setImporto_tot_riga(java.math.BigDecimal importo_tot_riga) {
		this.importo_tot_riga = importo_tot_riga;
	}

	public Integer getId_fattura_in_credito() {
		return id_fattura_in_credito;
	}

	public void setId_fattura_in_credito(Integer id_fattura_in_credito) {
		this.id_fattura_in_credito = id_fattura_in_credito;
	}

	public Integer getRiga_fattura_in_credito() {
		return riga_fattura_in_credito;
	}

	public void setRiga_fattura_in_credito(Integer riga_fattura_in_credito) {
		this.riga_fattura_in_credito = riga_fattura_in_credito;
	}



}
