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
package it.iccu.sbn.ejb.domain.periodici;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaListaFascicoliVO;
import it.iccu.sbn.servizi.batch.BatchLogWriter;

import java.util.Map;

import javax.ejb.EJBLocalObject;

public interface PeriodiciSBN extends EJBLocalObject {

	public CommandResultVO invoke(CommandInvokeVO command) throws ValidationException, ApplicationException;

	public void inserisciMessaggiFascicolo(ComunicazioneVO comunicazione) throws ValidationException, ApplicationException;

	public void cancellaMessaggiFascicolo(ComunicazioneVO comunicazione) throws ValidationException, ApplicationException;

	public int spostaFascicoliPerFusione(String bidOld, String bidNew, String uteVar) throws ValidationException, ApplicationException;

	public String getAnnateFascicoliTitolo(String bid) throws ValidationException, ApplicationException;

	public boolean getListaFascicoliPerStampa(
			StampaListaFascicoliVO listaFascicoli, String ticket,
			BatchLogWriter blw) throws ResourceNotFoundException,
			ApplicationException, ValidationException, DataException;

	//almaviva5_20170721 #5612
	public Map<String, Integer> countEsemplariBiblioteche(String bid) throws ValidationException, ApplicationException;

}
