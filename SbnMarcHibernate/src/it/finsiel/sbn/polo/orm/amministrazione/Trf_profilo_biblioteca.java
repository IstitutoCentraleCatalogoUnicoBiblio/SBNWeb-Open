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
package it.finsiel.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * FUNZIONI ASSOCIATE AI PROFILI DI ABILITAZIONE (TLFPFU)
 */
/**
 * ORM-Persistable Class
 */
public class Trf_profilo_biblioteca implements Serializable {

	private static final long serialVersionUID = -4264361118903003215L;

	public Trf_profilo_biblioteca() {
	}

	private int id_profilo_abilitazione_biblioteca;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita cd_attivita;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_profilo_abilitazione cd_prof;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private void setId_profilo_abilitazione_biblioteca(int value) {
		this.id_profilo_abilitazione_biblioteca = value;
	}

	public int getId_profilo_abilitazione_biblioteca() {
		return id_profilo_abilitazione_biblioteca;
	}

	public int getORMID() {
		return getId_profilo_abilitazione_biblioteca();
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

	public void setCd_attivita(it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita value) {
		this.cd_attivita = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita getCd_attivita() {
		return cd_attivita;
	}

	public void setCd_prof(it.finsiel.sbn.polo.orm.amministrazione.Tbf_profilo_abilitazione value) {
		this.cd_prof = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_profilo_abilitazione getCd_prof() {
		return cd_prof;
	}

	public String toString() {
		return String.valueOf(getId_profilo_abilitazione_biblioteca());
	}

}
