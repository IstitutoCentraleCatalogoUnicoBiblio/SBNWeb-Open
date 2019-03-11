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
public class Tbf_par_auth implements Serializable {


	private static final long serialVersionUID = -7562154769855692242L;

	public Tbf_par_auth(Tbf_par_auth aut) {
		super();
		this.cd_par_auth = aut.cd_par_auth;
		this.id_parametro = aut.id_parametro;
		this.tp_abil_auth = aut.tp_abil_auth;
		this.fl_abil_legame = aut.fl_abil_legame;
		this.fl_leg_auth = aut.fl_leg_auth;
		this.cd_livello = aut.cd_livello;
		this.cd_contr_sim = aut.cd_contr_sim;
		this.fl_abil_forzat = aut.fl_abil_forzat;
		this.sololocale = aut.sololocale;
		this._saved = aut._saved;
	}

	public Tbf_par_auth() {
		super();
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_par_auth))
			return false;
		Tbf_par_auth tbf_par_auth = (Tbf_par_auth)aObj;
		if ((getCd_par_auth() != null && !getCd_par_auth().equals(tbf_par_auth.getCd_par_auth())) || (getCd_par_auth() == null && tbf_par_auth.getCd_par_auth() != null))
			return false;
		if (getId_parametro() == null && tbf_par_auth.getId_parametro() != null)
			return false;
		if (!getId_parametro().equals(tbf_par_auth.getId_parametro()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCd_par_auth() == null ? 0 : getCd_par_auth().hashCode());
		if (getId_parametro() != null) {
			hashcode = hashcode + getId_parametro().getORMID();
		}
		return hashcode;
	}

	private String cd_par_auth;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro id_parametro;

	private char tp_abil_auth;

	private char fl_abil_legame;

	private char fl_leg_auth;

	private String cd_livello;

	private String cd_contr_sim;

	private char fl_abil_forzat;

	private char sololocale;

	public void setCd_par_auth(String value) {
		this.cd_par_auth = value;
	}

	public String getCd_par_auth() {
		return cd_par_auth;
	}

	public void setTp_abil_auth(char value) {
		this.tp_abil_auth = value;
	}

	public char getTp_abil_auth() {
		return tp_abil_auth;
	}

	public void setFl_abil_legame(char value) {
		this.fl_abil_legame = value;
	}

	public char getFl_abil_legame() {
		return fl_abil_legame;
	}

	public void setFl_leg_auth(char value) {
		this.fl_leg_auth = value;
	}

	public char getFl_leg_auth() {
		return fl_leg_auth;
	}

	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	public String getCd_livello() {
		return cd_livello;
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

	public void setId_parametro(it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro value) {
		this.id_parametro = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro getId_parametro() {
		return id_parametro;
	}

	public String toString() {
		return String.valueOf(getCd_par_auth() + " " + ((getId_parametro() == null) ? "" : String.valueOf(getId_parametro().getORMID())));
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
