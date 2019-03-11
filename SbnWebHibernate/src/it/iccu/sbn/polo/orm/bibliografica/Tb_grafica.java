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
 * MATERIALE GRAFICO
 */
/**
 * ORM-Persistable Class
 */
public class Tb_grafica implements Serializable {

	private static final long serialVersionUID = -3321412192456790703L;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private String cd_livello;

	private Character tp_materiale_gra;

	private Character cd_supporto;

	private Character cd_colore;

	private String cd_tecnica_dis_1;

	private String cd_tecnica_dis_2;

	private String cd_tecnica_dis_3;

	private String cd_tecnica_sta_1;

	private String cd_tecnica_sta_2;

	private String cd_tecnica_sta_3;

	private String cd_design_funz;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	public String getCd_livello() {
		return cd_livello;
	}

	public void setTp_materiale_gra(char value) {
		setTp_materiale_gra(new Character(value));
	}

	public void setTp_materiale_gra(Character value) {
		this.tp_materiale_gra = value;
	}

	public Character getTp_materiale_gra() {
		return tp_materiale_gra;
	}

	public void setCd_supporto(char value) {
		setCd_supporto(new Character(value));
	}

	public void setCd_supporto(Character value) {
		this.cd_supporto = value;
	}

	public Character getCd_supporto() {
		return cd_supporto;
	}

	public void setCd_colore(char value) {
		setCd_colore(new Character(value));
	}

	public void setCd_colore(Character value) {
		this.cd_colore = value;
	}

	public Character getCd_colore() {
		return cd_colore;
	}

	public void setCd_tecnica_dis_1(String value) {
		this.cd_tecnica_dis_1 = value;
	}

	public String getCd_tecnica_dis_1() {
		return cd_tecnica_dis_1;
	}

	public void setCd_tecnica_dis_2(String value) {
		this.cd_tecnica_dis_2 = value;
	}

	public String getCd_tecnica_dis_2() {
		return cd_tecnica_dis_2;
	}

	public void setCd_tecnica_dis_3(String value) {
		this.cd_tecnica_dis_3 = value;
	}

	public String getCd_tecnica_dis_3() {
		return cd_tecnica_dis_3;
	}

	public void setCd_tecnica_sta_1(String value) {
		this.cd_tecnica_sta_1 = value;
	}

	public String getCd_tecnica_sta_1() {
		return cd_tecnica_sta_1;
	}

	public void setCd_tecnica_sta_2(String value) {
		this.cd_tecnica_sta_2 = value;
	}

	public String getCd_tecnica_sta_2() {
		return cd_tecnica_sta_2;
	}

	public void setCd_tecnica_sta_3(String value) {
		this.cd_tecnica_sta_3 = value;
	}

	public String getCd_tecnica_sta_3() {
		return cd_tecnica_sta_3;
	}

	public void setCd_design_funz(String value) {
		this.cd_design_funz = value;
	}

	public String getCd_design_funz() {
		return cd_design_funz;
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
