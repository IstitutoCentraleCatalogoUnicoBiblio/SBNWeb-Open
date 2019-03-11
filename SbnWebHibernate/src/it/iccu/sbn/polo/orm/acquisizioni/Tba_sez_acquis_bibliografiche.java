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

import org.hibernate.proxy.HibernateProxyHelper;
/**
 * Sezioni d'acquisizione bibliografiche
 */
/**
 * ORM-Persistable Class
 */
public class Tba_sez_acquis_bibliografiche implements Serializable {

	private static final long serialVersionUID = -1322007242631616133L;


	public Tba_sez_acquis_bibliografiche() {
	}

	private int id_sez_acquis_bibliografiche;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String cod_sezione;

	private String nome;

	private String note;

	private java.math.BigDecimal somma_disp;

	private java.math.BigDecimal budget;

	private java.math.BigDecimal anno_val;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Date data_val;

	private java.util.Set Tba_ordini = new java.util.HashSet();

	private java.util.Set Tba_profili_acquisto = new java.util.HashSet();

	private java.util.Set Tba_suggerimenti_bibliografici = new java.util.HashSet();


	private void setId_sez_acquis_bibliografiche(int value) {
		this.id_sez_acquis_bibliografiche = value;
	}

	public int getId_sez_acquis_bibliografiche() {
		return id_sez_acquis_bibliografiche;
	}

	public int getORMID() {
		return getId_sez_acquis_bibliografiche();
	}

	/**
	 * codice identificativo della sezione d'acquisizione
	 */
	public void setCod_sezione(String value) {
		this.cod_sezione = value;
	}

	/**
	 * codice identificativo della sezione d'acquisizione
	 */
	public String getCod_sezione() {
		return cod_sezione;
	}

	/**
	 * descrizione della sezione d'acquisizione
	 */
	public void setNome(String value) {
		this.nome = value;
	}

	/**
	 * descrizione della sezione d'acquisizione
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * note relative alla sezione d'acquisizione
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * note relative alla sezione d'acquisizione
	 */
	public String getNote() {
		return note;
	}

	/**
	 * somma disponibile per la sezione relativa agli acquisti
	 */
	public void setSomma_disp(java.math.BigDecimal value) {
		this.somma_disp = value;
	}

	/**
	 * somma disponibile per la sezione relativa agli acquisti
	 */
	public java.math.BigDecimal getSomma_disp() {
		return somma_disp;
	}

	/**
	 * somma iniziale disponibile per la sezione relativa agli acquisti
	 */
	public void setBudget(java.math.BigDecimal value) {
		this.budget = value;
	}

	/**
	 * somma iniziale disponibile per la sezione relativa agli acquisti
	 */
	public java.math.BigDecimal getBudget() {
		return budget;
	}

	/**
	 * anno di validita della sezione
	 */
	public void setAnno_val(java.math.BigDecimal value) {
		this.anno_val = value;
	}

	/**
	 * anno di validita della sezione
	 */
	public java.math.BigDecimal getAnno_val() {
		return anno_val;
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

	public void setTba_ordini(java.util.Set value) {
		this.Tba_ordini = value;
	}

	public java.util.Set getTba_ordini() {
		return Tba_ordini;
	}


	public void setTba_profili_acquisto(java.util.Set value) {
		this.Tba_profili_acquisto = value;
	}

	public java.util.Set getTba_profili_acquisto() {
		return Tba_profili_acquisto;
	}


	public void setTba_suggerimenti_bibliografici(java.util.Set value) {
		this.Tba_suggerimenti_bibliografici = value;
	}

	public java.util.Set getTba_suggerimenti_bibliografici() {
		return Tba_suggerimenti_bibliografici;
	}


	public String toString() {
		return String.valueOf(getId_sez_acquis_bibliografiche());
	}

	public java.util.Date getData_val() {
		return data_val;
	}

	public void setData_val(java.util.Date data_val) {
		this.data_val = data_val;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_sez_acquis_bibliografiche;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(HibernateProxyHelper.getClassWithoutInitializingProxy(obj) == Tba_sez_acquis_bibliografiche.class))
			return false;
		Tba_sez_acquis_bibliografiche other = (Tba_sez_acquis_bibliografiche) obj;
		if (id_sez_acquis_bibliografiche != other.id_sez_acquis_bibliografiche)
			return false;
		return true;
	}

}
