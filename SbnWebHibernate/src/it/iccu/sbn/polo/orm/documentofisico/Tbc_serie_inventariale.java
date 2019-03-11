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
package it.iccu.sbn.polo.orm.documentofisico;

import java.io.Serializable;
/**
 * SERIE INVENTARIALI
 */
/**
 * ORM-Persistable Class
 */
public class Tbc_serie_inventariale implements Serializable {

	private static final long serialVersionUID = 9015993813769542896L;

	public Tbc_serie_inventariale() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbc_serie_inventariale))
			return false;
		Tbc_serie_inventariale tbc_serie_inventariale = (Tbc_serie_inventariale)aObj;
		if ((getCd_serie() != null && !getCd_serie().equals(tbc_serie_inventariale.getCd_serie())) || (getCd_serie() == null && tbc_serie_inventariale.getCd_serie() != null))
			return false;
		if (getCd_polo() == null && tbc_serie_inventariale.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(tbc_serie_inventariale.getCd_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCd_serie() == null ? 0 : getCd_serie().hashCode());
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getCd_biblioteca() == null ? 0 : getCd_polo().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_polo().getCd_polo() == null ? 0 : getCd_polo().getCd_polo().hashCode());
		}
		return hashcode;
	}

	private String cd_serie;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_polo;

	private String descr;

	private int prg_inv_corrente;

	private int prg_inv_pregresso;

	private int num_man;

	private int inizio_man;

	private int fine_man;

	private char flg_chiusa;

	private int buono_carico;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Date dt_ingr_inv_man;

	private java.util.Date dt_ingr_inv_preg;

	private java.util.Date dt_ingr_inv_ris1;

	private Integer inizio_man2;

	private Integer fine_man2;

	private java.util.Date dt_ingr_inv_ris2;

	private Integer inizio_man3;

	private Integer fine_man3;

	private java.util.Date dt_ingr_inv_ris3;

	private Integer inizio_man4;

	private Integer fine_man4;

	private java.util.Date dt_ingr_inv_ris4;

	private Character fl_default;

	private java.util.Set Tbc_inventario = new java.util.HashSet();

	public void setCd_serie(String value) {
		this.cd_serie = value;
	}

	public String getCd_serie() {
		return cd_serie;
	}

	/**
	 * descrizione della serie inventariale
	 */
	public void setDescr(String value) {
		this.descr = value;
	}

	/**
	 * descrizione della serie inventariale
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * progressivo per l'assegnazione automatica del n. inventario corrente
	 */
	public void setPrg_inv_corrente(int value) {
		this.prg_inv_corrente = value;
	}

	/**
	 * progressivo per l'assegnazione automatica del n. inventario corrente
	 */
	public int getPrg_inv_corrente() {
		return prg_inv_corrente;
	}

	/**
	 * progressivo per l'assegnazione automatica del n. inventario pregresso
	 */
	public void setPrg_inv_pregresso(int value) {
		this.prg_inv_pregresso = value;
	}

	/**
	 * progressivo per l'assegnazione automatica del n. inventario pregresso
	 */
	public int getPrg_inv_pregresso() {
		return prg_inv_pregresso;
	}

	/**
	 * numero di inventario di soglia per l'assegnazione manuale
	 */
	public void setNum_man(int value) {
		this.num_man = value;
	}

	/**
	 * numero di inventario di soglia per l'assegnazione manuale
	 */
	public int getNum_man() {
		return num_man;
	}

	/**
	 * numero di inizio intervallo per l'attribuzione manuale
	 */
	public void setInizio_man(int value) {
		this.inizio_man = value;
	}

	/**
	 * numero di inizio intervallo per l'attribuzione manuale
	 */
	public int getInizio_man() {
		return inizio_man;
	}

	/**
	 * numero di fine intervallo per l'attribuzione manuale
	 */
	public void setFine_man(int value) {
		this.fine_man = value;
	}

	/**
	 * numero di fine intervallo per l'attribuzione manuale
	 */
	public int getFine_man() {
		return fine_man;
	}

	/**
	 * indicatore di serie chiusa 1= serie non utilizzabile per assegnazione automatica
	 */
	public void setFlg_chiusa(char value) {
		this.flg_chiusa = value;
	}

	/**
	 * indicatore di serie chiusa 1= serie non utilizzabile per assegnazione automatica
	 */
	public char getFlg_chiusa() {
		return flg_chiusa;
	}

	/**
	 * progressivo del buono di carico
	 */
	public void setBuono_carico(int value) {
		this.buono_carico = value;
	}

	/**
	 * progressivo del buono di carico
	 */
	public int getBuono_carico() {
		return buono_carico;
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



	/**
	 * Data convenzionale da assumere come data di ingresso di default per l'inserimento di inventari manuali (n.ro num_man)
	 */
	public void setDt_ingr_inv_man(java.util.Date value) {
		this.dt_ingr_inv_man = value;
	}

	/**
	 * Data convenzionale da assumere come data di ingresso di default per l'inserimento di inventari manuali (n.ro num_man)
	 */
	public java.util.Date getDt_ingr_inv_man() {
		return dt_ingr_inv_man;
	}

	/**
	 * Data convenzionale da assumere come data di ingresso di default per l'inserimento di inventari pregressi
	 */
	public void setDt_ingr_inv_preg(java.util.Date value) {
		this.dt_ingr_inv_preg = value;
	}

	/**
	 * Data convenzionale da assumere come data di ingresso di default per l'inserimento di inventari pregressi
	 */
	public java.util.Date getDt_ingr_inv_preg() {
		return dt_ingr_inv_preg;
	}

	/**
	 * Data convenzionale di ingresso, default per l'inserimento di inventari nel primo intervallo riservato (inizo_man n.ro fine_man)
	 */
	public void setDt_ingr_inv_ris1(java.util.Date value) {
		this.dt_ingr_inv_ris1 = value;
	}

	/**
	 * Data convenzionale di ingresso, default per l'inserimento di inventari nel primo intervallo riservato (inizo_man n.ro fine_man)
	 */
	public java.util.Date getDt_ingr_inv_ris1() {
		return dt_ingr_inv_ris1;
	}

	/**
	 * numero di inizio secondo intervallo riservato per l'attribuzione manuale
	 */
	public void setInizio_man2(int value) {
		setInizio_man2(new Integer(value));
	}

	/**
	 * numero di inizio secondo intervallo riservato per l'attribuzione manuale
	 */
	public void setInizio_man2(Integer value) {
		this.inizio_man2 = value;
	}

	/**
	 * numero di inizio secondo intervallo riservato per l'attribuzione manuale
	 */
	public Integer getInizio_man2() {
		return inizio_man2;
	}

	/**
	 * numero di fine secondo intervallo riservato per l'attribuzione manuale
	 */
	public void setFine_man2(int value) {
		setFine_man2(new Integer(value));
	}

	/**
	 * numero di fine secondo intervallo riservato per l'attribuzione manuale
	 */
	public void setFine_man2(Integer value) {
		this.fine_man2 = value;
	}

	/**
	 * numero di fine secondo intervallo riservato per l'attribuzione manuale
	 */
	public Integer getFine_man2() {
		return fine_man2;
	}

	/**
	 * Data convenzionale di ingresso, default per l'inserimento di inventari nel secondo intervallo riservato (inizo_man2 n.ro fine_man2)
	 */
	public void setDt_ingr_inv_ris2(java.util.Date value) {
		this.dt_ingr_inv_ris2 = value;
	}

	/**
	 * Data convenzionale di ingresso, default per l'inserimento di inventari nel secondo intervallo riservato (inizo_man2 n.ro fine_man2)
	 */
	public java.util.Date getDt_ingr_inv_ris2() {
		return dt_ingr_inv_ris2;
	}

	/**
	 * numero di inizio terzo intervallo riservato per l'attribuzione manuale
	 */
	public void setInizio_man3(int value) {
		setInizio_man3(new Integer(value));
	}

	/**
	 * numero di inizio terzo intervallo riservato per l'attribuzione manuale
	 */
	public void setInizio_man3(Integer value) {
		this.inizio_man3 = value;
	}

	/**
	 * numero di inizio terzo intervallo riservato per l'attribuzione manuale
	 */
	public Integer getInizio_man3() {
		return inizio_man3;
	}

	/**
	 * numero di fine terzo intervallo riservato per l'attribuzione manuale
	 */
	public void setFine_man3(int value) {
		setFine_man3(new Integer(value));
	}

	/**
	 * numero di fine terzo intervallo riservato per l'attribuzione manuale
	 */
	public void setFine_man3(Integer value) {
		this.fine_man3 = value;
	}

	/**
	 * numero di fine terzo intervallo riservato per l'attribuzione manuale
	 */
	public Integer getFine_man3() {
		return fine_man3;
	}

	/**
	 * Data convenzionale di ingresso, default per l'inserimento di inventari nel terzo intervallo riservato (inizo_man3 n.ro fine_man3)
	 */
	public void setDt_ingr_inv_ris3(java.util.Date value) {
		this.dt_ingr_inv_ris3 = value;
	}

	/**
	 * Data convenzionale di ingresso, default per l'inserimento di inventari nel terzo intervallo riservato (inizo_man3 n.ro fine_man3)
	 */
	public java.util.Date getDt_ingr_inv_ris3() {
		return dt_ingr_inv_ris3;
	}

	/**
	 * numero di inizio quarto intervallo riservato per l'attribuzione manuale
	 */
	public void setInizio_man4(int value) {
		setInizio_man4(new Integer(value));
	}

	/**
	 * numero di inizio quarto intervallo riservato per l'attribuzione manuale
	 */
	public void setInizio_man4(Integer value) {
		this.inizio_man4 = value;
	}

	/**
	 * numero di inizio quarto intervallo riservato per l'attribuzione manuale
	 */
	public Integer getInizio_man4() {
		return inizio_man4;
	}

	/**
	 * numero di fine quarto intervallo riservato per l'attribuzione manuale
	 */
	public void setFine_man4(int value) {
		setFine_man4(new Integer(value));
	}

	/**
	 * numero di fine quarto intervallo riservato per l'attribuzione manuale
	 */
	public void setFine_man4(Integer value) {
		this.fine_man4 = value;
	}

	/**
	 * numero di fine quarto intervallo riservato per l'attribuzione manuale
	 */
	public Integer getFine_man4() {
		return fine_man4;
	}

	/**
	 * Data convenzionale di ingresso, default per l'inserimento di inventari nel quarto intervallo riservato (inizo_man4 n.ro fine_man4)
	 */
	public void setDt_ingr_inv_ris4(java.util.Date value) {
		this.dt_ingr_inv_ris4 = value;
	}

	/**
	 * Data convenzionale di ingresso, default per l'inserimento di inventari nel quarto intervallo riservato (inizo_man4 n.ro fine_man4)
	 */
	public java.util.Date getDt_ingr_inv_ris4() {
		return dt_ingr_inv_ris4;
	}

	/**
	 * Indicatore di serie da proporre come default in creazione nuovo inventario
	 */
	public void setFl_default(char value) {
		setFl_default(new Character(value));
	}

	/**
	 * Indicatore di serie da proporre come default in creazione nuovo inventario
	 */
	public void setFl_default(Character value) {
		this.fl_default = value;
	}

	/**
	 * Indicatore di serie da proporre come default in creazione nuovo inventario
	 */
	public Character getFl_default() {
		return fl_default;
	}




	public void setCd_polo(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_polo = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_polo() {
		return cd_polo;
	}

	public void setTbc_inventario(java.util.Set value) {
		this.Tbc_inventario = value;
	}

	public java.util.Set getTbc_inventario() {
		return Tbc_inventario;
	}


	public String toString() {
		return String.valueOf(getCd_serie() + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getCd_biblioteca()) + " " + String.valueOf(getCd_polo().getCd_polo())));
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
