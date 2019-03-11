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
package it.iccu.sbn.polo.orm.bibliografica;

import java.io.Serializable;
import java.math.BigInteger;
/**
 * NUMERI STANDARD
 */
/**
 * ORM-Persistable Class
 */
public class Tb_numero_std implements Serializable {

	private static final long serialVersionUID = -677125712259019073L;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String tp_numero_std;

	private String numero_std;

	private BigInteger numero_lastra;

	private String cd_paese;

	private String nota_numero_std;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setTp_numero_std(String value) {
		this.tp_numero_std = value;
	}

	public String getTp_numero_std() {
		return tp_numero_std;
	}

	public void setNumero_std(String value) {
		this.numero_std = value;
	}

	public String getNumero_std() {
		return numero_std;
	}

	public void setNumero_lastra(int value) {
		setNumero_lastra(new Integer(value));
	}

	public void setNumero_lastra(BigInteger value) {
		this.numero_lastra = value;
	}

	public BigInteger getNumero_lastra() {
		return numero_lastra;
	}

	public void setCd_paese(String value) {
		this.cd_paese = value;
	}

	public String getCd_paese() {
		return cd_paese;
	}

	public void setNota_numero_std(String value) {
		this.nota_numero_std = value;
	}

	public String getNota_numero_std() {
		return nota_numero_std;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * Timestamp di inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * Timestamp di inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public void setUte_var(String value) {
		this.ute_var = value;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public String getUte_var() {
		return ute_var;
	}

	/**
	 * Timestamp di variazione
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * Timestamp di variazione
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	/**
	 * Flag di cancellazione logica
	 */
	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	/**
	 * Flag di cancellazione logica
	 */
	public char getFl_canc() {
		return fl_canc;
	}

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getORMID() {
		return getB();
	}

	public String toString() {
		return String.valueOf(((getB() == null) ? "" : String.valueOf(getB().getORMID())));
	}

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_numero_std))
			return false;
		Tb_numero_std tb_numero_std = (Tb_numero_std)aObj;
		if (getB() == null)
			return false;
		if (!getB().equals(tb_numero_std.getB()))
			return false;
		if (getTp_numero_std() != null && !getTp_numero_std().equals(tb_numero_std.getTp_numero_std()))
			return false;
		if (getNumero_std() != null && !getNumero_std().equals(tb_numero_std.getNumero_std()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getB() != null) {
			hashcode = hashcode + getB().hashCode();
		}
		hashcode = hashcode + getTp_numero_std().hashCode();
		hashcode = hashcode + getNumero_std().hashCode();
		return hashcode;
	}

}
