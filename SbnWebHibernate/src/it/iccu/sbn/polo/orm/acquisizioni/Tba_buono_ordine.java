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
 * Buono d'ordine
 */
/**
 * ORM-Persistable Class
 */
public class Tba_buono_ordine implements Serializable {

	private static final long serialVersionUID = 970408499968030518L;

	public Tba_buono_ordine() {
	}

	private int id_buono_ordine;

	private it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci cod_mat;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String buono_ord;

	private it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori cod_fornitore;

	private char stato_buono;

	private java.util.Date data_buono;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

//	private java.util.Set Tbb_capitoli_bilanci = new java.util.HashSet();

	private java.util.Set Tbb_bilanci = new java.util.HashSet();

	private java.util.Set Tra_elementi_buono_ordine = new java.util.HashSet();

	private void setId_buono_ordine(int value) {
		this.id_buono_ordine = value;
	}

	public int getId_buono_ordine() {
		return id_buono_ordine;
	}

	public int getORMID() {
		return getId_buono_ordine();
	}

	/**
	 * identificativo del buono d'ordine
	 */
	public void setBuono_ord(String value) {
		this.buono_ord = value;
	}

	/**
	 * identificativo del buono d'ordine
	 */
	public String getBuono_ord() {
		return buono_ord;
	}

	/**
	 * stato del buono d'ordine
	 */
	public void setStato_buono(char value) {
		this.stato_buono = value;
	}

	/**
	 * stato del buono d'ordine
	 */
	public char getStato_buono() {
		return stato_buono;
	}

	/**
	 * data del buono d'ordine
	 */
	public void setData_buono(java.util.Date value) {
		this.data_buono = value;
	}

	/**
	 * data del buono d'ordine
	 */
	public java.util.Date getData_buono() {
		return data_buono;
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



	public void setCod_mat(it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci value) {
		this.cod_mat = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci getCod_mat() {
		return cod_mat;
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

	public void setTbb_bilanci(java.util.Set value) {
		this.Tbb_bilanci = value;
	}

	public java.util.Set getTbb_bilanci() {
		return Tbb_bilanci;
	}


	public String toString() {
		return String.valueOf(getId_buono_ordine());
	}

	public java.util.Set getTra_elementi_buono_ordine() {
		return Tra_elementi_buono_ordine;
	}

	public void setTra_elementi_buono_ordine(java.util.Set tra_elementi_buono_ordine) {
		Tra_elementi_buono_ordine = tra_elementi_buono_ordine;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(char fl_canc) {
		this.fl_canc = fl_canc;
	}

/*	public java.util.Set getTbb_capitoli_bilanci() {
		return Tbb_capitoli_bilanci;
	}

	public void setTbb_capitoli_bilanci(java.util.Set tbb_capitoli_bilanci) {
		Tbb_capitoli_bilanci = tbb_capitoli_bilanci;
	}*/

}
