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
 * SERVIZI EROGATI DALLA BIBLIOTECA
 */
/**
 * ORM-Persistable Class
 */

public class Tbl_servizio extends Tb_base {

	private static final long serialVersionUID = -286782846417701639L;

	public Tbl_servizio() {
	}

	private int id_servizio;

	private it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio id_tipo_servizio;

	private String cod_servizio;

	private String descr;

	private short num_max_rich;

	private short num_max_mov;

	private short dur_mov;

	private short dur_max_rinn1;

	private short dur_max_rinn2;

	private short dur_max_rinn3;

	private short num_max_riprod;

	private Short n_max_pren_posto;

	private Short n_gg_prep_supp;

	private String ts_orario_limite_pren;

	private short max_gg_ant;

	private short max_gg_dep;

	private short max_gg_cons;

	private java.math.BigDecimal n_max_pren;

	private java.math.BigDecimal n_max_ggval_pren;

	private Short n_gg_rest_ill;

	private it.iccu.sbn.polo.orm.servizi.Tbl_penalita_servizio Tbl_penalita_servizio;

	private java.util.Set<Tbl_richiesta_servizio> Tbl_richiesta_servizio = new java.util.HashSet<Tbl_richiesta_servizio>();

	private java.util.Set<Trl_autorizzazioni_servizi> Trl_autorizzazioni_servizi = new java.util.HashSet<Trl_autorizzazioni_servizi>();

	private java.util.Set<Trl_diritti_utente> Trl_diritti_utente = new java.util.HashSet<Trl_diritti_utente>();

	protected void setId_servizio(int value) {
		this.id_servizio = value;
	}

	public int getId_servizio() {
		return id_servizio;
	}

	public int getORMID() {
		return getId_servizio();
	}

	/**
	 * codice del servizio ad es. : p3= prestito max 3 libri
	 */
	public void setCod_servizio(String value) {
		this.cod_servizio = value;
	}

	/**
	 * codice del servizio ad es. : p3= prestito max 3 libri
	 */
	public String getCod_servizio() {
		return cod_servizio;
	}

	/**
	 * descrizione del servizio
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * descrizione del servizio
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * numero massimo di richieste possibili
	 */
	public void setNum_max_rich(short value) {
		this.num_max_rich = value;
	}

	/**
	 * numero massimo di richieste possibili
	 */
	public short getNum_max_rich() {
		return num_max_rich;
	}

	/**
	 * numero massimo di movimenti attivi previsti contemporaneamente
	 */
	public void setNum_max_mov(short value) {
		this.num_max_mov = value;
	}

	/**
	 * numero massimo di movimenti attivi previsti contemporaneamente
	 */
	public short getNum_max_mov() {
		return num_max_mov;
	}

	/**
	 * durata massima del servizio espressa in giorni
	 */
	public void setDur_mov(short value) {
		this.dur_mov = value;
	}

	/**
	 * durata massima del servizio espressa in giorni
	 */
	public short getDur_mov() {
		return dur_mov;
	}

	/**
	 * durata massima del servizio espressa in giorni in caso di primo rinnovo
	 */
	public void setDur_max_rinn1(short value) {
		this.dur_max_rinn1 = value;
	}

	/**
	 * durata massima del servizio espressa in giorni in caso di primo rinnovo
	 */
	public short getDur_max_rinn1() {
		return dur_max_rinn1;
	}

	/**
	 * durata massima del servizio espressa in giorni in caso di secondo rinnovo
	 */
	public void setDur_max_rinn2(short value) {
		this.dur_max_rinn2 = value;
	}

	/**
	 * durata massima del servizio espressa in giorni in caso di secondo rinnovo
	 */
	public short getDur_max_rinn2() {
		return dur_max_rinn2;
	}

	/**
	 * durata massima del servizio espressa in giorni in caso di terzo rinnovo
	 */
	public void setDur_max_rinn3(short value) {
		this.dur_max_rinn3 = value;
	}

	/**
	 * durata massima del servizio espressa in giorni in caso di terzo rinnovo
	 */
	public short getDur_max_rinn3() {
		return dur_max_rinn3;
	}

	/**
	 * numero massimo di riproduzioni che una biblioteca puo' effettuare giornalmente (per tipo servizio = fotoriproduzione, digitalizzazione, ...)
	 */
	public void setNum_max_riprod(short value) {
		this.num_max_riprod = value;
	}

	/**
	 * numero massimo di riproduzioni che una biblioteca puo' effettuare giornalmente (per tipo servizio = fotoriproduzione, digitalizzazione, ...)
	 */
	public short getNum_max_riprod() {
		return num_max_riprod;
	}

