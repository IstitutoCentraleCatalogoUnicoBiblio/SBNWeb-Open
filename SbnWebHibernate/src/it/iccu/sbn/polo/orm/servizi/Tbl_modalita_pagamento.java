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
 * Modalità di pagamento
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_modalita_pagamento implements Serializable {

	private static final long serialVersionUID = 4500580261688320164L;

	public Tbl_modalita_pagamento() {
	}

	private int id_modalita_pagamento;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private short cod_mod_pag;

	private String descr;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tbl_richiesta_servizio = new java.util.HashSet();

	private void setId_modalita_pagamento(int value) {
		this.id_modalita_pagamento = value;
	}

	public int getId_modalita_pagamento() {
		return id_modalita_pagamento;
	}

	public int getORMID() {
		return getId_modalita_pagamento();
	}

	/**
	 * codice della modalita' di pagamentio
	 */
	public void setCod_mod_pag(short value) {
		this.cod_mod_pag = value;
	}

	/**
	 * codice della modalita' di pagamentio
	 */
	public short getCod_mod_pag() {
		return cod_mod_pag;
	}

	/**
	 * descrizione della modalita' di pagamento
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * descrizione della modalita' di pagamento
	 */
	public String getDescr() {
		return descr;
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

	public void setTbl_richiesta_servizio(java.util.Set value) {
		this.Tbl_richiesta_servizio = value;
	}

	public java.util.Set getTbl_richiesta_servizio() {
		return Tbl_richiesta_servizio;
	}


	public String toString() {
		return String.valueOf(getId_modalita_pagamento());
	}

}
