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
 * AUTORI
 */
/**
 * ORM-Persistable Class
 */
public class Tb_autore implements Serializable {

	private static final long serialVersionUID = 1539587122504506728L;

	private String vid;

	private String isadn;

	private char tp_forma_aut;

	private String ky_cautun;

	private String ky_auteur;

	private String ky_el1_a;

	private String ky_el1_b;

	private String ky_el2_a;

	private String ky_el2_b;

	private char tp_nome_aut;

	private String ky_el3;

	private String ky_el4;

	private String ky_el5;

	private String ky_cles1_a;

	private String ky_cles2_a;

	private String cd_paese;

	private String cd_lingua;

	private String aa_nascita;

	private String aa_morte;

	private String cd_livello;

	private char fl_speciale;

	private String ds_nome_aut;

	private String cd_agenzia;

	private String cd_norme_cat;

	private String nota_aut;

	private String nota_cat_aut;

	private String vid_link;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private String ute_forza_ins;

	private String ute_forza_var;

	private char fl_canc;

	private char fl_condiviso;

	private String ute_condiviso;

	private java.sql.Timestamp ts_condiviso;

	private java.util.Set Tr_aut_aut = new java.util.HashSet();

	private java.util.Set Tr_aut_aut1 = new java.util.HashSet();

	private java.util.Set Tr_aut_bib = new java.util.HashSet();

	private java.util.Set Tr_aut_mar = new java.util.HashSet();

	private java.util.Set Tr_per_int = new java.util.HashSet();

	private java.util.Set Tr_rep_aut = new java.util.HashSet();

	private java.util.Set Tr_tit_aut = new java.util.HashSet();

	public void setVid(String value) {
		this.vid = value;
	}

	public String getVid() {
		return vid;
	}

	public String getORMID() {
		return getVid();
	}

	public void setIsadn(String value) {
		this.isadn = value;
	}

	public String getIsadn() {
		return isadn;
	}

	public void setTp_forma_aut(char value) {
		this.tp_forma_aut = value;
	}

	public char getTp_forma_aut() {
		return tp_forma_aut;
	}

	public void setKy_cautun(String value) {
		this.ky_cautun = value;
	}

	public String getKy_cautun() {
		return ky_cautun;
	}

	public void setKy_auteur(String value) {
		this.ky_auteur = value;
	}

	public String getKy_auteur() {
		return ky_auteur;
	}

	public void setKy_el1_a(String value) {
		this.ky_el1_a = value;
	}

	public String getKy_el1_a() {
		return ky_el1_a;
	}

	public void setKy_el1_b(String value) {
		this.ky_el1_b = value;
	}

	public String getKy_el1_b() {
		return ky_el1_b;
	}

	public void setKy_el2_a(String value) {
		this.ky_el2_a = value;
	}

	public String getKy_el2_a() {
		return ky_el2_a;
	}

	public void setKy_el2_b(String value) {
		this.ky_el2_b = value;
	}

	public String getKy_el2_b() {
		return ky_el2_b;
	}

	public void setTp_nome_aut(char value) {
		this.tp_nome_aut = value;
	}

	public char getTp_nome_aut() {
		return tp_nome_aut;
	}

	public void setKy_el3(String value) {
		this.ky_el3 = value;
	}

	public String getKy_el3() {
		return ky_el3;
	}

	public void setKy_el4(String value) {
		this.ky_el4 = value;
	}

	public String getKy_el4() {
		return ky_el4;
	}

	public void setKy_el5(String value) {
		this.ky_el5 = value;
	}

	public String getKy_el5() {
		return ky_el5;
	}

	public void setKy_cles1_a(String value) {
		this.ky_cles1_a = value;
	}

	public String getKy_cles1_a() {
		return ky_cles1_a;
	}

	public void setKy_cles2_a(String value) {
		this.ky_cles2_a = value;
	}

	public String getKy_cles2_a() {
		return ky_cles2_a;
	}

	public void setCd_paese(String value) {
		this.cd_paese = value;
	}

	public String getCd_paese() {
		return cd_paese;
	}

	public void setCd_lingua(String value) {
		this.cd_lingua = value;
	}

	public String getCd_lingua() {
		return cd_lingua;
	}

