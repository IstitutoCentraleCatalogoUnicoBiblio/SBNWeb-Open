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
package it.iccu.sbn.ejb.domain.servizi.batch;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AggiornaDirittiUtenteVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ArchiviazioneMovLocVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RinnovoDirittiDiffVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriAllineamentoBibliotecheILLVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchImportaUtentiVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchSollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.batch.RifiutaPrenotazioniScaduteVO;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.vo.xml.binding.esse3.PERSONA;
import it.iccu.sbn.web.exception.SbnBaseException;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface ServiziBMT extends EJBObject {
	public CommandResultVO invoke(CommandInvokeVO command) throws RemoteException, ApplicationException;
	   /**
	    * <!-- begin-xdoclet-definition -->
	    * @throws ResourceNotFoundException
	    * @throws ApplicationException
	    * @throws ValidationException
	    * @generated //TODO: Must provide implementation for bean create stub    */
	public RinnovoDirittiDiffVO gestioneDifferitaRinnovoDiritti(RinnovoDirittiDiffVO rinnDirVO ) throws RemoteException, ApplicationException;

	public ArchiviazioneMovLocVO gestioneDifferitaArchiviazioneMovLoc(ArchiviazioneMovLocVO richiesta, BatchLogWriter blw) throws RemoteException, ApplicationException;

	public AggiornaDirittiUtenteVO gestioneDifferitaAggiornamentoDirittiUtente(AggiornaDirittiUtenteVO aggDirUteVO, BatchLogWriter blog) throws RemoteException, ApplicationException;

	public ElaborazioniDifferiteOutputVo eseguiBatchSolleciti(ParametriBatchSollecitiVO params, BatchLogWriter log)
		throws ApplicationException, RemoteException;

	//almaviva5_20120228 migrazione TO0
	public void fixSegnatureDocNoSbn(String ticket) throws ApplicationException, RemoteException;

	public ElaborazioniDifferiteOutputVo rifiutaPrenotazioniScadute(RifiutaPrenotazioniScaduteVO params, BatchLogWriter log)
			throws SbnBaseException, RemoteException;

	public ElaborazioniDifferiteOutputVo allineaServerILL(ParametriAllineamentoBibliotecheILLVO params, BatchLogWriter log)
			throws SbnBaseException, RemoteException;

	public ElaborazioniDifferiteOutputVo importaUtentiESSE3(ParametriBatchImportaUtentiVO pbiuVO, BatchLogWriter log)
			throws SbnBaseException, RemoteException;

	public Boolean aggiornaUtentiESSE3(String codPolo, String codBib, String ticket, List<PERSONA> persone)
			throws SbnBaseException, RemoteException;

}
