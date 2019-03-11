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
public class Tbf_par_mat implements Serializable {


	private static final long serialVersionUID = -2273750276028347008L;

	public Tbf_par_mat(Tbf_par_mat mat) {
		super();
		this.cd_par_mat = mat.cd_par_mat;
		this.id_parametro = mat.id_parametro;
		this.tp_abilitaz = mat.tp_abilitaz;
		this.cd_contr_sim = mat.cd_contr_sim;
		this.fl_abil_forzat = mat.fl_abil_forzat;
		this.cd_livello = mat.cd_livello;
		this.sololocale = mat.sololocale;
		this._saved = mat._saved;
	}

	public Tbf_par_mat() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_par_mat))
			return false;
		Tbf_par_mat tbf_par_mat = (Tbf_par_mat)aObj;
		if (getCd_par_mat() != tbf_par_mat.getCd_par_mat())
			return false;
		if (getId_parametro() == null && tbf_par_mat.getId_parametro() != null)
			return false;
		if (!getId_parametro().equals(tbf_par_mat.getId_parametro()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCd_par_mat();
		if (getId_parametro() != null) {
			hashcode = hashcode + getId_parametro().getORMID();
		}
		return hashcode;
	}

	private char cd_par_mat;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro id_parametro;

	private char tp_abilitaz;

	private String cd_contr_sim;

	private char fl_abil_forzat;

	private String cd_livello;

	private char sololocale;

	public void setCd_par_mat(char value) {
		this.cd_par_mat = value;
	}

	public char getCd_par_mat() {
		return cd_par_mat;
	}

	public void setTp_abilitaz(char value) {
		this.tp_abilitaz = value;
	}

	public char getTp_abilitaz() {
		return tp_abilitaz;
	}

	public void setCd_contr_sim(String value) {
		this.cd_contr_sim = value;
	}

	public String getCd_contr_sim() {
		return cd_contr_sim;
	}

	public void setFl_abil_forzat(char value) {
		this.fl_abil_forzat = value;
	}

	public char getFl_abil_forzat() {
		return fl_abil_forzat;
	}

	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	public String getCd_livello() {
		return cd_livello;
	}

	public void setId_parametro(it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro value) {
		this.id_parametro = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro getId_parametro() {
		return id_parametro;
	}

	public String toString() {
		return String.valueOf(getCd_par_mat() + " " + ((getId_parametro() == null) ? "" : String.valueOf(getId_parametro().getORMID())));
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

	public char getSololocale() {
		return sololocale;
	}

	public void setSololocale(char sololocale) {
		this.sololocale = sololocale;
	}


}
