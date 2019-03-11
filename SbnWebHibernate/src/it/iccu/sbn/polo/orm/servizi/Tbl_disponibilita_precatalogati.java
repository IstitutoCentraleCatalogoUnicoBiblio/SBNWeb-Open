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
package it.iccu.sbn.polo.orm.servizi;

import java.io.Serializable;
/**
 * Disponibilità precatalogati
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_disponibilita_precatalogati implements Serializable {

	private static final long serialVersionUID = -1716473501213899907L;

	public Tbl_disponibilita_precatalogati() {
	}

	private int id_disponibilita_precatalogati;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private int cod_segn;

	private String segn_inizio;

	private String segn_fine;

	private String cod_no_disp;

	private String cod_frui;

	private String segn_da;

	private String segn_a;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private void setId_disponibilita_precatalogati(int value) {
		this.id_disponibilita_precatalogati = value;
	}

	public int getId_disponibilita_precatalogati() {
		return id_disponibilita_precatalogati;
	}

	public int getORMID() {
		return getId_disponibilita_precatalogati();
	}

	/**
	 * codice che identifica univocamente i range di segnatura
	 */
	public void setCod_segn(int value) {
		this.cod_segn = value;
	}

	/**
	 * codice che identifica univocamente i range di segnatura
	 */
	public int getCod_segn() {
		return cod_segn;
	}

	/**
	 * segnatura iniziale
	 */
	public void setSegn_inizio(String value) {
		this.segn_inizio = value;
	}

	/**
	 * segnatura iniziale
	 */
	public String getSegn_inizio() {
		return segn_inizio;
	}

	/**
	 * segnatura finale
	 */
	public void setSegn_fine(String value) {
		this.segn_fine = value;
	}

	/**
	 * segnatura finale
	 */
	public String getSegn_fine() {
		return segn_fine;
	}

	/**
	 * codice di non disponibilita' del documento
	 */
	public void setCod_no_disp(String value) {
		this.cod_no_disp = value;
	}

	/**
	 * codice di non disponibilita' del documento
	 */
	public String getCod_no_disp() {
		return cod_no_disp;
	}

	/**
	 * codice della fruizione
	 */
	public void setCod_frui(String value) {
		this.cod_frui = value;
	}

	/**
	 * codice della fruizione
	 */
	public String getCod_frui() {
		return cod_frui;
	}

	/**
	 * ESTREMO INIZIALE DELL’INTERVALLO (normalizzato)
	 */
	public void setSegn_da(String value) {
		this.segn_da = value;
	}

	/**
	 * ESTREMO INIZIALE DELL’INTERVALLO (normalizzato)
	 */
	public String getSegn_da() {
		return segn_da;
	}

	/**
	 * ESTREMO FINALE DELL’INTERVALLO (normalizzato)
	 */
	public void setSegn_a(String value) {
		this.segn_a = value;
	}

	/**
	 * ESTREMO FINALE DELL’INTERVALLO (normalizzato)
	 */
	public String getSegn_a() {
		return segn_a;
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

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public String toString() {
		return String.valueOf(getId_disponibilita_precatalogati());
	}

}