	public void setAa_nascita(String value) {
		this.aa_nascita = value;
	}

	public String getAa_nascita() {
		return aa_nascita;
	}

	public void setAa_morte(String value) {
		this.aa_morte = value;
	}

	public String getAa_morte() {
		return aa_morte;
	}

	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	public String getCd_livello() {
		return cd_livello;
	}

	public void setFl_speciale(char value) {
		this.fl_speciale = value;
	}

	public char getFl_speciale() {
		return fl_speciale;
	}

	public void setDs_nome_aut(String value) {
		this.ds_nome_aut = value;
	}

	public String getDs_nome_aut() {
		return ds_nome_aut;
	}

	public void setCd_agenzia(String value) {
		this.cd_agenzia = value;
	}

	public String getCd_agenzia() {
		return cd_agenzia;
	}

	public void setCd_norme_cat(String value) {
		this.cd_norme_cat = value;
	}

	public String getCd_norme_cat() {
		return cd_norme_cat;
	}

	public void setNota_aut(String value) {
		this.nota_aut = value;
	}

	public String getNota_aut() {
		return nota_aut;
	}

	public void setNota_cat_aut(String value) {
		this.nota_cat_aut = value;
	}

	public String getNota_cat_aut() {
		return nota_cat_aut;
	}

	public void setVid_link(String value) {
		this.vid_link = value;
	}

	public String getVid_link() {
		return vid_link;
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

	public void setUte_forza_ins(String value) {
		this.ute_forza_ins = value;
	}

	public String getUte_forza_ins() {
		return ute_forza_ins;
	}

	public void setUte_forza_var(String value) {
		this.ute_forza_var = value;
	}

	public String getUte_forza_var() {
		return ute_forza_var;
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

	/**
	 * Flag di condivisione gestione del legame con indice
	 */
	public void setFl_condiviso(char value) {
		this.fl_condiviso = value;
	}

	/**
	 * Flag di condivisione gestione del legame con indice
	 */
	public char getFl_condiviso() {
		return fl_condiviso;
	}

	/**
	 * Timestamp di condivisione gestione del legame con indice
	 */
	public void setUte_condiviso(String value) {
		this.ute_condiviso = value;
	}

	/**
	 * Timestamp di condivisione gestione del legame con indice
	 */
	public String getUte_condiviso() {
		return ute_condiviso;
	}

	/**
	 * Utente che ha attivato la gestione condivisa del legame con indice
	 */
	public void setTs_condiviso(java.sql.Timestamp value) {
		this.ts_condiviso = value;
	}

	/**
	 * Utente che ha attivato la gestione condivisa del legame con indice
	 */
	public java.sql.Timestamp getTs_condiviso() {
		return ts_condiviso;
	}

	public void setTr_aut_aut(java.util.Set value) {
		this.Tr_aut_aut = value;
	}

	public java.util.Set getTr_aut_aut() {
		return Tr_aut_aut;
	}


	public void setTr_aut_aut1(java.util.Set value) {
		this.Tr_aut_aut1 = value;
	}

	public java.util.Set getTr_aut_aut1() {
		return Tr_aut_aut1;
	}


	public void setTr_aut_bib(java.util.Set value) {
		this.Tr_aut_bib = value;
	}

	public java.util.Set getTr_aut_bib() {
		return Tr_aut_bib;
	}


	public void setTr_aut_mar(java.util.Set value) {
		this.Tr_aut_mar = value;
	}

	public java.util.Set getTr_aut_mar() {
		return Tr_aut_mar;
	}


	public void setTr_per_int(java.util.Set value) {
		this.Tr_per_int = value;
	}

	public java.util.Set getTr_per_int() {
		return Tr_per_int;
	}


	public void setTr_rep_aut(java.util.Set value) {
		this.Tr_rep_aut = value;
	}

	public java.util.Set getTr_rep_aut() {
		return Tr_rep_aut;
	}


	public void setTr_tit_aut(java.util.Set value) {
		this.Tr_tit_aut = value;
	}

	public java.util.Set getTr_tit_aut() {
		return Tr_tit_aut;
	}


	public String toString() {
		return String.valueOf(getVid());
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
