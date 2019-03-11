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
package it.iccu.sbn.ejb.domain.semantica.soggetti;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO.OrigineSoggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.vo.custom.semantica.UserMessage;
import it.iccu.sbn.web.exception.SbnBaseException;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface Soggetti extends EJBObject {

	public List<UserMessage> consumeMessages() throws RemoteException;

	public AnaliticaSoggettoVO importaSoggettoDaIndice(String ticket, DettaglioSoggettoVO soggetto) throws SbnBaseException, RemoteException;

	public AnaliticaSoggettoVO caricaReticoloSoggetto(String ticket, boolean livelloPolo, String cid) throws SbnBaseException, RemoteException;

	public boolean attivaCondivisioneSoggetto(String ticket, DettaglioSoggettoVO soggettoPolo, String idIndice, OrigineSoggetto origineSoggetto) throws SbnBaseException, RemoteException;

	public CreaVariaSoggettoVO modificaSoggetto(String ticket, CreaVariaSoggettoVO soggetto) throws SbnBaseException, RemoteException;

	public AnaliticaSoggettoVO inviaSoggettoInIndice(String ticket, DettaglioSoggettoVO soggetto) throws SbnBaseException, RemoteException;

	public DatiLegameTitoloSoggettoVO invioInIndiceLegamiTitoloSoggetto(String ticket,
			TreeElementViewTitoli reticoloIndice, List<ElementoSinteticaSoggettoVO> soggettiDaInviare)
			throws SbnBaseException, RemoteException;

	public boolean attivaCondivisioneTitoloSoggetto(String ticket, String cidPolo, String bid) throws SbnBaseException, RemoteException;

}
