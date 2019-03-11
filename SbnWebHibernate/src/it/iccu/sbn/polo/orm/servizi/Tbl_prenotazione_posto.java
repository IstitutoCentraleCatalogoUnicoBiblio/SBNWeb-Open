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

import it.iccu.sbn.polo.orm.Tb_base;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Prenotazione del posto in sala
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_prenotazione_posto extends Tb_base {

	private static final long serialVersionUID = -7745425281712720538L;

	private int id_prenotazione;
	private char cd_stato;
	private Tbf_biblioteca_in_polo biblioteca;
	private Tbl_posti_sala posto;
	private Tbl_utenti utente;
	private Set<Tbl_richiesta_servizio> richieste = new HashSet<Tbl_richiesta_servizio>();
	private Timestamp ts_inizio;
	private Timestamp ts_fine;

	private String cd_cat_mediazione;

	private Set<Tbl_utenti> utenti = new HashSet<Tbl_utenti>();

	public int getId_prenotazione() {
		return id_prenotazione;
	}

	public void setId_prenotazione(int id_prenotazione) {
		this.id_prenotazione = id_prenotazione;
	}

	public char getCd_stato() {
		return cd_stato;
	}

	public void setCd_stato(char cd_stato) {
		this.cd_stato = cd_stato;
	}

	public Tbf_biblioteca_in_polo getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(Tbf_biblioteca_in_polo biblioteca) {
		this.biblioteca = biblioteca;
	}

	public Tbl_posti_sala getPosto() {
		return posto;
	}

	public void setPosto(Tbl_posti_sala posto) {
		this.posto = posto;
	}

	public Tbl_utenti getUtente() {
		return utente;
	}

	public void setUtente(Tbl_utenti utente) {
		this.utente = utente;
	}

	public Set<Tbl_richiesta_servizio> getRichieste() {
		return richieste;
	}

	public void setRichieste(Set<Tbl_richiesta_servizio> servizio) {
		this.richieste = servizio;
	}

	public Timestamp getTs_inizio() {
		return ts_inizio;
	}

	public void setTs_inizio(Timestamp dt_inizio) {
		this.ts_inizio = dt_inizio;
	}

	public Timestamp getTs_fine() {
		return ts_fine;
	}

	public void setTs_fine(Timestamp dt_fine) {
		this.ts_fine = dt_fine;
	}

	public String getCd_cat_mediazione() {
		return cd_cat_mediazione;
	}

	public void setCd_cat_mediazione(String cd_cat_mediazione) {
		this.cd_cat_mediazione = cd_cat_mediazione;
	}

	public Set<Tbl_utenti> getUtenti() {
		return utenti;
	}

	public void setUtenti(Set<Tbl_utenti> utenti) {
		this.utenti = utenti;
	}

}
