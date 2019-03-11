/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 *
 * This is an automatic generated file. It will be regenerated every time
 * you generate persistence class.
 *
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: antoniospatera@libero.it
 * License Type: Evaluation
 */
package it.finsiel.sbn.polo.orm;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Tb_rappresent extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 653817141708473600L;

	private String BID;

	private String TP_GENERE;

	private String AA_RAPP;

	private String DS_PERIODO;

	private String DS_TEATRO;

	private String DS_LUOGO_RAPP;

	private String DS_OCCASIONE;

	private String NOTA_RAPP;

	public void setTP_GENERE(String value) {
		this.TP_GENERE = value;
    this.settaParametro(KeyParameter.XXXtp_genere,value);
	}

	public String getTP_GENERE() {
		return TP_GENERE;
	}

	public void setAA_RAPP(String value) {
		this.AA_RAPP = value;
    this.settaParametro(KeyParameter.XXXaa_rapp,value);
	}

	public String getAA_RAPP() {
		return AA_RAPP;
	}

	public void setDS_PERIODO(String value) {
		this.DS_PERIODO = value;
    this.settaParametro(KeyParameter.XXXds_periodo,value);
	}

	public String getDS_PERIODO() {
		return DS_PERIODO;
	}

	public void setDS_TEATRO(String value) {
		this.DS_TEATRO = value;
    this.settaParametro(KeyParameter.XXXds_teatro,value);
	}

	public String getDS_TEATRO() {
		return DS_TEATRO;
	}

	public void setDS_LUOGO_RAPP(String value) {
		this.DS_LUOGO_RAPP = value;
    this.settaParametro(KeyParameter.XXXds_luogo_rapp,value);
	}

	public String getDS_LUOGO_RAPP() {
		return DS_LUOGO_RAPP;
	}

	public void setDS_OCCASIONE(String value) {
		this.DS_OCCASIONE = value;
    this.settaParametro(KeyParameter.XXXds_occasione,value);
	}

	public String getDS_OCCASIONE() {
		return DS_OCCASIONE;
	}

	public void setNOTA_RAPP(String value) {
		this.NOTA_RAPP = value;
    this.settaParametro(KeyParameter.XXXnota_rapp,value);
	}

	public String getNOTA_RAPP() {
		return NOTA_RAPP;
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}


 public String getBID() {
		return BID;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())));
	}

}
