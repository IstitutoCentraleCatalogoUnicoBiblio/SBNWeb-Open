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
 * POSSESSORI E PROVENIENZE INVENTARI (solo libro antico)
 */
/**
 * ORM-Persistable Class
 */
public class Trc_poss_prov_inventari implements Serializable {

	private static final long serialVersionUID = -1647495316078199099L;

	public Trc_poss_prov_inventari() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trc_poss_prov_inventari))
			return false;
		Trc_poss_prov_inventari trc_poss_prov_inventari = (Trc_poss_prov_inventari)aObj;
		if (getP() == null && trc_poss_prov_inventari.getP() != null)
			return false;
		if (!getP().equals(trc_poss_prov_inventari.getP()))
			return false;
		if (getCd_polo() == null && trc_poss_prov_inventari.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(trc_poss_prov_inventari.getCd_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getP() != null) {
			hashcode = hashcode + (getP().getORMID() == null ? 0 : getP().getORMID().hashCode());
		}
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getCd_serie() == null ? 0 : getCd_polo().getCd_serie().hashCode());
			hashcode = hashcode + getCd_polo().getCd_inven();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza p;

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario cd_polo;

	private char cd_legame;

	private String nota;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	/**
	 * esplicita il legame tra inventario e proprietario; "r" = provenienza, "p" = possessore
	 */
	public void setCd_legame(char value) {
		this.cd_legame = value;
	}

	/**
	 * esplicita il legame tra inventario e proprietario; "r" = provenienza, "p" = possessore
	 */
	public char getCd_legame() {
		return cd_legame;
	}

	/**
	 * nota sul legame
	 */
	public void setNota(String value) {
		this.nota = value;
	}

	/**
	 * nota sul legame
	 */
	public String getNota() {
		return nota;
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

	public void setP(it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza value) {
		this.p = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza getP() {
		return p;
	}

	public void setCd_polo(it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario getCd_polo() {
		return cd_polo;
	}

	public String toString() {
		return String.valueOf(((getP() == null) ? "" : String.valueOf(getP().getORMID())) + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getCd_serie()) + " " + String.valueOf(getCd_polo().getCd_inven())));
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
