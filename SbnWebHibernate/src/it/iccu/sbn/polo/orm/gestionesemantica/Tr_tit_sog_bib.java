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
 * SOGGETTI DI OGGETTI BIBLIOGRAFICI IN BIBLIOTECA (TPSSOO))
 */
/**
 * ORM-Persistable Class
 */
public class Tr_tit_sog_bib extends Tb_base {

	private static final long serialVersionUID = 7796597167859182042L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_sog_bib))
			return false;
		Tr_tit_sog_bib tr_tit_sog_bib = (Tr_tit_sog_bib)aObj;
		if (getC() == null)
			return false;
		if (!getC().getORMID().equals(tr_tit_sog_bib.getC().getORMID()))
			return false;
		if (getB() == null)
			return false;
		if (!getB().getORMID().equals(tr_tit_sog_bib.getB().getORMID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getC() != null) {
			hashcode = hashcode + getC().getORMID().hashCode();
		}
		if (getB() != null) {
			hashcode = hashcode + getB().getORMID().hashCode();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto c;

	private it.iccu.sbn.polo.orm.gestionesemantica.Tr_soggettari_biblioteche cd_biblioteca;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String nota_tit_sog_bib;

	private Short posizione;

	/**
	 * note al legame
	 */
	public void setNota_tit_sog_bib(String value) {
		this.nota_tit_sog_bib = value;
	}

	/**
	 * note al legame
	 */
	public String getNota_tit_sog_bib() {
		return nota_tit_sog_bib;
	}

	public void setC(it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto value) {
		this.c = value;
	}

	public it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto getC() {
		return c;
	}

	public void setCd_biblioteca(it.iccu.sbn.polo.orm.gestionesemantica.Tr_soggettari_biblioteche value) {
		this.cd_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.gestionesemantica.Tr_soggettari_biblioteche getCd_biblioteca() {
		return cd_biblioteca;
	}

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public String toString() {
		return String.valueOf(((getC() == null) ? "" : String.valueOf(getC().getORMID())) + " " + ((getB() == null) ? "" : String.valueOf(getB().getORMID())));
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

	public Short getPosizione() {
		return posizione;
	}

	public void setPosizione(Short posizione) {
		this.posizione = posizione;
	}


}
