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
package it.iccu.sbn.ejb.domain.servizi.sale;

import it.iccu.sbn.ejb.vo.servizi.sale.PostoSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaPrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.util.ThreeState;
import it.iccu.sbn.vo.custom.servizi.sale.Mediazione;
import it.iccu.sbn.web.exception.SbnBaseException;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface Sale extends EJBObject {

	List<SalaVO> getListaSale(String ticket, RicercaSalaVO ricerca) throws SbnBaseException, RemoteException;

	SalaVO aggiornaSala(String ticket, SalaVO sala) throws SbnBaseException, RemoteException;

	SalaVO getSalaById(String ticket, int idSala) throws SbnBaseException, RemoteException;

	PostoSalaVO aggiornaPosto(String ticket, PostoSalaVO posto) throws SbnBaseException, RemoteException;

	SalaVO cancellaSala(String ticket, SalaVO sala) throws SbnBaseException, RemoteException;

	List<Mediazione> getListaCategorieMediazione(String ticket, String codPolo, String codBib, boolean legataPosto, ThreeState richiedeSupp) throws SbnBaseException, RemoteException;

	List<PrenotazionePostoVO> getListaPrenotazioniPosto(String ticket, RicercaPrenotazionePostoVO ricerca) throws SbnBaseException, RemoteException;

	PrenotazionePostoVO getPrenotazionePostoById(String ticket, int idPrenotazione) throws SbnBaseException, RemoteException;

	PrenotazionePostoVO aggiornaPrenotazionePosto(String ticket, PrenotazionePostoVO prenotazione, boolean aggiornaRichieste) throws SbnBaseException, RemoteException;

	List<PostoSalaVO> getPostiByCatMediazione(String ticket, String codPolo, String codBib, String cd_cat_mediazione, boolean utenteRemoto) throws SbnBaseException, RemoteException;

	PrenotazionePostoVO rifiutaPrenotazionePosto(String ticket, PrenotazionePostoVO pp, boolean inviaMailNotifica) throws SbnBaseException, RemoteException;

	int getNumeroPostiLiberi(String ticket, RicercaSalaVO ricerca) throws SbnBaseException, RemoteException;

}
