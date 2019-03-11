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
 * Sezioni d'acquisizione di fornitori
 */
/**
 * ORM-Persistable Class
 */
public class Tra_sez_acquisizione_fornitori implements Serializable {

	private static final long serialVersionUID = -570347644375845352L;

	public Tra_sez_acquisizione_fornitori() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tra_sez_acquisizione_fornitori))
			return false;
		Tra_sez_acquisizione_fornitori tra_sez_acquisizione_fornitori = (Tra_sez_acquisizione_fornitori)aObj;
		if (getCd_biblioteca() == null && tra_sez_acquisizione_fornitori.getCd_biblioteca() != null)
			return false;
		if (!getCd_biblioteca().equals(tra_sez_acquisizione_fornitori.getCd_biblioteca()))
			return false;
		if (getCod_prac() != tra_sez_acquisizione_fornitori.getCod_prac())
			return false;
		if (getCod_fornitore() != tra_sez_acquisizione_fornitori.getCod_fornitore())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_biblioteca() != null) {
			hashcode = hashcode + (getCd_biblioteca().getCd_biblioteca() == null ? 0 : getCd_biblioteca().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_biblioteca().getCd_polo() == null ? 0 : getCd_biblioteca().getCd_polo().hashCode());
		}
		hashcode = hashcode + (getCod_prac() == null ? 0 : getCod_prac().hashCode());
		hashcode = hashcode + getCod_fornitore();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	//private it.iccu.sbn.polo.orm.acquisizioni.Tba_profili_acquisto cod_prac;

	private java.math.BigDecimal cod_prac;

	private int cod_fornitore;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tba_profili_acquisto = new java.util.HashSet();

//	private java.util.Set Tbr_fornitori_biblioteche = new java.util.HashSet();


	public void setCod_prac(java.math.BigDecimal value) {
		this.cod_prac = value;
	}

	public java.math.BigDecimal getCod_prac() {
		return cod_prac;
	}

	public void setCod_fornitore(int value) {
		this.cod_fornitore = value;
	}

	public int getCod_fornitore() {
		return cod_fornitore;
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

	public void setCd_biblioteca(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_biblioteca() {
		return cd_biblioteca;
	}

	public String toString() {
		return String.valueOf(((getCd_biblioteca() == null) ? "" : String.valueOf(getCd_biblioteca().getCd_biblioteca()) + " " + String.valueOf(getCd_biblioteca().getCd_polo())) + " " + getCod_prac() + " " + getCod_fornitore());
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

/*	public it.iccu.sbn.polo.orm.acquisizioni.Tba_profili_acquisto getCod_prac() {
		return cod_prac;
	}

	public void setCod_prac(
			it.iccu.sbn.polo.orm.acquisizioni.Tba_profili_acquisto cod_prac) {
		this.cod_prac = cod_prac;
	}
*/
	public java.util.Set getTba_profili_acquisto() {
		return Tba_profili_acquisto;
	}

	public void setTba_profili_acquisto(java.util.Set tba_profili_acquisto) {
		Tba_profili_acquisto = tba_profili_acquisto;
	}

/*	public java.util.Set getTbr_fornitori_biblioteche() {
		return Tbr_fornitori_biblioteche;
	}

	public void setTbr_fornitori_biblioteche(java.util.Set tbr_fornitori_biblioteche) {
		Tbr_fornitori_biblioteche = tbr_fornitori_biblioteche;
	}
	*/

}
