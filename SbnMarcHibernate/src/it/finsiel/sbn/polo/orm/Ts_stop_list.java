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
public class Ts_stop_list extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 8310823412993816429L;

	private long ID_STOP_LIST;

	private String TP_STOP_LIST;

	private String CD_LINGUA;

	private String PAROLA;

	private String NOTA_STOP_LIST;

	private void setID_STOP_LIST(long value) {
		this.ID_STOP_LIST = value;
    this.settaParametro(KeyParameter.XXXid_stop_list,value);
	}

	public long getID_STOP_LIST() {
		return ID_STOP_LIST;
	}

	public long getORMID() {
		return getID_STOP_LIST();
	}

	public void setTP_STOP_LIST(String value) {
		this.TP_STOP_LIST = value;
    this.settaParametro(KeyParameter.XXXtp_stop_list,value);
	}

	public String getTP_STOP_LIST() {
		return TP_STOP_LIST;
	}

	public void setCD_LINGUA(String value) {
		this.CD_LINGUA = value;
    this.settaParametro(KeyParameter.XXXcd_lingua,value);
	}

	public String getCD_LINGUA() {
		return CD_LINGUA;
	}

	public void setPAROLA(String value) {
		this.PAROLA = value;
    this.settaParametro(KeyParameter.XXXparola,value);
	}

	public String getPAROLA() {
		return PAROLA;
	}

	public void setNOTA_STOP_LIST(String value) {
		this.NOTA_STOP_LIST = value;
    this.settaParametro(KeyParameter.XXXnota_stop_list,value);
	}

	public String getNOTA_STOP_LIST() {
		return NOTA_STOP_LIST;
	}

	public String toString() {
		return String.valueOf(getID_STOP_LIST());
	}

}
