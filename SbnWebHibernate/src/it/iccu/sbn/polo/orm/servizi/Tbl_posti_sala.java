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
 * Posti in sala
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_posti_sala extends Tb_base {

	private static final long serialVersionUID = -1313099715059012104L;

	private int id_posti_sala;

	private it.iccu.sbn.polo.orm.servizi.Tbl_sale id_sale;

	private short num_posto;

	private short gruppo;

	private char occupato;

	private Set<Trl_posti_sala_utenti_biblioteca> Trl_posti_sala_utenti_biblioteca = new HashSet<Trl_posti_sala_utenti_biblioteca>();

	private Set<Trl_posto_sala_categoria_mediazione> trl_posto_sala_categoria_mediazione = new HashSet<Trl_posto_sala_categoria_mediazione>();

	public void setId_posti_sala(int value) {
		this.id_posti_sala = value;
	}

	public int getId_posti_sala() {
		return id_posti_sala;
	}

	public int getORMID() {
		return getId_posti_sala();
	}

	/**
	 * numero del posto della sala
	 */
	public void setNum_posto(short value) {
		this.num_posto = value;
	}

	/**
	 * numero del posto della sala
	 */
	public short getNum_posto() {
		return num_posto;
	}

	public short getGruppo() {
		return gruppo;
	}

	public void setGruppo(short gruppo) {
		this.gruppo = gruppo;
	}

	/**
	 * indica se il posto e' occupato o libero
	 */
	public void setOccupato(char value) {
		this.occupato = value;
	}

	/**
	 * indica se il posto e' occupato o libero
	 */
	public char getOccupato() {
		return occupato;
	}

	public void setId_sale(it.iccu.sbn.polo.orm.servizi.Tbl_sale value) {
		this.id_sale = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_sale getId_sale() {
		return id_sale;
	}

	public String toString() {
		return String.valueOf(getId_posti_sala());
	}

	public Set<Trl_posti_sala_utenti_biblioteca> getTrl_posti_sala_utenti_biblioteca() {
		return Trl_posti_sala_utenti_biblioteca;
	}

	public void setTrl_posti_sala_utenti_biblioteca(Set<Trl_posti_sala_utenti_biblioteca> trl_posti_sala_utenti_biblioteca) {
		Trl_posti_sala_utenti_biblioteca = trl_posti_sala_utenti_biblioteca;
	}

	public Set<Trl_posto_sala_categoria_mediazione> getTrl_posto_sala_categoria_mediazione() {
		return trl_posto_sala_categoria_mediazione;
	}

	public void setTrl_posto_sala_categoria_mediazione(Set<Trl_posto_sala_categoria_mediazione> trl_posto_sala_categoria_mediazione) {
		this.trl_posto_sala_categoria_mediazione = trl_posto_sala_categoria_mediazione;
	}

}
