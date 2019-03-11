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
package it.iccu.sbn.web.actions.gestionebibliografica.isbd.akros;

/**
 * Classe EccezioneSbnDiagnostico
 *
 * Gestisce le eccezioni nella generazione
 * del codice Isbd.
 *
 * @author Finsiel
 *
 */
public class EccezioneSbnDiagnostico extends Throwable {

	private static final long serialVersionUID = -5189323795358884945L;

	public EccezioneSbnDiagnostico(int codice, String messaggio) {
		System.out.println("<IsbdException> Codice: " + codice + " Messaggio: " + messaggio + "</IsbdException>");
	}
}
