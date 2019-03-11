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
 * BIBLIOTECARI E USERID (TLRBIT + TLFPUT)
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_bibliotecario implements Serializable {

	private static final long serialVersionUID = -7289026324793037440L;

	public Tbf_bibliotecario() {
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro id_parametro;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali id_utente_professionale;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_profilo_abilitazione cd_prof;

	private String cd_livello_gbantico;

	private String cd_livello_gbmoderno;

	private String cd_livello_gssoggetti;

	private String cd_livello_gsclassi;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Trl_attivita_bibliotecario = new java.util.HashSet();

	private java.util.Set Tbf_bibliotecario_default = new java.util.HashSet();

	/**
	 * massimo livello di autorità utilizzabile dal bibliotecaro in catalogazione materiale antico
	 */
	public void setCd_livello_gbantico(String value) {
		this.cd_livello_gbantico = value;
	}

	/**
	 * massimo livello di autorità utilizzabile dal bibliotecaro in catalogazione materiale antico
	 */
	public String getCd_livello_gbantico() {
		return cd_livello_gbantico;
	}

	/**
	 * massimo livello di autorità utilizzabile dal bibliotecaro in catalogazione materiale moderno
	 */
	public void setCd_livello_gbmoderno(String value) {
		this.cd_livello_gbmoderno = value;
	}

	/**
	 * massimo livello di autorità utilizzabile dal bibliotecaro in catalogazione materiale moderno
	 */
	public String getCd_livello_gbmoderno() {
		return cd_livello_gbmoderno;
	}

	/**
	 * massimo livello di autorità utilizzabile dal bibliotecaro in catalogazione soggetti
	 */
	public void setCd_livello_gssoggetti(String value) {
		this.cd_livello_gssoggetti = value;
	}

	/**
	 * massimo livello di autorità utilizzabile dal bibliotecaro in catalogazione soggetti
	 */
	public String getCd_livello_gssoggetti() {
		return cd_livello_gssoggetti;
	}

	/**
	 * massimo livello di autorità utilizzabile dal bibliotecaro in gestione indici di classificazione
	 */
	public void setCd_livello_gsclassi(String value) {
		this.cd_livello_gsclassi = value;
	}

	/**
	 * massimo livello di autorità utilizzabile dal bibliotecaro in gestione indici di classificazione
	 */
	public String getCd_livello_gsclassi() {
		return cd_livello_gsclassi;
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

	public void setId_parametro(it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro value) {
		this.id_parametro = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro getId_parametro() {
		return id_parametro;
	}

	public void setCd_prof(it.iccu.sbn.polo.orm.amministrazione.Tbf_profilo_abilitazione value) {
		this.cd_prof = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_profilo_abilitazione getCd_prof() {
		return cd_prof;
	}

	public void setId_utente_professionale(it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali value) {
		this.id_utente_professionale = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali getId_utente_professionale() {
		return id_utente_professionale;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali getORMID() {
		return getId_utente_professionale();
	}

	public void setTrl_attivita_bibliotecario(java.util.Set value) {
		this.Trl_attivita_bibliotecario = value;
	}

	public java.util.Set getTrl_attivita_bibliotecario() {
		return Trl_attivita_bibliotecario;
	}

	public void setTbf_bibliotecario_default(java.util.Set value) {
		this.Tbf_bibliotecario_default = value;
	}

	public java.util.Set getTbf_bibliotecario_default() {
		return Tbf_bibliotecario_default;
	}

	public String toString() {
		return String.valueOf(((getId_utente_professionale() == null) ? "" : String.valueOf(getId_utente_professionale().getORMID())));
	}

}
