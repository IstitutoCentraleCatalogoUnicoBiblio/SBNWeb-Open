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
 * LUOGHI DI PUBBLICAZIONE
 */
/**
 * ORM-Persistable Class
 */
public class Tb_luogo implements Serializable {

	private static final long serialVersionUID = 3269551404183304204L;

	private String lid;

	private char tp_forma;

	private String cd_livello;

	private String ds_luogo;

	private String ky_luogo;

	private String ky_norm_luogo;

	private String cd_paese;

	private String nota_luogo;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tr_luo_luo = new java.util.HashSet();

	private java.util.Set Tr_luo_luo1 = new java.util.HashSet();

	private java.util.Set Tr_tit_luo = new java.util.HashSet();

	public void setLid(String value) {
		this.lid = value;
	}

	public String getLid() {
		return lid;
	}

	public String getORMID() {
		return getLid();
	}

	public void setTp_forma(char value) {
		this.tp_forma = value;
	}

	public char getTp_forma() {
		return tp_forma;
	}

	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	public String getCd_livello() {
		return cd_livello;
	}

	public void setDs_luogo(String value) {
		this.ds_luogo = value;
	}

	public String getDs_luogo() {
		return ds_luogo;
	}

	public void setKy_luogo(String value) {
		this.ky_luogo = value;
	}

	public String getKy_luogo() {
		return ky_luogo;
	}

	public void setKy_norm_luogo(String value) {
		this.ky_norm_luogo = value;
	}

	public String getKy_norm_luogo() {
		return ky_norm_luogo;
	}

	public void setCd_paese(String value) {
		this.cd_paese = value;
	}

	public String getCd_paese() {
		return cd_paese;
	}

	public void setNota_luogo(String value) {
		this.nota_luogo = value;
	}

	public String getNota_luogo() {
		return nota_luogo;
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

	public void setTr_luo_luo(java.util.Set value) {
		this.Tr_luo_luo = value;
	}

	public java.util.Set getTr_luo_luo() {
		return Tr_luo_luo;
	}


	public void setTr_luo_luo1(java.util.Set value) {
		this.Tr_luo_luo1 = value;
	}

	public java.util.Set getTr_luo_luo1() {
		return Tr_luo_luo1;
	}


	public void setTr_tit_luo(java.util.Set value) {
		this.Tr_tit_luo = value;
	}

	public java.util.Set getTr_tit_luo() {
		return Tr_tit_luo;
	}


	public String toString() {
		return String.valueOf(getLid());
	}

	private boolean _saved = false;

	public void onSave() {
		_saved=true;
	}


	public void onLoad() {
		_saved=true;
	}


	public boolean isSaved() {
		return _saved;
	}


}
