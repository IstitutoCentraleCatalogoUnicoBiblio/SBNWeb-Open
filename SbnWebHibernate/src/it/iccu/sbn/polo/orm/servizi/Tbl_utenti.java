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
 * Utenti
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_utenti extends Tb_base {

	private static final long serialVersionUID = 479601135528948298L;

	private int id_utenti;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private String cod_utente;

	private String password;

	private String cognome;

	private String nome;

	private String indirizzo_res;

	private String citta_res;

	private String cap_res;

	private String tel_res;

	private String fax_res;

	private String indirizzo_dom;

	private String citta_dom;

	private String cap_dom;

	private String tel_dom;

	private String fax_dom;

	private char sesso;

	private java.util.Date data_nascita;

	private String luogo_nascita;

	private String cod_fiscale;

	private String cod_ateneo;

	private String cod_matricola;

	private String corso_laurea;

	private String ind_posta_elettr;

	private Character persona_giuridica;

	private String nome_referente;

	private java.util.Date data_reg;

	private java.math.BigDecimal credito_polo;

	private String note_polo;

	private String note;

	private Character cod_proven;

	private Character tipo_pers_giur;

	private String paese_res;

	private String paese_citt;

	private Character tipo_docum1;

	private String num_docum1;

	private String aut_ril_docum1;

	private Character tipo_docum2;

	private String num_docum2;

	private String aut_ril_docum2;

	private Character tipo_docum3;

	private String num_docum3;

	private String aut_ril_docum3;

	private Character tipo_docum4;

	private String num_docum4;

	private String aut_ril_docum4;

	private String cod_bib;

	private String prov_dom;

	private String prov_res;

	private String cod_polo_bib;

	private Character allinea;

	private String chiave_ute;

	private java.util.Set Trl_diritti_utente = new java.util.HashSet();

	private java.util.Set Trl_materie_utenti = new java.util.HashSet();

	private java.util.Set Trl_utenti_biblioteca = new java.util.HashSet();

	private java.util.Set Tbl_documenti_lettori = new java.util.HashSet();

	// Inizio modifica,  almaviva del 06/2009
	private java.sql.Timestamp ts_var_password;

	private java.sql.Timestamp last_access;

	private char change_password;

	private Character professione;

	private Character tit_studio;

	private String codice_anagrafe;

	private String cd_bib_iscrizione;

	private String ind_posta_elettr2;

	private Set<Tbl_prenotazione_posto> prenotazioni_posti = new HashSet<Tbl_prenotazione_posto>();

	private Tbl_biblioteca_ill biblioteca_ill;

	private Character cd_tipo_ute;

	// Fine modifica,  almaviva del 06/2009
	public void setId_utenti(int value) {
		this.id_utenti = value;
	}

	public int getId_utenti() {
		return id_utenti;
	}

	public int getORMID() {
		return getId_utenti();
	}

	/**
	 * seconda parte del codice identificativo dell'utente , costituita dal
	 * progressivo numerico assegnatogli dalla prima biblioteca del polo alla
	 * quale l'utente SI E' ISCRITTO
	 */
	public void setCod_utente(String value) {
		this.cod_utente = value;
	}

	/**
	 * seconda parte del codice identificativo dell'utente , costituita dal
	 * progressivo numerico assegnatogli dalla prima biblioteca del polo alla
	 * quale l'utente SI E' ISCRITTO
	 */
	public String getCod_utente() {
		return cod_utente;
	}

	/**
	 * password dell'utente
	 */
	public void setPassword(String value) {
		this.password = value;
	}

	/**
	 * password dell'utente
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * cognome dell'utente
	 */
	public void setCognome(String value) {
		this.cognome = value;
	}

	/**
	 * cognome dell'utente
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * nome dell''utente
	 */
	public void setNome(String value) {
		this.nome = value;
	}

	/**
	 * nome dell''utente
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * indirizzo di residenza
	 */
	public void setIndirizzo_res(String value) {
		this.indirizzo_res = value;
	}

	/**
	 * indirizzo di residenza
	 */
	public String getIndirizzo_res() {
		return indirizzo_res;
	}

	/**
	 * denominazione della citta' di residenza
	 */
	public void setCitta_res(String value) {
		this.citta_res = value;
	}

	/**
	 * denominazione della citta' di residenza
	 */
	public String getCitta_res() {
		return citta_res;
	}

	/**
	 * codice di avviamento postale di residenza
	 */
	public void setCap_res(String value) {
		this.cap_res = value;
	}

	/**
	 * codice di avviamento postale di residenza
	 */
	public String getCap_res() {
		return cap_res;
	}

	/**
	 * numero di telefono di residenza
	 */
	public void setTel_res(String value) {
		this.tel_res = value;
	}

	/**
	 * numero di telefono di residenza
	 */
	public String getTel_res() {
		return tel_res;
	}

	/**
	 * numero di fax di residenza
	 */
	public void setFax_res(String value) {
		this.fax_res = value;
	}

	/**
	 * numero di fax di residenza
	 */
	public String getFax_res() {
		return fax_res;
	}

	/**
	 * indirizzo di domicilio
	 */
	public void setIndirizzo_dom(String value) {
		this.indirizzo_dom = value;
	}

	/**
	 * indirizzo di domicilio
	 */
	public String getIndirizzo_dom() {
		return indirizzo_dom;
	}

	/**
	 * denominazione della citta' di domicilio
	 */
	public void setCitta_dom(String value) {
		this.citta_dom = value;
	}

	/**
	 * denominazione della citta' di domicilio
	 */
	public String getCitta_dom() {
		return citta_dom;
	}

	/**
	 * codice di avviamento postale di domicilio
	 */
	public void setCap_dom(String value) {
		this.cap_dom = value;
	}

	/**
	 * codice di avviamento postale di domicilio
	 */
	public String getCap_dom() {
		return cap_dom;
	}

	/**
	 * numero di telefono di domicilio
	 */
	public void setTel_dom(String value) {
		this.tel_dom = value;
	}

	/**
	 * numero di telefono di domicilio
	 */
	public String getTel_dom() {
		return tel_dom;
	}

	/**
	 * numero di fax di domicilio
	 */
	public void setFax_dom(String value) {
		this.fax_dom = value;
	}

	/**
	 * numero di fax di domicilio
	 */
	public String getFax_dom() {
		return fax_dom;
	}

	/**
	 * sesso
	 */
	public void setSesso(char value) {
		this.sesso = value;
	}

	/**
	 * sesso
	 */
	public char getSesso() {
		return sesso;
	}

	/**
	 * data di nascita
	 */
	public void setData_nascita(java.util.Date value) {
		this.data_nascita = value;
	}

	/**
	 * data di nascita
	 */
	public java.util.Date getData_nascita() {
		return data_nascita;
	}

	/**
	 * denominazione del luogo di nascita
	 */
	public void setLuogo_nascita(String value) {
		this.luogo_nascita = value;
	}

	/**
	 * denominazione del luogo di nascita
	 */
	public String getLuogo_nascita() {
		return luogo_nascita;
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
	 * codice dell'ateneo di appartenenza nel caso di studente/ssa
	 * universitario/a
	 */
	public void setCod_ateneo(String value) {
		this.cod_ateneo = value;
	}

	/**
	 * codice dell'ateneo di appartenenza nel caso di studente/ssa
	 * universitario/a
	 */
	public String getCod_ateneo() {
		return cod_ateneo;
	}

	/**
	 * codice matricola universitaria
	 */
	public void setCod_matricola(String value) {
		this.cod_matricola = value;
	}

	/**
	 * codice matricola universitaria
	 */
	public String getCod_matricola() {
		return cod_matricola;
	}

	/**
	 * descrizione del corso di laurea
	 */
	public void setCorso_laurea(String value) {
		this.corso_laurea = value;
	}

	/**
	 * descrizione del corso di laurea
	 */
	public String getCorso_laurea() {
		return corso_laurea;
	}

	/**
	 * indirizzo di posta elettronica
	 */
	public void setInd_posta_elettr(String value) {
		this.ind_posta_elettr = value;
	}

	/**
	 * indirizzo di posta elettronica
	 */
	public String getInd_posta_elettr() {
		return ind_posta_elettr;
	}

	/**
	 * indica se l'utente e' una persona giuridica ammette i valori "s" = si,
	 * "n" = no
	 */
	public void setPersona_giuridica(char value) {
		setPersona_giuridica(new Character(value));
	}

	/**
	 * indica se l'utente e' una persona giuridica ammette i valori "s" = si,
	 * "n" = no
	 */
	public void setPersona_giuridica(Character value) {
		this.persona_giuridica = value;
	}

	/**
	 * indica se l'utente e' una persona giuridica ammette i valori "s" = si,
	 * "n" = no
	 */
	public Character getPersona_giuridica() {
		return persona_giuridica;
	}

	/**
	 * nome del referente nel caso in cui l'utente sia una persona giuridica
	 */
	public void setNome_referente(String value) {
		this.nome_referente = value;
	}

	/**
	 * nome del referente nel caso in cui l'utente sia una persona giuridica
	 */
	public String getNome_referente() {
		return nome_referente;
	}

	/**
	 * data della prima registrazione dell'utente da parte della biblioteca
	 */
	public void setData_reg(java.util.Date value) {
		this.data_reg = value;
	}

	/**
	 * data della prima registrazione dell'utente da parte della biblioteca
	 */
	public java.util.Date getData_reg() {
		return data_reg;
	}

	/**
	 * credito a disposizione dell'utente per i servizi di polo
	 */
	public void setCredito_polo(java.math.BigDecimal value) {
		this.credito_polo = value;
	}

	/**
	 * credito a disposizione dell'utente per i servizi di polo
	 */
	public java.math.BigDecimal getCredito_polo() {
		return credito_polo;
	}

	/**
	 * note riguardanti l'utente a livello di polo
	 */
	public void setNote_polo(String value) {
		this.note_polo = value;
	}

	/**
	 * note riguardanti l'utente a livello di polo
	 */
	public String getNote_polo() {
		return note_polo;
	}

	/**
	 * eventuali infrazioni effettuate a livello di polo
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * eventuali infrazioni effettuate a livello di polo
	 */
	public String getNote() {
		return note;
	}

	/**
	 * codice identificativo della provenienza dell'utente
	 */
	public void setCod_proven(char value) {
		setCod_proven(new Character(value));
	}

	/**
	 * codice identificativo della provenienza dell'utente
	 */
	public void setCod_proven(Character value) {
		this.cod_proven = value;
	}

	/**
	 * codice identificativo della provenienza dell'utente
	 */
	public Character getCod_proven() {
		return cod_proven;
	}

	/**
	 * codice identificativo del tipo di persona giuridica
	 */
	public void setTipo_pers_giur(char value) {
		setTipo_pers_giur(new Character(value));
	}

	/**
	 * codice identificativo del tipo di persona giuridica
	 */
	public void setTipo_pers_giur(Character value) {
		this.tipo_pers_giur = value;
	}

	/**
	 * codice identificativo del tipo di persona giuridica
	 */
	public Character getTipo_pers_giur() {
		return tipo_pers_giur;
	}

	/**
	 * codice identificativo del paese di residenza
	 */
	public void setPaese_res(String value) {
		this.paese_res = value;
	}

	/**
	 * codice identificativo del paese di residenza
	 */
	public String getPaese_res() {
		return paese_res;
	}

	/**
	 * codice identificativo del paese di cittadinanza
	 */
	public void setPaese_citt(String value) {
		this.paese_citt = value;
	}

	/**
	 * codice identificativo del paese di cittadinanza
	 */
	public String getPaese_citt() {
		return paese_citt;
	}

	/**
	 * codice identificativo del tipo del primo documento di riconoscimento
	 */
	public void setTipo_docum1(char value) {
		setTipo_docum1(new Character(value));
	}

	/**
	 * codice identificativo del tipo del primo documento di riconoscimento
	 */
	public void setTipo_docum1(Character value) {
		this.tipo_docum1 = value;
	}

	/**
	 * codice identificativo del tipo del primo documento di riconoscimento
	 */
	public Character getTipo_docum1() {
		return tipo_docum1;
	}

	/**
	 * numero del primo documento di riconoscimento
	 */
	public void setNum_docum1(String value) {
		this.num_docum1 = value;
	}

	/**
	 * numero del primo documento di riconoscimento
	 */
	public String getNum_docum1() {
		return num_docum1;
	}

	/**
	 * autorita' di rilascio del primo documento di riconoscimento
	 */
	public void setAut_ril_docum1(String value) {
		this.aut_ril_docum1 = value;
	}

	/**
	 * autorita' di rilascio del primo documento di riconoscimento
	 */
	public String getAut_ril_docum1() {
		return aut_ril_docum1;
	}

	/**
	 * codice identificativo del tipo del secondo documento di riconoscimento
	 */
	public void setTipo_docum2(char value) {
		setTipo_docum2(new Character(value));
	}

	/**
	 * codice identificativo del tipo del secondo documento di riconoscimento
	 */
	public void setTipo_docum2(Character value) {
		this.tipo_docum2 = value;
	}

	/**
	 * codice identificativo del tipo del secondo documento di riconoscimento
	 */
	public Character getTipo_docum2() {
		return tipo_docum2;
	}

	/**
	 * numero del secondo documento di riconoscimento
	 */
	public void setNum_docum2(String value) {
		this.num_docum2 = value;
	}

	/**
	 * numero del secondo documento di riconoscimento
	 */
	public String getNum_docum2() {
		return num_docum2;
	}

	/**
	 * autorita' di rilascio del secondo documento di riconoscimento
	 */
	public void setAut_ril_docum2(String value) {
		this.aut_ril_docum2 = value;
	}

	/**
	 * autorita' di rilascio del secondo documento di riconoscimento
	 */
	public String getAut_ril_docum2() {
		return aut_ril_docum2;
	}

	/**
	 * codice identificativo del tipo del terzo documento di riconoscimento
	 */
	public void setTipo_docum3(char value) {
		setTipo_docum3(new Character(value));
	}

	/**
	 * codice identificativo del tipo del terzo documento di riconoscimento
	 */
	public void setTipo_docum3(Character value) {
		this.tipo_docum3 = value;
	}

	/**
	 * codice identificativo del tipo del terzo documento di riconoscimento
	 */
	public Character getTipo_docum3() {
		return tipo_docum3;
	}

	/**
	 * numero del terzo documento di riconoscimento
	 */
	public void setNum_docum3(String value) {
		this.num_docum3 = value;
	}

	/**
	 * numero del terzo documento di riconoscimento
	 */
	public String getNum_docum3() {
		return num_docum3;
	}

	/**
	 * autorita' di rilascio del terzo documento di riconoscimento
	 */
	public void setAut_ril_docum3(String value) {
		this.aut_ril_docum3 = value;
	}

	/**
	 * autorita' di rilascio del terzo documento di riconoscimento
	 */
	public String getAut_ril_docum3() {
		return aut_ril_docum3;
	}

	/**
	 * codice identificativo del tipo del quarto documento di riconoscimento
	 */
	public void setTipo_docum4(char value) {
		setTipo_docum4(new Character(value));
	}

	/**
	 * codice identificativo del tipo del quarto documento di riconoscimento
	 */
	public void setTipo_docum4(Character value) {
		this.tipo_docum4 = value;
	}

	/**
	 * codice identificativo del tipo del quarto documento di riconoscimento
	 */
	public Character getTipo_docum4() {
		return tipo_docum4;
	}

	/**
	 * numero del quarto documento di riconoscimento
	 */
	public void setNum_docum4(String value) {
		this.num_docum4 = value;
	}

	/**
	 * numero del quarto documento di riconoscimento
	 */
	public String getNum_docum4() {
		return num_docum4;
	}

	/**
	 * autorita' di rilascio del quarto documento di riconoscimento
	 */
	public void setAut_ril_docum4(String value) {
		this.aut_ril_docum4 = value;
	}

	/**
	 * autorita' di rilascio del quarto documento di riconoscimento
	 */
	public String getAut_ril_docum4() {
		return aut_ril_docum4;
	}

	/**
	 * per l'utente-biblioteca, codice identificativo della biblioteca
	 * corrispondente
	 */
	public void setCod_bib(String value) {
		this.cod_bib = value;
	}

	/**
	 * per l'utente-biblioteca, codice identificativo della biblioteca
	 * corrispondente
	 */
	public String getCod_bib() {
		return cod_bib;
	}

	/**
	 * provincia di domicilio
	 */
	public void setProv_dom(String value) {
		this.prov_dom = value;
	}

	/**
	 * provincia di domicilio
	 */
	public String getProv_dom() {
		return prov_dom;
	}

	/**
	 * provincia di residenza
	 */
	public void setProv_res(String value) {
		this.prov_res = value;
	}

	/**
	 * provincia di residenza
	 */
	public String getProv_res() {
		return prov_res;
	}

	/**
	 * per l'utente-biblioteca, codice identificativo del polo della biblioteca
	 * corrispondente
	 */
	public void setCod_polo_bib(String value) {
		this.cod_polo_bib = value;
	}

	/**
	 * per l'utente-biblioteca, codice identificativo del polo della biblioteca
	 * corrispondente
	 */
	public String getCod_polo_bib() {
		return cod_polo_bib;
	}

	/**
	 * indicatore della necessita' di allineamento
	 */
	public void setAllinea(char value) {
		setAllinea(new Character(value));
	}

	/**
	 * indicatore della necessita' di allineamento
	 */
	public void setAllinea(Character value) {
		this.allinea = value;
	}

	/**
	 * indicatore della necessita' di allineamento
	 */
	public Character getAllinea() {
		return allinea;
	}

	/**
	 * chiave nome utente
	 */
	public void setChiave_ute(String value) {
		this.chiave_ute = value;
	}

	/**
	 * chiave nome utente
	 */
	public String getChiave_ute() {
		return chiave_ute;
	}

	public void setCd_bib(
			it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setTrl_diritti_utente(java.util.Set value) {
		this.Trl_diritti_utente = value;
	}

	public java.util.Set getTrl_diritti_utente() {
		return Trl_diritti_utente;
	}

	public void setTrl_materie_utenti(java.util.Set value) {
		this.Trl_materie_utenti = value;
	}

	public java.util.Set getTrl_materie_utenti() {
		return Trl_materie_utenti;
	}

	public void setTrl_utenti_biblioteca(java.util.Set value) {
		this.Trl_utenti_biblioteca = value;
	}

	public java.util.Set getTrl_utenti_biblioteca() {
		return Trl_utenti_biblioteca;
	}

	public void setTbl_documenti_lettori(java.util.Set value) {
		this.Tbl_documenti_lettori = value;
	}

	public java.util.Set getTbl_documenti_lettori() {
		return Tbl_documenti_lettori;
	}

	public String toString() {
		return String.valueOf(getId_utenti());
	}

	// Modifica  almaviva del 06/2009
	/**
	 * Timestamp di variazione password
	 */

	public void setTs_var_password(java.sql.Timestamp value) {
		this.ts_var_password = value;
	}

	//
	public java.sql.Timestamp getTs_var_password() {
		return ts_var_password;
	}

	/**
	 * Timestamp ultimo accesso
	 */

	public void setLast_access(java.sql.Timestamp value) {
		this.last_access = value;
	}

	//
	public java.sql.Timestamp getLast_access() {
		return last_access;
	}

	// Flg indica il cambiamento della password
	public void setChange_password(char change_password) {
		this.change_password = change_password;
	}

	public char getChange_password() {
		return change_password;
	}

	public void setTidx_vector(int value) {
		setTidx_vector(new Integer(value));
	}

	public String getCodice_anagrafe() {
		return codice_anagrafe;
	}

	public void setCodice_anagrafe(String codice_anagrafe) {
		this.codice_anagrafe = codice_anagrafe;
	}

	public void setProfessione(char value) {
		setProfessione(new Character(value));
	}

	public Character getProfessione() {
		return professione;
	}

	public void setProfessione(Character professione) {
		this.professione = professione;
	}

	public void setTit_studio(char value) {
		setTit_studio(new Character(value));
	}

	public Character getTit_studio() {
		return tit_studio;
	}

	public void setTit_studio(Character tit_studio) {
		this.tit_studio = tit_studio;
	}

	public String getCd_bib_iscrizione() {
		return cd_bib_iscrizione;
	}

	public void setCd_bib_iscrizione(String cdBibIscrizione) {
		cd_bib_iscrizione = cdBibIscrizione;
	}

	public String getInd_posta_elettr2() {
		return ind_posta_elettr2;
}

	public void setInd_posta_elettr2(String ind_posta_elettr2) {
		this.ind_posta_elettr2 = ind_posta_elettr2;
	}

	public Set<Tbl_prenotazione_posto> getPrenotazioni_posti() {
		return prenotazioni_posti;
	}

	public void setPrenotazioni_posti(Set<Tbl_prenotazione_posto> prenotazioni_posti) {
		this.prenotazioni_posti = prenotazioni_posti;
	}

	public Tbl_biblioteca_ill getBiblioteca_ill() {
		return biblioteca_ill;
	}

	public void setBiblioteca_ill(Tbl_biblioteca_ill biblioteca_ill) {
		this.biblioteca_ill = biblioteca_ill;
	}

	public Character getCd_tipo_ute() {
		return cd_tipo_ute;
	}

	public void setCd_tipo_ute(Character cd_tipo_ute) {
		this.cd_tipo_ute = cd_tipo_ute;
	}

}
