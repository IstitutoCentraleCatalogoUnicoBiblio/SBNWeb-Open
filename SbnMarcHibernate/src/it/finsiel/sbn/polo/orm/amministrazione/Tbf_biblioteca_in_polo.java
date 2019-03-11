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

import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;

import java.io.Serializable;
/**
 * BIBLIOTECHE ABILITATE AD OPERARE NEL POLO (TPFSBI)
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_biblioteca_in_polo extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 2861967810452570380L;

	public Tbf_biblioteca_in_polo() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_biblioteca_in_polo))
			return false;
		Tbf_biblioteca_in_polo tbf_biblioteca_in_polo = (Tbf_biblioteca_in_polo)aObj;
		if ((getCd_biblioteca() != null && !getCd_biblioteca().equals(tbf_biblioteca_in_polo.getCd_biblioteca())) || (getCd_biblioteca() == null && tbf_biblioteca_in_polo.getCd_biblioteca() != null))
			return false;
		if (getCd_polo() == null && tbf_biblioteca_in_polo.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(tbf_biblioteca_in_polo.getCd_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCd_biblioteca() == null ? 0 : getCd_biblioteca().hashCode());
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getORMID() == null ? 0 : getCd_polo().getORMID().hashCode());
		}
		return hashcode;
	}

	private String cd_biblioteca;

	private String  cod_polo;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro id_parametro;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo cd_polo;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita id_gruppo_attivita_polo;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca id_biblioteca;

	private String ky_biblioteca;

	private String cd_ana_biblioteca;

	private String ds_biblioteca;

	private String ds_citta;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tbf_contatore = new java.util.HashSet();

	private java.util.Set Tbf_profilo_abilitazione = new java.util.HashSet();

	private java.util.Set Trf_utente_professionale_biblioteca = new java.util.HashSet();

	private java.util.Set Tr_soggettari_biblioteche = new java.util.HashSet();

	private java.util.Set Tr_thesauri_biblioteche = new java.util.HashSet();

	private java.util.Set Tr_sistemi_classi_biblioteche = new java.util.HashSet();

	public void setCd_biblioteca(String value) {
		this.cd_biblioteca = value;
		this.settaParametro(KeyParameter.XXXcd_biblioteca,value);
	}

	public String getCd_biblioteca() {
		return cd_biblioteca;
	}

	public void setKy_biblioteca(String value) {
		this.ky_biblioteca = value;
		this.settaParametro(KeyParameter.XXXky_biblioteca,value);
	}

	public String getKy_biblioteca() {
		return ky_biblioteca;
	}

	public void setCd_ana_biblioteca(String value) {
		this.cd_ana_biblioteca = value;
		this.settaParametro(KeyParameter.XXXcd_ana_biblioteca,value);
	}

	public String getCd_ana_biblioteca() {
		return cd_ana_biblioteca;
	}

	public void setDs_biblioteca(String value) {
		this.ds_biblioteca = value;
		this.settaParametro(KeyParameter.XXXds_biblioteca,value);
	}

	public String getDs_biblioteca() {
		return ds_biblioteca;
	}

	public void setDs_citta(String value) {
		this.ds_citta = value;
		this.settaParametro(KeyParameter.XXXds_citta,value);
	}

	public String getDs_citta() {
		return ds_citta;
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
		this.settaParametro(KeyParameter.XXXfl_canc,value);
	}

	/**
	 * Flag di cancellazione logica
	 */
	public char getFl_canc() {
		return fl_canc;
	}

	public void setId_biblioteca(it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca value) {
		this.id_biblioteca = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca getId_biblioteca() {
		return id_biblioteca;
	}

	public void setId_gruppo_attivita_polo(it.finsiel.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita value) {
		this.id_gruppo_attivita_polo = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita getId_gruppo_attivita_polo() {
		return id_gruppo_attivita_polo;
	}

	public void setCd_polo(it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo value) {
		this.cd_polo = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo getCd_polo() {
		return cd_polo;
	}

	public void setId_parametro(it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro value) {
		this.id_parametro = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro getId_parametro() {
		return id_parametro;
	}

	public void setTbf_contatore(java.util.Set value) {
		this.Tbf_contatore = value;
	}

	public java.util.Set getTbf_contatore() {
		return Tbf_contatore;
	}


	public void setTbf_profilo_abilitazione(java.util.Set value) {
		this.Tbf_profilo_abilitazione = value;
	}

	public java.util.Set getTbf_profilo_abilitazione() {
		return Tbf_profilo_abilitazione;
	}


	public void setTrf_utente_professionale_biblioteca(java.util.Set value) {
		this.Trf_utente_professionale_biblioteca = value;
	}

	public java.util.Set getTrf_utente_professionale_biblioteca() {
		return Trf_utente_professionale_biblioteca;
	}

	public void setTr_soggettari_biblioteche(java.util.Set value) {
		this.Tr_soggettari_biblioteche = value;
	}

	public java.util.Set getTr_soggettari_biblioteche() {
		return Tr_soggettari_biblioteche;
	}

	public void setTr_thesauri_biblioteche(java.util.Set value) {
		this.Tr_thesauri_biblioteche = value;
	}

	public java.util.Set getTr_thesauri_biblioteche() {
		return Tr_thesauri_biblioteche;
	}

	public String toString() {
		return String.valueOf(getCd_biblioteca() + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getORMID())));
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

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
		this.settaParametro(KeyParameter.XXXcd_polo,cod_polo);
	}

	public java.util.Set getTr_sistemi_classi_biblioteche() {
		return Tr_sistemi_classi_biblioteche;
	}

	public void setTr_sistemi_classi_biblioteche(
			java.util.Set tr_sistemi_classi_biblioteche) {
		Tr_sistemi_classi_biblioteche = tr_sistemi_classi_biblioteche;
	}
}
