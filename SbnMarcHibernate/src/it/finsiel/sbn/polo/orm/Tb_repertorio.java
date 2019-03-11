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
public class Tb_repertorio extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -9037092627345675779L;

	private long ID_REPERTORIO;

	private String CD_SIG_REPERTORIO;

	private String DS_REPERTORIO;

	private String TP_REPERTORIO;

	public void setID_REPERTORIO(long value) {
		this.ID_REPERTORIO = value;
    this.settaParametro(KeyParameter.XXXid_repertorio,value);
	}

	public long getID_REPERTORIO() {
		return ID_REPERTORIO;
	}

	public void setCD_SIG_REPERTORIO(String value) {
		this.CD_SIG_REPERTORIO = value;
    this.settaParametro(KeyParameter.XXXcd_sig_repertorio,value);
	}

	public String getCD_SIG_REPERTORIO() {
		return CD_SIG_REPERTORIO;
	}

	public void setDS_REPERTORIO(String value) {
		this.DS_REPERTORIO = value;
    this.settaParametro(KeyParameter.XXXds_repertorio,value);
	}

	public String getDS_REPERTORIO() {
		return DS_REPERTORIO;
	}

	public void setTP_REPERTORIO(String value) {
		this.TP_REPERTORIO = value;
    this.settaParametro(KeyParameter.XXXtp_repertorio,value);
	}

	public String getTP_REPERTORIO() {
		return TP_REPERTORIO;
	}

	public String toString() {
		return String.valueOf(getID_REPERTORIO());
	}

}
