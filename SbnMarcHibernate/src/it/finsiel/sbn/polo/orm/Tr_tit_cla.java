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
public class Tr_tit_cla extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 6887017172431475850L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tr_tit_cla))
			return false;
		Tr_tit_cla tr_tit_cla = (Tr_tit_cla)aObj;

		if (getCD_SISTEMA() == null)
			return false;
		if (getCD_SISTEMA() != tr_tit_cla.getCD_SISTEMA())
			return false;

		if (getCD_EDIZIONE() == null)
			return false;
		if (getCD_EDIZIONE() != tr_tit_cla.getCD_EDIZIONE())
			return false;

		if (getCLASSE() == null)
			return false;
		if (getCLASSE().equals(tr_tit_cla.getCLASSE()))
			return false;

		if (getBID() == null)
			return false;
		if (!getBID().equals(tr_tit_cla.getBID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCD_SISTEMA().hashCode();
		hashcode = hashcode + getCD_EDIZIONE().hashCode();
    hashcode = hashcode + getCLASSE().hashCode();
		hashcode = hashcode + getBID().hashCode();
		return hashcode;
	}
    private String BID;

	private String CD_SISTEMA;

	private String CD_EDIZIONE;

	private String CLASSE;

	private String NOTA_TIT_CLA;

	public String toString() {
		return String.valueOf(((getCD_SISTEMA() == null) ? "" : String.valueOf(getCD_SISTEMA()) + " " +
               String.valueOf(getCD_EDIZIONE()) + " " + String.valueOf(getCD_EDIZIONE())) + " " +
               String.valueOf(getCLASSE()) + " " + String.valueOf(getCLASSE())) + " " +
               ((getBID() == null) ? "" : String.valueOf(getBID()));
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

    public String getBID() {
        return BID;
    }

    public void setBID(String bid) {
        BID = bid;
        this.settaParametro(KeyParameter.XXXbid,bid);
    }

	public String getNOTA_TIT_CLA() {
		return NOTA_TIT_CLA;
	}

	public void setNOTA_TIT_CLA(String value) {
		this.NOTA_TIT_CLA = value;
		this.settaParametro(KeyParameter.XXXnota_tit_cla,value);
	}


    public void setCD_SISTEMA(String value) {
        this.CD_SISTEMA = value;
        this.settaParametro(KeyParameter.XXXcd_sistema,value);
    }

    public String getCD_SISTEMA() {
        return CD_SISTEMA;
    }

    public void setCD_EDIZIONE(String value) {
        this.CD_EDIZIONE = value;
        this.settaParametro(KeyParameter.XXXcd_edizione,value);
    }

    public String getCD_EDIZIONE() {
        return CD_EDIZIONE;
    }

    public void setCLASSE(String value) {
        this.CLASSE = value;
        this.settaParametro(KeyParameter.XXXclasse,value);
    }

    public String getCLASSE() {
        return CLASSE;
    }


}
