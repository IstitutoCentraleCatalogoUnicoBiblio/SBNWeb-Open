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
 * LUOGO DI PUBBLICAZIONE DEL TITOLO
 */
/**
 * ORM-Persistable Class
 */
public class Tr_tit_luo implements Serializable {

	private static final long serialVersionUID = -8229346677341992971L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_luo))
			return false;
		Tr_tit_luo tr_tit_luo = (Tr_tit_luo)aObj;
		if (getB() == null)
			return false;
		if (!getB().getORMID().equals(tr_tit_luo.getB().getORMID()))
			return false;
		if (getL() == null)
			return false;
		if (!getL().getORMID().equals(tr_tit_luo.getL().getORMID()))
			return false;
		if (getTp_luogo() != tr_tit_luo.getTp_luogo())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getB() != null) {
			hashcode = hashcode + getB().getORMID().hashCode();
		}
		if (getL() != null) {
			hashcode = hashcode + getL().getORMID().hashCode();
		}
		hashcode = hashcode + getTp_luogo();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_luogo l;

	private char tp_luogo;

	private String nota_tit_luo;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setTp_luogo(char value) {
		this.tp_luogo = value;
	}

	public char getTp_luogo() {
		return tp_luogo;
	}

	public void setNota_tit_luo(String value) {
		this.nota_tit_luo = value;
	}

	public String getNota_tit_luo() {
		return nota_tit_luo;
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

	public void setL(it.iccu.sbn.polo.orm.bibliografica.Tb_luogo value) {
		this.l = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_luogo getL() {
		return l;
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())) + " " + ((getL() == null) ? "" : String.valueOf(getL().getORMID())) + " " + getTp_luogo());
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
