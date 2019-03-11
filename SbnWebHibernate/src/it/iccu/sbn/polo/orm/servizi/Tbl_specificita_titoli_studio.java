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
 * Specificazioni titoli studio
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_specificita_titoli_studio implements Serializable {

	private static final long serialVersionUID = 7385652428818259671L;

	public Tbl_specificita_titoli_studio() {
	}

	private int id_specificita_titoli_studio;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private char tit_studio;

	//private char specif_tit;
	private String specif_tit;

	private String descr;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca Trl_utenti_biblioteca;

	public void setId_specificita_titoli_studio(int value) {
		this.id_specificita_titoli_studio = value;
	}

	public int getId_specificita_titoli_studio() {
		return id_specificita_titoli_studio;
	}

	public int getORMID() {
		return getId_specificita_titoli_studio();
	}

	/**
	 * codice identificativo del titolo di studio
	 */
	public void setTit_studio(char value) {
		this.tit_studio = value;
	}

	/**
	 * codice identificativo del titolo di studio
	 */
	public char getTit_studio() {
		return tit_studio;
	}

	/**
	 * codice della specificazione del titolo di studio
	 */
	public void setSpecif_tit(String specif_tit) {
		this.specif_tit = specif_tit;
	}

	/**
	 * codice della specificazione del titolo di studio
	 */
	public String getSpecif_tit() {
		return specif_tit;
	}

	/**
	 * descrizione del titolo di studio
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * descrizione del titolo di studio
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * Timestamp di inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * Timestamp di inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public void setUte_var(String value) {
		this.ute_var = value;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public String getUte_var() {
		return ute_var;
	}

	/**
	 * Timestamp di variazione
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * Timestamp di variazione
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	/**
	 * Flag di cancellazione logica
	 */
	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	/**
	 * Flag di cancellazione logica
	 */
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
		return String.valueOf(getId_specificita_titoli_studio());
	}




}
