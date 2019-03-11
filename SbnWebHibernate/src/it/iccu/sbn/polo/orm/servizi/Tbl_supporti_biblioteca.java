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

import java.util.HashSet;
import java.util.Set;

/**
 * Supporti previsti in biblioteca
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_supporti_biblioteca extends Tb_base {

	private static final long serialVersionUID = -4294379403206425294L;

	public Tbl_supporti_biblioteca() {
	}

	private int id_supporti_biblioteca;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String cod_supporto;

	private java.math.BigDecimal imp_unita;

	private java.math.BigDecimal costo_fisso;

	private Character fl_svolg;

	private Set<Tbl_richiesta_servizio> Tbl_richiesta_servizio = new HashSet<Tbl_richiesta_servizio>();

	private Set<Trl_supporti_modalita_erogazione> Trl_supporti_modalita_erogazione = new HashSet<Trl_supporti_modalita_erogazione>();

	protected void setId_supporti_biblioteca(int value) {
		this.id_supporti_biblioteca = value;
	}

	public int getId_supporti_biblioteca() {
		return id_supporti_biblioteca;
	}

	public int getORMID() {
		return getId_supporti_biblioteca();
	}

	/**
	 * codice del tipo di supporto
	 */
	public void setCod_supporto(String value) {
		this.cod_supporto = value;
	}

	/**
	 * codice del tipo di supporto
	 */
	public String getCod_supporto() {
		return cod_supporto;
	}

	/**
	 * importo unitario
	 */
	public void setImp_unita(java.math.BigDecimal value) {
		this.imp_unita = value;
	}

	/**
	 * importo unitario
	 */
	public java.math.BigDecimal getImp_unita() {
		return imp_unita;
	}

	/**
	 * costo fisso
	 */
	public void setCosto_fisso(java.math.BigDecimal value) {
		this.costo_fisso = value;
	}

	/**
	 * costo fisso
	 */
	public java.math.BigDecimal getCosto_fisso() {
		return costo_fisso;
	}

	public Character getFl_svolg() {
		return fl_svolg;
	}

	public void setFl_svolg(Character fl_svolg) {
		this.fl_svolg = fl_svolg;
	}

	public void setCd_bib(
			it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setTbl_richiesta_servizio(Set<Tbl_richiesta_servizio> value) {
		this.Tbl_richiesta_servizio = value;
	}

	public Set<Tbl_richiesta_servizio> getTbl_richiesta_servizio() {
		return Tbl_richiesta_servizio;
	}

	public String toString() {
		return String.valueOf(getId_supporti_biblioteca());
	}

	public Set<Trl_supporti_modalita_erogazione> getTrl_supporti_modalita_erogazione() {
		return Trl_supporti_modalita_erogazione;
	}

	public void setTrl_supporti_modalita_erogazione(
			Set<Trl_supporti_modalita_erogazione> trl_supporti_modalita_erogazione) {
		Trl_supporti_modalita_erogazione = trl_supporti_modalita_erogazione;
	}

}
