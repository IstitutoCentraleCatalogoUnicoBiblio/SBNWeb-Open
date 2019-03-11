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
package it.finsiel.sbn.polo.factoring.transactionmaker;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;

import java.lang.reflect.InvocationTargetException;

import javax.transaction.UserTransaction;

/**
 * Interfaccia Factoring
 * <p>
 * Questa interfaccia, richiamanta dal wrapper, viene implementata da tutti i
 * factoring.
 * </p>
 **/
public interface FactoringAPI {

	public void setTransaction(UserTransaction transaction);

	public void eseguiTransazione() throws EccezioneIccu,
			InfrastructureException, IllegalArgumentException,
			InvocationTargetException, Exception;

	public SBNMarc eseguiRecupero();

	public void proseguiTransazione() throws IllegalArgumentException, InvocationTargetException, Exception;

	public String getXMLResult();

	public SBNMarc getSBNMarcResult();

	/**
	 * Controlla se il factoring è stato eseguito dallo schedulatore
	 */
	public boolean isScheduled();

	/**
	 * Imposta il flag di esecuzione del factoring true: factoring eseguito
	 * dallo scheduler false: factoring eseguito in diretta
	 */
	public void setScheduled(boolean scheduled_factoring);

	/**
	 * Restituisce il numero che specifica l'attività del factoring Deve essere
	 * ridefinito in ogni factoring
	 */
	public String getIdAttivita();

	/**
	 * Restituisce il numero che specifica l'attività del factoring per
	 * monitoraggio Deve essere ridefinito in ogni factoring
	 */
	public String getIdAttivitaSt();

	/**
	 * Restituisce la stringa di dodici caratteri che identifica l'utente.
	 *
	 * @return Stringa
	 */
	public String getCdUtente();

}
