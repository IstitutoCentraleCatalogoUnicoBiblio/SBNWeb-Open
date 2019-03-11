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
import it.finsiel.sbn.polo.orm.Tb_autore;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_autore_rep extends Tb_autore implements Serializable  {

	private static final long serialVersionUID = 6761478058642580211L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_autore_rep))
			return false;
		Vl_autore_rep vl_autore_rep = (Vl_autore_rep)aObj;
		if (getVID() != null && !getVID().equals(vl_autore_rep.getVID()))
			return false;
		if (getID_REPERTORIO() != vl_autore_rep.getID_REPERTORIO())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getVID().hashCode();
		hashcode = hashcode + (int) getID_REPERTORIO();
		return hashcode;
	}

	private long ID_REPERTORIO;

	private String NOTE_REP_AUT;

	private String FL_TROVATO;

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

	public void setID_REPERTORIO(long value) {
		this.ID_REPERTORIO = value;
    this.settaParametro(KeyParameter.XXXid_repertorio,value);
	}

	public long getID_REPERTORIO() {
		return ID_REPERTORIO;
	}

	public void setNOTE_REP_AUT(String value) {
		this.NOTE_REP_AUT = value;
    this.settaParametro(KeyParameter.XXXnote_rep_aut,value);
	}

	public String getNOTE_REP_AUT() {
		return NOTE_REP_AUT;
	}

	public void setFL_TROVATO(String value) {
		this.FL_TROVATO = value;
    this.settaParametro(KeyParameter.XXXfl_trovato,value);
	}

 public String getFL_TROVATO() {
		return FL_TROVATO;
	}

	public String toString() {
		return String.valueOf(getVID() + " " + getID_REPERTORIO());
	}

}
