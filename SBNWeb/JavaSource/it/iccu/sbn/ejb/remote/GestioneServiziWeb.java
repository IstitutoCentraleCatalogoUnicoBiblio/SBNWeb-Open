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
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ServizioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface GestioneServiziWeb extends EJBObject, SbnBusinessSessionRemote {

	public UtenteWeb login(String ticket, String userId, String password, String sip2_ticket, InetAddress inetAddress) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException;

	public UtenteWeb biblioDest() throws RemoteException;

	public DocumentoNonSbnVO ricercaPerSegnatura(String polo, String codBib, String segnatura) throws RemoteException;

	public UtenteWeb tipoDoc() throws RemoteException;

	public boolean inserisciNuovoDoc(DocumentoNonSbnVO doc) throws RemoteException;

	public UtenteWeb modErog() throws RemoteException;

	public UtenteWeb supporto() throws RemoteException;

	public UtenteWeb listaServ(Integer idUte) throws RemoteException;

	public UtenteWeb listaServILL() throws RemoteException;

	public List dirittiUtente(Integer idUtente) throws RemoteException;

	public List dirittiDocumento(DocumentoNonSbnVO datiDocumento) throws RemoteException;
	// almaviva 2009

	public boolean cambioPwd(String ticket, String userId, String pwdNew) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException;
	// almaviva 2009
	public UtenteWeb recuperoPassword(String username) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException;

	// almaviva 2009
	public List<BibliotecaVO> getListaBibAutoregistrazione(String cdpolo) throws DaoManagerException, UtenteNotFoundException, RemoteException;

	// almaviva 2009
	public List listaBiblioAuto(String cdpolo, Integer codUte, List codBib) throws DaoManagerException, UtenteNotFoundException, RemoteException;

	// almaviva 2009
	public List listaBiblioNonIscr(String cdpolo, Integer codUte, List codBib) throws DaoManagerException, UtenteNotFoundException, RemoteException;

	// almaviva 2009
	public List listaBiblioIscritto(String cdpolo, Integer codUte) throws DaoManagerException, UtenteNotFoundException, RemoteException;

	// almaviva 2009
	public boolean inserimentoUtenteWeb(UtenteBibliotecaVO utente) throws RemoteException;

	// almaviva 2009
	public UtenteWeb esistenzaUtenteWeb(String codfiscale, String mail) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException;

	// almaviva 2009
	public List<ServizioVO> getListaServiziAutorizzazione(String ticket, UtenteBibliotecaVO utente) throws RemoteException;

	// almaviva 2009
	public boolean updateUtentiBib(Integer id_utente) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException;

	// almaviva 2009
	public List<BibliotecaVO> controlloBibRicOpac(String codPolo, Integer idUtente, String cdBib)throws DaoManagerException, UtenteNotFoundException, RemoteException;

	// almaviva 2009
	public int getLimMax(String polo)throws DaoManagerException, UtenteNotFoundException, RemoteException;

	// almaviva 2009
	public boolean setRemote(UtenteWeb utente, String polo, String cdBib, InetAddress inetAddress) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException;

	// almaviva 2009
	public int contaRangeSegnature(String polo, String codBib)throws DaoManagerException, UtenteNotFoundException, RemoteException;


}

