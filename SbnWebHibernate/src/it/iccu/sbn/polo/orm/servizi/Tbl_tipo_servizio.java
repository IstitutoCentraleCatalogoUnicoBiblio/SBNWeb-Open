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
/**
 * TIPI DI SERVIZIO EROGATI DALLA BIBLIOTECA
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_tipo_servizio extends Tb_base {

	private static final long serialVersionUID = -6461482549164395256L;

	public Tbl_tipo_servizio() {
	}

	private int id_tipo_servizio;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String cod_tipo_serv;

	private short num_max_mov;

	private short ore_ridis;

	private char penalita;

	//private short coda_richieste;

	private char coda_richieste;

	private char ins_richieste_utente;

	private char anche_da_remoto;

	private String cd_iso_ill;

	private java.util.Set<Trl_tariffe_modalita_erogazione> Trl_tariffe_modalita_erogazione = new java.util.HashSet<Trl_tariffe_modalita_erogazione>();

	private java.util.Set<Tbl_calendario_servizi> Tbl_calendario_servizi = new java.util.HashSet<Tbl_calendario_servizi>();

	private java.util.Set<Tbl_servizio> Tbl_servizio = new java.util.HashSet<Tbl_servizio>();

	private java.util.Set<Tbl_iter_servizio> Tbl_iter_servizio = new java.util.HashSet<Tbl_iter_servizio>();

	private java.util.Set<Tbl_servizio_web_dati_richiesti> Tbl_servizio_web_dati_richiesti = new java.util.HashSet<Tbl_servizio_web_dati_richiesti>();

	protected void setId_tipo_servizio(int value) {
		this.id_tipo_servizio = value;
	}

	public int getId_tipo_servizio() {
		return id_tipo_servizio;
	}

	public int getORMID() {
		return getId_tipo_servizio();
	}

	/**
	 * codice tipo servizio
	 */
	public void setCod_tipo_serv(String value) {
		this.cod_tipo_serv = value;
	}

	/**
	 * codice tipo servizio
	 */
	public String getCod_tipo_serv() {
		return cod_tipo_serv;
	}

	/**
	 * numero massimo di movimenti contemporaneamente attivi previsti
	 */
	public void setNum_max_mov(short value) {
		this.num_max_mov = value;
	}

	/**
	 * numero massimo di movimenti contemporaneamente attivi previsti
	 */
	public short getNum_max_mov() {
		return num_max_mov;
	}

	/**
	 * tempo in ore necessario per la ridisponibilita' di un documento (ricollocazione). valore da utilizzare per il calcolo della data presunta di inizio del servizio.
	 */
	public void setOre_ridis(short value) {
		this.ore_ridis = value;
	}

	/**
	 * tempo in ore necessario per la ridisponibilita' di un documento (ricollocazione). valore da utilizzare per il calcolo della data presunta di inizio del servizio.
	 */
	public short getOre_ridis() {
		return ore_ridis;
	}

	/**
	 * indicatore di tipo servizio con penalita' per inosservanza del regolamento
	 */
	public void setPenalita(char value) {
		this.penalita = value;
	}

	/**
	 * indicatore di tipo servizio con penalita' per inosservanza del regolamento
	 */
	public char getPenalita() {
		return penalita;
	}

	/**
	 * numero massimo di richieste inevase
	 */
	public void setCoda_richieste(char value) {
		this.coda_richieste = value;
	}

	/**
	 * indicatore di tipo servizio con penalita' per inosservanza del regolamento
	 */
	public char getCoda_richieste() {
		return coda_richieste;
	}

/*	public void setCoda_richieste(short value) {
		this.coda_richieste = value;
	}
*/	/**
	 * numero massimo di richieste inevase
	 */
/*	public short getCoda_richieste() {
		return coda_richieste;
	}
*/

	public char getIns_richieste_utente() {
		return ins_richieste_utente;
	}

	public void setIns_richieste_utente(char value) {
		this.ins_richieste_utente = value;
	}

	public char getAnche_da_remoto() {
		return anche_da_remoto;
	}

	public void setAnche_da_remoto(char value) {
		this.anche_da_remoto = value;
	}

	public String getCd_iso_ill() {
		return cd_iso_ill;
	}

	public void setCd_iso_ill(String cd_iso_ill) {
		this.cd_iso_ill = cd_iso_ill;
	}

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public java.util.Set<Trl_tariffe_modalita_erogazione> getTrl_tariffe_modalita_erogazione() {
		return Trl_tariffe_modalita_erogazione;
	}

	public void setTrl_tariffe_modalita_erogazione(
			java.util.Set<Trl_tariffe_modalita_erogazione> trl_tariffe_modalita_erogazione) {
		Trl_tariffe_modalita_erogazione = trl_tariffe_modalita_erogazione;
	}

	public java.util.Set<Tbl_calendario_servizi> getTbl_calendario_servizi() {
		return Tbl_calendario_servizi;
	}

	public void setTbl_calendario_servizi(
			java.util.Set<Tbl_calendario_servizi> tbl_calendario_servizi) {
		Tbl_calendario_servizi = tbl_calendario_servizi;
	}

	public java.util.Set<Tbl_servizio> getTbl_servizio() {
		return Tbl_servizio;
	}

	public void setTbl_servizio(java.util.Set<Tbl_servizio> tbl_servizio) {
		Tbl_servizio = tbl_servizio;
	}

	public java.util.Set<Tbl_iter_servizio> getTbl_iter_servizio() {
		return Tbl_iter_servizio;
	}

	public void setTbl_iter_servizio(
			java.util.Set<Tbl_iter_servizio> tbl_iter_servizio) {
		Tbl_iter_servizio = tbl_iter_servizio;
	}

	public java.util.Set<Tbl_servizio_web_dati_richiesti> getTbl_servizio_web_dati_richiesti() {
		return Tbl_servizio_web_dati_richiesti;
	}

	public void setTbl_servizio_web_dati_richiesti(
			java.util.Set<Tbl_servizio_web_dati_richiesti> tbl_servizio_web_dati_richiesti) {
		Tbl_servizio_web_dati_richiesti = tbl_servizio_web_dati_richiesti;
	}

	public String toString() {
		return String.valueOf(getId_tipo_servizio());
	}

}
