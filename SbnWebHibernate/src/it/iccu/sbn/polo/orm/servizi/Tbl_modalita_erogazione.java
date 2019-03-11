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
 * Supporti previsti in biblioteca
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_modalita_erogazione extends Tb_base {

	private static final long serialVersionUID = -5937066454539180801L;

	public Tbl_modalita_erogazione() {
	}

	private int id_modalita_erogazione;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String cod_erog;

	private java.math.BigDecimal tar_base;

	private java.math.BigDecimal costo_unitario;

	private Character fl_svolg;

	protected void setId_modalita_erogazione(int value) {
		this.id_modalita_erogazione = value;
	}

	public int getId_modalita_erogazione() {
		return id_modalita_erogazione;
	}

	public int getORMID() {
		return getId_modalita_erogazione();
	}

	/**
	 * codice della modalità di erogazione
	 */
	public void setCod_erog(String value) {
		this.cod_erog = value;
	}

	public String getCod_erog() {
		return cod_erog;
	}

	/**
	 * costo fisso
	 */
	public java.math.BigDecimal getTar_base() {
		return tar_base;
	}

	/**
	 * costo fisso
	 */
	public void setTar_base(java.math.BigDecimal value) {
		this.tar_base = value;
	}

	/**
	 * costo unitario
	 */
	public java.math.BigDecimal getCosto_unitario() {
		return costo_unitario;
	}

	/**
	 * costo unitario
	 */
	public void setCosto_unitario(java.math.BigDecimal value) {
		this.costo_unitario = value;
	}

	public Character getFl_svolg() {
		return fl_svolg;
	}

	public void setFl_svolg(Character fl_svolg) {
		this.fl_svolg = fl_svolg;
	}

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public String toString() {
		return String.valueOf(getId_modalita_erogazione());
	}

}
