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
public class Ve_titolo_aut_nstd extends Tb_titolo implements Serializable {

	private static final long serialVersionUID = 1433116051195703831L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Ve_titolo_aut_nstd))
			return false;
		Ve_titolo_aut_nstd ve_titolo_aut_nstd = (Ve_titolo_aut_nstd)aObj;
		if (getBID() != null && !getBID().equals(ve_titolo_aut_nstd.getBID()))
			return false;
		if (getTP_NUMERO_STD() != null && !getTP_NUMERO_STD().equals(ve_titolo_aut_nstd.getTP_NUMERO_STD()))
			return false;
		if (getNUMERO_STD() != null && !getNUMERO_STD().equals(ve_titolo_aut_nstd.getNUMERO_STD()))
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

	private String BID;

	private String TP_NUMERO_STD;

	private String NUMERO_STD;

	private Integer NUMERO_LASTRA;

	private String CD_PAESE_STD;

	private String NOTA_NUMERO_STD;

	private String KY_CLES1_A;

	private String KY_CLES2_A;

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

	public void setKY_CLES1_A(String value) {
		this.KY_CLES1_A = value;
    this.settaParametro(KeyParameter.XXXky_cles1_a,value);
	}

	public String getKY_CLES1_A() {
		return KY_CLES1_A;
	}

	public void setKY_CLES2_A(String value) {
		this.KY_CLES2_A = value;
    this.settaParametro(KeyParameter.XXXky_cles2_a,value);
	}

 public String getKY_CLES2_A() {
		return KY_CLES2_A;
 }



	public String toString() {
		return String.valueOf(getBID() + " " + getTP_NUMERO_STD() + " " + getNUMERO_STD());
	}
}
