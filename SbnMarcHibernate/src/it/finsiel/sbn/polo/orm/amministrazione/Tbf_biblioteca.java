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
package it.finsiel.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * ANAGRAFICA BIBLIOTECHE (TPRBIB)
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_biblioteca implements Serializable {

	private static final long serialVersionUID = 6165158851070542189L;

	public Tbf_biblioteca() {
	}

	private int id_biblioteca;

	private String cd_ana_biblioteca;

	private String cd_polo;

	private String cd_bib;

	private String nom_biblioteca;

	private String unit_org;

	private String indirizzo;

	private String cpostale;

	private String cap;

	private String telefono;

	private String fax;

	private String note;

	private String p_iva;

	private String cd_fiscale;

	private String e_mail;

	private char tipo_biblioteca;

	private String paese;

	private String provincia;

	private String cd_bib_cs;

	private String cd_bib_ut;

	private Integer cd_utente;

	private char flag_bib;

	private String localita;

	private String chiave_bib;

	private String chiave_ente;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private java.util.Set Tbf_biblioteca_in_polo = new java.util.HashSet();

	private void setId_biblioteca(int value) {
		this.id_biblioteca = value;
	}

	public int getId_biblioteca() {
		return id_biblioteca;
	}

	public int getORMID() {
		return getId_biblioteca();
	}

	/**
	 * codice della biblioteca nell'anagrafe centrale
	 */
	public void setCd_ana_biblioteca(String value) {
		this.cd_ana_biblioteca = value;
	}

	/**
	 * codice della biblioteca nell'anagrafe centrale
	 */
	public String getCd_ana_biblioteca() {
		return cd_ana_biblioteca;
	}

	/**
	 * codice identificativo del polo di localizzazione
	 */
	public void setCd_polo(String value) {
		this.cd_polo = value;
	}

	/**
	 * codice identificativo del polo di localizzazione
	 */
	public String getCd_polo() {
		return cd_polo;
	}

	/**
	 * codice identificativo della biblioteca
	 */
	public void setCd_bib(String value) {
		this.cd_bib = value;
	}

	/**
	 * codice identificativo della biblioteca
	 */
	public String getCd_bib() {
		return cd_bib;
	}

	/**
	 * denominazione della biblioteca
	 */
	public void setNom_biblioteca(String value) {
		this.nom_biblioteca = value;
	}

	/**
	 * denominazione della biblioteca
	 */
	public String getNom_biblioteca() {
		return nom_biblioteca;
	}

	/**
	 * denominazione dell'unita' organizzativa della biblioteca
	 */
	public void setUnit_org(String value) {
		this.unit_org = value;
	}

	/**
	 * denominazione dell'unita' organizzativa della biblioteca
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
	 * note relative alla biblioteca
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * note relative alla biblioteca
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
	public void setCd_fiscale(String value) {
		this.cd_fiscale = value;
	}

	/**
	 * codice fiscale
	 */
	public String getCd_fiscale() {
		return cd_fiscale;
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
	 * codice identificativo della tipologia di biblioteca
	 */
	public void setTipo_biblioteca(char value) {
		this.tipo_biblioteca = value;
	}

	/**
	 * codice identificativo della tipologia di biblioteca
	 */
	public char getTipo_biblioteca() {
		return tipo_biblioteca;
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
	 * codice identificativo della biblioteca centro sistema di riferimento (solo per biblioteche sbn)
	 */
	public void setCd_bib_cs(String value) {
		this.cd_bib_cs = value;
	}

	/**
	 * codice identificativo della biblioteca centro sistema di riferimento (solo per biblioteche sbn)
	 */
	public String getCd_bib_cs() {
		return cd_bib_cs;
	}

	/**
	 * prima parte del codice utente assegnato alla biblioteca in quanto utente
	 */
	public void setCd_bib_ut(String value) {
		this.cd_bib_ut = value;
	}

	/**
	 * prima parte del codice utente assegnato alla biblioteca in quanto utente
	 */
	public String getCd_bib_ut() {
		return cd_bib_ut;
	}

	/**
	 * seconda parte del codice utente assegnato alla biblioteca in quanto utente
	 */
	public void setCd_utente(int value) {
		setCd_utente(new Integer(value));
	}

	/**
	 * seconda parte del codice utente assegnato alla biblioteca in quanto utente
	 */
	public void setCd_utente(Integer value) {
		this.cd_utente = value;
	}

	/**
	 * seconda parte del codice utente assegnato alla biblioteca in quanto utente
	 */
	public Integer getCd_utente() {
		return cd_utente;
	}

	/**
	 * indica se la biblioteca e' sbn - centro sistema ("c"), sbn - affiliata ("a"), non sbn ("n")
	 */
	public void setFlag_bib(char value) {
		this.flag_bib = value;
	}

	/**
	 * indica se la biblioteca e' sbn - centro sistema ("c"), sbn - affiliata ("a"), non sbn ("n")
	 */
	public char getFlag_bib() {
		return flag_bib;
	}

	/**
	 * localita'
	 */
	public void setLocalita(String value) {
		this.localita = value;
	}

	/**
	 * localita'
	 */
	public String getLocalita() {
		return localita;
	}

	/**
	 * chiave nome biblioteca
	 */
	public void setChiave_bib(String value) {
		this.chiave_bib = value;
	}

	/**
	 * chiave nome biblioteca
	 */
	public String getChiave_bib() {
		return chiave_bib;
	}

	/**
	 * chiave ente di appartenenza
	 */
	public void setChiave_ente(String value) {
		this.chiave_ente = value;
	}

	/**
	 * chiave ente di appartenenza
	 */
	public String getChiave_ente() {
		return chiave_ente;
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

	public void setTbf_biblioteca_in_polo(java.util.Set value) {
		this.Tbf_biblioteca_in_polo = value;
	}

	public java.util.Set getTbf_biblioteca_in_polo() {
		return Tbf_biblioteca_in_polo;
	}


	public String toString() {
		return String.valueOf(getId_biblioteca());
	}

}
