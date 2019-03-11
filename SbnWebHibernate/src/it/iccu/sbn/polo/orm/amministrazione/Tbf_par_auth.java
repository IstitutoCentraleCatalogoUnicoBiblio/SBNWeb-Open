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
 * ORM-Persistable Class
 */
public class Tbf_par_auth implements Serializable {

	private static final long serialVersionUID = 5593674078677110163L;

	public Tbf_par_auth() {
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

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro id_parametro;

	private char tp_abil_auth;

	private char fl_abil_legame;

	private char fl_leg_auth;

	private String cd_livello;

	private String cd_contr_sim;

	private char fl_abil_forzat;

	private Character sololocale;

	public Character getSololocale() {
		return sololocale;
	}

	public void setSololocale(Character sololocale) {
		this.sololocale = sololocale;
	}

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

	public void setId_parametro(it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro value) {
		this.id_parametro = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro getId_parametro() {
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


}
