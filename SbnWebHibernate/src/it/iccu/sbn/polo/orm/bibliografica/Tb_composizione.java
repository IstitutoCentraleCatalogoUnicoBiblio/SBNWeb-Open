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
/**
 * COMPOSIZIONI MUSICALI
 */
/**
 * ORM-Persistable Class
 */
public class Tb_composizione implements Serializable {

	private static final long serialVersionUID = -5671877384174636105L;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String cd_forma_1;

	private String cd_forma_2;

	private String cd_forma_3;

	private String numero_ordine;

	private String numero_opera;

	private String numero_cat_tem;

	private String cd_tonalita;

	private String datazione;

	private String aa_comp_1;

	private String aa_comp_2;

	private String ds_sezioni;

	private String ky_ord_ric;

	private String ky_est_ric;

	private String ky_app_ric;

	private String ky_ord_clet;

	private String ky_est_clet;

	private String ky_app_clet;

	private String ky_ord_pre;

	private String ky_est_pre;

	private String ky_app_pre;

	private String ky_ord_den;

	private String ky_est_den;

	private String ky_app_den;

	private String ky_ord_nor_pre;

	private String ky_est_nor_pre;

	private String ky_app_nor_pre;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setCd_forma_1(String value) {
		this.cd_forma_1 = value;
	}

	public String getCd_forma_1() {
		return cd_forma_1;
	}

	public void setCd_forma_2(String value) {
		this.cd_forma_2 = value;
	}

	public String getCd_forma_2() {
		return cd_forma_2;
	}

	public void setCd_forma_3(String value) {
		this.cd_forma_3 = value;
	}

	public String getCd_forma_3() {
		return cd_forma_3;
	}

	public void setNumero_ordine(String value) {
		this.numero_ordine = value;
	}

	public String getNumero_ordine() {
		return numero_ordine;
	}

	public void setNumero_opera(String value) {
		this.numero_opera = value;
	}

	public String getNumero_opera() {
		return numero_opera;
	}

	public void setNumero_cat_tem(String value) {
		this.numero_cat_tem = value;
	}

	public String getNumero_cat_tem() {
		return numero_cat_tem;
	}

	public void setCd_tonalita(String value) {
		this.cd_tonalita = value;
	}

	public String getCd_tonalita() {
		return cd_tonalita;
	}

	public void setDatazione(String value) {
		this.datazione = value;
	}

	public String getDatazione() {
		return datazione;
	}

	public void setAa_comp_1(String value) {
		this.aa_comp_1 = value;
	}

	public String getAa_comp_1() {
		return aa_comp_1;
	}

	public void setAa_comp_2(String value) {
		this.aa_comp_2 = value;
	}

	public String getAa_comp_2() {
		return aa_comp_2;
	}

	public void setDs_sezioni(String value) {
		this.ds_sezioni = value;
	}

	public String getDs_sezioni() {
		return ds_sezioni;
	}

	public void setKy_ord_ric(String value) {
		this.ky_ord_ric = value;
	}

	public String getKy_ord_ric() {
		return ky_ord_ric;
	}

	public void setKy_est_ric(String value) {
		this.ky_est_ric = value;
	}

	public String getKy_est_ric() {
		return ky_est_ric;
	}

	public void setKy_app_ric(String value) {
		this.ky_app_ric = value;
	}

	public String getKy_app_ric() {
		return ky_app_ric;
	}

	public void setKy_ord_clet(String value) {
		this.ky_ord_clet = value;
	}

	public String getKy_ord_clet() {
		return ky_ord_clet;
	}

	public void setKy_est_clet(String value) {
		this.ky_est_clet = value;
	}

	public String getKy_est_clet() {
		return ky_est_clet;
	}

	public void setKy_app_clet(String value) {
		this.ky_app_clet = value;
	}

	public String getKy_app_clet() {
		return ky_app_clet;
	}

	public void setKy_ord_pre(String value) {
		this.ky_ord_pre = value;
	}

	public String getKy_ord_pre() {
		return ky_ord_pre;
	}

	public void setKy_est_pre(String value) {
		this.ky_est_pre = value;
	}

	public String getKy_est_pre() {
		return ky_est_pre;
	}

	public void setKy_app_pre(String value) {
		this.ky_app_pre = value;
	}

	public String getKy_app_pre() {
		return ky_app_pre;
	}

	public void setKy_ord_den(String value) {
		this.ky_ord_den = value;
	}

	public String getKy_ord_den() {
		return ky_ord_den;
	}

	public void setKy_est_den(String value) {
		this.ky_est_den = value;
	}

	public String getKy_est_den() {
		return ky_est_den;
	}

	public void setKy_app_den(String value) {
		this.ky_app_den = value;
	}

	public String getKy_app_den() {
		return ky_app_den;
	}

	public void setKy_ord_nor_pre(String value) {
		this.ky_ord_nor_pre = value;
	}

	public String getKy_ord_nor_pre() {
		return ky_ord_nor_pre;
	}

	public void setKy_est_nor_pre(String value) {
		this.ky_est_nor_pre = value;
	}

	public String getKy_est_nor_pre() {
		return ky_est_nor_pre;
	}

	public void setKy_app_nor_pre(String value) {
		this.ky_app_nor_pre = value;
	}

	public String getKy_app_nor_pre() {
		return ky_app_nor_pre;
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

}
