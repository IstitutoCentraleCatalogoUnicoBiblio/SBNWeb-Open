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
package it.iccu.sbn.ejb.remote;

import it.iccu.sbn.ejb.SbnBusinessSessionRemote;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.OperazioneSuOrdiniMassivaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.AggDispVO;
import it.iccu.sbn.ejb.vo.documentofisico.ScaricoInventarialeVO;
import it.iccu.sbn.ejb.vo.documentofisico.SpostamentoCollocazioniVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.CodaJMSVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RichiestaElaborazioniDifferiteVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.ElaborazioniDifferiteConfig;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.TipoAttivita;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.vo.custom.amministrazione.BatchAttivazioneVO;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ElaborazioniDifferite extends javax.ejb.EJBObject, SbnBusinessSessionRemote {

	public DescrittoreBloccoVO getRichiesteElaborazioniDifferite(String ticket,
			RichiestaElaborazioniDifferiteVO richiestaElaborazioniDifferiteVO,
			int elementiPerBlocco) throws ValidationException, RemoteException;

	public String operazioneSuOrdine(OperazioneSuOrdiniMassivaVO operazioneSuOrdiniVO) throws RemoteException;

	//documentoFisico
	public String spostaCollocazioni(SpostamentoCollocazioniVO spostamentoCollocazioniVO) throws RemoteException;
	public String aggiornaDisponibilita(AggDispVO aggDispVO) throws RemoteException;
	public String scaricoInventariale(ScaricoInventarialeVO scaricoInventarialeVO) throws RemoteException;

	public List<BatchAttivazioneVO> getBatchAttivabili() throws RemoteException;

	public List<CodaJMSVO> getListaCodeBatch() throws RemoteException;

	public String prenotaElaborazioneDifferita(String ticket,
			ParametriRichiestaElaborazioneDifferitaVO richiesta, Map<String, Object> properties)
			throws ValidationException, ApplicationException, RemoteException;

	public String prenotaElaborazioneDifferita(String ticket,
			ParametriRichiestaElaborazioneDifferitaVO richiesta, Map<String, Object> properties,
			Validator<? extends ParametriRichiestaElaborazioneDifferitaVO> validator)
			throws ValidationException, ApplicationException, RemoteException;

	public void eliminaRichiesteElaborazioniDifferite(String ticket,
			String[] idBatch, Boolean deleteOutputs) throws ValidationException, ApplicationException,
			RemoteException;

    public ElaborazioniDifferiteConfig getConfigurazioneElaborazioniDifferite(TipoAttivita tipo) throws RemoteException;

}

