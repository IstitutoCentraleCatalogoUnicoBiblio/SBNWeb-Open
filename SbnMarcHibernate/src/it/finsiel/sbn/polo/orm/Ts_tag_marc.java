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
public class Ts_tag_marc extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = 1702371731433067996L;

	private long ID_TAG_MARC;

	private String NOME_TAG;

	private String INDICATORI;

	private String SOTTOCAMPO;

	private String VALORE_SOTTOCAMPO;

	private String BID_MARC;

	private void setID_TAG_MARC(long value) {
		this.ID_TAG_MARC = value;
    this.settaParametro(KeyParameter.XXXid_tag_marc,value);
	}

	public long getID_TAG_MARC() {
		return ID_TAG_MARC;
	}

	public long getORMID() {
		return getID_TAG_MARC();
	}

	public void setNOME_TAG(String value) {
		this.NOME_TAG = value;
    this.settaParametro(KeyParameter.XXXnome_tag,value);
	}

	public String getNOME_TAG() {
		return NOME_TAG;
	}

	public void setINDICATORI(String value) {
		this.INDICATORI = value;
    this.settaParametro(KeyParameter.XXXindicatori,value);
	}

	public String getINDICATORI() {
		return INDICATORI;
	}

	public void setSOTTOCAMPO(String value) {
		this.SOTTOCAMPO = value;
    this.settaParametro(KeyParameter.XXXsottocampo,value);
	}

	public String getSOTTOCAMPO() {
		return SOTTOCAMPO;
	}

	public void setVALORE_SOTTOCAMPO(String value) {
		this.VALORE_SOTTOCAMPO = value;
    this.settaParametro(KeyParameter.XXXvalore_sottocampo,value);
	}

	public String getVALORE_SOTTOCAMPO() {
		return VALORE_SOTTOCAMPO;
	}

	public void setBID_MARC(String value) {
		this.BID_MARC = value;
    this.settaParametro(KeyParameter.XXXbid_marc,value);
	}


 public String getBID_MARC() {
		return BID_MARC;
	}

	public String toString() {
		return String.valueOf(getID_TAG_MARC());
	}

}
