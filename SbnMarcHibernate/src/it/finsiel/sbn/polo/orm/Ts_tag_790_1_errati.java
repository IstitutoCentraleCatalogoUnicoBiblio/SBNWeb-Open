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
public class Ts_tag_790_1_errati extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -4954344283388874272L;

	private String BID_MARC;

	private String KCERCA;

	public void setBID_MARC(String value) {
		this.BID_MARC = value;
    this.settaParametro(KeyParameter.XXXbid_marc,value);
	}

	public String getBID_MARC() {
		return BID_MARC;
	}

	public String getORMID() {
		return getBID_MARC();
	}

	public void setKCERCA(String value) {
		this.KCERCA = value;
    this.settaParametro(KeyParameter.XXXkcerca,value);
	}


 public String getKCERCA() {
		return KCERCA;
	}

	public String toString() {
		return String.valueOf(getBID_MARC());
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
