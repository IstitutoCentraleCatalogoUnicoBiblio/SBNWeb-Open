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
public class Trf_utente_professionale_polo implements Serializable {

	private static final long serialVersionUID = -6455570984055028613L;

	public Trf_utente_professionale_polo() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trf_utente_professionale_polo))
			return false;
		Trf_utente_professionale_polo trf_utente_professionale_polo = (Trf_utente_professionale_polo)aObj;
		if (getId_utente_professionale() == null && trf_utente_professionale_polo.getId_utente_professionale() != null)
			return false;
		if (!getId_utente_professionale().equals(trf_utente_professionale_polo.getId_utente_professionale()))
			return false;
		if (getCd_polo() == null && trf_utente_professionale_polo.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(trf_utente_professionale_polo.getCd_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_utente_professionale() != null) {
			hashcode = hashcode + getId_utente_professionale().getORMID();
		}
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getORMID() == null ? 0 : getCd_polo().getORMID().hashCode());
		}
		return hashcode;
	}

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali id_utente_professionale;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo cd_polo;

	private String ute_var;

	private java.sql.Timestamp ts_ins;

	private String ute_ins;

	private java.sql.Timestamp ts_var;

	private Character fl_canc;

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setFl_canc(char value) {
		setFl_canc(new Character(value));
	}

	public void setFl_canc(Character value) {
		this.fl_canc = value;
	}

	public Character getFl_canc() {
		return fl_canc;
	}

	public void setCd_polo(it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo value) {
		this.cd_polo = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo getCd_polo() {
		return cd_polo;
	}

	public void setId_utente_professionale(it.finsiel.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali value) {
		this.id_utente_professionale = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali getId_utente_professionale() {
		return id_utente_professionale;
	}

	public String toString() {
		return String.valueOf(((getId_utente_professionale() == null) ? "" : String.valueOf(getId_utente_professionale().getORMID())) + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getORMID())));
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
