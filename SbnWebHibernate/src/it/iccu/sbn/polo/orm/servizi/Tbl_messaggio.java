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

import java.sql.Timestamp;

/**
 * Messaggio tra biblioteche
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_messaggio extends Tb_base {

	private static final long serialVersionUID = 6142479784016025575L;

	private int id_messaggio;
	private Tbl_dati_richiesta_ill dati_richiesta;
	private String note;
	private char fl_tipo;
	private String fl_condizione;
	private String cd_stato;
	private Timestamp data_messaggio;

	private String responderId;
	private String requesterId;
	private Character fl_ruolo;

	public int getId_messaggio() {
		return id_messaggio;
	}

	public void setId_messaggio(int id_messaggio) {
		this.id_messaggio = id_messaggio;
	}

	public Tbl_dati_richiesta_ill getDati_richiesta() {
		return dati_richiesta;
	}

	public void setDati_richiesta(Tbl_dati_richiesta_ill dati_richiesta) {
		this.dati_richiesta = dati_richiesta;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public char getFl_tipo() {
		return fl_tipo;
	}

	public void setFl_tipo(char fl_tipo) {
		this.fl_tipo = fl_tipo;
	}

	public String getFl_condizione() {
		return fl_condizione;
	}

	public void setFl_condizione(String fl_condizione) {
		this.fl_condizione = fl_condizione;
	}

	public String getCd_stato() {
		return cd_stato;
	}

	public void setCd_stato(String cd_stato) {
		this.cd_stato = cd_stato;
	}

	public Timestamp getData_messaggio() {
		return data_messaggio;
	}

	public void setData_messaggio(Timestamp data_messaggio) {
		this.data_messaggio = data_messaggio;
	}

	public String getResponderId() {
		return responderId;
	}

	public void setResponderId(String responderId) {
		this.responderId = responderId;
	}

	public String getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}

	public Character getFl_ruolo() {
		return fl_ruolo;
	}

	public void setFl_ruolo(Character fl_ruolo) {
		this.fl_ruolo = fl_ruolo;
	}

}
