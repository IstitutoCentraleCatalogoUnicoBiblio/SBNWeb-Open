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
 * Fornitori
 */
/**
 * ORM-Persistable Class
 */
public class Tbr_fornitori implements Serializable {

	private static final long serialVersionUID = -5532941442764064474L;

	public Tbr_fornitori() {
	}

	private int cod_fornitore;

	private String nom_fornitore;

	private String unit_org;

	private String indirizzo;

	private String cpostale;

	private String citta;

	private String cap;

	private String telefono;

	private String fax;

	private String note;

	private String p_iva;

	private String cod_fiscale;

	private String e_mail;

	private String paese;

	private char tipo_partner;

	private String provincia;

	private String cod_bib;

	private String chiave_for;

	private String cod_polo_bib;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private String regione;

	private java.util.Set Tbr_fornitori_biblioteche = new java.util.HashSet();

	private java.util.Set Tba_offerte_fornitore = new java.util.HashSet();

	private java.util.Set Tba_buono_ordine = new java.util.HashSet();

	private java.util.Set Tba_fatture = new java.util.HashSet();

	private java.util.Set Tba_ordini = new java.util.HashSet();

	private void setCod_fornitore(int value) {
		this.cod_fornitore = value;
	}

	public int getCod_fornitore() {
		return cod_fornitore;
	}

	public int getORMID() {
		return getCod_fornitore();
	}

	/**
	 * denominazione del fornitore
	 */
	public void setNom_fornitore(String value) {
		this.nom_fornitore = value;
	}

	/**
	 * denominazione del fornitore
	 */
	public String getNom_fornitore() {
		return nom_fornitore;
	}

	/**
	 * unita' organizzativa del fornitore
	 */
	public void setUnit_org(String value) {
		this.unit_org = value;
	}

	/**
	 * unita' organizzativa del fornitore
	 */
	public String getUnit_org() {
		return unit_org;
	}

	/**
	 * indirizzo
	 */
	public void setIndirizzo(String value) {
		this.indirizzo = value;
	}

	/**
	 * indirizzo
	 */
	public String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * casella postale
	 */
	public void setCpostale(String value) {
		this.cpostale = value;
	}

	/**
	 * casella postale
	 */
	public String getCpostale() {
		return cpostale;
	}

	/**
	 * citta'
	 */
	public void setCitta(String value) {
		this.citta = value;
	}

	/**
	 * citta'
	 */
	public String getCitta() {
		return citta;
	}

	/**
	 * codice d'avviamento postale
	 */
	public void setCap(String value) {
		this.cap = value;
	}

	/**
	 * codice d'avviamento postale
	 */
	public String getCap() {
		return cap;
	}

	/**
	 * numero telefonico
	 */
	public void setTelefono(String value) {
		this.telefono = value;
	}

	/**
	 * numero telefonico
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * numero del fax
	 */
	public void setFax(String value) {
		this.fax = value;
	}

	/**
	 * numero del fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * note riferite al fornitore
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * note riferite al fornitore
	 */
	public String getNote() {
		return note;
	}

	/**
	 * partita iva
	 */
	public void setP_iva(String value) {
		this.p_iva = value;
	}

	/**
	 * partita iva
	 */
	public String getP_iva() {
		return p_iva;
	}

	/**
	 * codice fiscale
	 */
	public void setCod_fiscale(String value) {
		this.cod_fiscale = value;
	}

	/**
	 * codice fiscale
	 */
	public String getCod_fiscale() {
		return cod_fiscale;
	}

	/**
	 * indirizzo elettronico
	 */
	public void setE_mail(String value) {
		this.e_mail = value;
	}

	/**
	 * indirizzo elettronico
	 */
	public String getE_mail() {
		return e_mail;
	}

	/**
	 * codice identificativo del paese
	 */
	public void setPaese(String value) {
		this.paese = value;
	}

	/**
	 * codice identificativo del paese
	 */
	public String getPaese() {
		return paese;
	}

	/**
	 * codice identificativo della tipologia di partner
	 */
	public void setTipo_partner(char value) {
		this.tipo_partner = value;
	}

	/**
	 * codice identificativo della tipologia di partner
	 */
	public char getTipo_partner() {
		return tipo_partner;
	}

	/**
	 * codice identificativo della provincia
	 */
	public void setProvincia(String value) {
		this.provincia = value;
	}

	/**
	 * codice identificativo della provincia
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * per il fornitore-biblioteca, codice  identificativo della biblioteca corrispondente
	 */
	public void setCod_bib(String value) {
		this.cod_bib = value;
	}

	/**
	 * per il fornitore-biblioteca, codice  identificativo della biblioteca corrispondente
	 */
	public String getCod_bib() {
		return cod_bib;
	}

	/**
	 * chiave nome fornitore
	 */
	public void setChiave_for(String value) {
		this.chiave_for = value;
	}

	/**
	 * chiave nome fornitore
	 */
	public String getChiave_for() {
		return chiave_for;
	}

	/**
	 * per il fornitore-biblioteca, codice identificativo del polo della biblioteca corrispondente
	 */
	public void setCod_polo_bib(String value) {
		this.cod_polo_bib = value;
	}

	/**
	 * per il fornitore-biblioteca, codice identificativo del polo della biblioteca corrispondente
	 */
	public String getCod_polo_bib() {
		return cod_polo_bib;
	}

	/**
	 * codice biblioteca che effettua inserimento
	 */
	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	/**
	 * codice biblioteca che effettua inserimento
	 */
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

	/**
	 * codice biblioteca che effettua aggiornamento
	 */
	public void setUte_var(String value) {
		this.ute_var = value;
	}

	/**
	 * codice biblioteca che effettua aggiornamento
	 */
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

	public void setTba_offerte_fornitore(java.util.Set value) {
		this.Tba_offerte_fornitore = value;
	}

	public java.util.Set getTba_offerte_fornitore() {
		return Tba_offerte_fornitore;
	}


	public void setTba_buono_ordine(java.util.Set value) {
		this.Tba_buono_ordine = value;
	}

	public java.util.Set getTba_buono_ordine() {
		return Tba_buono_ordine;
	}


	public void setTba_fatture(java.util.Set value) {
		this.Tba_fatture = value;
	}

	public java.util.Set getTba_fatture() {
		return Tba_fatture;
	}


	public void setTba_ordini(java.util.Set value) {
		this.Tba_ordini = value;
	}

	public java.util.Set getTba_ordini() {
		return Tba_ordini;
	}


	public String toString() {
		return String.valueOf(getCod_fornitore());
	}

	public java.util.Set getTbr_fornitori_biblioteche() {
		return Tbr_fornitori_biblioteche;
	}

	public void setTbr_fornitori_biblioteche(java.util.Set tbr_fornitori_biblioteche) {
		Tbr_fornitori_biblioteche = tbr_fornitori_biblioteche;
	}
	public void setTidx_vector(int value) {
		setTidx_vector(new Integer(value));
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

}
