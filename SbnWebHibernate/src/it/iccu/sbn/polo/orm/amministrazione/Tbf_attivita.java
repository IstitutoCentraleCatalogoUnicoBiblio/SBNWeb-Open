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
package it.iccu.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * CODICI DELLE FUNZIONI GESTITE DALL'APPLICATIVO (TVFDTB CON COD_TAB='69 ')
 */
/**
 * ORM-Persistable Class
 */
public class Tbf_attivita implements Serializable {

	private static final long serialVersionUID = 1593556204904885400L;

	public Tbf_attivita() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbf_attivita))
			return false;
		Tbf_attivita tbf_attivita = (Tbf_attivita)aObj;
		if ((getCd_attivita() != null && !getCd_attivita().equals(tbf_attivita.getCd_attivita())) || (getCd_attivita() == null && tbf_attivita.getCd_attivita() != null))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCd_attivita() == null ? 0 : getCd_attivita().hashCode());
		return hashcode;
	}

	private String cd_attivita;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita_sbnmarc id_attivita_sbnmarc;

	private String flg_menu;

	private Integer prg_ordimanento;

	private String cd_funzione_parent;

	private char fl_auto_abilita_figli;

	private char fl_assegna_a_cds;

	private String url_servizio;

	private Character fl_titolo;

	private Character fl_autore;

	private Character fl_marca;

	private Character fl_luogo;

	private Character fl_soggetto;

	private Character fl_classe;

	private String liv_autorita_da;

	private String liv_autorita_a;

	private Character gestione_in_indice;

	private Character gestione_in_polo;

	private Character natura_a;

	private Character natura_b;

	private Character natura_c;

	private Character natura_d;

	private Character natura_m;

	private Character natura_n;

	private Character natura_p;

	private Character natura_s;

	private Character natura_t;

	private Character natura_w;

	private Character natura_r;

	private Character natura_x;

	private Character forma_accettata;

	private Character forma_rinvio;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private String classe_java_sbnmarc;

	private java.util.Set Trf_funzioni_denominazioni = new java.util.HashSet();

	private java.util.Set Trf_attivita_polo = new java.util.HashSet();

	private java.util.Set Trf_profilo_biblioteca = new java.util.HashSet();

	private java.util.Set Tbq_batch_attivabili = new java.util.HashSet();

	private java.util.Set Tbf_batch_servizi = new java.util.HashSet();

	public void setCd_attivita(String value) {
		this.cd_attivita = value;
	}

	public String getCd_attivita() {
		return cd_attivita;
	}

	public String getORMID() {
		return getCd_attivita();
	}

	/**
	 * Indicatore di funzione da attivare da menu'
	 */
	public void setFlg_menu(String value) {
		this.flg_menu = value;
	}

	/**
	 * Indicatore di funzione da attivare da menu'
	 */
	public String getFlg_menu() {
		return flg_menu;
	}

	/**
	 * Ordinamento nel menu relativo di parent
	 */
	public void setPrg_ordimanento(int value) {
		setPrg_ordimanento(new Integer(value));
	}

	/**
	 * Ordinamento nel menu relativo di parent
	 */
	public void setPrg_ordimanento(Integer value) {
		this.prg_ordimanento = value;
	}

	/**
	 * Ordinamento nel menu relativo di parent
	 */
	public Integer getPrg_ordimanento() {
		return prg_ordimanento;
	}

	/**
	 * Codice della funzione parent, significativo solo se flag_menu='S'
	 */
	public void setCd_funzione_parent(String value) {
		this.cd_funzione_parent = value;
	}

	/**
	 * Codice della funzione parent, significativo solo se flag_menu='S'
	 */
	public String getCd_funzione_parent() {
		return cd_funzione_parent;
	}

	/**
	 * Se 'S'le funzioni di livello inferiore vengono abilitate all’atto dell’abilitazione della funzione in oggetto
	 */
	public void setFl_auto_abilita_figli(char value) {
		this.fl_auto_abilita_figli = value;
	}

	/**
	 * Se 'S'le funzioni di livello inferiore vengono abilitate all’atto dell’abilitazione della funzione in oggetto
	 */
	public char getFl_auto_abilita_figli() {
		return fl_auto_abilita_figli;
	}

	/**
	 * Se 'N'la funzione non è demandabile a cds, se 'E' è demandabile in modo esclusivo, se 'C' è demandabile in modo condiviso
	 */
	public void setFl_assegna_a_cds(char value) {
		this.fl_assegna_a_cds = value;
	}

	/**
	 * Se 'N'la funzione non è demandabile a cds, se 'E' è demandabile in modo esclusivo, se 'C' è demandabile in modo condiviso
	 */
	public char getFl_assegna_a_cds() {
		return fl_assegna_a_cds;
	}

	/**
	 * URL dell'action che svolge il servizio corrispondente alla richiesta
	 */
	public void setUrl_servizio(String value) {
		this.url_servizio = value;
	}

	/**
	 * URL dell'action che svolge il servizio corrispondente alla richiesta
	 */
	public String getUrl_servizio() {
		return url_servizio;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un titolo
	 */
	public void setFl_titolo(char value) {
		setFl_titolo(new Character(value));
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un titolo
	 */
	public void setFl_titolo(Character value) {
		this.fl_titolo = value;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un titolo
	 */
	public Character getFl_titolo() {
		return fl_titolo;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un autore
	 */
	public void setFl_autore(char value) {
		setFl_autore(new Character(value));
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un autore
	 */
	public void setFl_autore(Character value) {
		this.fl_autore = value;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un autore
	 */
	public Character getFl_autore() {
		return fl_autore;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di una marca
	 */
	public void setFl_marca(char value) {
		setFl_marca(new Character(value));
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di una marca
	 */
	public void setFl_marca(Character value) {
		this.fl_marca = value;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di una marca
	 */
	public Character getFl_marca() {
		return fl_marca;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un luogo
	 */
	public void setFl_luogo(char value) {
		setFl_luogo(new Character(value));
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un luogo
	 */
	public void setFl_luogo(Character value) {
		this.fl_luogo = value;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un luogo
	 */
	public Character getFl_luogo() {
		return fl_luogo;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un soggetto
	 */
	public void setFl_soggetto(char value) {
		setFl_soggetto(new Character(value));
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un soggetto
	 */
	public void setFl_soggetto(Character value) {
		this.fl_soggetto = value;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un soggetto
	 */
	public Character getFl_soggetto() {
		return fl_soggetto;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un classe
	 */
	public void setFl_classe(char value) {
		setFl_classe(new Character(value));
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un classe
	 */
	public void setFl_classe(Character value) {
		this.fl_classe = value;
	}

	/**
	 * Indicatore di funzione valida per titolo: se vale 'S' la funzione deve essere presentata nel vai a di un classe
	 */
	public Character getFl_classe() {
		return fl_classe;
	}

	/**
	 * Indica il livello di autorità minimo che deve avere l'oggetto corrente perchè la funzione venga presentata nel menu 'Vai a'
	 */
	public void setLiv_autorita_da(String value) {
		this.liv_autorita_da = value;
	}

	/**
	 * Indica il livello di autorità minimo che deve avere l'oggetto corrente perchè la funzione venga presentata nel menu 'Vai a'
	 */
	public String getLiv_autorita_da() {
		return liv_autorita_da;
	}

	/**
	 * Indica il livello di autorità massimo che deve avere l'oggetto corrente perchè la funzione venga presentata nel menu 'Vai a'
	 */
	public void setLiv_autorita_a(String value) {
		this.liv_autorita_a = value;
	}

	/**
	 * Indica il livello di autorità massimo che deve avere l'oggetto corrente perchè la funzione venga presentata nel menu 'Vai a'
	 */
	public String getLiv_autorita_a() {
		return liv_autorita_a;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' solo per oggetti gestiti in Indice, se 'N' solo per oggetti non gestiti in Indice
	 */
	public void setGestione_in_indice(char value) {
		setGestione_in_indice(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' solo per oggetti gestiti in Indice, se 'N' solo per oggetti non gestiti in Indice
	 */
	public void setGestione_in_indice(Character value) {
		this.gestione_in_indice = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' solo per oggetti gestiti in Indice, se 'N' solo per oggetti non gestiti in Indice
	 */
	public Character getGestione_in_indice() {
		return gestione_in_indice;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' solo per oggetti gestiti in polo, se 'N' solo per oggetti non gestiti in polo
	 */
	public void setGestione_in_polo(char value) {
		setGestione_in_polo(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' solo per oggetti gestiti in polo, se 'N' solo per oggetti non gestiti in polo
	 */
	public void setGestione_in_polo(Character value) {
		this.gestione_in_polo = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' solo per oggetti gestiti in polo, se 'N' solo per oggetti non gestiti in polo
	 */
	public Character getGestione_in_polo() {
		return gestione_in_polo;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura A, se 'N' non deve essere presentata per titoli di natura A
	 */
	public void setNatura_a(char value) {
		setNatura_a(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura A, se 'N' non deve essere presentata per titoli di natura A
	 */
	public void setNatura_a(Character value) {
		this.natura_a = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura A, se 'N' non deve essere presentata per titoli di natura A
	 */
	public Character getNatura_a() {
		return natura_a;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura B, se 'N' non deve essere presentata per titoli di natura B
	 */
	public void setNatura_b(char value) {
		setNatura_b(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura B, se 'N' non deve essere presentata per titoli di natura B
	 */
	public void setNatura_b(Character value) {
		this.natura_b = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura B, se 'N' non deve essere presentata per titoli di natura B
	 */
	public Character getNatura_b() {
		return natura_b;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura C, se 'N' non deve essere presentata per titoli di natura C
	 */
	public void setNatura_c(char value) {
		setNatura_c(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura C, se 'N' non deve essere presentata per titoli di natura C
	 */
	public void setNatura_c(Character value) {
		this.natura_c = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura C, se 'N' non deve essere presentata per titoli di natura C
	 */
	public Character getNatura_c() {
		return natura_c;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura D, se 'N' non deve essere presentata per titoli di natura D
	 */
	public void setNatura_d(char value) {
		setNatura_d(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura D, se 'N' non deve essere presentata per titoli di natura D
	 */
	public void setNatura_d(Character value) {
		this.natura_d = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura D, se 'N' non deve essere presentata per titoli di natura D
	 */
	public Character getNatura_d() {
		return natura_d;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura M, se 'N' non deve essere presentata per titoli di natura M
	 */
	public void setNatura_m(char value) {
		setNatura_m(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura M, se 'N' non deve essere presentata per titoli di natura M
	 */
	public void setNatura_m(Character value) {
		this.natura_m = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura M, se 'N' non deve essere presentata per titoli di natura M
	 */
	public Character getNatura_m() {
		return natura_m;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura N, se 'N' non deve essere presentata per titoli di natura N
	 */
	public void setNatura_n(char value) {
		setNatura_n(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura N, se 'N' non deve essere presentata per titoli di natura N
	 */
	public void setNatura_n(Character value) {
		this.natura_n = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura N, se 'N' non deve essere presentata per titoli di natura N
	 */
	public Character getNatura_n() {
		return natura_n;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura P, se 'N' non deve essere presentata per titoli di natura P
	 */
	public void setNatura_p(char value) {
		setNatura_p(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura P, se 'N' non deve essere presentata per titoli di natura P
	 */
	public void setNatura_p(Character value) {
		this.natura_p = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura P, se 'N' non deve essere presentata per titoli di natura P
	 */
	public Character getNatura_p() {
		return natura_p;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura S, se 'N' non deve essere presentata per titoli di natura S
	 */
	public void setNatura_s(char value) {
		setNatura_s(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura S, se 'N' non deve essere presentata per titoli di natura S
	 */
	public void setNatura_s(Character value) {
		this.natura_s = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura S, se 'N' non deve essere presentata per titoli di natura S
	 */
	public Character getNatura_s() {
		return natura_s;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura T, se 'N' non deve essere presentata per titoli di natura T
	 */
	public void setNatura_t(char value) {
		setNatura_t(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura T, se 'N' non deve essere presentata per titoli di natura T
	 */
	public void setNatura_t(Character value) {
		this.natura_t = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura T, se 'N' non deve essere presentata per titoli di natura T
	 */
	public Character getNatura_t() {
		return natura_t;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura W, se 'N' non deve essere presentata per titoli di natura W
	 */
	public void setNatura_w(char value) {
		setNatura_w(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura W, se 'N' non deve essere presentata per titoli di natura W
	 */
	public void setNatura_w(Character value) {
		this.natura_w = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura W, se 'N' non deve essere presentata per titoli di natura W
	 */
	public Character getNatura_w() {
		return natura_w;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura R, se 'N' non deve essere presentata per titoli di natura R
	 */
	public void setNatura_r(char value) {
		setNatura_r(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura R, se 'N' non deve essere presentata per titoli di natura R
	 */
	public void setNatura_r(Character value) {
		this.natura_r = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura R, se 'N' non deve essere presentata per titoli di natura R
	 */
	public Character getNatura_r() {
		return natura_r;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura X, se 'N' non deve essere presentata per titoli di natura X
	 */
	public void setNatura_x(char value) {
		setNatura_x(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura X, se 'N' non deve essere presentata per titoli di natura X
	 */
	public void setNatura_x(Character value) {
		this.natura_x = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per titoli di natura X, se 'N' non deve essere presentata per titoli di natura X
	 */
	public Character getNatura_x() {
		return natura_x;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per autori o luoghi in forma accettata, se 'N' non deve essere presentata per autori o luoghi in forma accettata
	 */
	public void setForma_accettata(char value) {
		setForma_accettata(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per autori o luoghi in forma accettata, se 'N' non deve essere presentata per autori o luoghi in forma accettata
	 */
	public void setForma_accettata(Character value) {
		this.forma_accettata = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per autori o luoghi in forma accettata, se 'N' non deve essere presentata per autori o luoghi in forma accettata
	 */
	public Character getForma_accettata() {
		return forma_accettata;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per autori o luoghi in forma di rinvio, se 'N' non deve essere presentata per autori o luoghi in forma di rinvio7
	 */
	public void setForma_rinvio(char value) {
		setForma_rinvio(new Character(value));
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per autori o luoghi in forma di rinvio, se 'N' non deve essere presentata per autori o luoghi in forma di rinvio7
	 */
	public void setForma_rinvio(Character value) {
		this.forma_rinvio = value;
	}

	/**
	 * Se 'S' la funzione deve essere presentata nel menu 'Vai a' per autori o luoghi in forma di rinvio, se 'N' non deve essere presentata per autori o luoghi in forma di rinvio7
	 */
	public Character getForma_rinvio() {
		return forma_rinvio;
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
	 * Nome della classe java per l'attività in SBNMARC
	 */
	public void setClasse_java_sbnmarc(String value) {
		this.classe_java_sbnmarc = value;
	}

	/**
	 * Nome della classe java per l'attività in SBNMARC
	 */
	public String getClasse_java_sbnmarc() {
		return classe_java_sbnmarc;
	}

	public void setId_attivita_sbnmarc(it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita_sbnmarc value) {
		this.id_attivita_sbnmarc = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita_sbnmarc getId_attivita_sbnmarc() {
		return id_attivita_sbnmarc;
	}

	public void setTrf_funzioni_denominazioni(java.util.Set value) {
		this.Trf_funzioni_denominazioni = value;
	}

	public java.util.Set getTrf_funzioni_denominazioni() {
		return Trf_funzioni_denominazioni;
	}


	public void setTrf_attivita_polo(java.util.Set value) {
		this.Trf_attivita_polo = value;
	}

	public java.util.Set getTrf_attivita_polo() {
		return Trf_attivita_polo;
	}


	public void setTrf_profilo_biblioteca(java.util.Set value) {
		this.Trf_profilo_biblioteca = value;
	}

	public java.util.Set getTrf_profilo_biblioteca() {
		return Trf_profilo_biblioteca;
	}


	public void setTbq_batch_attivabili(java.util.Set value) {
		this.Tbq_batch_attivabili = value;
	}

	public java.util.Set getTbq_batch_attivabili() {
		return Tbq_batch_attivabili;
	}


	public String toString() {
		return String.valueOf(getCd_attivita());
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

	public java.util.Set getTbf_batch_servizi() {
		return Tbf_batch_servizi;
	}

	public void setTbf_batch_servizi(java.util.Set tbf_batch_servizi) {
		Tbf_batch_servizi = tbf_batch_servizi;
	}


}
