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
public class Ts_servizio extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 4963660907501457155L;

	private String CD_RECORD;

	private String CAMPO1;

	private String CAMPO2;

	private String CAMPO3;

	public void setCD_RECORD(String value) {
		this.CD_RECORD = value;
    this.settaParametro(KeyParameter.XXXcd_record,value);
	}

	public String getCD_RECORD() {
		return CD_RECORD;
	}

	public String getORMID() {
		return getCD_RECORD();
	}

	public void setCAMPO1(String value) {
		this.CAMPO1 = value;
    this.settaParametro(KeyParameter.XXXcampo1,value);
	}

	public String getCAMPO1() {
		return CAMPO1;
	}

	public void setCAMPO2(String value) {
		this.CAMPO2 = value;
    this.settaParametro(KeyParameter.XXXcampo2,value);
	}

	public String getCAMPO2() {
		return CAMPO2;
	}

	public void setCAMPO3(String value) {
		this.CAMPO3 = value;
    this.settaParametro(KeyParameter.XXXcampo3,value);
	}

 public String getCAMPO3() {
		return CAMPO3;
 }

	public String toString() {
		return String.valueOf(getCD_RECORD());
	}

	private boolean _saved = false;

	public void onSave() {
		_saved=true;
	}


	public void onLoad() {
		_saved=true;
	}


	public boolean isSaved() {
		return _saved;
	}


}
