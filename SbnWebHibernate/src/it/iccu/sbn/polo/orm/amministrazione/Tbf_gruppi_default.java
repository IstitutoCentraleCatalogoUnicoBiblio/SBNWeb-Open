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
public class Tbf_gruppi_default implements Serializable {

	private static final long serialVersionUID = -5954012449398835696L;

	public Tbf_gruppi_default() {
	}

	private int id;

	private String etichetta;

	private String bundle;

	private java.util.Set Tbf_default = new java.util.HashSet();

	private void setId(int value) {
		this.id = value;
	}

	public int getId() {
		return id;
	}

	public int getORMID() {
		return getId();
	}

	/**
	 * Etichetta da prospettare
	 */
	public void setEtichetta(String value) {
		this.etichetta = value;
	}

	/**
	 * Etichetta da prospettare
	 */
	public String getEtichetta() {
		return etichetta;
	}

	public void setBundle(String value) {
		this.bundle = value;
	}

	public String getBundle() {
		return bundle;
	}

	public void setTbf_default(java.util.Set value) {
		this.Tbf_default = value;
	}

	public java.util.Set getTbf_default() {
		return Tbf_default;
	}


	public String toString() {
		return String.valueOf(getId());
	}

}
