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
public class Tr_tit_tit extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 6442058520100422230L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_tit))
			return false;
		Tr_tit_tit tr_tit_tit = (Tr_tit_tit)aObj;
		if (getBID_BASE() == null)
			return false;
		if (!getBID_BASE().equals(tr_tit_tit.getBID_BASE()))
			return false;
		if (getBID_COLL() == null)
			return false;
		if (!getBID_COLL().equals(tr_tit_tit.getBID_COLL()))
			return false;
		if (getTP_LEGAME() != null && !getTP_LEGAME().equals(tr_tit_tit.getTP_LEGAME()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getBID_BASE() != null) {
			hashcode = hashcode + getBID_BASE().hashCode();
		}
		if (getBID_COLL() != null) {
			hashcode = hashcode + getBID_COLL().hashCode();
		}
		hashcode = hashcode + getTP_LEGAME().hashCode();
		return hashcode;
	}

	private String BID_BASE;

	private String BID_COLL;

	private String TP_LEGAME;

	private String TP_LEGAME_MUSICA;

	private String CD_NATURA_BASE;

	private String CD_NATURA_COLL;

	private String SEQUENZA;

	private String NOTA_TIT_TIT;

	private String SEQUENZA_MUSICA;

	private String SICI;

	//	 CAMPI NUOVA ISTANZA
	private String FL_CONDIVISO;
	private String UTE_CONDIVISO;
	private java.util.Date TS_CONDIVISO;

	public String getFL_CONDIVISO() {
		return FL_CONDIVISO;
	}
	public java.util.Date getTS_CONDIVISO() {
		return TS_CONDIVISO;
	}
	public String getUTE_CONDIVISO() {
		return UTE_CONDIVISO;
	}

	public void setFL_CONDIVISO(String value) {
		this.FL_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXfl_condiviso,value);
	}

	public void setTS_CONDIVISO(java.util.Date value) {
		this.TS_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXts_condiviso,value);
	}

	public void setUTE_CONDIVISO(String value) {
		this.UTE_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXute_condiviso,value);
	}



	public void setTP_LEGAME(String value) {
		this.TP_LEGAME = value;
    this.settaParametro(KeyParameter.XXXtp_legame,value);
	}

	public String getTP_LEGAME() {
		return TP_LEGAME;
	}

	public void setTP_LEGAME_MUSICA(String value) {
		this.TP_LEGAME_MUSICA = value;
    this.settaParametro(KeyParameter.XXXtp_legame_musica,value);
	}

	public String getTP_LEGAME_MUSICA() {
		return TP_LEGAME_MUSICA;
	}

	public void setCD_NATURA_BASE(String value) {
		this.CD_NATURA_BASE = value;
    this.settaParametro(KeyParameter.XXXcd_natura_base,value);
	}

	public String getCD_NATURA_BASE() {
		return CD_NATURA_BASE;
	}

	public void setCD_NATURA_COLL(String value) {
		this.CD_NATURA_COLL = value;
    this.settaParametro(KeyParameter.XXXcd_natura_coll,value);
	}

	public String getCD_NATURA_COLL() {
		return CD_NATURA_COLL;
	}

	public void setSEQUENZA(String value) {
		this.SEQUENZA = value;
    this.settaParametro(KeyParameter.XXXsequenza,value);
	}

	public String getSEQUENZA() {
		return SEQUENZA;
	}

	public void setNOTA_TIT_TIT(String value) {
		this.NOTA_TIT_TIT = value;
    this.settaParametro(KeyParameter.XXXnota_tit_tit,value);
	}

	public String getNOTA_TIT_TIT() {
		return NOTA_TIT_TIT;
	}

	public void setSEQUENZA_MUSICA(String value) {
		this.SEQUENZA_MUSICA = value;
    this.settaParametro(KeyParameter.XXXsequenza_musica,value);
	}

	public String getSEQUENZA_MUSICA() {
		return SEQUENZA_MUSICA;
	}

	public void setSICI(String value) {
		this.SICI = value;
    this.settaParametro(KeyParameter.XXXsici,value);
	}

	public String getSICI() {
		return SICI;
	}



//    public void setTS_INS(Object value) {
//        this.TS_INS = (Time) value;
//    this.settaParametro(KeyParameter.XXXts_ins,value);
//    }

	public void setBID_BASE(String value) {
		this.BID_BASE = value;
    this.settaParametro(KeyParameter.XXXbid_base,value);
	}

	public String getBID_BASE() {
		return BID_BASE;
	}

	public void setBID_COLL(String value) {
		this.BID_COLL = value;
    this.settaParametro(KeyParameter.XXXbid_coll,value);
	}


 public String getBID_COLL() {
		return BID_COLL;
	}

	public String toString() {
		return String.valueOf(((getBID_BASE() == null) ? "" : String.valueOf(getBID_BASE())) + " " + ((getBID_COLL() == null) ? "" : String.valueOf(getBID_COLL())) + " " + getTP_LEGAME());
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
