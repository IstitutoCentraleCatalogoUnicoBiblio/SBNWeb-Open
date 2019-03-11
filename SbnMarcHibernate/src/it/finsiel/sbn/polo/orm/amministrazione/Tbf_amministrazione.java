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
public class Tbf_amministrazione implements Serializable {

	private static final long serialVersionUID = -7950575109946580220L;

	private int id_amministratore;

	private String tp_amministratore;

	private java.util.Set cd_polo = new java.util.HashSet();

	private java.util.Set Trf_amministrazione_polo = new java.util.HashSet();

	private void setId_amministratore(int value) {
		this.id_amministratore = value;
	}

	public int getId_amministratore() {
		return id_amministratore;
	}

	public int getORMID() {
		return getId_amministratore();
	}

	/**
	 * Puo Assumere 2 valori:
	 * Polo
	 * Biblioteca
	 */
	public void setTp_amministratore(String value) {
		this.tp_amministratore = value;
	}

	/**
	 * Puo Assumere 2 valori:
	 * Polo
	 * Biblioteca
	 */
	public String getTp_amministratore() {
		return tp_amministratore;
	}

	public void setCd_polo(java.util.Set value) {
		this.cd_polo = value;
	}

	public java.util.Set getCd_polo() {
		return cd_polo;
	}


	public void setTrf_amministrazione_polo(java.util.Set value) {
		this.Trf_amministrazione_polo = value;
	}

	public java.util.Set getTrf_amministrazione_polo() {
		return Trf_amministrazione_polo;
	}


	public String toString() {
		return String.valueOf(getId_amministratore());
	}

}
