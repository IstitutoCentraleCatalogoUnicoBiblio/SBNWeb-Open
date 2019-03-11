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
 * OGGETTO BIBLIOGRAFICO IN BIBLIOTECA
 */
/**
 * ORM-Persistable Class
 */
public class Tr_tit_bib implements Serializable {

	private static final long serialVersionUID = 4803471813223617301L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_bib))
			return false;
		Tr_tit_bib tr_tit_bib = (Tr_tit_bib)aObj;
		if (getB() == null)
			return false;
		if (!getB().getORMID().equals(tr_tit_bib.getB().getORMID()))
			return false;
		if (getCd_polo() == null)
			return false;
		if (getCd_polo().getCd_polo() != tr_tit_bib.getCd_polo().getCd_polo())
			return false;
		if (!getCd_polo().getCd_biblioteca().equals(tr_tit_bib.getCd_polo().getCd_biblioteca()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getB() != null) {
			hashcode = hashcode + getB().getORMID().hashCode();
		}
		if (getCd_polo() != null) {
			hashcode = hashcode + getCd_polo().getCd_polo().hashCode();
			hashcode = hashcode + getCd_polo().getCd_biblioteca().hashCode();
		}
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_polo;

	private char fl_gestione;

	private char fl_disp_elettr;

	private char fl_allinea;

	private char fl_allinea_sbnmarc;

	private char fl_allinea_cla;

	private char fl_allinea_sog;

	private char fl_allinea_luo;

	private char fl_allinea_rep;

	private char fl_mutilo;

	private String ds_consistenza;

	private char fl_possesso;

	private String ds_fondo;

	private String ds_segn;

	private String ds_antica_segn;

	private String nota_tit_bib;

	private String uri_copia;

	private Character tp_digitalizz;

	private java.sql.Timestamp ts_ins_prima_coll;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setFl_gestione(char value) {
		this.fl_gestione = value;
	}

	public char getFl_gestione() {
		return fl_gestione;
	}

	public void setFl_disp_elettr(char value) {
		this.fl_disp_elettr = value;
	}

	public char getFl_disp_elettr() {
		return fl_disp_elettr;
	}

	public void setFl_allinea(char value) {
		this.fl_allinea = value;
	}

	public char getFl_allinea() {
		return fl_allinea;
	}

	public void setFl_allinea_sbnmarc(char value) {
		this.fl_allinea_sbnmarc = value;
	}

	public char getFl_allinea_sbnmarc() {
		return fl_allinea_sbnmarc;
	}

	public void setFl_allinea_cla(char value) {
		this.fl_allinea_cla = value;
	}

	public char getFl_allinea_cla() {
		return fl_allinea_cla;
	}

	public void setFl_allinea_sog(char value) {
		this.fl_allinea_sog = value;
	}

	public char getFl_allinea_sog() {
		return fl_allinea_sog;
	}

	public void setFl_allinea_luo(char value) {
		this.fl_allinea_luo = value;
	}

	public char getFl_allinea_luo() {
		return fl_allinea_luo;
	}

	public void setFl_allinea_rep(char value) {
		this.fl_allinea_rep = value;
	}

	public char getFl_allinea_rep() {
		return fl_allinea_rep;
	}

	public void setFl_mutilo(char value) {
		this.fl_mutilo = value;
	}

	public char getFl_mutilo() {
		return fl_mutilo;
	}

	public void setDs_consistenza(String value) {
		this.ds_consistenza = value;
	}

	public String getDs_consistenza() {
		return ds_consistenza;
	}

	public void setFl_possesso(char value) {
		this.fl_possesso = value;
	}

	public char getFl_possesso() {
		return fl_possesso;
	}

	public void setDs_fondo(String value) {
		this.ds_fondo = value;
	}

	public String getDs_fondo() {
		return ds_fondo;
	}

	public void setDs_segn(String value) {
		this.ds_segn = value;
	}

	public String getDs_segn() {
		return ds_segn;
	}

	public void setDs_antica_segn(String value) {
		this.ds_antica_segn = value;
	}

	public String getDs_antica_segn() {
		return ds_antica_segn;
	}

	public void setNota_tit_bib(String value) {
		this.nota_tit_bib = value;
	}

	public String getNota_tit_bib() {
		return nota_tit_bib;
	}

	public void setUri_copia(String value) {
		this.uri_copia = value;
	}

	public String getUri_copia() {
		return uri_copia;
	}

	public void setTp_digitalizz(char value) {
		setTp_digitalizz(new Character(value));
	}

	public void setTp_digitalizz(Character value) {
		this.tp_digitalizz = value;
	}

	public Character getTp_digitalizz() {
		return tp_digitalizz;
	}

	public void setTs_ins_prima_coll(java.sql.Timestamp value) {
		this.ts_ins_prima_coll = value;
	}

	public java.sql.Timestamp getTs_ins_prima_coll() {
		return ts_ins_prima_coll;
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

	public void setCd_polo(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_polo() {
		return cd_polo;
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())) + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getCd_polo()) + " " + String.valueOf(getCd_polo().getCd_biblioteca())));
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
