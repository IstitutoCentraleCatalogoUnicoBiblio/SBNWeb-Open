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
import java.math.BigInteger;
/**
 * ORM-Persistable Class
 */
public class Tb_numero_std extends OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -5413579274902465365L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_numero_std))
			return false;
		Tb_numero_std tb_numero_std = (Tb_numero_std)aObj;
		if (getBID() == null)
			return false;
		if (!getBID().equals(tb_numero_std.getBID()))
			return false;
		if (getTP_NUMERO_STD() != null && !getTP_NUMERO_STD().equals(tb_numero_std.getTP_NUMERO_STD()))
			return false;
		if (getNUMERO_STD() != null && !getNUMERO_STD().equals(tb_numero_std.getNUMERO_STD()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getBID() != null) {
			hashcode = hashcode + getBID().hashCode();
		}
		hashcode = hashcode + getTP_NUMERO_STD().hashCode();
		hashcode = hashcode + getNUMERO_STD().hashCode();
		return hashcode;
	}

	private String BID;

	private String TP_NUMERO_STD;

	private String NUMERO_STD;

	private BigInteger NUMERO_LASTRA;

	private String CD_PAESE;

	private String NOTA_NUMERO_STD;

	public void setTP_NUMERO_STD(String value) {
		this.TP_NUMERO_STD = value;
    this.settaParametro(KeyParameter.XXXtp_numero_std,value);
	}

	public String getTP_NUMERO_STD() {
		return TP_NUMERO_STD;
	}

	public void setNUMERO_STD(String value) {
		this.NUMERO_STD = value;
    this.settaParametro(KeyParameter.XXXnumero_std,value);
	}

	public String getNUMERO_STD() {
		return NUMERO_STD;
	}

	public void setNUMERO_LASTRA(BigInteger value) {
		this.NUMERO_LASTRA = value;
    this.settaParametro(KeyParameter.XXXnumero_lastra,value);
	}

	public BigInteger getNUMERO_LASTRA() {
		return NUMERO_LASTRA;
	}

	public void setCD_PAESE(String value) {
		this.CD_PAESE = value;
    this.settaParametro(KeyParameter.XXXcd_paese,value);
	}

	public String getCD_PAESE() {
		return CD_PAESE;
	}

	public void setNOTA_NUMERO_STD(String value) {
		this.NOTA_NUMERO_STD = value;
    this.settaParametro(KeyParameter.XXXnota_numero_std,value);
	}

	public String getNOTA_NUMERO_STD() {
		return NOTA_NUMERO_STD;
	}

	public void setBID(String value) {
		this.BID = value;
    this.settaParametro(KeyParameter.XXXbid,value);
	}


 public String getBID() {
		return BID;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())) + " " + getTP_NUMERO_STD() + " " + getNUMERO_STD());
	}
}
