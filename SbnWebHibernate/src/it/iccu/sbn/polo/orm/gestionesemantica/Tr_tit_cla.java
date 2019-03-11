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
package it.iccu.sbn.polo.orm.gestionesemantica;

import java.io.Serializable;
/**
 * CLASSIFICAZIONI DI OGGETTI BIBLIOGRAFICI (TPSCOB))
 */
/**
 * ORM-Persistable Class
 */
public class Tr_tit_cla implements Serializable {

	private static final long serialVersionUID = -5112424280406644644L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_cla))
			return false;
		Tr_tit_cla tr_tit_cla = (Tr_tit_cla)aObj;
		if (getB() == null)
			return false;
		if (!getB().getORMID().equals(tr_tit_cla.getB().getORMID()))
			return false;
		if (getCd_sistema() == null)
			return false;
		if (!getCd_sistema().getCd_sistema().equals(tr_tit_cla.getCd_sistema().getCd_sistema()))
			return false;
		if (!getCd_sistema().getCd_edizione().equals(tr_tit_cla.getCd_sistema().getCd_edizione()))
			return false;
		if (!getCd_sistema().getClasse().equals(tr_tit_cla.getCd_sistema().getClasse()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getB() != null) {
			hashcode = hashcode + getB().getORMID().hashCode();
		}
		if (getCd_sistema() != null) {
			hashcode = hashcode + getCd_sistema().getCd_sistema().hashCode();
			hashcode = hashcode + getCd_sistema().getCd_edizione().hashCode();
			hashcode = hashcode + getCd_sistema().getClasse().hashCode();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private it.iccu.sbn.polo.orm.gestionesemantica.Tb_classe cd_sistema;

	private String nota_tit_cla;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	/**
	 * note al legame
	 */
	public void setNota_tit_cla(String value) {
		this.nota_tit_cla = value;
	}

	/**
	 * note al legame
	 */
	public String getNota_tit_cla() {
		return nota_tit_cla;
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

	public void setCd_sistema(it.iccu.sbn.polo.orm.gestionesemantica.Tb_classe value) {
		this.cd_sistema = value;
	}

	public it.iccu.sbn.polo.orm.gestionesemantica.Tb_classe getCd_sistema() {
		return cd_sistema;
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())) + " " + ((getCd_sistema() == null) ? "" : String.valueOf(getCd_sistema().getCd_sistema()) + " " + String.valueOf(getCd_sistema().getCd_edizione()) + " " + String.valueOf(getCd_sistema().getClasse())));
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