	public Short getN_max_pren_posto() {
		return n_max_pren_posto;
	}

	public void setN_max_pren_posto(Short n_max_pren_posto) {
		this.n_max_pren_posto = n_max_pren_posto;
	}

	public Short getN_gg_prep_supp() {
		return n_gg_prep_supp;
	}

	public void setN_gg_prep_supp(Short n_gg_prep_supp) {
		this.n_gg_prep_supp = n_gg_prep_supp;
	}

	public String getTs_orario_limite_pren() {
		return ts_orario_limite_pren;
	}

	public void setTs_orario_limite_pren(String ts_orario_limite_pren) {
		this.ts_orario_limite_pren = ts_orario_limite_pren;
	}

	/**
	 * numero massimo di giorni di anticipo con cui si puo' effettuare  una richiesta (ad es.: tipo servizio = prelazione)
	 */
	public void setMax_gg_ant(short value) {
		this.max_gg_ant = value;
	}

	/**
	 * numero massimo di giorni di anticipo con cui si puo' effettuare  una richiesta (ad es.: tipo servizio = prelazione)
	 */
	public short getMax_gg_ant() {
		return max_gg_ant;
	}

	/**
	 * numero massimo di giorni di durata del deposito per lettura
	 */
	public void setMax_gg_dep(short value) {
		this.max_gg_dep = value;
	}

	/**
	 * numero massimo di giorni di durata del deposito per lettura
	 */
	public short getMax_gg_dep() {
		return max_gg_dep;
	}

	/**
	 * numero massimo di giorni di ritardo nella consegna di materiale (ad es.: per quello fuori sede)
	 */
	public void setMax_gg_cons(short value) {
		this.max_gg_cons = value;
	}

	/**
	 * numero massimo di giorni di ritardo nella consegna di materiale (ad es.: per quello fuori sede)
	 */
	public short getMax_gg_cons() {
		return max_gg_cons;
	}

	/**
	 * numero massimo di prenotazioni
	 */
	public void setN_max_pren(java.math.BigDecimal value) {
		this.n_max_pren = value;
	}

	/**
	 * numero massimo di prenotazioni
	 */
	public java.math.BigDecimal getN_max_pren() {
		return n_max_pren;
	}

	/**
	 * numero massimo di giorni di validita' della prenotazione
	 */
	public void setN_max_ggval_pren(java.math.BigDecimal value) {
		this.n_max_ggval_pren = value;
	}

	/**
	 * numero massimo di giorni di validita' della prenotazione
	 */
	public java.math.BigDecimal getN_max_ggval_pren() {
		return n_max_ggval_pren;
	}

	public Short getN_gg_rest_ill() {
		return n_gg_rest_ill;
	}

	public void setN_gg_rest_ill(Short n_gg_rest_ill) {
		this.n_gg_rest_ill = n_gg_rest_ill;
	}

	public void setId_tipo_servizio(it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio value) {
		this.id_tipo_servizio = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio getId_tipo_servizio() {
		return id_tipo_servizio;
	}

	public void setTbl_penalita_servizio(it.iccu.sbn.polo.orm.servizi.Tbl_penalita_servizio value) {
		this.Tbl_penalita_servizio = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_penalita_servizio getTbl_penalita_servizio() {
		return Tbl_penalita_servizio;
	}

	public java.util.Set<Tbl_richiesta_servizio> getTbl_richiesta_servizio() {
		return Tbl_richiesta_servizio;
	}

	public void setTbl_richiesta_servizio(
			java.util.Set<Tbl_richiesta_servizio> tbl_richiesta_servizio) {
		Tbl_richiesta_servizio = tbl_richiesta_servizio;
	}

	public java.util.Set<Trl_autorizzazioni_servizi> getTrl_autorizzazioni_servizi() {
		return Trl_autorizzazioni_servizi;
	}

	public void setTrl_autorizzazioni_servizi(
			java.util.Set<Trl_autorizzazioni_servizi> trl_autorizzazioni_servizi) {
		Trl_autorizzazioni_servizi = trl_autorizzazioni_servizi;
	}

	public java.util.Set<Trl_diritti_utente> getTrl_diritti_utente() {
		return Trl_diritti_utente;
	}

	public void setTrl_diritti_utente(
			java.util.Set<Trl_diritti_utente> trl_diritti_utente) {
		Trl_diritti_utente = trl_diritti_utente;
	}

	public String toString() {
		return String.valueOf(getId_servizio());
	}

}
