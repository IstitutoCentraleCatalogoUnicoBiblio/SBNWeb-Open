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
 * Tabella per gestire le procedure differite (batch) programmate e non richiamate da utente.
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_batch_servizi implements Serializable {

	private static final long serialVersionUID = 1878182930903843232L;

	public Tbf_batch_servizi() {
	}

	private int id_batch_servizi;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita cd_attivita;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_coda_jms id_coda_input;

	private String nome_coda_output;

	private String class_name;

	private char visibilita;

	private char fl_canc;

	private void setId_batch_servizi(int value) {
		this.id_batch_servizi = value;
	}

	public int getId_batch_servizi() {
		return id_batch_servizi;
	}

	public int getORMID() {
		return getId_batch_servizi();
	}

	public void setCd_attivita(
			it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita value) {
		this.cd_attivita = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita getCd_attivita() {
		return cd_attivita;
	}

	public void setId_coda_input(
			it.iccu.sbn.polo.orm.amministrazione.Tbf_coda_jms value) {
		this.id_coda_input = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_coda_jms getId_coda_input() {
		return id_coda_input;
	}

	public String toString() {
		return String.valueOf(getId_batch_servizi());
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public char getVisibilita() {
		return visibilita;
	}

	public void setVisibilita(char visibilita) {
		this.visibilita = visibilita;
	}

	public String getNome_coda_output() {
		return nome_coda_output;
	}

	public void setNome_coda_output(String nome_coda_output) {
		this.nome_coda_output = nome_coda_output;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(char fl_canc) {
		this.fl_canc = fl_canc;
	}

}
