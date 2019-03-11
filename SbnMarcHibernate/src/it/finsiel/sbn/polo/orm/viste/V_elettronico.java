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
 * Classe V_elettronico ATTENZIONE! QUESTA CLASSE E' STATA GENERATA
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
 * @version 18/6/2014
 */
public class V_elettronico extends Tb_titolo {

	private static final long serialVersionUID = 8107848499889443328L;

	// Attributi
	private String CD_QUALITA;
	private String TP_RISORSA;
	private String CD_ORIGINE;
	private String CD_RIFORMATTAZIONE;
	private String CD_COLORE;
	private String CD_FORMATO_FILE;
	private String CD_LIVELLO_E;
	private String CD_COMPRESSIONE;
	private String CD_DIMENSIONE;
	private String CD_SUONO;
	private String CD_BIT_IMMAGINE;
	private String CD_DESIGNAZIONE;

	public void setTP_RISORSA(String value) {
		this.TP_RISORSA = value;
		this.settaParametro(KeyParameter.XXXtp_risorsa, value);
	}

	public String getTP_RISORSA() {
		return TP_RISORSA;
	}

	public void setCD_DESIGNAZIONE(String value) {
		this.CD_DESIGNAZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_designazione, value);
	}

	public String getCD_DESIGNAZIONE() {
		return CD_DESIGNAZIONE;
	}

	public void setCD_COLORE(String value) {
		this.CD_COLORE = value;
		this.settaParametro(KeyParameter.XXXcd_colore, value);
	}

	public String getCD_COLORE() {
		return CD_COLORE;
	}

	public void setCD_DIMENSIONE(String value) {
		this.CD_DIMENSIONE = value;
		this.settaParametro(KeyParameter.XXXcd_dimensione, value);
	}

	public String getCD_DIMENSIONE() {
		return CD_DIMENSIONE;
	}

	public void setCD_SUONO(String value) {
		this.CD_SUONO = value;
		this.settaParametro(KeyParameter.XXXcd_suono, value);
	}

	public String getCD_SUONO() {
		return CD_SUONO;
	}

	public void setCD_BIT_IMMAGINE(String value) {
		this.CD_BIT_IMMAGINE = value;
		this.settaParametro(KeyParameter.XXXcd_bit_immagine, value);
	}

	public String getCD_BIT_IMMAGINE() {
		return CD_BIT_IMMAGINE;
	}

	public void setCD_FORMATO_FILE(String value) {
		this.CD_FORMATO_FILE = value;
		this.settaParametro(KeyParameter.XXXcd_formato_file, value);
	}

	public String getCD_FORMATO_FILE() {
		return CD_FORMATO_FILE;
	}

	public void setCD_QUALITA(String value) {
		this.CD_QUALITA = value;
		this.settaParametro(KeyParameter.XXXcd_qualita, value);
	}

	public String getCD_QUALITA() {
		return CD_QUALITA;
	}

	public void setCD_ORIGINE(String value) {
		this.CD_ORIGINE = value;
		this.settaParametro(KeyParameter.XXXcd_origine, value);
	}

	public String getCD_ORIGINE() {
		return CD_ORIGINE;
	}

	public void setCD_LIVELLO_E(String value) {
		this.CD_LIVELLO_E = value;
		this.settaParametro(KeyParameter.XXXcd_livello_e, value);
	}

	public String getCD_LIVELLO_E() {
		return CD_LIVELLO_E;
	}

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String.valueOf(getBID())));
	}

	public String getCD_COMPRESSIONE() {
		return CD_COMPRESSIONE;
	}

	public void setCD_COMPRESSIONE(String value) {
		CD_COMPRESSIONE = value;
		this.settaParametro(KeyParameter.XXXcd_compressione, value);
	}

	public String getCD_RIFORMATTAZIONE() {
		return CD_RIFORMATTAZIONE;
	}

	public void setCD_RIFORMATTAZIONE(String value) {
		CD_RIFORMATTAZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_riformattazione, value);
	}


}
