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
public class Tbf_gruppo_attivita implements Serializable {

	private static final long serialVersionUID = -5642052652910195139L;

	public Tbf_gruppo_attivita() {
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_polo cd_polo;

	private int id_gruppo_attivita_polo;

	private String ds_name;

	private java.util.Set id_gruppo_attivita_polo_base = new java.util.HashSet();

	private java.util.Set id_gruppo_attivita_polo_coll = new java.util.HashSet();

	private java.util.Set Tbf_biblioteca_in_polo = new java.util.HashSet();

	private java.util.Set Trf_gruppo_attivita_polo = new java.util.HashSet();
	private it.iccu.sbn.polo.orm.amministrazione.Tbf_modello_profilazione_biblioteca Tbf_modello_profilazione_biblioteca;
	private it.iccu.sbn.polo.orm.amministrazione.Tbf_modello_profilazione_bibliotecario Tbf_modello_profilazione_bibliotecario;

	private void setId_gruppo_attivita_polo(int value) {
		this.id_gruppo_attivita_polo = value;
	}

	public int getId_gruppo_attivita_polo() {
		return id_gruppo_attivita_polo;
	}

	public int getORMID() {
		return getId_gruppo_attivita_polo();
	}

	public void setDs_name(String value) {
		this.ds_name = value;
	}

	public String getDs_name() {
		return ds_name;
	}

	public void setId_gruppo_attivita_polo_base(java.util.Set value) {
		this.id_gruppo_attivita_polo_base = value;
	}

	public java.util.Set getId_gruppo_attivita_polo_base() {
		return id_gruppo_attivita_polo_base;
	}


	public void setCd_polo(it.iccu.sbn.polo.orm.amministrazione.Tbf_polo value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_polo getCd_polo() {
		return cd_polo;
	}

	public void setId_gruppo_attivita_polo_coll(java.util.Set value) {
		this.id_gruppo_attivita_polo_coll = value;
	}

	public java.util.Set getId_gruppo_attivita_polo_coll() {
		return id_gruppo_attivita_polo_coll;
	}


	public void setTbf_biblioteca_in_polo(java.util.Set value) {
		this.Tbf_biblioteca_in_polo = value;
	}

	public java.util.Set getTbf_biblioteca_in_polo() {
		return Tbf_biblioteca_in_polo;
	}


	public void setTrf_gruppo_attivita_polo(java.util.Set value) {
		this.Trf_gruppo_attivita_polo = value;
	}

	public java.util.Set getTrf_gruppo_attivita_polo() {
		return Trf_gruppo_attivita_polo;
	}


	public void setTbf_modello_profilazione_biblioteca(it.iccu.sbn.polo.orm.amministrazione.Tbf_modello_profilazione_biblioteca value) {
		this.Tbf_modello_profilazione_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_modello_profilazione_biblioteca getTbf_modello_profilazione_biblioteca() {
		return Tbf_modello_profilazione_biblioteca;
	}
	public void setTbf_modello_profilazione_bibliotecario(it.iccu.sbn.polo.orm.amministrazione.Tbf_modello_profilazione_bibliotecario value) {
		this.Tbf_modello_profilazione_bibliotecario = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_modello_profilazione_bibliotecario getTbf_modello_profilazione_bibliotecario() {
		return Tbf_modello_profilazione_bibliotecario;
	}
	public String toString() {
		return String.valueOf(getId_gruppo_attivita_polo());
	}

}
