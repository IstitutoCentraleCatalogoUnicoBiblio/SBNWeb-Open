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

/**
 * Eccezione utilizzata per segnalare diagnostici di esecuzione.
 *
 * @author Jenny Barboni
 *
 */
public class EccezioneSbnDiagnostico extends EccezioneIccu{

	private static final long serialVersionUID = 792488821429166372L;


	/** Costruttore codice errore e eccezione generante*/
	public EccezioneSbnDiagnostico (int errorID, Exception eccezione) {
		super (errorID,eccezione);
	}

	/** Costruttore codice errore , messaggio e eccezione generante*/
	public EccezioneSbnDiagnostico (int errorID, String message, Exception eccezione) {
		super (errorID, message, eccezione);
	}

    /** Costruttore codice errore , messaggio e eccezione generante*/
    public EccezioneSbnDiagnostico (int errorID, String message) {
        super (errorID, message);
    }


	/** Costruttore codice errore */
	public EccezioneSbnDiagnostico (int errorID) {
		super (errorID);
	}

	public EccezioneSbnDiagnostico(int errorID, boolean hasLabels, String[] labels) {
		super(errorID, hasLabels, labels);
	}

}
