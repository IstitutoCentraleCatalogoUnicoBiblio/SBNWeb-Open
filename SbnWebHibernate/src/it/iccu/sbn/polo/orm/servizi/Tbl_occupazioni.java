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
 * Occupazioni
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_occupazioni implements Serializable {

	private static final long serialVersionUID = -1465594973116374267L;

	public Tbl_occupazioni() {
	}

	private int id_occupazioni;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private char professione;

	//private char occupazione; 22.12.09

	private String occupazione;

	private String descr;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca Trl_utenti_biblioteca;

	public void setId_occupazioni(int value) {
		this.id_occupazioni = value;
	}

	public int getId_occupazioni() {
		return id_occupazioni;
	}

	public int getORMID() {
		return getId_occupazioni();
	}

	/**
	 * codice identificativo della professione
	 */
	public void setProfessione(char value) {
		this.professione = value;
	}

	/**
	 * codice identificativo della professione
	 */
	public char getProfessione() {
		return professione;
	}

	/**
	 * codice dell'occupazione
	 */
	public void setOccupazione(String occupazione) {
		this.occupazione = occupazione;
	}

	/**
	 * codice dell'occupazione
	 */
	public String getOccupazione() {
		return occupazione;
	}

	/**
	 * descrizione dell'occupazione
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * descrizione dell'occupazione
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

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

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

	public void setTrl_utenti_biblioteca(it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca value) {
		this.Trl_utenti_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca getTrl_utenti_biblioteca() {
		return Trl_utenti_biblioteca;
	}

	public String toString() {
		return String.valueOf(getId_occupazioni());
	}


}
