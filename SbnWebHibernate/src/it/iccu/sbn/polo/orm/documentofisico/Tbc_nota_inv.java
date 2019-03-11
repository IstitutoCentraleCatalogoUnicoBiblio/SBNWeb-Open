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
 * SERIE INVENTARIALI
 */
/**
 * ORM-Persistable Class
 */
public class Tbc_nota_inv implements Serializable {

	private static final long serialVersionUID = 6583060950044178946L;

	public Tbc_nota_inv() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbc_nota_inv))
			return false;
		Tbc_nota_inv tbc_nota_inv = (Tbc_nota_inv)aObj;
		if (getCd_polo() == null && tbc_nota_inv.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(tbc_nota_inv.getCd_polo()))
			return false;
		if ((getCd_nota() != null && !getCd_nota().equals(tbc_nota_inv.getCd_nota())) || (getCd_nota() == null && tbc_nota_inv.getCd_nota() != null))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getCd_serie() == null ? 0 : getCd_polo().getCd_serie().hashCode());
			hashcode = hashcode + getCd_polo().getCd_inven();
		}
		hashcode = hashcode + (getCd_nota() == null ? 0 : getCd_nota().hashCode());
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario cd_polo;

	private String cd_nota;

	private String ds_nota_libera;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setCd_nota(String value) {
		this.cd_nota = value;
	}

	public String getCd_nota() {
		return cd_nota;
	}

	/**
	 * progressivo per l'assegnazione automatica del n. inventario pregresso
	 */
	public void setDs_nota_libera(String value) {
		this.ds_nota_libera = value;
	}

	/**
	 * progressivo per l'assegnazione automatica del n. inventario pregresso
	 */
	public String getDs_nota_libera() {
		return ds_nota_libera;
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

	public void setCd_polo(it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario getCd_polo() {
		return cd_polo;
	}

	public String toString() {
		return String.valueOf(((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getCd_serie()) + " " + String.valueOf(getCd_polo().getCd_inven())) + " " + getCd_nota());
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
