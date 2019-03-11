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
package it.finsiel.sbn.polo.exception;

import it.finsiel.sbn.polo.exception.util.Errore;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;

import org.apache.log4j.Category;

/**
 * Eccezione utilizzata per segnalare diagnostici di esecuzione.
 *
 * @author Jenny Barboni
 *
 */
public class EccezioneSbnMessage extends Exception{

	private static final long serialVersionUID = 6779984329318071845L;

	static Category log = Category.getInstance("it.finsiel.sbn.polo.exception.EccezioneSbnMessage");

	/** Numero che rappresenta il codice dell'errore occorso
	 * Qualora sia uguale a -1 vuol dire che è occorso un errore non noto
	 */
	int ID = -1;

	/** Descrizione dell'errore */
	String nota = null;

	/** Il messaggio che viene presentato nella toString() */
	String messaggio = null;

	Errore errore = null;

	/** L'eccezione che ha iniziato il problema */
	private Exception exception;

	/** Costruttore codice errore e eccezione generante*/
	public EccezioneSbnMessage (int errorID, Exception eccezione) {
		this(errorID,null,eccezione);
	}

	/** Costruttore codice errore , messaggio e eccezione generante*/
	public EccezioneSbnMessage (int errorID, String message, Exception eccezione) {
		this.ID = errorID;
		this.exception = eccezione;
		this.nota = message;
		//Inizializzo l'errore
		try {
			errore = Decodificatore.getErrore(ID);
			messaggio = errore.getDescrizione();
		} catch (Exception e) {
			log.error("ID errore unbounded : " + ID + "; nota : " + nota);
			errore = null;
			//this.ID = -1;
		};

	}

    /** Costruttore codice errore , messaggio e eccezione generante*/
    public EccezioneSbnMessage (int errorID, String message) {
        this (errorID, message,null);
    }


	/** Costruttore codice errore */
	public EccezioneSbnMessage (int errorID) {
		this (errorID,null,null);
	}
	/**
	 * Restituisce il messaggio legato all'eccezione o la descrizione
	 * dell'Errore legato.
	 * Forse sarà da modificare per sistemare le eccezioni.
	 * @return String
	 */
	public String getMessaggio() {
		if (messaggio != null) {
			return messaggio; // + "  ; referente: "+errore.getNome_referente();
		}
		return nota;
	}

	/**
	 * Restituisce la nota legata all'eccezione
	 */
	public String getNota() {
		return nota;
	}

	/**
	 * metodo utilizzato per modificare il messaggio recuperato sull'albero jndi
	 */
	public void setMessaggio(String nuovoMessaggio) {
		messaggio = nuovoMessaggio;
	}

	/**
	 * metodo utilizzato per modificare il messaggio recuperato sull'albero jndi
	 */
	public void appendMessaggio(String messaggioAggiunto) {
		if (messaggio != null)
			messaggio += " " + messaggioAggiunto;
		else
			messaggio = messaggioAggiunto;
	}

	/**
	 * Returns the iD.
	 * @return int
	 */
	public int getErrorID() {
		return ID;
	}

	/** Restituisce l'errore associato all'eccezione */
	public Errore getError() {
		return errore;
	}

}
