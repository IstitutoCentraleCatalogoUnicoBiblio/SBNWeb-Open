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
public class Trs_termini_titoli_biblioteche extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 7971660873878645548L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Trs_termini_titoli_biblioteche))
			return false;
		Trs_termini_titoli_biblioteche trs_termini_titoli_biblioteche = (Trs_termini_titoli_biblioteche)aObj;
		if (getBID() == null)
			return false;
		if (!getBID().equals(trs_termini_titoli_biblioteche.getBID()))
			return false;
		if (getDID() == null)
			return false;
		if (!getDID().equals(trs_termini_titoli_biblioteche.getDID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getBID() != null) {
			hashcode = hashcode + getBID().hashCode();
		}
		if (getDID() != null) {
			hashcode = hashcode + getDID().hashCode();
		}
		return hashcode;
	}

	private String BID;

	private String DID;

	private String CD_THE;

	private String CD_POLO;

	private String CD_BIBLIOTECA;

	private String NOTA_TERMINE_TITOLI_BIBLIOTECA;

	public String getBID() {
		return BID;
	}

	public void setBID(String value) {
		this.BID = value;
		this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getCD_BIBLIOTECA() {
		return CD_BIBLIOTECA;
	}

	public void setCD_BIBLIOTECA(String value) {
		this.CD_BIBLIOTECA = value;
		this.settaParametro(KeyParameter.XXXcd_biblioteca,value);
	}

	public String getCD_POLO() {
		return CD_POLO;
	}

	public void setCD_POLO(String value) {
		this.CD_POLO = value;
		this.settaParametro(KeyParameter.XXXcd_polo,value);
	}

	public String getCD_THE() {
		return CD_THE;
	}

	public void setCD_THE(String value) {
		this.CD_THE = value;
		this.settaParametro(KeyParameter.XXXcd_the,value);
	}

	public String getDID() {
		return DID;
	}

	public void setDID(String value) {
		this.DID = value;
		this.settaParametro(KeyParameter.XXXdid,value);
	}

	public String getNOTA_TERMINE_TITOLI_BIBLIOTECA() {
		return NOTA_TERMINE_TITOLI_BIBLIOTECA;
	}

	public void setNOTA_TERMINE_TITOLI_BIBLIOTECA(String value) {
		this.NOTA_TERMINE_TITOLI_BIBLIOTECA = value;
		this.settaParametro(KeyParameter.XXXnota_termine_titoli_biblioteca,value);
	}


	public String toString() {
		return String.valueOf(((getBID() == null) ? "" :
				String.valueOf(getBID())) + " " + ((getDID() == null) ? "" :
					String.valueOf(getDID())));
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
