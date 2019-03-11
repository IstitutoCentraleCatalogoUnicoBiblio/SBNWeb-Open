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
public class Tbf_par_sem implements Serializable {

	private static final long serialVersionUID = 6551214979744724428L;

	public Tbf_par_sem() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_par_sem))
			return false;
		Tbf_par_sem tbf_par_sem = (Tbf_par_sem)aObj;
		if ((getTp_tabella_codici() != null && !getTp_tabella_codici().equals(tbf_par_sem.getTp_tabella_codici())) || (getTp_tabella_codici() == null && tbf_par_sem.getTp_tabella_codici() != null))
			return false;
		if ((getCd_tabella_codici() != null && !getCd_tabella_codici().equals(tbf_par_sem.getCd_tabella_codici())) || (getCd_tabella_codici() == null && tbf_par_sem.getCd_tabella_codici() != null))
			return false;
		if (getId_parametro() == null && tbf_par_sem.getId_parametro() != null)
			return false;
		if (!getId_parametro().equals(tbf_par_sem.getId_parametro()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getTp_tabella_codici() == null ? 0 : getTp_tabella_codici().hashCode());
		hashcode = hashcode + (getCd_tabella_codici() == null ? 0 : getCd_tabella_codici().hashCode());
		if (getId_parametro() != null) {
			hashcode = hashcode + getId_parametro().getORMID();
		}
		return hashcode;
	}

	private String tp_tabella_codici;

	private String cd_tabella_codici;

	private char sololocale;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro id_parametro;

	public void setTp_tabella_codici(String value) {
		this.tp_tabella_codici = value;
	}

	public String getTp_tabella_codici() {
		return tp_tabella_codici;
	}

	public void setCd_tabella_codici(String value) {
		this.cd_tabella_codici = value;
	}

	public String getCd_tabella_codici() {
		return cd_tabella_codici;
	}

	public void setId_parametro(it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro value) {
		this.id_parametro = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro getId_parametro() {
		return id_parametro;
	}

	public String toString() {
		return String.valueOf(getTp_tabella_codici() + " " + getCd_tabella_codici() + " " + ((getId_parametro() == null) ? "" : String.valueOf(getId_parametro().getORMID())));
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
