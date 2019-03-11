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
public class Vl_titolo_num_std extends Tb_titolo implements Serializable  {

	private static final long serialVersionUID = 4958915694021973908L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Vl_titolo_num_std))
			return false;
		Vl_titolo_num_std vl_titolo_num_std = (Vl_titolo_num_std)aObj;
		if (getBID() != null && !getBID().equals(vl_titolo_num_std.getBID()))
			return false;
		if (getTP_NUMERO_STD() != null && !getTP_NUMERO_STD().equals(vl_titolo_num_std.getTP_NUMERO_STD()))
			return false;
		if (getNUMERO_STD() != null && !getNUMERO_STD().equals(vl_titolo_num_std.getNUMERO_STD()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getBID().hashCode();
		hashcode = hashcode + getTP_NUMERO_STD().hashCode();
		hashcode = hashcode + getNUMERO_STD().hashCode();
		return hashcode;
	}

	private String TP_NUMERO_STD;

	private String NUMERO_STD;

	private Integer NUMERO_LASTRA;

	private String CD_PAESE_STD;

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

	public void setNUMERO_LASTRA(int value) {
		setNUMERO_LASTRA(new Integer(value));
    this.settaParametro(KeyParameter.XXXnumero_lastra,value);
	}

	public void setNUMERO_LASTRA(Integer value) {
		this.NUMERO_LASTRA = value;
    this.settaParametro(KeyParameter.XXXnumero_lastra,value);
	}

	public Integer getNUMERO_LASTRA() {
		return NUMERO_LASTRA;
	}

	public void setCD_PAESE_STD(String value) {
		this.CD_PAESE_STD = value;
    this.settaParametro(KeyParameter.XXXcd_paese_std,value);
	}

	public String getCD_PAESE_STD() {
		return CD_PAESE_STD;
	}

	public void setNOTA_NUMERO_STD(String value) {
		this.NOTA_NUMERO_STD = value;
    this.settaParametro(KeyParameter.XXXnota_numero_std,value);
	}

 public String getNOTA_NUMERO_STD() {
		return NOTA_NUMERO_STD;
	}

	public String toString() {
		return String.valueOf(getBID() + " " + getTP_NUMERO_STD() + " " + getNUMERO_STD());
	}

}
