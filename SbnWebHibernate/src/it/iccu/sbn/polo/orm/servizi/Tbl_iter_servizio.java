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
 * Passi dell'iter del servizio
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_iter_servizio extends Tb_base {

	private static final long serialVersionUID = 3392189692967541517L;

	public Tbl_iter_servizio() {
	}

	private int id_iter_servizio;

	private short progr_iter;

	private it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio id_tipo_servizio;

	private String cod_attivita;

	private char flag_stampa;

	private short num_pag;

	private String testo;

	private char flg_abil;

	private char flg_rinn;

	private char stato_iter;

	private char obbl;

	private char cod_stato_rich;

	private char stato_mov;

	private char cod_stat_cir;

	private char cod_stato_ric_ill;

	private java.util.Set<Tbl_controllo_iter> Tbl_controllo_iter = new java.util.HashSet<Tbl_controllo_iter>();

	private java.util.Set<Trl_attivita_bibliotecario> Trl_attivita_bibliotecario = new java.util.HashSet<Trl_attivita_bibliotecario>();

	private java.util.Set<Tbl_richiesta_servizio> Tbl_richiesta_servizio = new java.util.HashSet<Tbl_richiesta_servizio>();

	protected void setId_iter_servizio(int value) {
		this.id_iter_servizio = value;
	}

	public int getId_iter_servizio() {
		return id_iter_servizio;
	}

	public int getORMID() {
		return getId_iter_servizio();
	}

	/**
	 * progressivo della sequenza dell'attivita'
	 */
	public void setProgr_iter(short value) {
		this.progr_iter = value;
	}

	/**
	 * progressivo della sequenza dell'attivita'
	 */
	public short getProgr_iter() {
		return progr_iter;
	}

	/**
	 * codice identificativo delle attivita' possibili nell'iter di un servizio
	 */
	public void setCod_attivita(String value) {
		this.cod_attivita = value;
	}

	/**
	 * codice identificativo delle attivita' possibili nell'iter di un servizio
	 */
	public String getCod_attivita() {
		return cod_attivita;
	}

	/**
	 * indicatore di effettuazione di stampa assume i valori s, n
	 */
	public void setFlag_stampa(char value) {
		this.flag_stampa = value;
	}

	/**
	 * indicatore di effettuazione di stampa assume i valori s, n
	 */
	public char getFlag_stampa() {
		return flag_stampa;
	}

	/**
	 * numero delle copie da stampare
	 */
	public void setNum_pag(short value) {
		this.num_pag = value;
	}

	/**
	 * numero delle copie da stampare
	 */
	public short getNum_pag() {
		return num_pag;
	}

	/**
	 * testo a disposizione della biblioteca nella stampa
	 */
	public void setTesto(String value) {
		this.testo = value;
	}

	/**
	 * testo a disposizione della biblioteca nella stampa
	 */
	public String getTesto() {
		return testo;
	}

	/**
	 * flag indicante che l'attivita' prevista nel passo puo' essere svolta da
	 * tutti i bibliotecari o da alcuni in particolare. assume i valori t=tutti,
	 * n=solo quelli incaricati
	 */
	public void setFlg_abil(char value) {
		this.flg_abil = value;
	}

	/**
	 * flag indicante che l'attivita' prevista nel passo puo' essere svolta da
	 * tutti i bibliotecari o da alcuni in particolare. assume i valori t=tutti,
	 * n=solo quelli incaricati
	 */
	public char getFlg_abil() {
		return flg_abil;
	}

	/**
	 * indicatore di possibilita', per questa attivita', di rinnovare il
	 * servizio cioe' posticipare la data fine prevista del servizio. assume i
	 * valori s, n
	 */
	public void setFlg_rinn(char value) {
		this.flg_rinn = value;
	}

	/**
	 * indicatore di possibilita', per questa attivita', di rinnovare il
	 * servizio cioe' posticipare la data fine prevista del servizio. assume i
	 * valori s, n
	 */
	public char getFlg_rinn() {
		return flg_rinn;
	}

	/**
	 * codice identificativo dello stato dell'iter del servizio
	 */
	public void setStato_iter(char value) {
		this.stato_iter = value;
	}

	/**
	 * codice identificativo dello stato dell'iter del servizio
	 */
	public char getStato_iter() {
		return stato_iter;
	}

	/**
	 * indicatore dell'obbligatorieta' della attivita'
	 */
	public void setObbl(char value) {
		this.obbl = value;
	}

	/**
	 * indicatore dell'obbligatorieta' della attivita'
	 */
	public char getObbl() {
		return obbl;
	}

	/**
	 * codice dello stato della richiesta
	 */
	public void setCod_stato_rich(char value) {
		this.cod_stato_rich = value;
	}

	/**
	 * codice dello stato della richiesta
	 */
	public char getCod_stato_rich() {
		return cod_stato_rich;
	}

	/**
	 * codice dello stato del movimento
	 */
	public void setStato_mov(char value) {
		this.stato_mov = value;
	}

	/**
	 * codice dello stato del movimento
	 */
	public char getStato_mov() {
		return stato_mov;
	}

	/**
	 * codice dello stato di circolazione del materiale oggetto del servizio
	 * interbibliotecario
	 */
	public void setCod_stat_cir(char value) {
		this.cod_stat_cir = value;
	}

	/**
	 * codice dello stato di circolazione del materiale oggetto del servizio
	 * interbibliotecario
	 */
	public char getCod_stat_cir() {
		return cod_stat_cir;
	}

	/**
	 * codice dello stato della richiesta di servizio interbibliotecario
	 */
	public void setCod_stato_ric_ill(char value) {
		this.cod_stato_ric_ill = value;
	}

	/**
	 * codice dello stato della richiesta di servizio interbibliotecario
	 */
	public char getCod_stato_ric_ill() {
		return cod_stato_ric_ill;
	}

	public void setId_tipo_servizio(
			it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio value) {
		this.id_tipo_servizio = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio getId_tipo_servizio() {
		return id_tipo_servizio;
	}

	public java.util.Set<Tbl_controllo_iter> getTbl_controllo_iter() {
		return Tbl_controllo_iter;
	}

	public void setTbl_controllo_iter(
			java.util.Set<Tbl_controllo_iter> tbl_controllo_iter) {
		Tbl_controllo_iter = tbl_controllo_iter;
	}

	public java.util.Set<Trl_attivita_bibliotecario> getTrl_attivita_bibliotecario() {
		return Trl_attivita_bibliotecario;
	}

	public void setTrl_attivita_bibliotecario(
			java.util.Set<Trl_attivita_bibliotecario> trl_attivita_bibliotecario) {
		Trl_attivita_bibliotecario = trl_attivita_bibliotecario;
	}

	public java.util.Set<Tbl_richiesta_servizio> getTbl_richiesta_servizio() {
		return Tbl_richiesta_servizio;
	}

	public void setTbl_richiesta_servizio(
			java.util.Set<Tbl_richiesta_servizio> tbl_richiesta_servizio) {
		Tbl_richiesta_servizio = tbl_richiesta_servizio;
	}

	public String toString() {
		return String.valueOf(getId_iter_servizio());
	}

}
