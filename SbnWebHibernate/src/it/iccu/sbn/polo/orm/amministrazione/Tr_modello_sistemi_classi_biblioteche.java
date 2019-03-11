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
 * SISTEMA DI CLASSIFICAZIONE IN BIBLIOTECA (TPSSCB))
 */
/**
 * ORM-Persistable Class
 */
public class Tr_modello_sistemi_classi_biblioteche implements Serializable {

	private static final long serialVersionUID = 1041906259817731074L;

	public Tr_modello_sistemi_classi_biblioteche() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tr_modello_sistemi_classi_biblioteche))
			return false;
		Tr_modello_sistemi_classi_biblioteche tr_modello_sistemi_classi_biblioteche = (Tr_modello_sistemi_classi_biblioteche)aObj;
		if (getId_modello() != tr_modello_sistemi_classi_biblioteche.getId_modello())
			return false;
		if ((getCd_sistema() != null && !getCd_sistema().equals(tr_modello_sistemi_classi_biblioteche.getCd_sistema())) || (getCd_sistema() == null && tr_modello_sistemi_classi_biblioteche.getCd_sistema() != null))
			return false;
		if ((getCd_edizione() != null && !getCd_edizione().equals(tr_modello_sistemi_classi_biblioteche.getCd_edizione())) || (getCd_edizione() == null && tr_modello_sistemi_classi_biblioteche.getCd_edizione() != null))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getId_modello();
		hashcode = hashcode + (getCd_sistema() == null ? 0 : getCd_sistema().hashCode());
		hashcode = hashcode + (getCd_edizione() == null ? 0 : getCd_edizione().hashCode());
		return hashcode;
	}

	private int id_modello;

	private String nome;

	private String cd_sistema;

	private String cd_edizione;

	private char sololocale;

	private char flg_att;

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

	public void setNome(String value) {
		this.nome = value;
	}

	public String getNome() {
		return nome;
	}

	public void setCd_sistema(String value) {
		this.cd_sistema = value;
	}

	public String getCd_sistema() {
		return cd_sistema;
	}

	public void setCd_edizione(String value) {
		this.cd_edizione = value;
	}

	public String getCd_edizione() {
		return cd_edizione;
	}

	public void setSololocale(char value) {
		this.sololocale = value;
	}

	public char getSololocale() {
		return sololocale;
	}

	/**
	 * indicatore dell'attuale utilizzo del sistema di classificazione da parte della biblioteca
	 */
	public void setFlg_att(char value) {
		this.flg_att = value;
	}

	/**
	 * indicatore dell'attuale utilizzo del sistema di classificazione da parte della biblioteca
	 */
	public char getFlg_att() {
		return flg_att;
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
		return String.valueOf(getId_modello() + " " + getCd_sistema() + " " + getCd_edizione());
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
