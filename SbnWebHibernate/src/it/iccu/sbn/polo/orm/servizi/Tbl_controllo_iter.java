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
 * Fasi di controllo
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_controllo_iter implements Serializable {

	private static final long serialVersionUID = 4874294017403488533L;

	public Tbl_controllo_iter() {
	}

	private int id_controllo_iter;

	private it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio id_iter_servizio;

	private short progr_fase;

	private char fl_bloccante;

	private String messaggio;

	private short cod_controllo;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private void setId_controllo_iter(int value) {
		this.id_controllo_iter = value;
	}

	public int getId_controllo_iter() {
		return id_controllo_iter;
	}

	public int getORMID() {
		return getId_controllo_iter();
	}

	/**
	 * progressivo che individua la sequenza di controllo
	 */
	public void setProgr_fase(short value) {
		this.progr_fase = value;
	}

	/**
	 * progressivo che individua la sequenza di controllo
	 */
	public short getProgr_fase() {
		return progr_fase;
	}

	/**
	 * indica se l'esito positivo del controllo, blocca la fase successiva dell'iter di controlli
	 */
	public void setFl_bloccante(char value) {
		this.fl_bloccante = value;
	}

	/**
	 * indica se l'esito positivo del controllo, blocca la fase successiva dell'iter di controlli
	 */
	public char getFl_bloccante() {
		return fl_bloccante;
	}

	/**
	 * messaggio presentato all'utente nel caso il controllo abbia dato positivo
	 */
	public void setMessaggio(String value) {
		this.messaggio = value;
	}

	/**
	 * messaggio presentato all'utente nel caso il controllo abbia dato positivo
	 */
	public String getMessaggio() {
		return messaggio;
	}

	/**
	 * codice che identifica il controllo
	 */
	public void setCod_controllo(short value) {
		this.cod_controllo = value;
	}

	/**
	 * codice che identifica il controllo
	 */
	public short getCod_controllo() {
		return cod_controllo;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * data e ora d'inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * data e ora d'inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setId_iter_servizio(it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio value) {
		this.id_iter_servizio = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio getId_iter_servizio() {
		return id_iter_servizio;
	}

	public String toString() {
		return String.valueOf(getId_controllo_iter());
	}

}
