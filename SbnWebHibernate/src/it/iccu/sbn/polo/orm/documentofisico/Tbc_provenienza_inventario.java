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
package it.iccu.sbn.polo.orm.documentofisico;

import java.io.Serializable;
/**
 * PROVENIENZE INVENTARIO
 */
/**
 * ORM-Persistable Class
 */
public class Tbc_provenienza_inventario implements Serializable {

	private static final long serialVersionUID = -5709276189808270243L;

	public Tbc_provenienza_inventario() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbc_provenienza_inventario))
			return false;
		Tbc_provenienza_inventario tbc_provenienza_inventario = (Tbc_provenienza_inventario)aObj;
		if ((getCd_proven() != null && !getCd_proven().equals(tbc_provenienza_inventario.getCd_proven())) || (getCd_proven() == null && tbc_provenienza_inventario.getCd_proven() != null))
			return false;
		if (getCd_polo() == null && tbc_provenienza_inventario.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(tbc_provenienza_inventario.getCd_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCd_proven() == null ? 0 : getCd_proven().hashCode());
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getCd_biblioteca() == null ? 0 : getCd_polo().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_polo().getCd_polo() == null ? 0 : getCd_polo().getCd_polo().hashCode());
		}
		return hashcode;
	}

	private String cd_proven;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_polo;

	private String descr;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tbc_inventario = new java.util.HashSet();

	public void setCd_proven(String value) {
		this.cd_proven = value;
	}

	public String getCd_proven() {
		return cd_proven;
	}

	/**
	 * descrizione della provenienza dell'inventario
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * descrizione della provenienza dell'inventario
	 */
	public String getDescr() {
		return descr;
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

	public void setCd_polo(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_polo() {
		return cd_polo;
	}

	public void setTbc_inventario(java.util.Set value) {
		this.Tbc_inventario = value;
	}

	public java.util.Set getTbc_inventario() {
		return Tbc_inventario;
	}


	public String toString() {
		return String.valueOf(getCd_proven() + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getCd_biblioteca()) + " " + String.valueOf(getCd_polo().getCd_polo())));
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
