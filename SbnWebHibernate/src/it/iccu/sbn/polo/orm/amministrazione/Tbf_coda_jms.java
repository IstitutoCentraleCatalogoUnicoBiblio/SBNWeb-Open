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
public class Tbf_coda_jms implements Serializable {

	private static final long serialVersionUID = -3015617162826099259L;

	public Tbf_coda_jms() {
	}

	private int id_coda;

	private String nome_jms;

	private char sincrona = 'N';

	private String cron_expression = "0 0/5 * * * ? 9999";

	private String id_descrizione;

	private String id_descr_orario_attivazione;

	private String id_orario_di_attivazione;

	private java.util.Set Tbf_batch_servizi = new java.util.HashSet();

	private void setId_coda(int value) {
		this.id_coda = value;
	}

	public int getId_coda() {
		return id_coda;
	}

	public int getORMID() {
		return getId_coda();
	}

	/**
	 * Nome della coda JMS
	 */
	public void setNome_jms(String value) {
		this.nome_jms = value;
	}

	/**
	 * Nome della coda JMS
	 */
	public String getNome_jms() {
		return nome_jms;
	}

	/**
	 * Coda di tipo sincrona (S) o asincrona (N)
	 */
	public void setSincrona(char value) {
		this.sincrona = value;
	}

	/**
	 * Coda di tipo sincrona (S) o asincrona (N)
	 */
	public char getSincrona() {
		return sincrona;
	}

	public void setId_descrizione(String value) {
		this.id_descrizione = value;
	}

	public String getId_descrizione() {
		return id_descrizione;
	}

	public void setId_descr_orario_attivazione(String value) {
		this.id_descr_orario_attivazione = value;
	}

	public String getId_descr_orario_attivazione() {
		return id_descr_orario_attivazione;
	}

	/**
	 * Orario di attivazione in chiaro eg. aaaa/mm/gg hh:mm
	 */
	public void setId_orario_di_attivazione(String value) {
		this.id_orario_di_attivazione = value;
	}

	/**
	 * Orario di attivazione in chiaro eg. aaaa/mm/gg hh:mm
	 */
	public String getId_orario_di_attivazione() {
		return id_orario_di_attivazione;
	}

	public void setTbf_batch_servizi(java.util.Set value) {
		this.Tbf_batch_servizi = value;
	}

	public java.util.Set getTbf_batch_servizi() {
		return Tbf_batch_servizi;
	}

	public String toString() {
		return String.valueOf(getId_coda());
	}

	public String getCron_expression() {
		return cron_expression;
	}

	public void setCron_expression(String cron_expression) {
		this.cron_expression = cron_expression;
	}

}
