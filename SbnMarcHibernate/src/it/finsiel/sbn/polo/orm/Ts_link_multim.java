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
package it.finsiel.sbn.polo.orm;

import java.io.Serializable;

/**
 * ORM-Persistable Class
 */
public class Ts_link_multim extends OggettoServerSbnMarc implements Serializable {

	//public ByteArrayOutputStream im;


	private static final long serialVersionUID = -7034319890185649433L;

	private long ID_LINK_MULTIM;

	private String KY_LINK_MULTIM;

	private String URI_MULTIM;

//	private java.sql.Blob IMMAGINE;
	private byte[] IMMAGINE;

	public void setID_LINK_MULTIM(long value) {
		this.ID_LINK_MULTIM = value;
    this.settaParametro(KeyParameter.XXXid_link_multim,value);
	}

	public long getID_LINK_MULTIM() {
		return ID_LINK_MULTIM;
	}

	public void setKY_LINK_MULTIM(String value) {
		this.KY_LINK_MULTIM = value;
    this.settaParametro(KeyParameter.XXXky_link_multim,value);
	}

	public String getKY_LINK_MULTIM() {
		return KY_LINK_MULTIM;
	}

	public void setURI_MULTIM(String value) {
		this.URI_MULTIM = value;
    this.settaParametro(KeyParameter.XXXuri_multim,value);
	}

	public String getURI_MULTIM() {
		return URI_MULTIM;
	}



//	public void setIMMAGINE(java.sql.Blob value) {
//		this.IMMAGINE = value;
//    this.settaParametro(KeyParameter.XXXimmagine,value);
//	}
//
//	public java.sql.Blob getIMMAGINE() {
//		return IMMAGINE;
//	}

	public void setIMMAGINE(byte[] value) {
		this.IMMAGINE = value;
    this.settaParametro(KeyParameter.XXXimmagine,value);
	}

	public byte[] getIMMAGINE() {
		return IMMAGINE;
	}



	public String toString() {
		return String.valueOf(getID_LINK_MULTIM());
	}

}
