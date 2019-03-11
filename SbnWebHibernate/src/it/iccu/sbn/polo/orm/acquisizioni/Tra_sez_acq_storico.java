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
package it.iccu.sbn.polo.orm.acquisizioni;

import java.io.Serializable;

import org.hibernate.proxy.HibernateProxyHelper;
/**
 * Sezioni d'acquisizione bibliografiche
 */
/**
 * ORM-Persistable Class
 */
public class Tra_sez_acq_storico implements Serializable {

	private static final long serialVersionUID = 279297603474549446L;

	public Tra_sez_acq_storico() {
	}
	private it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche id_sez_acquis_bibliografiche;

	private java.sql.Timestamp ts_ins;

	private java.util.Date data_var_bdg;

	private java.math.BigDecimal budget_old;

	private java.math.BigDecimal importo_diff;

	private String ute_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public java.math.BigDecimal getBudget_old() {
		return budget_old;
	}

	public void setBudget_old(java.math.BigDecimal budget_old) {
		this.budget_old = budget_old;
	}

	public java.util.Date getData_var_bdg() {
		return data_var_bdg;
	}

	public void setData_var_bdg(java.util.Date data_var_bdg) {
		this.data_var_bdg = data_var_bdg;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(char fl_canc) {
		this.fl_canc = fl_canc;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche getId_sez_acquis_bibliografiche() {
		return id_sez_acquis_bibliografiche;
	}

	public void setId_sez_acquis_bibliografiche(
			it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche id_sez_acquis_bibliografiche) {
		this.id_sez_acquis_bibliografiche = id_sez_acquis_bibliografiche;
	}

	public java.math.BigDecimal getImporto_diff() {
		return importo_diff;
	}

	public void setImporto_diff(java.math.BigDecimal importo_diff) {
		this.importo_diff = importo_diff;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setTs_ins(java.sql.Timestamp ts_ins) {
		this.ts_ins = ts_ins;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setTs_var(java.sql.Timestamp ts_var) {
		this.ts_var = ts_var;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setUte_ins(String ute_ins) {
		this.ute_ins = ute_ins;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setUte_var(String ute_var) {
		this.ute_var = ute_var;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((id_sez_acquis_bibliografiche == null) ? 0
						: id_sez_acquis_bibliografiche.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(HibernateProxyHelper.getClassWithoutInitializingProxy(obj) == Tra_sez_acq_storico.class))
			return false;
		Tra_sez_acq_storico other = (Tra_sez_acq_storico) obj;
		if (id_sez_acquis_bibliografiche == null) {
			if (other.id_sez_acquis_bibliografiche != null)
				return false;
		} else if (!id_sez_acquis_bibliografiche
				.equals(other.id_sez_acquis_bibliografiche))
			return false;
		return true;
	}




/*	public int getORMID() {
		return getId_sez_acquis_bibliografiche();
	}

	public String toString() {
		return String.valueOf(getId_sez_acquis_bibliografiche());
	}
*/


}
