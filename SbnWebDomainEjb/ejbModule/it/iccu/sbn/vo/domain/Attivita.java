/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.iccu.sbn.vo.domain;

public class Attivita {
	private String cd_attivita;

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

	public void setCd_attivita(String value) {
		this.cd_attivita = value;
	}

	public String getCd_attivita() {
		return cd_attivita;
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
}
