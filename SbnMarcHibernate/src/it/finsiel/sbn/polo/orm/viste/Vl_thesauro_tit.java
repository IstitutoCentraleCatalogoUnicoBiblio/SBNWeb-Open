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
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Vl_thesauro_tit extends Tb_termine_thesauro implements Serializable  {

	private static final long serialVersionUID = -5241438682870198623L;


	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_thesauro_tit))
			return false;
		Vl_thesauro_tit vl_thesauro_tit = (Vl_thesauro_tit)aObj;
		if (getBID() != null && !getBID().equals(vl_thesauro_tit.getBID()))
			return false;
		if (getDID() != null && !getDID().equals(vl_thesauro_tit.getDID()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getDID().hashCode();
		return hashcode;
	}

	private String BID;

	private String FL_CONDIVISO;


	private String NOTA_TERMINE_TITOLI_BIBLIOTECA;


	public String getFL_CONDIVISO() {
		return FL_CONDIVISO;
	}
	public void setFL_CONDIVISO(String value) {
		this.FL_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXfl_condiviso, value);
	}


	public String getNOTA_TERMINE_TITOLI_BIBLIOTECA() {
		return NOTA_TERMINE_TITOLI_BIBLIOTECA;
	}

	public void setNOTA_TERMINE_TITOLI_BIBLIOTECA(String value) {
		this.NOTA_TERMINE_TITOLI_BIBLIOTECA = value;
		this.settaParametro(KeyParameter.XXXnota_termine_titoli_biblioteca, value);
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}

 public String getBID() {
		return BID;
	}

	public String toString() {
		return String.valueOf(getBID() + " " + getDID());
	}
}
