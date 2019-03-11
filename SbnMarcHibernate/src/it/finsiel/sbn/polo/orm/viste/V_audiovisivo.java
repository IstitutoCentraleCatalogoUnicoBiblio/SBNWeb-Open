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
package it.finsiel.sbn.polo.orm.viste;

import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.Tb_titolo;

/**
 * Classe V_audiovisivo ATTENZIONE! QUESTA CLASSE E' STATA GENERATA
 * AUTOMATICAMENTE. NESSUNA MODIFICA DEVE ESSERE APPORTATA MANUALMENTE, PERCHE'
 * SARA' PERSA IN FUTURO. OGNI AGGIUNTA MANUALE NON E' ATTUALMENTE POSSIBILE.
 *
 * <p>
 * Classe che contiene gli attributi estratti dalla tavola del DB
 *
 * </p>
 *
 * @author Akros Informatica s.r.l.
 * @author generatore automatico di Ragazzini Taymer
 * @version 6/5/2014
 */
public class V_audiovisivo extends Tb_titolo {

	private static final long serialVersionUID = 6168074679943449593L;

	// Attributi
	private String TP_STAMPA_FILM;
	private String TP_TAGLIO;
	private String CD_FORMA;
	private String DURATA;
	private String CD_LARG_NASTRO;
	private String CD_BROADCAST;
	private String CD_ELEMENTI;
	private String CD_VELOCITA;
	private String CD_DIMENSIONE_DISCO;
	private String CD_CONFIGURAZIONE;
	private String CD_TECNICA_REGIS;
	private String TP_SUONO;
	private String TP_FORMATO_VIDEO;
	private Integer DT_ISPEZIONE;
	private String TP_FORMATO_FILM;
	private String CD_MATERIALE_BASE;
	private Integer LUNGHEZZA;
	private String CD_DIMENSIONE;
	private String TP_DISCO;
	private String CD_RIPRODUZIONE;
	private String CD_FORMA_VIDEO;
	private String CD_SUONO;
	private String CD_LIVELLO_A;
	private String CD_CATEG_COLORE;
	private String CD_SUPPORTO_SECOND;
	private String TP_MEDIA_SUONO;
	private String TP_SUONO_DISCO;
	private String TP_MATER_AUDIOVIS;
	private String CD_MATER_ACCOMP_1;
	private String CD_MATER_ACCOMP_2;
	private String CD_MATER_ACCOMP_3;
	private String CD_MATER_ACCOMP_4;
	private String CD_MATER_ACCOMP_5;
	private String CD_MATER_ACCOMP_6;
	private String CD_POLARITA;
	private String TP_MATERIALE_DISCO;
	private String CD_PELLICOLA;
	private String CD_MAT_ACCOMP_1;
	private String CD_MAT_ACCOMP_2;
	private String CD_MAT_ACCOMP_3;
	private String CD_MAT_ACCOMP_4;
	private String CD_TECNICA;
	private String CD_PISTA;
	private String CD_COLORE;
	private String CD_FORMA_REGIST;
	private String TP_GENERAZIONE;

	public void setTP_MATER_AUDIOVIS(String value) {
		this.TP_MATER_AUDIOVIS = value;
		this.settaParametro(KeyParameter.XXXtp_mater_audiovis, value);
	}

	public String getTP_MATER_AUDIOVIS() {
		return TP_MATER_AUDIOVIS;
	}

	public void setCD_COLORE(String value) {
		this.CD_COLORE = value;
		this.settaParametro(KeyParameter.XXXcd_colore, value);
	}

	public String getCD_COLORE() {
		return CD_COLORE;
	}

	public void setCD_SUONO(String value) {
		this.CD_SUONO = value;
		this.settaParametro(KeyParameter.XXXcd_suono, value);
	}

	public String getCD_SUONO() {
		return CD_SUONO;
	}

	public void setTP_MEDIA_SUONO(String value) {
		this.TP_MEDIA_SUONO = value;
		this.settaParametro(KeyParameter.XXXtp_media_suono, value);
	}

	public String getTP_MEDIA_SUONO() {
		return TP_MEDIA_SUONO;
	}

	public void setCD_DIMENSIONE_DISCO(String value) {
		this.CD_DIMENSIONE_DISCO = value;
		this.settaParametro(KeyParameter.XXXcd_dimensione_disco, value);
	}

	public String getCD_DIMENSIONE_DISCO() {
		return CD_DIMENSIONE_DISCO;
	}

	public void setCD_FORMA_VIDEO(String value) {
		this.CD_FORMA_VIDEO = value;
		this.settaParametro(KeyParameter.XXXcd_forma_video, value);
	}

	public String getCD_FORMA_VIDEO() {
		return CD_FORMA_VIDEO;
	}

	public void setCD_TECNICA(String value) {
		this.CD_TECNICA = value;
		this.settaParametro(KeyParameter.XXXcd_tecnica, value);
	}

	public String getCD_TECNICA() {
		return CD_TECNICA;
	}

	public void setTP_FORMATO_FILM(String value) {
		this.TP_FORMATO_FILM = value;
		this.settaParametro(KeyParameter.XXXtp_formato_film, value);
	}

