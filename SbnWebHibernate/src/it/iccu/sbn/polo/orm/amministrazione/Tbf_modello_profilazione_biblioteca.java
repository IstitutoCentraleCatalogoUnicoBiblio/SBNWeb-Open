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
package it.iccu.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Tbf_modello_profilazione_biblioteca implements Serializable {

	private static final long serialVersionUID = -2408566464208094443L;

	public Tbf_modello_profilazione_biblioteca() {
	}

	private int id_modello;

	private String nome;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro id_parametro;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita id_gruppo_attivita;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private void setId_modello(int value) {
		this.id_modello = value;
	}

	public int getId_modello() {
		return id_modello;
	}

	public int getORMID() {
		return getId_modello();
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

	public void setNome(String value) {
		this.nome = value;
	}

	public String getNome() {
		return nome;
	}
	public void setId_parametro(it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro value) {
		this.id_parametro = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro getId_parametro() {
		return id_parametro;
	}

	public void setId_gruppo_attivita(it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita value) {
		this.id_gruppo_attivita = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita getId_gruppo_attivita() {
		return id_gruppo_attivita;
	}

	public String toString() {
		return String.valueOf(getId_modello());
	}

}
