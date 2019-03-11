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
/**
 * Dati di fornitore in Biblioteca
 */
/**
 * ORM-Persistable Class
 */
public class Tbr_fornitori_biblioteche implements Serializable {

	private static final long serialVersionUID = -5684867649651838542L;


	public Tbr_fornitori_biblioteche() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbr_fornitori_biblioteche))
			return false;
		Tbr_fornitori_biblioteche tbr_fornitori_biblioteche = (Tbr_fornitori_biblioteche)aObj;
		if (getCd_biblioteca() == null && tbr_fornitori_biblioteche.getCd_biblioteca() != null)
			return false;
		if (!getCd_biblioteca().equals(tbr_fornitori_biblioteche.getCd_biblioteca()))
			return false;
		if (getCod_fornitore() != tbr_fornitori_biblioteche.getCod_fornitore())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_biblioteca() != null) {
			hashcode = hashcode + (getCd_biblioteca().getCd_biblioteca() == null ? 0 : getCd_biblioteca().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_biblioteca().getCd_polo() == null ? 0 : getCd_biblioteca().getCd_polo().hashCode());
		}
		hashcode = hashcode + getCod_fornitore();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_biblioteca;

	private int cod_fornitore;

	private String tipo_pagamento;

	private String cod_cliente;

	private String nom_contatto;

	private String tel_contatto;

	private String fax_contatto;

	private String valuta;

	private String cod_polo;

	private char allinea;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

//	private it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori Tbr_fornitori;

	private java.util.Set Tra_sez_acquisizione_fornitori = new java.util.HashSet();


	public void setCod_fornitore(int value) {
		this.cod_fornitore = value;
	}

	public int getCod_fornitore() {
		return cod_fornitore;
	}

	/**
	 * descrizione del modo di pagamento o titolo c/c bancario con il quale il fornitore vuole essere pagato dalla biblioteca
	 */
	public void setTipo_pagamento(String value) {
		this.tipo_pagamento = value;
	}

	/**
	 * descrizione del modo di pagamento o titolo c/c bancario con il quale il fornitore vuole essere pagato dalla biblioteca
	 */
	public String getTipo_pagamento() {
		return tipo_pagamento;
	}

	/**
	 * codice cliente che ha la biblioteca presso il fornitore
	 */
	public void setCod_cliente(String value) {
		this.cod_cliente = value;
	}

	/**
	 * codice cliente che ha la biblioteca presso il fornitore
	 */
	public String getCod_cliente() {
		return cod_cliente;
	}

	/**
	 * denominazione del personale da contattare nel caso in cui il fornitore sia un ente
	 */
	public void setNom_contatto(String value) {
		this.nom_contatto = value;
	}

	/**
	 * denominazione del personale da contattare nel caso in cui il fornitore sia un ente
	 */
	public String getNom_contatto() {
		return nom_contatto;
	}

	/**
	 * numero di telefono del personale da contattare nel caso in cui il fornitore sia un ente
	 */
	public void setTel_contatto(String value) {
		this.tel_contatto = value;
	}

	/**
	 * numero di telefono del personale da contattare nel caso in cui il fornitore sia un ente
	 */
	public String getTel_contatto() {
		return tel_contatto;
	}

	/**
	 * numero di fax del personale da contattare nel caso in cui il fornitore sia un ente
	 */
	public void setFax_contatto(String value) {
		this.fax_contatto = value;
	}

	/**
	 * numero di fax del personale da contattare nel caso in cui il fornitore sia un ente
	 */
	public String getFax_contatto() {
		return fax_contatto;
	}

	/**
	 * codice della valuta di pagamento
	 */
	public void setValuta(String value) {
		this.valuta = value;
	}

	/**
	 * codice della valuta di pagamento
	 */
	public String getValuta() {
		return valuta;
	}

	/**
	 * codice polo
	 */
	public void setCod_polo(String value) {
		this.cod_polo = value;
	}

	/**
	 * codice polo
	 */
	public String getCod_polo() {
		return cod_polo;
	}

	/**
	 * flag allinea
	 */
	public void setAllinea(char value) {
		this.allinea = value;
	}

	/**
	 * flag allinea
	 */
	public char getAllinea() {
		return allinea;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * data e ora d'inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * data e ora d'inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setCd_biblioteca(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_biblioteca() {
		return cd_biblioteca;
	}

	public String toString() {
		return String.valueOf(((getCd_biblioteca() == null) ? "" : String.valueOf(getCd_biblioteca().getCd_biblioteca()) + " " + String.valueOf(getCd_biblioteca().getCd_polo())) + " " + getCod_fornitore());
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

	public java.util.Set getTra_sez_acquisizione_fornitori() {
		return Tra_sez_acquisizione_fornitori;
	}

	public void setTra_sez_acquisizione_fornitori(
			java.util.Set tra_sez_acquisizione_fornitori) {
		Tra_sez_acquisizione_fornitori = tra_sez_acquisizione_fornitori;
	}



/*	public it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori getTbr_fornitori() {
		return Tbr_fornitori;
	}

	public void setTbr_fornitori(
			it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori tbr_fornitori) {
		Tbr_fornitori = tbr_fornitori;
	}*/

}
