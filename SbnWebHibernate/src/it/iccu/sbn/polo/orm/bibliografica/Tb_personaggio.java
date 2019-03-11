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
package it.iccu.sbn.polo.orm.bibliografica;

import java.io.Serializable;
/**
 * PERSONAGGIO
 */
/**
 * ORM-Persistable Class
 */
public class Tb_personaggio implements Serializable {

	private static final long serialVersionUID = 4364446183331056999L;

	private int id_personaggio;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String nome_personaggio;

	private String cd_timbro_vocale;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tr_per_int = new java.util.HashSet();

	private void setId_personaggio(int value) {
		this.id_personaggio = value;
	}

	public int getId_personaggio() {
		return id_personaggio;
	}

	public int getORMID() {
		return getId_personaggio();
	}

	public void setNome_personaggio(String value) {
		this.nome_personaggio = value;
	}

	public String getNome_personaggio() {
		return nome_personaggio;
	}

	public void setCd_timbro_vocale(String value) {
		this.cd_timbro_vocale = value;
	}

	public String getCd_timbro_vocale() {
		return cd_timbro_vocale;
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

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public void setTr_per_int(java.util.Set value) {
		this.Tr_per_int = value;
	}

	public java.util.Set getTr_per_int() {
		return Tr_per_int;
	}


	public String toString() {
		return String.valueOf(getId_personaggio());
	}

}
