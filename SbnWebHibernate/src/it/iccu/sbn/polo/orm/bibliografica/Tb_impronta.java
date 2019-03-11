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
package it.iccu.sbn.polo.orm.bibliografica;

import java.io.Serializable;
/**
 * IMPRONTE
 */
/**
 * ORM-Persistable Class
 */
public class Tb_impronta implements Serializable {

	private static final long serialVersionUID = 331616752839641246L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_impronta))
			return false;
		Tb_impronta tb_impronta = (Tb_impronta)aObj;
		if (getB() == null)
			return false;
		if (!getB().getORMID().equals(tb_impronta.getB().getORMID()))
			return false;
		if (getImpronta_1() != null && !getImpronta_1().equals(tb_impronta.getImpronta_1()))
			return false;
		if (getImpronta_2() != null && !getImpronta_2().equals(tb_impronta.getImpronta_2()))
			return false;
		if (getImpronta_3() != null && !getImpronta_3().equals(tb_impronta.getImpronta_3()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getB() != null) {
			hashcode = hashcode + getB().getORMID().hashCode();
		}
		hashcode = hashcode + getImpronta_1().hashCode();
		hashcode = hashcode + getImpronta_2().hashCode();
		hashcode = hashcode + getImpronta_3().hashCode();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String impronta_1;

	private String impronta_2;

	private String impronta_3;

	private String nota_impronta;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setImpronta_1(String value) {
		this.impronta_1 = value;
	}

	public String getImpronta_1() {
		return impronta_1;
	}

	public void setImpronta_2(String value) {
		this.impronta_2 = value;
	}

	public String getImpronta_2() {
		return impronta_2;
	}

	public void setImpronta_3(String value) {
		this.impronta_3 = value;
	}

	public String getImpronta_3() {
		return impronta_3;
	}

	/**
	 * Nota al collegamento titolo impronta
	 */
	public void setNota_impronta(String value) {
		this.nota_impronta = value;
	}

	/**
	 * Nota al collegamento titolo impronta
	 */
	public String getNota_impronta() {
		return nota_impronta;
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

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())) + " " + getImpronta_1() + " " + getImpronta_2() + " " + getImpronta_3());
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
