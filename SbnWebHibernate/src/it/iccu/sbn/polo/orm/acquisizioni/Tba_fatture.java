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
 * Fatture
 */
/**
 * ORM-Persistable Class
 */
public class Tba_fatture implements Serializable {

	private static final long serialVersionUID = -6011855815742237186L;

	public Tba_fatture() {
	}

	private int id_fattura;

	private it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori cod_fornitore;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private java.math.BigDecimal anno_fattura;

	private int progr_fattura;

	private String num_fattura;

	private java.util.Date data_fattura;

	private java.util.Date data_reg;

	private java.math.BigDecimal importo;

	private java.math.BigDecimal sconto;

	private String valuta;

	private java.math.BigDecimal cambio;

	private char stato_fattura;

	private char tipo_fattura;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tba_righe_fatture = new java.util.HashSet();

	private void setId_fattura(int value) {
		this.id_fattura = value;
	}

	public int getId_fattura() {
		return id_fattura;
	}

	public int getORMID() {
		return getId_fattura();
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
	 * numero della fattura emessa dal fornitore
	 */
	public void setNum_fattura(String value) {
		this.num_fattura = value;
	}

	/**
	 * numero della fattura emessa dal fornitore
	 */
	public String getNum_fattura() {
		return num_fattura;
	}

	/**
	 * data della fattura del fornitore
	 */
	public void setData_fattura(java.util.Date value) {
		this.data_fattura = value;
	}

	/**
	 * data della fattura del fornitore
	 */
	public java.util.Date getData_fattura() {
		return data_fattura;
	}

	/**
	 * data di registrazione della fattura
	 */
	public void setData_reg(java.util.Date value) {
		this.data_reg = value;
	}

	/**
	 * data di registrazione della fattura
	 */
	public java.util.Date getData_reg() {
		return data_reg;
	}

	/**
	 * importo totale della fattura
	 */
	public void setImporto(java.math.BigDecimal value) {
		this.importo = value;
	}

	/**
	 * importo totale della fattura
	 */
	public java.math.BigDecimal getImporto() {
		return importo;
	}

	/**
	 * sconto
	 */
	public void setSconto(java.math.BigDecimal value) {
		this.sconto = value;
	}

	/**
	 * sconto
	 */
	public java.math.BigDecimal getSconto() {
		return sconto;
	}

	/**
	 * codice della valuta
	 */
	public void setValuta(String value) {
		this.valuta = value;
	}

	/**
	 * codice della valuta
	 */
	public String getValuta() {
		return valuta;
	}

	/**
	 * cambio corrente
	 */
	public void setCambio(java.math.BigDecimal value) {
		this.cambio = value;
	}

	/**
	 * cambio corrente
	 */
	public java.math.BigDecimal getCambio() {
		return cambio;
	}

	/**
	 * stato della fattura
	 */
	public void setStato_fattura(char value) {
		this.stato_fattura = value;
	}

	/**
	 * stato della fattura
	 */
	public char getStato_fattura() {
		return stato_fattura;
	}

	/**
	 * codice della tipologia della fattura
	 */
	public void setTipo_fattura(char value) {
		this.tipo_fattura = value;
	}

	/**
	 * codice della tipologia della fattura
	 */
	public char getTipo_fattura() {
		return tipo_fattura;
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

	public void setCod_fornitore(it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori value) {
		this.cod_fornitore = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori getCod_fornitore() {
		return cod_fornitore;
	}

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setTba_righe_fatture(java.util.Set value) {
		this.Tba_righe_fatture = value;
	}

	public java.util.Set getTba_righe_fatture() {
		return Tba_righe_fatture;
	}


	public String toString() {
		return String.valueOf(getId_fattura());
	}

}
