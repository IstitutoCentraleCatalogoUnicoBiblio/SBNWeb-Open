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
import it.finsiel.sbn.util.RandomIdGenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.TransactionRolledbackException;

import org.apache.log4j.Logger;

/**
 * Classe EccezioneIccu
 * <p>
 * Rappresenta tutte le eccezioni che si possono generare nel progetto; è un wrapper,
 * ovvero contiene al suo interno un'altra eccezione.
 * Contiene campi e metodi comuni a tutte le eccezioni che la specializzano.
 * Per ora sono tre i campi che devono essere presenti, tutti riguardanti la
 * presentazione dell'eccezione all'utente:<br>
 * - numero rappresentante il codice dell'errore occorso<br>
 * - descrizione dell'errore<br>
 * - riferimento a chi rivolgersi per risolvere l'errore<br>
 * L'altro campo presente è l'eccezione che ha generato inizialmente l'errore.
 * </p>
 *
 * @author
 * @author
 *
 * @version 4-set-2002
 */
public abstract class EccezioneIccu extends TransactionRolledbackException {

	private static final long serialVersionUID = -2346113200093551026L;

	private static final Pattern LABEL_GROUP_REGEX = Pattern.compile("(\\{\\d+\\})");

	private final String incidentId = RandomIdGenerator.getId();

	static Logger log = Logger.getLogger("it.finsiel.sbn.polo.exception.EccezioneIccu");

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

	protected String[] labels;

	/**
	 * Costruttore con il solo nota di descrizione.
	 * Tutti gli altri campi rimangono vuoti.
	 */
	public EccezioneIccu(int ID, String nota) {
		this(ID, nota, null);
	}

	/**
	 * Costruttore con il solo codice errore.
	 */
	public EccezioneIccu(int ID) {
		this(ID, null, null);
	}

	/**
	 * Costruttore con il codice dell'errore e l'eccezione generante.
	 */
	public EccezioneIccu(int ID, Exception exception) {
		this(ID, null, exception);
	}

	/**
	 * Costruttore con il codice dell'errore e l'eccezione generante.
	 */
	public EccezioneIccu(int ID, String nota, Exception exception) {
		super();
		this.ID = ID;
		this.exception = exception;
		this.nota = nota;
		//Inizializzo l'errore
		try {
			errore = Decodificatore.getErrore(ID);

			// Inizio modifica almaviva2 13.07.2010 - Modifica per tornare la nota nel diagnostico a video
			// messaggio = errore.getDescrizione();
			if (nota != null) {
				messaggio = errore.getDescrizione() + " - " + nota;
			} else {
				messaggio = errore.getDescrizione();
			}
			// Fine modifica almaviva2 13.07.2010 - Modifica per tornare la nota nel diagnostico a video

			log.error("Errore: " + ID + " - " + nota);

	} catch (Exception e) {
			log.error("ID errore unbounded : " + ID + "; nota : " + nota);
			errore = null;
		}
	}

	private void decodeLabels() {
		try {
			String tmp = messaggio;
			for (;;) {
				Matcher m = LABEL_GROUP_REGEX.matcher(tmp);
				if (!m.find()) break;

				int idx = Integer.valueOf(m.group().substring(1, m.group().length() - 1));
				tmp = m.replaceFirst( labels[idx] );
			}

			messaggio = tmp;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EccezioneIccu(int errorID, boolean hasLabels, String[] labels) {
		this(errorID, null, null);
		if (hasLabels) {
			this.labels = labels;
			//almaviva5_20120504 evolutive CFI
			decodeLabels();
		}
	}

	/**
	 * Restituisce l'eccezione che è alla base dell'errore.
	 * Si invoca ricorsivamente per arrivare alla prima eccezione.
	 */
	public Exception getRootCasue() {
		if (exception instanceof EccezioneIccu) {
			return ((EccezioneIccu) exception).getRootCasue();
		}
		return exception == null ? this : exception;
	}

	/**
	 * Rappresentazione dell'eccezione sotto forma di stringa;
	 * forse è da modificare per rappresentare al meglio sia il messaggio che
	 * l'eccezione generante.
	 */
	public String toString() {
		if (exception instanceof EccezioneIccu) {
			return ((EccezioneIccu) exception).toString();
		}
		return exception == null ? super.toString() : exception.toString();
	}

	/**
	 * Returns the exception.
	 * @return Exception
	 */
	public Exception getException() {
		return exception;
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

	public String[] getLabels() {
		return labels;
	}

	public String getIncidentId() {
		return incidentId;
	}

}