	public String getTP_FORMATO_FILM() {
		return TP_FORMATO_FILM;
	}

	public void setCD_FORMA_REGIST(String value) {
		this.CD_FORMA_REGIST = value;
		this.settaParametro(KeyParameter.XXXcd_forma_regist, value);
	}

	public String getCD_FORMA_REGIST() {
		return CD_FORMA_REGIST;
	}

	public void setTP_FORMATO_VIDEO(String value) {
		this.TP_FORMATO_VIDEO = value;
		this.settaParametro(KeyParameter.XXXtp_formato_video, value);
	}

	public String getTP_FORMATO_VIDEO() {
		return TP_FORMATO_VIDEO;
	}

	public void setCD_MATERIALE_BASE(String value) {
		this.CD_MATERIALE_BASE = value;
		this.settaParametro(KeyParameter.XXXcd_materiale_base, value);
	}

	public String getCD_MATERIALE_BASE() {
		return CD_MATERIALE_BASE;
	}

	public void setCD_SUPPORTO_SECOND(String value) {
		this.CD_SUPPORTO_SECOND = value;
		this.settaParametro(KeyParameter.XXXcd_supporto_second, value);
	}

	public String getCD_SUPPORTO_SECOND() {
		return CD_SUPPORTO_SECOND;
	}

	public void setCD_BROADCAST(String value) {
		this.CD_BROADCAST = value;
		this.settaParametro(KeyParameter.XXXcd_broadcast, value);
	}

	public String getCD_BROADCAST() {
		return CD_BROADCAST;
	}

	public void setTP_GENERAZIONE(String value) {
		this.TP_GENERAZIONE = value;
		this.settaParametro(KeyParameter.XXXtp_generazione, value);
	}

	public String getTP_GENERAZIONE() {
		return TP_GENERAZIONE;
	}

	public void setCD_ELEMENTI(String value) {
		this.CD_ELEMENTI = value;
		this.settaParametro(KeyParameter.XXXcd_elementi, value);
	}

	public String getCD_ELEMENTI() {
		return CD_ELEMENTI;
	}

	public void setCD_CATEG_COLORE(String value) {
		this.CD_CATEG_COLORE = value;
		this.settaParametro(KeyParameter.XXXcd_categ_colore, value);
	}

	public String getCD_CATEG_COLORE() {
		return CD_CATEG_COLORE;
	}

	public void setCD_POLARITA(String value) {
		this.CD_POLARITA = value;
		this.settaParametro(KeyParameter.XXXcd_polarita, value);
	}

	public String getCD_POLARITA() {
		return CD_POLARITA;
	}

	public void setCD_PELLICOLA(String value) {
		this.CD_PELLICOLA = value;
		this.settaParametro(KeyParameter.XXXcd_pellicola, value);
	}

	public String getCD_PELLICOLA() {
		return CD_PELLICOLA;
	}

	public void setTP_SUONO_DISCO(String value) {
		this.TP_SUONO_DISCO = value;
		this.settaParametro(KeyParameter.XXXtp_suono_disco, value);
	}

	public String getTP_SUONO_DISCO() {
		return TP_SUONO_DISCO;
	}

	public void setTP_STAMPA_FILM(String value) {
		this.TP_STAMPA_FILM = value;
		this.settaParametro(KeyParameter.XXXtp_stampa_film, value);
	}

	public String getTP_STAMPA_FILM() {
		return TP_STAMPA_FILM;
	}

	public String getCD_LIVELLO_A() {
		return CD_LIVELLO_A;
	}

	public void setCD_LIVELLO_A(String value) {
		CD_LIVELLO_A = value;
		this.settaParametro(KeyParameter.XXXcd_livello_a, value);
	}

	public String getCD_MAT_ACCOMP_1() {
		return CD_MAT_ACCOMP_1;
	}

	public void setCD_MAT_ACCOMP_1(String value) {
		CD_MAT_ACCOMP_1 = value;
		this.settaParametro(KeyParameter.XXXcd_mat_accomp_1, value);
	}

	public String getCD_MAT_ACCOMP_2() {
		return CD_MAT_ACCOMP_2;
	}

	public void setCD_MAT_ACCOMP_2(String value) {
		CD_MAT_ACCOMP_2 = value;
		this.settaParametro(KeyParameter.XXXcd_mat_accomp_2, value);
	}

	public String getCD_MAT_ACCOMP_3() {
		return CD_MAT_ACCOMP_3;
	}

	public void setCD_MAT_ACCOMP_3(String value) {
		CD_MAT_ACCOMP_3 = value;
		this.settaParametro(KeyParameter.XXXcd_mat_accomp_3, value);
	}

	public String getCD_MAT_ACCOMP_4() {
		return CD_MAT_ACCOMP_4;
	}

	public void setCD_MAT_ACCOMP_4(String value) {
		CD_MAT_ACCOMP_4 = value;
		this.settaParametro(KeyParameter.XXXcd_mat_accomp_4, value);
	}

	public Integer getDT_ISPEZIONE() {
		return DT_ISPEZIONE;
	}

