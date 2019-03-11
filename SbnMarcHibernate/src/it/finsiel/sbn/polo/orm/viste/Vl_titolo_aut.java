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
import it.finsiel.sbn.polo.orm.Tb_titolo;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_titolo_aut extends Tb_titolo implements Serializable  {

	private static final long serialVersionUID = 4869233402154562971L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_titolo_aut))
			return false;
		Vl_titolo_aut vl_titolo_aut = (Vl_titolo_aut)aObj;
		if (getBID() != null && !getBID().equals(vl_titolo_aut.getBID()))
			return false;
		if (getVID() != null && !getVID().equals(vl_titolo_aut.getVID()))
			return false;
		if (getTP_RESPONSABILITA() != vl_titolo_aut.getTP_RESPONSABILITA())
			return false;
		if (getCD_RELAZIONE() != null && !getCD_RELAZIONE().equals(vl_titolo_aut.getCD_RELAZIONE()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getVID().hashCode();
		hashcode = hashcode + getTP_RESPONSABILITA().hashCode();
		hashcode = hashcode + getCD_RELAZIONE().hashCode();
		return hashcode;
	}

	private String VID;

	private String TP_RESPONSABILITA;

	private String CD_RELAZIONE;

	private String NOTA_TIT_AUT;

	private String FL_INCERTO;

	private String FL_SUPERFLUO;

	private String CD_STRUMENTO_MUS;

	private String FL_CONDIVISO;

	private String UTE_CONDIVISO;

	private java.util.Date TS_CONDIVISO;

    private String FL_CONDIVISO_LEGAME;

	private String UTE_CONDIVISO_LEGAME;

	private java.util.Date TS_CONDIVISO_LEGAME;

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
		this.settaParametro(KeyParameter.XXXfl_condiviso, value);
	}

	public void setTS_CONDIVISO(java.util.Date value) {
		this.TS_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXts_condiviso, value);
	}

	public void setUTE_CONDIVISO(String value) {
		this.UTE_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXute_condiviso, value);
	}
    public String getFL_CONDIVISO_LEGAME() {
        return FL_CONDIVISO_LEGAME;
	}

	public java.util.Date getTS_CONDIVISO_LEGAME() {
		return TS_CONDIVISO_LEGAME;
	}

	public String getUTE_CONDIVISO_LEGAME() {
		return UTE_CONDIVISO_LEGAME;
	}

	public void setFL_CONDIVISO_LEGAME(String value) {
		this.FL_CONDIVISO_LEGAME = value;
		this.settaParametro(KeyParameter.XXXfl_condiviso_legame, value);
	}

	public void setTS_CONDIVISO_LEGAME(java.util.Date value) {
		this.TS_CONDIVISO_LEGAME = value;
		this.settaParametro(KeyParameter.XXXts_condiviso_legame, value);
	}

	public void setUTE_CONDIVISO_LEGAME(String value) {
		this.UTE_CONDIVISO_LEGAME = value;
		this.settaParametro(KeyParameter.XXXute_condiviso_legame, value);
	}

	public void setVID(String value) {
		this.VID = value;
    this.settaParametro(KeyParameter.XXXvid,value);
	}

	public String getVID() {
		return VID;
	}

	public void setTP_RESPONSABILITA(String value) {
		this.TP_RESPONSABILITA = value;
    this.settaParametro(KeyParameter.XXXtp_responsabilita,value);
	}

	public String getTP_RESPONSABILITA() {
		return TP_RESPONSABILITA;
	}

	public void setCD_RELAZIONE(String value) {
		this.CD_RELAZIONE = value;
    this.settaParametro(KeyParameter.XXXcd_relazione,value);
	}

	public String getCD_RELAZIONE() {
		return CD_RELAZIONE;
	}

	public void setNOTA_TIT_AUT(String value) {
		this.NOTA_TIT_AUT = value;
    this.settaParametro(KeyParameter.XXXnota_tit_aut,value);
	}

	public String getNOTA_TIT_AUT() {
		return NOTA_TIT_AUT;
	}

	public void setFL_INCERTO(String value) {
		this.FL_INCERTO = value;
    this.settaParametro(KeyParameter.XXXfl_incerto,value);
	}

	public String getFL_INCERTO() {
		return FL_INCERTO;
	}

	public void setFL_SUPERFLUO(String value) {
		this.FL_SUPERFLUO = value;
    this.settaParametro(KeyParameter.XXXfl_superfluo,value);
	}

	public String getFL_SUPERFLUO() {
		return FL_SUPERFLUO;
	}

	public void setCD_STRUMENTO_MUS(String value) {
		this.CD_STRUMENTO_MUS = value;
    this.settaParametro(KeyParameter.XXXcd_strumento_mus,value);
	}

 public String getCD_STRUMENTO_MUS() {
		return CD_STRUMENTO_MUS;
	}

	public String toString() {
		return String.valueOf(getBID() + " " + getVID() + " " + getTP_RESPONSABILITA() + " " + getCD_RELAZIONE());
	}

}
