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
public class Tr_tit_luo extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 4493964229635703731L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_luo))
			return false;
		Tr_tit_luo tr_tit_luo = (Tr_tit_luo)aObj;
		if (getBID() == null)
			return false;
		if (!getBID().equals(tr_tit_luo.getBID()))
			return false;
		if (getLID() == null)
			return false;
		if (!getLID().equals(tr_tit_luo.getLID()))
			return false;
		if (getTP_LUOGO() != tr_tit_luo.getTP_LUOGO())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getBID() != null) {
			hashcode = hashcode + getBID().hashCode();
		}
		if (getLID() != null) {
			hashcode = hashcode + getLID().hashCode();
		}
		hashcode = hashcode + getTP_LUOGO().hashCode();
		return hashcode;
	}

	private String BID;

	private String LID;

	private String TP_LUOGO;

	private String NOTA_TIT_LUO;

	public void setTP_LUOGO(String value) {
		this.TP_LUOGO = value;
    this.settaParametro(KeyParameter.XXXtp_luogo,value);
	}

	public String getTP_LUOGO() {
		return TP_LUOGO;
	}

	public void setNOTA_TIT_LUO(String value) {
		this.NOTA_TIT_LUO = value;
    this.settaParametro(KeyParameter.XXXnota_tit_luo,value);
	}

	public String getNOTA_TIT_LUO() {
		return NOTA_TIT_LUO;
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

	public String getBID() {
		return BID;
	}

	public void setLID(String value) {
		this.LID = value;
    this.settaParametro(KeyParameter.XXXlid,value);
	}

 public String getLID() {
		return LID;
 }



	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())) + " " + ((getLID() == null) ? "" : String.valueOf(getLID())) + " " + getTP_LUOGO());
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