	public void setDT_ISPEZIONE(Integer value) {
		DT_ISPEZIONE = value;
		this.settaParametro(KeyParameter.XXXdt_ispezione, value);
	}

	public Integer getLUNGHEZZA() {
		return LUNGHEZZA;
	}

	public void setLUNGHEZZA(Integer value) {
		LUNGHEZZA = value;
		this.settaParametro(KeyParameter.XXXlunghezza, value);
	}

	public void setCD_FORMA(String value) {
		this.CD_FORMA = value;
		this.settaParametro(KeyParameter.XXXcd_forma, value);
	}

	public String getCD_FORMA() {
		return CD_FORMA;
	}

	public void setCD_VELOCITA(String value) {
		this.CD_VELOCITA = value;
		this.settaParametro(KeyParameter.XXXcd_velocita, value);
	}

	public String getCD_VELOCITA() {
		return CD_VELOCITA;
	}

	public void setTP_SUONO(String value) {
		this.TP_SUONO = value;
		this.settaParametro(KeyParameter.XXXtp_suono, value);
	}

	public String getTP_SUONO() {
		return TP_SUONO;
	}

	public void setCD_PISTA(String value) {
		this.CD_PISTA = value;
		this.settaParametro(KeyParameter.XXXcd_pista, value);
	}

	public String getCD_PISTA() {
		return CD_PISTA;
	}

	public void setCD_DIMENSIONE(String value) {
		this.CD_DIMENSIONE = value;
		this.settaParametro(KeyParameter.XXXcd_dimensione, value);
	}

	public String getCD_DIMENSIONE() {
		return CD_DIMENSIONE;
	}

	public void setCD_LARG_NASTRO(String value) {
		this.CD_LARG_NASTRO = value;
		this.settaParametro(KeyParameter.XXXcd_larg_nastro, value);
	}

	public String getCD_LARG_NASTRO() {
		return CD_LARG_NASTRO;
	}

	public void setCD_CONFIGURAZIONE(String value) {
		this.CD_CONFIGURAZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_configurazione, value);
	}

	public String getCD_CONFIGURAZIONE() {
		return CD_CONFIGURAZIONE;
	}

	public void setCD_TECNICA_REGIS(String value) {
		this.CD_TECNICA_REGIS = value;
		this.settaParametro(KeyParameter.XXXcd_tecnica_regis, value);
	}

	public String getCD_TECNICA_REGIS() {
		return CD_TECNICA_REGIS;
	}

	public void setCD_RIPRODUZIONE(String value) {
		this.CD_RIPRODUZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_riproduzione, value);
	}

	public String getCD_RIPRODUZIONE() {
		return CD_RIPRODUZIONE;
	}

	public void setTP_DISCO(String value) {
		this.TP_DISCO = value;
		this.settaParametro(KeyParameter.XXXtp_disco, value);
	}

	public String getTP_DISCO() {
		return TP_DISCO;
	}

	public void setTP_TAGLIO(String value) {
		this.TP_TAGLIO = value;
		this.settaParametro(KeyParameter.XXXtp_taglio, value);
	}

	public String getTP_TAGLIO() {
		return TP_TAGLIO;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String
				.valueOf(getBID())));
	}

	public String getCD_MATER_ACCOMP_1() {
		return CD_MATER_ACCOMP_1;
	}

	public void setCD_MATER_ACCOMP_1(String value) {
		CD_MATER_ACCOMP_1 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_1, value);
	}

	public String getCD_MATER_ACCOMP_2() {
		return CD_MATER_ACCOMP_2;
	}

	public void setCD_MATER_ACCOMP_2(String value) {
		CD_MATER_ACCOMP_2 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_2, value);
	}

	public String getCD_MATER_ACCOMP_3() {
		return CD_MATER_ACCOMP_3;
	}

	public void setCD_MATER_ACCOMP_3(String value) {
		CD_MATER_ACCOMP_3 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_3, value);
	}

	public String getCD_MATER_ACCOMP_4() {
		return CD_MATER_ACCOMP_4;
	}

	public void setCD_MATER_ACCOMP_4(String value) {
		CD_MATER_ACCOMP_4 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_4, value);
	}

	public String getCD_MATER_ACCOMP_5() {
		return CD_MATER_ACCOMP_5;
	}

	public void setCD_MATER_ACCOMP_5(String value) {
		CD_MATER_ACCOMP_5 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_5, value);
	}

	public String getCD_MATER_ACCOMP_6() {
		return CD_MATER_ACCOMP_6;
	}

	public void setCD_MATER_ACCOMP_6(String value) {
		CD_MATER_ACCOMP_6 = value;
		this.settaParametro(KeyParameter.XXXcd_mater_accomp_6, value);
	}

	public String getTP_MATERIALE_DISCO() {
		return TP_MATERIALE_DISCO;
	}

	public void setTP_MATERIALE_DISCO(String value) {
		TP_MATERIALE_DISCO = value;
		this.settaParametro(KeyParameter.XXXtp_materiale_disco, value);
	}

	public String getDURATA() {
		return DURATA;
	}

	public void setDURATA(String value) {
		DURATA = value;
		this.settaParametro(KeyParameter.XXXdurata, value);
	}

}
