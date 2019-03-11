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
 * Profili d'acquisto
 */
/**
 * ORM-Persistable Class
 */
public class Tba_profili_acquisto implements Serializable {

	private static final long serialVersionUID = 1135858219114586398L;


	public Tba_profili_acquisto() {
	}


	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tba_profili_acquisto))
			return false;
		Tba_profili_acquisto tba_profili_acquisto = (Tba_profili_acquisto)aObj;
		if (getCod_prac() != tba_profili_acquisto.getCod_prac())
			return false;
		if (getId_sez_acquis_bibliografiche() == null && tba_profili_acquisto.getId_sez_acquis_bibliografiche() != null)
		//if (getId_sez() == null && tba_profili_acquisto.getId_sez() != null)
			return false;
		if (!getId_sez_acquis_bibliografiche().equals(tba_profili_acquisto.getId_sez_acquis_bibliografiche()))
		//if (!getId_sez().equals(tba_profili_acquisto.getId_sez()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCod_prac();
		if (getId_sez_acquis_bibliografiche() != null) {
			hashcode = hashcode + getId_sez_acquis_bibliografiche().getORMID();
		}
		return hashcode;
	}


/*	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCod_prac() == null ? 0 : getCod_prac().hashCode());
		if (getId_sez_acquis_bibliografiche() != null) {
			hashcode = hashcode + (int) getId_sez_acquis_bibliografiche().getORMID();
		//if (getId_sez() != null) {
		//	hashcode = hashcode + (int) getId_sez().getORMID();
		}
		return hashcode;
	}*/

	private int cod_prac;
//	private java.math.BigDecimal cod_prac;

//	private java.util.List Tba_sez_acquis_bibliografiche;

	private String descr;

	private String paese;

	private String lingua;

	private java.sql.Timestamp ts_ins;

	private java.sql.Timestamp ts_var;

	private String ute_ins;

	private String ute_var;

	private char fl_canc;


	private it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche id_sez_acquis_bibliografiche;


//	private java.util.Set Tba_sez_acquis_bibliografiche = new java.util.HashSet();

//	private java.util.Set Tra_sez_acquisizione_fornitori = new java.util.HashSet();

//	private java.util.Set id_sez_acquis_bibliografiche = new java.util.HashSet();





	/**
	 * descrizione del profilo d'acquisizione
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * descrizione del profilo d'acquisizione
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * codice identificativo del paese
	 */
	public void setPaese(String value) {
		this.paese = value;
	}

	/**
	 * codice identificativo del paese
	 */
	public String getPaese() {
		return paese;
	}

	/**
	 * codice identificativo della lingua
	 */
	public void setLingua(String value) {
		this.lingua = value;
	}

	/**
	 * codice identificativo della lingua
	 */
	public String getLingua() {
		return lingua;
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

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setId_sez_acquis_bibliografiche(it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche value) {
		this.id_sez_acquis_bibliografiche = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche getId_sez_acquis_bibliografiche() {
		return id_sez_acquis_bibliografiche;
	}

	public String toString() {
		return String.valueOf(getCod_prac() + " " + ((getId_sez_acquis_bibliografiche() == null) ? "" : String.valueOf(getId_sez_acquis_bibliografiche().getORMID())));
	}

/*	public String toString() {
		return String.valueOf(getCod_prac() + " " + ((getId_sez() == null) ? "" : String.valueOf(getId_sez().getORMID())));
	}
*/
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

	public int getCod_prac() {
		return cod_prac;
	}

	public void setCod_prac(int cod_prac) {
		this.cod_prac = cod_prac;
	}



/*	public java.util.Set getTba_sez_acquis_bibliografiche() {
		return Tba_sez_acquis_bibliografiche;
	}

	public void setTba_sez_acquis_bibliografiche(
			java.util.Set tba_sez_acquis_bibliografiche) {
		Tba_sez_acquis_bibliografiche = tba_sez_acquis_bibliografiche;
	}*/






}
