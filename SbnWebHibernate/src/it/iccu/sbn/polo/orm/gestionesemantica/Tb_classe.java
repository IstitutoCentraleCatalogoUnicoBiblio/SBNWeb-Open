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
package it.iccu.sbn.polo.orm.gestionesemantica;

import java.io.Serializable;
/**
 * INDICI DI CLASSIFICAZIONI (TPSCLA))
 */
/**
 * ORM-Persistable Class
 */
public class Tb_classe implements Serializable {

	private static final long serialVersionUID = 1553943391913294614L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_classe))
			return false;
		Tb_classe tb_classe = (Tb_classe)aObj;
		if (getCd_sistema() != null && !getCd_sistema().equals(tb_classe.getCd_sistema()))
			return false;
		if (getCd_edizione() != null && !getCd_edizione().equals(tb_classe.getCd_edizione()))
			return false;
		if (getClasse() != null && !getClasse().equals(tb_classe.getClasse()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCd_sistema().hashCode();
		hashcode = hashcode + getCd_edizione().hashCode();
		hashcode = hashcode + getClasse().hashCode();
		return hashcode;
	}

	private String cd_sistema;

	private String cd_edizione;

	private String classe;

	private String ds_classe;

	private String cd_livello;

	private Character fl_costruito;

	private char fl_speciale;

	private String ky_classe_ord;

	private String suffisso;

	private String ult_term;

	private char fl_condiviso;

	private String ute_condiviso;

	private java.sql.Timestamp ts_condiviso;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione Tbc_collocazione;

	private java.util.Set Tr_sistemi_classi_biblioteche = new java.util.HashSet();

	private java.util.Set Tr_tit_cla = new java.util.HashSet();

	public void setCd_sistema(String value) {
		this.cd_sistema = value;
	}

	public String getCd_sistema() {
		return cd_sistema;
	}

	public void setCd_edizione(String value) {
		this.cd_edizione = value;
	}

	public String getCd_edizione() {
		return cd_edizione;
	}

	public void setClasse(String value) {
		this.classe = value;
	}

	public String getClasse() {
		return classe;
	}

	/**
	 * significato del simbolo di classificazione
	 */
	public void setDs_classe(String value) {
		this.ds_classe = value;
	}

	/**
	 * significato del simbolo di classificazione
	 */
	public String getDs_classe() {
		return ds_classe;
	}

	/**
	 * codice del livello di autorita'
	 */
	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	/**
	 * codice del livello di autorita'
	 */
	public String getCd_livello() {
		return cd_livello;
	}

	public void setFl_costruito(char value) {
		setFl_costruito(new Character(value));
	}

	public void setFl_costruito(Character value) {
		this.fl_costruito = value;
	}

	public Character getFl_costruito() {
		return fl_costruito;
	}

	public void setFl_speciale(char value) {
		this.fl_speciale = value;
	}

	public char getFl_speciale() {
		return fl_speciale;
	}

	/**
	 * indice per ordinamento
	 */
	public void setKy_classe_ord(String value) {
		this.ky_classe_ord = value;
	}

	/**
	 * indice per ordinamento
	 */
	public String getKy_classe_ord() {
		return ky_classe_ord;
	}

	/**
	 * Suffisso alfabetico degli indici Deweey, valorizzabile solo per indici deweey per ordinamento separato
	 */
	public void setSuffisso(String value) {
		this.suffisso = value;
	}

	/**
	 * Suffisso alfabetico degli indici Deweey, valorizzabile solo per indici deweey per ordinamento separato
	 */
	public String getSuffisso() {
		return suffisso;
	}

	/**
	 * ulteriori termini di ricerca
	 */
	public void setUlt_term(String value) {
		this.ult_term = value;
	}

	/**
	 * ulteriori termini di ricerca
	 */
	public String getUlt_term() {
		return ult_term;
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
	 * Utente che ha effettuato la condivisione con il sistema Indice
	 */
	public void setUte_condiviso(String value) {
		this.ute_condiviso = value;
	}

	/**
	 * Utente che ha effettuato la condivisione con il sistema Indice
	 */
	public String getUte_condiviso() {
		return ute_condiviso;
	}

	/**
	 * Timestamp di cndivisione con sistema indice
	 */
	public void setTs_condiviso(java.sql.Timestamp value) {
		this.ts_condiviso = value;
	}

	/**
	 * Timestamp di cndivisione con sistema indice
	 */
	public java.sql.Timestamp getTs_condiviso() {
		return ts_condiviso;
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

	public void setTbc_collocazione(it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione value) {
		this.Tbc_collocazione = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione getTbc_collocazione() {
		return Tbc_collocazione;
	}

	public void setTr_sistemi_classi_biblioteche(java.util.Set value) {
		this.Tr_sistemi_classi_biblioteche = value;
	}

	public java.util.Set getTr_sistemi_classi_biblioteche() {
		return Tr_sistemi_classi_biblioteche;
	}


	public void setTr_tit_cla(java.util.Set value) {
		this.Tr_tit_cla = value;
	}

	public java.util.Set getTr_tit_cla() {
		return Tr_tit_cla;
	}


	public String toString() {
		return String.valueOf(getCd_sistema() + " " + getCd_edizione() + " " + getClasse());
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
