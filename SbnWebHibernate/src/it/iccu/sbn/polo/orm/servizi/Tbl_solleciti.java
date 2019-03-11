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
 * Solleciti
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_solleciti implements Serializable {

	private static final long serialVersionUID = 3999690181131523953L;

	public Tbl_solleciti() {
	}

	private short progr_sollecito;

	private it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio cod_rich_serv;

	private java.util.Date data_invio;

	private String note;

	private char tipo_invio;

	private char esito;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setProgr_sollecito(short value) {
		this.progr_sollecito = value;
	}

	public short getProgr_sollecito() {
		return progr_sollecito;
	}

	/**
	 * data di invio di una lettera di sollecito
	 */
	public void setData_invio(java.util.Date value) {
		this.data_invio = value;
	}

	/**
	 * data di invio di una lettera di sollecito
	 */
	public java.util.Date getData_invio() {
		return data_invio;
	}

	/**
	 * note relative al sollecito
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * note relative al sollecito
	 */
	public String getNote() {
		return note;
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

	public it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio getCod_rich_serv() {
		return cod_rich_serv;
	}

	public String toString() {
		return String.valueOf(getProgr_sollecito() + " "
				+ getCod_rich_serv().getCod_rich_serv());
	}

	private boolean _saved = false;

	public void onSave() {
		_saved = true;
	}

	public void onLoad() {
		_saved = true;
	}

	public boolean isSaved() {
		return _saved;
	}

	public char getTipo_invio() {
		return tipo_invio;
	}

	public void setTipo_invio(char tipoInvio) {
		tipo_invio = tipoInvio;
	}

	public char getEsito() {
		return esito;
	}

	public void setEsito(char esito) {
		this.esito = esito;
	}

	public void setCod_rich_serv(
			it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio codRichServ) {
		cod_rich_serv = codRichServ;
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbl_solleciti))
			return false;
		Tbl_solleciti tbl_solleciti = (Tbl_solleciti) aObj;
		if (getProgr_sollecito() != tbl_solleciti.getProgr_sollecito())
			return false;
		if (getCod_rich_serv() == null
				&& tbl_solleciti.getCod_rich_serv() != null)
			return false;
		if (!getCod_rich_serv().equals(tbl_solleciti.getCod_rich_serv()))
			return false;

		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getProgr_sollecito();
		if (getCod_rich_serv() != null) {
			hashcode = hashcode + (int) getCod_rich_serv().getORMID();
		}
		return hashcode;
	}

}
