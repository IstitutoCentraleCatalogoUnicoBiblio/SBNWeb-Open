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
 * SISTEMI DI THESAURO IN BIBLIOTECA (TPSTBI)
 */
/**
 * ORM-Persistable Class
 */
public class Tr_modello_thesauri_biblioteche implements Serializable {

	private static final long serialVersionUID = -428804994085570224L;

	public Tr_modello_thesauri_biblioteche() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tr_modello_thesauri_biblioteche))
			return false;
		Tr_modello_thesauri_biblioteche tr_modello_thesauri_biblioteche = (Tr_modello_thesauri_biblioteche)aObj;
		if (getId_modello() != tr_modello_thesauri_biblioteche.getId_modello())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getId_modello();
		return hashcode;
	}

	private int id_modello;

	private String nome;

	private String cd_the;

	private Character sololocale;

	private char fl_att;

	private char fl_auto_loc;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setId_modello(int value) {
		this.id_modello = value;
	}

	public int getId_modello() {
		return id_modello;
	}

	public int getORMID() {
		return getId_modello();
	}

	public void setNome(String value) {
		this.nome = value;
	}

	public String getNome() {
		return nome;
	}

	/**
	 * codice thesauro
	 */
	public void setCd_the(String value) {
		this.cd_the = value;
	}

	/**
	 * codice thesauro
	 */
	public String getCd_the() {
		return cd_the;
	}

	public void setSololocale(char value) {
		setSololocale(new Character(value));
	}

	public void setSololocale(Character value) {
		this.sololocale = value;
	}

	public Character getSololocale() {
		return sololocale;
	}

	/**
	 * indicatore dell'attuale utilizzo del thesauro da parte della biblioteca
	 */
	public void setFl_att(char value) {
		this.fl_att = value;
	}

	/**
	 * indicatore dell'attuale utilizzo del thesauro da parte della biblioteca
	 */
	public char getFl_att() {
		return fl_att;
	}

	/**
	 * indicatore di localizzazione automatica legami a termini di thesauro inseriti da altra biblioteca
	 */
	public void setFl_auto_loc(char value) {
		this.fl_auto_loc = value;
	}

	/**
	 * indicatore di localizzazione automatica legami a termini di thesauro inseriti da altra biblioteca
	 */
	public char getFl_auto_loc() {
		return fl_auto_loc;
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

	public String toString() {
		return String.valueOf(getId_modello());
	}

	private boolean _saved = false;

	public void onSave() {
		_saved=true;
	}


	public void onLoad() {
		_saved=true;
	}


	public boolean isSaved() {
		return _saved;
	}


}
