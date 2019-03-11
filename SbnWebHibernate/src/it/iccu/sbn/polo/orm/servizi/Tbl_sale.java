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

import java.util.HashSet;
import java.util.Set;
/**
 * Sale
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_sale extends Tb_base {

	private static final long serialVersionUID = -4660087348067812681L;

	public Tbl_sale() {
	}

	private int id_sale;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String cod_sala;

	private String descr;

	private short num_max_posti;

	private short num_prg_posti;

	private Set<Tbl_posti_sala> Tbl_posti_sala = new HashSet<Tbl_posti_sala>();

	private Set<Tbl_modello_calendario> calendari = new HashSet<Tbl_modello_calendario>();

	private short durata_fascia;
	private short num_max_fasce_prenot;
	private short num_max_utenti_per_prenot;

	private char fl_prenot_remoto;

	protected void setId_sale(int value) {
		this.id_sale = value;
	}

	public int getId_sale() {
		return id_sale;
	}

	public int getORMID() {
		return getId_sale();
	}

	/**
	 * codice della sala
	 */
	public void setCod_sala(String value) {
		this.cod_sala = value;
	}

	/**
	 * codice della sala
	 */
	public String getCod_sala() {
		return cod_sala;
	}

	/**
	 * descrizione della sala
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * descrizione della sala
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * numero massimo di posti disponibili
	 */
	public void setNum_max_posti(short value) {
		this.num_max_posti = value;
	}

	/**
	 * numero massimo di posti disponibili
	 */
	public short getNum_max_posti() {
		return num_max_posti;
	}

	/**
	 * numero dei posti occupati in sala
	 */
	public void setNum_prg_posti(short value) {
		this.num_prg_posti = value;
	}

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public Set<Tbl_posti_sala> getTbl_posti_sala() {
		return Tbl_posti_sala;
	}

	public void setTbl_posti_sala(Set<Tbl_posti_sala> tbl_posti_sala) {
		Tbl_posti_sala = tbl_posti_sala;
	}

	public short getNum_prg_posti() {
		return num_prg_posti;
	}

	public String toString() {
		return String.valueOf(getId_sale());
	}

	public Set<Tbl_modello_calendario> getCalendari() {
		return calendari;
	}

	public void setCalendari(Set<Tbl_modello_calendario> calendario) {
		this.calendari = calendario;
	}

	public short getDurata_fascia() {
		return durata_fascia;
	}

	public void setDurata_fascia(short durata_fascia) {
		this.durata_fascia = durata_fascia;
	}

	public short getNum_max_fasce_prenot() {
		return num_max_fasce_prenot;
	}

	public void setNum_max_fasce_prenot(short nun_max_fasce_prenot) {
		this.num_max_fasce_prenot = nun_max_fasce_prenot;
	}

	public short getNum_max_utenti_per_prenot() {
		return num_max_utenti_per_prenot;
	}

	public void setNum_max_utenti_per_prenot(short num_max_utenti_per_prenot) {
		this.num_max_utenti_per_prenot = num_max_utenti_per_prenot;
	}

	public char getFl_prenot_remoto() {
		return fl_prenot_remoto;
	}

	public void setFl_prenot_remoto(char fl_prenot_remoto) {
		this.fl_prenot_remoto = fl_prenot_remoto;
	}

}
