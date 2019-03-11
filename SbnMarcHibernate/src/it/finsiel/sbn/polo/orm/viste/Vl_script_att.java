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
package it.finsiel.sbn.polo.orm.viste;

import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_script_att extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -4869923117160761316L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_script_att))
			return false;
		Vl_script_att vl_script_att = (Vl_script_att)aObj;
		if (getID_SCRIPT() != vl_script_att.getID_SCRIPT())
			return false;
		if (getID_TIPO_ATTIVITA() != vl_script_att.getID_TIPO_ATTIVITA())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (int) getID_SCRIPT();
		hashcode = hashcode + (int) getID_TIPO_ATTIVITA();
		return hashcode;
	}

	private long ID_SCRIPT;

	private long ID_TIPO_ATTIVITA;

	private String NOTA_TIPO_ATTIVITA;

	private String TP_ATTIVITA;

	private String DS_ATTIVITA;

	private String CD_UTENTE_AMM;

	private String CD_UTENTE;

	private long ID_SERVER;

	private String PARAMETRI_INPUT;

	private String FL_ESITO;

	private java.util.Date TS_START;

	private java.util.Date TS_END;

	public void setID_SCRIPT(long value) {
		this.ID_SCRIPT = value;
    this.settaParametro(KeyParameter.XXXid_script,value);
	}

	public long getID_SCRIPT() {
		return ID_SCRIPT;
	}

	public void setID_TIPO_ATTIVITA(long value) {
		this.ID_TIPO_ATTIVITA = value;
    this.settaParametro(KeyParameter.XXXid_tipo_attivita,value);
	}

	public long getID_TIPO_ATTIVITA() {
		return ID_TIPO_ATTIVITA;
	}

	public void setNOTA_TIPO_ATTIVITA(String value) {
		this.NOTA_TIPO_ATTIVITA = value;
    this.settaParametro(KeyParameter.XXXnota_tipo_attivita,value);
	}

	public String getNOTA_TIPO_ATTIVITA() {
		return NOTA_TIPO_ATTIVITA;
	}

	public void setTP_ATTIVITA(String value) {
		this.TP_ATTIVITA = value;
    this.settaParametro(KeyParameter.XXXtp_attivita,value);
	}

	public String getTP_ATTIVITA() {
		return TP_ATTIVITA;
	}

	public void setDS_ATTIVITA(String value) {
		this.DS_ATTIVITA = value;
    this.settaParametro(KeyParameter.XXXds_attivita,value);
	}

	public String getDS_ATTIVITA() {
		return DS_ATTIVITA;
	}

	public void setCD_UTENTE_AMM(String value) {
		this.CD_UTENTE_AMM = value;
    this.settaParametro(KeyParameter.XXXcd_utente_amm,value);
	}

	public String getCD_UTENTE_AMM() {
		return CD_UTENTE_AMM;
	}

	public void setCD_UTENTE(String value) {
		this.CD_UTENTE = value;
    this.settaParametro(KeyParameter.XXXcd_utente,value);
	}

	public String getCD_UTENTE() {
		return CD_UTENTE;
	}

	public void setID_SERVER(long value) {
		this.ID_SERVER = value;
    this.settaParametro(KeyParameter.XXXid_server,value);
	}

	public long getID_SERVER() {
		return ID_SERVER;
	}

	public void setPARAMETRI_INPUT(String value) {
		this.PARAMETRI_INPUT = value;
    this.settaParametro(KeyParameter.XXXparametri_input,value);
	}

	public String getPARAMETRI_INPUT() {
		return PARAMETRI_INPUT;
	}

	public void setFL_ESITO(String value) {
		this.FL_ESITO = value;
    this.settaParametro(KeyParameter.XXXfl_esito,value);
	}

	public String getFL_ESITO() {
		return FL_ESITO;
	}

	public void setTS_START(java.util.Date value) {
		this.TS_START = value;
    this.settaParametro(KeyParameter.XXXts_start,value);
	}

	public java.util.Date getTS_START() {
		return TS_START;
	}

	public void setTS_END(java.util.Date value) {
		this.TS_END = value;
    this.settaParametro(KeyParameter.XXXts_end,value);
	}

	public java.util.Date getTS_END() {
		return TS_END;
	}

	public String toString() {
		return String.valueOf(getID_SCRIPT() + " " + getID_TIPO_ATTIVITA());
	}
}
