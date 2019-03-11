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

import it.iccu.sbn.polo.orm.Tb_base;
/**
 * ORM-Persistable Class
 */
public class Tr_sogp_sogi extends Tb_base {

	private static final long serialVersionUID = -9108151187216817530L;

	public Tr_sogp_sogi() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tr_sogp_sogi))
			return false;
		Tr_sogp_sogi tr_sogp_sogi = (Tr_sogp_sogi)aObj;
		if (getCid_p() == null && tr_sogp_sogi.getCid_p() != null)
			return false;
		if (!getCid_p().equals(tr_sogp_sogi.getCid_p()))
			return false;
		if ((getCid_i() != null && !getCid_i().equals(tr_sogp_sogi.getCid_i())) || (getCid_i() == null && tr_sogp_sogi.getCid_i() != null))
			return false;
		if ((getBid() != null && !getBid().equals(tr_sogp_sogi.getBid())) || (getBid() == null && tr_sogp_sogi.getBid() != null))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCid_p() != null) {
			hashcode = hashcode + (getCid_p().getORMID() == null ? 0 : getCid_p().getORMID().hashCode());
		}
		hashcode = hashcode + (getCid_i() == null ? 0 : getCid_i().hashCode());
		hashcode = hashcode + (getBid() == null ? 0 : getBid().hashCode());
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto cid_p;

	private String cid_i;

	private String bid;

	private char fl_imp_sog;

	private Character fl_sog_mod_da;

	private Character fl_imp_tit_sog;

	public void setCid_i(String value) {
		this.cid_i = value;
	}

	public String getCid_i() {
		return cid_i;
	}

	public void setBid(String value) {
		this.bid = value;
	}

	public String getBid() {
		return bid;
	}

	public void setFl_imp_sog(char value) {
		this.fl_imp_sog = value;
	}

	public char getFl_imp_sog() {
		return fl_imp_sog;
	}

	public void setFl_sog_mod_da(char value) {
		setFl_sog_mod_da(new Character(value));
	}

	public void setFl_sog_mod_da(Character value) {
		this.fl_sog_mod_da = value;
	}

	public Character getFl_sog_mod_da() {
		return fl_sog_mod_da;
	}

	public void setFl_imp_tit_sog(char value) {
		setFl_imp_tit_sog(new Character(value));
	}

	public void setFl_imp_tit_sog(Character value) {
		this.fl_imp_tit_sog = value;
	}

	public Character getFl_imp_tit_sog() {
		return fl_imp_tit_sog;
	}

	public void setCid_p(it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto value) {
		this.cid_p = value;
	}

	public it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto getCid_p() {
		return cid_p;
	}

	public String toString() {
		return String.valueOf(((getCid_p() == null) ? "" : String.valueOf(getCid_p().getORMID())) + " " + getCid_i() + " " + getBid());
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
