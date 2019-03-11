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
 * ORM-Persistable Class
 */
public class Tbf_attivita_sbnmarc implements Serializable {

	private static final long serialVersionUID = -2029786584573069515L;

	public Tbf_attivita_sbnmarc() {
	}

	private int id_attivita_sbnmarc;

	private Integer id_tipo_attivita;

	private Character tp_attivita;

	private String ds_attivita;

	private String nota_tipo_attivita;

	private Integer livello;

	private String codice_attivita;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_moduli_funzionali id_modulo;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita Tbf_attivita;

	private void setId_attivita_sbnmarc(int value) {
		this.id_attivita_sbnmarc = value;
	}

	public int getId_attivita_sbnmarc() {
		return id_attivita_sbnmarc;
	}

	public int getORMID() {
		return getId_attivita_sbnmarc();
	}

	public void setId_tipo_attivita(int value) {
		setId_tipo_attivita(new Integer(value));
	}

	public void setId_tipo_attivita(Integer value) {
		this.id_tipo_attivita = value;
	}

	public Integer getId_tipo_attivita() {
		return id_tipo_attivita;
	}

	public void setTp_attivita(char value) {
		setTp_attivita(new Character(value));
	}

	public void setTp_attivita(Character value) {
		this.tp_attivita = value;
	}

	public Character getTp_attivita() {
		return tp_attivita;
	}

	public void setDs_attivita(String value) {
		this.ds_attivita = value;
	}

	public String getDs_attivita() {
		return ds_attivita;
	}

	public void setNota_tipo_attivita(String value) {
		this.nota_tipo_attivita = value;
	}

	public String getNota_tipo_attivita() {
		return nota_tipo_attivita;
	}

	public void setLivello(int value) {
		setLivello(new Integer(value));
	}

	public void setLivello(Integer value) {
		this.livello = value;
	}

	public Integer getLivello() {
		return livello;
	}

	public void setCodice_attivita(String value) {
		this.codice_attivita = value;
	}

	public String getCodice_attivita() {
		return codice_attivita;
	}

	public void setId_modulo(it.finsiel.sbn.polo.orm.amministrazione.Tbf_moduli_funzionali value) {
		this.id_modulo = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_moduli_funzionali getId_modulo() {
		return id_modulo;
	}

	public void setTbf_attivita(it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita value) {
		this.Tbf_attivita = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita getTbf_attivita() {
		return Tbf_attivita;
	}

	public String toString() {
		return String.valueOf(getId_attivita_sbnmarc());
	}

}
