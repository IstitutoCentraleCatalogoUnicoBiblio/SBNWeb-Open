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
package it.iccu.sbn.polo.orm.servizi;

import it.iccu.sbn.polo.orm.Tb_base;
/**
 * Penalità servizio
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_penalita_servizio extends Tb_base {

	private static final long serialVersionUID = -1653672296916568675L;

	public Tbl_penalita_servizio() {
	}

	private it.iccu.sbn.polo.orm.servizi.Tbl_servizio id_servizio;

	private short gg_sosp;

	private java.math.BigDecimal coeff_sosp;

	/**
	 * numero di giorni di sospensione, tale numero e' fisso ed e' un valore, puo' essere omesso nel caso in cui venga inserito il coefficente di sospensione
	 */
	public void setGg_sosp(short value) {
		this.gg_sosp = value;
	}

	/**
	 * numero di giorni di sospensione, tale numero e' fisso ed e' un valore, puo' essere omesso nel caso in cui venga inserito il coefficente di sospensione
	 */
	public short getGg_sosp() {
		return gg_sosp;
	}

	/**
	 * coefficiente per cui deve essere moltiplicato il numero dei giorni di ritardo. tale attributo non e' valorizzato nel caso in cui sia valorizzato l'attributo "gg sospensione"
	 */
	public void setCoeff_sosp(java.math.BigDecimal value) {
		this.coeff_sosp = value;
	}

	/**
	 * coefficiente per cui deve essere moltiplicato il numero dei giorni di ritardo. tale attributo non e' valorizzato nel caso in cui sia valorizzato l'attributo "gg sospensione"
	 */
	public java.math.BigDecimal getCoeff_sosp() {
		return coeff_sosp;
	}

	public void setId_servizio(it.iccu.sbn.polo.orm.servizi.Tbl_servizio value) {
		this.id_servizio = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_servizio getId_servizio() {
		return id_servizio;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_servizio getORMID() {
		return getId_servizio();
	}

	public String toString() {
		return String.valueOf(((getId_servizio() == null) ? "" : String.valueOf(getId_servizio().getORMID())));
	}

}
